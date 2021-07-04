package com.rembertime.notification.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class DrawableProvider(
    private val applicationContext: Context
) {

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(applicationContext, drawableRes)
    }
}