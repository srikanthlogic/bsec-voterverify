package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.internal.measurement.zzcf;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjj extends zzf {
    private zzdz zzb;
    private volatile Boolean zzc;
    private final zzam zzd;
    private final zzjz zze;
    private final zzam zzg;
    private final List<Runnable> zzf = new ArrayList();
    private final zzji zza = new zzji(this);

    public zzjj(zzfs zzfs) {
        super(zzfs);
        this.zze = new zzjz(zzfs.zzav());
        this.zzd = new zzit(this, zzfs);
        this.zzg = new zziv(this, zzfs);
    }

    private final zzp zzO(boolean z) {
        Pair<String, Long> zza;
        this.zzs.zzaw();
        zzea zzh = ((zze) this).zzs.zzh();
        String str = null;
        if (z) {
            zzei zzay = this.zzs.zzay();
            if (!(zzay.zzs.zzm().zzb == null || (zza = zzay.zzs.zzm().zzb.zza()) == null || zza == zzex.zza)) {
                String valueOf = String.valueOf(zza.second);
                String str2 = (String) zza.first;
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 1 + String.valueOf(str2).length());
                sb.append(valueOf);
                sb.append(":");
                sb.append(str2);
                str = sb.toString();
            }
        }
        return zzh.zzj(str);
    }

    public final void zzP() {
        zzg();
        this.zzs.zzay().zzj().zzb("Processing queued up service tasks", Integer.valueOf(this.zzf.size()));
        for (Runnable runnable : this.zzf) {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                this.zzs.zzay().zzd().zzb("Task exception while flushing queue", e);
            }
        }
        this.zzf.clear();
        this.zzg.zzb();
    }

    public final void zzQ() {
        zzg();
        this.zze.zzb();
        zzam zzam = this.zzd;
        this.zzs.zzf();
        zzam.zzd(zzdw.zzI.zza(null).longValue());
    }

    private final void zzR(Runnable runnable) throws IllegalStateException {
        zzg();
        if (zzL()) {
            runnable.run();
            return;
        }
        int size = this.zzf.size();
        this.zzs.zzf();
        if (((long) size) >= 1000) {
            this.zzs.zzay().zzd().zza("Discarding data. Max runnable queue size reached");
            return;
        }
        this.zzf.add(runnable);
        this.zzg.zzd(60000);
        zzr();
    }

    private final boolean zzS() {
        this.zzs.zzaw();
        return true;
    }

    public static /* bridge */ /* synthetic */ void zzo(zzjj zzjj, ComponentName componentName) {
        zzjj.zzg();
        if (zzjj.zzb != null) {
            zzjj.zzb = null;
            zzjj.zzs.zzay().zzj().zzb("Disconnected from device MeasurementService", componentName);
            zzjj.zzg();
            zzjj.zzr();
        }
    }

    public final void zzA(zzat zzat, String str) {
        Preconditions.checkNotNull(zzat);
        zzg();
        zza();
        zzS();
        zzR(new zziy(this, true, zzO(true), ((zze) this).zzs.zzi().zzo(zzat), zzat, str));
    }

    public final void zzB(zzcf zzcf, zzat zzat, String str) {
        zzg();
        zza();
        if (this.zzs.zzv().zzo(12451000) != 0) {
            this.zzs.zzay().zzk().zza("Not bundling data. Service unavailable or out of date");
            this.zzs.zzv().zzR(zzcf, new byte[0]);
            return;
        }
        zzR(new zziu(this, zzat, str, zzcf));
    }

    public final void zzC() {
        zzg();
        zza();
        zzp zzO = zzO(false);
        zzS();
        ((zze) this).zzs.zzi().zzj();
        zzR(new zzin(this, zzO));
    }

    public final void zzD(zzdz zzdz, AbstractSafeParcelable abstractSafeParcelable, zzp zzp) {
        int i;
        zzg();
        zza();
        zzS();
        this.zzs.zzf();
        int i2 = 0;
        int i3 = 100;
        while (i2 < 1001 && i3 == 100) {
            ArrayList arrayList = new ArrayList();
            List<AbstractSafeParcelable> zzi = ((zze) this).zzs.zzi().zzi(100);
            if (zzi != null) {
                arrayList.addAll(zzi);
                i = zzi.size();
            } else {
                i = 0;
            }
            if (abstractSafeParcelable != null && i < 100) {
                arrayList.add(abstractSafeParcelable);
            }
            int size = arrayList.size();
            for (int i4 = 0; i4 < size; i4++) {
                AbstractSafeParcelable abstractSafeParcelable2 = (AbstractSafeParcelable) arrayList.get(i4);
                if (abstractSafeParcelable2 instanceof zzat) {
                    try {
                        zzdz.zzk((zzat) abstractSafeParcelable2, zzp);
                    } catch (RemoteException e) {
                        this.zzs.zzay().zzd().zzb("Failed to send event to the service", e);
                    }
                } else if (abstractSafeParcelable2 instanceof zzkq) {
                    try {
                        zzdz.zzt((zzkq) abstractSafeParcelable2, zzp);
                    } catch (RemoteException e2) {
                        this.zzs.zzay().zzd().zzb("Failed to send user property to the service", e2);
                    }
                } else if (abstractSafeParcelable2 instanceof zzab) {
                    try {
                        zzdz.zzn((zzab) abstractSafeParcelable2, zzp);
                    } catch (RemoteException e3) {
                        this.zzs.zzay().zzd().zzb("Failed to send conditional user property to the service", e3);
                    }
                } else {
                    this.zzs.zzay().zzd().zza("Discarding data. Unrecognized parcel type.");
                }
            }
            i2++;
            i3 = i;
        }
    }

    public final void zzE(zzab zzab) {
        Preconditions.checkNotNull(zzab);
        zzg();
        zza();
        this.zzs.zzaw();
        zzR(new zziz(this, true, zzO(true), ((zze) this).zzs.zzi().zzn(zzab), new zzab(zzab), zzab));
    }

    public final void zzF(boolean z) {
        zzg();
        zza();
        if (z) {
            zzS();
            ((zze) this).zzs.zzi().zzj();
        }
        if (zzM()) {
            zzR(new zzix(this, zzO(false)));
        }
    }

    public final void zzG(zzic zzic) {
        zzg();
        zza();
        zzR(new zzir(this, zzic));
    }

    public final void zzH(Bundle bundle) {
        zzg();
        zza();
        zzR(new zzis(this, zzO(false), bundle));
    }

    public final void zzI() {
        zzg();
        zza();
        zzR(new zziw(this, zzO(true)));
    }

    public final void zzJ(zzdz zzdz) {
        zzg();
        Preconditions.checkNotNull(zzdz);
        this.zzb = zzdz;
        zzQ();
        zzP();
    }

    public final void zzK(zzkq zzkq) {
        zzg();
        zza();
        zzS();
        zzR(new zzil(this, zzO(true), ((zze) this).zzs.zzi().zzp(zzkq), zzkq));
    }

    public final boolean zzL() {
        zzg();
        zza();
        return this.zzb != null;
    }

    public final boolean zzM() {
        zzg();
        zza();
        if (!zzN() || this.zzs.zzv().zzm() >= zzdw.zzao.zza(null).intValue()) {
            return true;
        }
        return false;
    }

    public final boolean zzN() {
        Boolean bool;
        zzg();
        zza();
        if (this.zzc == null) {
            zzg();
            zza();
            zzex zzm = this.zzs.zzm();
            zzm.zzg();
            boolean z = false;
            if (!zzm.zza().contains("use_service")) {
                bool = null;
            } else {
                bool = Boolean.valueOf(zzm.zza().getBoolean("use_service", false));
            }
            boolean z2 = true;
            if (bool == null || !bool.booleanValue()) {
                this.zzs.zzaw();
                if (((zze) this).zzs.zzh().zzh() == 1) {
                    z = true;
                } else {
                    this.zzs.zzay().zzj().zza("Checking service availability");
                    int zzo = this.zzs.zzv().zzo(12451000);
                    if (zzo == 0) {
                        this.zzs.zzay().zzj().zza("Service available");
                        z = true;
                    } else if (zzo == 1) {
                        this.zzs.zzay().zzj().zza("Service missing");
                        z = true;
                        z2 = false;
                    } else if (zzo == 2) {
                        this.zzs.zzay().zzc().zza("Service container out of date");
                        if (this.zzs.zzv().zzm() < 17443) {
                            z = true;
                            z2 = false;
                        } else if (bool != null) {
                            z2 = false;
                        }
                    } else if (zzo == 3) {
                        this.zzs.zzay().zzk().zza("Service disabled");
                        z2 = false;
                    } else if (zzo == 9) {
                        this.zzs.zzay().zzk().zza("Service invalid");
                        z2 = false;
                    } else if (zzo != 18) {
                        this.zzs.zzay().zzk().zzb("Unexpected service status", Integer.valueOf(zzo));
                        z2 = false;
                    } else {
                        this.zzs.zzay().zzk().zza("Service updating");
                        z = true;
                    }
                }
                if (!z2 && this.zzs.zzf().zzx()) {
                    this.zzs.zzay().zzd().zza("No way to upload. Consider using the full version of Analytics");
                } else if (z) {
                    zzex zzm2 = this.zzs.zzm();
                    zzm2.zzg();
                    SharedPreferences.Editor edit = zzm2.zza().edit();
                    edit.putBoolean("use_service", z2);
                    edit.apply();
                }
            }
            this.zzc = Boolean.valueOf(z2);
        }
        return this.zzc.booleanValue();
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return false;
    }

    public final Boolean zzj() {
        return this.zzc;
    }

    public final void zzq() {
        zzg();
        zza();
        zzp zzO = zzO(true);
        ((zze) this).zzs.zzi().zzk();
        zzR(new zziq(this, zzO));
    }

    public final void zzr() {
        zzg();
        zza();
        if (!zzL()) {
            if (zzN()) {
                this.zza.zzc();
            } else if (!this.zzs.zzf().zzx()) {
                this.zzs.zzaw();
                List<ResolveInfo> queryIntentServices = this.zzs.zzau().getPackageManager().queryIntentServices(new Intent().setClassName(this.zzs.zzau(), "com.google.android.gms.measurement.AppMeasurementService"), 65536);
                if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                    this.zzs.zzay().zzd().zza("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
                    return;
                }
                Intent intent = new Intent("com.google.android.gms.measurement.START");
                Context zzau = this.zzs.zzau();
                this.zzs.zzaw();
                intent.setComponent(new ComponentName(zzau, "com.google.android.gms.measurement.AppMeasurementService"));
                this.zza.zzb(intent);
            }
        }
    }

    public final void zzs() {
        zzg();
        zza();
        this.zza.zzd();
        try {
            ConnectionTracker.getInstance().unbindService(this.zzs.zzau(), this.zza);
        } catch (IllegalArgumentException e) {
        } catch (IllegalStateException e2) {
        }
        this.zzb = null;
    }

    public final void zzt(zzcf zzcf) {
        zzg();
        zza();
        zzR(new zzip(this, zzO(false), zzcf));
    }

    public final void zzu(AtomicReference<String> atomicReference) {
        zzg();
        zza();
        zzR(new zzio(this, atomicReference, zzO(false)));
    }

    public final void zzv(zzcf zzcf, String str, String str2) {
        zzg();
        zza();
        zzR(new zzjb(this, str, str2, zzO(false), zzcf));
    }

    public final void zzw(AtomicReference<List<zzab>> atomicReference, String str, String str2, String str3) {
        zzg();
        zza();
        zzR(new zzja(this, atomicReference, null, str2, str3, zzO(false)));
    }

    public final void zzx(AtomicReference<List<zzkq>> atomicReference, boolean z) {
        zzg();
        zza();
        zzR(new zzim(this, atomicReference, zzO(false), z));
    }

    public final void zzy(zzcf zzcf, String str, String str2, boolean z) {
        zzg();
        zza();
        zzR(new zzik(this, str, str2, zzO(false), z, zzcf));
    }

    public final void zzz(AtomicReference<List<zzkq>> atomicReference, String str, String str2, String str3, boolean z) {
        zzg();
        zza();
        zzR(new zzjc(this, atomicReference, null, str2, str3, zzO(false), z));
    }
}
