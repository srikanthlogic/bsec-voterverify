package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2Fl-E7jI  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2FlE7jI implements SQLiteEventStore.Function {
    public static final /* synthetic */ $$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2FlE7jI INSTANCE = new $$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2FlE7jI();

    private /* synthetic */ $$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2FlE7jI() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        return SQLiteEventStore.lambda$getTransportContextId$2((Cursor) obj);
    }
}
