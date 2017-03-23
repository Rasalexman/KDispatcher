package phoperator.core

import phoperator.phinters.IOperator

typealias Subscriber<T> = (T, String?) -> Unit

/**
 * Created by Alex on 23.03.2017.
 */
class PHOperator:IOperator {

    private val subscribers = HashMap<String, ArrayList<Subscriber<Any?>>>()

    //-------- SINGLETON REALIZATION -----////
    private constructor()
    init { println("This ($this) is a SINGLETON was INITIALIZED") }
    private object Holder { val INSTANCE = PHOperator() }
    companion object {
        private val instance: PHOperator by lazy { Holder.INSTANCE }

        fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit) {
            instance.subscribe(notif, sub)
        }
        fun <T : Any> unsubscribe(notif: String, sub:(T, String?) -> Unit){
            instance.unsubscribe(notif, sub)
        }
        fun call(notif: String, data: Any?){
            instance.call(notif, data)
        }
    }
    //-----------------------------------///

    override fun hasSubscribers(notif: String): Boolean {
        return this.subscribers[notif!!] != null
    }

    override fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit) {
        val ls = this.subscribers.getOrPut(notif!!) { ArrayList() }
        if (ls!!.indexOf(sub) < 0) {
            ls!!.add(sub as Subscriber<Any?>)
        }
    }

    override fun <T : Any> unsubscribe(notif: String, sub:(T, String?) -> Unit) {
        val ls = this.subscribers[notif!!]
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
        ls?.forEach { it(data, notif) } ?: println("NO LISTENERS FOR EVENT $notif")
    }
}