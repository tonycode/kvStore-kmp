package dev.tonycode.kmp.cli_native

import dev.tonycode.kmp.cli_native.util.getBuildInfo
import dev.tonycode.kvstore.TransactionalKeyValueStore
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.TransactionalKeyValueStoreImpl


fun main() {

    println("""
        Hello from ${ BuildConfig.APP_NAME }!

        Version info: ${ getBuildInfo() }

    """.trimIndent())

    val trkvs: TransactionalKeyValueStore = TransactionalKeyValueStoreImpl()

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
