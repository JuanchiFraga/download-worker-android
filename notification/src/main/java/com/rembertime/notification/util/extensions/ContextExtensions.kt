package com.rembertime.notification.util.extensions

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri

internal fun Context.createOpenSharePendingIntent(title: String, file: Uri): PendingIntent {
    var intent = createOpenFileIntent(title, file)
    if (anyCanResolveIntent(intent)) {
        intent = createShareFileIntent(file)
    }
    return PendingIntent.getActivity(this, 0, Intent.createChooser(intent, title), FLAG_CANCEL_CURRENT)
}

@SuppressWarnings("QueryPermissionsNeeded")
private fun Context.anyCanResolveIntent(intent: Intent): Boolean {
    return intent.resolveActivity(packageManager) == null
}

private fun Context.createShareFileIntent(file: Uri): Intent {
    return Intent().apply {
        action = Intent.ACTION_SEND
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        type = contentResolver.getType(file)
        putExtra(Intent.EXTRA_STREAM, file)
    }
}

private fun Context.createOpenFileIntent(fileName: String, file: Uri): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(file, contentResolver.getType(file))
        clipData = ClipData(fileName, arrayOf(contentResolver.getType(file)), ClipData.Item(file))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}