package com.facebook.drawee.backends.pipeline.info;

import androidx.core.os.EnvironmentCompat;
import com.facebook.imagepipeline.producers.BitmapMemoryCacheGetProducer;
import com.facebook.imagepipeline.producers.BitmapMemoryCacheProducer;
import com.facebook.imagepipeline.producers.DataFetchProducer;
import com.facebook.imagepipeline.producers.DiskCacheReadProducer;
import com.facebook.imagepipeline.producers.EncodedMemoryCacheProducer;
import com.facebook.imagepipeline.producers.LocalAssetFetchProducer;
import com.facebook.imagepipeline.producers.LocalContentUriFetchProducer;
import com.facebook.imagepipeline.producers.LocalContentUriThumbnailFetchProducer;
import com.facebook.imagepipeline.producers.LocalFileFetchProducer;
import com.facebook.imagepipeline.producers.LocalResourceFetchProducer;
import com.facebook.imagepipeline.producers.NetworkFetchProducer;
import com.google.android.gms.common.internal.ImagesContract;
/* loaded from: classes.dex */
public class ImageOriginUtils {
    public static String toString(int imageOrigin) {
        if (imageOrigin == 2) {
            return "network";
        }
        if (imageOrigin == 3) {
            return "disk";
        }
        if (imageOrigin == 4) {
            return "memory_encoded";
        }
        if (imageOrigin == 5) {
            return "memory_bitmap";
        }
        if (imageOrigin != 6) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return ImagesContract.LOCAL;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int mapProducerNameToImageOrigin(String producerName) {
        char c;
        switch (producerName.hashCode()) {
            case -1914072202:
                if (producerName.equals(BitmapMemoryCacheGetProducer.PRODUCER_NAME)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1683996557:
                if (producerName.equals(LocalResourceFetchProducer.PRODUCER_NAME)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1579985851:
                if (producerName.equals(LocalFileFetchProducer.PRODUCER_NAME)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1307634203:
                if (producerName.equals(EncodedMemoryCacheProducer.PRODUCER_NAME)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1224383234:
                if (producerName.equals(NetworkFetchProducer.PRODUCER_NAME)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 656304759:
                if (producerName.equals(DiskCacheReadProducer.PRODUCER_NAME)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 957714404:
                if (producerName.equals(BitmapMemoryCacheProducer.PRODUCER_NAME)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1019542023:
                if (producerName.equals(LocalAssetFetchProducer.PRODUCER_NAME)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1721672898:
                if (producerName.equals(DataFetchProducer.PRODUCER_NAME)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1793127518:
                if (producerName.equals(LocalContentUriThumbnailFetchProducer.PRODUCER_NAME)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 2113652014:
                if (producerName.equals(LocalContentUriFetchProducer.PRODUCER_NAME)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
                return 6;
            default:
                return 1;
        }
    }

    private ImageOriginUtils() {
    }
}
