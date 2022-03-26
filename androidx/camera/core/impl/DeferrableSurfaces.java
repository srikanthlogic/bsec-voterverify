package androidx.camera.core.impl;

import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public final class DeferrableSurfaces {
    private DeferrableSurfaces() {
    }

    public static ListenableFuture<List<Surface>> surfaceListWithTimeout(Collection<DeferrableSurface> deferrableSurfaces, boolean removeNullSurfaces, long timeout, Executor executor, ScheduledExecutorService scheduledExecutorService) {
        List<ListenableFuture<Surface>> listenableFutureSurfaces = new ArrayList<>();
        for (DeferrableSurface deferrableSurface : deferrableSurfaces) {
            listenableFutureSurfaces.add(deferrableSurface.getSurface());
        }
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(listenableFutureSurfaces, scheduledExecutorService, executor, timeout, removeNullSurfaces) { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurfaces$_eJ7iY3oHlcbhnrK9kAok8keF3w
            private final /* synthetic */ List f$0;
            private final /* synthetic */ ScheduledExecutorService f$1;
            private final /* synthetic */ Executor f$2;
            private final /* synthetic */ long f$3;
            private final /* synthetic */ boolean f$4;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r6;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return DeferrableSurfaces.lambda$surfaceListWithTimeout$3(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, completer);
            }
        });
    }

    public static /* synthetic */ Object lambda$surfaceListWithTimeout$3(List listenableFutureSurfaces, ScheduledExecutorService scheduledExecutorService, Executor executor, long timeout, final boolean removeNullSurfaces, final CallbackToFutureAdapter.Completer completer) throws Exception {
        ListenableFuture<List<Surface>> listenableFuture = Futures.successfulAsList(listenableFutureSurfaces);
        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(new Runnable(executor, listenableFuture, completer, timeout) { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurfaces$1M8fKlBzTzms6mhVK_pmWAC05CU
            private final /* synthetic */ Executor f$0;
            private final /* synthetic */ ListenableFuture f$1;
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$2;
            private final /* synthetic */ long f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.execute(new Runnable(this.f$2, this.f$3) { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurfaces$Yekn-mWFALhEwuFd18dTMaz7qws
                    private final /* synthetic */ CallbackToFutureAdapter.Completer f$1;
                    private final /* synthetic */ long f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        DeferrableSurfaces.lambda$surfaceListWithTimeout$0(ListenableFuture.this, this.f$1, this.f$2);
                    }
                });
            }
        }, timeout, TimeUnit.MILLISECONDS);
        completer.addCancellationListener(new Runnable() { // from class: androidx.camera.core.impl.-$$Lambda$DeferrableSurfaces$pmCedm8iLOMS-IHrIv-Uw2AJ038
            @Override // java.lang.Runnable
            public final void run() {
                ListenableFuture.this.cancel(true);
            }
        }, executor);
        Futures.addCallback(listenableFuture, new FutureCallback<List<Surface>>() { // from class: androidx.camera.core.impl.DeferrableSurfaces.1
            public void onSuccess(List<Surface> list) {
                ArrayList arrayList = new ArrayList(list);
                if (removeNullSurfaces) {
                    arrayList.removeAll(Collections.singleton(null));
                }
                completer.set(arrayList);
                scheduledFuture.cancel(true);
            }

            @Override // androidx.camera.core.impl.utils.futures.FutureCallback
            public void onFailure(Throwable t) {
                completer.set(Collections.unmodifiableList(Collections.emptyList()));
                scheduledFuture.cancel(true);
            }
        }, executor);
        return "surfaceList";
    }

    public static /* synthetic */ void lambda$surfaceListWithTimeout$0(ListenableFuture listenableFuture, CallbackToFutureAdapter.Completer completer, long timeout) {
        if (!listenableFuture.isDone()) {
            completer.setException(new TimeoutException("Cannot complete surfaceList within " + timeout));
            listenableFuture.cancel(true);
        }
    }

    public static boolean tryIncrementAll(List<DeferrableSurface> surfaceList) {
        try {
            incrementAll(surfaceList);
            return true;
        } catch (DeferrableSurface.SurfaceClosedException e) {
            return false;
        }
    }

    public static void incrementAll(List<DeferrableSurface> surfaceList) throws DeferrableSurface.SurfaceClosedException {
        if (!surfaceList.isEmpty()) {
            int i = 0;
            do {
                try {
                    surfaceList.get(i).incrementUseCount();
                    i++;
                } catch (DeferrableSurface.SurfaceClosedException e) {
                    for (int i2 = i - 1; i2 >= 0; i2--) {
                        surfaceList.get(i2).decrementUseCount();
                    }
                    throw e;
                }
            } while (i < surfaceList.size());
        }
    }

    public static void decrementAll(List<DeferrableSurface> surfaceList) {
        for (DeferrableSurface surface : surfaceList) {
            surface.decrementUseCount();
        }
    }
}
