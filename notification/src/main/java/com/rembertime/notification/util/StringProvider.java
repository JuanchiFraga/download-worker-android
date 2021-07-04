package com.rembertime.notification.util;

import android.content.Context;

import androidx.annotation.StringRes;

public class StringProvider {

    private final Context applicationContext;

    public StringProvider(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getString(@StringRes Integer res) {
        return applicationContext.getResources().getString(res);
    }

    public String getString(@StringRes Integer res, Object... formatArgs) {
        return applicationContext.getResources().getString(res, formatArgs);
    }
}
