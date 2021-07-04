package com.rembertime.notification.domain.estimator

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.R
import com.rembertime.notification.domain.mapper.estimator.SizeEstimatorProvider
import com.rembertime.notification.util.StringProvider
import org.junit.Before
import org.junit.Test

internal class SizeEstimatorProviderTest {

    private lateinit var stringProvider: StringProvider
    private lateinit var sizeEstimatorProvider: SizeEstimatorProvider

    @Before
    fun setUp() {
        stringProvider = mock()
        sizeEstimatorProvider = SizeEstimatorProvider(stringProvider)
    }

    @Test
    fun givenMaxTotalInBytesThenShowAsBytes() {
        val totalBytes = 999L
        whenever(stringProvider.getString(R.string.size_bytes, 0L, totalBytes)).thenReturn("bytes")

        val remainingSize = sizeEstimatorProvider.remainingSize(0L, totalBytes)

        assert(remainingSize == "bytes")
    }

    @Test
    fun givenMaxTotalInKiloBytesThenShowAsBytes() {
        val totalBytes = 1_000L
        whenever(stringProvider.getString(R.string.size_kilo_bytes, 0.0, (totalBytes / KB).round())).thenReturn("KB")

        val remainingSize = sizeEstimatorProvider.remainingSize(0L, totalBytes)

        assert(remainingSize == "KB")
    }

    @Test
    fun givenMaxTotalInMegaBytesThenShowAsBytes() {
        val totalBytes = 1_000_000L
        whenever(stringProvider.getString(R.string.size_mega_bytes, 0.0, (totalBytes / MB).round())).thenReturn("MB")

        val remainingSize = sizeEstimatorProvider.remainingSize(0L, totalBytes)

        assert(remainingSize == "MB")
    }

    @Test
    fun givenMaxTotalInGigaBytesThenShowAsBytes() {
        val totalBytes = 1_000_000_000L
        whenever(stringProvider.getString(R.string.size_giga_bytes, 0.0, (totalBytes / GB).round())).thenReturn("GB")

        val remainingSize = sizeEstimatorProvider.remainingSize(0L, totalBytes)

        assert(remainingSize == "GB")
    }

    @Test
    fun givenMaxTotalInTeraBytesThenShowAsBytes() {
        val totalBytes = 1_000_000_000_000L
        whenever(stringProvider.getString(R.string.size_tera_bytes, 0.0, (totalBytes / TB).round())).thenReturn("TB")

        val remainingSize = sizeEstimatorProvider.remainingSize(0L, totalBytes)

        assert(remainingSize == "TB")
    }

    private fun Double.round(): Double {
        return String.format("%.1f", this).toDouble()
    }

    companion object {
        private const val KB = 1000.0
        private const val MB = KB * 1000.0
        private const val GB = MB * 1000.0
        private const val TB = GB * 1000.0
    }
}