package kotlin.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
/* compiled from: UArraysKt.kt */
@Deprecated(level = DeprecationLevel.HIDDEN, message = "Provided for binary compatibility")
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\t\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eJ\u001f\u0010\u0003\u001a\u00020\u0004*\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\u0004ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\u0016\u0010\u0012\u001a\u00020\u0013*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b \u0010!J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b\"\u0010#J\u0016\u0010\u001c\u001a\u00020\u001d*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b$\u0010%J\u001e\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b)\u0010*J\u001e\u0010&\u001a\u00020+*\u00020\t2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b,\u0010-J\u001e\u0010&\u001a\u00020.*\u00020\f2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u00100J\u001e\u0010&\u001a\u000201*\u00020\u000f2\u0006\u0010&\u001a\u00020(H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u00103J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020'05*\u00020\u0005H\u0007ø\u0001\u0000¢\u0006\u0004\b6\u00107J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020+05*\u00020\tH\u0007ø\u0001\u0000¢\u0006\u0004\b8\u00109J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020.05*\u00020\fH\u0007ø\u0001\u0000¢\u0006\u0004\b:\u0010;J\u001c\u00104\u001a\b\u0012\u0004\u0012\u00020105*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b<\u0010=\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006>"}, d2 = {"Lkotlin/collections/UArraysKt;", "", "()V", "contentEquals", "", "Lkotlin/UByteArray;", "other", "contentEquals-kdPth3s", "([B[B)Z", "Lkotlin/UIntArray;", "contentEquals-ctEhBpI", "([I[I)Z", "Lkotlin/ULongArray;", "contentEquals-us8wMrg", "([J[J)Z", "Lkotlin/UShortArray;", "contentEquals-mazbYpA", "([S[S)Z", "contentHashCode", "", "contentHashCode-GBYM_sE", "([B)I", "contentHashCode--ajY-9A", "([I)I", "contentHashCode-QwZRm1k", "([J)I", "contentHashCode-rL5Bavg", "([S)I", "contentToString", "", "contentToString-GBYM_sE", "([B)Ljava/lang/String;", "contentToString--ajY-9A", "([I)Ljava/lang/String;", "contentToString-QwZRm1k", "([J)Ljava/lang/String;", "contentToString-rL5Bavg", "([S)Ljava/lang/String;", "random", "Lkotlin/UByte;", "Lkotlin/random/Random;", "random-oSF2wD8", "([BLkotlin/random/Random;)B", "Lkotlin/UInt;", "random-2D5oskM", "([ILkotlin/random/Random;)I", "Lkotlin/ULong;", "random-JzugnMA", "([JLkotlin/random/Random;)J", "Lkotlin/UShort;", "random-s5X_as8", "([SLkotlin/random/Random;)S", "toTypedArray", "", "toTypedArray-GBYM_sE", "([B)[Lkotlin/UByte;", "toTypedArray--ajY-9A", "([I)[Lkotlin/UInt;", "toTypedArray-QwZRm1k", "([J)[Lkotlin/ULong;", "toTypedArray-rL5Bavg", "([S)[Lkotlin/UShort;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class UArraysKt {
    public static final UArraysKt INSTANCE = new UArraysKt();

    private UArraysKt() {
    }

    @JvmStatic
    /* renamed from: random-2D5oskM  reason: not valid java name */
    public static final int m356random2D5oskM(int[] $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UIntArray.m152isEmptyimpl($this$random)) {
            return UIntArray.m149getimpl($this$random, random.nextInt(UIntArray.m150getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @JvmStatic
    /* renamed from: random-JzugnMA  reason: not valid java name */
    public static final long m357randomJzugnMA(long[] $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!ULongArray.m221isEmptyimpl($this$random)) {
            return ULongArray.m218getimpl($this$random, random.nextInt(ULongArray.m219getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @JvmStatic
    /* renamed from: random-oSF2wD8  reason: not valid java name */
    public static final byte m358randomoSF2wD8(byte[] $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UByteArray.m83isEmptyimpl($this$random)) {
            return UByteArray.m80getimpl($this$random, random.nextInt(UByteArray.m81getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @JvmStatic
    /* renamed from: random-s5X_as8  reason: not valid java name */
    public static final short m359randoms5X_as8(short[] $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!UShortArray.m316isEmptyimpl($this$random)) {
            return UShortArray.m313getimpl($this$random, random.nextInt(UShortArray.m314getSizeimpl($this$random)));
        }
        throw new NoSuchElementException("Array is empty.");
    }

    @JvmStatic
    /* renamed from: contentEquals-ctEhBpI  reason: not valid java name */
    public static final boolean m344contentEqualsctEhBpI(int[] $this$contentEquals, int[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @JvmStatic
    /* renamed from: contentEquals-us8wMrg  reason: not valid java name */
    public static final boolean m347contentEqualsus8wMrg(long[] $this$contentEquals, long[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @JvmStatic
    /* renamed from: contentEquals-kdPth3s  reason: not valid java name */
    public static final boolean m345contentEqualskdPth3s(byte[] $this$contentEquals, byte[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @JvmStatic
    /* renamed from: contentEquals-mazbYpA  reason: not valid java name */
    public static final boolean m346contentEqualsmazbYpA(short[] $this$contentEquals, short[] other) {
        Intrinsics.checkParameterIsNotNull($this$contentEquals, "$this$contentEquals");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Arrays.equals($this$contentEquals, other);
    }

    @JvmStatic
    /* renamed from: contentHashCode--ajY-9A  reason: not valid java name */
    public static final int m348contentHashCodeajY9A(int[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @JvmStatic
    /* renamed from: contentHashCode-QwZRm1k  reason: not valid java name */
    public static final int m350contentHashCodeQwZRm1k(long[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @JvmStatic
    /* renamed from: contentHashCode-GBYM_sE  reason: not valid java name */
    public static final int m349contentHashCodeGBYM_sE(byte[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @JvmStatic
    /* renamed from: contentHashCode-rL5Bavg  reason: not valid java name */
    public static final int m351contentHashCoderL5Bavg(short[] $this$contentHashCode) {
        Intrinsics.checkParameterIsNotNull($this$contentHashCode, "$this$contentHashCode");
        return Arrays.hashCode($this$contentHashCode);
    }

    @JvmStatic
    /* renamed from: contentToString--ajY-9A  reason: not valid java name */
    public static final String m352contentToStringajY9A(int[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    /* renamed from: contentToString-QwZRm1k  reason: not valid java name */
    public static final String m354contentToStringQwZRm1k(long[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    /* renamed from: contentToString-GBYM_sE  reason: not valid java name */
    public static final String m353contentToStringGBYM_sE(byte[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    /* renamed from: contentToString-rL5Bavg  reason: not valid java name */
    public static final String m355contentToStringrL5Bavg(short[] $this$contentToString) {
        Intrinsics.checkParameterIsNotNull($this$contentToString, "$this$contentToString");
        return CollectionsKt.joinToString$default(Intrinsics.checkParameterIsNotNull($this$contentToString, "v"), ", ", "[", "]", 0, null, null, 56, null);
    }

    @JvmStatic
    /* renamed from: toTypedArray--ajY-9A  reason: not valid java name */
    public static final UInt[] m360toTypedArrayajY9A(int[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r0 = UIntArray.m150getSizeimpl($this$toTypedArray);
        UInt[] uIntArr = new UInt[r0];
        for (int index = 0; index < r0; index++) {
            uIntArr[index] = UInt.m92boximpl(UIntArray.m149getimpl($this$toTypedArray, index));
        }
        return uIntArr;
    }

    @JvmStatic
    /* renamed from: toTypedArray-QwZRm1k  reason: not valid java name */
    public static final ULong[] m362toTypedArrayQwZRm1k(long[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r0 = ULongArray.m219getSizeimpl($this$toTypedArray);
        ULong[] uLongArr = new ULong[r0];
        for (int index = 0; index < r0; index++) {
            uLongArr[index] = ULong.m161boximpl(ULongArray.m218getimpl($this$toTypedArray, index));
        }
        return uLongArr;
    }

    @JvmStatic
    /* renamed from: toTypedArray-GBYM_sE  reason: not valid java name */
    public static final UByte[] m361toTypedArrayGBYM_sE(byte[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r0 = UByteArray.m81getSizeimpl($this$toTypedArray);
        UByte[] uByteArr = new UByte[r0];
        for (int index = 0; index < r0; index++) {
            uByteArr[index] = UByte.m25boximpl(UByteArray.m80getimpl($this$toTypedArray, index));
        }
        return uByteArr;
    }

    @JvmStatic
    /* renamed from: toTypedArray-rL5Bavg  reason: not valid java name */
    public static final UShort[] m363toTypedArrayrL5Bavg(short[] $this$toTypedArray) {
        Intrinsics.checkParameterIsNotNull($this$toTypedArray, "$this$toTypedArray");
        int r0 = UShortArray.m314getSizeimpl($this$toTypedArray);
        UShort[] uShortArr = new UShort[r0];
        for (int index = 0; index < r0; index++) {
            uShortArr[index] = UShort.m258boximpl(UShortArray.m313getimpl($this$toTypedArray, index));
        }
        return uShortArr;
    }
}
