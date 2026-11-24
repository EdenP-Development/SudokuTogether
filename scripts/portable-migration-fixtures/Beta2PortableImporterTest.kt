package com.playereden.sudokutogether

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class Beta2PortableImporterTest {
    @Test fun restoreActualBeta1ExportIntoSeparateInstallation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.playereden.sudokutogether.qa.beta2integratedrc3", context.packageName)
        val file = File(context.filesDir, "local-player.json")
        val store = LocalPlayerStore(file)
        val destinationId = store.current().playerId
        val sourceId = File(context.filesDir, "migration-source-id.txt").readText()
        assertNotEquals(sourceId, destinationId)
        val prefs = context.getSharedPreferences("settings", 0)
        val backup = ProgressBackup.decode(File(context.filesDir, "migration-export.json").inputStream())
        val incoming = backup.progress.forInstallation(destinationId)
        assertEquals(750, incoming.records.size)
        assertEquals("upgrade-solo-progress", incoming.activeSolo?.id)
        assertEquals(4, incoming.activeSolo!!.cells[0].notes)
        assertEquals(1, incoming.activeSolo!!.history.size)
        assertEquals(1, incoming.activeSolo!!.hintsUsed)
        assertEquals("2026-11-17", incoming.dailyProgress.single().date)
        val restored = ProgressBackupStore(file, prefs).restore(store, backup)
        assertEquals(destinationId, restored.playerId)
        assertEquals(incoming.records, restored.records)
        assertEquals(incoming.activeSolo, restored.activeSolo)
        assertEquals(incoming.dailyProgress, restored.dailyProgress)
        assertTrue(Cosmetics.allowed("rose", restored.records, restored.ownedCosmeticIds))
        assertTrue(Cosmetics.allowed("woodland_robin", restored.records, restored.ownedCosmeticIds))
        assertEquals("Upgrade Player", prefs.getString("name", null))
        assertEquals("rose", prefs.getString("cosmeticInk", null))
        assertEquals("woodland_robin", prefs.getString("cosmeticPortrait", null))
        assertEquals("HARD", prefs.getString("difficulty", null))
        assertTrue(prefs.getBoolean("autoCleanNotes", false))
        assertEquals(47, Achievements.knownIds.size)
        val expected = Achievements.progress(incoming.records, emptyList()).filter { it.unlocked }.map { it.id }.toSet()
        assertTrue("solo-750" in expected)
        assertFalse("solo-1000" in expected)
        assertEquals(expected, restored.earnedAchievementIds.toSet())
        val reopened = LocalPlayerStore(file).current()
        assertEquals(restored, reopened)
        assertEquals(expected, Achievements.progress(reopened.records, reopened.earnedAchievementIds).filter { it.unlocked }.map { it.id }.toSet())
    }
}
