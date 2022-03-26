package kotlin;

import com.facebook.common.util.UriUtil;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.UIntRange;
/* compiled from: UInt.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 j2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001jB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0005J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u000fJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u000bJ\u001b\u0010\u0019\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u0016J\u0013\u0010\u001f\u001a\u00020 2\b\u0010\t\u001a\u0004\u0018\u00010!HÖ\u0003J\t\u0010\"\u001a\u00020\u0003HÖ\u0001J\u0013\u0010#\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b$\u0010\u0005J\u0013\u0010%\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b&\u0010\u0005J\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\b(\u0010\u000fJ\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b)\u0010\u000bJ\u001b\u0010'\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b*\u0010\u001dJ\u001b\u0010'\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b+\u0010\u0016J\u001b\u0010,\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b-\u0010\u000bJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\b/\u0010\u000fJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b0\u0010\u000bJ\u001b\u0010.\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b1\u0010\u001dJ\u001b\u0010.\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b2\u0010\u0016J\u001b\u00103\u001a\u0002042\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b5\u00106J\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\b8\u0010\u000fJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b9\u0010\u000bJ\u001b\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b:\u0010\u001dJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b;\u0010\u0016J\u001b\u0010<\u001a\u00020\u00002\u0006\u0010=\u001a\u00020\u0003H\u0087\fø\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00002\u0006\u0010=\u001a\u00020\u0003H\u0087\fø\u0001\u0000¢\u0006\u0004\b@\u0010\u000bJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u000fJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bC\u0010\u000bJ\u001b\u0010A\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bD\u0010\u001dJ\u001b\u0010A\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\bE\u0010\u0016J\u0010\u0010F\u001a\u00020GH\u0087\b¢\u0006\u0004\bH\u0010IJ\u0010\u0010J\u001a\u00020KH\u0087\b¢\u0006\u0004\bL\u0010MJ\u0010\u0010N\u001a\u00020OH\u0087\b¢\u0006\u0004\bP\u0010QJ\u0010\u0010R\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bS\u0010\u0005J\u0010\u0010T\u001a\u00020UH\u0087\b¢\u0006\u0004\bV\u0010WJ\u0010\u0010X\u001a\u00020YH\u0087\b¢\u0006\u0004\bZ\u0010[J\u000f\u0010\\\u001a\u00020]H\u0016¢\u0006\u0004\b^\u0010_J\u0013\u0010`\u001a\u00020\rH\u0087\bø\u0001\u0000¢\u0006\u0004\ba\u0010IJ\u0013\u0010b\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\bc\u0010\u0005J\u0013\u0010d\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\be\u0010WJ\u0013\u0010f\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\bg\u0010[J\u001b\u0010h\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\bi\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007ø\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006k"}, d2 = {"Lkotlin/UInt;", "", UriUtil.DATA_SCHEME, "", "constructor-impl", "(I)I", "data$annotations", "()V", "and", "other", "and-WZ4Q5Ns", "(II)I", "compareTo", "Lkotlin/UByte;", "compareTo-7apg3OU", "(IB)I", "compareTo-WZ4Q5Ns", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(IJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(IS)I", "dec", "dec-impl", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(IJ)J", "div-xj2QHRw", "equals", "", "", "hashCode", "inc", "inc-impl", "inv", "inv-impl", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-WZ4Q5Ns", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-WZ4Q5Ns", "(II)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-impl", "shr", "shr-impl", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(I)B", "toDouble", "", "toDouble-impl", "(I)D", "toFloat", "", "toFloat-impl", "(I)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(I)J", "toShort", "", "toShort-impl", "(I)S", "toString", "", "toString-impl", "(I)Ljava/lang/String;", "toUByte", "toUByte-impl", "toUInt", "toUInt-impl", "toULong", "toULong-impl", "toUShort", "toUShort-impl", "xor", "xor-WZ4Q5Ns", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class UInt implements Comparable<UInt> {
    public static final Companion Companion = new Companion(null);
    public static final int MAX_VALUE;
    public static final int MIN_VALUE;
    public static final int SIZE_BITS;
    public static final int SIZE_BYTES;
    private final int data;

    /* renamed from: box-impl */
    public static final /* synthetic */ UInt m92boximpl(int i) {
        return new UInt(i);
    }

    /* renamed from: compareTo-WZ4Q5Ns */
    private int m95compareToWZ4Q5Ns(int i) {
        return m96compareToWZ4Q5Ns(this.data, i);
    }

    public static /* synthetic */ void data$annotations() {
    }

    /* renamed from: equals-impl */
    public static boolean m104equalsimpl(int i, Object obj) {
        return (obj instanceof UInt) && i == ((UInt) obj).m141unboximpl();
    }

    /* renamed from: equals-impl0 */
    public static final boolean m105equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* renamed from: hashCode-impl */
    public static int m106hashCodeimpl(int i) {
        return i;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m104equalsimpl(this.data, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m106hashCodeimpl(this.data);
    }

    @Override // java.lang.Object
    public String toString() {
        return m135toStringimpl(this.data);
    }

    /* renamed from: unbox-impl */
    public final /* synthetic */ int m141unboximpl() {
        return this.data;
    }

    private /* synthetic */ UInt(int data) {
        this.data = data;
    }

    /* renamed from: constructor-impl */
    public static int m98constructorimpl(int data) {
        return data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UInt uInt) {
        return m95compareToWZ4Q5Ns(uInt.m141unboximpl());
    }

    /* compiled from: UInt.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u0013\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/UInt$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UInt;", "I", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    /* renamed from: compareTo-7apg3OU */
    private static final int m93compareTo7apg3OU(int $this, byte other) {
        return UnsignedKt.uintCompare($this, m98constructorimpl(other & 255));
    }

    /* renamed from: compareTo-xj2QHRw */
    private static final int m97compareToxj2QHRw(int $this, short other) {
        return UnsignedKt.uintCompare($this, m98constructorimpl(65535 & other));
    }

    /* renamed from: compareTo-WZ4Q5Ns */
    private static int m96compareToWZ4Q5Ns(int $this, int other) {
        return UnsignedKt.uintCompare($this, other);
    }

    /* renamed from: compareTo-VKZWuLQ */
    private static final int m94compareToVKZWuLQ(int $this, long other) {
        return UnsignedKt.ulongCompare(ULong.m167constructorimpl(((long) $this) & 4294967295L), other);
    }

    /* renamed from: plus-7apg3OU */
    private static final int m114plus7apg3OU(int $this, byte other) {
        return m98constructorimpl(m98constructorimpl(other & 255) + $this);
    }

    /* renamed from: plus-xj2QHRw */
    private static final int m117plusxj2QHRw(int $this, short other) {
        return m98constructorimpl(m98constructorimpl(65535 & other) + $this);
    }

    /* renamed from: plus-WZ4Q5Ns */
    private static final int m116plusWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this + other);
    }

    /* renamed from: plus-VKZWuLQ */
    private static final long m115plusVKZWuLQ(int $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 4294967295L) + other);
    }

    /* renamed from: minus-7apg3OU */
    private static final int m109minus7apg3OU(int $this, byte other) {
        return m98constructorimpl($this - m98constructorimpl(other & 255));
    }

    /* renamed from: minus-xj2QHRw */
    private static final int m112minusxj2QHRw(int $this, short other) {
        return m98constructorimpl($this - m98constructorimpl(65535 & other));
    }

    /* renamed from: minus-WZ4Q5Ns */
    private static final int m111minusWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this - other);
    }

    /* renamed from: minus-VKZWuLQ */
    private static final long m110minusVKZWuLQ(int $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 4294967295L) - other);
    }

    /* renamed from: times-7apg3OU */
    private static final int m125times7apg3OU(int $this, byte other) {
        return m98constructorimpl(m98constructorimpl(other & 255) * $this);
    }

    /* renamed from: times-xj2QHRw */
    private static final int m128timesxj2QHRw(int $this, short other) {
        return m98constructorimpl(m98constructorimpl(65535 & other) * $this);
    }

    /* renamed from: times-WZ4Q5Ns */
    private static final int m127timesWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this * other);
    }

    /* renamed from: times-VKZWuLQ */
    private static final long m126timesVKZWuLQ(int $this, long other) {
        return ULong.m167constructorimpl(ULong.m167constructorimpl(((long) $this) & 4294967295L) * other);
    }

    /* renamed from: div-7apg3OU */
    private static final int m100div7apg3OU(int $this, byte other) {
        return UnsignedKt.m324uintDivideJ1ME1BU($this, m98constructorimpl(other & 255));
    }

    /* renamed from: div-xj2QHRw */
    private static final int m103divxj2QHRw(int $this, short other) {
        return UnsignedKt.m324uintDivideJ1ME1BU($this, m98constructorimpl(65535 & other));
    }

    /* renamed from: div-WZ4Q5Ns */
    private static final int m102divWZ4Q5Ns(int $this, int other) {
        return UnsignedKt.m324uintDivideJ1ME1BU($this, other);
    }

    /* renamed from: div-VKZWuLQ */
    private static final long m101divVKZWuLQ(int $this, long other) {
        return UnsignedKt.m326ulongDivideeb3DHEI(ULong.m167constructorimpl(((long) $this) & 4294967295L), other);
    }

    /* renamed from: rem-7apg3OU */
    private static final int m119rem7apg3OU(int $this, byte other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU($this, m98constructorimpl(other & 255));
    }

    /* renamed from: rem-xj2QHRw */
    private static final int m122remxj2QHRw(int $this, short other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU($this, m98constructorimpl(65535 & other));
    }

    /* renamed from: rem-WZ4Q5Ns */
    private static final int m121remWZ4Q5Ns(int $this, int other) {
        return UnsignedKt.m325uintRemainderJ1ME1BU($this, other);
    }

    /* renamed from: rem-VKZWuLQ */
    private static final long m120remVKZWuLQ(int $this, long other) {
        return UnsignedKt.m327ulongRemaindereb3DHEI(ULong.m167constructorimpl(((long) $this) & 4294967295L), other);
    }

    /* renamed from: inc-impl */
    private static final int m107incimpl(int $this) {
        return m98constructorimpl($this + 1);
    }

    /* renamed from: dec-impl */
    private static final int m99decimpl(int $this) {
        return m98constructorimpl($this - 1);
    }

    /* renamed from: rangeTo-WZ4Q5Ns */
    private static final UIntRange m118rangeToWZ4Q5Ns(int $this, int other) {
        return new UIntRange($this, other, null);
    }

    /* renamed from: shl-impl */
    private static final int m123shlimpl(int $this, int bitCount) {
        return m98constructorimpl($this << bitCount);
    }

    /* renamed from: shr-impl */
    private static final int m124shrimpl(int $this, int bitCount) {
        return m98constructorimpl($this >>> bitCount);
    }

    /* renamed from: and-WZ4Q5Ns */
    private static final int m91andWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this & other);
    }

    /* renamed from: or-WZ4Q5Ns */
    private static final int m113orWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this | other);
    }

    /* renamed from: xor-WZ4Q5Ns */
    private static final int m140xorWZ4Q5Ns(int $this, int other) {
        return m98constructorimpl($this ^ other);
    }

    /* renamed from: inv-impl */
    private static final int m108invimpl(int $this) {
        return m98constructorimpl(~$this);
    }

    /* renamed from: toByte-impl */
    private static final byte m129toByteimpl(int $this) {
        return (byte) $this;
    }

    /* renamed from: toShort-impl */
    private static final short m134toShortimpl(int $this) {
        return (short) $this;
    }

    /* renamed from: toInt-impl */
    private static final int m132toIntimpl(int $this) {
        return $this;
    }

    /* renamed from: toLong-impl */
    private static final long m133toLongimpl(int $this) {
        return ((long) $this) & 4294967295L;
    }

    /* renamed from: toUByte-impl */
    private static final byte m136toUByteimpl(int $this) {
        return UByte.m31constructorimpl((byte) $this);
    }

    /* renamed from: toUShort-impl */
    private static final short m139toUShortimpl(int $this) {
        return UShort.m264constructorimpl((short) $this);
    }

    /* renamed from: toUInt-impl */
    private static final int m137toUIntimpl(int $this) {
        return $this;
    }

    /* renamed from: toULong-impl */
    private static final long m138toULongimpl(int $this) {
        return ULong.m167constructorimpl(((long) $this) & 4294967295L);
    }

    /* renamed from: toFloat-impl */
    private static final float m131toFloatimpl(int $this) {
        return (float) UnsignedKt.uintToDouble($this);
    }

    /* renamed from: toDouble-impl */
    private static final double m130toDoubleimpl(int $this) {
        return UnsignedKt.uintToDouble($this);
    }

    /* renamed from: toString-impl */
    public static String m135toStringimpl(int $this) {
        return String.valueOf(((long) $this) & 4294967295L);
    }
}
