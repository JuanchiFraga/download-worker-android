package com.rembertime.notification.domain.error

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.R
import com.rembertime.notification.domain.error.exception.NoConnectivityException
import com.rembertime.notification.util.StringProvider
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.lang.RuntimeException
import kotlin.coroutines.cancellation.CancellationException

internal class WorkerErrorHandlerTest {

    private lateinit var workerErrorHandler: WorkerErrorHandler
    private lateinit var stringProvider: StringProvider

    @Before
    fun setUp() {
        stringProvider = mock()
        workerErrorHandler = WorkerErrorHandler(stringProvider)
    }

    @Test
    fun givenCancellationThenRetrieveNullMessage() {
        val message = workerErrorHandler.getErrorMessage(CancellationException())

        assert(message == null)
    }

    @Test
    fun givenBufferCloseIOExceptionThenRetrieveNullMessage() {
        val message = workerErrorHandler.getErrorMessage(IOException("Stream closed"))

        assert(message == null)
    }

    @Test
    fun givenNoConnectivityExceptionThenRetrieveNoInternetErrorMessage() {
        val internetError = "It's seems there is no internet"
        whenever(stringProvider.getString(R.string.notification_connection_error_message)).thenReturn(internetError)

        val message = workerErrorHandler.getErrorMessage(NoConnectivityException())

        assert(message == internetError)
    }

    @Test
    fun givenAnyOtherExceptionThenRetrieveGenericErrorMessage() {
        val genericError = "Oops, something went wrong"
        whenever(stringProvider.getString(R.string.notification_generic_error_message)).thenReturn(genericError)

        val message = workerErrorHandler.getErrorMessage(RuntimeException())

        assert(message == genericError)
    }
}