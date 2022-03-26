package androidx.camera.core.internal;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ReadableConfig;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface ThreadConfig extends ReadableConfig {
    public static final Config.Option<Executor> OPTION_BACKGROUND_EXECUTOR = Config.Option.create("camerax.core.thread.backgroundExecutor", Executor.class);

    /* loaded from: classes.dex */
    public interface Builder<B> {
        B setBackgroundExecutor(Executor executor);
    }

    default Executor getBackgroundExecutor(Executor valueIfMissing) {
        return (Executor) retrieveOption(OPTION_BACKGROUND_EXECUTOR, valueIfMissing);
    }

    default Executor getBackgroundExecutor() {
        return (Executor) retrieveOption(OPTION_BACKGROUND_EXECUTOR);
    }
}
