package com.rembertime.notification.data.provider

import android.os.Build
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.test.core.app.ApplicationProvider
import com.rembertime.notification.di.NotificationContainerLocator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowEnvironment

@Config(sdk = [Build.VERSION_CODES.M], shadows = [ShadowEnvironment::class])
@RunWith(RobolectricTestRunner::class)
internal class ExternalFileDirProviderTest {

    private lateinit var externalFileDirProvider: ExternalFileDirProvider

    @Before
    fun setUp() {
        externalFileDirProvider = NotificationContainerLocator.locateComponent(ApplicationProvider.getApplicationContext()).externalFileDirProvider
    }

    @Test
    fun givenProvideExternalFileDirThenRetrieveDownloadDir() {
        assertEquals(externalFileDirProvider(), DOWNLOAD_DIR)
    }

    companion object {
        private val DOWNLOAD_DIR = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
    }
}