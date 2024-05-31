package dev.tonycode.kmp.common

import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.TransactionalKeyValueStore


// immutable
data class KvStoreUiState(
    val executionResult: String? = null,
) {
    fun mutate(
        commandString: String,
        trkvs: TransactionalKeyValueStore
    ): KvStoreUiState {
        val command: Command
        try {
            command = Command.fromString(commandString)
        } catch (iae: IllegalArgumentException) {
            return KvStoreUiState(
                executionResult = (iae.message ?: "unknown error")
            )
        }

        return KvStoreUiState(
            executionResult = when (
                val execResult = trkvs.onCommand(command)
            ) {
                is ExecutionResult.Success -> "OK"

                is ExecutionResult.SuccessWithResult -> execResult.result

                is ExecutionResult.SuccessWithIntResult -> execResult.result.toString()

                is ExecutionResult.Error -> execResult.errorMessage
            }
        )
    }
}
