package com.rembertime.notification.util.extensions

import java.io.IOException

internal fun IOException.isStreamClosedError(): Boolean {
    return message == "Stream closed"
}