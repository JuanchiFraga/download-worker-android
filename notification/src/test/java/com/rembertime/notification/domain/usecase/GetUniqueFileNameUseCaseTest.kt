package com.rembertime.notification.domain.usecase

import com.nhaarman.mockitokotlin2.mock
import com.rembertime.notification.data.provider.ExternalFileDirProvider
import com.rembertime.notification.util.createTestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class GetUniqueFileNameUseCaseTest {

    private lateinit var externalFileDirProvider: ExternalFileDirProvider
    private lateinit var getUniqueFileNameUseCase: GetUniqueFileNameUseCase

    @Before
    fun setUp() {
        externalFileDirProvider = mock()
        getUniqueFileNameUseCase = GetUniqueFileNameUseCase(createTestDispatcherProvider(), externalFileDirProvider)
    }

    @Test
    fun givenGetUniqueNameAndAnyFileExistWithThatNameThenRetrieveTheSame() = runBlockingTest {
        val fileName = "book.pdf"

        val uniqueName = getUniqueFileNameUseCase(fileName)

        assert(fileName == uniqueName)
    }
}