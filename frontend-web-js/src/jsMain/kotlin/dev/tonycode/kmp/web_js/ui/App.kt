package dev.tonycode.kmp.web_js.ui

import antd.Button
import antd.ButtonType
import antd.Direction
import antd.Input
import antd.Option
import antd.Select
import antd.Size
import antd.Space
import antd.Typography
import antd.setAddonBefore
import antd.setDefaultValue
import dev.tonycode.kmp.common.KvStoreUiState
import dev.tonycode.kmp.web_js.util.getBuildInfo
import dev.tonycode.kvstore.TransactionalKeyValueStore
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.hr
import react.useState


val App = FC<Props>("App") {

    val trkvs by useState(TransactionalKeyValueStore())

    var cmd by useState("SET")
    var cmdArgs by useState("key value")

    var uiState by useState(KvStoreUiState())


    Space {
        direction = Direction.vertical
        size = Size.large

        Typography.Title {
            level = 5
            +"Hello from Kotlin/JS + React!"
        }
        Typography.Text {
            code = true
            +"(cmd = $cmd ; cmdArgs = $cmdArgs)"
        }

        Input {
            setAddonBefore(Select.create {
                options = arrayOf(
                    Option("SET"),
                    Option("GET"),
                    Option("DELETE"),
                    Option("COUNT"),
                    Option("BEGIN"),
                    Option("COMMIT"),
                    Option("ROLLBACK"),
                )
                setDefaultValue(cmd)
                onChange = {
                    cmd = it
                    cmdArgs = when (it) {  // not properly updates UI
                        "SET" -> "key value"
                        "GET", "DELETE" -> "key"
                        "COUNT" -> "value"
                        else -> ""
                    }
                }
            })
            value = cmdArgs

            onChange = {
                cmdArgs = it.target.value
            }
        }

        Button {
            type = ButtonType.primary
            +"Execute"

            onClick = {
                val s = "$cmd $cmdArgs"
                //console.log("s = $s")  //DEBUG

                uiState = uiState.mutate(s, trkvs)
            }
        }

        uiState.executionResult?.let {
            Typography.Text {
                code = true
                +it
            }
        }


        hr()
        code { +getBuildInfo() }
    }

}
