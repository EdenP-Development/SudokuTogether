# AUD-03 — verified soundtrack inventory

Six owner-selected masters, provided in conversation after owner reported Suno Pro. Warm Puzzle Time is designated main theme. Original files inspected with ffprobe and SHA-256, all Opus stereo 48 kHz in M4A. Originals retained unchanged; AAC-LC 128 kbps working copies produced locally targeting -20 LUFS / -2 dBTP via FFmpeg loudnorm. Actual listening and achieved loudness/device transitions remain to be verified. No master or derivative binary transferred to repository yet.

| ID | Title | Seconds | Source bytes | SHA-256 (master) |
|---|---|---:|---:|---|
| 01 | The Final Piece | 149.96 | 2723484 | ffb35c674cd6f2bd38603294cd20d77cef3161d33e266d35bf3c7522d8c23db2 |
| 02 | Library Puzzle | 133.88 | 2414243 | ebffd6016b5be0b40063b3ad31ae950927d4abb856d0fadefdda4cb2b38a02c7 |
| 03 | Warm Puzzle Time | 174.00 | 3165121 | 0336580f79d63fb7ca6d4494c4b19cc08787df3463652bbf54fd233f9a17f0d3 |
| 04 | Sunny Sunday Puzzle | 147.60 | 2770147 | 3e0862426d73b13475bad40e82ea999f88436e680ecb127af93d10262194740d |
| 05 | Thoughtful Discovery | 154.92 | 2850390 | 056b7cd8359822df46cd8843f7726ebc0ddf7e54849de1ec46926ced575ab350 |
| 06 | Evening Puzzle | 184.40 | 3381371 | 3d48152be6be86426174eb8a19ff9198653a5a8e81cf813dcbc3a6e0d1e14c97 |

Runtime 944.76s. AAC working copy checksums:
01 cdcb72a7d7c8da92ed6b1133e98c18721a4a3172f604763ec4d87c9caf73976d
02 214c6a8457b0feafac97ef13a1806207bd7e39ebe7899fd0097d55f06bf3e250
03 49a3e6d6ff2273c1d7d4cff4a158f377d52c18f59fc5bc9a1e5980f3652bed2f
04 84bfc70376be6bb22d6e26f07799ced8074eb9e6eb89ed8d575ef098fe06112a
05 5a0a7c3bb125b8472a5127b26d61c8506ce674ed8e9f5cb2e34c9d42dfaed6b6
06 1e439245b42cc0796c594e87491623c5be2e3e186dd809fa5a0f27aee68010d6

AUD-04 interim soundtrack review extends proven MediaPlayer pilot to multiple local files, main theme first then shuffled other tracks, cyclic rotation, skip, independent volume, foreground/focus stop. Debug only; no permanent preferences or bundled assets yet. Review before replacing pilot with production playback. No public APK changes.
