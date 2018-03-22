# KDispatcher is a Kotlin EventDispatcher
This is light-weight event dispatcher based on KOTLIN
+ Added a PRIORITY to subscribe function

You can subscribe on event by calling:
```kotlin
val EVENT_CALL_ONE = "simple_event_name"
val eventListener = ::eventHandler
val priority = 1
KDispatcher.subscribe(EVENT_CALL_ONE, eventListener, priority)
```
where:
- EVENT_CALL_ONE - simple event type :String
- eventListener - function listener for event
- priority - the priority to sort calling functions


```kotlin
/**
* data:Any? - can be any type of data
* str:String? = null - current event type
* cause u may have more then one EVENT_TYPE for current event listener
*/
fun eventHandler(data:Any?, str:String? = null){
  when(str){
      EVENT_CALL_ONE -> println("FIRST EVENT")
  }
}
```
Of course u can simpe call the event for all listeners by
```kotlin
val test:MyTestClass = MyTestClass()
KDispatcher.call(EVENT_CALL_ONE, test)
```

Don't forget to unsubscribe your listeners when u dont need it anymore.
```kotlin
KDispatcher.unsubscribe(EVENT_CALL_ONE, eventListener)
```

Gradle: 
```
implementation 'com.rasalexman.kdispatcher:kdispatcher:0.1.1'
```

Maven:
```
<dependency>
  <groupId>com.rasalexman.kdispatcher</groupId>
  <artifactId>kdispatcher</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

- ThreadSafe
- Simple
- Usefull

License
----

MIT


**Free Software, Hell Yeah!**
