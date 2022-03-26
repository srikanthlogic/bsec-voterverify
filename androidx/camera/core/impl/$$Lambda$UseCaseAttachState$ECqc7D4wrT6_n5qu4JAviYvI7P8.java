package androidx.camera.core.impl;

import androidx.camera.core.impl.UseCaseAttachState;
/* compiled from: lambda */
/* renamed from: androidx.camera.core.impl.-$$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8 implements UseCaseAttachState.AttachStateFilter {
    public static final /* synthetic */ $$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8 INSTANCE = new $$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8();

    private /* synthetic */ $$Lambda$UseCaseAttachState$ECqc7D4wrT6_n5qu4JAviYvI7P8() {
    }

    @Override // androidx.camera.core.impl.UseCaseAttachState.AttachStateFilter
    public final boolean filter(UseCaseAttachState.UseCaseAttachInfo useCaseAttachInfo) {
        return useCaseAttachInfo.getAttached();
    }
}
