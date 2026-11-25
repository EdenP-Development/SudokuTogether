package com.playereden.sudokutogether

/** Presentation only. Stable cosmetic IDs and save data remain unchanged.
 * Missing resources and unknown collections use the original default scene.
 */
internal enum class CompanionHabitat(val caption: String) {
    DEFAULT("A quiet moment, one grid at a time"),
    WOODLAND("With your woodland companion"),
    OCEAN("With your ocean companion"),
    GARDEN("With your garden companion"),
    FARM("With your farm companion")
}

internal fun companionHabitat(portraitId: String, hasDrawable: Boolean): CompanionHabitat {
    if (!hasDrawable) return CompanionHabitat.DEFAULT
    return when {
        portraitId.startsWith("woodland_") -> CompanionHabitat.WOODLAND
        portraitId.startsWith("ocean_") -> CompanionHabitat.OCEAN
        portraitId.startsWith("garden_") -> CompanionHabitat.GARDEN
        portraitId.startsWith("farm_") -> CompanionHabitat.FARM
        else -> CompanionHabitat.DEFAULT
    }
}
