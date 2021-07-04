package com.rembertime.notification.data.repository

import com.rembertime.notification.data.mapper.DownloadFileMapper
import com.rembertime.notification.data.service.DownloadFileApi
import com.rembertime.notification.domain.error.exception.NoConnectivityException
import com.rembertime.notification.domain.error.exception.RequestFailException
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.jvm.Throws

internal class DownloadFileRepository constructor(
    private val service: DownloadFileApi,
    private val mapper: DownloadFileMapper
) {

    @Throws(NoConnectivityException::class, RequestFailException::class)
    suspend fun downloadFileIn(fileToDownload: String): ResponseBody {
        try {
            val response = service.downloadFile(fileToDownload)
            return mapper.responseToBody(response)
        } catch (e: IOException) {
            throw NoConnectivityException()
        }
    }
}