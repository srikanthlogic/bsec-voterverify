package com.google.android.gms.auth.api.signin;

import android.os.Bundle;
import com.google.android.gms.common.api.Scope;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public interface GoogleSignInOptionsExtension {
    public static final int FITNESS;
    public static final int GAMES;

    int getExtensionType();

    List<Scope> getImpliedScopes();

    Bundle toBundle();
}
