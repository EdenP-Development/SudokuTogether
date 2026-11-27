package com.playereden.sudokutogether

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.*

private enum class NameAlbumFilter { ALL, AVAILABLE, LOCKED }

@Composable internal fun NameAlbumContent(state: GameUiState, actions: GameActions) {
    val achievements = remember(state.profile.records, state.profile.earnedAchievementIds) {
        Achievements.progress(state.profile.records, state.profile.earnedAchievementIds).associateBy { it.id }
    }
    val earned = achievements.values.filter { it.unlocked }.map { it.id }.toSet()
    var slot by rememberSaveable { mutableStateOf(NameSlot.FIRST) }
    var filter by rememberSaveable { mutableStateOf(NameAlbumFilter.ALL) }
    var builder by rememberSaveable { mutableStateOf(false) }
    val available = NameCollection.available(slot, earned)
    val owned = NameCollection.words.count { it.achievementId == null || it.achievementId in earned }
    val visible = NameCollection.words.filter { word ->
        word.slot == slot && when (filter) {
            NameAlbumFilter.ALL -> true
            NameAlbumFilter.AVAILABLE -> word in available
            NameAlbumFilter.LOCKED -> word !in available
        }
    }
    GamePanel(Modifier.fillMaxWidth().testTag("name_album_summary")) {
        Text("Your name collection", style = MaterialTheme.typography.titleLarge, color = Forest)
        Text(state.name, style = MaterialTheme.typography.headlineSmall, color = Forest)
        Text("$owned of ${NameCollection.words.size} words collected", color = Muted)
        MilestoneTrack(owned, NameCollection.words.size)
        Text("Earn new words through achievements. Collected words stay available and never change puzzle difficulty.",
            style = MaterialTheme.typography.bodySmall, color = Muted)
        GamePrimaryButton("Build your name", { builder = true }, !state.busy)
    }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val stacked = maxWidth < 300.dp || LocalDensity.current.fontScale > 1.3f
        val categories = listOf(NameSlot.FIRST to "First", NameSlot.MIDDLE to "Middle", NameSlot.LAST to "Last")
        val filters = listOf(NameAlbumFilter.ALL to "All words", NameAlbumFilter.AVAILABLE to "Collected", NameAlbumFilter.LOCKED to "Locked")
        @Composable fun Category(item: NameSlot, label: String, modifier: Modifier) {
            FilterChip(selected = slot == item, onClick = { slot = item },
                label = { Text("$label words") },
                modifier = modifier.heightIn(min = 48.dp).testTag("name_album_slot_${item.name.lowercase()}"))
        }
        @Composable fun Filter(item: NameAlbumFilter, label: String, modifier: Modifier) {
            FilterChip(selected = filter == item, onClick = { filter = item }, label = { Text(label) },
                modifier = modifier.heightIn(min = 48.dp).testTag("name_album_filter_${item.name.lowercase()}"))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Word category", style = MaterialTheme.typography.labelLarge)
            if (stacked) {
                categories.forEach { (item, label) -> Category(item, label, Modifier.fillMaxWidth()) }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { (item, label) -> Category(item, label, Modifier.weight(1f)) }
                }
            }
            Text("${available.size} of ${NameCollection.words.count { it.slot == slot }} available", color = Muted)
            Text("Show", style = MaterialTheme.typography.labelLarge)
            if (stacked) {
                filters.forEach { (item, label) -> Filter(item, label, Modifier.fillMaxWidth()) }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    filters.forEach { (item, label) -> Filter(item, label, Modifier.weight(1f)) }
                }
            }
        }
    }
    if (visible.isEmpty()) Text("No words in this view.", color = Muted)
    for (word in visible) {
        val goal = word.achievementId?.let(achievements::get)
        val unlocked = word in available
        GamePanel(Modifier.fillMaxWidth().testTag("name_album_word_${word.id}"), padding = 10.dp) {
            Text(word.label, style = MaterialTheme.typography.titleMedium, color = Forest)
            Text(if (unlocked) (if (goal == null) "Starter word" else "Collected · ${goal.title}") else "Locked · ${goal?.title.orEmpty()}",
                style = MaterialTheme.typography.bodySmall, color = Muted)
            if (!unlocked && goal != null) {
                Text(goal.description, style = MaterialTheme.typography.bodySmall)
                Text("${goal.current.coerceIn(0, goal.target)} / ${goal.target}", style = MaterialTheme.typography.bodySmall, color = Muted)
                MilestoneTrack(goal.current.coerceIn(0, goal.target), goal.target)
            }
        }
    }
    if (builder) NameBuilderDialog(state.profile, state.name, onConfirm = { selection ->
        NameCollection.display(selection)?.let(actions.name)
        builder = false
    }, onDismiss = { builder = false })
}
