package com.playereden.sudokutogether

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Debug-only, user-selected audio audition. No bundled assets, internet,
 * progress-save changes or automatic playback. Activity-level owner keeps music playing between screens.
 */
@Composable internal fun rememberPrivateMusicPilot(enabled: Boolean): @Composable () -> Unit {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val player = remember(context) { PilotPlayer(context) }
    var tracks by remember { mutableStateOf(emptyList<Uri>()) }
    var titles by remember { mutableStateOf(emptyMap<Uri, String>()) }
    var expanded by remember { mutableStateOf(false) }
    var playing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Choose the soundtrack files.") }
    var volume by remember { mutableStateOf(.25f) }
    var index by remember { mutableIntStateOf(0) }
    var order by remember { mutableStateOf(emptyList<Uri>()) }
    fun title(uri: Uri): String = runCatching {
        context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
            null, null, null)?.use { if (it.moveToFirst()) it.getString(0) else null }
    }.getOrNull()?.substringBeforeLast('.') ?: "Track"
    fun playAt(position: Int) {
        if (order.isEmpty()) return
        index = position.mod(order.size)
        val selected = order[index]
        playing = player.start(selected, volume, looping = order.size == 1,
            onFinished = { playAt(index + 1) },
            onStopped = { playing = false; message = "Music stopped. Tap Play to retry." })
        message = if (playing) "Playing" else "Unable to play this track."
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { selected ->
        if (selected.isNotEmpty()) {
            player.stop()
            playing = false
            tracks = selected.distinct()
            titles = tracks.associateWith { title(it) }
            val main = tracks.firstOrNull { titles[it]?.replace('_', ' ')?.contains("Warm Puzzle Time", ignoreCase = true) == true }
            order = listOfNotNull(main) + tracks.filter { it != main }.shuffled()
            index = 0
            message = "${tracks.size} tracks ready."
        }
    }
    DisposableEffect(lifecycle, player) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                player.stop()
                playing = false
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer); player.stop() }
    }
    LaunchedEffect(enabled) { if (!enabled) { player.stop(); playing = false } }
    return {
        GamePanel(Modifier.fillMaxWidth()) {
            SectionTitle("The Music Corner")
            Text("Good puzzles. Good company.", color = Muted, style = MaterialTheme.typography.bodySmall)
            if (order.isEmpty()) {
                Text("Choose the soundtrack files to begin.", color = Muted, style = MaterialTheme.typography.bodySmall)
            } else {
                Text(titles[order[index]] ?: "Track", color = Forest, style = MaterialTheme.typography.titleMedium)
                Text("Track ${index + 1} of ${order.size} · ${if (playing) "Playing" else "Stopped"}",
                    color = Muted, style = MaterialTheme.typography.bodySmall)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = { if (playing) playAt(index - 1) else index = (index - 1).mod(order.size) },
                        enabled = enabled && order.size > 1, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Previous") }
                    Button(onClick = {
                        if (playing) { player.stop(); playing = false; message = "Music stopped." }
                        else playAt(index)
                    }, enabled = enabled, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(if (playing) "Stop" else "Play") }
                    TextButton(onClick = { if (playing) playAt(index + 1) else index = (index + 1).mod(order.size) },
                        enabled = enabled && order.size > 1, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Next") }
                }
                Text("Music volume: ${(volume * 100).toInt()}%", color = Forest)
                Slider(value = volume, onValueChange = { volume = it; player.volume(it) }, enabled = enabled)
                TextButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text(if (expanded) "Hide soundtrack" else "View soundtrack (${order.size})")
                }
                if (expanded) order.forEachIndexed { position, track ->
                    OutlinedButton(onClick = { playAt(position) }, enabled = enabled,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        Text("${if (position == index) "• " else ""}${titles[track] ?: "Track"}")
                    }
                }
            }
            Text(message, color = Muted, style = MaterialTheme.typography.bodySmall)
            OutlinedButton(onClick = { player.stop(); playing = false; picker.launch(arrayOf("audio/*")) },
                enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(if (order.isEmpty()) "Choose soundtrack" else "Change test files")
            }
        }
    }
}

private class PilotPlayer(private val context: Context) {
    private val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var media: MediaPlayer? = null
    private var focus: AudioFocusRequest? = null
    private var callback: (() -> Unit)? = null
    private var level = .25f
    private val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
    private val listener = AudioManager.OnAudioFocusChangeListener { change ->
        if (change < 0) {
            val notify = callback
            stop()
            notify?.invoke()
        }
    }

    @Suppress("DEPRECATION")
    fun start(uri: Uri, volume: Float, looping: Boolean, onFinished: () -> Unit, onStopped: () -> Unit): Boolean {
        stop()
        level = volume
        callback = onStopped
        return try {
            val granted = if (Build.VERSION.SDK_INT >= 26) {
                val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(attributes).setOnAudioFocusChangeListener(listener).build()
                focus = request
                manager.requestAudioFocus(request)
            } else manager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
            if (granted != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { stop(); return false }
            val next = MediaPlayer()
            media = next
            next.setAudioAttributes(attributes)
            next.setDataSource(context, uri)
            next.isLooping = looping
            next.setOnCompletionListener { if (media === it) onFinished() }
            next.setOnPreparedListener {
                if (media === it) {
                    try { it.setVolume(level, level); it.start() }
                    catch (_: Exception) { fail(it) }
                }
            }
            next.setOnErrorListener { failed, _, _ -> fail(failed); true }
            next.prepareAsync()
            true
        } catch (_: Exception) { stop(); false }
    }

    private fun fail(failed: MediaPlayer) {
        if (media !== failed) return
        val notify = callback
        stop()
        notify?.invoke()
    }

    fun volume(value: Float) {
        level = value.coerceIn(0f, 1f)
        runCatching { media?.setVolume(level, level) }
    }

    @Suppress("DEPRECATION")
    fun stop() {
        val previous = media
        media = null
        callback = null
        runCatching { previous?.release() }
        if (Build.VERSION.SDK_INT >= 26) {
            focus?.let { runCatching { manager.abandonAudioFocusRequest(it) } }
            focus = null
        } else runCatching { manager.abandonAudioFocus(listener) }
    }
}
