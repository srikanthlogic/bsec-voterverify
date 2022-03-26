package com.google.android.gms.internal.measurement;

import android.os.Binder;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzhc {
    public static <V> V zza(zzhd<V> zzhd) {
        try {
            return zzhd.zza();
        } catch (SecurityException e) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return zzhd.zza();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
