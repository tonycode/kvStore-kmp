package dev.tonycode.kvstore.coroutines

import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory.Type


object ConcurrentTransactionalKeyValueStoreFactory {

    fun create(type: Type = Type.RedBlackTree): ConcurrentTransactionalKeyValueStore =
        ConcurrentTransactionalKeyValueStoreImpl(type)

}
