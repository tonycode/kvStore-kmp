package dev.tonycode.kvstore

import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.tree.redblacktree.RedBlackTree


/**
 * Implementation based on RedBlackTree
 *
 * Not thread-safe
 */
internal class TransactionalKeyValueStoreRbtImpl : TransactionalKeyValueStore {

    private val store = mutableListOf(RedBlackTree<String, String>())


    override fun onCommand(command: Command): ExecutionResult {
        when (command) {
            is Command.Set -> {
                store.last().insert(command.key, command.value)
                return ExecutionResult.Success
            }

            is Command.Get -> {
                val item = store.last().find(command.key) ?: return ExecutionResult.Error(MSG_KEY_NOT_SET)

                return ExecutionResult.SuccessWithResult(item.second)
            }

            is Command.Delete -> {
                store.last().find(command.key) ?: return ExecutionResult.Error(MSG_KEY_NOT_SET)

                store.last().delete(command.key)
                return ExecutionResult.Success
            }

            is Command.Count -> {
                val result = store.last().count { it.second == command.value }
                return ExecutionResult.SuccessWithIntResult(result)
            }

            //region transaction commands
            is Command.Begin -> {
                store.add(store.last().clone())
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
