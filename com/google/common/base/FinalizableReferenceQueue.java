package com.google.common.base;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public class FinalizableReferenceQueue implements Closeable {
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
    private static final Method startFinalizer = getStartFinalizer(loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader()));
    final boolean threadStarted;
    final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    final PhantomReference<Object> frqRef = new PhantomReference<>(this, this.queue);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface FinalizerLoader {
        @NullableDecl
        Class<?> loadFinalizer();
    }

    public FinalizableReferenceQueue() {
        boolean threadStarted = false;
        try {
            startFinalizer.invoke(null, FinalizableReference.class, this.queue, this.frqRef);
            threadStarted = true;
        } catch (IllegalAccessException impossible) {
            throw new AssertionError(impossible);
        } catch (Throwable t) {
            logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
        }
        this.threadStarted = threadStarted;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.frqRef.enqueue();
        cleanUp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUp() {
        if (!this.threadStarted) {
            while (true) {
                Reference<?> reference = this.queue.poll();
                if (reference != null) {
                    reference.clear();
                    try {
                        ((FinalizableReference) reference).finalizeReferent();
                    } catch (Throwable t) {
                        logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
        for (FinalizerLoader loader : loaders) {
            Class<?> finalizer = loader.loadFinalizer();
            if (finalizer != null) {
                return finalizer;
            }
        }
        throw new AssertionError();
    }

    /* loaded from: classes.dex */
    static class SystemLoader implements FinalizerLoader {
        static boolean disabled;

        SystemLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        @NullableDecl
        public Class<?> loadFinalizer() {
            if (disabled) {
                return null;
            }
            try {
                ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
                if (systemLoader == null) {
                    return null;
                }
                try {
                    return systemLoader.loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            } catch (SecurityException e2) {
                FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";

        DecoupledLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        @NullableDecl
        public Class<?> loadFinalizer() {
            try {
                return newLoader(getBaseUrl()).loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (Exception e) {
                FinalizableReferenceQueue.logger.log(Level.WARNING, LOADING_ERROR, (Throwable) e);
                return null;
            }
        }

        URL getBaseUrl() throws IOException {
            String finalizerPath = FinalizableReferenceQueue.FINALIZER_CLASS_NAME.replace((char) FilenameUtils.EXTENSION_SEPARATOR, (char) IOUtils.DIR_SEPARATOR_UNIX) + ".class";
            URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
            if (finalizerUrl != null) {
                String urlString = finalizerUrl.toString();
                if (urlString.endsWith(finalizerPath)) {
                    return new URL(finalizerUrl, urlString.substring(0, urlString.length() - finalizerPath.length()));
                }
                throw new IOException("Unsupported path style: " + urlString);
            }
            throw new FileNotFoundException(finalizerPath);
        }

        URLClassLoader newLoader(URL base) {
            return new URLClassLoader(new URL[]{base}, null);
        }
    }

    /* loaded from: classes.dex */
    static class DirectLoader implements FinalizerLoader {
        DirectLoader() {
        }

        @Override // com.google.common.base.FinalizableReferenceQueue.FinalizerLoader
        public Class<?> loadFinalizer() {
            try {
                return Class.forName(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }

    static Method getStartFinalizer(Class<?> finalizer) {
        try {
            return finalizer.getMethod("startFinalizer", Class.class, ReferenceQueue.class, PhantomReference.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }
}
