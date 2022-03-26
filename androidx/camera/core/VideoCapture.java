package androidx.camera.core;

import android.location.Location;
import android.media.AudioRecord;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.UseCase;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.CaptureConfig;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ConfigProvider;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.ImmediateSurface;
import androidx.camera.core.impl.MutableConfig;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.VideoCaptureConfig;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.internal.TargetConfig;
import androidx.camera.core.internal.ThreadConfig;
import androidx.camera.core.internal.UseCaseEventConfig;
import androidx.camera.core.internal.utils.UseCaseConfigUtil;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class VideoCapture extends UseCase {
    private static final String AUDIO_MIME_TYPE;
    private static final int DEQUE_TIMEOUT_USEC;
    public static final int ERROR_ENCODER;
    public static final int ERROR_MUXER;
    public static final int ERROR_RECORDING_IN_PROGRESS;
    public static final int ERROR_UNKNOWN;
    private static final String TAG;
    private static final String VIDEO_MIME_TYPE;
    private int mAudioBitRate;
    private int mAudioBufferSize;
    private int mAudioChannelCount;
    private MediaCodec mAudioEncoder;
    private AudioRecord mAudioRecorder;
    private int mAudioSampleRate;
    private int mAudioTrackIndex;
    Surface mCameraSurface;
    private DeferrableSurface mDeferrableSurface;
    private MediaMuxer mMuxer;
    MediaCodec mVideoEncoder;
    private int mVideoTrackIndex;
    public static final Defaults DEFAULT_CONFIG = new Defaults();
    private static final Metadata EMPTY_METADATA = new Metadata();
    private static final int[] CamcorderQuality = {8, 6, 5, 4};
    private static final short[] sAudioEncoding = {2, 3, 4};
    private final MediaCodec.BufferInfo mVideoBufferInfo = new MediaCodec.BufferInfo();
    private final Object mMuxerLock = new Object();
    private final HandlerThread mVideoHandlerThread = new HandlerThread("CameraX-video encoding thread");
    private final HandlerThread mAudioHandlerThread = new HandlerThread("CameraX-audio encoding thread");
    private final AtomicBoolean mEndOfVideoStreamSignal = new AtomicBoolean(true);
    private final AtomicBoolean mEndOfAudioStreamSignal = new AtomicBoolean(true);
    private final AtomicBoolean mEndOfAudioVideoSignal = new AtomicBoolean(true);
    private final MediaCodec.BufferInfo mAudioBufferInfo = new MediaCodec.BufferInfo();
    private final AtomicBoolean mIsFirstVideoSampleWrite = new AtomicBoolean(false);
    private final AtomicBoolean mIsFirstAudioSampleWrite = new AtomicBoolean(false);
    private boolean mMuxerStarted = false;
    private boolean mIsRecording = false;
    private final Handler mVideoHandler = new Handler(this.mVideoHandlerThread.getLooper());
    private final Handler mAudioHandler = new Handler(this.mAudioHandlerThread.getLooper());

    /* loaded from: classes.dex */
    public static final class Metadata {
        public Location location;
    }

    /* loaded from: classes.dex */
    public interface OnVideoSavedCallback {
        void onError(int i, String str, Throwable th);

        void onVideoSaved(File file);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface VideoCaptureError {
    }

    VideoCapture(VideoCaptureConfig config) {
        super(config);
        this.mVideoHandlerThread.start();
        this.mAudioHandlerThread.start();
    }

    private static MediaFormat createMediaFormat(VideoCaptureConfig config, Size resolution) {
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, resolution.getWidth(), resolution.getHeight());
        format.setInteger("color-format", 2130708361);
        format.setInteger("bitrate", config.getBitRate());
        format.setInteger("frame-rate", config.getVideoFrameRate());
        format.setInteger("i-frame-interval", config.getIFrameInterval());
        return format;
    }

    @Override // androidx.camera.core.UseCase
    public UseCaseConfig.Builder<?, ?, ?> getDefaultBuilder(CameraInfo cameraInfo) {
        VideoCaptureConfig defaults = (VideoCaptureConfig) CameraX.getDefaultUseCaseConfig(VideoCaptureConfig.class, cameraInfo);
        if (defaults != null) {
            return Builder.fromConfig(defaults);
        }
        return null;
    }

    @Override // androidx.camera.core.UseCase
    protected Size onSuggestedResolutionUpdated(Size suggestedResolution) {
        if (this.mCameraSurface != null) {
            this.mVideoEncoder.stop();
            this.mVideoEncoder.release();
            this.mAudioEncoder.stop();
            this.mAudioEncoder.release();
            releaseCameraSurface(false);
        }
        try {
            this.mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            this.mAudioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE);
            setupEncoder(getCameraId(), suggestedResolution);
            return suggestedResolution;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create MediaCodec due to: " + e.getCause());
        }
    }

    public void startRecording(File saveLocation, Executor executor, OnVideoSavedCallback callback) {
        this.mIsFirstVideoSampleWrite.set(false);
        this.mIsFirstAudioSampleWrite.set(false);
        startRecording(saveLocation, EMPTY_METADATA, executor, callback);
    }

    public void startRecording(final File saveLocation, Metadata metadata, Executor executor, OnVideoSavedCallback callback) {
        IOException e;
        Throwable th;
        Log.i(TAG, "startRecording");
        final OnVideoSavedCallback postListener = new VideoSavedListenerWrapper(executor, callback);
        if (!this.mEndOfAudioVideoSignal.get()) {
            postListener.onError(3, "It is still in video recording!", null);
            return;
        }
        try {
            this.mAudioRecorder.startRecording();
            CameraInternal attachedCamera = getCamera();
            final String cameraId = getCameraId();
            final Size resolution = getAttachedSurfaceResolution();
            try {
                Log.i(TAG, "videoEncoder start");
                this.mVideoEncoder.start();
                Log.i(TAG, "audioEncoder start");
                this.mAudioEncoder.start();
                int relativeRotation = attachedCamera.getCameraInfoInternal().getSensorRotationDegrees(((ImageOutputConfig) getUseCaseConfig()).getTargetRotation(0));
                try {
                    synchronized (this.mMuxerLock) {
                        try {
                            this.mMuxer = new MediaMuxer(saveLocation.getAbsolutePath(), 0);
                            this.mMuxer.setOrientationHint(relativeRotation);
                            if (metadata.location != null) {
                                try {
                                    this.mMuxer.setLocation((float) metadata.location.getLatitude(), (float) metadata.location.getLongitude());
                                } catch (Throwable th2) {
                                    th = th2;
                                    while (true) {
                                        try {
                                            try {
                                                break;
                                            } catch (IOException e2) {
                                                e = e2;
                                                setupEncoder(cameraId, resolution);
                                                postListener.onError(2, "MediaMuxer creation failed!", e);
                                                return;
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                        }
                                    }
                                    throw th;
                                }
                            }
                            this.mEndOfVideoStreamSignal.set(false);
                            this.mEndOfAudioStreamSignal.set(false);
                            this.mEndOfAudioVideoSignal.set(false);
                            this.mIsRecording = true;
                            notifyActive();
                            this.mAudioHandler.post(new Runnable() { // from class: androidx.camera.core.VideoCapture.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    VideoCapture.this.audioEncode(postListener);
                                }
                            });
                            this.mVideoHandler.post(new Runnable() { // from class: androidx.camera.core.VideoCapture.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (!VideoCapture.this.videoEncode(postListener, cameraId, resolution)) {
                                        postListener.onVideoSaved(saveLocation);
                                    }
                                }
                            });
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                }
            } catch (IllegalStateException e4) {
                setupEncoder(cameraId, resolution);
                postListener.onError(1, "Audio/Video encoder start fail", e4);
            }
        } catch (IllegalStateException e5) {
            postListener.onError(1, "AudioRecorder start fail", e5);
        }
    }

    public void stopRecording() {
        Log.i(TAG, "stopRecording");
        notifyInactive();
        if (!this.mEndOfAudioVideoSignal.get() && this.mIsRecording) {
            this.mEndOfAudioStreamSignal.set(true);
        }
    }

    @Override // androidx.camera.core.UseCase
    public void clear() {
        this.mVideoHandlerThread.quitSafely();
        this.mAudioHandlerThread.quitSafely();
        MediaCodec mediaCodec = this.mAudioEncoder;
        if (mediaCodec != null) {
            mediaCodec.release();
            this.mAudioEncoder = null;
        }
        AudioRecord audioRecord = this.mAudioRecorder;
        if (audioRecord != null) {
            audioRecord.release();
            this.mAudioRecorder = null;
        }
        if (this.mCameraSurface != null) {
            releaseCameraSurface(true);
        }
    }

    private void releaseCameraSurface(boolean releaseVideoEncoder) {
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            MediaCodec videoEncoder = this.mVideoEncoder;
            deferrableSurface.close();
            this.mDeferrableSurface.getTerminationFuture().addListener(new Runnable(releaseVideoEncoder, videoEncoder) { // from class: androidx.camera.core.-$$Lambda$VideoCapture$vFHGdUhQ9YSrmNYVYvi35pHBmEc
                private final /* synthetic */ boolean f$0;
                private final /* synthetic */ MediaCodec f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    VideoCapture.lambda$releaseCameraSurface$0(this.f$0, this.f$1);
                }
            }, CameraXExecutors.mainThreadExecutor());
            if (releaseVideoEncoder) {
                this.mVideoEncoder = null;
            }
            this.mCameraSurface = null;
            this.mDeferrableSurface = null;
        }
    }

    public static /* synthetic */ void lambda$releaseCameraSurface$0(boolean releaseVideoEncoder, MediaCodec videoEncoder) {
        if (releaseVideoEncoder && videoEncoder != null) {
            videoEncoder.release();
        }
    }

    public void setTargetRotation(int rotation) {
        VideoCaptureConfig oldConfig = (VideoCaptureConfig) getUseCaseConfig();
        Builder builder = Builder.fromConfig(oldConfig);
        int oldRotation = oldConfig.getTargetRotation(-1);
        if (oldRotation == -1 || oldRotation != rotation) {
            UseCaseConfigUtil.updateTargetRotationAndRelatedConfigs(builder, rotation);
            updateUseCaseConfig(builder.getUseCaseConfig());
        }
    }

    void setupEncoder(final String cameraId, final Size resolution) {
        VideoCaptureConfig config = (VideoCaptureConfig) getUseCaseConfig();
        this.mVideoEncoder.reset();
        this.mVideoEncoder.configure(createMediaFormat(config, resolution), (Surface) null, (MediaCrypto) null, 1);
        if (this.mCameraSurface != null) {
            releaseCameraSurface(false);
        }
        Surface cameraSurface = this.mVideoEncoder.createInputSurface();
        this.mCameraSurface = cameraSurface;
        SessionConfig.Builder sessionConfigBuilder = SessionConfig.Builder.createFrom(config);
        DeferrableSurface deferrableSurface = this.mDeferrableSurface;
        if (deferrableSurface != null) {
            deferrableSurface.close();
        }
        this.mDeferrableSurface = new ImmediateSurface(this.mCameraSurface);
        ListenableFuture<Void> terminationFuture = this.mDeferrableSurface.getTerminationFuture();
        Objects.requireNonNull(cameraSurface);
        terminationFuture.addListener(new Runnable(cameraSurface) { // from class: androidx.camera.core.-$$Lambda$VideoCapture$bKhot3B1n1f2PgvvZExesMq2yMg
            private final /* synthetic */ Surface f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.release();
            }
        }, CameraXExecutors.mainThreadExecutor());
        sessionConfigBuilder.addSurface(this.mDeferrableSurface);
        sessionConfigBuilder.addErrorListener(new SessionConfig.ErrorListener() { // from class: androidx.camera.core.VideoCapture.3
            @Override // androidx.camera.core.impl.SessionConfig.ErrorListener
            public void onError(SessionConfig sessionConfig, SessionConfig.SessionError error) {
                if (VideoCapture.this.isCurrentCamera(cameraId)) {
                    VideoCapture.this.setupEncoder(cameraId, resolution);
                }
            }
        });
        updateSessionConfig(sessionConfigBuilder.build());
        setAudioParametersByCamcorderProfile(resolution, cameraId);
        this.mAudioEncoder.reset();
        this.mAudioEncoder.configure(createAudioMediaFormat(), (Surface) null, (MediaCrypto) null, 1);
        AudioRecord audioRecord = this.mAudioRecorder;
        if (audioRecord != null) {
            audioRecord.release();
        }
        this.mAudioRecorder = autoConfigAudioRecordSource(config);
        if (this.mAudioRecorder == null) {
            Log.e(TAG, "AudioRecord object cannot initialized correctly!");
        }
        this.mVideoTrackIndex = -1;
        this.mAudioTrackIndex = -1;
        this.mIsRecording = false;
    }

    private boolean writeVideoEncodedBuffer(int bufferIndex) {
        if (bufferIndex < 0) {
            Log.e(TAG, "Output buffer should not have negative index: " + bufferIndex);
            return false;
        }
        ByteBuffer outputBuffer = this.mVideoEncoder.getOutputBuffer(bufferIndex);
        if (outputBuffer == null) {
            Log.d(TAG, "OutputBuffer was null.");
            return false;
        }
        if (this.mAudioTrackIndex >= 0 && this.mVideoTrackIndex >= 0 && this.mVideoBufferInfo.size > 0) {
            outputBuffer.position(this.mVideoBufferInfo.offset);
            outputBuffer.limit(this.mVideoBufferInfo.offset + this.mVideoBufferInfo.size);
            this.mVideoBufferInfo.presentationTimeUs = System.nanoTime() / 1000;
            synchronized (this.mMuxerLock) {
                if (!this.mIsFirstVideoSampleWrite.get()) {
                    Log.i(TAG, "First video sample written.");
                    this.mIsFirstVideoSampleWrite.set(true);
                }
                this.mMuxer.writeSampleData(this.mVideoTrackIndex, outputBuffer, this.mVideoBufferInfo);
            }
        }
        this.mVideoEncoder.releaseOutputBuffer(bufferIndex, false);
        if ((this.mVideoBufferInfo.flags & 4) != 0) {
            return true;
        }
        return false;
    }

    private boolean writeAudioEncodedBuffer(int bufferIndex) {
        ByteBuffer buffer = getOutputBuffer(this.mAudioEncoder, bufferIndex);
        buffer.position(this.mAudioBufferInfo.offset);
        if (this.mAudioTrackIndex >= 0 && this.mVideoTrackIndex >= 0 && this.mAudioBufferInfo.size > 0 && this.mAudioBufferInfo.presentationTimeUs > 0) {
            try {
                synchronized (this.mMuxerLock) {
                    if (!this.mIsFirstAudioSampleWrite.get()) {
                        Log.i(TAG, "First audio sample written.");
                        this.mIsFirstAudioSampleWrite.set(true);
                    }
                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, buffer, this.mAudioBufferInfo);
                }
            } catch (Exception e) {
                Log.e(TAG, "audio error:size=" + this.mAudioBufferInfo.size + "/offset=" + this.mAudioBufferInfo.offset + "/timeUs=" + this.mAudioBufferInfo.presentationTimeUs);
                e.printStackTrace();
            }
        }
        this.mAudioEncoder.releaseOutputBuffer(bufferIndex, false);
        if ((this.mAudioBufferInfo.flags & 4) != 0) {
            return true;
        }
        return false;
    }

    boolean videoEncode(OnVideoSavedCallback videoSavedCallback, String cameraId, Size resolution) {
        boolean errorOccurred;
        boolean errorOccurred2 = false;
        boolean videoEos = false;
        while (!videoEos && !errorOccurred2) {
            if (this.mEndOfVideoStreamSignal.get()) {
                this.mVideoEncoder.signalEndOfInputStream();
                this.mEndOfVideoStreamSignal.set(false);
            }
            int outputBufferId = this.mVideoEncoder.dequeueOutputBuffer(this.mVideoBufferInfo, 10000);
            if (outputBufferId != -2) {
                videoEos = writeVideoEncodedBuffer(outputBufferId);
            } else {
                if (this.mMuxerStarted) {
                    videoSavedCallback.onError(1, "Unexpected change in video encoding format.", null);
                    errorOccurred = true;
                } else {
                    errorOccurred = errorOccurred2;
                }
                synchronized (this.mMuxerLock) {
                    this.mVideoTrackIndex = this.mMuxer.addTrack(this.mVideoEncoder.getOutputFormat());
                    if (this.mAudioTrackIndex >= 0 && this.mVideoTrackIndex >= 0) {
                        this.mMuxerStarted = true;
                        Log.i(TAG, "media mMuxer start");
                        this.mMuxer.start();
                    }
                }
                errorOccurred2 = errorOccurred;
            }
        }
        try {
            Log.i(TAG, "videoEncoder stop");
            this.mVideoEncoder.stop();
        } catch (IllegalStateException e) {
            videoSavedCallback.onError(1, "Video encoder stop failed!", e);
            errorOccurred2 = true;
        }
        try {
            synchronized (this.mMuxerLock) {
                if (this.mMuxer != null) {
                    if (this.mMuxerStarted) {
                        this.mMuxer.stop();
                    }
                    this.mMuxer.release();
                    this.mMuxer = null;
                }
            }
        } catch (IllegalStateException e2) {
            videoSavedCallback.onError(2, "Muxer stop failed!", e2);
            errorOccurred2 = true;
        }
        this.mMuxerStarted = false;
        setupEncoder(cameraId, resolution);
        notifyReset();
        this.mEndOfAudioVideoSignal.set(true);
        Log.i(TAG, "Video encode thread end.");
        return errorOccurred2;
    }

    boolean audioEncode(OnVideoSavedCallback videoSavedCallback) {
        boolean audioEos = false;
        while (true) {
            int i = 0;
            if (audioEos || !this.mIsRecording) {
                try {
                    Log.i(TAG, "audioRecorder stop");
                    this.mAudioRecorder.stop();
                } catch (IllegalStateException e) {
                    videoSavedCallback.onError(1, "Audio recorder stop failed!", e);
                }
                try {
                    this.mAudioEncoder.stop();
                } catch (IllegalStateException e2) {
                    videoSavedCallback.onError(1, "Audio encoder stop failed!", e2);
                }
                Log.i(TAG, "Audio encode thread end");
                this.mEndOfVideoStreamSignal.set(true);
                return false;
            }
            if (this.mEndOfAudioStreamSignal.get()) {
                this.mEndOfAudioStreamSignal.set(false);
                this.mIsRecording = false;
            }
            MediaCodec mediaCodec = this.mAudioEncoder;
            if (!(mediaCodec == null || this.mAudioRecorder == null)) {
                int index = mediaCodec.dequeueInputBuffer(-1);
                if (index >= 0) {
                    ByteBuffer buffer = getInputBuffer(this.mAudioEncoder, index);
                    buffer.clear();
                    int length = this.mAudioRecorder.read(buffer, this.mAudioBufferSize);
                    if (length > 0) {
                        MediaCodec mediaCodec2 = this.mAudioEncoder;
                        long nanoTime = System.nanoTime() / 1000;
                        if (!this.mIsRecording) {
                            i = 4;
                        }
                        mediaCodec2.queueInputBuffer(index, 0, length, nanoTime, i);
                    }
                }
                boolean audioEos2 = audioEos;
                do {
                    int outIndex = this.mAudioEncoder.dequeueOutputBuffer(this.mAudioBufferInfo, 0);
                    if (outIndex == -2) {
                        synchronized (this.mMuxerLock) {
                            this.mAudioTrackIndex = this.mMuxer.addTrack(this.mAudioEncoder.getOutputFormat());
                            if (this.mAudioTrackIndex >= 0 && this.mVideoTrackIndex >= 0) {
                                this.mMuxerStarted = true;
                                this.mMuxer.start();
                            }
                        }
                    } else if (outIndex != -1) {
                        audioEos2 = writeAudioEncodedBuffer(outIndex);
                    }
                    if (outIndex < 0) {
                        break;
                    }
                } while (!audioEos2);
                audioEos = audioEos2;
            }
        }
    }

    private ByteBuffer getInputBuffer(MediaCodec codec, int index) {
        return codec.getInputBuffer(index);
    }

    private ByteBuffer getOutputBuffer(MediaCodec codec, int index) {
        return codec.getOutputBuffer(index);
    }

    private MediaFormat createAudioMediaFormat() {
        MediaFormat format = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, this.mAudioSampleRate, this.mAudioChannelCount);
        format.setInteger("aac-profile", 2);
        format.setInteger("bitrate", this.mAudioBitRate);
        return format;
    }

    private AudioRecord autoConfigAudioRecordSource(VideoCaptureConfig config) {
        int channelConfig;
        int bufferSize;
        AudioRecord recorder;
        short[] sArr = sAudioEncoding;
        for (short audioFormat : sArr) {
            if (this.mAudioChannelCount == 1) {
                channelConfig = 16;
            } else {
                channelConfig = 12;
            }
            int source = config.getAudioRecordSource();
            try {
                bufferSize = AudioRecord.getMinBufferSize(this.mAudioSampleRate, channelConfig, audioFormat);
                if (bufferSize <= 0) {
                    bufferSize = config.getAudioMinBufferSize();
                }
                recorder = new AudioRecord(source, this.mAudioSampleRate, channelConfig, audioFormat, bufferSize * 2);
            } catch (Exception e) {
                Log.e(TAG, "Exception, keep trying.", e);
            }
            if (recorder.getState() == 1) {
                this.mAudioBufferSize = bufferSize;
                Log.i(TAG, "source: " + source + " audioSampleRate: " + this.mAudioSampleRate + " channelConfig: " + channelConfig + " audioFormat: " + ((int) audioFormat) + " bufferSize: " + bufferSize);
                return recorder;
            }
            continue;
        }
        return null;
    }

    private void setAudioParametersByCamcorderProfile(Size currentResolution, String cameraId) {
        boolean isCamcorderProfileFound = false;
        int[] iArr = CamcorderQuality;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            int quality = iArr[i];
            if (CamcorderProfile.hasProfile(Integer.parseInt(cameraId), quality)) {
                CamcorderProfile profile = CamcorderProfile.get(Integer.parseInt(cameraId), quality);
                if (currentResolution.getWidth() == profile.videoFrameWidth && currentResolution.getHeight() == profile.videoFrameHeight) {
                    this.mAudioChannelCount = profile.audioChannels;
                    this.mAudioSampleRate = profile.audioSampleRate;
                    this.mAudioBitRate = profile.audioBitRate;
                    isCamcorderProfileFound = true;
                    break;
                }
            }
            i++;
        }
        if (!isCamcorderProfileFound) {
            VideoCaptureConfig config = (VideoCaptureConfig) getUseCaseConfig();
            this.mAudioChannelCount = config.getAudioChannelCount();
            this.mAudioSampleRate = config.getAudioSampleRate();
            this.mAudioBitRate = config.getAudioBitRate();
        }
    }

    /* loaded from: classes.dex */
    public static final class Defaults implements ConfigProvider<VideoCaptureConfig> {
        private static final int DEFAULT_AUDIO_CHANNEL_COUNT;
        private static final int DEFAULT_AUDIO_MIN_BUFFER_SIZE;
        private static final int DEFAULT_AUDIO_RECORD_SOURCE;
        private static final int DEFAULT_BIT_RATE;
        private static final int DEFAULT_INTRA_FRAME_INTERVAL;
        private static final int DEFAULT_SURFACE_OCCUPANCY_PRIORITY;
        private static final int DEFAULT_VIDEO_FRAME_RATE;
        private static final Size DEFAULT_MAX_RESOLUTION = new Size(1920, 1080);
        private static final int DEFAULT_AUDIO_BIT_RATE;
        private static final int DEFAULT_AUDIO_SAMPLE_RATE;
        private static final VideoCaptureConfig DEFAULT_CONFIG = new Builder().setVideoFrameRate(30).setBitRate(8388608).setIFrameInterval(1).setAudioBitRate(DEFAULT_AUDIO_BIT_RATE).setAudioSampleRate(DEFAULT_AUDIO_SAMPLE_RATE).setAudioChannelCount(1).setAudioRecordSource(1).setAudioMinBufferSize(1024).setMaxResolution(DEFAULT_MAX_RESOLUTION).setSurfaceOccupancyPriority(3).getUseCaseConfig();

        @Override // androidx.camera.core.impl.ConfigProvider
        public VideoCaptureConfig getConfig(CameraInfo cameraInfo) {
            return DEFAULT_CONFIG;
        }
    }

    /* loaded from: classes.dex */
    public static final class VideoSavedListenerWrapper implements OnVideoSavedCallback {
        Executor mExecutor;
        OnVideoSavedCallback mOnVideoSavedCallback;

        VideoSavedListenerWrapper(Executor executor, OnVideoSavedCallback onVideoSavedCallback) {
            this.mExecutor = executor;
            this.mOnVideoSavedCallback = onVideoSavedCallback;
        }

        public /* synthetic */ void lambda$onVideoSaved$0$VideoCapture$VideoSavedListenerWrapper(File file) {
            this.mOnVideoSavedCallback.onVideoSaved(file);
        }

        @Override // androidx.camera.core.VideoCapture.OnVideoSavedCallback
        public void onVideoSaved(File file) {
            try {
                this.mExecutor.execute(new Runnable(file) { // from class: androidx.camera.core.-$$Lambda$VideoCapture$VideoSavedListenerWrapper$vLMoiAzzt8RX4-cghVgVbALA4Mc
                    private final /* synthetic */ File f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoCapture.VideoSavedListenerWrapper.this.lambda$onVideoSaved$0$VideoCapture$VideoSavedListenerWrapper(this.f$1);
                    }
                });
            } catch (RejectedExecutionException e) {
                Log.e(VideoCapture.TAG, "Unable to post to the supplied executor.");
            }
        }

        @Override // androidx.camera.core.VideoCapture.OnVideoSavedCallback
        public void onError(int videoCaptureError, String message, Throwable cause) {
            try {
                this.mExecutor.execute(new Runnable(videoCaptureError, message, cause) { // from class: androidx.camera.core.-$$Lambda$VideoCapture$VideoSavedListenerWrapper$ZG5otqrkESy2VwQvd4RLRJQ1fFY
                    private final /* synthetic */ int f$1;
                    private final /* synthetic */ String f$2;
                    private final /* synthetic */ Throwable f$3;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                        this.f$3 = r4;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoCapture.VideoSavedListenerWrapper.this.lambda$onError$1$VideoCapture$VideoSavedListenerWrapper(this.f$1, this.f$2, this.f$3);
                    }
                });
            } catch (RejectedExecutionException e) {
                Log.e(VideoCapture.TAG, "Unable to post to the supplied executor.");
            }
        }

        public /* synthetic */ void lambda$onError$1$VideoCapture$VideoSavedListenerWrapper(int videoCaptureError, String message, Throwable cause) {
            this.mOnVideoSavedCallback.onError(videoCaptureError, message, cause);
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder implements UseCaseConfig.Builder<VideoCapture, VideoCaptureConfig, Builder>, ImageOutputConfig.Builder<Builder>, ThreadConfig.Builder<Builder> {
        private final MutableOptionsBundle mMutableConfig;

        public Builder() {
            this(MutableOptionsBundle.create());
        }

        private Builder(MutableOptionsBundle mutableConfig) {
            this.mMutableConfig = mutableConfig;
            Class<?> oldConfigClass = (Class) mutableConfig.retrieveOption(TargetConfig.OPTION_TARGET_CLASS, null);
            if (oldConfigClass == null || oldConfigClass.equals(VideoCapture.class)) {
                setTargetClass(VideoCapture.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + oldConfigClass);
        }

        public static Builder fromConfig(VideoCaptureConfig configuration) {
            return new Builder(MutableOptionsBundle.from((Config) configuration));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public MutableConfig getMutableConfig() {
            return this.mMutableConfig;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public VideoCaptureConfig getUseCaseConfig() {
            return new VideoCaptureConfig(OptionsBundle.from(this.mMutableConfig));
        }

        @Override // androidx.camera.core.ExtendableBuilder
        public VideoCapture build() {
            if (getMutableConfig().retrieveOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO, null) == null || getMutableConfig().retrieveOption(ImageOutputConfig.OPTION_TARGET_RESOLUTION, null) == null) {
                return new VideoCapture(getUseCaseConfig());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        public Builder setVideoFrameRate(int videoFrameRate) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_VIDEO_FRAME_RATE, Integer.valueOf(videoFrameRate));
            return this;
        }

        public Builder setBitRate(int bitRate) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_BIT_RATE, Integer.valueOf(bitRate));
            return this;
        }

        public Builder setIFrameInterval(int interval) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_INTRA_FRAME_INTERVAL, Integer.valueOf(interval));
            return this;
        }

        public Builder setAudioBitRate(int bitRate) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_AUDIO_BIT_RATE, Integer.valueOf(bitRate));
            return this;
        }

        public Builder setAudioSampleRate(int sampleRate) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_AUDIO_SAMPLE_RATE, Integer.valueOf(sampleRate));
            return this;
        }

        public Builder setAudioChannelCount(int channelCount) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_AUDIO_CHANNEL_COUNT, Integer.valueOf(channelCount));
            return this;
        }

        public Builder setAudioRecordSource(int source) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_AUDIO_RECORD_SOURCE, Integer.valueOf(source));
            return this;
        }

        public Builder setAudioMinBufferSize(int minBufferSize) {
            getMutableConfig().insertOption(VideoCaptureConfig.OPTION_AUDIO_MIN_BUFFER_SIZE, Integer.valueOf(minBufferSize));
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetClass(Class<VideoCapture> targetClass) {
            getMutableConfig().insertOption(TargetConfig.OPTION_TARGET_CLASS, targetClass);
            if (getMutableConfig().retrieveOption(TargetConfig.OPTION_TARGET_NAME, null) == null) {
                setTargetName(targetClass.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        @Override // androidx.camera.core.internal.TargetConfig.Builder
        public Builder setTargetName(String targetName) {
            getMutableConfig().insertOption(TargetConfig.OPTION_TARGET_NAME, targetName);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatioCustom(Rational aspectRatio) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, aspectRatio);
            getMutableConfig().removeOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetAspectRatio(int aspectRatio) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO, Integer.valueOf(aspectRatio));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetRotation(int rotation) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ROTATION, Integer.valueOf(rotation));
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setTargetResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_RESOLUTION, resolution);
            if (resolution != null) {
                getMutableConfig().insertOption(ImageOutputConfig.OPTION_TARGET_ASPECT_RATIO_CUSTOM, new Rational(resolution.getWidth(), resolution.getHeight()));
            }
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setDefaultResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_DEFAULT_RESOLUTION, resolution);
            return null;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setMaxResolution(Size resolution) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_MAX_RESOLUTION, resolution);
            return this;
        }

        @Override // androidx.camera.core.impl.ImageOutputConfig.Builder
        public Builder setSupportedResolutions(List<Pair<Integer, Size[]>> resolutions) {
            getMutableConfig().insertOption(ImageOutputConfig.OPTION_SUPPORTED_RESOLUTIONS, resolutions);
            return this;
        }

        @Override // androidx.camera.core.internal.ThreadConfig.Builder
        public Builder setBackgroundExecutor(Executor executor) {
            getMutableConfig().insertOption(ThreadConfig.OPTION_BACKGROUND_EXECUTOR, executor);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultSessionConfig(SessionConfig sessionConfig) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_DEFAULT_SESSION_CONFIG, sessionConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setDefaultCaptureConfig(CaptureConfig captureConfig) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_DEFAULT_CAPTURE_CONFIG, captureConfig);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSessionOptionUnpacker(SessionConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_SESSION_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCaptureOptionUnpacker(CaptureConfig.OptionUnpacker optionUnpacker) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAPTURE_CONFIG_UNPACKER, optionUnpacker);
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setSurfaceOccupancyPriority(int priority) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_SURFACE_OCCUPANCY_PRIORITY, Integer.valueOf(priority));
            return this;
        }

        @Override // androidx.camera.core.impl.UseCaseConfig.Builder
        public Builder setCameraSelector(CameraSelector cameraSelector) {
            getMutableConfig().insertOption(UseCaseConfig.OPTION_CAMERA_SELECTOR, cameraSelector);
            return this;
        }

        @Override // androidx.camera.core.internal.UseCaseEventConfig.Builder
        public Builder setUseCaseEventCallback(UseCase.EventCallback useCaseEventCallback) {
            getMutableConfig().insertOption(UseCaseEventConfig.OPTION_USE_CASE_EVENT_CALLBACK, useCaseEventCallback);
            return this;
        }
    }
}
