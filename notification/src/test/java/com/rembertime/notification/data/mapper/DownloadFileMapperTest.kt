package com.rembertime.notification.data.mapper

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.domain.error.exception.RequestFailException
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class DownloadFileMapperTest {

    private lateinit var mapper: DownloadFileMapper

    @Before
    fun setUp() {
        mapper = DownloadFileMapper()
    }

    @Test(expected = RequestFailException::class)
    fun givenErrorResponseThenThrowRequestFailException() {
        val errorResponse: Response<ResponseBody> = mock()
        whenever(errorResponse.isSuccessful).thenReturn(false)
        whenever(errorResponse.code()).thenReturn(400)

        mapper.responseToBody(errorResponse)
    }

    @Test(expected = RequestFailException::class)
    fun givenNullBodyResponseThenThrowRequestFailException() {
        val errorResponse: Response<ResponseBody> = mock()
        whenever(errorResponse.isSuccessful).thenReturn(true)
        whenever(errorResponse.code()).thenReturn(200)
        whenever(errorResponse.body()).thenReturn(null)

        mapper.responseToBody(errorResponse)
    }

    @Test
    fun givenSuccessResponseWithNoNullBodyThenRetrieveResponseBody() {
        val successResponse: Response<ResponseBody> = mock()
        val responseBody: ResponseBody = mock()
        whenever(successResponse.isSuccessful).thenReturn(true)
        whenever(successResponse.code()).thenReturn(200)
        whenever(successResponse.body()).thenReturn(responseBody)

        val body = mapper.responseToBody(successResponse)

        assert(body == responseBody)
    }
}