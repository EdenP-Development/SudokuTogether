package com.playereden.sudokutogether

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Proposed Basic badge art, tied to the current stable emblem ID.
 * Gated with -PgridmatesBeta2InsigniaPreview=true. Regular builds continue
 * displaying the existing emblemSymbol() glyphs byte-for-byte.
 *
 * SVG-like vector drawable resources are committed as source XML, not
 * flattened screenshots, generated fonts, or third-party icon packs.
 */
internal fun candidateInsigniaDrawable(id: String): Int? = when (id) {
    "constellation" -> R.drawable.gm_insignia_candidate_constellation
    "sunrise" -> R.drawable.gm_insignia_candidate_sunrise
    "focus" -> R.drawable.gm_insignia_candidate_focus
    "compass" -> R.drawable.gm_insignia_candidate_compass
    "moonlight" -> R.drawable.gm_insignia_candidate_moonlight
    "grid_master" -> R.drawable.gm_insignia_candidate_grid_master
    else -> null
}

@Composable internal fun insigniaArtReviewEnabled(): Boolean =
    LocalContext.current.resources.getBoolean(R.bool.gridmates_insignia_preview_enabled)

/** Each candidate is compared with the REAL existing Unicode glyph on device. */
@Composable internal fun InsigniaCandidateComparison(id: String) {
    val art = candidateInsigniaDrawable(id) ?: return
    if (!insigniaArtReviewEnabled()) return

    Column(
        Modifier.fillMaxWidth().testTag("insignia_comparison_$id"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("ART STUDY · CURRENT / PROPOSED",
            color = Muted, fontSize = 11.sp, letterSpacing = .5.sp,
            fontWeight = FontWeight.SemiBold)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(Modifier.size(92.dp).clip(CircleShape).background(GreenTint),
                    contentAlignment = Alignment.Center) {
                    Text(emblemSymbol(id), color = Forest, fontSize = 52.sp)
                }
                Text("CURRENT", color = Muted, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(Modifier.size(92.dp).clip(CircleShape).background(GreenTint),
                    contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(art),
                        contentDescription = null, contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                            .testTag("insignia_candidate_art_$id"))
                }
                Text("PROPOSED", color = Forest, fontSize = 10.sp)
            }
        }
        Text("Concept artwork only. Original reward ID, unlock progress and ownership stay unchanged.",
            color = Muted, fontSize = 11.sp)
    }
}
