#!/usr/bin/env python3
"""Side-by-side actual Garden icons with four proposed mixed-theme icons.

This builds a review image from existing Android drawable PNGs, not a mockup.
The 72/48/30 labels indicate logical icon sizes, magnified 2x for inspection.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[3]
ART = ROOT / "app/src/main/res/drawable-nodpi"
PILOT = Path(__file__).resolve().parent
OLD = [("Honeybee", ART/"garden_honeybee.png"),
       ("Ladybug", ART/"garden_ladybug.png"),
       ("Butterfly", ART/"garden_butterfly.png"),
       ("Dragonfly", ART/"garden_dragonfly.png")]
NEW = [("Sunflower", PILOT/"garden_sunflower.png"),
       ("Strawberry", PILOT/"garden_strawberry.png"),
       ("Watering Can", PILOT/"garden_watering_can.png"),
       ("Potted Tulip", PILOT/"garden_potted_tulip.png")]
W, H = 1120, 660
paper=(244,243,235,255)
ink=(33,81,63,255)
sheet=Image.new("RGBA",(W,H),paper)
d=ImageDraw.Draw(sheet)
def label(x,y,t,color=ink):
    d.text((x,y),t,fill=color)
label(22,12,"GRIDMATES  /  GARDEN ICON ART CHECK")
label(22,38,"Actual existing icons (top) vs proposed plants and objects (bottom). All images use native PNGs.")
def tile(index,row,name,path):
    if not path.exists(): raise FileNotFoundError(path)
    png=Image.open(path).convert("RGBA")
    if png.size[0]<64 or png.size[1]<64: raise ValueError("Tiny asset "+str(path))
    x=20+index*275
    y=68+row*286
    d.rounded_rectangle((x,y,x+255,y+272),radius=22,fill=(255,254,251,255),outline=(213,220,209,255),width=2)
    d.text((x+14,y+12),name,fill=ink)
    for j,(dp,pixels) in enumerate([(72,144),(48,96),(30,60)]):
        cx=x+[78,178,224][j]
        cy=y+[134,130,135][j]
        box=(cx-pixels//2,cy-pixels//2,cx+pixels//2,cy+pixels//2)
        d.ellipse(box,fill=(224,237,225,255))
        icon=png.copy()
        icon.thumbnail((pixels-7,pixels-7),Image.Resampling.LANCZOS)
        sheet.alpha_composite(icon,(cx-icon.width//2,cy-icon.height//2))
        d.text((cx-15,y+232),f"{dp}dp",fill=(91,106,97,255))
for i,(name,path) in enumerate(OLD): tile(i,0,name,path)
for i,(name,path) in enumerate(NEW): tile(i,1,name,path)
dest=PILOT/"garden_style_comparison.png"
sheet.convert("RGB").save(dest,optimize=True)
print("Wrote actual Garden portrait comparison:",dest.relative_to(ROOT))
