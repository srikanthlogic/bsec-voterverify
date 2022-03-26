package com.facebook.imagepipeline.decoder;

import com.facebook.common.internal.Closeables;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.ByteArrayPool;
import com.facebook.common.memory.PooledByteArrayBufferedInputStream;
import com.facebook.common.util.StreamUtil;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class ProgressiveJpegParser {
    private static final int BUFFER_SIZE = 16384;
    private static final int NOT_A_JPEG = 6;
    private static final int READ_FIRST_JPEG_BYTE = 0;
    private static final int READ_MARKER_FIRST_BYTE_OR_ENTROPY_DATA = 2;
    private static final int READ_MARKER_SECOND_BYTE = 3;
    private static final int READ_SECOND_JPEG_BYTE = 1;
    private static final int READ_SIZE_FIRST_BYTE = 4;
    private static final int READ_SIZE_SECOND_BYTE = 5;
    private final ByteArrayPool mByteArrayPool;
    private boolean mEndMarkerRead;
    private int mBytesParsed = 0;
    private int mLastByteRead = 0;
    private int mNextFullScanNumber = 0;
    private int mBestScanEndOffset = 0;
    private int mBestScanNumber = 0;
    private int mParserState = 0;

    public ProgressiveJpegParser(ByteArrayPool byteArrayPool) {
        this.mByteArrayPool = (ByteArrayPool) Preconditions.checkNotNull(byteArrayPool);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [int] */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.facebook.common.memory.PooledByteArrayBufferedInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v0, types: [com.facebook.imagepipeline.decoder.ProgressiveJpegParser] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public boolean parseMoreData(EncodedImage encodedImage) {
        ?? r2;
        if (this.mParserState == 6 || encodedImage.getSize() <= (r2 = this.mBytesParsed)) {
            return false;
        }
        try {
            r2 = new PooledByteArrayBufferedInputStream(encodedImage.getInputStream(), this.mByteArrayPool.get(16384), this.mByteArrayPool);
            StreamUtil.skip(r2, (long) this.mBytesParsed);
            return doParseMoreData(r2);
        } catch (IOException ioe) {
            Throwables.propagate(ioe);
            return false;
        } finally {
            Closeables.closeQuietly((InputStream) r2);
        }
    }

    private boolean doParseMoreData(InputStream inputStream) {
        int nextByte;
        int oldBestScanNumber = this.mBestScanNumber;
        while (this.mParserState != 6 && (nextByte = inputStream.read()) != -1) {
            try {
                this.mBytesParsed++;
                if (this.mEndMarkerRead) {
                    this.mParserState = 6;
                    this.mEndMarkerRead = false;
                    return false;
                }
                int i = this.mParserState;
                if (i != 0) {
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (i == 4) {
                                    this.mParserState = 5;
                                } else if (i != 5) {
                                    Preconditions.checkState(false);
                                } else {
                                    int bytesToSkip = ((this.mLastByteRead << 8) + nextByte) - 2;
                                    StreamUtil.skip(inputStream, (long) bytesToSkip);
                                    this.mBytesParsed += bytesToSkip;
                                    this.mParserState = 2;
                                }
                            } else if (nextByte == 255) {
                                this.mParserState = 3;
                            } else if (nextByte == 0) {
                                this.mParserState = 2;
                            } else if (nextByte == 217) {
                                this.mEndMarkerRead = true;
                                newScanOrImageEndFound(this.mBytesParsed - 2);
                                this.mParserState = 2;
                            } else {
                                if (nextByte == 218) {
                                    newScanOrImageEndFound(this.mBytesParsed - 2);
                                }
                                if (doesMarkerStartSegment(nextByte)) {
                                    this.mParserState = 4;
                                } else {
                                    this.mParserState = 2;
                                }
                            }
                        } else if (nextByte == 255) {
                            this.mParserState = 3;
                        }
                    } else if (nextByte == 216) {
                        this.mParserState = 2;
                    } else {
                        this.mParserState = 6;
                    }
                } else if (nextByte == 255) {
                    this.mParserState = 1;
                } else {
                    this.mParserState = 6;
                }
                this.mLastByteRead = nextByte;
            } catch (IOException ioe) {
                Throwables.propagate(ioe);
            }
        }
        return (this.mParserState == 6 || this.mBestScanNumber == oldBestScanNumber) ? false : true;
    }

    private static boolean doesMarkerStartSegment(int markerSecondByte) {
        if (markerSecondByte == 1) {
            return false;
        }
        if (markerSecondByte < 208 || markerSecondByte > 215) {
            return (markerSecondByte == 217 || markerSecondByte == 216) ? false : true;
        }
        return false;
    }

    private void newScanOrImageEndFound(int offset) {
        if (this.mNextFullScanNumber > 0) {
            this.mBestScanEndOffset = offset;
        }
        int i = this.mNextFullScanNumber;
        this.mNextFullScanNumber = i + 1;
        this.mBestScanNumber = i;
    }

    public boolean isJpeg() {
        return this.mBytesParsed > 1 && this.mParserState != 6;
    }

    public int getBestScanEndOffset() {
        return this.mBestScanEndOffset;
    }

    public int getBestScanNumber() {
        return this.mBestScanNumber;
    }

    public boolean isEndMarkerRead() {
        return this.mEndMarkerRead;
    }
}
