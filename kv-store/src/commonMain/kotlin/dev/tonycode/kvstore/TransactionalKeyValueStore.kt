package dev.tonycode.kvstore


class TransactionalKeyValueStore {

    private val store = mutableListOf<HashMap<String, String>>()

    init {
        store.add(hashMapOf())
    }

    fun onOperation(operation: Operation): OperationResult {
        when (operation) {
            is Operation.Set -> {
                store.last()[operation.key] = operation.value
                return OperationResult.Success
            }

            is Operation.Get -> {
                if (!store.last().containsKey(operation.key)) return OperationResult.Error(MSG_KEY_NOT_SET)

                val value = store.last()[operation.key]!!
                return OperationResult.SuccessWithResult(value)
            }

            is Operation.Delete -> {
                store.last().remove(operation.key)
                return OperationResult.Success
            }

            is Operation.Count -> {
                val result = store.last().count { it.value == operation.value }
                return OperationResult.SuccessWithIntResult(result)
            }

            //region transaction commands
            is Operation.Begin -> {
                store.add(HashMap(store.last()))
                return OperationResult.Success
            }

            is Operation.Commit -> {
                if (store.size == 1) return OperationResult.Error(MSG_NO_TRANSACTION)

                store.removeAt(store.size-2)
                return OperationResult.Success
            }

            is Operation.Rollback -> {
                if (store.size == 1) return OperationResult.Error(MSG_NO_TRANSACTION)

                store.removeAt(store.size-1)
                return OperationResult.Success
            }
            //endregion
        }
    }

}

sealed class Operation {
    /**
     * Store the value for key
     */
    data class Set(val key: String, val value: String) : Operation()

    /**
     * Return the current value for key
     */
    data class Get(val key: String) : Operation()

    /**
     * Remove the entry for key
     */
    data class Delete(val key: String) : Operation()

    /**
     * Return the number of keys that have the given value
     */
    data class Count(val value: String) : Operation()

    /**
     * Start a new transaction
     */
    data object Begin : Operation()

    /**
     * Complete the current transaction
     */
    data object Commit : Operation()

    /**
     * Revert to state prior to BEGIN call
     */
    data object Rollback : Operation()
}

sealed class OperationResult {
    data object Success : OperationResult()

    data class SuccessWithResult(val result: String) : OperationResult()

    data class SuccessWithIntResult(val result: Int) : OperationResult()

    data class Error(val errorMessage: String) : OperationResult()
}
