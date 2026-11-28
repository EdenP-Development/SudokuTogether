package com.playereden.sudokutogether

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource

internal fun approvedGridMatesHeaderDrawable(): Int = R.drawable.gridmates_header_approved

// SUDOKU_GRID is retained as the existing default style identifier for callers.
internal enum class GridMatesMarkStyle { SUDOKU_GRID, TILE_DOT, SHARED_WINDOW }

@Composable internal fun GridMatesMark(modifier: Modifier, style: GridMatesMarkStyle = GridMatesMarkStyle.SUDOKU_GRID) {
    if (style == GridMatesMarkStyle.SUDOKU_GRID) {
        Box(modifier.clip(RoundedCornerShape(percent = 22)).background(Forest)) {
            Image(painterResource(approvedGridMatesHeaderDrawable()), contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.matchParentSize().testTag("gridmates_approved_header_mark"))
        }
        return
    }
    Canvas(modifier) {
        scale(size.width / 128f, size.height / 128f, pivot = Offset.Zero) {
            val accent = Color(0xFFE5C982)
            drawRoundRect(Forest, size = Size(128f, 128f), cornerRadius = CornerRadius(28f))
            if (style == GridMatesMarkStyle.TILE_DOT) {
                val g = Path().apply {
                    moveTo(78f, 39f); lineTo(63f, 39f)
                    cubicTo(46f, 39f, 35f, 50f, 35f, 66f)
                    cubicTo(35f, 82f, 46f, 93f, 63f, 93f)
                    lineTo(92f, 93f); lineTo(92f, 67f); lineTo(67f, 67f)
                }
                drawPath(g, OnForest, style = Stroke(12f, cap = StrokeCap.Square, join = StrokeJoin.Round))
                drawRoundRect(accent, Offset(86f, 28f), Size(15f, 15f), CornerRadius(3f))
            } else {
                drawRoundRect(OnForest, Offset(30f, 30f), Size(68f, 68f), CornerRadius(10f), style = Stroke(8f))
                drawLine(OnForest, Offset(64f, 30f), Offset(64f, 98f), strokeWidth = 8f)
                drawLine(OnForest, Offset(30f, 64f), Offset(98f, 64f), strokeWidth = 8f)
                drawRoundRect(accent, Offset(72f, 72f), Size(31f, 31f), CornerRadius(8f))
            }
        }
    }
}
