package dev.tonycode.kvstore

import com.varabyte.truthish.assertThat
import dev.tonycode.kvstore.TransactionalKeyValueStore.Command
import dev.tonycode.kvstore.TransactionalKeyValueStore.ExecutionResult
import dev.tonycode.kvstore.coroutines.ConcurrentTransactionalKeyValueStore
import dev.tonycode.kvstore.coroutines.ConcurrentTransactionalKeyValueStoreImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import kotlin.test.Test


class ConcurrentTransactionalKeyValueStoreJvmUnitTest {

    @Test
    fun setAndGetValuesConcurrently() {
        val store: ConcurrentTransactionalKeyValueStore = ConcurrentTransactionalKeyValueStoreImpl()

        testSetAndGetConcurrently(store, 2, 2, 5)
    }

    @Test
    fun setAndGetValuesConcurrentlyMassive() {
        val store: ConcurrentTransactionalKeyValueStore = ConcurrentTransactionalKeyValueStoreImpl()

        testSetAndGetConcurrently(store, 10, 1000, 50)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun testSetAndGetConcurrently(
        store: ConcurrentTransactionalKeyValueStore,
        numThreads: Int,
        numCoroutines: Int,
        numEmissions: Int
    ) = runBlocking {
        val dispatcher = newFixedThreadPoolContext(numThreads, "MyThreadPool")
        val keys = (1 .. numCoroutines).map { "key-$it" }
        val values = (1 .. numCoroutines).map { "value-$it" }

        val jobs = mutableListOf<Job>()
        repeat(numCoroutines) { coroutineId ->
            jobs += launch(dispatcher) {
                //println("Coroutine $coroutineId is running on thread ${ Thread.currentThread().name }")  //DEBUG

                repeat(numEmissions) { emissionId ->
                    store.onCommand(Command.Set(keys[coroutineId] + emissionId, values[coroutineId] + emissionId))
                }

                repeat(numEmissions) { emissionId ->
                    val retrievedValue = store.onCommand(Command.Get(keys[coroutineId]+emissionId))

                    assertThat(retrievedValue)
                        .isInstanceOf<ExecutionResult.SuccessWithResult>()
                    assertThat((retrievedValue as ExecutionResult.SuccessWithResult).result)
                        .isEqualTo(values[coroutineId]+emissionId)
                }
            }
        }

        jobs.joinAll()
    }

}
