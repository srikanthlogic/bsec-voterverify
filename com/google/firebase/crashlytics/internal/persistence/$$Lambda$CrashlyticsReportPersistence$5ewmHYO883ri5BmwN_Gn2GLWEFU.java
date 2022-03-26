package com.google.firebase.crashlytics.internal.persistence;

import java.io.File;
import java.io.FilenameFilter;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.persistence.-$$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU implements FilenameFilter {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU INSTANCE = new $$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU();

    private /* synthetic */ $$Lambda$CrashlyticsReportPersistence$5ewmHYO883ri5BmwN_Gn2GLWEFU() {
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        return CrashlyticsReportPersistence.isNormalPriorityEventFile(file, str);
    }
}
