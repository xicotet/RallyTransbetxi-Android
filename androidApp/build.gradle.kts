import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.maps.platform.secrets)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    kotlin("kapt")
}

// Create a variable called keystorePropertiesFile
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties object
val keystoreProperties = Properties()

// Load the keystore.properties file
keystoreProperties.load(FileInputStream(keystorePropertiesFile))
println("Resolved path: ${file(keystoreProperties["storeFile"] as String).absolutePath}") // TODO: Remove this print before production-ready
println("Keystore exists: ${file(keystoreProperties["storeFile"] as String).exists()}") // TODO: Remove this print before production-ready

android {
    namespace = "com.canolabs.rallytransbetxi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.canolabs.rallytransbetxi"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "0.4.0"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "DIRECTIONS_API_KEY", properties.getProperty("DIRECTIONS_API_KEY"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            // TODO Add this isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.lifecycle.runtime.ktx.v283)

    implementation(libs.androidx.activity.compose) // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui) // Compose
    implementation(libs.androidx.ui.graphics) // Compose
    implementation(libs.androidx.ui.tooling.preview) // Compose
    implementation(libs.androidx.material3) // Compose
    implementation(libs.androidx.navigation.compose) // Compose

    implementation(libs.maps.compose) // Maps Compose
    implementation(libs.play.services.maps) // Maps

    // New firebase
    implementation(libs.gitlive.firebase.firestore)  // Firebase Firestore
    implementation(libs.gitlive.firebase.messaging)  // Firebase Cloud Messaging
    implementation(libs.gitlive.firebase.config)     // Firebase Remote Config

    // Old firebase
    implementation(platform(libs.firebase.bom)) // Firebase BoM
    implementation(libs.firebase.analytics) // Firebase Analytics
    implementation(libs.firebase.crashlytics) // Firebase Crashlytics
    implementation(libs.firebase.storage.ktx) // Firebase Storage
    // implementation(libs.firebase.firestore) // Cloud Firestore
    implementation(libs.firebase.messaging) // Firebase Messaging
    implementation(libs.firebase.appcheck.playintegrity) // Firebase App Check
    implementation(libs.integrity) // Play Integrity
    implementation(libs.firebase.config.ktx) // Remote config

    implementation(libs.play.services.location) // Location services

    implementation(libs.androidx.vectordrawable) // Vector Drawable


    implementation(libs.revealswipe) // Reveal Swipe

    implementation(libs.androidx.room.ktx.v261) // Room
    implementation(libs.androidx.room.runtime.v261) // Room
    annotationProcessor(libs.androidx.room.compiler.v261)  // Room

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler.v261) // Kotlin annotation processing tool (kapt)

    implementation(libs.coil.compose) // Coil

    implementation(libs.androidx.core.splashscreen) // Splash Screen

    implementation(libs.kotlin.reflect) // Kotlin Reflect

    implementation(libs.ktor.client.core) // Ktor
    implementation(libs.ktor.client.okhttp) // Ktor
    implementation(libs.ktor.client.serialization) // Ktor
    implementation(libs.ktor.client.content.negotiation) // Ktor
    implementation(libs.ktor.client.logging) // Ktor

    implementation(libs.logging.interceptor.v491) // OkHttp Logging Interceptor

    implementation(libs.androidx.appcompat.v170)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewModel)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
