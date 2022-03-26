package androidx.camera.core;

import android.util.SparseArray;
import androidx.camera.core.impl.ImageProxyBundle;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SettableImageProxyBundle implements ImageProxyBundle {
    private final List<Integer> mCaptureIdList;
    final Object mLock = new Object();
    final SparseArray<CallbackToFutureAdapter.Completer<ImageProxy>> mCompleters = new SparseArray<>();
    private final SparseArray<ListenableFuture<ImageProxy>> mFutureResults = new SparseArray<>();
    private final List<ImageProxy> mOwnedImageProxies = new ArrayList();
    private boolean mClosed = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettableImageProxyBundle(List<Integer> captureIds) {
        this.mCaptureIdList = captureIds;
        setup();
    }

    @Override // androidx.camera.core.impl.ImageProxyBundle
    public ListenableFuture<ImageProxy> getImageProxy(int captureId) {
        ListenableFuture<ImageProxy> result;
        synchronized (this.mLock) {
            if (!this.mClosed) {
                result = this.mFutureResults.get(captureId);
                if (result == null) {
                    throw new IllegalArgumentException("ImageProxyBundle does not contain this id: " + captureId);
                }
            } else {
                throw new IllegalStateException("ImageProxyBundle already closed.");
            }
        }
        return result;
    }

    @Override // androidx.camera.core.impl.ImageProxyBundle
    public List<Integer> getCaptureIds() {
        return Collections.unmodifiableList(this.mCaptureIdList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addImageProxy(ImageProxy imageProxy) {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                Integer captureId = (Integer) imageProxy.getImageInfo().getTag();
                if (captureId != null) {
                    CallbackToFutureAdapter.Completer<ImageProxy> completer = this.mCompleters.get(captureId.intValue());
                    if (completer != null) {
                        this.mOwnedImageProxies.add(imageProxy);
                        completer.set(imageProxy);
                        return;
                    }
                    throw new IllegalArgumentException("ImageProxyBundle does not contain this id: " + captureId);
                }
                throw new IllegalArgumentException("CaptureId is null.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                for (ImageProxy imageProxy : this.mOwnedImageProxies) {
                    imageProxy.close();
                }
                this.mOwnedImageProxies.clear();
                this.mFutureResults.clear();
                this.mCompleters.clear();
                this.mClosed = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        synchronized (this.mLock) {
            if (!this.mClosed) {
                for (ImageProxy imageProxy : this.mOwnedImageProxies) {
                    imageProxy.close();
                }
                this.mOwnedImageProxies.clear();
                this.mFutureResults.clear();
                this.mCompleters.clear();
                setup();
            }
        }
    }

    private void setup() {
        synchronized (this.mLock) {
            for (Integer num : this.mCaptureIdList) {
                final int captureId = num.intValue();
                this.mFutureResults.put(captureId, CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<ImageProxy>() { // from class: androidx.camera.core.SettableImageProxyBundle.1
                    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                    public Object attachCompleter(CallbackToFutureAdapter.Completer<ImageProxy> completer) {
                        synchronized (SettableImageProxyBundle.this.mLock) {
                            SettableImageProxyBundle.this.mCompleters.put(captureId, completer);
                        }
                        return "getImageProxy(id: " + captureId + ")";
                    }
                }));
            }
        }
    }
}
