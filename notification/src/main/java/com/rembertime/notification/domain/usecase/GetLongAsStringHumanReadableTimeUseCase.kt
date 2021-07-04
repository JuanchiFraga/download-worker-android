package com.rembertime.notification.domain.usecase

import com.rembertime.notification.R
import com.rembertime.notification.util.PluralProvider
import com.rembertime.notification.util.StringProvider
import kotlin.math.abs

internal class GetLongAsStringHumanReadableTimeUseCase(
    private val stringProvider: StringProvider,
    private val pluralProvider: PluralProvider
) {

    @SuppressWarnings("ComplexMethod")
    operator fun invoke(timeInMillis: Long): String {
        val seconds = abs(timeInMillis) / SEC_IN_MILLIS
        val minutes = seconds / MIN_IN_SECS
        val hours = minutes / HOUR_IN_MINES
        val days = hours / DAY_IN_HOURS
        val month = days / MONTH_IN_DAYS
        val years = month / YEAR_IN_MONTH
        return when {
            seconds == 0L -> stringProvider.getString(R.string.empty)
            seconds in 1L until MIN_IN_SECS -> pluralProvider.getPlural(R.plurals.remaining_seconds, seconds.toInt(), seconds.toInt())
            minutes in 1L until HOUR_IN_MINES -> pluralProvider.getPlural(R.plurals.remaining_minutes, minutes.toInt(), minutes.toInt())
            hours in 1L until DAY_IN_HOURS -> pluralProvider.getPlural(R.plurals.remaining_hours, hours.toInt(), hours.toInt())
            days in 1L until MONTH_IN_DAYS -> pluralProvider.getPlural(R.plurals.remaining_days, days.toInt(), days.toInt())
            month in 1L until YEAR_IN_MONTH -> pluralProvider.getPlural(R.plurals.remaining_month, month.toInt(), month.toInt())
            else -> pluralProvider.getPlural(R.plurals.remaining_years, years.toInt(), years.toInt())
        }
    }

    companion object {
        private const val SEC_IN_MILLIS = 1000
        private const val MIN_IN_SECS = 60
        private const val HOUR_IN_MINES = 60
        private const val DAY_IN_HOURS = 24
        private const val MONTH_IN_DAYS = 30
        private const val YEAR_IN_MONTH = 12
    }
}