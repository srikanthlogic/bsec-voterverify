package com.google.firebase.crashlytics.internal.common;

import java.io.File;
import java.io.FilenameFilter;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.common.-$$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k implements FilenameFilter {
    public static final /* synthetic */ $$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k INSTANCE = new $$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k();

    private /* synthetic */ $$Lambda$CrashlyticsController$S4ND4vdF1CBimHR5Yfiv04HIz8k() {
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        return str.startsWith(CrashlyticsController.APP_EXCEPTION_MARKER_PREFIX);
    }
}
