package com.rasalexman.kdispatcher

import android.util.ArrayMap

/**
 * Created by alexander at 17.03.2018.
 */
typealias Subscriber<T> = (Notification<T>) -> Unit

/**
 * Created by alexander at 19.07.2018
 * Simple data handler class
 */
data class Notification<T : Any?>(var data: T? = null, var eventName: String)

/**
 * Created by a.minkin on 25.10.2017.
 */
object KDispatcher : IDispatcher {
    override val subscribers = ArrayMap<String, ArrayList<Subscriber<Any>>>()
    override val priorityListeners = ArrayMap<Subscriber<Any>, Int?>()
}

/**
 * Main KDispatcher interface
 */
interface IDispatcher {
    val subscribers: ArrayMap<String, ArrayList<Subscriber<Any>>>
    val priorityListeners: ArrayMap<Subscriber<Any>, Int?>
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
inline fun <reified T : Any> IDispatcher.subscribe(notificationName: String, noinline sub: Subscriber<T>, priority: Int? = null) {
    val ls = subscribers.getOrPut(notificationName) { ArrayList() }
    if (ls.indexOf(sub as Subscriber<Any>) < 0) {
        ls.add(sub)
        priority?.let {
            priorityListeners[sub] = priority
            ls.sortBy { subscriber -> priorityListeners.getOrPut(subscriber) { priority } }
        }
    }
}

/**
 * Unsubscribe listener from notification event name. if no listener there are remove all listeners by given `notif` name
 */
inline fun <reified T : Any> IDispatcher.unsubscribe(notificationName: String, noinline sub: Subscriber<T>? = null) {
    (sub as? Subscriber<Any>)?.let { subscriber ->
        priorityListeners.remove(subscriber)
        val ls = subscribers[notificationName]
        ls?.let { listeners ->
            if (listeners.remove(subscriber)) {
                if (listeners.isEmpty()) {
                    subscribers.remove(notificationName)?.clear()
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
        subscribers.remove(notif)?.clear()
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
    synchronized(subscribers) {
        val ls = subscribers[notif]
        val notification = Notification(data, notif)
        ls?.forEach {
            it(notification)
        } ?: println("NO LISTENERS FOR EVENT '$notif'")
        notification.data = null
    }
}

/**
 * Check if given event name has any handlers
 */
fun IDispatcher.hasSubscribers(notif: String): Boolean = subscribers[notif] != null

/**
 * Helper interface
 */
interface IKDispatcher

/**
 * Subscribe an notification with listener function like (T, String) -> Unit,
 * where T - generic type data, String - event name
 */
inline fun <reified T : Any> IKDispatcher.subscribe(notif: String, noinline sub: Subscriber<T>, priority: Int? = null) {
    KDispatcher.subscribe(notif, sub, priority)
}

/**
 * Subscribe an notification with listener function like (T, String) -> Unit without priority
 */
inline fun <reified T : Any> IKDispatcher.subscribe(notif: String, noinline sub: Subscriber<T>) {
    KDispatcher.subscribe(notif, sub, null)
}

/**
 * Subscribe a list of notifications to a single callback function
 */
inline fun <reified T : Any> IKDispatcher.subscribeList(notifes:List<String>, priority: Int? = null, noinline sub: Subscriber<T>) {
    notifes.forEach { notif->
        if (!hasSubscribers(notif)) KDispatcher.subscribe(notif, sub, priority)
    }
}


/**
 * Check if given event name has any handlers
 *
 * @param notif
 * This is a notification name to check handlers
 */
fun IKDispatcher.hasSubscribers(notif: String): Boolean {
    return KDispatcher.hasSubscribers(notif)
}

/**
 * Unsubscribe listener from notification, remove all listeners by given `notif` name if no listener
 */
fun IKDispatcher.unsubscribe(notif: String, sub: Subscriber<Any>? = null) {
    KDispatcher.unsubscribe(notif, sub)
}

/**
 * Unsubscribe all listeners by notification name
 */
fun IKDispatcher.unsubscribeAll(notif: String) {
    KDispatcher.unsubscribeAll(notif)
}

/**
 * Call notification listeners by given `notif` name and data
 */
fun IKDispatcher.call(notif: String, data: Any? = null) {
    KDispatcher.call(notif, data)
}
