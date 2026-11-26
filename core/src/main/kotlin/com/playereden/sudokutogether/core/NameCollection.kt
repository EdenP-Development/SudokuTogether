package com.playereden.sudokutogether.core

import kotlin.random.Random

enum class NameSlot { FIRST, MIDDLE, LAST }

data class NameWord(val id: String, val label: String, val slot: NameSlot, val achievementId: String? = null)

data class PlayerNameSelection(val firstId: String, val middleId: String? = null, val lastId: String)

object NameCollection {
    const val MAX_DISPLAY_LENGTH = 40

    // IDs are permanent literals, independent of display text and localization.
    val words: List<NameWord> = listOf(
        NameWord("first.happy", "Happy", NameSlot.FIRST),
        NameWord("first.clever", "Clever", NameSlot.FIRST),
        NameWord("first.curious", "Curious", NameSlot.FIRST),
        NameWord("first.gentle", "Gentle", NameSlot.FIRST),
        NameWord("first.lucky", "Lucky", NameSlot.FIRST),
        NameWord("first.brave", "Brave", NameSlot.FIRST),
        NameWord("first.cozy", "Cozy", NameSlot.FIRST),
        NameWord("first.quiet", "Quiet", NameSlot.FIRST),
        NameWord("first.swift", "Swift", NameSlot.FIRST),
        NameWord("first.friendly", "Friendly", NameSlot.FIRST),
        NameWord("middle.maple", "Maple", NameSlot.MIDDLE),
        NameWord("middle.clover", "Clover", NameSlot.MIDDLE),
        NameWord("middle.willow", "Willow", NameSlot.MIDDLE),
        NameWord("middle.meadow", "Meadow", NameSlot.MIDDLE),
        NameWord("middle.pebble", "Pebble", NameSlot.MIDDLE),
        NameWord("middle.sunny", "Sunny", NameSlot.MIDDLE),
        NameWord("middle.moss", "Moss", NameSlot.MIDDLE),
        NameWord("middle.river", "River", NameSlot.MIDDLE),
        NameWord("middle.honey", "Honey", NameSlot.MIDDLE),
        NameWord("middle.forest", "Forest", NameSlot.MIDDLE),
        NameWord("last.fox", "Fox", NameSlot.LAST),
        NameWord("last.owl", "Owl", NameSlot.LAST),
        NameWord("last.rabbit", "Rabbit", NameSlot.LAST),
        NameWord("last.bear", "Bear", NameSlot.LAST),
        NameWord("last.otter", "Otter", NameSlot.LAST),
        NameWord("last.turtle", "Turtle", NameSlot.LAST),
        NameWord("last.squirrel", "Squirrel", NameSlot.LAST),
        NameWord("last.badger", "Badger", NameSlot.LAST),
        NameWord("last.hedgehog", "Hedgehog", NameSlot.LAST),
        NameWord("last.potato", "Potato", NameSlot.LAST),
        NameWord("first.first", "First", NameSlot.FIRST, "first-grid"),
        NameWord("middle.acorn", "Acorn", NameSlot.MIDDLE, "first-grid"),
        NameWord("last.solver", "Solver", NameSlot.LAST, "first-grid"),
        NameWord("first.patient", "Patient", NameSlot.FIRST, "steady-hand"),
        NameWord("middle.fern", "Fern", NameSlot.MIDDLE, "steady-hand"),
        NameWord("last.wren", "Wren", NameSlot.LAST, "steady-hand"),
        NameWord("first.dedicated", "Dedicated", NameSlot.FIRST, "dedicated"),
        NameWord("middle.birch", "Birch", NameSlot.MIDDLE, "dedicated"),
        NameWord("last.deer", "Deer", NameSlot.LAST, "dedicated"),
        NameWord("first.steady", "Steady", NameSlot.FIRST, "solo-50"),
        NameWord("middle.oak", "Oak", NameSlot.MIDDLE, "solo-50"),
        NameWord("last.hare", "Hare", NameSlot.LAST, "solo-50"),
        NameWord("first.wise", "Wise", NameSlot.FIRST, "solo-100"),
        NameWord("middle.amber", "Amber", NameSlot.MIDDLE, "solo-100"),
        NameWord("last.lynx", "Lynx", NameSlot.LAST, "solo-100"),
        NameWord("first.seasoned", "Seasoned", NameSlot.FIRST, "solo-250"),
        NameWord("middle.cedar", "Cedar", NameSlot.MIDDLE, "solo-250"),
        NameWord("last.crane", "Crane", NameSlot.LAST, "solo-250"),
        NameWord("first.legendary", "Legendary", NameSlot.FIRST, "solo-500"),
        NameWord("middle.golden", "Golden", NameSlot.MIDDLE, "solo-500"),
        NameWord("last.stag", "Stag", NameSlot.LAST, "solo-500"),
        NameWord("first.resolute", "Resolute", NameSlot.FIRST, "solo-750"),
        NameWord("middle.silver", "Silver", NameSlot.MIDDLE, "solo-750"),
        NameWord("last.heron", "Heron", NameSlot.LAST, "solo-750"),
        NameWord("first.masterful", "Masterful", NameSlot.FIRST, "solo-1000"),
        NameWord("middle.crystal", "Crystal", NameSlot.MIDDLE, "solo-1000"),
        NameWord("last.falcon", "Falcon", NameSlot.LAST, "solo-1000"),
        NameWord("first.timeless", "Timeless", NameSlot.FIRST, "solo-1500"),
        NameWord("middle.ancient", "Ancient", NameSlot.MIDDLE, "solo-1500"),
        NameWord("last.phoenix", "Phoenix", NameSlot.LAST, "solo-1500"),
        NameWord("first.bright", "Bright", NameSlot.FIRST, "daily-3"),
        NameWord("middle.dawn", "Dawn", NameSlot.MIDDLE, "daily-3"),
        NameWord("last.robin", "Robin", NameSlot.LAST, "daily-3"),
        NameWord("first.cheerful", "Cheerful", NameSlot.FIRST, "daily-7"),
        NameWord("middle.starlight", "Starlight", NameSlot.MIDDLE, "daily-7"),
        NameWord("last.finch", "Finch", NameSlot.LAST, "daily-7"),
        NameWord("first.radiant", "Radiant", NameSlot.FIRST, "daily-15"),
        NameWord("middle.sunrise", "Sunrise", NameSlot.MIDDLE, "daily-15"),
        NameWord("last.lark", "Lark", NameSlot.LAST, "daily-15"),
        NameWord("first.dreamy", "Dreamy", NameSlot.FIRST, "daily-30"),
        NameWord("middle.moonlight", "Moonlight", NameSlot.MIDDLE, "daily-30"),
        NameWord("last.moth", "Moth", NameSlot.LAST, "daily-30"),
        NameWord("first.everlasting", "Everlasting", NameSlot.FIRST, "daily-60"),
        NameWord("middle.evergreen", "Evergreen", NameSlot.MIDDLE, "daily-60"),
        NameWord("last.kingfisher", "Kingfisher", NameSlot.LAST, "daily-60"),
        NameWord("first.faithful", "Faithful", NameSlot.FIRST, "daily-100"),
        NameWord("middle.twilight", "Twilight", NameSlot.MIDDLE, "daily-100"),
        NameWord("last.swan", "Swan", NameSlot.LAST, "daily-100"),
        NameWord("first.enduring", "Enduring", NameSlot.FIRST, "daily-180"),
        NameWord("middle.solstice", "Solstice", NameSlot.MIDDLE, "daily-180"),
        NameWord("last.albatross", "Albatross", NameSlot.LAST, "daily-180"),
        NameWord("first.thoughtful", "Thoughtful", NameSlot.FIRST, "medium-10"),
        NameWord("middle.brook", "Brook", NameSlot.MIDDLE, "medium-10"),
        NameWord("last.beaver", "Beaver", NameSlot.LAST, "medium-10"),
        NameWord("first.measured", "Measured", NameSlot.FIRST, "medium-25"),
        NameWord("middle.flint", "Flint", NameSlot.MIDDLE, "medium-25"),
        NameWord("last.marmot", "Marmot", NameSlot.LAST, "medium-25"),
        NameWord("first.focused", "Focused", NameSlot.FIRST, "medium-40"),
        NameWord("middle.compass", "Compass", NameSlot.MIDDLE, "medium-40"),
        NameWord("last.kestrel", "Kestrel", NameSlot.LAST, "medium-40"),
        NameWord("first.skillful", "Skillful", NameSlot.FIRST, "medium-100"),
        NameWord("middle.jasper", "Jasper", NameSlot.MIDDLE, "medium-100"),
        NameWord("last.eagle", "Eagle", NameSlot.LAST, "medium-100"),
        NameWord("first.precise", "Precise", NameSlot.FIRST, "medium-250"),
        NameWord("middle.quartz", "Quartz", NameSlot.MIDDLE, "medium-250"),
        NameWord("last.osprey", "Osprey", NameSlot.LAST, "medium-250"),
        NameWord("first.determined", "Determined", NameSlot.FIRST, "hard-5"),
        NameWord("middle.granite", "Granite", NameSlot.MIDDLE, "hard-5"),
        NameWord("last.bison", "Bison", NameSlot.LAST, "hard-5"),
        NameWord("first.inventive", "Inventive", NameSlot.FIRST, "hard-15"),
        NameWord("middle.nebula", "Nebula", NameSlot.MIDDLE, "hard-15"),
        NameWord("last.raven", "Raven", NameSlot.LAST, "hard-15"),
        NameWord("first.brilliant", "Brilliant", NameSlot.FIRST, "hard-50"),
        NameWord("middle.constellation", "Constellation", NameSlot.MIDDLE, "hard-50"),
        NameWord("last.dragon", "Dragon", NameSlot.LAST, "hard-50"),
        NameWord("first.strategic", "Strategic", NameSlot.FIRST, "hard-100"),
        NameWord("middle.obsidian", "Obsidian", NameSlot.MIDDLE, "hard-100"),
        NameWord("last.griffin", "Griffin", NameSlot.LAST, "hard-100"),
        NameWord("first.insightful", "Insightful", NameSlot.FIRST, "hard-250"),
        NameWord("middle.aurora", "Aurora", NameSlot.MIDDLE, "hard-250"),
        NameWord("last.unicorn", "Unicorn", NameSlot.LAST, "hard-250"),
        NameWord("first.mindful", "Mindful", NameSlot.FIRST, "clean-5"),
        NameWord("middle.lotus", "Lotus", NameSlot.MIDDLE, "clean-5"),
        NameWord("last.butterfly", "Butterfly", NameSlot.LAST, "clean-5"),
        NameWord("first.careful", "Careful", NameSlot.FIRST, "clean-12"),
        NameWord("middle.dewdrop", "Dewdrop", NameSlot.MIDDLE, "clean-12"),
        NameWord("last.ladybug", "Ladybug", NameSlot.LAST, "clean-12"),
        NameWord("first.composed", "Composed", NameSlot.FIRST, "clean-25"),
        NameWord("middle.snowdrop", "Snowdrop", NameSlot.MIDDLE, "clean-25"),
        NameWord("last.cricket", "Cricket", NameSlot.LAST, "clean-25"),
        NameWord("first.unshaken", "Unshaken", NameSlot.FIRST, "clean-50"),
        NameWord("middle.glacier", "Glacier", NameSlot.MIDDLE, "clean-50"),
        NameWord("last.seal", "Seal", NameSlot.LAST, "clean-50"),
        NameWord("first.exact", "Exact", NameSlot.FIRST, "clean-100"),
        NameWord("middle.diamond", "Diamond", NameSlot.MIDDLE, "clean-100"),
        NameWord("last.dolphin", "Dolphin", NameSlot.LAST, "clean-100"),
        NameWord("first.independent", "Independent", NameSlot.FIRST, "clean-250"),
        NameWord("middle.sapphire", "Sapphire", NameSlot.MIDDLE, "clean-250"),
        NameWord("last.whale", "Whale", NameSlot.LAST, "clean-250"),
        NameWord("first.helpful", "Helpful", NameSlot.FIRST, "two-minds"),
        NameWord("middle.daisy", "Daisy", NameSlot.MIDDLE, "two-minds"),
        NameWord("last.penguin", "Penguin", NameSlot.LAST, "two-minds"),
        NameWord("first.loyal", "Loyal", NameSlot.FIRST, "teamwork"),
        NameWord("middle.orchard", "Orchard", NameSlot.MIDDLE, "teamwork"),
        NameWord("last.panda", "Panda", NameSlot.LAST, "teamwork"),
        NameWord("first.supportive", "Supportive", NameSlot.FIRST, "team-15"),
        NameWord("middle.blossom", "Blossom", NameSlot.MIDDLE, "team-15"),
        NameWord("last.koala", "Koala", NameSlot.LAST, "team-15"),
        NameWord("first.harmonious", "Harmonious", NameSlot.FIRST, "team-30"),
        NameWord("middle.garden", "Garden", NameSlot.MIDDLE, "team-30"),
        NameWord("last.bee", "Bee", NameSlot.LAST, "team-30"),
        NameWord("first.trusted", "Trusted", NameSlot.FIRST, "team-75"),
        NameWord("middle.haven", "Haven", NameSlot.MIDDLE, "team-75"),
        NameWord("last.companion", "Companion", NameSlot.LAST, "team-75"),
        NameWord("first.bold", "Bold", NameSlot.FIRST, "challenger"),
        NameWord("middle.ember", "Ember", NameSlot.MIDDLE, "challenger"),
        NameWord("last.hawk", "Hawk", NameSlot.LAST, "challenger"),
        NameWord("first.playful", "Playful", NameSlot.FIRST, "rivalry"),
        NameWord("middle.breeze", "Breeze", NameSlot.MIDDLE, "rivalry"),
        NameWord("last.raccoon", "Raccoon", NameSlot.LAST, "rivalry"),
        NameWord("first.victorious", "Victorious", NameSlot.FIRST, "champion"),
        NameWord("middle.laurel", "Laurel", NameSlot.MIDDLE, "champion"),
        NameWord("last.champion", "Champion", NameSlot.LAST, "champion"),
        NameWord("first.spirited", "Spirited", NameSlot.FIRST, "versus-15"),
        NameWord("middle.comet", "Comet", NameSlot.MIDDLE, "versus-15"),
        NameWord("last.cheetah", "Cheetah", NameSlot.LAST, "versus-15"),
        NameWord("first.sporting", "Sporting", NameSlot.FIRST, "versus-50"),
        NameWord("middle.thunder", "Thunder", NameSlot.MIDDLE, "versus-50"),
        NameWord("last.gazelle", "Gazelle", NameSlot.LAST, "versus-50"),
        NameWord("first.persistent", "Persistent", NameSlot.FIRST, "versus-100"),
        NameWord("middle.tempest", "Tempest", NameSlot.MIDDLE, "versus-100"),
        NameWord("last.mustang", "Mustang", NameSlot.LAST, "versus-100"),
        NameWord("first.triumphant", "Triumphant", NameSlot.FIRST, "versus-30"),
        NameWord("middle.crown", "Crown", NameSlot.MIDDLE, "versus-30"),
        NameWord("last.jaguar", "Jaguar", NameSlot.LAST, "versus-30"),
        NameWord("first.fearless", "Fearless", NameSlot.FIRST, "versus-win-75"),
        NameWord("middle.summit", "Summit", NameSlot.MIDDLE, "versus-win-75"),
        NameWord("last.panther", "Panther", NameSlot.LAST, "versus-win-75"),
        NameWord("first.nimble", "Nimble", NameSlot.FIRST, "quick-thinker"),
        NameWord("middle.spark", "Spark", NameSlot.MIDDLE, "quick-thinker"),
        NameWord("last.hummingbird", "Hummingbird", NameSlot.LAST, "quick-thinker")
    )

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
        return label.takeUnless { it.length > MAX_DISPLAY_LENGTH || it in blockedNames }
    }

    fun parseCanonical(label: String): PlayerNameSelection? {
        if (label.length !in 1..MAX_DISPLAY_LENGTH) return null
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

    /** Stable starter fallback for legacy or no-longer-eligible profile names.
     *  The installation identity is not changed or included in the display name.
     */
    fun normalizedProfileName(name: String, profile: LocalPlayerData): String {
        val selection = parseCanonical(name)
        if (selection != null && canEquip(selection, earnedIds(profile))) return name
        return requireNotNull(display(randomName(emptySet(), true, Random(profile.playerId.hashCode()))))
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
