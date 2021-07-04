package com.rembertime.notification.domain.usecase

import android.net.Uri
import com.rembertime.notification.domain.error.exception.RequestFailException
import com.rembertime.notification.domain.error.exception.SaveInputStreamFileException
import com.rembertime.notification.data.repository.DownloadFileRepository
import kotlinx.coroutines.CancellationException
import java.lang.NullPointerException

/* We run the blocking code in Dispatcher.IO */
@Suppress("BlockingMethodInNonBlockingContext")
internal class DownloadFileUseCase constructor(
    private val downloadFileRepository: DownloadFileRepository,
    private val saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir
) {

    /**
     * Download any type of file and save it on external download folder
     *
     * @param filePath The string path to download like https://speed.hetzner.de/1GB.bin
     * @param fileName the name with which we want to save the file like 1GB.bin
     *
     * @return Content Uri that references our downloaded file
     *
     * @throws RequestFailException Request finish with errors.
     * @throws SaveInputStreamFileException error when trying to save the file on download folder, maybe some permission is missing.
     * @throws NullPointerException storage device is unavailable.
     * @throws CancellationException if download is canceled while the file is being written to the storage.
     */
    suspend operator fun invoke(filePath: String, fileName: String): Uri {
        val downloadFile = downloadFileRepository.downloadFileIn(filePath)
        return saveInputStreamAsFileOnDownloadDir(downloadFile.byteStream(), fileName)
    }

    fun cancel() {
        saveInputStreamAsFileOnDownloadDir.cancel()
    }
}