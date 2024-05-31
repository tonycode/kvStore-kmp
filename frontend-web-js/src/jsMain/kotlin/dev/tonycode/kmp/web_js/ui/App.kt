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
import dev.tonycode.kmp.web_js.util.getBuildInfo
import dev.tonycode.kvstore.Operation
import dev.tonycode.kvstore.OperationResult
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

    var executionResult by useState<String?>(null)


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

                var op: Operation? = null
                try {
                    op = Operation.fromString(s)
                } catch (iae: IllegalArgumentException) {
                    executionResult = (iae.message ?: "unknown error")
                }
                //console.log("op = $op")  //DEBUG

                op?.let {
                    executionResult = when (
                        val opResult = trkvs.onOperation(op)
                    ) {
                        is OperationResult.Success -> "OK"

                        is OperationResult.SuccessWithResult -> opResult.result

                        is OperationResult.SuccessWithIntResult -> opResult.result.toString()

                        is OperationResult.Error -> opResult.errorMessage
                    }
                }

            }
        }

        if (executionResult != null) {
            Typography.Text {
                code = true

                +executionResult
            }
        }


        hr()
        code { +getBuildInfo() }
    }

}
