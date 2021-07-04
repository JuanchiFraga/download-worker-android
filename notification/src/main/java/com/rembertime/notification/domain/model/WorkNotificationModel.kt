package com.rembertime.notification.domain.model

import android.graphics.Bitmap

internal class WorkNotificationModel {

    lateinit var filePath: String
    lateinit var fileName: String
    lateinit var channelId: String
    lateinit var channelName: String
    var notificationId: Int = -1
    lateinit var notificationTitle: String
    lateinit var notificationContent: String
    var applicationIcon: Int = -1
    var notificationIcon: Bitmap? = null
}