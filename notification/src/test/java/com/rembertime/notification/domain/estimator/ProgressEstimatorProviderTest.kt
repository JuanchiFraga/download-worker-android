package com.rembertime.notification.domain.estimator

import com.rembertime.notification.domain.mapper.estimator.ProgressEstimatorProvider
import org.junit.Before
import org.junit.Test

internal class ProgressEstimatorProviderTest {

    private lateinit var progressEstimatorProvider: ProgressEstimatorProvider

    @Before
    fun setUp() {
        progressEstimatorProvider = ProgressEstimatorProvider()
    }

    @Test
    fun givenCurrentReadBytesAreZeroAndTotalThenRetrieveZeroProgress() {
        val totalBytes = 10_000L
        val currentReadBytes = 0L

        val currentProgress = progressEstimatorProvider.actualProgress(currentReadBytes, totalBytes)

        assert(currentProgress == 0)
    }

    @Test
    fun givenCurrentReadBytesAreEqualsTotalThenRetrieveFullProgress() {
        val totalBytes = 10_000L
        val currentReadBytes = totalBytes

        val currentProgress = progressEstimatorProvider.actualProgress(currentReadBytes, totalBytes)

        assert(currentProgress == 100)
    }

    @Test
    fun givenCurrentReadBytesAndTotalThenRetrieveProgressByRuleOfThree() {
        val totalBytes = 10_000L
        val currentReadBytes = 1_356L
        val expectedProgress = ((currentReadBytes * 100) / totalBytes).toInt()

        val currentProgress = progressEstimatorProvider.actualProgress(currentReadBytes, totalBytes)

        assert(currentProgress == expectedProgress)
    }
}