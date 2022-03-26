package com.google.android.gms.internal.measurement;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzja extends zzjc {
    private final byte[] zzb;
    private int zzd;
    private int zze = Integer.MAX_VALUE;
    private int zzc = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzja(byte[] bArr, int i, int i2, boolean z, zziz zziz) {
        super(null);
        this.zzb = bArr;
    }

    public final int zza(int i) throws zzkj {
        int i2 = this.zze;
        this.zze = 0;
        int i3 = this.zzc + this.zzd;
        this.zzc = i3;
        if (i3 > 0) {
            this.zzd = i3;
            this.zzc = 0;
        } else {
            this.zzd = 0;
        }
        return i2;
    }
}
