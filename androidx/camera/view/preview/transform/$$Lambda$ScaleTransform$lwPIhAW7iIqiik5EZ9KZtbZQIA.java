package androidx.camera.view.preview.transform;

import androidx.camera.view.preview.transform.ScaleTransform;
/* compiled from: lambda */
/* renamed from: androidx.camera.view.preview.transform.-$$Lambda$ScaleTransform$l-wPIhAW7iIqiik5EZ9KZtbZQIA  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$ScaleTransform$lwPIhAW7iIqiik5EZ9KZtbZQIA implements ScaleTransform.FloatBiFunction {
    public static final /* synthetic */ $$Lambda$ScaleTransform$lwPIhAW7iIqiik5EZ9KZtbZQIA INSTANCE = new $$Lambda$ScaleTransform$lwPIhAW7iIqiik5EZ9KZtbZQIA();

    private /* synthetic */ $$Lambda$ScaleTransform$lwPIhAW7iIqiik5EZ9KZtbZQIA() {
    }

    @Override // androidx.camera.view.preview.transform.ScaleTransform.FloatBiFunction
    public final float apply(float f, float f2) {
        return Math.min(f, f2);
    }
}
