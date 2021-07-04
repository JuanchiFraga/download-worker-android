package com.rembertime.notification.domain.mapper.estimator

import android.os.SystemClock

internal class TimeEstimatorProvider {

    private var lastUpdateTime: Long = 0L
    private var lastReadBytes: Long = 0L
    private var averageSpeed: Long = 0L
    private var remainingTime: Long = 0L
    private var attempts: Int = 0

    fun remainingTime(currentBytes: Long, totalBytes: Long): Long {
        val elapsedTime = SystemClock.elapsedRealtime()
        val currentLasUpdateTime = lastUpdateTime
        lastUpdateTime = elapsedTime
        val currentLastReadBytes = lastReadBytes
        lastReadBytes = currentBytes
        val deltaReadBytes = currentBytes - currentLastReadBytes
        val deltaTime = elapsedTime - currentLasUpdateTime
        if (deltaTime != 0L && (deltaReadBytes / deltaTime) + averageSpeed / (attempts + 1) != 0L) {
            val speed = deltaReadBytes / deltaTime
            averageSpeed += speed
            attempts++
            remainingTime = (totalBytes - currentBytes) / (averageSpeed / attempts)
        }
        return remainingTime
    }
}