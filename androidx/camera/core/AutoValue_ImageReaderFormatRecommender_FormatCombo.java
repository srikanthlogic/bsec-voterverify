package androidx.camera.core;

import androidx.camera.core.ImageReaderFormatRecommender;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_ImageReaderFormatRecommender_FormatCombo extends ImageReaderFormatRecommender.FormatCombo {
    private final int imageAnalysisFormat;
    private final int imageCaptureFormat;

    public AutoValue_ImageReaderFormatRecommender_FormatCombo(int imageCaptureFormat, int imageAnalysisFormat) {
        this.imageCaptureFormat = imageCaptureFormat;
        this.imageAnalysisFormat = imageAnalysisFormat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.core.ImageReaderFormatRecommender.FormatCombo
    public int imageCaptureFormat() {
        return this.imageCaptureFormat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.core.ImageReaderFormatRecommender.FormatCombo
    public int imageAnalysisFormat() {
        return this.imageAnalysisFormat;
    }

    public String toString() {
        return "FormatCombo{imageCaptureFormat=" + this.imageCaptureFormat + ", imageAnalysisFormat=" + this.imageAnalysisFormat + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImageReaderFormatRecommender.FormatCombo)) {
            return false;
        }
        ImageReaderFormatRecommender.FormatCombo that = (ImageReaderFormatRecommender.FormatCombo) o;
        if (this.imageCaptureFormat == that.imageCaptureFormat() && this.imageAnalysisFormat == that.imageAnalysisFormat()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.imageCaptureFormat) * 1000003) ^ this.imageAnalysisFormat;
    }
}
