package dev.tonycode.kmp.web_js

import dev.tonycode.kmp.web_js.ui.App
import react.create
import react.dom.client.createRoot
import web.dom.Element
import web.dom.document


fun main() {
    val rootContainer: Element =
        document.getElementById("root") ?: error("Couldn't find root container!")

    createRoot(rootContainer).render(
        App.create()
    )
}
