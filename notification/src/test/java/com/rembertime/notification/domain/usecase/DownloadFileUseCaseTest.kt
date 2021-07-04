package com.rembertime.notification.domain.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.data.repository.DownloadFileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
internal class DownloadFileUseCaseTest {

    private lateinit var downloadFileRepository: DownloadFileRepository
    private lateinit var saveInputStreamAsFileOnDownloadDir: SaveInputStreamAsFileOnDownloadDir
    private lateinit var downloadFileUseCase: DownloadFileUseCase

    @Before
    fun setUp() {
        downloadFileRepository = mock()
        saveInputStreamAsFileOnDownloadDir = mock()
        downloadFileUseCase = DownloadFileUseCase(downloadFileRepository, saveInputStreamAsFileOnDownloadDir)
    }

    @Test
    fun givenDownloadThenSaveResultOnDownloadFolder() = runBlockingTest {
        val path = "http://www.domain.com/book.pdf"
        val fileName = "book.pdf"
        val responseBody: ResponseBody = mock()
        whenever(downloadFileRepository.downloadFileIn(path)).thenReturn(responseBody)

        downloadFileUseCase(path, fileName)

        verify(saveInputStreamAsFileOnDownloadDir)(responseBody.byteStream(), fileName)
    }

    @Test
    fun givenCancelThenCancelWriteToDownloadFolder() = runBlockingTest {
        downloadFileUseCase.cancel()

        verify(saveInputStreamAsFileOnDownloadDir).cancel()
    }
}