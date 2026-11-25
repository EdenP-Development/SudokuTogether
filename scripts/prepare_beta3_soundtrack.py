"""Verify the immutable review archive and extract only the six known audio files."""
import hashlib
import io
import json
import pathlib
import zipfile

root = pathlib.Path("design/audio/transfer")
parts = sorted(root.glob("part-*.bin"))
assert [p.name for p in parts] == [f"part-{i:02}.bin" for i in range(1,21)]
data = b"".join(p.read_bytes() for p in parts)
assert len(data) == 15369442
assert hashlib.sha256(data).hexdigest() == "273f9d88e0a795cad51af8b58a82ee9181a3acf792e9c0dbe42e7f2648080aa2"
expected = [
    ("01_The Final Piece.m4a", "cdcb72a7d7c8da92ed6b1133e98c18721a4a3172f604763ec4d87c9caf73976d"),
    ("02_Library Puzzle.m4a", "214c6a8457b0feafac97ef13a1806207bd7e39ebe7899fd0097d55f06bf3e250"),
    ("03_Warm Puzzle Time.m4a", "49a3e6d6ff2273c1d7d4cff4a158f377d52c18f59fc5bc9a1e5980f3652bed2f"),
    ("04_Sunny Sunday Puzzle.m4a", "84bfc70376be6bb22d6e26f07799ced8074eb9e6eb89ed8d575ef098fe06112a"),
    ("05_Thoughtful Discovery.m4a", "5a0a7c3bb125b8472a5127b26d61c8506ce674ed8e9f5cb2e34c9d42dfaed6b6"),
    ("06_Evening Puzzle.m4a", "1e439245b42cc0796c594e87491623c5be2e3e186dd809fa5a0f27aee68010d6"),
]
out = pathlib.Path("build/beta3-audio-assets/soundtrack")
out.mkdir(parents=True, exist_ok=True)
with zipfile.ZipFile(io.BytesIO(data)) as z:
    assert sorted(z.namelist()) == sorted(["manifest.json"] + [n for n, _ in expected])
    manifest = json.loads(z.read("manifest.json"))
    assert len(manifest) == len(expected)
    for i, (name, digest) in enumerate(expected, 1):
        payload = z.read(name)
        assert hashlib.sha256(payload).hexdigest() == digest
        entry = manifest[i-1]
        assert entry["id"] == i and entry["file"] == name
        assert entry["sha256"] == digest and entry["bytes"] == len(payload)
        (out / f"track_{i:02}.m4a").write_bytes(payload)
print("Verified all six soundtrack assets.")
