package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzjv;
import com.google.android.gms.internal.measurement.zzjz;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public class zzjv<MessageType extends zzjz<MessageType, BuilderType>, BuilderType extends zzjv<MessageType, BuilderType>> extends zzig<MessageType, BuilderType> {
    protected MessageType zza;
    protected boolean zzb = false;
    private final MessageType zzc;

    public zzjv(MessageType messagetype) {
        this.zzc = messagetype;
        this.zza = (MessageType) ((zzjz) messagetype.zzl(4, null, null));
    }

    private static final void zza(MessageType messagetype, MessageType messagetype2) {
        zzlo.zza().zzb(messagetype.getClass()).zzg(messagetype, messagetype2);
    }

    public final MessageType zzaA() {
        Object obj;
        MessageType zzaB = zzaC();
        boolean booleanValue = Boolean.TRUE.booleanValue();
        boolean z = true;
        byte byteValue = ((Byte) zzaB.zzl(1, null, null)).byteValue();
        if (byteValue != 1) {
            if (byteValue == 0) {
                z = false;
            } else {
                boolean zzj = zzlo.zza().zzb(zzaB.getClass()).zzj(zzaB);
                if (booleanValue) {
                    if (true != zzj) {
                        obj = null;
                    } else {
                        obj = zzaB;
                    }
                    zzaB.zzl(2, obj, null);
                }
                z = zzj;
            }
        }
        if (z) {
            return zzaB;
        }
        throw new zzmh(zzaB);
    }

    /* renamed from: zzaB */
    public MessageType zzaC() {
        if (this.zzb) {
            return this.zza;
        }
        MessageType messagetype = this.zza;
        zzlo.zza().zzb(messagetype.getClass()).zzf(messagetype);
        this.zzb = true;
        return this.zza;
    }

    public void zzaE() {
        MessageType messagetype = (MessageType) ((zzjz) this.zza.zzl(4, null, null));
        zza(messagetype, this.zza);
        this.zza = messagetype;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.measurement.zzig
    protected final /* bridge */ /* synthetic */ zzig zzar(zzih zzih) {
        zzay((zzjz) zzih);
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.zzig
    public final /* bridge */ /* synthetic */ zzig zzas(byte[] bArr, int i, int i2) throws zzkj {
        zzaz(bArr, 0, i2, zzjl.zza());
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.zzig
    public final /* bridge */ /* synthetic */ zzig zzat(byte[] bArr, int i, int i2, zzjl zzjl) throws zzkj {
        zzaz(bArr, 0, i2, zzjl);
        return this;
    }

    /* renamed from: zzax */
    public final BuilderType zzaq() {
        BuilderType buildertype = (BuilderType) ((zzjv) this.zzc.zzl(5, null, null));
        buildertype.zzay(zzaC());
        return buildertype;
    }

    public final BuilderType zzay(MessageType messagetype) {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        zza(this.zza, messagetype);
        return this;
    }

    public final BuilderType zzaz(byte[] bArr, int i, int i2, zzjl zzjl) throws zzkj {
        if (this.zzb) {
            zzaE();
            this.zzb = false;
        }
        try {
            zzlo.zza().zzb(this.zza.getClass()).zzh(this.zza, bArr, 0, i2, new zzik(zzjl));
            return this;
        } catch (zzkj e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from byte array should not throw IOException.", e2);
        } catch (IndexOutOfBoundsException e3) {
            throw zzkj.zzf();
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzlh
    public final /* bridge */ /* synthetic */ zzlg zzbL() {
        return this.zzc;
    }
}
