import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream
import java.util.Properties
import java.security.MessageDigest
import java.util.Locale
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

plugins { id("com.android.application"); kotlin("android"); kotlin("plugin.compose"); kotlin("plugin.serialization") }
// Explicit separate-package transition; legacy and existing QA remain the default.
// Staging cannot use the legacy signing environment or emit a signed replacement.
val permanentIdentityStaging = providers.gradleProperty("gridmatesPermanentIdentityStaging").orNull?.let {
    require(it == "true") { "gridmatesPermanentIdentityStaging only accepts true" }
    true
} ?: false
if (permanentIdentityStaging) require(System.getenv("SUDOKU_SIGNING_FILE").isNullOrBlank()) {
    "Permanent identity staging is unsigned; never use legacy production signing material"
}
val signedBeta = providers.gradleProperty("gridmatesSignedBeta").orNull?.let {
    require(it == "true")
    true
} ?: false
require(!(signedBeta && permanentIdentityStaging))
if (signedBeta) require(System.getenv("SUDOKU_SIGNING_FILE").isNullOrBlank()) { "Legacy signing is forbidden for the new beta" }
// Private unsigned production-shape audio candidate; never a signed distribution build.
val beta3AudioCandidate = providers.gradleProperty("gridmatesBeta3AudioCandidate").orNull?.let {
    require(it == "true") { "gridmatesBeta3AudioCandidate only accepts true" }
    true
} ?: false
if (beta3AudioCandidate) {
    require(permanentIdentityStaging && !signedBeta) { "Audio candidate requires unsigned permanent identity staging" }
    require(System.getenv("GRIDMATES_BETA_SIGNING_FILE").isNullOrBlank()) { "Candidate must not use beta signing material" }
    val expected = listOf(
        "cdcb72a7d7c8da92ed6b1133e98c18721a4a3172f604763ec4d87c9caf73976d",
        "214c6a8457b0feafac97ef13a1806207bd7e39ebe7899fd0097d55f06bf3e250",
        "49a3e6d6ff2273c1d7d4cff4a158f377d52c18f59fc5bc9a1e5980f3652bed2f",
        "84bfc70376be6bb22d6e26f07799ced8074eb9e6eb89ed8d575ef098fe06112a",
        "5a0a7c3bb125b8472a5127b26d61c8506ce674ed8e9f5cb2e34c9d42dfaed6b6",
        "1e439245b42cc0796c594e87491623c5be2e3e186dd809fa5a0f27aee68010d6"
    )
    expected.forEachIndexed { index, hash ->
        val track = rootProject.file("build/beta3-audio-assets/soundtrack/track_%02d.m4a".format(Locale.ROOT, index + 1))
        require(track.isFile) { "Missing candidate soundtrack: ${track.name}" }
        val actual = MessageDigest.getInstance("SHA-256").digest(track.readBytes()).joinToString("") { "%02x".format(it.toInt() and 0xff) }
        require(actual == hash) { "Candidate soundtrack checksum mismatch: ${track.name}" }
    }
}
val signingFile = System.getenv(if (signedBeta) "GRIDMATES_BETA_SIGNING_FILE" else "SUDOKU_SIGNING_FILE")?.let { file(it) }
if (signedBeta) require(signingFile?.isFile == true) { "Dedicated beta signing file required" }
val privateSigning = Properties().apply { if (signingFile?.isFile == true) signingFile.inputStream().use(::load) }
// Downloadable QA updates opt in to the original private debug identity.
// Ordinary developer builds may still use their own local debug key.
// Isolated owner art preview. No production signing or QA-update identity.
val woodlandV5Preview = providers.gradleProperty("gridmatesWoodlandV5Preview").orNull?.let {
    require(it == "true") { "gridmatesWoodlandV5Preview only accepts true" }
    true
} ?: false
// Experimental Beta 2 collection APK never shares production or V5.1 review data.
val beta2PortraitPreview = providers.gradleProperty("gridmatesBeta2PortraitPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2PortraitPreview only accepts true" }
    true
} ?: false
// Separately installed follow-on preview: includes first five + new Garden themed icons.
val beta2GardenPreview = providers.gradleProperty("gridmatesBeta2GardenPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2GardenPreview only accepts true" }
    true
} ?: false
val beta2FarmPreview = providers.gradleProperty("gridmatesBeta2FarmPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2FarmPreview only accepts true" }
    true
} ?: false
// Dedicated 75-item reviewer, never the production/other QA application IDs.
val beta2Unified75Preview = providers.gradleProperty("gridmatesBeta2Unified75Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2Unified75Preview only accepts true" }
    true
} ?: false
// New full-catalog reviewer; cannot replace public Beta 1 or the 75-item reviewer.
val beta2Unified79Preview = providers.gradleProperty("gridmatesBeta2Unified79Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2Unified79Preview only accepts true" }
    true
} ?: false
// Standalone full-matrix reviewer, distinct from 79-item art and Beta 1 packages.
val beta2MatrixPreview = providers.gradleProperty("gridmatesBeta2MatrixPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2MatrixPreview only accepts true" }
    true
} ?: false
// Full 79-content + 990-matrix + canonical Versus V7 preview. No install-over.
val beta2V7Preview = providers.gradleProperty("gridmatesBeta2V7Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2V7Preview only accepts true" }
    true
} ?: false
// Integrated owner test only: revised Basic vector emblems + Versus V8-B.
val beta2IntegratedRc3Preview = providers.gradleProperty("gridmatesBeta2IntegratedRc3Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2IntegratedRc3Preview only accepts true" }
    true
} ?: false
// Visible Beta 2 RC2 reviewer; do NOT enable unapproved art for ordinary builds.
val beta2VisualRc2Preview = providers.gradleProperty("gridmatesBeta2VisualRc2Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2VisualRc2Preview only accepts true" }
    true
} ?: false
// One integrated owner-review package, never production Beta 1 install-over.
val beta2Rc1Preview = providers.gradleProperty("gridmatesBeta2Rc1Preview").orNull?.let {
    require(it == "true") { "gridmatesBeta2Rc1Preview only accepts true" }
    true
} ?: false
// Dedicated compact-achievement-album reviewer. No install-over of existing test builds.
val beta2AlbumPreview = providers.gradleProperty("gridmatesBeta2AlbumPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2AlbumPreview only accepts true" }
    true
} ?: false
// One-time owner screenshot reviewer for Beta 2 layout refinements.
val beta2PolishPreview = providers.gradleProperty("gridmatesBeta2PolishPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2PolishPreview only accepts true" }
    true
} ?: false
// Veteran rewards QA build uses a fresh app identity, protecting the 79-item preview.
val beta2VeteranPreview = providers.gradleProperty("gridmatesBeta2VeteranPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2VeteranPreview only accepts true" }
    true
} ?: false
// Dedicated owner-review build; never collides with a previously installed
// V7 debug reviewer, whose temporary GitHub signer may differ between runs.
val beta2UnlockPreview = providers.gradleProperty("gridmatesBeta2UnlockPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2UnlockPreview only accepts true" }
    true
} ?: false
// Separate-install board palette reviewer; never overwrites public Beta 1.
val beta2BoardsPreview = providers.gradleProperty("gridmatesBeta2BoardsPreview").orNull?.let {
    require(it == "true") { "gridmatesBeta2BoardsPreview only accepts true" }
    true
} ?: false
require(listOf(beta2IntegratedRc3Preview, beta2VisualRc2Preview, beta2Rc1Preview, beta2AlbumPreview, beta2PolishPreview, beta2VeteranPreview, beta2UnlockPreview, beta2V7Preview, beta2MatrixPreview, beta2Unified79Preview, beta2Unified75Preview, beta2BoardsPreview, beta2FarmPreview, beta2GardenPreview, beta2PortraitPreview, woodlandV5Preview).count { it } <= 1) {
    "Only one isolated preview type can be selected"
}
val beta3VisualPreview = providers.gradleProperty("gridmatesBeta3VisualPreview").orNull?.let {
    require(it == "true")
    true
} ?: false
if (beta3VisualPreview) require(!signedBeta && !permanentIdentityStaging && signingFile == null) { "Beta 3 visual preview must use isolated debug signing" }
// Fresh owner QA identity; never overwrite an earlier temporary-signer preview.
val beta3ConsolidatedQa = providers.gradleProperty("gridmatesBeta3ConsolidatedQa").orNull?.let {
    require(it == "true") { "gridmatesBeta3ConsolidatedQa only accepts true" }
    true
} ?: false
if (beta3ConsolidatedQa) require(beta3VisualPreview) { "Consolidated QA requires the isolated visual preview" }
val beta3FullQa = providers.gradleProperty("gridmatesBeta3FullQa").orNull?.let {
    require(it == "true") { "gridmatesBeta3FullQa only accepts true" }
    true
} ?: false
if (beta3FullQa) {
    require(beta3ConsolidatedQa) { "Full QA requires consolidated isolated preview" }
    val tracks = rootProject.file("build/beta3-audio-assets/soundtrack")
    require((1..6).all { tracks.resolve("track_%02d.m4a".format(Locale.ROOT, it)).isFile }) { "Full QA requires all six prepared soundtrack tracks" }
}
// Stable cumulative owner QA is a separate package; never reuse a temporary preview identity.
val beta3StableFullQa = providers.gradleProperty("gridmatesBeta3StableFullQa").orNull?.let {
    require(it == "true") { "gridmatesBeta3StableFullQa only accepts true" }
    true
} ?: false
// Temporary fallback reviewer, deliberately distinct from FullQA1 and stable QA.
val beta3FullQa2 = providers.gradleProperty("gridmatesBeta3FullQa2").orNull?.let {
    require(it == "true") { "gridmatesBeta3FullQa2 only accepts true" }
    true
} ?: false
if (beta3FullQa2) require(beta3FullQa && !beta3StableFullQa) {
    "FullQA2 requires complete assets and a separate temporary identity"
}
// Final owner reviewer remains separate from temporary FullQA1/2 and persistent QA.
val beta3FullQa3 = providers.gradleProperty("gridmatesBeta3FullQa3").orNull?.let {
    require(it == "true") { "gridmatesBeta3FullQa3 only accepts true" }
    true
} ?: false
if (beta3FullQa3) require(beta3FullQa && !beta3FullQa2 && !beta3StableFullQa) {
    "FullQA3 requires complete assets and its own isolated temporary identity"
}
val qaUpdateFlag = providers.gradleProperty("gridmatesQaUpdate").orNull
if (beta3StableFullQa) require(beta3FullQa && qaUpdateFlag == "true") {
    "Stable full QA requires the complete asset build and persistent QA signer"
}
require(qaUpdateFlag == null || qaUpdateFlag == "true") { "gridmatesQaUpdate must be true when supplied" }
if (beta3VisualPreview && !beta3StableFullQa) require(qaUpdateFlag == null) { "Beta 3 preview must not use persistent QA signing" }
val qaVersion = providers.gradleProperty("gridmatesQaVersionCode").orNull?.let { value ->
    require(value.matches(Regex("[1-9][0-9]{0,9}"))) { "QA version code must be a positive integer" }
    val number = value.toLong()
    require(number in 20..2100000000L) { "QA updates must be newer than version 19 and within Android's range" }
    number.toInt()
}
require((qaUpdateFlag == "true") == (qaVersion != null)) { "QA update mode and version code must be supplied together" }
require(!(beta2PortraitPreview && qaVersion != null)) { "Portrait preview must not share QA-update identity" }
require(!(beta2GardenPreview && qaVersion != null)) { "Garden preview must not share QA-update identity" }
require(!(beta2FarmPreview && qaVersion != null)) { "Farm preview must not share QA-update identity" }
require(!(beta2Unified75Preview && qaVersion != null)) { "Unified 75 preview must not share QA-update identity" }
require(!(beta2Unified79Preview && qaVersion != null)) { "Unified 79 preview must not share QA-update identity" }
require(!(beta2MatrixPreview && qaVersion != null)) { "Matrix preview must not share QA-update identity" }
require(!(beta2V7Preview && qaVersion != null)) { "Beta2 V7 preview must not share QA-update identity" }
require(!(beta2IntegratedRc3Preview && qaVersion != null)) { "Integrated RC3 must not use QA-update identity" }
require(!(beta2VisualRc2Preview && qaVersion != null)) { "Visual RC2 must not use QA-update identity" }
require(!(beta2Rc1Preview && qaVersion != null)) { "RC1 reviewer must not share QA-update identity" }
require(!(beta2AlbumPreview && qaVersion != null)) { "Beta2 album preview must not share QA-update identity" }
require(!(beta2PolishPreview && qaVersion != null)) { "Beta2 polish preview must not share QA-update identity" }
require(!(beta2VeteranPreview && qaVersion != null)) { "Beta2 veteran preview must not share QA-update identity" }
require(!(beta2UnlockPreview && qaVersion != null)) { "Beta2 unlock preview must not share QA-update identity" }
require(!(beta2BoardsPreview && qaVersion != null)) { "Boards preview must not share QA-update identity" }
require(!(woodlandV5Preview && qaVersion != null)) {
    "Woodland V5 preview must not use a same-package QA update"
}
val qaKeystore = System.getenv("SUDOKU_QA_KEYSTORE_FILE")?.let { file(it) }
if (qaUpdateFlag == "true") require(qaKeystore?.isFile == true) {
    "Restore the existing private QA keystore before building an update; do not generate a replacement key"
}
// Owner approval is deliberately separate from a green reviewer build.
val beta2ArtApproval = Properties().apply {
    rootProject.file("design/versus-v9/release.properties").inputStream().use(::load)
}
val beta2ApprovedArt = beta2ArtApproval.getProperty("ownerApproved") == "true"
if (signedBeta) require(beta2ApprovedArt && privateSigning.isNotEmpty() && qaVersion == null) { "Approved art, dedicated signing and release version required" }
// Reproducible import of the independently verified Fox V1 transport.
val foxTransfer = rootProject.file("design/emotes/fox/v1/transfer")
val foxResources = layout.buildDirectory.dir("generated/foxEmotes/res")
val foxIds = "happy approve disapprove appreciation thinking celebration oops greeting confused applause break ready".split(" ")
val prepareFoxEmotes = tasks.register("prepareFoxEmotes") {
    inputs.dir(foxTransfer)
    outputs.dir(foxResources)
    doLast {
        val parts = (1..13).map { foxTransfer.resolve("part_%02d.bin".format(Locale.ROOT, it).replace('_', '-')) }
        val bytes = ByteArrayOutputStream().also { out -> parts.forEach { out.write(it.readBytes()) } }.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it.toInt() and 0xff) }
        require(bytes.size == 3754384 && digest == "c10b3fcae7e09d09a4c500d74a479c1842a19c54de6f9a7b1907714aa38bdf5b") { "Fox V1 artwork archive checksum mismatch" }
        val expected = foxIds.map { "runtime/gm_emote_fox_$it.webp" }.toSet()
        val found = mutableSetOf<String>()
        val destination = foxResources.get().asFile.resolve("drawable-nodpi")
        destination.mkdirs()
        ZipInputStream(bytes.inputStream()).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                if (entry.name in expected) {
                    require(found.add(entry.name))
                    destination.resolve(entry.name.substringAfterLast('/')).outputStream().use { zip.copyTo(it) }
                }
                zip.closeEntry()
            }
        }
        require(found == expected) { "Incomplete Fox V1 artwork" }
    }
}
// Home uses the same fingerprinted master as the approved adaptive launcher.
val brandingTransfer = rootProject.file("design/branding/transfer")
val headerResources = layout.buildDirectory.dir("generated/headerBranding/res")
val prepareHeaderBranding = tasks.register("prepareHeaderBranding") {
    inputs.dir(brandingTransfer)
    outputs.dir(headerResources)
    doLast {
        val parts = (1..14).map { brandingTransfer.resolve("part-%02d.bin".format(Locale.ROOT, it)) }
        val bytes = ByteArrayOutputStream().also { out -> parts.forEach { out.write(it.readBytes()) } }.toByteArray()
        fun digest(data: ByteArray) = MessageDigest.getInstance("SHA-256").digest(data)
            .joinToString("") { "%02x".format(it.toInt() and 0xff) }
        require(digest(bytes) == "487ee37d5087822e9b085b1dddf2e04b01b13a2d70d8e24dd90e60b233bf0dc8") { "Branding archive checksum mismatch" }
        var master: ByteArray? = null
        ZipInputStream(bytes.inputStream()).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                if (entry.name == "launcher-master.png") {
                    require(master == null) { "Duplicate branding master" }
                    master = zip.readBytes()
                }
                zip.closeEntry()
            }
        }
        val verified = requireNotNull(master) { "Missing branding master" }
        require(digest(verified) == "ea3812eb46f6d3601fd5edf1d1f1f2d1c9c445a4140c7e1d2a86c08563d3367f") { "Branding master checksum mismatch" }
        val image = ImageIO.read(verified.inputStream())
        require(image.width == 1254 && image.height == 1254) { "Unexpected branding dimensions" }
        val resized = BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB)
        val graphics = resized.createGraphics()
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
            graphics.drawImage(image, 0, 0, 256, 256, null)
        } finally { graphics.dispose() }
        val destination = headerResources.get().asFile.resolve("drawable-nodpi")
        destination.mkdirs()
        check(ImageIO.write(resized, "png", destination.resolve("gridmates_header_approved.png")))
    }
}
android {
    sourceSets.getByName("main").res.srcDir(foxResources)
    sourceSets.getByName("main").res.srcDir(headerResources)
    namespace = "com.playereden.sudokutogether"
    compileSdk = 36
    defaultConfig {
        applicationId = if (permanentIdentityStaging || signedBeta) "com.playereden.gridmates.sudoku.beta" else "com.playereden.sudokutogether"
        minSdk = 26; targetSdk = 36; versionCode = qaVersion ?: 31; versionName = "1.0.0-beta.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("boolean", "BETA3_AUDIO_CANDIDATE", beta3AudioCandidate.toString())
        buildConfigField("boolean", "BETA3_FULL_QA", beta3FullQa.toString())
        resValue("bool", "gridmates_insignia_preview_enabled",
            (beta2VisualRc2Preview || beta2IntegratedRc3Preview).toString())
        resValue("bool", "gridmates_insignia_art_enabled",
            (beta3VisualPreview || beta2VisualRc2Preview || beta2IntegratedRc3Preview).toString())
        resValue("bool", "gridmates_versus_v9_enabled", (beta3VisualPreview || beta2IntegratedRc3Preview).toString())
        resValue("bool", "gridmates_versus_v8b_enabled", beta2IntegratedRc3Preview.toString())
        resValue("bool", "gridmates_portrait_refinement_enabled", (beta3VisualPreview || beta2IntegratedRc3Preview).toString())
    }
    if (privateSigning.isNotEmpty()) signingConfigs.create("privateKey") {
        storeFile = file(privateSigning.getProperty("storeFile"))
        storePassword = privateSigning.getProperty("storePassword")
        keyAlias = privateSigning.getProperty("keyAlias")
        keyPassword = privateSigning.getProperty("keyPassword")
    }
    if (qaVersion != null) signingConfigs.create("qaContinuity") {
        storeFile = qaKeystore
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
    }
    buildTypes {
        debug {
            // QA uses a separate install so updating a test APK never
            // threatens the existing signed app's offline records.
            applicationIdSuffix = when {
                beta3StableFullQa -> ".qa.beta3fullstable"
                beta3FullQa3 -> ".qa.beta3full3"
                beta3FullQa2 -> ".qa.beta3full2"
                beta3FullQa -> ".qa.beta3full1"
                beta3ConsolidatedQa -> ".qa.beta3integrated1"
                beta3VisualPreview -> ".qa.beta3coach2"
                beta2BoardsPreview -> ".qa.beta2boards"
                beta2IntegratedRc3Preview -> ".qa.beta2integratedrc3"
                beta2VisualRc2Preview -> ".qa.beta2visualrc2"
                beta2Rc1Preview -> ".qa.beta2rc1"
                beta2AlbumPreview -> ".qa.beta2album"
                beta2PolishPreview -> ".qa.beta2polish"
                beta2VeteranPreview -> ".qa.beta2veteran"
                beta2UnlockPreview -> ".qa.beta2unlock"
                beta2V7Preview -> ".qa.beta2v7"
                beta2MatrixPreview -> ".qa.beta2matrix"
                beta2Unified79Preview -> ".qa.beta2unified79"
                beta2Unified75Preview -> ".qa.beta2unified75"
                beta2FarmPreview -> ".qa.beta2farm"
                beta2GardenPreview -> ".qa.beta2garden"
                beta2PortraitPreview -> ".qa.beta2portraits"
                woodlandV5Preview -> ".qa.woodlandv51"
                else -> ".qa"
            }
            versionNameSuffix = when {
                beta3StableFullQa -> "-qa.beta3fullstable.$qaVersion"
                beta3FullQa3 -> "-qa.beta3full3"
                beta3FullQa2 -> "-qa.beta3full2"
                beta3FullQa -> "-qa.beta3full1"
                beta3ConsolidatedQa -> "-qa.beta3integrated1"
                beta3VisualPreview -> "-qa.beta3coach2"
                beta2BoardsPreview -> "-qa.beta2boards"
                beta2IntegratedRc3Preview -> "-qa.beta2integratedrc3"
                beta2VisualRc2Preview -> "-qa.beta2visualrc2"
                beta2Rc1Preview -> "-qa.beta2rc1"
                beta2AlbumPreview -> "-qa.beta2album"
                beta2PolishPreview -> "-qa.beta2polish"
                beta2VeteranPreview -> "-qa.beta2veteran"
                beta2UnlockPreview -> "-qa.beta2unlock"
                beta2V7Preview -> "-qa.beta2v7"
                beta2MatrixPreview -> "-qa.beta2matrix"
                beta2Unified79Preview -> "-qa.beta2unified79"
                beta2Unified75Preview -> "-qa.beta2unified75"
                beta2FarmPreview -> "-qa.beta2farm"
                beta2GardenPreview -> "-qa.beta2garden"
                beta2PortraitPreview -> "-qa.beta2portraits"
                woodlandV5Preview -> "-qa.woodlandv51"
                qaVersion != null -> "-qa.$qaVersion"
                else -> "-qa"
            }
            if (qaVersion != null) signingConfig = signingConfigs.getByName("qaContinuity")
        }
        release {
            // Production displays accepted art, but never the comparison dialog.
            resValue("bool", "gridmates_insignia_art_enabled", beta2ApprovedArt.toString())
            resValue("bool", "gridmates_portrait_refinement_enabled", beta2ApprovedArt.toString())
            resValue("bool", "gridmates_versus_v9_enabled", beta2ApprovedArt.toString())
            resValue("bool", "gridmates_insignia_preview_enabled", "false")
            resValue("bool", "gridmates_versus_v8b_enabled", "false")
            isMinifyEnabled = false
            if (privateSigning.isNotEmpty()) signingConfig = signingConfigs.getByName("privateKey")
        }
    }
    if (beta3VisualPreview) {
        sourceSets.getByName("debug").res.srcDir(rootProject.file("build/beta3-visual-res"))
        sourceSets.getByName("debug").assets.srcDir(rootProject.file("build/beta3-audio-assets"))
    }
    if (beta3AudioCandidate) {
        sourceSets.getByName("release").assets.srcDir(rootProject.file("build/beta3-audio-assets"))
    }
    androidResources { noCompress += "m4a" }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}
tasks.matching { it.name == "preBuild" || (it.name.startsWith("merge") && it.name.endsWith("Resources")) }.configureEach {
    dependsOn(prepareFoxEmotes, prepareHeaderBranding)
}
// Do not create altered QA/debug variants when staging the new production ID.
androidComponents {
    beforeVariants(selector().withBuildType("debug")) { variant ->
        if (permanentIdentityStaging || signedBeta) variant.enable = false
    }
}
dependencies {
    implementation(project(":core"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2025.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
}

