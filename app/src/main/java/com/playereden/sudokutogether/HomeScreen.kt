package com.playereden.sudokutogether

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.playereden.sudokutogether.core.DailyActivity
import com.playereden.sudokutogether.core.SoloProgress

/**
 * A puzzle-game Home, not an app configuration page. The Solo book scene is
 * the centerpiece, Daily is immediately below, and nearby games are secondary.
 * No puzzle, LAN, records, or saved-progress behavior is reimplemented here.
 */
@Composable internal fun HomeScreen(
    state: GameUiState, actions: GameActions,
    fresh: () -> Unit, freshSolo: () -> Unit, freshRace: () -> Unit,
    help: () -> Unit, profile: () -> Unit,
    markStyle: GridMatesMarkStyle = GridMatesMarkStyle.SUDOKU_GRID
) {
    var nearbyMode by rememberSaveable { mutableStateOf<String?>(null) }
    val active = state.profile.activeSolo
    val savedSolo = active?.takeIf { state.resumeSolo && !it.completed }
    val recentDays = remember(state.todayDate, state.profile.records, state.profile.dailyProgress) {
        DailyActivity.recentSevenDays(state.todayDate, state.profile.records, state.profile.dailyProgress)
    }
    val hasToday = state.profile.dailyProgress.any { it.date == state.todayDate }
    val dayDone = recentDays.lastOrNull()?.complete == true
    val homeScroll = rememberScrollState()
    BackHandler(enabled = nearbyMode != null) { nearbyMode = null }
    LaunchedEffect(nearbyMode) { homeScroll.scrollTo(0) }
    Column(Modifier.fillMaxSize().verticalScroll(homeScroll)
        .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HomeHeader(state, help, profile, markStyle)
            (state.message ?: state.saveIssue)?.let { Notice(it) }
            if (nearbyMode == null) {
                BookSoloStage(state, actions, savedSolo, active?.completed == true, freshSolo) { nearbyMode = null }
                DailyGameRibbon(state, actions, hasToday, dayDone,
                    recentDays.map { Triple(it.weekday, it.date, if (it.complete) 2 else if (it.inProgress) 1 else 0) })
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NearbyModeTile("Co-op", "Solve together", GameSymbol.PEOPLE,
                        false, Modifier.weight(1f).testTag("home_mode_together")) { nearbyMode = "together" }
                    NearbyModeTile("Versus", "Race a friend", GameSymbol.TROPHY,
                        false, Modifier.weight(1f).testTag("home_mode_versus")) { nearbyMode = "versus" }
                }
            } else {
                // Dedicated local multiplayer view: room controls start near the top,
                // instead of appearing below the Solo book and Daily card.
                Row(Modifier.fillMaxWidth().testTag("nearby_mode_navigation"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NearbyNavChoice("Solo", GameSymbol.PERSON, false,
                        Modifier.weight(1f).testTag("home_mode_solo")) { nearbyMode = null }
                    NearbyNavChoice("Co-op", GameSymbol.PEOPLE, nearbyMode == "together",
                        Modifier.weight(1f).testTag("home_mode_together")) { nearbyMode = "together" }
                    NearbyNavChoice("Versus", GameSymbol.TROPHY, nearbyMode == "versus",
                        Modifier.weight(1f).testTag("home_mode_versus")) { nearbyMode = "versus" }
                }
                NearbyActions(nearbyMode!!, state, actions, fresh, freshRace)
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable private fun BookSoloStage(
    state: GameUiState, actions: GameActions,
    savedSolo: SoloProgress?, lastCompleted: Boolean,
    freshSolo: () -> Unit, selectSolo: () -> Unit
) {
    val largeText = LocalDensity.current.fontScale > 1.3f
    Surface(Modifier.fillMaxWidth().testTag("home_book_stage"),
        color = NightForest, shape = RoundedCornerShape(23.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = .55f)), shadowElevation = 4.dp) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            // Solo is already the selected mode on this screen. Removing the
            // no-op "Solo" control prioritizes the actual game action + Daily.
            Text("YOUR SUDOKU BOOK", color = GoldTint, fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp, fontSize = 10.sp, lineHeight = 14.sp)
            HomeChapterArt(state.appearance, Modifier.fillMaxWidth().height(if (largeText) 112.dp else 130.dp))
            Text(if (savedSolo == null) "Play Sudoku" else "Your puzzle awaits",
                fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = if (largeText) 16.sp else 24.sp,
                lineHeight = if (largeText) 32.sp else 28.sp,
                softWrap = true, maxLines = 3, color = OnForest)
            if (savedSolo != null) {
                val filled = savedSolo.puzzle.fillProgress(savedSolo.cells)
                Text("${difficultyName(savedSolo.puzzle.difficulty)} · ${filled.label} · ${formatElapsed(savedSolo.elapsedMs)}",
                    color = OnForest.copy(alpha = .85f), fontSize = 12.sp, lineHeight = 16.sp)
                GamePrimaryButton("Continue", { actions.solo(true) },
                    enabled = !state.busy && !state.localSaveBlocked, light = true)
                Text("New puzzle difficulty", color = OnForest.copy(alpha = .8f),
                    fontSize = 11.sp)
                BookDifficultyPicker(state, actions)
                OutlinedButton(freshSolo, enabled = !state.busy && !state.localSaveBlocked,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                    border = BorderStroke(1.dp, OnForest),
                    shape = RoundedCornerShape(14.dp)) {
                    Text("New puzzle", color = OnForest)
                }
            } else {
                Text("Choose your difficulty", color = OnForest.copy(alpha = .84f),
                    fontSize = 11.sp, lineHeight = 15.sp)
                BookDifficultyPicker(state, actions)
                GamePrimaryButton("New puzzle", freshSolo,
                    enabled = !state.busy && !state.localSaveBlocked, light = true)
                if (lastCompleted && state.resumeSolo) {
                    TextButton(onClick = { actions.solo(true) }, enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp)) {
                        Text("Review last puzzle", color = OnForest)
                    }
                }
            }
            if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth(),
                color = GoldTint, trackColor = OnForest.copy(alpha = .2f))
        }
    }
}

@Composable private fun DailyGameRibbon(
    state: GameUiState, actions: GameActions, hasToday: Boolean,
    complete: Boolean, recentDays: List<Triple<String, String, Int>>
) {
    val finished = recentDays.count { it.third == 2 }
    Surface(onClick = actions.daily, enabled = !state.busy && !state.localSaveBlocked,
        modifier = Modifier.fillMaxWidth().testTag("daily_challenge"),
        shape = RoundedCornerShape(19.dp), color = GoldTint,
        border = BorderStroke(1.dp, Gold.copy(alpha = .22f)), shadowElevation = 1.dp) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Box(Modifier.size(34.dp).background(Paper, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center) {
                    GameIcon(GameSymbol.CALENDAR, Gold, Modifier.size(22.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text("Daily puzzle", color = Forest, fontWeight = FontWeight.Bold,
                        fontSize = 15.sp, lineHeight = 19.sp)
                    Text(when { complete -> "Completed"; hasToday -> "In progress"; else -> "Ready" },
                        color = Muted, fontSize = 10.sp, lineHeight = 13.sp)
                    Text("${state.todayDate} · Easy", color = Muted,
                        fontSize = 9.sp, lineHeight = 12.sp)
                }
                GameIcon(if (complete) GameSymbol.CHECK else GameSymbol.RIGHT,
                    color = Forest, modifier = Modifier.size(20.dp))
            }
            HorizontalDivider(color = Gold.copy(alpha = .2f))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("$finished / 7", color = Forest, fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("daily_seven_day_summary"))
                Text("past 7 days", color = Muted, fontSize = 10.sp,
                    modifier = Modifier.weight(1f))
                if (LocalDensity.current.fontScale <= 1.3f) {
                    Row(Modifier.testTag("daily_seven_day_history"),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        recentDays.forEach { (weekday, date, status) ->
                            Column(Modifier.clearAndSetSemantics {
                                testTag = "daily_weekday_${weekday.take(2)}"
                                    contentDescription = "${weekday.take(2)}, $date: " + when(status) {
                                    2 -> "completed"; 1 -> "in progress"; else -> "not completed"
                                }
                            }, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(weekday.take(2), color = Muted, fontSize = 9.sp, lineHeight = 12.sp)
                                Box(Modifier.size(16.dp)
                                    .background(if (status == 2) Forest else Paper, CircleShape)
                                    .border(
                                        width = if (status == 1) 1.5.dp else 0.dp,
                                        color = if (status == 1) Gold else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                    contentAlignment = Alignment.Center) {
                                    when (status) {
                                        2 -> GameIcon(GameSymbol.CHECK, OnForest, Modifier.size(10.dp))
                                        1 -> Box(Modifier.size(6.dp).background(Gold, CircleShape))
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

@Composable private fun NearbyModeTile(
    title: String, subtitle: String, icon: GameSymbol,
    selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    val color by animateColorAsState(if (selected) GreenTint else Paper, label = "nearby mode")
    Surface(onClick = onClick,
        modifier = modifier.semantics { this.selected = selected },
        shape = RoundedCornerShape(17.dp), color = color,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) Forest else Rule),
        shadowElevation = if (selected) 2.dp else 0.dp) {
        Row(Modifier.fillMaxWidth().heightIn(min = 65.dp)
            .padding(horizontal = 9.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            GameIcon(icon, Forest, Modifier.size(25.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Forest, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = Muted, fontSize = 10.sp, lineHeight = 14.sp)
            }
        }
    }
}

/** Compact in-mode navigation; all multiplayer actions stay on this screen. */
@Composable private fun NearbyNavChoice(
    title: String, symbol: GameSymbol, selected: Boolean,
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Surface(onClick = onClick, modifier = modifier.semantics { this.selected = selected },
        shape = RoundedCornerShape(15.dp),
        color = if (selected) Forest else Paper,
        border = BorderStroke(1.dp, if (selected) Forest else Rule)) {
        Column(Modifier.fillMaxWidth().heightIn(min = 65.dp).padding(7.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            GameIcon(symbol, if (selected) OnForest else Forest, Modifier.size(19.dp))
            Text(title, color = if (selected) OnForest else Forest,
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable private fun NearbyActions(
    mode: String, state: GameUiState, actions: GameActions,
    fresh: () -> Unit, freshRace: () -> Unit
) {
    Column(Modifier.fillMaxWidth().testTag("nearby_setup").animateContentSize()
        .background(GreenTint, RoundedCornerShape(18.dp))
        .border(1.dp, Rule, RoundedCornerShape(18.dp))
        .padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(if (mode == "together") "Shared board" else "Race to finish",
            color = Forest, fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold, fontSize = 23.sp)
        Text(if (mode == "together") "Solve one puzzle together · Same Wi-Fi"
            else "Race on separate boards · Same Wi-Fi", color = Muted, fontSize = 12.sp)
        Text("New game difficulty", color = Muted, fontSize = 11.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf("EASY", "MEDIUM", "HARD").forEach { id ->
                val chosen = state.selectedDifficulty == id
                OutlinedButton(onClick = { actions.difficulty(id) }, enabled = !state.busy,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        .testTag("nearby_difficulty_$id")
                        .semantics { selected = chosen; contentDescription = difficultyName(id) + " difficulty for new games" },
                    shape = RoundedCornerShape(11.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                    border = BorderStroke(1.dp, if (chosen) Forest else Rule),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (chosen) Paper else Color.Transparent)) {
                    Text(difficultyName(id), color = Forest, fontSize = 12.sp)
                }
            }
        }
        Row(Modifier.fillMaxWidth().heightIn(min = 48.dp)
            .toggleable(state.requireRoomCode, enabled = !state.busy,
                role = Role.Switch, onValueChange = actions.requireRoomCode)
            .semantics { contentDescription = "Require a code for a new game" }
            .testTag("require_code"),
            verticalAlignment = Alignment.CenterVertically) {
            Text("Require a room code", color = Forest, fontSize = 13.sp,
                modifier = Modifier.weight(1f))
            Switch(
                checked = state.requireRoomCode,
                onCheckedChange = null,
                enabled = !state.busy,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = OnForest,
                    checkedTrackColor = Forest,
                    checkedBorderColor = Forest,
                    uncheckedThumbColor = Paper,
                    uncheckedTrackColor = Forest.copy(alpha = .46f),
                    uncheckedBorderColor = Forest.copy(alpha = .75f),
                    disabledUncheckedThumbColor = Paper.copy(alpha = .68f),
                    disabledUncheckedTrackColor = Muted.copy(alpha = .2f)
                )
            )
        }
        if (mode == "together") {
            if (state.resumeHost) GamePrimaryButton("Resume room",
                { actions.host(true) }, !state.busy)
            GamePrimaryButton("Start a game", fresh, !state.busy)
            OutlinedButton({ actions.showJoin(false) }, enabled = !state.busy,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Join a game") }
            if (state.resumeGuest) TextButton({ actions.showJoin(true) }, enabled = !state.busy) {
                Text("Rejoin room")
            }
        } else {
            if (state.resumeRace) GamePrimaryButton("Resume race",
                { actions.hostRace(true) }, !state.busy)
            GamePrimaryButton("Start a race", freshRace, !state.busy)
            OutlinedButton({ actions.showJoin(false) }, enabled = !state.busy,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Join a game") }
        }
    }
}

@Composable private fun HomeHeader(state: GameUiState, help: () -> Unit,
    profile: () -> Unit, markStyle: GridMatesMarkStyle) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val reflow = LocalDensity.current.fontScale > 1.3f || maxWidth < 280.dp
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                GridMatesMark(Modifier.size(35.dp), markStyle)
                Column(Modifier.weight(1f)) {
                    Text("GridMates", color = Forest, fontSize = 19.sp,
                        lineHeight = 21.sp, fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold)
                    Text("SUDOKU", color = Gold, fontSize = 10.sp,
                        lineHeight = 14.sp, letterSpacing = 1.5.sp)
                }
                if (!reflow) HomeHeaderActions(state, help, profile)
            }
            if (reflow) Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                HomeHeaderActions(state, help, profile)
            }
        }
    }
}

@Composable private fun HomeHeaderActions(
    state: GameUiState, help: () -> Unit, profile: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        IconButton(help, modifier = Modifier.size(46.dp)
            .semantics { contentDescription = "How to play Sudoku" }) {
            GameIcon(GameSymbol.HELP, modifier = Modifier.size(21.dp))
        }
        IconButton(profile, enabled = !state.busy,
            modifier = Modifier.size(46.dp).background(GreenTint, RoundedCornerShape(14.dp))
                .semantics { contentDescription = "My profile & records" }) {
            AnimalPortrait(state.appearance, Modifier.size(39.dp))
        }
    }
}

@Composable private fun BookDifficultyPicker(state: GameUiState, actions: GameActions) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        listOf("EASY", "MEDIUM", "HARD").forEach { id ->
            val chosen = state.selectedDifficulty == id
            OutlinedButton(onClick = { actions.difficulty(id) }, enabled = !state.busy,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    .testTag("difficulty_$id")
                    .semantics { selected = chosen; contentDescription = difficultyName(id) + " difficulty for new games" },
                shape = RoundedCornerShape(11.dp),
                contentPadding = PaddingValues(horizontal = 3.dp, vertical = 9.dp),
                border = BorderStroke(1.dp, if (chosen) GoldTint else OnForest.copy(alpha = .45f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (chosen) OnForest.copy(alpha = .18f) else Color.Transparent,
                    contentColor = OnForest)) {
                Text(difficultyName(id), color = OnForest, fontSize = 12.sp,
                    fontWeight = if (chosen) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}
