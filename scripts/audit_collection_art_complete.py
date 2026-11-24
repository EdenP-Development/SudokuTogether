#!/usr/bin/env python3
"""Source and perceptual framing audit for every current portrait, candidate and hero.
This is a reproducible source-render comparison, NOT owner or Android visual approval.
"""
import hashlib
import io
import json
import re
from pathlib import Path

import cairosvg
from PIL import Image, ImageDraw, ImageFont, ImageChops

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "app/src/main/res/drawable-nodpi"
VECTORS = ROOT / "app/src/main/res/drawable"
OUT = ROOT / "art-audit"
OUT.mkdir(exist_ok=True)
FAMILIES = {"woodland": 17, "ocean": 11, "garden": 9, "farm": 6}
SCALES = {"woodland_chipmunk": 1.30, "woodland_beaver": 1.28,
          "woodland_woodpecker": 1.26, "woodland_skunk": 1.28}
MANIFEST = ROOT / "design/collection-art/illustrated-five-manifest.json"
GREEN = (226, 235, 223, 255)
BG = (249, 247, 239, 255)
INK = (25, 67, 51, 255)
FONT = ImageFont.load_default(size=17)
SMALL = ImageFont.load_default(size=13)

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def render_vector(path):
    return Image.open(io.BytesIO(cairosvg.svg2png(url=str(path), output_width=512, output_height=512))).convert("RGBA")

def source_stats(im, scale):
    alpha = im.getchannel("A")
    solid = alpha.point(lambda p: 255 if p >= 128 else 0)
    box = solid.getbbox()
    assert box, "empty or nearly transparent sprite"
    w, h = im.size
    # Approximate actual Compose circle and 8% padding, at high res.
    size = 256
    fitted = im.copy()
    fitted.thumbnail((round(size * .84 * scale),) * 2, Image.Resampling.LANCZOS)
    canvas = Image.new("RGBA", (size, size))
    canvas.alpha_composite(fitted, ((size-fitted.width)//2, (size-fitted.height)//2))
    mask = Image.new("L", (size, size))
    ImageDraw.Draw(mask).ellipse((0, 0, size-1, size-1), fill=255)
    a = canvas.getchannel("A")
    clipped = ImageChops.multiply(a, mask)
    total = sum(a.getdata())
    lost = 1 - sum(clipped.getdata()) / max(1, total)
    return {"source_size": [w,h], "solid_bbox": list(box),
            "visible_extent": round(max((box[2]-box[0])/w,(box[3]-box[1])/h),3),
            "source_edge_contact": any((box[0]==0,box[1]==0,box[2]==w,box[3]==h)),
            "circle_alpha_loss_pct": round(lost*100,3),
            "display_scale": scale}

def badge(im, dp, scale):
    n = dp*2
    result = Image.new("RGBA", (n,n), BG)
    mask = Image.new("L", (n,n))
    ImageDraw.Draw(mask).ellipse((0,0,n-1,n-1), fill=255)
    result.paste(GREEN, (0,0,n,n), mask)
    src = im.copy()
    src.thumbnail((round(n*.84*scale),)*2, Image.Resampling.LANCZOS)
    layer = Image.new("RGBA", (n,n))
    layer.alpha_composite(src, ((n-src.width)//2,(n-src.height)//2))
    layer.putalpha(ImageChops.multiply(layer.getchannel("A"),mask))
    result.alpha_composite(layer)
    return result

def sheet(name, entries):
    # Each row contains the exact same source at 48/62/96dp, rendered at 2x.
    im = Image.new("RGBA",(740,66+len(entries)*210),BG)
    d = ImageDraw.Draw(im)
    d.text((15,12), name, font=FONT, fill=INK)
    for x,label in [(210,"48dp"),(340,"62dp"),(515,"96dp")]:
        d.text((x,38),label,font=SMALL,fill=INK)
    for i,(label,src,scale) in enumerate(entries):
        y=66+i*210
        d.text((12,y+86),label.replace("_","\n",1),font=SMALL,fill=INK)
        for x,dp in [(190,48),(310,62),(460,96)]:
            tile=badge(src,dp,scale)
            im.alpha_composite(tile,(x,y+(192-tile.height)//2))
        d.line((10,y+205,725,y+205),fill=(210,216,203,255))
    im.convert("RGB").save(OUT/(name+".png"))

def main():
    records=[]
    originals={}
    hashes=set()
    code=(ROOT/"app/src/main/java/com/playereden/sudokutogether/AnimalPortraits.kt").read_text()
    for family,count in FAMILIES.items():
        paths=sorted(RES.glob(family+"_*.png"))
        assert len(paths)==count,(family,len(paths),count)
        entries=[]
        for path in paths:
            id=path.stem
            assert re.search(r'"'+re.escape(id)+r'"\s*->\s*R\.drawable\.'+re.escape(id)+r'\b',code), id
            with Image.open(path) as opened:
                assert opened.mode=="RGBA", (id,opened.mode)
                im=opened.copy()
            digest=sha(path)
            assert digest not in hashes,("duplicate source",id)
            hashes.add(digest)
            originals[id]=im
            scale=SCALES.get(id,1)
            stats=source_stats(im,scale)
            records.append({"id":id,"kind":"original","sha256":digest,**stats})
            entries.append((id,im,scale))
        sheet(family+"-all-originals",entries)
    assert len(records)==43
    compare=[]
    manifest = json.loads(MANIFEST.read_text())
    assert len(manifest["records"]) == 5
    for entry in manifest["records"]:
        id = entry["id"]
        path = ROOT / entry["path"]
        assert sha(path) == entry["sha256"] and path.stat().st_size == entry["size_bytes"], id
        assert re.search(r'"'+re.escape(id)+r'"\s*->\s*R\.drawable\.'+re.escape(path.stem)+r'\b',code), id
        with Image.open(path) as opened:
            assert opened.mode == "RGBA" and opened.size == (512,512), id
            im = opened.copy()
        assert all(im.getpixel(p)[3] == 0 for p in ((0,0),(0,511),(511,0),(511,511))), id
        stats=source_stats(im,1)
        assert not stats["source_edge_contact"] and stats["circle_alpha_loss_pct"] < 1, (id,stats)
        assert entry["sha256"] not in hashes, ("duplicate candidate",id)
        hashes.add(entry["sha256"])
        records.append({"id":id,"kind":"candidate","sha256":entry["sha256"],**stats})
        anchor="woodland_bear" if id.startswith("woodland") else "garden_honeybee"
        compare.extend([(anchor,originals[anchor],1),(id+" original",originals[id],SCALES.get(id,1)),(id+" illustrated",im,1)])
        im.save(OUT/(id+"-candidate.png"))
    sheet("targeted-before-after",compare)
    # Native vector source inventory and every packaged hero background.
    heroes=[]
    for p in sorted(RES.glob("gm_*")):
        if p.suffix not in (".png",".webp") or p.name.startswith("gm_portrait_"): continue
        with Image.open(p) as src:
            heroes.append({"path":p.name,"size":list(src.size),"sha256":sha(p)})
    insignias=[{"path":p.name,"sha256":sha(p)} for p in sorted(VECTORS.glob("gm_insignia_candidate_*.xml"))]
    assert len(insignias)==6
    (OUT/"audit.json").write_text(json.dumps({"portrait_records":records,"hero_sources":heroes,
      "insignia_sources":insignias,"notes":"Source checks and simulated framing only; inspect actual Android and human style review separately."},indent=2)+"\n")
    print("PASS: 43 original portraits, 5 illustrated raster comparisons, six Basic vector sources and",len(heroes),"hero resources inventoried.")
    for row in records:
        if row["circle_alpha_loss_pct"]>1 or row["source_edge_contact"]:
            print("REVIEW framing",row["id"],row["kind"],row["circle_alpha_loss_pct"],row["source_edge_contact"])

if __name__=="__main__":
    main()
