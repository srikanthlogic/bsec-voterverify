package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class StagingArea {
    private static final Class<?> TAG = StagingArea.class;
    private Map<CacheKey, EncodedImage> mMap = new HashMap();

    private StagingArea() {
    }

    public static StagingArea getInstance() {
        return new StagingArea();
    }

    public synchronized void put(CacheKey key, EncodedImage encodedImage) {
        Preconditions.checkNotNull(key);
        Preconditions.checkArgument(EncodedImage.isValid(encodedImage));
        EncodedImage.closeSafely(this.mMap.put(key, EncodedImage.cloneOrNull(encodedImage)));
        logStats();
    }

    public void clearAll() {
        List<EncodedImage> old;
        synchronized (this) {
            old = new ArrayList<>(this.mMap.values());
            this.mMap.clear();
        }
        for (int i = 0; i < old.size(); i++) {
            EncodedImage encodedImage = old.get(i);
            if (encodedImage != null) {
                encodedImage.close();
            }
        }
    }

    public boolean remove(CacheKey key) {
        EncodedImage encodedImage;
        Preconditions.checkNotNull(key);
        synchronized (this) {
            encodedImage = this.mMap.remove(key);
        }
        if (encodedImage == null) {
            return false;
        }
        try {
            return encodedImage.isValid();
        } finally {
            encodedImage.close();
        }
    }

    public synchronized boolean remove(CacheKey key, EncodedImage encodedImage) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(encodedImage);
        Preconditions.checkArgument(EncodedImage.isValid(encodedImage));
        EncodedImage oldValue = this.mMap.get(key);
        if (oldValue == null) {
            return false;
        }
        CloseableReference<PooledByteBuffer> oldRef = oldValue.getByteBufferRef();
        CloseableReference<PooledByteBuffer> ref = encodedImage.getByteBufferRef();
        if (!(oldRef == null || ref == null || oldRef.get() != ref.get())) {
            this.mMap.remove(key);
            CloseableReference.closeSafely(ref);
            CloseableReference.closeSafely(oldRef);
            EncodedImage.closeSafely(oldValue);
            logStats();
            return true;
        }
        CloseableReference.closeSafely(ref);
        CloseableReference.closeSafely(oldRef);
        EncodedImage.closeSafely(oldValue);
        return false;
    }

    @Nullable
    public synchronized EncodedImage get(CacheKey key) {
        Throwable th;
        Preconditions.checkNotNull(key);
        EncodedImage storedEncodedImage = this.mMap.get(key);
        if (storedEncodedImage != null) {
            synchronized (storedEncodedImage) {
                try {
                    if (!EncodedImage.isValid(storedEncodedImage)) {
                        try {
                            this.mMap.remove(key);
                            FLog.w(TAG, "Found closed reference %d for key %s (%d)", Integer.valueOf(System.identityHashCode(storedEncodedImage)), key.getUriString(), Integer.valueOf(System.identityHashCode(key)));
                            return null;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } else {
                        EncodedImage storedEncodedImage2 = EncodedImage.cloneOrNull(storedEncodedImage);
                        try {
                            storedEncodedImage = storedEncodedImage2;
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                while (true) {
                    try {
                        break;
                    } catch (Throwable th5) {
                        th = th5;
                    }
                }
                throw th;
            }
        }
        return storedEncodedImage;
    }

    public synchronized boolean containsKey(CacheKey key) {
        Preconditions.checkNotNull(key);
        if (!this.mMap.containsKey(key)) {
            return false;
        }
        EncodedImage storedEncodedImage = this.mMap.get(key);
        synchronized (storedEncodedImage) {
            if (EncodedImage.isValid(storedEncodedImage)) {
                return true;
            }
            this.mMap.remove(key);
            FLog.w(TAG, "Found closed reference %d for key %s (%d)", Integer.valueOf(System.identityHashCode(storedEncodedImage)), key.getUriString(), Integer.valueOf(System.identityHashCode(key)));
            return false;
        }
    }

    private synchronized void logStats() {
        FLog.v(TAG, "Count = %d", Integer.valueOf(this.mMap.size()));
    }
}
