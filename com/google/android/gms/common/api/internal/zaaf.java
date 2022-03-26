package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zau;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.zak;
import com.google.android.gms.signin.zae;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaaf implements zaaw {
    private final zaaz zaa;
    private final Lock zab;
    private final Context zac;
    private final GoogleApiAvailabilityLight zad;
    private ConnectionResult zae;
    private int zaf;
    private int zah;
    private zae zak;
    private boolean zal;
    private boolean zam;
    private boolean zan;
    private IAccountAccessor zao;
    private boolean zap;
    private boolean zaq;
    private final ClientSettings zar;
    private final Map<Api<?>, Boolean> zas;
    private final Api.AbstractClientBuilder<? extends zae, SignInOptions> zat;
    private int zag = 0;
    private final Bundle zai = new Bundle();
    private final Set<Api.AnyClientKey> zaj = new HashSet();
    private ArrayList<Future<?>> zau = new ArrayList<>();

    public zaaf(zaaz zaaz, ClientSettings clientSettings, Map<Api<?>, Boolean> map, GoogleApiAvailabilityLight googleApiAvailabilityLight, Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder, Lock lock, Context context) {
        this.zaa = zaaz;
        this.zar = clientSettings;
        this.zas = map;
        this.zad = googleApiAvailabilityLight;
        this.zat = abstractClientBuilder;
        this.zab = lock;
        this.zac = context;
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final void zaa() {
        this.zaa.zab.clear();
        this.zam = false;
        this.zae = null;
        this.zag = 0;
        this.zal = true;
        this.zan = false;
        this.zap = false;
        HashMap hashMap = new HashMap();
        boolean z = false;
        for (Api<?> api : this.zas.keySet()) {
            Api.Client client = (Api.Client) Preconditions.checkNotNull(this.zaa.zaa.get(api.zac()));
            z |= api.zaa().getPriority() == 1;
            boolean booleanValue = this.zas.get(api).booleanValue();
            if (client.requiresSignIn()) {
                this.zam = true;
                if (booleanValue) {
                    this.zaj.add(api.zac());
                } else {
                    this.zal = false;
                }
            }
            hashMap.put(client, new zaah(this, api, booleanValue));
        }
        if (z) {
            this.zam = false;
        }
        if (this.zam) {
            Preconditions.checkNotNull(this.zar);
            Preconditions.checkNotNull(this.zat);
            this.zar.zaa(Integer.valueOf(System.identityHashCode(this.zaa.zad)));
            zaam zaam = new zaam(this, null);
            Api.AbstractClientBuilder<? extends zae, SignInOptions> abstractClientBuilder = this.zat;
            Context context = this.zac;
            Looper looper = this.zaa.zad.getLooper();
            ClientSettings clientSettings = this.zar;
            this.zak = (zae) abstractClientBuilder.buildClient(context, looper, clientSettings, (ClientSettings) clientSettings.zac(), (GoogleApiClient.ConnectionCallbacks) zaam, (GoogleApiClient.OnConnectionFailedListener) zaam);
        }
        this.zah = this.zaa.zaa.size();
        this.zau.add(zaba.zaa().submit(new zaag(this, hashMap)));
    }

    public final boolean zad() {
        this.zah--;
        int i = this.zah;
        if (i > 0) {
            return false;
        }
        if (i < 0) {
            Log.w("GACConnecting", this.zaa.zad.zac());
            Log.wtf("GACConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            zab(new ConnectionResult(8, null));
            return false;
        }
        ConnectionResult connectionResult = this.zae;
        if (connectionResult == null) {
            return true;
        }
        this.zaa.zac = this.zaf;
        zab(connectionResult);
        return false;
    }

    public final void zaa(zak zak) {
        if (zab(0)) {
            ConnectionResult zaa = zak.zaa();
            if (zaa.isSuccess()) {
                zau zau = (zau) Preconditions.checkNotNull(zak.zab());
                ConnectionResult zab = zau.zab();
                if (!zab.isSuccess()) {
                    String valueOf = String.valueOf(zab);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 48);
                    sb.append("Sign-in succeeded with resolve account failure: ");
                    sb.append(valueOf);
                    Log.wtf("GACConnecting", sb.toString(), new Exception());
                    zab(zab);
                    return;
                }
                this.zan = true;
                this.zao = (IAccountAccessor) Preconditions.checkNotNull(zau.zaa());
                this.zap = zau.zac();
                this.zaq = zau.zad();
                zae();
            } else if (zaa(zaa)) {
                zag();
                zae();
            } else {
                zab(zaa);
            }
        }
    }

    public final void zae() {
        if (this.zah == 0) {
            if (!this.zam || this.zan) {
                ArrayList arrayList = new ArrayList();
                this.zag = 1;
                this.zah = this.zaa.zaa.size();
                for (Api.AnyClientKey<?> anyClientKey : this.zaa.zaa.keySet()) {
                    if (!this.zaa.zab.containsKey(anyClientKey)) {
                        arrayList.add(this.zaa.zaa.get(anyClientKey));
                    } else if (zad()) {
                        zaf();
                    }
                }
                if (!arrayList.isEmpty()) {
                    this.zau.add(zaba.zaa().submit(new zaal(this, arrayList)));
                }
            }
        }
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final void zaa(Bundle bundle) {
        if (zab(1)) {
            if (bundle != null) {
                this.zai.putAll(bundle);
            }
            if (zad()) {
                zaf();
            }
        }
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (zab(1)) {
            zab(connectionResult, api, z);
            if (zad()) {
                zaf();
            }
        }
    }

    private final void zaf() {
        this.zaa.zai();
        zaba.zaa().execute(new zaae(this));
        zae zae = this.zak;
        if (zae != null) {
            if (this.zap) {
                zae.zaa((IAccountAccessor) Preconditions.checkNotNull(this.zao), this.zaq);
            }
            zaa(false);
        }
        for (Api.AnyClientKey<?> anyClientKey : this.zaa.zab.keySet()) {
            ((Api.Client) Preconditions.checkNotNull(this.zaa.zaa.get(anyClientKey))).disconnect();
        }
        this.zaa.zae.zaa(this.zai.isEmpty() ? null : this.zai);
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T zaa(T t) {
        this.zaa.zad.zaa.add(t);
        return t;
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T zab(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final void zac() {
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final boolean zab() {
        zah();
        zaa(true);
        this.zaa.zaa((ConnectionResult) null);
        return true;
    }

    @Override // com.google.android.gms.common.api.internal.zaaw
    public final void zaa(int i) {
        zab(new ConnectionResult(8, null));
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0024, code lost:
        if (r7 != false) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void zab(ConnectionResult connectionResult, Api<?> api, boolean z) {
        boolean z2;
        int priority = api.zaa().getPriority();
        boolean z3 = false;
        if (z) {
            if (connectionResult.hasResolution()) {
                z2 = true;
            } else {
                z2 = this.zad.getErrorResolutionIntent(connectionResult.getErrorCode()) != null;
            }
        }
        if (this.zae == null || priority < this.zaf) {
            z3 = true;
        }
        if (z3) {
            this.zae = connectionResult;
            this.zaf = priority;
        }
        this.zaa.zab.put(api.zac(), connectionResult);
    }

    public final void zag() {
        this.zam = false;
        this.zaa.zad.zac = Collections.emptySet();
        for (Api.AnyClientKey<?> anyClientKey : this.zaj) {
            if (!this.zaa.zab.containsKey(anyClientKey)) {
                this.zaa.zab.put(anyClientKey, new ConnectionResult(17, null));
            }
        }
    }

    public final boolean zaa(ConnectionResult connectionResult) {
        return this.zal && !connectionResult.hasResolution();
    }

    public final void zab(ConnectionResult connectionResult) {
        zah();
        zaa(!connectionResult.hasResolution());
        this.zaa.zaa(connectionResult);
        this.zaa.zae.zaa(connectionResult);
    }

    private final void zaa(boolean z) {
        zae zae = this.zak;
        if (zae != null) {
            if (zae.isConnected() && z) {
                zae.zaa();
            }
            zae.disconnect();
            ClientSettings clientSettings = (ClientSettings) Preconditions.checkNotNull(this.zar);
            this.zao = null;
        }
    }

    private final void zah() {
        ArrayList<Future<?>> arrayList = this.zau;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Future<?> future = arrayList.get(i);
            i++;
            future.cancel(true);
        }
        this.zau.clear();
    }

    public final Set<Scope> zai() {
        ClientSettings clientSettings = this.zar;
        if (clientSettings == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(clientSettings.getRequiredScopes());
        Map<Api<?>, ClientSettings.zaa> zaa = this.zar.zaa();
        for (Api<?> api : zaa.keySet()) {
            if (!this.zaa.zab.containsKey(api.zac())) {
                hashSet.addAll(zaa.get(api).zaa);
            }
        }
        return hashSet;
    }

    public final boolean zab(int i) {
        if (this.zag == i) {
            return true;
        }
        Log.w("GACConnecting", this.zaa.zad.zac());
        String valueOf = String.valueOf(this);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
        sb.append("Unexpected callback in ");
        sb.append(valueOf);
        Log.w("GACConnecting", sb.toString());
        int i2 = this.zah;
        StringBuilder sb2 = new StringBuilder(33);
        sb2.append("mRemainingConnections=");
        sb2.append(i2);
        Log.w("GACConnecting", sb2.toString());
        String zac = zac(this.zag);
        String zac2 = zac(i);
        StringBuilder sb3 = new StringBuilder(String.valueOf(zac).length() + 70 + String.valueOf(zac2).length());
        sb3.append("GoogleApiClient connecting is in step ");
        sb3.append(zac);
        sb3.append(" but received callback for step ");
        sb3.append(zac2);
        Log.e("GACConnecting", sb3.toString(), new Exception());
        zab(new ConnectionResult(8, null));
        return false;
    }

    private static String zac(int i) {
        if (i == 0) {
            return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
        }
        if (i != 1) {
            return "UNKNOWN";
        }
        return "STEP_GETTING_REMOTE_SERVICE";
    }
}
