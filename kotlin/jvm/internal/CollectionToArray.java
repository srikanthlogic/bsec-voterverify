package kotlin.jvm.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
/* compiled from: CollectionToArray.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CollectionToArray {
    private static final Object[] EMPTY = new Object[0];
    private static final int MAX_SIZE;

    /* JADX INFO: Multiple debug info for r3v8 java.lang.Object[]: [D('$i$a$-toArrayImpl-CollectionToArray$collectionToArray$1' int), D('result$iv' java.lang.Object[])] */
    public static final Object[] toArray(Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        int size$iv = collection.size();
        if (size$iv == 0) {
            return EMPTY;
        }
        Iterator iter$iv = collection.iterator();
        if (!iter$iv.hasNext()) {
            return EMPTY;
        }
        Object[] result$iv = new Object[size$iv];
        int i$iv = 0;
        while (true) {
            int i$iv2 = i$iv + 1;
            result$iv[i$iv] = iter$iv.next();
            if (i$iv2 >= result$iv.length) {
                if (!iter$iv.hasNext()) {
                    return result$iv;
                }
                int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                if (newSize$iv <= i$iv2) {
                    if (i$iv2 < MAX_SIZE) {
                        newSize$iv = MAX_SIZE;
                    } else {
                        throw new OutOfMemoryError();
                    }
                }
                Object[] copyOf = Arrays.copyOf(result$iv, newSize$iv);
                Intrinsics.checkExpressionValueIsNotNull(copyOf, "Arrays.copyOf(result, newSize)");
                result$iv = copyOf;
            } else if (!iter$iv.hasNext()) {
                Object[] result$iv2 = Arrays.copyOf(result$iv, i$iv2);
                Intrinsics.checkExpressionValueIsNotNull(result$iv2, "Arrays.copyOf(result, size)");
                return result$iv2;
            }
            i$iv = i$iv2;
        }
    }

    public static final Object[] toArray(Collection<?> collection, Object[] a2) {
        Object[] objArr;
        Object[] result$iv;
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        if (a2 != null) {
            int size$iv = collection.size();
            if (size$iv != 0) {
                Iterator iter$iv = collection.iterator();
                if (iter$iv.hasNext()) {
                    if (size$iv <= a2.length) {
                        objArr = a2;
                    } else {
                        Object newInstance = Array.newInstance(a2.getClass().getComponentType(), size$iv);
                        if (newInstance != null) {
                            objArr = (Object[]) newInstance;
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                        }
                    }
                    Object[] result$iv2 = objArr;
                    int i$iv = 0;
                    while (true) {
                        int i$iv2 = i$iv + 1;
                        result$iv2[i$iv] = iter$iv.next();
                        if (i$iv2 >= result$iv2.length) {
                            if (!iter$iv.hasNext()) {
                                return result$iv2;
                            }
                            int newSize$iv = ((i$iv2 * 3) + 1) >>> 1;
                            if (newSize$iv <= i$iv2) {
                                if (i$iv2 < MAX_SIZE) {
                                    newSize$iv = MAX_SIZE;
                                } else {
                                    throw new OutOfMemoryError();
                                }
                            }
                            Object[] copyOf = Arrays.copyOf(result$iv2, newSize$iv);
                            Intrinsics.checkExpressionValueIsNotNull(copyOf, "Arrays.copyOf(result, newSize)");
                            result$iv2 = copyOf;
                        } else if (!iter$iv.hasNext()) {
                            if (result$iv2 == a2) {
                                a2[i$iv2] = null;
                                result$iv = a2;
                            } else {
                                result$iv = Arrays.copyOf(result$iv2, i$iv2);
                                Intrinsics.checkExpressionValueIsNotNull(result$iv, "Arrays.copyOf(result, size)");
                            }
                            return result$iv;
                        }
                        i$iv = i$iv2;
                    }
                } else if (a2.length > 0) {
                    a2[0] = null;
                }
            } else if (a2.length > 0) {
                a2[0] = null;
            }
            return a2;
        }
        throw new NullPointerException();
    }

    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.Object, java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static final Object[] toArrayImpl(Collection<?> collection, Function0<Object[]> function0, Function1<? super Integer, Object[]> function1, Function2<? super Object[], ? super Integer, Object[]> function2) {
        int size = collection.size();
        if (size == 0) {
            return function0.invoke();
        }
        Iterator iter = collection.iterator();
        if (!iter.hasNext()) {
            return function0.invoke();
        }
        int i = 0;
        ?? r3 = function1.invoke(Integer.valueOf(size));
        while (true) {
            int i2 = i + 1;
            r3[i] = iter.next();
            if (i2 >= r3.length) {
                if (!iter.hasNext()) {
                    return r3;
                }
                int newSize = ((i2 * 3) + 1) >>> 1;
                if (newSize <= i2) {
                    if (i2 < MAX_SIZE) {
                        newSize = MAX_SIZE;
                    } else {
                        throw new OutOfMemoryError();
                    }
                }
                Object[] copyOf = Arrays.copyOf((Object[]) r3, newSize);
                Intrinsics.checkExpressionValueIsNotNull(copyOf, "Arrays.copyOf(result, newSize)");
                r3 = copyOf;
            } else if (!iter.hasNext()) {
                return function2.invoke(r3, Integer.valueOf(i2));
            }
            i = i2;
            r3 = r3;
        }
    }
}
