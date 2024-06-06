import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)  // Adds support for Kotlin/JS

    alias(libs.plugins.grgit)
    alias(libs.plugins.buildconfig)  // Adds support for BuildConfig.kt object generation
    //alias(libs.plugins.localproperties)  // Adds support to override certain gradle.properties with local.properties
}


val projectGroup = "dev.tonycode.kmp"
val projectArtifact = "web-js"
val projectPackage = "$projectGroup.$projectArtifact".replace("-", "_")
val projectName = rootProject.name
val projectVersion = "0.2.0"
val buildNumber = 4
val isProduction: Boolean = (properties["prod"] == "true")  // turn off debugging stuff (sourcemaps/logging/..) for prod

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

    buildConfigField("IS_PRODUCTION", isProduction)
}


kotlin {
    js {
        browser {
            commonWebpackConfig {
                outputFileName = "sample-client-web.js"
                sourceMaps = !isProduction
                cssSupport { enabled = true }
            }
        }

        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                //// Core
                implementation(project.dependencies.platform(libs.kotlin.bom.get()))  // Align versions of all Kotlin components
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.mvikotlin)
                implementation(libs.mvikotlin.main)
                implementation(libs.mvikotlin.logging)
                //implementation(libs.mvikotlin.timetravel)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.essenty)
                implementation(libs.instanceKeeper)

                implementation(projects.kvstoreCore)
                implementation(projects.frontendCommon)

                //// UI
                implementation(project.dependencies.platform(libs.kotlin.wrappers.bom.get()))
                implementation(libs.kotlinw.react)
                implementation(libs.kotlinw.react.dom)
                implementation(libs.kotlinw.emotion)
                implementation(libs.kotlinw.antd)
            }
        }
    }
}


tasks.withType<KotlinJsCompile>().configureEach {
    kotlinOptions {
        moduleName = projectArtifact
        sourceMap = !isProduction
    }
}

yarn.ignoreScripts = false  // WA from https://youtrack.jetbrains.com/issue/KT-52578
