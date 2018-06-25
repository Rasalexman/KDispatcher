package com.mincor.kdispatcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rasalexman.kdispatcher.*

class MainActivity : AppCompatActivity(), IKDispatcher {

    private val EVENT_CALL_ONE: String = "EVENT_CALL_ONE"
    private val EVENT_CALL_TWO: String = "EVENT_CALL_TWO"

    private val eventListenerOne = this::eventOneHandler
    private val eventListenerTwo = this::eventTwoHandler
    private val eventListenerThree = this::eventThreeHandler
    private val eventListenerFour = this::eventFourHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = MyTest()

        scopeOperation(test)

        call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        call(EVENT_CALL_TWO, test)

        unsubscribe(EVENT_CALL_ONE, eventListenerOne)
        unsubscribe(EVENT_CALL_TWO, eventListenerThree)

        call(EVENT_CALL_ONE, "SECONT CALL FROM KDISPATCHER")
        call(EVENT_CALL_TWO, test)

        unsubscribeAll(EVENT_CALL_ONE)
        call(EVENT_CALL_ONE, "THIRD CALL FROM KDISPATCHER")
    }

    private fun scopeOperation(test:MyTest) {
        subscribe(EVENT_CALL_ONE, 3, eventListenerOne)
        subscribe(EVENT_CALL_ONE, 1, eventListenerTwo)
        subscribe(EVENT_CALL_ONE, 2, eventListenerFour)

        subscribe(EVENT_CALL_TWO, 2, eventListenerThree)
        subscribe(EVENT_CALL_TWO, 1, test::eventFromObjectHandler)

        /**
         * But you can simple use inner lambda function to handler notification.
         * So as u hasn't a reference to ISubscriber handler function, when you call
         * `usubscribe(String)` you will delete all references ISubscriber-listener
         */
        val eventName = "LAMBDA_EVENT"
        subscribe<String>(eventName) { data, event->
            println("LAMBDA_EVENT HAS FIRED with event name $data")
            unsubscribe(event)
        }

        call(eventName, "FIRST CALL CUSTOM LABDA EVENT")
        // there is no event calling will be fired
        call(eventName, "SECOND HELLO FROM EVENT")
    }


    ////------- EVENT HANDLERS ------////
    fun eventOneHandler(data: String, str: String? = null) {
       println("eventOneHandler MY TEST IS COMING event = $str AND data = $data")
    }

    fun eventTwoHandler(data: String, str: String? = null) {
        println("eventTwoHandler MY TEST IS COMING event = $str AND data = $data")
    }

    fun eventFourHandler(data: String, str: String? = null) {
        println("eventFourHandler MY TEST IS COMING event = $str AND data = $data")
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


