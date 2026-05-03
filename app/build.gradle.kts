plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
<<<<<<< HEAD
=======
    // ✅ Plugin de Google Services - SOLO el ID, sin version, sin apply false
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    id("com.google.gms.google-services")
}

android {
<<<<<<< HEAD
    namespace = "com.example.fisiobotkids"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fisiobotkids"
        minSdk = 30
        targetSdk = 36
=======
    namespace = "com.example.roboapp"
    compileSdk = 35  // o 34, 35 es el recomendado actual

    defaultConfig {
        applicationId = "com.example.roboapp"
        minSdk = 24
        targetSdk = 35
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
<<<<<<< HEAD
=======

>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
<<<<<<< HEAD
    kotlinOptions {
        jvmTarget = "11"
    }
=======

    kotlinOptions {
        jvmTarget = "11"
    }

>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    buildFeatures {
        compose = true
    }
}

dependencies {
<<<<<<< HEAD
    // ── Core AndroidX y Lifecycle ──
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ── Jetpack Compose (Material 3) ──
=======
    // --- Tus dependencias existentes ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
<<<<<<< HEAD
    // Iconos extendidos (necesario para Icons.Default.Favorite, etc.)
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    // ── Firebase ──
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-database-ktx")   // Realtime Database
    implementation("com.google.firebase:firebase-auth-ktx")       // Autenticación

    // ── Navegación Compose ──
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // ── ViewModel + Compose ──
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    // ── Animaciones Lottie ──
    implementation("com.airbnb.android:lottie-compose:6.1.0")

    // ── Gráficas MPAndroidChart ──
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // ── Testing ──
=======
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)

    // Navegación
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Iconos extendidos
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    // Coil para imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")

    // Testing
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
<<<<<<< HEAD
=======

    // 🔥 FIREBASE (todo junto al final)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
}