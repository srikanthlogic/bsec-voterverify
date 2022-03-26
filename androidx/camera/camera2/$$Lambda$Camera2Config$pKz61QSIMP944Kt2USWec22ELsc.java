package androidx.camera.camera2;

import android.content.Context;
import androidx.camera.core.impl.UseCaseConfigFactory;
/* compiled from: lambda */
/* renamed from: androidx.camera.camera2.-$$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc implements UseCaseConfigFactory.Provider {
    public static final /* synthetic */ $$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc INSTANCE = new $$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc();

    private /* synthetic */ $$Lambda$Camera2Config$pKz61QSIMP944Kt2USWec22ELsc() {
    }

    @Override // androidx.camera.core.impl.UseCaseConfigFactory.Provider
    public final UseCaseConfigFactory newInstance(Context context) {
        return Camera2Config.lambda$defaultConfig$1(context);
    }
}
