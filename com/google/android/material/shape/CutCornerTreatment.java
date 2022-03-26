package com.google.android.material.shape;
/* loaded from: classes.dex */
public class CutCornerTreatment extends CornerTreatment {
    private final float size;

    public CutCornerTreatment(float size) {
        this.size = size;
    }

    @Override // com.google.android.material.shape.CornerTreatment
    public void getCornerPath(float angle, float interpolation, ShapePath shapePath) {
        shapePath.reset(0.0f, this.size * interpolation);
        shapePath.lineTo((float) (Math.sin((double) angle) * ((double) this.size) * ((double) interpolation)), (float) (Math.cos((double) angle) * ((double) this.size) * ((double) interpolation)));
    }
}
