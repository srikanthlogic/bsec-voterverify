package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.NetworkInfo;
import android.os.Build;
import com.facebook.imagepipeline.common.RotationOptions;
import com.squareup.picasso.NetworkRequestHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
/* loaded from: classes3.dex */
public class BitmapHunter implements Runnable {
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifOrientation;
    Future<?> future;
    final String key;
    Picasso.LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    Picasso.Priority priority;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence = SEQUENCE_GENERATOR.incrementAndGet();
    final Stats stats;
    private static final Object DECODE_LOCK = new Object();
    private static final ThreadLocal<StringBuilder> NAME_BUILDER = new ThreadLocal<StringBuilder>() { // from class: com.squareup.picasso.BitmapHunter.1
        @Override // java.lang.ThreadLocal
        public StringBuilder initialValue() {
            return new StringBuilder("Picasso-");
        }
    };
    private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger();
    private static final RequestHandler ERRORING_HANDLER = new RequestHandler() { // from class: com.squareup.picasso.BitmapHunter.2
        @Override // com.squareup.picasso.RequestHandler
        public boolean canHandleRequest(Request data) {
            return true;
        }

        @Override // com.squareup.picasso.RequestHandler
        public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
            throw new IllegalStateException("Unrecognized type of request: " + request);
        }
    };

    BitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, RequestHandler requestHandler) {
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.action = action;
        this.key = action.getKey();
        this.data = action.getRequest();
        this.priority = action.getPriority();
        this.memoryPolicy = action.getMemoryPolicy();
        this.networkPolicy = action.getNetworkPolicy();
        this.requestHandler = requestHandler;
        this.retryCount = requestHandler.getRetryCount();
    }

    static Bitmap decodeStream(Source source, Request request) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(source);
        boolean isWebPFile = Utils.isWebPFile(bufferedSource);
        boolean isPurgeable = request.purgeable && Build.VERSION.SDK_INT < 21;
        BitmapFactory.Options options = RequestHandler.createBitmapOptions(request);
        boolean calculateSize = RequestHandler.requiresInSampleSize(options);
        if (isWebPFile || isPurgeable) {
            byte[] bytes = bufferedSource.readByteArray();
            if (calculateSize) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, options, request);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        InputStream stream = bufferedSource.inputStream();
        if (calculateSize) {
            MarkableInputStream markStream = new MarkableInputStream(stream);
            stream = markStream;
            markStream.allowMarksToExpire(false);
            long mark = markStream.savePosition(1024);
            BitmapFactory.decodeStream(stream, null, options);
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, options, request);
            markStream.reset(mark);
            markStream.allowMarksToExpire(true);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        if (bitmap != null) {
            return bitmap;
        }
        throw new IOException("Failed to decode stream.");
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                try {
                    updateThreadName(this.data);
                    if (this.picasso.loggingEnabled) {
                        Utils.log("Hunter", "executing", Utils.getLogIdsForHunter(this));
                    }
                    this.result = hunt();
                    if (this.result == null) {
                        this.dispatcher.dispatchFailed(this);
                    } else {
                        this.dispatcher.dispatchComplete(this);
                    }
                } catch (NetworkRequestHandler.ResponseException e) {
                    if (!NetworkPolicy.isOfflineOnly(e.networkPolicy) || e.code != 504) {
                        this.exception = e;
                    }
                    this.dispatcher.dispatchFailed(this);
                } catch (Exception e2) {
                    this.exception = e2;
                    this.dispatcher.dispatchFailed(this);
                }
            } catch (IOException e3) {
                this.exception = e3;
                this.dispatcher.dispatchRetry(this);
            } catch (OutOfMemoryError e4) {
                StringWriter writer = new StringWriter();
                this.stats.createSnapshot().dump(new PrintWriter(writer));
                this.exception = new RuntimeException(writer.toString(), e4);
                this.dispatcher.dispatchFailed(this);
            }
        } finally {
            Thread.currentThread().setName("Picasso-Idle");
        }
    }

    public Bitmap hunt() throws IOException {
        Bitmap bitmap = null;
        if (!MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy) || (bitmap = this.cache.get(this.key)) == null) {
            this.networkPolicy = this.retryCount == 0 ? NetworkPolicy.OFFLINE.index : this.networkPolicy;
            RequestHandler.Result result = this.requestHandler.load(this.data, this.networkPolicy);
            if (result != null) {
                this.loadedFrom = result.getLoadedFrom();
                this.exifOrientation = result.getExifOrientation();
                bitmap = result.getBitmap();
                if (bitmap == null) {
                    Source source = result.getSource();
                    try {
                        bitmap = decodeStream(source, this.data);
                    } finally {
                        try {
                            source.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            if (bitmap != null) {
                if (this.picasso.loggingEnabled) {
                    Utils.log("Hunter", "decoded", this.data.logId());
                }
                this.stats.dispatchBitmapDecoded(bitmap);
                if (this.data.needsTransformation() || this.exifOrientation != 0) {
                    synchronized (DECODE_LOCK) {
                        if (this.data.needsMatrixTransform() || this.exifOrientation != 0) {
                            bitmap = transformResult(this.data, bitmap, this.exifOrientation);
                            if (this.picasso.loggingEnabled) {
                                Utils.log("Hunter", "transformed", this.data.logId());
                            }
                        }
                        if (this.data.hasCustomTransformations()) {
                            bitmap = applyCustomTransformations(this.data.transformations, bitmap);
                            if (this.picasso.loggingEnabled) {
                                Utils.log("Hunter", "transformed", this.data.logId(), "from custom transformations");
                            }
                        }
                    }
                    if (bitmap != null) {
                        this.stats.dispatchBitmapTransformed(bitmap);
                    }
                }
            }
            return bitmap;
        }
        this.stats.dispatchCacheHit();
        this.loadedFrom = Picasso.LoadedFrom.MEMORY;
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "decoded", this.data.logId(), "from cache");
        }
        return bitmap;
    }

    public void attach(Action action) {
        boolean loggingEnabled = this.picasso.loggingEnabled;
        Request request = action.request;
        if (this.action == null) {
            this.action = action;
            if (loggingEnabled) {
                List<Action> list = this.actions;
                if (list == null || list.isEmpty()) {
                    Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
                } else {
                    Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
                }
            }
        } else {
            if (this.actions == null) {
                this.actions = new ArrayList(3);
            }
            this.actions.add(action);
            if (loggingEnabled) {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
            }
            Picasso.Priority actionPriority = action.getPriority();
            if (actionPriority.ordinal() > this.priority.ordinal()) {
                this.priority = actionPriority;
            }
        }
    }

    public void detach(Action action) {
        boolean detached = false;
        if (this.action == action) {
            this.action = null;
            detached = true;
        } else {
            List<Action> list = this.actions;
            if (list != null) {
                detached = list.remove(action);
            }
        }
        if (detached && action.getPriority() == this.priority) {
            this.priority = computeNewPriority();
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    private Picasso.Priority computeNewPriority() {
        Picasso.Priority newPriority = Picasso.Priority.LOW;
        List<Action> list = this.actions;
        boolean hasAny = false;
        boolean hasMultiple = list != null && !list.isEmpty();
        if (this.action != null || hasMultiple) {
            hasAny = true;
        }
        if (!hasAny) {
            return newPriority;
        }
        Action action = this.action;
        if (action != null) {
            newPriority = action.getPriority();
        }
        if (hasMultiple) {
            int n = this.actions.size();
            for (int i = 0; i < n; i++) {
                Picasso.Priority actionPriority = this.actions.get(i).getPriority();
                if (actionPriority.ordinal() > newPriority.ordinal()) {
                    newPriority = actionPriority;
                }
            }
        }
        return newPriority;
    }

    public boolean cancel() {
        Future<?> future;
        if (this.action != null) {
            return false;
        }
        List<Action> list = this.actions;
        if ((list == null || list.isEmpty()) && (future = this.future) != null && future.cancel(false)) {
            return true;
        }
        return false;
    }

    public boolean isCancelled() {
        Future<?> future = this.future;
        return future != null && future.isCancelled();
    }

    public boolean shouldRetry(boolean airplaneMode, NetworkInfo info) {
        if (!(this.retryCount > 0)) {
            return false;
        }
        this.retryCount--;
        return this.requestHandler.shouldRetry(airplaneMode, info);
    }

    public boolean supportsReplay() {
        return this.requestHandler.supportsReplay();
    }

    public Bitmap getResult() {
        return this.result;
    }

    public String getKey() {
        return this.key;
    }

    public int getMemoryPolicy() {
        return this.memoryPolicy;
    }

    public Request getData() {
        return this.data;
    }

    public Action getAction() {
        return this.action;
    }

    public Picasso getPicasso() {
        return this.picasso;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public Exception getException() {
        return this.exception;
    }

    public Picasso.LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }

    public Picasso.Priority getPriority() {
        return this.priority;
    }

    static void updateThreadName(Request data) {
        String name = data.getName();
        StringBuilder builder = NAME_BUILDER.get();
        builder.ensureCapacity("Picasso-".length() + name.length());
        builder.replace("Picasso-".length(), builder.length(), name);
        Thread.currentThread().setName(builder.toString());
    }

    public static BitmapHunter forRequest(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        Request request = action.getRequest();
        List<RequestHandler> requestHandlers = picasso.getRequestHandlers();
        int count = requestHandlers.size();
        for (int i = 0; i < count; i++) {
            RequestHandler requestHandler = requestHandlers.get(i);
            if (requestHandler.canHandleRequest(request)) {
                return new BitmapHunter(picasso, dispatcher, cache, stats, action, requestHandler);
            }
        }
        return new BitmapHunter(picasso, dispatcher, cache, stats, action, ERRORING_HANDLER);
    }

    static Bitmap applyCustomTransformations(List<Transformation> transformations, Bitmap result) {
        int count = transformations.size();
        for (int i = 0; i < count; i++) {
            final Transformation transformation = transformations.get(i);
            try {
                Bitmap newResult = transformation.transform(result);
                if (newResult == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Transformation ");
                    sb.append(transformation.key());
                    sb.append(" returned null after ");
                    sb.append(i);
                    final StringBuilder builder = sb.append(" previous transformation(s).\n\nTransformation list:\n");
                    for (Transformation t : transformations) {
                        builder.append(t.key());
                        builder.append('\n');
                    }
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.4
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new NullPointerException(builder.toString());
                        }
                    });
                    return null;
                } else if (newResult == result && result.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.5
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + transformation.key() + " returned input Bitmap but recycled it.");
                        }
                    });
                    return null;
                } else if (newResult == result || result.isRecycled()) {
                    result = newResult;
                } else {
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.6
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + transformation.key() + " mutated input Bitmap but failed to recycle the original.");
                        }
                    });
                    return null;
                }
            } catch (RuntimeException e) {
                Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.3
                    @Override // java.lang.Runnable
                    public void run() {
                        throw new RuntimeException("Transformation " + transformation.key() + " crashed with exception.", e);
                    }
                });
                return null;
            }
        }
        return result;
    }

    /* JADX INFO: Multiple debug info for r10v15 int: [D('y4T' double), D('targetHeight' int)] */
    static Bitmap transformResult(Request data, Bitmap result, int exifOrientation) {
        int drawHeight;
        int drawWidth;
        int drawY;
        int drawX;
        Matrix matrix;
        Matrix matrix2;
        int inWidth;
        int inHeight;
        boolean onlyScaleDown;
        int targetWidth;
        int targetHeight;
        int exifRotation;
        int targetHeight2;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        int inHeight2;
        int inWidth2;
        float widthRatio;
        float f9;
        float f10;
        float scaleY;
        float scaleX;
        int drawX2;
        int drawY2;
        int inWidth3 = result.getWidth();
        int inHeight3 = result.getHeight();
        boolean onlyScaleDown2 = data.onlyScaleDown;
        Matrix matrix3 = new Matrix();
        if (data.needsMatrixTransform() || exifOrientation != 0) {
            int targetWidth2 = data.targetWidth;
            int targetHeight3 = data.targetHeight;
            float targetRotation = data.rotationDegrees;
            if (targetRotation != 0.0f) {
                double cosR = Math.cos(Math.toRadians((double) targetRotation));
                double sinR = Math.sin(Math.toRadians((double) targetRotation));
                drawX = 0;
                if (data.hasRotationPivot) {
                    drawY = 0;
                    matrix3.setRotate(targetRotation, data.rotationPivotX, data.rotationPivotY);
                    drawWidth = inWidth3;
                    drawHeight = inHeight3;
                    double x1T = (((double) data.rotationPivotX) * (1.0d - cosR)) + (((double) data.rotationPivotY) * sinR);
                    double y1T = (((double) data.rotationPivotY) * (1.0d - cosR)) - (((double) data.rotationPivotX) * sinR);
                    double x2T = (((double) data.targetWidth) * cosR) + x1T;
                    onlyScaleDown = onlyScaleDown2;
                    inHeight = inHeight3;
                    double y2T = (((double) data.targetWidth) * sinR) + y1T;
                    inWidth = inWidth3;
                    matrix2 = matrix3;
                    double x3T = ((((double) data.targetWidth) * cosR) + x1T) - (((double) data.targetHeight) * sinR);
                    double y3T = (((double) data.targetWidth) * sinR) + y1T + (((double) data.targetHeight) * cosR);
                    double x4T = x1T - (((double) data.targetHeight) * sinR);
                    double y4T = (((double) data.targetHeight) * cosR) + y1T;
                    double maxX = Math.max(x4T, Math.max(x3T, Math.max(x1T, x2T)));
                    double minX = Math.min(x4T, Math.min(x3T, Math.min(x1T, x2T)));
                    double maxY = Math.max(y4T, Math.max(y3T, Math.max(y1T, y2T)));
                    double minY = Math.min(y4T, Math.min(y3T, Math.min(y1T, y2T)));
                    targetWidth = (int) Math.floor(maxX - minX);
                    targetHeight = (int) Math.floor(maxY - minY);
                } else {
                    inWidth = inWidth3;
                    inHeight = inHeight3;
                    onlyScaleDown = onlyScaleDown2;
                    drawY = 0;
                    drawWidth = inWidth3;
                    drawHeight = inHeight3;
                    matrix2 = matrix3;
                    matrix2.setRotate(targetRotation);
                    double x2T2 = ((double) data.targetWidth) * cosR;
                    double y2T2 = ((double) data.targetWidth) * sinR;
                    double x3T2 = (((double) data.targetWidth) * cosR) - (((double) data.targetHeight) * sinR);
                    double y3T2 = (((double) data.targetWidth) * sinR) + (((double) data.targetHeight) * cosR);
                    double x4T2 = -(((double) data.targetHeight) * sinR);
                    double y4T2 = ((double) data.targetHeight) * cosR;
                    double maxX2 = Math.max(x4T2, Math.max(x3T2, Math.max(0.0d, x2T2)));
                    double minX2 = Math.min(x4T2, Math.min(x3T2, Math.min(0.0d, x2T2)));
                    targetHeight = (int) Math.floor(Math.max(y4T2, Math.max(y3T2, Math.max(0.0d, y2T2))) - Math.min(y4T2, Math.min(y3T2, Math.min(0.0d, y2T2))));
                    targetWidth = (int) Math.floor(maxX2 - minX2);
                }
            } else {
                inWidth = inWidth3;
                inHeight = inHeight3;
                onlyScaleDown = onlyScaleDown2;
                drawX = 0;
                drawY = 0;
                drawWidth = inWidth3;
                drawHeight = inHeight3;
                matrix2 = matrix3;
                targetWidth = targetWidth2;
                targetHeight = targetHeight3;
            }
            if (exifOrientation != 0) {
                int exifRotation2 = getExifRotation(exifOrientation);
                int exifTranslation = getExifTranslation(exifOrientation);
                if (exifRotation2 != 0) {
                    matrix = matrix2;
                    matrix.preRotate((float) exifRotation2);
                    if (exifRotation2 == 90 || exifRotation2 == 270) {
                        targetHeight = targetWidth;
                        targetWidth = targetHeight;
                    }
                } else {
                    matrix = matrix2;
                }
                if (exifTranslation != 1) {
                    matrix.postScale((float) exifTranslation, 1.0f);
                }
                exifRotation = targetHeight;
                targetHeight2 = targetWidth;
            } else {
                matrix = matrix2;
                exifRotation = targetHeight;
                targetHeight2 = targetWidth;
            }
            if (data.centerCrop) {
                if (targetHeight2 != 0) {
                    inWidth2 = inWidth;
                    widthRatio = ((float) targetHeight2) / ((float) inWidth2);
                    inHeight2 = inHeight;
                } else {
                    inWidth2 = inWidth;
                    inHeight2 = inHeight;
                    widthRatio = ((float) exifRotation) / ((float) inHeight2);
                }
                if (exifRotation != 0) {
                    f10 = (float) exifRotation;
                    f9 = (float) inHeight2;
                } else {
                    f10 = (float) targetHeight2;
                    f9 = (float) inWidth2;
                }
                float heightRatio = f10 / f9;
                if (widthRatio > heightRatio) {
                    int newSize = (int) Math.ceil((double) (((float) inHeight2) * (heightRatio / widthRatio)));
                    if ((data.centerCropGravity & 48) == 48) {
                        drawY2 = 0;
                    } else if ((data.centerCropGravity & 80) == 80) {
                        drawY2 = inHeight2 - newSize;
                    } else {
                        drawY2 = (inHeight2 - newSize) / 2;
                    }
                    scaleX = widthRatio;
                    scaleY = ((float) exifRotation) / ((float) newSize);
                    drawY = drawY2;
                    drawHeight = newSize;
                } else if (widthRatio < heightRatio) {
                    int newSize2 = (int) Math.ceil((double) (((float) inWidth2) * (widthRatio / heightRatio)));
                    if ((data.centerCropGravity & 3) == 3) {
                        drawX2 = 0;
                    } else if ((data.centerCropGravity & 5) == 5) {
                        drawX2 = inWidth2 - newSize2;
                    } else {
                        drawX2 = (inWidth2 - newSize2) / 2;
                    }
                    scaleX = ((float) targetHeight2) / ((float) newSize2);
                    scaleY = heightRatio;
                    drawX = drawX2;
                    drawWidth = newSize2;
                } else {
                    scaleY = heightRatio;
                    scaleX = heightRatio;
                    drawX = 0;
                    drawWidth = inWidth2;
                }
                if (shouldResize(onlyScaleDown, inWidth2, inHeight2, targetHeight2, exifRotation)) {
                    matrix.preScale(scaleX, scaleY);
                }
            } else if (data.centerInside) {
                if (targetHeight2 != 0) {
                    f6 = (float) targetHeight2;
                    f5 = (float) inWidth;
                } else {
                    f6 = (float) exifRotation;
                    f5 = (float) inHeight;
                }
                float widthRatio2 = f6 / f5;
                if (exifRotation != 0) {
                    f8 = (float) exifRotation;
                    f7 = (float) inHeight;
                } else {
                    f8 = (float) targetHeight2;
                    f7 = (float) inWidth;
                }
                float heightRatio2 = f8 / f7;
                float scale = widthRatio2 < heightRatio2 ? widthRatio2 : heightRatio2;
                if (shouldResize(onlyScaleDown, inWidth, inHeight, targetHeight2, exifRotation)) {
                    matrix.preScale(scale, scale);
                }
            } else if (!((targetHeight2 == 0 && exifRotation == 0) || (targetHeight2 == inWidth && exifRotation == inHeight))) {
                if (targetHeight2 != 0) {
                    f2 = (float) targetHeight2;
                    f = (float) inWidth;
                } else {
                    f2 = (float) exifRotation;
                    f = (float) inHeight;
                }
                float sx = f2 / f;
                if (exifRotation != 0) {
                    f4 = (float) exifRotation;
                    f3 = (float) inHeight;
                } else {
                    f4 = (float) targetHeight2;
                    f3 = (float) inWidth;
                }
                float sy = f4 / f3;
                if (shouldResize(onlyScaleDown, inWidth, inHeight, targetHeight2, exifRotation)) {
                    matrix.preScale(sx, sy);
                }
            }
        } else {
            drawX = 0;
            drawY = 0;
            drawWidth = inWidth3;
            drawHeight = inHeight3;
            matrix = matrix3;
        }
        Bitmap newResult = Bitmap.createBitmap(result, drawX, drawY, drawWidth, drawHeight, matrix, true);
        if (newResult == result) {
            return result;
        }
        result.recycle();
        return newResult;
    }

    private static boolean shouldResize(boolean onlyScaleDown, int inWidth, int inHeight, int targetWidth, int targetHeight) {
        return !onlyScaleDown || (targetWidth != 0 && inWidth > targetWidth) || (targetHeight != 0 && inHeight > targetHeight);
    }

    static int getExifRotation(int orientation) {
        switch (orientation) {
            case 3:
            case 4:
                return RotationOptions.ROTATE_180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    static int getExifTranslation(int orientation) {
        if (orientation == 2 || orientation == 7 || orientation == 4 || orientation == 5) {
            return -1;
        }
        return 1;
    }
}
