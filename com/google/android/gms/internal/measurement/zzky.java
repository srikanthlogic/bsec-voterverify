package com.google.android.gms.internal.measurement;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzky implements zzls {
    private static final zzle zza = new zzkw();
    private final zzle zzb;

    public zzky() {
        zzle zzle;
        zzle[] zzleArr = new zzle[2];
        zzleArr[0] = zzju.zza();
        try {
            zzle = (zzle) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            zzle = zza;
        }
        zzleArr[1] = zzle;
        zzkx zzkx = new zzkx(zzleArr);
        zzkh.zzf(zzkx, "messageInfoFactory");
        this.zzb = zzkx;
    }

    private static boolean zzb(zzld zzld) {
        return zzld.zzc() == 1;
    }

    @Override // com.google.android.gms.internal.measurement.zzls
    public final <T> zzlr<T> zza(Class<T> cls) {
        zzlt.zzG(cls);
        zzld zzb = this.zzb.zzb(cls);
        if (zzb.zzb()) {
            if (zzjz.class.isAssignableFrom(cls)) {
                return zzlk.zzc(zzlt.zzB(), zzjo.zzb(), zzb.zza());
            }
            return zzlk.zzc(zzlt.zzz(), zzjo.zza(), zzb.zza());
        } else if (zzjz.class.isAssignableFrom(cls)) {
            if (zzb(zzb)) {
                return zzlj.zzk(cls, zzb, zzlm.zzb(), zzku.zzd(), zzlt.zzB(), zzjo.zzb(), zzlc.zzb());
            }
            return zzlj.zzk(cls, zzb, zzlm.zzb(), zzku.zzd(), zzlt.zzB(), null, zzlc.zzb());
        } else if (zzb(zzb)) {
            return zzlj.zzk(cls, zzb, zzlm.zza(), zzku.zzc(), zzlt.zzz(), zzjo.zza(), zzlc.zza());
        } else {
            return zzlj.zzk(cls, zzb, zzlm.zza(), zzku.zzc(), zzlt.zzA(), null, zzlc.zza());
        }
    }
}
