package com.google.android.gms.measurement.internal;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzeo extends zzkd {
    private final SSLSocketFactory zza;

    public zzeo(zzkn zzkn) {
        super(zzkn);
        this.zza = Build.VERSION.SDK_INT < 19 ? new zzkw(HttpsURLConnection.getDefaultSSLSocketFactory()) : null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final HttpURLConnection zza(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        if (openConnection instanceof HttpURLConnection) {
            SSLSocketFactory sSLSocketFactory = this.zza;
            if (sSLSocketFactory != null && (openConnection instanceof HttpsURLConnection)) {
                ((HttpsURLConnection) openConnection).setSSLSocketFactory(sSLSocketFactory);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
            httpURLConnection.setDefaultUseCaches(false);
            this.zzs.zzf();
            httpURLConnection.setConnectTimeout(60000);
            this.zzs.zzf();
            httpURLConnection.setReadTimeout(61000);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        }
        throw new IOException("Failed to obtain HTTP connection");
    }

    @Override // com.google.android.gms.measurement.internal.zzkd
    protected final boolean zzb() {
        return false;
    }

    public final boolean zzc() {
        zzY();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.zzs.zzau().getSystemService("connectivity");
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            try {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            } catch (SecurityException e) {
            }
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
