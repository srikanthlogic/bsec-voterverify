package androidx.camera.core.internal;

import android.util.Log;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.UseCase;
import androidx.camera.core.VideoCapture;
import java.util.List;
/* loaded from: classes.dex */
public final class UseCaseOccupancy {
    private static final String TAG = "UseCaseOccupancy";

    private UseCaseOccupancy() {
    }

    public static boolean checkUseCaseLimitNotExceeded(List<UseCase> useCases) {
        int imageCaptureCount = 0;
        int videoCaptureCount = 0;
        for (UseCase useCase : useCases) {
            if (useCase instanceof ImageCapture) {
                imageCaptureCount++;
            } else if (useCase instanceof VideoCapture) {
                videoCaptureCount++;
            }
        }
        if (imageCaptureCount > 1) {
            Log.e(TAG, "Exceeded max simultaneously bound image capture use cases.");
            return false;
        } else if (videoCaptureCount <= 1) {
            return true;
        } else {
            Log.e(TAG, "Exceeded max simultaneously bound video capture use cases.");
            return false;
        }
    }
}
