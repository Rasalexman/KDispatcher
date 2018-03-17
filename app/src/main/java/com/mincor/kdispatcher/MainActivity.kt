package com.mincor.kdispatcher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ru.fortgroup.dpru.utils.KDispatcher

class MainActivity : AppCompatActivity() {

    val EVENT_CALL_ONE: String = "CALL_NUMBER_TWO"
    val callbackOne = this::nextFun
    val callbackTwo = this::testFun

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = MyTest()

        KDispatcher.subscribe(EVENT_CALL_ONE, callbackOne, 2)
        KDispatcher.subscribe(EVENT_CALL_ONE, callbackTwo, 1)

        KDispatcher.call("TEST_CALL", "String")
        KDispatcher.call(EVENT_CALL_ONE, test)

        KDispatcher.unsubscribe(EVENT_CALL_ONE, callbackOne)
        KDispatcher.call(EVENT_CALL_ONE, test)
    }

    fun testFun(data: Any?, str: String? = null) {
        println("testFun INVOKED With EVENT $str")
    }

    fun nextFun(data: Any?, str: String? = null) {
        when (data) {
            is MyTest -> println("MYTEST SI COMING $str")
        }
    }

    fun testOne(e: Any?): Unit {
        println("testOne FROM MY CLASS $e")
    }

    fun testTwo(e: Any?) {
        println("testTwo FROM MY CLASS $e")
    }

    inner class MyTest {
        init {

        }

        fun testOne(e: Any?): Unit {
            println("testOne FROM MY CLASS $e")
        }

        fun testTwo(e: Any?) {
            println("testTwo FROM MY CLASS $e")
        }
    }
}


