package com.facebook.imageformat;

import com.facebook.common.internal.Ints;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.webp.WebpSupportStatus;
import com.facebook.imageformat.ImageFormat;
import com.google.common.base.Ascii;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DefaultImageFormatChecker implements ImageFormat.FormatChecker {
    private static final int EXTENDED_WEBP_HEADER_LENGTH = 21;
    private static final int GIF_HEADER_LENGTH = 6;
    private static final int SIMPLE_WEBP_HEADER_LENGTH = 20;
    final int MAX_HEADER_LENGTH = Ints.max(21, 20, JPEG_HEADER_LENGTH, PNG_HEADER_LENGTH, 6, BMP_HEADER_LENGTH, ICO_HEADER_LENGTH, HEIF_HEADER_LENGTH);
    private static final byte[] JPEG_HEADER = {-1, -40, -1};
    private static final int JPEG_HEADER_LENGTH = JPEG_HEADER.length;
    private static final byte[] PNG_HEADER = {-119, 80, 78, 71, Ascii.CR, 10, Ascii.SUB, 10};
    private static final int PNG_HEADER_LENGTH = PNG_HEADER.length;
    private static final byte[] GIF_HEADER_87A = ImageFormatCheckerUtils.asciiBytes("GIF87a");
    private static final byte[] GIF_HEADER_89A = ImageFormatCheckerUtils.asciiBytes("GIF89a");
    private static final byte[] BMP_HEADER = ImageFormatCheckerUtils.asciiBytes("BM");
    private static final int BMP_HEADER_LENGTH = BMP_HEADER.length;
    private static final byte[] ICO_HEADER = {0, 0, 1, 0};
    private static final int ICO_HEADER_LENGTH = ICO_HEADER.length;
    private static final String[] HEIF_HEADER_SUFFIXES = {"heic", "heix", "hevc", "hevx", "mif1", "msf1"};
    private static final String HEIF_HEADER_PREFIX = "ftyp";
    private static final int HEIF_HEADER_LENGTH = ImageFormatCheckerUtils.asciiBytes(HEIF_HEADER_PREFIX + HEIF_HEADER_SUFFIXES[0]).length;

    @Override // com.facebook.imageformat.ImageFormat.FormatChecker
    public int getHeaderSize() {
        return this.MAX_HEADER_LENGTH;
    }

    @Override // com.facebook.imageformat.ImageFormat.FormatChecker
    @Nullable
    public final ImageFormat determineFormat(byte[] headerBytes, int headerSize) {
        Preconditions.checkNotNull(headerBytes);
        if (WebpSupportStatus.isWebpHeader(headerBytes, 0, headerSize)) {
            return getWebpFormat(headerBytes, headerSize);
        }
        if (isJpegHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.JPEG;
        }
        if (isPngHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.PNG;
        }
        if (isGifHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.GIF;
        }
        if (isBmpHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.BMP;
        }
        if (isIcoHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.ICO;
        }
        if (isHeifHeader(headerBytes, headerSize)) {
            return DefaultImageFormats.HEIF;
        }
        return ImageFormat.UNKNOWN;
    }

    private static ImageFormat getWebpFormat(byte[] imageHeaderBytes, int headerSize) {
        Preconditions.checkArgument(WebpSupportStatus.isWebpHeader(imageHeaderBytes, 0, headerSize));
        if (WebpSupportStatus.isSimpleWebpHeader(imageHeaderBytes, 0)) {
            return DefaultImageFormats.WEBP_SIMPLE;
        }
        if (WebpSupportStatus.isLosslessWebpHeader(imageHeaderBytes, 0)) {
            return DefaultImageFormats.WEBP_LOSSLESS;
        }
        if (!WebpSupportStatus.isExtendedWebpHeader(imageHeaderBytes, 0, headerSize)) {
            return ImageFormat.UNKNOWN;
        }
        if (WebpSupportStatus.isAnimatedWebpHeader(imageHeaderBytes, 0)) {
            return DefaultImageFormats.WEBP_ANIMATED;
        }
        if (WebpSupportStatus.isExtendedWebpHeaderWithAlpha(imageHeaderBytes, 0)) {
            return DefaultImageFormats.WEBP_EXTENDED_WITH_ALPHA;
        }
        return DefaultImageFormats.WEBP_EXTENDED;
    }

    private static boolean isJpegHeader(byte[] imageHeaderBytes, int headerSize) {
        byte[] bArr = JPEG_HEADER;
        return headerSize >= bArr.length && ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, bArr);
    }

    private static boolean isPngHeader(byte[] imageHeaderBytes, int headerSize) {
        byte[] bArr = PNG_HEADER;
        return headerSize >= bArr.length && ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, bArr);
    }

    private static boolean isGifHeader(byte[] imageHeaderBytes, int headerSize) {
        if (headerSize < 6) {
            return false;
        }
        if (ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, GIF_HEADER_87A) || ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, GIF_HEADER_89A)) {
            return true;
        }
        return false;
    }

    private static boolean isBmpHeader(byte[] imageHeaderBytes, int headerSize) {
        byte[] bArr = BMP_HEADER;
        if (headerSize < bArr.length) {
            return false;
        }
        return ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, bArr);
    }

    private static boolean isIcoHeader(byte[] imageHeaderBytes, int headerSize) {
        byte[] bArr = ICO_HEADER;
        if (headerSize < bArr.length) {
            return false;
        }
        return ImageFormatCheckerUtils.startsWithPattern(imageHeaderBytes, bArr);
    }

    private static boolean isHeifHeader(byte[] imageHeaderBytes, int headerSize) {
        if (headerSize < HEIF_HEADER_LENGTH || imageHeaderBytes[3] < 8) {
            return false;
        }
        String[] strArr = HEIF_HEADER_SUFFIXES;
        for (String heifFtype : strArr) {
            if (ImageFormatCheckerUtils.indexOfPattern(imageHeaderBytes, imageHeaderBytes.length, ImageFormatCheckerUtils.asciiBytes(HEIF_HEADER_PREFIX + heifFtype), HEIF_HEADER_LENGTH) > -1) {
                return true;
            }
        }
        return false;
    }
}
