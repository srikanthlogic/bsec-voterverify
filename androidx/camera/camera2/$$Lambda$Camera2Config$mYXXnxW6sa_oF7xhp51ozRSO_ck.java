package androidx.camera.camera2;

import android.content.Context;
import androidx.camera.core.impl.CameraDeviceSurfaceManager;
/* compiled from: lambda */
/* renamed from: androidx.camera.camera2.-$$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck implements CameraDeviceSurfaceManager.Provider {
    public static final /* synthetic */ $$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck INSTANCE = new $$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck();

    private /* synthetic */ $$Lambda$Camera2Config$mYXXnxW6sa_oF7xhp51ozRSO_ck() {
    }

    @Override // androidx.camera.core.impl.CameraDeviceSurfaceManager.Provider
    public final CameraDeviceSurfaceManager newInstance(Context context) {
        return Camera2Config.lambda$defaultConfig$0(context);
    }
}
