package androidx.camera.core;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SingleCloseImageProxy extends ForwardingImageProxy {
    private boolean mClosed = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingleCloseImageProxy(ImageProxy image) {
        super(image);
    }

    @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy, java.lang.AutoCloseable
    public synchronized void close() {
        if (!this.mClosed) {
            this.mClosed = true;
            super.close();
        }
    }
}
