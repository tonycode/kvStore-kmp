package dev.tonycode.kmp.common

import dev.tonycode.kvstore.Operation
import dev.tonycode.kvstore.OperationResult
import dev.tonycode.kvstore.TransactionalKeyValueStore


// immutable
data class KvStoreUiState(
    val executionResult: String? = null,
) {
    fun mutate(
        cmd: String,
        trkvs: TransactionalKeyValueStore
    ): KvStoreUiState {
        val op: Operation
        try {
            op = Operation.fromString(cmd)
        } catch (iae: IllegalArgumentException) {
            return KvStoreUiState(
                executionResult = (iae.message ?: "unknown error")
            )
        }

        return KvStoreUiState(
            executionResult = when (
                val opResult = trkvs.onOperation(op)
            ) {
                is OperationResult.Success -> "OK"

                is OperationResult.SuccessWithResult -> opResult.result

                is OperationResult.SuccessWithIntResult -> opResult.result.toString()

                is OperationResult.Error -> opResult.errorMessage
            }
        )
    }
}
