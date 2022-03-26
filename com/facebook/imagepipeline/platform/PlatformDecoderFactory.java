package com.facebook.imagepipeline.platform;

import android.os.Build;
import androidx.core.util.Pools;
import com.facebook.imagepipeline.memory.PoolFactory;
/* loaded from: classes.dex */
public class PlatformDecoderFactory {
    public static PlatformDecoder buildPlatformDecoder(PoolFactory poolFactory, boolean gingerbreadDecoderEnabled) {
        if (Build.VERSION.SDK_INT >= 26) {
            int maxNumThreads = poolFactory.getFlexByteArrayPoolMaxNumThreads();
            return new OreoDecoder(poolFactory.getBitmapPool(), maxNumThreads, new Pools.SynchronizedPool(maxNumThreads));
        } else if (Build.VERSION.SDK_INT >= 21) {
            int maxNumThreads2 = poolFactory.getFlexByteArrayPoolMaxNumThreads();
            return new ArtDecoder(poolFactory.getBitmapPool(), maxNumThreads2, new Pools.SynchronizedPool(maxNumThreads2));
        } else if (!gingerbreadDecoderEnabled || Build.VERSION.SDK_INT >= 19) {
            return new KitKatPurgeableDecoder(poolFactory.getFlexByteArrayPool());
        } else {
            return new GingerbreadPurgeableDecoder();
        }
    }
}
