package com.playereden.sudokutogether

import android.animation.ValueAnimator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Optional series stage; Sudoku is the sole approved two-stage pilot.
internal fun startupBrandingStages(seriesDrawable: Int? = null): List<Int> =
    listOfNotNull(R.drawable.spudacus_v1_full_transparent, seriesDrawable)

@Composable
internal fun StartupBranding(showInitially: Boolean, seriesDrawable: Int? = null) {
    val stages = remember(seriesDrawable) { startupBrandingStages(seriesDrawable) }
    var stage by remember { mutableIntStateOf(if (showInitially && ValueAnimator.areAnimatorsEnabled()) 0 else stages.size) }
    val opacity = remember { Animatable(0f) }
    val visible = stage < stages.size
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_STOP) stage = stages.size
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    BackHandler(visible) { stage = stages.size }
    LaunchedEffect(stage) {
        if (stage >= stages.size) return@LaunchedEffect
        if (!ValueAnimator.areAnimatorsEnabled()) {
            stage = stages.size
            return@LaunchedEffect
        }
        opacity.snapTo(0f)
        opacity.animateTo(1f, tween(250))
        delay(1000)
        opacity.animateTo(0f, tween(250))
        stage++
    }
    if (visible) {
        // Keep the cream surface continuous between both stages.
        Box(
            Modifier.fillMaxSize().background(Color(0xFFFAE7CD))
                .clickable(onClickLabel = "Skip startup") { stage = stages.size }
                .testTag("startup_branding"),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(stages[stage]),
                contentDescription = if (stage == 0) "Spudacus" else "GridMates",
                contentScale = ContentScale.Fit,
                modifier = Modifier.safeDrawingPadding().padding(24.dp)
                    .widthIn(max = 420.dp).fillMaxWidth(.82f)
                    .heightIn(max = 260.dp).alpha(opacity.value)
            )
        }
    }
}
