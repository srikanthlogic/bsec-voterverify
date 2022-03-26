package com.facebook.imagepipeline.nativecode;

import android.os.Build;
import com.facebook.soloader.SoLoader;
/* loaded from: classes.dex */
public class NativeJpegTranscoderSoLoader {
    private static boolean sInitialized;

    public static synchronized void ensure() {
        synchronized (NativeJpegTranscoderSoLoader.class) {
            if (!sInitialized) {
                if (Build.VERSION.SDK_INT <= 16) {
                    try {
                        SoLoader.loadLibrary("fb_jpegturbo");
                    } catch (UnsatisfiedLinkError e) {
                    }
                }
                SoLoader.loadLibrary("native-imagetranscoder");
                sInitialized = true;
            }
        }
    }
}
