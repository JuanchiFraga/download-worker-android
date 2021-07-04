package com.rembertime.notification.di

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.rembertime.notification.util.MockDownloadProgressListener
import com.rembertime.notification.util.TestContainer.verifyReturnsDifferentInstance
import com.rembertime.notification.util.TestContainer.verifyReturnsSingleInstance
import com.rembertime.notification.di.NotificationContainerLocator.locateComponent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.M])
@RunWith(RobolectricTestRunner::class)
internal class NotificationContainerTest {

    private lateinit var notificationContainer: NotificationContainer

    @Before
    fun setUp() {
        val applicationContext: Context = ApplicationProvider.getApplicationContext()
        notificationContainer = locateComponent(applicationContext).apply {
            downloadProgressListener = MockDownloadProgressListener()
        }
    }

    @Test
    fun whenDownloadApiIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::downloadFileApi)
    }

    @Test
    fun whenDownloadFileRepositoryIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::downloadFileRepository)
    }

    @Test
    fun whenSaveInputStreamAsFileOnDownloadDirIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::saveInputStreamAsFileOnDownloadDir)
    }

    @Test
    fun whenGetUniqueFileNameUseCaseIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::getUniqueFileNameUseCase)
    }

    @Test
    fun whenDownloadFileUseCaseIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::downloadFileUseCase)
    }

    @Test
    fun whenTimeEstimatorProviderIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::timeEstimatorProvider)
    }

    @Test
    fun whenFileNameProviderIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::fileNameProvider)
    }

    @Test
    fun whenNotificationFactoryIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::notificationFactory)
    }

    @Test
    fun whenNotificationModelMapperIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::notificationModelMapper)
    }

    @Test
    fun whenWorkerErrorHandlerIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::workerErrorHandler)
    }

    @Test
    fun whenProgressEstimatorProviderIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::progressEstimatorProvider)
    }

    @Test
    fun whenProgressMapperIsProvidedThenAlwaysReturnDifferentInstances() {
        verifyReturnsDifferentInstance(notificationContainer::progressMapper)
    }

    @Test
    fun whenDispatcherProviderIsProvidedThenAlwaysReturnSameInstances() {
        verifyReturnsSingleInstance(notificationContainer::dispatcherProvider)
    }

    @Test
    fun whenExternalFileDirProviderIsProvidedThenAlwaysReturnSameInstances() {
        verifyReturnsSingleInstance(notificationContainer::externalFileDirProvider)
    }

    @Test
    fun whenDownloadFileMapperIsProvidedThenAlwaysReturnSameInstances() {
        verifyReturnsSingleInstance(notificationContainer::downloadFileMapper)
    }
}