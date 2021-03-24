// Copyright (c) 2018 Aleksandr Minkin (sphc@yandex.ru)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software
// and associated documentation files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


@file:Suppress("UNCHECKED_CAST")

package com.rasalexman.kdispatcher

/**
 * Created by alexander at 17.03.2018.
 */
typealias Subscriber<T> = (Notification<T>) -> Unit

/**
 * Created by alexander at 19.07.2018
 * Simple data handler class
 *
 * @param data - any data to send
 * @param eventName - Name of notification
 */
data class Notification<T : Any?>(var data: T? = null, var eventName: String)

/**
 * Created by a.minkin on 25.10.2017.
 * Main Notifications storage
 */
object KDispatcher : IDispatcher {
    /**
     * Map of notification name and callback listener [Subscriber]
     */
    override val subscribers = mutableMapOf<String, MutableList<Subscriber<Any>>>()

    /**
     * Priority holder storage
     */
    override val priorityListeners = mutableMapOf<Subscriber<Any>, Int?>()
}

/**
 * Main KDispatcher interface
 */
interface IDispatcher {
    /**
     * Map of notification name and callback listener [Subscriber]
     */
    val subscribers: MutableMap<String, MutableList<Subscriber<Any>>>

    /**
     * Priority holder storage
     */
    val priorityListeners: MutableMap<Subscriber<Any>, Int?>
}

/**
 * Add event handler to given notification name
 *
 * @param notificationName
 * Event name to subscribe
 *
 * @param sub
 * Event handler function (Notification) -> Unit
 *
 * @param priority
 * Call priority of event handler
 */
inline fun <reified T : Any> IDispatcher.subscribe(notificationName: String, priority: Int? = null, noinline sub: Subscriber<T>) {
    val localSubscribers = subscribers
    val ls = localSubscribers.getOrPut(notificationName) { mutableListOf() }
    if (ls.indexOf(sub as Subscriber<*>) < 0) {
        ls.add(sub)
        priority?.takeIf { it > 0 }?.let {
            priorityListeners[sub] = priority
            ls.sortBy { subscriber -> priorityListeners.getOrPut(subscriber) { priority } }
        }
    }
}

/**
 * Unsubscribe listener from notification event name. if no listener there are remove all listeners by given `notif` name
 *
 * @param notificationName
 * The name of notification for unsubscribe
 *
 * @param sub
 * optional param for function that need to unsubscribe, if it's null there are going to unsubscribe all listeners for given notifName
 */
inline fun <reified T : Any> IDispatcher.unsubscribe(notificationName: String, noinline sub: Subscriber<T>? = null) {
    (sub as? Subscriber<Any>)?.let { subscriber ->
        val localSubscribers = subscribers
        val localPriorityListener = priorityListeners
        localPriorityListener.remove(subscriber)
        val ls = localSubscribers[notificationName]
        ls?.let { listeners ->
            if (listeners.remove(subscriber)) {
                if (listeners.isEmpty()) {
                    localSubscribers.remove(notificationName)?.clear()
                }
            }
        }
    } ?: unsubscribeAll(notificationName)
}

/**
 * Unsubscribe all listeners by notification name
 *
 * @param notif
 * Event name for remove all notification
 */
fun IDispatcher.unsubscribeAll(notif: String) {
    if (hasSubscribers(notif)) {
        val localSubscribers = subscribers
        localSubscribers.remove(notif)?.clear()
    }
}

/**
 * Call notification listeners by given `notif` name and data
 *
 * @param notif
 * Event name for call all notification listeners
 *
 * @param data
 * Notification data
 */
fun IDispatcher.call(notif: String, data: Any? = null) {
    val localSubscribers = subscribers.toMap()
    val ls = localSubscribers[notif]?.toList()
    ls?.let {
        val notification = Notification(data, notif)
        it.forEach {
            it(notification)
        }
        notification.data = null
    } ?: println("NO LISTENERS FOR EVENT '$notif'")
}

/**
 * Check if given event name has any handlers
 *
 * @param notif - notification name to check
 */
fun IDispatcher.hasSubscribers(notif: String): Boolean {
    val localSubscribers = subscribers.toMap()
    return localSubscribers[notif]?.isNotEmpty() == true
}

/**
 * Helper interface
 */
interface IKDispatcher

/**
 * Subscribe an notification with listener function like (T, String) -> Unit,
 * where T - generic type data, String - event name
 *
 * @param notif
 * Notification name
 *
 * @param sub
 * Callback function as lambda (Notification<T>) -> Unit
 *
 * @param priority
 * The priority of calling callback function
 */
inline fun <reified T : Any> IKDispatcher.subscribe(notif: String, priority: Int? = null, noinline sub: Subscriber<T>): IKDispatcher {
    KDispatcher.subscribe(notif, priority, sub)
    return this
}

/**
 * Subscribe a list of notifications to a single callback function
 *
 * @param notifs - list of notification to subscribe
 * @param priority - priority of subscribing
 * @param sub - callback listener like [Subscriber]
 */
inline fun <reified T : Any> IKDispatcher.subscribeList(notifs: List<String>, priority: Int? = null, noinline sub: Subscriber<T>): IKDispatcher {
    notifs.forEach { notif ->
        if (!hasSubscribers(notif)) KDispatcher.subscribe(notif, priority, sub)
    }
    return this
}

/**
 * Check if given event name has any handlers
 *
 * @param notif - This is a notification name to check handlers
 */
fun IKDispatcher.hasSubscribers(notif: String): Boolean {
    return KDispatcher.hasSubscribers(notif)
}

/**
 * Unsubscribe listener from notification, remove all listeners by given `notif` name if no listener
 *
 * @param notif - notification name to unsubscribe
 * @param sub - callback listener [Subscriber]
 */
fun IKDispatcher.unsubscribe(notif: String, sub: Subscriber<Any>? = null) {
    KDispatcher.unsubscribe(notif, sub)
}

/**
 * Unsubscribe all listeners by notification name
 *
 * @param notif - notification name to unsubscribe all listeners
 */
fun IKDispatcher.unsubscribeAll(notif: String) {
    KDispatcher.unsubscribeAll(notif)
}

/**
 * Unsubscribe a list of notifications
 *
 * @param notifs - list of notifications names to unsubscribe
 */
fun IKDispatcher.unsubscribeList(notifs: List<String>) {
    notifs.forEach { notif ->
        if (!hasSubscribers(notif)) unsubscribe(notif)
    }
}

/**
 * Call notification listeners by given `notif` name and data
 *
 * @param notif - notification name
 * @param data - any data to send
 */
fun IKDispatcher.call(notif: String, data: Any? = null) {
    KDispatcher.call(notif, data)
}

/**
 * Call helper function for pass data into notification [Subscriber]
 * @param data - any data to send
 */
infix fun <T : Any> String.callWith(data: T) {
    KDispatcher.call(this, data)
}