package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zau;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.zab;
import com.google.android.gms.signin.internal.zak;
import com.google.android.gms.signin.zae;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zace extends zab implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static Api.AbstractClientBuilder<? extends zae, SignInOptions> zaa = com.google.android.gms.signin.zab.zaa;
    private final Context zab;
    private final Handler zac;
    private final Api.AbstractClientBuilder<? extends zae, SignInOptions> zad;
    private Set<Scope> zae;
    private ClientSettings zaf;
    private zae zag;
    private zach zah;

    public zace(Context context, Handler handler, ClientSettings clientSettings) {
        this(context, handler, clientSettings, zaa);
    }

    private zace(Context context, Handler handler, ClientSettings clientSettings, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder) {
        this.zab = context;
        this.zac = handler;
        this.zaf = (ClientSettings) Preconditions.checkNotNull(clientSettings, "ClientSettings must not be null");
        this.zae = clientSettings.getRequiredScopes();
        this.zad = abstractClientBuilder;
    }

    public final void zaa(zach zach) {
        zae zae = this.zag;
        if (zae != null) {
            zae.disconnect();
        }
        this.zaf.zaa(Integer.valueOf(System.identityHashCode(this)));
        Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder = this.zad;
        Context context = this.zab;
        Looper looper = this.zac.getLooper();
        ClientSettings clientSettings = this.zaf;
        this.zag = (zae) abstractClientBuilder.buildClient(context, looper, clientSettings, (ClientSettings) clientSettings.zac(), (GoogleApiClient.ConnectionCallbacks) this, (GoogleApiClient.OnConnectionFailedListener) this);
        this.zah = zach;
        Set<Scope> set = this.zae;
        if (set == null || set.isEmpty()) {
            this.zac.post(new zacg(this));
        } else {
            this.zag.zab();
        }
    }

    public final void zaa() {
        zae zae = this.zag;
        if (zae != null) {
            zae.disconnect();
        }
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        this.zag.zaa(this);
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        this.zag.disconnect();
    }

    @Override // com.google.android.gms.common.api.internal.OnConnectionFailedListener
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zah.zaa(connectionResult);
    }

    @Override // com.google.android.gms.signin.internal.zab, com.google.android.gms.signin.internal.zae
    public final void zaa(zak zak) {
        this.zac.post(new zacf(this, zak));
    }

    public final void zab(zak zak) {
        ConnectionResult zaa2 = zak.zaa();
        if (zaa2.isSuccess()) {
            zau zau = (zau) Preconditions.checkNotNull(zak.zab());
            ConnectionResult zab = zau.zab();
            if (!zab.isSuccess()) {
                String valueOf = String.valueOf(zab);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 48);
                sb.append("Sign-in succeeded with resolve account failure: ");
                sb.append(valueOf);
                Log.wtf("SignInCoordinator", sb.toString(), new Exception());
                this.zah.zaa(zab);
                this.zag.disconnect();
                return;
            }
            this.zah.zaa(zau.zaa(), this.zae);
        } else {
            this.zah.zaa(zaa2);
        }
        this.zag.disconnect();
    }
}
