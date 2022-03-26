package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzig;
import com.google.android.gms.internal.measurement.zzih;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzih<MessageType extends zzih<MessageType, BuilderType>, BuilderType extends zzig<MessageType, BuilderType>> implements zzlg {
    protected int zzb = 0;

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> void zzbq(Iterable<T> iterable, List<? super T> list) {
        zzkh.zze(iterable);
        if (iterable instanceof zzko) {
            List<?> zzh = ((zzko) iterable).zzh();
            zzko zzko = (zzko) list;
            int size = list.size();
            for (Object obj : zzh) {
                if (obj == null) {
                    int size2 = zzko.size();
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(size2 - size);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size3 = zzko.size() - 1; size3 >= size; size3--) {
                        zzko.remove(size3);
                    }
                    throw new NullPointerException(sb2);
                } else if (obj instanceof zziy) {
                    zzko.zzi((zziy) obj);
                } else {
                    zzko.add((String) obj);
                }
            }
        } else if (!(iterable instanceof zzln)) {
            if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
                ((ArrayList) list).ensureCapacity(list.size() + iterable.size());
            }
            int size4 = list.size();
            for (T t : iterable) {
                if (t == 0) {
                    int size5 = list.size();
                    StringBuilder sb3 = new StringBuilder(37);
                    sb3.append("Element at index ");
                    sb3.append(size5 - size4);
                    sb3.append(" is null.");
                    String sb4 = sb3.toString();
                    for (int size6 = list.size() - 1; size6 >= size4; size6--) {
                        list.remove(size6);
                    }
                    throw new NullPointerException(sb4);
                }
                list.add(t);
            }
        } else {
            list.addAll(iterable);
        }
    }

    public int zzbo() {
        throw null;
    }

    @Override // com.google.android.gms.internal.measurement.zzlg
    public final zziy zzbp() {
        try {
            int zzbt = zzbt();
            zziy zziy = zziy.zzb;
            byte[] bArr = new byte[zzbt];
            zzjg zzC = zzjg.zzC(bArr);
            zzbH(zzC);
            zzC.zzD();
            return new zziv(bArr);
        } catch (IOException e) {
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 72);
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ByteString threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void zzbr(int i) {
        throw null;
    }

    public final byte[] zzbs() {
        try {
            byte[] bArr = new byte[zzbt()];
            zzjg zzC = zzjg.zzC(bArr);
            zzbH(zzC);
            zzC.zzD();
            return bArr;
        } catch (IOException e) {
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 72);
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a byte array threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }
}
