import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


plugins {
    alias(libs.plugins.kotlin.jvm)
    application  // Adds support for building a CLI application in Java

    alias(libs.plugins.grgit)
    alias(libs.plugins.buildconfig)  // Adds support for BuildConfig.kt object generation
    //alias(libs.plugins.localproperties)  // Adds support to override certain gradle.properties with local.properties
}


val projectGroup = "dev.tonycode.kmp"
val projectArtifact = "backend-jvm"
val projectPackage = "$projectGroup.$projectArtifact".replace("-", "_")
val projectName = rootProject.name
val projectVersion = "0.1.0"
val buildNumber = 2

group = projectGroup
version = projectVersion

buildConfig {
    packageName(projectPackage)

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

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

application {
    mainClass = "$projectPackage.AppKt"
}


dependencies {
    //// Core
    implementation(platform(libs.kotlin.bom))  // Align versions of all Kotlin components
    implementation(libs.kotlin.stdlib)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback)

    implementation(projects.kvstoreCore)
    implementation(projects.kvstoreCoroutines)
}


tasks.register("ping") {
    description = "Check whether app is responding"
    group = "Verification"

    doLast {
        exec {
            commandLine(
                "curl",
                "--silent",  // don't show %-table
                "--verbose",  // show details & http headers
                "http://127.0.0.1:8080/ping"
            )
        }
    }
}
