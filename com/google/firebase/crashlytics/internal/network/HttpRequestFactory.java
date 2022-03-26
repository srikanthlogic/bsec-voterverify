package com.google.firebase.crashlytics.internal.network;

import java.util.Collections;
import java.util.Map;
/* loaded from: classes3.dex */
public class HttpRequestFactory {
    public HttpGetRequest buildHttpGetRequest(String url) {
        return buildHttpGetRequest(url, Collections.emptyMap());
    }

    public HttpGetRequest buildHttpGetRequest(String url, Map<String, String> queryParams) {
        return new HttpGetRequest(url, queryParams);
    }
}
