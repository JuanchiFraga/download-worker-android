package com.rembertime.notification.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import com.rembertime.notification.di.NotificationContainerLocator.locateComponent
import com.rembertime.notification.domain.error.WorkerErrorHandler
import com.rembertime.notification.domain.listener.DownloadProgressListener
import com.rembertime.notification.domain.mapper.NotificationModelMapper
import com.rembertime.notification.domain.mapper.ProgressMapper
import com.rembertime.notification.domain.notification.NotificationFactory
import com.rembertime.notification.domain.usecase.DownloadFileUseCase
import com.rembertime.notification.model.NotificationModel
import com.rembertime.notification.domain.model.RemainingProgressModel
import com.rembertime.notification.domain.model.WorkNotificationModel
import com.rembertime.notification.util.CoroutineWorker
import java.util.UUID

class DownloadFileNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), DownloadProgressListener {

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var model: WorkNotificationModel

    private val downloadFileUseCase: DownloadFileUseCase
    private val progressMapper: ProgressMapper
    private val errorHandler: WorkerErrorHandler
    private val notificationFactory: NotificationFactory
    private var notificationManager: NotificationManagerCompat
    private var notificationModelMapper: NotificationModelMapper
    private var lastElapsedTime: Long = 0L

    init {
        val locator = locateComponent(appContext).apply {
            downloadProgressListener = this@DownloadFileNotificationWorker
        }
        downloadFileUseCase = locator.downloadFileUseCase
        progressMapper = locator.progressMapper
        errorHandler = locator.workerErrorHandler
        notificationFactory = locator.notificationFactory
        notificationModelMapper = locator.notificationModelMapper
        notificationManager = NotificationManagerCompat.from(appContext)
    }

    override suspend fun doWork(): Result = try {
        onPrepared()
        val file = downloadFileUseCase(model.filePath, model.fileName)
        onSuccess(file)
    } catch (e: Exception) {
        onFailed(e)
    }

    override fun onStopped() {
        super.onStopped()
        downloadFileUseCase.cancel()
    }

    override fun update(bytesRead: Long, totalBytes: Long) = synchronized(this) {
        if (lastElapsedTime + MIN_REFRESH_TIME_BETWEEN_CALLS <= System.currentTimeMillis()) {
            lastElapsedTime = System.currentTimeMillis()
            onRunning(progressMapper.mapToProgress(bytesRead, totalBytes))
        }
    }

    private suspend fun onPrepared() {
        model = notificationModelMapper.mapDataToModel(inputData)
        notificationBuilder = notificationFactory.createPendingNotification(model, id)
        createChannelIfShould()
        setForeground(ForegroundInfo(model.notificationId, notificationBuilder.build()))
    }

    private fun createChannelIfShould() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(model.channelId, model.channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun onRunning(remainingProgressModel: RemainingProgressModel) {
        notificationBuilder = notificationFactory.createRunningNotification(model, id, remainingProgressModel)
        setForegroundAsync(ForegroundInfo(model.notificationId, notificationBuilder.build()))
    }

    private fun onSuccess(file: Uri): Result {
        notificationBuilder = notificationFactory.createSuccessNotification(model, file)
        notificationManager.notify(model.notificationId.inc(), notificationBuilder.build())
        return Result.success(Data.Builder().putString(SUCCESS_DATA, file.toString()).build())
    }

    private fun onFailed(cause: Exception): Result {
        errorHandler.getErrorMessage(cause)?.let {
            notificationBuilder = notificationFactory.createErrorNotification(model, it)
            notificationManager.notify(model.notificationId.inc(), notificationBuilder.build())
        }
        return Result.failure(Data.Builder().putString(ERROR_MESSAGE, cause.toString()).build())
    }

    companion object {

        private const val MIN_REFRESH_TIME_BETWEEN_CALLS = 1_000L

        const val SUCCESS_DATA = "SUCCESS_DATA"
        const val ERROR_MESSAGE = "ERROR_MESSAGE"

        fun enqueue(context: Context, notificationModel: NotificationModel): UUID {
            val uploadWork = OneTimeWorkRequestBuilder<DownloadFileNotificationWorker>()
                .setInputData(notificationModel.getData())
                .build()
            WorkManager.getInstance(context).enqueue(uploadWork)
            return uploadWork.id
        }
    }
}