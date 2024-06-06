plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}


val projectGroup = "dev.tonycode.kmp"
val projectArtifact = "frontend-common"
val projectVersion = "0.1.0"

group = projectGroup
version = projectVersion


val currentOs = org.gradle.internal.os.OperatingSystem.current()!!
val isLinux = currentOs.isLinux
val isMacOsX = currentOs.isMacOsX
val isWindows = currentOs.isWindows

kotlin {
    if (true !in listOf(isLinux, isMacOsX, isWindows)) error("UNSUPPORTED host: ${currentOs.name}")

    // native
    if (isLinux) {
        linuxX64()
    }
    if (isMacOsX) {
        macosX64()
    }
    if (isWindows) {
        mingwX64()
    }

    // jvm
    jvm()
    jvmToolchain(libs.versions.java.get().toInt())

    // android
    androidTarget()

    // web-js
    js {
        moduleName = projectArtifact  // used for: package.json
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.kotlin.bom.get()))  // Align versions of all Kotlin components
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.mvikotlin)
                implementation(libs.mvikotlin.main)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.essenty)
                implementation(libs.instanceKeeper)

                implementation(projects.kvstoreCore)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.truthish)
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "$projectGroup.common.android"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    sourceSets {
        getByName("main") {
            kotlin.srcDir("src/androidMain/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        encoding = "UTF-8"
    }

}
