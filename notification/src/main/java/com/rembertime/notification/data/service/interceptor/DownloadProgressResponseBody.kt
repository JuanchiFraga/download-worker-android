package com.rembertime.notification.data.service.interceptor

import com.rembertime.notification.domain.listener.DownloadProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.Source
import okio.buffer
import okio.ForwardingSource
import java.io.IOException
import kotlin.jvm.Throws

internal class DownloadProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: DownloadProgressListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {

        return object : ForwardingSource(source) {

            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                val isDone = bytesRead != -1L
                totalBytesRead += if (isDone) bytesRead else 0
                progressListener.update(totalBytesRead, responseBody.contentLength())
                return bytesRead
            }
        }
    }
}