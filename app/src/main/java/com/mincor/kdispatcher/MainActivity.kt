package com.mincor.kdispatcher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rasalexman.kdispatcher.KDispatcher

class MainActivity : AppCompatActivity() {

    private val EVENT_CALL_ONE: String = "EVENT_CALL_ONE"
    private val EVENT_CALL_TWO: String = "EVENT_CALL_TWO"

    private val eventListenerOne = this::eventOneHandler
    private val eventListenerTwo = this::eventTwoHandler
    private val eventListenerThree = this::eventThreeHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = MyTest()

        scopeOperation(test)

        KDispatcher.call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        KDispatcher.call(EVENT_CALL_TWO, test)

        KDispatcher.unsubscribe(EVENT_CALL_ONE, eventListenerOne)
        KDispatcher.unsubscribe(EVENT_CALL_TWO, eventListenerThree)

        KDispatcher.call(EVENT_CALL_ONE, "SECONT CALL FROM KDISPATCHER")
        KDispatcher.call(EVENT_CALL_TWO, test)

        KDispatcher.unsubscribeAll(EVENT_CALL_ONE)
        KDispatcher.call(EVENT_CALL_ONE, "THIRD CALL FROM KDISPATCHER")
    }

    private fun scopeOperation(test:MyTest){
        KDispatcher.subscribe<String>(EVENT_CALL_ONE, eventListenerOne, 1)
        KDispatcher.subscribe(EVENT_CALL_ONE, eventListenerTwo, 2)

        KDispatcher.subscribe<MyTest>(EVENT_CALL_TWO, eventListenerThree, 1)
        KDispatcher.subscribe(EVENT_CALL_TWO, test::eventFromObjectHandler, 2)
    }


    ////------- EVENT HANDLERS ------////
    fun eventOneHandler(data: String, str: String? = null) {
       println("eventOneHandler MY TEST IS COMING event = $str AND data = $data")
    }

    fun eventTwoHandler(data: String, str: String? = null) {
        println("eventTwoHandler MY TEST IS COMING event = $str AND data = $data")
    }

    fun eventThreeHandler(data: MyTest, str: String? = null) {
        println("eventThreeHandler INVOKED With EVENT $str data is MyTest = $data")
    }

    inner class MyTest {

        fun eventFromObjectHandler(data: Any?, str: String? = null) {
            println("MyTest::eventFromObjectHandler FROM MyTest CLASS $data")
        }
    }
}


