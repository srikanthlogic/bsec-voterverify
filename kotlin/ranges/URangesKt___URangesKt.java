package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.URandomKt;
import okhttp3.internal.ws.WebSocketProtocol;
import org.apache.commons.io.FilenameUtils;
/* compiled from: _URanges.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u001e\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\u001e\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\u001e\u0010\u0000\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001e\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0004\u001a\u001e\u0010\u000e\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0007\u001a\u001e\u0010\u000e\u001a\u00020\b*\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\n\u001a\u001e\u0010\u000e\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\r\u001a&\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a&\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a$\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a&\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a$\u0010\u0014\u001a\u00020\b*\u00020\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001aH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010 \u001a&\u0010\u0014\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\b\u0010)\u001a\u0004\u0018\u00010\u0005H\u0087\nø\u0001\u0000¢\u0006\u0002\b*\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b+\u0010,\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b-\u0010.\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b0\u00101\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0005H\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b2\u00103\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\b\u0010)\u001a\u0004\u0018\u00010\bH\u0087\nø\u0001\u0000¢\u0006\u0002\b4\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002ø\u0001\u0000¢\u0006\u0004\b5\u00106\u001a\u001f\u00107\u001a\u000208*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b:\u0010;\u001a\u001f\u00107\u001a\u000208*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b<\u0010=\u001a\u001f\u00107\u001a\u00020>*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b?\u0010@\u001a\u001f\u00107\u001a\u000208*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\bA\u0010B\u001a\u0015\u0010C\u001a\u00020\u0005*\u00020%H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010D\u001a\u001c\u0010C\u001a\u00020\u0005*\u00020%2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000¢\u0006\u0002\u0010F\u001a\u0015\u0010C\u001a\u00020\b*\u00020/H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010G\u001a\u001c\u0010C\u001a\u00020\b*\u00020/2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000¢\u0006\u0002\u0010H\u001a\u0012\u0010I\u001a\u0004\u0018\u00010\u0005*\u00020%H\u0087\bø\u0001\u0000\u001a\u0019\u0010I\u001a\u0004\u0018\u00010\u0005*\u00020%2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000\u001a\u0012\u0010I\u001a\u0004\u0018\u00010\b*\u00020/H\u0087\bø\u0001\u0000\u001a\u0019\u0010I\u001a\u0004\u0018\u00010\b*\u00020/2\u0006\u0010C\u001a\u00020EH\u0007ø\u0001\u0000\u001a\f\u0010J\u001a\u000208*\u000208H\u0007\u001a\f\u0010J\u001a\u00020>*\u00020>H\u0007\u001a\u0015\u0010K\u001a\u000208*\u0002082\u0006\u0010K\u001a\u00020LH\u0087\u0004\u001a\u0015\u0010K\u001a\u00020>*\u00020>2\u0006\u0010K\u001a\u00020MH\u0087\u0004\u001a\u001f\u0010N\u001a\u00020%*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004ø\u0001\u0000¢\u0006\u0004\bO\u0010P\u001a\u001f\u0010N\u001a\u00020%*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004ø\u0001\u0000¢\u0006\u0004\bQ\u0010R\u001a\u001f\u0010N\u001a\u00020/*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\bS\u0010T\u001a\u001f\u0010N\u001a\u00020%*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\bU\u0010V\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006W"}, d2 = {"coerceAtLeast", "Lkotlin/UByte;", "minimumValue", "coerceAtLeast-Kr8caGY", "(BB)B", "Lkotlin/UInt;", "coerceAtLeast-J1ME1BU", "(II)I", "Lkotlin/ULong;", "coerceAtLeast-eb3DHEI", "(JJ)J", "Lkotlin/UShort;", "coerceAtLeast-5PvTz6A", "(SS)S", "coerceAtMost", "maximumValue", "coerceAtMost-Kr8caGY", "coerceAtMost-J1ME1BU", "coerceAtMost-eb3DHEI", "coerceAtMost-5PvTz6A", "coerceIn", "coerceIn-b33U2AM", "(BBB)B", "coerceIn-WZ9TVnA", "(III)I", "range", "Lkotlin/ranges/ClosedRange;", "coerceIn-wuiCnnA", "(ILkotlin/ranges/ClosedRange;)I", "coerceIn-sambcqE", "(JJJ)J", "coerceIn-JPwROB0", "(JLkotlin/ranges/ClosedRange;)J", "coerceIn-VKSA0NQ", "(SSS)S", "contains", "", "Lkotlin/ranges/UIntRange;", "value", "contains-68kG9v0", "(Lkotlin/ranges/UIntRange;B)Z", "element", "contains-biwQdVI", "contains-fz5IDCE", "(Lkotlin/ranges/UIntRange;J)Z", "contains-ZsK3CEQ", "(Lkotlin/ranges/UIntRange;S)Z", "Lkotlin/ranges/ULongRange;", "contains-ULb-yJY", "(Lkotlin/ranges/ULongRange;B)Z", "contains-Gab390E", "(Lkotlin/ranges/ULongRange;I)Z", "contains-GYNo2lE", "contains-uhHAxoY", "(Lkotlin/ranges/ULongRange;S)Z", "downTo", "Lkotlin/ranges/UIntProgression;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "randomOrNull", "reversed", "step", "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/ranges/URangesKt")
/* loaded from: classes3.dex */
class URangesKt___URangesKt {
    private static final int random(UIntRange $this$random) {
        return URangesKt.random($this$random, Random.Default);
    }

    private static final long random(ULongRange $this$random) {
        return URangesKt.random($this$random, Random.Default);
    }

    public static final int random(UIntRange $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextUInt(random, $this$random);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    public static final long random(ULongRange $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        try {
            return URandomKt.nextULong(random, $this$random);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    private static final UInt randomOrNull(UIntRange $this$randomOrNull) {
        return URangesKt.randomOrNull($this$randomOrNull, Random.Default);
    }

    private static final ULong randomOrNull(ULongRange $this$randomOrNull) {
        return URangesKt.randomOrNull($this$randomOrNull, Random.Default);
    }

    public static final UInt randomOrNull(UIntRange $this$randomOrNull, Random random) {
        Intrinsics.checkParameterIsNotNull($this$randomOrNull, "$this$randomOrNull");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return UInt.m92boximpl(URandomKt.nextUInt(random, $this$randomOrNull));
    }

    public static final ULong randomOrNull(ULongRange $this$randomOrNull, Random random) {
        Intrinsics.checkParameterIsNotNull($this$randomOrNull, "$this$randomOrNull");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return ULong.m161boximpl(URandomKt.nextULong(random, $this$randomOrNull));
    }

    /* renamed from: contains-biwQdVI  reason: not valid java name */
    private static final boolean m944containsbiwQdVI(UIntRange $this$contains, UInt element) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return element != null && $this$contains.m922containsWZ4Q5Ns(element.m141unboximpl());
    }

    /* renamed from: contains-GYNo2lE  reason: not valid java name */
    private static final boolean m940containsGYNo2lE(ULongRange $this$contains, ULong element) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return element != null && $this$contains.m924containsVKZWuLQ(element.m210unboximpl());
    }

    /* renamed from: contains-68kG9v0  reason: not valid java name */
    public static final boolean m939contains68kG9v0(UIntRange $this$contains, byte value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m922containsWZ4Q5Ns(UInt.m98constructorimpl(value & 255));
    }

    /* renamed from: contains-ULb-yJY  reason: not valid java name */
    public static final boolean m942containsULbyJY(ULongRange $this$contains, byte value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m924containsVKZWuLQ(ULong.m167constructorimpl(((long) value) & 255));
    }

    /* renamed from: contains-Gab390E  reason: not valid java name */
    public static final boolean m941containsGab390E(ULongRange $this$contains, int value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m924containsVKZWuLQ(ULong.m167constructorimpl(((long) value) & 4294967295L));
    }

    /* renamed from: contains-fz5IDCE  reason: not valid java name */
    public static final boolean m945containsfz5IDCE(UIntRange $this$contains, long value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return ULong.m167constructorimpl(value >>> 32) == 0 && $this$contains.m922containsWZ4Q5Ns(UInt.m98constructorimpl((int) value));
    }

    /* renamed from: contains-ZsK3CEQ  reason: not valid java name */
    public static final boolean m943containsZsK3CEQ(UIntRange $this$contains, short value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m922containsWZ4Q5Ns(UInt.m98constructorimpl(65535 & value));
    }

    /* renamed from: contains-uhHAxoY  reason: not valid java name */
    public static final boolean m946containsuhHAxoY(ULongRange $this$contains, short value) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return $this$contains.m924containsVKZWuLQ(ULong.m167constructorimpl(((long) value) & WebSocketProtocol.PAYLOAD_SHORT_MAX));
    }

    /* renamed from: downTo-Kr8caGY  reason: not valid java name */
    public static final UIntProgression m949downToKr8caGY(byte $this$downTo, byte to) {
        return UIntProgression.Companion.m921fromClosedRangeNkh28Cs(UInt.m98constructorimpl($this$downTo & 255), UInt.m98constructorimpl(to & 255), -1);
    }

    /* renamed from: downTo-J1ME1BU  reason: not valid java name */
    public static final UIntProgression m948downToJ1ME1BU(int $this$downTo, int to) {
        return UIntProgression.Companion.m921fromClosedRangeNkh28Cs($this$downTo, to, -1);
    }

    /* renamed from: downTo-eb3DHEI  reason: not valid java name */
    public static final ULongProgression m950downToeb3DHEI(long $this$downTo, long to) {
        return ULongProgression.Companion.m923fromClosedRange7ftBX0g($this$downTo, to, -1);
    }

    /* renamed from: downTo-5PvTz6A  reason: not valid java name */
    public static final UIntProgression m947downTo5PvTz6A(short $this$downTo, short to) {
        return UIntProgression.Companion.m921fromClosedRangeNkh28Cs(UInt.m98constructorimpl($this$downTo & UShort.MAX_VALUE), UInt.m98constructorimpl(65535 & to), -1);
    }

    public static final UIntProgression reversed(UIntProgression $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        return UIntProgression.Companion.m921fromClosedRangeNkh28Cs($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final ULongProgression reversed(ULongProgression $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        return ULongProgression.Companion.m923fromClosedRange7ftBX0g($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final UIntProgression step(UIntProgression $this$step, int step) {
        Intrinsics.checkParameterIsNotNull($this$step, "$this$step");
        RangesKt.checkStepIsPositive(step > 0, Integer.valueOf(step));
        return UIntProgression.Companion.m921fromClosedRangeNkh28Cs($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final ULongProgression step(ULongProgression $this$step, long step) {
        Intrinsics.checkParameterIsNotNull($this$step, "$this$step");
        RangesKt.checkStepIsPositive(step > 0, Long.valueOf(step));
        return ULongProgression.Companion.m923fromClosedRange7ftBX0g($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    /* renamed from: until-Kr8caGY  reason: not valid java name */
    public static final UIntRange m953untilKr8caGY(byte $this$until, byte to) {
        if (Intrinsics.compare(to & 255, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.m98constructorimpl($this$until & 255), UInt.m98constructorimpl(UInt.m98constructorimpl(to & 255) - 1), null);
    }

    /* renamed from: until-J1ME1BU  reason: not valid java name */
    public static final UIntRange m952untilJ1ME1BU(int $this$until, int to) {
        if (UnsignedKt.uintCompare(to, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange($this$until, UInt.m98constructorimpl(to - 1), null);
    }

    /* renamed from: until-eb3DHEI  reason: not valid java name */
    public static final ULongRange m954untileb3DHEI(long $this$until, long to) {
        if (UnsignedKt.ulongCompare(to, 0) <= 0) {
            return ULongRange.Companion.getEMPTY();
        }
        return new ULongRange($this$until, ULong.m167constructorimpl(to - ULong.m167constructorimpl(((long) 1) & 4294967295L)), null);
    }

    /* renamed from: until-5PvTz6A  reason: not valid java name */
    public static final UIntRange m951until5PvTz6A(short $this$until, short to) {
        if (Intrinsics.compare(to & UShort.MAX_VALUE, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.m98constructorimpl($this$until & UShort.MAX_VALUE), UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & to) - 1), null);
    }

    /* renamed from: coerceAtLeast-J1ME1BU  reason: not valid java name */
    public static final int m926coerceAtLeastJ1ME1BU(int $this$coerceAtLeast, int minimumValue) {
        return UnsignedKt.uintCompare($this$coerceAtLeast, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    /* renamed from: coerceAtLeast-eb3DHEI  reason: not valid java name */
    public static final long m928coerceAtLeasteb3DHEI(long $this$coerceAtLeast, long minimumValue) {
        return UnsignedKt.ulongCompare($this$coerceAtLeast, minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    /* renamed from: coerceAtLeast-Kr8caGY  reason: not valid java name */
    public static final byte m927coerceAtLeastKr8caGY(byte $this$coerceAtLeast, byte minimumValue) {
        return Intrinsics.compare($this$coerceAtLeast & 255, minimumValue & 255) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    /* renamed from: coerceAtLeast-5PvTz6A  reason: not valid java name */
    public static final short m925coerceAtLeast5PvTz6A(short $this$coerceAtLeast, short minimumValue) {
        return Intrinsics.compare($this$coerceAtLeast & UShort.MAX_VALUE, 65535 & minimumValue) < 0 ? minimumValue : $this$coerceAtLeast;
    }

    /* renamed from: coerceAtMost-J1ME1BU  reason: not valid java name */
    public static final int m930coerceAtMostJ1ME1BU(int $this$coerceAtMost, int maximumValue) {
        return UnsignedKt.uintCompare($this$coerceAtMost, maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    /* renamed from: coerceAtMost-eb3DHEI  reason: not valid java name */
    public static final long m932coerceAtMosteb3DHEI(long $this$coerceAtMost, long maximumValue) {
        return UnsignedKt.ulongCompare($this$coerceAtMost, maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    /* renamed from: coerceAtMost-Kr8caGY  reason: not valid java name */
    public static final byte m931coerceAtMostKr8caGY(byte $this$coerceAtMost, byte maximumValue) {
        return Intrinsics.compare($this$coerceAtMost & 255, maximumValue & 255) > 0 ? maximumValue : $this$coerceAtMost;
    }

    /* renamed from: coerceAtMost-5PvTz6A  reason: not valid java name */
    public static final short m929coerceAtMost5PvTz6A(short $this$coerceAtMost, short maximumValue) {
        return Intrinsics.compare($this$coerceAtMost & UShort.MAX_VALUE, 65535 & maximumValue) > 0 ? maximumValue : $this$coerceAtMost;
    }

    /* renamed from: coerceIn-WZ9TVnA  reason: not valid java name */
    public static final int m935coerceInWZ9TVnA(int $this$coerceIn, int minimumValue, int maximumValue) {
        if (UnsignedKt.uintCompare(minimumValue, maximumValue) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UInt.m135toStringimpl(maximumValue) + " is less than minimum " + UInt.m135toStringimpl(minimumValue) + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (UnsignedKt.uintCompare($this$coerceIn, minimumValue) < 0) {
            return minimumValue;
        } else {
            if (UnsignedKt.uintCompare($this$coerceIn, maximumValue) > 0) {
                return maximumValue;
            }
            return $this$coerceIn;
        }
    }

    /* renamed from: coerceIn-sambcqE  reason: not valid java name */
    public static final long m937coerceInsambcqE(long $this$coerceIn, long minimumValue, long maximumValue) {
        if (UnsignedKt.ulongCompare(minimumValue, maximumValue) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ULong.m204toStringimpl(maximumValue) + " is less than minimum " + ULong.m204toStringimpl(minimumValue) + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (UnsignedKt.ulongCompare($this$coerceIn, minimumValue) < 0) {
            return minimumValue;
        } else {
            if (UnsignedKt.ulongCompare($this$coerceIn, maximumValue) > 0) {
                return maximumValue;
            }
            return $this$coerceIn;
        }
    }

    /* renamed from: coerceIn-b33U2AM  reason: not valid java name */
    public static final byte m936coerceInb33U2AM(byte $this$coerceIn, byte minimumValue, byte maximumValue) {
        if (Intrinsics.compare(minimumValue & 255, maximumValue & 255) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UByte.m66toStringimpl(maximumValue) + " is less than minimum " + UByte.m66toStringimpl(minimumValue) + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (Intrinsics.compare($this$coerceIn & 255, minimumValue & 255) < 0) {
            return minimumValue;
        } else {
            if (Intrinsics.compare($this$coerceIn & 255, maximumValue & 255) > 0) {
                return maximumValue;
            }
            return $this$coerceIn;
        }
    }

    /* renamed from: coerceIn-VKSA0NQ  reason: not valid java name */
    public static final short m934coerceInVKSA0NQ(short $this$coerceIn, short minimumValue, short maximumValue) {
        if (Intrinsics.compare(minimumValue & UShort.MAX_VALUE, maximumValue & UShort.MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + UShort.m299toStringimpl(maximumValue) + " is less than minimum " + UShort.m299toStringimpl(minimumValue) + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (Intrinsics.compare($this$coerceIn & UShort.MAX_VALUE, minimumValue & UShort.MAX_VALUE) < 0) {
            return minimumValue;
        } else {
            if (Intrinsics.compare($this$coerceIn & UShort.MAX_VALUE, 65535 & maximumValue) > 0) {
                return maximumValue;
            }
            return $this$coerceIn;
        }
    }

    /* renamed from: coerceIn-wuiCnnA  reason: not valid java name */
    public static final int m938coerceInwuiCnnA(int $this$coerceIn, ClosedRange<UInt> closedRange) {
        Intrinsics.checkParameterIsNotNull(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((UInt) RangesKt.coerceIn(UInt.m92boximpl($this$coerceIn), (ClosedFloatingPointRange<UInt>) ((ClosedFloatingPointRange) closedRange))).m141unboximpl();
        }
        if (closedRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (UnsignedKt.uintCompare($this$coerceIn, closedRange.getStart().m141unboximpl()) < 0) {
            return closedRange.getStart().m141unboximpl();
        } else {
            if (UnsignedKt.uintCompare($this$coerceIn, closedRange.getEndInclusive().m141unboximpl()) > 0) {
                return closedRange.getEndInclusive().m141unboximpl();
            }
            return $this$coerceIn;
        }
    }

    /* renamed from: coerceIn-JPwROB0  reason: not valid java name */
    public static final long m933coerceInJPwROB0(long $this$coerceIn, ClosedRange<ULong> closedRange) {
        Intrinsics.checkParameterIsNotNull(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((ULong) RangesKt.coerceIn(ULong.m161boximpl($this$coerceIn), (ClosedFloatingPointRange<ULong>) ((ClosedFloatingPointRange) closedRange))).m210unboximpl();
        }
        if (closedRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + FilenameUtils.EXTENSION_SEPARATOR);
        } else if (UnsignedKt.ulongCompare($this$coerceIn, closedRange.getStart().m210unboximpl()) < 0) {
            return closedRange.getStart().m210unboximpl();
        } else {
            if (UnsignedKt.ulongCompare($this$coerceIn, closedRange.getEndInclusive().m210unboximpl()) > 0) {
                return closedRange.getEndInclusive().m210unboximpl();
            }
            return $this$coerceIn;
        }
    }
}
