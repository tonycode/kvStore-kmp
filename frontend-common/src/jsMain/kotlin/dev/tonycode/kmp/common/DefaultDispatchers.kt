package dev.tonycode.kmp.common

import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


object DefaultDispatchers : IDispatchers, JvmSerializable {

    override val main: CoroutineDispatcher get() = Dispatchers.Main.immediate
    override val io: CoroutineDispatcher get() = Dispatchers.Main
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined

}
