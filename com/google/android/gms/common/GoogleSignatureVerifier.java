package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
@CheckReturnValue
/* loaded from: classes.dex */
public class GoogleSignatureVerifier {
    @Nullable
    private static GoogleSignatureVerifier zza;
    private final Context zzb;
    private volatile String zzc;

    private GoogleSignatureVerifier(Context context) {
        this.zzb = context.getApplicationContext();
    }

    public static GoogleSignatureVerifier getInstance(Context context) {
        Preconditions.checkNotNull(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (zza == null) {
                zzc.zza(context);
                zza = new GoogleSignatureVerifier(context);
            }
        }
        return zza;
    }

    public boolean isUidGoogleSigned(int i) {
        zzs zzs;
        String[] packagesForUid = this.zzb.getPackageManager().getPackagesForUid(i);
        if (packagesForUid != null && packagesForUid.length != 0) {
            int length = packagesForUid.length;
            zzs = null;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    zzs = (zzs) Preconditions.checkNotNull(zzs);
                    break;
                }
                zzs = zza(packagesForUid[i2], false, false);
                if (zzs.zza) {
                    break;
                }
                i2++;
            }
        } else {
            zzs = zzs.zza("no pkgs");
        }
        zzs.zzc();
        return zzs.zza;
    }

    public boolean isPackageGoogleSigned(String str) {
        zzs zza2 = zza(str, false, false);
        zza2.zzc();
        return zza2.zza;
    }

    public static boolean zza(PackageInfo packageInfo, boolean z) {
        zzd zzd;
        if (!(packageInfo == null || packageInfo.signatures == null)) {
            if (z) {
                zzd = zza(packageInfo, zzi.zza);
            } else {
                zzd = zza(packageInfo, zzi.zza[0]);
            }
            if (zzd != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isGooglePublicSignedPackage(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (zza(packageInfo, false)) {
            return true;
        }
        if (zza(packageInfo, true)) {
            if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb)) {
                return true;
            }
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return false;
    }

    private final zzs zza(String str, boolean z, boolean z2) {
        zzs zza2;
        if (str == null) {
            return zzs.zza("null pkg");
        }
        if (str.equals(this.zzc)) {
            return zzs.zza();
        }
        if (zzc.zza()) {
            zza2 = zzc.zza(str, GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb), false, false);
        } else {
            try {
                PackageInfo packageInfo = this.zzb.getPackageManager().getPackageInfo(str, 64);
                boolean honorsDebugCertificates = GooglePlayServicesUtilLight.honorsDebugCertificates(this.zzb);
                if (packageInfo == null) {
                    zza2 = zzs.zza("null pkg");
                } else if (packageInfo.signatures == null || packageInfo.signatures.length != 1) {
                    zza2 = zzs.zza("single cert required");
                } else {
                    zzg zzg = new zzg(packageInfo.signatures[0].toByteArray());
                    String str2 = packageInfo.packageName;
                    zzs zza3 = zzc.zza(str2, (zzd) zzg, honorsDebugCertificates, false);
                    if (!zza3.zza || packageInfo.applicationInfo == null || (packageInfo.applicationInfo.flags & 2) == 0 || !zzc.zza(str2, (zzd) zzg, false, true).zza) {
                        zza2 = zza3;
                    } else {
                        zza2 = zzs.zza("debuggable release cert app rejected");
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                String valueOf = String.valueOf(str);
                return zzs.zza(valueOf.length() != 0 ? "no pkg ".concat(valueOf) : new String("no pkg "), e);
            }
        }
        if (zza2.zza) {
            this.zzc = str;
        }
        return zza2;
    }

    @Nullable
    private static zzd zza(PackageInfo packageInfo, zzd... zzdArr) {
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzg zzg = new zzg(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < zzdArr.length; i++) {
            if (zzdArr[i].equals(zzg)) {
                return zzdArr[i];
            }
        }
        return null;
    }
}
