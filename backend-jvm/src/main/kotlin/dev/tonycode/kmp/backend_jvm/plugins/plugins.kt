package dev.tonycode.kmp.backend_jvm.plugins

import dev.tonycode.kmp.backend_jvm.BuildConfig
import dev.tonycode.kmp.backend_jvm.util.getBuildInfo
import dev.tonycode.kmp.lib.Adder
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


fun Application.configureRouting() {

    val adder = Adder()


    routing {

        get("/") {
            call.application.environment.log.info(">>> got req!")
            call.respondText("""
                Hello, World from ${BuildConfig.APP_NAME}!
                2 + 3 = ${ adder.add(2, 3) }
            """.trimIndent())
        }

        get("/ping") {
            call.respondText("""
                pong!
                ${ getBuildInfo() }
            """.trimIndent())
        }

        post("/add") {
            val s = call.receive<String>()
            val numbers = s.split(Regex("\\s+")).map { it.toIntOrNull() }
            if (numbers.size != 2 || numbers.any { it == null }) {
                call.respondText("provide two integer numbers")
                return@post
            }

            val result = adder.add(numbers[0]!!, numbers[1]!!)
            call.respondText("result = $result")
        }

    }

}
