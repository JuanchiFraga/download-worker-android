package com.rembertime.notification.domain.mapper

import com.rembertime.notification.domain.mapper.estimator.ProgressEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.SizeEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.TimeEstimatorProvider
import com.rembertime.notification.domain.usecase.GetLongAsStringHumanReadableTimeUseCase
import com.rembertime.notification.domain.model.RemainingProgressModel

internal class ProgressMapper(
    private val sizeEstimatorProvider: SizeEstimatorProvider,
    private val getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase,
    private val timeEstimatorProvider: TimeEstimatorProvider,
    private val progressEstimatorProvider: ProgressEstimatorProvider
) {

    fun mapToProgress(bytesRead: Long, totalBytes: Long): RemainingProgressModel {
        val remainingTime = timeEstimatorProvider.remainingTime(bytesRead, totalBytes)
        val remainingSize = sizeEstimatorProvider.remainingSize(bytesRead, totalBytes)
        val actualProgress = progressEstimatorProvider.actualProgress(bytesRead, totalBytes)
        return createRemainingModel(remainingTime, remainingSize, actualProgress)
    }

    private fun createRemainingModel(remainingTime: Long, remainingSize: String, actualProgress: Int) = RemainingProgressModel(
        description = remainingSize + ", " + getLongAsStringHumanReadableTimeUseCase(remainingTime),
        progress = actualProgress
    )
}