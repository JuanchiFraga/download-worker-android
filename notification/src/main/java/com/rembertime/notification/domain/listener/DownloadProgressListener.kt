package com.rembertime.notification.domain.listener

internal interface DownloadProgressListener {

    fun update(bytesRead: Long, totalBytes: Long): Any?
}