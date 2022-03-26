package com.google.firebase.crashlytics.internal.model.serialization;

import android.util.JsonReader;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.model.serialization.-$$Lambda$CrashlyticsReportJsonTransform$PcYtebWm8LLEihGxNBWQex7movQ  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportJsonTransform$PcYtebWm8LLEihGxNBWQex7movQ implements CrashlyticsReportJsonTransform.ObjectParser {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$PcYtebWm8LLEihGxNBWQex7movQ INSTANCE = new $$Lambda$CrashlyticsReportJsonTransform$PcYtebWm8LLEihGxNBWQex7movQ();

    private /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$PcYtebWm8LLEihGxNBWQex7movQ() {
    }

    @Override // com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform.ObjectParser
    public final Object parse(JsonReader jsonReader) {
        return CrashlyticsReportJsonTransform.parseFile(jsonReader);
    }
}
