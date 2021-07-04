package com.rembertime.notification.model

import androidx.work.Data

data class NotificationModel(
    val filePath: String,
    val applicationIcon: Int = -1,
    val customFileName: String? = null,
    val customNotificationTitle: String? = null,
    val notificationIcon: Int = -1,
    val channelName: String = "Downloads",
    val channelId: String = "default_rembertime_channel"
) {

    internal fun getData() = Data.Builder().apply {
        putString(FILE_PATH, filePath)
        customFileName?.let { putString(FILE_NAME, it) }
        putInt(APP_ICON, applicationIcon)
        customNotificationTitle?.let { putString(TITLE, it) }
        putInt(ICON, notificationIcon)
        putString(CHANNEL_NAME, channelName)
        putString(CHANNEL_ID, channelId)
    }.build()

    internal companion object {
        const val FILE_PATH = "FILE_PATH"
        const val FILE_NAME = "FILE_NAME"
        const val APP_ICON = "APP_ICON"
        const val TITLE = "NOTIFICATION_TITLE"
        const val ICON = "NOTIFICATION_ICON"
        const val CHANNEL_NAME = "CHANNEL_NAME"
        const val CHANNEL_ID = "CHANNEL_ID"
    }
}