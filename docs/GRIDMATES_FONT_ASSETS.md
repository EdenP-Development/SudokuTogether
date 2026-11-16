# GridMates number fonts

GridMates ships these static TrueType faces in the Android APK. The app selects them using the existing cosmetic IDs. Existing saves, unlock requirements and multiplayer appearance messages use the same IDs.

| Cosmetic ID | Display name | Typeface | Android resource | SHA-256 |
| --- | --- | --- | --- | --- |
| `clear` | Clear | Noto Sans Regular 2.004 | `noto_sans_regular.ttf` | `89c3c497f618fdaa0b2d1e98fef93582f28c71debd2c4a8cdf41f190ced2909d` |
| `clear` | Clear clue weight | Noto Sans Bold 2.004 | `noto_sans_bold.ttf` | `e83493c945848ecd4a9ad0f6d19164541a0d3e23a9c952304a00a46e00272ac5` |
| `serif` | Bookish | Noto Serif Regular 2.004 | `noto_serif_regular.ttf` | `9d7583b7dc9e812afd32a14280c5cac3160012efe50c8d08938f4fea266ff67f` |
| `mono` | Terminal | Liberation Mono Regular 2.1.5 | `liberation_mono_regular.ttf` | `5883330d94debd992952cd8f0571b225f478c2d797d3f36c7521b0a5c9bde0f2` |
| `rounded` | Rounded | Nunito Bold 3.602 | `nunito_bold.ttf` | `9a580061c1993a96965a02550ba408092145b700ae8af4fa192e5c9ce6f28b76` |
| `pencil` | Pencil | Patrick Hand Regular 1.003 | `patrick_hand_regular.ttf` | `0f173b3e6cb6d1af25babf7f0057c5ac4ee11f9992b0469bb817e967ef4ad0fc` |

Clear, Bookish, and Terminal use the original bytes in Debian 13. [fonts-noto-core 20201225-2](https://packages.debian.org/trixie/fonts-noto-core) · [fonts-liberation 1:2.1.5-3](https://packages.debian.org/trixie/fonts-liberation)

Rounded is a static `wght=700` instance of the upstream Nunito variable font generated with FontTools. Pencil uses the original Patrick Hand Regular bytes from Google Fonts. [Nunito source](https://github.com/googlefonts/nunito/tree/8c6a9bb9732545b9ed53f29ec5e1ab0ff53c4e6f/fonts/variable) · [Patrick Hand source](https://github.com/google/fonts/tree/cc39c4c2c304bc368c9b7c311ea2bcc6013299fe/ofl/patrickhand)

All faces include digits 0–9. The APK includes the copyright notices and SIL Open Font License 1.1. [Font notices](../app/src/main/assets/font_licenses.txt)

Classic number display uses Clear for entries and notes and the default entry ink. Notes on light boards use the muted note color. Fixed clues use the bundled bold weight, and Collection shows the effective style alongside the equipped board. Turning Classic number display off shows the saved font and ink again. Chat uses the equipped typeface, with Android fallback for characters outside its coverage.

Opening a number font in Collection shows all nine sample digits in that face. The equipped style preview shows a mix of fixed clues and saved number styling.
