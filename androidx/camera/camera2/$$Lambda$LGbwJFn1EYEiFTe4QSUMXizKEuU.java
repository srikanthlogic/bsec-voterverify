package androidx.camera.camera2;

import android.content.Context;
import androidx.camera.camera2.internal.Camera2CameraFactory;
import androidx.camera.core.impl.CameraFactory;
import androidx.camera.core.impl.CameraThreadConfig;
/* compiled from: lambda */
/* renamed from: androidx.camera.camera2.-$$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU implements CameraFactory.Provider {
    public static final /* synthetic */ $$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU INSTANCE = new $$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU();

    private /* synthetic */ $$Lambda$LGbwJFn1EYEiFTe4QSUMXizKEuU() {
    }

    @Override // androidx.camera.core.impl.CameraFactory.Provider
    public final CameraFactory newInstance(Context context, CameraThreadConfig cameraThreadConfig) {
        return new Camera2CameraFactory(context, cameraThreadConfig);
    }
}
