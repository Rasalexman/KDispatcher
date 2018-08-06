package com.mincor.kdispatcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
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
        // set event listeners for call
        scopeOperation(test)
        // call event with data
        call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        call(EVENT_CALL_TWO, test)

        unsubscribe(EVENT_CALL_ONE, eventListenerOne)
        unsubscribe(EVENT_CALL_TWO, eventListenerThree)

        call(EVENT_CALL_ONE, "SECOND CALL FROM KDISPATCHER")
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
        subscribe<Any>(eventName) {
            println("LAMBDA_EVENT HAS FIRED with event name ${it.data}")
            unsubscribe(it.eventName)
        }

        call(eventName, "FIRST CALL CUSTOM LAMBDA EVENT")
        // there is no event calling will be fired
        call(eventName, "SECOND HELLO FROM EVENT")

        subscribeList<Any>(listOf("notif_one", "notif_two")) {
            when(it.eventName) {
                "notif_one" -> Toast.makeText(this, "This is notif_one", Toast.LENGTH_SHORT).show()
                "notif_two" -> Toast.makeText(this, "This is notif_two", Toast.LENGTH_SHORT).show()
            }
        }

        call("notif_one")
        call("notif_two")
    }


    ////------- EVENT HANDLERS ------////
    fun eventOneHandler(notification:Notification<Any>) {
       println("eventOneHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
    }

    fun eventTwoHandler(notification:Notification<Any>) {
        println("eventTwoHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
    }

    fun eventFourHandler(notification:Notification<Any>) {
        println("eventFourHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
    }

    fun eventThreeHandler(notification:Notification<Any>) {
        println("eventThreeHandler INVOKED With EVENT ${notification.data} AND data = ${notification.eventName}")
    }

    inner class MyTest {

        fun eventFromObjectHandler(notification: Notification<Any>) {
            println("MyTest::eventFromObjectHandler FROM MyTest CLASS ${notification.data} AND data = ${notification.eventName}")
        }
    }
}


