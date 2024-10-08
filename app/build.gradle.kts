plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    // Firebase
    id("com.google.gms.google-services")

    // Hilt
    id("com.google.dagger.hilt.android")

    // Kapt
    id("kotlin-kapt")
}

android {
    namespace = "com.wcsm.confectionaryadmin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wcsm.confectionaryadmin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi"
    }
    buildFeatures {
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Extended Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Jetpack Security
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Room
    implementation("androidx.room:room-ktx:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
