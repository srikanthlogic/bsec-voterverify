package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
final class zzkq extends zzku {
    private static final Class<?> zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzkq() {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzkq(zzkp zzkp) {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzku
    public final void zza(Object obj, long j) {
        Object obj2;
        List list = (List) zzms.zzf(obj, j);
        if (list instanceof zzko) {
            obj2 = ((zzko) list).zze();
        } else if (!zza.isAssignableFrom(list.getClass())) {
            if (!(list instanceof zzln) || !(list instanceof zzkg)) {
                obj2 = Collections.unmodifiableList(list);
            } else {
                zzkg zzkg = (zzkg) list;
                if (zzkg.zzc()) {
                    zzkg.zzb();
                    return;
                }
                return;
            }
        } else {
            return;
        }
        zzms.zzs(obj, j, obj2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzku
    public final <E> void zzb(Object obj, Object obj2, long j) {
        List list = (List) zzms.zzf(obj2, j);
        int size = list.size();
        List list2 = (List) zzms.zzf(obj, j);
        if (list2.isEmpty()) {
            if (list2 instanceof zzko) {
                list2 = new zzkn(size);
            } else if (!(list2 instanceof zzln) || !(list2 instanceof zzkg)) {
                list2 = new ArrayList(size);
            } else {
                list2 = ((zzkg) list2).zzd(size);
            }
            zzms.zzs(obj, j, list2);
        } else if (zza.isAssignableFrom(list2.getClass())) {
            ArrayList arrayList = new ArrayList(list2.size() + size);
            arrayList.addAll(list2);
            zzms.zzs(obj, j, arrayList);
            list2 = arrayList;
        } else if (list2 instanceof zzmn) {
            zzkn zzkn = new zzkn(list2.size() + size);
            zzkn.addAll(zzkn.size(), (zzmn) list2);
            zzms.zzs(obj, j, zzkn);
            list2 = zzkn;
        } else if ((list2 instanceof zzln) && (list2 instanceof zzkg)) {
            zzkg zzkg = (zzkg) list2;
            if (!zzkg.zzc()) {
                zzkg<E> zzd = zzkg.zzd(list2.size() + size);
                zzms.zzs(obj, j, zzd);
                list2 = zzd;
            }
        }
        int size2 = list2.size();
        int size3 = list.size();
        if (size2 > 0 && size3 > 0) {
            list2.addAll(list);
        }
        if (size2 > 0) {
            list = list2;
        }
        zzms.zzs(obj, j, list);
    }
}
