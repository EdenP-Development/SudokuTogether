# Permanent itch.io beta transition — conditional owner approval

Original public signer remains unresolved. Owner approved preparing the fallback if recovery fails. Proposed permanent package com.playereden.gridmates.sudoku.beta, code31, version 1.0.0-beta.2. Legacy package, QA and reviewer defaults remain unchanged. No new key or publication yet. Keep legacy game installed.

## Staging
Use -PgridmatesPermanentIdentityStaging=true for an unsigned release-only build. It refuses the legacy signing environment and disables debug variants. This verifies package/resource/manifest construction; it is not installable or a signed acceptance build. Existing approved-art production flag remains false until formal promotion. CI checks unsigned APK metadata, package, version, min/target SDK, permissions and absence of debug flag; no distribution APK uploaded.

## Owner-controlled custody
Follow https://github.com/PlayerEden/Eden-Development-Standards/blob/main/SIGNING_KEY_MANAGEMENT.md.
Before generating any production key, choose a simple owner-controlled private backup and independent recovery copy. Generate on a trusted owner machine using Java keytool (PKCS12, RSA 3072 or 4096, alias gridmates-sudoku, validity at least 10000 days), with an interactive password prompt; store password separately in password manager. Never paste password or key in chat. Verify both copies open and match the public SHA-256 fingerprint. Set dedicated repository Actions secrets privately only after backup verification; proposed GRIDMATES_SUDOKU_BETA_KEYSTORE_BASE64 and GRIDMATES_SUDOKU_BETA_KEYSTORE_PASSWORD. Do not reuse or overwrite SUDOKU_KEYSTORE_BASE64/PASSWORD for legacy. Record public fingerprint in reviewed policy and central registry before adding any signed build workflow. No temporary CI-generated production key. No key material in artifacts.

## Final acceptance
Recover original key if feasible; otherwise explicitly confirm new package and verified custody. Promote owner-approved art, add new fail-closed signing workflow pinned to verified new certificate, inspect signed APK and test launch, restore actual exported progress, and simulate future same-identity install-over on noncritical data. Existing public Beta1 to new package uses portable migration, NOT install-over. Automated 750-game and owner physical QA->reviewer tests passed; wife's individual test deferred. Actual production acceptance remains open. Publish clear one-time migration instructions and retain old download until approved. No automatic publication.

## Channel boundary
Itch.io is a public development beta, not the final Google Play product. Maintain beta-to-beta updates and save continuity where practical. Reserve com.playereden.gridmates.sudoku as a proposed future Play identity; no guarantee of beta progress migration into Play. Optional early-supporter rewards are a distant design idea, not a Beta 2 deliverable. No enterprise vault product is required. Existing QA identities stay unchanged.
