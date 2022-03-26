package okio.internal;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.base.Ascii;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.EOFException;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;
import okhttp3.internal.connection.RealConnection;
import okio.Buffer;
import okio.ByteString;
import okio.Options;
import okio.Platform;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Utf8;
import okio.Util;
/* compiled from: Buffer.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0000\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\u0013H\u0080\b\u001a\r\u0010\u0014\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u0010\u0015\u001a\u00020\u0013*\u00020\u0013H\u0080\b\u001a%\u0010\u0016\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0017\u0010\u001a\u001a\u00020\n*\u00020\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0080\b\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0005H\u0080\b\u001a\r\u0010 \u001a\u00020\b*\u00020\u0013H\u0080\b\u001a%\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a\u001d\u0010&\u001a\u00020\u0005*\u00020\u00132\u0006\u0010'\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\u0080\b\u001a-\u0010(\u001a\u00020\n*\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\u0080\b\u001a%\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010)\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010+\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020,H\u0080\b\u001a\r\u0010-\u001a\u00020\u001e*\u00020\u0013H\u0080\b\u001a\r\u0010.\u001a\u00020\u0001*\u00020\u0013H\u0080\b\u001a\u0015\u0010.\u001a\u00020\u0001*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u0010/\u001a\u00020%*\u00020\u0013H\u0080\b\u001a\u0015\u0010/\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00100\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\u0015\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\u0080\b\u001a\u001d\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00102\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u00103\u001a\u00020\b*\u00020\u0013H\u0080\b\u001a\r\u00104\u001a\u00020\u0005*\u00020\u0013H\u0080\b\u001a\r\u00105\u001a\u000206*\u00020\u0013H\u0080\b\u001a\u0015\u00107\u001a\u000208*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u00109\u001a\u00020\b*\u00020\u0013H\u0080\b\u001a\u000f\u0010:\u001a\u0004\u0018\u000108*\u00020\u0013H\u0080\b\u001a\u0015\u0010;\u001a\u000208*\u00020\u00132\u0006\u0010<\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010=\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?H\u0080\b\u001a\u0015\u0010@\u001a\u00020\u0012*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\r\u0010A\u001a\u00020%*\u00020\u0013H\u0080\b\u001a\u0015\u0010A\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u0015\u0010B\u001a\u00020\f*\u00020\u00132\u0006\u0010C\u001a\u00020\bH\u0080\b\u001a\u0015\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u0001H\u0080\b\u001a%\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010D\u001a\u00020\u0012*\u00020\u00132\u0006\u0010E\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a)\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010F\u001a\u00020%2\b\b\u0002\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\bH\u0080\b\u001a\u001d\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020G2\u0006\u0010\u0019\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010H\u001a\u00020\u0005*\u00020\u00132\u0006\u0010E\u001a\u00020GH\u0080\b\u001a\u0015\u0010I\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\"\u001a\u00020\bH\u0080\b\u001a\u0015\u0010J\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010L\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010M\u001a\u00020\u0013*\u00020\u00132\u0006\u0010N\u001a\u00020\bH\u0080\b\u001a\u0015\u0010O\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\u0080\b\u001a\u0015\u0010P\u001a\u00020\u0013*\u00020\u00132\u0006\u0010Q\u001a\u00020\bH\u0080\b\u001a%\u0010R\u001a\u00020\u0013*\u00020\u00132\u0006\u0010S\u001a\u0002082\u0006\u0010T\u001a\u00020\b2\u0006\u0010U\u001a\u00020\bH\u0080\b\u001a\u0015\u0010V\u001a\u00020\u0013*\u00020\u00132\u0006\u0010W\u001a\u00020\bH\u0080\b\u001a\u0014\u0010X\u001a\u000208*\u00020\u00132\u0006\u0010Y\u001a\u00020\u0005H\u0000\u001a<\u0010Z\u001a\u0002H[\"\u0004\b\u0000\u0010[*\u00020\u00132\u0006\u0010#\u001a\u00020\u00052\u001a\u0010\\\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H[0]H\u0080\b¢\u0006\u0002\u0010^\u001a\u001e\u0010_\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?2\b\b\u0002\u0010`\u001a\u00020\nH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\bX\u0080T¢\u0006\u0002\n\u0000¨\u0006a"}, d2 = {"HEX_DIGIT_BYTES", "", "getHEX_DIGIT_BYTES", "()[B", "OVERFLOW_DIGIT_START", "", "OVERFLOW_ZONE", "SEGMENTING_THRESHOLD", "", "rangeEquals", "", "segment", "Lokio/Segment;", "segmentPos", "bytes", "bytesOffset", "bytesLimit", "commonClear", "", "Lokio/Buffer;", "commonCompleteSegmentByteCount", "commonCopy", "commonCopyTo", "out", "offset", "byteCount", "commonEquals", "other", "", "commonGet", "", "pos", "commonHashCode", "commonIndexOf", "b", "fromIndex", "toIndex", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonRangeEquals", "commonRead", "sink", "commonReadAll", "Lokio/Sink;", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadLong", "commonReadShort", "", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonSnapshot", "commonWritableSegment", "minimumCapacity", "commonWrite", FirebaseAnalytics.Param.SOURCE, "byteString", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteLong", "commonWriteShort", "s", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "readUtf8Line", "newline", "seek", ExifInterface.GPS_DIRECTION_TRUE, "lambda", "Lkotlin/Function2;", "(Lokio/Buffer;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "selectPrefix", "selectTruncated", "okio"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class BufferKt {
    private static final byte[] HEX_DIGIT_BYTES = Platform.asUtf8ToByteArray("0123456789abcdef");
    public static final long OVERFLOW_DIGIT_START;
    public static final long OVERFLOW_ZONE;
    public static final int SEGMENTING_THRESHOLD;

    public static final byte[] getHEX_DIGIT_BYTES() {
        return HEX_DIGIT_BYTES;
    }

    public static final boolean rangeEquals(Segment segment, int segmentPos, byte[] bytes, int bytesOffset, int bytesLimit) {
        Intrinsics.checkParameterIsNotNull(segment, "segment");
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        Segment segment2 = segment;
        int segmentPos2 = segmentPos;
        int segmentLimit = segment2.limit;
        byte[] data = segment2.data;
        for (int i = bytesOffset; i < bytesLimit; i++) {
            if (segmentPos2 == segmentLimit) {
                Segment segment3 = segment2.next;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                segment2 = segment3;
                data = segment2.data;
                segmentPos2 = segment2.pos;
                segmentLimit = segment2.limit;
            }
            if (data[segmentPos2] != bytes[i]) {
                return false;
            }
            segmentPos2++;
        }
        return true;
    }

    public static final String readUtf8Line(Buffer $this$readUtf8Line, long newline) {
        Intrinsics.checkParameterIsNotNull($this$readUtf8Line, "$this$readUtf8Line");
        if (newline <= 0 || $this$readUtf8Line.getByte(newline - 1) != ((byte) 13)) {
            String result = $this$readUtf8Line.readUtf8(newline);
            $this$readUtf8Line.skip(1);
            return result;
        }
        String result2 = $this$readUtf8Line.readUtf8(newline - 1);
        $this$readUtf8Line.skip(2);
        return result2;
    }

    public static final <T> T seek(Buffer $this$seek, long fromIndex, Function2<? super Segment, ? super Long, ? extends T> function2) {
        Intrinsics.checkParameterIsNotNull($this$seek, "$this$seek");
        Intrinsics.checkParameterIsNotNull(function2, "lambda");
        Segment s = $this$seek.head;
        if (s == null) {
            return (T) function2.invoke(null, -1L);
        }
        if ($this$seek.size() - fromIndex < fromIndex) {
            long offset = $this$seek.size();
            while (offset > fromIndex) {
                Segment segment = s.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                s = segment;
                offset -= (long) (s.limit - s.pos);
            }
            return (T) function2.invoke(s, Long.valueOf(offset));
        }
        long offset2 = 0;
        while (true) {
            long nextOffset = ((long) (s.limit - s.pos)) + offset2;
            if (nextOffset > fromIndex) {
                return (T) function2.invoke(s, Long.valueOf(offset2));
            }
            Segment segment2 = s.next;
            if (segment2 == null) {
                Intrinsics.throwNpe();
            }
            s = segment2;
            offset2 = nextOffset;
        }
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return selectPrefix(buffer, options, z);
    }

    /* JADX INFO: Multiple debug info for r10v2 int: [D('scanOrSelect' int), D('triePos' int)] */
    /* JADX INFO: Multiple debug info for r12v1 int: [D('possiblePrefixIndex' int), D('triePos' int)] */
    /* JADX INFO: Multiple debug info for r4v4 int: [D('$this$and$iv' byte), D('byte' int)] */
    /* JADX INFO: Multiple debug info for r7v10 int: [D('$this$and$iv' byte), D('byte' int)] */
    /* JADX INFO: Multiple debug info for r7v9 byte: [D('pos' int), D('$this$and$iv' byte)] */
    public static final int selectPrefix(Buffer $this$selectPrefix, Options options, boolean selectTruncated) {
        int nextStep;
        int pos;
        Intrinsics.checkParameterIsNotNull($this$selectPrefix, "$this$selectPrefix");
        Intrinsics.checkParameterIsNotNull(options, "options");
        Segment head = $this$selectPrefix.head;
        int i = -1;
        if (head == null) {
            return selectTruncated ? -2 : -1;
        }
        Segment s = head;
        byte[] data = head.data;
        int pos2 = head.pos;
        int limit = head.limit;
        int[] trie = options.getTrie$okio();
        int scanOrSelect = 0;
        int prefixIndex = -1;
        loop0: while (true) {
            int triePos = scanOrSelect + 1;
            int triePos2 = trie[scanOrSelect];
            int triePos3 = triePos + 1;
            int triePos4 = trie[triePos];
            if (triePos4 != i) {
                prefixIndex = triePos4;
            }
            if (s == null) {
                break;
            }
            if (triePos2 < 0) {
                int trieLimit = triePos3 + (triePos2 * -1);
                while (true) {
                    int pos3 = pos2 + 1;
                    int triePos5 = triePos3 + 1;
                    if ((data[pos2] & 255) != trie[triePos3]) {
                        return prefixIndex;
                    }
                    boolean scanComplete = triePos5 == trieLimit;
                    if (pos3 == limit) {
                        if (s == null) {
                            Intrinsics.throwNpe();
                        }
                        Segment segment = s.next;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        s = segment;
                        pos = s.pos;
                        data = s.data;
                        limit = s.limit;
                        if (s == head) {
                            if (!scanComplete) {
                                break loop0;
                            }
                            s = null;
                        }
                    } else {
                        pos = pos3;
                    }
                    if (scanComplete) {
                        nextStep = trie[triePos5];
                        pos2 = pos;
                        break;
                    }
                    pos2 = pos;
                    triePos3 = triePos5;
                }
            } else {
                int pos4 = pos2 + 1;
                int i2 = data[pos2] & 255;
                int selectLimit = triePos3 + triePos2;
                while (triePos3 != selectLimit) {
                    if (i2 == trie[triePos3]) {
                        nextStep = trie[triePos3 + triePos2];
                        if (pos4 == limit) {
                            Segment segment2 = s.next;
                            if (segment2 == null) {
                                Intrinsics.throwNpe();
                            }
                            s = segment2;
                            int pos5 = s.pos;
                            data = s.data;
                            limit = s.limit;
                            if (s == head) {
                                s = null;
                                pos2 = pos5;
                            } else {
                                pos2 = pos5;
                            }
                        } else {
                            pos2 = pos4;
                        }
                    } else {
                        triePos3++;
                    }
                }
                return prefixIndex;
            }
            if (nextStep >= 0) {
                return nextStep;
            }
            scanOrSelect = -nextStep;
            i = -1;
        }
        if (selectTruncated) {
            return -2;
        }
        return prefixIndex;
    }

    public static final Buffer commonCopyTo(Buffer $this$commonCopyTo, Buffer out, long offset, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonCopyTo, "$this$commonCopyTo");
        Intrinsics.checkParameterIsNotNull(out, "out");
        long offset2 = offset;
        long byteCount2 = byteCount;
        Util.checkOffsetAndCount($this$commonCopyTo.size(), offset2, byteCount2);
        if (byteCount2 == 0) {
            return $this$commonCopyTo;
        }
        out.setSize$okio(out.size() + byteCount2);
        Segment s = $this$commonCopyTo.head;
        while (true) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            if (offset2 >= ((long) (s.limit - s.pos))) {
                offset2 -= (long) (s.limit - s.pos);
                s = s.next;
            }
        }
        while (byteCount2 > 0) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            Segment copy = s.sharedCopy();
            copy.pos += (int) offset2;
            copy.limit = Math.min(copy.pos + ((int) byteCount2), copy.limit);
            if (out.head == null) {
                copy.prev = copy;
                copy.next = copy.prev;
                out.head = copy.next;
            } else {
                Segment segment = out.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                Segment segment2 = segment.prev;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                segment2.push(copy);
            }
            byteCount2 -= (long) (copy.limit - copy.pos);
            offset2 = 0;
            s = s.next;
        }
        return $this$commonCopyTo;
    }

    public static final long commonCompleteSegmentByteCount(Buffer $this$commonCompleteSegmentByteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonCompleteSegmentByteCount, "$this$commonCompleteSegmentByteCount");
        long result = $this$commonCompleteSegmentByteCount.size();
        if (result == 0) {
            return 0;
        }
        Segment segment = $this$commonCompleteSegmentByteCount.head;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        Segment tail = segment.prev;
        if (tail == null) {
            Intrinsics.throwNpe();
        }
        if (tail.limit >= 8192 || !tail.owner) {
            return result;
        }
        return result - ((long) (tail.limit - tail.pos));
    }

    /* JADX INFO: Multiple debug info for r2v1 byte: [D('pos' int), D('b' byte)] */
    public static final byte commonReadByte(Buffer $this$commonReadByte) {
        Intrinsics.checkParameterIsNotNull($this$commonReadByte, "$this$commonReadByte");
        if ($this$commonReadByte.size() != 0) {
            Segment segment = $this$commonReadByte.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int pos = segment.pos;
            int limit = segment.limit;
            int pos2 = pos + 1;
            byte b = segment.data[pos];
            $this$commonReadByte.setSize$okio($this$commonReadByte.size() - 1);
            if (pos2 == limit) {
                $this$commonReadByte.head = segment.pop();
                SegmentPool.INSTANCE.recycle(segment);
            } else {
                segment.pos = pos2;
            }
            return b;
        }
        throw new EOFException();
    }

    /* JADX INFO: Multiple debug info for r2v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r7v2 byte: [D('pos' int), D('$this$and$iv' byte)] */
    public static final short commonReadShort(Buffer $this$commonReadShort) {
        Intrinsics.checkParameterIsNotNull($this$commonReadShort, "$this$commonReadShort");
        if ($this$commonReadShort.size() >= 2) {
            Segment segment = $this$commonReadShort.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int pos = segment.pos;
            int limit = segment.limit;
            if (limit - pos < 2) {
                return (short) ((($this$commonReadShort.readByte() & 255) << 8) | ($this$commonReadShort.readByte() & 255));
            }
            byte[] data = segment.data;
            int pos2 = pos + 1;
            int pos3 = pos2 + 1;
            int s = ((data[pos] & 255) << 8) | (data[pos2] & 255);
            $this$commonReadShort.setSize$okio($this$commonReadShort.size() - 2);
            if (pos3 == limit) {
                $this$commonReadShort.head = segment.pop();
                SegmentPool.INSTANCE.recycle(segment);
            } else {
                segment.pos = pos3;
            }
            return (short) s;
        }
        throw new EOFException();
    }

    /* JADX INFO: Multiple debug info for r2v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r7v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r7v5 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r8v2 byte: [D('pos' int), D('$this$and$iv' byte)] */
    public static final int commonReadInt(Buffer $this$commonReadInt) {
        Intrinsics.checkParameterIsNotNull($this$commonReadInt, "$this$commonReadInt");
        if ($this$commonReadInt.size() >= 4) {
            Segment segment = $this$commonReadInt.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int pos = segment.pos;
            int limit = segment.limit;
            if (((long) (limit - pos)) < 4) {
                return (($this$commonReadInt.readByte() & 255) << 24) | (($this$commonReadInt.readByte() & 255) << 16) | (($this$commonReadInt.readByte() & 255) << 8) | ($this$commonReadInt.readByte() & 255);
            }
            byte[] data = segment.data;
            int pos2 = pos + 1;
            int pos3 = pos2 + 1;
            int i = ((data[pos] & 255) << 24) | ((data[pos2] & 255) << 16);
            int pos4 = pos3 + 1;
            int i2 = i | ((data[pos3] & 255) << 8);
            int pos5 = pos4 + 1;
            int i3 = i2 | (data[pos4] & 255);
            $this$commonReadInt.setSize$okio($this$commonReadInt.size() - 4);
            if (pos5 == limit) {
                $this$commonReadInt.head = segment.pop();
                SegmentPool.INSTANCE.recycle(segment);
            } else {
                segment.pos = pos5;
            }
            return i3;
        }
        throw new EOFException();
    }

    /* JADX INFO: Multiple debug info for r3v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r3v4 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r5v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r5v4 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r8v3 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r8v7 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r9v1 byte: [D('pos' int), D('$this$and$iv' byte)] */
    /* JADX INFO: Multiple debug info for r9v4 byte: [D('pos' int), D('$this$and$iv' byte)] */
    public static final long commonReadLong(Buffer $this$commonReadLong) {
        Intrinsics.checkParameterIsNotNull($this$commonReadLong, "$this$commonReadLong");
        if ($this$commonReadLong.size() >= 8) {
            Segment segment = $this$commonReadLong.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int pos = segment.pos;
            int limit = segment.limit;
            if (((long) (limit - pos)) < 8) {
                return ((((long) $this$commonReadLong.readInt()) & 4294967295L) << 32) | (((long) $this$commonReadLong.readInt()) & 4294967295L);
            }
            byte[] data = segment.data;
            int pos2 = pos + 1;
            int pos3 = pos2 + 1;
            long j = (((long) data[pos2]) & 255) << 48;
            int pos4 = pos3 + 1;
            int pos5 = pos4 + 1;
            int pos6 = pos5 + 1;
            int pos7 = pos6 + 1;
            long j2 = j | ((255 & ((long) data[pos])) << 56) | ((255 & ((long) data[pos3])) << 40) | ((((long) data[pos4]) & 255) << 32) | ((255 & ((long) data[pos5])) << 24) | ((((long) data[pos6]) & 255) << 16);
            int pos8 = pos7 + 1;
            int pos9 = pos8 + 1;
            long v = j2 | ((255 & ((long) data[pos7])) << 8) | (((long) data[pos8]) & 255);
            $this$commonReadLong.setSize$okio($this$commonReadLong.size() - 8);
            if (pos9 == limit) {
                $this$commonReadLong.head = segment.pop();
                SegmentPool.INSTANCE.recycle(segment);
            } else {
                segment.pos = pos9;
            }
            return v;
        }
        throw new EOFException();
    }

    public static final byte commonGet(Buffer $this$commonGet, long pos) {
        Intrinsics.checkParameterIsNotNull($this$commonGet, "$this$commonGet");
        Util.checkOffsetAndCount($this$commonGet.size(), pos, 1);
        Segment s$iv = $this$commonGet.head;
        if (s$iv == null) {
            Segment s = null;
            Intrinsics.throwNpe();
            return s.data[(int) ((((long) s.pos) + pos) - -1)];
        } else if ($this$commonGet.size() - pos < pos) {
            long offset$iv = $this$commonGet.size();
            while (offset$iv > pos) {
                Segment segment = s$iv.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                s$iv = segment;
                offset$iv -= (long) (s$iv.limit - s$iv.pos);
            }
            if (s$iv == null) {
                Intrinsics.throwNpe();
            }
            return s$iv.data[(int) ((((long) s$iv.pos) + pos) - offset$iv)];
        } else {
            long offset$iv2 = 0;
            while (true) {
                long nextOffset$iv = ((long) (s$iv.limit - s$iv.pos)) + offset$iv2;
                if (nextOffset$iv > pos) {
                    break;
                }
                Segment segment2 = s$iv.next;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv = segment2;
                offset$iv2 = nextOffset$iv;
            }
            if (s$iv == null) {
                Intrinsics.throwNpe();
            }
            return s$iv.data[(int) ((((long) s$iv.pos) + pos) - offset$iv2)];
        }
    }

    public static final void commonClear(Buffer $this$commonClear) {
        Intrinsics.checkParameterIsNotNull($this$commonClear, "$this$commonClear");
        $this$commonClear.skip($this$commonClear.size());
    }

    /* JADX INFO: Multiple debug info for r4v2 int: [D('toSkip' int), D('b$iv' int)] */
    public static final void commonSkip(Buffer $this$commonSkip, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonSkip, "$this$commonSkip");
        long byteCount2 = byteCount;
        while (byteCount2 > 0) {
            Segment head = $this$commonSkip.head;
            if (head != null) {
                int b$iv = (int) Math.min(byteCount2, (long) (head.limit - head.pos));
                $this$commonSkip.setSize$okio($this$commonSkip.size() - ((long) b$iv));
                byteCount2 -= (long) b$iv;
                head.pos += b$iv;
                if (head.pos == head.limit) {
                    $this$commonSkip.head = head.pop();
                    SegmentPool.INSTANCE.recycle(head);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public static /* synthetic */ Buffer commonWrite$default(Buffer $this$commonWrite, ByteString byteString, int offset, int byteCount, int i, Object obj) {
        if ((i & 2) != 0) {
            offset = 0;
        }
        if ((i & 4) != 0) {
            byteCount = byteString.size();
        }
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(byteString, "byteString");
        byteString.write$okio($this$commonWrite, offset, byteCount);
        return $this$commonWrite;
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(byteString, "byteString");
        byteString.write$okio($this$commonWrite, offset, byteCount);
        return $this$commonWrite;
    }

    public static final Buffer commonWriteDecimalLong(Buffer $this$commonWriteDecimalLong, long v) {
        int width;
        Intrinsics.checkParameterIsNotNull($this$commonWriteDecimalLong, "$this$commonWriteDecimalLong");
        long v2 = v;
        if (v2 == 0) {
            return $this$commonWriteDecimalLong.writeByte(48);
        }
        boolean negative = false;
        if (v2 < 0) {
            v2 = -v2;
            if (v2 < 0) {
                return $this$commonWriteDecimalLong.writeUtf8("-9223372036854775808");
            }
            negative = true;
        }
        if (v2 < 100000000) {
            width = v2 < 10000 ? v2 < 100 ? v2 < 10 ? 1 : 2 : v2 < 1000 ? 3 : 4 : v2 < 1000000 ? v2 < 100000 ? 5 : 6 : v2 < 10000000 ? 7 : 8;
        } else if (v2 < 1000000000000L) {
            if (v2 < RealConnection.IDLE_CONNECTION_HEALTHY_NS) {
                width = v2 < 1000000000 ? 9 : 10;
            } else if (v2 < 100000000000L) {
                width = 11;
            } else {
                width = 12;
            }
        } else if (v2 < 1000000000000000L) {
            if (v2 < 10000000000000L) {
                width = 13;
            } else if (v2 < 100000000000000L) {
                width = 14;
            } else {
                width = 15;
            }
        } else if (v2 < 100000000000000000L) {
            if (v2 < 10000000000000000L) {
                width = 16;
            } else {
                width = 17;
            }
        } else if (v2 < 1000000000000000000L) {
            width = 18;
        } else {
            width = 19;
        }
        if (negative) {
            width++;
        }
        Segment tail = $this$commonWriteDecimalLong.writableSegment$okio(width);
        byte[] data = tail.data;
        int pos = tail.limit + width;
        while (v2 != 0) {
            long j = (long) 10;
            pos--;
            data[pos] = getHEX_DIGIT_BYTES()[(int) (v2 % j)];
            v2 /= j;
        }
        if (negative) {
            data[pos - 1] = (byte) 45;
        }
        tail.limit += width;
        $this$commonWriteDecimalLong.setSize$okio($this$commonWriteDecimalLong.size() + ((long) width));
        return $this$commonWriteDecimalLong;
    }

    public static final Buffer commonWriteHexadecimalUnsignedLong(Buffer $this$commonWriteHexadecimalUnsignedLong, long v) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteHexadecimalUnsignedLong, "$this$commonWriteHexadecimalUnsignedLong");
        long v2 = v;
        if (v2 == 0) {
            return $this$commonWriteHexadecimalUnsignedLong.writeByte(48);
        }
        long x = v2 | (v2 >>> 1);
        long x2 = x | (x >>> 2);
        long x3 = x2 | (x2 >>> 4);
        long x4 = x3 | (x3 >>> 8);
        long x5 = x4 | (x4 >>> 16);
        long x6 = x5 | (x5 >>> 32);
        long x7 = x6 - ((x6 >>> 1) & 6148914691236517205L);
        long x8 = ((x7 >>> 2) & 3689348814741910323L) + (3689348814741910323L & x7);
        long x9 = ((x8 >>> 4) + x8) & 1085102592571150095L;
        long x10 = x9 + (x9 >>> 8);
        long x11 = x10 + (x10 >>> 16);
        int width = (int) ((((long) 3) + ((x11 & 63) + (63 & (x11 >>> 32)))) / ((long) 4));
        Segment tail = $this$commonWriteHexadecimalUnsignedLong.writableSegment$okio(width);
        byte[] data = tail.data;
        int start = tail.limit;
        for (int pos = (tail.limit + width) - 1; pos >= start; pos--) {
            data[pos] = getHEX_DIGIT_BYTES()[(int) (15 & v2)];
            v2 >>>= 4;
        }
        tail.limit += width;
        $this$commonWriteHexadecimalUnsignedLong.setSize$okio($this$commonWriteHexadecimalUnsignedLong.size() + ((long) width));
        return $this$commonWriteHexadecimalUnsignedLong;
    }

    public static final Segment commonWritableSegment(Buffer $this$commonWritableSegment, int minimumCapacity) {
        Intrinsics.checkParameterIsNotNull($this$commonWritableSegment, "$this$commonWritableSegment");
        boolean z = true;
        if (minimumCapacity < 1 || minimumCapacity > 8192) {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException("unexpected capacity".toString());
        } else if ($this$commonWritableSegment.head == null) {
            Segment result = SegmentPool.INSTANCE.take();
            $this$commonWritableSegment.head = result;
            result.prev = result;
            result.next = result;
            return result;
        } else {
            Segment segment = $this$commonWritableSegment.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            Segment tail = segment.prev;
            if (tail == null) {
                Intrinsics.throwNpe();
            }
            if (tail.limit + minimumCapacity > 8192 || !tail.owner) {
                return tail.push(SegmentPool.INSTANCE.take());
            }
            return tail;
        }
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, byte[] source) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        return $this$commonWrite.write(source, 0, source.length);
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, byte[] source, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        int offset2 = offset;
        Util.checkOffsetAndCount((long) source.length, (long) offset2, (long) byteCount);
        int limit = offset2 + byteCount;
        while (offset2 < limit) {
            Segment tail = $this$commonWrite.writableSegment$okio(1);
            int toCopy = Math.min(limit - offset2, 8192 - tail.limit);
            ArraysKt.copyInto(source, tail.data, tail.limit, offset2, offset2 + toCopy);
            offset2 += toCopy;
            tail.limit += toCopy;
        }
        $this$commonWrite.setSize$okio($this$commonWrite.size() + ((long) byteCount));
        return $this$commonWrite;
    }

    public static final byte[] commonReadByteArray(Buffer $this$commonReadByteArray) {
        Intrinsics.checkParameterIsNotNull($this$commonReadByteArray, "$this$commonReadByteArray");
        return $this$commonReadByteArray.readByteArray($this$commonReadByteArray.size());
    }

    public static final byte[] commonReadByteArray(Buffer $this$commonReadByteArray, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonReadByteArray, "$this$commonReadByteArray");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadByteArray.size() >= byteCount) {
            byte[] result = new byte[(int) byteCount];
            $this$commonReadByteArray.readFully(result);
            return result;
        } else {
            throw new EOFException();
        }
    }

    public static final int commonRead(Buffer $this$commonRead, byte[] sink) {
        Intrinsics.checkParameterIsNotNull($this$commonRead, "$this$commonRead");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        return $this$commonRead.read(sink, 0, sink.length);
    }

    public static final void commonReadFully(Buffer $this$commonReadFully, byte[] sink) {
        Intrinsics.checkParameterIsNotNull($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        int offset = 0;
        while (offset < sink.length) {
            int read = $this$commonReadFully.read(sink, offset, sink.length - offset);
            if (read != -1) {
                offset += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public static final int commonRead(Buffer $this$commonRead, byte[] sink, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRead, "$this$commonRead");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        Segment s = $this$commonRead.head;
        if (s == null) {
            return -1;
        }
        int toCopy = Math.min(byteCount, s.limit - s.pos);
        ArraysKt.copyInto(s.data, sink, offset, s.pos, s.pos + toCopy);
        s.pos += toCopy;
        $this$commonRead.setSize$okio($this$commonRead.size() - ((long) toCopy));
        if (s.pos == s.limit) {
            $this$commonRead.head = s.pop();
            SegmentPool.INSTANCE.recycle(s);
        }
        return toCopy;
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ee, code lost:
        r1.setSize$okio(r18.size() - ((long) r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f7, code lost:
        if (r5 == false) goto L_0x00fb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00fc, code lost:
        return -r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:?, code lost:
        return r2;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00de  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final long commonReadDecimalLong(Buffer $this$commonReadDecimalLong) {
        int $i$f$commonReadDecimalLong;
        Buffer buffer;
        byte b;
        byte[] data;
        boolean done;
        Buffer buffer2 = $this$commonReadDecimalLong;
        int $i$f$commonReadDecimalLong2 = 0;
        Intrinsics.checkParameterIsNotNull(buffer2, "$this$commonReadDecimalLong");
        if ($this$commonReadDecimalLong.size() != 0) {
            long value = 0;
            int seen = 0;
            boolean negative = false;
            boolean done2 = false;
            long overflowDigit = -7;
            loop0: while (true) {
                Segment segment = buffer2.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                byte[] data2 = segment.data;
                int pos = segment.pos;
                int limit = segment.limit;
                while (pos < limit) {
                    b = data2[pos];
                    byte b2 = (byte) 48;
                    if (b < b2 || b > ((byte) 57)) {
                        $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                        done = done2;
                        data = data2;
                        if (b == ((byte) 45) && seen == 0) {
                            negative = true;
                            overflowDigit--;
                        } else if (seen != 0) {
                            done2 = true;
                            if (pos != limit) {
                                buffer = $this$commonReadDecimalLong;
                                buffer.head = segment.pop();
                                SegmentPool.INSTANCE.recycle(segment);
                            } else {
                                buffer = $this$commonReadDecimalLong;
                                segment.pos = pos;
                            }
                            if (done2 || buffer.head == null) {
                                break;
                            }
                            buffer2 = buffer;
                            $i$f$commonReadDecimalLong2 = $i$f$commonReadDecimalLong;
                        } else {
                            throw new NumberFormatException("Expected leading [0-9] or '-' character but was 0x" + Util.toHexString(b));
                        }
                    } else {
                        int digit = b2 - b;
                        if (value < OVERFLOW_ZONE) {
                            break loop0;
                        }
                        if (value == OVERFLOW_ZONE) {
                            $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                            if (((long) digit) < overflowDigit) {
                                break loop0;
                            }
                        } else {
                            $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                        }
                        value = (value * 10) + ((long) digit);
                        done = done2;
                        data = data2;
                    }
                    pos++;
                    seen++;
                    $i$f$commonReadDecimalLong2 = $i$f$commonReadDecimalLong;
                    done2 = done;
                    data2 = data;
                }
                $i$f$commonReadDecimalLong = $i$f$commonReadDecimalLong2;
                if (pos != limit) {
                }
                if (done2) {
                    break;
                }
                break;
            }
            Buffer buffer3 = new Buffer().writeDecimalLong(value).writeByte((int) b);
            if (!negative) {
                buffer3.readByte();
            }
            throw new NumberFormatException("Number too large: " + buffer3.readUtf8());
        }
        throw new EOFException();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00c5 A[EDGE_INSN: B:47:0x00c5->B:40:0x00c5 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final long commonReadHexadecimalUnsignedLong(Buffer $this$commonReadHexadecimalUnsignedLong) {
        int digit;
        Intrinsics.checkParameterIsNotNull($this$commonReadHexadecimalUnsignedLong, "$this$commonReadHexadecimalUnsignedLong");
        if ($this$commonReadHexadecimalUnsignedLong.size() != 0) {
            long value = 0;
            int seen = 0;
            boolean done = false;
            do {
                Segment segment = $this$commonReadHexadecimalUnsignedLong.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                byte[] data = segment.data;
                int pos = segment.pos;
                int limit = segment.limit;
                while (pos < limit) {
                    byte b = data[pos];
                    byte b2 = (byte) 48;
                    if (b < b2 || b > ((byte) 57)) {
                        byte b3 = (byte) 97;
                        if (b < b3 || b > ((byte) 102)) {
                            byte b4 = (byte) 65;
                            if (b >= b4 && b <= ((byte) 70)) {
                                digit = (b - b4) + 10;
                            } else if (seen != 0) {
                                done = true;
                                if (pos != limit) {
                                    $this$commonReadHexadecimalUnsignedLong.head = segment.pop();
                                    SegmentPool.INSTANCE.recycle(segment);
                                } else {
                                    segment.pos = pos;
                                }
                                if (!done) {
                                    break;
                                }
                            } else {
                                throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + Util.toHexString(b));
                            }
                        } else {
                            digit = (b - b3) + 10;
                        }
                    } else {
                        digit = b - b2;
                    }
                    if ((-1152921504606846976L & value) == 0) {
                        value = (value << 4) | ((long) digit);
                        pos++;
                        seen++;
                    } else {
                        Buffer buffer = new Buffer().writeHexadecimalUnsignedLong(value).writeByte((int) b);
                        throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                    }
                }
                if (pos != limit) {
                }
                if (!done) {
                }
            } while ($this$commonReadHexadecimalUnsignedLong.head != null);
            $this$commonReadHexadecimalUnsignedLong.setSize$okio($this$commonReadHexadecimalUnsignedLong.size() - ((long) seen));
            return value;
        }
        throw new EOFException();
    }

    public static final ByteString commonReadByteString(Buffer $this$commonReadByteString) {
        Intrinsics.checkParameterIsNotNull($this$commonReadByteString, "$this$commonReadByteString");
        return $this$commonReadByteString.readByteString($this$commonReadByteString.size());
    }

    public static final ByteString commonReadByteString(Buffer $this$commonReadByteString, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonReadByteString, "$this$commonReadByteString");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadByteString.size() < byteCount) {
            throw new EOFException();
        } else if (byteCount < ((long) 4096)) {
            return new ByteString($this$commonReadByteString.readByteArray(byteCount));
        } else {
            ByteString snapshot = $this$commonReadByteString.snapshot((int) byteCount);
            $this$commonReadByteString.skip(byteCount);
            return snapshot;
        }
    }

    public static final int commonSelect(Buffer $this$commonSelect, Options options) {
        Intrinsics.checkParameterIsNotNull($this$commonSelect, "$this$commonSelect");
        Intrinsics.checkParameterIsNotNull(options, "options");
        int index = selectPrefix$default($this$commonSelect, options, false, 2, null);
        if (index == -1) {
            return -1;
        }
        $this$commonSelect.skip((long) options.getByteStrings$okio()[index].size());
        return index;
    }

    public static final void commonReadFully(Buffer $this$commonReadFully, Buffer sink, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if ($this$commonReadFully.size() >= byteCount) {
            sink.write($this$commonReadFully, byteCount);
        } else {
            sink.write($this$commonReadFully, $this$commonReadFully.size());
            throw new EOFException();
        }
    }

    public static final long commonReadAll(Buffer $this$commonReadAll, Sink sink) {
        Intrinsics.checkParameterIsNotNull($this$commonReadAll, "$this$commonReadAll");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        long byteCount = $this$commonReadAll.size();
        if (byteCount > 0) {
            sink.write($this$commonReadAll, byteCount);
        }
        return byteCount;
    }

    public static final String commonReadUtf8(Buffer $this$commonReadUtf8, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonReadUtf8, "$this$commonReadUtf8");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadUtf8.size() < byteCount) {
            throw new EOFException();
        } else if (byteCount == 0) {
            return "";
        } else {
            Segment s = $this$commonReadUtf8.head;
            if (s == null) {
                Intrinsics.throwNpe();
            }
            if (((long) s.pos) + byteCount > ((long) s.limit)) {
                return _Utf8Kt.commonToUtf8String$default($this$commonReadUtf8.readByteArray(byteCount), 0, 0, 3, null);
            }
            String result = _Utf8Kt.commonToUtf8String(s.data, s.pos, s.pos + ((int) byteCount));
            s.pos += (int) byteCount;
            $this$commonReadUtf8.setSize$okio($this$commonReadUtf8.size() - byteCount);
            if (s.pos == s.limit) {
                $this$commonReadUtf8.head = s.pop();
                SegmentPool.INSTANCE.recycle(s);
            }
            return result;
        }
    }

    public static final String commonReadUtf8Line(Buffer $this$commonReadUtf8Line) {
        Intrinsics.checkParameterIsNotNull($this$commonReadUtf8Line, "$this$commonReadUtf8Line");
        long newline = $this$commonReadUtf8Line.indexOf((byte) 10);
        if (newline != -1) {
            return readUtf8Line($this$commonReadUtf8Line, newline);
        }
        if ($this$commonReadUtf8Line.size() != 0) {
            return $this$commonReadUtf8Line.readUtf8($this$commonReadUtf8Line.size());
        }
        return null;
    }

    public static final String commonReadUtf8LineStrict(Buffer $this$commonReadUtf8LineStrict, long limit) {
        Intrinsics.checkParameterIsNotNull($this$commonReadUtf8LineStrict, "$this$commonReadUtf8LineStrict");
        if (limit >= 0) {
            long scanLength = Long.MAX_VALUE;
            if (limit != Long.MAX_VALUE) {
                scanLength = limit + 1;
            }
            byte b = (byte) 10;
            long newline = $this$commonReadUtf8LineStrict.indexOf(b, 0, scanLength);
            if (newline != -1) {
                return readUtf8Line($this$commonReadUtf8LineStrict, newline);
            }
            if (scanLength < $this$commonReadUtf8LineStrict.size() && $this$commonReadUtf8LineStrict.getByte(scanLength - 1) == ((byte) 13) && $this$commonReadUtf8LineStrict.getByte(scanLength) == b) {
                return readUtf8Line($this$commonReadUtf8LineStrict, scanLength);
            }
            Buffer data = new Buffer();
            $this$commonReadUtf8LineStrict.copyTo(data, 0, Math.min((long) 32, $this$commonReadUtf8LineStrict.size()));
            throw new EOFException("\\n not found: limit=" + Math.min($this$commonReadUtf8LineStrict.size(), limit) + " content=" + data.readByteString().hex() + Typography.ellipsis);
        }
        throw new IllegalArgumentException(("limit < 0: " + limit).toString());
    }

    public static final int commonReadUtf8CodePoint(Buffer $this$commonReadUtf8CodePoint) {
        int min;
        int byteCount;
        int codePoint;
        Intrinsics.checkParameterIsNotNull($this$commonReadUtf8CodePoint, "$this$commonReadUtf8CodePoint");
        if ($this$commonReadUtf8CodePoint.size() != 0) {
            byte b0 = $this$commonReadUtf8CodePoint.getByte(0);
            if ((128 & b0) == 0) {
                codePoint = b0 & Byte.MAX_VALUE;
                byteCount = 1;
                min = 0;
            } else if ((224 & b0) == 192) {
                codePoint = b0 & Ascii.US;
                byteCount = 2;
                min = 128;
            } else if ((240 & b0) == 224) {
                codePoint = b0 & Ascii.SI;
                byteCount = 3;
                min = 2048;
            } else if ((248 & b0) == 240) {
                codePoint = b0 & 7;
                byteCount = 4;
                min = 65536;
            } else {
                $this$commonReadUtf8CodePoint.skip(1);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            if ($this$commonReadUtf8CodePoint.size() >= ((long) byteCount)) {
                for (int i = 1; i < byteCount; i++) {
                    byte b = $this$commonReadUtf8CodePoint.getByte((long) i);
                    if ((192 & b) == 128) {
                        codePoint = (codePoint << 6) | (63 & b);
                    } else {
                        $this$commonReadUtf8CodePoint.skip((long) i);
                        return Utf8.REPLACEMENT_CODE_POINT;
                    }
                }
                $this$commonReadUtf8CodePoint.skip((long) byteCount);
                if (codePoint > 1114111) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                if ((55296 <= codePoint && 57343 >= codePoint) || codePoint < min) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                return codePoint;
            }
            throw new EOFException("size < " + byteCount + ": " + $this$commonReadUtf8CodePoint.size() + " (to read code point prefixed 0x" + Util.toHexString(b0) + ')');
        }
        throw new EOFException();
    }

    public static final Buffer commonWriteUtf8(Buffer $this$commonWriteUtf8, String string, int beginIndex, int endIndex) {
        int runLimit;
        Intrinsics.checkParameterIsNotNull($this$commonWriteUtf8, "$this$commonWriteUtf8");
        Intrinsics.checkParameterIsNotNull(string, "string");
        int i = 1;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex <= string.length()) {
                    int runSize = beginIndex;
                    while (runSize < endIndex) {
                        int c = string.charAt(runSize);
                        if (c < 128) {
                            Segment tail = $this$commonWriteUtf8.writableSegment$okio(i);
                            byte[] data = tail.data;
                            int segmentOffset = tail.limit - runSize;
                            int runLimit2 = Math.min(endIndex, 8192 - segmentOffset);
                            int i2 = runSize + 1;
                            data[runSize + segmentOffset] = (byte) c;
                            while (i2 < runLimit2) {
                                int c2 = string.charAt(i2);
                                if (c2 >= 128) {
                                    break;
                                }
                                data[i2 + segmentOffset] = (byte) c2;
                                i2++;
                            }
                            int runSize2 = (i2 + segmentOffset) - tail.limit;
                            tail.limit += runSize2;
                            $this$commonWriteUtf8.setSize$okio(((long) runSize2) + $this$commonWriteUtf8.size());
                            runSize = i2;
                            runLimit = 1;
                        } else if (c < 2048) {
                            Segment tail2 = $this$commonWriteUtf8.writableSegment$okio(2);
                            tail2.data[tail2.limit] = (byte) ((c >> 6) | 192);
                            tail2.data[tail2.limit + 1] = (byte) (128 | (c & 63));
                            tail2.limit += 2;
                            $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 2);
                            runSize++;
                            runLimit = 1;
                        } else if (c < 55296 || c > 57343) {
                            Segment tail3 = $this$commonWriteUtf8.writableSegment$okio(3);
                            tail3.data[tail3.limit] = (byte) ((c >> 12) | 224);
                            runLimit = 1;
                            tail3.data[tail3.limit + 1] = (byte) ((63 & (c >> 6)) | 128);
                            tail3.data[tail3.limit + 2] = (byte) ((c & 63) | 128);
                            tail3.limit += 3;
                            $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 3);
                            runSize++;
                        } else {
                            int low = runSize + 1 < endIndex ? string.charAt(runSize + 1) : 0;
                            if (c > 56319 || 56320 > low || 57343 < low) {
                                $this$commonWriteUtf8.writeByte(63);
                                runSize++;
                                runLimit = 1;
                            } else {
                                int codePoint = (((c & 1023) << 10) | (low & 1023)) + 65536;
                                Segment tail4 = $this$commonWriteUtf8.writableSegment$okio(4);
                                tail4.data[tail4.limit] = (byte) ((codePoint >> 18) | 240);
                                tail4.data[tail4.limit + 1] = (byte) (((codePoint >> 12) & 63) | 128);
                                tail4.data[tail4.limit + 2] = (byte) (((codePoint >> 6) & 63) | 128);
                                tail4.data[tail4.limit + 3] = (byte) (128 | (codePoint & 63));
                                tail4.limit += 4;
                                $this$commonWriteUtf8.setSize$okio($this$commonWriteUtf8.size() + 4);
                                runSize += 2;
                                runLimit = 1;
                            }
                        }
                        i = runLimit;
                    }
                    return $this$commonWriteUtf8;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + string.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
    }

    public static final Buffer commonWriteUtf8CodePoint(Buffer $this$commonWriteUtf8CodePoint, int codePoint) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteUtf8CodePoint, "$this$commonWriteUtf8CodePoint");
        if (codePoint < 128) {
            $this$commonWriteUtf8CodePoint.writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment tail = $this$commonWriteUtf8CodePoint.writableSegment$okio(2);
            tail.data[tail.limit] = (byte) ((codePoint >> 6) | 192);
            tail.data[tail.limit + 1] = (byte) (128 | (codePoint & 63));
            tail.limit += 2;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 2);
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            $this$commonWriteUtf8CodePoint.writeByte(63);
        } else if (codePoint < 65536) {
            Segment tail2 = $this$commonWriteUtf8CodePoint.writableSegment$okio(3);
            tail2.data[tail2.limit] = (byte) ((codePoint >> 12) | 224);
            tail2.data[tail2.limit + 1] = (byte) ((63 & (codePoint >> 6)) | 128);
            tail2.data[tail2.limit + 2] = (byte) (128 | (codePoint & 63));
            tail2.limit += 3;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 3);
        } else if (codePoint <= 1114111) {
            Segment tail3 = $this$commonWriteUtf8CodePoint.writableSegment$okio(4);
            tail3.data[tail3.limit] = (byte) ((codePoint >> 18) | 240);
            tail3.data[tail3.limit + 1] = (byte) (((codePoint >> 12) & 63) | 128);
            tail3.data[tail3.limit + 2] = (byte) (((codePoint >> 6) & 63) | 128);
            tail3.data[tail3.limit + 3] = (byte) (128 | (codePoint & 63));
            tail3.limit += 4;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 4);
        } else {
            throw new IllegalArgumentException("Unexpected code point: 0x" + Util.toHexString(codePoint));
        }
        return $this$commonWriteUtf8CodePoint;
    }

    public static final long commonWriteAll(Buffer $this$commonWriteAll, Source source) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteAll, "$this$commonWriteAll");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long totalBytesRead = 0;
        while (true) {
            long readCount = source.read($this$commonWriteAll, (long) 8192);
            if (readCount == -1) {
                return totalBytesRead;
            }
            totalBytesRead += readCount;
        }
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, Source source, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long byteCount2 = byteCount;
        while (byteCount2 > 0) {
            long read = source.read($this$commonWrite, byteCount2);
            if (read != -1) {
                byteCount2 -= read;
            } else {
                throw new EOFException();
            }
        }
        return $this$commonWrite;
    }

    public static final Buffer commonWriteByte(Buffer $this$commonWriteByte, int b) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteByte, "$this$commonWriteByte");
        Segment tail = $this$commonWriteByte.writableSegment$okio(1);
        byte[] bArr = tail.data;
        int i = tail.limit;
        tail.limit = i + 1;
        bArr[i] = (byte) b;
        $this$commonWriteByte.setSize$okio($this$commonWriteByte.size() + 1);
        return $this$commonWriteByte;
    }

    public static final Buffer commonWriteShort(Buffer $this$commonWriteShort, int s) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteShort, "$this$commonWriteShort");
        Segment tail = $this$commonWriteShort.writableSegment$okio(2);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((s >>> 8) & 255);
        data[limit2] = (byte) (s & 255);
        tail.limit = limit2 + 1;
        $this$commonWriteShort.setSize$okio($this$commonWriteShort.size() + 2);
        return $this$commonWriteShort;
    }

    public static final Buffer commonWriteInt(Buffer $this$commonWriteInt, int i) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteInt, "$this$commonWriteInt");
        Segment tail = $this$commonWriteInt.writableSegment$okio(4);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((i >>> 24) & 255);
        int limit3 = limit2 + 1;
        data[limit2] = (byte) ((i >>> 16) & 255);
        int limit4 = limit3 + 1;
        data[limit3] = (byte) ((i >>> 8) & 255);
        data[limit4] = (byte) (i & 255);
        tail.limit = limit4 + 1;
        $this$commonWriteInt.setSize$okio($this$commonWriteInt.size() + 4);
        return $this$commonWriteInt;
    }

    public static final Buffer commonWriteLong(Buffer $this$commonWriteLong, long v) {
        Intrinsics.checkParameterIsNotNull($this$commonWriteLong, "$this$commonWriteLong");
        Segment tail = $this$commonWriteLong.writableSegment$okio(8);
        byte[] data = tail.data;
        int limit = tail.limit;
        int limit2 = limit + 1;
        data[limit] = (byte) ((int) ((v >>> 56) & 255));
        int limit3 = limit2 + 1;
        data[limit2] = (byte) ((int) ((v >>> 48) & 255));
        int limit4 = limit3 + 1;
        data[limit3] = (byte) ((int) ((v >>> 40) & 255));
        int limit5 = limit4 + 1;
        data[limit4] = (byte) ((int) ((v >>> 32) & 255));
        int limit6 = limit5 + 1;
        data[limit5] = (byte) ((int) ((v >>> 24) & 255));
        int limit7 = limit6 + 1;
        data[limit6] = (byte) ((int) ((v >>> 16) & 255));
        int limit8 = limit7 + 1;
        data[limit7] = (byte) ((int) ((v >>> 8) & 255));
        data[limit8] = (byte) ((int) (v & 255));
        tail.limit = limit8 + 1;
        $this$commonWriteLong.setSize$okio($this$commonWriteLong.size() + 8);
        return $this$commonWriteLong;
    }

    public static final void commonWrite(Buffer $this$commonWrite, Buffer source, long byteCount) {
        Segment tail;
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
        long byteCount2 = byteCount;
        if (source != $this$commonWrite) {
            Util.checkOffsetAndCount(source.size(), 0, byteCount2);
            while (byteCount2 > 0) {
                Segment segment = source.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                int i = segment.limit;
                Segment segment2 = source.head;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                if (byteCount2 < ((long) (i - segment2.pos))) {
                    if ($this$commonWrite.head != null) {
                        Segment segment3 = $this$commonWrite.head;
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        tail = segment3.prev;
                    } else {
                        tail = null;
                    }
                    if (tail != null && tail.owner) {
                        if ((((long) tail.limit) + byteCount2) - ((long) (tail.shared ? 0 : tail.pos)) <= ((long) 8192)) {
                            Segment segment4 = source.head;
                            if (segment4 == null) {
                                Intrinsics.throwNpe();
                            }
                            segment4.writeTo(tail, (int) byteCount2);
                            source.setSize$okio(source.size() - byteCount2);
                            $this$commonWrite.setSize$okio($this$commonWrite.size() + byteCount2);
                            return;
                        }
                    }
                    Segment segment5 = source.head;
                    if (segment5 == null) {
                        Intrinsics.throwNpe();
                    }
                    source.head = segment5.split((int) byteCount2);
                }
                Segment segmentToMove = source.head;
                if (segmentToMove == null) {
                    Intrinsics.throwNpe();
                }
                long movedByteCount = (long) (segmentToMove.limit - segmentToMove.pos);
                source.head = segmentToMove.pop();
                if ($this$commonWrite.head == null) {
                    $this$commonWrite.head = segmentToMove;
                    segmentToMove.prev = segmentToMove;
                    segmentToMove.next = segmentToMove.prev;
                } else {
                    Segment segment6 = $this$commonWrite.head;
                    if (segment6 == null) {
                        Intrinsics.throwNpe();
                    }
                    Segment tail2 = segment6.prev;
                    if (tail2 == null) {
                        Intrinsics.throwNpe();
                    }
                    tail2.push(segmentToMove).compact();
                }
                source.setSize$okio(source.size() - movedByteCount);
                $this$commonWrite.setSize$okio($this$commonWrite.size() + movedByteCount);
                byteCount2 -= movedByteCount;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    public static final long commonRead(Buffer $this$commonRead, Buffer sink, long byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRead, "$this$commonRead");
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        long byteCount2 = byteCount;
        if (!(byteCount2 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount2).toString());
        } else if ($this$commonRead.size() == 0) {
            return -1;
        } else {
            if (byteCount2 > $this$commonRead.size()) {
                byteCount2 = $this$commonRead.size();
            }
            sink.write($this$commonRead, byteCount2);
            return byteCount2;
        }
    }

    /* JADX INFO: Multiple debug info for r1v11 byte[]: [D('data' byte[]), D('offset$iv' long)] */
    public static final long commonIndexOf(Buffer $this$commonIndexOf, byte b, long fromIndex, long toIndex) {
        Buffer $this$seek$iv;
        Segment s$iv;
        int $i$f$commonIndexOf = 0;
        Intrinsics.checkParameterIsNotNull($this$commonIndexOf, "$this$commonIndexOf");
        long fromIndex2 = fromIndex;
        long toIndex2 = toIndex;
        if (0 <= fromIndex2 && toIndex2 >= fromIndex2) {
            if (toIndex2 > $this$commonIndexOf.size()) {
                toIndex2 = $this$commonIndexOf.size();
            }
            if (!(fromIndex2 == toIndex2 || (s$iv = ($this$seek$iv = $this$commonIndexOf).head) == null)) {
                if ($this$seek$iv.size() - fromIndex2 < fromIndex2) {
                    long offset$iv = $this$seek$iv.size();
                    while (offset$iv > fromIndex2) {
                        Segment segment = s$iv.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv = segment;
                        offset$iv -= (long) (s$iv.limit - s$iv.pos);
                    }
                    Segment s = s$iv;
                    int i = 0;
                    if (s == null) {
                        return -1;
                    }
                    long offset = offset$iv;
                    long fromIndex3 = fromIndex2;
                    Segment s2 = s;
                    while (offset < toIndex2) {
                        byte[] data = s2.data;
                        int limit = (int) Math.min((long) s2.limit, (((long) s2.pos) + toIndex2) - offset);
                        for (int pos = (int) ((((long) s2.pos) + fromIndex3) - offset); pos < limit; pos++) {
                            if (data[pos] == b) {
                                return ((long) (pos - s2.pos)) + offset;
                            }
                        }
                        offset += (long) (s2.limit - s2.pos);
                        fromIndex3 = offset;
                        Segment segment2 = s2.next;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        s2 = segment2;
                        $i$f$commonIndexOf = $i$f$commonIndexOf;
                        $this$seek$iv = $this$seek$iv;
                        s = s;
                        i = i;
                    }
                    return -1;
                }
                long offset$iv2 = 0;
                while (true) {
                    long nextOffset$iv = ((long) (s$iv.limit - s$iv.pos)) + offset$iv2;
                    if (nextOffset$iv > fromIndex2) {
                        break;
                    }
                    Segment segment3 = s$iv.next;
                    if (segment3 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv = segment3;
                    offset$iv2 = nextOffset$iv;
                }
                Segment s3 = s$iv;
                long offset2 = offset$iv2;
                if (s3 == null) {
                    return -1;
                }
                Segment s4 = s3;
                long offset3 = offset2;
                while (offset3 < toIndex2) {
                    byte[] data2 = s4.data;
                    int limit2 = (int) Math.min((long) s4.limit, (((long) s4.pos) + toIndex2) - offset3);
                    for (int pos2 = (int) ((((long) s4.pos) + fromIndex2) - offset3); pos2 < limit2; pos2++) {
                        if (data2[pos2] == b) {
                            return ((long) (pos2 - s4.pos)) + offset3;
                        }
                    }
                    offset3 += (long) (s4.limit - s4.pos);
                    fromIndex2 = offset3;
                    Segment segment4 = s4.next;
                    if (segment4 == null) {
                        Intrinsics.throwNpe();
                    }
                    s4 = segment4;
                    offset$iv2 = offset$iv2;
                    s3 = s3;
                    offset2 = offset2;
                }
                return -1;
            }
            return -1;
        }
        throw new IllegalArgumentException(("size=" + $this$commonIndexOf.size() + " fromIndex=" + fromIndex2 + " toIndex=" + toIndex2).toString());
    }

    /* JADX INFO: Multiple debug info for r2v10 int: [D('segmentLimit' int), D('a$iv' int)] */
    /* JADX INFO: Multiple debug info for r2v8 int: [D('segmentLimit' int), D('a$iv' int)] */
    public static final long commonIndexOf(Buffer $this$commonIndexOf, ByteString bytes, long fromIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        long fromIndex2 = fromIndex;
        if (bytes.size() > 0) {
            if (fromIndex2 >= 0) {
                long fromIndex$iv = fromIndex2;
                int $i$f$seek = 0;
                Segment s$iv = $this$commonIndexOf.head;
                if (s$iv == null) {
                    return -1;
                }
                if ($this$commonIndexOf.size() - fromIndex$iv < fromIndex$iv) {
                    long offset$iv = $this$commonIndexOf.size();
                    while (offset$iv > fromIndex$iv) {
                        Segment segment = s$iv.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        s$iv = segment;
                        offset$iv -= (long) (s$iv.limit - s$iv.pos);
                    }
                    Segment s = s$iv;
                    long offset = offset$iv;
                    if (s == null) {
                        return -1;
                    }
                    long offset2 = offset;
                    byte[] targetByteArray = bytes.internalArray$okio();
                    byte b0 = targetByteArray[0];
                    int bytesSize = bytes.size();
                    long resultLimit = ($this$commonIndexOf.size() - ((long) bytesSize)) + 1;
                    Segment s2 = s;
                    while (offset2 < resultLimit) {
                        byte[] data = s2.data;
                        int a$iv = (int) Math.min((long) s2.limit, (((long) s2.pos) + resultLimit) - offset2);
                        for (int pos = (int) ((((long) s2.pos) + fromIndex2) - offset2); pos < a$iv; pos++) {
                            if (data[pos] == b0 && rangeEquals(s2, pos + 1, targetByteArray, 1, bytesSize)) {
                                return ((long) (pos - s2.pos)) + offset2;
                            }
                        }
                        offset2 += (long) (s2.limit - s2.pos);
                        fromIndex2 = offset2;
                        Segment segment2 = s2.next;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        s2 = segment2;
                        $i$f$seek = $i$f$seek;
                        s$iv = s$iv;
                        s = s;
                        offset = offset;
                    }
                    return -1;
                }
                long offset$iv2 = 0;
                while (true) {
                    long nextOffset$iv = ((long) (s$iv.limit - s$iv.pos)) + offset$iv2;
                    if (nextOffset$iv > fromIndex$iv) {
                        break;
                    }
                    Segment segment3 = s$iv.next;
                    if (segment3 == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv = segment3;
                    offset$iv2 = nextOffset$iv;
                    fromIndex$iv = fromIndex$iv;
                }
                long offset3 = offset$iv2;
                int i = 0;
                if (s$iv == null) {
                    return -1;
                }
                Segment s3 = s$iv;
                long offset4 = offset3;
                byte[] targetByteArray2 = bytes.internalArray$okio();
                byte b02 = targetByteArray2[0];
                int bytesSize2 = bytes.size();
                long resultLimit2 = ($this$commonIndexOf.size() - ((long) bytesSize2)) + 1;
                while (offset4 < resultLimit2) {
                    byte[] data2 = s3.data;
                    int a$iv2 = (int) Math.min((long) s3.limit, (((long) s3.pos) + resultLimit2) - offset4);
                    for (int pos2 = (int) ((((long) s3.pos) + fromIndex2) - offset4); pos2 < a$iv2; pos2++) {
                        if (data2[pos2] == b02 && rangeEquals(s3, pos2 + 1, targetByteArray2, 1, bytesSize2)) {
                            return ((long) (pos2 - s3.pos)) + offset4;
                        }
                    }
                    offset4 += (long) (s3.limit - s3.pos);
                    fromIndex2 = offset4;
                    Segment segment4 = s3.next;
                    if (segment4 == null) {
                        Intrinsics.throwNpe();
                    }
                    s3 = segment4;
                    i = i;
                    fromIndex$iv = fromIndex$iv;
                    offset3 = offset3;
                }
                return -1;
            }
            throw new IllegalArgumentException(("fromIndex < 0: " + fromIndex2).toString());
        }
        throw new IllegalArgumentException("bytes is empty".toString());
    }

    /* JADX INFO: Multiple debug info for r2v12 byte: [D('limit' int), D('b' int)] */
    public static final long commonIndexOfElement(Buffer $this$commonIndexOfElement, ByteString targetBytes, long fromIndex) {
        ByteString byteString = targetBytes;
        int $i$f$commonIndexOfElement = 0;
        Intrinsics.checkParameterIsNotNull($this$commonIndexOfElement, "$this$commonIndexOfElement");
        Intrinsics.checkParameterIsNotNull(byteString, "targetBytes");
        long fromIndex2 = fromIndex;
        if (fromIndex2 >= 0) {
            Buffer $this$seek$iv = $this$commonIndexOfElement;
            Segment s$iv = $this$seek$iv.head;
            if (s$iv == null) {
                return -1;
            }
            if ($this$seek$iv.size() - fromIndex2 < fromIndex2) {
                long offset$iv = $this$seek$iv.size();
                while (offset$iv > fromIndex2) {
                    Segment segment = s$iv.prev;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    s$iv = segment;
                    offset$iv -= (long) (s$iv.limit - s$iv.pos);
                }
                if (s$iv == null) {
                    return -1;
                }
                long offset = offset$iv;
                if (targetBytes.size() == 2) {
                    byte b0 = byteString.getByte(0);
                    byte b1 = byteString.getByte(1);
                    long fromIndex3 = fromIndex2;
                    Segment s = s$iv;
                    while (offset < $this$commonIndexOfElement.size()) {
                        byte[] data = s.data;
                        int limit = s.limit;
                        for (int pos = (int) ((((long) s.pos) + fromIndex3) - offset); pos < limit; pos++) {
                            byte b = data[pos];
                            if (b == b0 || b == b1) {
                                return ((long) (pos - s.pos)) + offset;
                            }
                        }
                        offset += (long) (s.limit - s.pos);
                        fromIndex3 = offset;
                        Segment segment2 = s.next;
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        s = segment2;
                        $i$f$commonIndexOfElement = $i$f$commonIndexOfElement;
                        $this$seek$iv = $this$seek$iv;
                    }
                    return -1;
                }
                byte[] targetByteArray = targetBytes.internalArray$okio();
                long fromIndex4 = fromIndex2;
                Segment s2 = s$iv;
                while (offset < $this$commonIndexOfElement.size()) {
                    byte[] data2 = s2.data;
                    int pos2 = (int) ((((long) s2.pos) + fromIndex4) - offset);
                    int limit2 = s2.limit;
                    while (pos2 < limit2) {
                        byte b2 = data2[pos2];
                        for (byte t : targetByteArray) {
                            if (b2 == t) {
                                return ((long) (pos2 - s2.pos)) + offset;
                            }
                        }
                        pos2++;
                        data2 = data2;
                    }
                    offset += (long) (s2.limit - s2.pos);
                    fromIndex4 = offset;
                    Segment segment3 = s2.next;
                    if (segment3 == null) {
                        Intrinsics.throwNpe();
                    }
                    s2 = segment3;
                    targetByteArray = targetByteArray;
                }
                return -1;
            }
            long offset$iv2 = 0;
            while (true) {
                long nextOffset$iv = ((long) (s$iv.limit - s$iv.pos)) + offset$iv2;
                if (nextOffset$iv > fromIndex2) {
                    break;
                }
                Segment segment4 = s$iv.next;
                if (segment4 == null) {
                    Intrinsics.throwNpe();
                }
                s$iv = segment4;
                offset$iv2 = nextOffset$iv;
                byteString = targetBytes;
            }
            Segment s3 = s$iv;
            if (s3 == null) {
                return -1;
            }
            Segment s4 = s3;
            long offset2 = offset$iv2;
            if (targetBytes.size() == 2) {
                byte b02 = byteString.getByte(0);
                byte b12 = byteString.getByte(1);
                while (offset2 < $this$commonIndexOfElement.size()) {
                    byte[] data3 = s4.data;
                    int pos3 = (int) ((((long) s4.pos) + fromIndex2) - offset2);
                    for (int limit3 = s4.limit; pos3 < limit3; limit3 = limit3) {
                        byte b3 = data3[pos3];
                        if (b3 == b02 || b3 == b12) {
                            return ((long) (pos3 - s4.pos)) + offset2;
                        }
                        pos3++;
                    }
                    offset2 += (long) (s4.limit - s4.pos);
                    fromIndex2 = offset2;
                    Segment segment5 = s4.next;
                    if (segment5 == null) {
                        Intrinsics.throwNpe();
                    }
                    s4 = segment5;
                    offset$iv2 = offset$iv2;
                }
                return -1;
            }
            byte[] targetByteArray2 = targetBytes.internalArray$okio();
            while (offset2 < $this$commonIndexOfElement.size()) {
                byte[] data4 = s4.data;
                int pos4 = (int) ((((long) s4.pos) + fromIndex2) - offset2);
                int limit4 = s4.limit;
                while (pos4 < limit4) {
                    byte b4 = data4[pos4];
                    int i = 0;
                    for (int length = targetByteArray2.length; i < length; length = length) {
                        if (b4 == targetByteArray2[i]) {
                            return ((long) (pos4 - s4.pos)) + offset2;
                        }
                        i++;
                    }
                    pos4++;
                    data4 = data4;
                    s3 = s3;
                }
                offset2 += (long) (s4.limit - s4.pos);
                fromIndex2 = offset2;
                Segment segment6 = s4.next;
                if (segment6 == null) {
                    Intrinsics.throwNpe();
                }
                s4 = segment6;
                s3 = s3;
                targetByteArray2 = targetByteArray2;
            }
            return -1;
        }
        throw new IllegalArgumentException(("fromIndex < 0: " + fromIndex2).toString());
    }

    public static final boolean commonRangeEquals(Buffer $this$commonRangeEquals, long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || $this$commonRangeEquals.size() - offset < ((long) byteCount) || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i = 0; i < byteCount; i++) {
            if ($this$commonRangeEquals.getByte(((long) i) + offset) != bytes.getByte(bytesOffset + i)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Multiple debug info for r7v3 long: [D('i' long), D('posA' int)] */
    public static final boolean commonEquals(Buffer $this$commonEquals, Object other) {
        Intrinsics.checkParameterIsNotNull($this$commonEquals, "$this$commonEquals");
        if ($this$commonEquals == other) {
            return true;
        }
        if (!(other instanceof Buffer) || $this$commonEquals.size() != ((Buffer) other).size()) {
            return false;
        }
        if ($this$commonEquals.size() == 0) {
            return true;
        }
        Segment sa = $this$commonEquals.head;
        if (sa == null) {
            Intrinsics.throwNpe();
        }
        Segment sb = ((Buffer) other).head;
        if (sb == null) {
            Intrinsics.throwNpe();
        }
        int posA = sa.pos;
        int posB = sb.pos;
        long pos = 0;
        while (pos < $this$commonEquals.size()) {
            long count = (long) Math.min(sa.limit - posA, sb.limit - posB);
            int posA2 = posA;
            long i = 0;
            while (i < count) {
                int posA3 = posA2 + 1;
                int posB2 = posB + 1;
                if (sa.data[posA2] != sb.data[posB]) {
                    return false;
                }
                i = 1 + i;
                posA2 = posA3;
                posB = posB2;
            }
            if (posA2 == sa.limit) {
                Segment segment = sa.next;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                sa = segment;
                posA = sa.pos;
            } else {
                posA = posA2;
            }
            if (posB == sb.limit) {
                Segment segment2 = sb.next;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                sb = segment2;
                posB = sb.pos;
            }
            pos += count;
        }
        return true;
    }

    public static final int commonHashCode(Buffer $this$commonHashCode) {
        Intrinsics.checkParameterIsNotNull($this$commonHashCode, "$this$commonHashCode");
        Segment s = $this$commonHashCode.head;
        if (s == null) {
            return 0;
        }
        int result = 1;
        do {
            int limit = s.limit;
            for (int pos = s.pos; pos < limit; pos++) {
                result = (result * 31) + s.data[pos];
            }
            Segment segment = s.next;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            s = segment;
        } while (s != $this$commonHashCode.head);
        return result;
    }

    public static final Buffer commonCopy(Buffer $this$commonCopy) {
        Intrinsics.checkParameterIsNotNull($this$commonCopy, "$this$commonCopy");
        Buffer result = new Buffer();
        if ($this$commonCopy.size() == 0) {
            return result;
        }
        Segment head = $this$commonCopy.head;
        if (head == null) {
            Intrinsics.throwNpe();
        }
        Segment headCopy = head.sharedCopy();
        result.head = headCopy;
        headCopy.prev = result.head;
        headCopy.next = headCopy.prev;
        for (Segment s = head.next; s != head; s = s.next) {
            Segment segment = headCopy.prev;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            if (s == null) {
                Intrinsics.throwNpe();
            }
            segment.push(s.sharedCopy());
        }
        result.setSize$okio($this$commonCopy.size());
        return result;
    }

    public static final ByteString commonSnapshot(Buffer $this$commonSnapshot) {
        Intrinsics.checkParameterIsNotNull($this$commonSnapshot, "$this$commonSnapshot");
        if ($this$commonSnapshot.size() <= ((long) Integer.MAX_VALUE)) {
            return $this$commonSnapshot.snapshot((int) $this$commonSnapshot.size());
        }
        throw new IllegalStateException(("size > Int.MAX_VALUE: " + $this$commonSnapshot.size()).toString());
    }

    public static final ByteString commonSnapshot(Buffer $this$commonSnapshot, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonSnapshot, "$this$commonSnapshot");
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount($this$commonSnapshot.size(), 0, (long) byteCount);
        int offset = 0;
        int segmentCount = 0;
        Segment s = $this$commonSnapshot.head;
        while (offset < byteCount) {
            if (s == null) {
                Intrinsics.throwNpe();
            }
            if (s.limit != s.pos) {
                offset += s.limit - s.pos;
                segmentCount++;
                s = s.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] segments = new byte[segmentCount];
        int[] directory = new int[segmentCount * 2];
        int offset2 = 0;
        int segmentCount2 = 0;
        Segment s2 = $this$commonSnapshot.head;
        while (offset2 < byteCount) {
            if (s2 == null) {
                Intrinsics.throwNpe();
            }
            segments[segmentCount2] = s2.data;
            offset2 += s2.limit - s2.pos;
            directory[segmentCount2] = Math.min(offset2, byteCount);
            directory[segments.length + segmentCount2] = s2.pos;
            s2.shared = true;
            segmentCount2++;
            s2 = s2.next;
        }
        return new SegmentedByteString(segments, directory);
    }
}
