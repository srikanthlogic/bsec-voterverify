package com.facebook.imagepipeline.bitmaps;

import android.os.Build;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.platform.PlatformDecoder;
/* loaded from: classes.dex */
public class PlatformBitmapFactoryProvider {
    public static PlatformBitmapFactory buildPlatformBitmapFactory(PoolFactory poolFactory, PlatformDecoder platformDecoder) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ArtBitmapFactory(poolFactory.getBitmapPool());
        }
        if (Build.VERSION.SDK_INT >= 11) {
            return new HoneycombBitmapFactory(new EmptyJpegGenerator(poolFactory.getPooledByteBufferFactory()), platformDecoder);
        }
        return new GingerbreadBitmapFactory();
    }
}
