package com.facebook.common.references;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public final class CloseableReference<T> implements Cloneable, Closeable {
    private boolean mIsClosed = false;
    private final SharedReference<T> mSharedReference;
    private static Class<CloseableReference> TAG = CloseableReference.class;
    private static final ResourceReleaser<Closeable> DEFAULT_CLOSEABLE_RELEASER = new ResourceReleaser<Closeable>() { // from class: com.facebook.common.references.CloseableReference.1
        public void release(Closeable value) {
            try {
                Closeables.close(value, true);
            } catch (IOException e) {
            }
        }
    };

    private CloseableReference(SharedReference<T> sharedReference) {
        this.mSharedReference = (SharedReference) Preconditions.checkNotNull(sharedReference);
        sharedReference.addReference();
    }

    private CloseableReference(T t, ResourceReleaser<T> resourceReleaser) {
        this.mSharedReference = new SharedReference<>(t, resourceReleaser);
    }

    public static CloseableReference of(Closeable closeable) {
        if (closeable == null) {
            return null;
        }
        return new CloseableReference(closeable, DEFAULT_CLOSEABLE_RELEASER);
    }

    public static <T> CloseableReference<T> of(T t, ResourceReleaser<T> resourceReleaser) {
        if (t == null) {
            return null;
        }
        return new CloseableReference<>(t, resourceReleaser);
    }

    public synchronized T get() {
        Preconditions.checkState(!this.mIsClosed);
        return this.mSharedReference.get();
    }

    @Override // java.lang.Object
    public synchronized CloseableReference<T> clone() {
        Preconditions.checkState(isValid());
        return new CloseableReference<>(this.mSharedReference);
    }

    @Nullable
    public synchronized CloseableReference<T> cloneOrNull() {
        if (!isValid()) {
            return null;
        }
        return clone();
    }

    public synchronized boolean isValid() {
        return !this.mIsClosed;
    }

    public synchronized SharedReference<T> getUnderlyingReferenceTestOnly() {
        return this.mSharedReference;
    }

    public int getValueHash() {
        if (isValid()) {
            return System.identityHashCode(this.mSharedReference.get());
        }
        return 0;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            if (!this.mIsClosed) {
                this.mIsClosed = true;
                this.mSharedReference.deleteReference();
            }
        }
    }

    public static boolean isValid(@Nullable CloseableReference<?> ref) {
        return ref != null && ref.isValid();
    }

    @Nullable
    public static <T> CloseableReference<T> cloneOrNull(@Nullable CloseableReference<T> ref) {
        if (ref != null) {
            return ref.cloneOrNull();
        }
        return null;
    }

    public static <T> List<CloseableReference<T>> cloneOrNull(Collection<CloseableReference<T>> refs) {
        if (refs == null) {
            return null;
        }
        List<CloseableReference<T>> ret = new ArrayList<>(refs.size());
        for (CloseableReference<T> ref : refs) {
            ret.add(cloneOrNull(ref));
        }
        return ret;
    }

    public static void closeSafely(@Nullable CloseableReference<?> ref) {
        if (ref != null) {
            ref.close();
        }
    }

    public static void closeSafely(@Nullable Iterable<? extends CloseableReference<?>> references) {
        if (references != null) {
            for (CloseableReference<?> ref : references) {
                closeSafely(ref);
            }
        }
    }

    @Override // java.lang.Object
    protected void finalize() throws Throwable {
        try {
            synchronized (this) {
                if (!this.mIsClosed) {
                    FLog.w(TAG, "Finalized without closing: %x %x (type = %s)", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(this.mSharedReference)), this.mSharedReference.get().getClass().getName());
                    close();
                }
            }
        } finally {
            super.finalize();
        }
    }
}
