package dev.tonycode.kmp.backend_jvm.plugins

import dev.tonycode.kmp.backend_jvm.BuildConfig
import dev.tonycode.kmp.backend_jvm.util.getBuildInfo
import dev.tonycode.kvstore.Operation
import dev.tonycode.kvstore.OperationResult
import dev.tonycode.kvstore.TransactionalKeyValueStore
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


fun Application.configureRouting() {

    val trkvs = TransactionalKeyValueStore()


    routing {

        get("/ping") {
            call.respondText("""
                pong!

                ${ getBuildInfo() }
            """.trimIndent())
        }

        get {
            call.respondText("""
                Hello from ${BuildConfig.APP_NAME}!

                Use POST to execute commands
                """.trimIndent())
        }

        post {
            val s = call.receive<String>()

            val op: Operation
            try {
                op = Operation.fromString(s)
            } catch (iae: IllegalArgumentException) {
                call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = iae.message ?: "unknown error"
                )
                return@post
            }

            when (
                val opResult = trkvs.onOperation(op)
            ) {
                is OperationResult.Success -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = "OK"
                )

                is OperationResult.SuccessWithResult -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = opResult.result
                )

                is OperationResult.SuccessWithIntResult -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = opResult.result.toString()
                )

                is OperationResult.Error -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = opResult.errorMessage
                )
            }
        }

    }

}
