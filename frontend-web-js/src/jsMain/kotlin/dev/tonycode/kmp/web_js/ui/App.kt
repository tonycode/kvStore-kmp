package dev.tonycode.kmp.web_js.ui

import dev.tonycode.kmp.lib.Adder
import dev.tonycode.kmp.web_js.ui.components.KeyValueInput
import dev.tonycode.kmp.web_js.util.getBuildInfo
import react.FC
import react.Props
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p
import react.useState


val App = FC<Props>("App") {

    var a by useState<Int?>(null)
    var b by useState<Int?>(null)

    val adder = Adder()


    div {
        h1 { +"Hello from Kotlin/JS + React!" }

        KeyValueInput {
            id = "inputA"
            title = "A = "
            value = a?.toString()
            onValueChanged = {
                a = it.toIntOrNull()
            }
        }

        KeyValueInput {
            id = "inputB"
            title = "B = "
            value = b?.toString()
            onValueChanged = {
                b = it.toIntOrNull()
            }
        }

        if (a == null || b == null) {
            p { +"enter integer values for A and B" }
        } else {
            p {
                +"$a + $b = ${adder.add(a!!, b!!)}"
            }
        }

        hr()
        code { +getBuildInfo() }
    }

}
