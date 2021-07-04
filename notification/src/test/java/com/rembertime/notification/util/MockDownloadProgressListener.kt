package com.rembertime.notification.util

import com.rembertime.notification.domain.listener.DownloadProgressListener

internal class MockDownloadProgressListener : DownloadProgressListener {
    override fun update(bytesRead: Long, totalBytes: Long): Any? {
        return null
    }
}