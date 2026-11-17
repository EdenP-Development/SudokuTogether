package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playereden.sudokutogether.core.GUEST
import com.playereden.sudokutogether.core.HOST
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** One-device regression for a real hosted round and pure guest event identity.
 *  Does NOT claim an independent two-phone LAN connection was exercised.
 */
@RunWith(AndroidJUnit4::class)
class MultiplayerRoundInputModeTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Before fun ready() {
        compose.waitUntil(30000) {
            compose.onAllNodes(hasTestTag("nav_play") and isEnabled()).fetchSemanticsNodes().isNotEmpty()
        }
        EmulatorSystemDialogs.dismiss()
        compose.waitUntil(10000) {
            if (!compose.activity.hasWindowFocus()) EmulatorSystemDialogs.dismiss()
            compose.activity.hasWindowFocus()
        }
    }

    @Test fun roundIdentityWorksForBothPlayerRolesAndDoesNotResetDuringOrdinaryEdits() {
        val initial = polishState()
        val snapshot = requireNotNull(initial.snapshot)
        for (role in listOf(HOST, GUEST)) {
            val state = initial.copy(role = role, notes = true, selected = 6)
            assertFalse("An edit to this round is not a fresh board",
                state.isNewMultiplayerRound(snapshot.copy(revision = snapshot.revision + 1)))
            assertFalse("Reconnecting to the same room/epoch preserves its identity",
                state.isNewMultiplayerRound(snapshot.copy(guestName = "Changed")))
            assertTrue("A rematch has a new epoch",
                state.isNewMultiplayerRound(snapshot.copy(epoch = "new-round")))
            assertTrue("Joining another room also resets the temporary input mode",
                state.isNewMultiplayerRound(snapshot.copy(sessionId = "different-room")))
            assertTrue(state.copy(snapshot = null).isNewMultiplayerRound(snapshot))
        }
    }

    @Test fun confirmedNextCoopBoardStartsWithNumberEntryAfterNotes() {
        compose.onNodeWithText("Co-op").performScrollTo().performClick()
        compose.onNodeWithText("Start a game").performScrollTo().performClick()
        if (compose.onAllNodesWithText("Start new").fetchSemanticsNodes().isNotEmpty())
            compose.onNodeWithText("Start new").performClick()

        val model = ViewModelProvider(compose.activity)[GameViewModel::class.java]
        compose.waitUntil(60000) {
            val state = model.state.value
            state.role == HOST && state.snapshot != null && !state.busy
        }
        val before = requireNotNull(model.state.value.snapshot)
        compose.onNodeWithText("Notes").assertIsDisplayed().performClick()
        compose.runOnIdle { assertTrue(model.state.value.notes) }

        // A confirmation is still required for replacing the unfinished shared board.
        compose.onNodeWithText("Game details").performClick()
        compose.onNodeWithText("New puzzle").performClick()
        compose.onNodeWithText("Start a new puzzle?").assertIsDisplayed()
        compose.onNodeWithText("Keep playing").performClick()
        compose.runOnIdle {
            assertEquals(before.epoch, model.state.value.snapshot?.epoch)
            assertTrue(model.state.value.notes)
        }

        compose.onNodeWithText("Game details").performClick()
        compose.onNodeWithText("New puzzle").performClick()
        compose.onNodeWithText("Start new").performClick()
        compose.waitUntil(90000) {
            val current = model.state.value
            current.snapshot?.epoch != before.epoch &&
                current.snapshot?.sessionId == before.sessionId && !current.busy
        }
        compose.runOnIdle {
            val current = model.state.value
            assertFalse("Next Co-op round must start in number-entry mode", current.notes)
            assertEquals(-1, current.selected)
            assertEquals(-1, current.partnerCell)
            assertNull(current.pendingId)
            assertTrue(current.snapshot!!.cells.all { it.notes == 0 })
        }
        compose.onNodeWithText("Notes").assertIsDisplayed()
        compose.onNodeWithText("Notes on").assertDoesNotExist()
    }
}
