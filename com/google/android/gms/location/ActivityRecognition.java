package com.google.android.gms.location;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.location.zzaz;
import com.google.android.gms.internal.location.zzg;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public class ActivityRecognition {
    public static final String CLIENT_NAME = "activity_recognition";
    private static final Api.ClientKey<zzaz> zza = new Api.ClientKey<>();
    private static final Api.AbstractClientBuilder<zzaz, Api.ApiOptions.NoOptions> zzb = new zza();
    public static final Api<Api.ApiOptions.NoOptions> API = new Api<>("ActivityRecognition.API", zzb, zza);
    @Deprecated
    public static final ActivityRecognitionApi ActivityRecognitionApi = new zzg();

    private ActivityRecognition() {
    }

    public static ActivityRecognitionClient getClient(Activity activity) {
        return new ActivityRecognitionClient(activity);
    }

    public static ActivityRecognitionClient getClient(Context context) {
        return new ActivityRecognitionClient(context);
    }
}
