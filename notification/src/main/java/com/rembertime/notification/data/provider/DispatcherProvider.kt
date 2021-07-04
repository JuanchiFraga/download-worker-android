package com.rembertime.notification.data.provider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class DispatcherProvider(
    val main: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val io: CoroutineDispatcher
) {

    constructor() : this(Dispatchers.Main, Dispatchers.Default, Dispatchers.IO)
}