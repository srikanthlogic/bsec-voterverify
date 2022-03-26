package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SchemaManager$GnoKRnczwOa6F-k7ZCPhACcfPzQ  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SchemaManager$GnoKRnczwOa6Fk7ZCPhACcfPzQ implements SchemaManager.Migration {
    public static final /* synthetic */ $$Lambda$SchemaManager$GnoKRnczwOa6Fk7ZCPhACcfPzQ INSTANCE = new $$Lambda$SchemaManager$GnoKRnczwOa6Fk7ZCPhACcfPzQ();

    private /* synthetic */ $$Lambda$SchemaManager$GnoKRnczwOa6Fk7ZCPhACcfPzQ() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager.Migration
    public final void upgrade(SQLiteDatabase sQLiteDatabase) {
        SchemaManager.lambda$static$3(sQLiteDatabase);
    }
}
