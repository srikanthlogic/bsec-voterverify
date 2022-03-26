package kotlin.collections;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _UCollections.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/collections/UCollectionsKt")
/* loaded from: classes3.dex */
class UCollectionsKt___UCollectionsKt {
    public static final byte[] toUByteArray(Collection<UByte> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "$this$toUByteArray");
        byte[] result = UByteArray.m74constructorimpl(collection.size());
        int index = 0;
        for (UByte uByte : collection) {
            UByteArray.m85setVurrAj0(result, index, uByte.m72unboximpl());
            index++;
        }
        return result;
    }

    public static final int[] toUIntArray(Collection<UInt> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "$this$toUIntArray");
        int[] result = UIntArray.m143constructorimpl(collection.size());
        int index = 0;
        for (UInt uInt : collection) {
            UIntArray.m154setVXSXFK8(result, index, uInt.m141unboximpl());
            index++;
        }
        return result;
    }

    public static final long[] toULongArray(Collection<ULong> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "$this$toULongArray");
        long[] result = ULongArray.m212constructorimpl(collection.size());
        int index = 0;
        for (ULong uLong : collection) {
            ULongArray.m223setk8EXiF4(result, index, uLong.m210unboximpl());
            index++;
        }
        return result;
    }

    public static final short[] toUShortArray(Collection<UShort> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "$this$toUShortArray");
        short[] result = UShortArray.m307constructorimpl(collection.size());
        int index = 0;
        for (UShort uShort : collection) {
            UShortArray.m318set01HTLdE(result, index, uShort.m305unboximpl());
            index++;
        }
        return result;
    }

    public static final int sumOfUInt(Iterable<UInt> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$this$sum");
        int sum = 0;
        for (UInt uInt : iterable) {
            sum = UInt.m98constructorimpl(sum + uInt.m141unboximpl());
        }
        return sum;
    }

    public static final long sumOfULong(Iterable<ULong> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$this$sum");
        long sum = 0;
        for (ULong uLong : iterable) {
            sum = ULong.m167constructorimpl(sum + uLong.m210unboximpl());
        }
        return sum;
    }

    public static final int sumOfUByte(Iterable<UByte> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$this$sum");
        int sum = 0;
        for (UByte uByte : iterable) {
            sum = UInt.m98constructorimpl(UInt.m98constructorimpl(uByte.m72unboximpl() & 255) + sum);
        }
        return sum;
    }

    public static final int sumOfUShort(Iterable<UShort> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$this$sum");
        int sum = 0;
        for (UShort uShort : iterable) {
            sum = UInt.m98constructorimpl(UInt.m98constructorimpl(65535 & uShort.m305unboximpl()) + sum);
        }
        return sum;
    }
}
