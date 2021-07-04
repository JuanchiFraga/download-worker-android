package com.rembertime.notification.domain.usecase

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.RELATIVE_PATH
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.provider.MediaStore.MediaColumns.MIME_TYPE
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.rembertime.notification.data.provider.DispatcherProvider
import com.rembertime.notification.data.provider.ExternalFileDirProvider
import com.rembertime.notification.domain.error.exception.NoConnectivityException
import com.rembertime.notification.domain.error.exception.SaveInputStreamFileException
import com.rembertime.notification.util.extensions.deleteFile
import com.rembertime.notification.util.extensions.isStreamClosedError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.IOException
import java.io.File
import javax.net.ssl.SSLException

internal class SaveInputStreamAsFileOnDownloadDir(
    private val dispatcherProvider: DispatcherProvider,
    private val externalFileDirProvider: ExternalFileDirProvider,
    private val applicationContext: Context
) {

    private var outputStream: OutputStream? = null
    private var bufferedInputStream: BufferedInputStream? = null
    private var downloadedUri: Uri? = null

    suspend operator fun invoke(inputStream: InputStream, fileName: String): Uri = try {
        tryToSaveAsFileOnDownloadDirectory(inputStream, fileName)
    } catch (e: Exception) {
        downloadedUri?.deleteFile(applicationContext)
        throw onError(e)
    }

    private suspend fun tryToSaveAsFileOnDownloadDirectory(
        inputStream: InputStream,
        fileName: String
    ): Uri = withContext(dispatcherProvider.io) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getDownloadDirectoryAboveQ(fileName)
        } else {
            getDownloadDirectoryLessQ(fileName)
        }.also { downloadedUri ->
            copyFileOnDownloadDirectory(inputStream, downloadedUri)
        }
    }

    private fun onError(cause: Exception): Exception = when {
        cause is IOException && cause.isStreamClosedError() -> CancellationException(cause.message, cause)
        cause is SSLException -> NoConnectivityException()
        else -> SaveInputStreamFileException(cause)
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun getDownloadDirectoryAboveQ(fileName: String): Uri {
        val contentValues = ContentValues().apply {
            put(DISPLAY_NAME, fileName)
            put(MIME_TYPE, getMimeTypeFromFile(fileName))
            put(RELATIVE_PATH, DIRECTORY_DOWNLOADS)
        }
        return applicationContext.contentResolver.insert(EXTERNAL_CONTENT_URI, contentValues)!!
    }

    private fun getDownloadDirectoryLessQ(fileName: String): Uri {
        val authority = "${applicationContext.packageName}.fileprovider"
        val destinyFile = File(externalFileDirProvider(), fileName)
        return FileProvider.getUriForFile(applicationContext, authority, destinyFile)
    }

    private fun copyFileOnDownloadDirectory(inputStream: InputStream, downloadedUri: Uri) {
        this.downloadedUri = downloadedUri
        applicationContext.contentResolver.openOutputStream(downloadedUri).use { outStream ->
            bufferedInputStream = BufferedInputStream(inputStream)
            outputStream = outStream
            bufferedInputStream?.use { fileOut -> outputStream?.let { fileOut.copyTo(it) } }
            outputStream?.flush()
            bufferedInputStream?.close()
        }
    }

    private fun getMimeTypeFromFile(fileName: String): String {
        val extension: String = fileName.substring(fileName.lastIndexOf(".") + 1)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
    }

    fun cancel() {
        outputStream?.flush()
        bufferedInputStream?.close()
    }
}