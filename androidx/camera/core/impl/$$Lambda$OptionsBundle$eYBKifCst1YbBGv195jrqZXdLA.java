package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.Comparator;
/* compiled from: lambda */
/* renamed from: androidx.camera.core.impl.-$$Lambda$OptionsBundle$eYBKifCst1-YbBGv195jrqZXdLA  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$OptionsBundle$eYBKifCst1YbBGv195jrqZXdLA implements Comparator {
    public static final /* synthetic */ $$Lambda$OptionsBundle$eYBKifCst1YbBGv195jrqZXdLA INSTANCE = new $$Lambda$OptionsBundle$eYBKifCst1YbBGv195jrqZXdLA();

    private /* synthetic */ $$Lambda$OptionsBundle$eYBKifCst1YbBGv195jrqZXdLA() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return ((Config.Option) obj).getId().compareTo(((Config.Option) obj2).getId());
    }
}
