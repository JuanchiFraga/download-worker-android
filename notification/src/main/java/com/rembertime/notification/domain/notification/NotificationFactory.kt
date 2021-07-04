package com.rembertime.notification.domain.notification

import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.rembertime.notification.R
import com.rembertime.notification.domain.model.RemainingProgressModel
import com.rembertime.notification.domain.model.WorkNotificationModel
import com.rembertime.notification.util.extensions.createOpenSharePendingIntent
import java.util.UUID

internal class NotificationFactory(private val applicationContext: Context) {

    fun createPendingNotification(model: WorkNotificationModel, workId: UUID): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, model.channelId).apply {
            setContentTitle(model.notificationTitle)
            setContentText(model.notificationContent)
            setSmallIcon(model.applicationIcon)
            model.notificationIcon?.let { setLargeIcon(it) }
            setOnlyAlertOnce(true)
            setOngoing(true)
            setShowWhen(false)
            setChannelId(model.channelId)
            setProgress(TOTAL_PROGRESS, 0, true)
            addAction(
                android.R.drawable.ic_delete,
                applicationContext.getString(R.string.notification_cancel).uppercase(),
                WorkManager.getInstance(applicationContext).createCancelPendingIntent(workId)
            )
        }
    }

    fun createRunningNotification(model: WorkNotificationModel, workId: UUID, remainingModel: RemainingProgressModel): NotificationCompat.Builder {
        return createPendingNotification(model, workId).apply {
            setContentText(remainingModel.description)
            setProgress(TOTAL_PROGRESS, remainingModel.progress, false)
        }
    }

    fun createSuccessNotification(model: WorkNotificationModel, file: Uri): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, model.channelId).apply {
            setContentTitle(model.fileName)
            setContentText(applicationContext.getString(R.string.notification_complete))
            setContentIntent(applicationContext.createOpenSharePendingIntent(model.fileName, file))
            setSmallIcon(model.applicationIcon)
            setChannelId(model.channelId)
            model.notificationIcon?.let { setLargeIcon(it) }
            setOnlyAlertOnce(true)
        }
    }

    fun createErrorNotification(model: WorkNotificationModel, errorMessage: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, model.channelId).apply {
            setContentTitle(applicationContext.getString(R.string.notification_error))
            setContentText(errorMessage)
            setChannelId(model.channelId)
            setSmallIcon(model.applicationIcon)
            model.notificationIcon?.let { setLargeIcon(it) }
            setOnlyAlertOnce(true)
        }
    }

    companion object {
        private const val TOTAL_PROGRESS = 100
    }
}