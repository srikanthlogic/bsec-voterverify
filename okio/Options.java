package okio;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Options.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004:\u0001\u0015B\u001f\b\u0002\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0011\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u000eH\u0096\u0002R\u001e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006X\u0080\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0016"}, d2 = {"Lokio/Options;", "Lkotlin/collections/AbstractList;", "Lokio/ByteString;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "byteStrings", "", "trie", "", "([Lokio/ByteString;[I)V", "getByteStrings$okio", "()[Lokio/ByteString;", "[Lokio/ByteString;", "size", "", "getSize", "()I", "getTrie$okio", "()[I", "get", FirebaseAnalytics.Param.INDEX, "Companion", "okio"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    public static final Companion Companion = new Companion(null);
    private final ByteString[] byteStrings;
    private final int[] trie;

    @JvmStatic
    public static final Options of(ByteString... byteStringArr) {
        return Companion.of(byteStringArr);
    }

    private Options(ByteString[] byteStrings, int[] trie) {
        this.byteStrings = byteStrings;
        this.trie = trie;
    }

    public /* synthetic */ Options(ByteString[] byteStrings, int[] trie, DefaultConstructorMarker $constructor_marker) {
        this(byteStrings, trie);
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof ByteString) {
            return contains((ByteString) obj);
        }
        return false;
    }

    public /* bridge */ boolean contains(ByteString byteString) {
        return super.contains((Object) byteString);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof ByteString) {
            return indexOf((ByteString) obj);
        }
        return -1;
    }

    public /* bridge */ int indexOf(ByteString byteString) {
        return super.indexOf((Object) byteString);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof ByteString) {
            return lastIndexOf((ByteString) obj);
        }
        return -1;
    }

    public /* bridge */ int lastIndexOf(ByteString byteString) {
        return super.lastIndexOf((Object) byteString);
    }

    public final ByteString[] getByteStrings$okio() {
        return this.byteStrings;
    }

    public final int[] getTrie$okio() {
        return this.trie;
    }

    @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
    public int getSize() {
        return this.byteStrings.length;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public ByteString get(int index) {
        return this.byteStrings[index];
    }

    /* compiled from: Options.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JT\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\r2\b\b\u0002\u0010\u0012\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002J!\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00100\u0016\"\u00020\u0010H\u0007¢\u0006\u0002\u0010\u0017R\u0018\u0010\u0003\u001a\u00020\u0004*\u00020\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0018"}, d2 = {"Lokio/Options$Companion;", "", "()V", "intCount", "", "Lokio/Buffer;", "getIntCount", "(Lokio/Buffer;)J", "buildTrieRecursive", "", "nodeOffset", "node", "byteStringOffset", "", "byteStrings", "", "Lokio/ByteString;", "fromIndex", "toIndex", "indexes", "of", "Lokio/Options;", "", "([Lokio/ByteString;)Lokio/Options;", "okio"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX INFO: Multiple debug info for r5v3 java.util.ArrayList: [D('$this$map$iv' java.lang.Object[]), D('$this$toTypedArray$iv' java.util.Collection)] */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x0113, code lost:
            continue;
         */
        @JvmStatic
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final Options of(ByteString... byteStrings) {
            Intrinsics.checkParameterIsNotNull(byteStrings, "byteStrings");
            if (byteStrings.length == 0) {
                return new Options(new ByteString[0], new int[]{0, -1}, null);
            }
            List list = ArraysKt.toMutableList(byteStrings);
            CollectionsKt.sort(list);
            Collection destination$iv$iv = new ArrayList(byteStrings.length);
            for (ByteString byteString : byteStrings) {
                destination$iv$iv.add(-1);
            }
            Object[] array = ((List) destination$iv$iv).toArray(new Integer[0]);
            if (array != null) {
                Integer[] numArr = (Integer[]) array;
                List indexes = CollectionsKt.mutableListOf((Integer[]) Arrays.copyOf(numArr, numArr.length));
                int index$iv = 0;
                int length = byteStrings.length;
                int i = 0;
                while (i < length) {
                    indexes.set(CollectionsKt.binarySearch$default(list, byteStrings[i], 0, 0, 6, (Object) null), Integer.valueOf(index$iv));
                    i++;
                    index$iv++;
                }
                if (((ByteString) list.get(0)).size() > 0) {
                    for (int a2 = 0; a2 < list.size(); a2++) {
                        ByteString prefix = (ByteString) list.get(a2);
                        int b = a2 + 1;
                        while (b < list.size()) {
                            ByteString byteString2 = (ByteString) list.get(b);
                            if (!byteString2.startsWith(prefix)) {
                                break;
                            }
                            if (!(byteString2.size() != prefix.size())) {
                                throw new IllegalArgumentException(("duplicate option: " + byteString2).toString());
                            } else if (((Number) indexes.get(b)).intValue() > ((Number) indexes.get(a2)).intValue()) {
                                list.remove(b);
                                indexes.remove(b);
                            } else {
                                b++;
                            }
                        }
                    }
                    Buffer trieBytes = new Buffer();
                    buildTrieRecursive$default(this, 0, trieBytes, 0, list, 0, 0, indexes, 53, null);
                    int[] trie = new int[(int) getIntCount(trieBytes)];
                    int i2 = 0;
                    while (!trieBytes.exhausted()) {
                        trie[i2] = trieBytes.readInt();
                        i2++;
                    }
                    Object[] copyOf = Arrays.copyOf(byteStrings, byteStrings.length);
                    Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
                    return new Options((ByteString[]) copyOf, trie, null);
                }
                throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long j, Buffer buffer, int i, List list, int i2, int i3, List list2, int i4, Object obj) {
            int i5;
            int i6;
            int i7;
            long j2 = (i4 & 1) != 0 ? 0 : j;
            if ((i4 & 4) != 0) {
                i5 = 0;
            } else {
                i5 = i;
            }
            if ((i4 & 16) != 0) {
                i6 = 0;
            } else {
                i6 = i2;
            }
            if ((i4 & 32) != 0) {
                i7 = list.size();
            } else {
                i7 = i3;
            }
            companion.buildTrieRecursive(j2, buffer, i5, list, i6, i7, list2);
        }

        /* JADX INFO: Multiple debug info for r4v7 'scanByteCount'  int: [D('i' int), D('scanByteCount' int)] */
        private final void buildTrieRecursive(long nodeOffset, Buffer node, int byteStringOffset, List<? extends ByteString> list, int fromIndex, int toIndex, List<Integer> list2) {
            ByteString from;
            int fromIndex2;
            int prefixIndex;
            ByteString from2;
            ByteString from3;
            Buffer childNodes;
            int rangeEnd;
            int selectChoiceCount;
            int prefixIndex2;
            int fromIndex3;
            List<Integer> list3 = list2;
            boolean z = false;
            if (fromIndex < toIndex) {
                for (int i = fromIndex; i < toIndex; i++) {
                    if (!(((ByteString) list.get(i)).size() >= byteStringOffset)) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                ByteString from4 = (ByteString) list.get(fromIndex);
                ByteString to = (ByteString) list.get(toIndex - 1);
                if (byteStringOffset == from4.size()) {
                    int prefixIndex3 = list3.get(fromIndex).intValue();
                    int fromIndex4 = fromIndex + 1;
                    fromIndex2 = fromIndex4;
                    from = (ByteString) list.get(fromIndex4);
                    prefixIndex = prefixIndex3;
                } else {
                    fromIndex2 = fromIndex;
                    from = from4;
                    prefixIndex = -1;
                }
                if (from.getByte(byteStringOffset) != to.getByte(byteStringOffset)) {
                    int selectChoiceCount2 = 1;
                    for (int i2 = fromIndex2 + 1; i2 < toIndex; i2++) {
                        if (((ByteString) list.get(i2 - 1)).getByte(byteStringOffset) != ((ByteString) list.get(i2)).getByte(byteStringOffset)) {
                            selectChoiceCount2++;
                        }
                    }
                    long childNodesOffset = nodeOffset + getIntCount(node) + ((long) 2) + ((long) (selectChoiceCount2 * 2));
                    node.writeInt(selectChoiceCount2);
                    node.writeInt(prefixIndex);
                    for (int i3 = fromIndex2; i3 < toIndex; i3++) {
                        byte rangeByte = ((ByteString) list.get(i3)).getByte(byteStringOffset);
                        if (i3 == fromIndex2 || rangeByte != ((ByteString) list.get(i3 - 1)).getByte(byteStringOffset)) {
                            node.writeInt(255 & rangeByte);
                        }
                    }
                    Buffer childNodes2 = new Buffer();
                    int rangeStart = fromIndex2;
                    while (rangeStart < toIndex) {
                        byte rangeByte2 = ((ByteString) list.get(rangeStart)).getByte(byteStringOffset);
                        int rangeEnd2 = toIndex;
                        int i4 = rangeStart + 1;
                        while (true) {
                            if (i4 >= toIndex) {
                                i4 = rangeEnd2;
                                break;
                            } else if (rangeByte2 != ((ByteString) list.get(i4)).getByte(byteStringOffset)) {
                                break;
                            } else {
                                i4++;
                                rangeEnd2 = rangeEnd2;
                            }
                        }
                        if (rangeStart + 1 == i4 && byteStringOffset + 1 == ((ByteString) list.get(rangeStart)).size()) {
                            node.writeInt(list3.get(rangeStart).intValue());
                            rangeEnd = i4;
                            childNodes = childNodes2;
                            selectChoiceCount = selectChoiceCount2;
                            prefixIndex2 = prefixIndex;
                            fromIndex3 = fromIndex2;
                            from3 = from;
                            rangeStart = rangeEnd;
                            fromIndex2 = fromIndex3;
                            prefixIndex = prefixIndex2;
                            selectChoiceCount2 = selectChoiceCount;
                            childNodes2 = childNodes;
                            from = from3;
                            list3 = list2;
                        }
                        node.writeInt(((int) (childNodesOffset + getIntCount(childNodes2))) * -1);
                        rangeEnd = i4;
                        childNodes = childNodes2;
                        selectChoiceCount = selectChoiceCount2;
                        prefixIndex2 = prefixIndex;
                        fromIndex3 = fromIndex2;
                        from3 = from;
                        buildTrieRecursive(childNodesOffset, childNodes2, byteStringOffset + 1, list, rangeStart, rangeEnd, list2);
                        rangeStart = rangeEnd;
                        fromIndex2 = fromIndex3;
                        prefixIndex = prefixIndex2;
                        selectChoiceCount2 = selectChoiceCount;
                        childNodes2 = childNodes;
                        from = from3;
                        list3 = list2;
                    }
                    node.writeAll(childNodes2);
                    return;
                }
                ByteString from5 = from;
                int min = Math.min(from5.size(), to.size());
                int scanByteCount = 0;
                int i5 = byteStringOffset;
                while (true) {
                    if (i5 >= min) {
                        from2 = from5;
                        break;
                    }
                    from2 = from5;
                    if (from2.getByte(i5) != to.getByte(i5)) {
                        break;
                    }
                    scanByteCount++;
                    i5++;
                    from5 = from2;
                }
                long childNodesOffset2 = nodeOffset + getIntCount(node) + ((long) 2) + ((long) scanByteCount) + 1;
                node.writeInt(-scanByteCount);
                node.writeInt(prefixIndex);
                int i6 = byteStringOffset + scanByteCount;
                for (int i7 = byteStringOffset; i7 < i6; i7++) {
                    node.writeInt(from2.getByte(i7) & 255);
                }
                if (fromIndex2 + 1 == toIndex) {
                    if (byteStringOffset + scanByteCount == ((ByteString) list.get(fromIndex2)).size()) {
                        z = true;
                    }
                    if (z) {
                        node.writeInt(list2.get(fromIndex2).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                Buffer childNodes3 = new Buffer();
                node.writeInt(((int) (childNodesOffset2 + getIntCount(childNodes3))) * -1);
                buildTrieRecursive(childNodesOffset2, childNodes3, byteStringOffset + scanByteCount, list, fromIndex2, toIndex, list2);
                node.writeAll(childNodes3);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long getIntCount(Buffer $this$intCount) {
            return $this$intCount.size() / ((long) 4);
        }
    }
}
