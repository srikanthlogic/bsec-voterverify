package com.google.firebase;

import android.content.Context;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* compiled from: lambda */
/* renamed from: com.google.firebase.-$$Lambda$FirebaseCommonRegistrar$pGT1R--cP4RapBpOq2V73IRqI1I  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$FirebaseCommonRegistrar$pGT1RcP4RapBpOq2V73IRqI1I implements LibraryVersionComponent.VersionExtractor {
    public static final /* synthetic */ $$Lambda$FirebaseCommonRegistrar$pGT1RcP4RapBpOq2V73IRqI1I INSTANCE = new $$Lambda$FirebaseCommonRegistrar$pGT1RcP4RapBpOq2V73IRqI1I();

    private /* synthetic */ $$Lambda$FirebaseCommonRegistrar$pGT1RcP4RapBpOq2V73IRqI1I() {
    }

    @Override // com.google.firebase.platforminfo.LibraryVersionComponent.VersionExtractor
    public final String extract(Object obj) {
        return FirebaseCommonRegistrar.lambda$getComponents$1((Context) obj);
    }
}
