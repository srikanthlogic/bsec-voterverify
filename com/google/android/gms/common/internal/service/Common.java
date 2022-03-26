package com.google.android.gms.common.internal.service;

import com.google.android.gms.common.api.Api;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class Common {
    public static final Api.ClientKey<zah> CLIENT_KEY = new Api.ClientKey<>();
    private static final Api.AbstractClientBuilder<zah, Api.ApiOptions.NoOptions> zab = new zac();
    public static final Api<Api.ApiOptions.NoOptions> API = new Api<>("Common.API", zab, CLIENT_KEY);
    public static final zab zaa = new zae();
}
