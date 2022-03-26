package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.internal.service.zaq;
import com.google.android.gms.common.internal.service.zar;
import com.google.android.gms.common.internal.zaaa;
import com.google.android.gms.common.internal.zaac;
import com.google.android.gms.common.internal.zaj;
import com.google.android.gms.common.internal.zao;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.internal.base.zas;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public class GoogleApiManager implements Handler.Callback {
    public static final Status zaa = new Status(4, "Sign-out occurred while this API call was in progress.");
    private static final Status zab = new Status(4, "The user must be signed in to make this API call.");
    private static final Object zag = new Object();
    private static GoogleApiManager zaj;
    private zaaa zah;
    private zaac zai;
    private final Context zak;
    private final GoogleApiAvailability zal;
    private final zaj zam;
    @NotOnlyInitialized
    private final Handler zat;
    private volatile boolean zau;
    private long zac = 5000;
    private long zad = 120000;
    private long zae = 10000;
    private boolean zaf = false;
    private final AtomicInteger zan = new AtomicInteger(1);
    private final AtomicInteger zao = new AtomicInteger(0);
    private final Map<ApiKey<?>, zaa<?>> zap = new ConcurrentHashMap(5, 0.75f, 1);
    private zay zaq = null;
    private final Set<ApiKey<?>> zar = new ArraySet();
    private final Set<ApiKey<?>> zas = new ArraySet();

    public static GoogleApiManager zaa(Context context) {
        GoogleApiManager googleApiManager;
        synchronized (zag) {
            if (zaj == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zaj = new GoogleApiManager(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            googleApiManager = zaj;
        }
        return googleApiManager;
    }

    /* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
    /* loaded from: classes.dex */
    public static class zab {
        private final ApiKey<?> zaa;
        private final Feature zab;

        private zab(ApiKey<?> apiKey, Feature feature) {
            this.zaa = apiKey;
            this.zab = feature;
        }

        public final boolean equals(Object obj) {
            if (obj == null || !(obj instanceof zab)) {
                return false;
            }
            zab zab = (zab) obj;
            if (!Objects.equal(this.zaa, zab.zaa) || !Objects.equal(this.zab, zab.zab)) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            return Objects.hashCode(this.zaa, this.zab);
        }

        public final String toString() {
            return Objects.toStringHelper(this).add("key", this.zaa).add("feature", this.zab).toString();
        }

        /* synthetic */ zab(ApiKey apiKey, Feature feature, zabd zabd) {
            this(apiKey, feature);
        }
    }

    /* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
    /* loaded from: classes.dex */
    public class zac implements zach, BaseGmsClient.ConnectionProgressReportCallbacks {
        private final Api.Client zab;
        private final ApiKey<?> zac;
        private IAccountAccessor zad = null;
        private Set<Scope> zae = null;
        private boolean zaf = false;

        public zac(Api.Client client, ApiKey<?> apiKey) {
            GoogleApiManager.this = r1;
            this.zab = client;
            this.zac = apiKey;
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
        public final void onReportServiceBinding(ConnectionResult connectionResult) {
            GoogleApiManager.this.zat.post(new zabj(this, connectionResult));
        }

        @Override // com.google.android.gms.common.api.internal.zach
        public final void zaa(ConnectionResult connectionResult) {
            zaa zaa = (zaa) GoogleApiManager.this.zap.get(this.zac);
            if (zaa != null) {
                zaa.zaa(connectionResult);
            }
        }

        @Override // com.google.android.gms.common.api.internal.zach
        public final void zaa(IAccountAccessor iAccountAccessor, Set<Scope> set) {
            if (iAccountAccessor == null || set == null) {
                Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
                zaa(new ConnectionResult(4));
                return;
            }
            this.zad = iAccountAccessor;
            this.zae = set;
            zaa();
        }

        public final void zaa() {
            IAccountAccessor iAccountAccessor;
            if (this.zaf && (iAccountAccessor = this.zad) != null) {
                this.zab.getRemoteService(iAccountAccessor, this.zae);
            }
        }
    }

    public static GoogleApiManager zaa() {
        GoogleApiManager googleApiManager;
        synchronized (zag) {
            Preconditions.checkNotNull(zaj, "Must guarantee manager is non-null before using getInstance");
            googleApiManager = zaj;
        }
        return googleApiManager;
    }

    /* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
    /* loaded from: classes.dex */
    public class zaa<O extends Api.ApiOptions> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, zap {
        @NotOnlyInitialized
        private final Api.Client zac;
        private final ApiKey<O> zad;
        private final int zah;
        private final zace zai;
        private boolean zaj;
        private final Queue<zab> zab = new LinkedList();
        private final Set<zaj> zaf = new HashSet();
        private final Map<ListenerHolder.ListenerKey<?>, zabv> zag = new HashMap();
        private final List<zab> zak = new ArrayList();
        private ConnectionResult zal = null;
        private int zam = 0;
        private final zav zae = new zav();

        public zaa(GoogleApi<O> googleApi) {
            GoogleApiManager.this = r3;
            this.zac = googleApi.zaa(r3.zat.getLooper(), this);
            this.zad = googleApi.getApiKey();
            this.zah = googleApi.zaa();
            if (this.zac.requiresSignIn()) {
                this.zai = googleApi.zaa(r3.zak, r3.zat);
            } else {
                this.zai = null;
            }
        }

        @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
        public final void onConnected(Bundle bundle) {
            if (Looper.myLooper() == GoogleApiManager.this.zat.getLooper()) {
                zao();
            } else {
                GoogleApiManager.this.zat.post(new zabf(this));
            }
        }

        public final void zao() {
            zad();
            zac(ConnectionResult.RESULT_SUCCESS);
            zaq();
            Iterator<zabv> it = this.zag.values().iterator();
            while (it.hasNext()) {
                zabv next = it.next();
                if (zaa(next.zaa.getRequiredFeatures()) != null) {
                    it.remove();
                } else {
                    try {
                        next.zaa.registerListener(this.zac, new TaskCompletionSource<>());
                    } catch (DeadObjectException e) {
                        onConnectionSuspended(3);
                        this.zac.disconnect("DeadObjectException thrown while calling register listener method.");
                    } catch (RemoteException e2) {
                        it.remove();
                    }
                }
            }
            zap();
            zar();
        }

        @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
        public final void onConnectionSuspended(int i) {
            if (Looper.myLooper() == GoogleApiManager.this.zat.getLooper()) {
                zaa(i);
            } else {
                GoogleApiManager.this.zat.post(new zabe(this, i));
            }
        }

        public final void zaa(int i) {
            zad();
            this.zaj = true;
            this.zae.zaa(i, this.zac.getLastDisconnectMessage());
            GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 9, this.zad), GoogleApiManager.this.zac);
            GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 11, this.zad), GoogleApiManager.this.zad);
            GoogleApiManager.this.zam.zaa();
            for (zabv zabv : this.zag.values()) {
                zabv.zac.run();
            }
        }

        public final void zaa(ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            Api.Client client = this.zac;
            String name = client.getClass().getName();
            String valueOf = String.valueOf(connectionResult);
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 25 + String.valueOf(valueOf).length());
            sb.append("onSignInFailed for ");
            sb.append(name);
            sb.append(" with ");
            sb.append(valueOf);
            client.disconnect(sb.toString());
            onConnectionFailed(connectionResult);
        }

        private final boolean zab(ConnectionResult connectionResult) {
            synchronized (GoogleApiManager.zag) {
                if (GoogleApiManager.this.zaq == null || !GoogleApiManager.this.zar.contains(this.zad)) {
                    return false;
                }
                GoogleApiManager.this.zaq.zab(connectionResult, this.zah);
                return true;
            }
        }

        @Override // com.google.android.gms.common.api.internal.zap
        public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
            if (Looper.myLooper() == GoogleApiManager.this.zat.getLooper()) {
                onConnectionFailed(connectionResult);
            } else {
                GoogleApiManager.this.zat.post(new zabh(this, connectionResult));
            }
        }

        @Override // com.google.android.gms.common.api.internal.OnConnectionFailedListener
        public final void onConnectionFailed(ConnectionResult connectionResult) {
            zaa(connectionResult, (Exception) null);
        }

        private final void zaa(ConnectionResult connectionResult, Exception exc) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            zace zace = this.zai;
            if (zace != null) {
                zace.zaa();
            }
            zad();
            GoogleApiManager.this.zam.zaa();
            zac(connectionResult);
            if (this.zac instanceof zar) {
                GoogleApiManager.this.zaf = true;
                GoogleApiManager.this.zat.sendMessageDelayed(GoogleApiManager.this.zat.obtainMessage(19), 300000);
            }
            if (connectionResult.getErrorCode() == 4) {
                zaa(GoogleApiManager.zab);
            } else if (this.zab.isEmpty()) {
                this.zal = connectionResult;
            } else if (exc != null) {
                Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
                zaa((Status) null, exc, false);
            } else if (!GoogleApiManager.this.zau) {
                zaa(zad(connectionResult));
            } else {
                zaa(zad(connectionResult), (Exception) null, true);
                if (!this.zab.isEmpty() && !zab(connectionResult) && !GoogleApiManager.this.zaa(connectionResult, this.zah)) {
                    if (connectionResult.getErrorCode() == 18) {
                        this.zaj = true;
                    }
                    if (this.zaj) {
                        GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 9, this.zad), GoogleApiManager.this.zac);
                    } else {
                        zaa(zad(connectionResult));
                    }
                }
            }
        }

        private final void zap() {
            ArrayList arrayList = new ArrayList(this.zab);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                zab zab = (zab) obj;
                if (!this.zac.isConnected()) {
                    return;
                }
                if (zab(zab)) {
                    this.zab.remove(zab);
                }
            }
        }

        public final void zaa(zab zab) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            if (!this.zac.isConnected()) {
                this.zab.add(zab);
                ConnectionResult connectionResult = this.zal;
                if (connectionResult == null || !connectionResult.hasResolution()) {
                    zai();
                } else {
                    onConnectionFailed(this.zal);
                }
            } else if (zab(zab)) {
                zar();
            } else {
                this.zab.add(zab);
            }
        }

        public final void zaa() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            zaa(GoogleApiManager.zaa);
            this.zae.zab();
            for (ListenerHolder.ListenerKey listenerKey : (ListenerHolder.ListenerKey[]) this.zag.keySet().toArray(new ListenerHolder.ListenerKey[0])) {
                zaa(new zag(listenerKey, new TaskCompletionSource()));
            }
            zac(new ConnectionResult(4));
            if (this.zac.isConnected()) {
                this.zac.onUserSignOut(new zabg(this));
            }
        }

        public final Api.Client zab() {
            return this.zac;
        }

        public final Map<ListenerHolder.ListenerKey<?>, zabv> zac() {
            return this.zag;
        }

        public final void zad() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            this.zal = null;
        }

        public final ConnectionResult zae() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            return this.zal;
        }

        private final boolean zab(zab zab) {
            if (!(zab instanceof zad)) {
                zac(zab);
                return true;
            }
            zad zad = (zad) zab;
            Feature zaa = zaa(zad.zac(this));
            if (zaa == null) {
                zac(zab);
                return true;
            }
            String name = this.zac.getClass().getName();
            String name2 = zaa.getName();
            long version = zaa.getVersion();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 77 + String.valueOf(name2).length());
            sb.append(name);
            sb.append(" could not execute call because it requires feature (");
            sb.append(name2);
            sb.append(", ");
            sb.append(version);
            sb.append(").");
            Log.w("GoogleApiManager", sb.toString());
            if (!GoogleApiManager.this.zau || !zad.zad(this)) {
                zad.zaa(new UnsupportedApiCallException(zaa));
                return true;
            }
            zab zab2 = new zab(this.zad, zaa, null);
            int indexOf = this.zak.indexOf(zab2);
            if (indexOf >= 0) {
                zab zab3 = this.zak.get(indexOf);
                GoogleApiManager.this.zat.removeMessages(15, zab3);
                GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 15, zab3), GoogleApiManager.this.zac);
                return false;
            }
            this.zak.add(zab2);
            GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 15, zab2), GoogleApiManager.this.zac);
            GoogleApiManager.this.zat.sendMessageDelayed(Message.obtain(GoogleApiManager.this.zat, 16, zab2), GoogleApiManager.this.zad);
            ConnectionResult connectionResult = new ConnectionResult(2, null);
            if (zab(connectionResult)) {
                return false;
            }
            GoogleApiManager.this.zaa(connectionResult, this.zah);
            return false;
        }

        private final void zac(zab zab) {
            zab.zaa(this.zae, zak());
            try {
                zab.zaa((zaa<?>) this);
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zac.disconnect("DeadObjectException thrown while running ApiCallRunner.");
            } catch (Throwable th) {
                throw new IllegalStateException(String.format("Error in GoogleApi implementation for client %s.", this.zac.getClass().getName()), th);
            }
        }

        private final void zaa(Status status, Exception exc, boolean z) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            boolean z2 = true;
            boolean z3 = status == null;
            if (exc != null) {
                z2 = false;
            }
            if (z3 != z2) {
                Iterator<zab> it = this.zab.iterator();
                while (it.hasNext()) {
                    zab next = it.next();
                    if (!z || next.zaa == 2) {
                        if (status != null) {
                            next.zaa(status);
                        } else {
                            next.zaa(exc);
                        }
                        it.remove();
                    }
                }
                return;
            }
            throw new IllegalArgumentException("Status XOR exception should be null");
        }

        public final void zaa(Status status) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            zaa(status, (Exception) null, false);
        }

        public final void zaf() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            if (this.zaj) {
                zai();
            }
        }

        private final void zaq() {
            if (this.zaj) {
                GoogleApiManager.this.zat.removeMessages(11, this.zad);
                GoogleApiManager.this.zat.removeMessages(9, this.zad);
                this.zaj = false;
            }
        }

        public final void zag() {
            Status status;
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            if (this.zaj) {
                zaq();
                if (GoogleApiManager.this.zal.isGooglePlayServicesAvailable(GoogleApiManager.this.zak) == 18) {
                    status = new Status(21, "Connection timed out waiting for Google Play services update to complete.");
                } else {
                    status = new Status(22, "API failed to connect while resuming due to an unknown error.");
                }
                zaa(status);
                this.zac.disconnect("Timing out connection while resuming.");
            }
        }

        private final void zar() {
            GoogleApiManager.this.zat.removeMessages(12, this.zad);
            GoogleApiManager.this.zat.sendMessageDelayed(GoogleApiManager.this.zat.obtainMessage(12, this.zad), GoogleApiManager.this.zae);
        }

        public final boolean zah() {
            return zaa(true);
        }

        public final boolean zaa(boolean z) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            if (!this.zac.isConnected() || this.zag.size() != 0) {
                return false;
            }
            if (this.zae.zaa()) {
                if (z) {
                    zar();
                }
                return false;
            }
            this.zac.disconnect("Timing out service connection.");
            return true;
        }

        public final void zai() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            if (!this.zac.isConnected() && !this.zac.isConnecting()) {
                try {
                    int zaa = GoogleApiManager.this.zam.zaa(GoogleApiManager.this.zak, this.zac);
                    if (zaa != 0) {
                        ConnectionResult connectionResult = new ConnectionResult(zaa, null);
                        String name = this.zac.getClass().getName();
                        String valueOf = String.valueOf(connectionResult);
                        StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 35 + String.valueOf(valueOf).length());
                        sb.append("The service for ");
                        sb.append(name);
                        sb.append(" is not available: ");
                        sb.append(valueOf);
                        Log.w("GoogleApiManager", sb.toString());
                        onConnectionFailed(connectionResult);
                        return;
                    }
                    zac zac = new zac(this.zac, this.zad);
                    if (this.zac.requiresSignIn()) {
                        ((zace) Preconditions.checkNotNull(this.zai)).zaa(zac);
                    }
                    try {
                        this.zac.connect(zac);
                    } catch (SecurityException e) {
                        zaa(new ConnectionResult(10), e);
                    }
                } catch (IllegalStateException e2) {
                    zaa(new ConnectionResult(10), e2);
                }
            }
        }

        public final void zaa(zaj zaj) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.zat);
            this.zaf.add(zaj);
        }

        private final void zac(ConnectionResult connectionResult) {
            for (zaj zaj : this.zaf) {
                String str = null;
                if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
                    str = this.zac.getEndpointPackageName();
                }
                zaj.zaa(this.zad, connectionResult, str);
            }
            this.zaf.clear();
        }

        final boolean zaj() {
            return this.zac.isConnected();
        }

        public final boolean zak() {
            return this.zac.requiresSignIn();
        }

        public final int zal() {
            return this.zah;
        }

        private final Feature zaa(Feature[] featureArr) {
            if (featureArr == null || featureArr.length == 0) {
                return null;
            }
            Feature[] availableFeatures = this.zac.getAvailableFeatures();
            if (availableFeatures == null) {
                availableFeatures = new Feature[0];
            }
            ArrayMap arrayMap = new ArrayMap(availableFeatures.length);
            for (Feature feature : availableFeatures) {
                arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
            }
            for (Feature feature2 : featureArr) {
                Long l = (Long) arrayMap.get(feature2.getName());
                if (l == null || l.longValue() < feature2.getVersion()) {
                    return feature2;
                }
            }
            return null;
        }

        public final void zaa(zab zab) {
            if (!this.zak.contains(zab) || this.zaj) {
                return;
            }
            if (!this.zac.isConnected()) {
                zai();
            } else {
                zap();
            }
        }

        public final void zab(zab zab) {
            Feature[] zac;
            if (this.zak.remove(zab)) {
                GoogleApiManager.this.zat.removeMessages(15, zab);
                GoogleApiManager.this.zat.removeMessages(16, zab);
                Feature feature = zab.zab;
                ArrayList arrayList = new ArrayList(this.zab.size());
                for (zab zab2 : this.zab) {
                    if ((zab2 instanceof zad) && (zac = ((zad) zab2).zac(this)) != null && ArrayUtils.contains(zac, feature)) {
                        arrayList.add(zab2);
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList2.get(i);
                    i++;
                    zab zab3 = (zab) obj;
                    this.zab.remove(zab3);
                    zab3.zaa(new UnsupportedApiCallException(feature));
                }
            }
        }

        public final int zam() {
            return this.zam;
        }

        public final void zan() {
            this.zam++;
        }

        private final Status zad(ConnectionResult connectionResult) {
            return GoogleApiManager.zab((ApiKey<?>) this.zad, connectionResult);
        }

        public static /* synthetic */ void zaa(zaa zaa, int i) {
            zaa.zaa(i);
        }
    }

    public static void reportSignOut() {
        synchronized (zag) {
            if (zaj != null) {
                GoogleApiManager googleApiManager = zaj;
                googleApiManager.zao.incrementAndGet();
                googleApiManager.zat.sendMessageAtFrontOfQueue(googleApiManager.zat.obtainMessage(10));
            }
        }
    }

    private GoogleApiManager(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zau = true;
        this.zak = context;
        this.zat = new zas(looper, this);
        this.zal = googleApiAvailability;
        this.zam = new zaj(googleApiAvailability);
        if (DeviceProperties.isAuto(context)) {
            this.zau = false;
        }
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(6));
    }

    public final int zab() {
        return this.zan.getAndIncrement();
    }

    public final void zaa(GoogleApi<?> googleApi) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(7, googleApi));
    }

    private final zaa<?> zac(GoogleApi<?> googleApi) {
        ApiKey<?> apiKey = googleApi.getApiKey();
        zaa<?> zaa2 = this.zap.get(apiKey);
        if (zaa2 == null) {
            zaa2 = new zaa<>(googleApi);
            this.zap.put(apiKey, zaa2);
        }
        if (zaa2.zak()) {
            this.zas.add(apiKey);
        }
        zaa2.zai();
        return zaa2;
    }

    public final void zaa(zay zay) {
        synchronized (zag) {
            if (this.zaq != zay) {
                this.zaq = zay;
                this.zar.clear();
            }
            this.zar.addAll(zay.zac());
        }
    }

    public final void zab(zay zay) {
        synchronized (zag) {
            if (this.zaq == zay) {
                this.zaq = null;
                this.zar.clear();
            }
        }
    }

    public final zaa zaa(ApiKey<?> apiKey) {
        return this.zap.get(apiKey);
    }

    public final Task<Map<ApiKey<?>, String>> zaa(Iterable<? extends HasApiKey<?>> iterable) {
        zaj zaj2 = new zaj(iterable);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(2, zaj2));
        return zaj2.zab();
    }

    public final void zac() {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(3));
    }

    public final Task<Boolean> zab(GoogleApi<?> googleApi) {
        zaz zaz = new zaz(googleApi.getApiKey());
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(14, zaz));
        return zaz.zab().getTask();
    }

    public final <O extends Api.ApiOptions> void zaa(GoogleApi<O> googleApi, int i, BaseImplementation.ApiMethodImpl<? extends Result, Api.AnyClient> apiMethodImpl) {
        zaf zaf = new zaf(i, apiMethodImpl);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, new zabu(zaf, this.zao.get(), googleApi)));
    }

    public final <O extends Api.ApiOptions, ResultT> void zaa(GoogleApi<O> googleApi, int i, TaskApiCall<Api.AnyClient, ResultT> taskApiCall, TaskCompletionSource<ResultT> taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        zaa((TaskCompletionSource) taskCompletionSource, taskApiCall.zab(), (GoogleApi<?>) googleApi);
        zah zah = new zah(i, taskApiCall, taskCompletionSource, statusExceptionMapper);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(4, new zabu(zah, this.zao.get(), googleApi)));
    }

    public final boolean zad() {
        if (this.zaf) {
            return false;
        }
        RootTelemetryConfiguration config = RootTelemetryConfigManager.getInstance().getConfig();
        if (config != null && !config.getMethodInvocationTelemetryEnabled()) {
            return false;
        }
        int zaa2 = this.zam.zaa(this.zak, 203390000);
        if (zaa2 == -1 || zaa2 == 0) {
            return true;
        }
        return false;
    }

    public final <O extends Api.ApiOptions> Task<Void> zaa(GoogleApi<O> googleApi, RegisterListenerMethod<Api.AnyClient, ?> registerListenerMethod, UnregisterListenerMethod<Api.AnyClient, ?> unregisterListenerMethod, Runnable runnable) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zaa(taskCompletionSource, registerListenerMethod.zab(), (GoogleApi<?>) googleApi);
        zae zae = new zae(new zabv(registerListenerMethod, unregisterListenerMethod, runnable), taskCompletionSource);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(8, new zabu(zae, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends Api.ApiOptions> Task<Boolean> zaa(GoogleApi<O> googleApi, ListenerHolder.ListenerKey<?> listenerKey, int i) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zaa(taskCompletionSource, i, (GoogleApi<?>) googleApi);
        zag zag2 = new zag(listenerKey, taskCompletionSource);
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(13, new zabu(zag2, this.zao.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    private final <T> void zaa(TaskCompletionSource<T> taskCompletionSource, int i, GoogleApi<?> googleApi) {
        zabr zaa2;
        if (i != 0 && (zaa2 = zabr.zaa(this, i, googleApi.getApiKey())) != null) {
            Task<T> task = taskCompletionSource.getTask();
            Handler handler = this.zat;
            handler.getClass();
            task.addOnCompleteListener(zabc.zaa(handler), zaa2);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        zaa<?> zaa2;
        long j = 300000;
        switch (message.what) {
            case 1:
                if (((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zae = j;
                this.zat.removeMessages(12);
                for (ApiKey<?> apiKey : this.zap.keySet()) {
                    Handler handler = this.zat;
                    handler.sendMessageDelayed(handler.obtainMessage(12, apiKey), this.zae);
                }
                break;
            case 2:
                zaj zaj2 = (zaj) message.obj;
                Iterator<ApiKey<?>> it = zaj2.zaa().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    } else {
                        ApiKey<?> next = it.next();
                        zaa<?> zaa3 = this.zap.get(next);
                        if (zaa3 == null) {
                            zaj2.zaa(next, new ConnectionResult(13), null);
                            break;
                        } else if (zaa3.zaj()) {
                            zaj2.zaa(next, ConnectionResult.RESULT_SUCCESS, zaa3.zab().getEndpointPackageName());
                        } else {
                            ConnectionResult zae = zaa3.zae();
                            if (zae != null) {
                                zaj2.zaa(next, zae, null);
                            } else {
                                zaa3.zaa(zaj2);
                                zaa3.zai();
                            }
                        }
                    }
                }
            case 3:
                for (zaa<?> zaa4 : this.zap.values()) {
                    zaa4.zad();
                    zaa4.zai();
                }
                break;
            case 4:
            case 8:
            case 13:
                zabu zabu = (zabu) message.obj;
                zaa<?> zaa5 = this.zap.get(zabu.zac.getApiKey());
                if (zaa5 == null) {
                    zaa5 = zac(zabu.zac);
                }
                if (!zaa5.zak() || this.zao.get() == zabu.zab) {
                    zaa5.zaa(zabu.zaa);
                    break;
                } else {
                    zabu.zaa.zaa(zaa);
                    zaa5.zaa();
                    break;
                }
            case 5:
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator<zaa<?>> it2 = this.zap.values().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        zaa2 = it2.next();
                        if (zaa2.zal() == i) {
                        }
                    } else {
                        zaa2 = null;
                    }
                }
                if (zaa2 != null) {
                    if (connectionResult.getErrorCode() == 13) {
                        String errorString = this.zal.getErrorString(connectionResult.getErrorCode());
                        String errorMessage = connectionResult.getErrorMessage();
                        StringBuilder sb = new StringBuilder(String.valueOf(errorString).length() + 69 + String.valueOf(errorMessage).length());
                        sb.append("Error resolution was canceled by the user, original error message: ");
                        sb.append(errorString);
                        sb.append(": ");
                        sb.append(errorMessage);
                        zaa2.zaa(new Status(17, sb.toString()));
                        break;
                    } else {
                        zaa2.zaa(zab(((zaa) zaa2).zad, connectionResult));
                        break;
                    }
                } else {
                    StringBuilder sb2 = new StringBuilder(76);
                    sb2.append("Could not find API instance ");
                    sb2.append(i);
                    sb2.append(" while trying to fail enqueued calls.");
                    Log.wtf("GoogleApiManager", sb2.toString(), new Exception());
                    break;
                }
            case 6:
                if (this.zak.getApplicationContext() instanceof Application) {
                    BackgroundDetector.initialize((Application) this.zak.getApplicationContext());
                    BackgroundDetector.getInstance().addListener(new zabd(this));
                    if (!BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) {
                        this.zae = 300000;
                        break;
                    }
                }
                break;
            case 7:
                zac((GoogleApi) message.obj);
                break;
            case 9:
                if (this.zap.containsKey(message.obj)) {
                    this.zap.get(message.obj).zaf();
                    break;
                }
                break;
            case 10:
                for (ApiKey<?> apiKey2 : this.zas) {
                    zaa<?> remove = this.zap.remove(apiKey2);
                    if (remove != null) {
                        remove.zaa();
                    }
                }
                this.zas.clear();
                break;
            case 11:
                if (this.zap.containsKey(message.obj)) {
                    this.zap.get(message.obj).zag();
                    break;
                }
                break;
            case 12:
                if (this.zap.containsKey(message.obj)) {
                    this.zap.get(message.obj).zah();
                    break;
                }
                break;
            case 14:
                zaz zaz = (zaz) message.obj;
                ApiKey<?> zaa6 = zaz.zaa();
                if (!this.zap.containsKey(zaa6)) {
                    zaz.zab().setResult(false);
                    break;
                } else {
                    zaz.zab().setResult(Boolean.valueOf(this.zap.get(zaa6).zaa(false)));
                    break;
                }
            case 15:
                zab zab2 = (zab) message.obj;
                if (this.zap.containsKey(zab2.zaa)) {
                    this.zap.get(zab2.zaa).zaa(zab2);
                    break;
                }
                break;
            case 16:
                zab zab3 = (zab) message.obj;
                if (this.zap.containsKey(zab3.zaa)) {
                    this.zap.get(zab3.zaa).zab(zab3);
                    break;
                }
                break;
            case 17:
                zag();
                break;
            case 18:
                zabq zabq = (zabq) message.obj;
                if (zabq.zac == 0) {
                    zah().zaa(new zaaa(zabq.zab, Arrays.asList(zabq.zaa)));
                    break;
                } else {
                    zaaa zaaa = this.zah;
                    if (zaaa != null) {
                        List<zao> zab4 = zaaa.zab();
                        if (this.zah.zaa() != zabq.zab || (zab4 != null && zab4.size() >= zabq.zad)) {
                            this.zat.removeMessages(17);
                            zag();
                        } else {
                            this.zah.zaa(zabq.zaa);
                        }
                    }
                    if (this.zah == null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(zabq.zaa);
                        this.zah = new zaaa(zabq.zab, arrayList);
                        Handler handler2 = this.zat;
                        handler2.sendMessageDelayed(handler2.obtainMessage(17), zabq.zac);
                        break;
                    }
                }
                break;
            case 19:
                this.zaf = false;
                break;
            default:
                int i2 = message.what;
                StringBuilder sb3 = new StringBuilder(31);
                sb3.append("Unknown message id: ");
                sb3.append(i2);
                Log.w("GoogleApiManager", sb3.toString());
                return false;
        }
        return true;
    }

    final boolean zaa(ConnectionResult connectionResult, int i) {
        return this.zal.zaa(this.zak, connectionResult, i);
    }

    public final void zab(ConnectionResult connectionResult, int i) {
        if (!zaa(connectionResult, i)) {
            Handler handler = this.zat;
            handler.sendMessage(handler.obtainMessage(5, i, 0, connectionResult));
        }
    }

    public static Status zab(ApiKey<?> apiKey, ConnectionResult connectionResult) {
        String zaa2 = apiKey.zaa();
        String valueOf = String.valueOf(connectionResult);
        StringBuilder sb = new StringBuilder(String.valueOf(zaa2).length() + 63 + String.valueOf(valueOf).length());
        sb.append("API: ");
        sb.append(zaa2);
        sb.append(" is not available on this device. Connection failed with: ");
        sb.append(valueOf);
        return new Status(connectionResult, sb.toString());
    }

    public final void zaa(zao zao, int i, long j, int i2) {
        Handler handler = this.zat;
        handler.sendMessage(handler.obtainMessage(18, new zabq(zao, i, j, i2)));
    }

    private final void zag() {
        zaaa zaaa = this.zah;
        if (zaaa != null) {
            if (zaaa.zaa() > 0 || zad()) {
                zah().zaa(zaaa);
            }
            this.zah = null;
        }
    }

    private final zaac zah() {
        if (this.zai == null) {
            this.zai = new zaq(this.zak);
        }
        return this.zai;
    }
}
