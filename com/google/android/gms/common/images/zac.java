package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Objects;
import java.lang.ref.WeakReference;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zac extends zab {
    private WeakReference<ImageManager.OnImageLoadedListener> zac;

    public zac(ImageManager.OnImageLoadedListener onImageLoadedListener, Uri uri) {
        super(uri, 0);
        Asserts.checkNotNull(onImageLoadedListener);
        this.zac = new WeakReference<>(onImageLoadedListener);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zaa);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zac)) {
            return false;
        }
        zac zac = (zac) obj;
        ImageManager.OnImageLoadedListener onImageLoadedListener = this.zac.get();
        ImageManager.OnImageLoadedListener onImageLoadedListener2 = zac.zac.get();
        if (onImageLoadedListener2 == null || onImageLoadedListener == null || !Objects.equal(onImageLoadedListener2, onImageLoadedListener) || !Objects.equal(zac.zaa, this.zaa)) {
            return false;
        }
        return true;
    }

    @Override // com.google.android.gms.common.images.zab
    protected final void zaa(Drawable drawable, boolean z, boolean z2, boolean z3) {
        ImageManager.OnImageLoadedListener onImageLoadedListener;
        if (!z2 && (onImageLoadedListener = this.zac.get()) != null) {
            onImageLoadedListener.onImageLoaded(this.zaa.zaa, drawable, z3);
        }
    }
}
