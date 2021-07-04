package com.rembertime.notification.data.service.interceptor

import com.rembertime.notification.domain.listener.DownloadProgressListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

internal class DownloadInterceptor(private val listener: DownloadProgressListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val wrappedBody = response.body?.let { DownloadProgressResponseBody(it, listener) }
        return response.newBuilder().body(wrappedBody).build()
    }
}