package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzjv;
import com.google.android.gms.internal.measurement.zzjz;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzjz<MessageType extends zzjz<MessageType, BuilderType>, BuilderType extends zzjv<MessageType, BuilderType>> extends zzih<MessageType, BuilderType> {
    private static final Map<Object, zzjz<?, ?>> zza = new ConcurrentHashMap();
    protected zzmj zzc = zzmj.zzc();
    protected int zzd = -1;

    public static <E> zzkg<E> zzbA() {
        return zzlp.zze();
    }

    public static <E> zzkg<E> zzbB(zzkg<E> zzkg) {
        int i;
        int size = zzkg.size();
        if (size == 0) {
            i = 10;
        } else {
            i = size + size;
        }
        return zzkg.zzd(i);
    }

    public static Object zzbE(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
            }
        }
    }

    protected static Object zzbF(zzlg zzlg, String str, Object[] objArr) {
        return new zzlq(zzlg, str, objArr);
    }

    public static <T extends zzjz> void zzbG(Class<T> cls, T t) {
        zza.put(cls, t);
    }

    public static <T extends zzjz> T zzbw(Class<T> cls) {
        zzjz<?, ?> zzjz = zza.get(cls);
        if (zzjz == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzjz = zza.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzjz == null) {
            zzjz = (zzjz) ((zzjz) zzms.zze(cls)).zzl(6, null, null);
            if (zzjz != null) {
                zza.put(cls, zzjz);
            } else {
                throw new IllegalStateException();
            }
        }
        return zzjz;
    }

    protected static zzke zzbx() {
        return zzka.zzf();
    }

    protected static zzkf zzby() {
        return zzkv.zzf();
    }

    public static zzkf zzbz(zzkf zzkf) {
        int i;
        int size = zzkf.size();
        if (size == 0) {
            i = 10;
        } else {
            i = size + size;
        }
        return zzkf.zze(i);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return zzlo.zza().zzb(getClass()).zzi(this, (zzjz) obj);
        }
        return false;
    }

    public final int hashCode() {
        int i = this.zzb;
        if (i != 0) {
            return i;
        }
        int zzb = zzlo.zza().zzb(getClass()).zzb(this);
        this.zzb = zzb;
        return zzb;
    }

    public final String toString() {
        return zzli.zza(this, super.toString());
    }

    @Override // com.google.android.gms.internal.measurement.zzlg
    public final /* bridge */ /* synthetic */ zzlf zzbC() {
        return (zzjv) zzl(5, null, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzlg
    public final /* bridge */ /* synthetic */ zzlf zzbD() {
        zzjv zzjv = (zzjv) zzl(5, null, null);
        zzjv.zzay(this);
        return zzjv;
    }

    @Override // com.google.android.gms.internal.measurement.zzlg
    public final void zzbH(zzjg zzjg) throws IOException {
        zzlo.zza().zzb(getClass()).zzm(this, zzjh.zza(zzjg));
    }

    @Override // com.google.android.gms.internal.measurement.zzlh
    public final /* bridge */ /* synthetic */ zzlg zzbL() {
        return (zzjz) zzl(6, null, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzih
    public final int zzbo() {
        return this.zzd;
    }

    @Override // com.google.android.gms.internal.measurement.zzih
    public final void zzbr(int i) {
        this.zzd = i;
    }

    @Override // com.google.android.gms.internal.measurement.zzlg
    public final int zzbt() {
        int i = this.zzd;
        if (i != -1) {
            return i;
        }
        int zza2 = zzlo.zza().zzb(getClass()).zza(this);
        this.zzd = zza2;
        return zza2;
    }

    public final <MessageType extends zzjz<MessageType, BuilderType>, BuilderType extends zzjv<MessageType, BuilderType>> BuilderType zzbu() {
        return (BuilderType) ((zzjv) zzl(5, null, null));
    }

    public final BuilderType zzbv() {
        BuilderType buildertype = (BuilderType) ((zzjv) zzl(5, null, null));
        buildertype.zzay(this);
        return buildertype;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object zzl(int i, Object obj, Object obj2);
}
