#!/usr/bin/env python3
"""Source-backed visual comparison, canonical GridMates vs five Beta2 pilots."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
ROOT=Path(__file__).resolve().parents[3]
refs=[
("Fox · existing","app/src/main/res/drawable-nodpi/woodland_fox.png"),
("Owl · existing","app/src/main/res/drawable-nodpi/woodland_owl.png"),
("Octopus · existing","app/src/main/res/drawable-nodpi/ocean_octopus.png"),
("Honeybee · existing","app/src/main/res/drawable-nodpi/garden_honeybee.png"),
]
pilot=[
("Cow · pilot","design/beta2-content-pack/pilot-art/farm_cow_pilot.png"),
("Chicken · pilot","design/beta2-content-pack/pilot-art/farm_chicken_pilot.png"),
("Raccoon · pilot","design/beta2-content-pack/pilot-art/woodland_raccoon_pilot.png"),
("Sea Turtle · pilot","design/beta2-content-pack/pilot-art/ocean_sea_turtle_pilot.png"),
("Firefly · pilot","design/beta2-content-pack/pilot-art/garden_firefly_pilot.png"),
]
width=1120
sheet=Image.new("RGB",(width,530),(246,246,238))
draw=ImageDraw.Draw(sheet)
draw.text((22,14),"CURRENT GRIDMATES COLLECTION PORTRAITS — canonical style",fill=(27,73,58))
draw.text((22,235),"BETA 2 CANDIDATES — same display scale and circle",fill=(27,73,58))
def tile(label,path,x,y):
    image=Image.open(ROOT/path).convert("RGBA")
    if image.getbbox() is None:raise RuntimeError(f"Blank art {path}")
    # Use the same 160px circular presentation for both actual collection
    # resources and pilots; white/green rings show alpha-edge issues.
    draw.ellipse((x+18,y+4,x+178,y+164),fill=(224,236,226))
    image.thumbnail((151,151),Image.Resampling.LANCZOS)
    px=x+98-image.width//2;py=y+84-image.height//2
    sheet.paste(image,(px,py),image)
    draw.text((x+8,y+177),label,fill=(27,73,58))
for index,(label,path) in enumerate(refs):
    tile(label,path,18+index*210,42)
for index,(label,path) in enumerate(pilot):
    tile(label,path,18+index*210,265)
out=ROOT/"design/beta2-content-pack/pilot-art/canonical_style_comparison.png"
out.parent.mkdir(parents=True,exist_ok=True)
sheet.save(out,optimize=True)
print("Wrote",out.relative_to(ROOT),"with 4 actual Collection refs + 5 pilots")
