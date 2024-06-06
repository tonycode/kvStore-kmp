package dev.tonycode.kmp.web_js.ui.components

import antd.Direction
import antd.Size
import antd.Space
import antd.Typography
import antd.TypographyType
import dev.tonycode.kmp.common.main.store.MainStore
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.code
import web.cssom.Color


external interface ExecutionHistoryProps : Props {
    var items: List<MainStore.State.HistoryItem>
}

val ExecutionHistory = FC<ExecutionHistoryProps>("ExecutionHistory") { props ->

    Space {
        direction = Direction.vertical
        size = Size.small

        Typography.Title {
            level = 5
            +"History"
        }

        if (props.items.isEmpty()) {
            Typography.Text {
                type = TypographyType.secondary
                +"empty"
            }

        } else {
            props.items.forEach { item ->
                code {
                    css {
                        if (!item.succeed) color = Color("red")
                    }
                    +"> ${item.commandString}"
                }

                item.output?.let { output ->
                    code {
                        css {
                            if (!item.succeed) color = Color("red")
                        }
                        +output
                    }
                }
            }
        }
    }

}
