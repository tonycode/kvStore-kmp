package dev.tonycode.kmp.web_js.ui.components

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import web.html.InputType


external interface KeyValueInputProps : Props {
    var id: String
    var title: String
    var placeholder: String?
    var value: String?
    var onValueChanged: ((newValue: String) -> Unit)?
}


val KeyValueInput = FC<KeyValueInputProps>("KeyValueInput") { props ->
    div {
        label {
            htmlFor = props.id
            +props.title
        }

        input {
            id = props.id
            type = InputType.text
            props.placeholder?.let { placeholder = it }
            props.value?.let { defaultValue = it }
            props.onValueChanged?.let { onChange = { event ->
                it(event.target.value)
            } }
        }
    }
}
