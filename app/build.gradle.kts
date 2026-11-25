import java.util.Properties

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
val qaUpdateFlag = providers.gradleProperty("gridmatesQaUpdate").orNull
require(qaUpdateFlag == null || qaUpdateFlag == "true") { "gridmatesQaUpdate must be true when supplied" }
if (beta3VisualPreview) require(qaUpdateFlag == null) { "Beta 3 preview must not use persistent QA signing" }
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
android {
    namespace = "com.playereden.sudokutogether"
    compileSdk = 36
    defaultConfig {
        applicationId = if (permanentIdentityStaging || signedBeta) "com.playereden.gridmates.sudoku.beta" else "com.playereden.sudokutogether"
        minSdk = 26; targetSdk = 36; versionCode = qaVersion ?: 31; versionName = "1.0.0-beta.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    androidResources { noCompress += "m4a" }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
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

