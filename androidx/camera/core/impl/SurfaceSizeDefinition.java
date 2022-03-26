package androidx.camera.core.impl;

import android.util.Size;
/* loaded from: classes.dex */
public abstract class SurfaceSizeDefinition {
    public abstract Size getAnalysisSize();

    public abstract Size getPreviewSize();

    public abstract Size getRecordSize();

    public static SurfaceSizeDefinition create(Size analysisSize, Size previewSize, Size recordSize) {
        return new AutoValue_SurfaceSizeDefinition(analysisSize, previewSize, recordSize);
    }
}
