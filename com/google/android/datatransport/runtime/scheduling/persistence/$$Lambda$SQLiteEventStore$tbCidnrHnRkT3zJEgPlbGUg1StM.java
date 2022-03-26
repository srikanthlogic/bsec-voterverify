package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM implements SQLiteEventStore.Function {
    public static final /* synthetic */ $$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM INSTANCE = new $$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM();

    private /* synthetic */ $$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        return SQLiteEventStore.lambda$loadActiveContexts$9((SQLiteDatabase) obj);
    }
}
