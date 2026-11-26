package com.playereden.sudokutogether.core

import kotlinx.serialization.Serializable

/**
 * Only curated identifiers travel over the network; never send arbitrary fonts,
 * downloadable assets, colors or executable/custom style data.
 */
@Serializable data class Appearance(
    val ink: String = "forest",
    val board: String = "classic",
    val font: String = "clear",
    val emblem: String = "tile",
    val portrait: String = ""
)

data class Cosmetic(val id: String, val name: String, val requirement: String)

data class CosmeticProgress(
    val current: Int, val target: Int, val earned: Boolean = false,
    val alternateCurrent: Int = 0, val alternateTarget: Int = 0
) {
    val unlocked: Boolean get() = earned || current >= target ||
        (alternateTarget > 0 && alternateCurrent >= alternateTarget)
}

object Cosmetics {
    private enum class Milestone(val verb: String, val singular: String, val plural: String) {
        FINISH("Complete", "puzzle", "puzzles"),
        SOLO("Solve", "solo puzzle", "solo puzzles"),
        COOP("Complete", "co-op puzzle", "co-op puzzles"),
        RACE("Win", "versus race", "versus races");

        fun describe(target: Int) = if (target == 0) "Starter"
            else "$verb $target ${if (target == 1) singular else plural}"
    }

    private enum class Challenge {
        DAILY_DAYS, MEDIUM_SOLO, HARD_SOLO, CLEAN_SOLO;

        fun describe(target: Int): String = when (this) {
            DAILY_DAYS -> "complete $target different Daily puzzles"
            MEDIUM_SOLO -> "solve $target Medium solo puzzles"
            HARD_SOLO -> "solve $target Hard solo puzzles"
            CLEAN_SOLO -> "solve $target solo puzzles without hints or checks"
        }
    }

    private data class AlternateGoal(val kind: Challenge, val target: Int) {
        fun describe() = kind.describe(target)
    }

    /**
     * Extra routes to selected portraits and veteran emblems—not a new economy.
     * Original completed-puzzle targets stay valid for every existing player.
     * Alternative milestones are supported by the existing durable record fields.
     */
    private val extraGoals = mapOf(
        "woodland_beaver" to AlternateGoal(Challenge.MEDIUM_SOLO, 5),
        "woodland_woodpecker" to AlternateGoal(Challenge.HARD_SOLO, 3),
        "woodland_skunk" to AlternateGoal(Challenge.CLEAN_SOLO, 12),
        "ocean_seahorse" to AlternateGoal(Challenge.DAILY_DAYS, 3),
        "ocean_dolphin" to AlternateGoal(Challenge.MEDIUM_SOLO, 7),
        "ocean_seashell" to AlternateGoal(Challenge.DAILY_DAYS, 7),
        "ocean_clownfish" to AlternateGoal(Challenge.HARD_SOLO, 5),
        "garden_strawberry" to AlternateGoal(Challenge.DAILY_DAYS, 2),
        "garden_firefly" to AlternateGoal(Challenge.CLEAN_SOLO, 5),
        "garden_watering_can" to AlternateGoal(Challenge.DAILY_DAYS, 5),
        "garden_potted_tulip" to AlternateGoal(Challenge.DAILY_DAYS, 10),
        "farm_sheep" to AlternateGoal(Challenge.MEDIUM_SOLO, 3),
        "farm_pig" to AlternateGoal(Challenge.CLEAN_SOLO, 4),
        "farm_pumpkin" to AlternateGoal(Challenge.DAILY_DAYS, 4),
        "farm_hay_bale" to AlternateGoal(Challenge.HARD_SOLO, 4),
        // Beta 2 veteran insignias: advanced paths in addition to total solves.
        "constellation" to AlternateGoal(Challenge.HARD_SOLO, 15),
        "sunrise" to AlternateGoal(Challenge.DAILY_DAYS, 15),
        "focus" to AlternateGoal(Challenge.CLEAN_SOLO, 25),
        "compass" to AlternateGoal(Challenge.MEDIUM_SOLO, 40),
        "moonlight" to AlternateGoal(Challenge.DAILY_DAYS, 30),
        "grid_master" to AlternateGoal(Challenge.HARD_SOLO, 50)
    )

    private data class CatalogEntry(val id: String, val name: String, val target: Int = 0, val milestone: Milestone = Milestone.SOLO) {
        fun cosmetic(): Cosmetic {
            val original = milestone.describe(target)
            val alternative = extraGoals[id]
            return Cosmetic(id, name, if (alternative == null) original
                else "$original or ${alternative.describe()}")
        }
    }

    private val woodlandEntries = linkedMapOf(
        "owl" to 0, "fox" to 0, "rabbit" to 1, "bear" to 2,
        "badger" to 3, "hedgehog" to 4, "robin" to 5, "deer" to 6,
        "squirrel" to 8, "otter" to 10, "tortoise" to 12, "cat" to 15,
        "chipmunk" to 16, "raccoon" to 24, "beaver" to 39,
        "woodpecker" to 56, "skunk" to 90
    ).map { (animal, count) ->
        CatalogEntry("woodland_$animal", animal.replaceFirstChar { it.uppercase() }, count, Milestone.FINISH)
    }
    private val oceanEntries = listOf(
        CatalogEntry("ocean_octopus", "Octopus", 0, Milestone.FINISH),
        CatalogEntry("ocean_sea_turtle", "Sea Turtle", 12, Milestone.FINISH),
        CatalogEntry("ocean_seal", "Harbor Seal", 18, Milestone.FINISH),
        CatalogEntry("ocean_hermit_crab", "Hermit Crab", 25, Milestone.FINISH),
        CatalogEntry("ocean_seahorse", "Seahorse", 30, Milestone.FINISH),
        CatalogEntry("ocean_manta_ray", "Manta Ray", 35, Milestone.FINISH),
        CatalogEntry("ocean_dolphin", "Dolphin", 46, Milestone.FINISH),
        CatalogEntry("ocean_jellyfish", "Jellyfish", 50, Milestone.FINISH),
        CatalogEntry("ocean_seashell", "Seashell", 67, Milestone.FINISH),
        CatalogEntry("ocean_narwhal", "Narwhal", 75, Milestone.FINISH),
        CatalogEntry("ocean_clownfish", "Clownfish", 95, Milestone.FINISH)
    )
    private val gardenEntries = listOf(
        CatalogEntry("garden_honeybee", "Honeybee", 0, Milestone.FINISH),
        CatalogEntry("garden_ladybug", "Ladybug", 4, Milestone.FINISH),
        CatalogEntry("garden_butterfly", "Butterfly", 10, Milestone.FINISH),
        // Mix flowers into the first Garden row instead of showing only bugs.
        CatalogEntry("garden_sunflower", "Sunflower", 11, Milestone.FINISH),
        CatalogEntry("garden_dragonfly", "Dragonfly", 20, Milestone.FINISH),
        CatalogEntry("garden_strawberry", "Strawberry", 23, Milestone.FINISH),
        CatalogEntry("garden_firefly", "Firefly", 42, Milestone.FINISH),
        CatalogEntry("garden_watering_can", "Watering Can", 62, Milestone.FINISH),
        CatalogEntry("garden_potted_tulip", "Potted Tulip", 100, Milestone.FINISH)
    )
    private val farmEntries = listOf(
        CatalogEntry("farm_cow", "Cow", 0, Milestone.FINISH),
        CatalogEntry("farm_chicken", "Chicken", 7, Milestone.FINISH),
        // Mixed Farm life: two animals, a crop and a rustic item.
        CatalogEntry("farm_sheep", "Sheep", 20, Milestone.FINISH),
        CatalogEntry("farm_pig", "Pig", 36, Milestone.FINISH),
        CatalogEntry("farm_pumpkin", "Pumpkin", 58, Milestone.FINISH),
        CatalogEntry("farm_hay_bale", "Hay Bale", 85, Milestone.FINISH)
    )
    private val inkEntries = listOf(
        CatalogEntry("forest", "Forest"),
        CatalogEntry("cobalt", "Cobalt", 1),
        CatalogEntry("amber", "Amber", 3),
        CatalogEntry("rose", "Rose", 5),
        CatalogEntry("violet", "Violet", 2, Milestone.RACE),
        CatalogEntry("lagoon", "Lagoon"),
        CatalogEntry("clay", "Clay", 4, Milestone.FINISH),
        CatalogEntry("slate", "Slate", 10, Milestone.FINISH),
        // Beta 1.2 color candidates, both available without multiplayer.
        CatalogEntry("sepia", "Sepia", 6, Milestone.SOLO),
        CatalogEntry("indigo", "Indigo", 14, Milestone.FINISH)
    )
    private val boardEntries = listOf(
        CatalogEntry("classic", "Classic"),
        CatalogEntry("linen", "Linen", 2),
        CatalogEntry("midnight", "Midnight", 5),
        CatalogEntry("circuit", "Circuit", 3, Milestone.RACE),
        // Beta 1.2 pilot boards. Both are obtainable by playing Solo.
        CatalogEntry("parchment", "Parchment", 8, Milestone.SOLO),
        CatalogEntry("ocean", "Ocean", 12, Milestone.SOLO),
        CatalogEntry("graphite", "Graphite", 20, Milestone.FINISH),
        // Four Beta 2 palette milestones; preserve original board ordering.
        CatalogEntry("moss", "Moss", 16, Milestone.FINISH),
        CatalogEntry("rosewood", "Rosewood", 28, Milestone.FINISH),
        CatalogEntry("pebble", "Pebble", 48, Milestone.FINISH),
        CatalogEntry("tidal", "Tidal", 70, Milestone.FINISH)
    )
    // Co-op display priority follows the curated board catalog order.
    // New board IDs cannot be accidentally omitted from a second list.
    private val sharedBoardOrder = boardEntries.map { it.id }
    private val fontEntries = listOf(
        CatalogEntry("clear", "Clear"),
        CatalogEntry("serif", "Bookish", 1),
        CatalogEntry("mono", "Terminal", 2, Milestone.COOP),
        CatalogEntry("rounded", "Rounded", 2, Milestone.FINISH),
        CatalogEntry("pencil", "Pencil", 8, Milestone.FINISH),
        // Four OFL-bundled Beta 2 number fonts; originals and unlocks unchanged.
        CatalogEntry("typewriter", "Typewriter", 14, Milestone.FINISH),
        CatalogEntry("editorial", "Editorial", 26, Milestone.FINISH),
        CatalogEntry("soft_print", "Soft Print", 45, Milestone.FINISH),
        CatalogEntry("tall_print", "Tall Print", 75, Milestone.FINISH)
    )
    private val emblemEntries = listOf(
        CatalogEntry("tile", "Gridkeeper"),
        CatalogEntry("leaf", "First Sprout", 1),
        CatalogEntry("duo", "Two Minds", 1, Milestone.COOP),
        CatalogEntry("flash", "Challenger", 1, Milestone.RACE),
        CatalogEntry("star", "Star Solver", 5),
        CatalogEntry("laurel", "Champion", 3, Milestone.RACE),
        // Six genuinely new earnable player insignias (no third-party sprites).
        CatalogEntry("constellation", "Constellation", 135, Milestone.FINISH),
        CatalogEntry("sunrise", "Sunrise", 160, Milestone.FINISH),
        CatalogEntry("focus", "Focused Solver", 190, Milestone.FINISH),
        CatalogEntry("compass", "True North", 230, Milestone.FINISH),
        CatalogEntry("moonlight", "Moonlight", 300, Milestone.FINISH),
        CatalogEntry("grid_master", "Grid Master", 500, Milestone.FINISH)
    )
    private val portraitEntries = woodlandEntries + oceanEntries + gardenEntries + farmEntries
    private val entries = (portraitEntries + inkEntries + boardEntries + fontEntries + emblemEntries).associateBy { it.id }

    val woodlandPortraits = woodlandEntries.map(CatalogEntry::cosmetic)
    val oceanPortraits = oceanEntries.map(CatalogEntry::cosmetic)
    val gardenPortraits = gardenEntries.map(CatalogEntry::cosmetic)
    val farmPortraits = farmEntries.map(CatalogEntry::cosmetic)
    val portraits = woodlandPortraits + oceanPortraits + gardenPortraits + farmPortraits
    val inks = inkEntries.map(CatalogEntry::cosmetic)
    val boards = boardEntries.map(CatalogEntry::cosmetic)
    val fonts = fontEntries.map(CatalogEntry::cosmetic)
    val emblems = emblemEntries.map(CatalogEntry::cosmetic)

    /** Ownership survives record retention and later tuning of unlock thresholds. */
    val knownIds: Set<String> get() = entries.keys

    fun preserveEarned(profile: LocalPlayerData): LocalPlayerData {
        val fromCurrentRecords = progressAll(profile.records).filterValues { it.unlocked && it.target > 0 }.keys
        val owned = (profile.ownedCosmeticIds + fromCurrentRecords).distinct().sorted()
        return if (profile.ownedCosmeticIds == owned) profile else profile.copy(ownedCosmeticIds = owned)
    }

    /** The room's display priority is independent of either player's unlocks. */
    fun sharedCoopBoard(hostBoard: String, guestBoard: String?): String =
        sharedBoardOrder[maxOf(0, sharedBoardOrder.indexOf(hostBoard), sharedBoardOrder.indexOf(guestBoard))]

    private data class Totals(
        val finish: Int, val solo: Int, val coop: Int, val race: Int,
        val dailyDays: Int, val mediumSolo: Int, val hardSolo: Int, val cleanSolo: Int
    ) {
        fun challengeCount(challenge: Challenge): Int = when (challenge) {
            Challenge.DAILY_DAYS -> dailyDays
            Challenge.MEDIUM_SOLO -> mediumSolo
            Challenge.HARD_SOLO -> hardSolo
            Challenge.CLEAN_SOLO -> cleanSolo
        }
    }

    /** One deduplicated pass across locally stored records for every catalog item. */
    private fun totals(records: List<GameRecord>): Totals {
        var finish = 0
        var solo = 0
        var coop = 0
        var race = 0
        var mediumSolo = 0
        var hardSolo = 0
        var cleanSolo = 0
        val dailyDates = HashSet<String>()
        for (record in records.distinctBy { it.id }) {
            when {
                record.mode == SOLO && record.result == "SOLVED" -> {
                    finish++; solo++
                    when (record.difficulty?.uppercase()) {
                        "MEDIUM" -> mediumSolo++
                        "HARD" -> hardSolo++
                    }
                    if (!record.assisted && record.hintsUsed == 0 && record.checksUsed == 0) cleanSolo++
                }
                record.mode == DAILY && record.result == "SOLVED" -> {
                    finish++
                    val day = record.dailyDate
                    // A valid date is required; a date-less legacy record is
                    // still a completed puzzle but not a distinct Daily day.
                    if (day != null && runCatching { java.time.LocalDate.parse(day) }.isSuccess)
                        dailyDates.add(day)
                }
                record.mode == COOP && record.result == "SOLVED" -> { finish++; coop++ }
                record.mode == VERSUS && record.result == "WIN" -> { finish++; race++ }
            }
        }
        return Totals(finish, solo, coop, race, dailyDates.size, mediumSolo, hardSolo, cleanSolo)
    }

    private fun progress(entry: CatalogEntry, totals: Totals, earned: Boolean = false): CosmeticProgress {
        // Starter items retain 0/0 progress, even for veteran profiles.
        val current = if (entry.target == 0) 0 else when (entry.milestone) {
            Milestone.FINISH -> totals.finish
            Milestone.SOLO -> totals.solo
            Milestone.COOP -> totals.coop
            Milestone.RACE -> totals.race
        }
        val alternate = extraGoals[entry.id]
        return CosmeticProgress(current, entry.target, earned,
            alternate?.let { totals.challengeCount(it.kind) } ?: 0,
            alternate?.target ?: 0)
    }

    fun completedPuzzles(records: List<GameRecord>): Int = totals(records).finish

    /** The same milestone controls the preview and the equip permission. */
    fun progress(id: String, records: List<GameRecord>, owned: Collection<String> = emptyList()): CosmeticProgress? =
        entries[id]?.let { progress(it, totals(records), id in owned) }

    /** Collection-wide progress with one record scan, even as new items arrive. */
    fun progressAll(records: List<GameRecord>, owned: Collection<String> = emptyList()): Map<String, CosmeticProgress> {
        val counts = totals(records)
        return entries.mapValues { (id, entry) -> progress(entry, counts, id in owned) }
    }

    fun valid(a: Appearance): Boolean = inks.any { it.id == a.ink } &&
        boards.any { it.id == a.board } && fonts.any { it.id == a.font } &&
        emblems.any { it.id == a.emblem } && (a.portrait.isEmpty() || portraits.any { it.id == a.portrait })
    fun allowed(id: String, records: List<GameRecord>, owned: Collection<String> = emptyList()): Boolean =
        progress(id, records, owned)?.unlocked == true
    fun accessible(a: Appearance, records: List<GameRecord>, owned: Collection<String> = emptyList()): Appearance = Appearance(
        ink = a.ink.takeIf { allowed(it, records, owned) } ?: "forest",
        board = a.board.takeIf { allowed(it, records, owned) } ?: "classic",
        font = a.font.takeIf { allowed(it, records, owned) } ?: "clear",
        emblem = a.emblem.takeIf { allowed(it, records, owned) } ?: "tile",
        portrait = a.portrait.takeIf { it.isEmpty() || (portraits.any { item -> item.id == it } && allowed(it, records, owned)) } ?: ""
    )
}

@Serializable data class ChatLine(
    val id: String, val player: String, val text: String, val atMs: Long
)

/** Stable semantic IDs; English labels remain the transitional wire representation. */
enum class QuickChatGroup { RESPONSE, TEAMWORK, FRIENDLY }
enum class QuickChatPhrase(val id: String, val label: String, val group: QuickChatGroup) {
    YES("yes", "Yes", QuickChatGroup.RESPONSE),
    NO("no", "No", QuickChatGroup.RESPONSE),
    UNSURE("unsure", "Not sure", QuickChatGroup.RESPONSE),
    OKAY("okay", "Okay!", QuickChatGroup.RESPONSE),
    NEED_HELP("need_help", "Need help?", QuickChatGroup.TEAMWORK),
    GOT_IT("got_it", "I've got it!", QuickChatGroup.TEAMWORK),
    NICE_MOVE("nice_move", "Nice move!", QuickChatGroup.FRIENDLY),
    GOOD_GAME("good_game", "Good game!", QuickChatGroup.FRIENDLY),
    THANKS("thanks", "Thank you!", QuickChatGroup.FRIENDLY),
    REMATCH("rematch", "Rematch?", QuickChatGroup.FRIENDLY),
    TAKE_YOUR_TIME("take_your_time", "Take your time.", QuickChatGroup.FRIENDLY),
    BE_RIGHT_BACK("be_right_back", "Be right back.", QuickChatGroup.FRIENDLY),
    READY("ready", "I'm ready!", QuickChatGroup.FRIENDLY),
    SORRY("sorry", "Sorry!", QuickChatGroup.FRIENDLY)
}
/** Stable reaction IDs independent of temporary glyphs and future artwork. */
enum class GridMatesEmote(val id: String, val label: String, val glyph: String) {
    HAPPY("happy", "Happy", "😊"),
    APPROVE("approve", "Approval", "👍"),
    DISAPPROVE("disapprove", "Disapproval", "👎"),
    APPRECIATION("appreciation", "Appreciation", "❤️"),
    THINKING("thinking", "Thinking", "🤔"),
    CELEBRATION("celebration", "Celebration", "🎉"),
    OOPS("oops", "Oops", "😅"),
    GREETING("greeting", "Greeting", "👋"),
    CONFUSED("confused", "Confused", "❓"),
    APPLAUSE("applause", "Applause", "👏"),
    BREAK("break", "Taking a break", "☕"),
    READY("ready", "Ready", "✔️");

    val wireValue: String get() = "emote:" + id
    companion object {
        fun fromWire(value: String): GridMatesEmote? = entries.firstOrNull { it.wireValue == value }
    }
}

object QuickChat {
    val phrases: List<String> = QuickChatPhrase.entries.map { it.label }
    fun byId(id: String): QuickChatPhrase? = QuickChatPhrase.entries.firstOrNull { it.id == id }
    fun isPreset(text: String): Boolean = QuickChatPhrase.entries.any { it.label == text } || GridMatesEmote.fromWire(text) != null
}

object ChatRules {
    const val MAX_LENGTH = 160
    const val HISTORY_LIMIT = 30
    const val MIN_INTERVAL_MS = 650L
    fun clean(raw: String): String = raw.filter { it == ' ' || !it.isISOControl() }
        .trim().take(MAX_LENGTH)
}
