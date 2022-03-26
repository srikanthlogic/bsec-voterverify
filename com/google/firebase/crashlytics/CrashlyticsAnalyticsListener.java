package com.google.firebase.crashlytics;

import android.os.Bundle;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.crashlytics.internal.Logger;
import com.google.firebase.crashlytics.internal.analytics.AnalyticsEventReceiver;
import java.util.Locale;
/* loaded from: classes3.dex */
class CrashlyticsAnalyticsListener implements AnalyticsConnector.AnalyticsConnectorListener {
    static final String CRASHLYTICS_ORIGIN = "clx";
    static final String EVENT_NAME_KEY = "name";
    static final String EVENT_ORIGIN_KEY = "_o";
    static final String EVENT_PARAMS_KEY = "params";
    private AnalyticsEventReceiver breadcrumbEventReceiver;
    private AnalyticsEventReceiver crashlyticsOriginEventReceiver;

    public void setCrashlyticsOriginEventReceiver(AnalyticsEventReceiver receiver) {
        this.crashlyticsOriginEventReceiver = receiver;
    }

    public void setBreadcrumbEventReceiver(AnalyticsEventReceiver receiver) {
        this.breadcrumbEventReceiver = receiver;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorListener
    public void onMessageTriggered(int id, Bundle extras) {
        String name;
        Logger.getLogger().v(String.format(Locale.US, "Analytics listener received message. ID: %d, Extras: %s", Integer.valueOf(id), extras));
        if (extras != null && (name = extras.getString("name")) != null) {
            Bundle params = extras.getBundle(EVENT_PARAMS_KEY);
            if (params == null) {
                params = new Bundle();
            }
            notifyEventReceivers(name, params);
        }
    }

    private void notifyEventReceivers(String name, Bundle params) {
        AnalyticsEventReceiver receiver;
        if (CRASHLYTICS_ORIGIN.equals(params.getString(EVENT_ORIGIN_KEY))) {
            receiver = this.crashlyticsOriginEventReceiver;
        } else {
            receiver = this.breadcrumbEventReceiver;
        }
        notifyEventReceiver(receiver, name, params);
    }

    private static void notifyEventReceiver(AnalyticsEventReceiver receiver, String name, Bundle params) {
        if (receiver != null) {
            receiver.onEvent(name, params);
        }
    }
}
