package com.playereden.sudokutogether

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import com.playereden.sudokutogether.core.DailyActivity
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

@Composable internal fun HomeScreen(
    state: GameUiState, actions: GameActions,
    fresh: () -> Unit, freshSolo: () -> Unit, freshRace: () -> Unit,
    help: () -> Unit, profile: () -> Unit,
    markStyle: GridMatesMarkStyle = GridMatesMarkStyle.SUDOKU_GRID
) {
    var mode by rememberSaveable { mutableStateOf("solo") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            HomeHeader(state, help, profile, markStyle)
            (state.message ?: state.saveIssue)?.let { Notice(it) }
            HomeGameBanner(state.resumeSolo && state.profile.activeSolo?.completed == false)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                listOf(Triple("solo", "Solo", GameSymbol.PERSON), Triple("together", "Co-op", GameSymbol.PEOPLE),
                    Triple("versus", "Versus", GameSymbol.TROPHY)).forEach { (id, label, symbol) ->
                    val chosen = mode == id
                    val fill by animateColorAsState(if (chosen) Forest else Paper, label = "mode color")
                    Surface(onClick = { mode = id }, modifier = Modifier.weight(1f).heightIn(min = 80.dp)
                        .testTag("home_mode_$id").semantics { selected = chosen }, shape = RoundedCornerShape(18.dp),
                        color = fill, border = BorderStroke(1.dp, if (chosen) Forest else Rule),
                        shadowElevation = if (chosen) 3.dp else 0.dp) {
                        Column(Modifier.padding(horizontal = 4.dp, vertical = 9.dp),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(Modifier.size(34.dp).background(
                                if (chosen) OnForest.copy(alpha = .15f) else GreenTint, CircleShape),
                                contentAlignment = Alignment.Center) {
                                GameIcon(symbol, if (chosen) OnForest else Forest, Modifier.size(21.dp))
                            }
                            Text(label, color = if (chosen) OnForest else Forest, fontSize = 13.sp,
                                lineHeight = 17.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
            val active = state.profile.activeSolo
            val savedSolo = active?.takeIf { mode == "solo" && state.resumeSolo && !it.completed }
            val unfinished = savedSolo != null
            Surface(shape = RoundedCornerShape(25.dp), color = NightForest,
                border = BorderStroke(1.dp, Forest), shadowElevation = 2.dp) {
                Column(Modifier.fillMaxWidth().padding(17.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (mode == "solo") "A MOMENT TO YOURSELF"
                            else if (mode == "together") "A PUZZLE TO SHARE" else "A FRIENDLY CHALLENGE",
                            color = GoldTint, fontSize = 10.sp, lineHeight = 14.sp,
                            letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f))
                        GameIcon(when (mode) {
                            "together" -> GameSymbol.PEOPLE
                            "versus" -> GameSymbol.TROPHY
                            else -> GameSymbol.GRID
                        }, OnForest.copy(alpha = .75f), Modifier.size(23.dp))
                    }
                    Text(when {
                        unfinished -> "Your puzzle"
                        mode == "together" -> "Shared board"
                        mode == "versus" -> "Race to finish"
                        else -> "Classic 9×9"
                    }, color = OnForest, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold,
                        fontSize = 27.sp, lineHeight = 31.sp)
                    if (savedSolo != null) {
                        val fill = savedSolo.puzzle.fillProgress(savedSolo.cells)
                        Text("${difficultyName(savedSolo.puzzle.difficulty)} · ${fill.label} · " +
                            formatElapsed(savedSolo.elapsedMs),
                            color = OnForest.copy(alpha = .8f), style = MaterialTheme.typography.bodyMedium)
                        GamePrimaryButton("Continue", { actions.solo(true) },
                            !state.busy && !state.localSaveBlocked, light = true)
                        Text("Start another puzzle", color = OnForest.copy(alpha = .8f),
                            style = MaterialTheme.typography.bodyMedium)
                    } else Text(when (mode) {
                        "together" -> "Solve the same board · Same Wi-Fi"
                        "versus" -> "Race on separate boards · Same Wi-Fi"
                        else -> "A classic grid, at your own pace."
                    }, color = OnForest.copy(alpha = .8f), style = MaterialTheme.typography.bodyMedium)
                    DifficultyPicker(state, actions)
                    if (mode != "solo") Row(Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        .toggleable(state.requireRoomCode, enabled = !state.busy, role = Role.Switch, onValueChange = actions.requireRoomCode)
                        .semantics { contentDescription = "Require a code for a new game" }.testTag("require_code"),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Require a code", color = OnForest, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Switch(state.requireRoomCode, onCheckedChange = null, enabled = !state.busy,
                            colors = SwitchDefaults.colors(checkedThumbColor = Forest, checkedTrackColor = OnForest,
                                uncheckedThumbColor = OnForest, uncheckedTrackColor = NightForest, uncheckedBorderColor = OnForest.copy(alpha = .6f)))
                    }
                    when (mode) {
                        "solo" -> {
                            if (unfinished) OutlinedButton(freshSolo, enabled = !state.busy && !state.localSaveBlocked,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, OnForest.copy(alpha = .4f))) { Text("New puzzle", color = OnForest) }
                            else GamePrimaryButton("New puzzle", freshSolo, !state.busy && !state.localSaveBlocked, light = true)
                            if (active?.completed == true && state.resumeSolo) TextButton(onClick = { actions.solo(true) },
                                enabled = !state.busy && !state.localSaveBlocked, modifier = Modifier.fillMaxWidth()) {
                                Text("Review last puzzle", color = OnForest)
                            }
                        }
                        "together" -> {
                            if (state.resumeHost) GamePrimaryButton("Resume room", { actions.host(true) }, !state.busy, light = true)
                            GamePrimaryButton("Start a game", fresh, !state.busy, light = true)
                            OutlinedButton(onClick = { actions.showJoin(false) }, enabled = !state.busy,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, OnForest.copy(alpha = .4f))) { Text("Join a game", color = OnForest) }
                            if (state.resumeGuest) TextButton(onClick = { actions.showJoin(true) }, enabled = !state.busy,
                                modifier = Modifier.fillMaxWidth()) { Text("Rejoin room", color = OnForest) }
                        }
                        else -> {
                            if (state.resumeRace) GamePrimaryButton("Resume race", { actions.hostRace(true) }, !state.busy, light = true)
                            GamePrimaryButton("Start a race", freshRace, !state.busy, light = true)
                            OutlinedButton(onClick = { actions.showJoin(false) }, enabled = !state.busy,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, OnForest.copy(alpha = .4f))) { Text("Join a game", color = OnForest) }
                        }
                    }
                    if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth(), color = OnForest, trackColor = OnForest.copy(alpha = .15f))
                }
            }
            val today = state.profile.dailyProgress.firstOrNull { it.date == state.todayDate }?.solo
            val recentDays = remember(state.todayDate, state.profile.records, state.profile.dailyProgress) {
                DailyActivity.recentSevenDays(state.todayDate, state.profile.records, state.profile.dailyProgress)
            }
            val finishedDays = recentDays.count { it.complete }
            val currentDayDone = recentDays.lastOrNull()?.complete == true
            Surface(onClick = actions.daily, enabled = !state.busy && !state.localSaveBlocked,
                modifier = Modifier.fillMaxWidth().heightIn(min = 68.dp).testTag("daily_challenge"),
                shape = RoundedCornerShape(19.dp), color = GoldTint.copy(alpha = .65f), border = BorderStroke(1.dp, Rule)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(38.dp).background(Paper, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            GameIcon(GameSymbol.CALENDAR, Gold)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Daily puzzle", color = Forest, fontSize = 15.sp, lineHeight = 19.sp, fontWeight = FontWeight.SemiBold)
                            Text(when { currentDayDone -> "Completed"; today != null -> "In progress"; else -> "Ready" },
                                color = Forest, fontSize = 11.sp, lineHeight = 15.sp)
                            Text(state.todayDate + " · Easy", color = Muted, fontSize = 10.sp, lineHeight = 14.sp)
                        }
                        GameIcon(if (currentDayDone) GameSymbol.CHECK else GameSymbol.RIGHT, modifier = Modifier.size(19.dp))
                    }
                    HorizontalDivider(color = Gold.copy(alpha = .2f))
                    Text("${finishedDays} of the last 7 days completed", color = Forest,
                        style = MaterialTheme.typography.bodySmall, modifier = Modifier.testTag("daily_seven_day_summary"))
                    if (LocalDensity.current.fontScale <= 1.3f) {
                        Row(Modifier.fillMaxWidth().testTag("daily_seven_day_history"),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            recentDays.forEach { day ->
                                Column(Modifier.weight(1f)
                                    .clearAndSetSemantics {
                                        contentDescription = "${day.date}: " +
                                            (if (day.complete) "completed" else if (day.inProgress) "in progress" else "not completed")
                                    }, horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(day.weekday, fontSize = 10.sp, lineHeight = 14.sp, color = Muted)
                                    Box(Modifier.size(25.dp).background(
                                        if (day.complete) Forest else if (day.inProgress) AmberLight else Paper, CircleShape),
                                        contentAlignment = Alignment.Center) {
                                        if (day.complete) GameIcon(GameSymbol.CHECK, OnForest, Modifier.size(14.dp))
                                        else if (day.inProgress) Text("·", color = Forest, fontSize = 18.sp)
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

@Composable private fun HomeHeader(state: GameUiState, help: () -> Unit, profile: () -> Unit,
    markStyle: GridMatesMarkStyle) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val reflow = LocalDensity.current.fontScale > 1.3f || maxWidth < 280.dp
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GridMatesMark(Modifier.size(40.dp), markStyle)
                Column(Modifier.weight(1f)) {
                    Text("GridMates", color = Forest, fontSize = 20.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Sudoku", color = Muted, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.sp)
                }
                if (!reflow) HomeHeaderActions(state, help, profile)
            }
            if (reflow) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                HomeHeaderActions(state, help, profile)
            }
        }
    }
}

@Composable private fun HomeHeaderActions(state: GameUiState, help: () -> Unit, profile: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IconButton(help, modifier = Modifier.size(48.dp).semantics { contentDescription = "How to play Sudoku" }) {
            GameIcon(GameSymbol.HELP, modifier = Modifier.size(22.dp))
        }
        IconButton(profile, enabled = !state.busy,
            modifier = Modifier.size(48.dp).background(GreenTint, RoundedCornerShape(16.dp))
                .semantics { contentDescription = "My profile & records" }) {
            AnimalPortrait(state.appearance, Modifier.size(40.dp))
        }
    }
}

@Composable private fun DifficultyPicker(state: GameUiState, actions: GameActions) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        listOf("EASY", "MEDIUM", "HARD").forEach { id ->
            val chosen = state.selectedDifficulty == id
            OutlinedButton(onClick = { actions.difficulty(id) }, enabled = !state.busy,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("difficulty_$id").semantics { selected = chosen; contentDescription = difficultyName(id) + " difficulty for new games" },
                shape = RoundedCornerShape(11.dp), contentPadding = PaddingValues(horizontal = 3.dp, vertical = 10.dp),
                border = BorderStroke(1.dp, if (chosen) OnForest else OnForest.copy(alpha = .3f)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (chosen) OnForest.copy(alpha = .16f) else Color.Transparent,
                    contentColor = OnForest, disabledContentColor = OnForest.copy(alpha = .5f))) {
                Text(difficultyName(id), fontSize = 12.sp, fontWeight = if (chosen) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}
