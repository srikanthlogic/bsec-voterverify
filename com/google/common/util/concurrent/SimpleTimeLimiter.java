package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes3.dex */
public final class SimpleTimeLimiter implements TimeLimiter {
    private final ExecutorService executor;

    private SimpleTimeLimiter(ExecutorService executor) {
        this.executor = (ExecutorService) Preconditions.checkNotNull(executor);
    }

    public static SimpleTimeLimiter create(ExecutorService executor) {
        return new SimpleTimeLimiter(executor);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(interfaceType);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
        final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
        return (T) newProxy(interfaceType, new InvocationHandler() { // from class: com.google.common.util.concurrent.SimpleTimeLimiter.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable {
                return SimpleTimeLimiter.this.callWithTimeout(new Callable<Object>() { // from class: com.google.common.util.concurrent.SimpleTimeLimiter.1.1
                    @Override // java.util.concurrent.Callable
                    public Object call() throws Exception {
                        try {
                            return method.invoke(target, args);
                        } catch (InvocationTargetException e) {
                            throw SimpleTimeLimiter.throwCause(e, false);
                        }
                    }
                }, timeoutDuration, timeoutUnit, interruptibleMethods.contains(method));
            }
        });
    }

    private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
        return interfaceType.cast(Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, handler));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Future<T> future = this.executor.submit(callable);
        try {
            if (!amInterruptible) {
                return (T) Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
            }
            try {
                return future.get(timeoutDuration, timeoutUnit);
            } catch (InterruptedException e) {
                future.cancel(true);
                throw e;
            }
        } catch (ExecutionException e2) {
            throw throwCause(e2, true);
        } catch (TimeoutException e3) {
            future.cancel(true);
            throw new UncheckedTimeoutException(e3);
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException, ExecutionException {
        Exception e;
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Future<T> future = this.executor.submit(callable);
        try {
            return future.get(timeoutDuration, timeoutUnit);
        } catch (InterruptedException e2) {
            e = e2;
            future.cancel(true);
            throw e;
        } catch (ExecutionException e3) {
            wrapAndThrowExecutionExceptionOrError(e3.getCause());
            throw new AssertionError();
        } catch (TimeoutException e4) {
            e = e4;
            future.cancel(true);
            throw e;
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, ExecutionException {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Future<T> future = this.executor.submit(callable);
        try {
            return (T) Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
        } catch (ExecutionException e) {
            wrapAndThrowExecutionExceptionOrError(e.getCause());
            throw new AssertionError();
        } catch (TimeoutException e2) {
            future.cancel(true);
            throw e2;
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException {
        Exception e;
        Preconditions.checkNotNull(runnable);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Future<?> future = this.executor.submit(runnable);
        try {
            future.get(timeoutDuration, timeoutUnit);
        } catch (InterruptedException e2) {
            e = e2;
            future.cancel(true);
            throw e;
        } catch (ExecutionException e3) {
            wrapAndThrowRuntimeExecutionExceptionOrError(e3.getCause());
            throw new AssertionError();
        } catch (TimeoutException e4) {
            e = e4;
            future.cancel(true);
            throw e;
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException {
        Preconditions.checkNotNull(runnable);
        Preconditions.checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
        Future<?> future = this.executor.submit(runnable);
        try {
            Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
        } catch (ExecutionException e) {
            wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
            throw new AssertionError();
        } catch (TimeoutException e2) {
            future.cancel(true);
            throw e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
        Throwable cause = e.getCause();
        if (cause != null) {
            if (combineStackTraces) {
                cause.setStackTrace((StackTraceElement[]) ObjectArrays.concat(cause.getStackTrace(), e.getStackTrace(), StackTraceElement.class));
            }
            if (cause instanceof Exception) {
                throw ((Exception) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw e;
            }
        } else {
            throw e;
        }
    }

    private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
        Set<Method> set = Sets.newHashSet();
        Method[] methods = interfaceType.getMethods();
        for (Method m : methods) {
            if (declaresInterruptedEx(m)) {
                set.add(m);
            }
        }
        return set;
    }

    private static boolean declaresInterruptedEx(Method method) {
        for (Class<?> exType : method.getExceptionTypes()) {
            if (exType == InterruptedException.class) {
                return true;
            }
        }
        return false;
    }

    private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        } else if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        } else {
            throw new ExecutionException(cause);
        }
    }

    private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        throw new UncheckedExecutionException(cause);
    }

    private static void checkPositiveTimeout(long timeoutDuration) {
        Preconditions.checkArgument(timeoutDuration > 0, "timeout must be positive: %s", timeoutDuration);
    }
}
