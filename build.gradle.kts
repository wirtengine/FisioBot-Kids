<<<<<<< HEAD
=======
// Top-level build file where you can add configuration options common to all sub-projects/modules.
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
<<<<<<< HEAD
=======
    // ✅ Aquí sí, con version y apply false
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
    id("com.google.gms.google-services") version "4.4.0" apply false
}