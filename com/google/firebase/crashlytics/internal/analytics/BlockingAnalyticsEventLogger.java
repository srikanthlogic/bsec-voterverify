package com.google.firebase.crashlytics.internal.analytics;

import android.os.Bundle;
import com.google.firebase.crashlytics.internal.Logger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes3.dex */
public class BlockingAnalyticsEventLogger implements AnalyticsEventReceiver, AnalyticsEventLogger {
    static final String APP_EXCEPTION_EVENT_NAME = "_ae";
    private final CrashlyticsOriginAnalyticsEventLogger baseAnalyticsEventLogger;
    private CountDownLatch eventLatch;
    private final TimeUnit timeUnit;
    private final int timeout;
    private final Object latchLock = new Object();
    private boolean callbackReceived = false;

    public BlockingAnalyticsEventLogger(CrashlyticsOriginAnalyticsEventLogger baseAnalyticsEventLogger, int timeout, TimeUnit timeUnit) {
        this.baseAnalyticsEventLogger = baseAnalyticsEventLogger;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override // com.google.firebase.crashlytics.internal.analytics.AnalyticsEventLogger
    public void logEvent(String name, Bundle params) {
        synchronized (this.latchLock) {
            Logger logger = Logger.getLogger();
            logger.v("Logging event " + name + " to Firebase Analytics with params " + params);
            this.eventLatch = new CountDownLatch(1);
            this.callbackReceived = false;
            this.baseAnalyticsEventLogger.logEvent(name, params);
            Logger.getLogger().v("Awaiting app exception callback from Analytics...");
            try {
                if (this.eventLatch.await((long) this.timeout, this.timeUnit)) {
                    this.callbackReceived = true;
                    Logger.getLogger().v("App exception callback received from Analytics listener.");
                } else {
                    Logger.getLogger().w("Timeout exceeded while awaiting app exception callback from Analytics listener.");
                }
            } catch (InterruptedException e) {
                Logger.getLogger().e("Interrupted while awaiting app exception callback from Analytics listener.");
            }
            this.eventLatch = null;
        }
    }

    @Override // com.google.firebase.crashlytics.internal.analytics.AnalyticsEventReceiver
    public void onEvent(String name, Bundle params) {
        CountDownLatch eventLatch = this.eventLatch;
        if (eventLatch != null && APP_EXCEPTION_EVENT_NAME.equals(name)) {
            eventLatch.countDown();
        }
    }

    boolean isCallbackReceived() {
        return this.callbackReceived;
    }
}
