package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public abstract class zzcb extends zzbn implements zzcc {
    public zzcb() {
        super("com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
    }

    public static zzcc asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface queryLocalInterface = obj.queryLocalInterface("com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
        if (queryLocalInterface instanceof zzcc) {
            return (zzcc) queryLocalInterface;
        }
        return new zzca(obj);
    }

    @Override // com.google.android.gms.internal.measurement.zzbn
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        zzcf zzcf;
        zzcf zzcf2 = null;
        zzcf zzcf3 = null;
        zzcf zzcf4 = null;
        zzci zzci = null;
        zzci zzci2 = null;
        zzci zzci3 = null;
        zzcf zzcf5 = null;
        zzcf zzcf6 = null;
        zzcf zzcf7 = null;
        zzcf zzcf8 = null;
        zzcf zzcf9 = null;
        zzcf zzcf10 = null;
        zzck zzck = null;
        zzcf zzcf11 = null;
        zzcf zzcf12 = null;
        zzcf zzcf13 = null;
        zzcf zzcf14 = null;
        switch (i) {
            case 1:
                initialize(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), (zzcl) zzbo.zza(parcel, zzcl.CREATOR), parcel.readLong());
                break;
            case 2:
                logEvent(parcel.readString(), parcel.readString(), (Bundle) zzbo.zza(parcel, Bundle.CREATOR), zzbo.zzf(parcel), zzbo.zzf(parcel), parcel.readLong());
                break;
            case 3:
                String readString = parcel.readString();
                String readString2 = parcel.readString();
                Bundle bundle = (Bundle) zzbo.zza(parcel, Bundle.CREATOR);
                IBinder readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder == null) {
                    zzcf = null;
                } else {
                    IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface instanceof zzcf) {
                        zzcf = (zzcf) queryLocalInterface;
                    } else {
                        zzcf = new zzcd(readStrongBinder);
                    }
                }
                logEventAndBundle(readString, readString2, bundle, zzcf, parcel.readLong());
                break;
            case 4:
                setUserProperty(parcel.readString(), parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), zzbo.zzf(parcel), parcel.readLong());
                break;
            case 5:
                String readString3 = parcel.readString();
                String readString4 = parcel.readString();
                boolean zzf = zzbo.zzf(parcel);
                IBinder readStrongBinder2 = parcel.readStrongBinder();
                if (readStrongBinder2 != null) {
                    IInterface queryLocalInterface2 = readStrongBinder2.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface2 instanceof zzcf) {
                        zzcf2 = (zzcf) queryLocalInterface2;
                    } else {
                        zzcf2 = new zzcd(readStrongBinder2);
                    }
                }
                getUserProperties(readString3, readString4, zzf, zzcf2);
                break;
            case 6:
                String readString5 = parcel.readString();
                IBinder readStrongBinder3 = parcel.readStrongBinder();
                if (readStrongBinder3 != null) {
                    IInterface queryLocalInterface3 = readStrongBinder3.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface3 instanceof zzcf) {
                        zzcf14 = (zzcf) queryLocalInterface3;
                    } else {
                        zzcf14 = new zzcd(readStrongBinder3);
                    }
                }
                getMaxUserProperties(readString5, zzcf14);
                break;
            case 7:
                setUserId(parcel.readString(), parcel.readLong());
                break;
            case 8:
                setConditionalUserProperty((Bundle) zzbo.zza(parcel, Bundle.CREATOR), parcel.readLong());
                break;
            case 9:
                clearConditionalUserProperty(parcel.readString(), parcel.readString(), (Bundle) zzbo.zza(parcel, Bundle.CREATOR));
                break;
            case 10:
                String readString6 = parcel.readString();
                String readString7 = parcel.readString();
                IBinder readStrongBinder4 = parcel.readStrongBinder();
                if (readStrongBinder4 != null) {
                    IInterface queryLocalInterface4 = readStrongBinder4.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface4 instanceof zzcf) {
                        zzcf13 = (zzcf) queryLocalInterface4;
                    } else {
                        zzcf13 = new zzcd(readStrongBinder4);
                    }
                }
                getConditionalUserProperties(readString6, readString7, zzcf13);
                break;
            case 11:
                setMeasurementEnabled(zzbo.zzf(parcel), parcel.readLong());
                break;
            case 12:
                resetAnalyticsData(parcel.readLong());
                break;
            case 13:
                setMinimumSessionDuration(parcel.readLong());
                break;
            case 14:
                setSessionTimeoutDuration(parcel.readLong());
                break;
            case 15:
                setCurrentScreen(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString(), parcel.readLong());
                break;
            case 16:
                IBinder readStrongBinder5 = parcel.readStrongBinder();
                if (readStrongBinder5 != null) {
                    IInterface queryLocalInterface5 = readStrongBinder5.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface5 instanceof zzcf) {
                        zzcf12 = (zzcf) queryLocalInterface5;
                    } else {
                        zzcf12 = new zzcd(readStrongBinder5);
                    }
                }
                getCurrentScreenName(zzcf12);
                break;
            case 17:
                IBinder readStrongBinder6 = parcel.readStrongBinder();
                if (readStrongBinder6 != null) {
                    IInterface queryLocalInterface6 = readStrongBinder6.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface6 instanceof zzcf) {
                        zzcf11 = (zzcf) queryLocalInterface6;
                    } else {
                        zzcf11 = new zzcd(readStrongBinder6);
                    }
                }
                getCurrentScreenClass(zzcf11);
                break;
            case 18:
                IBinder readStrongBinder7 = parcel.readStrongBinder();
                if (readStrongBinder7 != null) {
                    IInterface queryLocalInterface7 = readStrongBinder7.queryLocalInterface("com.google.android.gms.measurement.api.internal.IStringProvider");
                    if (queryLocalInterface7 instanceof zzck) {
                        zzck = (zzck) queryLocalInterface7;
                    } else {
                        zzck = new zzcj(readStrongBinder7);
                    }
                }
                setInstanceIdProvider(zzck);
                break;
            case 19:
                IBinder readStrongBinder8 = parcel.readStrongBinder();
                if (readStrongBinder8 != null) {
                    IInterface queryLocalInterface8 = readStrongBinder8.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface8 instanceof zzcf) {
                        zzcf10 = (zzcf) queryLocalInterface8;
                    } else {
                        zzcf10 = new zzcd(readStrongBinder8);
                    }
                }
                getCachedAppInstanceId(zzcf10);
                break;
            case 20:
                IBinder readStrongBinder9 = parcel.readStrongBinder();
                if (readStrongBinder9 != null) {
                    IInterface queryLocalInterface9 = readStrongBinder9.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface9 instanceof zzcf) {
                        zzcf9 = (zzcf) queryLocalInterface9;
                    } else {
                        zzcf9 = new zzcd(readStrongBinder9);
                    }
                }
                getAppInstanceId(zzcf9);
                break;
            case 21:
                IBinder readStrongBinder10 = parcel.readStrongBinder();
                if (readStrongBinder10 != null) {
                    IInterface queryLocalInterface10 = readStrongBinder10.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface10 instanceof zzcf) {
                        zzcf8 = (zzcf) queryLocalInterface10;
                    } else {
                        zzcf8 = new zzcd(readStrongBinder10);
                    }
                }
                getGmpAppId(zzcf8);
                break;
            case 22:
                IBinder readStrongBinder11 = parcel.readStrongBinder();
                if (readStrongBinder11 != null) {
                    IInterface queryLocalInterface11 = readStrongBinder11.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface11 instanceof zzcf) {
                        zzcf7 = (zzcf) queryLocalInterface11;
                    } else {
                        zzcf7 = new zzcd(readStrongBinder11);
                    }
                }
                generateEventId(zzcf7);
                break;
            case 23:
                beginAdUnitExposure(parcel.readString(), parcel.readLong());
                break;
            case 24:
                endAdUnitExposure(parcel.readString(), parcel.readLong());
                break;
            case 25:
                onActivityStarted(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                break;
            case 26:
                onActivityStopped(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                break;
            case 27:
                onActivityCreated(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), (Bundle) zzbo.zza(parcel, Bundle.CREATOR), parcel.readLong());
                break;
            case 28:
                onActivityDestroyed(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                break;
            case 29:
                onActivityPaused(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                break;
            case 30:
                onActivityResumed(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readLong());
                break;
            case 31:
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(parcel.readStrongBinder());
                IBinder readStrongBinder12 = parcel.readStrongBinder();
                if (readStrongBinder12 != null) {
                    IInterface queryLocalInterface12 = readStrongBinder12.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface12 instanceof zzcf) {
                        zzcf6 = (zzcf) queryLocalInterface12;
                    } else {
                        zzcf6 = new zzcd(readStrongBinder12);
                    }
                }
                onActivitySaveInstanceState(asInterface, zzcf6, parcel.readLong());
                break;
            case 32:
                Bundle bundle2 = (Bundle) zzbo.zza(parcel, Bundle.CREATOR);
                IBinder readStrongBinder13 = parcel.readStrongBinder();
                if (readStrongBinder13 != null) {
                    IInterface queryLocalInterface13 = readStrongBinder13.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface13 instanceof zzcf) {
                        zzcf5 = (zzcf) queryLocalInterface13;
                    } else {
                        zzcf5 = new zzcd(readStrongBinder13);
                    }
                }
                performAction(bundle2, zzcf5, parcel.readLong());
                break;
            case 33:
                logHealthData(parcel.readInt(), parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                break;
            case 34:
                IBinder readStrongBinder14 = parcel.readStrongBinder();
                if (readStrongBinder14 != null) {
                    IInterface queryLocalInterface14 = readStrongBinder14.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    if (queryLocalInterface14 instanceof zzci) {
                        zzci3 = (zzci) queryLocalInterface14;
                    } else {
                        zzci3 = new zzcg(readStrongBinder14);
                    }
                }
                setEventInterceptor(zzci3);
                break;
            case 35:
                IBinder readStrongBinder15 = parcel.readStrongBinder();
                if (readStrongBinder15 != null) {
                    IInterface queryLocalInterface15 = readStrongBinder15.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    if (queryLocalInterface15 instanceof zzci) {
                        zzci2 = (zzci) queryLocalInterface15;
                    } else {
                        zzci2 = new zzcg(readStrongBinder15);
                    }
                }
                registerOnMeasurementEventListener(zzci2);
                break;
            case 36:
                IBinder readStrongBinder16 = parcel.readStrongBinder();
                if (readStrongBinder16 != null) {
                    IInterface queryLocalInterface16 = readStrongBinder16.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    if (queryLocalInterface16 instanceof zzci) {
                        zzci = (zzci) queryLocalInterface16;
                    } else {
                        zzci = new zzcg(readStrongBinder16);
                    }
                }
                unregisterOnMeasurementEventListener(zzci);
                break;
            case 37:
                initForTests(zzbo.zzb(parcel));
                break;
            case 38:
                IBinder readStrongBinder17 = parcel.readStrongBinder();
                if (readStrongBinder17 != null) {
                    IInterface queryLocalInterface17 = readStrongBinder17.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface17 instanceof zzcf) {
                        zzcf4 = (zzcf) queryLocalInterface17;
                    } else {
                        zzcf4 = new zzcd(readStrongBinder17);
                    }
                }
                getTestFlag(zzcf4, parcel.readInt());
                break;
            case 39:
                setDataCollectionEnabled(zzbo.zzf(parcel));
                break;
            case 40:
                IBinder readStrongBinder18 = parcel.readStrongBinder();
                if (readStrongBinder18 != null) {
                    IInterface queryLocalInterface18 = readStrongBinder18.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    if (queryLocalInterface18 instanceof zzcf) {
                        zzcf3 = (zzcf) queryLocalInterface18;
                    } else {
                        zzcf3 = new zzcd(readStrongBinder18);
                    }
                }
                isDataCollectionEnabled(zzcf3);
                break;
            case 41:
            default:
                return false;
            case 42:
                setDefaultEventParameters((Bundle) zzbo.zza(parcel, Bundle.CREATOR));
                break;
            case 43:
                clearMeasurementEnabled(parcel.readLong());
                break;
            case 44:
                setConsent((Bundle) zzbo.zza(parcel, Bundle.CREATOR), parcel.readLong());
                break;
            case 45:
                setConsentThirdParty((Bundle) zzbo.zza(parcel, Bundle.CREATOR), parcel.readLong());
                break;
        }
        parcel2.writeNoException();
        return true;
    }
}
