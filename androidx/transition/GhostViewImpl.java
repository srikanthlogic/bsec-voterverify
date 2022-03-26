package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes.dex */
interface GhostViewImpl {
    void reserveEndViewTransition(ViewGroup viewGroup, View view);

    @Override // androidx.transition.GhostViewImpl
    void setVisibility(int i);
}
