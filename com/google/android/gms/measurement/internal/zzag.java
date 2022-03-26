package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzag {
    public static final zzag zza = new zzag(null, null);
    private final Boolean zzb;
    private final Boolean zzc;

    public zzag(Boolean bool, Boolean bool2) {
        this.zzb = bool;
        this.zzc = bool2;
    }

    public static zzag zza(Bundle bundle) {
        if (bundle == null) {
            return zza;
        }
        return new zzag(zzo(bundle.getString("ad_storage")), zzo(bundle.getString("analytics_storage")));
    }

    public static zzag zzb(String str) {
        Boolean bool;
        Boolean bool2 = null;
        if (str != null) {
            if (str.length() >= 3) {
                bool = zzp(str.charAt(2));
            } else {
                bool = null;
            }
            if (str.length() >= 4) {
                bool2 = zzp(str.charAt(3));
            }
        } else {
            bool = null;
        }
        return new zzag(bool, bool2);
    }

    static Boolean zzg(Boolean bool, Boolean bool2) {
        if (bool == null) {
            return bool2;
        }
        if (bool2 == null) {
            return bool;
        }
        boolean z = false;
        if (bool.booleanValue() && bool2.booleanValue()) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static String zzh(Bundle bundle) {
        String string = bundle.getString("ad_storage");
        if (string != null && zzo(string) == null) {
            return string;
        }
        String string2 = bundle.getString("analytics_storage");
        if (string2 == null || zzo(string2) != null) {
            return null;
        }
        return string2;
    }

    public static boolean zzl(int i, int i2) {
        return i <= i2;
    }

    static final int zzn(Boolean bool) {
        if (bool == null) {
            return 0;
        }
        return bool.booleanValue() ? 1 : 2;
    }

    private static Boolean zzo(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals("granted")) {
            return Boolean.TRUE;
        }
        if (str.equals("denied")) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static Boolean zzp(char c) {
        if (c == '-') {
            return null;
        }
        if (c == '0') {
            return Boolean.FALSE;
        }
        if (c != '1') {
            return null;
        }
        return Boolean.TRUE;
    }

    private static final char zzq(Boolean bool) {
        if (bool == null) {
            return '-';
        }
        return bool.booleanValue() ? '1' : '0';
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzag)) {
            return false;
        }
        zzag zzag = (zzag) obj;
        if (zzn(this.zzb) == zzn(zzag.zzb) && zzn(this.zzc) == zzn(zzag.zzc)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return ((zzn(this.zzb) + 527) * 31) + zzn(this.zzc);
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder("ConsentSettings: ");
        sb.append("adStorage=");
        Boolean bool = this.zzb;
        String str2 = "denied";
        if (bool == null) {
            sb.append("uninitialized");
        } else {
            if (true != bool.booleanValue()) {
                str = str2;
            } else {
                str = "granted";
            }
            sb.append(str);
        }
        sb.append(", analyticsStorage=");
        Boolean bool2 = this.zzc;
        if (bool2 == null) {
            sb.append("uninitialized");
        } else {
            if (true == bool2.booleanValue()) {
                str2 = "granted";
            }
            sb.append(str2);
        }
        return sb.toString();
    }

    public final zzag zzc(zzag zzag) {
        return new zzag(zzg(this.zzb, zzag.zzb), zzg(this.zzc, zzag.zzc));
    }

    public final zzag zzd(zzag zzag) {
        Boolean bool = this.zzb;
        if (bool == null) {
            bool = zzag.zzb;
        }
        Boolean bool2 = this.zzc;
        if (bool2 == null) {
            bool2 = zzag.zzc;
        }
        return new zzag(bool, bool2);
    }

    public final Boolean zze() {
        return this.zzb;
    }

    public final Boolean zzf() {
        return this.zzc;
    }

    public final String zzi() {
        return "G1" + zzq(this.zzb) + zzq(this.zzc);
    }

    public final boolean zzj() {
        Boolean bool = this.zzb;
        return bool == null || bool.booleanValue();
    }

    public final boolean zzk() {
        Boolean bool = this.zzc;
        return bool == null || bool.booleanValue();
    }

    public final boolean zzm(zzag zzag) {
        if (this.zzb != Boolean.FALSE || zzag.zzb == Boolean.FALSE) {
            return this.zzc == Boolean.FALSE && zzag.zzc != Boolean.FALSE;
        }
        return true;
    }
}
