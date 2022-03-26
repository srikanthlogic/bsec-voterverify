package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
/* loaded from: classes3.dex */
public final class LruCache implements Cache {
    final android.util.LruCache<String, BitmapAndSize> cache;

    public LruCache(Context context) {
        this(Utils.calculateMemoryCacheSize(context));
    }

    public LruCache(int maxByteCount) {
        this.cache = new android.util.LruCache<String, BitmapAndSize>(maxByteCount) { // from class: com.squareup.picasso.LruCache.1
            /* JADX INFO: Access modifiers changed from: protected */
            public int sizeOf(String key, BitmapAndSize value) {
                return value.byteCount;
            }
        };
    }

    @Override // com.squareup.picasso.Cache
    public Bitmap get(String key) {
        BitmapAndSize bitmapAndSize = this.cache.get(key);
        if (bitmapAndSize != null) {
            return bitmapAndSize.bitmap;
        }
        return null;
    }

    @Override // com.squareup.picasso.Cache
    public void set(String key, Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new NullPointerException("key == null || bitmap == null");
        }
        int byteCount = Utils.getBitmapBytes(bitmap);
        if (byteCount > maxSize()) {
            this.cache.remove(key);
        } else {
            this.cache.put(key, new BitmapAndSize(bitmap, byteCount));
        }
    }

    @Override // com.squareup.picasso.Cache
    public int size() {
        return this.cache.size();
    }

    @Override // com.squareup.picasso.Cache
    public int maxSize() {
        return this.cache.maxSize();
    }

    @Override // com.squareup.picasso.Cache
    public void clear() {
        this.cache.evictAll();
    }

    @Override // com.squareup.picasso.Cache
    public void clearKeyUri(String uri) {
        for (String key : this.cache.snapshot().keySet()) {
            if (key.startsWith(uri) && key.length() > uri.length() && key.charAt(uri.length()) == '\n') {
                this.cache.remove(key);
            }
        }
    }

    public int hitCount() {
        return this.cache.hitCount();
    }

    public int missCount() {
        return this.cache.missCount();
    }

    public int putCount() {
        return this.cache.putCount();
    }

    public int evictionCount() {
        return this.cache.evictionCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class BitmapAndSize {
        final Bitmap bitmap;
        final int byteCount;

        BitmapAndSize(Bitmap bitmap, int byteCount) {
            this.bitmap = bitmap;
            this.byteCount = byteCount;
        }
    }
}
