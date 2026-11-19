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
    // V3: native vector backdrop authored for this wide card avoids enlarging the
    // 512x256 Home preview. Scale-independent, deliberately simpler motifs sit
    // in the right-side safe zone; native text and gameplay controls stay intact.
    val motif = if (together) R.drawable.gm_mode_coop_symbol_v3
        else R.drawable.gm_mode_versus_symbol_v3
    val eyebrow = if (together) "PLAY TOGETHER" else "FRIENDLY COMPETITION"
    val heading = if (together) "Shared board" else "Race to finish"

    Box(modifier.height(if (largeText) 128.dp else 137.dp)
        .clip(RoundedCornerShape(topStart = 19.dp, topEnd = 19.dp))
        .testTag(if (together) "nearby_art_coop" else "nearby_art_versus")) {
        Image(
            painter = painterResource(R.drawable.gm_nearby_woodland_v3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().testTag("nearby_art_image")
        )
        Image(
            painter = painterResource(motif),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.CenterEnd)
                .width(if (largeText) 112.dp else 144.dp)
                .fillMaxHeight()
                .padding(end = 7.dp, top = 8.dp, bottom = 8.dp)
                .testTag("nearby_art_motif")
        )
        // Native text stays readable; none of its words are baked into art.
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            0f to NightForest.copy(alpha = .79f),
            .54f to NightForest.copy(alpha = .16f),
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
