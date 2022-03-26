package com.facebook.drawee.controller;

import android.graphics.drawable.Animatable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ForwardingControllerListener<INFO> implements ControllerListener<INFO> {
    private static final String TAG = "FdingControllerListener";
    private final List<ControllerListener<? super INFO>> mListeners = new ArrayList(2);

    public static <INFO> ForwardingControllerListener<INFO> create() {
        return new ForwardingControllerListener<>();
    }

    public static <INFO> ForwardingControllerListener<INFO> of(ControllerListener<? super INFO> listener) {
        ForwardingControllerListener<INFO> forwarder = create();
        forwarder.addListener(listener);
        return forwarder;
    }

    public static <INFO> ForwardingControllerListener<INFO> of(ControllerListener<? super INFO> listener1, ControllerListener<? super INFO> listener2) {
        ForwardingControllerListener<INFO> forwarder = create();
        forwarder.addListener(listener1);
        forwarder.addListener(listener2);
        return forwarder;
    }

    public synchronized void addListener(ControllerListener<? super INFO> listener) {
        this.mListeners.add(listener);
    }

    public synchronized void removeListener(ControllerListener<? super INFO> listener) {
        int index = this.mListeners.indexOf(listener);
        if (index != -1) {
            this.mListeners.set(index, null);
        }
    }

    public synchronized void clearListeners() {
        this.mListeners.clear();
    }

    private synchronized void onException(String message, Throwable t) {
        Log.e(TAG, message, t);
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public synchronized void onSubmit(String id, Object callerContext) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onSubmit(id, callerContext);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onSubmit", exception);
            }
        }
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public synchronized void onFinalImageSet(String id, @Nullable INFO imageInfo, @Nullable Animatable animatable) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onFinalImageSet(id, imageInfo, animatable);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onFinalImageSet", exception);
            }
        }
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public void onIntermediateImageSet(String id, @Nullable INFO imageInfo) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onIntermediateImageSet(id, imageInfo);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onIntermediateImageSet", exception);
            }
        }
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public void onIntermediateImageFailed(String id, Throwable throwable) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onIntermediateImageFailed(id, throwable);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onIntermediateImageFailed", exception);
            }
        }
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public synchronized void onFailure(String id, Throwable throwable) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onFailure(id, throwable);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onFailure", exception);
            }
        }
    }

    @Override // com.facebook.drawee.controller.ControllerListener
    public synchronized void onRelease(String id) {
        int numberOfListeners = this.mListeners.size();
        for (int i = 0; i < numberOfListeners; i++) {
            try {
                ControllerListener<? super INFO> listener = this.mListeners.get(i);
                if (listener != null) {
                    listener.onRelease(id);
                }
            } catch (Exception exception) {
                onException("InternalListener exception in onRelease", exception);
            }
        }
    }
}
