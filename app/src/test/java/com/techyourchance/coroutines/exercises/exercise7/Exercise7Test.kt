package com.techyourchance.coroutines.exercises.exercise7

import com.techyourchance.coroutines.common.TestUtils
import com.techyourchance.coroutines.common.TestUtils.printCoroutineScopeInfo
import com.techyourchance.coroutines.common.TestUtils.printJobsHierarchy
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.lang.Exception
import kotlin.coroutines.EmptyCoroutineContext

class Exercise7Test {

    /*
    Write nested withContext blocks, explore the resulting Job's hierarchy, test cancellation
    of the outer scope
     */
    @Test
    fun nestedWithContext() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch {
                try {
                    delay(100)
                    try {
                        withContext(CoroutineName("first withContext") + Dispatchers.Default) {
                            try {
                                printJobsHierarchy(scopeJob)
                                withContext(CoroutineName("second withContext")) {
                                    delay(100)
                                    println("second with condtext done !")
                                }
                            } catch (e: CancellationException) {
                                println("second withContext is canceled")
                            }
                            delay(100)
                            println("with context done !")
                        }
                    } catch (e: CancellationException) {
                        println("first withContext is canceled")
                    }
                    delay(100)
                    println("scope launch done !")
                } catch (e: CancellationException) {
                    println("outer scope is canceled")
                }
            }
            scopeJob.cancel()
            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine inside another coroutine, explore the resulting Job's hierarchy, test cancellation
    of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedLaunchBuilders() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch {
                try {
                    delay(100)
                    try {
                        withContext(CoroutineName("first withContext") + Dispatchers.Default) {
                            try {
                                printJobsHierarchy(scopeJob)
                                launch(CoroutineName("second withContext")) {
                                    delay(100)
                                    println("second with condtext done !")
                                }
                            } catch (e: CancellationException) {
                                println("second withContext is canceled")
                            }
                            delay(100)
                            println("with context done !")
                        }
                    } catch (e: CancellationException) {
                        println("first withContext is canceled")
                    }
                    delay(100)
                    println("scope launch done !")
                } catch (e: CancellationException) {
                    println("outer scope is canceled")
                }
            }
            delay(250)
            scopeJob.cancel()
            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine on "outer scope" inside another coroutine, explore the resulting Job's hierarchy,
    test cancellation of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedCoroutineInOuterScope() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch {
                try {
                    delay(100)
                    try {
                        withContext(CoroutineName("first withContext") + Dispatchers.Default) {
                            try {
                                printJobsHierarchy(scopeJob)
                                scope.launch(CoroutineName("second withContext")) {
                                    delay(100)
                                    println("second with condtext done !")
                                }
                            } catch (e: CancellationException) {
                                println("second withContext is canceled")
                            }
                            delay(100)
                            println("with context done !")
                        }
                    } catch (e: CancellationException) {
                        println("first withContext is canceled")
                    }
                    delay(100)
                    println("scope launch done !")
                } catch (e: CancellationException) {
                    println("outer scope is canceled")
                }
            }
            delay(250)
            scopeJob.cancel()
            scopeJob.join()
            println("test done")
        }
    }


}