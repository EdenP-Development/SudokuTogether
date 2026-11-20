import java.util.Properties

plugins { id("com.android.application"); kotlin("android"); kotlin("plugin.compose"); kotlin("plugin.serialization") }
val signingFile = System.getenv("SUDOKU_SIGNING_FILE")?.let { file(it) }
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
val versusV6Preview = providers.gradleProperty("gridmatesVersusV6Preview").orNull?.let {
    require(it == "true") { "gridmatesVersusV6Preview only accepts true" }
    true
} ?: false
require(listOf(versusV6Preview, beta2GardenPreview, beta2PortraitPreview, woodlandV5Preview).count { it } <= 1) {
    "Only one isolated preview type can be selected"
}
val qaUpdateFlag = providers.gradleProperty("gridmatesQaUpdate").orNull
require(qaUpdateFlag == null || qaUpdateFlag == "true") { "gridmatesQaUpdate must be true when supplied" }
val qaVersion = providers.gradleProperty("gridmatesQaVersionCode").orNull?.let { value ->
    require(value.matches(Regex("[1-9][0-9]{0,9}"))) { "QA version code must be a positive integer" }
    val number = value.toLong()
    require(number in 20..2100000000L) { "QA updates must be newer than version 19 and within Android's range" }
    number.toInt()
}
require((qaUpdateFlag == "true") == (qaVersion != null)) { "QA update mode and version code must be supplied together" }
require(!(beta2PortraitPreview && qaVersion != null)) { "Portrait preview must not share QA-update identity" }
require(!(beta2GardenPreview && qaVersion != null)) { "Garden preview must not share QA-update identity" }
require(!(versusV6Preview && qaVersion != null)) { "Versus V6 reviewer must not share QA-update identity" }
require(!(woodlandV5Preview && qaVersion != null)) {
    "Woodland V5 preview must not use a same-package QA update"
}
val qaKeystore = System.getenv("SUDOKU_QA_KEYSTORE_FILE")?.let { file(it) }
if (qaUpdateFlag == "true") require(qaKeystore?.isFile == true) {
    "Restore the existing private QA keystore before building an update; do not generate a replacement key"
}
android {
    namespace = "com.playereden.sudokutogether"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.playereden.sudokutogether"
        minSdk = 26; targetSdk = 36; versionCode = qaVersion ?: 31; versionName = "1.0.0-beta.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
                versusV6Preview -> ".qa.versusv6"
                beta2GardenPreview -> ".qa.beta2garden"
                beta2PortraitPreview -> ".qa.beta2portraits"
                woodlandV5Preview -> ".qa.woodlandv51"
                else -> ".qa"
            }
            versionNameSuffix = when {
                versusV6Preview -> "-qa.versusv6"
                beta2GardenPreview -> "-qa.beta2garden"
                beta2PortraitPreview -> "-qa.beta2portraits"
                woodlandV5Preview -> "-qa.woodlandv51"
                qaVersion != null -> "-qa.$qaVersion"
                else -> "-qa"
            }
            if (qaVersion != null) signingConfig = signingConfigs.getByName("qaContinuity")
        }
        release {
            isMinifyEnabled = false
            if (privateSigning.isNotEmpty()) signingConfig = signingConfigs.getByName("privateKey")
        }
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
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

