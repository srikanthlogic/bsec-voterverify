package com.google.android.gms.location;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.internal.location.zzaz;
import com.google.android.gms.internal.location.zzba;
import com.google.android.gms.internal.location.zzbj;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public class FusedLocationProviderClient extends GoogleApi<Api.ApiOptions.NoOptions> {
    public static final String KEY_MOCK_LOCATION;
    public static final String KEY_VERTICAL_ACCURACY;

    public FusedLocationProviderClient(Activity activity) {
        super(activity, LocationServices.API, Api.ApiOptions.NO_OPTIONS, (StatusExceptionMapper) new ApiExceptionMapper());
    }

    private final Task<Void> zze(zzba zzba, LocationCallback locationCallback, Looper looper, zzan zzan, int i) {
        ListenerHolder createListenerHolder = ListenerHolders.createListenerHolder(locationCallback, zzbj.zza(looper), LocationCallback.class.getSimpleName());
        zzak zzak = new zzak(this, createListenerHolder);
        return doRegisterEventListener(RegistrationMethods.builder().register(new RemoteCall(this, zzak, locationCallback, zzan, zzba, createListenerHolder) { // from class: com.google.android.gms.location.zzae
            private final FusedLocationProviderClient zza;
            private final zzap zzb;
            private final LocationCallback zzc;
            private final zzan zzd;
            private final zzba zze;
            private final ListenerHolder zzf;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
                this.zzb = r2;
                this.zzc = r3;
                this.zzd = r4;
                this.zze = r5;
                this.zzf = r6;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                this.zza.zzb(this.zzb, this.zzc, this.zzd, this.zze, this.zzf, (zzaz) obj, (TaskCompletionSource) obj2);
            }
        }).unregister(zzak).withHolder(createListenerHolder).setMethodKey(i).build());
    }

    public Task<Void> flushLocations() {
        return doWrite(TaskApiCall.builder().run(zzw.zza).setMethodKey(2422).build());
    }

    public Task<Location> getCurrentLocation(int i, CancellationToken cancellationToken) {
        LocationRequest create = LocationRequest.create();
        create.setPriority(i);
        create.setInterval(0);
        create.setFastestInterval(0);
        create.setExpirationDuration(30000);
        zzba zza = zzba.zza(null, create);
        zza.zzd(true);
        zza.zzb(10000);
        Task doRead = doRead(TaskApiCall.builder().run(new RemoteCall(this, cancellationToken, zza) { // from class: com.google.android.gms.location.zzab
            private final FusedLocationProviderClient zza;
            private final CancellationToken zzb;
            private final zzba zzc;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
                this.zzb = r2;
                this.zzc = r3;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                this.zza.zzc(this.zzb, this.zzc, (zzaz) obj, (TaskCompletionSource) obj2);
            }
        }).setFeatures(zzu.zzd).setMethodKey(2415).build());
        if (cancellationToken == null) {
            return doRead;
        }
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource(cancellationToken);
        doRead.continueWithTask(new Continuation(taskCompletionSource) { // from class: com.google.android.gms.location.zzac
            private final TaskCompletionSource zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.tasks.Continuation
            public final Object then(Task task) {
                TaskCompletionSource taskCompletionSource2 = this.zza;
                String str = FusedLocationProviderClient.KEY_MOCK_LOCATION;
                if (task.isSuccessful()) {
                    taskCompletionSource2.trySetResult((Location) task.getResult());
                } else {
                    Exception exception = task.getException();
                    if (exception != null) {
                        taskCompletionSource2.setException(exception);
                    }
                }
                return taskCompletionSource2.getTask();
            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<Location> getLastLocation() {
        return doRead(TaskApiCall.builder().run(new RemoteCall(this) { // from class: com.google.android.gms.location.zzv
            private final FusedLocationProviderClient zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                this.zza.zzd((zzaz) obj, (TaskCompletionSource) obj2);
            }
        }).setMethodKey(2414).build());
    }

    public Task<LocationAvailability> getLocationAvailability() {
        return doRead(TaskApiCall.builder().run(zzad.zza).setMethodKey(2416).build());
    }

    public Task<Void> removeLocationUpdates(PendingIntent pendingIntent) {
        return doWrite(TaskApiCall.builder().run(new RemoteCall(pendingIntent) { // from class: com.google.android.gms.location.zzag
            private final PendingIntent zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                ((zzaz) obj).zzG(this.zza, new zzao((TaskCompletionSource) obj2));
            }
        }).setMethodKey(2418).build());
    }

    public Task<Void> requestLocationUpdates(LocationRequest locationRequest, PendingIntent pendingIntent) {
        return doWrite(TaskApiCall.builder().run(new RemoteCall(this, zzba.zza(null, locationRequest), pendingIntent) { // from class: com.google.android.gms.location.zzaf
            private final FusedLocationProviderClient zza;
            private final zzba zzb;
            private final PendingIntent zzc;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
                this.zzb = r2;
                this.zzc = r3;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                this.zza.zza(this.zzb, this.zzc, (zzaz) obj, (TaskCompletionSource) obj2);
            }
        }).setMethodKey(2417).build());
    }

    public Task<Void> setMockLocation(Location location) {
        return doWrite(TaskApiCall.builder().run(new RemoteCall(location) { // from class: com.google.android.gms.location.zzai
            private final Location zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                Location location2 = this.zza;
                String str = FusedLocationProviderClient.KEY_MOCK_LOCATION;
                ((zzaz) obj).zzJ(location2);
                ((TaskCompletionSource) obj2).setResult(null);
            }
        }).setMethodKey(2421).build());
    }

    public Task<Void> setMockMode(boolean z) {
        return doWrite(TaskApiCall.builder().run(new RemoteCall(z) { // from class: com.google.android.gms.location.zzah
            private final boolean zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                boolean z2 = this.zza;
                String str = FusedLocationProviderClient.KEY_MOCK_LOCATION;
                ((zzaz) obj).zzI(z2);
                ((TaskCompletionSource) obj2).setResult(null);
            }
        }).setMethodKey(2420).build());
    }

    public final /* synthetic */ void zza(zzba zzba, PendingIntent pendingIntent, zzaz zzaz, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzao zzao = new zzao(taskCompletionSource);
        zzba.zzc(getContextAttributionTag());
        zzaz.zzD(zzba, pendingIntent, zzao);
    }

    public final /* synthetic */ void zzb(zzap zzap, LocationCallback locationCallback, zzan zzan, zzba zzba, ListenerHolder listenerHolder, zzaz zzaz, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzam zzam = new zzam(taskCompletionSource, new zzan(this, zzap, locationCallback, zzan) { // from class: com.google.android.gms.location.zzx
            private final FusedLocationProviderClient zza;
            private final zzap zzb;
            private final LocationCallback zzc;
            private final zzan zzd;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
                this.zzb = r2;
                this.zzc = r3;
                this.zzd = r4;
            }

            @Override // com.google.android.gms.location.zzan
            public final void zza() {
                FusedLocationProviderClient fusedLocationProviderClient = this.zza;
                zzap zzap2 = this.zzb;
                LocationCallback locationCallback2 = this.zzc;
                zzan zzan2 = this.zzd;
                zzap2.zzb(false);
                fusedLocationProviderClient.removeLocationUpdates(locationCallback2);
                if (zzan2 != null) {
                    zzan2.zza();
                }
            }
        });
        zzba.zzc(getContextAttributionTag());
        zzaz.zzB(zzba, listenerHolder, zzam);
    }

    public final /* synthetic */ void zzc(CancellationToken cancellationToken, zzba zzba, zzaz zzaz, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzaj zzaj = new zzaj(this, taskCompletionSource);
        if (cancellationToken != null) {
            cancellationToken.onCanceledRequested(new OnTokenCanceledListener(this, zzaj) { // from class: com.google.android.gms.location.zzy
                private final FusedLocationProviderClient zza;
                private final LocationCallback zzb;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.zza = r1;
                    this.zzb = r2;
                }

                @Override // com.google.android.gms.tasks.OnTokenCanceledListener
                public final void onCanceled() {
                    this.zza.removeLocationUpdates(this.zzb);
                }
            });
        }
        zze(zzba, zzaj, Looper.getMainLooper(), new zzan(taskCompletionSource) { // from class: com.google.android.gms.location.zzz
            private final TaskCompletionSource zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.location.zzan
            public final void zza() {
                TaskCompletionSource taskCompletionSource2 = this.zza;
                String str = FusedLocationProviderClient.KEY_MOCK_LOCATION;
                taskCompletionSource2.trySetResult(null);
            }
        }, 2437).continueWithTask(new Continuation(taskCompletionSource) { // from class: com.google.android.gms.location.zzaa
            private final TaskCompletionSource zza;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.zza = r1;
            }

            @Override // com.google.android.gms.tasks.Continuation
            public final Object then(Task task) {
                TaskCompletionSource taskCompletionSource2 = this.zza;
                String str = FusedLocationProviderClient.KEY_MOCK_LOCATION;
                if (!task.isSuccessful()) {
                    if (task.getException() != null) {
                        Exception exception = task.getException();
                        if (exception != null) {
                            taskCompletionSource2.setException(exception);
                        }
                    } else {
                        taskCompletionSource2.trySetResult(null);
                    }
                }
                return taskCompletionSource2.getTask();
            }
        });
    }

    public final /* synthetic */ void zzd(zzaz zzaz, TaskCompletionSource taskCompletionSource) throws RemoteException {
        taskCompletionSource.setResult(zzaz.zzz(getContextAttributionTag()));
    }

    public FusedLocationProviderClient(Context context) {
        super(context, LocationServices.API, Api.ApiOptions.NO_OPTIONS, new ApiExceptionMapper());
    }

    public Task<Void> removeLocationUpdates(LocationCallback locationCallback) {
        return TaskUtil.toVoidTaskThatFailsOnFalse(doUnregisterEventListener(ListenerHolders.createListenerKey(locationCallback, LocationCallback.class.getSimpleName())));
    }

    public Task<Void> requestLocationUpdates(LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
        return zze(zzba.zza(null, locationRequest), locationCallback, looper, null, 2436);
    }
}
