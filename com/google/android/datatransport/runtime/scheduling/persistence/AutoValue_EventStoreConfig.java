package com.google.android.datatransport.runtime.scheduling.persistence;

import com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig;
/* loaded from: classes.dex */
final class AutoValue_EventStoreConfig extends EventStoreConfig {
    private final int criticalSectionEnterTimeoutMs;
    private final long eventCleanUpAge;
    private final int loadBatchSize;
    private final int maxBlobByteSizePerRow;
    private final long maxStorageSizeInBytes;

    private AutoValue_EventStoreConfig(long maxStorageSizeInBytes, int loadBatchSize, int criticalSectionEnterTimeoutMs, long eventCleanUpAge, int maxBlobByteSizePerRow) {
        this.maxStorageSizeInBytes = maxStorageSizeInBytes;
        this.loadBatchSize = loadBatchSize;
        this.criticalSectionEnterTimeoutMs = criticalSectionEnterTimeoutMs;
        this.eventCleanUpAge = eventCleanUpAge;
        this.maxBlobByteSizePerRow = maxBlobByteSizePerRow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig
    public long getMaxStorageSizeInBytes() {
        return this.maxStorageSizeInBytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig
    public int getLoadBatchSize() {
        return this.loadBatchSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig
    public int getCriticalSectionEnterTimeoutMs() {
        return this.criticalSectionEnterTimeoutMs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig
    public long getEventCleanUpAge() {
        return this.eventCleanUpAge;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig
    public int getMaxBlobByteSizePerRow() {
        return this.maxBlobByteSizePerRow;
    }

    public String toString() {
        return "EventStoreConfig{maxStorageSizeInBytes=" + this.maxStorageSizeInBytes + ", loadBatchSize=" + this.loadBatchSize + ", criticalSectionEnterTimeoutMs=" + this.criticalSectionEnterTimeoutMs + ", eventCleanUpAge=" + this.eventCleanUpAge + ", maxBlobByteSizePerRow=" + this.maxBlobByteSizePerRow + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventStoreConfig)) {
            return false;
        }
        EventStoreConfig that = (EventStoreConfig) o;
        if (this.maxStorageSizeInBytes == that.getMaxStorageSizeInBytes() && this.loadBatchSize == that.getLoadBatchSize() && this.criticalSectionEnterTimeoutMs == that.getCriticalSectionEnterTimeoutMs() && this.eventCleanUpAge == that.getEventCleanUpAge() && this.maxBlobByteSizePerRow == that.getMaxBlobByteSizePerRow()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.maxStorageSizeInBytes;
        long j2 = this.eventCleanUpAge;
        return (((((((((1 * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ this.loadBatchSize) * 1000003) ^ this.criticalSectionEnterTimeoutMs) * 1000003) ^ ((int) (j2 ^ (j2 >>> 32)))) * 1000003) ^ this.maxBlobByteSizePerRow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Builder extends EventStoreConfig.Builder {
        private Integer criticalSectionEnterTimeoutMs;
        private Long eventCleanUpAge;
        private Integer loadBatchSize;
        private Integer maxBlobByteSizePerRow;
        private Long maxStorageSizeInBytes;

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig.Builder setMaxStorageSizeInBytes(long maxStorageSizeInBytes) {
            this.maxStorageSizeInBytes = Long.valueOf(maxStorageSizeInBytes);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig.Builder setLoadBatchSize(int loadBatchSize) {
            this.loadBatchSize = Integer.valueOf(loadBatchSize);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig.Builder setCriticalSectionEnterTimeoutMs(int criticalSectionEnterTimeoutMs) {
            this.criticalSectionEnterTimeoutMs = Integer.valueOf(criticalSectionEnterTimeoutMs);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig.Builder setEventCleanUpAge(long eventCleanUpAge) {
            this.eventCleanUpAge = Long.valueOf(eventCleanUpAge);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig.Builder setMaxBlobByteSizePerRow(int maxBlobByteSizePerRow) {
            this.maxBlobByteSizePerRow = Integer.valueOf(maxBlobByteSizePerRow);
            return this;
        }

        @Override // com.google.android.datatransport.runtime.scheduling.persistence.EventStoreConfig.Builder
        EventStoreConfig build() {
            String missing = "";
            if (this.maxStorageSizeInBytes == null) {
                missing = missing + " maxStorageSizeInBytes";
            }
            if (this.loadBatchSize == null) {
                missing = missing + " loadBatchSize";
            }
            if (this.criticalSectionEnterTimeoutMs == null) {
                missing = missing + " criticalSectionEnterTimeoutMs";
            }
            if (this.eventCleanUpAge == null) {
                missing = missing + " eventCleanUpAge";
            }
            if (this.maxBlobByteSizePerRow == null) {
                missing = missing + " maxBlobByteSizePerRow";
            }
            if (missing.isEmpty()) {
                return new AutoValue_EventStoreConfig(this.maxStorageSizeInBytes.longValue(), this.loadBatchSize.intValue(), this.criticalSectionEnterTimeoutMs.intValue(), this.eventCleanUpAge.longValue(), this.maxBlobByteSizePerRow.intValue());
            }
            throw new IllegalStateException("Missing required properties:" + missing);
        }
    }
}
