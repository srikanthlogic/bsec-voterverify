package com.google.firebase.crashlytics.internal.common;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.app.NotificationCompat;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.internal.Logger;
/* loaded from: classes3.dex */
class BatteryState {
    static final int VELOCITY_CHARGING = 2;
    static final int VELOCITY_FULL = 3;
    static final int VELOCITY_UNPLUGGED = 1;
    private final Float level;
    private final boolean powerConnected;

    private BatteryState(Float level, boolean powerConnected) {
        this.powerConnected = powerConnected;
        this.level = level;
    }

    boolean isPowerConnected() {
        return this.powerConnected;
    }

    public Float getBatteryLevel() {
        return this.level;
    }

    public int getBatteryVelocity() {
        Float f;
        if (!this.powerConnected || (f = this.level) == null) {
            return 1;
        }
        if (((double) f.floatValue()) < 0.99d) {
            return 2;
        }
        return 3;
    }

    public static BatteryState get(Context context) {
        boolean powerConnected = false;
        Float level = null;
        try {
            Intent batteryStatusIntent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (batteryStatusIntent != null) {
                powerConnected = isPowerConnected(batteryStatusIntent);
                level = getLevel(batteryStatusIntent);
            }
        } catch (IllegalStateException ex) {
            Logger.getLogger().e("An error occurred getting battery state.", ex);
        }
        return new BatteryState(level, powerConnected);
    }

    private static boolean isPowerConnected(Intent batteryStatusIntent) {
        int status = batteryStatusIntent.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
        if (status == -1) {
            return false;
        }
        if (status == 2 || status == 5) {
            return true;
        }
        return false;
    }

    private static Float getLevel(Intent batteryStatusIntent) {
        int level = batteryStatusIntent.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
        int scale = batteryStatusIntent.getIntExtra("scale", -1);
        if (level == -1 || scale == -1) {
            return null;
        }
        return Float.valueOf(((float) level) / ((float) scale));
    }
}
