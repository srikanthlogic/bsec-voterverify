package kotlinx.coroutines;

import java.util.Iterator;
import java.util.ServiceLoader;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.MainDispatcherFactory;
/* compiled from: Dispatchers.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\f\u0010\u0005\u001a\u00020\u0004*\u00020\u0006H\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/MainDispatcherLoader;", "", "()V", "dispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "tryCreateDispatcher", "Lkotlinx/coroutines/internal/MainDispatcherFactory;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
final class MainDispatcherLoader {
    public static final MainDispatcherLoader INSTANCE;
    public static final MainCoroutineDispatcher dispatcher;

    /* JADX INFO: Multiple debug info for r1v2 java.util.List: [D('clz' java.lang.Class), D('$receiver$iv' java.lang.Iterable)] */
    /* JADX INFO: Multiple debug info for r9v2 int: [D('it' kotlinx.coroutines.internal.MainDispatcherFactory), D('v$iv' int)] */
    static {
        Object maxElem$iv;
        MissingMainCoroutineDispatcher missingMainCoroutineDispatcher;
        MainDispatcherLoader mainDispatcherLoader = new MainDispatcherLoader();
        INSTANCE = mainDispatcherLoader;
        ServiceLoader load = ServiceLoader.load(MainDispatcherFactory.class, MainDispatcherFactory.class.getClassLoader());
        Intrinsics.checkExpressionValueIsNotNull(load, "ServiceLoader.load(clz, clz.classLoader)");
        Iterator iterator$iv = CollectionsKt.toList(load).iterator();
        if (!iterator$iv.hasNext()) {
            maxElem$iv = null;
        } else {
            maxElem$iv = iterator$iv.next();
            int maxValue$iv = ((MainDispatcherFactory) maxElem$iv).getLoadPriority();
            while (iterator$iv.hasNext()) {
                Object e$iv = iterator$iv.next();
                int v$iv = ((MainDispatcherFactory) e$iv).getLoadPriority();
                if (maxValue$iv < v$iv) {
                    maxElem$iv = e$iv;
                    maxValue$iv = v$iv;
                }
            }
        }
        MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) maxElem$iv;
        if (mainDispatcherFactory == null || (missingMainCoroutineDispatcher = mainDispatcherLoader.tryCreateDispatcher(mainDispatcherFactory)) == null) {
            missingMainCoroutineDispatcher = new MissingMainCoroutineDispatcher(null);
        }
        dispatcher = missingMainCoroutineDispatcher;
    }

    private MainDispatcherLoader() {
    }

    private final MainCoroutineDispatcher tryCreateDispatcher(MainDispatcherFactory $receiver) {
        try {
            return $receiver.createDispatcher();
        } catch (Throwable cause) {
            return new MissingMainCoroutineDispatcher(cause);
        }
    }
}
