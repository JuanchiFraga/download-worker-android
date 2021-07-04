package com.rembertime.notification.domain.mapper

import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.work.Data
import com.rembertime.notification.R
import com.rembertime.notification.data.provider.DispatcherProvider
import com.rembertime.notification.data.provider.FileNameProvider
import com.rembertime.notification.data.provider.RandomProvider
import com.rembertime.notification.domain.usecase.GetUniqueFileNameUseCase
import com.rembertime.notification.model.NotificationModel
import com.rembertime.notification.domain.model.WorkNotificationModel
import com.rembertime.notification.util.DrawableProvider
import com.rembertime.notification.util.StringProvider
import kotlinx.coroutines.withContext
import java.lang.NullPointerException
import kotlin.jvm.Throws

internal class NotificationModelMapper(
    private val randomProvider: RandomProvider,
    private val stringProvider: StringProvider,
    private val drawableProvider: DrawableProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val fileNameProvider: FileNameProvider,
    private val getUniqueFileNameUseCase: GetUniqueFileNameUseCase
) {

    @Throws(NullPointerException::class)
    suspend fun mapDataToModel(inputData: Data) = withContext(dispatcherProvider.io) {
        WorkNotificationModel().apply {
            filePath = inputData.getString(NotificationModel.FILE_PATH)!!
            fileName = getUniqueFileName(inputData, filePath)
            channelId = inputData.getString(NotificationModel.CHANNEL_ID)!!
            channelName = inputData.getString(NotificationModel.CHANNEL_NAME)!!
            notificationId = randomProvider().nextInt()
            notificationTitle = inputData.getString(NotificationModel.TITLE) ?: fileName
            notificationContent = stringProvider.getString(R.string.notification_pending)
            applicationIcon = inputData.getInt(NotificationModel.APP_ICON, -1)
            notificationIcon = getNotificationIconIfCould(inputData)
        }
    }

    private suspend fun getNotificationIconIfCould(inputData: Data): Bitmap? = withContext(dispatcherProvider.computation) {
        val notificationIcon = inputData.getInt(NotificationModel.ICON, -1)
        if (notificationIcon != -1) drawableProvider.getDrawable(notificationIcon)?.toBitmap() else null
    }

    private suspend fun getUniqueFileName(inputData: Data, filePath: String): String {
        val desiredFileName = inputData.getString(NotificationModel.FILE_NAME) ?: fileNameProvider.find(filePath)
        return getUniqueFileNameUseCase(desiredFileName)
    }
}