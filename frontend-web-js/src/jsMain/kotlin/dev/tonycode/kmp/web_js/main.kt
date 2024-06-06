package dev.tonycode.kmp.web_js

import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dev.tonycode.kmp.common.DefaultDispatchers
import dev.tonycode.kmp.web_js.ui.App
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory
import react.create
import react.dom.client.createRoot
import web.dom.Element
import web.dom.document


fun main() {
    val rootContainer: Element =
        document.getElementById("root") ?: error("Couldn't find root container!")

    createRoot(rootContainer).render(
        App.create {
            storeFactory = LoggingStoreFactory(DefaultStoreFactory())
            trkvs = TransactionalKeyValueStoreFactory.create()
            dispatchers = DefaultDispatchers
        }
    )
}
