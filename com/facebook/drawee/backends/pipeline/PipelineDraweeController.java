package com.facebook.drawee.backends.pipeline;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.time.AwakeTimeSinceBootClock;
import com.facebook.datasource.DataSource;
import com.facebook.drawable.base.DrawableWithCaches;
import com.facebook.drawee.backends.pipeline.debug.DebugOverlayImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.ForwardingImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.ImageOriginListener;
import com.facebook.drawee.backends.pipeline.info.ImageOriginRequestListener;
import com.facebook.drawee.backends.pipeline.info.ImagePerfDataListener;
import com.facebook.drawee.backends.pipeline.info.ImagePerfMonitor;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.debug.DebugControllerOverlayDrawable;
import com.facebook.drawee.debug.listener.ImageLoadingTimeControllerListener;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.listener.ForwardingRequestListener;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.systrace.FrescoSystrace;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class PipelineDraweeController extends AbstractDraweeController<CloseableReference<CloseableImage>, ImageInfo> {
    private static final Class<?> TAG = PipelineDraweeController.class;
    private CacheKey mCacheKey;
    @Nullable
    private ImmutableList<DrawableFactory> mCustomDrawableFactories;
    private Supplier<DataSource<CloseableReference<CloseableImage>>> mDataSourceSupplier;
    private DebugOverlayImageOriginListener mDebugOverlayImageOriginListener;
    private final DrawableFactory mDefaultDrawableFactory;
    private boolean mDrawDebugOverlay;
    @Nullable
    private final ImmutableList<DrawableFactory> mGlobalDrawableFactories;
    @Nullable
    private ImageOriginListener mImageOriginListener;
    @Nullable
    private ImagePerfMonitor mImagePerfMonitor;
    @Nullable
    private final MemoryCache<CacheKey, CloseableImage> mMemoryCache;
    @Nullable
    private Set<RequestListener> mRequestListeners;
    private final Resources mResources;

    public PipelineDraweeController(Resources resources, DeferredReleaser deferredReleaser, DrawableFactory animatedDrawableFactory, Executor uiThreadExecutor, @Nullable MemoryCache<CacheKey, CloseableImage> memoryCache, @Nullable ImmutableList<DrawableFactory> globalDrawableFactories) {
        super(deferredReleaser, uiThreadExecutor, null, null);
        this.mResources = resources;
        this.mDefaultDrawableFactory = new DefaultDrawableFactory(resources, animatedDrawableFactory);
        this.mGlobalDrawableFactories = globalDrawableFactories;
        this.mMemoryCache = memoryCache;
    }

    public void initialize(Supplier<DataSource<CloseableReference<CloseableImage>>> dataSourceSupplier, String id, CacheKey cacheKey, Object callerContext, @Nullable ImmutableList<DrawableFactory> customDrawableFactories, @Nullable ImageOriginListener imageOriginListener) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("PipelineDraweeController#initialize");
        }
        super.initialize(id, callerContext);
        init(dataSourceSupplier);
        this.mCacheKey = cacheKey;
        setCustomDrawableFactories(customDrawableFactories);
        clearImageOriginListeners();
        maybeUpdateDebugOverlay(null);
        addImageOriginListener(imageOriginListener);
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    public synchronized void initializePerformanceMonitoring(@Nullable ImagePerfDataListener imagePerfDataListener) {
        if (this.mImagePerfMonitor != null) {
            this.mImagePerfMonitor.reset();
        }
        if (imagePerfDataListener != null) {
            if (this.mImagePerfMonitor == null) {
                this.mImagePerfMonitor = new ImagePerfMonitor(AwakeTimeSinceBootClock.get(), this);
            }
            this.mImagePerfMonitor.addImagePerfDataListener(imagePerfDataListener);
            this.mImagePerfMonitor.setEnabled(true);
        }
    }

    public void setDrawDebugOverlay(boolean drawDebugOverlay) {
        this.mDrawDebugOverlay = drawDebugOverlay;
    }

    public void setCustomDrawableFactories(@Nullable ImmutableList<DrawableFactory> customDrawableFactories) {
        this.mCustomDrawableFactories = customDrawableFactories;
    }

    public synchronized void addRequestListener(RequestListener requestListener) {
        if (this.mRequestListeners == null) {
            this.mRequestListeners = new HashSet();
        }
        this.mRequestListeners.add(requestListener);
    }

    public synchronized void removeRequestListener(RequestListener requestListener) {
        if (this.mRequestListeners != null) {
            this.mRequestListeners.remove(requestListener);
        }
    }

    public synchronized void addImageOriginListener(ImageOriginListener imageOriginListener) {
        if (this.mImageOriginListener instanceof ForwardingImageOriginListener) {
            ((ForwardingImageOriginListener) this.mImageOriginListener).addImageOriginListener(imageOriginListener);
        } else if (this.mImageOriginListener != null) {
            this.mImageOriginListener = new ForwardingImageOriginListener(this.mImageOriginListener, imageOriginListener);
        } else {
            this.mImageOriginListener = imageOriginListener;
        }
    }

    public synchronized void removeImageOriginListener(ImageOriginListener imageOriginListener) {
        if (this.mImageOriginListener instanceof ForwardingImageOriginListener) {
            ((ForwardingImageOriginListener) this.mImageOriginListener).removeImageOriginListener(imageOriginListener);
        } else if (this.mImageOriginListener != null) {
            this.mImageOriginListener = new ForwardingImageOriginListener(this.mImageOriginListener, imageOriginListener);
        } else {
            this.mImageOriginListener = imageOriginListener;
        }
    }

    protected void clearImageOriginListeners() {
        synchronized (this) {
            this.mImageOriginListener = null;
        }
    }

    private void init(Supplier<DataSource<CloseableReference<CloseableImage>>> dataSourceSupplier) {
        this.mDataSourceSupplier = dataSourceSupplier;
        maybeUpdateDebugOverlay(null);
    }

    protected Resources getResources() {
        return this.mResources;
    }

    protected CacheKey getCacheKey() {
        return this.mCacheKey;
    }

    @Nullable
    public synchronized RequestListener getRequestListener() {
        RequestListener imageOriginRequestListener = null;
        if (this.mImageOriginListener != null) {
            imageOriginRequestListener = new ImageOriginRequestListener(getId(), this.mImageOriginListener);
        }
        if (this.mRequestListeners == null) {
            return imageOriginRequestListener;
        }
        ForwardingRequestListener requestListener = new ForwardingRequestListener(this.mRequestListeners);
        if (imageOriginRequestListener != null) {
            requestListener.addRequestListener(imageOriginRequestListener);
        }
        return requestListener;
    }

    @Override // com.facebook.drawee.controller.AbstractDraweeController
    protected DataSource<CloseableReference<CloseableImage>> getDataSource() {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("PipelineDraweeController#getDataSource");
        }
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x: getDataSource", Integer.valueOf(System.identityHashCode(this)));
        }
        DataSource<CloseableReference<CloseableImage>> result = this.mDataSourceSupplier.get();
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
        return result;
    }

    public Drawable createDrawable(CloseableReference<CloseableImage> image) {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("PipelineDraweeController#createDrawable");
            }
            Preconditions.checkState(CloseableReference.isValid(image));
            CloseableImage closeableImage = image.get();
            maybeUpdateDebugOverlay(closeableImage);
            Drawable drawable = maybeCreateDrawableFromFactories(this.mCustomDrawableFactories, closeableImage);
            if (drawable != null) {
                return drawable;
            }
            Drawable drawable2 = maybeCreateDrawableFromFactories(this.mGlobalDrawableFactories, closeableImage);
            if (drawable2 != null) {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
                return drawable2;
            }
            Drawable drawable3 = this.mDefaultDrawableFactory.createDrawable(closeableImage);
            if (drawable3 != null) {
                if (FrescoSystrace.isTracing()) {
                    FrescoSystrace.endSection();
                }
                return drawable3;
            }
            throw new UnsupportedOperationException("Unrecognized image class: " + closeableImage);
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    @Nullable
    private Drawable maybeCreateDrawableFromFactories(@Nullable ImmutableList<DrawableFactory> drawableFactories, CloseableImage closeableImage) {
        Drawable drawable;
        if (drawableFactories == null) {
            return null;
        }
        Iterator<DrawableFactory> it = drawableFactories.iterator();
        while (it.hasNext()) {
            DrawableFactory factory = it.next();
            if (factory.supportsImageType(closeableImage) && (drawable = factory.createDrawable(closeableImage)) != null) {
                return drawable;
            }
        }
        return null;
    }

    @Override // com.facebook.drawee.controller.AbstractDraweeController, com.facebook.drawee.interfaces.DraweeController
    public void setHierarchy(@Nullable DraweeHierarchy hierarchy) {
        super.setHierarchy(hierarchy);
        maybeUpdateDebugOverlay(null);
    }

    @Override // com.facebook.drawee.interfaces.DraweeController
    public boolean isSameImageRequest(@Nullable DraweeController other) {
        CacheKey cacheKey = this.mCacheKey;
        if (cacheKey == null || !(other instanceof PipelineDraweeController)) {
            return false;
        }
        return Objects.equal(cacheKey, ((PipelineDraweeController) other).getCacheKey());
    }

    private void maybeUpdateDebugOverlay(@Nullable CloseableImage image) {
        if (this.mDrawDebugOverlay) {
            if (getControllerOverlay() == null) {
                DebugControllerOverlayDrawable controllerOverlay = new DebugControllerOverlayDrawable();
                ImageLoadingTimeControllerListener overlayImageLoadListener = new ImageLoadingTimeControllerListener(controllerOverlay);
                this.mDebugOverlayImageOriginListener = new DebugOverlayImageOriginListener();
                addControllerListener(overlayImageLoadListener);
                setControllerOverlay(controllerOverlay);
            }
            if (this.mImageOriginListener == null) {
                addImageOriginListener(this.mDebugOverlayImageOriginListener);
            }
            if (getControllerOverlay() instanceof DebugControllerOverlayDrawable) {
                DebugControllerOverlayDrawable debugOverlay = (DebugControllerOverlayDrawable) getControllerOverlay();
                debugOverlay.setControllerId(getId());
                DraweeHierarchy draweeHierarchy = getHierarchy();
                ScalingUtils.ScaleType scaleType = null;
                if (draweeHierarchy != null) {
                    ScaleTypeDrawable scaleTypeDrawable = ScalingUtils.getActiveScaleTypeDrawable(draweeHierarchy.getTopLevelDrawable());
                    scaleType = scaleTypeDrawable != null ? scaleTypeDrawable.getScaleType() : null;
                }
                debugOverlay.setScaleType(scaleType);
                debugOverlay.setOrigin(this.mDebugOverlayImageOriginListener.getImageOrigin());
                if (image != null) {
                    debugOverlay.setDimensions(image.getWidth(), image.getHeight());
                    debugOverlay.setImageSize(image.getSizeInBytes());
                    return;
                }
                debugOverlay.reset();
            }
        }
    }

    public ImageInfo getImageInfo(CloseableReference<CloseableImage> image) {
        Preconditions.checkState(CloseableReference.isValid(image));
        return image.get();
    }

    public int getImageHash(@Nullable CloseableReference<CloseableImage> image) {
        if (image != null) {
            return image.getValueHash();
        }
        return 0;
    }

    public void releaseImage(@Nullable CloseableReference<CloseableImage> image) {
        CloseableReference.closeSafely(image);
    }

    @Override // com.facebook.drawee.controller.AbstractDraweeController
    protected void releaseDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof DrawableWithCaches) {
            ((DrawableWithCaches) drawable).dropCaches();
        }
    }

    @Override // com.facebook.drawee.controller.AbstractDraweeController
    @Nullable
    public CloseableReference<CloseableImage> getCachedImage() {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("PipelineDraweeController#getCachedImage");
        }
        try {
            if (!(this.mMemoryCache == null || this.mCacheKey == null)) {
                CloseableReference<CloseableImage> closeableImage = this.mMemoryCache.get(this.mCacheKey);
                if (closeableImage == null || closeableImage.get().getQualityInfo().isOfFullQuality()) {
                    if (FrescoSystrace.isTracing()) {
                        FrescoSystrace.endSection();
                    }
                    return closeableImage;
                }
                closeableImage.close();
                return null;
            }
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
            return null;
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    public void onImageLoadedFromCacheImmediately(String id, CloseableReference<CloseableImage> cachedImage) {
        super.onImageLoadedFromCacheImmediately(id, (String) cachedImage);
        synchronized (this) {
            if (this.mImageOriginListener != null) {
                this.mImageOriginListener.onImageLoaded(id, 5, true);
            }
        }
    }

    protected Supplier<DataSource<CloseableReference<CloseableImage>>> getDataSourceSupplier() {
        return this.mDataSourceSupplier;
    }

    @Override // com.facebook.drawee.controller.AbstractDraweeController
    public String toString() {
        return Objects.toStringHelper(this).add("super", super.toString()).add("dataSourceSupplier", this.mDataSourceSupplier).toString();
    }
}
