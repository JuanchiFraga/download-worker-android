package com.rembertime.notification.util;

import android.content.Context;
import androidx.annotation.PluralsRes;

public class PluralProvider {

    private final Context applicationContext;

    public PluralProvider(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getPlural(@PluralsRes int res, int quantity) {
        return applicationContext.getResources().getQuantityString(res, quantity);
    }

    public String getPlural(@PluralsRes int res, int quantity, Object... formatArgs) {
        return applicationContext.getResources().getQuantityString(res, quantity, formatArgs);
    }
}
