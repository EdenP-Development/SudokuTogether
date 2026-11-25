package com.playereden.sudokutogether

/** Optional sound feedback must never prevent a puzzle action on unsupported devices. */
internal fun playPuzzleClick(play: () -> Unit) {
    try { play() } catch (_: RuntimeException) { /* Device audio is optional. */ }
}
