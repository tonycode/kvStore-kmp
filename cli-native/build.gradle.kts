import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


plugins {
    alias(libs.plugins.kotlin.multiplatform)  // Adds support for Kotlin

    alias(libs.plugins.grgit)
    alias(libs.plugins.buildconfig)  // Adds support for BuildConfig.kt object generation
    //alias(libs.plugins.localproperties)  // Adds support to override certain gradle.properties with local.properties
}


val projectGroup = "dev.tonycode.kmp"
val projectArtifact = "cli-native"
val projectPackage = "$projectGroup.$projectArtifact".replace("-", "_")
val projectName = rootProject.name
val projectVersion = "0.1.0"
val buildNumber = 2

group = projectGroup
version = projectVersion

buildConfig {
    packageName = projectPackage

    // build info
    buildConfigField("APP_NAME", "$projectName/$projectArtifact")
    buildConfigField("BUILD_VERSION", projectVersion)
    buildConfigField("BUILD_NUMBER", buildNumber)
    buildConfigField("GIT_BRANCH_NAME", grgit.branch.current().name)
    buildConfigField("GIT_COMMIT_ID", grgit.head().abbreviatedId)
    buildConfigField("BUILD_TIME",
        SimpleDateFormat("yyyy.MM.dd EEE 'at' HH:mm:ss.SSS z").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }.format(Date(System.currentTimeMillis()))
    )
    buildConfigField("BUILD_TIME_MILLIS", System.currentTimeMillis())
}

val currentOs = org.gradle.internal.os.OperatingSystem.current()!!
val isLinux = currentOs.isLinux
val isMacOsX = currentOs.isMacOsX
val isWindows = currentOs.isWindows

kotlin {
    // native
    val nativeTarget = when {
        isLinux -> linuxX64("native")
        isMacOsX -> macosX64("native")
        isWindows -> mingwX64("native")
        else -> error("UNSUPPORTED host: ${currentOs.name}")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "$projectPackage.main"
            }
        }
    }


    sourceSets {
        nativeMain.dependencies {
            //// Core
            implementation(project.dependencies.platform(libs.kotlin.bom))  // Align versions of all Kotlin components
            implementation(libs.kotlin.stdlib)

            implementation(projects.kvstoreCore)
        }
    }
}

tasks.withType<Exec> {
    standardInput = System.`in` // Enable reading from stdin
}
