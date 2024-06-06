package dev.tonycode.kmp.common.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import dev.tonycode.kmp.common.main.store.MainStore.Intent
import dev.tonycode.kmp.common.main.store.MainStore.State
import dev.tonycode.kvstore.TransactionalKeyValueStore
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.CommandDescription
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


internal fun StoreFactory.mainStore(
    trkvs: TransactionalKeyValueStore,
    mainContext: CoroutineContext,
    ioContext: CoroutineContext,
): MainStore = object : MainStore, Store<Intent, State, Nothing> by create(
    name = "MainStore",
    initialState = State(),

    executorFactory = coroutineExecutorFactory(mainContext) {
        onIntent<Intent.SelectCommand> { intent ->
            if (state().selectedCommand == intent.command) return@onIntent

            val commandDescription =
                TransactionalKeyValueStore.commands.first { it.command == intent.command }

            dispatch(Msg.CommandSelected(commandDescription))
        }

        onIntent<Intent.SetKeyArgument> {
            dispatch(Msg.KeyArgumentChanged(it.key))
        }

        onIntent<Intent.SetValueArgument> {
            dispatch(Msg.ValueArgumentChanged(it.value))
        }

        onIntent<Intent.ExecuteCommand> {
            val state = state()
            if (state.selectedCommand == null) throw IllegalStateException()

            val commandString = StringBuilder(state.selectedCommand).apply {
                if (state.hasKeyInput) append(" ").append(state.key)
                if (state.hasValueInput) append(" ").append(state.value)
            }.toString()

            launch {
                val executionResult = withContext(ioContext) {
                    trkvs.onCommand(Command.fromString(commandString))
                }
                dispatch(Msg.CommandExecuted(commandString, executionResult))
            }
        }
    },

    reducer = { msg: Msg -> when (msg) {
        is Msg.CommandSelected -> {
            val cd = msg.commandDescription
            copy(
                selectedCommand = cd.command,
                hasKeyInput = cd.hasKeyArgument,
                key = null,
                hasValueInput = cd.hasValueArgument,
                value = null,
                executionResult = null,
            )
        }

        is Msg.KeyArgumentChanged -> copy(key = msg.key)

        is Msg.ValueArgumentChanged -> copy(value = msg.value)

        is Msg.CommandExecuted -> {
            val executionResultString = when (msg.executionResult) {
                is ExecutionResult.Success -> null
                is ExecutionResult.SuccessWithResult -> msg.executionResult.result
                is ExecutionResult.SuccessWithIntResult -> msg.executionResult.result.toString()
                is ExecutionResult.Error -> msg.executionResult.errorMessage
            }
            val successful = (msg.executionResult !is ExecutionResult.Error)

            copy(
                executionResult = executionResultString ?: "OK",
                isExecutionSuccessful = successful,
                executionHistory = executionHistory +
                    State.HistoryItem(
                        commandString = msg.commandString,
                        output = executionResultString,
                        succeed = successful
                    )
            )
        }
    } }

) {}


private sealed interface Msg : JvmSerializable {
    data class CommandSelected(val commandDescription: CommandDescription) : Msg
    data class KeyArgumentChanged(val key: String) : Msg
    data class ValueArgumentChanged(val value: String) : Msg
    data class CommandExecuted(val commandString: String, val executionResult: ExecutionResult) : Msg
}
