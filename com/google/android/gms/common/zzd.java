package com.google.android.gms.common;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzm;
import com.google.android.gms.common.internal.zzo;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.apache.commons.codec.CharEncoding;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public abstract class zzd extends zzo {
    private int zza;

    public zzd(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 25);
        this.zza = Arrays.hashCode(bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract byte[] zza();

    @Override // java.lang.Object
    public int hashCode() {
        return this.zza;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        IObjectWrapper zzb;
        if (obj == null || !(obj instanceof zzm)) {
            return false;
        }
        try {
            zzm zzm = (zzm) obj;
            if (zzm.zzc() == hashCode() && (zzb = zzm.zzb()) != null) {
                return Arrays.equals(zza(), (byte[]) ObjectWrapper.unwrap(zzb));
            }
            return false;
        } catch (RemoteException e) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
            return false;
        }
    }

    @Override // com.google.android.gms.common.internal.zzm
    public final IObjectWrapper zzb() {
        return ObjectWrapper.wrap(zza());
    }

    @Override // com.google.android.gms.common.internal.zzm
    public final int zzc() {
        return hashCode();
    }

    public static byte[] zza(String str) {
        try {
            return str.getBytes(CharEncoding.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
