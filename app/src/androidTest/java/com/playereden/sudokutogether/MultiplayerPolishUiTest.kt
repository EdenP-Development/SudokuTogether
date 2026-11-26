package com.playereden.sudokutogether

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

internal fun polishState(unread: Int = 0): GameUiState {
    val clues = "530070000600195000098000060800060003400803001700020006060000280000419005000080079".map { it.digitToInt() }
    val puzzle = Puzzle("polish", clues, "EASY")
    return GameUiState(page = Page.PLAY, role = HOST, name = "Joseph", roomCode = "123456", address = "192.168.1.20:41421",
        connected = true, unreadChat = unread, status = RoomStatus(guestActive = true),
        snapshot = Snapshot("polish", "epoch", puzzle, clues.map { Cell(it) }, hostName = "Joseph", guestName = "Taylor"))
}
class MultiplayerPolishUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun bothCoopPlayersSeeTheSharedBoardAndKeepAuthoredNumbers() {
        val base = polishState()
        val host = Appearance(board = "linen", ink = "clay", font = "rounded")
        val guest = Appearance(board = "midnight", ink = "lagoon", font = "pencil")
        val initial = requireNotNull(base.snapshot)
        val cells = initial.cells.toMutableList().apply {
            this[2] = Cell(4, owner = HOST)
            this[3] = Cell(6, owner = GUEST)
            this[6] = Cell(notes = 511, owner = HOST)
            this[7] = Cell(notes = 511, owner = GUEST)
        }
        val shared = initial.copy(cells = cells, hostAppearance = host, guestAppearance = guest)
        val state = mutableStateOf(base.copy(snapshot = shared, appearance = host))
        compose.setContent { SudokuTogetherTheme { PlayScreen(state.value, polishActions(), {}, {}, {}, {}) } }

        fun checkBoard(theme: String) {
            val pixels = compose.onNodeWithTag("cell_5").captureToImage().toPixelMap()
            assertEquals(boardFillFor(theme), pixels[pixels.width / 2, pixels.height / 2])
        }
        fun digitLayout(cell: Int, number: String): TextLayoutResult {
            val layout = mutableListOf<TextLayoutResult>()
            compose.onNode(hasText(number) and hasAnyAncestor(hasTestTag("cell_$cell")), useUnmergedTree = true)
                .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layout) }
            return layout.single()
        }
        fun checkFont(cell: Int, digit: String, font: String) {
            val layout = digitLayout(cell, digit)
            assertEquals(fontFor(font), layout.layoutInput.style.fontFamily)
            assertFalse("Cell $cell should fit $digit in $font", layout.hasVisualOverflow)
        }
        fun checkInk(cell: Int, ink: String) {
            val pixels = compose.onNodeWithTag("cell_$cell").captureToImage().toPixelMap()
            val expected = inkFor(ink, darkBoard = true)
            assertTrue("Cell $cell should use the $ink ink for a dark board", (0 until pixels.height).any { y ->
                (0 until pixels.width).any { x ->
                    val color = pixels[x, y]
                    abs(color.red - expected.red) < .05f && abs(color.green - expected.green) < .05f &&
                        abs(color.blue - expected.blue) < .05f
                }
            })
        }

        compose.onNodeWithText("Shared board · Midnight").assertIsDisplayed()
        checkBoard("midnight")
        checkFont(2, "4", "rounded")
        checkFont(3, "6", "pencil")
        checkInk(2, "clay")
        checkInk(3, "lagoon")
        for (digit in '1'..'9') {
            checkFont(6, digit.toString(), "rounded")
            checkFont(7, digit.toString(), "pencil")
        }
        compose.runOnIdle { state.value = state.value.copy(role = GUEST, appearance = guest, connected = false) }
        checkBoard("midnight")
        checkFont(2, "4", "rounded")
        checkFont(3, "6", "pencil")

        compose.runOnIdle { state.value = state.value.copy(uniformNumbers = true) }
        checkFont(2, "4", "clear")
        checkFont(3, "6", "clear")
        checkFont(6, "4", "clear")
        checkFont(7, "6", "clear")
        checkInk(2, "forest")
        checkInk(3, "forest")
        compose.runOnIdle { state.value = base.copy(snapshot = shared.copy(mode = VERSUS), appearance = host) }
        compose.onNodeWithText("Shared board · Midnight").assertDoesNotExist()
        checkBoard("linen")
    }

    @Test fun everyBundledNumberFontFitsAllNineNotesOnTheCompactCoachSize() {
        val initial = polishState()
        val original = requireNotNull(initial.snapshot)
        val withNotes = original.copy(cells = original.cells.mapIndexed { index, cell ->
            if (index == 2) Cell(notes = 511) else cell
        })
        val font = mutableStateOf("clear")
        val boardSize = mutableStateOf(280.dp)
        compose.setContent {
            SudokuTogetherTheme {
                SudokuBoard(
                    snapshot = withNotes, selected = 2, partner = -1, size = boardSize.value,
                    select = {}, boardTheme = "classic", localStyle = Appearance(font = font.value),
                    solo = true, uniformNumbers = false
                )
            }
        }
        val overflows = mutableListOf<String>()
        for (size in listOf(280.dp, 360.dp)) {
            compose.runOnIdle { boardSize.value = size }
            for (id in listOf("clear", "serif", "mono", "rounded", "pencil")) {
                compose.runOnIdle { font.value = id }
                for (digit in 1..9) {
                    val layout = mutableListOf<TextLayoutResult>()
                    compose.onNode(
                        hasText(digit.toString()) and hasAnyAncestor(hasTestTag("cell_2")),
                        useUnmergedTree = true
                    ).performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layout) }
                    if (layout.single().hasVisualOverflow) overflows.add("$size/$id/$digit")
                }
            }
        }
        assertTrue("Dense pencil candidate overflow: " + overflows.joinToString(), overflows.isEmpty())
    }

    @Test fun completedCoopGuestHasOneReconnectAction() {
        val base = polishState()
        val state = mutableStateOf(base.copy(role = GUEST, connected = false,
            snapshot = requireNotNull(base.snapshot).copy(completed = true)))
        compose.setContent { SudokuTogetherTheme { PlayScreen(state.value, polishActions(), {}, {}, {}, {}) } }
        compose.onNodeWithText("Reconnect").assertExists().assertIsEnabled()
        compose.onAllNodesWithText("Reconnect").assertCountEquals(1)
        compose.runOnIdle { state.value = state.value.copy(busy = true) }
        compose.onNodeWithText("Reconnect").assertIsNotEnabled()
        compose.runOnIdle { state.value = base.copy(snapshot = requireNotNull(base.snapshot).copy(completed = true), busy = true) }
        compose.onNodeWithTag("finish_next_puzzle").assertIsNotEnabled()
    }

    @Test fun unreadCountDoesNotSplitTheRoomTitleAtNormalPhoneWidth() {
        compose.setContent { SudokuTogetherTheme { CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 1.15f)) {
            Box(Modifier.width(360.dp).fillMaxHeight()) { PlayScreen(polishState(999), polishActions(), {}, {}, {}, {}) }
        } } }
        val result = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText("Co-op").performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(result) }
        assertEquals("The title must not wrap when unread chat grows", 1, result.single().lineCount)
        compose.onNodeWithText("Game details").assertIsDisplayed()
        compose.onNodeWithText("Copy code").assertDoesNotExist()
    }
    @Test fun largeTextKeepsHeaderActionsAndRoomCodeReachable() {
        compose.setContent { SudokuTogetherTheme { CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
            Box(Modifier.width(320.dp).fillMaxHeight()) { PlayScreen(polishState(1), polishActions(), {}, {}, {}, {}) }
        } } }
        compose.onNodeWithText("Co-op").assertIsDisplayed()
        compose.onNodeWithText("Game details").performClick()
        compose.onNodeWithText("Copy code").performScrollTo().assertIsDisplayed()
        compose.onNodeWithContentDescription("Room code 123456").assertExists()
    }
    @Test fun approvedRepliesAndEmotesAreReachableWithoutTextEntry() {
        val sent = mutableListOf<String>()
        val state = polishState().copy(chat = listOf(ChatLine("a", HOST, "Yes", 1), ChatLine("b", GUEST, GridMatesEmote.HAPPY.wireValue, 2)))
        compose.setContent { SudokuTogetherTheme { RoomChatSheet(state, polishActions(send = sent::add), {}) } }
        compose.onNodeWithText("You").assertIsDisplayed()
        compose.onNodeWithText("Taylor").assertIsDisplayed()
        compose.onNodeWithTag("chat_emote_happy").assertExists()
        compose.onNodeWithContentDescription(GridMatesEmote.HAPPY.label).assertExists()
        compose.onNodeWithTag("chat_input").assertDoesNotExist()
        for ((category, group) in listOf("Responses" to QuickChatGroup.RESPONSE, "Teamwork" to QuickChatGroup.TEAMWORK, "Friendly" to QuickChatGroup.FRIENDLY)) {
            compose.onNodeWithText(category).performScrollTo().performClick()
            for (reply in QuickChatPhrase.entries.filter { it.group == group })
                compose.onNodeWithTag("quick_chat_" + reply.id).performScrollTo().assertIsDisplayed().performClick()
        }
        compose.onNodeWithTag("quick_chat_emotes").performScrollTo().performClick()
        for (emote in GridMatesEmote.entries) {
            compose.onNodeWithTag("emote_" + emote.id).performScrollTo().assertIsDisplayed().performClick()
        }
        compose.runOnIdle { assertEquals(QuickChat.phrases + GridMatesEmote.entries.map { it.wireValue }, sent) }
    }

    @Test fun largeTextCommunicationKeepsResponsesAndReactionsReachable() {
        val sent = mutableListOf<String>()
        compose.setContent { SudokuTogetherTheme { CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
            RoomChatSheet(polishState(), polishActions(send = sent::add), {})
        } } }
        compose.onNodeWithTag("quick_chat_yes").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithTag("quick_chat_no").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithTag("quick_chat_emotes").performScrollTo().performClick()
        compose.onNodeWithTag("emote_ready").performScrollTo().assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals(listOf("Yes", "No", GridMatesEmote.READY.wireValue), sent) }
    }

    @Test fun partnerPingControlRequiresSelectionAndActiveCoopPartner() {
        val base = polishState()
        val state = mutableStateOf(base)
        var sent = 0
        compose.setContent { SudokuTogetherTheme { PlayScreen(state.value, polishActions().copy(partnerPing = { sent++ }), {}, {}, {}, {}) } }
        compose.onNodeWithTag("partner_ping_send").assertIsNotEnabled()
        compose.runOnIdle { state.value = base.copy(selected = 2) }
        compose.onNodeWithTag("partner_ping_send").performScrollTo().assertIsEnabled().performClick()
        compose.runOnIdle { assertEquals(1, sent); state.value = state.value.copy(connected = false) }
        compose.onNodeWithTag("partner_ping_send").assertIsNotEnabled()
        compose.runOnIdle { state.value = base.copy(snapshot = requireNotNull(base.snapshot).copy(mode = VERSUS)) }
        compose.onNodeWithTag("partner_ping_send").assertDoesNotExist()
    }

    @Test fun expiredPartnerMarkerDoesNotAppearAndActiveMarkerIsAccessible() {
        val base = polishState().copy(selected = 2)
        val snapshot = requireNotNull(base.snapshot)
        val marker = PartnerPing(snapshot.sessionId, snapshot.epoch, 4)
        val state = mutableStateOf(base.copy(partnerPing = marker, partnerPingUntil = 0L))
        compose.setContent { SudokuTogetherTheme { PlayScreen(state.value, polishActions(), {}, {}, {}, {}) } }
        compose.onNodeWithText("Partner: Look here! R1 C5").assertDoesNotExist()
        compose.runOnIdle { state.value = state.value.copy(partnerPingUntil = android.os.SystemClock.elapsedRealtime() + PartnerPing.DISPLAY_MS) }
        compose.onNodeWithText("Partner: Look here! R1 C5").performScrollTo().assertIsDisplayed()
        compose.onNodeWithContentDescription("Row 1 column 5, clue 7, partner attention marker").assertExists()
        compose.runOnIdle { assertEquals(2, state.value.selected) }
        compose.mainClock.advanceTimeBy(PartnerPing.DISPLAY_MS + 100L)
        compose.onNodeWithText("Partner: Look here! R1 C5").assertDoesNotExist()
    }

    @Test fun communicationVisibilityHidesIncomingHistoryButKeepsSendingAvailable() {
        val sent = mutableListOf<String>()
        val history = listOf(
            ChatLine("mine", HOST, "Thank you!", 1),
            ChatLine("partner", GUEST, GridMatesEmote.HAPPY.wireValue, 2)
        )
        val state = mutableStateOf(polishState().copy(chat = history))
        val actions = polishActions(send = sent::add).copy(muteCommunication = { muted ->
            state.value = state.value.copy(communicationMuted = muted, unreadChat = 0, partnerPing = null, partnerPingUntil = 0L)
        })
        compose.setContent { SudokuTogetherTheme { RoomChatSheet(state.value, actions, {}) } }
        compose.onNodeWithTag("communication_visibility").assertIsOn().performClick()
        compose.onNodeWithTag("communication_visibility").assertIsOff()
        compose.onNodeWithTag("chat_emote_happy").assertDoesNotExist()
        compose.onNodeWithText("Thank you!").assertExists()
        compose.onNodeWithTag("quick_chat_yes").performScrollTo().assertIsEnabled().performClick()
        compose.runOnIdle { assertEquals(listOf("Yes"), sent); assertEquals(history, state.value.chat); assertTrue(state.value.connected) }
        compose.onNodeWithTag("communication_visibility").performScrollTo().performClick()
        compose.onNodeWithTag("chat_emote_happy").assertExists()
        compose.onNodeWithTag("communication_visibility").assertIsOn()
    }
}
