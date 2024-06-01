import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile


plugins {
    alias(libs.plugins.kotlin.multiplatform)
}


val projectGroup = "dev.tonycode.kmp"
val projectArtifact = "kvstore-core"
val projectVersion = libs.versions.kvstore.get()

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

    // web-js
    js {
        moduleName = projectArtifact  // used for: package.json
        browser()

        compilations {
            val main by getting
            main.apply {
                kotlinOptions {
                    // used for: klib/manifest/unique_name
                    freeCompilerArgs += "-Xir-module-name=$projectGroup:$projectArtifact"
                }
            }
        }
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
                implementation(libs.truthish)
                implementation(libs.kotlin.test)
            }
        }
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    kotlinOptions {
        //moduleName = projectArtifact
        moduleKind = "umd"
        sourceMap = true
    }
}
