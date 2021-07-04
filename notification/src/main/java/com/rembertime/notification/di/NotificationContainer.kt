package com.rembertime.notification.di

import android.content.Context
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

internal class NotificationContainer(
    private val applicationContext: Context,
    private val notificationModule: NotificationModule = NotificationModule()
) {

    lateinit var downloadProgressListener: DownloadProgressListener

    val downloadFileApi: DownloadFileApi
        get() = getDownloadFileApiInstance()

    val downloadFileRepository: DownloadFileRepository
        get() = getDownloadFileRepositoryInstance()

    val notificationFactory: NotificationFactory
        get() = getNotificationFactoryInstance()

    val notificationModelMapper: NotificationModelMapper
        get() = getNotificationModelMapperInstance()

    val workerErrorHandler: WorkerErrorHandler
        get() = getWorkerErrorHandlerInstance()

    val saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir
        get() = getSaveInputStreamAsFileOnDownloadDirInstance()

    val getUniqueFileNameUseCase: GetUniqueFileNameUseCase
        get() = getGetUniqueFileNameUseCaseInstance()

    val downloadFileUseCase: DownloadFileUseCase
        get() = getDownloadFileUseCaseInstance()

    val timeEstimatorProvider: TimeEstimatorProvider
        get() = getTimeEstimatorProviderInstance()

    val fileNameProvider: FileNameProvider
        get() = getFileNameProviderInstance()

    val sizeEstimatorProvider: SizeEstimatorProvider
        get() = getSizeEstimatorProviderInstance()

    val getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase
        get() = getGetLongAsStringHumanReadableTimeUseCaseInstance()

    val progressEstimatorProvider: ProgressEstimatorProvider
        get() = getProgressEstimatorProviderInstance()

    val progressMapper: ProgressMapper
        get() = getProgressMapperInstance()

    /*/
        Singletons
     */
    val dispatcherProvider: DispatcherProvider by lazy {
        getDispatcherProviderInstance()
    }

    val externalFileDirProvider: ExternalFileDirProvider by lazy {
        getExternalFileDirProviderInstance()
    }

    val downloadFileMapper: DownloadFileMapper by lazy {
        getDownloadFileMapperInstance()
    }

    val stringProvider: StringProvider by lazy {
        getStringProviderInstance()
    }

    val pluralProvider: PluralProvider by lazy {
        getPluralProviderInstance()
    }

    val drawableProvider: DrawableProvider by lazy {
        getDrawableProviderIntance()
    }

    val randomProvider: RandomProvider by lazy {
        getRandomProviderInstance()
    }

    private fun getDispatcherProviderInstance(): DispatcherProvider {
        return notificationModule.provideDispatcherProvider()
    }

    private fun getDownloadFileMapperInstance(): DownloadFileMapper {
        return notificationModule.provideDownloadFileMapper()
    }

    private fun getNotificationFactoryInstance(): NotificationFactory {
        return notificationModule.provideNotificationFactory(applicationContext)
    }

    private fun getNotificationModelMapperInstance(): NotificationModelMapper {
        return notificationModule.provideNotificationModelMapper(
            randomProvider,
            drawableProvider,
            stringProvider,
            dispatcherProvider,
            fileNameProvider,
            getUniqueFileNameUseCase
        )
    }

    private fun getDrawableProviderIntance(): DrawableProvider {
        return notificationModule.provideDrawableProvider(applicationContext)
    }

    private fun getWorkerErrorHandlerInstance(): WorkerErrorHandler {
        return notificationModule.provideWorkerErrorHandler(stringProvider)
    }

    private fun getDownloadFileApiInstance(): DownloadFileApi {
        return notificationModule.provideDownloadFileApi(downloadProgressListener)
    }

    private fun getDownloadFileRepositoryInstance(): DownloadFileRepository {
        return notificationModule.provideDownloadFileRepository(downloadFileApi, downloadFileMapper)
    }

    private fun getSaveInputStreamAsFileOnDownloadDirInstance(): SaveInputStreamAsFileOnDownloadDir {
        return notificationModule.provideSaveInputStreamAsFileOnDownloadDir(dispatcherProvider, externalFileDirProvider, applicationContext)
    }

    private fun getDownloadFileUseCaseInstance(): DownloadFileUseCase {
        return notificationModule.provideDownloadFileUseCase(
            downloadFileRepository, saveInputStreamAsFileOnDownloadDir
        )
    }

    private fun getExternalFileDirProviderInstance(): ExternalFileDirProvider {
        return notificationModule.provideExternalFileDirProvider()
    }

    private fun getGetUniqueFileNameUseCaseInstance(): GetUniqueFileNameUseCase {
        return notificationModule.provideGetUniqueFileNameUseCase(dispatcherProvider, externalFileDirProvider)
    }

    private fun getFileNameProviderInstance(): FileNameProvider {
        return notificationModule.provideFileNameProvider()
    }

    private fun getTimeEstimatorProviderInstance(): TimeEstimatorProvider {
        return notificationModule.provideTimeEstimatorProvider()
    }

    private fun getSizeEstimatorProviderInstance(): SizeEstimatorProvider {
        return notificationModule.provideSizeEstimatorProvider(stringProvider)
    }

    private fun getProgressEstimatorProviderInstance(): ProgressEstimatorProvider {
        return notificationModule.provideProgressEstimatorProvider()
    }

    private fun getProgressMapperInstance(): ProgressMapper {
        return notificationModule.provideProgressMapper(
            getLongAsStringHumanReadableTimeUseCase,
            sizeEstimatorProvider,
            timeEstimatorProvider,
            progressEstimatorProvider
        )
    }

    private fun getRandomProviderInstance(): RandomProvider {
        return notificationModule.provideRandomProvider()
    }

    private fun getStringProviderInstance(): StringProvider {
        return notificationModule.provideStringProvider(applicationContext)
    }

    private fun getPluralProviderInstance(): PluralProvider {
        return notificationModule.providePluralProvider(applicationContext)
    }

    private fun getGetLongAsStringHumanReadableTimeUseCaseInstance(): GetLongAsStringHumanReadableTimeUseCase {
        return notificationModule.provideGetLongAsStringHumanReadableTimeUseCase(stringProvider, pluralProvider)
    }
}