package com.google.android.gms.measurement.internal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.internal.AccountType;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzan extends zzgm {
    private long zza;
    private String zzb;
    private AccountManager zzc;
    private Boolean zzd;
    private long zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzan(zzfs zzfs) {
        super(zzfs);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long zza() {
        zzg();
        return this.zze;
    }

    public final long zzb() {
        zzu();
        return this.zza;
    }

    public final String zzc() {
        zzu();
        return this.zzb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzd() {
        zzg();
        this.zzd = null;
        this.zze = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zze() {
        Account[] result;
        zzg();
        long currentTimeMillis = this.zzs.zzav().currentTimeMillis();
        if (currentTimeMillis - this.zze > 86400000) {
            this.zzd = null;
        }
        Boolean bool = this.zzd;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (ContextCompat.checkSelfPermission(this.zzs.zzau(), "android.permission.GET_ACCOUNTS") != 0) {
            this.zzs.zzay().zzm().zza("Permission error checking for dasher/unicorn accounts");
            this.zze = currentTimeMillis;
            this.zzd = false;
            return false;
        }
        if (this.zzc == null) {
            this.zzc = AccountManager.get(this.zzs.zzau());
        }
        try {
            result = this.zzc.getAccountsByTypeAndFeatures(AccountType.GOOGLE, new String[]{"service_HOSTED"}, null, null).getResult();
        } catch (AuthenticatorException | OperationCanceledException | IOException e) {
            this.zzs.zzay().zzh().zzb("Exception checking account types", e);
        }
        if (result == null || result.length <= 0) {
            Account[] result2 = this.zzc.getAccountsByTypeAndFeatures(AccountType.GOOGLE, new String[]{"service_uca"}, null, null).getResult();
            if (result2 != null && result2.length > 0) {
                this.zzd = true;
                this.zze = currentTimeMillis;
                return true;
            }
            this.zze = currentTimeMillis;
            this.zzd = false;
            return false;
        }
        this.zzd = true;
        this.zze = currentTimeMillis;
        return true;
    }

    @Override // com.google.android.gms.measurement.internal.zzgm
    protected final boolean zzf() {
        Calendar instance = Calendar.getInstance();
        this.zza = TimeUnit.MINUTES.convert((long) (instance.get(15) + instance.get(16)), TimeUnit.MILLISECONDS);
        Locale locale = Locale.getDefault();
        String lowerCase = locale.getLanguage().toLowerCase(Locale.ENGLISH);
        String lowerCase2 = locale.getCountry().toLowerCase(Locale.ENGLISH);
        StringBuilder sb = new StringBuilder(String.valueOf(lowerCase).length() + 1 + String.valueOf(lowerCase2).length());
        sb.append(lowerCase);
        sb.append("-");
        sb.append(lowerCase2);
        this.zzb = sb.toString();
        return false;
    }
}
