package com.rembertime.notification.data.provider

import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File
import java.lang.NullPointerException

internal class ExternalFileDirProvider {

    /**
     * Provide external storage
     *
     * @throws NullPointerException if a storage device is unavailable.
     */
    operator fun invoke(): File = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
}