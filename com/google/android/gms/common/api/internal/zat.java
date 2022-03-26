package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zat implements zabn {
    private final /* synthetic */ zas zaa;

    private zat(zas zas) {
        this.zaa = zas;
    }

    @Override // com.google.android.gms.common.api.internal.zabn
    public final void zaa(Bundle bundle) {
        this.zaa.zam.lock();
        try {
            this.zaa.zak = ConnectionResult.RESULT_SUCCESS;
            this.zaa.zah();
        } finally {
            this.zaa.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabn
    public final void zaa(ConnectionResult connectionResult) {
        this.zaa.zam.lock();
        try {
            this.zaa.zak = connectionResult;
            this.zaa.zah();
        } finally {
            this.zaa.zam.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zabn
    public final void zaa(int i, boolean z) {
        this.zaa.zam.lock();
        try {
            if (this.zaa.zal) {
                this.zaa.zal = false;
                this.zaa.zaa(i, z);
                return;
            }
            this.zaa.zal = true;
            this.zaa.zad.onConnectionSuspended(i);
        } finally {
            this.zaa.zam.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zat(zas zas, zar zar) {
        this(zas);
    }
}
