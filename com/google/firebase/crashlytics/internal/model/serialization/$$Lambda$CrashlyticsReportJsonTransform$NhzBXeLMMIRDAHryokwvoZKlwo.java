package com.google.firebase.crashlytics.internal.model.serialization;

import android.util.JsonReader;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.model.serialization.-$$Lambda$CrashlyticsReportJsonTransform$NhzBXeLMMIRDAHr-yokwvoZKlwo  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportJsonTransform$NhzBXeLMMIRDAHryokwvoZKlwo implements CrashlyticsReportJsonTransform.ObjectParser {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$NhzBXeLMMIRDAHryokwvoZKlwo INSTANCE = new $$Lambda$CrashlyticsReportJsonTransform$NhzBXeLMMIRDAHryokwvoZKlwo();

    private /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$NhzBXeLMMIRDAHryokwvoZKlwo() {
    }

    @Override // com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform.ObjectParser
    public final Object parse(JsonReader jsonReader) {
        return CrashlyticsReportJsonTransform.parseCustomAttribute(jsonReader);
    }
}
