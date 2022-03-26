package kotlin;

import com.facebook.common.util.UriUtil;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.ULongRange;
import okhttp3.internal.ws.WebSocketProtocol;
/* compiled from: ULong.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 m2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001mB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u000bJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b!\u0010\"J\u0013\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%HÖ\u0003J\t\u0010&\u001a\u00020\rHÖ\u0001J\u0013\u0010'\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b(\u0010\u0005J\u0013\u0010)\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b*\u0010\u0005J\u001b\u0010+\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b,\u0010\u001dJ\u001b\u0010+\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b-\u0010\u001fJ\u001b\u0010+\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b.\u0010\u000bJ\u001b\u0010+\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\"J\u001b\u00100\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b1\u0010\u000bJ\u001b\u00102\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u001dJ\u001b\u00102\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u001fJ\u001b\u00102\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b5\u0010\u000bJ\u001b\u00102\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b6\u0010\"J\u001b\u00107\u001a\u0002082\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b9\u0010:J\u001b\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b<\u0010\u001dJ\u001b\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b=\u0010\u001fJ\u001b\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010;\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b?\u0010\"J\u001b\u0010@\u001a\u00020\u00002\u0006\u0010A\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bB\u0010\u001fJ\u001b\u0010C\u001a\u00020\u00002\u0006\u0010A\u001a\u00020\rH\u0087\fø\u0001\u0000¢\u0006\u0004\bD\u0010\u001fJ\u001b\u0010E\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bF\u0010\u001dJ\u001b\u0010E\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bG\u0010\u001fJ\u001b\u0010E\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bH\u0010\u000bJ\u001b\u0010E\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bI\u0010\"J\u0010\u0010J\u001a\u00020KH\u0087\b¢\u0006\u0004\bL\u0010MJ\u0010\u0010N\u001a\u00020OH\u0087\b¢\u0006\u0004\bP\u0010QJ\u0010\u0010R\u001a\u00020SH\u0087\b¢\u0006\u0004\bT\u0010UJ\u0010\u0010V\u001a\u00020\rH\u0087\b¢\u0006\u0004\bW\u0010XJ\u0010\u0010Y\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bZ\u0010\u0005J\u0010\u0010[\u001a\u00020\\H\u0087\b¢\u0006\u0004\b]\u0010^J\u000f\u0010_\u001a\u00020`H\u0016¢\u0006\u0004\ba\u0010bJ\u0013\u0010c\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\bd\u0010MJ\u0013\u0010e\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\bf\u0010XJ\u0013\u0010g\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bh\u0010\u0005J\u0013\u0010i\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\bj\u0010^J\u001b\u0010k\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\bl\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006n"}, d2 = {"Lkotlin/ULong;", "", UriUtil.DATA_SCHEME, "", "constructor-impl", "(J)J", "data$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-impl", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-impl", "shr", "shr-impl", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "(J)I", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-VKZWuLQ", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ULong implements Comparable<ULong> {
    public static final Companion Companion = new Companion(null);
    public static final long MAX_VALUE;
    public static final long MIN_VALUE;
    public static final int SIZE_BITS;
    public static final int SIZE_BYTES;
    private final long data;

    /* renamed from: box-impl */
    public static final /* synthetic */ ULong m161boximpl(long j) {
        return new ULong(j);
    }

    /* renamed from: compareTo-VKZWuLQ */
    private int m163compareToVKZWuLQ(long j) {
        return m164compareToVKZWuLQ(this.data, j);
    }

    public static /* synthetic */ void data$annotations() {
    }

    /* renamed from: equals-impl */
    public static boolean m173equalsimpl(long j, Object obj) {
        return (obj instanceof ULong) && j == ((ULong) obj).m210unboximpl();
    }

    /* renamed from: equals-impl0 */
    public static final boolean m174equalsimpl0(long j, long j2) {
        return j == j2;
    }

    /* renamed from: hashCode-impl */
    public static int m175hashCodeimpl(long j) {
        return (int) (j ^ (j >>> 32));
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m173equalsimpl(this.data, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m175hashCodeimpl(this.data);
    }

    @Override // java.lang.Object
    public String toString() {
        return m204toStringimpl(this.data);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ long m210unboximpl() {
        return this.data;
    }

    private /* synthetic */ ULong(long data) {
        this.data = data;
    }

    /* renamed from: constructor-impl */
    public static long m167constructorimpl(long data) {
        return data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(ULong uLong) {
        return m163compareToVKZWuLQ(uLong.m210unboximpl());
    }

    /* compiled from: ULong.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/ULong$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/ULong;", "J", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    /* renamed from: compareTo-7apg3OU */
    private static final int m162compareTo7apg3OU(long $this, byte other) {
        return UnsignedKt.ulongCompare($this, m167constructorimpl(((long) other) & 255));
    }

    /* renamed from: compareTo-xj2QHRw */
    private static final int m166compareToxj2QHRw(long $this, short other) {
        return UnsignedKt.ulongCompare($this, m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX));
    }

    /* renamed from: compareTo-WZ4Q5Ns */
    private static final int m165compareToWZ4Q5Ns(long $this, int other) {
        return UnsignedKt.ulongCompare($this, m167constructorimpl(((long) other) & 4294967295L));
    }

    /* renamed from: compareTo-VKZWuLQ */
    private static int m164compareToVKZWuLQ(long $this, long other) {
        return UnsignedKt.ulongCompare($this, other);
    }

    /* renamed from: plus-7apg3OU */
    private static final long m183plus7apg3OU(long $this, byte other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & 255) + $this);
    }

    /* renamed from: plus-xj2QHRw */
    private static final long m186plusxj2QHRw(long $this, short other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX) + $this);
    }

    /* renamed from: plus-WZ4Q5Ns */
    private static final long m185plusWZ4Q5Ns(long $this, int other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & 4294967295L) + $this);
    }

    /* renamed from: plus-VKZWuLQ */
    private static final long m184plusVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this + other);
    }

    /* renamed from: minus-7apg3OU */
    private static final long m178minus7apg3OU(long $this, byte other) {
        return m167constructorimpl($this - m167constructorimpl(((long) other) & 255));
    }

    /* renamed from: minus-xj2QHRw */
    private static final long m181minusxj2QHRw(long $this, short other) {
        return m167constructorimpl($this - m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX));
    }

    /* renamed from: minus-WZ4Q5Ns */
    private static final long m180minusWZ4Q5Ns(long $this, int other) {
        return m167constructorimpl($this - m167constructorimpl(((long) other) & 4294967295L));
    }

    /* renamed from: minus-VKZWuLQ */
    private static final long m179minusVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this - other);
    }

    /* renamed from: times-7apg3OU */
    private static final long m194times7apg3OU(long $this, byte other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & 255) * $this);
    }

    /* renamed from: times-xj2QHRw */
    private static final long m197timesxj2QHRw(long $this, short other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX) * $this);
    }

    /* renamed from: times-WZ4Q5Ns */
    private static final long m196timesWZ4Q5Ns(long $this, int other) {
        return m167constructorimpl(m167constructorimpl(((long) other) & 4294967295L) * $this);
    }

    /* renamed from: times-VKZWuLQ */
    private static final long m195timesVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this * other);
    }

    /* renamed from: div-7apg3OU */
    private static final long m169div7apg3OU(long $this, byte other) {
        return UnsignedKt.m326ulongDivideeb3DHEI($this, m167constructorimpl(((long) other) & 255));
    }

    /* renamed from: div-xj2QHRw */
    private static final long m172divxj2QHRw(long $this, short other) {
        return UnsignedKt.m326ulongDivideeb3DHEI($this, m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX));
    }

    /* renamed from: div-WZ4Q5Ns */
    private static final long m171divWZ4Q5Ns(long $this, int other) {
        return UnsignedKt.m326ulongDivideeb3DHEI($this, m167constructorimpl(((long) other) & 4294967295L));
    }

    /* renamed from: div-VKZWuLQ */
    private static final long m170divVKZWuLQ(long $this, long other) {
        return UnsignedKt.m326ulongDivideeb3DHEI($this, other);
    }

    /* renamed from: rem-7apg3OU */
    private static final long m188rem7apg3OU(long $this, byte other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI($this, m167constructorimpl(((long) other) & 255));
    }

    /* renamed from: rem-xj2QHRw */
    private static final long m191remxj2QHRw(long $this, short other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI($this, m167constructorimpl(((long) other) & WebSocketProtocol.PAYLOAD_SHORT_MAX));
    }

    /* renamed from: rem-WZ4Q5Ns */
    private static final long m190remWZ4Q5Ns(long $this, int other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI($this, m167constructorimpl(((long) other) & 4294967295L));
    }

    /* renamed from: rem-VKZWuLQ */
    private static final long m189remVKZWuLQ(long $this, long other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI($this, other);
    }

    /* renamed from: inc-impl */
    private static final long m176incimpl(long $this) {
        return m167constructorimpl(1 + $this);
    }

    /* renamed from: dec-impl */
    private static final long m168decimpl(long $this) {
        return m167constructorimpl(-1 + $this);
    }

    /* renamed from: rangeTo-VKZWuLQ */
    private static final ULongRange m187rangeToVKZWuLQ(long $this, long other) {
        return new ULongRange($this, other, null);
    }

    /* renamed from: shl-impl */
    private static final long m192shlimpl(long $this, int bitCount) {
        return m167constructorimpl($this << bitCount);
    }

    /* renamed from: shr-impl */
    private static final long m193shrimpl(long $this, int bitCount) {
        return m167constructorimpl($this >>> bitCount);
    }

    /* renamed from: and-VKZWuLQ */
    private static final long m160andVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this & other);
    }

    /* renamed from: or-VKZWuLQ */
    private static final long m182orVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this | other);
    }

    /* renamed from: xor-VKZWuLQ */
    private static final long m209xorVKZWuLQ(long $this, long other) {
        return m167constructorimpl($this ^ other);
    }

    /* renamed from: inv-impl */
    private static final long m177invimpl(long $this) {
        return m167constructorimpl(~$this);
    }

    /* renamed from: toByte-impl */
    private static final byte m198toByteimpl(long $this) {
        return (byte) ((int) $this);
    }

    /* renamed from: toShort-impl */
    private static final short m203toShortimpl(long $this) {
        return (short) ((int) $this);
    }

    /* renamed from: toInt-impl */
    private static final int m201toIntimpl(long $this) {
        return (int) $this;
    }

    /* renamed from: toLong-impl */
    private static final long m202toLongimpl(long $this) {
        return $this;
    }

    /* renamed from: toUByte-impl */
    private static final byte m205toUByteimpl(long $this) {
        return UByte.m31constructorimpl((byte) ((int) $this));
    }

    /* renamed from: toUShort-impl */
    private static final short m208toUShortimpl(long $this) {
        return UShort.m264constructorimpl((short) ((int) $this));
    }

    /* renamed from: toUInt-impl */
    private static final int m206toUIntimpl(long $this) {
        return UInt.m98constructorimpl((int) $this);
    }

    /* renamed from: toULong-impl */
    private static final long m207toULongimpl(long $this) {
        return $this;
    }

    /* renamed from: toFloat-impl */
    private static final float m200toFloatimpl(long $this) {
        return (float) UnsignedKt.ulongToDouble($this);
    }

    /* renamed from: toDouble-impl */
    private static final double m199toDoubleimpl(long $this) {
        return UnsignedKt.ulongToDouble($this);
    }

    /* renamed from: toString-impl */
    public static String m204toStringimpl(long $this) {
        return UnsignedKt.ulongToString($this);
    }
}
