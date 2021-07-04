package com.rembertime.notification.data.provider

import kotlin.random.Random

internal class RandomProvider {

    operator fun invoke(): Random.Default {
        return Random
    }
}