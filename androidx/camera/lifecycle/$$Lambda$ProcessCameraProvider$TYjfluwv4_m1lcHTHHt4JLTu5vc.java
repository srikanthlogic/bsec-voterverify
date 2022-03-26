package androidx.camera.lifecycle;

import androidx.arch.core.util.Function;
import androidx.camera.core.CameraX;
/* compiled from: lambda */
/* renamed from: androidx.camera.lifecycle.-$$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc implements Function {
    public static final /* synthetic */ $$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc INSTANCE = new $$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc();

    private /* synthetic */ $$Lambda$ProcessCameraProvider$TYjfluwv4_m1lcHTHHt4JLTu5vc() {
    }

    @Override // androidx.arch.core.util.Function
    public final Object apply(Object obj) {
        return ProcessCameraProvider.sAppInstance.setCameraX((CameraX) obj);
    }
}
