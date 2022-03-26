package kotlin;

import kotlin.jvm.functions.Function1;
/* compiled from: ULongArray.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u001f\u0010\b\u001a\u00020\u00012\n\u0010\t\u001a\u00020\u0001\"\u00020\u0006H\u0087\bø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"ULongArray", "Lkotlin/ULongArray;", "size", "", "init", "Lkotlin/Function1;", "Lkotlin/ULong;", "(ILkotlin/jvm/functions/Function1;)[J", "ulongArrayOf", "elements", "ulongArrayOf-QwZRm1k", "([J)[J", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ULongArrayKt {
    private static final long[] ULongArray(int size, Function1<? super Integer, ULong> function1) {
        long[] jArr = new long[size];
        for (int index = 0; index < size; index++) {
            jArr[index] = function1.invoke(Integer.valueOf(index)).m210unboximpl();
        }
        return ULongArray.m213constructorimpl(jArr);
    }

    /* renamed from: ulongArrayOf-QwZRm1k  reason: not valid java name */
    private static final long[] m228ulongArrayOfQwZRm1k(long... elements) {
        return elements;
    }
}
