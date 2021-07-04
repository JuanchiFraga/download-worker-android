package com.rembertime.notification.domain.usecase

import com.rembertime.notification.data.provider.DispatcherProvider
import com.rembertime.notification.data.provider.ExternalFileDirProvider
import com.rembertime.notification.util.extensions.catchAndGet
import kotlinx.coroutines.withContext
import java.io.File

internal class GetUniqueFileNameUseCase(
    private val dispatcherProvider: DispatcherProvider,
    private val externalFileDirProvider: ExternalFileDirProvider
) {

    suspend operator fun invoke(fileName: String): String = withContext(dispatcherProvider.computation) {
        var count = 0
        var uniqueFileName = fileName
        while (fileExist(uniqueFileName)) {
            count++
            uniqueFileName = fileName.replace(".", "($count).")
        }
        uniqueFileName
    }

    private suspend fun fileExist(fileName: String): Boolean = withContext(dispatcherProvider.io) {
        { File(externalFileDirProvider(), fileName).exists() }.catchAndGet(false)
    }
}