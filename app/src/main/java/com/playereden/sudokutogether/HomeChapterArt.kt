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
 * An actual illustrated scene, not a card inside another card.
 * The environmental artwork is a separate, compressed resource. All mascots
 * remain the existing canonical Collection PNGs, never redrawn into scenery.
 * No fixed Sudoku board, fictional progress, text baked in the image or
 * interactive targets rendered as decorative artwork.
 */
@Composable internal fun HomeChapterArt(appearance: Appearance, modifier: Modifier = Modifier) {
    val largeText = LocalDensity.current.fontScale > 1.3f
    val portraitId = appearance.portrait
    val portraitDrawable = refinedAnimalDrawable(portraitId, portraitRefinementEnabled())
    val habitat = companionHabitat(portraitId, portraitDrawable != null)
    val woodland = habitat == CompanionHabitat.DEFAULT || habitat == CompanionHabitat.WOODLAND
    val companion = habitat.caption

    Box(modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        .testTag("home_chapter_art")) {
        if (woodland) {
            Image(
                painter = painterResource(R.drawable.gm_woodland_storybook_hd_v4),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().testTag("home_woodland_backdrop")
            )
        } else {
            // Non-Woodland appearances remain neutral until their own illustrated masters are approved.
            Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
                listOf(Color(0xFF527467), Color(0xFFDCE9DE), Color(0xFFF2EBD7))
            )))
        }
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            listOf(NightForest.copy(alpha = if (woodland) .68f else .42f),
                NightForest.copy(alpha = .13f), Color.Transparent)
        )))
        Row(
            Modifier.fillMaxSize().padding(start = 15.dp, end = 6.dp,
                top = 12.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("YOUR PUZZLE CORNER", color = OnForest, fontSize = 10.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp,
                    lineHeight = 14.sp, maxLines = 2)
                if (!largeText) {
                    Text(companion, color = OnForest.copy(alpha = .95f),
                        fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp, lineHeight = 19.sp, maxLines = 3)
                }
            }
            Box(Modifier.width(if (largeText) 110.dp else 155.dp).fillMaxHeight(),
                contentAlignment = Alignment.BottomEnd) {
                if (portraitDrawable == null) {
                    Image(painterResource(R.drawable.woodland_owl), contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.BottomStart)
                            .size(if (largeText) 56.dp else 80.dp)
                            .testTag("home_chapter_owl"))
                }
                Box(Modifier.align(Alignment.BottomEnd)
                    .size(if (largeText) 91.dp else 117.dp)
                    .testTag("home_portrait_" + portraitId.ifBlank { "default_fox" }),
                    contentAlignment = Alignment.Center) {
                    Image(
                        painterResource(portraitDrawable ?: R.drawable.woodland_fox),
                        contentDescription = null, contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().testTag("home_chapter_featured")
                    )
                }
            }
        }
    }
}
