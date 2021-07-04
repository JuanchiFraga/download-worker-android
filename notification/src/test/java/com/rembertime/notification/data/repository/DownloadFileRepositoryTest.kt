package com.rembertime.notification.data.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.data.mapper.DownloadFileMapper
import com.rembertime.notification.data.service.DownloadFileApi
import com.rembertime.notification.domain.error.exception.NoConnectivityException
import com.rembertime.notification.domain.error.exception.RequestFailException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/* We run the blocking code in Dispatcher.IO */
@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
internal class DownloadFileRepositoryTest {

    private lateinit var service: DownloadFileApi
    private lateinit var mapper: DownloadFileMapper
    private lateinit var repository: DownloadFileRepository

    @Before
    fun setUp() {
        service = mock()
        mapper = mock()
        repository = DownloadFileRepository(service, mapper)
    }

    @Test(expected = NoConnectivityException::class)
    fun givenDownloadWithoutNetworkConnectionThenThrowNoConnectivityException() = runBlockingTest {
        val fileName = "file"
        whenever(service.downloadFile(fileName)).then { throw IOException() }

        repository.downloadFileIn(fileName)
    }

    @Test(expected = RequestFailException::class)
    fun givenRequestFailThrowRequestFailException() = runBlockingTest {
        val fileName = "file"
        val response: Response<ResponseBody> = mock()
        whenever(service.downloadFile(fileName)).thenReturn(response)
        whenever(mapper.responseToBody(response)).thenThrow(RequestFailException::class.java)

        repository.downloadFileIn(fileName)
    }

    @Test
    fun givenRequestSuccessThenRetrieveResponseBody() = runBlockingTest {
        val fileName = "file"
        val expectedBody: ResponseBody = mock()
        val response: Response<ResponseBody> = mock()
        whenever(service.downloadFile(fileName)).thenReturn(response)
        whenever(mapper.responseToBody(response)).thenReturn(expectedBody)

        val body = repository.downloadFileIn(fileName)

        assert(body == expectedBody)
    }
}