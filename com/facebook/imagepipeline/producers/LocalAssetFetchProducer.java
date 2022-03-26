package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class LocalAssetFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalAssetFetchProducer";
    private final AssetManager mAssetManager;

    public LocalAssetFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, AssetManager assetManager) {
        super(executor, pooledByteBufferFactory);
        this.mAssetManager = assetManager;
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        return getEncodedImage(this.mAssetManager.open(getAssetName(imageRequest), 2), getLength(imageRequest));
    }

    private int getLength(ImageRequest imageRequest) {
        AssetFileDescriptor fd = null;
        try {
            fd = this.mAssetManager.openFd(getAssetName(imageRequest));
            int length = (int) fd.getLength();
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e) {
                }
            }
            return length;
        } catch (IOException e2) {
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e3) {
                }
            }
            return -1;
        } catch (Throwable th) {
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected String getProducerName() {
        return PRODUCER_NAME;
    }

    private static String getAssetName(ImageRequest imageRequest) {
        return imageRequest.getSourceUri().getPath().substring(1);
    }
}
