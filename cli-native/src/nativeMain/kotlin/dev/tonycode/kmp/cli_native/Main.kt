package dev.tonycode.kmp.cli_native

import dev.tonycode.kmp.cli_native.util.getBuildInfo
import dev.tonycode.kmp.lib.Adder


/**
 * @param args provided via `java -jar app/build/libs/app-<ver>-all.jar arg1 arg2 ...`
 *   or `./app/build/distributions/app-<ver>/bin/app arg1 arg2` (after `./gradlew distTar`)
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
