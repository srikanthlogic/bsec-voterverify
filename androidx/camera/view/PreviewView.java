package androidx.camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.core.impl.CameraInfoInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.utils.Threads;
import androidx.camera.view.PreviewViewImplementation;
import androidx.camera.view.preview.transform.PreviewTransform;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.facebook.imagepipeline.common.RotationOptions;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public class PreviewView extends FrameLayout {
    static final int DEFAULT_BACKGROUND_COLOR;
    private static final ImplementationMode DEFAULT_IMPL_MODE = ImplementationMode.SURFACE_VIEW;
    private static final String TAG;
    private AtomicReference<PreviewStreamStateObserver> mActiveStreamStateObserver;
    PreviewViewImplementation mImplementation;
    private final View.OnLayoutChangeListener mOnLayoutChangeListener;
    private ImplementationMode mPreferredImplementationMode;
    private MutableLiveData<StreamState> mPreviewStreamStateLiveData;
    private PreviewTransform mPreviewTransform;

    /* loaded from: classes.dex */
    public enum ImplementationMode {
        SURFACE_VIEW,
        TEXTURE_VIEW
    }

    /* loaded from: classes.dex */
    public enum StreamState {
        IDLE,
        STREAMING
    }

    public PreviewView(Context context) {
        this(context, null);
    }

    public PreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARN: Finally extract failed */
    public PreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mPreferredImplementationMode = DEFAULT_IMPL_MODE;
        this.mPreviewTransform = new PreviewTransform();
        this.mPreviewStreamStateLiveData = new MutableLiveData<>(StreamState.IDLE);
        this.mActiveStreamStateObserver = new AtomicReference<>();
        this.mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: androidx.camera.view.PreviewView.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (PreviewView.this.mImplementation != null) {
                    PreviewView.this.mImplementation.redrawPreview();
                }
            }
        };
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PreviewView, defStyleAttr, defStyleRes);
        if (Build.VERSION.SDK_INT >= 29) {
            saveAttributeDataForStyleable(context, R.styleable.PreviewView, attrs, attributes, defStyleAttr, defStyleRes);
        }
        try {
            setScaleType(ScaleType.fromId(attributes.getInteger(R.styleable.PreviewView_scaleType, this.mPreviewTransform.getScaleType().getId())));
            attributes.recycle();
            if (getBackground() == null) {
                setBackgroundColor(ContextCompat.getColor(getContext(), DEFAULT_BACKGROUND_COLOR));
            }
        } catch (Throwable th) {
            attributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        PreviewViewImplementation previewViewImplementation = this.mImplementation;
        if (previewViewImplementation != null) {
            previewViewImplementation.onAttachedToWindow();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        PreviewViewImplementation previewViewImplementation = this.mImplementation;
        if (previewViewImplementation != null) {
            previewViewImplementation.onDetachedFromWindow();
        }
    }

    public void setPreferredImplementationMode(ImplementationMode preferredMode) {
        this.mPreferredImplementationMode = preferredMode;
    }

    public ImplementationMode getPreferredImplementationMode() {
        return this.mPreferredImplementationMode;
    }

    public Preview.SurfaceProvider createSurfaceProvider() {
        Threads.checkMainThread();
        removeAllViews();
        return new Preview.SurfaceProvider() { // from class: androidx.camera.view.-$$Lambda$PreviewView$hqWNTNJFpdxLAnMl9Mw2vmmrFic
            @Override // androidx.camera.core.Preview.SurfaceProvider
            public final void onSurfaceRequested(SurfaceRequest surfaceRequest) {
                PreviewView.this.lambda$createSurfaceProvider$1$PreviewView(surfaceRequest);
            }
        };
    }

    public /* synthetic */ void lambda$createSurfaceProvider$1$PreviewView(SurfaceRequest surfaceRequest) {
        Log.d(TAG, "Surface requested by Preview.");
        CameraInternal camera = (CameraInternal) surfaceRequest.getCamera();
        ImplementationMode actualImplementationMode = computeImplementationMode(camera.getCameraInfo(), this.mPreferredImplementationMode);
        this.mPreviewTransform.setSensorDimensionFlipNeeded(isSensorDimensionFlipNeeded(camera.getCameraInfo()));
        this.mImplementation = computeImplementation(actualImplementationMode);
        this.mImplementation.init(this, this.mPreviewTransform);
        PreviewStreamStateObserver streamStateObserver = new PreviewStreamStateObserver((CameraInfoInternal) camera.getCameraInfo(), this.mPreviewStreamStateLiveData, this.mImplementation);
        this.mActiveStreamStateObserver.set(streamStateObserver);
        camera.getCameraState().addObserver(ContextCompat.getMainExecutor(getContext()), streamStateObserver);
        this.mImplementation.onSurfaceRequested(surfaceRequest, new PreviewViewImplementation.OnSurfaceNotInUseListener(streamStateObserver, camera) { // from class: androidx.camera.view.-$$Lambda$PreviewView$JwjZCrolfVsio0JBZDSbpG1PEkU
            private final /* synthetic */ PreviewStreamStateObserver f$1;
            private final /* synthetic */ CameraInternal f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // androidx.camera.view.PreviewViewImplementation.OnSurfaceNotInUseListener
            public final void onSurfaceNotInUse() {
                PreviewView.this.lambda$createSurfaceProvider$0$PreviewView(this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$createSurfaceProvider$0$PreviewView(PreviewStreamStateObserver streamStateObserver, CameraInternal camera) {
        if (this.mActiveStreamStateObserver.compareAndSet(streamStateObserver, null)) {
            streamStateObserver.updatePreviewStreamState(StreamState.IDLE);
        }
        streamStateObserver.clear();
        camera.getCameraState().removeObserver(streamStateObserver);
    }

    public void setScaleType(ScaleType scaleType) {
        this.mPreviewTransform.setScaleType(scaleType);
        PreviewViewImplementation previewViewImplementation = this.mImplementation;
        if (previewViewImplementation != null) {
            previewViewImplementation.redrawPreview();
        }
    }

    public int getDeviceRotationForRemoteDisplayMode() {
        return this.mPreviewTransform.getDeviceRotation();
    }

    public void setDeviceRotationForRemoteDisplayMode(int deviceRotation) {
        if (deviceRotation != this.mPreviewTransform.getDeviceRotation() && isRemoteDisplayMode()) {
            this.mPreviewTransform.setDeviceRotation(deviceRotation);
            PreviewViewImplementation previewViewImplementation = this.mImplementation;
            if (previewViewImplementation != null) {
                previewViewImplementation.redrawPreview();
            }
        }
    }

    public ScaleType getScaleType() {
        return this.mPreviewTransform.getScaleType();
    }

    public MeteringPointFactory createMeteringPointFactory(CameraSelector cameraSelector) {
        Display display = getDisplay();
        PreviewViewImplementation previewViewImplementation = this.mImplementation;
        return new PreviewViewMeteringPointFactory(display, cameraSelector, previewViewImplementation == null ? null : previewViewImplementation.getResolution(), this.mPreviewTransform.getScaleType(), getWidth(), getHeight());
    }

    public LiveData<StreamState> getPreviewStreamState() {
        return this.mPreviewStreamStateLiveData;
    }

    public Bitmap getBitmap() {
        PreviewViewImplementation previewViewImplementation = this.mImplementation;
        if (previewViewImplementation == null) {
            return null;
        }
        return previewViewImplementation.getBitmap();
    }

    private ImplementationMode computeImplementationMode(CameraInfo cameraInfo, ImplementationMode preferredMode) {
        return (Build.VERSION.SDK_INT <= 24 || cameraInfo.getImplementationType().equals(CameraInfo.IMPLEMENTATION_TYPE_CAMERA2_LEGACY) || isRemoteDisplayMode()) ? ImplementationMode.TEXTURE_VIEW : preferredMode;
    }

    /* renamed from: androidx.camera.view.PreviewView$2 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$view$PreviewView$ImplementationMode = new int[ImplementationMode.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$view$PreviewView$ImplementationMode[ImplementationMode.SURFACE_VIEW.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$camera$view$PreviewView$ImplementationMode[ImplementationMode.TEXTURE_VIEW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private PreviewViewImplementation computeImplementation(ImplementationMode mode) {
        int i = AnonymousClass2.$SwitchMap$androidx$camera$view$PreviewView$ImplementationMode[mode.ordinal()];
        if (i == 1) {
            return new SurfaceViewImplementation();
        }
        if (i == 2) {
            return new TextureViewImplementation();
        }
        throw new IllegalStateException("Unsupported implementation mode " + mode);
    }

    private boolean isSensorDimensionFlipNeeded(CameraInfo cameraInfo) {
        return cameraInfo.getSensorRotationDegrees() % RotationOptions.ROTATE_180 == 90;
    }

    private boolean isRemoteDisplayMode() {
        Display display = ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
        return (((DisplayManager) getContext().getSystemService("display")).getDisplays().length <= 1 || display == null || display.getDisplayId() == 0) ? false : true;
    }

    /* loaded from: classes.dex */
    public enum ScaleType {
        FILL_START(0),
        FILL_CENTER(1),
        FILL_END(2),
        FIT_START(3),
        FIT_CENTER(4),
        FIT_END(5);
        
        private final int mId;

        ScaleType(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }

        public static ScaleType fromId(int id) {
            ScaleType[] values = values();
            for (ScaleType scaleType : values) {
                if (scaleType.mId == id) {
                    return scaleType;
                }
            }
            throw new IllegalArgumentException("Unknown scale type id " + id);
        }
    }
}
