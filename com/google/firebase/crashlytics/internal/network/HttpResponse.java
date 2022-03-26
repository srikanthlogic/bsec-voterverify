package com.google.firebase.crashlytics.internal.network;
/* loaded from: classes3.dex */
public class HttpResponse {
    private final String body;
    private final int code;

    public HttpResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int code() {
        return this.code;
    }

    public String body() {
        return this.body;
    }
}
