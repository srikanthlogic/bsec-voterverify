package com.google.firebase.analytics.connector;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzee;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.analytics.connector.internal.zza;
import com.google.firebase.analytics.connector.internal.zzc;
import com.google.firebase.analytics.connector.internal.zze;
import com.google.firebase.analytics.connector.internal.zzg;
import com.google.firebase.events.Event;
import com.google.firebase.events.Subscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
/* loaded from: classes3.dex */
public class AnalyticsConnectorImpl implements AnalyticsConnector {
    private static volatile AnalyticsConnector zzc;
    final AppMeasurementSdk zza;
    final Map<String, zza> zzb = new ConcurrentHashMap();

    AnalyticsConnectorImpl(AppMeasurementSdk appMeasurementSdk) {
        Preconditions.checkNotNull(appMeasurementSdk);
        this.zza = appMeasurementSdk;
    }

    public static AnalyticsConnector getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    public static /* synthetic */ void zza(Event event) {
        boolean z = ((DataCollectionDefaultChange) event.getPayload()).enabled;
        synchronized (AnalyticsConnectorImpl.class) {
            ((AnalyticsConnectorImpl) Preconditions.checkNotNull(zzc)).zza.zza(z);
        }
    }

    public final boolean zzc(String str) {
        return !str.isEmpty() && this.zzb.containsKey(str) && this.zzb.get(str) != null;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public void clearConditionalUserProperty(String userPropertyName, String clearEventName, Bundle clearEventParams) {
        if (clearEventName == null || zzc.zzj(clearEventName, clearEventParams)) {
            this.zza.clearConditionalUserProperty(userPropertyName, clearEventName, clearEventParams);
        }
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public List<AnalyticsConnector.ConditionalUserProperty> getConditionalUserProperties(String origin, String propertyNamePrefix) {
        ArrayList arrayList = new ArrayList();
        for (Bundle bundle : this.zza.getConditionalUserProperties(origin, propertyNamePrefix)) {
            arrayList.add(zzc.zzb(bundle));
        }
        return arrayList;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public int getMaxUserProperties(String origin) {
        return this.zza.getMaxUserProperties(origin);
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public Map<String, Object> getUserProperties(boolean includeInternal) {
        return this.zza.getUserProperties(null, null, includeInternal);
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public void logEvent(String origin, String name, Bundle params) {
        if (params == null) {
            params = new Bundle();
        }
        if (zzc.zzl(origin) && zzc.zzj(name, params) && zzc.zzh(origin, name, params)) {
            zzc.zze(origin, name, params);
            this.zza.logEvent(origin, name, params);
        }
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public AnalyticsConnector.AnalyticsConnectorHandle registerAnalyticsConnectorListener(final String origin, AnalyticsConnector.AnalyticsConnectorListener listener) {
        zza zza;
        Preconditions.checkNotNull(listener);
        if (!zzc.zzl(origin) || zzc(origin)) {
            return null;
        }
        AppMeasurementSdk appMeasurementSdk = this.zza;
        if (AppMeasurement.FIAM_ORIGIN.equals(origin)) {
            zza = new zze(appMeasurementSdk, listener);
        } else {
            zza = (AppMeasurement.CRASH_ORIGIN.equals(origin) || "clx".equals(origin)) ? new zzg(appMeasurementSdk, listener) : null;
        }
        if (zza == null) {
            return null;
        }
        this.zzb.put(origin, zza);
        return new AnalyticsConnector.AnalyticsConnectorHandle() { // from class: com.google.firebase.analytics.connector.AnalyticsConnectorImpl.1
            @Override // com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorHandle
            public void registerEventNames(Set<String> set) {
                if (AnalyticsConnectorImpl.this.zzc(origin) && origin.equals(AppMeasurement.FIAM_ORIGIN) && set != null && !set.isEmpty()) {
                    AnalyticsConnectorImpl.this.zzb.get(origin).zzb(set);
                }
            }

            @Override // com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorHandle
            public final void unregister() {
                if (AnalyticsConnectorImpl.this.zzc(origin)) {
                    AnalyticsConnector.AnalyticsConnectorListener zza2 = AnalyticsConnectorImpl.this.zzb.get(origin).zza();
                    if (zza2 != null) {
                        zza2.onMessageTriggered(0, null);
                    }
                    AnalyticsConnectorImpl.this.zzb.remove(origin);
                }
            }

            @Override // com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorHandle
            public void unregisterEventNames() {
                if (AnalyticsConnectorImpl.this.zzc(origin) && origin.equals(AppMeasurement.FIAM_ORIGIN)) {
                    AnalyticsConnectorImpl.this.zzb.get(origin).zzc();
                }
            }
        };
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public void setConditionalUserProperty(AnalyticsConnector.ConditionalUserProperty conditionalUserProperty) {
        if (zzc.zzi(conditionalUserProperty)) {
            this.zza.setConditionalUserProperty(zzc.zza(conditionalUserProperty));
        }
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    public void setUserProperty(String origin, String name, Object value) {
        if (zzc.zzl(origin) && zzc.zzm(origin, name)) {
            this.zza.setUserProperty(origin, name, value);
        }
    }

    public static AnalyticsConnector getInstance(FirebaseApp app) {
        return (AnalyticsConnector) app.get(AnalyticsConnector.class);
    }

    public static AnalyticsConnector getInstance(FirebaseApp app, Context context, Subscriber subscriber) {
        Preconditions.checkNotNull(app);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(subscriber);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzc == null) {
            synchronized (AnalyticsConnectorImpl.class) {
                if (zzc == null) {
                    Bundle bundle = new Bundle(1);
                    if (app.isDefaultApp()) {
                        subscriber.subscribe(DataCollectionDefaultChange.class, zzb.zza, zza.zza);
                        bundle.putBoolean("dataCollectionDefaultEnabled", app.isDataCollectionDefaultEnabled());
                    }
                    zzc = new AnalyticsConnectorImpl(zzee.zzg(context, null, null, null, bundle).zzd());
                }
            }
        }
        return zzc;
    }
}
