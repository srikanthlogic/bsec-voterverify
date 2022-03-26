package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.internal.base.zas;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public abstract class zal extends LifecycleCallback implements DialogInterface.OnCancelListener {
    protected volatile boolean zaa;
    protected final AtomicReference<zak> zab;
    protected final GoogleApiAvailability zac;
    private final Handler zad;

    public zal(LifecycleFragment lifecycleFragment) {
        this(lifecycleFragment, GoogleApiAvailability.getInstance());
    }

    protected abstract void zaa();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void zaa(ConnectionResult connectionResult, int i);

    public zal(LifecycleFragment lifecycleFragment, GoogleApiAvailability googleApiAvailability) {
        super(lifecycleFragment);
        this.zab = new AtomicReference<>(null);
        this.zad = new zas(Looper.getMainLooper());
        this.zac = googleApiAvailability;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        zaa(new ConnectionResult(13, null), zaa(this.zab.get()));
        zab();
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void onCreate(Bundle bundle) {
        zak zak;
        super.onCreate(bundle);
        if (bundle != null) {
            AtomicReference<zak> atomicReference = this.zab;
            if (bundle.getBoolean("resolving_error", false)) {
                zak = new zak(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1));
            } else {
                zak = null;
            }
            atomicReference.set(zak);
        }
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zak zak = this.zab.get();
        if (zak != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", zak.zaa());
            bundle.putInt("failed_status", zak.zab().getErrorCode());
            bundle.putParcelable("failed_resolution", zak.zab().getResolution());
        }
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void onStart() {
        super.onStart();
        this.zaa = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006c  */
    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void onActivityResult(int i, int i2, Intent intent) {
        zak zak = this.zab.get();
        boolean z = true;
        if (i != 1) {
            if (i == 2) {
                int isGooglePlayServicesAvailable = this.zac.isGooglePlayServicesAvailable(getActivity());
                if (isGooglePlayServicesAvailable != 0) {
                    z = false;
                }
                if (zak != null) {
                    if (zak.zab().getErrorCode() == 18 && isGooglePlayServicesAvailable == 18) {
                        return;
                    }
                    if (z) {
                        zab();
                        return;
                    } else if (zak != null) {
                        zaa(zak.zab(), zak.zaa());
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            z = false;
            if (z) {
            }
        } else {
            if (i2 != -1) {
                if (i2 == 0) {
                    if (zak != null) {
                        int i3 = 13;
                        if (intent != null) {
                            i3 = intent.getIntExtra("<<ResolutionFailureErrorDetail>>", 13);
                        }
                        zak zak2 = new zak(new ConnectionResult(i3, null, zak.zab().toString()), zaa(zak));
                        this.zab.set(zak2);
                        zak = zak2;
                        z = false;
                    } else {
                        return;
                    }
                }
                z = false;
            }
            if (z) {
            }
        }
    }

    @Override // com.google.android.gms.common.api.internal.LifecycleCallback
    public void onStop() {
        super.onStop();
        this.zaa = false;
    }

    public final void zab() {
        this.zab.set(null);
        zaa();
    }

    public final void zab(ConnectionResult connectionResult, int i) {
        zak zak = new zak(connectionResult, i);
        if (this.zab.compareAndSet(null, zak)) {
            this.zad.post(new zan(this, zak));
        }
    }

    private static int zaa(zak zak) {
        if (zak == null) {
            return -1;
        }
        return zak.zaa();
    }
}
