package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SchemaManager$OryUNQUvlV-1zPxAbQpc_K9Bcpc  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SchemaManager$OryUNQUvlV1zPxAbQpc_K9Bcpc implements SchemaManager.Migration {
    public static final /* synthetic */ $$Lambda$SchemaManager$OryUNQUvlV1zPxAbQpc_K9Bcpc INSTANCE = new $$Lambda$SchemaManager$OryUNQUvlV1zPxAbQpc_K9Bcpc();

    private /* synthetic */ $$Lambda$SchemaManager$OryUNQUvlV1zPxAbQpc_K9Bcpc() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager.Migration
    public final void upgrade(SQLiteDatabase sQLiteDatabase) {
        SchemaManager.lambda$static$0(sQLiteDatabase);
    }
}
