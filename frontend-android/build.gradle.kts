import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.grgit)
}


android {
    namespace = "dev.tonycode.kmp.android"

    defaultConfig {
        applicationId = "dev.tonycode.kmp.android"
        versionCode = 2
        versionName = "0.1.0"
        archivesName = "app-v$versionName-build_$versionCode"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        //multiDexEnabled = true
        //vectorDrawables.useSupportLibrary = true
        resourceConfigurations.addAll(listOf("en"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // build info
        buildConfigField("String", "APP_NAME", "\"${ rootProject.name }\"")
        buildConfigField("String", "GIT_BRANCH_NAME", "\"${ grgit.branch.current().name }\"")
        buildConfigField("String", "GIT_COMMIT_ID", "\"${ grgit.head().abbreviatedId }\"")
        buildConfigField("String", "BUILD_TIME", "\"${
            SimpleDateFormat("yyyy.MM.dd EEE 'at' HH:mm:ss.SSS z").apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }.format(Date(System.currentTimeMillis()))
        }\"")
        buildConfigField("long", "BUILD_TIME_MILLIS", "${ System.currentTimeMillis() }L")
    }

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    sourceSets {
        all {
            kotlin.srcDir("src/$name/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        encoding = "UTF-8"

        //isCoreLibraryDesugaringEnabled = true  // https://developer.android.com/studio/write/java8-support#library-desugaring
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get()
    }


    signingConfigs {
        create("release") {
            storeFile = file("../android-keystore/test.jks")
            storePassword = "test1234"
            keyAlias = "key0"
            keyPassword = "test1234"
        }
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "app dbg")
            applicationIdSuffix = ".debug"

            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }

        release {
            resValue("string", "app_name", "app")
            signingConfig = signingConfigs.getByName("release")

            isDebuggable = false
            // https://developer.android.com/studio/build/shrink-code
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    testCoverage {
        jacocoVersion = libs.versions.jacoco.get()
    }
}

dependencies {
    //// Core
    //implementation(libs.androidx.multidex)
    implementation(platform(libs.kotlin.bom))  // Align versions of all Kotlin components
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)

    implementation(projects.kvstoreCore)
    implementation(projects.frontendCommon)

    //// UI
    implementation(libs.androidx.lifecycle)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.android.material)
    implementation(libs.androidx.compose.material3)

    //// Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //// Unit tests
    testImplementation(libs.junit)

    //// Automated ui tests
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
}
