package com.playereden.sudokutogether

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.lifecycle.ViewModelProvider
import com.playereden.sudokutogether.core.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Logical continuity after first launch of a test-signed Beta3 APK. */
@RunWith(AndroidJUnit4::class)
class Beta2ToBeta3VerifierTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun verifyMigratedProgressAndName() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.playereden.sudokutogether.qa", context.packageName)
        assertEquals(32, context.packageManager.getPackageInfo(context.packageName, 0).versionCode)
        val old = Json.decodeFromString<LocalPlayerData>(File(context.filesDir, "beta2-upgrade-reference.json").readText())
        lateinit var model: GameViewModel
        compose.runOnUiThread { model = ViewModelProvider(compose.activity)[GameViewModel::class.java] }
        compose.waitUntil(30000) { !model.state.value.busy }
        val state = model.state.value
        assertFalse(state.localSaveBlocked)
        val current = state.profile
        assertEquals(old.playerId, current.playerId)
        assertEquals(old.records, current.records)
        assertEquals(old.activeSolo, current.activeSolo)
        assertEquals(old.dailyProgress, current.dailyProgress)
        assertTrue(current.ownedCosmeticIds.containsAll(old.ownedCosmeticIds))
        assertTrue(current.earnedAchievementIds.containsAll(old.earnedAchievementIds))
        val normalized = NameCollection.normalizedProfileName("Upgrade Player", current)
        assertEquals(normalized, state.name)
        assertEquals(normalized, context.getSharedPreferences("settings", 0).getString("name", null))
        assertNotNull(NameCollection.parseCanonical(state.name))
        assertTrue(state.autoCleanNotes)
        assertEquals("HARD", state.selectedDifficulty)
        assertEquals("rose", state.appearance.ink)
        assertEquals("woodland_robin", state.appearance.portrait)
        compose.runOnUiThread { model.solo(true) }
        compose.waitUntil(30000) { model.state.value.page == Page.PLAY && model.state.value.canEdit }
        assertEquals(old.activeSolo!!.id, model.state.value.snapshot!!.sessionId)
        assertEquals(1, model.state.value.snapshot!!.cells[0].value)
        assertEquals(4, model.state.value.snapshot!!.cells[1].notes)
        assertEquals(1, model.state.value.snapshot!!.undoTargets[SOLO])
        compose.runOnUiThread { model.home() }
        compose.waitUntil(15000) { model.state.value.page == Page.HOME && !model.state.value.busy }
    }
}
