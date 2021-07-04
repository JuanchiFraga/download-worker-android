package com.rembertime.notification.domain.mapper.estimator

class ProgressEstimatorProvider {

    fun actualProgress(bytesRead: Long, totalBytes: Long): Int {
        return ((bytesRead * TOTAL_PROGRESS) / totalBytes).toInt()
    }

    companion object {
        private const val TOTAL_PROGRESS = 100
    }
}