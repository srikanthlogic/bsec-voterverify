package com.facebook.drawee.controller;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.components.DraweeEventTracker;
import com.facebook.drawee.components.RetryManager;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.interfaces.SettableDraweeHierarchy;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class AbstractDraweeController<T, INFO> implements DraweeController, DeferredReleaser.Releasable, GestureDetector.ClickListener {
    private static final Class<?> TAG = AbstractDraweeController.class;
    private Object mCallerContext;
    @Nullable
    private String mContentDescription;
    @Nullable
    protected ControllerListener<INFO> mControllerListener;
    @Nullable
    private Drawable mControllerOverlay;
    @Nullable
    private ControllerViewportVisibilityListener mControllerViewportVisibilityListener;
    @Nullable
    private DataSource<T> mDataSource;
    private final DeferredReleaser mDeferredReleaser;
    @Nullable
    private Drawable mDrawable;
    @Nullable
    private T mFetchedImage;
    @Nullable
    private GestureDetector mGestureDetector;
    private boolean mHasFetchFailed;
    private String mId;
    private boolean mIsAttached;
    private boolean mIsRequestSubmitted;
    private boolean mIsVisibleInViewportHint;
    private boolean mRetainImageOnFailure;
    @Nullable
    private RetryManager mRetryManager;
    @Nullable
    private SettableDraweeHierarchy mSettableDraweeHierarchy;
    private final Executor mUiThreadImmediateExecutor;
    private final DraweeEventTracker mEventTracker = DraweeEventTracker.newInstance();
    private boolean mJustConstructed = true;

    protected abstract Drawable createDrawable(T t);

    protected abstract DataSource<T> getDataSource();

    @Nullable
    protected abstract INFO getImageInfo(T t);

    protected abstract void releaseDrawable(@Nullable Drawable drawable);

    protected abstract void releaseImage(@Nullable T t);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InternalForwardingListener<INFO> extends ForwardingControllerListener<INFO> {
        private InternalForwardingListener() {
        }

        public static <INFO> InternalForwardingListener<INFO> createInternal(ControllerListener<? super INFO> listener1, ControllerListener<? super INFO> listener2) {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("AbstractDraweeController#createInternal");
            }
            InternalForwardingListener<INFO> forwarder = new InternalForwardingListener<>();
            forwarder.addListener(listener1);
            forwarder.addListener(listener2);
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            return forwarder;
        }
    }

    public AbstractDraweeController(DeferredReleaser deferredReleaser, Executor uiThreadImmediateExecutor, String id, Object callerContext) {
        this.mDeferredReleaser = deferredReleaser;
        this.mUiThreadImmediateExecutor = uiThreadImmediateExecutor;
        init(id, callerContext);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initialize(String id, Object callerContext) {
        init(id, callerContext);
        this.mJustConstructed = false;
    }

    private synchronized void init(String id, Object callerContext) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("AbstractDraweeController#init");
        }
        this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_INIT_CONTROLLER);
        if (!this.mJustConstructed && this.mDeferredReleaser != null) {
            this.mDeferredReleaser.cancelDeferredRelease(this);
        }
        this.mIsAttached = false;
        this.mIsVisibleInViewportHint = false;
        releaseFetch();
        this.mRetainImageOnFailure = false;
        if (this.mRetryManager != null) {
            this.mRetryManager.init();
        }
        if (this.mGestureDetector != null) {
            this.mGestureDetector.init();
            this.mGestureDetector.setClickListener(this);
        }
        if (this.mControllerListener instanceof InternalForwardingListener) {
            ((InternalForwardingListener) this.mControllerListener).clearListeners();
        } else {
            this.mControllerListener = null;
        }
        this.mControllerViewportVisibilityListener = null;
        if (this.mSettableDraweeHierarchy != null) {
            this.mSettableDraweeHierarchy.reset();
            this.mSettableDraweeHierarchy.setControllerOverlay(null);
            this.mSettableDraweeHierarchy = null;
        }
        this.mControllerOverlay = null;
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s -> %s: initialize", Integer.valueOf(System.identityHashCode(this)), this.mId, id);
        }
        this.mId = id;
        this.mCallerContext = callerContext;
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    @Override // com.facebook.drawee.components.DeferredReleaser.Releasable
    public void release() {
        this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_RELEASE_CONTROLLER);
        RetryManager retryManager = this.mRetryManager;
        if (retryManager != null) {
            retryManager.reset();
        }
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.reset();
        }
        SettableDraweeHierarchy settableDraweeHierarchy = this.mSettableDraweeHierarchy;
        if (settableDraweeHierarchy != null) {
            settableDraweeHierarchy.reset();
        }
        releaseFetch();
    }

    private void releaseFetch() {
        boolean wasRequestSubmitted = this.mIsRequestSubmitted;
        this.mIsRequestSubmitted = false;
        this.mHasFetchFailed = false;
        DataSource<T> dataSource = this.mDataSource;
        if (dataSource != null) {
            dataSource.close();
            this.mDataSource = null;
        }
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            releaseDrawable(drawable);
        }
        if (this.mContentDescription != null) {
            this.mContentDescription = null;
        }
        this.mDrawable = null;
        T t = this.mFetchedImage;
        if (t != null) {
            logMessageAndImage("release", t);
            releaseImage(this.mFetchedImage);
            this.mFetchedImage = null;
        }
        if (wasRequestSubmitted) {
            getControllerListener().onRelease(this.mId);
        }
    }

    public String getId() {
        return this.mId;
    }

    public Object getCallerContext() {
        return this.mCallerContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RetryManager getRetryManager() {
        if (this.mRetryManager == null) {
            this.mRetryManager = new RetryManager();
        }
        return this.mRetryManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public GestureDetector getGestureDetector() {
        return this.mGestureDetector;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setGestureDetector(@Nullable GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
        GestureDetector gestureDetector2 = this.mGestureDetector;
        if (gestureDetector2 != null) {
            gestureDetector2.setClickListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRetainImageOnFailure(boolean enabled) {
        this.mRetainImageOnFailure = enabled;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    @Nullable
    public String getContentDescription() {
        return this.mContentDescription;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public void setContentDescription(@Nullable String contentDescription) {
        this.mContentDescription = contentDescription;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addControllerListener(ControllerListener<? super INFO> controllerListener) {
        Preconditions.checkNotNull(controllerListener);
        ControllerListener<INFO> controllerListener2 = this.mControllerListener;
        if (controllerListener2 instanceof InternalForwardingListener) {
            ((InternalForwardingListener) controllerListener2).addListener(controllerListener);
        } else if (controllerListener2 != null) {
            this.mControllerListener = InternalForwardingListener.createInternal(controllerListener2, controllerListener);
        } else {
            this.mControllerListener = controllerListener;
        }
    }

    public void removeControllerListener(ControllerListener<? super INFO> controllerListener) {
        Preconditions.checkNotNull(controllerListener);
        ControllerListener<INFO> controllerListener2 = this.mControllerListener;
        if (controllerListener2 instanceof InternalForwardingListener) {
            ((InternalForwardingListener) controllerListener2).removeListener(controllerListener);
        } else if (controllerListener2 == controllerListener) {
            this.mControllerListener = null;
        }
    }

    protected ControllerListener<INFO> getControllerListener() {
        ControllerListener<INFO> controllerListener = this.mControllerListener;
        if (controllerListener == null) {
            return BaseControllerListener.getNoOpListener();
        }
        return controllerListener;
    }

    public void setControllerViewportVisibilityListener(@Nullable ControllerViewportVisibilityListener controllerViewportVisibilityListener) {
        this.mControllerViewportVisibilityListener = controllerViewportVisibilityListener;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    @Nullable
    public DraweeHierarchy getHierarchy() {
        return this.mSettableDraweeHierarchy;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public void setHierarchy(@Nullable DraweeHierarchy hierarchy) {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: setHierarchy: %s", Integer.valueOf(System.identityHashCode(this)), this.mId, hierarchy);
        }
        this.mEventTracker.recordEvent(hierarchy != null ? DraweeEventTracker.Event.ON_SET_HIERARCHY : DraweeEventTracker.Event.ON_CLEAR_HIERARCHY);
        if (this.mIsRequestSubmitted) {
            this.mDeferredReleaser.cancelDeferredRelease(this);
            release();
        }
        SettableDraweeHierarchy settableDraweeHierarchy = this.mSettableDraweeHierarchy;
        if (settableDraweeHierarchy != null) {
            settableDraweeHierarchy.setControllerOverlay(null);
            this.mSettableDraweeHierarchy = null;
        }
        if (hierarchy != null) {
            Preconditions.checkArgument(hierarchy instanceof SettableDraweeHierarchy);
            this.mSettableDraweeHierarchy = (SettableDraweeHierarchy) hierarchy;
            this.mSettableDraweeHierarchy.setControllerOverlay(this.mControllerOverlay);
        }
    }

    protected void setControllerOverlay(@Nullable Drawable controllerOverlay) {
        this.mControllerOverlay = controllerOverlay;
        SettableDraweeHierarchy settableDraweeHierarchy = this.mSettableDraweeHierarchy;
        if (settableDraweeHierarchy != null) {
            settableDraweeHierarchy.setControllerOverlay(this.mControllerOverlay);
        }
    }

    @Nullable
    protected Drawable getControllerOverlay() {
        return this.mControllerOverlay;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public void onAttach() {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("AbstractDraweeController#onAttach");
        }
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: onAttach: %s", Integer.valueOf(System.identityHashCode(this)), this.mId, this.mIsRequestSubmitted ? "request already submitted" : "request needs submit");
        }
        this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_ATTACH_CONTROLLER);
        Preconditions.checkNotNull(this.mSettableDraweeHierarchy);
        this.mDeferredReleaser.cancelDeferredRelease(this);
        this.mIsAttached = true;
        if (!this.mIsRequestSubmitted) {
            submitRequest();
        }
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public void onDetach() {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("AbstractDraweeController#onDetach");
        }
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: onDetach", Integer.valueOf(System.identityHashCode(this)), this.mId);
        }
        this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_DETACH_CONTROLLER);
        this.mIsAttached = false;
        this.mDeferredReleaser.scheduleDeferredRelease(this);
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public void onViewportVisibilityHint(boolean isVisibleInViewportHint) {
        ControllerViewportVisibilityListener listener = this.mControllerViewportVisibilityListener;
        if (listener != null) {
            if (isVisibleInViewportHint && !this.mIsVisibleInViewportHint) {
                listener.onDraweeViewportEntry(this.mId);
            } else if (!isVisibleInViewportHint && this.mIsVisibleInViewportHint) {
                listener.onDraweeViewportExit(this.mId);
            }
        }
        this.mIsVisibleInViewportHint = isVisibleInViewportHint;
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public boolean onTouchEvent(MotionEvent event) {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: onTouchEvent %s", Integer.valueOf(System.identityHashCode(this)), this.mId, event);
        }
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector == null) {
            return false;
        }
        if (!gestureDetector.isCapturingGesture() && !shouldHandleGesture()) {
            return false;
        }
        this.mGestureDetector.onTouchEvent(event);
        return true;
    }

    protected boolean shouldHandleGesture() {
        return shouldRetryOnTap();
    }

    private boolean shouldRetryOnTap() {
        RetryManager retryManager;
        return this.mHasFetchFailed && (retryManager = this.mRetryManager) != null && retryManager.shouldRetryOnTap();
    }

    @Override // com.facebook.drawee.gestures.GestureDetector.ClickListener
    public boolean onClick() {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: onClick", Integer.valueOf(System.identityHashCode(this)), this.mId);
        }
        if (!shouldRetryOnTap()) {
            return false;
        }
        this.mRetryManager.notifyTapToRetry();
        this.mSettableDraweeHierarchy.reset();
        submitRequest();
        return true;
    }

    protected void submitRequest() {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("AbstractDraweeController#submitRequest");
        }
        T closeableImage = getCachedImage();
        if (closeableImage != null) {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("AbstractDraweeController#submitRequest->cache");
            }
            this.mDataSource = null;
            this.mIsRequestSubmitted = true;
            this.mHasFetchFailed = false;
            this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_SUBMIT_CACHE_HIT);
            getControllerListener().onSubmit(this.mId, this.mCallerContext);
            onImageLoadedFromCacheImmediately(this.mId, closeableImage);
            onNewResultInternal(this.mId, this.mDataSource, closeableImage, 1.0f, true, true, true);
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
                return;
            }
            return;
        }
        this.mEventTracker.recordEvent(DraweeEventTracker.Event.ON_DATASOURCE_SUBMIT);
        getControllerListener().onSubmit(this.mId, this.mCallerContext);
        this.mSettableDraweeHierarchy.setProgress(0.0f, true);
        this.mIsRequestSubmitted = true;
        this.mHasFetchFailed = false;
        this.mDataSource = getDataSource();
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: submitRequest: dataSource: %x", Integer.valueOf(System.identityHashCode(this)), this.mId, Integer.valueOf(System.identityHashCode(this.mDataSource)));
        }
        final String id = this.mId;
        final boolean wasImmediate = this.mDataSource.hasResult();
        this.mDataSource.subscribe(new BaseDataSubscriber<T>() { // from class: com.facebook.drawee.controller.AbstractDraweeController.1
            @Override // com.facebook.datasource.BaseDataSubscriber
            public void onNewResultImpl(DataSource<T> dataSource) {
                boolean isFinished = dataSource.isFinished();
                boolean hasMultipleResults = dataSource.hasMultipleResults();
                float progress = dataSource.getProgress();
                T image = dataSource.getResult();
                if (image != null) {
                    AbstractDraweeController.this.onNewResultInternal(id, dataSource, image, progress, isFinished, wasImmediate, hasMultipleResults);
                } else if (isFinished) {
                    AbstractDraweeController.this.onFailureInternal(id, dataSource, new NullPointerException(), true);
                }
            }

            @Override // com.facebook.datasource.BaseDataSubscriber
            public void onFailureImpl(DataSource<T> dataSource) {
                AbstractDraweeController.this.onFailureInternal(id, dataSource, dataSource.getFailureCause(), true);
            }

            @Override // com.facebook.datasource.BaseDataSubscriber, com.facebook.datasource.DataSubscriber
            public void onProgressUpdate(DataSource<T> dataSource) {
                boolean isFinished = dataSource.isFinished();
                AbstractDraweeController.this.onProgressUpdateInternal(id, dataSource, dataSource.getProgress(), isFinished);
            }
        }, this.mUiThreadImmediateExecutor);
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNewResultInternal(String id, DataSource<T> dataSource, @Nullable T image, float progress, boolean isFinished, boolean wasImmediate, boolean deliverTempResult) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("AbstractDraweeController#onNewResultInternal");
            }
            if (!isExpectedDataSource(id, dataSource)) {
                logMessageAndImage("ignore_old_datasource @ onNewResult", image);
                releaseImage(image);
                dataSource.close();
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                    return;
                }
                return;
            }
            this.mEventTracker.recordEvent(isFinished ? DraweeEventTracker.Event.ON_DATASOURCE_RESULT : DraweeEventTracker.Event.ON_DATASOURCE_RESULT_INT);
            try {
                Drawable drawable = createDrawable(image);
                T previousImage = this.mFetchedImage;
                Drawable previousDrawable = this.mDrawable;
                this.mFetchedImage = image;
                this.mDrawable = drawable;
                if (isFinished) {
                    logMessageAndImage("set_final_result @ onNewResult", image);
                    this.mDataSource = null;
                    this.mSettableDraweeHierarchy.setImage(drawable, 1.0f, wasImmediate);
                    getControllerListener().onFinalImageSet(id, getImageInfo(image), getAnimatable());
                } else if (deliverTempResult) {
                    logMessageAndImage("set_temporary_result @ onNewResult", image);
                    this.mSettableDraweeHierarchy.setImage(drawable, 1.0f, wasImmediate);
                    getControllerListener().onFinalImageSet(id, getImageInfo(image), getAnimatable());
                } else {
                    logMessageAndImage("set_intermediate_result @ onNewResult", image);
                    this.mSettableDraweeHierarchy.setImage(drawable, progress, wasImmediate);
                    getControllerListener().onIntermediateImageSet(id, getImageInfo(image));
                }
                if (!(previousDrawable == null || previousDrawable == drawable)) {
                    releaseDrawable(previousDrawable);
                }
                if (!(previousImage == null || previousImage == image)) {
                    logMessageAndImage("release_previous_result @ onNewResult", previousImage);
                    releaseImage(previousImage);
                }
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            } catch (Exception exception) {
                logMessageAndImage("drawable_failed @ onNewResult", image);
                releaseImage(image);
                onFailureInternal(id, dataSource, exception, isFinished);
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
            }
        } catch (Throwable exception2) {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            throw exception2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailureInternal(String id, DataSource<T> dataSource, Throwable throwable, boolean isFinished) {
        Drawable drawable;
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("AbstractDraweeController#onFailureInternal");
        }
        if (!isExpectedDataSource(id, dataSource)) {
            logMessageAndFailure("ignore_old_datasource @ onFailure", throwable);
            dataSource.close();
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
                return;
            }
            return;
        }
        this.mEventTracker.recordEvent(isFinished ? DraweeEventTracker.Event.ON_DATASOURCE_FAILURE : DraweeEventTracker.Event.ON_DATASOURCE_FAILURE_INT);
        if (isFinished) {
            logMessageAndFailure("final_failed @ onFailure", throwable);
            this.mDataSource = null;
            this.mHasFetchFailed = true;
            if (this.mRetainImageOnFailure && (drawable = this.mDrawable) != null) {
                this.mSettableDraweeHierarchy.setImage(drawable, 1.0f, true);
            } else if (shouldRetryOnTap()) {
                this.mSettableDraweeHierarchy.setRetry(throwable);
            } else {
                this.mSettableDraweeHierarchy.setFailure(throwable);
            }
            getControllerListener().onFailure(this.mId, throwable);
        } else {
            logMessageAndFailure("intermediate_failed @ onFailure", throwable);
            getControllerListener().onIntermediateImageFailed(this.mId, throwable);
        }
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProgressUpdateInternal(String id, DataSource<T> dataSource, float progress, boolean isFinished) {
        if (!isExpectedDataSource(id, dataSource)) {
            logMessageAndFailure("ignore_old_datasource @ onProgress", null);
            dataSource.close();
        } else if (!isFinished) {
            this.mSettableDraweeHierarchy.setProgress(progress, false);
        }
    }

    private boolean isExpectedDataSource(String id, DataSource<T> dataSource) {
        if (dataSource == null && this.mDataSource == null) {
            return true;
        }
        if (!id.equals(this.mId) || dataSource != this.mDataSource || !this.mIsRequestSubmitted) {
            return false;
        }
        return true;
    }

    private void logMessageAndImage(String messageAndMethod, T image) {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: %s: image: %s %x", Integer.valueOf(System.identityHashCode(this)), this.mId, messageAndMethod, getImageClass(image), Integer.valueOf(getImageHash(image)));
        }
    }

    private void logMessageAndFailure(String messageAndMethod, Throwable throwable) {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x %s: %s: failure: %s", Integer.valueOf(System.identityHashCode(this)), this.mId, messageAndMethod, throwable);
        }
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    @Nullable
    public Animatable getAnimatable() {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof Animatable) {
            return (Animatable) drawable;
        }
        return null;
    }

    protected String getImageClass(@Nullable T image) {
        return image != null ? image.getClass().getSimpleName() : "<null>";
    }

    protected int getImageHash(@Nullable T image) {
        return System.identityHashCode(image);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("isAttached", this.mIsAttached).add("isRequestSubmitted", this.mIsRequestSubmitted).add("hasFetchFailed", this.mHasFetchFailed).add("fetchedImage", getImageHash(this.mFetchedImage)).add("events", this.mEventTracker.toString()).toString();
    }

    @Nullable
    protected T getCachedImage() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onImageLoadedFromCacheImmediately(String id, T cachedImage) {
    }
}
