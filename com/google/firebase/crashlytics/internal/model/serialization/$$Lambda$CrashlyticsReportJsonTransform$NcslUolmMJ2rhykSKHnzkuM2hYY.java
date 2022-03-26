package com.google.firebase.crashlytics.internal.model.serialization;

import android.util.JsonReader;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.model.serialization.-$$Lambda$CrashlyticsReportJsonTransform$NcslUolmMJ2rhykSKHnzkuM2hYY  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportJsonTransform$NcslUolmMJ2rhykSKHnzkuM2hYY implements CrashlyticsReportJsonTransform.ObjectParser {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$NcslUolmMJ2rhykSKHnzkuM2hYY INSTANCE = new $$Lambda$CrashlyticsReportJsonTransform$NcslUolmMJ2rhykSKHnzkuM2hYY();

    private /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$NcslUolmMJ2rhykSKHnzkuM2hYY() {
    }

    @Override // com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform.ObjectParser
    public final Object parse(JsonReader jsonReader) {
        return CrashlyticsReportJsonTransform.parseEventThread(jsonReader);
    }
}
