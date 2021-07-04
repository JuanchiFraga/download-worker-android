package com.rembertime.notification.domain.error.exception

import java.lang.RuntimeException

internal class RequestFailException(cause: String) : RuntimeException(cause)