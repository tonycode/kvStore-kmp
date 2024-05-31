package dev.tonycode.kvstore

import com.varabyte.truthish.assertThat
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import kotlin.test.Test


class TransactionalKeyValueStoreUnitTest {

    private val trkvs = TransactionalKeyValueStore()


    @Test
    fun set_and_get_value() {
        assertThat(trkvs.onCommand(Command.Set("foo", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("123"))
    }

    @Test
    fun delete_a_value() {
        assertThat(trkvs.onCommand(Command.Delete("foo")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.Error(MSG_KEY_NOT_SET))
    }

    @Test
    fun get_unknown_key_and_update_existing_key() {
        assertThat(trkvs.onCommand(Command.Set("foo", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("foo", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("456"))
    }

    @Test
    fun count_the_number_of_occurrences_of_a_value() {
        assertThat(trkvs.onCommand(Command.Set("foo", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("bar", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("baz", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Count("123")))
            .isEqualTo(ExecutionResult.SuccessWithIntResult(2))

        assertThat(trkvs.onCommand(Command.Count("456")))
            .isEqualTo(ExecutionResult.SuccessWithIntResult(1))
    }

    @Test
    fun commit_a_transaction() {
        assertThat(trkvs.onCommand(Command.Set("bar", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.SuccessWithResult("123"))

        assertThat(trkvs.onCommand(Command.Begin))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("foo", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.SuccessWithResult("123"))

        assertThat(trkvs.onCommand(Command.Delete("bar")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Commit))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onCommand(Command.Rollback))
            .isEqualTo(ExecutionResult.Error(MSG_NO_TRANSACTION))

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("456"))
    }

    @Test
    fun rollback_a_transaction() {
        assertThat(trkvs.onCommand(Command.Set("foo", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("bar", "abc")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Begin))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("foo", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("456"))

        assertThat(trkvs.onCommand(Command.Set("bar", "def")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.SuccessWithResult("def"))

        assertThat(trkvs.onCommand(Command.Rollback))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("123"))

        assertThat(trkvs.onCommand(Command.Get("bar")))
            .isEqualTo(ExecutionResult.SuccessWithResult("abc"))

        assertThat(trkvs.onCommand(Command.Commit))
            .isEqualTo(ExecutionResult.Error(MSG_NO_TRANSACTION))
    }

    @Test
    fun nested_transactions() {
        assertThat(trkvs.onCommand(Command.Set("foo", "123")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("bar", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Begin))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Set("foo", "456")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Begin))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Count("456")))
            .isEqualTo(ExecutionResult.SuccessWithIntResult(2))

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("456"))

        assertThat(trkvs.onCommand(Command.Set("foo", "789")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("789"))

        assertThat(trkvs.onCommand(Command.Rollback))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("456"))

        assertThat(trkvs.onCommand(Command.Delete("foo")))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onCommand(Command.Rollback))
            .isEqualTo(ExecutionResult.Success)

        assertThat(trkvs.onCommand(Command.Get("foo")))
            .isEqualTo(ExecutionResult.SuccessWithResult("123"))
    }

}
