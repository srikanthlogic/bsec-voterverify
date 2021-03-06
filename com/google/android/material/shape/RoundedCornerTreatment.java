package com.google.android.material.shape;
/* loaded from: classes.dex */
public class RoundedCornerTreatment extends CornerTreatment {
    private final float radius;

    public RoundedCornerTreatment(float radius) {
        this.radius = radius;
    }

    @Override // com.google.android.material.shape.CornerTreatment
    public void getCornerPath(float angle, float interpolation, ShapePath shapePath) {
        shapePath.reset(0.0f, this.radius * interpolation);
        float f = this.radius;
        shapePath.addArc(0.0f, 0.0f, f * 2.0f * interpolation, f * 2.0f * interpolation, angle + 180.0f, 90.0f);
    }
}
