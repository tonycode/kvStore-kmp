package dev.tonycode.kvstore.coroutines

import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory.Type
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


internal class ConcurrentTransactionalKeyValueStoreImpl(
    type: Type = Type.RedBlackTree,
) : ConcurrentTransactionalKeyValueStore {

    private val store = TransactionalKeyValueStoreFactory.create(type)

    private val mutex = Mutex()


    override suspend fun onCommand(command: Command): ExecutionResult = mutex.withLock {
        return store.onCommand(command)
    }

}
