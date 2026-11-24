package com.playereden.sudokutogether

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Run on the SAME Android test installation after installing the upgraded
 * Beta 2 candidate over a Beta 1 debug APK with the exact same ephemeral
 * test signer and application ID. This does not prove production-key custody.
 */
@RunWith(AndroidJUnit4::class)
class Beta2UpgradeVerifierTest {
    @Test fun beta1ProfileAndSettingsSurviveRealApkInstallOverAndMigrateWithoutLoss() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.filesDir, "local-player.json")
        val oldBytes = file.readBytes()
        assertFalse("The original Beta 1 file should not contain Beta 2-owned IDs",
            oldBytes.toString(Charsets.UTF_8).contains("ownedCosmeticIds"))
        assertFalse("The original Beta 1 file should not contain Beta 2-earned medals",
            oldBytes.toString(Charsets.UTF_8).contains("earnedAchievementIds"))
        // Five clean, 90-second Easy solves in the legacy seeder qualify for
        // exactly these four medals; the other 27 must remain locked.
        val expectedMedals = setOf("first-grid", "steady-hand", "clean-5", "quick-thinker")
        val original = Json.decodeFromString<LocalPlayerData>(oldBytes.toString(Charsets.UTF_8))
        val store = LocalPlayerStore(file)
        val upgraded = store.current()

        assertEquals("Existing player identity", original.playerId, upgraded.playerId)
        assertEquals("All five completed matches", 5, upgraded.records.size)
        assertEquals(original.records, upgraded.records)
        assertEquals("Unfinished Solo remains resumable", "upgrade-solo-progress",
            upgraded.activeSolo?.id)
        assertEquals("Pencil marks survive", 4, upgraded.activeSolo!!.cells[0].notes)
        assertEquals("Hints survive", 1, upgraded.activeSolo!!.hintsUsed)
        assertEquals("Undo history survives", 1, upgraded.activeSolo!!.history.size)
        assertEquals("Daily board remains saved", "2026-11-17",
            upgraded.dailyProgress.single().date)
        assertEquals(original.dailyProgress, upgraded.dailyProgress)
        assertEquals("Daily board still matches deterministic challenge",
            DailyChallenge.puzzle("2026-11-17"), upgraded.dailyProgress.single().solo.puzzle)
        assertTrue("Rose earned in Beta 1 stays unlocked",
            Cosmetics.allowed("rose", upgraded.records, upgraded.ownedCosmeticIds))
        assertTrue("Robin earned in Beta 1 stays unlocked",
            Cosmetics.allowed("woodland_robin", upgraded.records, upgraded.ownedCosmeticIds))
        assertTrue("Rare items are not auto-granted",
            !Cosmetics.allowed("ocean_narwhal", upgraded.records, upgraded.ownedCosmeticIds))

        assertEquals("All 31 achievement IDs are available", 31, Achievements.knownIds.size)
        assertEquals("Legacy history earns exactly the qualifying medals on load",
            expectedMedals, upgraded.earnedAchievementIds.toSet())
        assertEquals(expectedMedals, Achievements.progress(upgraded.records, upgraded.earnedAchievementIds)
            .filter { it.unlocked }.map { it.id }.toSet())

        val prefs = context.getSharedPreferences("settings", 0)
        assertEquals("Upgrade Player", prefs.getString("name", null))
        assertEquals("rose", prefs.getString("cosmeticInk", null))
        assertEquals("woodland_robin", prefs.getString("cosmeticPortrait", null))
        assertEquals("HARD", prefs.getString("difficulty", null))
        assertTrue(prefs.getBoolean("autoCleanNotes", false))
        assertEquals("Migration must not overwrite the old profile just on read",
            oldBytes.toList(), file.readBytes().toList())

        // Ordinary verified save writes Beta 2 entitlement IDs, never resetting
        // the old Solo/Daily state, settings or match history.
        store.update { it }
        val reopened = LocalPlayerStore(file).current()
        assertEquals(original.playerId, reopened.playerId)
        assertEquals(original.activeSolo, reopened.activeSolo)
        assertEquals(original.dailyProgress, reopened.dailyProgress)
        assertEquals(original.records, reopened.records)
        assertTrue(reopened.ownedCosmeticIds.containsAll(listOf("rose", "woodland_robin")))
        assertTrue(file.readText().contains("ownedCosmeticIds"))
        assertTrue(file.readText().contains("earnedAchievementIds"))
        assertEquals("Retroactive medals persist across a verified save and reload",
            expectedMedals, reopened.earnedAchievementIds.toSet())

        // Future retention cannot retroactively revoke the earned styles.
        val trimmed = store.update { it.copy(records = it.records.take(2)) }
        assertTrue(Cosmetics.allowed("rose", trimmed.records, trimmed.ownedCosmeticIds))
        assertTrue(Cosmetics.allowed("woodland_robin", trimmed.records, trimmed.ownedCosmeticIds))
        assertEquals(original.activeSolo, trimmed.activeSolo)
        assertEquals(original.dailyProgress, trimmed.dailyProgress)
        assertEquals(original.playerId, trimmed.playerId)
        val afterTrim = LocalPlayerStore(file).current()
        assertEquals("Earned medals survive pruning and reopening",
            expectedMedals, afterTrim.earnedAchievementIds.toSet())
        assertEquals("No unrelated medal is granted",
            expectedMedals, Achievements.progress(afterTrim.records, afterTrim.earnedAchievementIds)
                .filter { it.unlocked }.map { it.id }.toSet())
        assertEquals(original.activeSolo, afterTrim.activeSolo)
        assertEquals(original.dailyProgress, afterTrim.dailyProgress)
    }
}
