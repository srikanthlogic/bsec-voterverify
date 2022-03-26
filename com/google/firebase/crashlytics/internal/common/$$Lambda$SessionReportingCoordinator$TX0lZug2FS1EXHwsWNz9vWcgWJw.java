package com.google.firebase.crashlytics.internal.common;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import java.util.Comparator;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.common.-$$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw implements Comparator {
    public static final /* synthetic */ $$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw INSTANCE = new $$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw();

    private /* synthetic */ $$Lambda$SessionReportingCoordinator$TX0lZug2FS1EXHwsWNz9vWcgWJw() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((CrashlyticsReport.CustomAttribute) obj).getKey().compareTo(((CrashlyticsReport.CustomAttribute) obj2).getKey());
    }
}
