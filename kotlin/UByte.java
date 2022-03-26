package kotlin;

import com.facebook.common.util.UriUtil;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
/* compiled from: UByte.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 f2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001fB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u000fJ\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0012J\u001b\u0010\u001b\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u0013\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003J\t\u0010$\u001a\u00020\rHÖ\u0001J\u0013\u0010%\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b&\u0010\u0005J\u0013\u0010'\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0005J\u001b\u0010)\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b*\u0010\u000fJ\u001b\u0010)\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b+\u0010\u0012J\u001b\u0010)\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b,\u0010\u001fJ\u001b\u0010)\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b-\u0010\u0018J\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b/\u0010\u000bJ\u001b\u00100\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u000fJ\u001b\u00100\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b2\u0010\u0012J\u001b\u00100\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u001fJ\u001b\u00100\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u0018J\u001b\u00105\u001a\u0002062\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b7\u00108J\u001b\u00109\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b:\u0010\u000fJ\u001b\u00109\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b;\u0010\u0012J\u001b\u00109\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b<\u0010\u001fJ\u001b\u00109\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b=\u0010\u0018J\u001b\u0010>\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b?\u0010\u000fJ\u001b\u0010>\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u0012J\u001b\u0010>\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u001fJ\u001b\u0010>\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u0018J\u0010\u0010C\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bD\u0010\u0005J\u0010\u0010E\u001a\u00020FH\u0087\b¢\u0006\u0004\bG\u0010HJ\u0010\u0010I\u001a\u00020JH\u0087\b¢\u0006\u0004\bK\u0010LJ\u0010\u0010M\u001a\u00020\rH\u0087\b¢\u0006\u0004\bN\u0010OJ\u0010\u0010P\u001a\u00020QH\u0087\b¢\u0006\u0004\bR\u0010SJ\u0010\u0010T\u001a\u00020UH\u0087\b¢\u0006\u0004\bV\u0010WJ\u000f\u0010X\u001a\u00020YH\u0016¢\u0006\u0004\bZ\u0010[J\u0013\u0010\\\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b]\u0010\u0005J\u0013\u0010^\u001a\u00020\u0010H\u0087\bø\u0001\u0000¢\u0006\u0004\b_\u0010OJ\u0013\u0010`\u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\ba\u0010SJ\u0013\u0010b\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\bc\u0010WJ\u001b\u0010d\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\be\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006g"}, d2 = {"Lkotlin/UByte;", "", UriUtil.DATA_SCHEME, "", "constructor-impl", "(B)B", "data$annotations", "()V", "and", "other", "and-7apg3OU", "(BB)B", "compareTo", "", "compareTo-7apg3OU", "(BB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(BI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(BJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(BS)I", "dec", "dec-impl", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(BJ)J", "div-xj2QHRw", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-7apg3OU", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-7apg3OU", "(BB)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "toByte-impl", "toDouble", "", "toDouble-impl", "(B)D", "toFloat", "", "toFloat-impl", "(B)F", "toInt", "toInt-impl", "(B)I", "toLong", "", "toLong-impl", "(B)J", "toShort", "", "toShort-impl", "(B)S", "toString", "", "toString-impl", "(B)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-7apg3OU", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class UByte implements Comparable<UByte> {
    public static final Companion Companion = new Companion(null);
    public static final byte MAX_VALUE;
    public static final byte MIN_VALUE;
    public static final int SIZE_BITS;
    public static final int SIZE_BYTES;
    private final byte data;

    /* renamed from: box-impl */
    public static final /* synthetic */ UByte m25boximpl(byte b) {
        return new UByte(b);
    }

    /* renamed from: compareTo-7apg3OU */
    private int m26compareTo7apg3OU(byte b) {
        return m27compareTo7apg3OU(this.data, b);
    }

    public static /* synthetic */ void data$annotations() {
    }

    /* renamed from: equals-impl */
    public static boolean m37equalsimpl(byte b, Object obj) {
        return (obj instanceof UByte) && b == ((UByte) obj).m72unboximpl();
    }

    /* renamed from: equals-impl0 */
    public static final boolean m38equalsimpl0(byte b, byte b2) {
        return b == b2;
    }

    /* renamed from: hashCode-impl */
    public static int m39hashCodeimpl(byte b) {
        return b;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m37equalsimpl(this.data, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m39hashCodeimpl(this.data);
    }

    @Override // java.lang.Object
    public String toString() {
        return m66toStringimpl(this.data);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ byte m72unboximpl() {
        return this.data;
    }

    private /* synthetic */ UByte(byte data) {
        this.data = data;
    }

    /* renamed from: constructor-impl */
    public static byte m31constructorimpl(byte data) {
        return data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UByte uByte) {
        return m26compareTo7apg3OU(uByte.m72unboximpl());
    }

    /* compiled from: UByte.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/UByte$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UByte;", "B", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    /* renamed from: compareTo-7apg3OU */
    private static int m27compareTo7apg3OU(byte $this, byte other) {
        return Intrinsics.compare($this & 255, other & 255);
    }

    /* renamed from: compareTo-xj2QHRw */
    private static final int m30compareToxj2QHRw(byte $this, short other) {
        return Intrinsics.compare($this & 255, 65535 & other);
    }

    /* renamed from: compareTo-WZ4Q5Ns */
    private static final int m29compareToWZ4Q5Ns(byte $this, int other) {
        return UnsignedKt.uintCompare(UInt.m98constructorimpl($this & 255), other);
    }

    /* renamed from: compareTo-VKZWuLQ */
    private static final int m28compareToVKZWuLQ(byte $this, long other) {
        return UnsignedKt.ulongCompare(ULong.m167constructorimpl(((long) $this) & 255), other);
    }

    /* renamed from: plus-7apg3OU */
    private static final int m47plus7apg3OU(byte $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) + UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: plus-xj2QHRw */
    private static final int m50plusxj2QHRw(byte $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) + UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: plus-WZ4Q5Ns */
    private static final int m49plusWZ4Q5Ns(byte $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) + other);
    }

    /* renamed from: plus-VKZWuLQ */
    private static final long m48plusVKZWuLQ(byte $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 255) + other);
    }

    /* renamed from: minus-7apg3OU */
    private static final int m42minus7apg3OU(byte $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) - UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: minus-xj2QHRw */
    private static final int m45minusxj2QHRw(byte $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) - UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: minus-WZ4Q5Ns */
    private static final int m44minusWZ4Q5Ns(byte $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) - other);
    }

    /* renamed from: minus-VKZWuLQ */
    private static final long m43minusVKZWuLQ(byte $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 255) - other);
    }

    /* renamed from: times-7apg3OU */
    private static final int m56times7apg3OU(byte $this, byte other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) * UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: times-xj2QHRw */
    private static final int m59timesxj2QHRw(byte $this, short other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) * UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: times-WZ4Q5Ns */
    private static final int m58timesWZ4Q5Ns(byte $this, int other) {
        return UInt.m98constructorimpl(UInt.m98constructorimpl($this & 255) * other);
    }

    /* renamed from: times-VKZWuLQ */
    private static final long m57timesVKZWuLQ(byte $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 255) * other);
    }

    /* renamed from: div-7apg3OU */
    private static final int m33div7apg3OU(byte $this, byte other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl($this & 255), UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: div-xj2QHRw */
    private static final int m36divxj2QHRw(byte $this, short other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl($this & 255), UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: div-WZ4Q5Ns */
    private static final int m35divWZ4Q5Ns(byte $this, int other) {
        return UnsignedKt.m324uintDivideJ1ME1BU(UInt.m98constructorimpl($this & 255), other);
    }

    /* renamed from: div-VKZWuLQ */
    private static final long m34divVKZWuLQ(byte $this, long other) {
        return UnsignedKt.m326ulongDivideeb3DHEI(ULong.m167constructorimpl(((long) $this) & 255), other);
    }

    /* renamed from: rem-7apg3OU */
    private static final int m52rem7apg3OU(byte $this, byte other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl($this & 255), UInt.m98constructorimpl(other & 255));
    }

    /* renamed from: rem-xj2QHRw */
    private static final int m55remxj2QHRw(byte $this, short other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl($this & 255), UInt.m98constructorimpl(65535 & other));
    }

    /* renamed from: rem-WZ4Q5Ns */
    private static final int m54remWZ4Q5Ns(byte $this, int other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU(UInt.m98constructorimpl($this & 255), other);
    }

    /* renamed from: rem-VKZWuLQ */
    private static final long m53remVKZWuLQ(byte $this, long other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI(ULong.m167constructorimpl(((long) $this) & 255), other);
    }

    /* renamed from: inc-impl */
    private static final byte m40incimpl(byte $this) {
        return m31constructorimpl((byte) ($this + 1));
    }

    /* renamed from: dec-impl */
    private static final byte m32decimpl(byte $this) {
        return m31constructorimpl((byte) ($this - 1));
    }

    /* renamed from: rangeTo-7apg3OU */
    private static final UIntRange m51rangeTo7apg3OU(byte $this, byte other) {
        return new UIntRange(UInt.m98constructorimpl($this & 255), UInt.m98constructorimpl(other & 255), null);
    }

    /* renamed from: and-7apg3OU */
    private static final byte m24and7apg3OU(byte $this, byte other) {
        return m31constructorimpl((byte) ($this & other));
    }

    /* renamed from: or-7apg3OU */
    private static final byte m46or7apg3OU(byte $this, byte other) {
        return m31constructorimpl((byte) ($this | other));
    }

    /* renamed from: xor-7apg3OU */
    private static final byte m71xor7apg3OU(byte $this, byte other) {
        return m31constructorimpl((byte) ($this ^ other));
    }

    /* renamed from: inv-impl */
    private static final byte m41invimpl(byte $this) {
        return m31constructorimpl((byte) (~$this));
    }

    /* renamed from: toByte-impl */
    private static final byte m60toByteimpl(byte $this) {
        return $this;
    }

    /* renamed from: toShort-impl */
    private static final short m65toShortimpl(byte $this) {
        return (short) (((short) $this) & 255);
    }

    /* renamed from: toInt-impl */
    private static final int m63toIntimpl(byte $this) {
        return $this & 255;
    }

    /* renamed from: toLong-impl */
    private static final long m64toLongimpl(byte $this) {
        return ((long) $this) & 255;
    }

    /* renamed from: toUByte-impl */
    private static final byte m67toUByteimpl(byte $this) {
        return $this;
    }

    /* renamed from: toUShort-impl */
    private static final short m70toUShortimpl(byte $this) {
        return UShort.m264constructorimpl((short) (((short) $this) & 255));
    }

    /* renamed from: toUInt-impl */
    private static final int m68toUIntimpl(byte $this) {
        return UInt.m98constructorimpl($this & 255);
    }

    /* renamed from: toULong-impl */
    private static final long m69toULongimpl(byte $this) {
        return ULong.m167constructorimpl(((long) $this) & 255);
    }

    /* renamed from: toFloat-impl */
    private static final float m62toFloatimpl(byte $this) {
        return (float) ($this & 255);
    }

    /* renamed from: toDouble-impl */
    private static final double m61toDoubleimpl(byte $this) {
        return (double) ($this & 255);
    }

    /* renamed from: toString-impl */
    public static String m66toStringimpl(byte $this) {
        return String.valueOf($this & 255);
    }
}
