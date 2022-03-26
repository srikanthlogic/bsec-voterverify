package androidx.camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.LensFacingConverter;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import java.io.File;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CameraView extends FrameLayout {
    static final boolean DEBUG;
    private static final String EXTRA_CAMERA_DIRECTION;
    private static final String EXTRA_CAPTURE_MODE;
    private static final String EXTRA_FLASH;
    private static final String EXTRA_MAX_VIDEO_DURATION;
    private static final String EXTRA_MAX_VIDEO_SIZE;
    private static final String EXTRA_PINCH_TO_ZOOM_ENABLED;
    private static final String EXTRA_SCALE_TYPE;
    private static final String EXTRA_SUPER;
    private static final String EXTRA_ZOOM_RATIO;
    private static final int FLASH_MODE_AUTO;
    private static final int FLASH_MODE_OFF;
    private static final int FLASH_MODE_ON;
    static final int INDEFINITE_VIDEO_DURATION;
    static final int INDEFINITE_VIDEO_SIZE;
    private static final int LENS_FACING_BACK;
    private static final int LENS_FACING_FRONT;
    private static final int LENS_FACING_NONE;
    static final String TAG = CameraView.class.getSimpleName();
    CameraXModule mCameraModule;
    private final DisplayManager.DisplayListener mDisplayListener;
    private long mDownEventTimestamp;
    private boolean mIsPinchToZoomEnabled;
    private PinchToZoomGestureDetector mPinchToZoomGestureDetector;
    private PreviewView mPreviewView;
    private MotionEvent mUpEvent;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsPinchToZoomEnabled = true;
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: androidx.camera.view.CameraView.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                CameraView.this.mCameraModule.invalidateView();
            }
        };
        init(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsPinchToZoomEnabled = true;
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: androidx.camera.view.CameraView.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                CameraView.this.mCameraModule.invalidateView();
            }
        };
        init(context, attrs);
    }

    public void bindToLifecycle(LifecycleOwner lifecycleOwner) {
        this.mCameraModule.bindToLifecycle(lifecycleOwner);
    }

    private void init(Context context, AttributeSet attrs) {
        PreviewView previewView = new PreviewView(getContext());
        this.mPreviewView = previewView;
        addView(previewView, 0);
        this.mCameraModule = new CameraXModule(this);
        if (attrs != null) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.CameraView);
            setScaleType(PreviewView.ScaleType.fromId(a2.getInteger(R.styleable.CameraView_scaleType, getScaleType().getId())));
            setPinchToZoomEnabled(a2.getBoolean(R.styleable.CameraView_pinchToZoomEnabled, isPinchToZoomEnabled()));
            setCaptureMode(CaptureMode.fromId(a2.getInteger(R.styleable.CameraView_captureMode, getCaptureMode().getId())));
            int lensFacing = a2.getInt(R.styleable.CameraView_lensFacing, 2);
            if (lensFacing == 0) {
                setCameraLensFacing(null);
            } else if (lensFacing == 1) {
                setCameraLensFacing(0);
            } else if (lensFacing == 2) {
                setCameraLensFacing(1);
            }
            int flashMode = a2.getInt(R.styleable.CameraView_flash, 0);
            if (flashMode == 1) {
                setFlash(0);
            } else if (flashMode == 2) {
                setFlash(1);
            } else if (flashMode == 4) {
                setFlash(2);
            }
            a2.recycle();
        }
        if (getBackground() == null) {
            setBackgroundColor(-15658735);
        }
        this.mPinchToZoomGestureDetector = new PinchToZoomGestureDetector(this, context);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(-1, -1);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable(EXTRA_SUPER, super.onSaveInstanceState());
        state.putInt(EXTRA_SCALE_TYPE, getScaleType().getId());
        state.putFloat(EXTRA_ZOOM_RATIO, getZoomRatio());
        state.putBoolean(EXTRA_PINCH_TO_ZOOM_ENABLED, isPinchToZoomEnabled());
        state.putString(EXTRA_FLASH, FlashModeConverter.nameOf(getFlash()));
        state.putLong(EXTRA_MAX_VIDEO_DURATION, getMaxVideoDuration());
        state.putLong(EXTRA_MAX_VIDEO_SIZE, getMaxVideoSize());
        if (getCameraLensFacing() != null) {
            state.putString(EXTRA_CAMERA_DIRECTION, LensFacingConverter.nameOf(getCameraLensFacing().intValue()));
        }
        state.putInt(EXTRA_CAPTURE_MODE, getCaptureMode().getId());
        return state;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable savedState) {
        Integer num;
        if (savedState instanceof Bundle) {
            Bundle state = (Bundle) savedState;
            super.onRestoreInstanceState(state.getParcelable(EXTRA_SUPER));
            setScaleType(PreviewView.ScaleType.fromId(state.getInt(EXTRA_SCALE_TYPE)));
            setZoomRatio(state.getFloat(EXTRA_ZOOM_RATIO));
            setPinchToZoomEnabled(state.getBoolean(EXTRA_PINCH_TO_ZOOM_ENABLED));
            setFlash(FlashModeConverter.valueOf(state.getString(EXTRA_FLASH)));
            setMaxVideoDuration(state.getLong(EXTRA_MAX_VIDEO_DURATION));
            setMaxVideoSize(state.getLong(EXTRA_MAX_VIDEO_SIZE));
            String lensFacingString = state.getString(EXTRA_CAMERA_DIRECTION);
            if (TextUtils.isEmpty(lensFacingString)) {
                num = null;
            } else {
                num = Integer.valueOf(LensFacingConverter.valueOf(lensFacingString));
            }
            setCameraLensFacing(num);
            setCaptureMode(CaptureMode.fromId(state.getInt(EXTRA_CAPTURE_MODE)));
            return;
        }
        super.onRestoreInstanceState(savedState);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((DisplayManager) getContext().getSystemService("display")).registerDisplayListener(this.mDisplayListener, new Handler(Looper.getMainLooper()));
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((DisplayManager) getContext().getSystemService("display")).unregisterDisplayListener(this.mDisplayListener);
    }

    public LiveData<PreviewView.StreamState> getPreviewStreamState() {
        return this.mPreviewView.getPreviewStreamState();
    }

    public PreviewView getPreviewView() {
        return this.mPreviewView;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            this.mCameraModule.bindToLifecycleAfterViewMeasured();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mCameraModule.bindToLifecycleAfterViewMeasured();
        this.mCameraModule.invalidateView();
        super.onLayout(changed, left, top, right, bottom);
    }

    public int getDisplaySurfaceRotation() {
        Display display = getDisplay();
        if (display == null) {
            return 0;
        }
        return display.getRotation();
    }

    public PreviewView.ScaleType getScaleType() {
        return this.mPreviewView.getScaleType();
    }

    public void setScaleType(PreviewView.ScaleType scaleType) {
        this.mPreviewView.setScaleType(scaleType);
    }

    public CaptureMode getCaptureMode() {
        return this.mCameraModule.getCaptureMode();
    }

    public void setCaptureMode(CaptureMode captureMode) {
        this.mCameraModule.setCaptureMode(captureMode);
    }

    public long getMaxVideoDuration() {
        return this.mCameraModule.getMaxVideoDuration();
    }

    private void setMaxVideoDuration(long duration) {
        this.mCameraModule.setMaxVideoDuration(duration);
    }

    private long getMaxVideoSize() {
        return this.mCameraModule.getMaxVideoSize();
    }

    private void setMaxVideoSize(long size) {
        this.mCameraModule.setMaxVideoSize(size);
    }

    public void takePicture(Executor executor, ImageCapture.OnImageCapturedCallback callback) {
        this.mCameraModule.takePicture(executor, callback);
    }

    public void takePicture(ImageCapture.OutputFileOptions outputFileOptions, Executor executor, ImageCapture.OnImageSavedCallback callback) {
        this.mCameraModule.takePicture(outputFileOptions, executor, callback);
    }

    public void startRecording(File file, Executor executor, VideoCapture.OnVideoSavedCallback callback) {
        this.mCameraModule.startRecording(file, executor, callback);
    }

    public void stopRecording() {
        this.mCameraModule.stopRecording();
    }

    public boolean isRecording() {
        return this.mCameraModule.isRecording();
    }

    public boolean hasCameraWithLensFacing(int lensFacing) {
        return this.mCameraModule.hasCameraWithLensFacing(lensFacing);
    }

    public void toggleCamera() {
        this.mCameraModule.toggleCamera();
    }

    public void setCameraLensFacing(Integer lensFacing) {
        this.mCameraModule.setCameraLensFacing(lensFacing);
    }

    public Integer getCameraLensFacing() {
        return this.mCameraModule.getLensFacing();
    }

    public int getFlash() {
        return this.mCameraModule.getFlash();
    }

    public void setFlash(int flashMode) {
        this.mCameraModule.setFlash(flashMode);
    }

    private long delta() {
        return System.currentTimeMillis() - this.mDownEventTimestamp;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCameraModule.isPaused()) {
            return false;
        }
        if (isPinchToZoomEnabled()) {
            this.mPinchToZoomGestureDetector.onTouchEvent(event);
        }
        if (event.getPointerCount() == 2 && isPinchToZoomEnabled() && isZoomSupported()) {
            return true;
        }
        int action = event.getAction();
        if (action == 0) {
            this.mDownEventTimestamp = System.currentTimeMillis();
        } else if (action != 1) {
            return false;
        } else {
            if (delta() < ((long) ViewConfiguration.getLongPressTimeout()) && this.mCameraModule.isBoundToLifecycle()) {
                this.mUpEvent = event;
                performClick();
            }
        }
        return true;
    }

    @Override // android.view.View
    public boolean performClick() {
        super.performClick();
        MotionEvent motionEvent = this.mUpEvent;
        float x = motionEvent != null ? motionEvent.getX() : getX() + (((float) getWidth()) / 2.0f);
        MotionEvent motionEvent2 = this.mUpEvent;
        float y = motionEvent2 != null ? motionEvent2.getY() : getY() + (((float) getHeight()) / 2.0f);
        this.mUpEvent = null;
        MeteringPointFactory pointFactory = this.mPreviewView.createMeteringPointFactory(new CameraSelector.Builder().requireLensFacing(this.mCameraModule.getLensFacing().intValue()).build());
        MeteringPoint afPoint = pointFactory.createPoint(x, y, 0.16666667f);
        MeteringPoint aePoint = pointFactory.createPoint(x, y, 1.5f * 0.16666667f);
        Camera camera = this.mCameraModule.getCamera();
        if (camera != null) {
            Futures.addCallback(camera.getCameraControl().startFocusAndMetering(new FocusMeteringAction.Builder(afPoint, 1).addPoint(aePoint, 2).build()), new FutureCallback<FocusMeteringResult>() { // from class: androidx.camera.view.CameraView.2
                public void onSuccess(FocusMeteringResult result) {
                }

                @Override // androidx.camera.core.impl.utils.futures.FutureCallback
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            }, CameraXExecutors.directExecutor());
        } else {
            Log.d(TAG, "cannot access camera");
        }
        return true;
    }

    float rangeLimit(float val, float max, float min) {
        return Math.min(Math.max(val, min), max);
    }

    public boolean isPinchToZoomEnabled() {
        return this.mIsPinchToZoomEnabled;
    }

    public void setPinchToZoomEnabled(boolean enabled) {
        this.mIsPinchToZoomEnabled = enabled;
    }

    public float getZoomRatio() {
        return this.mCameraModule.getZoomRatio();
    }

    public void setZoomRatio(float zoomRatio) {
        this.mCameraModule.setZoomRatio(zoomRatio);
    }

    public float getMinZoomRatio() {
        return this.mCameraModule.getMinZoomRatio();
    }

    public float getMaxZoomRatio() {
        return this.mCameraModule.getMaxZoomRatio();
    }

    public boolean isZoomSupported() {
        return this.mCameraModule.isZoomSupported();
    }

    public void enableTorch(boolean torch) {
        this.mCameraModule.enableTorch(torch);
    }

    public boolean isTorchOn() {
        return this.mCameraModule.isTorchOn();
    }

    /* loaded from: classes.dex */
    public enum CaptureMode {
        IMAGE(0),
        VIDEO(1),
        MIXED(2);
        
        private final int mId;

        int getId() {
            return this.mId;
        }

        CaptureMode(int id) {
            this.mId = id;
        }

        static CaptureMode fromId(int id) {
            CaptureMode[] values = values();
            for (CaptureMode f : values) {
                if (f.mId == id) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class S extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleGestureDetector.OnScaleGestureListener mListener;

        S() {
        }

        void setRealGestureDetector(ScaleGestureDetector.OnScaleGestureListener l) {
            this.mListener = l;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            return this.mListener.onScale(detector);
        }
    }

    /* loaded from: classes.dex */
    public class PinchToZoomGestureDetector extends ScaleGestureDetector implements ScaleGestureDetector.OnScaleGestureListener {
        PinchToZoomGestureDetector(CameraView cameraView, Context context) {
            this(context, new S());
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        PinchToZoomGestureDetector(Context context, S s) {
            super(context, s);
            CameraView.this = r1;
            s.setRealGestureDetector(this);
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            float scale;
            float scale2 = detector.getScaleFactor();
            if (scale2 > 1.0f) {
                scale = ((scale2 - 1.0f) * 2.0f) + 1.0f;
            } else {
                scale = 1.0f - ((1.0f - scale2) * 2.0f);
            }
            CameraView cameraView = CameraView.this;
            CameraView.this.setZoomRatio(cameraView.rangeLimit(CameraView.this.getZoomRatio() * scale, cameraView.getMaxZoomRatio(), CameraView.this.getMinZoomRatio()));
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }
}
