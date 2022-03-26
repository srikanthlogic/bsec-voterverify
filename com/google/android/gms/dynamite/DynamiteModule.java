package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import dalvik.system.DelegateLastClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
public final class DynamiteModule {
    private static Boolean zza;
    private static zzl zzb;
    private static zzn zzc;
    private static String zzd;
    private final Context zzj;
    private static int zze = -1;
    private static final ThreadLocal<zza> zzf = new ThreadLocal<>();
    private static final ThreadLocal<Long> zzg = new zza();
    private static final VersionPolicy.zzb zzh = new zzb();
    public static final VersionPolicy PREFER_REMOTE = new zze();
    public static final VersionPolicy PREFER_LOCAL = new zzd();
    public static final VersionPolicy PREFER_REMOTE_VERSION_NO_FORCE_STAGING = new zzg();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzf();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zzi();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzh();
    private static final VersionPolicy zzi = new zzj();

    /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
    /* loaded from: classes.dex */
    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
    /* loaded from: classes.dex */
    public interface VersionPolicy {

        /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
        /* loaded from: classes.dex */
        public static class zza {
            public int zza = 0;
            public int zzb = 0;
            public int zzc = 0;
        }

        /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
        /* loaded from: classes.dex */
        public interface zzb {
            int zza(Context context, String str);

            int zza(Context context, String str, boolean z) throws LoadingException;
        }

        zza zza(Context context, String str, zzb zzb2) throws LoadingException;
    }

    /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
    /* loaded from: classes.dex */
    public static class zza {
        public Cursor zza;

        private zza() {
        }

        /* synthetic */ zza(zza zza) {
            this();
        }
    }

    public static DynamiteModule load(Context context, VersionPolicy versionPolicy, String str) throws LoadingException {
        zza zza2 = zzf.get();
        zza zza3 = new zza(null);
        zzf.set(zza3);
        long longValue = zzg.get().longValue();
        try {
            zzg.set(Long.valueOf(SystemClock.elapsedRealtime()));
            VersionPolicy.zza zza4 = versionPolicy.zza(context, str, zzh);
            int i = zza4.zza;
            int i2 = zza4.zzb;
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 68 + String.valueOf(str).length());
            sb.append("Considering local module ");
            sb.append(str);
            sb.append(":");
            sb.append(i);
            sb.append(" and remote module ");
            sb.append(str);
            sb.append(":");
            sb.append(i2);
            Log.i("DynamiteModule", sb.toString());
            if (zza4.zzc == 0 || ((zza4.zzc == -1 && zza4.zza == 0) || (zza4.zzc == 1 && zza4.zzb == 0))) {
                int i3 = zza4.zza;
                int i4 = zza4.zzb;
                StringBuilder sb2 = new StringBuilder(91);
                sb2.append("No acceptable module found. Local version is ");
                sb2.append(i3);
                sb2.append(" and remote version is ");
                sb2.append(i4);
                sb2.append(".");
                throw new LoadingException(sb2.toString(), (zza) null);
            } else if (zza4.zzc == -1) {
                DynamiteModule zza5 = zza(context, str);
                if (longValue == 0) {
                    zzg.remove();
                } else {
                    zzg.set(Long.valueOf(longValue));
                }
                if (zza3.zza != null) {
                    zza3.zza.close();
                }
                zzf.set(zza2);
                return zza5;
            } else if (zza4.zzc == 1) {
                try {
                    DynamiteModule zza6 = zza(context, str, zza4.zzb);
                    if (longValue == 0) {
                        zzg.remove();
                    } else {
                        zzg.set(Long.valueOf(longValue));
                    }
                    if (zza3.zza != null) {
                        zza3.zza.close();
                    }
                    zzf.set(zza2);
                    return zza6;
                } catch (LoadingException e) {
                    String valueOf = String.valueOf(e.getMessage());
                    Log.w("DynamiteModule", valueOf.length() != 0 ? "Failed to load remote module: ".concat(valueOf) : new String("Failed to load remote module: "));
                    if (zza4.zza == 0 || versionPolicy.zza(context, str, new zzb(zza4.zza, 0)).zzc != -1) {
                        throw new LoadingException("Remote load failed. No local fallback found.", e, null);
                    }
                    DynamiteModule zza7 = zza(context, str);
                    if (longValue == 0) {
                        zzg.remove();
                    } else {
                        zzg.set(Long.valueOf(longValue));
                    }
                    if (zza3.zza != null) {
                        zza3.zza.close();
                    }
                    zzf.set(zza2);
                    return zza7;
                }
            } else {
                int i5 = zza4.zzc;
                StringBuilder sb3 = new StringBuilder(47);
                sb3.append("VersionPolicy returned invalid code:");
                sb3.append(i5);
                throw new LoadingException(sb3.toString(), (zza) null);
            }
        } catch (Throwable th) {
            if (longValue == 0) {
                zzg.remove();
            } else {
                zzg.set(Long.valueOf(longValue));
            }
            if (zza3.zza != null) {
                zza3.zza.close();
            }
            zzf.set(zza2);
            throw th;
        }
    }

    /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
    /* loaded from: classes.dex */
    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ LoadingException(String str, zza zza) {
            this(str);
        }

        /* synthetic */ LoadingException(String str, Throwable th, zza zza) {
            this(str, th);
        }
    }

    /* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
    /* loaded from: classes.dex */
    public static class zzb implements VersionPolicy.zzb {
        private final int zza;
        private final int zzb = 0;

        public zzb(int i, int i2) {
            this.zza = i;
        }

        @Override // com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.zzb
        public final int zza(Context context, String str, boolean z) {
            return 0;
        }

        @Override // com.google.android.gms.dynamite.DynamiteModule.VersionPolicy.zzb
        public final int zza(Context context, String str) {
            return this.zza;
        }
    }

    public static int getLocalVersion(Context context, String str) {
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 61);
            sb.append("com.google.android.gms.dynamite.descriptors.");
            sb.append(str);
            sb.append(".ModuleDescriptor");
            Class<?> loadClass = classLoader.loadClass(sb.toString());
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (Objects.equal(declaredField.get(null), str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 51 + String.valueOf(str).length());
            sb2.append("Module descriptor id '");
            sb2.append(valueOf);
            sb2.append("' didn't match expected id '");
            sb2.append(str);
            sb2.append("'");
            Log.e("DynamiteModule", sb2.toString());
            return 0;
        } catch (ClassNotFoundException e) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(str).length() + 45);
            sb3.append("Local module descriptor class for ");
            sb3.append(str);
            sb3.append(" not found.");
            Log.w("DynamiteModule", sb3.toString());
            return 0;
        } catch (Exception e2) {
            String valueOf2 = String.valueOf(e2.getMessage());
            Log.e("DynamiteModule", valueOf2.length() != 0 ? "Failed to load module descriptor class: ".concat(valueOf2) : new String("Failed to load module descriptor class: "));
            return 0;
        }
    }

    public static int zza(Context context, String str, boolean z) {
        Field declaredField;
        ClassLoader classLoader;
        try {
            synchronized (DynamiteModule.class) {
                Boolean bool = zza;
                if (bool == null) {
                    try {
                        declaredField = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName()).getDeclaredField("sClassLoader");
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                        String valueOf = String.valueOf(e);
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 30);
                        sb.append("Failed to load module via V2: ");
                        sb.append(valueOf);
                        Log.w("DynamiteModule", sb.toString());
                        bool = Boolean.FALSE;
                    }
                    synchronized (declaredField.getDeclaringClass()) {
                        ClassLoader classLoader2 = (ClassLoader) declaredField.get(null);
                        if (classLoader2 != null) {
                            if (classLoader2 == ClassLoader.getSystemClassLoader()) {
                                bool = Boolean.FALSE;
                            } else {
                                try {
                                    zza(classLoader2);
                                } catch (LoadingException e2) {
                                }
                                bool = Boolean.TRUE;
                            }
                        } else if ("com.google.android.gms".equals(context.getApplicationContext().getPackageName())) {
                            declaredField.set(null, ClassLoader.getSystemClassLoader());
                            bool = Boolean.FALSE;
                        } else {
                            try {
                                int zzc2 = zzc(context, str, z);
                                if (zzd != null && !zzd.isEmpty()) {
                                    if (Build.VERSION.SDK_INT >= 29) {
                                        classLoader = new DelegateLastClassLoader((String) Preconditions.checkNotNull(zzd), ClassLoader.getSystemClassLoader());
                                    } else {
                                        classLoader = new zzc((String) Preconditions.checkNotNull(zzd), ClassLoader.getSystemClassLoader());
                                    }
                                    zza(classLoader);
                                    declaredField.set(null, classLoader);
                                    zza = Boolean.TRUE;
                                    return zzc2;
                                }
                                return zzc2;
                            } catch (LoadingException e3) {
                                declaredField.set(null, ClassLoader.getSystemClassLoader());
                                bool = Boolean.FALSE;
                            }
                        }
                        zza = bool;
                    }
                }
                if (!bool.booleanValue()) {
                    return zzb(context, str, z);
                }
                try {
                    return zzc(context, str, z);
                } catch (LoadingException e4) {
                    String valueOf2 = String.valueOf(e4.getMessage());
                    Log.w("DynamiteModule", valueOf2.length() != 0 ? "Failed to retrieve remote module version: ".concat(valueOf2) : new String("Failed to retrieve remote module version: "));
                    return 0;
                }
            }
        } catch (Throwable th) {
            CrashUtils.addDynamiteErrorToDropBox(context, th);
            throw th;
        }
    }

    private static int zzb(Context context, String str, boolean z) {
        Throwable th;
        RemoteException e;
        zzl zza2 = zza(context);
        if (zza2 == null) {
            return 0;
        }
        try {
            Cursor cursor = null;
            try {
                int zzb2 = zza2.zzb();
                if (zzb2 >= 3) {
                    Cursor cursor2 = (Cursor) ObjectWrapper.unwrap(zza2.zza(ObjectWrapper.wrap(context), str, z, zzg.get().longValue()));
                    if (cursor2 != null) {
                        try {
                            if (cursor2.moveToFirst()) {
                                int i = cursor2.getInt(0);
                                if (i > 0 && zza(cursor2)) {
                                    cursor2 = null;
                                }
                                if (cursor2 != null) {
                                    cursor2.close();
                                }
                                return i;
                            }
                        } catch (RemoteException e2) {
                            e = e2;
                            cursor = cursor2;
                            String valueOf = String.valueOf(e.getMessage());
                            Log.w("DynamiteModule", valueOf.length() != 0 ? "Failed to retrieve remote module version: ".concat(valueOf) : new String("Failed to retrieve remote module version: "));
                            if (cursor != null) {
                                cursor.close();
                            }
                            return 0;
                        } catch (Throwable th2) {
                            th = th2;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            throw th;
                        }
                    }
                    Log.w("DynamiteModule", "Failed to retrieve remote module version.");
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return 0;
                } else if (zzb2 == 2) {
                    Log.w("DynamiteModule", "IDynamite loader version = 2, no high precision latency measurement.");
                    return zza2.zzb(ObjectWrapper.wrap(context), str, z);
                } else {
                    Log.w("DynamiteModule", "IDynamite loader version < 2, falling back to getModuleVersion2");
                    return zza2.zza(ObjectWrapper.wrap(context), str, z);
                }
            } catch (RemoteException e3) {
                e = e3;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00b0  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static int zzc(Context context, String str, boolean z) throws LoadingException {
        Cursor cursor;
        Exception e;
        Cursor cursor2 = null;
        try {
            Cursor query = context.getContentResolver().query(new Uri.Builder().scheme("content").authority("com.google.android.gms.chimera").path(z ? "api_force_staging" : "api").appendPath(str).appendQueryParameter("requestStartTime", String.valueOf(zzg.get().longValue())).build(), null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        if (i > 0) {
                            synchronized (DynamiteModule.class) {
                                zzd = query.getString(2);
                                int columnIndex = query.getColumnIndex("loaderVersion");
                                if (columnIndex >= 0) {
                                    zze = query.getInt(columnIndex);
                                }
                            }
                            if (zza(query)) {
                                query = null;
                            }
                        }
                        if (query != null) {
                            query.close();
                        }
                        return i;
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursor = query;
                    try {
                        if (e instanceof LoadingException) {
                            throw e;
                        }
                        throw new LoadingException("V2 version check failed", e, null);
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = query;
                    if (cursor2 != null) {
                    }
                    throw th;
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new LoadingException("Failed to connect to dynamite module ContentResolver.", (zza) null);
        } catch (Exception e3) {
            e = e3;
            cursor = null;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private static boolean zza(Cursor cursor) {
        zza zza2 = zzf.get();
        if (zza2 == null || zza2.zza != null) {
            return false;
        }
        zza2.zza = cursor;
        return true;
    }

    public static int getRemoteVersion(Context context, String str) {
        return zza(context, str, false);
    }

    private static DynamiteModule zza(Context context, String str) {
        String valueOf = String.valueOf(str);
        Log.i("DynamiteModule", valueOf.length() != 0 ? "Selected local version of ".concat(valueOf) : new String("Selected local version of "));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        Boolean bool;
        IObjectWrapper iObjectWrapper;
        try {
            synchronized (DynamiteModule.class) {
                bool = zza;
            }
            if (bool == null) {
                throw new LoadingException("Failed to determine which loading route to use.", (zza) null);
            } else if (bool.booleanValue()) {
                return zzb(context, str, i);
            } else {
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
                sb.append("Selected remote version of ");
                sb.append(str);
                sb.append(", version >= ");
                sb.append(i);
                Log.i("DynamiteModule", sb.toString());
                zzl zza2 = zza(context);
                if (zza2 != null) {
                    int zzb2 = zza2.zzb();
                    if (zzb2 >= 3) {
                        zza zza3 = zzf.get();
                        if (zza3 != null) {
                            iObjectWrapper = zza2.zza(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(zza3.zza));
                        } else {
                            throw new LoadingException("No cached result cursor holder", (zza) null);
                        }
                    } else if (zzb2 == 2) {
                        Log.w("DynamiteModule", "IDynamite loader version = 2");
                        iObjectWrapper = zza2.zzb(ObjectWrapper.wrap(context), str, i);
                    } else {
                        Log.w("DynamiteModule", "Dynamite loader version < 2, falling back to createModuleContext");
                        iObjectWrapper = zza2.zza(ObjectWrapper.wrap(context), str, i);
                    }
                    if (ObjectWrapper.unwrap(iObjectWrapper) != null) {
                        return new DynamiteModule((Context) ObjectWrapper.unwrap(iObjectWrapper));
                    }
                    throw new LoadingException("Failed to load remote module.", (zza) null);
                }
                throw new LoadingException("Failed to create IDynamiteLoader.", (zza) null);
            }
        } catch (RemoteException e) {
            throw new LoadingException("Failed to load remote module.", e, null);
        } catch (LoadingException e2) {
            throw e2;
        } catch (Throwable th) {
            CrashUtils.addDynamiteErrorToDropBox(context, th);
            throw new LoadingException("Failed to load remote module.", th, null);
        }
    }

    private static zzl zza(Context context) {
        zzl zzl;
        synchronized (DynamiteModule.class) {
            if (zzb != null) {
                return zzb;
            }
            try {
                IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                if (iBinder == null) {
                    zzl = null;
                } else {
                    IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                    if (queryLocalInterface instanceof zzl) {
                        zzl = (zzl) queryLocalInterface;
                    } else {
                        zzl = new zzk(iBinder);
                    }
                }
            } catch (Exception e) {
                String valueOf = String.valueOf(e.getMessage());
                Log.e("DynamiteModule", valueOf.length() != 0 ? "Failed to load IDynamiteLoader from GmsCore: ".concat(valueOf) : new String("Failed to load IDynamiteLoader from GmsCore: "));
            }
            if (zzl == null) {
                return null;
            }
            zzb = zzl;
            return zzl;
        }
    }

    public final Context getModuleContext() {
        return this.zzj;
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException, RemoteException {
        zzn zzn;
        IObjectWrapper iObjectWrapper;
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        synchronized (DynamiteModule.class) {
            zzn = zzc;
        }
        if (zzn != null) {
            zza zza2 = zzf.get();
            if (zza2 == null || zza2.zza == null) {
                throw new LoadingException("No result cursor", (zza) null);
            }
            Context applicationContext = context.getApplicationContext();
            Cursor cursor = zza2.zza;
            ObjectWrapper.wrap(null);
            if (zza().booleanValue()) {
                Log.v("DynamiteModule", "Dynamite loader version >= 2, using loadModule2NoCrashUtils");
                iObjectWrapper = zzn.zzb(ObjectWrapper.wrap(applicationContext), str, i, ObjectWrapper.wrap(cursor));
            } else {
                Log.w("DynamiteModule", "Dynamite loader version < 2, falling back to loadModule2");
                iObjectWrapper = zzn.zza(ObjectWrapper.wrap(applicationContext), str, i, ObjectWrapper.wrap(cursor));
            }
            Context context2 = (Context) ObjectWrapper.unwrap(iObjectWrapper);
            if (context2 != null) {
                return new DynamiteModule(context2);
            }
            throw new LoadingException("Failed to get module context", (zza) null);
        }
        throw new LoadingException("DynamiteLoaderV2 was not cached.", (zza) null);
    }

    private static Boolean zza() {
        Boolean valueOf;
        synchronized (DynamiteModule.class) {
            valueOf = Boolean.valueOf(zze >= 2);
        }
        return valueOf;
    }

    private static void zza(ClassLoader classLoader) throws LoadingException {
        zzn zzn;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzn = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                if (queryLocalInterface instanceof zzn) {
                    zzn = (zzn) queryLocalInterface;
                } else {
                    zzn = new zzm(iBinder);
                }
            }
            zzc = zzn;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e, null);
        }
    }

    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzj.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String valueOf = String.valueOf(str);
            throw new LoadingException(valueOf.length() != 0 ? "Failed to instantiate module class: ".concat(valueOf) : new String("Failed to instantiate module class: "), e, null);
        }
    }

    private DynamiteModule(Context context) {
        this.zzj = (Context) Preconditions.checkNotNull(context);
    }
}
