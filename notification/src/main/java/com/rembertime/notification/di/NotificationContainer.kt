package com.rembertime.notification.di

import com.rembertime.notification.data.mapper.DownloadFileMapper
import com.rembertime.notification.data.provider.DispatcherProvider
import com.rembertime.notification.data.provider.ExternalFileDirProvider
import com.rembertime.notification.data.provider.FileNameProvider
import com.rembertime.notification.data.provider.RandomProvider
import com.rembertime.notification.domain.mapper.estimator.TimeEstimatorProvider
import com.rembertime.notification.domain.mapper.estimator.SizeEstimatorProvider
import com.rembertime.notification.data.repository.DownloadFileRepository
import com.rembertime.notification.data.service.DownloadFileApi
import com.rembertime.notification.domain.error.WorkerErrorHandler
import com.rembertime.notification.domain.listener.DownloadProgressListener
import com.rembertime.notification.domain.mapper.NotificationModelMapper
import com.rembertime.notification.domain.mapper.ProgressMapper
import com.rembertime.notification.domain.mapper.estimator.ProgressEstimatorProvider
import com.rembertime.notification.domain.notification.NotificationFactory
import com.rembertime.notification.domain.usecase.SaveInputStreamAsFileOnDownloadDir
import com.rembertime.notification.domain.usecase.DownloadFileUseCase
import com.rembertime.notification.domain.usecase.GetLongAsStringHumanReadableTimeUseCase
import com.rembertime.notification.domain.usecase.GetUniqueFileNameUseCase
import com.rembertime.notification.util.DrawableProvider
import com.rembertime.notification.util.PluralProvider
import com.rembertime.notification.util.StringProvider

internal interface NotificationContainer {

    var downloadProgressListener: DownloadProgressListener

    val downloadFileApi: DownloadFileApi

    val downloadFileRepository: DownloadFileRepository

    val notificationFactory: NotificationFactory

    val notificationModelMapper: NotificationModelMapper

    val workerErrorHandler: WorkerErrorHandler

    val saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir

    val getUniqueFileNameUseCase: GetUniqueFileNameUseCase

    val downloadFileUseCase: DownloadFileUseCase

    val timeEstimatorProvider: TimeEstimatorProvider

    val fileNameProvider: FileNameProvider

    val sizeEstimatorProvider: SizeEstimatorProvider

    val getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase

    val progressEstimatorProvider: ProgressEstimatorProvider

    val progressMapper: ProgressMapper

    val dispatcherProvider: DispatcherProvider

    val externalFileDirProvider: ExternalFileDirProvider

    val downloadFileMapper: DownloadFileMapper

    val stringProvider: StringProvider

    val pluralProvider: PluralProvider

    val drawableProvider: DrawableProvider

    val randomProvider: RandomProvider
}