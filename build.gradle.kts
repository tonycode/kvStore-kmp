import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
    `kotlin-dsl`  // to configure Yarn & NodeJs
    alias(libs.plugins.gradle.versions)

    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.grgit) apply false
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().apply {
        version = libs.versions.yarn.get()
        lockFileDirectory = projectDir
        yarnLockMismatchReport = YarnLockMismatchReport.NONE
        ignoreScripts = false  // WA from https://youtrack.jetbrains.com/issue/KT-52578
    }
}

plugins.withType<NodeJsRootPlugin> {
    the<NodeJsRootExtension>().apply {
        nodeVersion = libs.versions.nodejs.get()
    }
}


//region tasks
/* generates gradle-wrapper via `gradle wrapper` */
tasks.wrapper {
    gradleVersion = libs.versions.gradle.get()
    distributionType = Wrapper.DistributionType.ALL
}

fun String.isStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { this.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    return (stableKeyword || regex.matches(this))
}

fun String.isNonStable(): Boolean = isStable().not()

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable() && currentVersion.isStable()
    }

    checkForGradleUpdate = true
    outputFormatter = "html"  // https://github.com/ben-manes/gradle-versions-plugin#report-format
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

tasks.register("showDependencyUpdates") {
    dependsOn("dependencyUpdates")
    doLast {
        exec {
            commandLine(
                "xdg-open",
                "build/dependencyUpdates/report.html"
            )
        }
    }
}
//endregion
