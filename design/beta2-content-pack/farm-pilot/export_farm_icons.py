#!/usr/bin/env python3
"""Regenerate the four GridMates Farm PNGs from the approved 2x2 source sheet.

Requires Pillow==12.3.0. The images remain art candidates until the owner
approves their in-game display. This script never touches game progression.
"""
from pathlib import Path
from PIL import Image
import hashlib, json

folder = Path(__file__).resolve().parent
manifest = json.loads((folder/"manifest.json").read_text())
source = folder/"farm_mixed_source_sheet.png"
assert hashlib.sha256(source.read_bytes()).hexdigest() == manifest["source"]["sha256"]
image = Image.open(source).convert("RGBA")
assert image.size == (1254,1254)
for index,item in enumerate(manifest["items"]):
    c,r = index%2,index//2
    icon = image.crop((c*627,r*627,(c+1)*627,(r+1)*627))
    alpha = icon.getchannel("A").point(lambda n: 0 if n<5 else n)
    icon.putalpha(alpha)
    bounds = alpha.getbbox()
    if bounds is None: raise ValueError("Blank icon: "+item["id"])
    icon = icon.crop(bounds)
    size = tuple(max(1,round(x*432/max(icon.size))) for x in icon.size)
    small = icon.resize(size,Image.Resampling.LANCZOS)
    dest = Image.new("RGBA",(512,512),(0,0,0,0))
    dest.alpha_composite(small,((512-size[0])//2,(512-size[1])//2))
    out=folder/(item["id"]+".png")
    dest.save(out,optimize=True)
    if hashlib.sha256(out.read_bytes()).hexdigest()!=item["sha256"]:
        raise ValueError("Crop/encoding changed for "+item["id"])
    print("VERIFIED",item["id"],item["sha256"])
