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
 * Soundtrack controller with private import restricted to developer review. Device-local preferences, no progress-save changes.
 * Activity-level owner keeps music playing between screens. Bundled music resumes only in foreground.
 */
@Composable internal fun rememberPrivateMusicPilot(enabled: Boolean, allowPrivateImport: Boolean = BuildConfig.DEBUG): @Composable () -> Unit {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val player = remember(context) { PilotPlayer(context) }
    val bundled = remember(context) {
        listOf("The Final Piece", "Library Puzzle", "Warm Puzzle Time", "Sunny Sunday Puzzle",
            "Thoughtful Discovery", "Evening Puzzle").mapIndexedNotNull { i, name ->
            val path = "soundtrack/track_%02d.m4a".format(java.util.Locale.ROOT, i + 1)
            if (runCatching { context.assets.openFd(path).use { } }.isSuccess)
                Uri.parse("file:///android_asset/$path") to name else null
        }.toMap()
    }
    var tracks by remember { mutableStateOf(bundled.keys.toList()) }
    var titles by remember { mutableStateOf(bundled) }
    var expanded by remember { mutableStateOf(false) }
    var playing by remember { mutableStateOf(false) }
    var preparing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf(if (bundled.isEmpty()) { if (allowPrivateImport) "Choose the soundtrack files." else "Soundtrack unavailable." } else "Soundtrack ready.") }
    val preferences = remember(context) { context.getSharedPreferences("gridmates_music", Context.MODE_PRIVATE) }
    var volume by remember { mutableStateOf(normalizeMusicVolume(
        runCatching { preferences.getFloat("volume", .50f) }.getOrDefault(.50f))) }
    var musicEnabled by remember { mutableStateOf(
        runCatching { preferences.getBoolean("enabled", true) }.getOrDefault(true)) }
    fun setMusicEnabled(value: Boolean) {
        musicEnabled = value
        preferences.edit().putBoolean("enabled", value).apply()
    }
    var foreground by remember { mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) }
    var interrupted by remember { mutableStateOf(false) }
    var order by remember {
        val main = bundled.entries.firstOrNull { it.value == "Warm Puzzle Time" }?.key
        mutableStateOf(listOfNotNull(main) + bundled.keys.filter { it != main }.shuffled())
    }
    var index by remember {
        val saved = runCatching { preferences.getString("last_track", null) }.getOrNull()
        mutableIntStateOf(restoredTrackIndex(order.map { it.toString() }, saved))
    }
    fun selectTrack(position: Int) {
        if (order.isEmpty()) return
        index = position.mod(order.size)
        if (order[index] in bundled) preferences.edit().putString("last_track", order[index].toString()).apply()
    }
    fun title(uri: Uri): String = runCatching {
        context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
            null, null, null)?.use { if (it.moveToFirst()) it.getString(0) else null }
    }.getOrNull()?.substringBeforeLast('.') ?: "Track"
    fun playAt(position: Int) {
        if (!enabled || !musicEnabled || order.isEmpty() || !lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) return
        selectTrack(position)
        val selected = order[index]
        if (player.isActive(selected)) return
        playing = false
        preparing = true
        message = "Preparing music…"
        val accepted = player.start(selected, volume, looping = order.size == 1,
            onStarted = { preparing = false; playing = true; message = "Playing" },
            onFinished = { preparing = false; playing = false; message = "Music stopped."; playAt(index + 1) },
            onStopped = { interrupted = true; preparing = false; playing = false; message = "Music interrupted. Tap Play to retry." })
        if (!accepted) {
            preparing = false
            interrupted = true
            message = "Unable to play this track."
        }
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { selected ->
        if (allowPrivateImport && selected.isNotEmpty()) {
            player.stop()
            preparing = false
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
            if (event == Lifecycle.Event.ON_RESUME) foreground = true
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                foreground = false
                player.stop()
                preparing = false
                playing = false
                if (!interrupted && order.isNotEmpty()) message = "Music stopped."
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer); player.stop() }
    }
    LaunchedEffect(enabled, foreground, musicEnabled) {
        if (!enabled || !foreground || !musicEnabled) {
            player.stop()
            preparing = false
            playing = false
            if (!interrupted && order.isNotEmpty()) message = "Music stopped."
        } else if (bundled.isNotEmpty() && !playing && !preparing && !interrupted) {
            playAt(index)
        }
    }
    return {
        GamePanel(Modifier.fillMaxWidth()) {
            SectionTitle("The Music Corner")
            Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Background music", modifier = Modifier.weight(1f), color = Forest)
                Switch(checked = musicEnabled, enabled = enabled, colors = gameSwitchColors(), onCheckedChange = { value ->
                    interrupted = false
                    setMusicEnabled(value)
                    if (value && order.isNotEmpty() && bundled.isEmpty()) playAt(index)
                    else if (!value) { player.stop(); preparing = false; playing = false; message = "Music stopped." }
                })
            }
            Text("Good puzzles. Good company.", color = Muted, style = MaterialTheme.typography.bodySmall)
            if (order.isEmpty()) {
                Text(if (allowPrivateImport) "Choose the soundtrack files to begin." else "Soundtrack unavailable.", color = Muted, style = MaterialTheme.typography.bodySmall)
            } else {
                Text(titles[order[index]] ?: "Track", color = Forest, style = MaterialTheme.typography.titleMedium)
                Text("Track ${index + 1} of ${order.size} · ${if (playing) "Playing" else if (preparing) "Preparing" else if (interrupted) "Interrupted" else "Stopped"}",
                    color = Muted, style = MaterialTheme.typography.bodySmall)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = { if (playing || preparing) playAt(index - 1) else selectTrack(index - 1) },
                        enabled = enabled && order.size > 1, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Previous") }
                    Button(onClick = {
                        if (playing || preparing) { setMusicEnabled(false); player.stop(); preparing = false; playing = false; message = "Music stopped." }
                        else { interrupted = false; setMusicEnabled(true); playAt(index) }
                    }, enabled = enabled, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text(if (playing || preparing) "Stop" else "Play") }
                    TextButton(onClick = { if (playing || preparing) playAt(index + 1) else selectTrack(index + 1) },
                        enabled = enabled && order.size > 1, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Next") }
                }
                Text("Music volume: ${(volume * 100).toInt()}%", color = Forest)
                Slider(value = volume, onValueChange = { volume = normalizeMusicVolume(it); player.volume(volume) },
                    onValueChangeFinished = { preferences.edit().putFloat("volume", volume).apply() }, enabled = enabled)
                TextButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text(if (expanded) "Hide soundtrack" else "View soundtrack (${order.size})")
                }
                if (expanded) order.forEachIndexed { position, track ->
                    OutlinedButton(onClick = { interrupted = false; setMusicEnabled(true); playAt(position) }, enabled = enabled,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        Text("${if (position == index) "• " else ""}${titles[track] ?: "Track"}")
                    }
                }
            }
            if (showMusicStatusDetail(order.isNotEmpty(), message)) {
                Text(message, color = Muted, style = MaterialTheme.typography.bodySmall)
            }
            if (allowPrivateImport && bundled.isEmpty()) OutlinedButton(onClick = { player.stop(); preparing = false; playing = false; picker.launch(arrayOf("audio/*")) },
                enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(if (order.isEmpty()) "Choose soundtrack" else "Change test files")
            }
        }
    }
}

private class PilotPlayer(private val context: Context) {
    private val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var media: MediaPlayer? = null
    private var activeUri: Uri? = null
    private var focus: AudioFocusRequest? = null
    private var callback: (() -> Unit)? = null
    private var level = .50f
    private val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
    private val listener = AudioManager.OnAudioFocusChangeListener { change ->
        if (change < 0) {
            val notify = callback
            stop()
            notify?.invoke()
        }
    }

    fun isActive(uri: Uri): Boolean = media != null && activeUri == uri

    @Suppress("DEPRECATION")
    fun start(uri: Uri, volume: Float, looping: Boolean, onStarted: () -> Unit, onFinished: () -> Unit, onStopped: () -> Unit): Boolean {
        if (media != null && activeUri == uri) return true
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
            activeUri = uri
            next.setAudioAttributes(attributes)
            if (uri.scheme == "file" && uri.path?.startsWith("/android_asset/") == true) {
                context.assets.openFd(uri.path!!.removePrefix("/android_asset/")).use {
                    next.setDataSource(it.fileDescriptor, it.startOffset, it.length)
                }
            } else next.setDataSource(context, uri)
            next.isLooping = looping
            next.setOnCompletionListener { if (media === it) { stop(); onFinished() } }
            next.setOnPreparedListener {
                if (media === it) {
                    try { it.setVolume(level, level); it.start(); onStarted() }
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
        activeUri = null
        callback = null
        runCatching { previous?.release() }
        if (Build.VERSION.SDK_INT >= 26) {
            focus?.let { runCatching { manager.abandonAudioFocusRequest(it) } }
            focus = null
        } else runCatching { manager.abandonAudioFocus(listener) }
    }
}

internal fun normalizeMusicVolume(value: Float): Float = if (value.isFinite()) value.coerceIn(0f, 1f) else .50f

internal fun restoredTrackIndex(tracks: List<String>, saved: String?): Int = tracks.indexOf(saved).coerceAtLeast(0)

/** Keep actionable errors and interruption guidance; ordinary status is already beside the track count. */
internal fun showMusicStatusDetail(hasTracks: Boolean, message: String): Boolean =
    message !in setOf("Playing", "Preparing music…", "Music stopped.", "Soundtrack ready.",
        "Choose the soundtrack files.", "Soundtrack unavailable.") &&
        !(hasTracks && message.endsWith(" tracks ready."))
