package dev.tonycode.kmp.common.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import dev.tonycode.kmp.common.main.store.MainStore.Intent
import dev.tonycode.kmp.common.main.store.MainStore.State
import dev.tonycode.kvstore.TransactionalKeyValueStore


interface MainStore : Store<Intent, State, Nothing> {

    sealed interface Intent : JvmSerializable {
        data class SelectCommand(val command: String) : Intent
        data class SetKeyArgument(val key: String) : Intent
        data class SetValueArgument(val value: String) : Intent
        data object ExecuteCommand : Intent
    }

    data class State(
        /** A list of all commands available for current user */
        val commands: List<String>,
        /** Current command that user have selected */
        val selectedCommand: String?,
        val hasKeyInput: Boolean,
        val key: String?,
        val hasValueInput: Boolean,
        val value: String?,
        val executionResult: String?,
        val isExecutionSuccessful: Boolean,
        val executionHistory: List<HistoryItem>,
    ) : JvmSerializable {
        constructor() : this(  // no-arg constructor is required for js & ios targets
            commands = TransactionalKeyValueStore.commands.map { it.command },
            selectedCommand = null,
            hasKeyInput = false,
            key = null,
            hasValueInput = false,
            value = null,
            executionResult = null,
            isExecutionSuccessful = false,
            executionHistory = emptyList(),
        )

        val isExecuteButtonEnabled: Boolean
            get() = (selectedCommand != null) &&
                (!hasKeyInput || !key.isNullOrBlank()) &&
                (!hasValueInput || !value.isNullOrBlank())

        data class HistoryItem(val commandString: String, val output: String?, val succeed: Boolean = true)
    }

}
