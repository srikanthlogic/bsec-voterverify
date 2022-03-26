package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public class Response<T extends Result> {
    private T zza;

    public Response() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Response(T t) {
        this.zza = t;
    }

    protected T getResult() {
        return this.zza;
    }

    public void setResult(T t) {
        this.zza = t;
    }
}
