package dev.tonycode.kmp.lib

import kotlin.test.Test
import kotlin.test.assertEquals


class AdderUnitTest {

    private val adder = Adder()


    @Test
    fun test1() {
        assertEquals(5, adder.add(2, 3))
    }

    @Test
    fun test2() {
        assertEquals(0, adder.add(-5, 5))
    }

}
