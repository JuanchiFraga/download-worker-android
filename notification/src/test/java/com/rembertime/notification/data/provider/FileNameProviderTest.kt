package com.rembertime.notification.data.provider

import org.junit.Before
import org.junit.Test

internal class FileNameProviderTest {

    private lateinit var fileNameProvider: FileNameProvider

    @Before
    fun setUp() {
        fileNameProvider = FileNameProvider()
    }

    @Test
    fun givenValidPathThenRetrieveFileName() {
        val fileName = "file.txt"
        val validPath = "http://www.domain.com/$fileName"

        val fileNameFound = fileNameProvider.find(validPath)

        assert(fileNameFound == fileName)
    }
}