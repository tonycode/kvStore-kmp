package dev.tonycode.kmp.common.utils

import com.arkivanov.mvikotlin.core.view.BaseMviView


open class ViewProxy<in Model : Any, Event : Any>(
    private val render: (Model) -> Unit,
) : BaseMviView<Model, Event>() {

    override fun render(model: Model) {
        super.render(model)
        render.invoke(model)
    }

}
