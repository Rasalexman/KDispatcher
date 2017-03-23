import phoperator.core.PHOperator

/**
 * Created by Alex on 23.03.2017.
 */

val EVENT_CALL_ONE:String = "CALL_NUMBER_TWO";

fun main(args : Array<String>) {
    println("Hello, world!")

    val test:MyTest = MyTest()

    PHOperator.subscribe(EVENT_CALL_ONE, ::nextFun)
    PHOperator.subscribe(EVENT_CALL_ONE, ::testFun)

    PHOperator.call("TEST_CALL", "String")
    PHOperator.call(EVENT_CALL_ONE, test)

    PHOperator.unsubscribe(EVENT_CALL_ONE, ::nextFun)
    PHOperator.call(EVENT_CALL_ONE, test)

}

fun testFun(data:Any?, str:String? = null):Unit{
    println("testFun INVOKED With EVENT $str")
}
fun nextFun(data:Any?, str:String? = null){
    when(data){
        is MyTest -> println("MYTEST SI COMING $str")
    }
}

class MyTest {

    init {
        //PHOperator.subscribe("TEST_CALL", this::testTwo)
    }

    fun testOne(e:Any?):Unit{
        println("testOne FROM MY CLASS $e")
    }
    fun testTwo(e:Any?){
        println("testTwo FROM MY CLASS $e")
    }
}
