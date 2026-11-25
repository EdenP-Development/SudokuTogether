package com.playereden.sudokutogether

import android.content.Context
import android.content.Intent
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
 * progress-save changes or automatic playback. Stops when leaving this panel.
 */
@Composable internal fun PrivateMusicPilot(enabled: Boolean) {
    if (!BuildConfig.DEBUG) return
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val player = remember(context) { PilotPlayer(context) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var playing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Choose a local audio file to audition.") }
    var volume by remember { mutableStateOf(.25f) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { selected ->
        if (selected != null) {
            player.stop()
            playing = false
            uri = selected
            message = "Track selected. Tap Play to audition."
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
    GamePanel(Modifier.fillMaxWidth()) {
        SectionTitle("Private music audition")
        Text("Development builds only. Audio stays on your device.", color = Muted,
            style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = { player.stop(); playing = false; picker.launch(arrayOf("audio/*")) },
            enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Choose audio file") }
        Text(message, color = Ink, style = MaterialTheme.typography.bodySmall)
        Text("Music volume: ${(volume * 100).toInt()}%", color = Forest)
        Slider(value = volume, onValueChange = { volume = it; player.volume(it) }, enabled = enabled)
        Button(onClick = {
            if (playing) { player.stop(); playing = false; message = "Playback stopped." }
            else uri?.let { selected ->
                playing = player.start(selected, volume) {
                    playing = false
                    message = "Playback stopped or audio unavailable. Tap Play to retry."
                }
                message = if (playing) "Loading loop preview…" else "Unable to open audio or obtain audio focus."
            }
        }, enabled = enabled && uri != null, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(if (playing) "Stop" else "Play loop")
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
    fun start(uri: Uri, volume: Float, onStopped: () -> Unit): Boolean {
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
            next.isLooping = true
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
