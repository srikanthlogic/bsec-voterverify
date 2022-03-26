package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
/* loaded from: classes.dex */
public class PoolConfig {
    public static final int BITMAP_POOL_MAX_BITMAP_SIZE_DEFAULT = 4194304;
    private final int mBitmapPoolMaxBitmapSize;
    private final int mBitmapPoolMaxPoolSize;
    private final PoolParams mBitmapPoolParams;
    private final PoolStatsTracker mBitmapPoolStatsTracker;
    private final String mBitmapPoolType;
    private final PoolParams mFlexByteArrayPoolParams;
    private final PoolParams mMemoryChunkPoolParams;
    private final PoolStatsTracker mMemoryChunkPoolStatsTracker;
    private final MemoryTrimmableRegistry mMemoryTrimmableRegistry;
    private final boolean mRegisterLruBitmapPoolAsMemoryTrimmable;
    private final PoolParams mSmallByteArrayPoolParams;
    private final PoolStatsTracker mSmallByteArrayPoolStatsTracker;

    private PoolConfig(Builder builder) {
        PoolParams poolParams;
        PoolStatsTracker poolStatsTracker;
        PoolParams poolParams2;
        MemoryTrimmableRegistry memoryTrimmableRegistry;
        PoolParams poolParams3;
        PoolStatsTracker poolStatsTracker2;
        PoolParams poolParams4;
        PoolStatsTracker poolStatsTracker3;
        String str;
        int i;
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("PoolConfig()");
        }
        if (builder.mBitmapPoolParams == null) {
            poolParams = DefaultBitmapPoolParams.get();
        } else {
            poolParams = builder.mBitmapPoolParams;
        }
        this.mBitmapPoolParams = poolParams;
        if (builder.mBitmapPoolStatsTracker == null) {
            poolStatsTracker = NoOpPoolStatsTracker.getInstance();
        } else {
            poolStatsTracker = builder.mBitmapPoolStatsTracker;
        }
        this.mBitmapPoolStatsTracker = poolStatsTracker;
        if (builder.mFlexByteArrayPoolParams == null) {
            poolParams2 = DefaultFlexByteArrayPoolParams.get();
        } else {
            poolParams2 = builder.mFlexByteArrayPoolParams;
        }
        this.mFlexByteArrayPoolParams = poolParams2;
        if (builder.mMemoryTrimmableRegistry == null) {
            memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        } else {
            memoryTrimmableRegistry = builder.mMemoryTrimmableRegistry;
        }
        this.mMemoryTrimmableRegistry = memoryTrimmableRegistry;
        if (builder.mMemoryChunkPoolParams == null) {
            poolParams3 = DefaultNativeMemoryChunkPoolParams.get();
        } else {
            poolParams3 = builder.mMemoryChunkPoolParams;
        }
        this.mMemoryChunkPoolParams = poolParams3;
        if (builder.mMemoryChunkPoolStatsTracker == null) {
            poolStatsTracker2 = NoOpPoolStatsTracker.getInstance();
        } else {
            poolStatsTracker2 = builder.mMemoryChunkPoolStatsTracker;
        }
        this.mMemoryChunkPoolStatsTracker = poolStatsTracker2;
        if (builder.mSmallByteArrayPoolParams == null) {
            poolParams4 = DefaultByteArrayPoolParams.get();
        } else {
            poolParams4 = builder.mSmallByteArrayPoolParams;
        }
        this.mSmallByteArrayPoolParams = poolParams4;
        if (builder.mSmallByteArrayPoolStatsTracker == null) {
            poolStatsTracker3 = NoOpPoolStatsTracker.getInstance();
        } else {
            poolStatsTracker3 = builder.mSmallByteArrayPoolStatsTracker;
        }
        this.mSmallByteArrayPoolStatsTracker = poolStatsTracker3;
        if (builder.mBitmapPoolType == null) {
            str = "legacy";
        } else {
            str = builder.mBitmapPoolType;
        }
        this.mBitmapPoolType = str;
        this.mBitmapPoolMaxPoolSize = builder.mBitmapPoolMaxPoolSize;
        if (builder.mBitmapPoolMaxBitmapSize > 0) {
            i = builder.mBitmapPoolMaxBitmapSize;
        } else {
            i = 4194304;
        }
        this.mBitmapPoolMaxBitmapSize = i;
        this.mRegisterLruBitmapPoolAsMemoryTrimmable = builder.mRegisterLruBitmapPoolAsMemoryTrimmable;
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    public PoolParams getBitmapPoolParams() {
        return this.mBitmapPoolParams;
    }

    public PoolStatsTracker getBitmapPoolStatsTracker() {
        return this.mBitmapPoolStatsTracker;
    }

    public MemoryTrimmableRegistry getMemoryTrimmableRegistry() {
        return this.mMemoryTrimmableRegistry;
    }

    public PoolParams getMemoryChunkPoolParams() {
        return this.mMemoryChunkPoolParams;
    }

    public PoolStatsTracker getMemoryChunkPoolStatsTracker() {
        return this.mMemoryChunkPoolStatsTracker;
    }

    public PoolParams getFlexByteArrayPoolParams() {
        return this.mFlexByteArrayPoolParams;
    }

    public PoolParams getSmallByteArrayPoolParams() {
        return this.mSmallByteArrayPoolParams;
    }

    public PoolStatsTracker getSmallByteArrayPoolStatsTracker() {
        return this.mSmallByteArrayPoolStatsTracker;
    }

    public String getBitmapPoolType() {
        return this.mBitmapPoolType;
    }

    public int getBitmapPoolMaxPoolSize() {
        return this.mBitmapPoolMaxPoolSize;
    }

    public int getBitmapPoolMaxBitmapSize() {
        return this.mBitmapPoolMaxBitmapSize;
    }

    public boolean isRegisterLruBitmapPoolAsMemoryTrimmable() {
        return this.mRegisterLruBitmapPoolAsMemoryTrimmable;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int mBitmapPoolMaxBitmapSize;
        private int mBitmapPoolMaxPoolSize;
        private PoolParams mBitmapPoolParams;
        private PoolStatsTracker mBitmapPoolStatsTracker;
        private String mBitmapPoolType;
        private PoolParams mFlexByteArrayPoolParams;
        private PoolParams mMemoryChunkPoolParams;
        private PoolStatsTracker mMemoryChunkPoolStatsTracker;
        private MemoryTrimmableRegistry mMemoryTrimmableRegistry;
        private boolean mRegisterLruBitmapPoolAsMemoryTrimmable;
        private PoolParams mSmallByteArrayPoolParams;
        private PoolStatsTracker mSmallByteArrayPoolStatsTracker;

        private Builder() {
        }

        public Builder setBitmapPoolParams(PoolParams bitmapPoolParams) {
            this.mBitmapPoolParams = (PoolParams) Preconditions.checkNotNull(bitmapPoolParams);
            return this;
        }

        public Builder setBitmapPoolStatsTracker(PoolStatsTracker bitmapPoolStatsTracker) {
            this.mBitmapPoolStatsTracker = (PoolStatsTracker) Preconditions.checkNotNull(bitmapPoolStatsTracker);
            return this;
        }

        public Builder setFlexByteArrayPoolParams(PoolParams flexByteArrayPoolParams) {
            this.mFlexByteArrayPoolParams = flexByteArrayPoolParams;
            return this;
        }

        public Builder setMemoryTrimmableRegistry(MemoryTrimmableRegistry memoryTrimmableRegistry) {
            this.mMemoryTrimmableRegistry = memoryTrimmableRegistry;
            return this;
        }

        public Builder setNativeMemoryChunkPoolParams(PoolParams memoryChunkPoolParams) {
            this.mMemoryChunkPoolParams = (PoolParams) Preconditions.checkNotNull(memoryChunkPoolParams);
            return this;
        }

        public Builder setNativeMemoryChunkPoolStatsTracker(PoolStatsTracker memoryChunkPoolStatsTracker) {
            this.mMemoryChunkPoolStatsTracker = (PoolStatsTracker) Preconditions.checkNotNull(memoryChunkPoolStatsTracker);
            return this;
        }

        public Builder setSmallByteArrayPoolParams(PoolParams commonByteArrayPoolParams) {
            this.mSmallByteArrayPoolParams = (PoolParams) Preconditions.checkNotNull(commonByteArrayPoolParams);
            return this;
        }

        public Builder setSmallByteArrayPoolStatsTracker(PoolStatsTracker smallByteArrayPoolStatsTracker) {
            this.mSmallByteArrayPoolStatsTracker = (PoolStatsTracker) Preconditions.checkNotNull(smallByteArrayPoolStatsTracker);
            return this;
        }

        public PoolConfig build() {
            return new PoolConfig(this);
        }

        public Builder setBitmapPoolType(String bitmapPoolType) {
            this.mBitmapPoolType = bitmapPoolType;
            return this;
        }

        public Builder setBitmapPoolMaxPoolSize(int bitmapPoolMaxPoolSize) {
            this.mBitmapPoolMaxPoolSize = bitmapPoolMaxPoolSize;
            return this;
        }

        public Builder setBitmapPoolMaxBitmapSize(int bitmapPoolMaxBitmapSize) {
            this.mBitmapPoolMaxBitmapSize = bitmapPoolMaxBitmapSize;
            return this;
        }

        public void setRegisterLruBitmapPoolAsMemoryTrimmable(boolean registerLruBitmapPoolAsMemoryTrimmable) {
            this.mRegisterLruBitmapPoolAsMemoryTrimmable = registerLruBitmapPoolAsMemoryTrimmable;
        }
    }
}
