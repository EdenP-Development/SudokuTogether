package com.playereden.sudokutogether

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

/**
 * Companion piece to the asset-backed Woodland Home.
 *
 * Only art and title are in this layer. Every control, room value, difficulty
 * and action remains native Compose below it. Art never bakes in puzzle
 * numbers, game state, controls, or invented player identity.
 */
@Composable internal fun NearbyModeBanner(mode: String, modifier: Modifier = Modifier) {
    val together = mode == "together"
    val largeText = LocalDensity.current.fontScale > 1.3f
    // V4 uses the exact high-resolution illustrated Woodland raster used by Home.
    // The actual packaged collectible portraits, not synthetic substitutes or
    // hand-drawn scene symbols, provide the right-side visual accent.
    val eyebrow = if (together) "PLAY TOGETHER" else "FRIENDLY COMPETITION"
    val heading = if (together) "Shared board" else "Race to finish"

    Box(modifier.height(if (largeText) 128.dp else 137.dp)
        .clip(RoundedCornerShape(topStart = 19.dp, topEnd = 19.dp))
        .testTag(if (together) "nearby_art_coop" else "nearby_art_versus")) {
        Image(
            painter = painterResource(R.drawable.gm_woodland_storybook_hd_v4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().testTag("nearby_art_image")
        )
        // Reuse real high-fidelity portrait assets already packaged for Collection.
        // These are decorative mascots, not a claim that a player owns them.
        Box(
            Modifier.align(Alignment.BottomEnd)
                .width(if (largeText) 116.dp else 151.dp)
                .fillMaxHeight()
                .padding(end = 7.dp, top = 6.dp, bottom = 6.dp)
                .testTag("nearby_art_motif"),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (together) {
                Image(
                    painter = painterResource(R.drawable.woodland_owl),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.align(Alignment.BottomStart)
                        .size(if (largeText) 62.dp else 77.dp)
                        .testTag("nearby_art_owl")
                )
                Image(
                    painter = painterResource(R.drawable.woodland_fox),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .size(if (largeText) 87.dp else 106.dp)
                        .testTag("nearby_art_fox")
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.woodland_fox),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.align(Alignment.BottomStart)
                        .size(if (largeText) 75.dp else 89.dp)
                        .testTag("nearby_art_fox")
                )
                Image(
                    painter = painterResource(R.drawable.woodland_owl),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .size(if (largeText) 75.dp else 89.dp)
                        .testTag("nearby_art_owl")
                )
            }
        }
        // Native text stays readable; none of its words are baked into art.
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            0f to NightForest.copy(alpha = .70f),
            .54f to NightForest.copy(alpha = .08f),
            1f to Color.Transparent
        )))
        Column(Modifier.fillMaxHeight().fillMaxWidth(.59f)
            .padding(start = 17.dp, top = 14.dp, bottom = 13.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start) {
            if (!largeText) {
                Text(eyebrow, color = GoldTint,
                    fontWeight = FontWeight.Bold, fontSize = 10.sp,
                    lineHeight = 14.sp, letterSpacing = .8.sp,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(7.dp))
            }
            Text(heading, color = OnForest,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (largeText) 15.sp else 23.sp,
                lineHeight = if (largeText) 19.sp else 27.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }
    }
}
