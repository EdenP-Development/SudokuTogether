# GridMates: Sudoku — Beta 2 isolated owner-phone preview

**Source:** Production main checkpoint after PR #71, at `90382797543faaa9c2711b543d67a7cf04b48603`, with branch-only changes to Android debug `applicationIdSuffix` and launcher label.

**Purpose:** Let the owner visually check the full current Beta 2 gameplay/UI while the original production signing key is unavailable. This is an isolated QA preview, **not** the public Beta 2 release or a Beta 1 update.

- Android package: `com.playereden.sudokutogether.qa.beta2owner`.
- Launcher: **Sudoku B2 Review**.
- Android version code: `31`; debug version name `1.0.0-beta.2-qa`.
- Test-signed by a temporary GitHub-hosted runner Android debug certificate; it may change between workflow runs. **Do not promise future install-over updates** to this preview.
- Stores its own data separately. Does **not** carry over existing Beta 1, old QA, or older preview progress automatically.
- Does not read, clear, uninstall or update the installed `com.playereden.sudokutogether` package.
- Never distribute this QA artifact as the public itch.io APK. The GitHub repository is public, and the artifact is visible to repository readers.

## Scope

The application and core game sources match the cited `main` checkpoint except for the two branch-only app-identity/launcher changes. No puzzle rules, Collection requirements, saved data migration, LAN transport, or ownership logic were modified.

The isolated test workflow compiles, runs JVM/Android unit and lint checks, assembles a debug APK, verifies Android package/version/signature/hash, then performs an Android 36 emulator install/launch smoke test. It records its source commit and checksum in the artifact receipt. An emulator success does **not** certify real-phone layout, production APK installer signature, or live player data preservation.

## On-phone review

Install the exact artifact alongside existing games, **without uninstalling** them. Start a fresh Solo game, test pencil notes, Coach, Daily, Profile and Collections; open About and Progress backup controls, then examine readable text/spacing on your own phone. Optional screenshots should not reveal real names, IP addresses or room codes. This preview cannot be used to test the real production Beta 1 → Beta 2 install-over.

The actual public Beta 2 remains blocked on recovering the original production signing credentials and passing an on-device production-signed upgrade from the real published Beta 1 APK.
