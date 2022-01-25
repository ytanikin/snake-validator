package com.yeldos.snakevalidator

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T

    fun whenever(any: Any): OngoingStubbing<Any> = Mockito.`when`(any)
}