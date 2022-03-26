package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.net.URL;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzen implements Runnable {
    final /* synthetic */ zzeo zza;
    private final URL zzb;
    private final byte[] zzc;
    private final zzek zzd;
    private final String zze;
    private final Map<String, String> zzf;

    public zzen(zzeo zzeo, String str, URL url, byte[] bArr, Map<String, String> map, zzek zzek) {
        this.zza = zzeo;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(zzek);
        this.zzb = url;
        this.zzc = bArr;
        this.zzd = zzek;
        this.zze = str;
        this.zzf = map;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 11, insn: 0x00e0: MOVE  (r9 I:??[OBJECT, ARRAY]) = (r11 I:??[OBJECT, ARRAY]), block:B:43:0x00dd
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:171)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:143)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:60)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    @Override // java.lang.Runnable
    public final void run() {
        /*
        // Method dump skipped, instructions count: 367
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzen.run():void");
    }
}
