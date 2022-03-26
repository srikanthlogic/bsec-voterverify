package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzcl;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.measurement.internal.zzfs;
import com.google.android.gms.measurement.internal.zzgo;
import com.google.android.gms.measurement.internal.zzgs;
import com.google.android.gms.measurement.internal.zzgt;
import com.google.android.gms.measurement.internal.zzhw;
import com.google.android.gms.measurement.internal.zzib;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
@Deprecated
/* loaded from: classes.dex */
public class AppMeasurement {
    public static final String CRASH_ORIGIN = "crash";
    public static final String FCM_ORIGIN = "fcm";
    public static final String FIAM_ORIGIN = "fiam";
    private static volatile AppMeasurement zza;
    private final zzd zzb;

    /* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
    /* loaded from: classes.dex */
    public interface EventInterceptor extends zzgs {
        @Override // com.google.android.gms.measurement.internal.zzgs
        void interceptEvent(String str, String str2, Bundle bundle, long j);
    }

    /* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
    /* loaded from: classes.dex */
    public interface OnEventListener extends zzgt {
        @Override // com.google.android.gms.measurement.internal.zzgt
        void onEvent(String str, String str2, Bundle bundle, long j);
    }

    public AppMeasurement(zzfs zzfs) {
        this.zzb = new zza(zzfs);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0038 A[Catch: all -> 0x005d, TRY_ENTER, TryCatch #1 {, blocks: (B:6:0x0007, B:9:0x000c, B:11:0x0013, B:16:0x0038, B:17:0x0040, B:18:0x005b), top: B:27:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0040 A[Catch: all -> 0x005d, TryCatch #1 {, blocks: (B:6:0x0007, B:9:0x000c, B:11:0x0013, B:16:0x0038, B:17:0x0040, B:18:0x005b), top: B:27:0x0007 }] */
    @Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static AppMeasurement getInstance(Context context) {
        zzhw zzhw;
        if (zza == null) {
            synchronized (AppMeasurement.class) {
                if (zza == null) {
                    try {
                        zzhw = (zzhw) Class.forName("com.google.firebase.analytics.FirebaseAnalytics").getDeclaredMethod("getScionFrontendApiImplementation", Context.class, Bundle.class).invoke(null, context, null);
                    } catch (Exception e) {
                        zzhw = null;
                        if (zzhw == null) {
                        }
                        return zza;
                    }
                    if (zzhw == null) {
                        zza = new AppMeasurement(zzhw);
                    } else {
                        zza = new AppMeasurement(zzfs.zzp(context, new zzcl(0, 0, true, null, null, null, null, null), null));
                    }
                }
            }
        }
        return zza;
    }

    public void beginAdUnitExposure(String adUnitId) {
        this.zzb.zzp(adUnitId);
    }

    public void clearConditionalUserProperty(String userPropertyName, String clearEventName, Bundle clearEventParams) {
        this.zzb.zzq(userPropertyName, clearEventName, clearEventParams);
    }

    public void endAdUnitExposure(String adUnitId) {
        this.zzb.zzr(adUnitId);
    }

    public long generateEventId() {
        return this.zzb.zzb();
    }

    public String getAppInstanceId() {
        return this.zzb.zzh();
    }

    public Boolean getBoolean() {
        return this.zzb.zzc();
    }

    public List<ConditionalUserProperty> getConditionalUserProperties(String origin, String propertyNamePrefix) {
        int i;
        List<Bundle> zzm = this.zzb.zzm(origin, propertyNamePrefix);
        if (zzm == null) {
            i = 0;
        } else {
            i = zzm.size();
        }
        ArrayList arrayList = new ArrayList(i);
        for (Bundle bundle : zzm) {
            arrayList.add(new ConditionalUserProperty(bundle));
        }
        return arrayList;
    }

    public String getCurrentScreenClass() {
        return this.zzb.zzi();
    }

    public String getCurrentScreenName() {
        return this.zzb.zzj();
    }

    public Double getDouble() {
        return this.zzb.zzd();
    }

    public String getGmpAppId() {
        return this.zzb.zzk();
    }

    public Integer getInteger() {
        return this.zzb.zze();
    }

    public Long getLong() {
        return this.zzb.zzf();
    }

    public int getMaxUserProperties(String origin) {
        return this.zzb.zza(origin);
    }

    public String getString() {
        return this.zzb.zzl();
    }

    protected Map<String, Object> getUserProperties(String origin, String propertyNamePrefix, boolean includeInternal) {
        return this.zzb.zzo(origin, propertyNamePrefix, includeInternal);
    }

    public void logEventInternal(String origin, String name, Bundle params) {
        this.zzb.zzs(origin, name, params);
    }

    public void logEventInternalNoInterceptor(String origin, String name, Bundle params, long timestampInMillis) {
        this.zzb.zzt(origin, name, params, timestampInMillis);
    }

    public void registerOnMeasurementEventListener(OnEventListener listener) {
        this.zzb.zzu(listener);
    }

    public void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        zzd zzd = this.zzb;
        Bundle bundle = new Bundle();
        String str = conditionalUserProperty.mAppId;
        if (str != null) {
            bundle.putString("app_id", str);
        }
        String str2 = conditionalUserProperty.mOrigin;
        if (str2 != null) {
            bundle.putString("origin", str2);
        }
        String str3 = conditionalUserProperty.mName;
        if (str3 != null) {
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.NAME, str3);
        }
        Object obj = conditionalUserProperty.mValue;
        if (obj != null) {
            zzgo.zzb(bundle, obj);
        }
        String str4 = conditionalUserProperty.mTriggerEventName;
        if (str4 != null) {
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, str4);
        }
        bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, conditionalUserProperty.mTriggerTimeout);
        String str5 = conditionalUserProperty.mTimedOutEventName;
        if (str5 != null) {
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, str5);
        }
        Bundle bundle2 = conditionalUserProperty.mTimedOutEventParams;
        if (bundle2 != null) {
            bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, bundle2);
        }
        String str6 = conditionalUserProperty.mTriggeredEventName;
        if (str6 != null) {
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, str6);
        }
        Bundle bundle3 = conditionalUserProperty.mTriggeredEventParams;
        if (bundle3 != null) {
            bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, bundle3);
        }
        bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, conditionalUserProperty.mTimeToLive);
        String str7 = conditionalUserProperty.mExpiredEventName;
        if (str7 != null) {
            bundle.putString(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, str7);
        }
        Bundle bundle4 = conditionalUserProperty.mExpiredEventParams;
        if (bundle4 != null) {
            bundle.putBundle(AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, bundle4);
        }
        bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, conditionalUserProperty.mCreationTimestamp);
        bundle.putBoolean(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, conditionalUserProperty.mActive);
        bundle.putLong(AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, conditionalUserProperty.mTriggeredTimestamp);
        zzd.zzv(bundle);
    }

    public void setEventInterceptor(EventInterceptor interceptor) {
        this.zzb.zzw(interceptor);
    }

    public void unregisterOnMeasurementEventListener(OnEventListener listener) {
        this.zzb.zzx(listener);
    }

    public AppMeasurement(zzhw zzhw) {
        this.zzb = new zzb(zzhw);
    }

    public Map<String, Object> getUserProperties(boolean includeInternal) {
        return this.zzb.zzn(includeInternal);
    }

    /* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
    /* loaded from: classes.dex */
    public static class ConditionalUserProperty {
        public boolean mActive;
        public String mAppId;
        public long mCreationTimestamp;
        public String mExpiredEventName;
        public Bundle mExpiredEventParams;
        public String mName;
        public String mOrigin;
        public long mTimeToLive;
        public String mTimedOutEventName;
        public Bundle mTimedOutEventParams;
        public String mTriggerEventName;
        public long mTriggerTimeout;
        public String mTriggeredEventName;
        public Bundle mTriggeredEventParams;
        public long mTriggeredTimestamp;
        public Object mValue;

        public ConditionalUserProperty() {
        }

        ConditionalUserProperty(Bundle bundle) {
            Preconditions.checkNotNull(bundle);
            this.mAppId = (String) zzgo.zza(bundle, "app_id", String.class, null);
            this.mOrigin = (String) zzgo.zza(bundle, "origin", String.class, null);
            this.mName = (String) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.NAME, String.class, null);
            this.mValue = zzgo.zza(bundle, "value", Object.class, null);
            this.mTriggerEventName = (String) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, String.class, null);
            this.mTriggerTimeout = ((Long) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, Long.class, 0L)).longValue();
            this.mTimedOutEventName = (String) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, String.class, null);
            this.mTimedOutEventParams = (Bundle) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, Bundle.class, null);
            this.mTriggeredEventName = (String) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, String.class, null);
            this.mTriggeredEventParams = (Bundle) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, Bundle.class, null);
            this.mTimeToLive = ((Long) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, Long.class, 0L)).longValue();
            this.mExpiredEventName = (String) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, String.class, null);
            this.mExpiredEventParams = (Bundle) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, Bundle.class, null);
            this.mActive = ((Boolean) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.ACTIVE, Boolean.class, false)).booleanValue();
            this.mCreationTimestamp = ((Long) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, Long.class, 0L)).longValue();
            this.mTriggeredTimestamp = ((Long) zzgo.zza(bundle, AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, Long.class, 0L)).longValue();
        }

        public ConditionalUserProperty(ConditionalUserProperty other) {
            Preconditions.checkNotNull(other);
            this.mAppId = other.mAppId;
            this.mOrigin = other.mOrigin;
            this.mCreationTimestamp = other.mCreationTimestamp;
            this.mName = other.mName;
            Object obj = other.mValue;
            if (obj != null) {
                this.mValue = zzib.zza(obj);
                if (this.mValue == null) {
                    this.mValue = other.mValue;
                }
            }
            this.mActive = other.mActive;
            this.mTriggerEventName = other.mTriggerEventName;
            this.mTriggerTimeout = other.mTriggerTimeout;
            this.mTimedOutEventName = other.mTimedOutEventName;
            Bundle bundle = other.mTimedOutEventParams;
            if (bundle != null) {
                this.mTimedOutEventParams = new Bundle(bundle);
            }
            this.mTriggeredEventName = other.mTriggeredEventName;
            Bundle bundle2 = other.mTriggeredEventParams;
            if (bundle2 != null) {
                this.mTriggeredEventParams = new Bundle(bundle2);
            }
            this.mTriggeredTimestamp = other.mTriggeredTimestamp;
            this.mTimeToLive = other.mTimeToLive;
            this.mExpiredEventName = other.mExpiredEventName;
            Bundle bundle3 = other.mExpiredEventParams;
            if (bundle3 != null) {
                this.mExpiredEventParams = new Bundle(bundle3);
            }
        }
    }
}
