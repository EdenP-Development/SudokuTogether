package com.playereden.sudokutogether

/** Optional sound feedback must never prevent a puzzle action on unsupported devices. */
internal fun playPuzzleClick(play: () -> Unit) {
    try { play() } catch (_: RuntimeException) { /* Device audio is optional. */ }
}

/** Original synthesized PCM cues, loaded once per gameplay screen, no per-tap synthesis. */
internal enum class PuzzleCue { SELECT, NUMBER, NOTE, ERASE, UNDO, COMPLETE }

internal fun puzzleCueSamples(cue: PuzzleCue): ShortArray {
    val rate = 22050
    if (cue == PuzzleCue.COMPLETE) return completionSamples(rate)
    if (cue == PuzzleCue.ERASE) return eraseSamples(rate)
    val duration = when (cue) {
        PuzzleCue.SELECT -> 0.035
        PuzzleCue.NUMBER -> 0.085
        PuzzleCue.NOTE -> 0.028
        PuzzleCue.ERASE -> error("Handled above")
        PuzzleCue.UNDO -> 0.065
        PuzzleCue.COMPLETE -> error("Handled above")
    }
    val frequency = when (cue) {
        PuzzleCue.SELECT -> 520.0
        PuzzleCue.NUMBER -> 410.0
        PuzzleCue.NOTE -> 1050.0
        PuzzleCue.ERASE -> error("Handled above")
        PuzzleCue.UNDO -> 440.0
        PuzzleCue.COMPLETE -> error("Handled above")
    }
    val random = kotlin.random.Random(17 + cue.ordinal)
    var filteredNoise = 0.0
    return ShortArray((duration * rate).toInt()) { i ->
        val t = i.toDouble() / rate
        val attack = (t / (if (cue == PuzzleCue.NUMBER) .012 else .003)).coerceAtMost(1.0)
        val decay = (1.0 - t / duration).coerceAtLeast(0.0)
        val tone = kotlin.math.sin(2 * kotlin.math.PI * frequency * t)
        val overtone = kotlin.math.sin(2 * kotlin.math.PI * frequency * 1.73 * t)
        val noise = random.nextDouble(-1.0, 1.0)
        filteredNoise = .78 * filteredNoise + .22 * noise
        val blend = when (cue) {
            PuzzleCue.ERASE -> error("Handled above")
            PuzzleCue.NUMBER -> .28 * tone + .72 * filteredNoise
            else -> .72 * tone + .18 * overtone + .10 * noise
        }
        (blend * attack * decay * decay * 10000).toInt().coerceIn(-10000, 10000).toShort()
    }
}

internal class PuzzleSoundBank(context: android.content.Context) {
    private val preferences = context.getSharedPreferences("gridmates_audio_effects", 0)
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
                    java.io.FileInputStream(file).use { input ->
                        ids[cue] = pool.load(input.fd, 0L, file.length(), 1)
                    }
                } finally { file.delete() }
            }
        }
    }
    fun play(cue: PuzzleCue) {
        val id = ids[cue] ?: return
        val level = readEffectsVolume(preferences)
        if (id in ready && level > 0f) pool.play(id, level, level, if (cue == PuzzleCue.COMPLETE) 2 else 1, 0, 1f)
    }
    fun release() { pool.setOnLoadCompleteListener(null); pool.release(); ready.clear(); ids.clear() }
}


/** A short original C-major arpeggio; bounded amplitude and softly decaying notes. */
private fun completionSamples(rate: Int): ShortArray {
    val notes = doubleArrayOf(523.25, 659.25, 783.99)
    return ShortArray((rate * .72).toInt()) { i ->
        val t = i.toDouble() / rate
        val mix = notes.mapIndexed { index, frequency ->
            val age = t - index * .13
            if (age < 0 || age >= .44) 0.0 else {
                val envelope = (age / .012).coerceAtMost(1.0) *
                    (1.0 - age / .44) * kotlin.math.exp(-5 * age)
                envelope * (kotlin.math.sin(2 * kotlin.math.PI * frequency * age) +
                    .16 * kotlin.math.sin(4 * kotlin.math.PI * frequency * age))
            }
        }.sum()
        (mix * 4700).toInt().coerceIn(-10000, 10000).toShort()
    }
}

/** Only a transition observed within the same active round can celebrate. */
internal class CompletionCueGuard {
    private var round: String? = null
    private var previouslyIncomplete = false
    private var celebrated = false

    fun observe(roundId: String?, completed: Boolean): Boolean {
        if (roundId == null || roundId != round) {
            round = roundId
            previouslyIncomplete = roundId != null && !completed
            celebrated = completed
            return false
        }
        if (!completed) {
            previouslyIncomplete = true
            return false
        }
        val emit = previouslyIncomplete && !celebrated
        celebrated = true
        return emit
    }
}

internal fun readEffectsVolume(prefs: android.content.SharedPreferences): Float = normalizeMusicVolume(runCatching { prefs.getFloat("volume", .50f) }.getOrDefault(.50f))


/** Two soft friction strokes, rather than a percussive onset or pitched note. */
private fun eraseSamples(rate: Int): ShortArray {
    val random = kotlin.random.Random(61)
    var low = 0.0
    var smooth = 0.0
    return ShortArray((rate * .24).toInt()) { i ->
        val t = i.toDouble() / rate
        val noise = random.nextDouble(-1.0, 1.0)
        low += .055 * (noise - low)
        smooth += .36 * ((noise - low) - smooth)
        fun stroke(start: Double, duration: Double): Double {
            val x = (t - start) / duration
            if (x !in 0.0..1.0) return 0.0
            val shape = kotlin.math.sin(kotlin.math.PI * x)
            return shape * shape
        }
        val envelope = .85 * stroke(0.0, .115) + .65 * stroke(.125, .115)
        (smooth * envelope * 12000).toInt().coerceIn(-10000, 10000).toShort()
    }
}
