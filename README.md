# KDispatcher is a Kotlin EventDispatcher
This is light-weight event dispatcher based on KOTLIN
+ Added a PRIORITY to subscribe function

You can subscribe on event by calling:
```kotlin
KDispatcher.subscribe(EVENT_CALL_ONE, ::nextFun, 1)
```
where:
- EVENT_CALL_ONE - simple event type :String
- nextFun - function listener for event
- 1 - the priority to sort calling functions


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
KDispatcher.call(EVENT_CALL_ONE, test)
```

Don't forget to unsubscribe your listeners when u dont need it anymore.
```kotlin
KDispatcher.unsubscribe(EVENT_CALL_ONE, ::nextFun)
```

Gradle: 
```
implementation 'com.rasalexman.kdispatcher:kdispatcher:0.1.0'
```

Maven:
```
<dependency>
  <groupId>com.rasalexman.kdispatcher</groupId>
  <artifactId>kdispatcher</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

- TreadSafe
- Simple
- Usefull

License
----

MIT


**Free Software, Hell Yeah!**
