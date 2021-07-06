package com.rembertime.notification.work

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.ListenableWorker.Result
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.di.NotificationContainerLocator
import com.rembertime.notification.domain.error.WorkerErrorHandler
import com.rembertime.notification.domain.error.exception.SaveInputStreamFileException
import com.rembertime.notification.domain.mapper.NotificationModelMapper
import com.rembertime.notification.domain.mapper.ProgressMapper
import com.rembertime.notification.domain.model.RemainingProgressModel
import com.rembertime.notification.domain.model.WorkNotificationModel
import com.rembertime.notification.domain.notification.NotificationFactory
import com.rembertime.notification.domain.usecase.DownloadFileUseCase
import com.rembertime.notification.util.TestNotificationComponent
import com.rembertime.notification.work.DownloadFileNotificationWorker.Companion.ERROR_MESSAGE
import com.rembertime.notification.work.DownloadFileNotificationWorker.Companion.SUCCESS_DATA
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNotificationManager

@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.M], shadows = [ShadowNotificationManager::class])
@RunWith(RobolectricTestRunner::class)
internal class DownloadFileNotificationWorkerTest {

    private lateinit var downloadFileUseCase: DownloadFileUseCase
    private lateinit var progressMapper: ProgressMapper
    private lateinit var errorHandler: WorkerErrorHandler
    private lateinit var notificationFactory: NotificationFactory
    private lateinit var notificationModelMapper: NotificationModelMapper
    private lateinit var notificationSuccessBuilder: NotificationCompat.Builder
    private var jobWasCancel = false
    private lateinit var context: Context

    @Before
    fun setUp() {
        val testComponent = TestNotificationComponent()
        downloadFileUseCase = testComponent.downloadFileUseCase
        progressMapper = testComponent.progressMapper
        notificationFactory = testComponent.notificationFactory
        notificationModelMapper = testComponent.notificationModelMapper
        errorHandler = testComponent.workerErrorHandler
        context = ApplicationProvider.getApplicationContext()
        notificationSuccessBuilder = mock()
        whenever(notificationSuccessBuilder.build()).thenReturn(mock())
        doAnswer { jobWasCancel = true }.whenever(downloadFileUseCase).cancel()
        NotificationContainerLocator.setComponent(testComponent)
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun givenDownloadSuccessThenRetrieveSuccessResult() = runBlockingTest {
        val worker = TestListenableWorkerBuilder<DownloadFileNotificationWorker>(context).build()
        val workerModel: WorkNotificationModel = mockWorkModel(worker)
        val fileResult: Uri = mock()
        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenReturn(fileResult)
        whenever(notificationFactory.createSuccessNotification(workerModel, fileResult)).thenReturn(notificationSuccessBuilder)

        val result = worker.doWork()

        val expectedResult = Result.success(Data.Builder().putString(SUCCESS_DATA, fileResult.toString()).build())
        assertEquals(result, expectedResult)
    }

    @Test
    fun givenDownloadFailThenRetrieveErrorResult() = runBlockingTest {
        val worker = TestListenableWorkerBuilder<DownloadFileNotificationWorker>(context).build()
        val workerModel: WorkNotificationModel = mockWorkModel(worker)
        val errorMessage = "fail during write"
        val error = SaveInputStreamFileException(RuntimeException(errorMessage))
        whenever(errorHandler.getErrorMessage(error)).thenReturn(errorMessage)
        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenThrow(error)
        whenever(notificationFactory.createErrorNotification(workerModel, errorMessage)).thenReturn(notificationSuccessBuilder)

        val result = worker.doWork()

        val expectedResult = Result.failure(Data.Builder().putString(ERROR_MESSAGE, error.toString()).build())
        assertEquals(result, expectedResult)
    }

    @Test
    fun givenWorkStopThenCancelDownload() = runBlockingTest {
        val worker = TestListenableWorkerBuilder<DownloadFileNotificationWorker>(context).build()
        val workerModel: WorkNotificationModel = mockWorkModel(worker)
        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenReturn(mock())
        whenever(notificationFactory.createErrorNotification(workerModel, "")).thenReturn(notificationSuccessBuilder)

        val workDeferred = async { delay(1_000L); worker.doWork() }
        worker.onStopped()
        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenThrow(CancellationException())
        val result = workDeferred.await()

        val expectedResult = Result.failure(Data.Builder().putString(ERROR_MESSAGE, CancellationException().toString()).build())
        assertEquals(result, expectedResult)
    }

    @Test
    fun givenWorkRunningThenItRunOnForeground() = runBlockingTest {
        val worker = TestListenableWorkerBuilder<DownloadFileNotificationWorker>(context).build()
        val workerModel: WorkNotificationModel = mockWorkModel(worker)
        val byteRead = 0L
        val totalBytes = 10L
        val remainingModel = mock<RemainingProgressModel>()
        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenReturn(mock())
        whenever(progressMapper.mapToProgress(byteRead, totalBytes)).thenReturn(remainingModel)
        whenever(notificationFactory.createErrorNotification(workerModel, "")).thenReturn(notificationSuccessBuilder)

        worker.doWork()
        whenever(notificationFactory.createRunningNotification(workerModel, worker.id, remainingModel)).thenReturn(notificationSuccessBuilder)

        worker.update(byteRead, totalBytes)

        assert(worker.isRunInForeground)
    }

    @Test
    fun givenDownloadFailThenShowErrorNotification() = runBlockingTest {
        val worker = TestListenableWorkerBuilder<DownloadFileNotificationWorker>(context).build()
        val workerModel: WorkNotificationModel = mockWorkModel(worker)
        val errorMessage = "fail during write"
        val error = SaveInputStreamFileException(RuntimeException(errorMessage))

        whenever(downloadFileUseCase(workerModel.filePath, workerModel.fileName)).thenThrow(error)
        whenever(notificationFactory.createErrorNotification(workerModel, errorMessage)).thenReturn(notificationSuccessBuilder)

        val result = worker.doWork()

        val expectedResult = Result.failure(Data.Builder().putString(ERROR_MESSAGE, error.toString()).build())
        assertEquals(result, expectedResult)
    }

    private suspend fun mockWorkModel(worker: DownloadFileNotificationWorker): WorkNotificationModel {
        val workerModel: WorkNotificationModel = mock()
        whenever(notificationModelMapper.mapDataToModel(worker.inputData)).thenReturn(workerModel)
        whenever(notificationFactory.createPendingNotification(workerModel, worker.id)).thenReturn(mock())
        whenever(workerModel.filePath).thenReturn("")
        whenever(workerModel.fileName).thenReturn("")
        whenever(workerModel.channelId).thenReturn("")
        whenever(workerModel.channelName).thenReturn("")
        whenever(workerModel.notificationTitle).thenReturn("")
        whenever(workerModel.notificationContent).thenReturn("")
        whenever(workerModel.notificationId).thenReturn(1)
        return workerModel
    }
}