package com.rembertime.notification.util

import com.rembertime.notification.data.provider.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
fun createTestDispatcherProvider() = DispatcherProvider(TestCoroutineDispatcher(), TestCoroutineDispatcher(), TestCoroutineDispatcher())