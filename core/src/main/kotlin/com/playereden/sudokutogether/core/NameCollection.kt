package com.playereden.sudokutogether.core

import kotlin.random.Random
import java.util.Locale

enum class NameSlot { FIRST, MIDDLE, LAST }

data class NameWord(val id: String, val label: String, val slot: NameSlot, val achievementId: String? = null)

data class PlayerNameSelection(val firstId: String, val middleId: String? = null, val lastId: String)

object NameCollection {
    // Catalog IDs are permanent and independent of future localized display text.
    private fun word(slot: NameSlot, label: String, achievementId: String? = null) =
        NameWord(slot.name.lowercase(Locale.ROOT) + "." + label.lowercase(Locale.ROOT), label, slot, achievementId)

    private fun bundle(achievementId: String, first: String, middle: String, last: String) =
        listOf(word(NameSlot.FIRST, first, achievementId), word(NameSlot.MIDDLE, middle, achievementId), word(NameSlot.LAST, last, achievementId))

    val words: List<NameWord> = buildList {
        listOf("Happy", "Clever", "Curious", "Gentle", "Lucky", "Brave", "Cozy", "Quiet", "Swift", "Friendly").forEach { add(word(NameSlot.FIRST, it)) }
        listOf("Maple", "Clover", "Willow", "Meadow", "Pebble", "Sunny", "Moss", "River", "Honey", "Forest").forEach { add(word(NameSlot.MIDDLE, it)) }
        listOf("Fox", "Owl", "Rabbit", "Bear", "Otter", "Turtle", "Squirrel", "Badger", "Hedgehog", "Potato").forEach { add(word(NameSlot.LAST, it)) }
        addAll(bundle("first-grid", "First", "Acorn", "Solver"))
        addAll(bundle("steady-hand", "Patient", "Fern", "Wren"))
        addAll(bundle("dedicated", "Dedicated", "Birch", "Deer"))
        addAll(bundle("solo-50", "Steady", "Oak", "Hare"))
        addAll(bundle("solo-100", "Wise", "Amber", "Lynx"))
        addAll(bundle("solo-250", "Seasoned", "Cedar", "Crane"))
        addAll(bundle("solo-500", "Legendary", "Golden", "Stag"))
        addAll(bundle("solo-750", "Resolute", "Silver", "Heron"))
        addAll(bundle("solo-1000", "Masterful", "Crystal", "Falcon"))
        addAll(bundle("solo-1500", "Timeless", "Ancient", "Phoenix"))
        addAll(bundle("daily-3", "Bright", "Dawn", "Robin"))
        addAll(bundle("daily-7", "Cheerful", "Starlight", "Finch"))
        addAll(bundle("daily-15", "Radiant", "Sunrise", "Lark"))
        addAll(bundle("daily-30", "Dreamy", "Moonlight", "Moth"))
        addAll(bundle("daily-60", "Everlasting", "Evergreen", "Kingfisher"))
        addAll(bundle("daily-100", "Faithful", "Twilight", "Swan"))
        addAll(bundle("daily-180", "Enduring", "Solstice", "Albatross"))
        addAll(bundle("medium-10", "Thoughtful", "Brook", "Beaver"))
        addAll(bundle("medium-25", "Measured", "Flint", "Marmot"))
        addAll(bundle("medium-40", "Focused", "Compass", "Kestrel"))
        addAll(bundle("medium-100", "Skillful", "Jasper", "Eagle"))
        addAll(bundle("medium-250", "Precise", "Quartz", "Osprey"))
        addAll(bundle("hard-5", "Determined", "Granite", "Bison"))
        addAll(bundle("hard-15", "Inventive", "Nebula", "Raven"))
        addAll(bundle("hard-50", "Brilliant", "Constellation", "Dragon"))
        addAll(bundle("hard-100", "Strategic", "Obsidian", "Griffin"))
        addAll(bundle("hard-250", "Insightful", "Aurora", "Unicorn"))
        addAll(bundle("clean-5", "Mindful", "Lotus", "Butterfly"))
        addAll(bundle("clean-12", "Careful", "Dewdrop", "Ladybug"))
        addAll(bundle("clean-25", "Composed", "Snowdrop", "Cricket"))
        addAll(bundle("clean-50", "Unshaken", "Glacier", "Seal"))
        addAll(bundle("clean-100", "Exact", "Diamond", "Dolphin"))
        addAll(bundle("clean-250", "Independent", "Sapphire", "Whale"))
        addAll(bundle("two-minds", "Helpful", "Daisy", "Penguin"))
        addAll(bundle("teamwork", "Loyal", "Orchard", "Panda"))
        addAll(bundle("team-15", "Supportive", "Blossom", "Koala"))
        addAll(bundle("team-30", "Harmonious", "Garden", "Bee"))
        addAll(bundle("team-75", "Trusted", "Haven", "Companion"))
        addAll(bundle("challenger", "Bold", "Ember", "Hawk"))
        addAll(bundle("rivalry", "Playful", "Breeze", "Raccoon"))
        addAll(bundle("champion", "Victorious", "Laurel", "Champion"))
        addAll(bundle("versus-15", "Spirited", "Comet", "Cheetah"))
        addAll(bundle("versus-50", "Sporting", "Thunder", "Gazelle"))
        addAll(bundle("versus-100", "Persistent", "Tempest", "Mustang"))
        addAll(bundle("versus-30", "Triumphant", "Crown", "Jaguar"))
        addAll(bundle("versus-win-75", "Fearless", "Summit", "Panther"))
        addAll(bundle("quick-thinker", "Nimble", "Spark", "Hummingbird"))
    }

    private val byId = words.associateBy { it.id }
    private val byLabel = words.associateBy { it.slot to it.label }
    // Reserved explicit combination exclusions, to be populated after catalog review.
    private val blockedNames: Set<String> = emptySet()

    fun available(slot: NameSlot, earnedIds: Set<String>): List<NameWord> =
        words.filter { it.slot == slot && (it.achievementId == null || it.achievementId in earnedIds) }

    fun earnedIds(profile: LocalPlayerData): Set<String> =
        Achievements.progress(profile.records, profile.earnedAchievementIds).filter { it.unlocked }.map { it.id }.toSet()

    fun display(selection: PlayerNameSelection): String? {
        val first = byId[selection.firstId]?.takeIf { it.slot == NameSlot.FIRST } ?: return null
        val last = byId[selection.lastId]?.takeIf { it.slot == NameSlot.LAST } ?: return null
        val middle = if (selection.middleId == null) null
            else byId[selection.middleId]?.takeIf { it.slot == NameSlot.MIDDLE } ?: return null
        val label = listOfNotNull(first.label, middle?.label, last.label).joinToString(" ")
        return label.takeUnless { it in blockedNames }
    }

    fun parseCanonical(label: String): PlayerNameSelection? {
        val parts = label.split(' ')
        if (parts.size !in 2..3) return null
        val first = byLabel[NameSlot.FIRST to parts.first()] ?: return null
        val last = byLabel[NameSlot.LAST to parts.last()] ?: return null
        val middle = if (parts.size == 3) byLabel[NameSlot.MIDDLE to parts[1]] ?: return null else null
        val selection = PlayerNameSelection(first.id, middle?.id, last.id)
        return selection.takeIf { display(it) == label }
    }

    fun canEquip(selection: PlayerNameSelection, earnedIds: Set<String>): Boolean =
        display(selection) != null && listOfNotNull(selection.firstId, selection.middleId, selection.lastId).all { id ->
            byId[id]?.let { it.achievementId == null || it.achievementId in earnedIds } == true
        }

    fun randomName(earnedIds: Set<String>, includeMiddle: Boolean, random: Random = Random.Default): PlayerNameSelection {
        val first = available(NameSlot.FIRST, earnedIds)
        val middle = available(NameSlot.MIDDLE, earnedIds)
        val last = available(NameSlot.LAST, earnedIds)
        repeat(64) {
            val selection = PlayerNameSelection(first.random(random).id, if (includeMiddle) middle.random(random).id else null, last.random(random).id)
            if (canEquip(selection, earnedIds)) return selection
        }
        for (f in first) for (m in if (includeMiddle) middle.map { it.id } else listOf(null)) for (l in last) {
            val selection = PlayerNameSelection(f.id, m, l.id)
            if (canEquip(selection, earnedIds)) return selection
        }
        error("No approved name combination available")
    }
}
