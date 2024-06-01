package dev.tonycode.kvstore


object TransactionalKeyValueStoreFactory {

    fun create(type: Type = Type.RedBlackTree) = when (type) {
        Type.HashMap -> TransactionalKeyValueStoreImpl()
        Type.RedBlackTree -> TransactionalKeyValueStoreRbtImpl()
    }


    enum class Type {
        HashMap,
        RedBlackTree,
    }

}
