package com.rembertime.notification.data.mapper

import com.rembertime.notification.domain.error.exception.RequestFailException
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.jvm.Throws

internal class DownloadFileMapper {

    /**
     * Map response to optional body
     *
     * @param response to convert
     * @return the optional body response
     */
    @Throws(RequestFailException::class)
    fun responseToBody(response: Response<ResponseBody>): ResponseBody {
        if (response.isSuccessful.not() || response.body() == null) {
            throw RequestFailException("Request fail with: $response")
        }
        return response.body()!!
    }
}