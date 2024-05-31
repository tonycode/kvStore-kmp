package dev.tonycode.kvstore.tree


interface Tree<K: Comparable<K>, V> {

    fun insert(key: K, value: V)

    fun find(key: K): Pair<K, V>?

    fun delete(key: K)

}
