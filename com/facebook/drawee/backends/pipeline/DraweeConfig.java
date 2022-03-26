package com.facebook.drawee.backends.pipeline;

import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.internal.Suppliers;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DraweeConfig {
    @Nullable
    private final ImmutableList<DrawableFactory> mCustomDrawableFactories;
    private final Supplier<Boolean> mDebugOverlayEnabledSupplier;
    @Nullable
    private final PipelineDraweeControllerFactory mPipelineDraweeControllerFactory;

    private DraweeConfig(Builder builder) {
        Supplier<Boolean> supplier;
        this.mCustomDrawableFactories = builder.mCustomDrawableFactories != null ? ImmutableList.copyOf(builder.mCustomDrawableFactories) : null;
        if (builder.mDebugOverlayEnabledSupplier != null) {
            supplier = builder.mDebugOverlayEnabledSupplier;
        } else {
            supplier = Suppliers.of(false);
        }
        this.mDebugOverlayEnabledSupplier = supplier;
        this.mPipelineDraweeControllerFactory = builder.mPipelineDraweeControllerFactory;
    }

    @Nullable
    public ImmutableList<DrawableFactory> getCustomDrawableFactories() {
        return this.mCustomDrawableFactories;
    }

    @Nullable
    public PipelineDraweeControllerFactory getPipelineDraweeControllerFactory() {
        return this.mPipelineDraweeControllerFactory;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Supplier<Boolean> getDebugOverlayEnabledSupplier() {
        return this.mDebugOverlayEnabledSupplier;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private List<DrawableFactory> mCustomDrawableFactories;
        private Supplier<Boolean> mDebugOverlayEnabledSupplier;
        private PipelineDraweeControllerFactory mPipelineDraweeControllerFactory;

        public Builder addCustomDrawableFactory(DrawableFactory factory) {
            if (this.mCustomDrawableFactories == null) {
                this.mCustomDrawableFactories = new ArrayList();
            }
            this.mCustomDrawableFactories.add(factory);
            return this;
        }

        public Builder setDrawDebugOverlay(boolean drawDebugOverlay) {
            return setDebugOverlayEnabledSupplier(Suppliers.of(Boolean.valueOf(drawDebugOverlay)));
        }

        public Builder setDebugOverlayEnabledSupplier(Supplier<Boolean> debugOverlayEnabledSupplier) {
            Preconditions.checkNotNull(debugOverlayEnabledSupplier);
            this.mDebugOverlayEnabledSupplier = debugOverlayEnabledSupplier;
            return this;
        }

        public Builder setPipelineDraweeControllerFactory(PipelineDraweeControllerFactory factory) {
            this.mPipelineDraweeControllerFactory = factory;
            return this;
        }

        public DraweeConfig build() {
            return new DraweeConfig(this);
        }
    }
}
