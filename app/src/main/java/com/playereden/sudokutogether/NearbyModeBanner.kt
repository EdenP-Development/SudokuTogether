package com.playereden.sudokutogether

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
    // Versus V7 is a new 1774x887 original forest scene with the exact
    // canonical Collection Fox/Owl portraits composited behind verified 9x9
    // decorative boards. Keep V5 assets in repo for safe visual comparison.
    // Co-op continues using its original approved V5 banner unchanged.
    val context = LocalContext.current
    val existingV5Name = if (together) "gm_mode_coop_woodland_v5_hd"
        else "gm_mode_versus_woodland_v5_hd"
    val existingV5Id = context.resources.getIdentifier(
        existingV5Name, "drawable", context.packageName
    )
    val usingVersusV7 = !together
    val backgroundId = when {
        usingVersusV7 -> R.drawable.gm_mode_versus_woodland_v7_hd
        existingV5Id != 0 -> existingV5Id
        else -> R.drawable.gm_woodland_storybook_hd_v4
    }
    val artworkIncludesMascots = usingVersusV7 || existingV5Id != 0
    val eyebrow = if (together) "PLAY TOGETHER" else "FRIENDLY COMPETITION"
    val heading = if (together) "Shared board" else "Race to finish"

    Box(modifier.height(if (largeText) 128.dp else 137.dp)
        .clip(RoundedCornerShape(topStart = 19.dp, topEnd = 19.dp))
        .testTag(if (together) "nearby_art_coop" else "nearby_art_versus")) {
        Image(
            painter = painterResource(backgroundId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().testTag("nearby_art_image")
        )
        // The V6 Versus asset includes canonical mascots; Co-op V5 includes
        // its existing mascots. Never overlay another Fox/Owl on either image.
        if (artworkIncludesMascots) {
            Box(Modifier.matchParentSize().testTag(
                if (usingVersusV7) "nearby_art_v7" else "nearby_art_v5"
            ))
        } else {
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
        }
        // Native text stays readable; none of its words are baked into art.
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            0f to NightForest.copy(alpha = .70f),
            .54f to NightForest.copy(alpha = .08f),
            1f to Color.Transparent
        )))
        // Co-op's approved illustration places the fox and shared board close
        // to the middle of the banner. Reserve a narrower text-only zone
        // instead of letting "Shared board" cross the character's face.
        val titleSafeWidth = if (together) .48f else .59f
        Column(Modifier.fillMaxHeight().fillMaxWidth(titleSafeWidth)
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
                fontSize = if (largeText) 15.sp else if (together) 21.sp else 21.sp,
                lineHeight = if (largeText) 19.sp else 25.sp,
                // A small dark halo preserves white type over woodland detail,
                // without embedding text into the image or dulling the whole scene.
                style = if (together) TextStyle(shadow = Shadow(
                    color = NightForest.copy(alpha = .96f),
                    offset = Offset(0f, 1.2f), blurRadius = 2.8f
                )) else TextStyle.Default,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }
    }
}
