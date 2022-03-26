package com.facebook.imagepipeline.nativecode;

import com.facebook.soloader.SoLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ImagePipelineNativeLoader {
    public static final List<String> DEPENDENCIES = Collections.unmodifiableList(new ArrayList<>());
    public static final String DSO_NAME;

    public static void load() {
        SoLoader.loadLibrary(DSO_NAME);
    }
}
