# KDispatcher is a Kotlin EventDispatcher

[![Kotlin 1.2.71](https://img.shields.io/badge/Kotlin-1.2.71-blue.svg)](http://kotlinlang.org)[![Download](https://api.bintray.com/packages/sphc/KDispatcher/kdispatcher/images/download.svg)](https://bintray.com/sphc/KDispatcher/kdispatcher/_latestVersion)[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f61b6230a3da404580250008f2e966b2)](https://app.codacy.com/app/Rasalexman/KDispatcher?utm_source=github.com&utm_medium=referral&utm_content=Rasalexman/KDispatcher&utm_campaign=Badge_Grade_Dashboard)

This is light-weight event dispatcher based on KOTLIN
* `priority: Int? = null` to subscribe function for sorting
* Inline function Included

You can subscribe on event by calling:
```kotlin
val EVENT_CALL_ONE = "simple_event_name"
val eventListener = ::eventHandler
val priority = 1
KDispatcher.subscribe(EVENT_CALL_ONE, eventListener, priority)
```
where:
* EVENT_CALL_ONE - simple event type :String
* eventListener - function listener for event
* priority - the priority to sort calling functions


```kotlin
/**
* notif:Notification<T:Any> - event holder object that store
* data:T? = null - can be any type of data
* eventName:String? = null - current event type
* cause u may have more then one EVENT_TYPE for current event listener
*/
fun eventHandler(notif:Notification<Any>){
  when(notif.eventName){
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

Sinse version 0.1.2 you can use extension and inline functions of KDispatcher. All you need to do is implement `IKDispatcher` interface. Also you can use single lambda functions like `(Notification<T:Any>) -> Unit` as event handlers
```kotlin
class MainActivity : AppCompatActivity(), IKDispatcher {

    private val eventListenerOne = this::eventOneHandler
    //...
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeOperation()
    }
    
    private fun scopeOperation() {
        // subscribe event to this handlers
        subscribe(EVENT_CALL_ONE, eventListenerOne, 3)
        subscribe(EVENT_CALL_ONE, ::eventListenerTwo, 1)
        subscribe(EVENT_CALL_ONE, MyClass::eventListenerFour, 2)
        // call event
        call(EVENT_CALL_ONE, "FIRST CALL FROM KDISPATCHER")
        
        /**
         * But you can simple use inner lambda function to handler notification.
         * So as u hasn't a reference to ISubscriber handler function, when you call
         * `usubscribe(String)` you will delete all references ISubscriber-listener
         */
        val eventName = "LAMBDA_EVENT"
        subscribe<String>(eventName) { notification ->
            println("LAMBDA_EVENT HAS FIRED with event name ${notification.eventName} and data ${notification.data}")
            unsubscribe(notification.eventName)
        }
        
        call(eventName, "FIRST CALL CUSTOM LABDA EVENT")
        
        /**
        * Since version 0.1.7 you can subscribe by scope of events by a single callback
        */
        subscribeList<Any>(listOf("notif_one", "notif_two")) {
            when(it.eventName) {
                "notif_one" -> Toast.makeText(this, "This is notif_one", Toast.LENGTH_SHORT).show()
                "notif_two" -> Toast.makeText(this, "This is notif_two", Toast.LENGTH_SHORT).show()
            }
        }

        call("notif_one")
        call("notif_two")
    }
    
    fun eventOneHandler(notification:Notification<Any>) {
       println("eventOneHandler MY TEST IS COMING event = ${notification.eventName} AND data = ${notification.data}")
    }

}
```

Gradle: 
```kotlin
implementation 'com.rasalexman.kdispatcher:kdispatcher:x.y.z'
```

Maven:
```kotlin
<dependency>
  <groupId>com.rasalexman.kdispatcher</groupId>
  <artifactId>kdispatcher</artifactId>
  <version>x.y.z</version>
  <type>pom</type>
</dependency>
```

- ThreadSafe
- Simple
- Usefull

License
----

MIT License

Copyright (c) 2018 Aleksandr Minkin (sphc@yandex.ru)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
