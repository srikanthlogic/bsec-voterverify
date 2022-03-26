package com.google.firebase.crashlytics.internal.common;

import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.settings.SettingsDataProvider;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes3.dex */
class CrashlyticsUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final CrashListener crashListener;
    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final AtomicBoolean isHandlingException = new AtomicBoolean(false);
    private final SettingsDataProvider settingsDataProvider;

    /* loaded from: classes3.dex */
    interface CrashListener {
        void onUncaughtException(SettingsDataProvider settingsDataProvider, Thread thread, Throwable th);
    }

    public CrashlyticsUncaughtExceptionHandler(CrashListener crashListener, SettingsDataProvider settingsProvider, Thread.UncaughtExceptionHandler defaultHandler) {
        this.crashListener = crashListener;
        this.settingsDataProvider = settingsProvider;
        this.defaultHandler = defaultHandler;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.util.concurrent.atomic.AtomicBoolean] */
    /* JADX WARN: Type inference failed for: r1v5 */
    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable ex) {
        String str;
        this.isHandlingException.set(true);
        String str2 = "Completed exception processing. Invoking default exception handler.";
        try {
            try {
                if (thread == null) {
                    Logger.getLogger().e("Could not handle uncaught exception; null thread");
                    str = str2;
                } else if (ex == null) {
                    Logger.getLogger().e("Could not handle uncaught exception; null throwable");
                    str = str2;
                } else {
                    this.crashListener.onUncaughtException(this.settingsDataProvider, thread, ex);
                    str = str2;
                }
            } catch (Exception e) {
                Logger.getLogger().e("An error occurred in the uncaught exception handler", e);
                str = str2;
            }
            Logger.getLogger().d(str);
            this.defaultHandler.uncaughtException(thread, ex);
            str2 = this.isHandlingException;
            str2.set(false);
        } catch (Throwable th) {
            Logger.getLogger().d(str2);
            this.defaultHandler.uncaughtException(thread, ex);
            this.isHandlingException.set(false);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHandlingException() {
        return this.isHandlingException.get();
    }
}
