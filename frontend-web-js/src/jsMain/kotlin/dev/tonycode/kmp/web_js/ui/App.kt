package dev.tonycode.kmp.web_js.ui

import antd.Button
import antd.ButtonType
import antd.Direction
import antd.Input
import antd.Radio
import antd.RadioOptionType
import antd.Size
import antd.Space
import antd.Typography
import antd.TypographyType
import antd.setOptions
import antd.setValue
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.tonycode.kmp.common.IDispatchers
import dev.tonycode.kmp.common.main.MainController
import dev.tonycode.kmp.common.main.MainView
import dev.tonycode.kmp.common.main.store.MainStore.Intent
import dev.tonycode.kmp.common.main.store.MainStore.State
import dev.tonycode.kmp.common.utils.ViewProxy
import dev.tonycode.kmp.web_js.ui.components.ExecutionHistory
import dev.tonycode.kmp.web_js.util.getBuildInfo
import dev.tonycode.kmp.web_js.util.useInstanceKeeper
import dev.tonycode.kmp.web_js.util.useLifecycle
import dev.tonycode.kvstore.TransactionalKeyValueStore
import react.FC
import react.Props
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.hr
import react.useEffectOnce
import react.useMemo
import react.useState


external interface AppProps : Props {
    var storeFactory: StoreFactory
    var trkvs: TransactionalKeyValueStore
    var dispatchers: IDispatchers
}

val App = FC<AppProps>("App") { props ->
    val lifecycle = useLifecycle()
    val instanceKeeper = useInstanceKeeper()

    val controller = useMemo {
        MainController(
            storeFactory = props.storeFactory,
            trkvs = props.trkvs,
            instanceKeeper = instanceKeeper,
            dispatchers = props.dispatchers,
        )
    }

    var model by useState(::State)
    val view = useMemo {
        object : ViewProxy<State, Intent>(
            render = { model = it }
        ), MainView {}
    }

    useEffectOnce {
        controller.onViewCreated(view, lifecycle)
    }


    Space {
        direction = Direction.vertical
        size = Size.large

        Typography.Title {
            level = 3
            +"Hello from Kotlin/JS + React!"
        }

        Radio.Group {
            setOptions(model.commands)
            model.selectedCommand?.let { setValue(it) }
            optionType = RadioOptionType.button
            onChange = {
                view.dispatch(Intent.SelectCommand(command = it.target.value))
            }
        }

        if (model.hasKeyInput) {
            Space {
                direction = Direction.horizontal
                size = Size.middle

                Typography.Text { +"key = " }
                Input {
                    value = model.key ?: ""
                    onChange = {
                        view.dispatch(Intent.SetKeyArgument(key = it.target.value))
                    }
                }
            }
        }

        if (model.hasValueInput) {
            Space {
                direction = Direction.horizontal
                size = Size.small

                Typography.Text { +"value = " }
                Input {
                    value = model.value ?: ""
                    onChange = {
                        view.dispatch(Intent.SetValueArgument(value = it.target.value))
                    }
                }
            }
        }

        Button {
            +"Execute"
            type = ButtonType.primary
            disabled = !model.isExecuteButtonEnabled

            onClick = {
                view.dispatch(Intent.ExecuteCommand)
            }
        }

        model.executionResult?.let {
            Typography.Text {
                code = true
                type = if (model.isExecutionSuccessful) TypographyType.success else TypographyType.warning

                +it
            }
        }

        hr()

        ExecutionHistory {
            items = model.executionHistory
        }

        hr()
        code { +getBuildInfo() }
    }

}
