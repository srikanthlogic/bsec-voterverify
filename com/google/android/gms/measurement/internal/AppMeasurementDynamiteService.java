package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.measurement.zzcb;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzci;
import com.google.android.gms.internal.measurement.zzck;
import com.google.android.gms.internal.measurement.zzcl;
import com.google.android.gms.internal.measurement.zzob;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/* compiled from: com.google.android.gms:play-services-measurement-sdk@@19.0.1 */
/* loaded from: classes.dex */
public class AppMeasurementDynamiteService extends zzcb {
    zzfs zza = null;
    private final Map<Integer, zzgt> zzb = new ArrayMap();

    @EnsuresNonNull({"scion"})
    private final void zzb() {
        if (this.zza == null) {
            throw new IllegalStateException("Attempting to perform action before initialize.");
        }
    }

    private final void zzc(zzcf zzcf, String str) {
        zzb();
        this.zza.zzv().zzU(zzcf, str);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void beginAdUnitExposure(String adUnitId, long timestamp) throws RemoteException {
        zzb();
        this.zza.zzd().zzd(adUnitId, timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void clearConditionalUserProperty(String userPropertyName, String clearEventName, Bundle clearEventParams) throws RemoteException {
        zzb();
        this.zza.zzq().zzz(userPropertyName, clearEventName, clearEventParams);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void clearMeasurementEnabled(long j) throws RemoteException {
        zzb();
        this.zza.zzq().zzT(null);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void endAdUnitExposure(String adUnitId, long timestamp) throws RemoteException {
        zzb();
        this.zza.zzd().zze(adUnitId, timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void generateEventId(zzcf receiver) throws RemoteException {
        zzb();
        long zzq = this.zza.zzv().zzq();
        zzb();
        this.zza.zzv().zzT(receiver, zzq);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getAppInstanceId(zzcf receiver) throws RemoteException {
        zzb();
        this.zza.zzaz().zzp(new zzh(this, receiver));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getCachedAppInstanceId(zzcf receiver) throws RemoteException {
        zzb();
        zzc(receiver, this.zza.zzq().zzo());
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getConditionalUserProperties(String origin, String propertyNamePrefix, zzcf receiver) throws RemoteException {
        zzb();
        this.zza.zzaz().zzp(new zzl(this, receiver, origin, propertyNamePrefix));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getCurrentScreenClass(zzcf receiver) throws RemoteException {
        zzb();
        zzc(receiver, this.zza.zzq().zzp());
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getCurrentScreenName(zzcf receiver) throws RemoteException {
        zzb();
        zzc(receiver, this.zza.zzq().zzq());
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getGmpAppId(zzcf receiver) throws RemoteException {
        String str;
        zzb();
        zzhv zzq = this.zza.zzq();
        if (zzq.zzs.zzw() != null) {
            str = zzq.zzs.zzw();
        } else {
            try {
                str = zzib.zzc(zzq.zzs.zzau(), "google_app_id", zzq.zzs.zzz());
            } catch (IllegalStateException e) {
                zzq.zzs.zzay().zzd().zzb("getGoogleAppId failed with exception", e);
                str = null;
            }
        }
        zzc(receiver, str);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getMaxUserProperties(String origin, zzcf receiver) throws RemoteException {
        zzb();
        this.zza.zzq().zzh(origin);
        zzb();
        this.zza.zzv().zzS(receiver, 25);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getTestFlag(zzcf receiver, int type) throws RemoteException {
        zzb();
        if (type == 0) {
            this.zza.zzv().zzU(receiver, this.zza.zzq().zzr());
        } else if (type == 1) {
            this.zza.zzv().zzT(receiver, this.zza.zzq().zzm().longValue());
        } else if (type == 2) {
            zzku zzv = this.zza.zzv();
            double doubleValue = this.zza.zzq().zzj().doubleValue();
            Bundle bundle = new Bundle();
            bundle.putDouble("r", doubleValue);
            try {
                receiver.zzd(bundle);
            } catch (RemoteException e) {
                zzv.zzs.zzay().zzk().zzb("Error returning double value to wrapper", e);
            }
        } else if (type == 3) {
            this.zza.zzv().zzS(receiver, this.zza.zzq().zzl().intValue());
        } else if (type == 4) {
            this.zza.zzv().zzO(receiver, this.zza.zzq().zzi().booleanValue());
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void getUserProperties(String origin, String propertyNamePrefix, boolean getInternal, zzcf receiver) throws RemoteException {
        zzb();
        this.zza.zzaz().zzp(new zzj(this, receiver, origin, propertyNamePrefix, getInternal));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void initForTests(Map map) throws RemoteException {
        zzb();
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void initialize(IObjectWrapper context, zzcl params, long timestamp) throws RemoteException {
        zzfs zzfs = this.zza;
        if (zzfs == null) {
            this.zza = zzfs.zzp((Context) Preconditions.checkNotNull((Context) ObjectWrapper.unwrap(context)), params, Long.valueOf(timestamp));
        } else {
            zzfs.zzay().zzk().zza("Attempting to initialize multiple times");
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void isDataCollectionEnabled(zzcf receiver) throws RemoteException {
        zzb();
        this.zza.zzaz().zzp(new zzm(this, receiver));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void logEvent(String origin, String name, Bundle params, boolean isInternal, boolean allowInterceptor, long timestamp) throws RemoteException {
        zzb();
        this.zza.zzq().zzD(origin, name, params, isInternal, allowInterceptor, timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void logEventAndBundle(String packageName, String eventName, Bundle params, zzcf receiver, long timestamp) throws RemoteException {
        zzb();
        Preconditions.checkNotEmpty(eventName);
        (params != null ? new Bundle(params) : new Bundle()).putString("_o", "app");
        this.zza.zzaz().zzp(new zzi(this, receiver, new zzat(eventName, new zzar(params), "app", timestamp), packageName));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void logHealthData(int priority, String key, IObjectWrapper context1, IObjectWrapper context2, IObjectWrapper context3) throws RemoteException {
        Object obj;
        Object obj2;
        Object obj3;
        zzb();
        if (context1 == null) {
            obj = null;
        } else {
            obj = ObjectWrapper.unwrap(context1);
        }
        if (context2 == null) {
            obj2 = null;
        } else {
            obj2 = ObjectWrapper.unwrap(context2);
        }
        if (context3 == null) {
            obj3 = null;
        } else {
            obj3 = ObjectWrapper.unwrap(context3);
        }
        this.zza.zzay().zzt(priority, true, false, key, obj, obj2, obj3);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityCreated(IObjectWrapper activity, Bundle savedInstanceState, long j) throws RemoteException {
        zzb();
        zzhu zzhu = this.zza.zzq().zza;
        if (zzhu != null) {
            this.zza.zzq().zzA();
            zzhu.onActivityCreated((Activity) ObjectWrapper.unwrap(activity), savedInstanceState);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityDestroyed(IObjectWrapper activity, long j) throws RemoteException {
        zzb();
        zzhu zzhu = this.zza.zzq().zza;
        if (zzhu != null) {
            this.zza.zzq().zzA();
            zzhu.onActivityDestroyed((Activity) ObjectWrapper.unwrap(activity));
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityPaused(IObjectWrapper activity, long j) throws RemoteException {
        zzb();
        zzhu zzhu = this.zza.zzq().zza;
        if (zzhu != null) {
            this.zza.zzq().zzA();
            zzhu.onActivityPaused((Activity) ObjectWrapper.unwrap(activity));
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityResumed(IObjectWrapper activity, long j) throws RemoteException {
        zzb();
        zzhu zzhu = this.zza.zzq().zza;
        if (zzhu != null) {
            this.zza.zzq().zzA();
            zzhu.onActivityResumed((Activity) ObjectWrapper.unwrap(activity));
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivitySaveInstanceState(IObjectWrapper activity, zzcf receiver, long j) throws RemoteException {
        zzb();
        zzhu zzhu = this.zza.zzq().zza;
        Bundle bundle = new Bundle();
        if (zzhu != null) {
            this.zza.zzq().zzA();
            zzhu.onActivitySaveInstanceState((Activity) ObjectWrapper.unwrap(activity), bundle);
        }
        try {
            receiver.zzd(bundle);
        } catch (RemoteException e) {
            this.zza.zzay().zzk().zzb("Error returning bundle value to wrapper", e);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityStarted(IObjectWrapper activity, long j) throws RemoteException {
        zzb();
        if (this.zza.zzq().zza != null) {
            this.zza.zzq().zzA();
            Activity activity2 = (Activity) ObjectWrapper.unwrap(activity);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void onActivityStopped(IObjectWrapper activity, long j) throws RemoteException {
        zzb();
        if (this.zza.zzq().zza != null) {
            this.zza.zzq().zzA();
            Activity activity2 = (Activity) ObjectWrapper.unwrap(activity);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void performAction(Bundle bundle, zzcf receiver, long j) throws RemoteException {
        zzb();
        receiver.zzd(null);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void registerOnMeasurementEventListener(zzci listenerProxy) throws RemoteException {
        zzgt zzgt;
        zzb();
        synchronized (this.zzb) {
            zzgt = this.zzb.get(Integer.valueOf(listenerProxy.zzd()));
            if (zzgt == null) {
                zzgt = new zzo(this, listenerProxy);
                this.zzb.put(Integer.valueOf(listenerProxy.zzd()), zzgt);
            }
        }
        this.zza.zzq().zzI(zzgt);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void resetAnalyticsData(long timestamp) throws RemoteException {
        zzb();
        this.zza.zzq().zzJ(timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setConditionalUserProperty(Bundle conditionalUserProperty, long timestamp) throws RemoteException {
        zzb();
        if (conditionalUserProperty == null) {
            this.zza.zzay().zzd().zza("Conditional user property must not be null");
        } else {
            this.zza.zzq().zzP(conditionalUserProperty, timestamp);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setConsent(Bundle consentMap, long timestamp) throws RemoteException {
        zzb();
        zzhv zzq = this.zza.zzq();
        zzob.zzc();
        if (!zzq.zzs.zzf().zzs(null, zzdw.zzau) || TextUtils.isEmpty(((zze) zzq).zzs.zzh().zzn())) {
            zzq.zzQ(consentMap, 0, timestamp);
        } else {
            zzq.zzs.zzay().zzl().zza("Using developer consent only; google app id found");
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setConsentThirdParty(Bundle consentMap, long timestamp) throws RemoteException {
        zzb();
        this.zza.zzq().zzQ(consentMap, -20, timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setCurrentScreen(IObjectWrapper activity, String screenName, String screenClassOverride, long j) throws RemoteException {
        zzb();
        this.zza.zzs().zzw((Activity) ObjectWrapper.unwrap(activity), screenName, screenClassOverride);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setDataCollectionEnabled(boolean enabled) throws RemoteException {
        zzb();
        zzhv zzq = this.zza.zzq();
        zzq.zza();
        zzfs zzfs = ((zze) zzq).zzs;
        zzq.zzs.zzaz().zzp(new zzgx(zzq, enabled));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setDefaultEventParameters(Bundle parameters) {
        Bundle parameters2;
        zzb();
        zzhv zzq = this.zza.zzq();
        if (parameters == null) {
            parameters2 = null;
        } else {
            parameters2 = new Bundle(parameters);
        }
        zzq.zzs.zzaz().zzp(new Runnable(parameters2) { // from class: com.google.android.gms.measurement.internal.zzgw
            public final /* synthetic */ Bundle zzb;

            {
                this.zzb = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                zzhv.this.zzB(this.zzb);
            }
        });
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setEventInterceptor(zzci interceptor) throws RemoteException {
        zzb();
        zzn zzn = new zzn(this, interceptor);
        if (this.zza.zzaz().zzs()) {
            this.zza.zzq().zzS(zzn);
        } else {
            this.zza.zzaz().zzp(new zzk(this, zzn));
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setInstanceIdProvider(zzck zzck) throws RemoteException {
        zzb();
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setMeasurementEnabled(boolean enabled, long j) throws RemoteException {
        zzb();
        this.zza.zzq().zzT(Boolean.valueOf(enabled));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setMinimumSessionDuration(long j) throws RemoteException {
        zzb();
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setSessionTimeoutDuration(long milliseconds) throws RemoteException {
        zzb();
        zzhv zzq = this.zza.zzq();
        zzfs zzfs = ((zze) zzq).zzs;
        zzq.zzs.zzaz().zzp(new zzgz(zzq, milliseconds));
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setUserId(String id, long timestamp) throws RemoteException {
        zzb();
        if (!this.zza.zzf().zzs(null, zzdw.zzas) || id == null || id.length() != 0) {
            this.zza.zzq().zzW(null, DBHelper.Key_ID, id, true, timestamp);
        } else {
            this.zza.zzay().zzk().zza("User ID must be non-empty");
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void setUserProperty(String origin, String name, IObjectWrapper value, boolean isInternal, long timestamp) throws RemoteException {
        zzb();
        this.zza.zzq().zzW(origin, name, ObjectWrapper.unwrap(value), isInternal, timestamp);
    }

    @Override // com.google.android.gms.internal.measurement.zzcc
    public void unregisterOnMeasurementEventListener(zzci listenerProxy) throws RemoteException {
        zzgt remove;
        zzb();
        synchronized (this.zzb) {
            remove = this.zzb.remove(Integer.valueOf(listenerProxy.zzd()));
        }
        if (remove == null) {
            remove = new zzo(this, listenerProxy);
        }
        this.zza.zzq().zzY(remove);
    }
}
