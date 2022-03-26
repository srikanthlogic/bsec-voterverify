package com.google.common.cache;
/* loaded from: classes.dex */
public enum RemovalCause {
    EXPLICIT {
        @Override // com.google.common.cache.RemovalCause
        boolean wasEvicted() {
            return false;
        }
    },
    REPLACED {
        @Override // com.google.common.cache.RemovalCause
        boolean wasEvicted() {
            return false;
        }
    },
    COLLECTED {
        @Override // com.google.common.cache.RemovalCause
        boolean wasEvicted() {
            return true;
        }
    },
    EXPIRED {
        @Override // com.google.common.cache.RemovalCause
        boolean wasEvicted() {
            return true;
        }
    },
    SIZE {
        @Override // com.google.common.cache.RemovalCause
        boolean wasEvicted() {
            return true;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean wasEvicted();
}
