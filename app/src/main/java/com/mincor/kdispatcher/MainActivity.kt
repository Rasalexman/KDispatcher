package com.mincor.kdispatcher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rasalexman.kdispatcher.KDispatcher

class MainActivity : AppCompatActivity() {

    private val EVENT_CALL_ONE: String = "CALL_NUMBER_TWO"
    private val callbackOne = this::nextFun
    private val callbackTwo = this::testFun

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = MyTest()

        scopeOperation(test)

        KDispatcher.call("TEST_CALL", "String")
        KDispatcher.call(EVENT_CALL_ONE, test)

        KDispatcher.unsubscribe(EVENT_CALL_ONE, callbackOne)
        KDispatcher.call(EVENT_CALL_ONE, test)
        KDispatcher.unsubscribeAll(EVENT_CALL_ONE)
        KDispatcher.call(EVENT_CALL_ONE, test)
    }

    private fun scopeOperation(test:MyTest){
        KDispatcher.subscribe(EVENT_CALL_ONE, callbackOne, 2)
        KDispatcher.subscribe(EVENT_CALL_ONE, callbackTwo, 1)
        KDispatcher.subscribe(EVENT_CALL_ONE, test::testOne, 3)
    }

    fun testFun(data: Any?, str: String? = null) {
        println("testFun INVOKED With EVENT $str")
    }

    fun nextFun(data: Any?, str: String? = null) {
        when (data) {
            is MyTest -> println("nextFun MY TEST IS COMING $str ${data.testOne(data, str)}")
        }
    }

    fun testOne(data: Any?, str: String? = null): Unit {
        println("testOne FROM MY CLASS $data")
    }

    fun testTwo(e: Any?) {
        println("testTwo FROM MY CLASS $e")
    }

    inner class MyTest {
        init {

        }

        fun testOne(data: Any?, str: String? = null): Unit {
            println("testOne FROM MyTest CLASS $data")
        }

        fun testTwo(e: Any?) {
            println("testTwo FROM MY CLASS $e")
        }
    }
}


