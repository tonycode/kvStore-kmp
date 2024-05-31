package dev.tonycode.kmp.android.util

import dev.tonycode.kmp.android.BuildConfig


fun getBuildInfo(): String {
    return "${BuildConfig.APP_NAME} v${BuildConfig.VERSION_NAME}" +
        " (" +
        "#${BuildConfig.VERSION_CODE}" +
        ", git: ${BuildConfig.GIT_BRANCH_NAME}@${BuildConfig.GIT_COMMIT_ID}" +
        ", built on ${BuildConfig.BUILD_TIME}" +
        ")"
}
