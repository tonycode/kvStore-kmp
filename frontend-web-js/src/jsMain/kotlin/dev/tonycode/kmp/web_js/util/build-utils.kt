package dev.tonycode.kmp.web_js.util

import dev.tonycode.kmp.web_js.BuildConfig


fun getBuildInfo(): String {
    return "${BuildConfig.APP_NAME} v${BuildConfig.BUILD_VERSION}" +
        " (" +
        "#${BuildConfig.BUILD_NUMBER}" +
        ", git: ${BuildConfig.GIT_BRANCH_NAME}@${BuildConfig.GIT_COMMIT_ID}" +
        ", built on ${BuildConfig.BUILD_TIME}" +
        ")"
}
