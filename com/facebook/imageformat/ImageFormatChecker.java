package com.facebook.imageformat;

import com.facebook.common.internal.ByteStreams;
import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.imageformat.ImageFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ImageFormatChecker {
    private static ImageFormatChecker sInstance;
    @Nullable
    private List<ImageFormat.FormatChecker> mCustomImageFormatCheckers;
    private final ImageFormat.FormatChecker mDefaultFormatChecker = new DefaultImageFormatChecker();
    private int mMaxHeaderLength;

    private ImageFormatChecker() {
        updateMaxHeaderLength();
    }

    public void setCustomImageFormatCheckers(@Nullable List<ImageFormat.FormatChecker> customImageFormatCheckers) {
        this.mCustomImageFormatCheckers = customImageFormatCheckers;
        updateMaxHeaderLength();
    }

    public ImageFormat determineImageFormat(InputStream is) throws IOException {
        Preconditions.checkNotNull(is);
        int i = this.mMaxHeaderLength;
        byte[] imageHeaderBytes = new byte[i];
        int headerSize = readHeaderFromStream(i, is, imageHeaderBytes);
        ImageFormat format = this.mDefaultFormatChecker.determineFormat(imageHeaderBytes, headerSize);
        if (!(format == null || format == ImageFormat.UNKNOWN)) {
            return format;
        }
        List<ImageFormat.FormatChecker> list = this.mCustomImageFormatCheckers;
        if (list != null) {
            for (ImageFormat.FormatChecker formatChecker : list) {
                ImageFormat format2 = formatChecker.determineFormat(imageHeaderBytes, headerSize);
                if (!(format2 == null || format2 == ImageFormat.UNKNOWN)) {
                    return format2;
                }
            }
        }
        return ImageFormat.UNKNOWN;
    }

    private void updateMaxHeaderLength() {
        this.mMaxHeaderLength = this.mDefaultFormatChecker.getHeaderSize();
        List<ImageFormat.FormatChecker> list = this.mCustomImageFormatCheckers;
        if (list != null) {
            for (ImageFormat.FormatChecker checker : list) {
                this.mMaxHeaderLength = Math.max(this.mMaxHeaderLength, checker.getHeaderSize());
            }
        }
    }

    private static int readHeaderFromStream(int maxHeaderLength, InputStream is, byte[] imageHeaderBytes) throws IOException {
        Preconditions.checkNotNull(is);
        Preconditions.checkNotNull(imageHeaderBytes);
        Preconditions.checkArgument(imageHeaderBytes.length >= maxHeaderLength);
        if (!is.markSupported()) {
            return ByteStreams.read(is, imageHeaderBytes, 0, maxHeaderLength);
        }
        try {
            is.mark(maxHeaderLength);
            return ByteStreams.read(is, imageHeaderBytes, 0, maxHeaderLength);
        } finally {
            is.reset();
        }
    }

    public static synchronized ImageFormatChecker getInstance() {
        ImageFormatChecker imageFormatChecker;
        synchronized (ImageFormatChecker.class) {
            if (sInstance == null) {
                sInstance = new ImageFormatChecker();
            }
            imageFormatChecker = sInstance;
        }
        return imageFormatChecker;
    }

    public static ImageFormat getImageFormat(InputStream is) throws IOException {
        return getInstance().determineImageFormat(is);
    }

    public static ImageFormat getImageFormat_WrapIOException(InputStream is) {
        try {
            return getImageFormat(is);
        } catch (IOException ioe) {
            throw Throwables.propagate(ioe);
        }
    }

    public static ImageFormat getImageFormat(String filename) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filename);
            return getImageFormat(fileInputStream);
        } catch (IOException e) {
            return ImageFormat.UNKNOWN;
        } finally {
            Closeables.closeQuietly(fileInputStream);
        }
    }
}
