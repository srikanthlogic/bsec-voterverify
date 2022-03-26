package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import okio.internal.SegmentedByteStringKt;
/* compiled from: SegmentedByteString.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016J\u0015\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u0014J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0096\u0002J\r\u0010\u0019\u001a\u00020\u001aH\u0010¢\u0006\u0002\b\u001bJ\b\u0010\u001c\u001a\u00020\u001aH\u0016J\b\u0010\u001d\u001a\u00020\u0010H\u0016J\u001d\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0001H\u0010¢\u0006\u0002\b J\u0018\u0010!\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u001aH\u0016J\r\u0010#\u001a\u00020\u0004H\u0010¢\u0006\u0002\b$J\u0015\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u001aH\u0010¢\u0006\u0002\b(J\u0018\u0010)\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u001aH\u0016J(\u0010*\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0016J(\u0010*\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0016J\u0010\u0010.\u001a\u00020\u00102\u0006\u0010/\u001a\u000200H\u0016J\u0018\u00101\u001a\u00020\u00012\u0006\u00102\u001a\u00020\u001a2\u0006\u00103\u001a\u00020\u001aH\u0016J\b\u00104\u001a\u00020\u0001H\u0016J\b\u00105\u001a\u00020\u0001H\u0016J\b\u00106\u001a\u00020\u0004H\u0016J\b\u00107\u001a\u00020\u0001H\u0002J\b\u00108\u001a\u00020\u0010H\u0016J\u0010\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020<H\u0016J%\u00109\u001a\u00020:2\u0006\u0010=\u001a\u00020>2\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0010¢\u0006\u0002\b?J\b\u0010@\u001a\u00020AH\u0002R\u0014\u0010\u0005\u001a\u00020\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0080\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b¨\u0006B"}, d2 = {"Lokio/SegmentedByteString;", "Lokio/ByteString;", "segments", "", "", "directory", "", "([[B[I)V", "getDirectory$okio", "()[I", "getSegments$okio", "()[[B", "[[B", "asByteBuffer", "Ljava/nio/ByteBuffer;", "base64", "", "base64Url", "digest", "algorithm", "digest$okio", "equals", "", "other", "", "getSize", "", "getSize$okio", "hashCode", "hex", "hmac", "key", "hmac$okio", "indexOf", "fromIndex", "internalArray", "internalArray$okio", "internalGet", "", "pos", "internalGet$okio", "lastIndexOf", "rangeEquals", "offset", "otherOffset", "byteCount", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "beginIndex", "endIndex", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toByteString", "toString", "write", "", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$okio", "writeReplace", "Ljava/lang/Object;", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class SegmentedByteString extends ByteString {
    private final transient int[] directory;
    private final transient byte[][] segments;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SegmentedByteString(byte[][] segments, int[] directory) {
        super(ByteString.EMPTY.getData$okio());
        Intrinsics.checkParameterIsNotNull(segments, "segments");
        Intrinsics.checkParameterIsNotNull(directory, "directory");
        this.segments = segments;
        this.directory = directory;
    }

    public final byte[][] getSegments$okio() {
        return this.segments;
    }

    public final int[] getDirectory$okio() {
        return this.directory;
    }

    @Override // okio.ByteString
    public String string(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return toByteString().string(charset);
    }

    @Override // okio.ByteString
    public String base64() {
        return toByteString().base64();
    }

    @Override // okio.ByteString
    public String hex() {
        return toByteString().hex();
    }

    @Override // okio.ByteString
    public ByteString toAsciiLowercase() {
        return toByteString().toAsciiLowercase();
    }

    @Override // okio.ByteString
    public ByteString toAsciiUppercase() {
        return toByteString().toAsciiUppercase();
    }

    @Override // okio.ByteString
    public ByteString digest$okio(String algorithm) {
        Intrinsics.checkParameterIsNotNull(algorithm, "algorithm");
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        int segmentCount$iv = getSegments$okio().length;
        int pos$iv = 0;
        for (int s$iv = 0; s$iv < segmentCount$iv; s$iv++) {
            int segmentPos$iv = getDirectory$okio()[segmentCount$iv + s$iv];
            int nextSegmentOffset$iv = getDirectory$okio()[s$iv];
            digest.update(getSegments$okio()[s$iv], segmentPos$iv, nextSegmentOffset$iv - pos$iv);
            pos$iv = nextSegmentOffset$iv;
        }
        byte[] digest2 = digest.digest();
        Intrinsics.checkExpressionValueIsNotNull(digest2, "digest.digest()");
        return new ByteString(digest2);
    }

    @Override // okio.ByteString
    public ByteString hmac$okio(String algorithm, ByteString key) {
        Intrinsics.checkParameterIsNotNull(algorithm, "algorithm");
        Intrinsics.checkParameterIsNotNull(key, "key");
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.toByteArray(), algorithm));
            int segmentCount$iv = getSegments$okio().length;
            int pos$iv = 0;
            for (int s$iv = 0; s$iv < segmentCount$iv; s$iv++) {
                int segmentPos$iv = getDirectory$okio()[segmentCount$iv + s$iv];
                int nextSegmentOffset$iv = getDirectory$okio()[s$iv];
                mac.update(getSegments$okio()[s$iv], segmentPos$iv, nextSegmentOffset$iv - pos$iv);
                pos$iv = nextSegmentOffset$iv;
            }
            byte[] doFinal = mac.doFinal();
            Intrinsics.checkExpressionValueIsNotNull(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override // okio.ByteString
    public String base64Url() {
        return toByteString().base64Url();
    }

    @Override // okio.ByteString
    public ByteString substring(int beginIndex, int endIndex) {
        int segmentOffset$iv = 0;
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= size()) {
                int subLen$iv = endIndex - beginIndex;
                if (subLen$iv < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex=" + endIndex + " < beginIndex=" + beginIndex).toString());
                } else if (beginIndex == 0 && endIndex == size()) {
                    return this;
                } else {
                    if (beginIndex == endIndex) {
                        return ByteString.EMPTY;
                    }
                    int beginSegment$iv = SegmentedByteStringKt.segment(this, beginIndex);
                    int endSegment$iv = SegmentedByteStringKt.segment(this, endIndex - 1);
                    byte[][] newSegments$iv = (byte[][]) ArraysKt.copyOfRange(getSegments$okio(), beginSegment$iv, endSegment$iv + 1);
                    int[] newDirectory$iv = new int[newSegments$iv.length * 2];
                    int index$iv = 0;
                    if (beginSegment$iv <= endSegment$iv) {
                        int s$iv = beginSegment$iv;
                        while (true) {
                            newDirectory$iv[index$iv] = Math.min(getDirectory$okio()[s$iv] - beginIndex, subLen$iv);
                            int index$iv2 = index$iv + 1;
                            newDirectory$iv[index$iv + newSegments$iv.length] = getDirectory$okio()[getSegments$okio().length + s$iv];
                            if (s$iv == endSegment$iv) {
                                break;
                            }
                            s$iv++;
                            index$iv = index$iv2;
                        }
                    }
                    if (beginSegment$iv != 0) {
                        segmentOffset$iv = getDirectory$okio()[beginSegment$iv - 1];
                    }
                    int length = newSegments$iv.length;
                    newDirectory$iv[length] = newDirectory$iv[length] + (beginIndex - segmentOffset$iv);
                    return new SegmentedByteString(newSegments$iv, newDirectory$iv);
                }
            } else {
                throw new IllegalArgumentException(("endIndex=" + endIndex + " > length(" + size() + ')').toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex=" + beginIndex + " < 0").toString());
        }
    }

    @Override // okio.ByteString
    public byte internalGet$okio(int pos) {
        Util.checkOffsetAndCount((long) getDirectory$okio()[getSegments$okio().length - 1], (long) pos, 1);
        int segment$iv = SegmentedByteStringKt.segment(this, pos);
        return getSegments$okio()[segment$iv][(pos - (segment$iv == 0 ? 0 : getDirectory$okio()[segment$iv - 1])) + getDirectory$okio()[getSegments$okio().length + segment$iv]];
    }

    @Override // okio.ByteString
    public int getSize$okio() {
        return getDirectory$okio()[getSegments$okio().length - 1];
    }

    @Override // okio.ByteString
    public byte[] toByteArray() {
        byte[] result$iv = new byte[size()];
        int resultPos$iv = 0;
        int segmentCount$iv$iv = getSegments$okio().length;
        int pos$iv$iv = 0;
        for (int s$iv$iv = 0; s$iv$iv < segmentCount$iv$iv; s$iv$iv++) {
            int segmentPos$iv$iv = getDirectory$okio()[segmentCount$iv$iv + s$iv$iv];
            int nextSegmentOffset$iv$iv = getDirectory$okio()[s$iv$iv];
            int byteCount$iv = nextSegmentOffset$iv$iv - pos$iv$iv;
            ArraysKt.copyInto(getSegments$okio()[s$iv$iv], result$iv, resultPos$iv, segmentPos$iv$iv, segmentPos$iv$iv + byteCount$iv);
            resultPos$iv += byteCount$iv;
            pos$iv$iv = nextSegmentOffset$iv$iv;
        }
        return result$iv;
    }

    @Override // okio.ByteString
    public ByteBuffer asByteBuffer() {
        ByteBuffer asReadOnlyBuffer = ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
        Intrinsics.checkExpressionValueIsNotNull(asReadOnlyBuffer, "ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer()");
        return asReadOnlyBuffer;
    }

    @Override // okio.ByteString
    public void write(OutputStream out) throws IOException {
        Intrinsics.checkParameterIsNotNull(out, "out");
        int segmentCount$iv = getSegments$okio().length;
        int pos$iv = 0;
        for (int s$iv = 0; s$iv < segmentCount$iv; s$iv++) {
            int segmentPos$iv = getDirectory$okio()[segmentCount$iv + s$iv];
            int nextSegmentOffset$iv = getDirectory$okio()[s$iv];
            out.write(getSegments$okio()[s$iv], segmentPos$iv, nextSegmentOffset$iv - pos$iv);
            pos$iv = nextSegmentOffset$iv;
        }
    }

    @Override // okio.ByteString
    public void write$okio(Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        int endIndex$iv$iv = offset + byteCount;
        int s$iv$iv = SegmentedByteStringKt.segment(this, offset);
        int pos$iv$iv = offset;
        while (pos$iv$iv < endIndex$iv$iv) {
            int segmentOffset$iv$iv = s$iv$iv == 0 ? 0 : getDirectory$okio()[s$iv$iv - 1];
            int segmentPos$iv$iv = getDirectory$okio()[getSegments$okio().length + s$iv$iv];
            int byteCount$iv$iv = Math.min(endIndex$iv$iv, segmentOffset$iv$iv + (getDirectory$okio()[s$iv$iv] - segmentOffset$iv$iv)) - pos$iv$iv;
            int offset$iv$iv = (pos$iv$iv - segmentOffset$iv$iv) + segmentPos$iv$iv;
            Segment segment$iv = new Segment(getSegments$okio()[s$iv$iv], offset$iv$iv, offset$iv$iv + byteCount$iv$iv, true, false);
            if (buffer.head == null) {
                segment$iv.prev = segment$iv;
                segment$iv.next = segment$iv.prev;
                buffer.head = segment$iv.next;
            } else {
                Segment segment = buffer.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                Segment segment2 = segment.prev;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                segment2.push(segment$iv);
            }
            pos$iv$iv += byteCount$iv$iv;
            s$iv$iv++;
        }
        buffer.setSize$okio(buffer.size() + ((long) size()));
    }

    @Override // okio.ByteString
    public boolean rangeEquals(int offset, ByteString other, int otherOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        SegmentedByteString $this$commonRangeEquals$iv = this;
        if (offset >= 0 && offset <= $this$commonRangeEquals$iv.size() - byteCount) {
            int otherOffset$iv = otherOffset;
            int endIndex$iv$iv = offset + byteCount;
            int s$iv$iv = SegmentedByteStringKt.segment($this$commonRangeEquals$iv, offset);
            int pos$iv$iv = offset;
            while (pos$iv$iv < endIndex$iv$iv) {
                int segmentOffset$iv$iv = s$iv$iv == 0 ? 0 : $this$commonRangeEquals$iv.getDirectory$okio()[s$iv$iv - 1];
                int segmentPos$iv$iv = $this$commonRangeEquals$iv.getDirectory$okio()[$this$commonRangeEquals$iv.getSegments$okio().length + s$iv$iv];
                int byteCount$iv$iv = Math.min(endIndex$iv$iv, segmentOffset$iv$iv + ($this$commonRangeEquals$iv.getDirectory$okio()[s$iv$iv] - segmentOffset$iv$iv)) - pos$iv$iv;
                if (!other.rangeEquals(otherOffset$iv, $this$commonRangeEquals$iv.getSegments$okio()[s$iv$iv], (pos$iv$iv - segmentOffset$iv$iv) + segmentPos$iv$iv, byteCount$iv$iv)) {
                    return false;
                }
                otherOffset$iv += byteCount$iv$iv;
                pos$iv$iv += byteCount$iv$iv;
                s$iv$iv++;
                $this$commonRangeEquals$iv = $this$commonRangeEquals$iv;
            }
            return true;
        }
        return false;
    }

    @Override // okio.ByteString
    public boolean rangeEquals(int offset, byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (offset < 0 || offset > size() - byteCount || otherOffset < 0 || otherOffset > other.length - byteCount) {
            return false;
        }
        int otherOffset$iv = otherOffset;
        int endIndex$iv$iv = offset + byteCount;
        int s$iv$iv = SegmentedByteStringKt.segment(this, offset);
        int pos$iv$iv = offset;
        while (pos$iv$iv < endIndex$iv$iv) {
            int segmentOffset$iv$iv = s$iv$iv == 0 ? 0 : getDirectory$okio()[s$iv$iv - 1];
            int segmentPos$iv$iv = getDirectory$okio()[getSegments$okio().length + s$iv$iv];
            int byteCount$iv$iv = Math.min(endIndex$iv$iv, segmentOffset$iv$iv + (getDirectory$okio()[s$iv$iv] - segmentOffset$iv$iv)) - pos$iv$iv;
            if (!Util.arrayRangeEquals(getSegments$okio()[s$iv$iv], segmentPos$iv$iv + (pos$iv$iv - segmentOffset$iv$iv), other, otherOffset$iv, byteCount$iv$iv)) {
                return false;
            }
            otherOffset$iv += byteCount$iv$iv;
            pos$iv$iv += byteCount$iv$iv;
            s$iv$iv++;
        }
        return true;
    }

    @Override // okio.ByteString
    public int indexOf(byte[] other, int fromIndex) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        return toByteString().indexOf(other, fromIndex);
    }

    @Override // okio.ByteString
    public int lastIndexOf(byte[] other, int fromIndex) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        return toByteString().lastIndexOf(other, fromIndex);
    }

    private final ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    @Override // okio.ByteString
    public byte[] internalArray$okio() {
        return toByteArray();
    }

    @Override // okio.ByteString, java.lang.Object
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == size() && rangeEquals(0, (ByteString) other, 0, size());
        }
        return false;
    }

    @Override // okio.ByteString, java.lang.Object
    public int hashCode() {
        int result$iv = getHashCode$okio();
        if (result$iv == 0) {
            result$iv = 1;
            int segmentCount$iv$iv = getSegments$okio().length;
            int pos$iv$iv = 0;
            for (int s$iv$iv = 0; s$iv$iv < segmentCount$iv$iv; s$iv$iv++) {
                int segmentPos$iv$iv = getDirectory$okio()[segmentCount$iv$iv + s$iv$iv];
                int nextSegmentOffset$iv$iv = getDirectory$okio()[s$iv$iv];
                byte[] data$iv = getSegments$okio()[s$iv$iv];
                int limit$iv = segmentPos$iv$iv + (nextSegmentOffset$iv$iv - pos$iv$iv);
                for (int i$iv = segmentPos$iv$iv; i$iv < limit$iv; i$iv++) {
                    result$iv = (result$iv * 31) + data$iv[i$iv];
                }
                pos$iv$iv = nextSegmentOffset$iv$iv;
            }
            setHashCode$okio(result$iv);
        }
        return result$iv;
    }

    @Override // okio.ByteString, java.lang.Object
    public String toString() {
        return toByteString().toString();
    }

    private final Object writeReplace() {
        ByteString byteString = toByteString();
        if (byteString != null) {
            return byteString;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
    }
}
