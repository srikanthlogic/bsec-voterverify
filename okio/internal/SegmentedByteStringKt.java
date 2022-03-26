package okio.internal;

import com.facebook.common.util.UriUtil;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.SegmentedByteString;
import okio.Util;
/* compiled from: SegmentedByteString.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a$\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0000\u001a\u0017\u0010\u0006\u001a\u00020\u0007*\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0080\b\u001a\r\u0010\u000b\u001a\u00020\u0001*\u00020\bH\u0080\b\u001a\r\u0010\f\u001a\u00020\u0001*\u00020\bH\u0080\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0001H\u0080\b\u001a-\u0010\u0010\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\u0080\b\u001a-\u0010\u0010\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\u0080\b\u001a\u001d\u0010\u0016\u001a\u00020\u0015*\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u0001H\u0080\b\u001a\r\u0010\u0019\u001a\u00020\u0012*\u00020\bH\u0080\b\u001a%\u0010\u001a\u001a\u00020\u001b*\u00020\b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\u0080\b\u001aZ\u0010\u001e\u001a\u00020\u001b*\u00020\b2K\u0010\u001f\u001aG\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u001b0 H\u0080\b\u001aj\u0010\u001e\u001a\u00020\u001b*\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00012K\u0010\u001f\u001aG\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u001b0 H\u0082\b\u001a\u0014\u0010$\u001a\u00020\u0001*\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0001H\u0000¨\u0006%"}, d2 = {"binarySearch", "", "", "value", "fromIndex", "toIndex", "commonEquals", "", "Lokio/SegmentedByteString;", "other", "", "commonGetSize", "commonHashCode", "commonInternalGet", "", "pos", "commonRangeEquals", "offset", "", "otherOffset", "byteCount", "Lokio/ByteString;", "commonSubstring", "beginIndex", "endIndex", "commonToByteArray", "commonWrite", "", "buffer", "Lokio/Buffer;", "forEachSegment", "action", "Lkotlin/Function3;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, UriUtil.DATA_SCHEME, "segment", "okio"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class SegmentedByteStringKt {
    public static final int binarySearch(int[] $this$binarySearch, int value, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        int left = fromIndex;
        int right = toIndex - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int midVal = $this$binarySearch[mid];
            if (midVal < value) {
                left = mid + 1;
            } else if (midVal <= value) {
                return mid;
            } else {
                right = mid - 1;
            }
        }
        return (-left) - 1;
    }

    public static final int segment(SegmentedByteString $this$segment, int pos) {
        Intrinsics.checkParameterIsNotNull($this$segment, "$this$segment");
        int i = binarySearch($this$segment.getDirectory$okio(), pos + 1, 0, $this$segment.getSegments$okio().length);
        return i >= 0 ? i : ~i;
    }

    public static final void forEachSegment(SegmentedByteString $this$forEachSegment, Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        Intrinsics.checkParameterIsNotNull($this$forEachSegment, "$this$forEachSegment");
        Intrinsics.checkParameterIsNotNull(function3, "action");
        int segmentCount = $this$forEachSegment.getSegments$okio().length;
        int pos = 0;
        for (int s = 0; s < segmentCount; s++) {
            int segmentPos = $this$forEachSegment.getDirectory$okio()[segmentCount + s];
            int nextSegmentOffset = $this$forEachSegment.getDirectory$okio()[s];
            function3.invoke($this$forEachSegment.getSegments$okio()[s], Integer.valueOf(segmentPos), Integer.valueOf(nextSegmentOffset - pos));
            pos = nextSegmentOffset;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void forEachSegment(SegmentedByteString $this$forEachSegment, int beginIndex, int endIndex, Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        int s = segment($this$forEachSegment, beginIndex);
        int pos = beginIndex;
        while (pos < endIndex) {
            int segmentOffset = s == 0 ? 0 : $this$forEachSegment.getDirectory$okio()[s - 1];
            int segmentPos = $this$forEachSegment.getDirectory$okio()[$this$forEachSegment.getSegments$okio().length + s];
            int byteCount = Math.min(endIndex, segmentOffset + ($this$forEachSegment.getDirectory$okio()[s] - segmentOffset)) - pos;
            function3.invoke($this$forEachSegment.getSegments$okio()[s], Integer.valueOf((pos - segmentOffset) + segmentPos), Integer.valueOf(byteCount));
            pos += byteCount;
            s++;
        }
    }

    public static final ByteString commonSubstring(SegmentedByteString $this$commonSubstring, int beginIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$commonSubstring, "$this$commonSubstring");
        int segmentOffset = 0;
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= $this$commonSubstring.size()) {
                int subLen = endIndex - beginIndex;
                if (subLen < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex=" + endIndex + " < beginIndex=" + beginIndex).toString());
                } else if (beginIndex == 0 && endIndex == $this$commonSubstring.size()) {
                    return $this$commonSubstring;
                } else {
                    if (beginIndex == endIndex) {
                        return ByteString.EMPTY;
                    }
                    int beginSegment = segment($this$commonSubstring, beginIndex);
                    int endSegment = segment($this$commonSubstring, endIndex - 1);
                    byte[][] newSegments = (byte[][]) ArraysKt.copyOfRange($this$commonSubstring.getSegments$okio(), beginSegment, endSegment + 1);
                    int[] newDirectory = new int[newSegments.length * 2];
                    int index = 0;
                    if (beginSegment <= endSegment) {
                        int s = beginSegment;
                        while (true) {
                            newDirectory[index] = Math.min($this$commonSubstring.getDirectory$okio()[s] - beginIndex, subLen);
                            int index2 = index + 1;
                            newDirectory[index + newSegments.length] = $this$commonSubstring.getDirectory$okio()[$this$commonSubstring.getSegments$okio().length + s];
                            if (s == endSegment) {
                                break;
                            }
                            s++;
                            index = index2;
                        }
                    }
                    if (beginSegment != 0) {
                        segmentOffset = $this$commonSubstring.getDirectory$okio()[beginSegment - 1];
                    }
                    int length = newSegments.length;
                    newDirectory[length] = newDirectory[length] + (beginIndex - segmentOffset);
                    return new SegmentedByteString(newSegments, newDirectory);
                }
            } else {
                throw new IllegalArgumentException(("endIndex=" + endIndex + " > length(" + $this$commonSubstring.size() + ')').toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex=" + beginIndex + " < 0").toString());
        }
    }

    public static final byte commonInternalGet(SegmentedByteString $this$commonInternalGet, int pos) {
        Intrinsics.checkParameterIsNotNull($this$commonInternalGet, "$this$commonInternalGet");
        Util.checkOffsetAndCount((long) $this$commonInternalGet.getDirectory$okio()[$this$commonInternalGet.getSegments$okio().length - 1], (long) pos, 1);
        int segment = segment($this$commonInternalGet, pos);
        return $this$commonInternalGet.getSegments$okio()[segment][(pos - (segment == 0 ? 0 : $this$commonInternalGet.getDirectory$okio()[segment - 1])) + $this$commonInternalGet.getDirectory$okio()[$this$commonInternalGet.getSegments$okio().length + segment]];
    }

    public static final int commonGetSize(SegmentedByteString $this$commonGetSize) {
        Intrinsics.checkParameterIsNotNull($this$commonGetSize, "$this$commonGetSize");
        return $this$commonGetSize.getDirectory$okio()[$this$commonGetSize.getSegments$okio().length - 1];
    }

    public static final byte[] commonToByteArray(SegmentedByteString $this$commonToByteArray) {
        Intrinsics.checkParameterIsNotNull($this$commonToByteArray, "$this$commonToByteArray");
        byte[] result = new byte[$this$commonToByteArray.size()];
        int resultPos = 0;
        int segmentCount$iv = $this$commonToByteArray.getSegments$okio().length;
        int pos$iv = 0;
        for (int s$iv = 0; s$iv < segmentCount$iv; s$iv++) {
            int segmentPos$iv = $this$commonToByteArray.getDirectory$okio()[segmentCount$iv + s$iv];
            int nextSegmentOffset$iv = $this$commonToByteArray.getDirectory$okio()[s$iv];
            int byteCount = nextSegmentOffset$iv - pos$iv;
            ArraysKt.copyInto($this$commonToByteArray.getSegments$okio()[s$iv], result, resultPos, segmentPos$iv, segmentPos$iv + byteCount);
            resultPos += byteCount;
            pos$iv = nextSegmentOffset$iv;
        }
        return result;
    }

    public static final void commonWrite(SegmentedByteString $this$commonWrite, Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        int endIndex$iv = offset + byteCount;
        int s$iv = segment($this$commonWrite, offset);
        int pos$iv = offset;
        while (pos$iv < endIndex$iv) {
            int segmentOffset$iv = s$iv == 0 ? 0 : $this$commonWrite.getDirectory$okio()[s$iv - 1];
            int segmentPos$iv = $this$commonWrite.getDirectory$okio()[$this$commonWrite.getSegments$okio().length + s$iv];
            int byteCount$iv = Math.min(endIndex$iv, segmentOffset$iv + ($this$commonWrite.getDirectory$okio()[s$iv] - segmentOffset$iv)) - pos$iv;
            int offset$iv = (pos$iv - segmentOffset$iv) + segmentPos$iv;
            Segment segment = new Segment($this$commonWrite.getSegments$okio()[s$iv], offset$iv, offset$iv + byteCount$iv, true, false);
            if (buffer.head == null) {
                segment.prev = segment;
                segment.next = segment.prev;
                buffer.head = segment.next;
            } else {
                Segment segment2 = buffer.head;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                Segment segment3 = segment2.prev;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                segment3.push(segment);
            }
            pos$iv += byteCount$iv;
            s$iv++;
        }
        buffer.setSize$okio(buffer.size() + ((long) $this$commonWrite.size()));
    }

    public static final boolean commonRangeEquals(SegmentedByteString $this$commonRangeEquals, int offset, ByteString other, int otherOffset, int byteCount) {
        int $i$f$commonRangeEquals = 0;
        Intrinsics.checkParameterIsNotNull($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (offset >= 0 && offset <= $this$commonRangeEquals.size() - byteCount) {
            int otherOffset2 = otherOffset;
            int endIndex$iv = offset + byteCount;
            int s$iv = segment($this$commonRangeEquals, offset);
            int pos$iv = offset;
            while (pos$iv < endIndex$iv) {
                int segmentOffset$iv = s$iv == 0 ? 0 : $this$commonRangeEquals.getDirectory$okio()[s$iv - 1];
                int segmentPos$iv = $this$commonRangeEquals.getDirectory$okio()[$this$commonRangeEquals.getSegments$okio().length + s$iv];
                int byteCount$iv = Math.min(endIndex$iv, segmentOffset$iv + ($this$commonRangeEquals.getDirectory$okio()[s$iv] - segmentOffset$iv)) - pos$iv;
                if (!other.rangeEquals(otherOffset2, $this$commonRangeEquals.getSegments$okio()[s$iv], (pos$iv - segmentOffset$iv) + segmentPos$iv, byteCount$iv)) {
                    return false;
                }
                otherOffset2 += byteCount$iv;
                pos$iv += byteCount$iv;
                s$iv++;
                $i$f$commonRangeEquals = $i$f$commonRangeEquals;
            }
            return true;
        }
        return false;
    }

    public static final boolean commonRangeEquals(SegmentedByteString $this$commonRangeEquals, int offset, byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (offset < 0 || offset > $this$commonRangeEquals.size() - byteCount || otherOffset < 0 || otherOffset > other.length - byteCount) {
            return false;
        }
        int otherOffset2 = otherOffset;
        int endIndex$iv = offset + byteCount;
        int s$iv = segment($this$commonRangeEquals, offset);
        int pos$iv = offset;
        while (pos$iv < endIndex$iv) {
            int segmentOffset$iv = s$iv == 0 ? 0 : $this$commonRangeEquals.getDirectory$okio()[s$iv - 1];
            int segmentPos$iv = $this$commonRangeEquals.getDirectory$okio()[$this$commonRangeEquals.getSegments$okio().length + s$iv];
            int byteCount$iv = Math.min(endIndex$iv, segmentOffset$iv + ($this$commonRangeEquals.getDirectory$okio()[s$iv] - segmentOffset$iv)) - pos$iv;
            if (!Util.arrayRangeEquals($this$commonRangeEquals.getSegments$okio()[s$iv], segmentPos$iv + (pos$iv - segmentOffset$iv), other, otherOffset2, byteCount$iv)) {
                return false;
            }
            otherOffset2 += byteCount$iv;
            pos$iv += byteCount$iv;
            s$iv++;
        }
        return true;
    }

    public static final boolean commonEquals(SegmentedByteString $this$commonEquals, Object other) {
        Intrinsics.checkParameterIsNotNull($this$commonEquals, "$this$commonEquals");
        if (other == $this$commonEquals) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == $this$commonEquals.size() && $this$commonEquals.rangeEquals(0, (ByteString) other, 0, $this$commonEquals.size());
        }
        return false;
    }

    public static final int commonHashCode(SegmentedByteString $this$commonHashCode) {
        Intrinsics.checkParameterIsNotNull($this$commonHashCode, "$this$commonHashCode");
        int result = $this$commonHashCode.getHashCode$okio();
        if (result != 0) {
            return result;
        }
        int result2 = 1;
        int segmentCount$iv = $this$commonHashCode.getSegments$okio().length;
        int pos$iv = 0;
        for (int s$iv = 0; s$iv < segmentCount$iv; s$iv++) {
            int segmentPos$iv = $this$commonHashCode.getDirectory$okio()[segmentCount$iv + s$iv];
            int nextSegmentOffset$iv = $this$commonHashCode.getDirectory$okio()[s$iv];
            byte[] data = $this$commonHashCode.getSegments$okio()[s$iv];
            int limit = segmentPos$iv + (nextSegmentOffset$iv - pos$iv);
            for (int i = segmentPos$iv; i < limit; i++) {
                result2 = (result2 * 31) + data[i];
            }
            pos$iv = nextSegmentOffset$iv;
        }
        $this$commonHashCode.setHashCode$okio(result2);
        return result2;
    }
}
