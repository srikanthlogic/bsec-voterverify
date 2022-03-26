package androidx.camera.view.preview.transform;

import androidx.camera.view.preview.transform.ScaleTransform;
/* compiled from: lambda */
/* renamed from: androidx.camera.view.preview.transform.-$$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw implements ScaleTransform.FloatBiFunction {
    public static final /* synthetic */ $$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw INSTANCE = new $$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw();

    private /* synthetic */ $$Lambda$ScaleTransform$d3P8984_eeiWSAQFGMGoCy9GDSw() {
    }

    @Override // androidx.camera.view.preview.transform.ScaleTransform.FloatBiFunction
    public final float apply(float f, float f2) {
        return Math.max(f, f2);
    }
}
