package com.google.firebase.analytics.connector.internal;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
/* loaded from: classes3.dex */
public final class zzg implements zza {
    private final AnalyticsConnector.AnalyticsConnectorListener zza;
    private final AppMeasurementSdk zzb;
    private final zzf zzc = new zzf(this);

    public zzg(AppMeasurementSdk appMeasurementSdk, AnalyticsConnector.AnalyticsConnectorListener analyticsConnectorListener) {
        this.zza = analyticsConnectorListener;
        this.zzb = appMeasurementSdk;
        this.zzb.registerOnMeasurementEventListener(this.zzc);
    }

    @Override // com.google.firebase.analytics.connector.internal.zza
    public final AnalyticsConnector.AnalyticsConnectorListener zza() {
        return this.zza;
    }

    @Override // com.google.firebase.analytics.connector.internal.zza
    public final void zzb(Set<String> set) {
    }

    @Override // com.google.firebase.analytics.connector.internal.zza
    public final void zzc() {
    }
}
