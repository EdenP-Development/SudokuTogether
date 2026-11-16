package com.playereden.sudokutogether

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playereden.sudokutogether.core.Cell
import com.playereden.sudokutogether.core.Puzzle
import com.playereden.sudokutogether.core.SOLO
import com.playereden.sudokutogether.core.VERSUS

data class GameActions(
    val name: (String) -> Unit, val host: (Boolean) -> Unit, val showJoin: (Boolean) -> Unit,
    val home: () -> Unit, val join: (Endpoint, String) -> Unit, val select: (Int) -> Unit,
    val number: (Int) -> Unit, val toggleNotes: () -> Unit, val erase: () -> Unit,
    val undo: () -> Unit, val replacePartner: () -> Unit, val newPuzzle: () -> Unit,
    val solo: (Boolean) -> Unit, val profile: () -> Unit, val hostRace: (Boolean) -> Unit,
    val sendChat: (String) -> Unit, val chatVisible: (Boolean) -> Unit, val cosmetic: (String, String) -> Unit,
    val uniformNumbers: (Boolean) -> Unit,
    val touchFeedback: (Boolean) -> Unit,
    val difficulty: (String) -> Unit,
    val hint: () -> Unit,
    val dismissHint: () -> Unit,
    val autoCleanNotes: (Boolean) -> Unit,
    val daily: () -> Unit,
    val openDaily: (String) -> Unit,
    val checkWork: () -> Unit = {},
    val requireRoomCode: (Boolean) -> Unit = {},
    val rematch: (String) -> Unit = {}
)

@Composable fun SudokuApp(
    state: GameUiState, actions: GameActions, share: (() -> Unit)?,
    settingsLocked: Boolean = false, settingsExtra: @Composable () -> Unit = {}
) {
    var confirmation by remember { mutableStateOf<String?>(null) }
    var helpOpen by rememberSaveable { mutableStateOf(false) }
    var practiceOpen by rememberSaveable { mutableStateOf(false) }
    var journalTab by rememberSaveable { mutableStateOf(JournalTab.RECORDS) }
    var profileSection by rememberSaveable { mutableStateOf(ProfileSection.OVERVIEW) }
    fun journal(tab: JournalTab, section: ProfileSection = ProfileSection.OVERVIEW) {
        journalTab = tab
        profileSection = section
        if (state.page != Page.PROFILE) actions.profile()
    }
    val routedActions = actions.copy(profile = {
        journal(JournalTab.RECORDS, if (state.dailyDate != null) ProfileSection.HISTORY else ProfileSection.STATISTICS)
    })
    BackHandler(state.page != Page.HOME) {
        when {
            state.page == Page.PROFILE && journalTab == JournalTab.SETTINGS && settingsLocked -> Unit
            state.page == Page.PROFILE && journalTab == JournalTab.RECORDS && profileSection != ProfileSection.OVERVIEW -> {
                profileSection = ProfileSection.OVERVIEW
            }
            state.page == Page.PLAY && state.role != SOLO -> confirmation = "leave"
            else -> actions.home()
        }
    }
    Surface(Modifier.fillMaxSize(), color = Cream) {
        Column(Modifier.fillMaxSize().safeDrawingPadding()) {
            Box(Modifier.weight(1f).fillMaxWidth()) {
                when (state.page) {
                    Page.HOME -> HomeScreen(state, actions, { if (state.resumeHost || state.saveIssue != null) confirmation = "fresh" else actions.host(false) },
                        { if (state.resumeSolo && state.profile.activeSolo?.completed == false) confirmation = "freshSolo" else actions.solo(false) },
                        { if (state.resumeRace) confirmation = "freshRace" else actions.hostRace(false) }, { helpOpen = true }, { journal(JournalTab.RECORDS) })
                    Page.JOIN -> JoinScreen(state, actions)
                    Page.PLAY -> PlayScreen(state, routedActions, { if (state.role == SOLO) actions.home() else confirmation = "leave" }, { confirmation = "new" }, { confirmation = "partner" }, { helpOpen = true })
                    Page.PROFILE -> JournalScreen(state, actions, journalTab, profileSection,
                        openSection = { profileSection = it }, collection = { journal(JournalTab.COLLECTION) },
                        help = { helpOpen = true }, share = share, settingsLocked = settingsLocked, settingsExtra = settingsExtra)
                }
            }
            if (state.page == Page.HOME || state.page == Page.PROFILE)
                GameNavigation(state.page, journalTab, actions.home, { journal(it) }, enabled = !state.busy && !settingsLocked)
        }
    }
    if (helpOpen) SudokuHelpDialog(
        close = { helpOpen = false },
        openPractice = if (state.page == Page.HOME && !state.busy) {
            { helpOpen = false; practiceOpen = true }
        } else null
    )
    if (practiceOpen) CoachPracticeDialog(state.appearance, state.uniformNumbers) { practiceOpen = false }
    confirmation?.let { kind ->
        val title = when (kind) { "leave" -> "Leave the room?"; "partner" -> "Replace your partner?"; else -> "Start a new puzzle?" }
        val body = when (kind) {
            "leave" -> "Confirmed progress is saved. You can resume or rejoin this puzzle later."
            "partner" -> "Your current partner will be disconnected. A partner can join again using the room code. The puzzle stays as it is."
            "freshSolo" -> "Starting again replaces your unfinished solo puzzle. Your previous completed records are kept."
            "freshRace" -> "Starting a fresh race replaces the saved head-to-head room and its unfinished match."
            else -> if (state.role == SOLO) "This replaces your current solo puzzle. Completed records are kept." else if (state.snapshot?.mode == VERSUS) "This starts another race on both phones and resets their entries." else "This replaces your saved shared puzzle, including its entries and notes."
        }
        AlertDialog(onDismissRequest = { confirmation = null }, title = { Text(title) }, text = { Text(body) },
            confirmButton = { TextButton(onClick = {
                confirmation = null
                when (kind) { "leave" -> actions.home(); "partner" -> actions.replacePartner(); "fresh" -> actions.host(false); "freshSolo" -> actions.solo(false); "freshRace" -> actions.hostRace(false); else -> actions.newPuzzle() }
            }) { Text(if (kind == "leave") "Leave room" else if (kind == "partner") "Replace partner" else "Start new") } },
            dismissButton = { TextButton(onClick = { confirmation = null }) { Text("Keep playing") } })
    }
}

@Composable private fun SudokuHelpDialog(close: () -> Unit, openPractice: (() -> Unit)?) {
    var more by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { if (more) more = false else close() },
        title = {
            Text(if (more) "Rules and controls" else "How to play Sudoku", color = Forest, fontFamily = FontFamily.Serif)
        },
        text = {
            val scroll = key(more) { rememberScrollState() }
            Column(Modifier.verticalScroll(scroll), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (more) {
                    SectionTitle("Board controls")
                    Text("The counter under each number shows how many are still missing. Erase clears a square. Undo reverses your last safe move.", color = Ink)
                    SectionTitle("Solo and Daily help")
                    Text("Hint explains a deduction. If the Coach runs out of steps, you can reveal one number. Check my work can find an earlier mistake and lets you choose whether to erase it.", color = Ink)
                    Text("Clean pencil marks in Settings removes matching notes when you enter a number. Undo restores the notes. Hints, checks, and automatic cleanup mark a finish Assisted. Unassisted best times stay separate.", color = Ink)
                    SectionTitle("Daily puzzle")
                    Text("There is one offline Easy puzzle per day. It saves separately from your Solo puzzle.", color = Ink)
                    SectionTitle("Nearby play")
                    Text("Both players use the same Wi-Fi. In Co-op, you share one board. In Versus, each player races on their own board.", color = Ink)
                } else {
                    Text("Place 1–9 once in each row, column, and 3×3 box.", color = Ink)
                    Text("Tap a square, then a number. Use Notes to pencil in possibilities.", color = Ink)
                    Text("Tap Hint in Solo or Daily for help with your next move.", color = Ink)
                    TextButton(onClick = { more = true }, modifier = Modifier.heightIn(min = 48.dp),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)) {
                        Text("More rules and controls")
                    }
                }
            }
        },
        confirmButton = {
            when {
                more -> TextButton(onClick = { more = false }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Back to basics") }
                openPractice != null -> Button(onClick = openPractice, modifier = Modifier.heightIn(min = 48.dp),
                    shape = RoundedCornerShape(14.dp)) { Text("Coach practice") }
                else -> TextButton(onClick = close, modifier = Modifier.heightIn(min = 48.dp)) { Text("Got it") }
            }
        },
        dismissButton = {
            if (!more && openPractice != null)
                TextButton(onClick = close, modifier = Modifier.heightIn(min = 48.dp)) { Text("Got it") }
        }
    )
}

internal data class PuzzleFill(val filled: Int, val target: Int) {
    val label get() = "$filled of $target filled"
    val fraction get() = filled.toFloat() / target.coerceAtLeast(1)
}

internal fun Puzzle.fillProgress(cells: List<Cell>): PuzzleFill {
    val editable = clues.indices.filter { clues[it] == 0 }
    return PuzzleFill(editable.count { cells[it].value != 0 }, editable.size)
}

internal fun difficultyName(level: String?): String = when (level) {
    "EASY" -> "Easy"
    "MEDIUM" -> "Medium"
    "HARD" -> "Hard"
    else -> "Legacy"
}

@Composable internal fun Notice(message: String) {
    Surface(color = Color(0xFFF8E8E0), shape = RoundedCornerShape(13.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            GameIcon(GameSymbol.HELP, Brick, Modifier.size(19.dp))
            Text(message, fontSize = 12.sp, lineHeight = 18.sp, color = Brick, modifier = Modifier.weight(1f))
        }
    }
}
