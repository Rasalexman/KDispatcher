package phoperator.phinters

/**
 * Created by Alex on 23.03.2017.
 */
interface IOperator {

    fun <T : Any> subscribe(notif:String, sub:(T, String?)->Unit):Unit
    fun <T : Any> unsubscribe(notif:String, sub:(T, String?)->Unit):Unit
    fun call(notif:String?, data:Any?):Unit
    fun hasSubscribers(notif:String):Boolean
}