package com.playereden.sudokutogether

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

/** A welcoming visual focal point, not another settings or configuration form. */
@Composable internal fun HomeGameBanner(resumeAvailable: Boolean) {
    BoxWithConstraints(Modifier.fillMaxWidth().testTag("home_game_banner")) {
        val showPuzzleArtwork = maxWidth >= 340.dp && LocalDensity.current.fontScale <= 1.3f
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(23.dp),
            color = GreenTint,
            border = BorderStroke(1.dp, Rule),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 106.dp)
                    .padding(horizontal = 17.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("YOUR PUZZLE CORNER", color = Gold, fontSize = 10.sp, lineHeight = 14.sp,
                        letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
                    Text("Good puzzles.\nGood company.", color = Forest,
                        fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp, lineHeight = 26.sp)
                    Text(if (resumeAvailable) "Your next move is waiting."
                        else "A quiet moment, one grid at a time.",
                        color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
                }
                if (showPuzzleArtwork) HomePuzzleMotif()
            }
        }
    }
}

/** An original, scalable miniature grid for the menu. Not an interactive or solvable puzzle. */
@Composable private fun HomePuzzleMotif() {
    val numbers = listOf("2", "", "", "", "5", "", "", "", "9")
    Column(
        Modifier.size(88.dp)
            .background(Paper, RoundedCornerShape(12.dp))
            .border(1.dp, Forest.copy(alpha = .32f), RoundedCornerShape(12.dp))
            .padding(6.dp)
    ) {
        repeat(3) { row ->
            Row(Modifier.weight(1f)) {
                repeat(3) { col ->
                    val index = row * 3 + col
                    val accented = index == 4
                    Box(
                        Modifier.weight(1f).fillMaxHeight()
                            .background(if (accented) GoldTint else Paper)
                            .border(.5.dp, Rule),
                        contentAlignment = Alignment.Center
                    ) {
                        if (numbers[index].isNotEmpty()) {
                            Text(numbers[index], color = Forest,
                                fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold,
                                fontSize = 15.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
