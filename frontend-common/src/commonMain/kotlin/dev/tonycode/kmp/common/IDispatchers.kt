package dev.tonycode.kmp.common

import kotlinx.coroutines.CoroutineDispatcher


interface IDispatchers {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher

}
