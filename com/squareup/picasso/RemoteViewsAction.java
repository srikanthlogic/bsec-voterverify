package com.squareup.picasso;

import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.squareup.picasso.Picasso;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class RemoteViewsAction extends Action<RemoteViewsTarget> {
    Callback callback;
    final RemoteViews remoteViews;
    private RemoteViewsTarget target;
    final int viewId;

    abstract void update();

    RemoteViewsAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int errorResId, int memoryPolicy, int networkPolicy, Object tag, String key, Callback callback) {
        super(picasso, null, data, memoryPolicy, networkPolicy, errorResId, null, key, tag, false);
        this.remoteViews = remoteViews;
        this.viewId = viewId;
        this.callback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public void complete(Bitmap result, Picasso.LoadedFrom from) {
        this.remoteViews.setImageViewBitmap(this.viewId, result);
        update();
        Callback callback = this.callback;
        if (callback != null) {
            callback.onSuccess();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.squareup.picasso.Action
    public void cancel() {
        super.cancel();
        if (this.callback != null) {
            this.callback = null;
        }
    }

    @Override // com.squareup.picasso.Action
    public void error(Exception e) {
        if (this.errorResId != 0) {
            setImageResource(this.errorResId);
        }
        Callback callback = this.callback;
        if (callback != null) {
            callback.onError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.squareup.picasso.Action
    public RemoteViewsTarget getTarget() {
        if (this.target == null) {
            this.target = new RemoteViewsTarget(this.remoteViews, this.viewId);
        }
        return this.target;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImageResource(int resId) {
        this.remoteViews.setImageViewResource(this.viewId, resId);
        update();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class RemoteViewsTarget {
        final RemoteViews remoteViews;
        final int viewId;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RemoteViewsTarget(RemoteViews remoteViews, int viewId) {
            this.remoteViews = remoteViews;
            this.viewId = viewId;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RemoteViewsTarget remoteViewsTarget = (RemoteViewsTarget) o;
            if (this.viewId != remoteViewsTarget.viewId || !this.remoteViews.equals(remoteViewsTarget.remoteViews)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.remoteViews.hashCode() * 31) + this.viewId;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class AppWidgetAction extends RemoteViewsAction {
        private final int[] appWidgetIds;

        @Override // com.squareup.picasso.RemoteViewsAction, com.squareup.picasso.Action
        /* bridge */ /* synthetic */ RemoteViewsTarget getTarget() {
            return RemoteViewsAction.super.getTarget();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public AppWidgetAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int[] appWidgetIds, int memoryPolicy, int networkPolicy, String key, Object tag, int errorResId, Callback callback) {
            super(picasso, data, remoteViews, viewId, errorResId, memoryPolicy, networkPolicy, tag, key, callback);
            this.appWidgetIds = appWidgetIds;
        }

        @Override // com.squareup.picasso.RemoteViewsAction
        void update() {
            AppWidgetManager.getInstance(this.picasso.context).updateAppWidget(this.appWidgetIds, this.remoteViews);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class NotificationAction extends RemoteViewsAction {
        private final Notification notification;
        private final int notificationId;
        private final String notificationTag;

        @Override // com.squareup.picasso.RemoteViewsAction, com.squareup.picasso.Action
        /* bridge */ /* synthetic */ RemoteViewsTarget getTarget() {
            return RemoteViewsAction.super.getTarget();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public NotificationAction(Picasso picasso, Request data, RemoteViews remoteViews, int viewId, int notificationId, Notification notification, String notificationTag, int memoryPolicy, int networkPolicy, String key, Object tag, int errorResId, Callback callback) {
            super(picasso, data, remoteViews, viewId, errorResId, memoryPolicy, networkPolicy, tag, key, callback);
            this.notificationId = notificationId;
            this.notificationTag = notificationTag;
            this.notification = notification;
        }

        @Override // com.squareup.picasso.RemoteViewsAction
        void update() {
            ((NotificationManager) Utils.getService(this.picasso.context, "notification")).notify(this.notificationTag, this.notificationId, this.notification);
        }
    }
}
