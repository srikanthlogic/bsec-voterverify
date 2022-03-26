package com.facebook.imagepipeline.animated.factory;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.core.ExecutorSupplier;
import com.facebook.imagepipeline.image.CloseableImage;
/* loaded from: classes.dex */
public class AnimatedFactoryProvider {
    private static AnimatedFactory sImpl = null;
    private static boolean sImplLoaded;

    public static AnimatedFactory getAnimatedFactory(PlatformBitmapFactory platformBitmapFactory, ExecutorSupplier executorSupplier, CountingMemoryCache<CacheKey, CloseableImage> backingCache, boolean downscaleFrameToDrawableDimensions) {
        if (!sImplLoaded) {
            try {
                sImpl = (AnimatedFactory) Class.forName("com.facebook.fresco.animation.factory.AnimatedFactoryV2Impl").getConstructor(PlatformBitmapFactory.class, ExecutorSupplier.class, CountingMemoryCache.class, Boolean.TYPE).newInstance(platformBitmapFactory, executorSupplier, backingCache, Boolean.valueOf(downscaleFrameToDrawableDimensions));
            } catch (Throwable th) {
            }
            if (sImpl != null) {
                sImplLoaded = true;
            }
        }
        return sImpl;
    }
}
