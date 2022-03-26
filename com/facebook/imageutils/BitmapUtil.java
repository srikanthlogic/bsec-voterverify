package com.facebook.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.util.Pair;
import androidx.core.util.Pools;
import com.facebook.common.internal.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public final class BitmapUtil {
    public static final int ALPHA_8_BYTES_PER_PIXEL;
    public static final int ARGB_4444_BYTES_PER_PIXEL;
    public static final int ARGB_8888_BYTES_PER_PIXEL;
    private static final Pools.SynchronizedPool<ByteBuffer> DECODE_BUFFERS = new Pools.SynchronizedPool<>(12);
    private static final int DECODE_BUFFER_SIZE;
    public static final float MAX_BITMAP_SIZE;
    private static final int POOL_SIZE;
    public static final int RGBA_F16_BYTES_PER_PIXEL;
    public static final int RGB_565_BYTES_PER_PIXEL;

    public static int getSizeInBytes(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT > 19) {
            try {
                return bitmap.getAllocationByteCount();
            } catch (NullPointerException e) {
            }
        }
        if (Build.VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Nullable
    public static Pair<Integer, Integer> decodeDimensions(byte[] bytes) {
        return decodeDimensions(new ByteArrayInputStream(bytes));
    }

    @Nullable
    public static Pair<Integer, Integer> decodeDimensions(Uri uri) {
        Preconditions.checkNotNull(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        return new Pair<>(Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
    }

    @Nullable
    public static Pair<Integer, Integer> decodeDimensions(InputStream is) {
        Preconditions.checkNotNull(is);
        ByteBuffer byteBuffer = DECODE_BUFFERS.acquire();
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(16384);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            options.inTempStorage = byteBuffer.array();
            Pair<Integer, Integer> pair = null;
            BitmapFactory.decodeStream(is, null, options);
            if (!(options.outWidth == -1 || options.outHeight == -1)) {
                pair = new Pair<>(Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
            }
            return pair;
        } finally {
            DECODE_BUFFERS.release(byteBuffer);
        }
    }

    public static ImageMetaData decodeDimensionsAndColorSpace(InputStream is) {
        Preconditions.checkNotNull(is);
        ByteBuffer byteBuffer = DECODE_BUFFERS.acquire();
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(16384);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            options.inTempStorage = byteBuffer.array();
            BitmapFactory.decodeStream(is, null, options);
            ColorSpace colorSpace = null;
            if (Build.VERSION.SDK_INT >= 26) {
                colorSpace = options.outColorSpace;
            }
            return new ImageMetaData(options.outWidth, options.outHeight, colorSpace);
        } finally {
            DECODE_BUFFERS.release(byteBuffer);
        }
    }

    /* renamed from: com.facebook.imageutils.BitmapUtil$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ALPHA_8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGB_565.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGBA_F16.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static int getPixelSizeForBitmapConfig(Bitmap.Config bitmapConfig) {
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[bitmapConfig.ordinal()];
        if (i == 1) {
            return 4;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3 || i == 4) {
            return 2;
        }
        if (i == 5) {
            return 8;
        }
        throw new UnsupportedOperationException("The provided Bitmap.Config is not supported");
    }

    public static int getSizeInByteForBitmap(int width, int height, Bitmap.Config bitmapConfig) {
        return width * height * getPixelSizeForBitmapConfig(bitmapConfig);
    }
}
