package dev.tonycode.kmp.web_js.ui.components

import antd.Direction
import antd.Size
import antd.Space
import antd.Typography
import antd.TypographyType
import react.FC
import react.Props


external interface ExecutionHistoryProps : Props {
    var items: List<String>
}

val ExecutionHistory = FC<ExecutionHistoryProps>("ExecutionHistory") { props ->

    Space {
        direction = Direction.vertical
        size = Size.middle

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
            props.items.forEachIndexed { index, item ->
                Space {
                    direction = Direction.horizontal
                    size = Size.small

                    Typography.Text { +"${index + 1}." }

                    Typography.Text {
                        code = true
                        +item
                    }
                }
            }
        }
    }

}
