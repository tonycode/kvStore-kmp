package dev.tonycode.kvstore

import com.varabyte.truthish.assertThat
import kotlin.test.Test


class TransactionalKeyValueStoreUnitTest {

    private val trkvs = TransactionalKeyValueStore()


    @Test
    fun set_and_get_value() {
        assertThat(trkvs.onOperation(Operation.Set("foo", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("123"))
    }

    @Test
    fun delete_a_value() {
        assertThat(trkvs.onOperation(Operation.Delete("foo")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.Error(MSG_KEY_NOT_SET))
    }

    @Test
    fun get_unknown_key_and_update_existing_key() {
        assertThat(trkvs.onOperation(Operation.Set("foo", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("foo", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("456"))
    }

    @Test
    fun count_the_number_of_occurrences_of_a_value() {
        assertThat(trkvs.onOperation(Operation.Set("foo", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("bar", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("baz", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Count("123")))
            .isEqualTo(OperationResult.SuccessWithIntResult(2))

        assertThat(trkvs.onOperation(Operation.Count("456")))
            .isEqualTo(OperationResult.SuccessWithIntResult(1))
    }

    @Test
    fun commit_a_transaction() {
        assertThat(trkvs.onOperation(Operation.Set("bar", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.SuccessWithResult("123"))

        assertThat(trkvs.onOperation(Operation.Begin))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("foo", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.SuccessWithResult("123"))

        assertThat(trkvs.onOperation(Operation.Delete("bar")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Commit))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onOperation(Operation.Rollback))
            .isEqualTo(OperationResult.Error(MSG_NO_TRANSACTION))

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("456"))
    }

    @Test
    fun rollback_a_transaction() {
        assertThat(trkvs.onOperation(Operation.Set("foo", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("bar", "abc")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Begin))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("foo", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("456"))

        assertThat(trkvs.onOperation(Operation.Set("bar", "def")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.SuccessWithResult("def"))

        assertThat(trkvs.onOperation(Operation.Rollback))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("123"))

        assertThat(trkvs.onOperation(Operation.Get("bar")))
            .isEqualTo(OperationResult.SuccessWithResult("abc"))

        assertThat(trkvs.onOperation(Operation.Commit))
            .isEqualTo(OperationResult.Error(MSG_NO_TRANSACTION))
    }

    @Test
    fun nested_transactions() {
        assertThat(trkvs.onOperation(Operation.Set("foo", "123")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("bar", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Begin))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Set("foo", "456")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Begin))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Count("456")))
            .isEqualTo(OperationResult.SuccessWithIntResult(2))

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("456"))

        assertThat(trkvs.onOperation(Operation.Set("foo", "789")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("789"))

        assertThat(trkvs.onOperation(Operation.Rollback))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("456"))

        assertThat(trkvs.onOperation(Operation.Delete("foo")))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.Error(MSG_KEY_NOT_SET))

        assertThat(trkvs.onOperation(Operation.Rollback))
            .isEqualTo(OperationResult.Success)

        assertThat(trkvs.onOperation(Operation.Get("foo")))
            .isEqualTo(OperationResult.SuccessWithResult("123"))
    }

}
