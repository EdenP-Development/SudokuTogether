package com.playereden.sudokutogether

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.clearAndSetSemantics

/**
 * A small, original illustrated living puzzle book, drawn with responsive vectors.
 * This is a decorative sample grid, NOT the player's current saved board.
 * Every functional button, label and real progress value is drawn by Compose above/below it.
 */
@Composable internal fun LivingBookIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val sx = size.width / 360f
        val sy = size.height / 160f
        fun x(v: Float) = v * sx
        fun y(v: Float) = v * sy
        fun pt(a: Float, b: Float) = Offset(x(a), y(b))
        fun rect(l: Float, t: Float, w: Float, h: Float, color: Color, radius: Float = 0f) =
            drawRoundRect(color, topLeft = pt(l,t), size = Size(x(w),y(h)), cornerRadius = CornerRadius(x(radius)))
        val parchment = Color(0xFFFFFAE9)
        val pageRule = Color(0xFFB6BFAE)
        val brass = Color(0xFFDEB879)
        val dark = Color(0xFF102D27)
        // Deep book cloth, with warm edge glow and restrained etched details.
        drawRoundRect(
            Brush.linearGradient(listOf(Color(0xFF193F35), Color(0xFF285949), Color(0xFF17392F))),
            cornerRadius = CornerRadius(x(16f))
        )
        for (i in 0..5) {
            val row = 18f + i * 23f
            drawLine(brass.copy(alpha = .075f), pt(6f,row), pt(352f,row+3f), x(.65f))
        }
        drawRoundRect(brass.copy(alpha = .45f), pt(7f,7f), Size(x(346f),y(146f)),
            CornerRadius(x(13f)), style = Stroke(width = x(.9f)))
        // A little antique binding and stacked page edges.
        rect(19f, 19f, 325f, 125f, dark.copy(alpha = .35f), 10f)
        rect(24f, 13f, 312f, 132f, Color(0xFF8F7146), 8f)
        rect(29f, 15f, 300f, 126f, Color(0xFFDDD2AA), 7f)
        rect(33f, 17f, 294f, 122f, Color(0xFFE6DCB9), 7f)
        // Two pages meeting at the central binding.
        drawRoundRect(Brush.linearGradient(listOf(Color(0xFFFFF8DE), Color(0xFFF3EBD3))),
            pt(35f, 19f), Size(x(147f),y(116f)), CornerRadius(x(5f)))
        drawRoundRect(Brush.linearGradient(listOf(Color(0xFFF0E5C7), Color(0xFFFFFAE9))),
            pt(180f, 19f), Size(x(144f),y(116f)), CornerRadius(x(5f)))
        drawLine(Color(0xFFB5A580), pt(180f,20f), pt(180f,132f), x(2f))
        drawLine(Color(0xFFFFFFFF).copy(alpha=.7f), pt(182f,23f), pt(182f,128f), x(1f))
        // Illustrated left-hand page: woodland hills, a compass and quiet ruled text.
        val hill = Path().apply {
            moveTo(x(47f),y(108f)); lineTo(x(76f),y(83f)); lineTo(x(88f),y(93f))
            lineTo(x(116f),y(72f)); lineTo(x(154f),y(110f)); close()
        }
        drawPath(hill, Color(0xFFB7C8AE))
        val foreground = Path().apply {
            moveTo(x(43f),y(118f)); lineTo(x(81f),y(105f)); lineTo(x(122f),y(110f))
            lineTo(x(170f),y(97f)); lineTo(x(169f),y(124f)); lineTo(x(44f),y(124f)); close()
        }
        drawPath(foreground, Color(0xFF678875).copy(alpha=.85f))
        for (i in 0..4) {
            val treeX = 52f + i * 25f
            val treeHeight = 15f + (i%3)*4f
            drawLine(Forest, pt(treeX,115f), pt(treeX,115f-treeHeight), x(1.6f))
            val pine = Path().apply {
                moveTo(x(treeX),y(107f-treeHeight)); lineTo(x(treeX-7f),y(115f))
                lineTo(x(treeX+7f),y(115f)); close()
            }
            drawPath(pine, Forest.copy(alpha=.8f))
        }
        drawCircle(brass, x(13f), pt(101f,46f), style=Stroke(x(1.2f)))
        drawLine(Gold, pt(101f,35f),pt(101f,57f),x(1f),cap=StrokeCap.Round)
        drawLine(Gold, pt(90f,46f),pt(112f,46f),x(1f),cap=StrokeCap.Round)
        drawCircle(Gold, x(2f), pt(101f,46f))
        for (i in 0..2) drawLine(Gold.copy(alpha = .25f),pt(57f,65f+i*4f),pt(144f,65f+i*4f),x(.7f))
        // Right page: a readable 9x9 miniature of an illustrative Sudoku.
        val left = 194f; val top = 27f; val side = 114f
        rect(left,top,side,side,parchment,2f)
        for (i in 0..9) {
            val pos=side*i/9f
            val thick=if (i%3==0) 1.25f else .45f
            drawLine(if (i%3==0) Forest else pageRule,pt(left+pos,top),pt(left+pos,top+side),x(thick))
            drawLine(if (i%3==0) Forest else pageRule,pt(left,top+pos),pt(left+side,top+pos),x(thick))
        }
        rect(left+7*side/9f+.9f,top+3*side/9f+.9f,side/9f-1.7f,side/9f-1.7f,
            Color(0xFFEED69D))
        val digits = listOf(
            "800620530","070009010","200000090","020017000",
            "100030960","384200100","000308409","092065783","000940050")
        val inkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(34,79,63)
            typeface = Typeface.create(Typeface.SERIF,Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            textSize = y(9.1f)
        }
        drawIntoCanvas { canvas ->
            for (row in 0..8) for (col in 0..8) {
                val digit=digits[row][col]
                if(digit!='0') {
                    canvas.nativeCanvas.drawText(digit.toString(),
                        x(left+(col+.5f)*side/9f),
                        y(top+(row+.79f)*side/9f),
                        inkPaint)
                }
            }
        }
        // A golden bookmark belongs to the book, not to an on-screen button.
        rect(309f,10f,15f,37f,brass,2f)
        val ribbon = Path().apply {
            moveTo(x(309f),y(45f));lineTo(x(316.5f),y(38f))
            lineTo(x(324f),y(45f));close()
        }
        drawPath(ribbon,Color(0xFF285949))
        drawLine(brass.copy(alpha=.7f),pt(22f,146f),pt(338f,146f),x(.7f))
    }
}
