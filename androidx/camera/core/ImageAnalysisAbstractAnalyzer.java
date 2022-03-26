package androidx.camera.core;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.impl.ImageReaderProxy;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.os.OperationCanceledException;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ImageAnalysisAbstractAnalyzer implements ImageReaderProxy.OnImageAvailableListener {
    private final Object mAnalyzerLock = new Object();
    private AtomicBoolean mIsClosed = new AtomicBoolean(false);
    private volatile int mRelativeRotation;
    private ImageAnalysis.Analyzer mSubscribedAnalyzer;
    private Executor mUserExecutor;

    ListenableFuture<Void> analyzeImage(ImageProxy imageProxy) {
        Executor executor;
        ImageAnalysis.Analyzer analyzer;
        synchronized (this.mAnalyzerLock) {
            executor = this.mUserExecutor;
            analyzer = this.mSubscribedAnalyzer;
        }
        if (analyzer == null || executor == null) {
            return Futures.immediateFailedFuture(new OperationCanceledException("No analyzer or executor currently set."));
        }
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(executor, imageProxy, analyzer) { // from class: androidx.camera.core.-$$Lambda$ImageAnalysisAbstractAnalyzer$mR2LN44DwXC209Sm8-KLIHvFCIo
            private final /* synthetic */ Executor f$1;
            private final /* synthetic */ ImageProxy f$2;
            private final /* synthetic */ ImageAnalysis.Analyzer f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ImageAnalysisAbstractAnalyzer.this.lambda$analyzeImage$1$ImageAnalysisAbstractAnalyzer(this.f$1, this.f$2, this.f$3, completer);
            }
        });
    }

    public /* synthetic */ Object lambda$analyzeImage$1$ImageAnalysisAbstractAnalyzer(Executor executor, ImageProxy imageProxy, ImageAnalysis.Analyzer analyzer, CallbackToFutureAdapter.Completer completer) throws Exception {
        executor.execute(new Runnable(imageProxy, analyzer, completer) { // from class: androidx.camera.core.-$$Lambda$ImageAnalysisAbstractAnalyzer$V6Gtux2-h8spda8vf4wFnIZntYI
            private final /* synthetic */ ImageProxy f$1;
            private final /* synthetic */ ImageAnalysis.Analyzer f$2;
            private final /* synthetic */ CallbackToFutureAdapter.Completer f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ImageAnalysisAbstractAnalyzer.this.lambda$analyzeImage$0$ImageAnalysisAbstractAnalyzer(this.f$1, this.f$2, this.f$3);
            }
        });
        return "analyzeImage";
    }

    public /* synthetic */ void lambda$analyzeImage$0$ImageAnalysisAbstractAnalyzer(ImageProxy imageProxy, ImageAnalysis.Analyzer analyzer, CallbackToFutureAdapter.Completer completer) {
        if (!isClosed()) {
            analyzer.analyze(new SettableImageProxy(imageProxy, ImmutableImageInfo.create(imageProxy.getImageInfo().getTag(), imageProxy.getImageInfo().getTimestamp(), this.mRelativeRotation)));
            completer.set(null);
            return;
        }
        completer.setException(new OperationCanceledException("Closed before analysis"));
    }

    public void setRelativeRotation(int relativeRotation) {
        this.mRelativeRotation = relativeRotation;
    }

    public void setAnalyzer(Executor userExecutor, ImageAnalysis.Analyzer subscribedAnalyzer) {
        synchronized (this.mAnalyzerLock) {
            this.mSubscribedAnalyzer = subscribedAnalyzer;
            this.mUserExecutor = userExecutor;
        }
    }

    public void open() {
        this.mIsClosed.set(false);
    }

    public void close() {
        this.mIsClosed.set(true);
    }

    boolean isClosed() {
        return this.mIsClosed.get();
    }
}
