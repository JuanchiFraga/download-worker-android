package com.rembertime.notification.domain.mapper

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.domain.mapper.estimator.ProgressEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.SizeEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.TimeEstimatorProvider
import com.rembertime.notification.domain.model.RemainingProgressModel
import com.rembertime.notification.domain.usecase.GetLongAsStringHumanReadableTimeUseCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

internal class ProgressMapperTest {

    private lateinit var sizeEstimatorProvider: SizeEstimatorProvider
    private lateinit var getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase
    private lateinit var timeEstimatorProvider: TimeEstimatorProvider
    private lateinit var progressEstimatorProvider: ProgressEstimatorProvider
    private lateinit var mapper: ProgressMapper

    @Before
    fun setUp() {
        sizeEstimatorProvider = mock()
        getLongAsStringHumanReadableTimeUseCase = mock()
        timeEstimatorProvider = mock()
        progressEstimatorProvider = mock()
        mapper = ProgressMapper(sizeEstimatorProvider, getLongAsStringHumanReadableTimeUseCase, timeEstimatorProvider, progressEstimatorProvider)
    }

    @Test
    fun givenMapInputDataWithPathThenRetrieveIt() = runBlockingTest {
        val bytesRead = 0L
        val totalBytes = 1_000L
        val remainingTime = 6400L
        val remainingTimeString = "1 hour"
        val remainingSize = "1MB"
        val actualProgress = 0
        val expectedResult = RemainingProgressModel(progress = actualProgress, description = "$remainingSize, $remainingTimeString")
        whenever(getLongAsStringHumanReadableTimeUseCase(remainingTime)).thenReturn(remainingTimeString)
        whenever(timeEstimatorProvider.remainingTime(bytesRead, totalBytes)).thenReturn(remainingTime)
        whenever(sizeEstimatorProvider.remainingSize(bytesRead, totalBytes)).thenReturn(remainingSize)
        whenever(progressEstimatorProvider.actualProgress(bytesRead, totalBytes)).thenReturn(actualProgress)

        val progressModel = mapper.mapToProgress(bytesRead, totalBytes)

        assert(expectedResult == progressModel)
    }
}