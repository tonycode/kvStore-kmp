package dev.tonycode.kmp.backend_jvm

import dev.tonycode.kmp.backend_jvm.plugins.configureRouting
import dev.tonycode.kmp.backend_jvm.util.getBuildInfo
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


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

    embeddedServer(Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

private fun Application.module() {
    configureRouting()
}
