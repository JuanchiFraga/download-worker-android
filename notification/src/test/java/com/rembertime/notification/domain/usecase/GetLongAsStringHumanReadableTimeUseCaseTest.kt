package com.rembertime.notification.domain.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.R
import com.rembertime.notification.util.PluralProvider
import com.rembertime.notification.util.StringProvider
import org.junit.Before
import org.junit.Test

internal class GetLongAsStringHumanReadableTimeUseCaseTest {

    private lateinit var stringProvider: StringProvider
    private lateinit var pluralProvider: PluralProvider
    private lateinit var getLongAsStringHumanReadableTimeUseCase: GetLongAsStringHumanReadableTimeUseCase

    @Before
    fun setUp() {
        stringProvider = mock()
        pluralProvider = mock()
        getLongAsStringHumanReadableTimeUseCase = GetLongAsStringHumanReadableTimeUseCase(stringProvider, pluralProvider)
        whenever(stringProvider.getString(any())).thenReturn("")
        whenever(pluralProvider.getPlural(any(), any(), any())).thenReturn("")
    }

    @Test
    fun givenTimeIsZeroMillisThenRetrieveEmpty() {
        val timeInMillis = 0L

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(stringProvider).getString(R.string.empty)
    }

    @Test
    fun givenTimeIsZeroSecsThenRetrieveEmpty() {
        val timeInMillis = 999L // 0 sec

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(stringProvider).getString(R.string.empty)
    }

    @Test
    fun givenTimeIsRepresentedInSecsThenRetrieveRemainingTimeAsSecs() {
        val timeInMillis = 1_000L // 1 sec

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_seconds, 1, 1)
    }

    @Test
    fun givenTimeIsRepresentedInMaxSecsThenRetrieveRemainingTimeAsSecs() {
        val timeInMillis = 59_999L // 59 secs

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_seconds, 59, 59)
    }

    @Test
    fun givenTimeIsRepresentedInMinesThenRetrieveRemainingTimeAsMines() {
        val timeInMillis = 60_000L // 1 min

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_minutes, 1, 1)
    }

    @Test
    fun givenTimeIsRepresentedInMaxMinesThenRetrieveRemainingTimeAsMines() {
        val timeInMillis = 3_599_999L // 59 mines - 59 secs

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_minutes, 59, 59)
    }

    @Test
    fun givenTimeIsRepresentedInHoursThenRetrieveRemainingTimeAsHours() {
        val timeInMillis = 3_600_000L // 1 hour

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_hours, 1, 1)
    }

    @Test
    fun givenTimeIsRepresentedInMaxHoursThenRetrieveRemainingTimeAsHours() {
        val timeInMillis = 86_399_999L // 23 hours - 59 mines - 59 secs

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_hours, 23, 23)
    }

    @Test
    fun givenTimeIsRepresentedInDaysThenRetrieveRemainingTimeAsDays() {
        val timeInMillis = 86_400_000L // 1 days

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_days, 1, 1)
    }

    @Test
    fun givenTimeIsRepresentedInMaxDaysThenRetrieveRemainingTimeAsDays() {
        val timeInMillis = 2_591_999_999L // 29 days - 23 hours - 59 mines - 59 secs

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_days, 29, 29)
    }

    @Test
    fun givenTimeIsRepresentedInMonthThenRetrieveRemainingTimeAsMonth() {
        val timeInMillis = 2_592_000_000L // 1 month (30 days)

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_month, 1, 1)
    }

    @Test
    fun givenTimeIsRepresentedInMaxMonthThenRetrieveRemainingTimeAsMonth() {
        val timeInMillis = 31_103_999_999 // 11 month - 29 days - 23 hours - 59 mines - 59 secs

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_month, 11, 11)
    }

    @Test
    fun givenTimeIsRepresentedMoreElevenMonthThenRetrieveRemainingTimeAsYears() {
        val timeInMillis = 31_104_000_000 // 12 month (1 year)

        getLongAsStringHumanReadableTimeUseCase(timeInMillis)

        verify(pluralProvider).getPlural(R.plurals.remaining_years, 1, 1)
    }
}