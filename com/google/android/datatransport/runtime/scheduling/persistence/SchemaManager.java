package com.google.android.datatransport.runtime.scheduling.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SchemaManager extends SQLiteOpenHelper {
    private static final String CREATE_CONTEXTS_SQL_V1;
    private static final String CREATE_CONTEXT_BACKEND_PRIORITY_INDEX_V1;
    private static final String CREATE_EVENTS_SQL_V1;
    private static final String CREATE_EVENT_BACKEND_INDEX_V1;
    private static final String CREATE_EVENT_METADATA_SQL_V1;
    private static final String CREATE_PAYLOADS_TABLE_V4;
    static final String DB_NAME;
    private static final String DROP_CONTEXTS_SQL;
    private static final String DROP_EVENTS_SQL;
    private static final String DROP_EVENT_METADATA_SQL;
    private static final String DROP_PAYLOADS_SQL;
    private boolean configured = false;
    private final int schemaVersion;
    static int SCHEMA_VERSION = 4;
    private static final Migration MIGRATE_TO_V1 = $$Lambda$SchemaManager$OryUNQUvlV1zPxAbQpc_K9Bcpc.INSTANCE;
    private static final Migration MIGRATE_TO_V2 = $$Lambda$SchemaManager$V2XpHOEqNsxjZeHRKfPgpBoXd8.INSTANCE;
    private static final Migration MIGRATE_TO_V3 = $$Lambda$SchemaManager$KMc4V7kHVkAjH45Fz8HtRNyME4U.INSTANCE;
    private static final Migration MIGRATE_TO_V4 = $$Lambda$SchemaManager$GnoKRnczwOa6Fk7ZCPhACcfPzQ.INSTANCE;
    private static final List<Migration> INCREMENTAL_MIGRATIONS = Arrays.asList(MIGRATE_TO_V1, MIGRATE_TO_V2, MIGRATE_TO_V3, MIGRATE_TO_V4);

    /* loaded from: classes.dex */
    public interface Migration {
        void upgrade(SQLiteDatabase sQLiteDatabase);
    }

    public static /* synthetic */ void lambda$static$0(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_SQL_V1);
        db.execSQL(CREATE_EVENT_METADATA_SQL_V1);
        db.execSQL(CREATE_CONTEXTS_SQL_V1);
        db.execSQL(CREATE_EVENT_BACKEND_INDEX_V1);
        db.execSQL(CREATE_CONTEXT_BACKEND_PRIORITY_INDEX_V1);
    }

    public static /* synthetic */ void lambda$static$1(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE transport_contexts ADD COLUMN extras BLOB");
        db.execSQL("CREATE UNIQUE INDEX contexts_backend_priority_extras on transport_contexts(backend_name, priority, extras)");
        db.execSQL("DROP INDEX contexts_backend_priority");
    }

    public static /* synthetic */ void lambda$static$3(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE events ADD COLUMN inline BOOLEAN NOT NULL DEFAULT 1");
        db.execSQL(DROP_PAYLOADS_SQL);
        db.execSQL(CREATE_PAYLOADS_TABLE_V4);
    }

    @Inject
    public SchemaManager(Context context, @Named("SQLITE_DB_NAME") String dbName, @Named("SCHEMA_VERSION") int schemaVersion) {
        super(context, dbName, (SQLiteDatabase.CursorFactory) null, schemaVersion);
        this.schemaVersion = schemaVersion;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onConfigure(SQLiteDatabase db) {
        this.configured = true;
        db.rawQuery("PRAGMA busy_timeout=0;", new String[0]).close();
        if (Build.VERSION.SDK_INT >= 16) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    private void ensureConfigured(SQLiteDatabase db) {
        if (!this.configured) {
            onConfigure(db);
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        onCreate(db, this.schemaVersion);
    }

    private void onCreate(SQLiteDatabase db, int version) {
        ensureConfigured(db);
        upgrade(db, 0, version);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ensureConfigured(db);
        upgrade(db, oldVersion, newVersion);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_SQL);
        db.execSQL(DROP_EVENT_METADATA_SQL);
        db.execSQL(DROP_CONTEXTS_SQL);
        db.execSQL(DROP_PAYLOADS_SQL);
        onCreate(db, newVersion);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onOpen(SQLiteDatabase db) {
        ensureConfigured(db);
    }

    private void upgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
        if (toVersion <= INCREMENTAL_MIGRATIONS.size()) {
            for (int version = fromVersion; version < toVersion; version++) {
                INCREMENTAL_MIGRATIONS.get(version).upgrade(db);
            }
            return;
        }
        throw new IllegalArgumentException("Migration from " + fromVersion + " to " + toVersion + " was requested, but cannot be performed. Only " + INCREMENTAL_MIGRATIONS.size() + " migrations are provided");
    }
}
