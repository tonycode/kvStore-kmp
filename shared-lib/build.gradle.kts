plugins {
    alias(libs.plugins.kotlin.multiplatform)
}


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

    // web-js
    js {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.kotlin.bom.get()))  // Align versions of all Kotlin components
                implementation(libs.kotlin.stdlib)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
