package androidx.camera.core;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageSaver;
import androidx.camera.core.impl.utils.Exif;
import androidx.camera.core.internal.utils.ImageUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ImageSaver implements Runnable {
    private static final int COPY_BUFFER_SIZE;
    private static final int NOT_PENDING;
    private static final int PENDING;
    private static final String TAG;
    private static final String TEMP_FILE_PREFIX;
    private static final String TEMP_FILE_SUFFIX;
    final OnImageSavedCallback mCallback;
    private final Executor mExecutor;
    private final ImageProxy mImage;
    private final int mOrientation;
    private final ImageCapture.OutputFileOptions mOutputFileOptions;

    /* loaded from: classes.dex */
    public interface OnImageSavedCallback {
        void onError(SaveError saveError, String str, Throwable th);

        void onImageSaved(ImageCapture.OutputFileResults outputFileResults);
    }

    /* loaded from: classes.dex */
    public enum SaveError {
        FILE_IO_FAILED,
        ENCODE_FAILED,
        CROP_FAILED,
        UNKNOWN
    }

    public ImageSaver(ImageProxy image, ImageCapture.OutputFileOptions outputFileOptions, int orientation, Executor executor, OnImageSavedCallback callback) {
        this.mImage = image;
        this.mOutputFileOptions = outputFileOptions;
        this.mOrientation = orientation;
        this.mCallback = callback;
        this.mExecutor = executor;
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x014d, code lost:
        if (isSaveToFile() != false) goto L_0x0161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x015e, code lost:
        if (isSaveToFile() == false) goto L_0x0104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0161, code lost:
        if (r0 == null) goto L_0x0167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0163, code lost:
        postError(r0, r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0167, code lost:
        postSuccess(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x016a, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:?, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fb A[Catch: IOException -> 0x0152, IllegalArgumentException -> 0x0150, CodecFailedException -> 0x0121, all -> 0x011f, TRY_ENTER, TRY_LEAVE, TryCatch #7 {CodecFailedException -> 0x0121, IOException -> 0x0152, IllegalArgumentException -> 0x0150, blocks: (B:8:0x001c, B:42:0x00fb, B:50:0x011b, B:51:0x011e), top: B:85:0x001c, outer: #8 }] */
    @Override // java.lang.Runnable
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void run() {
        File file;
        Exception e;
        ContentValues values;
        SaveError saveError = null;
        String errorMessage = null;
        Exception exception = null;
        Uri outputUri = null;
        try {
            if (isSaveToFile()) {
                file = this.mOutputFileOptions.getFile();
            } else {
                file = File.createTempFile(TEMP_FILE_PREFIX, ".tmp");
            }
            try {
                try {
                    ImageProxy imageToClose = this.mImage;
                    try {
                        FileOutputStream output = new FileOutputStream(file);
                        output.write(ImageUtil.imageToJpegByteArray(this.mImage));
                        Exif exif = Exif.createFromFile(file);
                        exif.attachTimestamp();
                        if (this.mImage.getFormat() == 256) {
                            ByteBuffer buffer = this.mImage.getPlanes()[0].getBuffer();
                            buffer.rewind();
                            byte[] data = new byte[buffer.capacity()];
                            buffer.get(data);
                            exif.setOrientation(Exif.createFromInputStream(new ByteArrayInputStream(data)).getOrientation());
                        } else {
                            exif.rotate(this.mOrientation);
                        }
                        ImageCapture.Metadata metadata = this.mOutputFileOptions.getMetadata();
                        if (metadata.isReversedHorizontal()) {
                            exif.flipHorizontally();
                        }
                        if (metadata.isReversedVertical()) {
                            exif.flipVertically();
                        }
                        if (metadata.getLocation() != null) {
                            exif.attachLocation(this.mOutputFileOptions.getMetadata().getLocation());
                        }
                        exif.save();
                        if (isSaveToMediaStore()) {
                            if (this.mOutputFileOptions.getContentValues() != null) {
                                values = new ContentValues(this.mOutputFileOptions.getContentValues());
                            } else {
                                values = new ContentValues();
                            }
                            setContentValuePending(values, 1);
                            outputUri = this.mOutputFileOptions.getContentResolver().insert(this.mOutputFileOptions.getSaveCollection(), values);
                            if (outputUri == null) {
                                saveError = SaveError.FILE_IO_FAILED;
                                errorMessage = "Failed to insert URI.";
                            } else {
                                if (!copyTempFileToUri(file, outputUri)) {
                                    saveError = SaveError.FILE_IO_FAILED;
                                    errorMessage = "Failed to save to URI.";
                                }
                                setUriNotPending(outputUri);
                            }
                        } else if (isSaveToOutputStream()) {
                            copyTempFileToOutputStream(file, this.mOutputFileOptions.getOutputStream());
                            output.close();
                            if (imageToClose != null) {
                                imageToClose.close();
                            }
                        }
                        output.close();
                        if (imageToClose != null) {
                        }
                    } catch (Throwable th) {
                        if (imageToClose != null) {
                            try {
                                imageToClose.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (ImageUtil.CodecFailedException e2) {
                    int i = AnonymousClass1.$SwitchMap$androidx$camera$core$internal$utils$ImageUtil$CodecFailedException$FailureType[e2.getFailureType().ordinal()];
                    if (i == 1) {
                        saveError = SaveError.ENCODE_FAILED;
                        errorMessage = "Failed to encode mImage";
                    } else if (i != 2) {
                        saveError = SaveError.UNKNOWN;
                        errorMessage = "Failed to transcode mImage";
                    } else {
                        saveError = SaveError.CROP_FAILED;
                        errorMessage = "Failed to crop mImage";
                    }
                    exception = e2;
                } catch (IOException e3) {
                    e = e3;
                    saveError = SaveError.FILE_IO_FAILED;
                    errorMessage = "Failed to write or close the file";
                    exception = e;
                } catch (IllegalArgumentException e4) {
                    e = e4;
                    saveError = SaveError.FILE_IO_FAILED;
                    errorMessage = "Failed to write or close the file";
                    exception = e;
                }
            } finally {
                if (!isSaveToFile()) {
                    file.delete();
                }
            }
        } catch (IOException e5) {
            postError(SaveError.FILE_IO_FAILED, "Failed to create temp file", e5);
        }
    }

    /* renamed from: androidx.camera.core.ImageSaver$1 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$core$internal$utils$ImageUtil$CodecFailedException$FailureType = new int[ImageUtil.CodecFailedException.FailureType.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$core$internal$utils$ImageUtil$CodecFailedException$FailureType[ImageUtil.CodecFailedException.FailureType.ENCODE_FAILED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$camera$core$internal$utils$ImageUtil$CodecFailedException$FailureType[ImageUtil.CodecFailedException.FailureType.DECODE_FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$camera$core$internal$utils$ImageUtil$CodecFailedException$FailureType[ImageUtil.CodecFailedException.FailureType.UNKNOWN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private boolean isSaveToMediaStore() {
        return (this.mOutputFileOptions.getSaveCollection() == null || this.mOutputFileOptions.getContentResolver() == null || this.mOutputFileOptions.getContentValues() == null) ? false : true;
    }

    private boolean isSaveToFile() {
        return this.mOutputFileOptions.getFile() != null;
    }

    private boolean isSaveToOutputStream() {
        return this.mOutputFileOptions.getOutputStream() != null;
    }

    private void setUriNotPending(Uri outputUri) {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues values = new ContentValues();
            setContentValuePending(values, 0);
            this.mOutputFileOptions.getContentResolver().update(outputUri, values, null, null);
        }
    }

    private void setContentValuePending(ContentValues values, int isPending) {
        if (Build.VERSION.SDK_INT >= 29) {
            values.put("is_pending", Integer.valueOf(isPending));
        }
    }

    private boolean copyTempFileToUri(File tempFile, Uri uri) throws IOException {
        OutputStream outputStream = this.mOutputFileOptions.getContentResolver().openOutputStream(uri);
        if (outputStream == null) {
            if (outputStream != null) {
                outputStream.close();
            }
            return false;
        }
        try {
            copyTempFileToOutputStream(tempFile, outputStream);
            if (outputStream == null) {
                return true;
            }
            outputStream.close();
            return true;
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void copyTempFileToOutputStream(File tempFile, OutputStream outputStream) throws IOException {
        InputStream in = new FileInputStream(tempFile);
        try {
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    outputStream.write(buf, 0, len);
                } else {
                    in.close();
                    return;
                }
            }
        } catch (Throwable th) {
            try {
                in.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void postSuccess(Uri outputUri) {
        try {
            this.mExecutor.execute(new Runnable(outputUri) { // from class: androidx.camera.core.-$$Lambda$ImageSaver$S9mrYGMPcUwPIjUa0oK9HMzbx_o
                private final /* synthetic */ Uri f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ImageSaver.this.lambda$postSuccess$0$ImageSaver(this.f$1);
                }
            });
        } catch (RejectedExecutionException e) {
            Log.e(TAG, "Application executor rejected executing OnImageSavedCallback.onImageSaved callback. Skipping.");
        }
    }

    public /* synthetic */ void lambda$postSuccess$0$ImageSaver(Uri outputUri) {
        this.mCallback.onImageSaved(new ImageCapture.OutputFileResults(outputUri));
    }

    private void postError(SaveError saveError, String message, Throwable cause) {
        try {
            this.mExecutor.execute(new Runnable(saveError, message, cause) { // from class: androidx.camera.core.-$$Lambda$ImageSaver$eAp-cZyzsEk-LVLazzLE-ezQzwo
                private final /* synthetic */ ImageSaver.SaveError f$1;
                private final /* synthetic */ String f$2;
                private final /* synthetic */ Throwable f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ImageSaver.this.lambda$postError$1$ImageSaver(this.f$1, this.f$2, this.f$3);
                }
            });
        } catch (RejectedExecutionException e) {
            Log.e(TAG, "Application executor rejected executing OnImageSavedCallback.onError callback. Skipping.");
        }
    }

    public /* synthetic */ void lambda$postError$1$ImageSaver(SaveError saveError, String message, Throwable cause) {
        this.mCallback.onError(saveError, message, cause);
    }
}
