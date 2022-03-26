package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class Finalizer implements Runnable {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    @NullableDecl
    private static final Field inheritableThreadLocals;
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    @NullableDecl
    private static final Constructor<Thread> bigThreadConstructor = getBigThreadConstructor();

    static {
        inheritableThreadLocals = bigThreadConstructor == null ? getInheritableThreadLocalsField() : null;
    }

    public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
        if (finalizableReferenceClass.getName().equals(FINALIZABLE_REFERENCE)) {
            Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
            String threadName = Finalizer.class.getName();
            Thread thread = null;
            Constructor<Thread> constructor = bigThreadConstructor;
            if (constructor != null) {
                try {
                    thread = constructor.newInstance(null, finalizer, threadName, 0L, false);
                } catch (Throwable t) {
                    logger.log(Level.INFO, "Failed to create a thread without inherited thread-local values", t);
                }
            }
            if (thread == null) {
                thread = new Thread(null, finalizer, threadName);
            }
            thread.setDaemon(true);
            try {
                if (inheritableThreadLocals != null) {
                    inheritableThreadLocals.set(thread, null);
                }
            } catch (Throwable t2) {
                logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t2);
            }
            thread.start();
            return;
        }
        throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }

    private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
        this.queue = queue;
        this.finalizableReferenceClassReference = new WeakReference<>(finalizableReferenceClass);
        this.frqReference = frqReference;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (cleanUp(this.queue.remove())) {
        }
    }

    private boolean cleanUp(Reference<?> reference) {
        Reference<? extends Object> poll;
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        if (finalizeReferentMethod == null) {
            return false;
        }
        do {
            reference.clear();
            if (reference == this.frqReference) {
                return false;
            }
            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
            }
            poll = this.queue.poll();
            reference = poll;
        } while (poll != null);
        return true;
    }

    @NullableDecl
    private Method getFinalizeReferentMethod() {
        Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
        if (finalizableReferenceClass == null) {
            return null;
        }
        try {
            return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    @NullableDecl
    private static Field getInheritableThreadLocalsField() {
        try {
            Field inheritableThreadLocals2 = Thread.class.getDeclaredField("inheritableThreadLocals");
            inheritableThreadLocals2.setAccessible(true);
            return inheritableThreadLocals2;
        } catch (Throwable th) {
            logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }

    @NullableDecl
    private static Constructor<Thread> getBigThreadConstructor() {
        try {
            return Thread.class.getConstructor(ThreadGroup.class, Runnable.class, String.class, Long.TYPE, Boolean.TYPE);
        } catch (Throwable th) {
            return null;
        }
    }
}
