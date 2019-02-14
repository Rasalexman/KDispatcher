package com.mincor.kdispatcher

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.rasalexman.kdispatcher.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity(), IKDispatcher {

    private val EVENT_CALL_ONE: String = "EVENT_CALL_ONE"
    private val EVENT_CALL_TWO: String = "EVENT_CALL_TWO"
    private val EVENT_CALL_THREE = "LAMBDA_EVENT"

    private val eventListenerOne = this::eventOneHandler
    private val eventListenerTwo = this::eventTwoHandler
    private val eventListenerThree = this::eventThreeHandler
    private val eventListenerFour = this::eventFourHandler

    private val eventNameTV by lazy {
        find<TextView>(textViewFieldId)
    }

    var textViewFieldId = 101
    var buttonOneId = 102
    var buttonTwoId = 103
    var buttonThreeId = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        relativeLayout {
            lparams(matchParent, matchParent)
                textView("HELLO KDISPATCHER") {
                id = textViewFieldId
                textSize = 20f
                textColor = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER_HORIZONTAL
                background = roundedBg(Color.WHITE, 24f, true)
            }.lparams(matchParent) {
                margin = dip(24)
            }

            button("CALL EVENT ONE") {
                id = buttonOneId
                background = roundedBg(Color.LTGRAY)
                textColor = Color.WHITE
                gravity = Gravity.CENTER_HORIZONTAL

                onClick {
                    call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
                }
            }.lparams {
                below(textViewFieldId)
                setPadding(dip(16), 0, dip(16), 0)
                centerHorizontally()
                topMargin = dip(16)
            }

            button("CALL EVENT TWO") {
                id = buttonTwoId
                background = roundedBg(Color.LTGRAY)
                textColor = Color.WHITE
                gravity = Gravity.CENTER_HORIZONTAL

                onClick {
                    call(EVENT_CALL_TWO, "SECOND CALL FROM KDISPATCHER")
                }
            }.lparams {
                below(buttonOneId)
                setPadding(dip(16), 0, dip(16), 0)
                centerHorizontally()
                topMargin = dip(16)
            }

            button("CALL EVENT THREE") {
                id = buttonThreeId
                background = roundedBg(Color.LTGRAY)
                textColor = Color.WHITE
                gravity = Gravity.CENTER_HORIZONTAL

                onClick {
                    call(EVENT_CALL_THREE, "THIRD CALL FROM KDISPATCHER")
                }
            }.lparams {
                below(buttonTwoId)
                setPadding(dip(16), 0, dip(16), 0)
                centerHorizontally()
                topMargin = dip(16)
            }
        }

        val test = MyTest()
        // set event listeners for call
        scopeOperation(test)
        // call event with data
        //call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        //call(EVENT_CALL_TWO, test)

        //unsubscribe(EVENT_CALL_ONE, eventListenerOne)
        //unsubscribe(EVENT_CALL_TWO, eventListenerThree)

       // call(EVENT_CALL_ONE, "SECOND CALL FROM KDISPATCHER")
        //call(EVENT_CALL_TWO, test)

        //unsubscribeAll(EVENT_CALL_ONE)
        //call(EVENT_CALL_ONE, "THIRD CALL FROM KDISPATCHER")
    }

    private fun scopeOperation(test:MyTest) {
        subscribe<Any>(EVENT_CALL_ONE, eventListenerOne, 3).
        subscribe(EVENT_CALL_ONE, eventListenerTwo, 2).
        subscribe(EVENT_CALL_ONE, eventListenerFour, 1)

        subscribe(EVENT_CALL_TWO, eventListenerThree, 2)
        subscribe(EVENT_CALL_TWO, test::eventFromObjectHandler, 1)

        /**
         * But you can simple use inner lambda function to handler notification.
         * So as u hasn't a reference to ISubscriber handler function, when you call
         * `usubscribe(String)` you will delete all references ISubscriber-listener
         */

        subscribe<Any>(EVENT_CALL_THREE) {
            println("LAMBDA_EVENT HAS FIRED with event name ${it.data}")
            //unsubscribe(it.eventName)
            eventNameTV.text = "${it.eventName} DATA = ${it.data}"
        }

       // call(EVENT_CALL_THREE, "FIRST CALL CUSTOM LAMBDA EVENT")
        // there is no event calling will be fired
       /// call(EVENT_CALL_THREE, "SECOND HELLO FROM EVENT")

        /**
         * Since version
         */
        subscribeList<Any>(listOf("notif_one", "notif_two")) {
            when(it.eventName) {
                "notif_one" -> this.toast("This is notif one")
                "notif_two" -> this.toast("This is notif two")
            }
        }

        call("notif_one")
        call("notif_two")
    }


    ////------- EVENT HANDLERS ------////
    fun eventOneHandler(notification:Notification<Any>) {
       println("eventOneHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
        eventNameTV.text = "${notification.eventName} DATA = ${notification.data}"
    }

    fun eventTwoHandler(notification:Notification<Any>) {
        println("eventTwoHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
    }

    fun eventFourHandler(notification:Notification<Any>) {
        println("eventFourHandler MY TEST IS COMING event = ${notification.data} AND data = ${notification.eventName}")
    }

    fun eventThreeHandler(notification:Notification<Any>) {
        println("eventThreeHandler INVOKED With EVENT ${notification.data} AND data = ${notification.eventName}")
        eventNameTV.text = "${notification.eventName} DATA = ${notification.data}"
    }

    inner class MyTest {

        fun eventFromObjectHandler(notification: Notification<Any>) {
            println("MyTest::eventFromObjectHandler FROM MyTest CLASS ${notification.data} AND data = ${notification.eventName}")
        }
    }
}


/***
 * Custom View For somethings like rounded drawable
 * */
fun roundedBg(col: Int, corners: Float = 100f, withStroke: Boolean = false, strokeColor: Int = android.graphics.Color.LTGRAY, strokeWeight: Int = 2) = android.graphics.drawable.GradientDrawable().apply {
    shape = android.graphics.drawable.GradientDrawable.RECTANGLE
    cornerRadius = corners
    setColor(col)
    if (withStroke) setStroke(strokeWeight, strokeColor)
}