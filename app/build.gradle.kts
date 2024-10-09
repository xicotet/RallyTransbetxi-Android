import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
}

// Create a variable called keystorePropertiesFile
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties object
val keystoreProperties = Properties()

// Load the keystore.properties file
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.canolabs.rallytransbetxi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.canolabs.rallytransbetxi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"

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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val roomVersion = "2.6.1"

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    implementation("androidx.activity:activity-compose:1.9.0") // Compose
    implementation(platform("androidx.compose:compose-bom:2024.06.00")) // Compose
    implementation("androidx.compose.ui:ui") // Compose
    implementation("androidx.compose.ui:ui-graphics") // Compose
    implementation("androidx.compose.ui:ui-tooling-preview") // Compose
    implementation("androidx.compose.material3:material3") // Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Compose

    implementation("com.google.maps.android:maps-compose:2.11.4") // Maps Compose
    implementation("com.google.android.gms:play-services-maps:19.0.0") // Maps

    implementation(platform("com.google.firebase:firebase-bom:33.1.1")) // Firebase BoM
    implementation("com.google.firebase:firebase-analytics") // Firebase Analytics
    implementation("com.google.firebase:firebase-crashlytics") // Firebase Crashlytics
    implementation("com.google.firebase:firebase-storage-ktx") // Firebase Storage
    implementation("com.google.firebase:firebase-firestore") // Cloud Firestore
    implementation("com.google.firebase:firebase-messaging") // Firebase Messaging
    implementation("com.google.firebase:firebase-appcheck-playintegrity") // Firebase App Check
    implementation("com.google.android.play:integrity:1.4.0") // Play Integrity

    implementation("com.google.android.gms:play-services-location:21.3.0") // Location services

    implementation("androidx.vectordrawable:vectordrawable:1.2.0") // Vector Drawable

    implementation("de.charlex.compose:revealswipe:2.0.0-beta01") // Reveal Swipe

    implementation("com.google.dagger:hilt-android:2.50") // Hilt
    kapt("com.google.dagger:hilt-android-compiler:2.50") // Hilt

    implementation("androidx.room:room-ktx:$roomVersion") // Room
    implementation("androidx.room:room-runtime:$roomVersion") // Room
    annotationProcessor("androidx.room:room-compiler:$roomVersion")  // Room

    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$roomVersion") // Kotlin annotation processing tool (kapt)

    implementation("io.coil-kt:coil-compose:2.6.0") // Coil

    implementation("androidx.core:core-splashscreen:1.0.1") // Splash Screen

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23") // Kotlin Reflect

    implementation("com.squareup.retrofit2:retrofit:2.11.0") // Retrofit
    implementation("com.google.code.gson:gson:2.10.1") // Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // Gson Converter

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1") // OkHttp Logging Interceptor

    implementation("androidx.appcompat:appcompat:1.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}
