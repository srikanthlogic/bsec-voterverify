package kotlin;

import androidx.exifinterface.media.ExifInterface;
import com.facebook.common.util.UriUtil;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import okhttp3.internal.ws.WebSocketProtocol;
/* compiled from: UShort.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 f2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001fB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u0010J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0013J\u001b\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u0013\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003J\t\u0010$\u001a\u00020\rHÖ\u0001J\u0013\u0010%\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b&\u0010\u0005J\u0013\u0010'\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0005J\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b*\u0010\u0010J\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b+\u0010\u0013J\u001b\u0010)\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b,\u0010\u001fJ\u001b\u0010)\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b-\u0010\u0018J\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b/\u0010\u000bJ\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u0010J\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b2\u0010\u0013J\u001b\u00100\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u001fJ\u001b\u00100\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u0018J\u001b\u00105\u001a\u0002062\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b7\u00108J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b:\u0010\u0010J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b;\u0010\u0013J\u001b\u00109\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b<\u0010\u001fJ\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b=\u0010\u0018J\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b?\u0010\u0010J\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u0013J\u001b\u0010>\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u001fJ\u001b\u0010>\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u0018J\u0010\u0010C\u001a\u00020DH\u0087\b¢\u0006\u0004\bE\u0010FJ\u0010\u0010G\u001a\u00020HH\u0087\b¢\u0006\u0004\bI\u0010JJ\u0010\u0010K\u001a\u00020LH\u0087\b¢\u0006\u0004\bM\u0010NJ\u0010\u0010O\u001a\u00020\rH\u0087\b¢\u0006\u0004\bP\u0010QJ\u0010\u0010R\u001a\u00020SH\u0087\b¢\u0006\u0004\bT\u0010UJ\u0010\u0010V\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bW\u0010\u0005J\u000f\u0010X\u001a\u00020YH\u0016¢\u0006\u0004\bZ\u0010[J\u0013\u0010\\\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\b]\u0010FJ\u0013\u0010^\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010QJ\u0013\u0010`\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\ba\u0010UJ\u0013\u0010b\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bc\u0010\u0005J\u001b\u0010d\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\be\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006g"}, d2 = {"Lkotlin/UShort;", "", UriUtil.DATA_SCHEME, "", "constructor-impl", "(S)S", "data$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-impl", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toDouble", "", "toDouble-impl", "(S)D", "toFloat", "", "toFloat-impl", "(S)F", "toInt", "toInt-impl", "(S)I", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-xj2QHRw", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class UShort implements Comparable<UShort> {
    public static final Companion Companion = new Companion(null);
    public static final short MAX_VALUE;
    public static final short MIN_VALUE;
    public static final int SIZE_BITS;
    public static final int SIZE_BYTES;
    private final short data;

    /* renamed from: box-impl */
    public static final /* synthetic */ UShort m258boximpl(short s) {
        return new UShort(s);
    }

    /* renamed from: compareTo-xj2QHRw */
    private int m262compareToxj2QHRw(short s) {
        return m263compareToxj2QHRw(this.data, s);
    }

    public static /* synthetic */ void data$annotations() {
    }

    /* renamed from: equals-impl */
    public static boolean m270equalsimpl(short s, Object obj) {
        return (obj instanceof UShort) && s == ((UShort) obj).m305unboximpl();
    }

    /* renamed from: equals-impl0 */
    public static final boolean m271equalsimpl0(short s, short s2) {
        return s == s2;
    }

    /* renamed from: hashCode-impl */
    public static int m272hashCodeimpl(short s) {
        return s;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m270equalsimpl(this.data, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m272hashCodeimpl(this.data);
    }

    @Override // java.lang.Object
    public String toString() {
        return m299toStringimpl(this.data);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ short m305unboximpl() {
        return this.data;
    }

    private /* synthetic */ UShort(short data) {
        this.data = data;
    }

    /* renamed from: constructor-impl */
    public static short m264constructorimpl(short data) {
        return data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UShort uShort) {
        return m262compareToxj2QHRw(uShort.m305unboximpl());
    }

    /* compiled from: UShort.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UShort;", ExifInterface.LATITUDE_SOUTH, "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    /* renamed from: compareTo-7apg3OU */
    private static final int m259compareTo7apg3OU(short $this, byte other) {
        return Intrinsics.compare(65535 & $this, other & 255);
    }

    /* renamed from: compareTo-xj2QHRw */
    private static int m263compareToxj2QHRw(short $this, short other) {
        return Intrinsics.compare($this & MAX_VALUE, 65535 & other);
    }

    /* renamed from: compareTo-WZ4Q5Ns */
    private static final int m261compareToWZ4Q5Ns(short $this, int other) {
        return UnsignedKt.uintCompare(UInt.m98constructorimpl(65535 & $this), other);
    }

    /* renamed from: compareTo-VKZWuLQ */
    private static final int m260compareToVKZWuLQ(short $this, long other) {
        return UnsignedKt.ulongCompare(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: plus-7apg3OU */
    private static final int m280plus7apg3OU(short $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) + UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: plus-xj2QHRw */
    private static final int m283plusxj2QHRw(short $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & MAX_VALUE) + UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: plus-WZ4Q5Ns */
    private static final int m282plusWZ4Q5Ns(short $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) + other);
    }

    /* renamed from: plus-VKZWuLQ */
    private static final long m281plusVKZWuLQ(short $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX) + other);
    }

    /* renamed from: minus-7apg3OU */
    private static final int m275minus7apg3OU(short $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) - UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: minus-xj2QHRw */
    private static final int m278minusxj2QHRw(short $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & MAX_VALUE) - UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: minus-WZ4Q5Ns */
    private static final int m277minusWZ4Q5Ns(short $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) - other);
    }

    /* renamed from: minus-VKZWuLQ */
    private static final long m276minusVKZWuLQ(short $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX) - other);
    }

    /* renamed from: times-7apg3OU */
    private static final int m289times7apg3OU(short $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) * UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: times-xj2QHRw */
    private static final int m292timesxj2QHRw(short $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & MAX_VALUE) * UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: times-WZ4Q5Ns */
    private static final int m291timesWZ4Q5Ns(short $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & $this) * other);
    }

    /* renamed from: times-VKZWuLQ */
    private static final long m290timesVKZWuLQ(short $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX) * other);
    }

    /* renamed from: div-7apg3OU */
    private static final int m266div7apg3OU(short $this, byte other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl(65535 & $this), UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: div-xj2QHRw */
    private static final int m269divxj2QHRw(short $this, short other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl($this & MAX_VALUE), UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: div-WZ4Q5Ns */
    private static final int m268divWZ4Q5Ns(short $this, int other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl(65535 & $this), other);
    }

    /* renamed from: div-VKZWuLQ */
    private static final long m267divVKZWuLQ(short $this, long other) {
        return UnsignedKt.m326ulongDivideeb3DHEI(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: rem-7apg3OU */
    private static final int m285rem7apg3OU(short $this, byte other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl(65535 & $this), UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: rem-xj2QHRw */
    private static final int m288remxj2QHRw(short $this, short other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl($this & MAX_VALUE), UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: rem-WZ4Q5Ns */
    private static final int m287remWZ4Q5Ns(short $this, int other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl(65535 & $this), other);
    }

    /* renamed from: rem-VKZWuLQ */
    private static final long m286remVKZWuLQ(short $this, long other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI(ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX), other);
    }

    /* renamed from: inc-impl */
    private static final short m273incimpl(short $this) {
        return m264constructorimpl((short) ($this + 1));
    }

    /* renamed from: dec-impl */
    private static final short m265decimpl(short $this) {
        return m264constructorimpl((short) ($this - 1));
    }

    /* renamed from: rangeTo-xj2QHRw */
    private static final UIntRange m284rangeToxj2QHRw(short $this, short other) {
        return new UIntRange(UInt.m98constructorimpl($this & MAX_VALUE), UInt.m98constructorimpl(65535 & other), null);
    }

    /* renamed from: and-xj2QHRw */
    private static final short m257andxj2QHRw(short $this, short other) {
        return m264constructorimpl((short) ($this & other));
    }

    /* renamed from: or-xj2QHRw */
    private static final short m279orxj2QHRw(short $this, short other) {
        return m264constructorimpl((short) ($this | other));
    }

    /* renamed from: xor-xj2QHRw */
    private static final short m304xorxj2QHRw(short $this, short other) {
        return m264constructorimpl((short) ($this ^ other));
    }

    /* renamed from: inv-impl */
    private static final short m274invimpl(short $this) {
        return m264constructorimpl((short) (~$this));
    }

    /* renamed from: toByte-impl */
    private static final byte m293toByteimpl(short $this) {
        return (byte) $this;
    }

    /* renamed from: toShort-impl */
    private static final short m298toShortimpl(short $this) {
        return $this;
    }

    /* renamed from: toInt-impl */
    private static final int m296toIntimpl(short $this) {
        return 65535 & $this;
    }

    /* renamed from: toLong-impl */
    private static final long m297toLongimpl(short $this) {
        return ((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX;
    }

    /* renamed from: toUByte-impl */
    private static final byte m300toUByteimpl(short $this) {
        return UByte.m31constructorimpl((byte) $this);
    }

    /* renamed from: toUShort-impl */
    private static final short m303toUShortimpl(short $this) {
        return $this;
    }

    /* renamed from: toUInt-impl */
    private static final int m301toUIntimpl(short $this) {
        return UInt.m98constructorimpl(65535 & $this);
    }

    /* renamed from: toULong-impl */
    private static final long m302toULongimpl(short $this) {
        return ULong.m167constructorimpl(((long) $this) & WebSocketProtocol.PAYLOAD_SHORT_MAX);
    }

    /* renamed from: toFloat-impl */
    private static final float m295toFloatimpl(short $this) {
        return (float) (65535 & $this);
    }

    /* renamed from: toDouble-impl */
    private static final double m294toDoubleimpl(short $this) {
        return (double) (65535 & $this);
    }

    /* renamed from: toString-impl */
    public static String m299toStringimpl(short $this) {
        return String.valueOf(65535 & $this);
    }
}
