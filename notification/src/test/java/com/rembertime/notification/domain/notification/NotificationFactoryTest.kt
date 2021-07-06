package com.rembertime.notification.domain.notification

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat.FLAG_ONLY_ALERT_ONCE
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.nhaarman.mockitokotlin2.mock
import com.rembertime.notification.domain.model.WorkNotificationModel
import com.rembertime.notification.util.extensions.createOpenSharePendingIntent
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@Config(sdk = [Build.VERSION_CODES.M])
@RunWith(RobolectricTestRunner::class)
internal class NotificationFactoryTest {

    private lateinit var factory: NotificationFactory
    private lateinit var model: WorkNotificationModel
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        factory = NotificationFactory(context)
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        model = createWorkNotificationModel()
    }

    @Test
    fun givenCreatePendingNotificationThenRetrieveNotificationWithCancelAction() {
        val uuid: UUID = mock()

        val notificationBuilder = factory.createPendingNotification(model, uuid)

        assertEquals(notificationBuilder.build().actions[0].title, "CANCEL")
    }

    @Test(expected = NoSuchMethodError::class)
    fun givenCreateNotificationOnApiLessThanQThenRetrieveNotificationWithoutChannelId() {
        val uuid: UUID = mock()

        val notificationBuilder = factory.createPendingNotification(model, uuid)

        notificationBuilder.build().channelId
    }

    @Test
    fun givenCreateSuccessNotificationThenRetrieveOpenShareIntentNotification() {
        val uri: Uri = mock()
        val notificationBuilder = factory.createSuccessNotification(model, uri)

        assert(notificationBuilder.build().contentIntent == context.createOpenSharePendingIntent(model.fileName, uri))
    }

    @Test
    fun givenCreateSuccessNotificationThenRetrieveOnlyAlertOnceNotification() {
        val uri: Uri = mock()
        val notificationBuilder = factory.createSuccessNotification(model, uri)

        assert(notificationBuilder.build().flags == FLAG_ONLY_ALERT_ONCE)
    }

    @Test
    fun givenCreateErrorNotificationThenRetrieveOnlyAlertOnceNotification() {
        val notificationBuilder = factory.createErrorNotification(model, "error")

        assert(notificationBuilder.build().flags == FLAG_ONLY_ALERT_ONCE)
    }

    private fun createWorkNotificationModel() = WorkNotificationModel().apply {
        filePath = "http://www.domain.com/book.pdf"
        fileName = "book.pdf"
        channelId = "channelId"
        channelName = "Download"
        notificationId = 0
        notificationTitle = "book"
        notificationContent = "content"
        applicationIcon = 1
    }
}