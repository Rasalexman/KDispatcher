package com.mincor.kdispatcher.core

typealias Subscriber<T> = (T, String?) -> Unit

/**
 * Created by a.minkin on 25.10.2017.
 */
object KDispatcher: IDispatcher {

    @Volatile private var subscribers:MutableMap<String, ArrayList<Subscriber<Any?>>> = mutableMapOf()
    private val _priorityListeners by lazy {HashMap<Subscriber<Any?>, Int>()}

    override fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit, priority: Int) {
        val ls = subscribers.getOrPut(notif) { ArrayList() }
        _priorityListeners.getOrPut(sub as Subscriber<Any?>) {priority}
        if (ls.indexOf(sub) < 0) {
            ls.add(sub)
            ls.sortBy({ _priorityListeners.getOrPut(it) {0}})
        }
    }

    override fun <T : Any> unsubscribe(notif: String, sub: (T, String?) -> Unit) {
        val ls = subscribers[notif]
        _priorityListeners.remove(sub)
        if (ls != null) {
            if (ls.remove(sub)) {
                if (ls.isEmpty()) {
                    subscribers.remove(notif)
                }
            }
        }
    }

    override fun unsubscribeAll(notif: String) {
        if(hasSubscribers(notif)){
            subscribers.remove(notif)
        }
    }

    @Synchronized
    override fun call(notif: String?, data: Any?) {
        synchronized(subscribers){
            val ls = subscribers[notif!!]
            ls?.forEach { it(data, notif) } ?: println("NO LISTENERS FOR EVENT '$notif'")
        }
    }

    override fun hasSubscribers(notif: String): Boolean = subscribers[notif] != null
}

interface IDispatcher {
    fun <T : Any> subscribe(notif:String, sub:(T, String?)->Unit, priority:Int = 0)
    fun <T : Any> unsubscribe(notif:String, sub:(T, String?)->Unit)
    fun unsubscribeAll(notif:String)
    fun call(notif:String?, data:Any?)
    fun hasSubscribers(notif:String):Boolean
}