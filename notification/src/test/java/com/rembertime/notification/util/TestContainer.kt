package com.rembertime.notification.util

import java.util.function.Supplier

internal object TestContainer {

    fun verifyReturnsSingleInstance(supplier: Supplier<*>) {
        val firstInstance = supplier.get()
        val secondInstance = supplier.get()
        assert(firstInstance == secondInstance)
    }

    fun verifyReturnsDifferentInstance(supplier: Supplier<*>) {
        val firstInstance = supplier.get()
        val secondInstance = supplier.get()
        assert(firstInstance != secondInstance)
    }
}