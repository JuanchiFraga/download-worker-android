package com.rembertime.notification.util.extensions

import java.lang.Exception

internal fun <T> (() -> T).catchAndGet(onErrorValue: T): T {
    return try { this() } catch (e: Exception) { onErrorValue }
}