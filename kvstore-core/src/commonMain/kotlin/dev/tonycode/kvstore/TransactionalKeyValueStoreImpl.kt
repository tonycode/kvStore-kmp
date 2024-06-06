package dev.tonycode.kvstore

import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult


/**
 * Implementation based on Kotlin stdlib's HashMap
 *
 * Not thread-safe
 */
internal class TransactionalKeyValueStoreImpl : TransactionalKeyValueStore {

    private val store = mutableListOf(hashMapOf<String, String>())


    override fun onCommand(command: Command): ExecutionResult {
        when (command) {
            is Command.Set -> {
                store.last()[command.key] = command.value
                return ExecutionResult.Success
            }

            is Command.Get -> {
                if (!store.last().containsKey(command.key)) return ExecutionResult.Error(MSG_KEY_NOT_SET)

                val value = store.last()[command.key]!!
                return ExecutionResult.SuccessWithResult(value)
            }

            is Command.Delete -> {
                if (!store.last().containsKey(command.key)) return ExecutionResult.Error(MSG_KEY_NOT_SET)

                store.last().remove(command.key)
                return ExecutionResult.Success
            }

            is Command.Count -> {
                val result = store.last().count { it.value == command.value }
                return ExecutionResult.SuccessWithIntResult(result)
            }

            //region transaction commands
            is Command.Begin -> {
                store.add(HashMap(store.last()))
                return ExecutionResult.Success
            }

            is Command.Commit -> {
                if (store.size == 1) return ExecutionResult.Error(MSG_NO_TRANSACTION)

                store.removeAt(store.size - 2)
                return ExecutionResult.Success
            }

            is Command.Rollback -> {
                if (store.size == 1) return ExecutionResult.Error(MSG_NO_TRANSACTION)

                store.removeAt(store.size - 1)
                return ExecutionResult.Success
            }
            //endregion
        }
    }

}
