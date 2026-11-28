package com.playereden.sudokutogether

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.HumanSudoku.Technique
import com.playereden.sudokutogether.core.HumanSudoku.Technique.SOLUTION_REVEAL
import com.playereden.sudokutogether.core.SOLO
import com.playereden.sudokutogether.core.SudokuAssistance

/** Only explicit Solo/Daily assistance reaches this dialog. */
@Composable internal fun PuzzleCoachDialog(state: GameUiState, actions: GameActions) {
    if (state.role != SOLO || state.page != Page.PLAY) return
    val hint = state.hint
    val check = state.entryCheck
    if (hint == null && check == null && !state.hintUnavailable) return
    val scroll = rememberScrollState()
    val title = when (check?.kind) {
        SudokuAssistance.CheckKind.INCORRECT_ENTRY -> "Entry to review"
        SudokuAssistance.CheckKind.CONFLICT -> "Repeated number"
        SudokuAssistance.CheckKind.CORRECT_SO_FAR -> "No errors found"
        SudokuAssistance.CheckKind.NO_ENTRIES -> "No entries to check"
        null -> when {
            hint == null -> "Something may be blocking your puzzle"
            hint.steps.first().technique == SOLUTION_REVEAL -> SOLUTION_REVEAL.label
            else -> "Your next deduction"
        }
    }
    AlertDialog(
        onDismissRequest = actions.dismissHint,
        title = { Text(title, color = Forest, fontFamily = FontFamily.Serif) },
        text = {
            Column(Modifier.verticalScroll(scroll), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                when {
                    check != null -> {
                        if (check.cell in 0..80) Text("Row ${check.cell / 9 + 1}, column ${check.cell % 9 + 1}",color=Forest)
                        Text(when (check.kind) {
                            SudokuAssistance.CheckKind.INCORRECT_ENTRY -> "This entry doesn't match the puzzle's solution."
                            SudokuAssistance.CheckKind.CONFLICT -> "This number repeats in its row, column or box. Review the highlighted square."
                            SudokuAssistance.CheckKind.CORRECT_SO_FAR -> "Your entries match the puzzle's solution so far."
                            SudokuAssistance.CheckKind.NO_ENTRIES -> "Place a number first."
                        },color=Ink)
                        val reviewSnapshot = state.snapshot
                        if (check.cell in 0..80 && reviewSnapshot != null &&
                            (check.kind == SudokuAssistance.CheckKind.INCORRECT_ENTRY ||
                                check.kind == SudokuAssistance.CheckKind.CONFLICT)) {
                            BoxWithConstraints(Modifier.fillMaxWidth().testTag("coach_entry_review_board"),
                                contentAlignment = Alignment.Center) {
                                SudokuBoard(reviewSnapshot, selected = -1, partner = -1,
                                    size = maxWidth.coerceAtMost(280.dp), select = null,
                                    boardTheme = state.appearance.board, localStyle = state.appearance,
                                    solo = true, uniformNumbers = state.uniformNumbers,
                                    proofTargets = setOf(check.cell),
                                    proofSources = check.conflicts - check.cell)
                            }
                            Text(if (check.kind == SudokuAssistance.CheckKind.INCORRECT_ENTRY)
                                "The outlined square contains your entry. You can erase it or return to the puzzle to review it yourself."
                                else "The outlined square is the entry to review. Shaded squares repeat that number in the same row, column or box. Other errors may remain.",
                                color = Muted, style = MaterialTheme.typography.bodySmall)
                        }
                        if (check.usesSolution) Text("This puzzle is marked Assisted.",color=Muted)
                    }
                    hint != null -> {
                        CoachProof(state, hint, scroll, actions.select)
                        if (hint.steps.first().technique != SOLUTION_REVEAL) {
                            Text("Hints mark this puzzle Assisted.", color = Muted)
                        }
                    }
                    else -> {
                        Text("An earlier entry may be preventing the Coach from finding a safe deduction. Check my work can help you review it.", color = Ink)
                        Text("Solution checks mark this puzzle Assisted.", color = Muted)
                    }
                }
            }
        },
        confirmButton = {
            val secondaryLabel = when {
                check?.kind == SudokuAssistance.CheckKind.INCORRECT_ENTRY -> "Erase entry"
                check == null -> "Check my work"
                else -> null
            }
            val secondaryEnabled = state.canEdit &&
                (check?.kind != SudokuAssistance.CheckKind.INCORRECT_ENTRY || state.selected == check.cell)
            val secondaryAction: () -> Unit = {
                if (check?.kind == SudokuAssistance.CheckKind.INCORRECT_ENTRY) {
                    actions.dismissHint()
                    actions.erase()
                } else actions.checkWork()
            }
            BoxWithConstraints(Modifier.fillMaxWidth().testTag("coach_footer")) {
                val stacked = maxWidth < 260.dp || LocalDensity.current.fontScale > 1.3f
                if (secondaryLabel == null) {
                    TextButton(onClick = actions.dismissHint,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("coach_back")) {
                        Text("Back to puzzle")
                    }
                } else if (stacked) {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        OutlinedButton(onClick = secondaryAction, enabled = secondaryEnabled,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("coach_secondary")) {
                            Text(secondaryLabel)
                        }
                        TextButton(onClick = actions.dismissHint,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("coach_back")) {
                            Text("Back to puzzle")
                        }
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(onClick = secondaryAction, enabled = secondaryEnabled,
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("coach_secondary"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
                            Text(secondaryLabel, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                        TextButton(onClick = actions.dismissHint,
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("coach_back"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
                            Text("Back to puzzle", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }
            }
        }
    )
}

@Composable private fun CoachProof(
    state: GameUiState, hint: SudokuAssistance.CoachHint, scroll: ScrollState,
    select: (Int) -> Unit
) {
    val snapshot = state.snapshot ?: return
    var index by rememberSaveable(hint) { mutableIntStateOf(0) }
    var explainedSteps by rememberSaveable(hint) { mutableStateOf(intArrayOf()) }
    val explained = index in explainedSteps
    var evidenceSteps by rememberSaveable(hint) { mutableStateOf(intArrayOf()) }
    val evidence = index in evidenceSteps || explained
    var revealed by rememberSaveable(hint) { mutableStateOf(false) }
    LaunchedEffect(index) { scroll.scrollTo(0) }
    val step = hint.steps[index.coerceIn(hint.steps.indices)]
    val reveal = step.technique == SOLUTION_REVEAL
    var showResult by rememberSaveable(hint, index) { mutableStateOf(false) }
    val observation = when (step.technique) {
        Technique.NAKED_SINGLE -> "Which digits are already used in the highlighted square’s row, column and box?"
        Technique.HIDDEN_SINGLE -> "Which digit has only one possible home in a row, column or box?"
        Technique.LOCKED_CANDIDATE -> "Look for a digit whose possible squares are confined to one line or box."
        Technique.NAKED_PAIR -> "Look for two squares in the same group that share just two possible digits."
        Technique.HIDDEN_PAIR -> "Look for two digits that can only fit in the same two squares."
        Technique.X_WING -> "Look for one digit confined to the same two positions in two parallel lines."
        Technique.NAKED_TRIPLE -> "Look for three squares in one row, column or box whose candidates together use only three digits."
        Technique.HIDDEN_TRIPLE -> "Look for three digits that can only appear within the same three squares of one group."
        Technique.NAKED_QUADRUPLE -> "Look for four squares in one row, column or box whose combined candidates contain exactly four digits."
        Technique.HIDDEN_QUADRUPLE -> "Look for four digits that can only appear within the same four squares of one group."
        SOLUTION_REVEAL -> ""
    }
    val targets = step.targetCells.toSet()
    val sources = if (evidence) step.sourceCells.toSet() else emptySet()
    if (hint.steps.size > 1) {
        Text("Step ${index + 1} of ${hint.steps.size}", color = Forest)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextButton(onClick = { index-- }, enabled = index > 0,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Previous step") }
            TextButton(onClick = { index++ }, enabled = explained && index < hint.steps.lastIndex,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Next step") }
        }
        if (index > 0 && !reveal) Text("Earlier deductions are already taken into account in this step.",
            color = Muted, style = MaterialTheme.typography.bodySmall)
    }
    // Keep the board above changing teaching copy so each stage has the same anchor.
    val resultVisible = explained && !reveal && step.eliminations.isNotEmpty() && showResult
    val preview = remember(snapshot, step, evidence, explained, resultVisible) {
        snapshot.copy(cells = snapshot.cells.mapIndexed { cellIndex, cell ->
            val removed = if (explained) step.eliminations[cellIndex].orEmpty() else emptyList()
            val supporting = if (step.eliminations.isNotEmpty() && cellIndex in sources)
                step.proofCandidates[cellIndex].orEmpty().filter { it in step.digits } else emptyList()
            val shown = when {
                resultVisible && cellIndex in targets -> step.proofCandidates[cellIndex].orEmpty().filterNot { it in removed }
                explained -> removed + supporting
                evidence && step.eliminations.isNotEmpty() && cellIndex in (targets + sources) -> step.proofCandidates[cellIndex].orEmpty()
                else -> emptyList()
            }
            cell.copy(notes = shown.fold(0) { notes, digit -> notes or (1 shl (digit - 1)) })
        })
    }
    BoxWithConstraints(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SudokuBoard(preview, selected = -1, partner = -1, size = maxWidth.coerceAtMost(280.dp), select = null,
            boardTheme = state.appearance.board, localStyle = state.appearance, solo = true,
            uniformNumbers = state.uniformNumbers, proofTargets = targets, proofSources = sources,
            proofEliminations = if (explained && !resultVisible) step.eliminations else emptyMap())
    }
    if (reveal) {
        val cell = step.targetCells.single()
        if (index > 0) Text(step.technique.label, color = Forest, style = MaterialTheme.typography.labelLarge)
        if (revealed) {
            Text("Place ${step.digits.single()} in row ${cell / 9 + 1}, column ${cell % 9 + 1}.", color = Ink)
        } else {
            Text(step.explanation, color = Ink)
            if (index == 0) Text("This hint marks the finish Assisted.", color = Muted)
            OutlinedButton(onClick = { revealed = true }, modifier = Modifier.heightIn(min = 48.dp)) {
                Text("Reveal number")
            }
        }
    } else if (explained) {
        Text(step.technique.label, color = Forest, style = MaterialTheme.typography.labelLarge)
        Text(step.explanation, color = Ink)
        if (step.placementExclusions.isNotEmpty()) HiddenSingleEvidence(step, snapshot.cells.map { it.value })
        if (step.digitExclusions.isNotEmpty()) NakedSingleEvidence(step)
    } else {
        val cell = step.targetCells.singleOrNull()
        Text(if (cell == null) "Look at the outlined squares."
            else "Look at row ${cell / 9 + 1}, column ${cell % 9 + 1}.", color = Ink)
        Text(observation, color = Ink, style = MaterialTheme.typography.bodyMedium)
        Text(if (evidence) "Study the shaded evidence and candidate marks. Can you work out the deduction?"
            else "Try spotting the pattern before opening the explanation.", color = Muted,
            style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = {
            if (evidence) explainedSteps = (explainedSteps + index).distinct().toIntArray()
            else evidenceSteps = (evidenceSteps + index).distinct().toIntArray()
        }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(if (evidence) "Show why" else "Show evidence")
        }
    }
    val placementShown = if (reveal) revealed else explained
    if (placementShown && step.eliminations.isEmpty()) {
        OutlinedButton(onClick = { select(step.targetCells.single()) }, modifier = Modifier.heightIn(min = 48.dp)) {
            Text("Go to square")
        }
    }

    if (explained && !reveal) {
        Text(if (step.eliminations.isEmpty()) "Return to your puzzle and enter the number yourself."
            else "Crossed-off pencil marks are no longer possible. These changes are shown in the example only.", color = Ink,
            style = MaterialTheme.typography.bodySmall)
        Text("Outlined squares are the focus. Shaded squares support the deduction.", color = Muted,
            style = MaterialTheme.typography.bodySmall)
        if (step.eliminations.isNotEmpty()) {
            OutlinedButton(onClick = { showResult = !showResult },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("coach_show_result")) {
                Text(if (resultVisible) "Show deduction" else "Show remaining candidates")
            }
            if (resultVisible) Text("After this deduction", color = Forest,
                style = MaterialTheme.typography.titleSmall)
        }
        for ((cell, digits) in step.eliminations) {
            val location = "row ${cell / 9 + 1}, column ${cell % 9 + 1}"
            val before = step.proofCandidates[cell].orEmpty()
            val after = before.filterNot { it in digits }
            Text("Cross off ${digits.joinToString(" and ")} at $location.",
                color = Ink, style = MaterialTheme.typography.bodySmall)
            if (resultVisible) Text("${before.joinToString(", ")} → ${after.joinToString(", ")}",
                color = Forest, style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("coach_result_$cell"))
        }
    }
}

/** A compact, read-only group diagram. Crosses are explanations, never player marks. */
@Composable private fun HiddenSingleEvidence(step: SudokuAssistance.CoachStep, values: List<Int>) {
    val target = step.targetCells.single()
    val digit = step.digits.single()
    val excluded = step.placementExclusions.associateBy { it.cell }
    fun location(cell: Int) = "R${cell / 9 + 1}C${cell % 9 + 1}"
    val group = step.placementGroup
    val lines = if (group.map { it / 9 }.distinct().size == 1) listOf(group)
        else if (group.map { it % 9 }.distinct().size == 1) group.chunked(3)
        else group.chunked(3)
    Text("Why the other squares cannot take $digit", color = Forest,
        style = MaterialTheme.typography.titleSmall)
    Column(Modifier.fillMaxWidth().testTag("coach_hidden_single_group"), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        for (line in lines) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            for (cell in line) {
                val occupied = values[cell] != 0
                Box(Modifier.weight(1f).heightIn(min = 30.dp)
                    .background(if (cell == target) Color(0xFFF0E2B7) else Color(0xFFF1F2EC), RoundedCornerShape(3.dp))
                    .border(1.dp, if (cell == target) Color(0xFFB38A3D) else Color(0xFFBEC8BB), RoundedCornerShape(3.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(if (occupied) values[cell].toString() else if (cell == target) digit.toString() else "×",
                        color = if (occupied || cell == target) Forest else Muted,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
    Text("× = ruled out for $digit; filled squares are already occupied.", color = Muted,
        style = MaterialTheme.typography.bodySmall)
    for (exclusion in step.placementExclusions) {
        val reason = when (exclusion.kind) {
            SudokuAssistance.ExclusionKind.OCCUPIED -> "already contains ${values[exclusion.cell]}"
            SudokuAssistance.ExclusionKind.PEER_DIGIT -> "blocked by $digit at ${location(requireNotNull(exclusion.blocker))}"
            SudokuAssistance.ExclusionKind.EARLIER_DEDUCTION -> "ruled out by an earlier deduction"
        }
        Text("${location(exclusion.cell)}: $reason.", color = Ink,
            style = MaterialTheme.typography.bodySmall, modifier = Modifier.testTag("coach_exclusion_${exclusion.cell}"))
    }
}

/** Explanatory candidate chart, not the player's keypad or pencil marks. */
@Composable private fun NakedSingleEvidence(step: SudokuAssistance.CoachStep) {
    val answer = step.digits.single()
    Text("Why only $answer fits", color = Forest, style = MaterialTheme.typography.titleSmall)
    Column(Modifier.fillMaxWidth().testTag("coach_naked_single_digits"),
        verticalArrangement = Arrangement.spacedBy(3.dp)) {
        for (line in (1..9).chunked(3)) Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            for (digit in line) {
                val retained = digit == answer
                Box(Modifier.weight(1f).heightIn(min = 36.dp)
                    .background(if (retained) Color(0xFFF0E2B7) else Color(0xFFF1F2EC), RoundedCornerShape(3.dp))
                    .border(1.dp, if (retained) Color(0xFFB38A3D) else Color(0xFFBEC8BB), RoundedCornerShape(3.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(if (retained) "$digit ✓" else "$digit ×",
                        color = if (retained) Forest else Muted,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
    for (exclusion in step.digitExclusions) {
        val blocker = exclusion.blocker
        val reason = when {
            blocker != null -> "already appears at R${blocker / 9 + 1}C${blocker % 9 + 1}"
            exclusion.earlierStep != null -> "removed by step ${exclusion.earlierStep}"
            else -> "not a remaining candidate"
        }
        Text("${exclusion.digit}: $reason.", color = Ink,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.testTag("coach_digit_exclusion_${exclusion.digit}"))
    }
}
