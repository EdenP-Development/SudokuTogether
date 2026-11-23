package com.playereden.sudokutogether

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.Appearance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Review-only launcher enabled solely for -PgridmatesBeta2ArtZipPreview=true.
 * The temporary images never enter save data, gameplay, LAN or the actual catalog.
 */
class ArtReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudokuTogetherTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                var candidates by remember { mutableStateOf<Map<String, Bitmap>>(emptyMap()) }
                var notice by remember {
                    mutableStateOf("Choose the revised seven-sprite ZIP to compare it with original GridMates art.")
                }
                val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                    if (uri != null) scope.launch {
                        val result = runCatching {
                            withContext(Dispatchers.IO) {
                                context.contentResolver.openInputStream(uri)?.use(ArtReviewZipReader::read)
                                    ?: error("Could not open selected ZIP")
                            }
                        }
                        result.onSuccess {
                            candidates = it
                            notice = "Loaded 7 of 7 candidates. Artwork is for review only."
                        }.onFailure {
                            candidates = emptyMap()
                            notice = "Import failed: ${it.message ?: "Invalid ZIP"}"
                        }
                    }
                }
                ArtReviewScreen(candidates, notice) {
                    picker.launch(arrayOf("*/*"))
                }
            }
        }
    }
}

internal data class ArtReviewSample(
    val id: String,
    val name: String,
    val family: String,
    val originalA: String,
    val originalB: String
)
internal val artReviewSamples = listOf(
    ArtReviewSample("farm_goat", "Goat", "Farm", "farm_cow", "farm_chicken"),
    ArtReviewSample("farm_duck", "Duck", "Farm", "farm_chicken", "farm_cow"),
    ArtReviewSample("farm_barn", "Barn", "Farm", "farm_cow", "farm_chicken"),
    ArtReviewSample("garden_snail", "Snail", "Garden", "garden_honeybee", "garden_sunflower"),
    ArtReviewSample("garden_daisy", "Daisy", "Garden", "garden_sunflower", "garden_honeybee"),
    ArtReviewSample("garden_mushroom", "Mushroom", "Garden", "garden_honeybee", "garden_sunflower"),
    ArtReviewSample("ocean_sea_star", "Sea Star", "Ocean", "ocean_octopus", "ocean_seahorse")
)

/** Decode only approved candidate filenames; never extract ZIP files onto disk. */
internal object ArtReviewZipReader {
    private const val MAX_ZIP = 12_000_000
    private const val MAX_ENTRY = 1_500_000
    private val names = artReviewSamples.associate { (it.id + "_candidate.png") to it.id }

    fun read(input: InputStream): Map<String, Bitmap> {
        val zipBytes = readLimited(input, MAX_ZIP)
        val found = linkedMapOf<String, Bitmap>()
        var entries = 0
        ZipInputStream(ByteArrayInputStream(zipBytes)).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                require(++entries <= 50) { "Too many ZIP entries" }
                val filename = entry.name.substringAfterLast('/')
                val id = names[filename]
                if (!entry.isDirectory && id != null) {
                    require(id !in found) { "Duplicate candidate: $filename" }
                    val bytes = readLimited(zip, MAX_ENTRY)
                    require(bytes.size >= 8 && bytes.copyOfRange(0, 8).contentEquals(
                        byteArrayOf(0x89.toByte(), 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a)
                    )) { "$filename is not a PNG" }
                    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                    require(options.outWidth == 512 && options.outHeight == 512) {
                        "$filename must be 512×512"
                    }
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        ?: error("Cannot decode $filename")
                    require(bitmap.hasAlpha() && listOf(
                        bitmap.getPixel(0, 0), bitmap.getPixel(511, 0),
                        bitmap.getPixel(0, 511), bitmap.getPixel(511, 511)
                    ).all { AndroidColor.alpha(it) == 0 }) { "$filename must have transparent corners" }
                    found[id] = bitmap
                }
                zip.closeEntry()
            }
        }
        val missing = names.values.filterNot { it in found }
        require(missing.isEmpty()) { "Missing ${missing.joinToString()}" }
        return found
    }

    private fun readLimited(input: InputStream, limit: Int): ByteArray {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        var total = 0
        while (true) {
            val read = input.read(buffer)
            if (read < 0) break
            total += read
            require(total <= limit) { "ZIP or image exceeds review size limit" }
            result.write(buffer, 0, read)
        }
        return result.toByteArray()
    }
}

@Composable internal fun ArtReviewScreen(
    images: Map<String, Bitmap>, notice: String, loadZip: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Cream).padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(Modifier.fillMaxWidth().padding(top = 22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("GridMates · Art Review", style = MaterialTheme.typography.headlineSmall, color = Forest)
                Text("Compare candidates against the actual original Collection portraits. Not an unlock screen.",
                    style = MaterialTheme.typography.bodyMedium, color = Muted)
                Button(onClick = loadZip, modifier = Modifier.testTag("art_review_load_zip")) {
                    Text("Load seven-sprite ZIP")
                }
                Text(notice, modifier = Modifier.testTag("art_review_status"),
                    color = Forest, style = MaterialTheme.typography.bodySmall)
                Text("Originals and candidates use the same circle, padding and ContentScale.Fit.",
                    style = MaterialTheme.typography.labelSmall, color = Muted)
            }
        }
        items(artReviewSamples, key = { it.id }) { sample ->
            Surface(
                color = GreenTint, shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("art_review_${sample.id}")
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${sample.family} · ${sample.name}", style = MaterialTheme.typography.titleMedium, color = Forest)
                    Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AnimalPortrait(Appearance(portrait = sample.originalA), Modifier.size(62.dp))
                            Text("Original A", style = MaterialTheme.typography.labelSmall, color = Muted)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AnimalPortrait(Appearance(portrait = sample.originalB), Modifier.size(62.dp))
                            Text("Original B", style = MaterialTheme.typography.labelSmall, color = Muted)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ReviewCandidatePicture(images[sample.id], 62.dp, sample.id)
                            Text("Candidate", style = MaterialTheme.typography.labelSmall, color = Forest)
                        }
                    }
                    Text("Candidate at 48 / 62 / 96 dp", style = MaterialTheme.typography.labelSmall, color = Muted)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        listOf(48.dp, 62.dp, 96.dp).forEach { diameter ->
                            ReviewCandidatePicture(images[sample.id], diameter, sample.id)
                        }
                    }
                }
            }
        }
        item {
            Text("QA-only viewer · Original saves and the 79-cosmetic catalog are untouched.",
                modifier = Modifier.padding(bottom = 24.dp),
                color = Muted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable private fun ReviewCandidatePicture(bitmap: Bitmap?, diameter: Dp, id: String) {
    BoxWithConstraints(
        modifier = Modifier.size(diameter).clip(CircleShape).background(GreenTint)
            .testTag("candidate_${id}_${diameter.value.toInt()}"),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(), contentDescription = "$id candidate",
                modifier = Modifier.fillMaxSize().padding(maxWidth * .08f),
                contentScale = ContentScale.Fit
            )
        } else {
            Text("?", style = MaterialTheme.typography.titleLarge, color = Muted)
        }
    }
}
