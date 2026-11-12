plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.secrets)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.ksp)
}

android {
    namespace = "com.example.androidweather"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.androidweather"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            pickFirsts += "assets/PublicSuffixDatabase.list"
            pickFirsts += "/assets/PublicSuffixDatabase.list"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Koin for Android
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.test)
    implementation(libs.koin.androidx.compose)

    /// Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.serialization.kotlinx)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client.log)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.content.negociation)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Navigation
    implementation(libs.compose.navigation)

    // Location
    implementation(libs.play.service.location)

    // Map
    implementation(libs.play.service.map)
    implementation(libs.compose.map)

    // Lottie
    implementation(libs.lottie)

    // Datastore
    implementation(libs.androidx.datastore)

    // Room
    implementation(libs.android.room)
    annotationProcessor(libs.android.room.compiler)
    implementation(libs.android.room.ktx)
    ksp(libs.android.room.compiler)
    implementation(libs.android.gson)
}
