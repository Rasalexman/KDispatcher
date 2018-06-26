# KDispatcher is a Kotlin EventDispatcher
[ ![Download](https://api.bintray.com/packages/sphc/KDispatcher/kdispatcher/images/download.svg) ](https://bintray.com/sphc/KDispatcher/kdispatcher/_latestVersion)

This is light-weight event dispatcher based on KOTLIN
+ Added a PRIORITY to subscribe function
+ Inline function Included

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

Sinse version 0.1.2 you can use inline functions of KDispatcher. All you need to do is implement `IKDispatcher` interface. Also you can use single lambda functions like (T, String) -> Unit
```kotlin
class MainActivity : AppCompatActivity(), IKDispatcher {

    private val eventListenerOne = this::eventOneHandler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeOperation()
    }
    
    private fun scopeOperation() {
        subscribe(EVENT_CALL_ONE, 3, eventListenerOne)
        subscribe(EVENT_CALL_ONE, 1, eventListenerTwo)
        subscribe(EVENT_CALL_ONE, 2, eventListenerFour)
        
        call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        
        /**
         * But you can simple use inner lambda function to handler notification.
         * So as u hasn't a reference to ISubscriber handler function, when you call
         * `usubscribe(String)` you will delete all references ISubscriber-listener
         */
        val eventName = "LAMBDA_EVENT"
        subscribe<String>(eventName) { data, event->
            println("LAMBDA_EVENT HAS FIRED with event name $data")
            unsubscribe(event)
        }
        
        call(eventName, "FIRST CALL CUSTOM LABDA EVENT")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val test = MyTest()
        // set event listeners for call
        scopeOperation(test)
        // call event with data
        call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        
    }
    
    fun eventOneHandler(data: String, str: String? = null) {
       println("eventOneHandler MY TEST IS COMING event = $str AND data = $data")
    }

}
```

Gradle: 
```
implementation 'com.rasalexman.kdispatcher:kdispatcher:0.1.2'
```

Maven:
```
<dependency>
  <groupId>com.rasalexman.kdispatcher</groupId>
  <artifactId>kdispatcher</artifactId>
  <version>0.1.2</version>
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
