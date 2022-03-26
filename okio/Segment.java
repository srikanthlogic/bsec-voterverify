package okio;

import com.facebook.common.util.UriUtil;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Segment.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u000b\b\u0000\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B/\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t¢\u0006\u0002\u0010\u000bJ\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u0004\u0018\u00010\u0000J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u0000J\u0006\u0010\u0013\u001a\u00020\u0000J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0006J\u0006\u0010\u0016\u001a\u00020\u0000J\u0016\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\t8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lokio/Segment;", "", "()V", UriUtil.DATA_SCHEME, "", "pos", "", "limit", "shared", "", "owner", "([BIIZZ)V", "next", "prev", "compact", "", "pop", "push", "segment", "sharedCopy", "split", "byteCount", "unsharedCopy", "writeTo", "sink", "Companion", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Segment {
    public static final Companion Companion = new Companion(null);
    public static final int SHARE_MINIMUM;
    public static final int SIZE;
    public final byte[] data;
    public int limit;
    public Segment next;
    public boolean owner;
    public int pos;
    public Segment prev;
    public boolean shared;

    public Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }

    public Segment(byte[] data, int pos, int limit, boolean shared, boolean owner) {
        Intrinsics.checkParameterIsNotNull(data, UriUtil.DATA_SCHEME);
        this.data = data;
        this.pos = pos;
        this.limit = limit;
        this.shared = shared;
        this.owner = owner;
    }

    public final Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true, false);
    }

    public final Segment unsharedCopy() {
        byte[] bArr = this.data;
        byte[] copyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return new Segment(copyOf, this.pos, this.limit, false, true);
    }

    public final Segment pop() {
        Segment result = this.next;
        if (result == this) {
            result = null;
        }
        Segment segment = this.prev;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        segment.next = this.next;
        Segment segment2 = this.next;
        if (segment2 == null) {
            Intrinsics.throwNpe();
        }
        segment2.prev = this.prev;
        Segment segment3 = null;
        this.next = segment3;
        this.prev = segment3;
        return result;
    }

    public final Segment push(Segment segment) {
        Intrinsics.checkParameterIsNotNull(segment, "segment");
        segment.prev = this;
        segment.next = this.next;
        Segment segment2 = this.next;
        if (segment2 == null) {
            Intrinsics.throwNpe();
        }
        segment2.prev = segment;
        this.next = segment;
        return segment;
    }

    public final Segment split(int byteCount) {
        Segment prefix;
        if (byteCount > 0 && byteCount <= this.limit - this.pos) {
            if (byteCount >= 1024) {
                prefix = sharedCopy();
            } else {
                prefix = SegmentPool.INSTANCE.take();
                byte[] bArr = this.data;
                byte[] bArr2 = prefix.data;
                int i = this.pos;
                ArraysKt.copyInto$default(bArr, bArr2, 0, i, i + byteCount, 2, (Object) null);
            }
            prefix.limit = prefix.pos + byteCount;
            this.pos += byteCount;
            Segment segment = this.prev;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            segment.push(prefix);
            return prefix;
        }
        throw new IllegalArgumentException("byteCount out of range".toString());
    }

    public final void compact() {
        int i = 0;
        if (this.prev != this) {
            Segment segment = this.prev;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            if (segment.owner) {
                int byteCount = this.limit - this.pos;
                Segment segment2 = this.prev;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                int i2 = 8192 - segment2.limit;
                Segment segment3 = this.prev;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!segment3.shared) {
                    Segment segment4 = this.prev;
                    if (segment4 == null) {
                        Intrinsics.throwNpe();
                    }
                    i = segment4.pos;
                }
                if (byteCount <= i2 + i) {
                    Segment segment5 = this.prev;
                    if (segment5 == null) {
                        Intrinsics.throwNpe();
                    }
                    writeTo(segment5, byteCount);
                    pop();
                    SegmentPool.INSTANCE.recycle(this);
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalStateException("cannot compact".toString());
    }

    public final void writeTo(Segment sink, int byteCount) {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        if (sink.owner) {
            int i = sink.limit;
            if (i + byteCount > 8192) {
                if (!sink.shared) {
                    int i2 = sink.pos;
                    if ((i + byteCount) - i2 <= 8192) {
                        byte[] bArr = sink.data;
                        ArraysKt.copyInto$default(bArr, bArr, 0, i2, i, 2, (Object) null);
                        sink.limit -= sink.pos;
                        sink.pos = 0;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
            byte[] bArr2 = this.data;
            byte[] bArr3 = sink.data;
            int i3 = sink.limit;
            int i4 = this.pos;
            ArraysKt.copyInto(bArr2, bArr3, i3, i4, i4 + byteCount);
            sink.limit += byteCount;
            this.pos += byteCount;
            return;
        }
        throw new IllegalStateException("only owner can write".toString());
    }

    /* compiled from: Segment.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lokio/Segment$Companion;", "", "()V", "SHARE_MINIMUM", "", "SIZE", "okio"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
