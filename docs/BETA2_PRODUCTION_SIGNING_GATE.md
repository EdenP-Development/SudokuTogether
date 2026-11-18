# GridMates Beta 2 — original production signer gate

**Prepared November 17, 2026 · preflight is a release gate, not publication**

## Grounded original release baseline

- Public Android package: `com.playereden.sudokutogether`
- Original Beta 1 code: `30`; next Beta 2 code: `31`
- Original signing certificate **SHA-256 (public fingerprint only):** `d362dab018a255ccb313582df1e3cf836246e7da1778a198eb1da03a7cfe7cd1`
- Recorded Beta 1 public APK **SHA-256:** `6d7dda34129fc4a6c74ab8146dcb36128967340558db50d9f7bac58cbe41c8c3`
- Beta 2 app source version: `1.0.0-beta.2` from `app/build.gradle.kts`. The exact SHA must match the GitHub run receipt before an APK is considered reproducible.
- Those public identifiers come from the [Beta 1 release records](GRIDMATES_V1_BETA_RELEASE.md) and [.github/workflows/android.yml](../.github/workflows/android.yml). They **do not** imply the current keystore secrets exist.

## What's new and how to read results

[Production key preflight and candidate workflow](../.github/workflows/gridmates-production-signing-preflight.yml) runs one time when merged into `main` and is then available manually in Actions. This workflow **does not** print or upload private signing-key material.

**Job 1 — original signing check:**
- Reads `SUDOKU_KEYSTORE_BASE64` and `SUDOKU_KEYSTORE_PASSWORD` only on a GitHub-hosted runner, without checking out or executing repository code.
- Refuses to proceed if secrets are unavailable, cannot be decoded or the keystore doesn't contain the expected `sudokutogether` alias/certificate.
- Computes the public certificate SHA-256 and compares with the pinned Beta 1 fingerprint; logs only a PASS or generic BLOCKED message.
- Deletes the temporary private keystore at job exit.

**Job 2 — private release candidate only after Job 1 passes:**
- Reconstructs the same original signer privately and verifies its public certificate again.
- Builds and tests the exact `main` release source using core JVM tests, app unit tests, lint and Gradle `assembleRelease`.
- Verifies the actual APK independently with `apksigner` and `aapt`: original public package, versionCode 31, versionName `1.0.0-beta.2`, original public signing certificate and minimum Android API 26 verification.
- Generates an SHA-256 file and non-secret JSON receipt pinning source commit, package, signer, version and APK checksum.
- Uploads only the **private APK candidate plus receipt/checksum** to GitHub Actions with 14-day artifact retention. **Never** uploads private keys, decoded keystore, signing properties or user progress. Signing files are cleaned up afterward.
- It does **not** visit or publish to itch.io, alter a user's installed game, upload a Google Play AAB, or demonstrate that the *actual itch-downloaded Beta 1 APK* can be updated.

## Decisions after the workflow

| Outcome | Correct next action |
|---|---|
| **Preflight blocked — secrets absent** | Recover the **original** Beta 1 signing keystore from its secure private backup and configure the existing GitHub repository secret names. Never generate a substitute key to update existing app installations. Do not ask the owner to paste signing secrets in chat. |
| **Preflight blocked — fingerprint mismatch** | Stop. Investigate the original production signing identity privately. No replacement certificate will update the installed public Beta 1. |
| **Preflight PASS, candidate build failure** | Investigate build/test/Gradle or APK verification logs; **no artifact should be considered approved** until all candidate checks pass. |
| **Candidate PASS** | Record source SHA, signer and checksum; download the actual old itch.io APK, compare hash and certificate, then use a non-critical test installation with an exported progress backup to rehearse the **production-signed** Android install-over without uninstalling. Only afterward proceed to final real-phone UX, approved privacy/support information and itch.io publication. |

## Safe handling and non-negotiable release boundary

Existing family progress is the priority. Never uninstall or clear the wife's Beta 1 app to bypass an Android signature or version mismatch. A manually exported progress backup is prudent before a compatible update. The previous [source-backed debug-certificate upgrade rehearsal](BETA2_ACTUAL_APK_INSTALL_OVER_QA.md) passed, but production signing remains a separate acceptance test.

The [Beta 2 itch listing draft](GRIDMATES_ITCH_BETA2_LISTING_DRAFT.md) is unpublished. Renewed two-physical-phone LAN testing remains deferred at the owner's request. Source signing-gate work does not add cosmetics, change the puzzle engine or alter current saved-data format.
