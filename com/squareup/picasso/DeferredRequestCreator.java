package com.squareup.picasso;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
/* loaded from: classes3.dex */
public class DeferredRequestCreator implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    Callback callback;
    private final RequestCreator creator;
    final WeakReference<ImageView> target;

    public DeferredRequestCreator(RequestCreator creator, ImageView target, Callback callback) {
        this.creator = creator;
        this.target = new WeakReference<>(target);
        this.callback = callback;
        target.addOnAttachStateChangeListener(this);
        if (target.getWindowToken() != null) {
            onViewAttachedToWindow(target);
        }
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        view.getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        view.getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        ImageView target = this.target.get();
        if (target == null) {
            return true;
        }
        ViewTreeObserver vto = target.getViewTreeObserver();
        if (!vto.isAlive()) {
            return true;
        }
        int width = target.getWidth();
        int height = target.getHeight();
        if (width <= 0 || height <= 0) {
            return true;
        }
        target.removeOnAttachStateChangeListener(this);
        vto.removeOnPreDrawListener(this);
        this.target.clear();
        this.creator.unfit().resize(width, height).into(target, this.callback);
        return true;
    }

    public void cancel() {
        this.creator.clearTag();
        this.callback = null;
        ImageView target = this.target.get();
        if (target != null) {
            this.target.clear();
            target.removeOnAttachStateChangeListener(this);
            ViewTreeObserver vto = target.getViewTreeObserver();
            if (vto.isAlive()) {
                vto.removeOnPreDrawListener(this);
            }
        }
    }

    public Object getTag() {
        return this.creator.getTag();
    }
}
