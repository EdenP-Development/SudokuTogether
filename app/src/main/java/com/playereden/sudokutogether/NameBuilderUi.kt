package com.playereden.sudokutogether

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.*

/** Isolated builder; persistence and legacy migration are deliberately owned by the caller. */
@Composable
internal fun NameBuilderDialog(
    profile: LocalPlayerData,
    currentName: String,
    onConfirm: (PlayerNameSelection) -> Unit,
    onDismiss: () -> Unit
) {
    val earned = remember(profile) { NameCollection.earnedIds(profile) }
    val initial = remember(currentName, earned) {
        NameCollection.parseCanonical(currentName)?.takeIf { NameCollection.canEquip(it, earned) }
            ?: NameCollection.randomName(earned, true)
    }
    var first by rememberSaveable(currentName) { mutableStateOf(initial.firstId) }
    var middle by rememberSaveable(currentName) { mutableStateOf(initial.middleId) }
    var last by rememberSaveable(currentName) { mutableStateOf(initial.lastId) }
    val selection = PlayerNameSelection(first, middle, last)
    val valid = NameCollection.canEquip(selection, earned)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Build your name") },
        text = {
            Column(Modifier.fillMaxWidth().heightIn(max = 480.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(NameCollection.display(selection) ?: "Choose another combination",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.testTag("name_preview"))
                Text("Your name is used in your profile and nearby games. Earn more words by playing.",
                    style = MaterialTheme.typography.bodySmall)
                Text("Tap a word to choose from your collection, or use the arrows to browse.",
                    style = MaterialTheme.typography.bodySmall, color = Muted)
                NameWordPicker("First word", NameSlot.FIRST, first, earned) { first = it ?: first }
                NameWordPicker("Middle word (optional)", NameSlot.MIDDLE, middle, earned) { middle = it }
                NameWordPicker("Last word", NameSlot.LAST, last, earned) { last = it ?: last }
                OutlinedButton(
                    onClick = {
                        val next = NameCollection.randomName(earned, middle != null)
                        first = next.firstId; middle = next.middleId; last = next.lastId
                    },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("name_randomize")
                ) { Text("Randomize name") }
                Text("${NameCollection.words.count { it.achievementId == null || it.achievementId in earned }} of ${NameCollection.words.size} words collected",
                    style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = { if (valid) onConfirm(selection) }, enabled = valid,
                modifier = Modifier.heightIn(min = 48.dp).testTag("name_confirm")) { Text("Save name") }
        },
        dismissButton = { TextButton(onClick = onDismiss, modifier = Modifier.heightIn(min = 48.dp)) { Text("Cancel") } }
    )
}

@Composable
private fun NameWordPicker(
    label: String, slot: NameSlot, selected: String?, earned: Set<String>, choose: (String?) -> Unit
) {
    val available = remember(slot, earned) { NameCollection.available(slot, earned) }
    var expanded by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableIntStateOf(0) }
    val menuWidth = with(LocalDensity.current) { anchorWidth.toDp() }
    val choices = if (slot == NameSlot.MIDDLE) listOf(null) + available.map { it.id } else available.map { it.id }
    val index = choices.indexOf(selected).coerceAtLeast(0)
    val currentLabel = available.firstOrNull { it.id == selected }?.label ?: "None"
    val tag = slot.name.lowercase()
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedButton(onClick = { choose(choices[(index + choices.size - 1) % choices.size]) },
                contentPadding = PaddingValues(0.dp), modifier = Modifier.size(48.dp).testTag("name_${tag}_previous").semantics { contentDescription = "Previous ${label.lowercase()}" }) {
                Text("‹")
            }
            Box(Modifier.weight(1f).onSizeChanged { anchorWidth = it.width }) {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("name_${tag}_picker")) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Text(currentLabel, modifier = Modifier.weight(1f, fill = false),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Spacer(Modifier.width(4.dp))
                        Text("▾", modifier = Modifier.clearAndSetSemantics { })
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },
                    modifier = Modifier.width(menuWidth).heightIn(max = 320.dp)) {
                    if (slot == NameSlot.MIDDLE) DropdownMenuItem(text = { Text("None", color = Forest) }, trailingIcon = { if (selected == null) Text("✓", color = Forest, modifier = Modifier.clearAndSetSemantics {}) }, onClick = { choose(null); expanded = false },
                        modifier = Modifier.heightIn(min = 48.dp).background(if (selected == null) GreenTint else Paper).testTag("name_middle_option_none").semantics { this.selected = selected == null })
                    available.forEach { word ->
                        DropdownMenuItem(text = { Text(word.label, color = Forest) }, trailingIcon = { if (word.id == selected) Text("✓", color = Forest, modifier = Modifier.clearAndSetSemantics {}) }, onClick = { choose(word.id); expanded = false },
                            modifier = Modifier.heightIn(min = 48.dp).background(if (word.id == selected) GreenTint else Paper).testTag("name_option_${word.id}").semantics { this.selected = word.id == selected })
                    }
                }
            }
            OutlinedButton(onClick = { choose(choices[(index + 1) % choices.size]) },
                contentPadding = PaddingValues(0.dp), modifier = Modifier.size(48.dp).testTag("name_${tag}_next").semantics { contentDescription = "Next ${label.lowercase()}" }) {
                Text("›")
            }
        }
    }
}
