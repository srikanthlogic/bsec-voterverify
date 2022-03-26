package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzhy implements Runnable {
    final /* synthetic */ zzhz zza;
    private final URL zzb;
    private final String zzc;
    private final zzfq zzd;

    public zzhy(zzhz zzhz, String str, URL url, byte[] bArr, Map map, zzfq zzfq, byte[] bArr2) {
        this.zza = zzhz;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(zzfq);
        this.zzb = url;
        this.zzd = zzfq;
        this.zzc = str;
    }

    private final void zzb(int i, Exception exc, byte[] bArr, Map<String, List<String>> map) {
        this.zza.zzs.zzaz().zzp(new Runnable(i, exc, bArr, map) { // from class: com.google.android.gms.measurement.internal.zzhx
            public final /* synthetic */ int zzb;
            public final /* synthetic */ Exception zzc;
            public final /* synthetic */ byte[] zzd;
            public final /* synthetic */ Map zze;

            {
                this.zzb = r2;
                this.zzc = r3;
                this.zzd = r4;
                this.zze = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                zzhy.this.zza(this.zzb, this.zzc, this.zzd, this.zze);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0077  */
    @Override // java.lang.Runnable
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final void run() {
        Map<String, List<String>> map;
        int i;
        HttpURLConnection httpURLConnection;
        IOException e;
        Map<String, List<String>> map2;
        Throwable th;
        InputStream inputStream;
        Throwable th2;
        this.zza.zzax();
        try {
            httpURLConnection = this.zza.zza(this.zzb);
            try {
                i = httpURLConnection.getResponseCode();
            } catch (IOException e2) {
                e = e2;
                map = null;
                i = 0;
            } catch (Throwable th3) {
                th = th3;
                map2 = null;
                i = 0;
            }
        } catch (IOException e3) {
            e = e3;
            i = 0;
            map = null;
            httpURLConnection = null;
        } catch (Throwable th4) {
            th = th4;
            i = 0;
            map2 = null;
            httpURLConnection = null;
        }
        try {
            try {
                Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, read);
                        }
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        zzb(i, null, byteArray, headerFields);
                    } catch (Throwable th5) {
                        th2 = th5;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th2;
                    }
                } catch (Throwable th6) {
                    th2 = th6;
                    inputStream = null;
                }
            } catch (IOException e4) {
                e = e4;
                map = null;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                zzb(i, e, null, map);
            } catch (Throwable th7) {
                th = th7;
                map2 = null;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                zzb(i, null, null, map2);
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            if (httpURLConnection != null) {
            }
            zzb(i, e, null, map);
        } catch (Throwable th8) {
            th = th8;
            if (httpURLConnection != null) {
            }
            zzb(i, null, null, map2);
            throw th;
        }
    }

    public final /* synthetic */ void zza(int i, Exception exc, byte[] bArr, Map map) {
        zzfq zzfq = this.zzd;
        zzfq.zza.zzC(this.zzc, i, exc, bArr, map);
    }
}
