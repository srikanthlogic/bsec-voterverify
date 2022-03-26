package com.google.android.datatransport.runtime.scheduling.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.SystemClock;
import android.util.Base64;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.runtime.EncodedPayload;
import com.google.android.datatransport.runtime.EventInternal;
import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.logging.Logging;
import com.google.android.datatransport.runtime.synchronization.SynchronizationException;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import com.google.android.datatransport.runtime.time.Clock;
import com.google.android.datatransport.runtime.util.PriorityMapping;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class SQLiteEventStore implements EventStore, SynchronizationGuard {
    private static final int LOCK_RETRY_BACK_OFF_MILLIS;
    private static final String LOG_TAG;
    static final int MAX_RETRIES;
    private static final Encoding PROTOBUF_ENCODING = Encoding.of("proto");
    private final EventStoreConfig config;
    private final Clock monotonicClock;
    private final SchemaManager schemaManager;
    private final Clock wallClock;

    /* loaded from: classes.dex */
    public interface Function<T, U> {
        U apply(T t);
    }

    /* loaded from: classes.dex */
    public interface Producer<T> {
        T produce();
    }

    @Inject
    public SQLiteEventStore(Clock wallClock, Clock clock, EventStoreConfig config, SchemaManager schemaManager) {
        this.schemaManager = schemaManager;
        this.wallClock = wallClock;
        this.monotonicClock = clock;
        this.config = config;
    }

    SQLiteDatabase getDb() {
        SchemaManager schemaManager = this.schemaManager;
        Objects.requireNonNull(schemaManager);
        return (SQLiteDatabase) retryIfDbLocked(new Producer() { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$iV7-zVaNm3OzwRKNnkXrWOPrqvk
            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Producer
            public final Object produce() {
                return SchemaManager.this.getWritableDatabase();
            }
        }, $$Lambda$SQLiteEventStore$um25oEoA60fAOv07ztYlCvK_sgs.INSTANCE);
    }

    public static /* synthetic */ SQLiteDatabase lambda$getDb$0(Throwable ex) {
        throw new SynchronizationException("Timed out while trying to open db.", ex);
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public PersistedEvent persist(TransportContext transportContext, EventInternal event) {
        Logging.d(LOG_TAG, "Storing event with priority=%s, name=%s for destination %s", transportContext.getPriority(), event.getTransportName(), transportContext.getBackendName());
        long newRowId = ((Long) inTransaction(new Function(transportContext, event) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$RQwNXHn8P-jEndjLySaOreAnLK4
            private final /* synthetic */ TransportContext f$1;
            private final /* synthetic */ EventInternal f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.this.lambda$persist$1$SQLiteEventStore(this.f$1, this.f$2, (SQLiteDatabase) obj);
            }
        })).longValue();
        if (newRowId < 1) {
            return null;
        }
        return PersistedEvent.create(newRowId, transportContext, event);
    }

    public /* synthetic */ Long lambda$persist$1$SQLiteEventStore(TransportContext transportContext, EventInternal event, SQLiteDatabase db) {
        if (isStorageAtLimit()) {
            return -1L;
        }
        long contextId = ensureTransportContext(db, transportContext);
        int maxBlobSizePerRow = this.config.getMaxBlobByteSizePerRow();
        byte[] payloadBytes = event.getEncodedPayload().getBytes();
        boolean inline = payloadBytes.length <= maxBlobSizePerRow;
        ContentValues values = new ContentValues();
        values.put("context_id", Long.valueOf(contextId));
        values.put("transport_name", event.getTransportName());
        values.put("timestamp_ms", Long.valueOf(event.getEventMillis()));
        values.put("uptime_ms", Long.valueOf(event.getUptimeMillis()));
        values.put("payload_encoding", event.getEncodedPayload().getEncoding().getName());
        values.put("code", event.getCode());
        values.put("num_attempts", (Integer) 0);
        values.put("inline", Boolean.valueOf(inline));
        values.put("payload", inline ? payloadBytes : new byte[0]);
        long newEventId = db.insert("events", null, values);
        if (!inline) {
            int numChunks = (int) Math.ceil(((double) payloadBytes.length) / ((double) maxBlobSizePerRow));
            for (int chunk = 1; chunk <= numChunks; chunk++) {
                byte[] chunkBytes = Arrays.copyOfRange(payloadBytes, (chunk - 1) * maxBlobSizePerRow, Math.min(chunk * maxBlobSizePerRow, payloadBytes.length));
                ContentValues payloadValues = new ContentValues();
                payloadValues.put("event_id", Long.valueOf(newEventId));
                payloadValues.put("sequence_num", Integer.valueOf(chunk));
                payloadValues.put("bytes", chunkBytes);
                db.insert("event_payloads", null, payloadValues);
            }
        }
        for (Map.Entry<String, String> entry : event.getMetadata().entrySet()) {
            ContentValues metadata = new ContentValues();
            metadata.put("event_id", Long.valueOf(newEventId));
            metadata.put(AppMeasurementSdk.ConditionalUserProperty.NAME, entry.getKey());
            metadata.put("value", entry.getValue());
            db.insert("event_metadata", null, metadata);
        }
        return Long.valueOf(newEventId);
    }

    private long ensureTransportContext(SQLiteDatabase db, TransportContext transportContext) {
        Long existingId = getTransportContextId(db, transportContext);
        if (existingId != null) {
            return existingId.longValue();
        }
        ContentValues record = new ContentValues();
        record.put("backend_name", transportContext.getBackendName());
        record.put("priority", Integer.valueOf(PriorityMapping.toInt(transportContext.getPriority())));
        record.put("next_request_ms", (Integer) 0);
        if (transportContext.getExtras() != null) {
            record.put("extras", Base64.encodeToString(transportContext.getExtras(), 0));
        }
        return db.insert("transport_contexts", null, record);
    }

    private Long getTransportContextId(SQLiteDatabase db, TransportContext transportContext) {
        StringBuilder selection = new StringBuilder("backend_name = ? and priority = ?");
        ArrayList<String> selectionArgs = new ArrayList<>(Arrays.asList(transportContext.getBackendName(), String.valueOf(PriorityMapping.toInt(transportContext.getPriority()))));
        if (transportContext.getExtras() != null) {
            selection.append(" and extras = ?");
            selectionArgs.add(Base64.encodeToString(transportContext.getExtras(), 0));
        } else {
            selection.append(" and extras is null");
        }
        return (Long) tryWithCursor(db.query("transport_contexts", new String[]{DBHelper.Key_ID}, selection.toString(), (String[]) selectionArgs.toArray(new String[0]), null, null, null), $$Lambda$SQLiteEventStore$huP16S6r4seAEE6hUvL2FlE7jI.INSTANCE);
    }

    public static /* synthetic */ Long lambda$getTransportContextId$2(Cursor cursor) {
        if (!cursor.moveToNext()) {
            return null;
        }
        return Long.valueOf(cursor.getLong(0));
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public void recordFailure(Iterable<PersistedEvent> events) {
        if (events.iterator().hasNext()) {
            inTransaction(new Function("UPDATE events SET num_attempts = num_attempts + 1 WHERE _id in " + toIdList(events)) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$JfVnNuqzscnRJB0EenzRf-BX8bw
                private final /* synthetic */ String f$0;

                {
                    this.f$0 = r1;
                }

                @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
                public final Object apply(Object obj) {
                    return SQLiteEventStore.lambda$recordFailure$3(this.f$0, (SQLiteDatabase) obj);
                }
            });
        }
    }

    public static /* synthetic */ Object lambda$recordFailure$3(String query, SQLiteDatabase db) {
        db.compileStatement(query).execute();
        db.compileStatement("DELETE FROM events WHERE num_attempts >= 16").execute();
        return null;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public void recordSuccess(Iterable<PersistedEvent> events) {
        if (events.iterator().hasNext()) {
            getDb().compileStatement("DELETE FROM events WHERE _id in " + toIdList(events)).execute();
        }
    }

    private static String toIdList(Iterable<PersistedEvent> events) {
        StringBuilder idList = new StringBuilder("(");
        Iterator<PersistedEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            idList.append(iterator.next().getId());
            if (iterator.hasNext()) {
                idList.append(',');
            }
        }
        idList.append(')');
        return idList.toString();
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public long getNextCallTime(TransportContext transportContext) {
        return ((Long) tryWithCursor(getDb().rawQuery("SELECT next_request_ms FROM transport_contexts WHERE backend_name = ? and priority = ?", new String[]{transportContext.getBackendName(), String.valueOf(PriorityMapping.toInt(transportContext.getPriority()))}), $$Lambda$SQLiteEventStore$58qwLe9VEE9edwaCrH8OyrydJA.INSTANCE)).longValue();
    }

    public static /* synthetic */ Long lambda$getNextCallTime$4(Cursor cursor) {
        if (cursor.moveToNext()) {
            return Long.valueOf(cursor.getLong(0));
        }
        return 0L;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public boolean hasPendingEventsFor(TransportContext transportContext) {
        return ((Boolean) inTransaction(new Function(transportContext) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$L3gvhPMwXDfJapMFWdvDgqtBgNQ
            private final /* synthetic */ TransportContext f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.this.lambda$hasPendingEventsFor$5$SQLiteEventStore(this.f$1, (SQLiteDatabase) obj);
            }
        })).booleanValue();
    }

    public /* synthetic */ Boolean lambda$hasPendingEventsFor$5$SQLiteEventStore(TransportContext transportContext, SQLiteDatabase db) {
        Long contextId = getTransportContextId(db, transportContext);
        if (contextId == null) {
            return false;
        }
        return (Boolean) tryWithCursor(getDb().rawQuery("SELECT 1 FROM events WHERE context_id = ? LIMIT 1", new String[]{contextId.toString()}), $$Lambda$ky9PmMQY9PhnKcqK77KNYHUaLEk.INSTANCE);
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public void recordNextCallTime(TransportContext transportContext, long timestampMs) {
        inTransaction(new Function(timestampMs, transportContext) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$kAFd-MrVPIaFPjiS3W68XTcAvus
            private final /* synthetic */ long f$0;
            private final /* synthetic */ TransportContext f$1;

            {
                this.f$0 = r1;
                this.f$1 = r3;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.lambda$recordNextCallTime$6(this.f$0, this.f$1, (SQLiteDatabase) obj);
            }
        });
    }

    public static /* synthetic */ Object lambda$recordNextCallTime$6(long timestampMs, TransportContext transportContext, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("next_request_ms", Long.valueOf(timestampMs));
        if (db.update("transport_contexts", values, "backend_name = ? and priority = ?", new String[]{transportContext.getBackendName(), String.valueOf(PriorityMapping.toInt(transportContext.getPriority()))}) < 1) {
            values.put("backend_name", transportContext.getBackendName());
            values.put("priority", Integer.valueOf(PriorityMapping.toInt(transportContext.getPriority())));
            db.insert("transport_contexts", null, values);
        }
        return null;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public Iterable<PersistedEvent> loadBatch(TransportContext transportContext) {
        return (Iterable) inTransaction(new Function(transportContext) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$_KxQpqIlciOPxCRJLYXwZ5CQaKA
            private final /* synthetic */ TransportContext f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.this.lambda$loadBatch$7$SQLiteEventStore(this.f$1, (SQLiteDatabase) obj);
            }
        });
    }

    public /* synthetic */ List lambda$loadBatch$7$SQLiteEventStore(TransportContext transportContext, SQLiteDatabase db) {
        List<PersistedEvent> events = loadEvents(db, transportContext);
        return join(events, loadMetadata(db, events));
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public Iterable<TransportContext> loadActiveContexts() {
        return (Iterable) inTransaction($$Lambda$SQLiteEventStore$tbCidnrHnRkT3zJEgPlbGUg1StM.INSTANCE);
    }

    public static /* synthetic */ List lambda$loadActiveContexts$9(SQLiteDatabase db) {
        return (List) tryWithCursor(db.rawQuery("SELECT distinct t._id, t.backend_name, t.priority, t.extras FROM transport_contexts AS t, events AS e WHERE e.context_id = t._id", new String[0]), $$Lambda$SQLiteEventStore$f8j27GHeu2bGuoRq6ij9d42apy8.INSTANCE);
    }

    public static /* synthetic */ List lambda$loadActiveContexts$8(Cursor cursor) {
        List<TransportContext> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            results.add(TransportContext.builder().setBackendName(cursor.getString(1)).setPriority(PriorityMapping.valueOf(cursor.getInt(2))).setExtras(maybeBase64Decode(cursor.getString(3))).build());
        }
        return results;
    }

    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStore
    public int cleanUp() {
        return ((Integer) inTransaction(new Function(this.wallClock.getTime() - this.config.getEventCleanUpAge()) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$6FHvqY_1RRIiwBGQcwGfmoTJgfQ
            private final /* synthetic */ long f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((SQLiteDatabase) obj).delete("events", "timestamp_ms < ?", new String[]{String.valueOf(this.f$0)}));
            }
        })).intValue();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.schemaManager.close();
    }

    public void clearDb() {
        inTransaction($$Lambda$SQLiteEventStore$7lzpdRCmVweMjmsmt2DJiad_WGE.INSTANCE);
    }

    public static /* synthetic */ Object lambda$clearDb$11(SQLiteDatabase db) {
        db.delete("events", null, new String[0]);
        db.delete("transport_contexts", null, new String[0]);
        return null;
    }

    private static byte[] maybeBase64Decode(String value) {
        if (value == null) {
            return null;
        }
        return Base64.decode(value, 0);
    }

    private List<PersistedEvent> loadEvents(SQLiteDatabase db, TransportContext transportContext) {
        List<PersistedEvent> events = new ArrayList<>();
        Long contextId = getTransportContextId(db, transportContext);
        if (contextId == null) {
            return events;
        }
        tryWithCursor(db.query("events", new String[]{DBHelper.Key_ID, "transport_name", "timestamp_ms", "uptime_ms", "payload_encoding", "payload", "code", "inline"}, "context_id = ?", new String[]{contextId.toString()}, null, null, null, String.valueOf(this.config.getLoadBatchSize())), new Function(events, transportContext) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$Hs_rmSNfpQOgIhTfkvzEQOGYaPI
            private final /* synthetic */ List f$1;
            private final /* synthetic */ TransportContext f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.this.lambda$loadEvents$12$SQLiteEventStore(this.f$1, this.f$2, (Cursor) obj);
            }
        });
        return events;
    }

    public /* synthetic */ Object lambda$loadEvents$12$SQLiteEventStore(List events, TransportContext transportContext, Cursor cursor) {
        while (cursor.moveToNext()) {
            boolean inline = false;
            long id = cursor.getLong(0);
            if (cursor.getInt(7) != 0) {
                inline = true;
            }
            EventInternal.Builder event = EventInternal.builder().setTransportName(cursor.getString(1)).setEventMillis(cursor.getLong(2)).setUptimeMillis(cursor.getLong(3));
            if (inline) {
                event.setEncodedPayload(new EncodedPayload(toEncoding(cursor.getString(4)), cursor.getBlob(5)));
            } else {
                event.setEncodedPayload(new EncodedPayload(toEncoding(cursor.getString(4)), readPayload(id)));
            }
            if (!cursor.isNull(6)) {
                event.setCode(Integer.valueOf(cursor.getInt(6)));
            }
            events.add(PersistedEvent.create(id, transportContext, event.build()));
        }
        return null;
    }

    private byte[] readPayload(long eventId) {
        return (byte[]) tryWithCursor(getDb().query("event_payloads", new String[]{"bytes"}, "event_id = ?", new String[]{String.valueOf(eventId)}, null, null, "sequence_num"), $$Lambda$SQLiteEventStore$Jmi2exuDu8tmshpagYXvDJMRg04.INSTANCE);
    }

    public static /* synthetic */ byte[] lambda$readPayload$13(Cursor cursor) {
        List<byte[]> chunks = new ArrayList<>();
        int totalLength = 0;
        while (cursor.moveToNext()) {
            byte[] chunk = cursor.getBlob(0);
            chunks.add(chunk);
            totalLength += chunk.length;
        }
        byte[] payloadBytes = new byte[totalLength];
        int offset = 0;
        for (int i = 0; i < chunks.size(); i++) {
            byte[] chunk2 = chunks.get(i);
            System.arraycopy(chunk2, 0, payloadBytes, offset, chunk2.length);
            offset += chunk2.length;
        }
        return payloadBytes;
    }

    private static Encoding toEncoding(String value) {
        if (value == null) {
            return PROTOBUF_ENCODING;
        }
        return Encoding.of(value);
    }

    private Map<Long, Set<Metadata>> loadMetadata(SQLiteDatabase db, List<PersistedEvent> events) {
        Map<Long, Set<Metadata>> metadataIndex = new HashMap<>();
        StringBuilder whereClause = new StringBuilder("event_id IN (");
        for (int i = 0; i < events.size(); i++) {
            whereClause.append(events.get(i).getId());
            if (i < events.size() - 1) {
                whereClause.append(',');
            }
        }
        whereClause.append(')');
        tryWithCursor(db.query("event_metadata", new String[]{"event_id", AppMeasurementSdk.ConditionalUserProperty.NAME, "value"}, whereClause.toString(), null, null, null, null), new Function(metadataIndex) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$QkmlCmlc2GpgtHSossp0jN_048s
            private final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Function
            public final Object apply(Object obj) {
                return SQLiteEventStore.lambda$loadMetadata$14(this.f$0, (Cursor) obj);
            }
        });
        return metadataIndex;
    }

    public static /* synthetic */ Object lambda$loadMetadata$14(Map metadataIndex, Cursor cursor) {
        while (cursor.moveToNext()) {
            long eventId = cursor.getLong(0);
            Set<Metadata> currentSet = (Set) metadataIndex.get(Long.valueOf(eventId));
            if (currentSet == null) {
                currentSet = new HashSet<>();
                metadataIndex.put(Long.valueOf(eventId), currentSet);
            }
            currentSet.add(new Metadata(cursor.getString(1), cursor.getString(2)));
        }
        return null;
    }

    private List<PersistedEvent> join(List<PersistedEvent> events, Map<Long, Set<Metadata>> metadataIndex) {
        ListIterator<PersistedEvent> iterator = events.listIterator();
        while (iterator.hasNext()) {
            PersistedEvent current = iterator.next();
            if (metadataIndex.containsKey(Long.valueOf(current.getId()))) {
                EventInternal.Builder newEvent = current.getEvent().toBuilder();
                for (Metadata metadata : metadataIndex.get(Long.valueOf(current.getId()))) {
                    newEvent.addMetadata(metadata.key, metadata.value);
                }
                iterator.set(PersistedEvent.create(current.getId(), current.getTransportContext(), newEvent.build()));
            }
        }
        return events;
    }

    private <T> T retryIfDbLocked(Producer<T> retriable, Function<Throwable, T> failureHandler) {
        long startTime = this.monotonicClock.getTime();
        while (true) {
            try {
                return retriable.produce();
            } catch (SQLiteDatabaseLockedException ex) {
                if (this.monotonicClock.getTime() >= ((long) this.config.getCriticalSectionEnterTimeoutMs()) + startTime) {
                    return failureHandler.apply(ex);
                }
                SystemClock.sleep(50);
            }
        }
    }

    private void ensureBeginTransaction(SQLiteDatabase db) {
        retryIfDbLocked(new Producer(db) { // from class: com.google.android.datatransport.runtime.scheduling.persistence.-$$Lambda$SQLiteEventStore$-aPr_tTbYSmziDAuGPp84pA8W7o
            private final /* synthetic */ SQLiteDatabase f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore.Producer
            public final Object produce() {
                return this.f$0.beginTransaction();
            }
        }, $$Lambda$SQLiteEventStore$TVwK3apUpe_e9DZxOParL7W2gXI.INSTANCE);
    }

    public static /* synthetic */ Object lambda$ensureBeginTransaction$16(Throwable ex) {
        throw new SynchronizationException("Timed out while trying to acquire the lock.", ex);
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard
    public <T> T runCriticalSection(SynchronizationGuard.CriticalSection<T> criticalSection) {
        SQLiteDatabase db = getDb();
        ensureBeginTransaction(db);
        try {
            T result = criticalSection.execute();
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    <T> T inTransaction(Function<SQLiteDatabase, T> function) {
        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            T result = function.apply(db);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /* loaded from: classes.dex */
    public static class Metadata {
        final String key;
        final String value;

        private Metadata(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private boolean isStorageAtLimit() {
        return getPageCount() * getPageSize() >= this.config.getMaxStorageSizeInBytes();
    }

    long getByteSize() {
        return getPageCount() * getPageSize();
    }

    private long getPageSize() {
        return getDb().compileStatement("PRAGMA page_size").simpleQueryForLong();
    }

    private long getPageCount() {
        return getDb().compileStatement("PRAGMA page_count").simpleQueryForLong();
    }

    static <T> T tryWithCursor(Cursor c, Function<Cursor, T> function) {
        try {
            return function.apply(c);
        } finally {
            c.close();
        }
    }
}
