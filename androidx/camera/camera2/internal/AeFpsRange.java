package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.util.Range;
import androidx.camera.camera2.impl.Camera2ImplConfig;
/* loaded from: classes.dex */
final class AeFpsRange {
    private Range<Integer> mAeTargetFpsRange;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AeFpsRange(CameraCharacteristics cameraCharacteristics) {
        this.mAeTargetFpsRange = null;
        Integer hardwareLevel = (Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (hardwareLevel != null && hardwareLevel.intValue() == 2) {
            this.mAeTargetFpsRange = pickSuitableFpsRange((Range[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
        }
    }

    public void addAeFpsRangeOptions(Camera2ImplConfig.Builder configBuilder) {
        if (this.mAeTargetFpsRange != null) {
            configBuilder.setCaptureRequestOption(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.mAeTargetFpsRange);
        }
    }

    private static Range<Integer> getCorrectedFpsRange(Range<Integer> fpsRange) {
        int newUpper = fpsRange.getUpper().intValue();
        int newLower = fpsRange.getLower().intValue();
        if (fpsRange.getUpper().intValue() >= 1000) {
            newUpper = fpsRange.getUpper().intValue() / 1000;
        }
        if (fpsRange.getLower().intValue() >= 1000) {
            newLower = fpsRange.getLower().intValue() / 1000;
        }
        return new Range<>(Integer.valueOf(newLower), Integer.valueOf(newUpper));
    }

    private static Range<Integer> pickSuitableFpsRange(Range<Integer>[] availableFpsRanges) {
        if (availableFpsRanges == null || availableFpsRanges.length == 0) {
            return null;
        }
        Range<Integer> pickedRange = null;
        for (Range<Integer> fpsRange : availableFpsRanges) {
            Range<Integer> fpsRange2 = getCorrectedFpsRange(fpsRange);
            if (fpsRange2.getUpper().intValue() == 30) {
                if (pickedRange == null) {
                    pickedRange = fpsRange2;
                } else if (fpsRange2.getLower().intValue() < pickedRange.getLower().intValue()) {
                    pickedRange = fpsRange2;
                }
            }
        }
        return pickedRange;
    }
}
