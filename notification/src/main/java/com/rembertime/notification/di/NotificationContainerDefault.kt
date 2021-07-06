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

internal class NotificationContainerDefault(
    private val applicationContext: Context,
    private val notificationModule: NotificationModule = NotificationModule()
) : NotificationContainer {

    override lateinit var downloadProgressListener: DownloadProgressListener

    override val downloadFileApi: DownloadFileApi
        get() = getDownloadFileApiInstance()

    override val downloadFileRepository: DownloadFileRepository
        get() = getDownloadFileRepositoryInstance()

    override val notificationFactory: NotificationFactory
        get() = getNotificationFactoryInstance()

    override val notificationModelMapper: NotificationModelMapper
        get() = getNotificationModelMapperInstance()

    override val workerErrorHandler: WorkerErrorHandler
        get() = getWorkerErrorHandlerInstance()

    override val saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir
        get() = getSaveInputStreamAsFileOnDownloadDirInstance()

    override val getUniqueFileNameUseCase: GetUniqueFileNameUseCase
        get() = getGetUniqueFileNameUseCaseInstance()

    override val downloadFileUseCase: DownloadFileUseCase
        get() = getDownloadFileUseCaseInstance()

    override val timeEstimatorProvider: TimeEstimatorProvider
        get() = getTimeEstimatorProviderInstance()

    override val fileNameProvider: FileNameProvider
        get() = getFileNameProviderInstance()

    override val sizeEstimatorProvider: SizeEstimatorProvider
        get() = getSizeEstimatorProviderInstance()

    override val getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase
        get() = getGetLongAsStringHumanReadableTimeUseCaseInstance()

    override val progressEstimatorProvider: ProgressEstimatorProvider
        get() = getProgressEstimatorProviderInstance()

    override val progressMapper: ProgressMapper
        get() = getProgressMapperInstance()

    /*/
        Singletons
     */
    override val dispatcherProvider: DispatcherProvider by lazy {
        getDispatcherProviderInstance()
    }

    override val externalFileDirProvider: ExternalFileDirProvider by lazy {
        getExternalFileDirProviderInstance()
    }

    override val downloadFileMapper: DownloadFileMapper by lazy {
        getDownloadFileMapperInstance()
    }

    override val stringProvider: StringProvider by lazy {
        getStringProviderInstance()
    }

    override val pluralProvider: PluralProvider by lazy {
        getPluralProviderInstance()
    }

    override val drawableProvider: DrawableProvider by lazy {
        getDrawableProviderIntance()
    }

    override val randomProvider: RandomProvider by lazy {
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