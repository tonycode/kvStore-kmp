package dev.tonycode.kmp.cli_native

import dev.tonycode.kmp.cli_native.util.getBuildInfo
import dev.tonycode.kvstore.Operation
import dev.tonycode.kvstore.OperationResult
import dev.tonycode.kvstore.TransactionalKeyValueStore


fun main() {

    println("""
        Hello from ${ BuildConfig.APP_NAME }!

        Version info: ${ getBuildInfo() }

    """.trimIndent())

    val trkvs = TransactionalKeyValueStore()

    do {
        print("> ")
        val s = readlnOrNull() ?: return

        val op: Operation
        try {
            op = Operation.fromString(s)
        } catch (iae: IllegalArgumentException) {
            println(iae.message ?: "unknown error")
            continue
        }

        when (
            val opResult = trkvs.onOperation(op)
        ) {
            is OperationResult.Success -> { }

            is OperationResult.SuccessWithResult -> println(opResult.result)

            is OperationResult.SuccessWithIntResult -> println(opResult.result.toString())

            is OperationResult.Error -> println(opResult.errorMessage)
        }

    } while(true)
}
