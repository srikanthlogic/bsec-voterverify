package com.google.firebase.crashlytics;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.analytics.AnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.analytics.BlockingAnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.analytics.BreadcrumbAnalyticsEventReceiver;
import com.google.firebase.crashlytics.internal.analytics.CrashlyticsOriginAnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.analytics.UnavailableAnalyticsEventLogger;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbHandler;
import com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbSource;
import com.google.firebase.crashlytics.internal.breadcrumbs.DisabledBreadcrumbSource;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/* loaded from: classes3.dex */
public class AnalyticsDeferredProxy {
    private final Deferred<AnalyticsConnector> analyticsConnectorDeferred;
    private volatile AnalyticsEventLogger analyticsEventLogger;
    private final List<BreadcrumbHandler> breadcrumbHandlerList;
    private volatile BreadcrumbSource breadcrumbSource;

    public AnalyticsDeferredProxy(Deferred<AnalyticsConnector> analyticsConnectorDeferred) {
        this(analyticsConnectorDeferred, new DisabledBreadcrumbSource(), new UnavailableAnalyticsEventLogger());
    }

    public AnalyticsDeferredProxy(Deferred<AnalyticsConnector> analyticsConnectorDeferred, BreadcrumbSource breadcrumbSource, AnalyticsEventLogger analyticsEventLogger) {
        this.analyticsConnectorDeferred = analyticsConnectorDeferred;
        this.breadcrumbSource = breadcrumbSource;
        this.breadcrumbHandlerList = new ArrayList();
        this.analyticsEventLogger = analyticsEventLogger;
        init();
    }

    public BreadcrumbSource getDeferredBreadcrumbSource() {
        return new BreadcrumbSource() { // from class: com.google.firebase.crashlytics.-$$Lambda$AnalyticsDeferredProxy$XlQJJbDAh2jtrDIs0f3MUS2H3YU
            @Override // com.google.firebase.crashlytics.internal.breadcrumbs.BreadcrumbSource
            public final void registerBreadcrumbHandler(BreadcrumbHandler breadcrumbHandler) {
                AnalyticsDeferredProxy.this.lambda$getDeferredBreadcrumbSource$0$AnalyticsDeferredProxy(breadcrumbHandler);
            }
        };
    }

    public /* synthetic */ void lambda$getDeferredBreadcrumbSource$0$AnalyticsDeferredProxy(BreadcrumbHandler breadcrumbHandler) {
        synchronized (this) {
            if (this.breadcrumbSource instanceof DisabledBreadcrumbSource) {
                this.breadcrumbHandlerList.add(breadcrumbHandler);
            }
            this.breadcrumbSource.registerBreadcrumbHandler(breadcrumbHandler);
        }
    }

    public AnalyticsEventLogger getAnalyticsEventLogger() {
        return new AnalyticsEventLogger() { // from class: com.google.firebase.crashlytics.-$$Lambda$AnalyticsDeferredProxy$1RYJBD5bXWtJsWZhT0xTWH8lCIs
            @Override // com.google.firebase.crashlytics.internal.analytics.AnalyticsEventLogger
            public final void logEvent(String str, Bundle bundle) {
                AnalyticsDeferredProxy.this.lambda$getAnalyticsEventLogger$1$AnalyticsDeferredProxy(str, bundle);
            }
        };
    }

    public /* synthetic */ void lambda$getAnalyticsEventLogger$1$AnalyticsDeferredProxy(String name, Bundle params) {
        this.analyticsEventLogger.logEvent(name, params);
    }

    private void init() {
        this.analyticsConnectorDeferred.whenAvailable(new Deferred.DeferredHandler() { // from class: com.google.firebase.crashlytics.-$$Lambda$AnalyticsDeferredProxy$odiQrirLM4B0uxtU277c4OKYmwY
            @Override // com.google.firebase.inject.Deferred.DeferredHandler
            public final void handle(Provider provider) {
                AnalyticsDeferredProxy.this.lambda$init$2$AnalyticsDeferredProxy(provider);
            }
        });
    }

    public /* synthetic */ void lambda$init$2$AnalyticsDeferredProxy(Provider analyticsConnector) {
        Logger.getLogger().d("AnalyticsConnector now available.");
        AnalyticsConnector connector = (AnalyticsConnector) analyticsConnector.get();
        CrashlyticsOriginAnalyticsEventLogger directAnalyticsEventLogger = new CrashlyticsOriginAnalyticsEventLogger(connector);
        CrashlyticsAnalyticsListener crashlyticsAnalyticsListener = new CrashlyticsAnalyticsListener();
        if (subscribeToAnalyticsEvents(connector, crashlyticsAnalyticsListener) != null) {
            Logger.getLogger().d("Registered Firebase Analytics listener.");
            BreadcrumbAnalyticsEventReceiver breadcrumbReceiver = new BreadcrumbAnalyticsEventReceiver();
            BlockingAnalyticsEventLogger blockingAnalyticsEventLogger = new BlockingAnalyticsEventLogger(directAnalyticsEventLogger, 500, TimeUnit.MILLISECONDS);
            synchronized (this) {
                for (BreadcrumbHandler handler : this.breadcrumbHandlerList) {
                    breadcrumbReceiver.registerBreadcrumbHandler(handler);
                }
                crashlyticsAnalyticsListener.setBreadcrumbEventReceiver(breadcrumbReceiver);
                crashlyticsAnalyticsListener.setCrashlyticsOriginEventReceiver(blockingAnalyticsEventLogger);
                this.breadcrumbSource = breadcrumbReceiver;
                this.analyticsEventLogger = blockingAnalyticsEventLogger;
            }
            return;
        }
        Logger.getLogger().w("Could not register Firebase Analytics listener; a listener is already registered.");
    }

    private static AnalyticsConnector.AnalyticsConnectorHandle subscribeToAnalyticsEvents(AnalyticsConnector analyticsConnector, CrashlyticsAnalyticsListener listener) {
        AnalyticsConnector.AnalyticsConnectorHandle handle = analyticsConnector.registerAnalyticsConnectorListener("clx", listener);
        if (handle == null) {
            Logger.getLogger().d("Could not register AnalyticsConnectorListener with Crashlytics origin.");
            handle = analyticsConnector.registerAnalyticsConnectorListener(AppMeasurement.CRASH_ORIGIN, listener);
            if (handle != null) {
                Logger.getLogger().w("A new version of the Google Analytics for Firebase SDK is now available. For improved performance and compatibility with Crashlytics, please update to the latest version.");
            }
        }
        return handle;
    }
}
