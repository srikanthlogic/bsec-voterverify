package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlinx.coroutines.DebugKt;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzij extends zzf {
    protected zzic zza;
    private volatile zzic zzb;
    private volatile zzic zzc;
    private Activity zze;
    private volatile boolean zzf;
    private volatile zzic zzg;
    private zzic zzh;
    private boolean zzi;
    private zzic zzk;
    private String zzl;
    private final Object zzj = new Object();
    private final Map<Activity, zzic> zzd = new ConcurrentHashMap();

    public zzij(zzfs zzfs) {
        super(zzfs);
    }

    public final void zzB(zzic zzic, zzic zzic2, long j, boolean z, Bundle bundle) {
        boolean z2;
        String str;
        long j2;
        long j3;
        zzg();
        boolean z3 = false;
        if (zzic2 == null || zzic2.zzc != zzic.zzc || !zzku.zzak(zzic2.zzb, zzic.zzb)) {
            z2 = true;
        } else {
            z2 = !zzku.zzak(zzic2.zza, zzic.zza);
        }
        if (z && this.zza != null) {
            z3 = true;
        }
        if (z2) {
            Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
            zzku.zzJ(zzic, bundle2, true);
            if (zzic2 != null) {
                String str2 = zzic2.zza;
                if (str2 != null) {
                    bundle2.putString("_pn", str2);
                }
                String str3 = zzic2.zzb;
                if (str3 != null) {
                    bundle2.putString("_pc", str3);
                }
                bundle2.putLong("_pi", zzic2.zzc);
            }
            if (z3) {
                zzjw zzjw = ((zze) this).zzs.zzu().zzb;
                long j4 = j - zzjw.zzb;
                zzjw.zzb = j;
                if (j4 > 0) {
                    this.zzs.zzv().zzH(bundle2, j4);
                }
            }
            if (!this.zzs.zzf().zzu()) {
                bundle2.putLong("_mst", 1);
            }
            if (true != zzic.zze) {
                str = DebugKt.DEBUG_PROPERTY_VALUE_AUTO;
            } else {
                str = "app";
            }
            long currentTimeMillis = this.zzs.zzav().currentTimeMillis();
            if (zzic.zze) {
                j3 = currentTimeMillis;
                long j5 = zzic.zzf;
                if (j5 != 0) {
                    j2 = j5;
                    ((zze) this).zzs.zzq().zzG(str, "_vs", j2, bundle2);
                }
            } else {
                j3 = currentTimeMillis;
            }
            j2 = j3;
            ((zze) this).zzs.zzq().zzG(str, "_vs", j2, bundle2);
        }
        if (z3) {
            zzC(this.zza, true, j);
        }
        this.zza = zzic;
        if (zzic.zze) {
            this.zzh = zzic;
        }
        ((zze) this).zzs.zzt().zzG(zzic);
    }

    public final void zzC(zzic zzic, boolean z, long j) {
        boolean z2;
        ((zze) this).zzs.zzd().zzf(this.zzs.zzav().elapsedRealtime());
        if (zzic == null || !zzic.zzd) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (((zze) this).zzs.zzu().zzb.zzd(z2, z, j) && zzic != null) {
            zzic.zzd = false;
        }
    }

    public static /* bridge */ /* synthetic */ void zzp(zzij zzij, Bundle bundle, zzic zzic, zzic zzic2, long j) {
        bundle.remove(FirebaseAnalytics.Param.SCREEN_NAME);
        bundle.remove(FirebaseAnalytics.Param.SCREEN_CLASS);
        zzij.zzB(zzic, zzic2, j, true, zzij.zzs.zzv().zzy(null, FirebaseAnalytics.Event.SCREEN_VIEW, bundle, null, false));
    }

    private final zzic zzz(Activity activity) {
        Preconditions.checkNotNull(activity);
        zzic zzic = this.zzd.get(activity);
        if (zzic == null) {
            zzic zzic2 = new zzic(null, zzl(activity.getClass(), "Activity"), this.zzs.zzv().zzq());
            this.zzd.put(activity, zzic2);
            zzic = zzic2;
        }
        return this.zzg != null ? this.zzg : zzic;
    }

    @Override // com.google.android.gms.measurement.internal.zzf
    protected final boolean zzf() {
        return false;
    }

    public final zzic zzi() {
        return this.zzb;
    }

    public final zzic zzj(boolean z) {
        zza();
        zzg();
        if (!z) {
            return this.zza;
        }
        zzic zzic = this.zza;
        return zzic != null ? zzic : this.zzh;
    }

    final String zzl(Class<?> cls, String str) {
        String str2;
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            return "Activity";
        }
        String[] split = canonicalName.split("\\.");
        int length = split.length;
        if (length > 0) {
            str2 = split[length - 1];
        } else {
            str2 = "";
        }
        int length2 = str2.length();
        this.zzs.zzf();
        if (length2 <= 100) {
            return str2;
        }
        this.zzs.zzf();
        return str2.substring(0, 100);
    }

    public final void zzr(Activity activity, Bundle bundle) {
        Bundle bundle2;
        if (this.zzs.zzf().zzu() && bundle != null && (bundle2 = bundle.getBundle("com.google.app_measurement.screen_service")) != null) {
            this.zzd.put(activity, new zzic(bundle2.getString(AppMeasurementSdk.ConditionalUserProperty.NAME), bundle2.getString("referrer_name"), bundle2.getLong("id")));
        }
    }

    public final void zzs(Activity activity) {
        synchronized (this.zzj) {
            if (activity == this.zze) {
                this.zze = null;
            }
        }
        if (this.zzs.zzf().zzu()) {
            this.zzd.remove(activity);
        }
    }

    public final void zzt(Activity activity) {
        synchronized (this.zzj) {
            this.zzi = false;
            this.zzf = true;
        }
        long elapsedRealtime = this.zzs.zzav().elapsedRealtime();
        if (!this.zzs.zzf().zzu()) {
            this.zzb = null;
            this.zzs.zzaz().zzp(new zzig(this, elapsedRealtime));
            return;
        }
        zzic zzz = zzz(activity);
        this.zzc = this.zzb;
        this.zzb = null;
        this.zzs.zzaz().zzp(new zzih(this, zzz, elapsedRealtime));
    }

    public final void zzu(Activity activity) {
        synchronized (this.zzj) {
            this.zzi = true;
            if (activity != this.zze) {
                synchronized (this.zzj) {
                    this.zze = activity;
                    this.zzf = false;
                }
                if (this.zzs.zzf().zzu()) {
                    this.zzg = null;
                    this.zzs.zzaz().zzp(new zzii(this));
                }
            }
        }
        if (!this.zzs.zzf().zzu()) {
            this.zzb = this.zzg;
            this.zzs.zzaz().zzp(new zzif(this));
            return;
        }
        zzA(activity, zzz(activity), false);
        zzd zzd = ((zze) this).zzs.zzd();
        zzd.zzs.zzaz().zzp(new zzc(zzd, zzd.zzs.zzav().elapsedRealtime()));
    }

    public final void zzv(Activity activity, Bundle bundle) {
        zzic zzic;
        if (this.zzs.zzf().zzu() && bundle != null && (zzic = this.zzd.get(activity)) != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("id", zzic.zzc);
            bundle2.putString(AppMeasurementSdk.ConditionalUserProperty.NAME, zzic.zza);
            bundle2.putString("referrer_name", zzic.zzb);
            bundle.putBundle("com.google.app_measurement.screen_service", bundle2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0088, code lost:
        if (r1 <= 100) goto L_0x00a3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00b4, code lost:
        if (r1 <= 100) goto L_0x00cf;
     */
    @Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzw(Activity activity, String str, String str2) {
        String str3;
        if (!this.zzs.zzf().zzu()) {
            this.zzs.zzay().zzl().zza("setCurrentScreen cannot be called while screen reporting is disabled.");
            return;
        }
        zzic zzic = this.zzb;
        if (zzic == null) {
            this.zzs.zzay().zzl().zza("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzd.get(activity) == null) {
            this.zzs.zzay().zzl().zza("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzl(activity.getClass(), "Activity");
            }
            boolean zzak = zzku.zzak(zzic.zzb, str2);
            boolean zzak2 = zzku.zzak(zzic.zza, str);
            if (!zzak || !zzak2) {
                if (str != null) {
                    if (str.length() > 0) {
                        int length = str.length();
                        this.zzs.zzf();
                    }
                    this.zzs.zzay().zzl().zzb("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
                    return;
                }
                if (str2 != null) {
                    if (str2.length() > 0) {
                        int length2 = str2.length();
                        this.zzs.zzf();
                    }
                    this.zzs.zzay().zzl().zzb("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
                    return;
                }
                zzeg zzj = this.zzs.zzay().zzj();
                if (str == null) {
                    str3 = "null";
                } else {
                    str3 = str;
                }
                zzj.zzc("Setting current screen to name, class", str3, str2);
                zzic zzic2 = new zzic(str, str2, this.zzs.zzv().zzq());
                this.zzd.put(activity, zzic2);
                zzA(activity, zzic2, true);
                return;
            }
            this.zzs.zzay().zzl().zza("setCurrentScreen cannot be called with the same class and name");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0031, code lost:
        if (r2 > 100) goto L_0x0033;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0063, code lost:
        if (r4 > 100) goto L_0x0065;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zzx(Bundle bundle, long j) {
        String str;
        String str2;
        String str3;
        zzic zzic;
        synchronized (this.zzj) {
            if (!this.zzi) {
                this.zzs.zzay().zzl().zza("Cannot log screen view event when the app is in the background.");
                return;
            }
            String string = bundle.getString(FirebaseAnalytics.Param.SCREEN_NAME);
            if (string != null) {
                if (string.length() > 0) {
                    int length = string.length();
                    this.zzs.zzf();
                }
                this.zzs.zzay().zzl().zzb("Invalid screen name length for screen view. Length", Integer.valueOf(string.length()));
                return;
            }
            String string2 = bundle.getString(FirebaseAnalytics.Param.SCREEN_CLASS);
            if (string2 != null) {
                if (string2.length() > 0) {
                    int length2 = string2.length();
                    this.zzs.zzf();
                }
                this.zzs.zzay().zzl().zzb("Invalid screen class length for screen view. Length", Integer.valueOf(string2.length()));
                return;
            }
            if (string2 == null) {
                Activity activity = this.zze;
                if (activity != null) {
                    str = zzl(activity.getClass(), "Activity");
                } else {
                    str = "Activity";
                }
            } else {
                str = string2;
            }
            zzic zzic2 = this.zzb;
            if (this.zzf && zzic2 != null) {
                this.zzf = false;
                boolean zzak = zzku.zzak(zzic2.zzb, str);
                boolean zzak2 = zzku.zzak(zzic2.zza, string);
                if (zzak && zzak2) {
                    this.zzs.zzay().zzl().zza("Ignoring call to log screen view event with duplicate parameters.");
                    return;
                }
            }
            zzeg zzj = this.zzs.zzay().zzj();
            if (string == null) {
                str2 = "null";
            } else {
                str2 = string;
            }
            if (str == null) {
                str3 = "null";
            } else {
                str3 = str;
            }
            zzj.zzc("Logging screen view with name, class", str2, str3);
            if (this.zzb == null) {
                zzic = this.zzc;
            } else {
                zzic = this.zzb;
            }
            zzic zzic3 = new zzic(string, str, this.zzs.zzv().zzq(), true, j);
            this.zzb = zzic3;
            this.zzc = zzic;
            this.zzg = zzic3;
            this.zzs.zzaz().zzp(new zzid(this, bundle, zzic3, zzic, this.zzs.zzav().elapsedRealtime()));
        }
    }

    public final void zzy(String str, zzic zzic) {
        zzg();
        synchronized (this) {
            String str2 = this.zzl;
            if (str2 == null || str2.equals(str) || zzic != null) {
                this.zzl = str;
                this.zzk = zzic;
            }
        }
    }

    private final void zzA(Activity activity, zzic zzic, boolean z) {
        zzic zzic2;
        zzic zzic3 = this.zzb == null ? this.zzc : this.zzb;
        if (zzic.zzb == null) {
            zzic2 = new zzic(zzic.zza, activity != null ? zzl(activity.getClass(), "Activity") : null, zzic.zzc, zzic.zze, zzic.zzf);
        } else {
            zzic2 = zzic;
        }
        this.zzc = this.zzb;
        this.zzb = zzic2;
        this.zzs.zzaz().zzp(new zzie(this, zzic2, zzic3, this.zzs.zzav().elapsedRealtime(), z));
    }
}
