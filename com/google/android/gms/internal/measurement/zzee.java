package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.measurement.dynamite.ModuleDescriptor;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.measurement.internal.zzfk;
import com.google.android.gms.measurement.internal.zzgs;
import com.google.android.gms.measurement.internal.zzgt;
import com.google.android.gms.measurement.internal.zzib;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
/* compiled from: com.google.android.gms:play-services-measurement-sdk-api@@19.0.1 */
/* loaded from: classes.dex */
public final class zzee {
    private static volatile zzee zzc;
    protected final Clock zza;
    protected final ExecutorService zzb;
    private final String zzd;
    private final AppMeasurementSdk zze;
    private final List<Pair<zzgt, zzdv>> zzf;
    private int zzg;
    private boolean zzh;
    private final String zzi;
    private volatile zzcc zzj;

    protected zzee(Context context, String str, String str2, String str3, Bundle bundle) {
        if (str == null || !zzV(str2, str3)) {
            this.zzd = "FA";
        } else {
            this.zzd = str;
        }
        this.zza = DefaultClock.getInstance();
        this.zzb = zzbx.zza().zzb(new zzdi(this), 1);
        this.zze = new AppMeasurementSdk(this);
        this.zzf = new ArrayList();
        try {
            if (zzib.zzc(context, "google_app_id", zzfk.zza(context)) != null && !zzR()) {
                this.zzi = null;
                this.zzh = true;
                Log.w(this.zzd, "Disabling data collection. Found google_app_id in strings.xml but Google Analytics for Firebase is missing. Remove this value or add Google Analytics for Firebase to resume data collection.");
                return;
            }
        } catch (IllegalStateException e) {
        }
        if (!zzV(str2, str3)) {
            this.zzi = "fa";
            if (str2 == null || str3 == null) {
                boolean z = false;
                if ((str3 == null ? true : z) ^ (str2 == null)) {
                    Log.w(this.zzd, "Specified origin or custom app id is null. Both parameters will be ignored.");
                }
            } else {
                Log.v(this.zzd, "Deferring to Google Analytics for Firebase for event data collection. https://goo.gl/J1sWQy");
            }
        } else {
            this.zzi = str2;
        }
        zzU(new zzcx(this, str2, str3, context, bundle));
        Application application = (Application) context.getApplicationContext();
        if (application == null) {
            Log.w(this.zzd, "Unable to register lifecycle notifications. Application null.");
        } else {
            application.registerActivityLifecycleCallbacks(new zzed(this));
        }
    }

    protected static final boolean zzR() {
        try {
            Class.forName("com.google.firebase.analytics.FirebaseAnalytics");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public final void zzS(Exception exc, boolean z, boolean z2) {
        this.zzh |= z;
        if (z) {
            Log.w(this.zzd, "Data collection startup failed. No data will be collected.", exc);
            return;
        }
        if (z2) {
            zzA(5, "Error with data collection. Data lost.", exc, null, null);
        }
        Log.w(this.zzd, "Error with data collection. Data lost.", exc);
    }

    private final void zzT(String str, String str2, Bundle bundle, boolean z, boolean z2, Long l) {
        zzU(new zzdr(this, l, str, str2, bundle, z, z2));
    }

    public final void zzU(zzdt zzdt) {
        this.zzb.execute(zzdt);
    }

    public static final boolean zzV(String str, String str2) {
        return (str2 == null || str == null || zzR()) ? false : true;
    }

    public static zzee zzg(Context context, String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotNull(context);
        if (zzc == null) {
            synchronized (zzee.class) {
                if (zzc == null) {
                    zzc = new zzee(context, str, str2, str3, bundle);
                }
            }
        }
        return zzc;
    }

    public final void zzA(int i, String str, Object obj, Object obj2, Object obj3) {
        zzU(new zzdg(this, false, 5, str, obj, null, null));
    }

    public final void zzB(zzgt zzgt) {
        Preconditions.checkNotNull(zzgt);
        synchronized (this.zzf) {
            for (int i = 0; i < this.zzf.size(); i++) {
                if (zzgt.equals(this.zzf.get(i).first)) {
                    Log.w(this.zzd, "OnEventListener already registered.");
                    return;
                }
            }
            zzdv zzdv = new zzdv(zzgt);
            this.zzf.add(new Pair<>(zzgt, zzdv));
            if (this.zzj != null) {
                try {
                    this.zzj.registerOnMeasurementEventListener(zzdv);
                    return;
                } catch (BadParcelableException | NetworkOnMainThreadException | RemoteException | IllegalArgumentException | IllegalStateException | NullPointerException | SecurityException | UnsupportedOperationException e) {
                    Log.w(this.zzd, "Failed to register event listener on calling thread. Trying again on the dynamite thread.");
                }
            }
            zzU(new zzdp(this, zzdv));
        }
    }

    public final void zzC() {
        zzU(new zzcv(this));
    }

    public final void zzD(Bundle bundle) {
        zzU(new zzcn(this, bundle));
    }

    public final void zzE(Bundle bundle) {
        zzU(new zzct(this, bundle));
    }

    public final void zzF(Bundle bundle) {
        zzU(new zzcu(this, bundle));
    }

    public final void zzG(Activity activity, String str, String str2) {
        zzU(new zzcr(this, activity, str, str2));
    }

    public final void zzH(boolean z) {
        zzU(new zzdm(this, z));
    }

    public final void zzI(Bundle bundle) {
        zzU(new zzdn(this, bundle));
    }

    public final void zzJ(zzgs zzgs) {
        zzdu zzdu = new zzdu(zzgs);
        if (this.zzj != null) {
            try {
                this.zzj.setEventInterceptor(zzdu);
                return;
            } catch (BadParcelableException | NetworkOnMainThreadException | RemoteException | IllegalArgumentException | IllegalStateException | NullPointerException | SecurityException | UnsupportedOperationException e) {
                Log.w(this.zzd, "Failed to set event interceptor on calling thread. Trying again on the dynamite thread.");
            }
        }
        zzU(new zzdo(this, zzdu));
    }

    public final void zzK(Boolean bool) {
        zzU(new zzcs(this, bool));
    }

    public final void zzL(long j) {
        zzU(new zzcw(this, j));
    }

    public final void zzM(String str) {
        zzU(new zzcq(this, str));
    }

    public final void zzN(String str, String str2, Object obj, boolean z) {
        zzU(new zzds(this, str, str2, obj, z));
    }

    public final void zzO(zzgt zzgt) {
        Pair<zzgt, zzdv> pair;
        Preconditions.checkNotNull(zzgt);
        synchronized (this.zzf) {
            int i = 0;
            while (true) {
                if (i >= this.zzf.size()) {
                    pair = null;
                    break;
                } else if (zzgt.equals(this.zzf.get(i).first)) {
                    pair = this.zzf.get(i);
                    break;
                } else {
                    i++;
                }
            }
            if (pair == null) {
                Log.w(this.zzd, "OnEventListener had not been registered.");
                return;
            }
            this.zzf.remove(pair);
            zzdv zzdv = (zzdv) pair.second;
            if (this.zzj != null) {
                try {
                    this.zzj.unregisterOnMeasurementEventListener(zzdv);
                    return;
                } catch (BadParcelableException | NetworkOnMainThreadException | RemoteException | IllegalArgumentException | IllegalStateException | NullPointerException | SecurityException | UnsupportedOperationException e) {
                    Log.w(this.zzd, "Failed to unregister event listener on calling thread. Trying again on the dynamite thread.");
                }
            }
            zzU(new zzdq(this, zzdv));
        }
    }

    public final int zza(String str) {
        zzbz zzbz = new zzbz();
        zzU(new zzdj(this, str, zzbz));
        Integer num = (Integer) zzbz.zze(zzbz.zzb(10000), Integer.class);
        if (num == null) {
            return 25;
        }
        return num.intValue();
    }

    public final long zzb() {
        zzbz zzbz = new zzbz();
        zzU(new zzdc(this, zzbz));
        Long l = (Long) zzbz.zze(zzbz.zzb(500), Long.class);
        if (l != null) {
            return l.longValue();
        }
        long nextLong = new Random(System.nanoTime() ^ this.zza.currentTimeMillis()).nextLong();
        int i = this.zzg + 1;
        this.zzg = i;
        return nextLong + ((long) i);
    }

    public final Bundle zzc(Bundle bundle, boolean z) {
        zzbz zzbz = new zzbz();
        zzU(new zzdh(this, bundle, zzbz));
        if (z) {
            return zzbz.zzb(5000);
        }
        return null;
    }

    public final AppMeasurementSdk zzd() {
        return this.zze;
    }

    public final zzcc zzf(Context context, boolean z) {
        try {
            return zzcb.asInterface(DynamiteModule.load(context, DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION, ModuleDescriptor.MODULE_ID).instantiate("com.google.android.gms.measurement.internal.AppMeasurementDynamiteService"));
        } catch (DynamiteModule.LoadingException e) {
            zzS(e, true, false);
            return null;
        }
    }

    public final Object zzh(int i) {
        zzbz zzbz = new zzbz();
        zzU(new zzdl(this, zzbz, i));
        return zzbz.zze(zzbz.zzb(15000), Object.class);
    }

    public final String zzj() {
        return this.zzi;
    }

    public final String zzk() {
        zzbz zzbz = new zzbz();
        zzU(new zzdk(this, zzbz));
        return zzbz.zzc(120000);
    }

    public final String zzl() {
        zzbz zzbz = new zzbz();
        zzU(new zzdb(this, zzbz));
        return zzbz.zzc(50);
    }

    public final String zzm() {
        zzbz zzbz = new zzbz();
        zzU(new zzde(this, zzbz));
        return zzbz.zzc(500);
    }

    public final String zzn() {
        zzbz zzbz = new zzbz();
        zzU(new zzdd(this, zzbz));
        return zzbz.zzc(500);
    }

    public final String zzo() {
        zzbz zzbz = new zzbz();
        zzU(new zzda(this, zzbz));
        return zzbz.zzc(500);
    }

    public final List<Bundle> zzp(String str, String str2) {
        zzbz zzbz = new zzbz();
        zzU(new zzcp(this, str, str2, zzbz));
        List<Bundle> list = (List) zzbz.zze(zzbz.zzb(5000), List.class);
        return list == null ? Collections.emptyList() : list;
    }

    public final Map<String, Object> zzq(String str, String str2, boolean z) {
        zzbz zzbz = new zzbz();
        zzU(new zzdf(this, str, str2, z, zzbz));
        Bundle zzb = zzbz.zzb(5000);
        if (zzb == null || zzb.size() == 0) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap(zzb.size());
        for (String str3 : zzb.keySet()) {
            Object obj = zzb.get(str3);
            if ((obj instanceof Double) || (obj instanceof Long) || (obj instanceof String)) {
                hashMap.put(str3, obj);
            }
        }
        return hashMap;
    }

    public final void zzu(String str) {
        zzU(new zzcy(this, str));
    }

    public final void zzv(String str, String str2, Bundle bundle) {
        zzU(new zzco(this, str, str2, bundle));
    }

    public final void zzw(String str) {
        zzU(new zzcz(this, str));
    }

    public final void zzx(String str, Bundle bundle) {
        zzT(null, str, bundle, false, true, null);
    }

    public final void zzy(String str, String str2, Bundle bundle) {
        zzT(str, str2, bundle, true, true, null);
    }

    public final void zzz(String str, String str2, Bundle bundle, long j) {
        zzT(str, str2, bundle, true, false, Long.valueOf(j));
    }
}
