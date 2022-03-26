package com.facebook.imagepipeline.image;

import android.graphics.ColorSpace;
import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.SharedReference;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.common.BytesRange;
import com.facebook.imageutils.BitmapUtil;
import com.facebook.imageutils.HeifExifUtil;
import com.facebook.imageutils.ImageMetaData;
import com.facebook.imageutils.JfifUtil;
import com.facebook.imageutils.WebpUtil;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class EncodedImage implements Closeable {
    public static final int DEFAULT_SAMPLE_SIZE;
    public static final int UNKNOWN_HEIGHT;
    public static final int UNKNOWN_ROTATION_ANGLE;
    public static final int UNKNOWN_STREAM_SIZE;
    public static final int UNKNOWN_WIDTH;
    @Nullable
    private BytesRange mBytesRange;
    @Nullable
    private ColorSpace mColorSpace;
    private int mExifOrientation;
    private int mHeight;
    private ImageFormat mImageFormat;
    @Nullable
    private final Supplier<FileInputStream> mInputStreamSupplier;
    @Nullable
    private final CloseableReference<PooledByteBuffer> mPooledByteBufferRef;
    private int mRotationAngle;
    private int mSampleSize;
    private int mStreamSize;
    private int mWidth;

    public EncodedImage(CloseableReference<PooledByteBuffer> pooledByteBufferRef) {
        this.mImageFormat = ImageFormat.UNKNOWN;
        this.mRotationAngle = -1;
        this.mExifOrientation = 0;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mSampleSize = 1;
        this.mStreamSize = -1;
        Preconditions.checkArgument(CloseableReference.isValid(pooledByteBufferRef));
        this.mPooledByteBufferRef = pooledByteBufferRef.clone();
        this.mInputStreamSupplier = null;
    }

    public EncodedImage(Supplier<FileInputStream> inputStreamSupplier) {
        this.mImageFormat = ImageFormat.UNKNOWN;
        this.mRotationAngle = -1;
        this.mExifOrientation = 0;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mSampleSize = 1;
        this.mStreamSize = -1;
        Preconditions.checkNotNull(inputStreamSupplier);
        this.mPooledByteBufferRef = null;
        this.mInputStreamSupplier = inputStreamSupplier;
    }

    public EncodedImage(Supplier<FileInputStream> inputStreamSupplier, int streamSize) {
        this(inputStreamSupplier);
        this.mStreamSize = streamSize;
    }

    @Nullable
    public static EncodedImage cloneOrNull(EncodedImage encodedImage) {
        if (encodedImage != null) {
            return encodedImage.cloneOrNull();
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    @Nullable
    public EncodedImage cloneOrNull() {
        EncodedImage encodedImage;
        EncodedImage encodedImage2;
        Supplier<FileInputStream> supplier = this.mInputStreamSupplier;
        if (supplier != null) {
            encodedImage = new EncodedImage(supplier, this.mStreamSize);
        } else {
            CloseableReference<PooledByteBuffer> pooledByteBufferRef = CloseableReference.cloneOrNull(this.mPooledByteBufferRef);
            if (pooledByteBufferRef == null) {
                encodedImage2 = null;
            } else {
                try {
                    encodedImage2 = new EncodedImage(pooledByteBufferRef);
                } catch (Throwable th) {
                    CloseableReference.closeSafely(pooledByteBufferRef);
                    throw th;
                }
            }
            CloseableReference.closeSafely(pooledByteBufferRef);
            encodedImage = encodedImage2;
        }
        if (encodedImage != null) {
            encodedImage.copyMetaDataFrom(this);
        }
        return encodedImage;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        CloseableReference.closeSafely(this.mPooledByteBufferRef);
    }

    public synchronized boolean isValid() {
        boolean z;
        if (!CloseableReference.isValid(this.mPooledByteBufferRef)) {
            if (this.mInputStreamSupplier == null) {
                z = false;
            }
        }
        z = true;
        return z;
    }

    public CloseableReference<PooledByteBuffer> getByteBufferRef() {
        return CloseableReference.cloneOrNull(this.mPooledByteBufferRef);
    }

    @Nullable
    public InputStream getInputStream() {
        Supplier<FileInputStream> supplier = this.mInputStreamSupplier;
        if (supplier != null) {
            return supplier.get();
        }
        CloseableReference<PooledByteBuffer> pooledByteBufferRef = CloseableReference.cloneOrNull(this.mPooledByteBufferRef);
        if (pooledByteBufferRef == null) {
            return null;
        }
        try {
            return new PooledByteBufferInputStream(pooledByteBufferRef.get());
        } finally {
            CloseableReference.closeSafely(pooledByteBufferRef);
        }
    }

    public void setImageFormat(ImageFormat imageFormat) {
        this.mImageFormat = imageFormat;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setRotationAngle(int rotationAngle) {
        this.mRotationAngle = rotationAngle;
    }

    public void setExifOrientation(int exifOrientation) {
        this.mExifOrientation = exifOrientation;
    }

    public void setSampleSize(int sampleSize) {
        this.mSampleSize = sampleSize;
    }

    public void setStreamSize(int streamSize) {
        this.mStreamSize = streamSize;
    }

    public void setBytesRange(@Nullable BytesRange bytesRange) {
        this.mBytesRange = bytesRange;
    }

    public ImageFormat getImageFormat() {
        parseMetaDataIfNeeded();
        return this.mImageFormat;
    }

    public int getRotationAngle() {
        parseMetaDataIfNeeded();
        return this.mRotationAngle;
    }

    public int getExifOrientation() {
        parseMetaDataIfNeeded();
        return this.mExifOrientation;
    }

    public int getWidth() {
        parseMetaDataIfNeeded();
        return this.mWidth;
    }

    public int getHeight() {
        parseMetaDataIfNeeded();
        return this.mHeight;
    }

    @Nullable
    public ColorSpace getColorSpace() {
        parseMetaDataIfNeeded();
        return this.mColorSpace;
    }

    public int getSampleSize() {
        return this.mSampleSize;
    }

    @Nullable
    public BytesRange getBytesRange() {
        return this.mBytesRange;
    }

    public boolean isCompleteAt(int length) {
        if (this.mImageFormat != DefaultImageFormats.JPEG || this.mInputStreamSupplier != null) {
            return true;
        }
        Preconditions.checkNotNull(this.mPooledByteBufferRef);
        PooledByteBuffer buf = this.mPooledByteBufferRef.get();
        if (buf.read(length - 2) == -1 && buf.read(length - 1) == -39) {
            return true;
        }
        return false;
    }

    public int getSize() {
        CloseableReference<PooledByteBuffer> closeableReference = this.mPooledByteBufferRef;
        if (closeableReference == null || closeableReference.get() == null) {
            return this.mStreamSize;
        }
        return this.mPooledByteBufferRef.get().size();
    }

    public String getFirstBytesAsHexString(int length) {
        CloseableReference<PooledByteBuffer> imageBuffer = getByteBufferRef();
        if (imageBuffer == null) {
            return "";
        }
        int resultSampleSize = Math.min(getSize(), length);
        byte[] bytesBuffer = new byte[resultSampleSize];
        try {
            PooledByteBuffer pooledByteBuffer = imageBuffer.get();
            if (pooledByteBuffer == null) {
                return "";
            }
            pooledByteBuffer.read(0, bytesBuffer, 0, resultSampleSize);
            imageBuffer.close();
            StringBuilder stringBuilder = new StringBuilder(bytesBuffer.length * 2);
            int length2 = bytesBuffer.length;
            for (int i = 0; i < length2; i++) {
                stringBuilder.append(String.format("%02X", Byte.valueOf(bytesBuffer[i])));
            }
            return stringBuilder.toString();
        } finally {
            imageBuffer.close();
        }
    }

    private void parseMetaDataIfNeeded() {
        if (this.mWidth < 0 || this.mHeight < 0) {
            parseMetaData();
        }
    }

    public void parseMetaData() {
        Pair<Integer, Integer> dimensions;
        ImageFormat imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(getInputStream());
        this.mImageFormat = imageFormat;
        if (DefaultImageFormats.isWebpFormat(imageFormat)) {
            dimensions = readWebPImageSize();
        } else {
            dimensions = readImageMetaData().getDimensions();
        }
        if (imageFormat == DefaultImageFormats.JPEG && this.mRotationAngle == -1) {
            if (dimensions != null) {
                this.mExifOrientation = JfifUtil.getOrientation(getInputStream());
                this.mRotationAngle = JfifUtil.getAutoRotateAngleFromOrientation(this.mExifOrientation);
            }
        } else if (imageFormat == DefaultImageFormats.HEIF && this.mRotationAngle == -1) {
            this.mExifOrientation = HeifExifUtil.getOrientation(getInputStream());
            this.mRotationAngle = JfifUtil.getAutoRotateAngleFromOrientation(this.mExifOrientation);
        } else {
            this.mRotationAngle = 0;
        }
    }

    private Pair<Integer, Integer> readWebPImageSize() {
        Pair<Integer, Integer> dimensions = WebpUtil.getSize(getInputStream());
        if (dimensions != null) {
            this.mWidth = ((Integer) dimensions.first).intValue();
            this.mHeight = ((Integer) dimensions.second).intValue();
        }
        return dimensions;
    }

    private ImageMetaData readImageMetaData() {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream();
            ImageMetaData metaData = BitmapUtil.decodeDimensionsAndColorSpace(inputStream);
            this.mColorSpace = metaData.getColorSpace();
            Pair<Integer, Integer> dimensions = metaData.getDimensions();
            if (dimensions != null) {
                this.mWidth = ((Integer) dimensions.first).intValue();
                this.mHeight = ((Integer) dimensions.second).intValue();
            }
            return metaData;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void copyMetaDataFrom(EncodedImage encodedImage) {
        this.mImageFormat = encodedImage.getImageFormat();
        this.mWidth = encodedImage.getWidth();
        this.mHeight = encodedImage.getHeight();
        this.mRotationAngle = encodedImage.getRotationAngle();
        this.mExifOrientation = encodedImage.getExifOrientation();
        this.mSampleSize = encodedImage.getSampleSize();
        this.mStreamSize = encodedImage.getSize();
        this.mBytesRange = encodedImage.getBytesRange();
        this.mColorSpace = encodedImage.getColorSpace();
    }

    public static boolean isMetaDataAvailable(EncodedImage encodedImage) {
        return encodedImage.mRotationAngle >= 0 && encodedImage.mWidth >= 0 && encodedImage.mHeight >= 0;
    }

    public static void closeSafely(@Nullable EncodedImage encodedImage) {
        if (encodedImage != null) {
            encodedImage.close();
        }
    }

    public static boolean isValid(@Nullable EncodedImage encodedImage) {
        return encodedImage != null && encodedImage.isValid();
    }

    @Nullable
    public synchronized SharedReference<PooledByteBuffer> getUnderlyingReferenceTestOnly() {
        return this.mPooledByteBufferRef != null ? this.mPooledByteBufferRef.getUnderlyingReferenceTestOnly() : null;
    }
}
