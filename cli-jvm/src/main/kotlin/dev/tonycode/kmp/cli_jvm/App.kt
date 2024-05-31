package dev.tonycode.kmp.cli_jvm

import dev.tonycode.kmp.cli_jvm.util.getBuildInfo
import dev.tonycode.kmp.lib.Adder


/**
 * @param args provided via `java -jar cli-jvm/build/libs/app-<ver>-all.jar arg1 arg2 ...`
 *   or `./cli-jvm/build/distributions/app-<ver>/bin/app arg1 arg2` (after `./gradlew cli-jvm:distTar`)
 */
fun main(args: Array<String>) {
    println("""
        Hello from ${ BuildConfig.APP_NAME }!

        Version info: ${ getBuildInfo() }

        args="${ args.joinToString("; ") }"
    """.trimIndent())

    val adder = Adder()
    println("2 + 3 = ${ adder.add(2, 3) }")
}
