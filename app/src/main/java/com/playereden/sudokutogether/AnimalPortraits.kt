package com.playereden.sudokutogether

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.playereden.sudokutogether.core.*

internal fun animalDrawable(id: String): Int? = when (id) {
    "woodland_owl" -> R.drawable.woodland_owl
    "woodland_fox" -> R.drawable.woodland_fox
    "woodland_rabbit" -> R.drawable.woodland_rabbit
    "woodland_bear" -> R.drawable.woodland_bear
    "woodland_badger" -> R.drawable.woodland_badger
    "woodland_hedgehog" -> R.drawable.woodland_hedgehog
    "woodland_robin" -> R.drawable.woodland_robin
    "woodland_deer" -> R.drawable.woodland_deer
    "woodland_squirrel" -> R.drawable.woodland_squirrel
    "woodland_otter" -> R.drawable.woodland_otter
    "woodland_tortoise" -> R.drawable.woodland_tortoise
    "woodland_cat" -> R.drawable.woodland_cat
    "woodland_raccoon" -> R.drawable.woodland_raccoon
    "woodland_chipmunk" -> R.drawable.woodland_chipmunk
    "woodland_beaver" -> R.drawable.woodland_beaver
    "woodland_woodpecker" -> R.drawable.woodland_woodpecker
    "woodland_skunk" -> R.drawable.woodland_skunk
    "ocean_octopus" -> R.drawable.ocean_octopus
    "ocean_seal" -> R.drawable.ocean_seal
    "ocean_hermit_crab" -> R.drawable.ocean_hermit_crab
    "ocean_manta_ray" -> R.drawable.ocean_manta_ray
    "ocean_jellyfish" -> R.drawable.ocean_jellyfish
    "ocean_narwhal" -> R.drawable.ocean_narwhal
    "ocean_sea_turtle" -> R.drawable.ocean_sea_turtle
    "ocean_seahorse" -> R.drawable.ocean_seahorse
    "ocean_dolphin" -> R.drawable.ocean_dolphin
    "ocean_seashell" -> R.drawable.ocean_seashell
    "ocean_clownfish" -> R.drawable.ocean_clownfish
    "garden_honeybee" -> R.drawable.garden_honeybee
    "garden_ladybug" -> R.drawable.garden_ladybug
    "garden_butterfly" -> R.drawable.garden_butterfly
    "garden_dragonfly" -> R.drawable.garden_dragonfly
    "garden_firefly" -> R.drawable.garden_firefly
    "garden_sunflower" -> R.drawable.garden_sunflower
    "garden_strawberry" -> R.drawable.garden_strawberry
    "garden_watering_can" -> R.drawable.garden_watering_can
    "garden_potted_tulip" -> R.drawable.garden_potted_tulip
    "farm_cow" -> R.drawable.farm_cow
    "farm_chicken" -> R.drawable.farm_chicken
    "farm_sheep" -> R.drawable.farm_sheep
    "farm_pig" -> R.drawable.farm_pig
    "farm_pumpkin" -> R.drawable.farm_pumpkin
    "farm_hay_bale" -> R.drawable.farm_hay_bale
    else -> null
}

/** Preserve original portrait resources and stable IDs. Review replacements
 * in the combined RC3 app, including the equipped Home companion. */
internal fun refinedAnimalDrawable(id: String, enabled: Boolean): Int? =
    if (enabled) when (id) {
        "woodland_woodpecker" -> R.drawable.gm_portrait_woodpecker_b
        "garden_firefly" -> R.drawable.gm_portrait_firefly_v2
        else -> animalDrawable(id)
    } else animalDrawable(id)

@Composable internal fun portraitRefinementEnabled(): Boolean =
    LocalContext.current.resources.getBoolean(R.bool.gridmates_portrait_refinement_enabled)

internal fun refinedPortraitScale(id: String, enabled: Boolean): Float =
    if (enabled && id in setOf("woodland_woodpecker", "garden_firefly")) 1f else portraitDisplayScale(id)

internal fun identityStyleName(appearance: Appearance): String {
    Cosmetics.farmPortraits.firstOrNull { it.id == appearance.portrait }?.let { return "${it.name} · Farm" }
    Cosmetics.gardenPortraits.firstOrNull { it.id == appearance.portrait }?.let { return "${it.name} · Garden" }
    Cosmetics.oceanPortraits.firstOrNull { it.id == appearance.portrait }?.let { return "${it.name} · Ocean" }
    Cosmetics.woodlandPortraits.firstOrNull { it.id == appearance.portrait }?.let { return "${it.name} · Woodland" }
    return "${emblemName(appearance.emblem)} · Basic"
}

/**
 * Four late Woodland candidate PNGs use 512px canvases with substantially more
 * transparent space than the older near-full 384px artwork. Normalize only
 * their DRAWN size in the circle; keep original bytes/IDs fully unchanged.
 * Source image hashes continue to be verified by the Beta 2 CI gate.
 */
internal fun portraitDisplayScale(id: String): Float = when (id) {
    "woodland_chipmunk" -> 1.30f
    "woodland_beaver" -> 1.28f
    "woodland_woodpecker" -> 1.26f
    "woodland_skunk" -> 1.28f
    else -> 1f
}

@Composable internal fun AnimalPortrait(appearance: Appearance, modifier: Modifier = Modifier.size(48.dp)) {
    val refinement = portraitRefinementEnabled()
    val drawable = refinedAnimalDrawable(appearance.portrait, refinement)
    // Unapproved designs show only inside their separate QA build; all normal
    // users retain existing Basic emblems and their saved chosen IDs.
    val concept = if (appearance.portrait.isEmpty() &&
        LocalContext.current.resources.getBoolean(R.bool.gridmates_insignia_preview_enabled)
    ) candidateInsigniaDrawable(appearance.emblem) else null
    BoxWithConstraints(modifier.sizeIn(minWidth = 24.dp, minHeight = 24.dp, maxWidth = 140.dp, maxHeight = 140.dp)
        .clip(CircleShape).background(GreenTint)
        .testTag("portrait_image_${appearance.portrait.ifEmpty { appearance.emblem }}"), contentAlignment = Alignment.Center) {
        if (drawable != null) Image(painterResource(drawable), contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(maxWidth * .08f)
                .scale(refinedPortraitScale(appearance.portrait, refinement)),
            contentScale = ContentScale.Fit)
        else if (concept != null) Image(painterResource(concept), contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(maxWidth * .08f),
            contentScale = ContentScale.Fit)
        else Text(emblemSymbol(appearance.emblem), color = inkFor(appearance.ink, false),
            fontSize = (minOf(maxWidth.value, maxHeight.value) * .64f / LocalDensity.current.fontScale.coerceAtLeast(1f)).sp)
    }
}
