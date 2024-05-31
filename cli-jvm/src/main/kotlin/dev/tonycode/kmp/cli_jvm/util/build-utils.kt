package dev.tonycode.kmp.cli_jvm.util

import dev.tonycode.kmp.cli_jvm.BuildConfig


fun getBuildInfo(): String {
    return "${BuildConfig.APP_NAME} v${BuildConfig.BUILD_VERSION}" +
        " (" +
        "#${BuildConfig.BUILD_NUMBER}" +
        ", git: ${BuildConfig.GIT_BRANCH_NAME}@${BuildConfig.GIT_COMMIT_ID}" +
        ", built on ${BuildConfig.BUILD_TIME}" +
        ")"
}
