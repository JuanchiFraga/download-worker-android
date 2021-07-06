package com.rembertime.notification.data.provider

import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

internal class DispatcherProviderTest {

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = DispatcherProvider()
    }

    @Test
    fun givenProvideDispatcherMainThenRetrieveMainDispatcher() {
        assert(dispatcherProvider.main == Dispatchers.Main)
    }

    @Test
    fun givenProvideDispatcherComputationThenRetrieveDefaultDispatcher() {
        assert(dispatcherProvider.computation == Dispatchers.Default)
    }

    @Test
    fun givenProvideDispatcherIoThenRetrieveIODispatcher() {
        assert(dispatcherProvider.io == Dispatchers.IO)
    }
}