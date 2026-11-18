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
import androidx.compose.ui.unit.*
import com.playereden.sudokutogether.core.Appearance

/**
 * A compact, decorative game-mode scene; it REPLACES the large plain heading,
 * it is not a new setup step. Real game state, host/join actions and controls
 * stay as native Compose components below this artwork.
 *
 * Original Woodland environment and original bundled character PNGs.
 * No fabricated connected partner, room code, puzzle, or owned reward.
 */
@Composable internal fun NearbyModeScene(
    mode: String,
    appearance: Appearance,
    modifier: Modifier = Modifier
) {
    val isCoop = mode == "together"
    val largeText = LocalDensity.current.fontScale > 1.3f
    val portrait = animalDrawable(appearance.portrait)
    val showWoodlandScene = portrait == null || appearance.portrait.startsWith("woodland_")
    val mainCharacter = portrait ?: R.drawable.woodland_fox
    val heading = if (isCoop) "Shared board" else "Race to finish"
    val subtitle = if (isCoop) "Solve one puzzle together · Same Wi-Fi"
                   else "Race on separate boards · Same Wi-Fi"

    Box(modifier.clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
        .testTag("nearby_mode_scene")) {
        if (showWoodlandScene) {
            Image(painterResource(R.drawable.gm_home_chapter_woodland_v1),
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().testTag("nearby_woodland_backdrop"))
        } else {
            // Until matching Ocean/Garden environments exist, keep the portrait
            // in an intentionally neutral scene rather than a false forest set.
            Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
                listOf(Color(0xFF476D5D), Color(0xFF92AC9B), Color(0xFFF2EBD7))
            )))
        }
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            listOf(NightForest.copy(alpha = .85f),
                NightForest.copy(alpha = .44f),
                NightForest.copy(alpha = .04f))
        )))
        Row(Modifier.fillMaxSize().padding(start = 15.dp, end = 6.dp, top = 8.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(if (isCoop) "CO-OP" else "VERSUS",
                    color = GoldTint, fontWeight = FontWeight.Bold,
                    fontSize = 10.sp, lineHeight = 13.sp, letterSpacing = 1.sp)
                Text(heading, color = OnForest, fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = if (largeText) 15.sp else 22.sp,
                    lineHeight = if (largeText) 19.sp else 27.sp, maxLines = 2,
                    modifier = Modifier.testTag("nearby_mode_title"))
                if (!largeText) {
                    Text(subtitle, color = OnForest.copy(alpha = .93f),
                        fontSize = 10.sp, lineHeight = 14.sp, maxLines = 2)
                }
            }
            Box(Modifier.width(if (largeText) 95.dp else 132.dp).fillMaxHeight(),
                contentAlignment = Alignment.BottomEnd) {
                if (portrait == null) {
                    Image(painterResource(R.drawable.woodland_owl),
                        contentDescription = null, contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.BottomStart)
                            .size(if (largeText) 53.dp else 73.dp))
                }
                Image(painterResource(mainCharacter), contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .size(if (largeText) 82.dp else 106.dp)
                        .testTag("nearby_mode_character"))
            }
        }
    }
}
