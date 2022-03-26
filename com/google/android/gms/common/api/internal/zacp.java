package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.base.zas;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zacp extends zas {
    private final /* synthetic */ zacn zaa;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zacp(zacn zacn, Looper looper) {
        super(looper);
        this.zaa = zacn;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            PendingResult<?> pendingResult = (PendingResult) message.obj;
            synchronized (this.zaa.zae) {
                zacn zacn = (zacn) Preconditions.checkNotNull(this.zaa.zab);
                if (pendingResult == null) {
                    zacn.zaa(new Status(13, "Transform returned null"));
                } else if (pendingResult instanceof zacc) {
                    zacn.zaa(((zacc) pendingResult).zaa());
                } else {
                    zacn.zaa(pendingResult);
                }
            }
        } else if (i != 1) {
            int i2 = message.what;
            StringBuilder sb = new StringBuilder(70);
            sb.append("TransformationResultHandler received unknown message type: ");
            sb.append(i2);
            Log.e("TransformedResultImpl", sb.toString());
        } else {
            RuntimeException runtimeException = (RuntimeException) message.obj;
            String valueOf = String.valueOf(runtimeException.getMessage());
            Log.e("TransformedResultImpl", valueOf.length() != 0 ? "Runtime exception on the transformation worker thread: ".concat(valueOf) : new String("Runtime exception on the transformation worker thread: "));
            throw runtimeException;
        }
    }
}
