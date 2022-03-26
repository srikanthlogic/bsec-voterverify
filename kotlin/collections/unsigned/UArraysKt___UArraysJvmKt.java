package kotlin.collections.unsigned;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _UArraysJvm.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0016\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b'\u0010(\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006)"}, d2 = {"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", FirebaseAnalytics.Param.INDEX, "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, pn = "kotlin.collections", xi = 1, xs = "kotlin/collections/unsigned/UArraysKt")
/* loaded from: classes3.dex */
class UArraysKt___UArraysJvmKt {
    /* renamed from: elementAt-qFRl0hI  reason: not valid java name */
    private static final int m378elementAtqFRl0hI(int[] $this$elementAt, int index) {
        return UIntArray.m149getimpl($this$elementAt, index);
    }

    /* renamed from: elementAt-r7IrZao  reason: not valid java name */
    private static final long m379elementAtr7IrZao(long[] $this$elementAt, int index) {
        return ULongArray.m218getimpl($this$elementAt, index);
    }

    /* renamed from: elementAt-PpDY95g  reason: not valid java name */
    private static final byte m376elementAtPpDY95g(byte[] $this$elementAt, int index) {
        return UByteArray.m80getimpl($this$elementAt, index);
    }

    /* renamed from: elementAt-nggk6HY  reason: not valid java name */
    private static final short m377elementAtnggk6HY(short[] $this$elementAt, int index) {
        return UShortArray.m313getimpl($this$elementAt, index);
    }

    /* renamed from: asList--ajY-9A  reason: not valid java name */
    public static final List<UInt> m364asListajY9A(int[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new RandomAccess($this$asList) { // from class: kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$1
            final /* synthetic */ int[] $this_asList;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_asList = $receiver;
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public final /* bridge */ boolean contains(Object obj) {
                if (obj instanceof UInt) {
                    return m380containsWZ4Q5Ns(((UInt) obj).m141unboximpl());
                }
                return false;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int indexOf(Object obj) {
                if (obj instanceof UInt) {
                    return m381indexOfWZ4Q5Ns(((UInt) obj).m141unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int lastIndexOf(Object obj) {
                if (obj instanceof UInt) {
                    return m382lastIndexOfWZ4Q5Ns(((UInt) obj).m141unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
            public int getSize() {
                return UIntArray.m150getSizeimpl(this.$this_asList);
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return UIntArray.m152isEmptyimpl(this.$this_asList);
            }

            /* renamed from: contains-WZ4Q5Ns  reason: not valid java name */
            public boolean m380containsWZ4Q5Ns(int element) {
                return UIntArray.m145containsWZ4Q5Ns(this.$this_asList, element);
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public UInt get(int index) {
                return UInt.m92boximpl(UIntArray.m149getimpl(this.$this_asList, index));
            }

            /* renamed from: indexOf-WZ4Q5Ns  reason: not valid java name */
            public int m381indexOfWZ4Q5Ns(int element) {
                return ArraysKt.indexOf(this.$this_asList, element);
            }

            /* renamed from: lastIndexOf-WZ4Q5Ns  reason: not valid java name */
            public int m382lastIndexOfWZ4Q5Ns(int element) {
                return ArraysKt.lastIndexOf(this.$this_asList, element);
            }
        };
    }

    /* renamed from: asList-QwZRm1k  reason: not valid java name */
    public static final List<ULong> m366asListQwZRm1k(long[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new RandomAccess($this$asList) { // from class: kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$2
            final /* synthetic */ long[] $this_asList;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_asList = $receiver;
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public final /* bridge */ boolean contains(Object obj) {
                if (obj instanceof ULong) {
                    return m383containsVKZWuLQ(((ULong) obj).m210unboximpl());
                }
                return false;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int indexOf(Object obj) {
                if (obj instanceof ULong) {
                    return m384indexOfVKZWuLQ(((ULong) obj).m210unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int lastIndexOf(Object obj) {
                if (obj instanceof ULong) {
                    return m385lastIndexOfVKZWuLQ(((ULong) obj).m210unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
            public int getSize() {
                return ULongArray.m219getSizeimpl(this.$this_asList);
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return ULongArray.m221isEmptyimpl(this.$this_asList);
            }

            /* renamed from: contains-VKZWuLQ  reason: not valid java name */
            public boolean m383containsVKZWuLQ(long element) {
                return ULongArray.m214containsVKZWuLQ(this.$this_asList, element);
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public ULong get(int index) {
                return ULong.m161boximpl(ULongArray.m218getimpl(this.$this_asList, index));
            }

            /* renamed from: indexOf-VKZWuLQ  reason: not valid java name */
            public int m384indexOfVKZWuLQ(long element) {
                return ArraysKt.indexOf(this.$this_asList, element);
            }

            /* renamed from: lastIndexOf-VKZWuLQ  reason: not valid java name */
            public int m385lastIndexOfVKZWuLQ(long element) {
                return ArraysKt.lastIndexOf(this.$this_asList, element);
            }
        };
    }

    /* renamed from: asList-GBYM_sE  reason: not valid java name */
    public static final List<UByte> m365asListGBYM_sE(byte[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new RandomAccess($this$asList) { // from class: kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$3
            final /* synthetic */ byte[] $this_asList;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_asList = $receiver;
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public final /* bridge */ boolean contains(Object obj) {
                if (obj instanceof UByte) {
                    return m386contains7apg3OU(((UByte) obj).m72unboximpl());
                }
                return false;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int indexOf(Object obj) {
                if (obj instanceof UByte) {
                    return m387indexOf7apg3OU(((UByte) obj).m72unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int lastIndexOf(Object obj) {
                if (obj instanceof UByte) {
                    return m388lastIndexOf7apg3OU(((UByte) obj).m72unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
            public int getSize() {
                return UByteArray.m81getSizeimpl(this.$this_asList);
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return UByteArray.m83isEmptyimpl(this.$this_asList);
            }

            /* renamed from: contains-7apg3OU  reason: not valid java name */
            public boolean m386contains7apg3OU(byte element) {
                return UByteArray.m76contains7apg3OU(this.$this_asList, element);
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public UByte get(int index) {
                return UByte.m25boximpl(UByteArray.m80getimpl(this.$this_asList, index));
            }

            /* renamed from: indexOf-7apg3OU  reason: not valid java name */
            public int m387indexOf7apg3OU(byte element) {
                return ArraysKt.indexOf(this.$this_asList, element);
            }

            /* renamed from: lastIndexOf-7apg3OU  reason: not valid java name */
            public int m388lastIndexOf7apg3OU(byte element) {
                return ArraysKt.lastIndexOf(this.$this_asList, element);
            }
        };
    }

    /* renamed from: asList-rL5Bavg  reason: not valid java name */
    public static final List<UShort> m367asListrL5Bavg(short[] $this$asList) {
        Intrinsics.checkParameterIsNotNull($this$asList, "$this$asList");
        return new RandomAccess($this$asList) { // from class: kotlin.collections.unsigned.UArraysKt___UArraysJvmKt$asList$4
            final /* synthetic */ short[] $this_asList;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_asList = $receiver;
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public final /* bridge */ boolean contains(Object obj) {
                if (obj instanceof UShort) {
                    return m389containsxj2QHRw(((UShort) obj).m305unboximpl());
                }
                return false;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int indexOf(Object obj) {
                if (obj instanceof UShort) {
                    return m390indexOfxj2QHRw(((UShort) obj).m305unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public final /* bridge */ int lastIndexOf(Object obj) {
                if (obj instanceof UShort) {
                    return m391lastIndexOfxj2QHRw(((UShort) obj).m305unboximpl());
                }
                return -1;
            }

            @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
            public int getSize() {
                return UShortArray.m314getSizeimpl(this.$this_asList);
            }

            @Override // kotlin.collections.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return UShortArray.m316isEmptyimpl(this.$this_asList);
            }

            /* renamed from: contains-xj2QHRw  reason: not valid java name */
            public boolean m389containsxj2QHRw(short element) {
                return UShortArray.m309containsxj2QHRw(this.$this_asList, element);
            }

            @Override // kotlin.collections.AbstractList, java.util.List
            public UShort get(int index) {
                return UShort.m258boximpl(UShortArray.m313getimpl(this.$this_asList, index));
            }

            /* renamed from: indexOf-xj2QHRw  reason: not valid java name */
            public int m390indexOfxj2QHRw(short element) {
                return ArraysKt.indexOf(this.$this_asList, element);
            }

            /* renamed from: lastIndexOf-xj2QHRw  reason: not valid java name */
            public int m391lastIndexOfxj2QHRw(short element) {
                return ArraysKt.lastIndexOf(this.$this_asList, element);
            }
        };
    }

    /* renamed from: binarySearch-2fe2U9s$default  reason: not valid java name */
    public static /* synthetic */ int m369binarySearch2fe2U9s$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = UIntArray.m150getSizeimpl(iArr);
        }
        return UArraysKt.m368binarySearch2fe2U9s(iArr, i, i2, i3);
    }

    /* renamed from: binarySearch-2fe2U9s  reason: not valid java name */
    public static final int m368binarySearch2fe2U9s(int[] $this$binarySearch, int element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UIntArray.m150getSizeimpl($this$binarySearch));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = UnsignedKt.uintCompare($this$binarySearch[mid], element);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    /* renamed from: binarySearch-K6DWlUc$default  reason: not valid java name */
    public static /* synthetic */ int m373binarySearchK6DWlUc$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = ULongArray.m219getSizeimpl(jArr);
        }
        return UArraysKt.m372binarySearchK6DWlUc(jArr, j, i, i2);
    }

    /* renamed from: binarySearch-K6DWlUc  reason: not valid java name */
    public static final int m372binarySearchK6DWlUc(long[] $this$binarySearch, long element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, ULongArray.m219getSizeimpl($this$binarySearch));
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = UnsignedKt.ulongCompare($this$binarySearch[mid], element);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    /* renamed from: binarySearch-WpHrYlw$default  reason: not valid java name */
    public static /* synthetic */ int m375binarySearchWpHrYlw$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UByteArray.m81getSizeimpl(bArr);
        }
        return UArraysKt.m374binarySearchWpHrYlw(bArr, b, i, i2);
    }

    /* renamed from: binarySearch-WpHrYlw  reason: not valid java name */
    public static final int m374binarySearchWpHrYlw(byte[] $this$binarySearch, byte element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UByteArray.m81getSizeimpl($this$binarySearch));
        int signedElement = element & 255;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = UnsignedKt.uintCompare($this$binarySearch[mid], signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    /* renamed from: binarySearch-EtDCXyQ$default  reason: not valid java name */
    public static /* synthetic */ int m371binarySearchEtDCXyQ$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UShortArray.m314getSizeimpl(sArr);
        }
        return UArraysKt.m370binarySearchEtDCXyQ(sArr, s, i, i2);
    }

    /* renamed from: binarySearch-EtDCXyQ  reason: not valid java name */
    public static final int m370binarySearchEtDCXyQ(short[] $this$binarySearch, short element, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UShortArray.m314getSizeimpl($this$binarySearch));
        int signedElement = 65535 & element;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = UnsignedKt.uintCompare($this$binarySearch[mid], signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        return -(low + 1);
    }
}
