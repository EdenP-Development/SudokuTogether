# STAB-07 — release evidence and recovery

Active branch `feat/beta3-foundation`. As of 2026-11-27, latest verified code 8e2dcee: quality 45962075841 and unsigned candidate 45962075789 passed. Earlier save instrumentation 45957495632 passed; integrated reward/save 45962562834 at 8242358 in progress, SDK/KVM passed, instrumentation not yet accepted.

## Release stop conditions and evidence
| Gate | Evidence / required next action |
| --- | --- |
| Exact baseline | Itch Beta 2 owner-reported public, package com.playereden.gridmates.sudoku.beta, code 31, version 1.0.0-beta.2, certificate 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d. Exact accepted APK hash/archive and downloaded bytes not independently verified. |
| Custody | Shared DISASTER_RECOVERY.md PORT-09: independent recoverable signer copies unconfirmed, not lost. Owner privately verifies two encrypted copies and original certificate; no secrets in public docs. |
| Automated | Current core/debug/release checks passed; inspect integrated emulator reports and fix failures. Add targeted accessibility runtime evidence. |
| Actual upgrade | Follow STAB-05-APK-UPGRADE-GATE.md; exact prior APK to higher version with original signer on noncritical backed-up device, verify progress and restart. Never uninstall or clear data as workaround. |
| Physical | Named handset/tablet/Fire OS and exact candidate; TalkBack, 200% text, portrait/landscape, offline gameplay, two-device LAN/reconnect/rematch, emotes and long names. |
| Rights | Six soundtrack generation entitlement records and master archive custody, artwork/fonts/dependency rights and notices; technical hashes do not prove rights. See BETA3_AUDIO_PRODUCTION_GATE.md and shared ASSET_LICENSING.md. |
| Privacy and storefront | Permanent privacy URL, support route, audience/communication disclosures, actual Fire/tablet screenshots, current policy review and owner-approved store metadata. Amazon rehearsal only; Google Play separate long-term release strategy. |
| Authorization | Explicit approval for exact artifact/channel; archive signed APK and receipt, verify distributed bytes, update registry and handoff. No approval inferred from green CI. |

## Standards cross-reference
EdenP-Development/Eden-Development-Standards main: RELEASE_CHECKLIST.md PORT-10, SAVE_PRESERVATION.md, DISASTER_RECOVERY.md PORT-09, ASSET_LICENSING.md inspected. Existing requirements retained, no new standards or shared registry changes. See project roadmap and STAB-04/05/06 handoffs for detailed source history. Next STAB-07.03 reconcile evidence pointers and pending runtime results; then STAB-08 feature freeze. No production code or release changes.

## STAB-07.03 — synchronization closeout
Latest live run inspection: 45962562834 instrumentation still running; report upload pending. Both 8e2dcee build gates passed. Updated central recovery and progress pointers; repository synchronization addressed, with runtime and release gates explicitly carried forward. Next STAB-08 scope freeze; do not freeze out defect repairs or mark release accepted. No shared registry or release change.
