package com.playereden.sudokutogether

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import android.view.HapticFeedbackConstants
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.playereden.sudokutogether.core.*
import kotlinx.coroutines.delay

@Composable internal fun PlayScreen(state: GameUiState, actions: GameActions, leave: () -> Unit,
    new: () -> Unit, replace: () -> Unit, help: () -> Unit) {
    var details by rememberSaveable { mutableStateOf(false) }
    var chatOpen by rememberSaveable { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current
    val haptics = LocalHapticFeedback.current
    val feedbackView = LocalView.current
    val context = LocalContext.current
    val sounds = remember(context, state.soundEffects) {
        if (state.soundEffects) runCatching { PuzzleSoundBank(context) }.getOrNull() else null
    }
    DisposableEffect(sounds) { onDispose { sounds?.release() } }
    fun tick(cue: PuzzleCue) {
        if (state.touchFeedback) playPuzzleClick { feedbackView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) }
        if (state.soundEffects) playPuzzleClick { sounds?.play(cue) }
    }
    fun copyCode() { clipboard.setText(AnnotatedString(state.roomCode)); copied = true
        if (state.touchFeedback) haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
    LaunchedEffect(copied) { if (copied) { delay(1800); copied = false } }
    val snapshot = state.snapshot
    val completionGuard = remember { CompletionCueGuard() }
    val roundId = snapshot?.let { "${it.sessionId}:${it.epoch}" }
    LaunchedEffect(roundId, snapshot?.completed) {
        val newlyCompleted = completionGuard.observe(roundId, snapshot?.completed == true)
        if (newlyCompleted && snapshot != null && (snapshot.mode != VERSUS || snapshot.winner == state.role)) {
            if (state.soundEffects) playPuzzleClick { sounds?.play(PuzzleCue.COMPLETE) }
            if (state.touchFeedback) playPuzzleClick {
                feedbackView.performHapticFeedback(
                    if (android.os.Build.VERSION.SDK_INT >= 30) HapticFeedbackConstants.CONFIRM
                    else HapticFeedbackConstants.KEYBOARD_TAP)
            }
        }
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val scale = LocalDensity.current.fontScale.coerceIn(1f, 2f)
        // Narrow large-text layouts need room for the two-row toolbar; let the whole page scroll.
        // Compact three-row keypad also needs a scrollable page at normal font scale.
        val shortStack = maxWidth < 288.dp || (maxWidth < 364.dp && scale > 1.3f) || maxHeight < 160.dp || (maxWidth < 520.dp && maxHeight < (380f + (scale - 1f) * 160f).dp)
        if (shortStack) {
            // A short window must be able to scroll the header and dock too;
            // reserving their height would give the active grid no viewport.
            val scroll = key(snapshot?.sessionId, snapshot?.epoch, snapshot?.completed) { rememberScrollState() }
            Column(Modifier.fillMaxSize().verticalScroll(scroll)) {
                PlayTopBar(state, leave, help, { details = true }) { chatOpen = true; actions.chatVisible(true) }
                if (snapshot == null) OpeningPuzzle(Modifier.fillMaxWidth().height(160.dp))
                else {
                    PlayBoardPane(state, actions, new,
                        Modifier.fillMaxWidth(), roomStatus = true, scrollable = false, feedback = ::tick)
                    if (!snapshot.completed) SudokuInputDock(snapshot, state, actions, ::tick)
                }
            }
        } else Column(Modifier.fillMaxSize()) {
            PlayTopBar(state, leave, help, { details = true }) { chatOpen = true; actions.chatVisible(true) }
            if (snapshot == null) OpeningPuzzle(Modifier.weight(1f).fillMaxWidth())
            else BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
                val wide = maxWidth >= 520.dp && maxWidth > maxHeight && !snapshot.completed
                val availablePlayHeight = maxHeight
                if (wide) {
                    // Give large landscape windows a wider control rail without
                    // consuming the space needed by the board. Compact windows
                    // retain their established rail dimensions.
                    val railWidth = if (maxWidth >= 1000.dp) {
                        (maxWidth * .36f).coerceIn(352.dp, 480.dp)
                    } else (maxWidth * .46f).coerceIn(288.dp, 352.dp)
                    Row(Modifier.fillMaxSize().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PlayBoardPane(state, actions, new,
                            Modifier.weight(1f).fillMaxHeight(), roomStatus = false, feedback = ::tick)
                        Column(Modifier.width(railWidth).fillMaxHeight().verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (state.role != SOLO) {
                                if (snapshot.mode == VERSUS) VersusPresence(state, snapshot) { actions.showJoin(true) }
                                else RoomStatusCard(state, actions)
                            }
                            if (!snapshot.completed) SudokuInputDock(snapshot, state, actions, ::tick)
                        }
                    }
                } else Column(Modifier.fillMaxSize()) {
                    // On tall portrait phones keep the entire board/meta/
                    // caption cluster *visually centered* in the weighted
                    // play area. The old scrollable Column received infinite
                    // vertical constraints and Arrangement.Center had no
                    // effect; hence the large empty board-to-keypad gap.
                    // Preserve the original scrolling board on short phones,
                    // large-font layouts and all multiplayer modes.
                    val centerSoloBoard = state.role == SOLO && snapshot.mode == SOLO &&
                        !snapshot.completed && availablePlayHeight >= 670.dp &&
                        LocalDensity.current.fontScale <= 1.3f
                    if (centerSoloBoard) {
                        Box(Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.Center) {
                            PlayBoardPane(state, actions, new,
                                Modifier.fillMaxWidth(), roomStatus = true, scrollable = false, feedback = ::tick)
                        }
                    } else {
                        PlayBoardPane(state, actions, new,
                            Modifier.weight(1f).fillMaxWidth(), roomStatus = true, feedback = ::tick)
                    }
                    if (!snapshot.completed) SudokuInputDock(snapshot, state, actions, ::tick)
                }
            }
        }
    }
    if (details && state.role != SOLO) AlertDialog(onDismissRequest = { details = false },
        title = { Text("Game details") }, text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.role == HOST && snapshot != null) {
                    Text(nearbyRoomLabel(snapshot.sessionId), color = Forest,
                        modifier = Modifier.testTag("host_nearby_room_label"))
                }
                if (state.role == HOST && state.roomCode.isNotBlank()) {
                    SelectionContainer { Text(state.roomCode, color = Forest, fontSize = 18.sp,
                        modifier = Modifier.semantics { contentDescription = "Room code ${state.roomCode}" }) }
                    CopyControl(::copyCode, copied)
                } else if (state.role == HOST) Text("Open nearby game", color = Forest)
                if (state.address.isNotBlank()) SelectionContainer { Text("Join address\n${state.address}", color = Forest, fontSize = 13.sp) }
                Text("Keep both apps open while you play.", color = Muted, fontSize = 13.sp)
                if (state.role == HOST) {
                    if (snapshot?.mode != VERSUS || !snapshot.completed) {
                        TextButton({ details = false; new() }, enabled = !state.busy, modifier = Modifier.heightIn(min = 48.dp)) {
                            Text(if (snapshot?.mode == VERSUS) "Next race" else "New puzzle")
                        }
                    }
                    if (state.snapshot?.guestName != null) TextButton({ details = false; replace() }, enabled = !state.busy,
                        modifier = Modifier.heightIn(min = 48.dp)) { Text("Replace partner") }
                }
            }
        }, confirmButton = { TextButton({ details = false }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Done") } })
    HintAndChat(state, actions, chatOpen) { chatOpen = false; actions.chatVisible(false) }
}

@Composable private fun OpeningPuzzle(modifier: Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator(); Text("Opening your puzzle…", color = Muted)
        }
    }
}

@Composable private fun PlayTopBar(state: GameUiState, home: () -> Unit, help: () -> Unit, details: () -> Unit, chat: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            HomeControl(home)
            PlayTitle(state, Modifier.weight(1f))
            HelpControl(help)
        }
        if (state.role != SOLO) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(details, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Game details", fontSize = 13.sp) }
            ChatControl(state, chat, Modifier.weight(1f))
        }
    }
}

@Composable private fun HomeControl(home: () -> Unit) {
    TextButton(home, modifier = Modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 6.dp)) {
        GameIcon(GameSymbol.BACK, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Home", fontSize = 12.sp)
    }
}

@Composable private fun PlayTitle(state: GameUiState, modifier: Modifier) {
    Column(modifier) {
        Text(when { state.dailyDate != null -> "Daily"; state.role == SOLO -> "Solo"; state.snapshot?.mode == VERSUS -> "Versus"; else -> "Co-op" },
            color = Forest, fontSize = 22.sp, lineHeight = 25.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold)
        Text(difficultyName(if (state.snapshot == null) state.selectedDifficulty else state.snapshot.puzzle.difficulty) + when {
            state.dailyDate != null -> " · ${state.dailyDate}"
            else -> " · Classic 9×9"
        }, color = Muted, fontSize = 10.sp, lineHeight = 14.sp)
    }
}

@Composable private fun CopyControl(copy: () -> Unit, copied: Boolean, modifier: Modifier = Modifier) {
    TextButton(copy, modifier = modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
        Text(if (copied) "Copied" else "Copy code", fontSize = 13.sp, lineHeight = 14.sp)
    }
}

@Composable private fun ChatControl(state: GameUiState, chat: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(chat, modifier = modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
        GameIcon(GameSymbol.CHAT, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp))
        Text(if (state.unreadChat > 0) "Chat (${state.unreadChat})" else "Chat",
            fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable private fun HelpControl(help: () -> Unit) {
    IconButton(help, modifier = Modifier.size(48.dp).semantics { contentDescription = "How to play Sudoku" }) {
        GameIcon(GameSymbol.HELP, modifier = Modifier.size(22.dp))
    }
}

@Composable private fun PlayBoardPane(state: GameUiState, actions: GameActions, new: () -> Unit,
    modifier: Modifier, roomStatus: Boolean, scrollable: Boolean = true, feedback: (PuzzleCue) -> Unit = {}) {
    val snapshot = state.snapshot ?: return
    BoxWithConstraints(modifier) {
        val viewportHeight = if (maxHeight.value.isFinite()) maxHeight else maxWidth + 160.dp
        val race = snapshot.takeIf { it.mode == VERSUS }
        val contentModifier = if (scrollable) {
            val scroll = key(snapshot.sessionId, snapshot.epoch, snapshot.completed) { rememberScrollState() }
            Modifier.fillMaxSize().verticalScroll(scroll)
        } else Modifier.fillMaxWidth()
        // On tall portrait phones the grid reaches its width-based maximum
        // while the weighted board pane is taller than its contents. Center
        // the Solo board cluster in that spare area, cutting the apparent
        // dead zone between its caption and the bottom keypad in half.
        // Retain scrolling for small phones / large fonts, and leave LAN
        // boards and finished-result layouts unchanged.
        val soloActive = state.role == SOLO && snapshot.mode == SOLO && !snapshot.completed
        Column(contentModifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (soloActive && scrollable) Arrangement.Center
                else Arrangement.spacedBy(8.dp)) {
            if (snapshot.completed) {
                if (race != null) VersusResults(state, race, { actions.rematch(race.epoch) }) { actions.showJoin(true) }
                else PuzzleFinishCard(state, actions, new)
            }
            PlayGridViewport(state, actions, new, roomStatus, viewportHeight - 8.dp, feedback)
            if (state.role != SOLO && snapshot.mode != VERSUS) PartnerStrip(state)
            if (snapshot.mode == COOP && snapshot.completed && snapshot.guestName != null) {
                GamePanel(Modifier.widthIn(max = 460.dp).fillMaxWidth()) {
                    SectionTitle("A board solved together")
                    Text("Final entries", color = Muted, style = MaterialTheme.typography.labelMedium)
                    for ((playerRole, playerName) in listOf(HOST to snapshot.hostName, GUEST to snapshot.guestName.orEmpty())) {
                        val entries = snapshot.cells.count { it.owner == playerRole && it.value in 1..9 }
                        Row(Modifier.fillMaxWidth().testTag("coop_final_entries_" + playerRole),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(playerName, modifier = Modifier.weight(1f), color = Ink,
                                style = MaterialTheme.typography.bodyMedium)
                            Text(entries.toString(), color = Ink, style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.semantics { contentDescription = "$entries final entries" })
                        }
                    }
                    Text("A shared contribution, not a skill ranking.", color = Muted, fontSize = 11.sp)
                }
            }
            state.message?.let { Notice(it) }
            if (roomStatus && snapshot.mode != VERSUS && !snapshot.completed && state.role == GUEST && !state.connected)
                GamePrimaryButton("Reconnect", { actions.showJoin(true) }, !state.busy)

        }
    }
}

/** Measure related content in the same layout pass; no layout-to-state feedback. */
@Composable private fun PlayGridViewport(state: GameUiState, actions: GameActions, new: () -> Unit, roomStatus: Boolean, viewportHeight: Dp, feedback: (PuzzleCue) -> Unit) {
    val snapshot = state.snapshot ?: return
    val coOp = state.role != SOLO && snapshot.mode == COOP
    var pingVisible by remember(state.partnerPing, state.partnerPingUntil) { mutableStateOf(state.partnerPing != null && state.partnerPingUntil > android.os.SystemClock.elapsedRealtime()) }
    LaunchedEffect(state.partnerPing, state.partnerPingUntil) {
        if (pingVisible) {
            delay((state.partnerPingUntil - android.os.SystemClock.elapsedRealtime()).coerceAtLeast(0L))
            pingVisible = false
        }
    }
    val pingCell = if (coOp && pingVisible && state.partnerPing?.matches(snapshot) == true) state.partnerPing.cell else -1
    val boardTheme = if (coOp) {
        Cosmetics.sharedCoopBoard(snapshot.hostAppearance.board, snapshot.guestAppearance.board.takeIf { snapshot.guestName != null })
    } else state.appearance.board
    val sharedBoardName = if (coOp && snapshot.guestName != null && boardTheme != "classic") {
        Cosmetics.boards.firstOrNull { it.id == boardTheme }?.name
    } else null
    val selectedEntry = snapshot.cells.getOrNull(state.selected)?.value ?: 0
    val conflictingEntries = remember(snapshot.cells) { Sudoku.conflicts(snapshot.cells.map { it.value }) }
    val selectedConflict = !snapshot.completed && !state.notes && selectedEntry != 0 &&
        state.selected in conflictingEntries && snapshot.puzzle.clues.getOrNull(state.selected) == 0
    val caption = when {
        snapshot.completed && snapshot.mode == VERSUS -> if (snapshot.winner == state.role) "Your solved grid" else "Your grid at the finish"
        snapshot.completed -> "Your finished grid"
        state.selected !in 0..80 -> "Tap a square, then choose a number."
        snapshot.puzzle.clues[state.selected] != 0 -> "R${state.selected / 9 + 1} · C${state.selected % 9 + 1} · Starting clue"
        state.notes && snapshot.cells[state.selected].value != 0 ->
            "R${state.selected / 9 + 1} · C${state.selected % 9 + 1} · Erase entry before adding pencil marks"
        selectedConflict -> "R${state.selected / 9 + 1} · C${state.selected % 9 + 1} · Repeated $selectedEntry — check row, column or box"
        state.notes -> "R${state.selected / 9 + 1} · C${state.selected % 9 + 1} · Pencil marks"
        else -> "R${state.selected / 9 + 1} · C${state.selected % 9 + 1} · Enter a number"
    }
    SubcomposeLayout(Modifier.fillMaxWidth()) { constraints ->
        val textConstraints = Constraints(minWidth = constraints.maxWidth, maxWidth = constraints.maxWidth, maxHeight = Constraints.Infinity)
        val meta = subcompose("meta") {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.role != SOLO && snapshot.mode == VERSUS) {
                    if (roomStatus && !snapshot.completed) VersusPresence(state, snapshot) { actions.showJoin(true) }
                } else {
                    if (roomStatus && state.role != SOLO) Text(coopPresence(state).label, color = Forest, fontSize = 11.sp, lineHeight = 16.sp,
                        minLines = 2, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                    if (sharedBoardName != null) Text("Shared board · $sharedBoardName", color = Forest, fontSize = 11.sp, lineHeight = 16.sp)
                    PuzzleMeta(state)
                }
            }
        }.single().measure(textConstraints)
        val label = subcompose("caption") {
            val canNewSolo = state.role == SOLO && !snapshot.completed && state.dailyDate == null
            Row(Modifier.fillMaxWidth().heightIn(min = if (canNewSolo) 48.dp else 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(if (pingCell >= 0) "Partner: Look here! R${pingCell / 9 + 1} C${pingCell % 9 + 1}" else caption, color = if (selectedConflict) Brick else Muted,
                    fontSize = 11.sp, lineHeight = 16.sp,
                    minLines = if (snapshot.completed) 1 else if (LocalDensity.current.fontScale > 1.3f) 3 else 2,
                    textAlign = if (canNewSolo) TextAlign.Start else TextAlign.Center,
                    modifier = Modifier.weight(1f).testTag("board_context").semantics { if (pingCell >= 0) liveRegion = LiveRegionMode.Polite })
                if (coOp && !snapshot.completed) {
                    TextButton(onClick = actions.partnerPing, enabled = state.selected in 0..80 && state.connected && state.status.hostActive && state.status.guestActive,
                        modifier = Modifier.heightIn(min = 48.dp).testTag("partner_ping_send"), contentPadding = PaddingValues(horizontal = 6.dp)) {
                        Text("Look here", fontSize = 12.sp)
                    }
                }
                if (canNewSolo) {
                    TextButton(onClick = new, modifier = Modifier.heightIn(min = 48.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)) {
                        Text("New puzzle", color = Forest, fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }.single().measure(textConstraints)
        val gap = 8.dp.roundToPx()
        val frame = gap * 2
        // Expand only when the board pane itself has tablet-sized space.
        // A wide window with a narrow landscape rail must still respect its
        // actual pane width and measured vertical budget.
        val gridCap = if (constraints.maxWidth >= 580.dp.roundToPx()) 600.dp else 440.dp
        val maxGrid = (constraints.maxWidth - frame).coerceAtLeast(1).coerceAtMost(gridCap.roundToPx())
        val minGrid = minOf(144.dp.roundToPx(), maxGrid)
        val gridTop = meta.height + if (meta.height > 0) gap else 0
        val gridPixels = (viewportHeight.roundToPx() - gridTop - label.height - frame - gap).coerceIn(minGrid, maxGrid)
        val gridSize = gridPixels.toDp()
        val grid = subcompose("grid") {
            Surface(shape = RoundedCornerShape(16.dp), color = boardFillFor(boardTheme), shadowElevation = 3.dp,
                border = BorderStroke(1.dp, Rule), modifier = Modifier.testTag("board_frame")) {
                Box(Modifier.padding(8.dp)) {
                    SudokuBoard(snapshot, state.selected, if (state.role == SOLO || snapshot.mode == VERSUS) -1 else state.partnerCell,
                        gridSize, { if (!snapshot.completed) feedback(PuzzleCue.SELECT); actions.select(it) },
                        boardTheme, state.appearance, state.role == SOLO, state.uniformNumbers, pingCell = pingCell)
                }
            }
        }.single().measure(Constraints.fixed(gridPixels + frame, gridPixels + frame))
        layout(constraints.maxWidth, gridTop + grid.height + gap + label.height) {
            meta.placeRelative(0, 0)
            grid.placeRelative((constraints.maxWidth - grid.width) / 2, gridTop)
            label.placeRelative(0, gridTop + grid.height + gap)
        }
    }
}

@Composable private fun PuzzleMeta(state: GameUiState) {
    val snapshot = state.snapshot ?: return
    val fill = snapshot.puzzle.fillProgress(snapshot.cells)
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        if (state.role == SOLO) {
            if (LocalDensity.current.fontScale > 1.3f) Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(fill.label, color = Muted, fontSize = 11.sp)
                SoloTimer(state.soloElapsedMs, state.soloClockStartedAt)
            } else Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(fill.label, color = Muted, fontSize = 11.sp)
                SoloTimer(state.soloElapsedMs, state.soloClockStartedAt)
            }
        } else {
            val sharedStatus = if (state.partnerCell >= 0)
                "Partner at R${state.partnerCell / 9 + 1} C${state.partnerCell % 9 + 1}" else "Shared grid"
            Text("${fill.label} · $sharedStatus", color = Forest, fontSize = 11.sp)
        }
        LinearProgressIndicator(progress = { fill.fraction }, modifier = Modifier.fillMaxWidth().height(3.dp),
            color = Forest, trackColor = Rule.copy(alpha = .6f))
    }
}

@Composable private fun RoomStatusCard(state: GameUiState, actions: GameActions) {
    val presence = coopPresence(state)
    Surface(color = if (presence.attention) GoldTint else GreenTint, shape = RoundedCornerShape(13.dp)) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 9.dp)
            .testTag("coop_room_status"), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(presence.label, color = Forest, fontSize = 12.sp, lineHeight = 17.sp,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
            if (presence.reconnect) TextButton(onClick = { actions.showJoin(true) }, enabled = !state.busy,
                modifier = Modifier.heightIn(min = 48.dp).testTag("coop_reconnect")) { Text("Reconnect") }
        }
    }
}

@Composable private fun PartnerStrip(state: GameUiState) {
    val snapshot = state.snapshot ?: return
    val other = if (state.role == HOST) snapshot.guestAppearance else snapshot.hostAppearance
    val partner = if (state.role == HOST) snapshot.guestName else snapshot.hostName
    val identities = listOf(state.appearance to state.name, other to (partner ?: "Waiting for partner"))
    val stacked = LocalDensity.current.fontScale > 1.3f
    @Composable fun Identity(index: Int, modifier: Modifier) {
        val (look, name) = identities[index]
        Row(modifier.testTag("coop_player_identity_$index"),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            AnimalPortrait(look, Modifier.size(24.dp).clearAndSetSemantics {})
            Text(name, modifier = Modifier.weight(1f), color = inkFor(look.ink, false),
                style = MaterialTheme.typography.bodySmall, maxLines = if (stacked) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis)
        }
    }
    if (stacked) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            identities.indices.forEach { Identity(it, Modifier.fillMaxWidth()) }
        }
    } else {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            identities.indices.forEach { Identity(it, Modifier.weight(1f)) }
        }
    }
}

@Composable private fun DockTool(label: String, symbol: GameSymbol, enabled: Boolean, chosen: Boolean = false,
    modifier: Modifier, description: String? = null, selectionControl: Boolean = false, click: () -> Unit) {
    OutlinedButton(click, enabled = enabled, modifier = modifier.heightIn(min = 52.dp)
        .then(if (description != null) Modifier.semantics { contentDescription = description } else Modifier)
        .then(if (selectionControl) Modifier.semantics { selected = chosen } else Modifier), shape = RoundedCornerShape(12.dp),
        border = BorderStroke(if (chosen) 2.dp else 1.dp, if (chosen) Forest else Rule),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (chosen) GreenTint else Paper),
        contentPadding = PaddingValues(horizontal = 3.dp, vertical = 7.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            GameIcon(symbol, if (enabled) Forest else Muted.copy(alpha = .45f), Modifier.size(17.dp))
            Text(label, modifier = if (description != null) Modifier.clearAndSetSemantics {} else Modifier,
                fontSize = 11.sp, lineHeight = 14.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable private fun PuzzleFinishCard(state: GameUiState, actions: GameActions, new: () -> Unit) {
    val snapshot = state.snapshot ?: return
    if (snapshot.completed) {
        OutlinedCard(Modifier.widthIn(max = 440.dp).fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(containerColor = GoldTint)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(52.dp).background(Forest, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    GameIcon(GameSymbol.TROPHY, OnForest, Modifier.size(29.dp))
                }
                Eyebrow(if (state.dailyDate != null) "DAILY PAGE FINISHED"
                    else if (state.role == SOLO) "ONE MORE PUZZLE SOLVED" else "A SHARED VICTORY")
                Text(when {
                    state.dailyDate != null -> "Daily challenge complete"
                    state.role == SOLO -> "Puzzle completed"
                    else -> "Solved together!"
                }, color = Forest, fontSize = 20.sp, fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                if (state.role == SOLO) {
                    val finished = state.finishedSolo?.takeIf { it.id == snapshot.sessionId }
                        ?: if (state.dailyDate == null) state.profile.activeSolo
                        else state.profile.dailyProgress.firstOrNull { it.date == state.dailyDate }?.solo
                    if (finished?.id == snapshot.sessionId && finished.completed) {
                        val assisted = finished.assisted
                        Text("Time  " + formatElapsed(finished.elapsedMs) + "   ·   " +
                            difficultyName(finished.puzzle.difficulty),
                            color = Forest, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                            textAlign = TextAlign.Center)
                        Text("${finished.moves} " + (if (finished.moves == 1) "move" else "moves") + "   ·   " +
                            (if (assisted) "Assisted" else "Unassisted"),
                            color = Ink, fontSize = 12.sp, textAlign = TextAlign.Center)
                        if (finished.hintsUsed > 0)
                            Text("${finished.hintsUsed} Coach hint" +
                                (if (finished.hintsUsed == 1) "" else "s") + " used",
                                color = Muted, fontSize = 11.sp)
                        if (finished.checksUsed > 0)
                            Text("${if (finished.checksUsed == 9999) "9999+" else finished.checksUsed} entry check" +
                                (if (finished.checksUsed == 1) "" else "s") + " used",
                                color = Muted, fontSize = 11.sp)
                    }
                }
                CompletionRewardPanel(state, snapshot)
                if (state.dailyDate != null) {
                    val completedDays = DailyChallenge.completedDays(state.profile.records)
                    if (completedDays > 0) Text("${completedDays} daily puzzle" +
                        (if (completedDays == 1) " completed" else "s completed"),
                        color = Forest, style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.testTag("finish_daily_total"))
                    Text(if (state.dailyDate == state.todayDate) "New puzzle tomorrow."
                        else "Today's puzzle is on Home.", color = Muted, fontSize = 12.sp)
                    Button(onClick = actions.home,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)) {
                        Text("Back home")
                    }
                } else if (state.role == HOST || state.role == SOLO) {
                    Button(onClick = new, enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp).testTag("finish_next_puzzle")) {
                        Text(if (state.busy) "Opening puzzle…" else "New puzzle")
                    }
                } else if (!state.connected) {
                    Text("Reconnect to join the next puzzle.", color = Muted, fontSize = 12.sp)
                    GamePrimaryButton("Reconnect", { actions.showJoin(true) }, !state.busy)
                } else Text(if (state.status.hostActive) "Your host can start the next puzzle."
                    else "Waiting for the host to return.", color = Muted, fontSize = 12.sp)
                if (state.role == SOLO) TextButton(onClick = actions.profile,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text("Records")
                }
            }
        }
    }
}

@Composable private fun HintAndChat(state: GameUiState, actions: GameActions, chatOpen: Boolean, close: () -> Unit) {
    PuzzleCoachDialog(state,actions)
    if (chatOpen && state.role != SOLO) RoomChatSheet(state, actions, close)
}

@Composable private fun SudokuInputDock(snapshot: Snapshot, state: GameUiState, actions: GameActions, tick: (PuzzleCue) -> Unit) {
    val fontScale = LocalDensity.current.fontScale
    val editable = state.selected in 0..80 && snapshot.puzzle.clues[state.selected] == 0
    val selectedCell = snapshot.cells.getOrNull(state.selected)
    // Both Solo and LAN rules reject note edits on filled squares. Disable
    // the keypad first rather than surfacing a misleading failed-save message.
    val noteTargetOpen = !state.notes || selectedCell?.value == 0
    val chosenValue = selectedCell?.value
    val hasEraseTarget = selectedCell?.let { it.value != 0 || it.notes != 0 } == true
    Column(Modifier.fillMaxWidth().background(Paper, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        .border(1.dp, Rule, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        .testTag("input_dock")
        .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val tablet = maxWidth >= 560.dp
            val dockWidth = if (tablet) 560.dp else 440.dp
            val keypadHeight = maxOf(if (tablet) 60.dp else 48.dp, ((if (tablet) 48 else 40) * fontScale).dp)
            val availableDockWidth = minOf(maxWidth, dockWidth)
            val splitTools = availableDockWidth < 210.dp || (availableDockWidth < 340.dp && fontScale > 1.3f)
            // Five 48dp keys plus four 6dp gaps need 264dp.
            val compactKeys = availableDockWidth < 264.dp
            Column(Modifier.widthIn(max = dockWidth).fillMaxWidth().align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val tools = if (state.role == SOLO) listOf("notes", "erase", "undo", "hint")
                    else listOf("notes", "erase", "undo")
                val rows = if (splitTools) tools.chunked(2) else listOf(tools)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.testTag("solving_toolbar")) {
                    for (rowTools in rows) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (tool in rowTools) when (tool) {
                                "notes" -> DockTool(if (state.notes) "Notes on" else "Notes", GameSymbol.PENCIL,
                                    state.canEdit, state.notes, Modifier.weight(1f), selectionControl = true) { tick(PuzzleCue.NOTE); actions.toggleNotes() }
                                "erase" -> DockTool("Erase", GameSymbol.ERASE, state.canEdit && editable && hasEraseTarget,
                                    modifier = Modifier.weight(1f)) { tick(PuzzleCue.ERASE); actions.erase() }
                                "undo" -> DockTool("Undo", GameSymbol.UNDO, state.canEdit && snapshot.undoTargets.containsKey(state.role),
                                    modifier = Modifier.weight(1f)) { tick(PuzzleCue.UNDO); actions.undo() }
                                "hint" -> DockTool("Hint", GameSymbol.HELP, state.canEdit, modifier = Modifier.weight(1f),
                                    description = "Request a solo Sudoku hint") { actions.hint() }
                            }
                        }
                    }
                }
                // Keep the guidance row present across selection and input-mode changes.
                // Removing it used to change dock height and therefore the board viewport.
                val guidance = when {
                    !editable -> "Choose an empty or editable square"
                    state.notes && !noteTargetOpen -> "Erase the entry to add pencil marks"
                    state.notes -> "Tap a number to add or remove a pencil mark"
                    else -> "Small numbers show how many of each digit are left"
                }
                Text(guidance, color = Muted, fontSize = 10.sp, lineHeight = 14.sp,
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                        .testTag(if (state.notes || !editable) "keypad_mode_guidance" else "keypad_remaining_explanation"),
                    textAlign = TextAlign.Center)
                val numberRows = if (compactKeys) (1..9).toList().chunked(3)
                    else listOf((1..5).toList(), (6..9).toList())
                for (numbers in numberRows) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Center the four-key second row rather than leaving a dead slot on its right.
                        if (!compactKeys && numbers.size == 4) Spacer(Modifier.weight(.5f))
                        for (number in numbers) {
                            val remaining = (9 - snapshot.cells.count { it.value == number }).coerceAtLeast(0)
                    
                            // Tell the player what a note tap will do in the selected square.
                            // Remaining counts describe placed values, not pencil marks.
                            val notePresent = ((selectedCell?.notes ?: 0) and (1 shl (number - 1))) != 0
                            val highlighted = if (state.notes) editable && noteTargetOpen && notePresent else number == chosenValue
                            val spokenAction = if (state.notes) {
                                if (notePresent) "Remove pencil mark $number" else "Add pencil mark $number"
                            } else "Number $number, $remaining remaining"
                            OutlinedButton(onClick = { tick(if (state.notes) PuzzleCue.NOTE else PuzzleCue.NUMBER); actions.number(number) },
                                enabled = state.canEdit && editable && noteTargetOpen,
                                modifier = Modifier.weight(1f).height(keypadHeight).testTag("number_$number")
                                    .semantics {
                                        contentDescription = spokenAction
                                        if (state.notes) selected = highlighted
                                    },
                                shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(0.dp),
                                border = BorderStroke(if (highlighted) 2.dp else 1.dp, if (highlighted) Forest else Rule),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (highlighted) TealLight else Paper,
                                    disabledContainerColor = Paper,
                                    disabledContentColor = Forest.copy(alpha = .60f))) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center) {
                                    Text(number.toString(), modifier = Modifier.clearAndSetSemantics {}, fontSize = if (tablet) 26.sp else 21.sp, lineHeight = if (tablet) 28.sp else 22.sp,
                                        fontWeight = FontWeight.SemiBold)
                                    // A placed-digit count is misleading while editing pencil notes.
                                    if (!state.notes) {
                                        Text(remaining.toString(), modifier = Modifier.testTag("number_remaining_$number").clearAndSetSemantics {},
                                            fontSize = if (tablet) 12.sp else 10.sp, lineHeight = if (tablet) 14.sp else 11.sp, color = Muted)
                                    } else Text(if (highlighted) "✓" else " ",
                                        modifier = Modifier.clearAndSetSemantics {},
                                        fontSize = 10.sp, lineHeight = 11.sp, color = Forest,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        if (!compactKeys && numbers.size == 4) Spacer(Modifier.weight(.5f))
                    }
                }
            }
        }
    }
}

@Composable internal fun SudokuBoard(snapshot: Snapshot, selected: Int, partner: Int, size: Dp, select: ((Int) -> Unit)?,
    boardTheme: String, localStyle: Appearance, solo: Boolean, uniformNumbers: Boolean,
    proofTargets: Set<Int> = emptySet(), proofSources: Set<Int> = emptySet(),
    proofEliminations: Map<Int, List<Int>> = emptyMap(), announceAllCells: Boolean = false, pingCell: Int = -1) {
    val systemScale = LocalDensity.current.fontScale.coerceAtLeast(.5f)
    // A nine-cell row has fixed space. Let surrounding controls scale freely,
    // while fitting grid glyphs and their line heights inside each square.
    val correction = systemScale.coerceAtMost(1.15f) / systemScale
    val digitSize = (size.value / 9 * .64f * correction).sp
    // A 3x3 pencil glyph should be readable on small Coach boards as well as full puzzles.
    // At .82 of the note slot height, nine candidates still fit without overlap.
    val noteSlot = (size.value / 9 - 2f) / 3 * correction
    val noteSize = (noteSlot * .82f).sp
    val digitStyle = TextStyle(fontSize = digitSize, lineHeight = digitSize, platformStyle = PlatformTextStyle(includeFontPadding = false))
    val noteStyle = TextStyle(fontSize = noteSize, lineHeight = noteSize, platformStyle = PlatformTextStyle(includeFontPadding = false))
    val conflicts = remember(snapshot.cells) { Sudoku.conflicts(snapshot.cells.map { it.value }) }
    val selectedValue = snapshot.cells.getOrNull(selected)?.value ?: 0
    val palette = boardPalette(boardTheme)
    val darkBoard = palette.dark
    val defaultStyle = Appearance()
    val soloNumbers = localStyle.withNumberDisplay(uniformNumbers)
    val hostNumbers = snapshot.hostAppearance.withNumberDisplay(uniformNumbers)
    val guestNumbers = snapshot.guestAppearance.withNumberDisplay(uniformNumbers)
    val boardPaper = palette.fill
    val boardRule = palette.thinRule
    val boardEdge = palette.thickRule
    val neutralInk = palette.clueInk
    Column(Modifier.size(size).clip(RoundedCornerShape(3.dp)).background(boardPaper).drawWithContent {
        drawContent()
        val step = this.size.width / 9
        for (i in 0..9) {
            val stroke = if (i % 3 == 0) 1.8.dp.toPx() else .5.dp.toPx()
            val color = if (i % 3 == 0) boardEdge else boardRule
            drawLine(color, Offset(i * step, 0f), Offset(i * step, this.size.height), stroke)
            drawLine(color, Offset(0f, i * step), Offset(this.size.width, i * step), stroke)
        }
    }) {
        for (row in 0..8) Row(Modifier.weight(1f).fillMaxWidth()) {
            for (col in 0..8) {
                val i = row * 9 + col; val cell = snapshot.cells[i]; val given = snapshot.puzzle.clues[i] != 0
                val noteNumbers = (1..9).filter { cell.notes and (1 shl (it - 1)) != 0 }
                val crossedOff = proofEliminations[i].orEmpty()
                val content = "Row ${row + 1} column ${col + 1}, " + when { given -> "clue ${cell.value}"; cell.value != 0 -> "value ${cell.value}"; noteNumbers.isNotEmpty() -> "empty, notes ${noteNumbers.joinToString(" ")}"; else -> "empty" }
                val authored = when {
                    given -> defaultStyle
                    solo -> soloNumbers
                    cell.owner == HOST -> hostNumbers
                    cell.owner == GUEST -> guestNumbers
                    else -> defaultStyle
                }
                val authoredFont = fontFor(authored.font)
                val authoredInk = inkFor(authored.ink, darkBoard)
                val proofTarget = i in proofTargets
                val proofSource = i in proofSources
                val sameHouse = selected in 0..80 && (
                    row == selected / 9 || col == selected % 9 ||
                    (row / 3 == selected / 27 && col / 3 == (selected % 9) / 3)
                )
                val background = when {
                    proofSource -> palette.partnerCell
                    proofTarget -> palette.selectedCell
                    i == selected -> palette.selectedCell
                    i == partner -> palette.partnerCell
                    i in conflicts && !given -> palette.conflictCell
                    i in conflicts && given -> palette.matchingNumberCell
                    selectedValue != 0 && selectedValue == cell.value -> palette.matchingNumberCell
                    sameHouse -> palette.relatedCell
                    else -> boardPaper
                }
                val outline = when {
                    i == pingCell -> Modifier.border(3.dp, palette.proofOutline)
                    proofTarget -> Modifier.border(2.dp, palette.proofOutline)
                    i == partner || i == selected -> Modifier.border(2.dp, boardEdge)
                    else -> Modifier
                }
                // A short underline flags a repeated entered value without relying on hue.
                // Keep fixed clues and Coach proof annotations distinct.
                val conflictMarker = i in conflicts && !given && cell.value != 0 && !proofTarget && !proofSource
                val click = if (select == null) Modifier else Modifier.clickable(role = Role.Button) { select(i) }
                val tag = if (select == null) "coach_cell_$i" else "cell_$i"
                val semantics = if (select == null && !announceAllCells && !proofTarget && !proofSource) Modifier.clearAndSetSemantics {}
                    else Modifier.testTag(tag).semantics(mergeDescendants = true) {
                        contentDescription = if (i == pingCell) "$content, partner attention marker" else if (crossedOff.isEmpty()) content else
                            "$content, cross off ${crossedOff.joinToString(" and ")}"
                        this.selected = i == selected
                        stateDescription = when {
                            proofTarget && proofSource -> "Hint target and proof square"
                            proofTarget -> "Hint target"
                            proofSource -> "Proof square"
                            i in conflicts && given -> if (i == selected) "Selected fixed clue duplicated by an entry"
                                else "Fixed clue duplicated by an entry; check placed values"
                            i in conflicts -> if (i == selected) "Selected square, conflicting entered value"
                                else "Conflicting entered value"
                            i == selected -> "Selected square"
                            i == partner -> "Partner's selected square"
                            selectedValue != 0 && selectedValue == cell.value -> "Matching number"
                            sameHouse -> "Related square"
                            else -> "Puzzle square"
                        }
                    }
                Box(Modifier.weight(1f).fillMaxHeight().background(background).then(outline).then(click).then(semantics),
                    contentAlignment = Alignment.Center) {
                    if (cell.value != 0) Text(cell.value.toString(), modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
                        style = digitStyle.copy(fontFamily = authoredFont), textAlign = TextAlign.Center,
                        maxLines = 1, softWrap = false, fontWeight = if (given) FontWeight.Bold else FontWeight.Normal,
                        color = if (i in conflicts && !given) conflictDigitInk(background, palette) else if (given) neutralInk
                        else legibleBoardDigitInk(authoredInk, background, palette),
                        textDecoration = if (conflictMarker) TextDecoration.Underline else null)
                    else if (cell.notes != 0) Column(Modifier.fillMaxSize().padding(1.dp)) {
                        // Keep the equipped color. If a dark-board highlight makes
                        // small pencil marks too faint, lift their contrast locally.
                        val pencilColor = pencilNoteInk(authoredInk, background, palette)
                        // A dense 3x3 pencil grid must fit every bundled font.
                        // Compact Coach grids and taller hand/rounded typefaces use
                        // the conservative fitting size; Clear and Serif can be
                        // slightly larger on the full board. Mono is narrow enough
                        // to retain the enlarged note size on full boards.
                        val fitFactor = when {
                            // At 150–200% system text, the existing correction
                            // alone can still clip line metrics in 3×3 note slots.
                            // Tighten only large-text pencil glyphs: normal-size
                            // players retain their existing visual appearance.
                            systemScale > 1.3f && size <= 300.dp -> .50f
                            systemScale > 1.3f -> .55f
                            size <= 300.dp -> .68f
                            authored.font == "mono" -> .82f
                            authored.font == "clear" || authored.font == "serif" -> .72f
                            else -> .68f
                        }
                        val fittedSize = (noteSlot * fitFactor).sp
                        val inkNoteStyle = noteStyle.copy(fontFamily = authoredFont,
                            fontSize = fittedSize, lineHeight = fittedSize)
                        for (r in 0..2) Row(Modifier.weight(1f).fillMaxWidth()) {
                            for (c in 0..2) {
                                val n = r * 3 + c + 1
                                Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    if (n in noteNumbers) {
                                        val removed = n in crossedOff
                                        val color = if (removed) crossedOffNoteInk(background, palette) else pencilColor
                                        Text(n.toString(), modifier = Modifier.fillMaxWidth().testTag("pencil_cell_${i}_${n}").clearAndSetSemantics {}, color = color,
                                            style = inkNoteStyle, textAlign = TextAlign.Center,
                                            textDecoration = if (removed) TextDecoration.LineThrough else null,
                                            maxLines = 1, softWrap = false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



/** Shared result panel for Solo, Daily, Co-op and Versus. */
@Composable internal fun CompletionRewardPanel(state: GameUiState, snapshot: Snapshot) {
    val recordId = if (state.role == SOLO) snapshot.sessionId else "${snapshot.sessionId}:${snapshot.epoch}:${state.role}"
    val earned = state.completionRewards.takeIf { state.completionRewardRecordId == recordId } ?: return
    if (!earned.hasNewRewards) return
    var showAllRewards by rememberSaveable(recordId) { mutableStateOf(false) }
    Surface(Modifier.fillMaxWidth().testTag("finish_new_rewards"),
        color = Paper, shape = RoundedCornerShape(13.dp), border = BorderStroke(1.dp, Rule)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Eyebrow("EARNED WITH THIS PUZZLE")
            (if (showAllRewards) earned.achievements else earned.achievements.take(2)).forEach { title ->
                Text("Achievement · $title", style = MaterialTheme.typography.bodySmall, color = Forest)
            }
            (if (showAllRewards) earned.cosmetics else earned.cosmetics.take(2)).forEach { title ->
                Text("Collection unlock · $title", style = MaterialTheme.typography.bodySmall, color = Forest)
            }
            if (earned.nameWords.isNotEmpty()) {
                Column(Modifier.fillMaxWidth().testTag("finish_name_words"), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${earned.nameWords.size} name words collected", style = MaterialTheme.typography.titleSmall, color = Forest)
                    val words = if (showAllRewards) earned.nameWords else earned.nameWords.take(3)
                    Text(words.joinToString(" · ") { it.label }, style = MaterialTheme.typography.bodyMedium, color = Forest)
                    Text("Added to your Name Collection", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
            val more = (earned.achievements.size - 2).coerceAtLeast(0) +
                (earned.cosmetics.size - 2).coerceAtLeast(0) + (earned.nameWords.size - 3).coerceAtLeast(0)
            if (more > 0) TextButton(onClick = { showAllRewards = !showAllRewards },
                modifier = Modifier.heightIn(min = 48.dp).testTag("finish_rewards_expand")) {
                Text(if (showAllRewards) "Show fewer rewards" else "Show all rewards (+$more)")
            }
        }
    }
}
