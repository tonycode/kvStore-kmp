package dev.tonycode.kmp.common.main

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.tonycode.kmp.common.IDispatchers
import dev.tonycode.kmp.common.main.store.mainStore
import dev.tonycode.kvstore.TransactionalKeyValueStore


class MainController(
    private val storeFactory: StoreFactory,
    private val trkvs: TransactionalKeyValueStore,
    instanceKeeper: InstanceKeeper,
    private val dispatchers: IDispatchers,
) {

    private val mainStore = instanceKeeper.getStore {
        storeFactory.mainStore(
            trkvs = trkvs,
            mainContext = dispatchers.main,
            ioContext = dispatchers.io
        )
    }


    fun onViewCreated(view: MainView, viewLifecycle: Lifecycle) {
        bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            view.events bindTo mainStore
        }

        bind(viewLifecycle, BinderLifecycleMode.START_STOP, dispatchers.unconfined) {
            mainStore.states bindTo view
        }
    }

}
