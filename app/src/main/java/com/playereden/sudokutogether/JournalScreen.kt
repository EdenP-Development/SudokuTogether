package com.playereden.sudokutogether

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import com.playereden.sudokutogether.core.*

internal enum class ProfileSection(val title: String) {
    OVERVIEW("Profile"), STATISTICS("Statistics"), HISTORY("Game history"), ACHIEVEMENTS("Achievements"), NAMES("Name Collection")
}

@Composable internal fun JournalScreen(
    state: GameUiState, actions: GameActions, tab: JournalTab, section: ProfileSection,
    openSection: (ProfileSection) -> Unit, collection: () -> Unit, help: () -> Unit, share: (() -> Unit)?,
    settingsLocked: Boolean, settingsExtra: @Composable () -> Unit
) {
    key(tab, section) {
        val scroll = rememberScrollState()
        Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column(Modifier.widthIn(max = 520.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                val nested = tab == JournalTab.RECORDS && section != ProfileSection.OVERVIEW
                JournalHeader(if (nested) section.title else tab.title,
                    back = if (nested) ({ openSection(ProfileSection.OVERVIEW) }) else null,
                    backLabel = "Back to Profile")
                if (state.localSaveBlocked) state.saveIssue?.let { Notice(it) }
                when (tab) {
                    JournalTab.RECORDS -> when (section) {
                        ProfileSection.OVERVIEW -> ProfileOverview(state, actions, openSection, collection)
                        ProfileSection.STATISTICS -> StatisticsContent(state)
                        ProfileSection.HISTORY -> HistoryContent(state, actions)
                        ProfileSection.ACHIEVEMENTS -> AchievementsContent(state.profile)
                        ProfileSection.NAMES -> NameAlbumContent(state, actions)
                    }
                    JournalTab.COLLECTION -> CollectionContent(state, actions, scroll)
                    JournalTab.SETTINGS -> SettingsContent(state, actions, help, share, settingsLocked, settingsExtra)
                }
            }
        }
    }
}

@Composable private fun ProfileOverview(
    state: GameUiState, actions: GameActions, openSection: (ProfileSection) -> Unit, collection: () -> Unit
) {
    var draft by rememberSaveable { mutableStateOf<String?>(null) }
    val largeText = LocalDensity.current.fontScale > 1.3f
    val iconSize = if (largeText) 68.dp else 76.dp
    // The actual equipped character is also the shortest route to customization.
    // Reuse the Collection so unlocks, selection and persistence have one owner.
    val portrait: @Composable () -> Unit = {
        Surface(onClick = collection, enabled = !state.busy,
            modifier = Modifier.size(iconSize).testTag("profile_icon_quick_change")
                .semantics { contentDescription = "Change player icon" },
            color = Paper, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, Rule)) {
            Box(Modifier.padding(5.dp), contentAlignment = Alignment.Center) {
                AnimalPortrait(state.appearance, Modifier.fillMaxSize().clearAndSetSemantics {})
            }
        }
    }
    val editName: @Composable () -> Unit = {
        OutlinedIconButton(
            onClick = { draft = state.name },
            enabled = !state.busy,
            modifier = Modifier.size(48.dp).semantics { contentDescription = "Edit name" },
            colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = Paper, contentColor = Forest),
            border = BorderStroke(1.dp, Rule)
        ) {
            GameIcon(GameSymbol.PENCIL, LocalContentColor.current, Modifier.size(20.dp))
        }
    }
    // The player passport is the companion to the illustrated book on Home.
    // Both profile edit and appearance selection still call the existing actions.
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("profile_player_card"),
        color = NightForest,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = .52f)),
        shadowElevation = 3.dp
    ) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Eyebrow("YOUR PLAYER JOURNAL", GoldTint)
            // Reflow without crushing long names or accessibility-sized text.
            if (largeText) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    portrait()
                    editName()
                }
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(state.name.ifBlank { "Player" },
                        style = MaterialTheme.typography.titleLarge, color = OnForest)
                    Text(identityStyleName(state.appearance),
                        style = MaterialTheme.typography.bodySmall,
                        color = OnForest.copy(alpha = .76f))
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    portrait()
                    Column(Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(state.name.ifBlank { "Player" },
                            style = MaterialTheme.typography.titleLarge, color = OnForest)
                        Text(identityStyleName(state.appearance),
                            style = MaterialTheme.typography.bodySmall,
                            color = OnForest.copy(alpha = .76f))
                    }
                    editName()
                }
            }
            HorizontalDivider(color = GoldTint.copy(alpha = .24f))
            val actionColor = if (state.busy) OnForest.copy(alpha = .45f) else OnForest
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(11.dp))
                    .clickable(enabled = !state.busy, role = Role.Button, onClick = collection)
                    .heightIn(min = 54.dp).padding(horizontal = 6.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("Change icon", style = MaterialTheme.typography.labelLarge,
                        color = actionColor)
                    Text(if (state.appearance.portrait.isEmpty() && state.appearance.emblem == "tile")
                        "Try Owl, Fox, Octopus or Honeybee — all ready to use"
                    else "Explore portraits, boards & numbers",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnForest.copy(alpha = .74f))
                }
                GameIcon(GameSymbol.RIGHT, actionColor, Modifier.size(18.dp))
            }
        }
    }

    if (state.nameMigrationNotice) {
        GamePanel(Modifier.fillMaxWidth().testTag("name_migration_notice"), padding = 14.dp) {
            SectionTitle("Your name, your adventure")
            Text("GridMates now lets you build your name using collectible words. We've selected a starter name for you.",
                style = MaterialTheme.typography.bodyMedium, color = Forest)
            Text("Changing your name does not reset your achievements or puzzle progress.",
                style = MaterialTheme.typography.bodySmall, color = Muted)
            OutlinedButton(onClick = { draft = state.name }, enabled = !state.busy && !state.localSaveBlocked,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("name_migration_customize")) {
                Text("Customize my name")
            }
            TextButton(onClick = actions.dismissNameMigrationNotice, enabled = !state.busy && !state.localSaveBlocked,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("name_migration_dismiss")) {
                Text("Dismiss")
            }
        }
    }

    val records = state.profile.records
    val stats = PersonalPlay.stats(state.profile)
    GamePanel(Modifier.fillMaxWidth(), padding = 14.dp) {
        SectionTitle("Your play")
        if (records.isEmpty()) {
            // A useful first-visit state, not a dead stats box. Continue any
            // existing game through Home; never fabricate wins or rewards.
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(46.dp)
                    .background(GreenTint, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center) {
                    GameIcon(GameSymbol.GRID, Forest, Modifier.size(28.dp))
                }
                Column(Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(if (state.resumeSolo) "Your next grid is waiting"
                        else "Your first chapter starts here",
                        style = MaterialTheme.typography.titleSmall, color = Forest)
                    Text("Welcome! Your first puzzle finish will appear here.",
                        style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
            OutlinedButton(onClick = actions.home, enabled = !state.busy,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Forest)) {
                Text("Go to puzzles", color = Forest)
                Spacer(Modifier.width(9.dp))
                GameIcon(GameSymbol.RIGHT, Forest, Modifier.size(18.dp))
            }
        } else {
            val highlights = listOf("Solo solved" to stats.soloWins,
                "Daily finished" to DailyChallenge.completedDays(records),
                "Co-op solved" to stats.coopCompletions, "Versus wins" to stats.versusWins)
            if (LocalDensity.current.fontScale > 1.3f) {
                highlights.forEach { (label, count) ->
                    Text("$label · $count", style = MaterialTheme.typography.bodyMedium, color = Ink)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    highlights.chunked(2).forEach { pair ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pair.forEach { (label, count) ->
                                Column(Modifier.weight(1f)) {
                                    Text(count.toString(), style = MaterialTheme.typography.titleLarge, color = Forest)
                                    Text(label, style = MaterialTheme.typography.bodySmall, color = Muted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val milestones = Achievements.progress(records, state.profile.earnedAchievementIds)
    val earned = milestones.count { it.unlocked }
    // Spotlight an actual saved-record milestone instead of inventing points,
    // daily streaks or a second progression system.
    val upcoming = milestones.filterNot { it.unlocked }.minWithOrNull(
        compareBy<AchievementProgress>(
            { (it.target - it.current).coerceAtLeast(0).toFloat() / it.target.coerceAtLeast(1) },
            { it.target }
        )
    )
    GamePanel(Modifier.fillMaxWidth(), color = GoldTint.copy(alpha = .55f).compositeOver(Cream), padding = 14.dp) {
        SectionTitle("Achievements", "$earned of ${milestones.size} earned")
        if (upcoming != null) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .background(Paper.copy(alpha = .7f)).padding(12.dp)
                .testTag("profile_next_achievement"),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Eyebrow("NEXT MILESTONE")
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GameIcon(GameSymbol.TROPHY, Gold, Modifier.size(20.dp))
                    Text(upcoming.title, style = MaterialTheme.typography.titleSmall, color = Forest,
                        modifier = Modifier.weight(1f))
                }
                // Keep the numeric progress on its own line: it must not
                // squeeze a milestone title into one letter per row.
                Text("${upcoming.current} of ${upcoming.target}",
                    style = MaterialTheme.typography.bodySmall, color = Muted)
                Text(upcoming.description, style = MaterialTheme.typography.bodySmall, color = Muted)
                MilestoneTrack(upcoming.current, upcoming.target)
            }
        } else {
            Text("All achievements earned — nicely done!", style = MaterialTheme.typography.bodyMedium, color = Forest)
        }
        GamePrimaryButton("View achievements", { openSection(ProfileSection.ACHIEVEMENTS) })
    }
    val collectedNameWords = NameCollection.words.count { word ->
        word.achievementId == null || milestones.any { it.id == word.achievementId && it.unlocked }
    }
    ProfileLink("Name Collection", "$collectedNameWords of ${NameCollection.words.size} words collected · Build your name",
        GameSymbol.PENCIL) { openSection(ProfileSection.NAMES) }
    ProfileLink("Statistics", "Best times and puzzle totals", GameSymbol.CLOCK) { openSection(ProfileSection.STATISTICS) }
    ProfileLink("Game history", "Daily puzzles and recent finishes", GameSymbol.CALENDAR) { openSection(ProfileSection.HISTORY) }

    draft?.let { name ->
        NameBuilderDialog(state.profile, name, onConfirm = { selection ->
            NameCollection.display(selection)?.let(actions.name)
            draft = null
        }, onDismiss = { draft = null })
    }
}

@Composable private fun ProfileLink(label: String, detail: String, symbol: GameSymbol, open: () -> Unit) {
    OutlinedCard(onClick = open, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Paper), border = BorderStroke(1.dp, Rule)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            GameIcon(symbol, Forest, Modifier.size(21.dp))
            Column(Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleSmall, color = Forest)
                Text(detail, style = MaterialTheme.typography.bodySmall, color = Muted)
            }
            GameIcon(GameSymbol.RIGHT, Forest, Modifier.size(18.dp))
        }
    }
}

@Composable private fun StatisticsContent(state: GameUiState) {
    val records = state.profile.records
    val stats = PersonalPlay.stats(state.profile)
    val soloSummary = PersonalPlay.soloSummary(records)
    GamePanel(Modifier.fillMaxWidth(), padding = 16.dp) {
        SectionTitle("Solo Sudoku")
        Text("Puzzles solved: ${stats.soloWins}", color = Ink)
        Text("Unassisted ${soloSummary.unassisted} · Assisted ${soloSummary.assisted}", color = Muted, fontSize = 12.sp)
        Text("Best unassisted: " + (soloSummary.bestUnassistedMs?.let(::formatElapsed) ?: "Not yet"), color = Forest, fontSize = 13.sp)
        if (soloSummary.assisted > 0) Text("Best assisted: " +
            (soloSummary.bestAssistedMs?.let(::formatElapsed) ?: "Not yet"), color = Muted, fontSize = 12.sp)
        HorizontalDivider(color = Rule)
        for (tier in listOf("EASY", "MEDIUM", "HARD")) {
            val tierSummary = PersonalPlay.soloSummary(records, tier)
            Text(difficultyName(tier) + " · Unassisted ${tierSummary.unassisted}" +
                " · Best " + (tierSummary.bestUnassistedMs?.let(::formatElapsed) ?: "Not yet"), color = Muted, fontSize = 12.sp)
            if (tierSummary.assisted > 0) Text("Assisted ${tierSummary.assisted} · Best " +
                (tierSummary.bestAssistedMs?.let(::formatElapsed) ?: "Not yet"), color = Muted, fontSize = 12.sp)
        }
        Text("Total solving time: ${formatElapsed(stats.soloTotalMs)}", color = Ink)
    }
    GamePanel(Modifier.fillMaxWidth(), padding = 16.dp) {
        SectionTitle("Daily challenges")
        val daily = records.distinctBy { it.id }.filter { it.mode == DAILY && it.result == "SOLVED" }
        val (assisted, unassisted) = daily.partition { it.assisted }
        Text("Daily challenges: ${DailyChallenge.completedDays(records)} completed", color = Ink)
        Text("Unassisted ${unassisted.size} · Assisted ${assisted.size}", color = Muted, fontSize = 12.sp)
        Text("Daily best unassisted: " + (unassisted.minOfOrNull { it.elapsedMs }?.let(::formatElapsed) ?: "Not yet"),
            color = Forest, fontSize = 13.sp)
        assisted.minOfOrNull { it.elapsedMs }?.let {
            Text("Daily best assisted: ${formatElapsed(it)}", color = Muted, fontSize = 12.sp)
        }
    }
    GamePanel(Modifier.fillMaxWidth(), padding = 16.dp) {
        SectionTitle("Co-op")
        Text("Co-op puzzles completed: ${stats.coopCompletions}", color = Ink)
    }
    GamePanel(Modifier.fillMaxWidth(), padding = 16.dp) {
        SectionTitle("Versus")
        Text("Wins ${stats.versusWins} · Losses ${stats.versusLosses} · Draws ${stats.versusDraws}", color = Ink)
        Text("Head-to-head results are saved only on this phone.", fontSize = 12.sp, color = Muted)
    }
}

@Composable private fun HistoryContent(state: GameUiState, actions: GameActions) {
    DailyHistoryCard(state.profile.dailyProgress, !state.busy && !state.localSaveBlocked, actions.openDaily)
    GamePanel(Modifier.fillMaxWidth(), padding = 16.dp) {
        SectionTitle("Recent finishes")
        val recent = state.profile.records.takeLast(15).reversed()
        if (recent.isEmpty()) Text("No games yet.", fontSize = 14.sp, color = Muted)
        recent.forEach { result ->
            val mode = historyModeLabel(result.mode)
            Text("$mode · ${historyResultLabel(result.result)} · ${difficultyName(result.difficulty)}" +
                (if (result.mode == SOLO || result.mode == DAILY) (if (result.assisted) " · Assisted" else " · Unassisted") else ""),
                fontSize = 13.sp, color = Ink)
            Text(when {
                result.mode == COOP -> "Finished"
                result.mode == VERSUS && result.elapsedMs == 0L -> "Time unavailable"
                else -> formatElapsed(result.elapsedMs)
            }, fontSize = 12.sp, color = Forest)
            if (result.checksUsed > 0) Text("${if (result.checksUsed == 9999) "9999+" else result.checksUsed} entry check" +
                (if (result.checksUsed == 1) "" else "s"), fontSize = 11.sp, color = Muted)
            HorizontalDivider(color = Rule)
        }
    }
}

@Composable private fun AchievementsContent(profile: LocalPlayerData) {
    val milestones = remember(profile.records, profile.earnedAchievementIds) {
        Achievements.progress(profile.records, profile.earnedAchievementIds)
    }
    val locked = milestones.filterNot { it.unlocked }.sortedWith(
        compareBy<AchievementProgress>(
            { (it.target - it.current).coerceAtLeast(0).toFloat() / it.target.coerceAtLeast(1) },
            { it.target }
        )
    )
    val earned = milestones.filter { it.unlocked }
    var allGoals by rememberSaveable { mutableStateOf(false) }
    var showEarned by rememberSaveable { mutableStateOf(false) }
    var selectedMedalId by rememberSaveable { mutableStateOf<String?>(null) }

    SectionTitle("Your achievement album", "${earned.size} of ${milestones.size} earned")
    // The album's single progress line summarizes the whole collection;
    // individual unfinished goals show precise counts below.
    MilestoneTrack(earned.size, milestones.size)
    Text("Every puzzle brings you closer to another medal. What you earn is yours to keep!",
        color = Muted, style = MaterialTheme.typography.bodySmall)
    if (locked.isEmpty()) {
        GamePanel(Modifier.fillMaxWidth(), color = GoldTint.copy(alpha = .45f).compositeOver(Cream)) {
            Text("You've earned every achievement — outstanding!", color = Forest,
                style = MaterialTheme.typography.titleSmall)
        }
    } else {
        SectionTitle("Closest goals", "A little something to work toward")
        locked.take(3).forEach { AchievementMedal(it) }
        if (locked.size > 3) {
            OutlinedButton(
                onClick = { allGoals = !allGoals },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    .testTag("achievements_all_goals_toggle"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Rule)
            ) {
                Text(if (allGoals) "Show closest goals only"
                    else "Explore all ${locked.size} remaining goals", color = Forest)
            }
            if (allGoals) {
                locked.drop(3).groupBy { it.category }.forEach { (category, goals) ->
                    Text(category, style = MaterialTheme.typography.titleMedium, color = Forest,
                        modifier = Modifier.padding(top = 8.dp))
                    // The full list is an index, not another wall of large cards.
                    goals.forEach { AchievementGoalRow(it) }
                }
            }
        }
    }
    SectionTitle("Earned medals", "${earned.size} collected")
    if (earned.isNotEmpty()) {
        OutlinedButton(onClick = { showEarned = !showEarned },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                .testTag("achievements_earned_toggle"),
            shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Rule)) {
            Text(if (showEarned) "Hide earned medals" else "Browse earned medals", color = Forest)
        }
        if (showEarned) earned.groupBy { it.category }.forEach { (category, medals) ->
            Text(category, style = MaterialTheme.typography.titleMedium, color = Forest,
                modifier = Modifier.padding(top = 8.dp))
            EarnedMedalGallery(medals) { selectedMedalId = it.id }
        }
    } else {
        Text("Your first medal is just one Solo puzzle away.", color = Muted)
    }
    earned.firstOrNull { it.id == selectedMedalId }?.let { medal ->
        AlertDialog(onDismissRequest = { selectedMedalId = null },
            title = { Text(medal.title, color = Forest, fontFamily = FontFamily.Serif) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Earned medal · ${medal.category}", color = Forest,
                        style = MaterialTheme.typography.labelLarge)
                    Text(medal.description, color = Ink)
                    Text("This achievement is yours to keep.", color = Muted,
                        style = MaterialTheme.typography.bodySmall)
                    medal.rewardId?.let { reward ->
                        Cosmetics.emblems.firstOrNull { it.id == reward }?.let {
                            Text("Collection reward: ${it.name}", color = Forest)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedMedalId = null }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Close")
                }
            })
    }
}

/** Expanded achievement index: full goal text, compact footprint, exact progress. */
@Composable private fun AchievementGoalRow(progress: AchievementProgress) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("achievement_${progress.id}"),
        shape = RoundedCornerShape(14.dp),
        color = Paper,
        border = BorderStroke(1.dp, Rule.copy(alpha = .7f))
    ) {
        Row(
            Modifier.fillMaxWidth().heightIn(min = 58.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GameIcon(achievementCategoryIcon(progress.category), Forest, Modifier.size(21.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(progress.title, style = MaterialTheme.typography.titleSmall, color = Forest)
                Text(progress.description, style = MaterialTheme.typography.bodySmall, color = Muted)
                // The individual track adds value once a goal is actually underway.
                if (progress.current > 0) MilestoneTrack(progress.current, progress.target)
                progress.rewardId?.let { reward ->
                    val name = Cosmetics.emblems.firstOrNull { it.id == reward }?.name
                    if (name != null) Text("Reward: $name",
                        style = MaterialTheme.typography.labelSmall, color = Forest)
                }
            }
            Text("${progress.current.coerceAtMost(progress.target)} / ${progress.target}",
                style = MaterialTheme.typography.labelSmall, color = Muted)
        }
    }
}

/** Earned achievements become a browsable medal shelf, not empty progress cards. */
@Composable private fun EarnedMedalGallery(medals: List<AchievementProgress>, open: (AchievementProgress) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth().testTag("earned_medal_gallery")) {
        val oneColumn = maxWidth < 300.dp || LocalDensity.current.fontScale > 1.3f
        val columns = if (oneColumn) 1 else 2
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            medals.chunked(columns).forEach { group ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    group.forEach { medal ->
                        Surface(
                            onClick = { open(medal) },
                            modifier = Modifier.weight(1f).testTag("achievement_${medal.id}"),
                            color = GoldTint.copy(alpha = .55f).compositeOver(Cream),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Gold.copy(alpha = .45f))
                        ) {
                            Column(
                                Modifier.fillMaxWidth().heightIn(min = 100.dp)
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                    GameIcon(achievementCategoryIcon(medal.category),
                                        Gold, Modifier.size(24.dp))
                                    Text("EARNED", fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 1.sp, color = Forest)
                                }
                                Text(medal.title, style = MaterialTheme.typography.titleSmall,
                                    color = Forest)
                                medal.rewardId?.let { reward ->
                                    val name = Cosmetics.emblems.firstOrNull { it.id == reward }?.name
                                    if (name != null) Text("Collection: $name",
                                        style = MaterialTheme.typography.labelSmall, color = Forest)
                                }
                            }
                        }
                    }
                    repeat(columns - group.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

/** Distinct, existing in-house line symbols; no new mixed-style emoji set. */
internal fun achievementCategoryIcon(category: String): GameSymbol = when (category) {
    "Solo" -> GameSymbol.GRID
    "Daily" -> GameSymbol.CALENDAR
    "Difficulty" -> GameSymbol.TROPHY
    "Clean solves" -> GameSymbol.CHECK
    "Together" -> GameSymbol.PEOPLE
    "Versus" -> GameSymbol.TROPHY
    else -> GameSymbol.TROPHY
}

/** Compact reusable medal; no full-height identical trophy wall by default. */
@Composable private fun AchievementMedal(progress: AchievementProgress) {
    GamePanel(Modifier.fillMaxWidth().testTag("achievement_${progress.id}"),
        color = if (progress.unlocked) GoldTint.copy(alpha = .45f).compositeOver(Cream) else Paper,
        padding = 10.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            GameIcon(achievementCategoryIcon(progress.category),
                if (progress.unlocked) Gold else Forest, Modifier.size(23.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(progress.title, style = MaterialTheme.typography.titleSmall, color = Forest)
                Text(progress.description, style = MaterialTheme.typography.bodySmall, color = Muted)
            }
        }
        Text(if (progress.unlocked) "Earned" else "${progress.current.coerceAtMost(progress.target)} / ${progress.target}",
            color = if (progress.unlocked) Forest else Muted,
            style = MaterialTheme.typography.bodySmall)
        MilestoneTrack(progress.current, progress.target)
        progress.rewardId?.let { reward ->
            val name = Cosmetics.emblems.firstOrNull { it.id == reward }?.name
            if (name != null) Text("Matching Collection reward: $name",
                style = MaterialTheme.typography.labelSmall, color = Forest)
        }
    }
}

@Composable private fun SettingsContent(state: GameUiState, actions: GameActions, help: () -> Unit, share: (() -> Unit)?,
    locked: Boolean, extra: @Composable () -> Unit) {
    var aboutOpen by rememberSaveable { mutableStateOf(false) }
    val enabled = !state.busy && !locked
    GamePanel(Modifier.fillMaxWidth()) {
        SectionTitle("Comfort & display")
        SettingToggle("Classic number display", "Use the Clear font for entries and notes, and the default entry color.",
            state.uniformNumbers, actions.uniformNumbers, enabled)
        HorizontalDivider(color = Rule)
        SettingToggle("Touch feedback", "Board and keypad vibration.", state.touchFeedback, actions.touchFeedback, enabled)
        HorizontalDivider(color = Rule)
        SettingToggle("Puzzle sounds", "Hear sounds when selecting squares, entering numbers, erasing and finishing puzzles.",
            state.soundEffects, actions.soundEffects, enabled)
        PuzzleSoundSettings(state.soundEffects && enabled)
    }
    GamePanel(Modifier.fillMaxWidth()) {
        SectionTitle("Solo assistance")
        SettingToggle("Clean pencil marks", "Remove matching notes from nearby cells.", state.autoCleanNotes, actions.autoCleanNotes, enabled)
        Text("Marks puzzles Assisted. Unassisted best times stay separate.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
    }
    extra()
    GamePanel(Modifier.fillMaxWidth()) {
        SectionTitle("Help")
        OutlinedButton(help, enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(13.dp)) {
            GameIcon(GameSymbol.HELP, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("How to play Sudoku")
        }
        OutlinedButton(onClick = { aboutOpen = true }, enabled = enabled,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp), shape = RoundedCornerShape(13.dp)) {
            Text("About GridMates")
        }
    }
    if (aboutOpen) AboutGame({ aboutOpen = false }, share)
}

@Composable private fun SettingToggle(title: String, detail: String, checked: Boolean, change: (Boolean) -> Unit, enabled: Boolean) {
    Row(Modifier.fillMaxWidth().heightIn(min = 64.dp).toggleable(checked, enabled = enabled, onValueChange = change, role = Role.Switch)
        .padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Forest, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 19.sp)
            Text(detail, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
        }
        Switch(checked, onCheckedChange = null, enabled = enabled)
    }
}


@Composable private fun PuzzleSoundSettings(enabled: Boolean) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = androidx.compose.runtime.remember(context) { context.getSharedPreferences("gridmates_audio_effects", 0) }
    var volume by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(readEffectsVolume(preferences)) }
    val bank = androidx.compose.runtime.remember(context, enabled) {
        if (enabled) runCatching { PuzzleSoundBank(context) }.getOrNull() else null
    }
    androidx.compose.runtime.DisposableEffect(bank) { onDispose { bank?.release() } }
    Text("Puzzle sound volume: ${(volume * 100).toInt()}%", color = Forest)
    Slider(value = volume, onValueChange = {
        volume = normalizeMusicVolume(it)
        preferences.edit().putFloat("volume", volume).apply()
    }, enabled = enabled)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = { playPuzzleClick { bank?.play(PuzzleCue.NUMBER) } },
            enabled = enabled && bank != null, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Test number") }
        OutlinedButton(onClick = { playPuzzleClick { bank?.play(PuzzleCue.ERASE) } },
            enabled = enabled && bank != null, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Test erase") }
    }
}
