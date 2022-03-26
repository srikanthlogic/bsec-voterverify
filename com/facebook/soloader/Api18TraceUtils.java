package com.facebook.soloader;

import android.os.Trace;
/* loaded from: classes.dex */
class Api18TraceUtils {
    Api18TraceUtils() {
    }

    public static void beginTraceSection(String sectionName) {
        Trace.beginSection(sectionName);
    }

    public static void endSection() {
        Trace.endSection();
    }
}
