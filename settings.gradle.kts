rootProject.name = "kvStore-kmp"


pluginManagement {
    repositories {
        gradlePluginPortal()  // https://plugins.gradle.org
        google()  // https://maven.google.com/web/index.html - Android-specific artifacts
        mavenCentral()  // https://repo.maven.apache.org/maven2/
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_PROJECT  // kotlin-js adds "Node Distributions" repo

    repositories {
        // https://mvnrepository.com/ - find repository & artifact version for a given package

        google()  // https://maven.google.com/web/index.html - Android-specific artifacts
        mavenCentral()  // https://repo.maven.apache.org/maven2/
        //maven("https://jitpack.io")

        //mavenLocal()  // ~/.m2/repository
    }
}

// https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


include(
    "shared-lib",
    "kv-store",
    "cli-native",
    "cli-jvm",
    "backend-jvm",
    "frontend-android",
    "frontend-web-js",
)
