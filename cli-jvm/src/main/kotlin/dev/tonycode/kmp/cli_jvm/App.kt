package dev.tonycode.kmp.cli_jvm

import dev.tonycode.kmp.cli_jvm.util.getBuildInfo
import dev.tonycode.kvstore.TransactionalKeyValueStore
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult


fun main() {

    println("""
        Hello from ${ BuildConfig.APP_NAME }!

        Version info: ${ getBuildInfo() }

    """.trimIndent())

    val trkvs = TransactionalKeyValueStore()

    do {
        print("> ")
        val commandString = readlnOrNull() ?: return

        val command: Command
        try {
            command = Command.fromString(commandString)
        } catch (iae: IllegalArgumentException) {
            println(iae.message ?: "unknown error")
            continue
        }

        when (
            val executionResult = trkvs.onCommand(command)
        ) {
            is ExecutionResult.Success -> { }

            is ExecutionResult.SuccessWithResult -> println(executionResult.result)

            is ExecutionResult.SuccessWithIntResult -> println(executionResult.result.toString())

            is ExecutionResult.Error -> println(executionResult.errorMessage)
        }

    } while(true)
}
