package com.google.android.datatransport.runtime.scheduling.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager;
/* compiled from: lambda */
/* renamed from: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SchemaManager$V2XpHOEqNs-xjZeHRKfPgpBoXd8  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$SchemaManager$V2XpHOEqNsxjZeHRKfPgpBoXd8 implements SchemaManager.Migration {
    public static final /* synthetic */ $$Lambda$SchemaManager$V2XpHOEqNsxjZeHRKfPgpBoXd8 INSTANCE = new $$Lambda$SchemaManager$V2XpHOEqNsxjZeHRKfPgpBoXd8();

    private /* synthetic */ $$Lambda$SchemaManager$V2XpHOEqNsxjZeHRKfPgpBoXd8() {
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.SchemaManager.Migration
    public final void upgrade(SQLiteDatabase sQLiteDatabase) {
        SchemaManager.lambda$static$1(sQLiteDatabase);
    }
}
