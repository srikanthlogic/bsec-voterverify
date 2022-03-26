package com.google.firebase.crashlytics.internal.persistence;

import java.io.File;
import java.util.Comparator;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.persistence.-$$Lambda$CrashlyticsReportPersistence$cus4xBFpk-zHLsoBdoVy1SdmpDs  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportPersistence$cus4xBFpkzHLsoBdoVy1SdmpDs implements Comparator {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportPersistence$cus4xBFpkzHLsoBdoVy1SdmpDs INSTANCE = new $$Lambda$CrashlyticsReportPersistence$cus4xBFpkzHLsoBdoVy1SdmpDs();

    private /* synthetic */ $$Lambda$CrashlyticsReportPersistence$cus4xBFpkzHLsoBdoVy1SdmpDs() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((File) obj2).getName().compareTo(((File) obj).getName());
    }
}
