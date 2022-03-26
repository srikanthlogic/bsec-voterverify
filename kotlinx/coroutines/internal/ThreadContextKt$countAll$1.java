package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.ThreadContextElement;
/* compiled from: ThreadContext.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, d2 = {"<no name provided>", "", "countOrElement", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke"}, k = 3, mv = {1, 1, 13})
/* loaded from: classes3.dex */
final class ThreadContextKt$countAll$1 extends Lambda implements Function2<Object, CoroutineContext.Element, Object> {
    public static final ThreadContextKt$countAll$1 INSTANCE = new ThreadContextKt$countAll$1();

    ThreadContextKt$countAll$1() {
        super(2);
    }

    public final Object invoke(Object countOrElement, CoroutineContext.Element element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        if (!(element instanceof ThreadContextElement)) {
            return countOrElement;
        }
        Integer num = (Integer) (!(countOrElement instanceof Integer) ? null : countOrElement);
        int inCount = num != null ? num.intValue() : 1;
        return inCount == 0 ? element : Integer.valueOf(inCount + 1);
    }
}
