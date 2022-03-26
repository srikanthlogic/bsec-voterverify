package com.squareup.picasso;

import android.graphics.Bitmap;
import com.squareup.picasso.Picasso;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class FetchAction extends Action<Object> {
    private Callback callback;
    private final Object target = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public FetchAction(Picasso picasso, Request data, int memoryPolicy, int networkPolicy, Object tag, String key, Callback callback) {
        super(picasso, null, data, memoryPolicy, networkPolicy, 0, null, key, tag, false);
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public void complete(Bitmap result, Picasso.LoadedFrom from) {
        Callback callback = this.callback;
        if (callback != null) {
            callback.onSuccess();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public void error(Exception e) {
        Callback callback = this.callback;
        if (callback != null) {
            callback.onError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public void cancel() {
        super.cancel();
        this.callback = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public Object getTarget() {
        return this.target;
    }
}
