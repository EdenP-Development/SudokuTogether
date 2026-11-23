package com.playereden.sudokutogether

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * V8-A owner-review experiment. Composes TRUE original Collection mascots
 * and two precise native 9x9 decorative grids over the existing empty
 * forest backdrop. Does not alter any artwork PNG, game board, or LAN code.
 *
 * Intentionally not approved for public Beta 2 until owner phone review.
 */
internal val versusV8GridPositions: List<Float> = (0..9).map { it / 9f }
internal val versusV8MajorLines: List<Int> = listOf(0, 3, 6, 9)

@Composable internal fun VersusV8LiveArt(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.testTag("versus_v8_live_art")) {
        // 60% of the banner belongs to large foreground participants;
        // the native text keeps the 40% dark woodland safe zone.
        val stageWidth = maxWidth * .60f
        val portraitSize = minOf(82.dp, maxHeight * .61f, stageWidth * .43f)
        val boardSize = minOf(68.dp, maxHeight * .49f, stageWidth * .36f)

        Row(
            Modifier.align(Alignment.TopEnd).width(stageWidth)
                .padding(end = 6.dp, top = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painterResource(R.drawable.woodland_fox),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(portraitSize).testTag("versus_v8_fox")
            )
            Image(
                painterResource(R.drawable.woodland_owl),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(portraitSize).testTag("versus_v8_owl")
            )
        }
        // Boards deliberately overlap the lowest part of the sprites so
        // mascots appear seated at their puzzle station rather than floating.
        Row(
            Modifier.align(Alignment.BottomEnd).width(stageWidth)
                .padding(end = 6.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VersusV8MiniBoard(boardSize, "versus_v8_board_fox")
            VersusV8MiniBoard(boardSize, "versus_v8_board_owl")
        }
    }
}

@Composable private fun VersusV8MiniBoard(size: androidx.compose.ui.unit.Dp, tag: String) {
    Box(
        Modifier.size(size)
            .shadow(3.dp, RoundedCornerShape(3.dp))
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0xFF8A542D))
            .border(1.dp, Color(0xFF4A3021), RoundedCornerShape(3.dp))
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize().padding(4.dp)) {
            val paper = Color(0xFFF5E9C9)
            val lines = Color(0xFF204B3B)
            drawRect(paper)
            for (i in 0..9) {
                val major = i % 3 == 0
                val pos = size.width * versusV8GridPositions[i]
                val stroke = if (major) 1.65.dp.toPx() else .48.dp.toPx()
                drawLine(lines, Offset(pos, 0f), Offset(pos, size.height), stroke)
                val ypos = size.height * versusV8GridPositions[i]
                drawLine(lines, Offset(0f, ypos), Offset(size.width, ypos), stroke)
            }
        }
    }
}
