package com.google.android.gms.measurement.internal;

import android.os.Process;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.BlockingQueue;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzfo extends Thread {
    final /* synthetic */ zzfp zza;
    private final BlockingQueue<zzfn<?>> zzc;
    private boolean zzd = false;
    private final Object zzb = new Object();

    public zzfo(zzfp zzfp, String str, BlockingQueue<zzfn<?>> blockingQueue) {
        this.zza = zzfp;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(blockingQueue);
        this.zzc = blockingQueue;
        setName(str);
    }

    private final void zzb() {
        synchronized (this.zza.zzh) {
            if (!this.zzd) {
                this.zza.zzi.release();
                this.zza.zzh.notifyAll();
                if (this == this.zza.zzb) {
                    this.zza.zzb = null;
                } else if (this == this.zza.zzc) {
                    this.zza.zzc = null;
                } else {
                    this.zza.zzs.zzay().zzd().zza("Current scheduler thread is neither worker nor network");
                }
                this.zzd = true;
            }
        }
    }

    private final void zzc(InterruptedException interruptedException) {
        this.zza.zzs.zzay().zzk().zzb(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        int i;
        boolean z = false;
        while (!z) {
            try {
                this.zza.zzi.acquire();
                z = true;
            } catch (InterruptedException e) {
                zzc(e);
            }
        }
        try {
            int threadPriority = Process.getThreadPriority(Process.myTid());
            while (true) {
                zzfn<?> poll = this.zzc.poll();
                if (poll == null) {
                    synchronized (this.zzb) {
                        if (this.zzc.peek() == null) {
                            boolean unused = this.zza.zzj;
                            try {
                                this.zzb.wait(30000);
                            } catch (InterruptedException e2) {
                                zzc(e2);
                            }
                        }
                    }
                    synchronized (this.zza.zzh) {
                        if (this.zzc.peek() == null) {
                            break;
                        }
                    }
                } else {
                    if (true != poll.zza) {
                        i = 10;
                    } else {
                        i = threadPriority;
                    }
                    Process.setThreadPriority(i);
                    poll.run();
                }
            }
            if (this.zza.zzs.zzf().zzs(null, zzdw.zzak)) {
                zzb();
            }
        } finally {
            zzb();
        }
    }

    public final void zza() {
        synchronized (this.zzb) {
            this.zzb.notifyAll();
        }
    }
}
