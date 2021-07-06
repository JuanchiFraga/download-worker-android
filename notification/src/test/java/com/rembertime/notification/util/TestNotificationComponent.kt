package com.rembertime.notification.util

import com.nhaarman.mockitokotlin2.mock
import com.rembertime.notification.data.mapper.DownloadFileMapper
import com.rembertime.notification.data.provider.DispatcherProvider
import com.rembertime.notification.data.provider.ExternalFileDirProvider
import com.rembertime.notification.data.provider.FileNameProvider
import com.rembertime.notification.data.provider.RandomProvider
import com.rembertime.notification.data.repository.DownloadFileRepository
import com.rembertime.notification.data.service.DownloadFileApi
import com.rembertime.notification.di.NotificationContainer
import com.rembertime.notification.domain.error.WorkerErrorHandler
import com.rembertime.notification.domain.listener.DownloadProgressListener
import com.rembertime.notification.domain.mapper.NotificationModelMapper
import com.rembertime.notification.domain.mapper.ProgressMapper
import com.rembertime.notification.domain.mapper.estimator.ProgressEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.SizeEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.TimeEstimatorProvider
import com.rembertime.notification.domain.notification.NotificationFactory
import com.rembertime.notification.domain.usecase.DownloadFileUseCase
import com.rembertime.notification.domain.usecase.GetLongAsStringHumanReadableTimeUseCase
import com.rembertime.notification.domain.usecase.GetUniqueFileNameUseCase
import com.rembertime.notification.domain.usecase.SaveInputStreamAsFileOnDownloadDir

internal class TestNotificationComponent : NotificationContainer {

    override lateinit var downloadProgressListener: DownloadProgressListener
    override val downloadFileApi: DownloadFileApi = mock()
    override val downloadFileRepository: DownloadFileRepository = mock()
    override val notificationFactory: NotificationFactory = mock()
    override val notificationModelMapper: NotificationModelMapper = mock()
    override val workerErrorHandler: WorkerErrorHandler = mock()
    override val saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir = mock()
    override val getUniqueFileNameUseCase: GetUniqueFileNameUseCase = mock()
    override val downloadFileUseCase: DownloadFileUseCase = mock()
    override val timeEstimatorProvider: TimeEstimatorProvider = mock()
    override val fileNameProvider: FileNameProvider = mock()
    override val sizeEstimatorProvider: SizeEstimatorProvider = mock()
    override val getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase = mock()
    override val progressEstimatorProvider: ProgressEstimatorProvider = mock()
    override val progressMapper: ProgressMapper = mock()
    override val dispatcherProvider: DispatcherProvider = mock()
    override val externalFileDirProvider: ExternalFileDirProvider = mock()
    override val downloadFileMapper: DownloadFileMapper = mock()
    override val stringProvider: StringProvider = mock()
    override val pluralProvider: PluralProvider = mock()
    override val drawableProvider: DrawableProvider = mock()
    override val randomProvider: RandomProvider = mock()
}