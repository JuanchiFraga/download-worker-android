package com.rembertime.notification.data.provider

import org.junit.Before
import org.junit.Test
import kotlin.random.Random

internal class RandomProviderTest {

    private lateinit var randomProvider: RandomProvider

    @Before
    fun setUp() {
        randomProvider = RandomProvider()
    }

    @Test
    fun givenRandomIsProvideThenRetrieveRandomDefaultInstance() {
        assert(randomProvider() == Random.Default)
    }
}