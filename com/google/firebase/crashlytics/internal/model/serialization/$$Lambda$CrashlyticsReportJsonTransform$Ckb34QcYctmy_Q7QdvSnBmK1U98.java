package com.google.firebase.crashlytics.internal.model.serialization;

import android.util.JsonReader;
import com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform;
/* compiled from: lambda */
/* renamed from: com.google.firebase.crashlytics.internal.model.serialization.-$$Lambda$CrashlyticsReportJsonTransform$Ckb34QcYctmy_Q7QdvSnBmK1U98  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$CrashlyticsReportJsonTransform$Ckb34QcYctmy_Q7QdvSnBmK1U98 implements CrashlyticsReportJsonTransform.ObjectParser {
    public static final /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$Ckb34QcYctmy_Q7QdvSnBmK1U98 INSTANCE = new $$Lambda$CrashlyticsReportJsonTransform$Ckb34QcYctmy_Q7QdvSnBmK1U98();

    private /* synthetic */ $$Lambda$CrashlyticsReportJsonTransform$Ckb34QcYctmy_Q7QdvSnBmK1U98() {
    }

    @Override // com.google.firebase.crashlytics.internal.model.serialization.CrashlyticsReportJsonTransform.ObjectParser
    public final Object parse(JsonReader jsonReader) {
        return CrashlyticsReportJsonTransform.parseEvent(jsonReader);
    }
}
