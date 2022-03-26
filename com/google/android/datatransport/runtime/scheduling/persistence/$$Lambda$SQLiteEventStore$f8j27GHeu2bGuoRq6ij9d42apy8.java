package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.Cursor;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8 implements SQLiteEventStore.Function {
    public static final /* synthetic */ $$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8 INSTANCE = new $$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8();

    private /* synthetic */ $$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        return SQLiteEventStore.lambda$loadActiveContexts$8((Cursor) obj);
    }
}
