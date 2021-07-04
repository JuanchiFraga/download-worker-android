package com.rembertime.notification.domain.mapper.estimator

import com.rembertime.notification.R
import com.rembertime.notification.util.StringProvider

internal class SizeEstimatorProvider(private val stringProvider: StringProvider) {

    fun remainingSize(currentBytes: Long, totalBytes: Long): String {
        val totalBytesKB = totalBytes / KB
        val totalBytesMB = totalBytes / MB
        val totalBytesGB = totalBytes / GB
        val totalBytesTB = totalBytes / TB
        val currentBytesKB = currentBytes / KB
        val currentBytesMB = currentBytes / MB
        val currentBytesGB = currentBytes / GB
        val currentBytesTB = currentBytes / TB
        return when {
            totalBytesKB < 1 -> stringProvider.getString(R.string.size_bytes, currentBytes, totalBytes)
            totalBytesKB >= 1 && totalBytesMB < 1 -> stringProvider.getString(R.string.size_kilo_bytes, currentBytesKB.round(), totalBytesKB.round())
            totalBytesMB >= 1 && totalBytesGB < 1 -> stringProvider.getString(R.string.size_mega_bytes, currentBytesMB.round(), totalBytesMB.round())
            totalBytesGB >= 1 && totalBytesTB < 1 -> stringProvider.getString(R.string.size_giga_bytes, currentBytesGB.round(), totalBytesGB.round())
            else -> stringProvider.getString(R.string.size_tera_bytes, currentBytesTB.round(), totalBytesTB.round())
        }
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