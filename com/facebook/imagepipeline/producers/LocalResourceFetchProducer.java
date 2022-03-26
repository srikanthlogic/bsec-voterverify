package com.facebook.imagepipeline.producers;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.io.IOException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class LocalResourceFetchProducer extends LocalFetchProducer {
    public static final String PRODUCER_NAME = "LocalResourceFetchProducer";
    private final Resources mResources;

    public LocalResourceFetchProducer(Executor executor, PooledByteBufferFactory pooledByteBufferFactory, Resources resources) {
        super(executor, pooledByteBufferFactory);
        this.mResources = resources;
    }

    @Override // com.facebook.imagepipeline.producers.LocalFetchProducer
    protected EncodedImage getEncodedImage(ImageRequest imageRequest) throws IOException {
        return getEncodedImage(this.mResources.openRawResource(getResourceId(imageRequest)), getLength(imageRequest));
    }

    private int getLength(ImageRequest imageRequest) {
        AssetFileDescriptor fd = null;
        try {
            fd = this.mResources.openRawResourceFd(getResourceId(imageRequest));
            int length = (int) fd.getLength();
            if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e) {
                }
            }
            return length;
        } catch (Resources.NotFoundException e2) {
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

    private static int getResourceId(ImageRequest imageRequest) {
        return Integer.parseInt(imageRequest.getSourceUri().getPath().substring(1));
    }
}
