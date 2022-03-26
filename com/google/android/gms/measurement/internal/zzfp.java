package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfp extends zzgm {
    private static final AtomicLong zza = new AtomicLong(Long.MIN_VALUE);
    private zzfo zzb;
    private zzfo zzc;
    private volatile boolean zzj;
    private final Object zzh = new Object();
    private final Semaphore zzi = new Semaphore(2);
    private final PriorityBlockingQueue<zzfn<?>> zzd = new PriorityBlockingQueue<>();
    private final BlockingQueue<zzfn<?>> zze = new LinkedBlockingQueue();
    private final Thread.UncaughtExceptionHandler zzf = new zzfm(this, "Thread death: Uncaught exception on worker thread");
    private final Thread.UncaughtExceptionHandler zzg = new zzfm(this, "Thread death: Uncaught exception on network thread");

    public zzfp(zzfs zzfs) {
        super(zzfs);
    }

    private final void zzt(zzfn<?> zzfn) {
        synchronized (this.zzh) {
            this.zzd.add(zzfn);
            zzfo zzfo = this.zzb;
            if (zzfo == null) {
                this.zzb = new zzfo(this, "Measurement Worker", this.zzd);
                this.zzb.setUncaughtExceptionHandler(this.zzf);
                this.zzb.start();
            } else {
                zzfo.zza();
            }
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzgl
    public final void zzax() {
        if (Thread.currentThread() != this.zzc) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    public final <T> T zzd(AtomicReference<T> atomicReference, long j, String str, Runnable runnable) {
        synchronized (atomicReference) {
            this.zzs.zzaz().zzp(runnable);
            try {
                atomicReference.wait(j);
            } catch (InterruptedException e) {
                this.zzs.zzay().zzk().zza(str.length() != 0 ? "Interrupted waiting for ".concat(str) : new String("Interrupted waiting for "));
                return null;
            }
        }
        T t = atomicReference.get();
        if (t == null) {
            this.zzs.zzay().zzk().zza(str.length() != 0 ? "Timed out waiting for ".concat(str) : new String("Timed out waiting for "));
        }
        return t;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final boolean zzf() {
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.zzgl
    public final void zzg() {
        if (Thread.currentThread() != this.zzb) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public final <V> Future<V> zzh(Callable<V> callable) throws IllegalStateException {
        zzu();
        Preconditions.checkNotNull(callable);
        zzfn<?> zzfn = new zzfn<>(this, (Callable<?>) callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzb) {
            if (!this.zzd.isEmpty()) {
                this.zzs.zzay().zzk().zza("Callable skipped the worker queue.");
            }
            zzfn.run();
        } else {
            zzt(zzfn);
        }
        return zzfn;
    }

    public final <V> Future<V> zzi(Callable<V> callable) throws IllegalStateException {
        zzu();
        Preconditions.checkNotNull(callable);
        zzfn<?> zzfn = new zzfn<>(this, (Callable<?>) callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzb) {
            zzfn.run();
        } else {
            zzt(zzfn);
        }
        return zzfn;
    }

    public final void zzo(Runnable runnable) throws IllegalStateException {
        zzu();
        Preconditions.checkNotNull(runnable);
        zzfn<?> zzfn = new zzfn<>(this, runnable, false, "Task exception on network thread");
        synchronized (this.zzh) {
            this.zze.add(zzfn);
            zzfo zzfo = this.zzc;
            if (zzfo == null) {
                this.zzc = new zzfo(this, "Measurement Network", this.zze);
                this.zzc.setUncaughtExceptionHandler(this.zzg);
                this.zzc.start();
            } else {
                zzfo.zza();
            }
        }
    }

    public final void zzp(Runnable runnable) throws IllegalStateException {
        zzu();
        Preconditions.checkNotNull(runnable);
        zzt(new zzfn<>(this, runnable, false, "Task exception on worker thread"));
    }

    public final void zzq(Runnable runnable) throws IllegalStateException {
        zzu();
        Preconditions.checkNotNull(runnable);
        zzt(new zzfn<>(this, runnable, true, "Task exception on worker thread"));
    }

    public final boolean zzs() {
        return Thread.currentThread() == this.zzb;
    }
}
