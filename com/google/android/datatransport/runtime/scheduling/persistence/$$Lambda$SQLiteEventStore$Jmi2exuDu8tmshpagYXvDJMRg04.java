package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04 implements SQLiteEventStore.Function {
    public static final /* synthetic */ $$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04 INSTANCE = new $$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04();

    private /* synthetic */ $$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        return SQLiteEventStore.lambda$readPayload$13((Cursor) obj);
    }
}
