package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import java.util.concurrent.Callable;
import javax.annotation.CheckReturnValue;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
@CheckReturnValue
/* loaded from: classes.dex */
public final class zzc {
    private static volatile zzr zza;
    private static final Object zzb = new Object();
    private static Context zzc;

    public static synchronized void zza(Context context) {
        synchronized (zzc.class) {
            if (zzc != null) {
                Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
            } else if (context != null) {
                zzc = context.getApplicationContext();
            }
        }
    }

    private static void zzb() throws DynamiteModule.LoadingException {
        if (zza == null) {
            Preconditions.checkNotNull(zzc);
            synchronized (zzb) {
                if (zza == null) {
                    zza = zzq.zza(DynamiteModule.load(zzc, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.googlecertificates").instantiate("com.google.android.gms.common.GoogleCertificatesImpl"));
                }
            }
        }
    }

    public static zzs zza(String str, boolean z, boolean z2, boolean z3) {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return zzb(str, z, false, false);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    public static zzs zza(String str, zzd zzd, boolean z, boolean z2) {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return zzb(str, zzd, z, z2);
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 0, insn: 0x0022: INVOKE  (r0 I:android.os.StrictMode$ThreadPolicy) type: STATIC call: android.os.StrictMode.setThreadPolicy(android.os.StrictMode$ThreadPolicy):void, block:B:11:0x0022
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:171)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:143)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:60)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    static boolean zza() {
        /*
            android.os.StrictMode$ThreadPolicy r0 = android.os.StrictMode.allowThreadDiskReads()
            zzb()     // Catch: LoadingException -> 0x0015, RemoteException -> 0x0013, all -> 0x0011
            com.google.android.gms.common.internal.zzr r1 = com.google.android.gms.common.zzc.zza     // Catch: LoadingException -> 0x0015, RemoteException -> 0x0013, all -> 0x0011
            boolean r1 = r1.zza()     // Catch: LoadingException -> 0x0015, RemoteException -> 0x0013, all -> 0x0011
            android.os.StrictMode.setThreadPolicy(r0)
            return r1
        L_0x0011:
            r1 = move-exception
            goto L_0x0022
        L_0x0013:
            r1 = move-exception
            goto L_0x0016
        L_0x0015:
            r1 = move-exception
        L_0x0016:
            java.lang.String r2 = "GoogleCertificates"
            java.lang.String r3 = "Failed to get Google certificates from remote"
            android.util.Log.e(r2, r3, r1)     // Catch: all -> 0x0011
            android.os.StrictMode.setThreadPolicy(r0)
            r0 = 0
            return r0
        L_0x0022:
            android.os.StrictMode.setThreadPolicy(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zzc.zza():boolean");
    }

    private static zzs zzb(String str, boolean z, boolean z2, boolean z3) {
        Preconditions.checkNotNull(zzc);
        try {
            zzb();
            try {
                zzl zza2 = zza.zza(new zzj(str, z, z2, ObjectWrapper.wrap(zzc).asBinder(), false));
                if (zza2.zza()) {
                    return zzs.zza();
                }
                String zzb2 = zza2.zzb();
                if (zzb2 == null) {
                    zzb2 = "error checking package certificate";
                }
                if (zza2.zzc().equals(zzo.PACKAGE_NOT_FOUND)) {
                    return zzs.zza(zzb2, new PackageManager.NameNotFoundException());
                }
                return zzs.zza(zzb2);
            } catch (RemoteException e) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                return zzs.zza("module call", e);
            }
        } catch (DynamiteModule.LoadingException e2) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e2);
            String valueOf = String.valueOf(e2.getMessage());
            return zzs.zza(valueOf.length() != 0 ? "module init: ".concat(valueOf) : new String("module init: "), e2);
        }
    }

    private static zzs zzb(String str, zzd zzd, boolean z, boolean z2) {
        try {
            zzb();
            Preconditions.checkNotNull(zzc);
            try {
                if (zza.zza(new zzq(str, zzd, z, z2), ObjectWrapper.wrap(zzc.getPackageManager()))) {
                    return zzs.zza();
                }
                return zzs.zza(new Callable(z, str, zzd) { // from class: com.google.android.gms.common.zze
                    private final boolean zza;
                    private final String zzb;
                    private final zzd zzc;

                    /* JADX INFO: Access modifiers changed from: package-private */
                    {
                        this.zza = r1;
                        this.zzb = r2;
                        this.zzc = r3;
                    }

                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return zzc.zza(this.zza, this.zzb, this.zzc);
                    }
                });
            } catch (RemoteException e) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
                return zzs.zza("module call", e);
            }
        } catch (DynamiteModule.LoadingException e2) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e2);
            String valueOf = String.valueOf(e2.getMessage());
            return zzs.zza(valueOf.length() != 0 ? "module init: ".concat(valueOf) : new String("module init: "), e2);
        }
    }

    public static final /* synthetic */ String zza(boolean z, String str, zzd zzd) throws Exception {
        boolean z2 = true;
        if (z || !zzb(str, zzd, true, false).zza) {
            z2 = false;
        }
        return zzs.zza(str, zzd, z, z2);
    }
}
