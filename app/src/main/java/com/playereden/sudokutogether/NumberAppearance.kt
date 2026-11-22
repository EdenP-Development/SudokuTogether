package com.playereden.sudokutogether

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.playereden.sudokutogether.core.Appearance

private val clearNumbers = FontFamily(
    Font(R.font.noto_sans_regular),
    Font(R.font.noto_sans_bold, weight = FontWeight.Bold)
)
private val bookishNumbers = FontFamily(Font(R.font.noto_serif_regular))
private val terminalNumbers = FontFamily(Font(R.font.liberation_mono_regular))
private val roundedNumbers = FontFamily(Font(R.font.nunito_bold))
private val pencilNumbers = FontFamily(Font(R.font.patrick_hand_regular))
// Official OFL font binaries are vendored locally with full license notices.
private val typewriterNumbers = FontFamily(Font(R.font.typewriter_courier_prime))
private val editorialNumbers = FontFamily(Font(R.font.editorial_literata))
private val softPrintNumbers = FontFamily(Font(R.font.soft_print_atkinson_next))
private val tallPrintNumbers = FontFamily(Font(R.font.tall_print_oswald))
private val defaultAppearance = Appearance()

internal fun fontFor(id: String): FontFamily = when (id) {
    "serif" -> bookishNumbers
    "mono" -> terminalNumbers
    "rounded" -> roundedNumbers
    "pencil" -> pencilNumbers
    "typewriter" -> typewriterNumbers
    "editorial" -> editorialNumbers
    "soft_print" -> softPrintNumbers
    "tall_print" -> tallPrintNumbers
    else -> clearNumbers
}

internal fun Appearance.withNumberDisplay(uniformNumbers: Boolean): Appearance =
    if (uniformNumbers) copy(ink = defaultAppearance.ink, font = defaultAppearance.font) else this
