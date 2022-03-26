package com.google.firebase.crashlytics.internal.persistence;

import java.io.File;
import java.io.FilenameFilter;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.persistence.-$$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ implements FilenameFilter {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ INSTANCE = new $$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ();

    private /* synthetic */ $$Lambda$CrashlyticsReportPersistence$yrLDYcvA2rHplfuqiXhfFmNn2UQ() {
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        return str.startsWith("event");
    }
}
