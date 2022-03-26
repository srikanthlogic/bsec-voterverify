package com.facebook.drawee.backends.pipeline.info;

import com.facebook.common.internal.Objects;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ImagePerfData {
    public static final int UNSET;
    @Nullable
    private final Object mCallerContext;
    @Nullable
    private final String mComponentTag;
    private final long mControllerCancelTimeMs;
    private final long mControllerFailureTimeMs;
    private final long mControllerFinalImageSetTimeMs;
    @Nullable
    private final String mControllerId;
    private final long mControllerIntermediateImageSetTimeMs;
    private final long mControllerSubmitTimeMs;
    @Nullable
    private final ImageInfo mImageInfo;
    private final int mImageOrigin;
    @Nullable
    private final ImageRequest mImageRequest;
    private final long mImageRequestEndTimeMs;
    private final long mImageRequestStartTimeMs;
    private final long mInvisibilityEventTimeMs;
    private final boolean mIsPrefetch;
    private final int mOnScreenHeightPx;
    private final int mOnScreenWidthPx;
    @Nullable
    private final String mRequestId;
    private final long mVisibilityEventTimeMs;
    private final int mVisibilityState;

    public ImagePerfData(@Nullable String controllerId, @Nullable String requestId, @Nullable ImageRequest imageRequest, @Nullable Object callerContext, @Nullable ImageInfo imageInfo, long controllerSubmitTimeMs, long controllerIntermediateImageSetTimeMs, long controllerFinalImageSetTimeMs, long controllerFailureTimeMs, long controllerCancelTimeMs, long imageRequestStartTimeMs, long imageRequestEndTimeMs, int imageOrigin, boolean isPrefetch, int onScreenWidthPx, int onScreenHeightPx, int visibilityState, long visibilityEventTimeMs, long invisibilityEventTime, @Nullable String componentTag) {
        this.mControllerId = controllerId;
        this.mRequestId = requestId;
        this.mImageRequest = imageRequest;
        this.mCallerContext = callerContext;
        this.mImageInfo = imageInfo;
        this.mControllerSubmitTimeMs = controllerSubmitTimeMs;
        this.mControllerIntermediateImageSetTimeMs = controllerIntermediateImageSetTimeMs;
        this.mControllerFinalImageSetTimeMs = controllerFinalImageSetTimeMs;
        this.mControllerFailureTimeMs = controllerFailureTimeMs;
        this.mControllerCancelTimeMs = controllerCancelTimeMs;
        this.mImageRequestStartTimeMs = imageRequestStartTimeMs;
        this.mImageRequestEndTimeMs = imageRequestEndTimeMs;
        this.mImageOrigin = imageOrigin;
        this.mIsPrefetch = isPrefetch;
        this.mOnScreenWidthPx = onScreenWidthPx;
        this.mOnScreenHeightPx = onScreenHeightPx;
        this.mVisibilityState = visibilityState;
        this.mVisibilityEventTimeMs = visibilityEventTimeMs;
        this.mInvisibilityEventTimeMs = invisibilityEventTime;
        this.mComponentTag = componentTag;
    }

    @Nullable
    public String getControllerId() {
        return this.mControllerId;
    }

    @Nullable
    public String getRequestId() {
        return this.mRequestId;
    }

    @Nullable
    public ImageRequest getImageRequest() {
        return this.mImageRequest;
    }

    @Nullable
    public Object getCallerContext() {
        return this.mCallerContext;
    }

    @Nullable
    public ImageInfo getImageInfo() {
        return this.mImageInfo;
    }

    public long getControllerSubmitTimeMs() {
        return this.mControllerSubmitTimeMs;
    }

    public long getControllerIntermediateImageSetTimeMs() {
        return this.mControllerIntermediateImageSetTimeMs;
    }

    public long getControllerFinalImageSetTimeMs() {
        return this.mControllerFinalImageSetTimeMs;
    }

    public long getControllerFailureTimeMs() {
        return this.mControllerFailureTimeMs;
    }

    public long getImageRequestStartTimeMs() {
        return this.mImageRequestStartTimeMs;
    }

    public long getImageRequestEndTimeMs() {
        return this.mImageRequestEndTimeMs;
    }

    public int getImageOrigin() {
        return this.mImageOrigin;
    }

    public boolean isPrefetch() {
        return this.mIsPrefetch;
    }

    public int getOnScreenWidthPx() {
        return this.mOnScreenWidthPx;
    }

    public int getOnScreenHeightPx() {
        return this.mOnScreenHeightPx;
    }

    public long getFinalImageLoadTimeMs() {
        if (getImageRequestEndTimeMs() == -1 || getImageRequestStartTimeMs() == -1) {
            return -1;
        }
        return getImageRequestEndTimeMs() - getImageRequestStartTimeMs();
    }

    public long getIntermediateImageLoadTimeMs() {
        if (getControllerIntermediateImageSetTimeMs() == -1 || getControllerSubmitTimeMs() == -1) {
            return -1;
        }
        return getControllerIntermediateImageSetTimeMs() - getControllerSubmitTimeMs();
    }

    public int getVisibilityState() {
        return this.mVisibilityState;
    }

    public long getVisibilityEventTimeMs() {
        return this.mVisibilityEventTimeMs;
    }

    public long getInvisibilityEventTimeMs() {
        return this.mInvisibilityEventTimeMs;
    }

    @Nullable
    public String getComponentTag() {
        return this.mComponentTag;
    }

    public String createDebugString() {
        return Objects.toStringHelper(this).add("controller ID", this.mControllerId).add("request ID", this.mRequestId).add("controller submit", this.mControllerSubmitTimeMs).add("controller final image", this.mControllerFinalImageSetTimeMs).add("controller failure", this.mControllerFailureTimeMs).add("controller cancel", this.mControllerCancelTimeMs).add("start time", this.mImageRequestStartTimeMs).add("end time", this.mImageRequestEndTimeMs).add("origin", ImageOriginUtils.toString(this.mImageOrigin)).add("prefetch", this.mIsPrefetch).add("caller context", this.mCallerContext).add("image request", this.mImageRequest).add("image info", this.mImageInfo).add("on-screen width", this.mOnScreenWidthPx).add("on-screen height", this.mOnScreenHeightPx).add("visibility state", this.mVisibilityState).add("component tag", this.mComponentTag).toString();
    }
}
