package com.rembertime.notification.domain.estimator

import android.os.Build
import com.rembertime.notification.domain.mapper.estimator.TimeEstimatorProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSystemClock
import java.time.Duration

@Config(sdk = [Build.VERSION_CODES.M], shadows = [ShadowSystemClock::class])
@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
internal class TimeEstimatorProviderTest {

    private lateinit var timeEstimatorProvider: TimeEstimatorProvider

    @Before
    fun setUp() {
        timeEstimatorProvider = TimeEstimatorProvider()
    }

    @Test
    fun givenCalculateRemainingTimeAndReadZeroThenEmitZero() {
        val totalBytes = 10_000L
        val currentReadBytes = 0L

        val remainingTime = timeEstimatorProvider.remainingTime(currentReadBytes, totalBytes)

        assertEquals(0L, remainingTime)
    }

    @Test
    fun givenCalculateRemainingTimeThenUseSpeedAverageBetweenReadings() {
        val totalBytes = 10_000L
        val currentReadBytes = 0L
        val nextReadBytes = 4_000L
        val timeBetweenCalls = 1_000L
        ShadowSystemClock.advanceBy(Duration.ofMillis(timeBetweenCalls))
        timeEstimatorProvider.remainingTime(currentReadBytes, totalBytes)
        ShadowSystemClock.advanceBy(Duration.ofMillis(timeBetweenCalls))

        val remainingTime = timeEstimatorProvider.remainingTime(nextReadBytes, totalBytes)

        val speed = (nextReadBytes - currentReadBytes) / timeBetweenCalls
        val expectedRemainingTime = (totalBytes - nextReadBytes) / speed
        assertEquals(expectedRemainingTime, remainingTime)
    }
}