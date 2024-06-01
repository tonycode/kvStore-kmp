package dev.tonycode.kvstore.coroutines

import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult


interface ConcurrentTransactionalKeyValueStore {

    suspend fun onCommand(command: Command): ExecutionResult

}
