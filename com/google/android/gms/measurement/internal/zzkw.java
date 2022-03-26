package com.google.android.gms.measurement.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzkw extends SSLSocketFactory {
    private final SSLSocketFactory zza;

    zzkw() {
        this.zza = HttpsURLConnection.getDefaultSSLSocketFactory();
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket() throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket());
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public final String[] getDefaultCipherSuites() {
        return this.zza.getDefaultCipherSuites();
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public final String[] getSupportedCipherSuites() {
        return this.zza.getSupportedCipherSuites();
    }

    public zzkw(SSLSocketFactory sSLSocketFactory) {
        this.zza = sSLSocketFactory;
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(String str, int i) throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket(str, i));
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket(str, i, inetAddress, i2));
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket(inetAddress, i));
    }

    @Override // javax.net.SocketFactory
    public final Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket(inetAddress, i, inetAddress2, i2));
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public final Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return new zzkv(this, (SSLSocket) this.zza.createSocket(socket, str, i, z));
    }
}