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
import com.rembertime.notification.data.service.interceptor.DownloadInterceptor
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@SuppressWarnings("TooManyFunctions")
internal class NotificationModule {

    fun provideDownloadFileApi(downloadProgressListener: DownloadProgressListener): DownloadFileApi {
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS })
            addNetworkInterceptor(DownloadInterceptor(downloadProgressListener))
            retryOnConnectionFailure(true)
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        }
        return Retrofit.Builder()
            .baseUrl("http://localhost/") /* We use dynamic URL (@Url) the base URL will be ignored */
            .client(httpClient.build())
            .build()
            .create(DownloadFileApi::class.java)
    }

    fun provideDownloadFileMapper(): DownloadFileMapper {
        return DownloadFileMapper()
    }

    fun provideNotificationFactory(applicationContext: Context): NotificationFactory {
        return NotificationFactory(applicationContext)
    }

    fun provideNotificationModelMapper(
        randomProvider: RandomProvider,
        drawableProvider: DrawableProvider,
        stringProvider: StringProvider,
        dispatcherProvider: DispatcherProvider,
        fileNameProvider: FileNameProvider,
        getUniqueFileNameUseCase: GetUniqueFileNameUseCase
    ): NotificationModelMapper {
        return NotificationModelMapper(
            randomProvider,
            stringProvider,
            drawableProvider,
            dispatcherProvider,
            fileNameProvider,
            getUniqueFileNameUseCase
        )
    }

    fun provideRandomProvider(): RandomProvider {
        return RandomProvider()
    }

    fun provideDrawableProvider(applicationContext: Context): DrawableProvider {
        return DrawableProvider(applicationContext)
    }

    fun provideDownloadFileRepository(service: DownloadFileApi, mapper: DownloadFileMapper): DownloadFileRepository {
        return DownloadFileRepository(service, mapper)
    }

    fun provideSaveInputStreamAsFileOnDownloadDir(
        dispatcherProvider: DispatcherProvider,
        externalFileDirProvider: ExternalFileDirProvider,
        applicationContext: Context
    ): SaveInputStreamAsFileOnDownloadDir {
        return SaveInputStreamAsFileOnDownloadDir(dispatcherProvider, externalFileDirProvider, applicationContext)
    }

    fun provideDownloadFileUseCase(
        downloadFileRepository: DownloadFileRepository,
        saveInputStreamAsFileOnDownloadDirUseCase: SaveInputStreamAsFileOnDownloadDir
    ): DownloadFileUseCase {
        return DownloadFileUseCase(downloadFileRepository, saveInputStreamAsFileOnDownloadDirUseCase)
    }

    fun provideWorkerErrorHandler(stringProvider: StringProvider): WorkerErrorHandler {
        return WorkerErrorHandler(stringProvider)
    }

    fun provideFileNameProvider(): FileNameProvider {
        return FileNameProvider()
    }

    fun provideTimeEstimatorProvider(): TimeEstimatorProvider {
        return TimeEstimatorProvider()
    }

    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProvider()
    }

    fun provideGetUniqueFileNameUseCase(
        dispatcherProvider: DispatcherProvider,
        externalFileDirProvider: ExternalFileDirProvider
    ): GetUniqueFileNameUseCase {
        return GetUniqueFileNameUseCase(dispatcherProvider, externalFileDirProvider)
    }

    fun provideExternalFileDirProvider(): ExternalFileDirProvider {
        return ExternalFileDirProvider()
    }

    fun provideSizeEstimatorProvider(stringProvider: StringProvider): SizeEstimatorProvider {
        return SizeEstimatorProvider(stringProvider)
    }

    fun provideProgressEstimatorProvider(): ProgressEstimatorProvider {
        return ProgressEstimatorProvider()
    }

    fun provideProgressMapper(
        getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase,
        sizeEstimatorProvider: SizeEstimatorProvider,
        timeEstimatorProvider: TimeEstimatorProvider,
        progressEstimatorProvider: ProgressEstimatorProvider
    ): ProgressMapper {
        return ProgressMapper(sizeEstimatorProvider, getLongAsStringHumanReadableTimeUseCase, timeEstimatorProvider, progressEstimatorProvider)
    }

    fun provideStringProvider(applicationContext: Context): StringProvider {
        return StringProvider(applicationContext)
    }

    fun providePluralProvider(applicationContext: Context): PluralProvider {
        return PluralProvider(applicationContext)
    }

    fun provideGetLongAsStringHumanReadableTimeUseCase(
        stringProvider: StringProvider,
        pluralProvider: PluralProvider
    ): GetLongAsStringHumanReadableTimeUseCase {
        return GetLongAsStringHumanReadableTimeUseCase(stringProvider, pluralProvider)
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 20L
    }
}