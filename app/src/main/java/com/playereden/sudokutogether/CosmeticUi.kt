package com.playereden.sudokutogether

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playereden.sudokutogether.core.*
import kotlinx.coroutines.launch

fun emblemSymbol(id: String): String = when (id) {
    "leaf" -> "✿"
    "duo" -> "◈"
    "flash" -> "ϟ"
    "star" -> "★"
    "laurel" -> "♛"
    "constellation" -> "✦"
    "sunrise" -> "☀"
    "focus" -> "◉"
    "compass" -> "✧"
    "moonlight" -> "☾"
    "grid_master" -> "❖"
    else -> "◆"
}

/** Two Sudoku glyphs, with visible spacing so a sample is not misread as fifty-nine. */
internal const val NUMBER_PREVIEW_SAMPLE = "5 9"

/** Never let one-word icon names break into orphan letters on phone galleries. */
internal fun collectionNameIsLongSingleWord(name: String): Boolean =
    name.length >= 9 && name.none { it.isWhitespace() }

/** Font sizes tested against three-column album widths, keeping whole words visible. */
internal fun collectionIconLabelSize(name: String): Int = when {
    name.any { it.isWhitespace() } -> 14
    name.length >= 12 -> 11
    name.length >= 9 -> 12
    else -> 14
}

fun emblemName(id: String): String = Cosmetics.emblems.firstOrNull { it.id == id }?.name ?: "Gridkeeper"

fun styleLabel(a: Appearance): String = listOf(
    Cosmetics.inks.firstOrNull { it.id == a.ink }?.name ?: "Forest",
    Cosmetics.boards.firstOrNull { it.id == a.board }?.name ?: "Classic",
    Cosmetics.fonts.firstOrNull { it.id == a.font }?.name ?: "Clear"
).joinToString(" · ")

private enum class CollectionCategory(val label: String) { ICONS("Player icons"), BOARDS("Boards"), NUMBERS("Numbers") }
private enum class IconFamily(val label: String) { ALL("All"), BASIC("Basic"), WOODLAND("Woodland"), OCEAN("Ocean"), GARDEN("Garden"), FARM("Farm") }
private enum class OwnershipFilter(val label: String) { ALL("All items"), AVAILABLE("Available"), LOCKED("Locked") }
private enum class NumberCategory(val label: String) { COLORS("Number colors"), FONTS("Number fonts") }

private enum class CosmeticGroup(val key: String, val label: String, val useLabel: String) {
    EMBLEM("emblem", "Player icon", "Use icon"),
    PORTRAIT("portrait", "Player icon", "Use icon"),
    BOARD("board", "Board style", "Use board"),
    INK("ink", "Number color", "Use color"),
    FONT("font", "Number font", "Use font");

    fun withItem(look: Appearance, id: String): Appearance = when (this) {
        EMBLEM -> look.copy(emblem = id, portrait = "")
        PORTRAIT -> look.copy(portrait = id)
        BOARD -> look.copy(board = id)
        INK -> look.copy(ink = id)
        FONT -> look.copy(font = id)
    }
}

private data class CollectionItem(val cosmetic: Cosmetic, val group: CosmeticGroup, val family: IconFamily? = null) {
    val id: String get() = cosmetic.id
    val tag: String get() = if (group == CosmeticGroup.PORTRAIT) "portrait_$id" else "cosmetic_${group.key}_$id"
    val previewLabel: String get() = family?.let { "${it.label} player icon" } ?: group.label
    fun isEquipped(look: Appearance): Boolean = group.withItem(look, id) == look
}

private val iconItems = Cosmetics.emblems.map { CollectionItem(it, CosmeticGroup.EMBLEM, IconFamily.BASIC) } +
    Cosmetics.woodlandPortraits.map { CollectionItem(it, CosmeticGroup.PORTRAIT, IconFamily.WOODLAND) } +
    Cosmetics.oceanPortraits.map { CollectionItem(it, CosmeticGroup.PORTRAIT, IconFamily.OCEAN) } +
    Cosmetics.gardenPortraits.map { CollectionItem(it, CosmeticGroup.PORTRAIT, IconFamily.GARDEN) } +
    Cosmetics.farmPortraits.map { CollectionItem(it, CosmeticGroup.PORTRAIT, IconFamily.FARM) }
private val boardItems = Cosmetics.boards.map { CollectionItem(it, CosmeticGroup.BOARD) }
private val inkItems = Cosmetics.inks.map { CollectionItem(it, CosmeticGroup.INK) }
private val fontItems = Cosmetics.fonts.map { CollectionItem(it, CosmeticGroup.FONT) }
private val allCollectionItems = iconItems + boardItems + inkItems + fontItems

@Composable internal fun CollectionContent(state: GameUiState, actions: GameActions, scroll: ScrollState) {
    var category by rememberSaveable { mutableStateOf(CollectionCategory.ICONS) }
    var family by rememberSaveable { mutableStateOf(IconFamily.ALL) }
    var numbers by rememberSaveable { mutableStateOf(NumberCategory.COLORS) }
    var ownership by rememberSaveable { mutableStateOf(OwnershipFilter.ALL) }
    var stylePreviewOpen by rememberSaveable { mutableStateOf(false) }
    var previewId by rememberSaveable { mutableStateOf<String?>(null) }
    val familyScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val records = state.profile.records
    // Compute durable milestone totals once per profile, not once per
    // portrait/board/ink/font. Keep rendering smooth as collections expand.
    val progress = remember(records, state.profile.ownedCosmeticIds) {
        Cosmetics.progressAll(records, state.profile.ownedCosmeticIds)
    }
    val look = state.appearance
    val open: (CollectionItem) -> Unit = { previewId = it.id }

    CollectionChoices(CollectionCategory.entries, category, { it.label }, Role.Tab) { category = it }
    // The same full-style dialog is reachable from every tab, but its entry
    // point should show the style component a player is currently browsing.
    val displayedNumbers = look.withNumberDisplay(state.uniformNumbers)
    val previewTitle = when (category) {
        CollectionCategory.ICONS -> identityStyleName(look)
        CollectionCategory.BOARDS -> (Cosmetics.boards.firstOrNull { it.id == look.board }?.name ?: "Classic") + " · Board"
        CollectionCategory.NUMBERS -> {
            val inkName = Cosmetics.inks.firstOrNull { it.id == displayedNumbers.ink }?.name ?: "Forest"
            val fontName = Cosmetics.fonts.firstOrNull { it.id == displayedNumbers.font }?.name ?: "Clear"
            "$inkName · $fontName"
        }
    }
    val previewSubtitle = when (category) {
        CollectionCategory.ICONS -> "Preview your style"
        CollectionCategory.BOARDS -> "Preview your current board"
        CollectionCategory.NUMBERS -> if (state.uniformNumbers)
            "Classic number display is on" else "Preview your current numbers"
    }
    // Treat the equipped look as a small showcase rather than another item
    // in the unlock catalog. Its preview continues to reflect the active tab.
    val showcaseSize = if (LocalDensity.current.fontScale > 1.3f) 48.dp else 62.dp
    Surface(onClick = { stylePreviewOpen = true }, modifier = Modifier.fillMaxWidth()
        .testTag("collection_current_style_preview")
        .semantics { contentDescription = "Preview equipped style" },
        color = GreenTint, shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Gold.copy(alpha = .28f)), shadowElevation = 1.dp) {
        Row(Modifier.heightIn(min = 82.dp).padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            when (category) {
                CollectionCategory.ICONS -> AnimalPortrait(look, Modifier.size(showcaseSize).clearAndSetSemantics {})
                CollectionCategory.BOARDS -> StyleSampleBoard(look, displayedNumbers,
                    Modifier.size(showcaseSize).clearAndSetSemantics {}, compact = true)
                CollectionCategory.NUMBERS -> Box(
                    Modifier.size(showcaseSize).clip(RoundedCornerShape(12.dp))
                        .background(Cream).clearAndSetSemantics {},
                    contentAlignment = Alignment.Center
                ) {
                    Text(NUMBER_PREVIEW_SAMPLE, fontFamily = fontFor(displayedNumbers.font),
                        color = inkFor(displayedNumbers.ink, false), fontSize = 22.sp)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text("EQUIPPED", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
                Text(previewTitle, style = MaterialTheme.typography.titleSmall, color = Forest)
                Text(previewSubtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
            }
            GameIcon(GameSymbol.RIGHT, Forest, Modifier.size(18.dp))
        }
    }
    val items = when (category) {
        CollectionCategory.ICONS -> {
            CollectionChoices(IconFamily.entries, family, { it.label }, Role.RadioButton,
                horizontalScrollState = familyScroll) { family = it }
            iconItems.filter { family == IconFamily.ALL || it.family == family }
        }
        CollectionCategory.BOARDS -> boardItems
        CollectionCategory.NUMBERS -> {
            CollectionChoices(NumberCategory.entries, numbers, { it.label }, Role.Tab) { numbers = it }
            if (state.uniformNumbers) {
                Text("Classic number display is on. You can still preview colors and fonts here.",
                    style = MaterialTheme.typography.bodySmall, color = Muted)
            }
            if (numbers == NumberCategory.COLORS) inkItems else fontItems
        }
    }
    val (available, locked) = items.partition { it.isEquipped(look) || progress.getValue(it.id).unlocked }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Eyebrow("YOUR ALBUM", Gold)
        SectionTitle("${available.size} of ${items.size} available")
        MilestoneTrack(available.size, items.size)
    }
    if (locked.isNotEmpty()) {
        // A compact next-goal spotlight, not a second wall of duplicated cards.
        val next = locked.minByOrNull {
            val goal = progress.getValue(it.id)
            minOf(
                (goal.target - goal.current).coerceAtLeast(0),
                if (goal.alternateTarget > 0) (goal.alternateTarget - goal.alternateCurrent).coerceAtLeast(0)
                else Int.MAX_VALUE
            )
        }
        if (next != null) {
            val goal = progress.getValue(next.id)
            Surface(onClick = { open(next) }, enabled = !state.busy,
                modifier = Modifier.fillMaxWidth().testTag("collection_next_unlock"),
                color = GoldTint.copy(alpha = .45f).compositeOver(Cream),
                shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, Rule)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CosmeticSwatch(next, next.group.withItem(look, next.id), Modifier.size(48.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Eyebrow("NEXT TO UNLOCK")
                        Text(next.cosmetic.name, style = MaterialTheme.typography.titleSmall, color = Forest)
                        Text(next.cosmetic.requirement,
                            style = MaterialTheme.typography.bodySmall, color = Muted)
                        Text("${goal.current} of ${goal.target} completed",
                            style = MaterialTheme.typography.labelSmall, color = Forest)
                        MilestoneTrack(goal.current, goal.target)
                        if (goal.alternateTarget > 0) {
                            Text("Challenge: ${goal.alternateCurrent} of ${goal.alternateTarget}",
                                style = MaterialTheme.typography.labelSmall, color = Muted)
                            MilestoneTrack(goal.alternateCurrent, goal.alternateTarget)
                        }
                    }
                    GameIcon(GameSymbol.RIGHT, Forest, Modifier.size(18.dp))
                }
            }
        }
    }
    CollectionChoices(OwnershipFilter.entries, ownership, { it.label }, Role.RadioButton,
        tagPrefix = "collection_ownership") { ownership = it }
    val visibleItems = when (ownership) {
        OwnershipFilter.ALL -> items
        OwnershipFilter.AVAILABLE -> available
        OwnershipFilter.LOCKED -> locked
    }
    if (ownership != OwnershipFilter.ALL) {
        Text("Showing ${visibleItems.size} of ${items.size} items", color = Muted,
            style = MaterialTheme.typography.bodySmall, modifier = Modifier.testTag("collection_filtered_count"))
    }
    if (visibleItems.isEmpty()) {
        Text(if (ownership == OwnershipFilter.LOCKED) "Everything in this set is available."
            else "No available items in this set yet.", color = Ink,
            modifier = Modifier.testTag("collection_empty_filter"))
    }
    // Preserve chapter totals and ordering regardless of the ownership filter.
    if (category == CollectionCategory.ICONS && family == IconFamily.ALL) {
        items.groupBy { it.family }.entries.forEachIndexed { chapterIndex, (chapter, chapterItems) ->
            val owned = chapterItems.count { it.isEquipped(look) || progress.getValue(it.id).unlocked }
            CollectionChapterHeading(chapter?.label ?: "Basic", chapterIndex + 1, owned, chapterItems.size)
            val visibleChapter = chapterItems.filter { it in visibleItems }
            if (visibleChapter.isNotEmpty()) CosmeticGrid(visibleChapter, progress, look, !state.busy, iconGallery = true, open = open)
        }
    } else {
        if (visibleItems.isNotEmpty()) CosmeticGrid(visibleItems, progress, look, !state.busy, category == CollectionCategory.ICONS, open)
    }
    if (stylePreviewOpen) {
        AlertDialog(
            onDismissRequest = { stylePreviewOpen = false },
            title = { Text("Your style") },
            text = { Column(Modifier.verticalScroll(rememberScrollState())) { StylePreview(look, state.uniformNumbers) } },
            confirmButton = {
                TextButton(onClick = { stylePreviewOpen = false }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Close") }
            }
        )
    }
    allCollectionItems.firstOrNull { it.id == previewId }?.let { item ->
        CosmeticPreview(item, progress.getValue(item.id), state, close = { previewId = null }, use = {
            actions.cosmetic(item.group.key, item.id)
            previewId = null
            scope.launch { scroll.animateScrollTo(0) }
        })
    }
}

/** Lightweight album section divider; never changes unlock order or identity. */
@Composable private fun CollectionChapterHeading(name: String, index: Int, available: Int, total: Int) {
    Column(Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp)
        .testTag("collection_chapter_$index"), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("CHAPTER " + index.toString().padStart(2, '0'),
                    color = Gold, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = .8.sp)
                Text("$name collection", color = Forest, fontFamily = FontFamily.Serif,
                    fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
            Text("$available / $total", color = Forest, style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.testTag("collection_chapter_count_$index")
                    .semantics { contentDescription = "$available of $total available" })
        }
        MilestoneTrack(available, total)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun <T> CollectionChoices(
    values: List<T>,
    choice: T,
    label: (T) -> String,
    role: Role,
    horizontalScrollState: ScrollState? = null,
    tagPrefix: String? = null,
    select: (T) -> Unit
) {
    val horizontalPadding = if (role == Role.RadioButton) 8.dp else 11.dp
    val buttons: @Composable () -> Unit = {
        values.forEach { value ->
            val selected = value == choice
            val selectedItem = remember(value) { BringIntoViewRequester() }
            // The selected family can otherwise be hidden after changing screens
            // or restoring the Collection on a narrower phone.
            if (selected && horizontalScrollState != null) {
                LaunchedEffect(value, horizontalScrollState) { selectedItem.bringIntoView() }
            }
            val shape = RoundedCornerShape(13.dp)
            val choiceModifier = if (tagPrefix == null) Modifier else Modifier.testTag("${tagPrefix}_$value")
            Box(choiceModifier.bringIntoViewRequester(selectedItem)
                .clip(shape).background(if (selected) Forest else Paper).border(1.dp, if (selected) Forest else Rule, shape)
                .selectable(selected, role = role, onClick = { select(value) })
                .sizeIn(minWidth = 48.dp, minHeight = 48.dp).padding(horizontal = horizontalPadding, vertical = 7.dp),
                contentAlignment = Alignment.Center) {
                Text(label(value), style = MaterialTheme.typography.labelLarge, color = if (selected) OnForest else Forest)
            }
        }
    }
    val group = Modifier.fillMaxWidth().selectableGroup()
    val spacing = Arrangement.spacedBy(6.dp)
    if (horizontalScrollState != null) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(group.horizontalScroll(horizontalScrollState), horizontalArrangement = spacing) { buttons() }
            // The clipped edge of a chip is too subtle a scroll affordance.
            // Show a short hint only when the family row actually overflows.
            if (horizontalScrollState.maxValue > 0) {
                Text("Swipe for more icon sets ↔",
                    modifier = Modifier.align(Alignment.End).testTag("collection_family_scroll_hint"),
                    style = MaterialTheme.typography.labelSmall, color = Muted)
            }
        }
    } else {
        FlowRow(group, horizontalArrangement = spacing, verticalArrangement = Arrangement.spacedBy(4.dp)) { buttons() }
    }
}

@Composable private fun CosmeticGrid(items: List<CollectionItem>, progress: Map<String, CosmeticProgress>,
    look: Appearance, enabled: Boolean, iconGallery: Boolean, open: (CollectionItem) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val scale = LocalDensity.current.fontScale
        val columns = when {
            scale > 1.3f || maxWidth < 260.dp -> 1
            iconGallery && maxWidth >= 320.dp -> 3
            maxWidth >= 430.dp -> 3
            else -> 2
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.chunked(columns).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { item ->
                        CosmeticCard(item, progress.getValue(item.id), look, enabled, columns == 1,
                            iconGallery, Modifier.weight(1f), open = { open(item) })
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable private fun CosmeticCard(item: CollectionItem, progress: CosmeticProgress, look: Appearance, enabled: Boolean,
    singleColumn: Boolean, iconGallery: Boolean, modifier: Modifier, open: () -> Unit) {
    val equipped = item.isEquipped(look)
    val status = when { equipped -> "Equipped"; progress.unlocked -> "Available"; else -> "Locked" }
    val family = item.family?.label
    Surface(onClick = open, enabled = enabled, modifier = modifier.testTag(item.tag).semantics {
        role = Role.Button
        selected = equipped
        stateDescription = if (family == null) status else "$family. $status"
    }, shape = RoundedCornerShape(15.dp), color = if (equipped) GreenTint else Paper,
        border = BorderStroke(if (equipped) 2.dp else 1.dp, if (equipped) Forest else Rule)) {
        val sample = item.group.withItem(look, item.id)
        // The themed chapter/filter already names the family. Avoid wasting
        // a line repeating 'Garden' or 'Farm' on every portrait card.
        val detail = when {
            equipped -> status
            progress.unlocked -> "Available"
            iconGallery -> null
            else -> family
        }
        val label: @Composable () -> Unit = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = if (singleColumn) Alignment.Start else Alignment.CenterHorizontally) {
                val singleWordIcon = !singleColumn && iconGallery &&
                    item.cosmetic.name.none { it.isWhitespace() }
                Text(item.cosmetic.name, color = Forest,
                    // Prevent Woodpecke/r and Constellati/on: never break a
                    // single-word portrait or emblem label across lines.
                    fontSize = if (singleWordIcon) collectionIconLabelSize(item.cosmetic.name).sp else 14.sp,
                    lineHeight = if (singleWordIcon) 15.sp else 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = if (singleWordIcon) 1 else 2,
                    softWrap = !singleWordIcon,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (singleColumn) TextAlign.Start else TextAlign.Center)
                if (detail != null) {
                    Text(detail, color = Muted, fontSize = 11.sp, lineHeight = 14.sp,
                        modifier = Modifier.clearAndSetSemantics {})
                }
            }
        }
        val statusIcon: @Composable (Modifier) -> Unit = { iconModifier ->
            if (equipped || !progress.unlocked) {
                GameIcon(if (equipped) GameSymbol.CHECK else GameSymbol.LOCK, if (equipped) Forest else Muted,
                    iconModifier.size(16.dp))
            }
        }
        if (singleColumn) {
            Row(Modifier.heightIn(min = 78.dp).padding(10.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CosmeticSwatch(item, sample, Modifier.size(50.dp))
                Box(Modifier.weight(1f)) { label() }
                statusIcon(Modifier)
            }
        } else {
            Box(Modifier.fillMaxWidth().heightIn(min = if (iconGallery) 126.dp else 110.dp).padding(8.dp)) {
                Column(Modifier.fillMaxWidth().align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    CosmeticSwatch(item, sample, Modifier.size(if (iconGallery) 62.dp else 50.dp))
                    label()
                }
                statusIcon(Modifier.align(Alignment.TopEnd))
            }
        }
    }
}

@Composable private fun CosmeticSwatch(item: CollectionItem, look: Appearance, modifier: Modifier) {
    when (item.group) {
        CosmeticGroup.EMBLEM, CosmeticGroup.PORTRAIT -> AnimalPortrait(look, modifier.clearAndSetSemantics {})
        CosmeticGroup.BOARD -> StyleSampleBoard(look, Appearance(), modifier.clearAndSetSemantics {}, compact = true)
        CosmeticGroup.INK, CosmeticGroup.FONT -> {
            val scale = LocalDensity.current.fontScale.coerceAtLeast(1f)
            Box(modifier.clip(RoundedCornerShape(10.dp)).background(Cream).clearAndSetSemantics {},
                contentAlignment = Alignment.Center) {
                Text(NUMBER_PREVIEW_SAMPLE, color = inkFor(look.ink, false), fontFamily = fontFor(look.font), fontSize = (26f / scale).sp)
            }
        }
    }
}

@Composable private fun CosmeticPreview(item: CollectionItem, progress: CosmeticProgress, state: GameUiState,
    close: () -> Unit, use: () -> Unit) {
    val sample = item.group.withItem(state.appearance, item.id)
    val equipped = item.isEquipped(state.appearance)
    val canUse = progress.unlocked && !equipped
    AlertDialog(onDismissRequest = close, title = { Text(item.cosmetic.name) }, text = {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (item.family != null) {
                val reviewingNewInsignia = item.group == CosmeticGroup.EMBLEM &&
                    insigniaArtReviewEnabled() && candidateInsigniaDrawable(item.id) != null
                if (reviewingNewInsignia) {
                    // This owner-only art reviewer compares originals with new art in
                    // one place. Avoid a redundant third oversized proposed image.
                    InsigniaCandidateComparison(item.id)
                } else {
                    val portrait = item.group == CosmeticGroup.PORTRAIT
                    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(GreenTint)
                        .padding(vertical = if (portrait) 4.dp else 12.dp)
                        .testTag("collection_detail_art"), contentAlignment = Alignment.Center) {
                        // Give the actual collectible more of the same panel,
                        // instead of adding blank height to the detail dialog.
                        AnimalPortrait(sample, Modifier.size(if (portrait) 136.dp else 116.dp)
                            .clearAndSetSemantics {})
                    }
                }
            }
            Text(item.previewLabel, color = Muted, style = MaterialTheme.typography.bodySmall)
            Text(when { equipped -> "Equipped"; progress.unlocked -> "Available to use"; else -> "Locked" },
                color = Forest, style = MaterialTheme.typography.titleSmall)
            if (!equipped && !progress.unlocked) {
                Text(item.cosmetic.requirement, color = Ink)
                Text("${progress.current} of ${progress.target}" + if (item.group == CosmeticGroup.PORTRAIT) " completed" else "",
                    color = Muted, style = MaterialTheme.typography.bodySmall)
                MilestoneTrack(progress.current, progress.target)
                if (progress.alternateTarget > 0) {
                    Text("Challenge progress: ${progress.alternateCurrent} of ${progress.alternateTarget}",
                        color = Muted, style = MaterialTheme.typography.bodySmall)
                    MilestoneTrack(progress.alternateCurrent, progress.alternateTarget)
                }
            }
            if (item.family == null) {
                StylePreview(sample, state.uniformNumbers && item.group == CosmeticGroup.BOARD,
                    allEntries = item.group == CosmeticGroup.FONT)
                if (state.uniformNumbers && item.group != CosmeticGroup.BOARD) {
                    Text("Turn off Classic number display in Settings to see this style in a puzzle.",
                        style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
        }
    }, confirmButton = {
        if (canUse) Button(onClick = use, enabled = !state.busy, modifier = Modifier.heightIn(min = 48.dp)) { Text(item.group.useLabel) }
        else TextButton(onClick = close, modifier = Modifier.heightIn(min = 48.dp)) { Text("Close") }
    }, dismissButton = {
        if (canUse) TextButton(onClick = close, modifier = Modifier.heightIn(min = 48.dp)) { Text("Close") }
    })
}

@Composable internal fun MilestoneTrack(current: Int, target: Int) {
    val fill = if (target <= 0) 1f else (current.toFloat() / target).coerceIn(0f, 1f)
    Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(Rule)) {
        if (fill > 0f) Box(Modifier.fillMaxWidth(fill).fillMaxHeight().background(Forest))
    }
}

/** A labeled visual sample, never a generated or scored game board. */
@Composable internal fun StylePreview(look: Appearance, uniformNumbers: Boolean, allEntries: Boolean = false) {
    val numberLook = look.withNumberDisplay(uniformNumbers)
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("STYLE PREVIEW", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
        StyleSampleBoard(look, numberLook, Modifier.fillMaxWidth().height(144.dp), allEntries = allEntries)
        Text(styleLabel(numberLook), color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
        if (uniformNumbers) {
            Text("Classic number display is on. Turn it off in Settings to use your equipped font and ink.",
                color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}

@Composable private fun StyleSampleBoard(
    look: Appearance, numberLook: Appearance, modifier: Modifier, compact: Boolean = false, allEntries: Boolean = false
) {
    val entryFont = fontFor(numberLook.font)
    val clueFont = fontFor(Appearance().font)
    val palette = boardPalette(look.board)
    val dark = palette.dark
    val scale = LocalDensity.current.fontScale.coerceAtLeast(1f)
    val digitSize = ((if (compact) 11f else 25f) / scale).sp
    val edge = palette.previewRule
    val shape = RoundedCornerShape(if (compact) 7.dp else 12.dp)
    Column(modifier.testTag(if (compact) "compact_board_swatch" else "expanded_board_preview")
        .clip(shape).background(palette.fill).border(1.dp, edge, shape).drawWithContent {
        drawContent()
        for (i in 1..2) {
            drawLine(edge, Offset(size.width * i / 3, 0f), Offset(size.width * i / 3, size.height), 1.dp.toPx())
            drawLine(edge, Offset(0f, size.height * i / 3), Offset(size.width, size.height * i / 3), 1.dp.toPx())
        }
    }) {
        for (r in 0..2) Row(Modifier.weight(1f).fillMaxWidth()) {
            for (c in 0..2) Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                if (!compact || r == c) {
                    val entered = allEntries || (r + c) % 2 == 0
                    Text((r * 3 + c + 1).toString(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        maxLines = 1, softWrap = false, fontFamily = if (entered) entryFont else clueFont,
                        fontSize = digitSize, lineHeight = digitSize, fontWeight = if (entered) FontWeight.Normal else FontWeight.Bold,
                        color = if (entered) legibleBoardDigitInk(inkFor(numberLook.ink, dark), palette.fill, palette) else palette.clueInk)
                }
            }
        }
    }
}

private val TealColor = Color(0xFFDCECE4)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun RoomChatSheet(state: GameUiState, actions: GameActions, close: () -> Unit) {
    var group by rememberSaveable { mutableStateOf<QuickChatGroup?>(QuickChatGroup.RESPONSE) }
    val scroll = rememberLazyListState()
    val visibleChat = if (state.communicationMuted) state.chat.filter { it.player == state.role } else state.chat
    val enabled = state.role == HOST || state.connected
    LaunchedEffect(visibleChat.lastOrNull()?.id, state.communicationMuted) { if (visibleChat.isNotEmpty()) scroll.animateScrollToItem(visibleChat.lastIndex) }
    ModalBottomSheet(onDismissRequest = close, containerColor = Cream,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(Modifier.fillMaxWidth().heightIn(max = 560.dp).verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp).padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Quick Chat & Emotes", fontFamily = FontFamily.Serif, color = Forest, fontSize = 23.sp)
            Text("Choose a message for your partner. Messages aren't saved.", color = Muted, fontSize = 12.sp)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(if (state.communicationMuted) "Incoming communication hidden" else "Show incoming communication", modifier = Modifier.weight(1f), fontSize = 13.sp, color = Forest)
                Switch(checked = !state.communicationMuted, onCheckedChange = { actions.muteCommunication(!it) }, modifier = Modifier.testTag("communication_visibility").semantics { contentDescription = "Show incoming messages, emotes and markers" })
            }
            if (state.communicationMuted) Text("Your partner can still play and receive your messages. Unmute to see recent messages again.", color = Muted, fontSize = 12.sp)
            if (!enabled) Text("Reconnect to send messages.", color = Muted,
                style = MaterialTheme.typography.bodyMedium, modifier = Modifier.testTag("quick_chat_disconnected"))
            LazyColumn(Modifier.fillMaxWidth().height(128.dp).testTag("quick_chat_history"), state = scroll,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (visibleChat.isEmpty()) item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                        Text(if (state.communicationMuted) "Incoming messages are hidden." else "No messages yet.",
                            color = Muted, style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center, modifier = Modifier.testTag("quick_chat_empty"))
                    }
                }
                items(visibleChat, key = { it.id }) { msg ->
                    val mine = msg.player == state.role
                    val author = if (msg.player == HOST) state.snapshot?.hostName ?: "Host" else state.snapshot?.guestName ?: "Partner"
                    val look = if (msg.player == HOST) state.snapshot?.hostAppearance ?: Appearance() else state.snapshot?.guestAppearance ?: Appearance()
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (mine) Alignment.End else Alignment.Start) {
                        Row(Modifier.widthIn(max = 420.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            AnimalPortrait(look, Modifier.size(24.dp).clearAndSetSemantics {})
                            Text(if (mine) "You" else author, modifier = Modifier.weight(1f, fill = false),
                                style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        val reaction = GridMatesEmote.fromWire(msg.text)
                        if (reaction == null) {
                            Text(msg.text, modifier = Modifier.background(if (mine) TealColor else Paper,
                                RoundedCornerShape(12.dp)).padding(12.dp), fontFamily = fontFor(look.font),
                                color = inkFor(look.ink, false), fontSize = 14.sp)
                        } else {
                            Image(painterResource(foxEmoteDrawable(reaction)), contentDescription = reaction.label,
                                modifier = Modifier.size(72.dp).testTag("chat_emote_" + reaction.id))
                        }
                    }
                }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.selectableGroup()) {
                QuickChatGroup.entries.forEach { category ->
                    val label = when (category) {
                        QuickChatGroup.RESPONSE -> "Responses"
                        QuickChatGroup.TEAMWORK -> "Teamwork"
                        QuickChatGroup.FRIENDLY -> "Friendly"
                    }
                    FilterChip(selected = group == category, onClick = { group = category },
                        label = { Text(label) }, modifier = Modifier.heightIn(min = 48.dp))
                }
                FilterChip(selected = group == null, onClick = { group = null },
                    label = { Text("Emotes") }, modifier = Modifier.heightIn(min = 48.dp).testTag("quick_chat_emotes"))
            }
            val largeText = LocalDensity.current.fontScale > 1.3f
            val emoteColumns = if (largeText) 2 else 3
            val phraseColumns = if (largeText) 1 else 2
            if (group == null) GridMatesEmote.entries.chunked(emoteColumns).forEach { trio ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    trio.forEach { emote ->
                        OutlinedButton(onClick = { actions.sendChat(emote.wireValue) },
                            modifier = Modifier.weight(1f).heightIn(min = 76.dp).testTag("emote_" + emote.id),
                            contentPadding = PaddingValues(4.dp), enabled = enabled) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(painterResource(foxEmoteDrawable(emote)), contentDescription = null, modifier = Modifier.size(48.dp))
                                Text(emote.label, fontSize = 12.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                    repeat(emoteColumns - trio.size) { Spacer(Modifier.weight(1f)) }
                }
            } else QuickChatPhrase.entries.filter { it.group == group }.chunked(phraseColumns).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { phrase ->
                        OutlinedButton(onClick = { actions.sendChat(phrase.label) },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("quick_chat_" + phrase.id),
                            enabled = enabled) { Text(phrase.label, textAlign = TextAlign.Center) }
                    }
                    repeat(phraseColumns - pair.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

/** Stable wire IDs map to the approved, bundled Fox V1 stickers. */
internal fun foxEmoteDrawable(emote: GridMatesEmote): Int = when (emote.id) {
    "happy" -> R.drawable.gm_emote_fox_happy
    "approve" -> R.drawable.gm_emote_fox_approve
    "disapprove" -> R.drawable.gm_emote_fox_disapprove
    "appreciation" -> R.drawable.gm_emote_fox_appreciation
    "thinking" -> R.drawable.gm_emote_fox_thinking
    "celebration" -> R.drawable.gm_emote_fox_celebration
    "oops" -> R.drawable.gm_emote_fox_oops
    "greeting" -> R.drawable.gm_emote_fox_greeting
    "confused" -> R.drawable.gm_emote_fox_confused
    "applause" -> R.drawable.gm_emote_fox_applause
    "break" -> R.drawable.gm_emote_fox_break
    "ready" -> R.drawable.gm_emote_fox_ready
    else -> error("Unmapped GridMates emote")
}
