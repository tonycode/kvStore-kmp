package dev.tonycode.kmp.backend_jvm.plugins

import dev.tonycode.kmp.backend_jvm.BuildConfig
import dev.tonycode.kmp.backend_jvm.util.getBuildInfo
import dev.tonycode.kvstore.TransactionalKeyValueStore
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


fun Application.configureRouting() {

    val trkvs: TransactionalKeyValueStore = TransactionalKeyValueStoreFactory.create()


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
            val commandString = call.receive<String>()

            val command: Command
            try {
                command = Command.fromString(commandString)
            } catch (iae: IllegalArgumentException) {
                call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = iae.message ?: "unknown error"
                )
                return@post
            }

            when (
                val executionResult = trkvs.onCommand(command)
            ) {
                is ExecutionResult.Success -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = "OK"
                )

                is ExecutionResult.SuccessWithResult -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = executionResult.result
                )

                is ExecutionResult.SuccessWithIntResult -> call.respondText(
                    status = HttpStatusCode.OK,
                    text = executionResult.result.toString()
                )

                is ExecutionResult.Error -> call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = executionResult.errorMessage
                )
            }
        }

    }

}
