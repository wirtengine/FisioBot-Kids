pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< HEAD
        maven { url = uri("https://jitpack.io") }   // ← añadido para MPAndroidChart
    }
}

rootProject.name = "FisioBotKids"
include(":app")
=======
    }
}

rootProject.name = "Roboapp"
include(":app")
>>>>>>> 5a7cd6fbbd02e6e979d14afa24f85d0f8fe13f1a
