package dev.tonycode.kmp.common.main.store

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.varabyte.truthish.assertThat
import dev.tonycode.kmp.common.main.store.MainStore.Intent
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class MainStoreUnitTest {

    private lateinit var store: MainStore


    @BeforeTest
    fun before() {
        isAssertOnMainThreadEnabled = false
    }

    @AfterTest
    fun after() {
        isAssertOnMainThreadEnabled = true
    }

    @Test
    fun updates_inputs_WHEN_Intent_SelectCommand() {
        createStore()

        store.accept(Intent.SelectCommand(command = "SET"))
        assertThat(store.state.selectedCommand).isEqualTo("SET")
        assertThat(store.state.hasKeyInput).isTrue()
        assertThat(store.state.key).isNull()
        assertThat(store.state.hasValueInput).isTrue()
        assertThat(store.state.value).isNull()
        assertThat(store.state.executionResult).isNull()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()
        assertThat(store.state.executionHistory).isEmpty()

        store.accept(Intent.SelectCommand(command = "GET"))
        assertThat(store.state.selectedCommand).isEqualTo("GET")
        assertThat(store.state.hasKeyInput).isTrue()
        assertThat(store.state.key).isNull()
        assertThat(store.state.hasValueInput).isFalse()
        assertThat(store.state.executionResult).isNull()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()
        assertThat(store.state.executionHistory).isEmpty()

        store.accept(Intent.SelectCommand(command = "BEGIN"))
        assertThat(store.state.selectedCommand).isEqualTo("BEGIN")
        assertThat(store.state.hasKeyInput).isFalse()
        assertThat(store.state.hasValueInput).isFalse()
        assertThat(store.state.executionResult).isNull()
        assertThat(store.state.isExecuteButtonEnabled).isTrue()
        assertThat(store.state.executionHistory).isEmpty()
    }

    @Test
    fun toggles_ExecuteButton_WHEN_Intent_SetArgument() {
        createStore()

        assertThat(store.state.isExecuteButtonEnabled).isFalse()

        store.accept(Intent.SelectCommand(command = "SET"))
        assertThat(store.state.selectedCommand).isEqualTo("SET")
        assertThat(store.state.key).isNull()
        assertThat(store.state.value).isNull()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()

        store.accept(Intent.SetKeyArgument(key = "var1"))
        assertThat(store.state.key).isEqualTo("var1")
        assertThat(store.state.value).isNull()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()

        store.accept(Intent.SetValueArgument(value = "value1"))
        assertThat(store.state.key).isEqualTo("var1")
        assertThat(store.state.value).isEqualTo("value1")
        assertThat(store.state.isExecuteButtonEnabled).isTrue()

        store.accept(Intent.SetKeyArgument(key = ""))
        assertThat(store.state.key).isEqualTo("")
        assertThat(store.state.value).isEqualTo("value1")
        assertThat(store.state.isExecuteButtonEnabled).isFalse()

        store.accept(Intent.SelectCommand(command = "GET"))
        assertThat(store.state.selectedCommand).isEqualTo("GET")
        assertThat(store.state.hasKeyInput).isTrue()
        assertThat(store.state.key).isNull()
        assertThat(store.state.hasValueInput).isFalse()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()

        store.accept(Intent.SetKeyArgument(key = "var1"))
        assertThat(store.state.key).isEqualTo("var1")
        assertThat(store.state.hasValueInput).isFalse()
        assertThat(store.state.isExecuteButtonEnabled).isTrue()

        store.accept(Intent.SetKeyArgument(key = ""))
        assertThat(store.state.key).isEqualTo("")
        assertThat(store.state.hasValueInput).isFalse()
        assertThat(store.state.isExecuteButtonEnabled).isFalse()
    }

    private fun createStore() {
        store = DefaultStoreFactory().mainStore(
            trkvs = TransactionalKeyValueStoreFactory.create(),
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        )
    }

}
