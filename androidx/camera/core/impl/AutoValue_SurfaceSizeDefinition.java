package androidx.camera.core.impl;

import android.util.Size;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_SurfaceSizeDefinition extends SurfaceSizeDefinition {
    private final Size analysisSize;
    private final Size previewSize;
    private final Size recordSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SurfaceSizeDefinition(Size analysisSize, Size previewSize, Size recordSize) {
        if (analysisSize != null) {
            this.analysisSize = analysisSize;
            if (previewSize != null) {
                this.previewSize = previewSize;
                if (recordSize != null) {
                    this.recordSize = recordSize;
                    return;
                }
                throw new NullPointerException("Null recordSize");
            }
            throw new NullPointerException("Null previewSize");
        }
        throw new NullPointerException("Null analysisSize");
    }

    @Override // androidx.camera.core.impl.SurfaceSizeDefinition
    public Size getAnalysisSize() {
        return this.analysisSize;
    }

    @Override // androidx.camera.core.impl.SurfaceSizeDefinition
    public Size getPreviewSize() {
        return this.previewSize;
    }

    @Override // androidx.camera.core.impl.SurfaceSizeDefinition
    public Size getRecordSize() {
        return this.recordSize;
    }

    public String toString() {
        return "SurfaceSizeDefinition{analysisSize=" + this.analysisSize + ", previewSize=" + this.previewSize + ", recordSize=" + this.recordSize + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SurfaceSizeDefinition)) {
            return false;
        }
        SurfaceSizeDefinition that = (SurfaceSizeDefinition) o;
        if (!this.analysisSize.equals(that.getAnalysisSize()) || !this.previewSize.equals(that.getPreviewSize()) || !this.recordSize.equals(that.getRecordSize())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.analysisSize.hashCode()) * 1000003) ^ this.previewSize.hashCode()) * 1000003) ^ this.recordSize.hashCode();
    }
}
