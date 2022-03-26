package com.google.android.gms.internal.measurement;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzbt {
    private static final Method zza;
    private static final Method zzb;

    static {
        Method method;
        Method method2 = null;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                method = JobScheduler.class.getDeclaredMethod("scheduleAsPackage", JobInfo.class, String.class, Integer.TYPE, String.class);
            } catch (NoSuchMethodException e) {
                if (Log.isLoggable("JobSchedulerCompat", 6)) {
                    Log.e("JobSchedulerCompat", "No scheduleAsPackage method available, falling back to schedule");
                    method = null;
                } else {
                    method = null;
                }
            }
        } else {
            method = null;
        }
        zza = method;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                method2 = UserHandle.class.getDeclaredMethod("myUserId", new Class[0]);
            } catch (NoSuchMethodException e2) {
                if (Log.isLoggable("JobSchedulerCompat", 6)) {
                    Log.e("JobSchedulerCompat", "No myUserId method available");
                }
            }
        }
        zzb = method2;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static int zza(Context context, JobInfo jobInfo, String str, String str2) {
        int i;
        Integer num;
        Method method;
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler == null) {
            throw null;
        } else if (zza == null || context.checkSelfPermission("android.permission.UPDATE_DEVICE_STATS") != 0) {
            return jobScheduler.schedule(jobInfo);
        } else {
            Method method2 = zzb;
            if (method2 != null) {
                try {
                    num = (Integer) method2.invoke(UserHandle.class, new Object[0]);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    if (Log.isLoggable("JobSchedulerCompat", 6)) {
                        Log.e("JobSchedulerCompat", "myUserId invocation illegal", e);
                        i = 0;
                    } else {
                        i = 0;
                    }
                }
                if (num != null) {
                    i = num.intValue();
                    method = zza;
                    if (method != null) {
                        try {
                            Integer num2 = (Integer) method.invoke(jobScheduler, jobInfo, "com.google.android.gms", Integer.valueOf(i), "UploadAlarm");
                            if (num2 != null) {
                                return num2.intValue();
                            }
                            return 0;
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            Log.e("UploadAlarm", "error calling scheduleAsPackage", e2);
                        }
                    }
                    return jobScheduler.schedule(jobInfo);
                }
            }
            i = 0;
            method = zza;
            if (method != null) {
            }
            return jobScheduler.schedule(jobInfo);
        }
    }
}
