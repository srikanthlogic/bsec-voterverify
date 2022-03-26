package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE implements SQLiteEventStore.Function {
    public static final /* synthetic */ $$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE INSTANCE = new $$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE();

    private /* synthetic */ $$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
    public final Object apply(Object obj) {
        return SQLiteEventStore.lambda$clearDb$11((SQLiteDatabase) obj);
    }
}
