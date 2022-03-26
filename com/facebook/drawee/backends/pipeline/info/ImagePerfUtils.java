package com.facebook.drawee.backends.pipeline.info;

import androidx.core.os.EnvironmentCompat;
import com.google.firebase.analytics.FirebaseAnalytics;
/* loaded from: classes.dex */
public class ImagePerfUtils {
    public static String toString(int imageLoadStatus) {
        if (imageLoadStatus == 0) {
            return "requested";
        }
        if (imageLoadStatus == 1) {
            return "origin_available";
        }
        if (imageLoadStatus == 2) {
            return "intermediate_available";
        }
        if (imageLoadStatus == 3) {
            return FirebaseAnalytics.Param.SUCCESS;
        }
        if (imageLoadStatus == 4) {
            return "canceled";
        }
        if (imageLoadStatus != 5) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return "error";
    }

    private ImagePerfUtils() {
    }
}
