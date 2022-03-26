package androidx.camera.core.impl.utils.futures;

import androidx.core.util.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChainingListenableFuture<I, O> extends FutureChain<O> implements Runnable {
    private AsyncFunction<? super I, ? extends O> mFunction;
    private ListenableFuture<? extends I> mInputFuture;
    private final BlockingQueue<Boolean> mMayInterruptIfRunningChannel = new LinkedBlockingQueue(1);
    private final CountDownLatch mOutputCreated = new CountDownLatch(1);
    volatile ListenableFuture<? extends O> mOutputFuture;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
        this.mFunction = (AsyncFunction) Preconditions.checkNotNull(function);
        this.mInputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
    }

    @Override // androidx.camera.core.impl.utils.futures.FutureChain, java.util.concurrent.Future
    public O get() throws InterruptedException, ExecutionException {
        if (!isDone()) {
            ListenableFuture<? extends I> inputFuture = this.mInputFuture;
            if (inputFuture != null) {
                inputFuture.get();
            }
            this.mOutputCreated.await();
            ListenableFuture<? extends O> outputFuture = this.mOutputFuture;
            if (outputFuture != null) {
                outputFuture.get();
            }
        }
        return (O) super.get();
    }

    @Override // androidx.camera.core.impl.utils.futures.FutureChain, java.util.concurrent.Future
    public O get(long timeout, TimeUnit unit) throws TimeoutException, ExecutionException, InterruptedException {
        if (!isDone()) {
            if (unit != TimeUnit.NANOSECONDS) {
                timeout = TimeUnit.NANOSECONDS.convert(timeout, unit);
                unit = TimeUnit.NANOSECONDS;
            }
            ListenableFuture<? extends I> inputFuture = this.mInputFuture;
            if (inputFuture != null) {
                long start = System.nanoTime();
                inputFuture.get(timeout, unit);
                timeout -= Math.max(0L, System.nanoTime() - start);
            }
            long start2 = System.nanoTime();
            if (this.mOutputCreated.await(timeout, unit)) {
                timeout -= Math.max(0L, System.nanoTime() - start2);
                ListenableFuture<? extends O> outputFuture = this.mOutputFuture;
                if (outputFuture != null) {
                    outputFuture.get(timeout, unit);
                }
            } else {
                throw new TimeoutException();
            }
        }
        return (O) super.get(timeout, unit);
    }

    @Override // androidx.camera.core.impl.utils.futures.FutureChain, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!super.cancel(mayInterruptIfRunning)) {
            return false;
        }
        putUninterruptibly(this.mMayInterruptIfRunningChannel, Boolean.valueOf(mayInterruptIfRunning));
        cancel(this.mInputFuture, mayInterruptIfRunning);
        cancel(this.mOutputFuture, mayInterruptIfRunning);
        return true;
    }

    private void cancel(Future<?> future, boolean mayInterruptIfRunning) {
        if (future != null) {
            future.cancel(mayInterruptIfRunning);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
        jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 0, insn: 0x0084: IPUT  
          (r0 I:androidx.camera.core.impl.utils.futures.AsyncFunction<? super I, ? extends O>)
          (r5 I:androidx.camera.core.impl.utils.futures.ChainingListenableFuture A[D('this' androidx.camera.core.impl.utils.futures.ChainingListenableFuture<I, O>)])
         androidx.camera.core.impl.utils.futures.ChainingListenableFuture.mFunction androidx.camera.core.impl.utils.futures.AsyncFunction, block:B:32:0x0084
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVarsInBlock(SSATransform.java:171)
        	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:143)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:60)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    @Override // java.lang.Runnable
    public void run() {
        /*
            r5 = this;
            r0 = 0
            com.google.common.util.concurrent.ListenableFuture<? extends I> r1 = r5.mInputFuture     // Catch: CancellationException -> 0x0058, ExecutionException -> 0x0046, UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            java.lang.Object r1 = androidx.camera.core.impl.utils.futures.Futures.getUninterruptibly(r1)     // Catch: CancellationException -> 0x0058, ExecutionException -> 0x0046, UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            androidx.camera.core.impl.utils.futures.AsyncFunction<? super I, ? extends O> r2 = r5.mFunction     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            com.google.common.util.concurrent.ListenableFuture r2 = r2.apply(r1)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.mOutputFuture = r2     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            boolean r3 = r5.isCancelled()     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            if (r3 == 0) goto L_0x0031
            java.util.concurrent.BlockingQueue<java.lang.Boolean> r3 = r5.mMayInterruptIfRunningChannel     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            java.lang.Object r3 = r5.takeUninterruptibly(r3)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            boolean r3 = r3.booleanValue()     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r2.cancel(r3)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.mOutputFuture = r0     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.mFunction = r0
            r5.mInputFuture = r0
            java.util.concurrent.CountDownLatch r0 = r5.mOutputCreated
            r0.countDown()
            return
        L_0x0031:
            androidx.camera.core.impl.utils.futures.ChainingListenableFuture$1 r3 = new androidx.camera.core.impl.utils.futures.ChainingListenableFuture$1     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r3.<init>(r2)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            java.util.concurrent.Executor r4 = androidx.camera.core.impl.utils.executor.CameraXExecutors.directExecutor()     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r2.addListener(r3, r4)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            goto L_0x0070
        L_0x003e:
            r1 = move-exception
            goto L_0x0084
        L_0x0040:
            r1 = move-exception
            goto L_0x0067
        L_0x0042:
            r1 = move-exception
            goto L_0x006c
        L_0x0044:
            r1 = move-exception
            goto L_0x007a
        L_0x0046:
            r1 = move-exception
            java.lang.Throwable r2 = r1.getCause()     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.setException(r2)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.mFunction = r0
            r5.mInputFuture = r0
            java.util.concurrent.CountDownLatch r0 = r5.mOutputCreated
            r0.countDown()
            return
        L_0x0058:
            r1 = move-exception
            r2 = 0
            r5.cancel(r2)     // Catch: UndeclaredThrowableException -> 0x0044, Exception -> 0x0042, Error -> 0x0040, all -> 0x003e
            r5.mFunction = r0
            r5.mInputFuture = r0
            java.util.concurrent.CountDownLatch r0 = r5.mOutputCreated
            r0.countDown()
            return
        L_0x0067:
            r5.setException(r1)     // Catch: all -> 0x003e
            goto L_0x0070
        L_0x006c:
            r5.setException(r1)     // Catch: all -> 0x003e
        L_0x0070:
            r5.mFunction = r0
            r5.mInputFuture = r0
            java.util.concurrent.CountDownLatch r0 = r5.mOutputCreated
            r0.countDown()
            goto L_0x0083
        L_0x007a:
            java.lang.Throwable r2 = r1.getCause()     // Catch: all -> 0x003e
            r5.setException(r2)     // Catch: all -> 0x003e
            goto L_0x0070
        L_0x0083:
            return
        L_0x0084:
            r5.mFunction = r0
            r5.mInputFuture = r0
            java.util.concurrent.CountDownLatch r0 = r5.mOutputCreated
            r0.countDown()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.impl.utils.futures.ChainingListenableFuture.run():void");
    }

    private <E> E takeUninterruptibly(BlockingQueue<E> queue) {
        E take;
        boolean interrupted = false;
        while (true) {
            try {
                take = queue.take();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return take;
    }

    private <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
        boolean interrupted = false;
        while (true) {
            try {
                queue.put(element);
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
