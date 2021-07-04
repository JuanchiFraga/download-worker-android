package com.rembertime.notification.domain.error

import com.rembertime.notification.R
import com.rembertime.notification.domain.error.exception.NoConnectivityException
import com.rembertime.notification.util.StringProvider
import com.rembertime.notification.util.extensions.isStreamClosedError
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.lang.Exception

internal class WorkerErrorHandler(private val stringProvider: StringProvider) {

    fun getErrorMessage(cause: Exception): String? = when {
        wasACancellation(cause) -> null
        cause is NoConnectivityException -> stringProvider.getString(R.string.notification_connection_error_message)
        else -> stringProvider.getString(R.string.notification_generic_error_message)
    }

    private fun wasACancellation(cause: Exception): Boolean {
        return cause is CancellationException || cause is IOException && cause.isStreamClosedError()
    }
}