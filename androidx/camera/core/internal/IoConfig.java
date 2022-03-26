package androidx.camera.core.internal;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.ReadableConfig;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface IoConfig extends ReadableConfig {
    public static final Config.Option<Executor> OPTION_IO_EXECUTOR = Config.Option.create("camerax.core.io.ioExecutor", Executor.class);

    /* loaded from: classes.dex */
    public interface Builder<B> {
        B setIoExecutor(Executor executor);
    }

    default Executor getIoExecutor(Executor valueIfMissing) {
        return (Executor) retrieveOption(OPTION_IO_EXECUTOR, valueIfMissing);
    }

    default Executor getIoExecutor() {
        return (Executor) retrieveOption(OPTION_IO_EXECUTOR);
    }
}
