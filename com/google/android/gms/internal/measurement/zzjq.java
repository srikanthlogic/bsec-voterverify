package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzjp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzjq<T extends zzjp<T>> {
    private static final zzjq zzb = new zzjq(true);
    final zzme<T, Object> zza = new zzlu(16);
    private boolean zzc;
    private boolean zzd;

    private zzjq() {
    }

    public static <T extends zzjp<T>> zzjq<T> zza() {
        zzjq zzjq = zzb;
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static final void zzd(T t, Object obj) {
        boolean z;
        zzmy zzb2 = t.zzb();
        zzkh.zze(obj);
        zzmy zzmy = zzmy.DOUBLE;
        zzmz zzmz = zzmz.INT;
        switch (zzb2.zza().ordinal()) {
            case 0:
                z = obj instanceof Integer;
                if (z) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 1:
                z = obj instanceof Long;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 2:
                z = obj instanceof Float;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 3:
                z = obj instanceof Double;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 4:
                z = obj instanceof Boolean;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 5:
                z = obj instanceof String;
                if (z) {
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 6:
                if ((obj instanceof zziy) || (obj instanceof byte[])) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 7:
                if ((obj instanceof Integer) || (obj instanceof zzkb)) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            case 8:
                if ((obj instanceof zzlg) || (obj instanceof zzkl)) {
                    return;
                }
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
            default:
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(t.zza()), t.zzb().zza(), obj.getClass().getName()));
        }
    }

    public final /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzjq zzjq = new zzjq();
        for (int i = 0; i < this.zza.zzb(); i++) {
            Map.Entry<T, Object> zzg = this.zza.zzg(i);
            zzjq.zzc(zzg.getKey(), zzg.getValue());
        }
        for (Map.Entry<T, Object> entry : this.zza.zzc()) {
            zzjq.zzc(entry.getKey(), entry.getValue());
        }
        zzjq.zzd = this.zzd;
        return zzjq;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzjq)) {
            return false;
        }
        return this.zza.equals(((zzjq) obj).zza);
    }

    public final int hashCode() {
        return this.zza.hashCode();
    }

    public final void zzb() {
        if (!this.zzc) {
            this.zza.zza();
            this.zzc = true;
        }
    }

    public final void zzc(T t, Object obj) {
        if (!t.zzc()) {
            zzd(t, obj);
        } else if (obj instanceof List) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zzd(t, arrayList.get(i));
            }
            obj = arrayList;
        } else {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        if (obj instanceof zzkl) {
            this.zzd = true;
        }
        this.zza.put(t, obj);
    }

    private zzjq(boolean z) {
        zzb();
        zzb();
    }
}
