package com.mincor.kdispatcher

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

fun main() {
    val flow = flow<Int> {
        repeat(10) {
            this.emit(it)
        }
    }

    runBlocking {
        (0..10).asFlow().flowOn(Dispatchers.IO).collect {
            println("Another flow $it")
        }
    }


    object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Default

        fun launchOn() {
            runBlocking {
                flow.collect {
                    println("----> DATA FROM FLOW $it")
                }
            }
        }
    }.launchOn()

    //val flowCollector = SimpleFlowClass(flow)
    // flowCollector.subscribe()
}

class SimpleFlowClass<T>(private val coldFlow: Flow<T>) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    fun subscribe() {
        launch {
            withContext(Dispatchers.IO) {
                coldFlow.collect {
                    println("----> DATA FROM FLOW $it")
                }
            }

        }
    }
}