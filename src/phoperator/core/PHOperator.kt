package phoperator.core

typealias Subscriber<T> = (T, String?) -> Unit

/**
 * Created by Alex on 23.03.2017.
 */
class PHOperator:IOperator {

    private val subscribers = HashMap<String, ArrayList<Subscriber<Any?>>>()
    private val _priorityListeners by lazy {HashMap<Subscriber<Any?>, Int>()}

    //-------- SINGLETON REALIZATION -----////
    private constructor()
    init { println("This ($this) is a SINGLETON was INITIALIZED") }
    private object Holder { val INSTANCE = PHOperator() }
    companion object {
        private val instance: PHOperator by lazy { Holder.INSTANCE }

        fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit, priority:Int = 0) {
            instance.subscribe(notif, sub, priority)
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
        return this.subscribers[notif] != null
    }

    override fun <T : Any> subscribe(notif: String, sub: (T, String?) -> Unit, priority: Int) {
        val ls = this.subscribers.getOrPut(notif) { ArrayList() }
        _priorityListeners.getOrPut(sub as Subscriber<Any?>) {priority}
        if (ls.indexOf(sub) < 0) {
            ls.add(sub)
            ls.sortBy({_priorityListeners.getOrPut(it) {0}})
        }
    }

    override fun <T : Any> unsubscribe(notif: String, sub:(T, String?) -> Unit) {
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
}

interface IOperator {
    fun <T : Any> subscribe(notif:String, sub:(T, String?)->Unit, priority:Int = 0):Unit
    fun <T : Any> unsubscribe(notif:String, sub:(T, String?)->Unit):Unit
    fun call(notif:String?, data:Any?):Unit
    fun hasSubscribers(notif:String):Boolean
}