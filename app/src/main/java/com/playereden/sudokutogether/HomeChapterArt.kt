package com.playereden.sudokutogether

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Asset-first chapter spotlight. Uses ONLY the actual packaged 22 collectible
 * portraits, not another hand-drawn or AI-generated approximation of them.
 *
 * The default Fox/Owl illustrations are decorative, never represented as
 * equipped or earned. The selected portrait is used when available.
 * No fake puzzle digits, fixed progress, baked-in controls, or asset-ID changes.
 */
@Composable internal fun HomeChapterArt(
    appearance: Appearance,
    modifier: Modifier = Modifier
) {
    val largeText = LocalDensity.current.fontScale > 1.3f
    val chosenId = appearance.portrait
    val chapter = when {
        chosenId.startsWith("ocean_") -> "Ocean"
        chosenId.startsWith("garden_") -> "Garden"
        chosenId.startsWith("woodland_") -> "Woodland"
        else -> "Woodland"
    }
    val selectedDrawable = animalDrawable(chosenId)
    val featuredDrawable = selectedDrawable ?: R.drawable.woodland_fox
    Surface(
        modifier = modifier.testTag("home_chapter_art"),
        shape = RoundedCornerShape(17.dp),
        color = Color(0xFFF4EBD7),
        border = BorderStroke(1.dp, Gold.copy(alpha = .56f))
    ) {
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
            listOf(Color(0xFFF8F1E3), Color(0xFFE2EBD9))
        ))) {
            Row(
                Modifier.fillMaxSize().padding(start = 12.dp, end = 6.dp, top = 7.dp, bottom = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (!largeText) {
                        Text("COLLECTION SPOTLIGHT", color = Gold, fontSize = 9.sp,
                            fontWeight = FontWeight.Bold, letterSpacing = .7.sp,
                            lineHeight = 12.sp)
                    }
                    Text("$chapter companions", color = NightForest,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = if (largeText) 12.sp else 18.sp,
                        lineHeight = if (largeText) 17.sp else 22.sp,
                        maxLines = 2)
                    if (!largeText) {
                        Text(if (selectedDrawable == null) "Meet Fox and Owl"
                            else "Featuring your chosen portrait",
                            color = Forest.copy(alpha = .86f),
                            fontSize = 10.sp, lineHeight = 14.sp)
                    }
                }
                Box(Modifier.width(134.dp).fillMaxHeight(),
                    contentAlignment = Alignment.BottomEnd) {
                    if (selectedDrawable == null) {
                        Image(painterResource(R.drawable.woodland_owl),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.align(Alignment.BottomStart)
                                .size(73.dp).testTag("home_chapter_owl"))
                    }
                    Image(painterResource(featuredDrawable),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .size(108.dp).testTag("home_chapter_featured"))
                }
            }
        }
    }
}
