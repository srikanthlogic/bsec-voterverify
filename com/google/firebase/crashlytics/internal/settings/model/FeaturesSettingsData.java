package com.google.firebase.crashlytics.internal.settings.model;
/* loaded from: classes3.dex */
public class FeaturesSettingsData {
    public final boolean collectAnrs;
    public final boolean collectReports;

    public FeaturesSettingsData(boolean collectReports, boolean collectAnrs) {
        this.collectReports = collectReports;
        this.collectAnrs = collectAnrs;
    }
}
