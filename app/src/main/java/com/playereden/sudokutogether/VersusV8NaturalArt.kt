package com.playereden.sudokutogether

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Versus V8-B: large on-model Collection Fox/Owl layered BEHIND the two
 * perspective-correct 9x9 board stations already in the approved original
 * V7 background source. Do not draw another board over the scene.
 *
 * The composed asset is still owner-review-only. Exact PNG pixels untouched.
 */
@Composable internal fun VersusV8NaturalArt(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.testTag("versus_v8b_natural_art")) {
        val mascotSize = minOf(88.dp, maxHeight * .60f, maxWidth * .26f)
        // Scene boards live at approximately 66% and 86% of the full banner
        // width. These face centers align the characters with their stations.
        val foxLeft = maxWidth * .66f - mascotSize / 2
        val owlLeft = maxWidth * .86f - mascotSize / 2
        Image(
            painterResource(R.drawable.woodland_fox),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.offset(x = foxLeft, y = 6.dp)
                .size(mascotSize).testTag("versus_v8b_fox")
        )
        Image(
            painterResource(R.drawable.woodland_owl),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.offset(x = owlLeft, y = 6.dp)
                .size(mascotSize).testTag("versus_v8b_owl")
        )
    }
}
