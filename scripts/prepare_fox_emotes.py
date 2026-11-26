"""Verify the approved Fox V1 transport; optionally stage runtime resources."""
import hashlib
import io
import json
from pathlib import Path
import zipfile
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
PARTS = ROOT / "design/emotes/fox/v1/transfer"
EXPECTED = "c10b3fcae7e09d09a4c500d74a479c1842a19c54de6f9a7b1907714aa38bdf5b"
IDS = "happy approve disapprove appreciation thinking celebration oops greeting confused applause break ready".split()
parts = sorted(PARTS.glob("part-*.bin"))
assert [p.name for p in parts] == [f"part-{i:02}.bin" for i in range(1, 14)]
raw = b"".join(p.read_bytes() for p in parts)
assert len(raw) == 3754384 and hashlib.sha256(raw).hexdigest() == EXPECTED
with zipfile.ZipFile(io.BytesIO(raw)) as z:
    expected = {"README.txt", "manifest.json", "preview.jpg"}
    expected.update(f"masters/{i}.png" for i in IDS)
    expected.update(f"runtime/gm_emote_fox_{i}.webp" for i in IDS)
    assert len(z.infolist()) == len(expected) and set(z.namelist()) == expected
    assert sum(e.file_size for e in z.infolist()) <= 4_000_000
    assert z.testzip() is None
    items = json.loads(z.read("manifest.json"))["items"]
    assert len(items) == 12 and {x["id"] for x in items} == set(IDS)
    for item in items:
        i = item["id"]
        for key, size, fmt, name in [
            ("master", 512, "PNG", f"masters/{i}.png"),
            ("runtime", 192, "WEBP", f"runtime/gm_emote_fox_{i}.webp"),
        ]:
            assert item[key] == name
            data = z.read(name)
            digest = hashlib.sha256(data).hexdigest()
            if key == "master":
                assert digest == item["master_sha256"]
            with Image.open(io.BytesIO(data)) as im:
                assert im.format == fmt and im.size == (size, size) and im.mode == "RGBA"
                alpha = im.getchannel("A")
                bbox = alpha.getbbox()
                assert bbox and bbox[0] > 0 and bbox[1] > 0 and bbox[2] < size and bbox[3] < size
                assert alpha.getextrema() == (0, 255)
            print(name, digest)
print("Fox V1 archive and all 24 images verified.")
