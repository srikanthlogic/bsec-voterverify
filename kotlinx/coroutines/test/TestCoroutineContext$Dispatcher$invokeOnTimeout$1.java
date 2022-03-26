package kotlinx.coroutines.test;

import kotlin.Metadata;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.test.TestCoroutineContext;
/* compiled from: TestCoroutineContext.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"kotlinx/coroutines/test/TestCoroutineContext$Dispatcher$invokeOnTimeout$1", "Lkotlinx/coroutines/DisposableHandle;", "dispose", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class TestCoroutineContext$Dispatcher$invokeOnTimeout$1 implements DisposableHandle {
    final /* synthetic */ TimedRunnable $node;
    final /* synthetic */ TestCoroutineContext.Dispatcher this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TestCoroutineContext$Dispatcher$invokeOnTimeout$1(TestCoroutineContext.Dispatcher $outer, TimedRunnable $captured_local_variable$1) {
        this.this$0 = $outer;
        this.$node = $captured_local_variable$1;
    }

    @Override // kotlinx.coroutines.DisposableHandle
    public void dispose() {
        TestCoroutineContext.this.queue.remove(this.$node);
    }
}
