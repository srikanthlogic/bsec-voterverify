package com.google.android.gms.common;

import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class zzt extends zzs {
    private final Callable<String> zzb;

    private zzt(Callable<String> callable) {
        super(false, null, null);
        this.zzb = callable;
    }

    @Override // com.google.android.gms.common.zzs
    final String zzb() {
        try {
            return this.zzb.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
