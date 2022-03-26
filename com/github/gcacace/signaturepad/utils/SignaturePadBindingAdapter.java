package com.github.gcacace.signaturepad.utils;

import com.github.gcacace.signaturepad.views.SignaturePad;
/* loaded from: classes.dex */
public final class SignaturePadBindingAdapter {

    /* loaded from: classes.dex */
    public interface OnClearListener {
        void onClear();
    }

    /* loaded from: classes.dex */
    public interface OnSignedListener {
        void onSigned();
    }

    /* loaded from: classes.dex */
    public interface OnStartSigningListener {
        void onStartSigning();
    }

    public static void setOnSignedListener(SignaturePad view, OnStartSigningListener onStartSigningListener) {
        setOnSignedListener(view, onStartSigningListener, null, null);
    }

    public static void setOnSignedListener(SignaturePad view, OnSignedListener onSignedListener) {
        setOnSignedListener(view, null, onSignedListener, null);
    }

    public static void setOnSignedListener(SignaturePad view, OnClearListener onClearListener) {
        setOnSignedListener(view, null, null, onClearListener);
    }

    public static void setOnSignedListener(SignaturePad view, final OnStartSigningListener onStartSigningListener, final OnSignedListener onSignedListener, final OnClearListener onClearListener) {
        view.setOnSignedListener(new SignaturePad.OnSignedListener() { // from class: com.github.gcacace.signaturepad.utils.SignaturePadBindingAdapter.1
            @Override // com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
            public void onStartSigning() {
                OnStartSigningListener onStartSigningListener2 = OnStartSigningListener.this;
                if (onStartSigningListener2 != null) {
                    onStartSigningListener2.onStartSigning();
                }
            }

            @Override // com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
            public void onSigned() {
                OnSignedListener onSignedListener2 = onSignedListener;
                if (onSignedListener2 != null) {
                    onSignedListener2.onSigned();
                }
            }

            @Override // com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
            public void onClear() {
                OnClearListener onClearListener2 = onClearListener;
                if (onClearListener2 != null) {
                    onClearListener2.onClear();
                }
            }
        });
    }
}
