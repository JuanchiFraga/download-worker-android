package com.rembertime.notification.util.extensions

import android.content.Context
import android.net.Uri
import java.io.File

fun Uri.deleteFile(context: Context) {
    context.contentResolver.delete(this, null, null)
    path?.let {
        val file = File(it)
        file.delete()
        if (file.exists()) {
            file.canonicalFile.delete()
            if (file.exists()) {
                context.deleteFile(file.name)
            }
        }
    }
}