package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
/* compiled from: Await.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a=\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001e\u0010\u0003\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004\"\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a%\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\u0004\"\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\r\u001a\u001b\u0010\u0007\u001a\u00020\b*\b\u0012\u0004\u0012\u00020\n0\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"awaitAll", "", ExifInterface.GPS_DIRECTION_TRUE, "deferreds", "", "Lkotlinx/coroutines/Deferred;", "([Lkotlinx/coroutines/Deferred;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "joinAll", "", "jobs", "Lkotlinx/coroutines/Job;", "([Lkotlinx/coroutines/Job;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "", "(Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class AwaitKt {
    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> Object awaitAll(Deferred<? extends T>[] deferredArr, Continuation<? super List<? extends T>> continuation) {
        AwaitKt$awaitAll$1 awaitKt$awaitAll$1;
        int i;
        if (continuation instanceof AwaitKt$awaitAll$1) {
            awaitKt$awaitAll$1 = (AwaitKt$awaitAll$1) continuation;
            if ((awaitKt$awaitAll$1.label & Integer.MIN_VALUE) != 0) {
                awaitKt$awaitAll$1.label -= Integer.MIN_VALUE;
                Object obj = awaitKt$awaitAll$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = awaitKt$awaitAll$1.label;
                if (i == 0) {
                    if (i == 1) {
                        Deferred[] deferreds = (Deferred[]) awaitKt$awaitAll$1.L$0;
                        if (obj instanceof Result.Failure) {
                            throw ((Result.Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj instanceof Result.Failure)) {
                    if (deferredArr.length == 0) {
                        return CollectionsKt.emptyList();
                    }
                    AwaitAll awaitAll = new AwaitAll(deferredArr);
                    awaitKt$awaitAll$1.L$0 = deferredArr;
                    awaitKt$awaitAll$1.label = 1;
                    obj = awaitAll.await(awaitKt$awaitAll$1);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    throw ((Result.Failure) obj).exception;
                }
                return (List) obj;
            }
        }
        awaitKt$awaitAll$1 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.AwaitKt$awaitAll$1
            Object L$0;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return AwaitKt.awaitAll((Deferred[]) null, this);
            }
        };
        Object obj2 = awaitKt$awaitAll$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = awaitKt$awaitAll$1.label;
        if (i == 0) {
        }
        return (List) obj2;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003e  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final <T> Object awaitAll(Collection<? extends Deferred<? extends T>> collection, Continuation<? super List<? extends T>> continuation) {
        AwaitKt$awaitAll$2 awaitKt$awaitAll$2;
        int i;
        if (continuation instanceof AwaitKt$awaitAll$2) {
            awaitKt$awaitAll$2 = (AwaitKt$awaitAll$2) continuation;
            if ((awaitKt$awaitAll$2.label & Integer.MIN_VALUE) != 0) {
                awaitKt$awaitAll$2.label -= Integer.MIN_VALUE;
                Object obj = awaitKt$awaitAll$2.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = awaitKt$awaitAll$2.label;
                if (i == 0) {
                    if (i == 1) {
                        Collection $receiver = (Collection) awaitKt$awaitAll$2.L$0;
                        if (obj instanceof Result.Failure) {
                            throw ((Result.Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (obj instanceof Result.Failure) {
                    throw ((Result.Failure) obj).exception;
                } else if (collection.isEmpty()) {
                    return CollectionsKt.emptyList();
                } else {
                    if (collection != null) {
                        Object[] array = collection.toArray(new Deferred[0]);
                        if (array != null) {
                            AwaitAll awaitAll = new AwaitAll((Deferred[]) array);
                            awaitKt$awaitAll$2.L$0 = collection;
                            awaitKt$awaitAll$2.label = 1;
                            obj = awaitAll.await(awaitKt$awaitAll$2);
                            if (obj == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
                    }
                }
                return (List) obj;
            }
        }
        awaitKt$awaitAll$2 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.AwaitKt$awaitAll$2
            Object L$0;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return AwaitKt.awaitAll((Collection) null, this);
            }
        };
        Object obj2 = awaitKt$awaitAll$2.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = awaitKt$awaitAll$2.label;
        if (i == 0) {
        }
        return (List) obj2;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0086  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final Object joinAll(Job[] jobs, Continuation<? super Unit> continuation) {
        AwaitKt$joinAll$1 awaitKt$joinAll$1;
        int i;
        Object[] objArr;
        int i2;
        int i3;
        Object[] $receiver$iv;
        if (continuation instanceof AwaitKt$joinAll$1) {
            awaitKt$joinAll$1 = (AwaitKt$joinAll$1) continuation;
            if ((awaitKt$joinAll$1.label & Integer.MIN_VALUE) != 0) {
                awaitKt$joinAll$1.label -= Integer.MIN_VALUE;
                Object obj = awaitKt$joinAll$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = awaitKt$joinAll$1.label;
                if (i == 0) {
                    if (i == 1) {
                        Job it = (Job) awaitKt$joinAll$1.L$4;
                        Object element$iv = (Job) awaitKt$joinAll$1.L$3;
                        i3 = awaitKt$joinAll$1.I$1;
                        i2 = awaitKt$joinAll$1.I$0;
                        objArr = (Job[]) awaitKt$joinAll$1.L$2;
                        $receiver$iv = (Job[]) awaitKt$joinAll$1.L$1;
                        jobs = (Job[]) awaitKt$joinAll$1.L$0;
                        if (!(obj instanceof Result.Failure)) {
                            i3++;
                            if (i3 < i2) {
                                Job job = objArr[i3];
                                awaitKt$joinAll$1.L$0 = jobs;
                                awaitKt$joinAll$1.L$1 = $receiver$iv;
                                awaitKt$joinAll$1.L$2 = objArr;
                                awaitKt$joinAll$1.I$0 = i2;
                                awaitKt$joinAll$1.I$1 = i3;
                                awaitKt$joinAll$1.L$3 = job;
                                awaitKt$joinAll$1.L$4 = job;
                                awaitKt$joinAll$1.label = 1;
                                if (job.join(awaitKt$joinAll$1) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                i3++;
                                if (i3 < i2) {
                                    return Unit.INSTANCE;
                                }
                            }
                        } else {
                            throw ((Result.Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj instanceof Result.Failure)) {
                    i2 = jobs.length;
                    objArr = jobs;
                    i3 = 0;
                    $receiver$iv = objArr;
                    if (i3 < i2) {
                    }
                } else {
                    throw ((Result.Failure) obj).exception;
                }
            }
        }
        awaitKt$joinAll$1 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.AwaitKt$joinAll$1
            int I$0;
            int I$1;
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return AwaitKt.joinAll((Job[]) null, this);
            }
        };
        Object obj2 = awaitKt$joinAll$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = awaitKt$joinAll$1.label;
        if (i == 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static final Object joinAll(Collection<? extends Job> collection, Continuation<? super Unit> continuation) {
        AwaitKt$joinAll$3 awaitKt$joinAll$3;
        int i;
        Iterator it;
        Iterable $receiver$iv;
        if (continuation instanceof AwaitKt$joinAll$3) {
            awaitKt$joinAll$3 = (AwaitKt$joinAll$3) continuation;
            if ((awaitKt$joinAll$3.label & Integer.MIN_VALUE) != 0) {
                awaitKt$joinAll$3.label -= Integer.MIN_VALUE;
                Object obj = awaitKt$joinAll$3.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = awaitKt$joinAll$3.label;
                if (i == 0) {
                    if (i == 1) {
                        Job it2 = (Job) awaitKt$joinAll$3.L$4;
                        Object element$iv = awaitKt$joinAll$3.L$3;
                        it = (Iterator) awaitKt$joinAll$3.L$2;
                        $receiver$iv = (Iterable) awaitKt$joinAll$3.L$1;
                        collection = (Collection) awaitKt$joinAll$3.L$0;
                        if (obj instanceof Result.Failure) {
                            throw ((Result.Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj instanceof Result.Failure)) {
                    Collection<? extends Job> $receiver$iv2 = collection;
                    it = $receiver$iv2.iterator();
                    $receiver$iv = $receiver$iv2;
                } else {
                    throw ((Result.Failure) obj).exception;
                }
                while (it.hasNext()) {
                    Object element$iv2 = it.next();
                    Job it3 = (Job) element$iv2;
                    awaitKt$joinAll$3.L$0 = collection;
                    awaitKt$joinAll$3.L$1 = $receiver$iv;
                    awaitKt$joinAll$3.L$2 = it;
                    awaitKt$joinAll$3.L$3 = element$iv2;
                    awaitKt$joinAll$3.L$4 = it3;
                    awaitKt$joinAll$3.label = 1;
                    if (it3.join(awaitKt$joinAll$3) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            }
        }
        awaitKt$joinAll$3 = new ContinuationImpl(continuation) { // from class: kotlinx.coroutines.AwaitKt$joinAll$3
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            int label;
            /* synthetic */ Object result;

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                this.result = obj2;
                this.label |= Integer.MIN_VALUE;
                return AwaitKt.joinAll((Collection<? extends Job>) null, this);
            }
        };
        Object obj2 = awaitKt$joinAll$3.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = awaitKt$joinAll$3.label;
        if (i == 0) {
        }
        while (it.hasNext()) {
        }
        return Unit.INSTANCE;
    }
}
