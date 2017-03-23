# PHOperator Kotlin EventDispatcher
This is light-weight event dispatcher based on KOTLIN 1.1.+

You can subscribe on event by calling:
```kotlin
PHOperator.subscribe(EVENT_CALL_ONE, ::nextFun)
```
where:
- EVENT_CALL_ONE - simple event type :String
- nextFun - function listener for event like


```kotlin
/**
* data:Any? - can be any type of data
* str:String? = null - current event type
* cause u may have more then one EVENT_TYPE for current event listener
*/
fun nextFun(data:Any?, str:String? = null){
  when(str){
      EVENT_CALL_ONE -> println("FIRST EVENT")
  }
}
```
Of course u can simpe call the event for all listeners by
```kotlin
val test:MyTestClass = MyTestClass()
PHOperator.call(EVENT_CALL_ONE, test)
```

Don't forget to unsubscribe your listeners when u dont need it anymore.
```kotlin
PHOperator.unsubscribe(EVENT_CALL_ONE, ::nextFun)
```

### Todos

 - Write Performance Test
 - Add "Priority" of calling listeners

License
----

MIT


**Free Software, Hell Yeah!**
