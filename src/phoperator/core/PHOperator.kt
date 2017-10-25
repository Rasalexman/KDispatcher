package ru.fortgroup.dpru.utils

typealias Subscriber<T> = (T, String?) -> Unit

/**
 * Created by a.minkin on 25.10.2017.
 */
object KEventDispatcher:IOperator {
    private val subscribers = HashMap<String, ArrayList<Subscriber<Any?>>>()
    private val _priorityListeners by lazy {HashMap<Subscriber<Any?>, Int>()}

    override fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit, priority: Int) {
        val ls = this.subscribers.getOrPut(notif) { ArrayList() }
        _priorityListeners.getOrPut(sub as Subscriber<Any?>) {priority}
        if (ls.indexOf(sub) < 0) {
            ls.add(sub)
            ls.sortBy({_priorityListeners.getOrPut(it) {0}})
        }
    }

    override fun <T : Any> unsubscribe(notif: String, sub: (T, String?) -> Unit) {
        val ls = this.subscribers[notif]
        _priorityListeners.remove(sub)
        if (ls != null) {
            if (ls.remove(sub)) {
                if (ls.isEmpty()) {
                    this.subscribers.remove(notif)
                }
            }
        }
    }

    override fun call(notif: String?, data: Any?) {
        val ls = this.subscribers[notif!!]
        ls?.forEach { it(data, notif) } ?: println("NO LISTENERS FOR EVENT '$notif'")
    }

    override fun hasSubscribers(notif: String): Boolean {
        return this.subscribers[notif] != null
    }
}

interface IOperator {
    fun <T : Any> subscribe(notif:String, sub:(T, String?)->Unit, priority:Int = 0):Unit
    fun <T : Any> unsubscribe(notif:String, sub:(T, String?)->Unit):Unit
    fun call(notif:String?, data:Any?):Unit
    fun hasSubscribers(notif:String):Boolean
}