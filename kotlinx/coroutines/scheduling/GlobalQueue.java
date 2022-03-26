package kotlinx.coroutines.scheduling;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeMPMCQueue;
/* compiled from: Tasks.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0010\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0002J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016J\b\u0010\b\u001a\u0004\u0018\u00010\u0002¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/scheduling/GlobalQueue;", "Lkotlinx/coroutines/internal/LockFreeMPMCQueue;", "Lkotlinx/coroutines/scheduling/Task;", "()V", "add", "", "task", "removeFirstBlockingModeOrNull", "removeFirstIfNotClosed", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class GlobalQueue extends LockFreeMPMCQueue<Task> {
    public final boolean add(Task task) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        while (true) {
            Task tailValue = getTailValue();
            Task nextValue = tailValue.getNextValue();
            if (nextValue != null) {
                tailCas(tailValue, nextValue);
            } else {
                if ((tailValue != TasksKt.getCLOSED_TASK() ? 1 : null) == null) {
                    return false;
                }
                if (tailValue.nextCas(null, task)) {
                    tailCas(tailValue, task);
                    return true;
                }
            }
        }
    }

    public final Task removeFirstIfNotClosed() {
        Task task;
        boolean z = false;
        while (true) {
            Task headValue = getHeadValue();
            Task first = headValue.getNextValue();
            task = null;
            if (first != null) {
                if (first != TasksKt.getCLOSED_TASK()) {
                    z = true;
                }
                if (z) {
                    if (headCas(headValue, first)) {
                        task = first;
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return task;
    }

    public Task removeFirstBlockingModeOrNull() {
        Task task;
        boolean z = false;
        while (true) {
            Task headValue = getHeadValue();
            Task it = headValue.getNextValue();
            task = null;
            if (it != null) {
                if (it.getMode() == TaskMode.PROBABLY_BLOCKING) {
                    z = true;
                }
                if (z) {
                    if (headCas(headValue, it)) {
                        task = it;
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return task;
    }
}
