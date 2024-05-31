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

    var commandKey: String by useState(TransactionalKeyValueStore.commands.first().first)
    var commandArgs: String? by useState(TransactionalKeyValueStore.commands.first().second)

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
            +"(commandKey = $commandKey ; commandArgs = $commandArgs)"
        }

        Input {
            setAddonBefore(Select.create {
                options = TransactionalKeyValueStore.commands.map { Option(it.first) }.toTypedArray()
                setDefaultValue(commandKey)
                onChange = { newCmd ->
                    commandKey = newCmd
                    commandArgs = TransactionalKeyValueStore.commands.firstOrNull { it.first == newCmd }?.second
                }
            })
            value = commandArgs ?: ""

            onChange = {
                commandArgs = it.target.value
            }
        }

        Button {
            type = ButtonType.primary
            +"Execute"

            onClick = {
                val commandString = "$commandKey $commandArgs"
                //console.log("commandString = $commandString")  //DEBUG

                uiState = uiState.mutate(commandString, trkvs)
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
