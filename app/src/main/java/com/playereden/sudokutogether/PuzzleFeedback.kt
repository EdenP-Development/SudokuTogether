package com.playereden.sudokutogether

/** Optional sound feedback must never prevent a puzzle action on unsupported devices. */
internal fun playPuzzleClick(play: () -> Unit) {
    try { play() } catch (_: RuntimeException) { /* Device audio is optional. */ }
}

/** Original synthesized PCM cues, loaded once per gameplay screen, no per-tap synthesis. */
internal enum class PuzzleCue { SELECT, NUMBER, NOTE, ERASE, UNDO }

internal fun puzzleCueSamples(cue: PuzzleCue): ShortArray {
    val rate = 22050
    val duration = when (cue) {
        PuzzleCue.SELECT -> 0.035
        PuzzleCue.NUMBER -> 0.055
        PuzzleCue.NOTE -> 0.028
        PuzzleCue.ERASE -> 0.075
        PuzzleCue.UNDO -> 0.065
    }
    val frequency = when (cue) {
        PuzzleCue.SELECT -> 520.0
        PuzzleCue.NUMBER -> 740.0
        PuzzleCue.NOTE -> 1050.0
        PuzzleCue.ERASE -> 290.0
        PuzzleCue.UNDO -> 440.0
    }
    val random = kotlin.random.Random(17 + cue.ordinal)
    return ShortArray((duration * rate).toInt()) { i ->
        val t = i.toDouble() / rate
        val attack = (t / 0.003).coerceAtMost(1.0)
        val decay = (1.0 - t / duration).coerceAtLeast(0.0)
        val tone = kotlin.math.sin(2 * kotlin.math.PI * frequency * t)
        val overtone = kotlin.math.sin(2 * kotlin.math.PI * frequency * 1.73 * t)
        val noise = random.nextDouble(-1.0, 1.0)
        val blend = if (cue == PuzzleCue.ERASE) .25 * tone + .75 * noise
            else .72 * tone + .18 * overtone + .10 * noise
        (blend * attack * decay * decay * 10000).toInt().toShort()
    }
}

internal class PuzzleSoundBank(context: android.content.Context) {
    private val pool = android.media.SoundPool.Builder().setMaxStreams(2)
        .setAudioAttributes(android.media.AudioAttributes.Builder()
            .setUsage(android.media.AudioAttributes.USAGE_GAME)
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION).build()).build()
    private val ids = mutableMapOf<PuzzleCue, Int>()
    private val ready = mutableSetOf<Int>()
    init {
        pool.setOnLoadCompleteListener { _, id, status -> if (status == 0) ready.add(id) }
        PuzzleCue.entries.forEach { cue ->
            runCatching {
                val file = java.io.File.createTempFile("gridmates_cue_", ".wav", context.cacheDir)
                try {
                    val samples = puzzleCueSamples(cue)
                    val bytes = java.nio.ByteBuffer.allocate(44 + samples.size * 2)
                        .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    bytes.put("RIFF".toByteArray()).putInt(36 + samples.size * 2)
                    bytes.put("WAVEfmt ".toByteArray()).putInt(16).putShort(1).putShort(1)
                    bytes.putInt(22050).putInt(44100).putShort(2).putShort(16)
                    bytes.put("data".toByteArray()).putInt(samples.size * 2)
                    samples.forEach { bytes.putShort(it) }
                    file.writeBytes(bytes.array())
                    ids[cue] = pool.load(file.absolutePath, 1)
                } finally { file.delete() }
            }
        }
    }
    fun play(cue: PuzzleCue) {
        val id = ids[cue] ?: return
        if (id in ready) pool.play(id, .45f, .45f, 1, 0, 1f)
    }
    fun release() { pool.release() }
}
