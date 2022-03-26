package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class CaptureConfig {
    final List<CameraCaptureCallback> mCameraCaptureCallbacks;
    final Config mImplementationOptions;
    final List<DeferrableSurface> mSurfaces;
    private final Object mTag;
    final int mTemplateType;
    private final boolean mUseRepeatingSurface;
    public static final Config.Option<Integer> OPTION_ROTATION = Config.Option.create("camerax.core.captureConfig.rotation", Integer.TYPE);
    public static final Config.Option<Integer> OPTION_JPEG_QUALITY = Config.Option.create("camerax.core.captureConfig.jpegQuality", Integer.class);

    /* loaded from: classes.dex */
    public interface OptionUnpacker {
        void unpack(UseCaseConfig<?> useCaseConfig, Builder builder);
    }

    CaptureConfig(List<DeferrableSurface> surfaces, Config implementationOptions, int templateType, List<CameraCaptureCallback> cameraCaptureCallbacks, boolean useRepeatingSurface, Object tag) {
        this.mSurfaces = surfaces;
        this.mImplementationOptions = implementationOptions;
        this.mTemplateType = templateType;
        this.mCameraCaptureCallbacks = Collections.unmodifiableList(cameraCaptureCallbacks);
        this.mUseRepeatingSurface = useRepeatingSurface;
        this.mTag = tag;
    }

    public static CaptureConfig defaultEmptyCaptureConfig() {
        return new Builder().build();
    }

    public List<DeferrableSurface> getSurfaces() {
        return Collections.unmodifiableList(this.mSurfaces);
    }

    public Config getImplementationOptions() {
        return this.mImplementationOptions;
    }

    public int getTemplateType() {
        return this.mTemplateType;
    }

    public boolean isUseRepeatingSurface() {
        return this.mUseRepeatingSurface;
    }

    public List<CameraCaptureCallback> getCameraCaptureCallbacks() {
        return this.mCameraCaptureCallbacks;
    }

    public Object getTag() {
        return this.mTag;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private List<CameraCaptureCallback> mCameraCaptureCallbacks;
        private MutableConfig mImplementationOptions;
        private final Set<DeferrableSurface> mSurfaces;
        private Object mTag;
        private int mTemplateType;
        private boolean mUseRepeatingSurface;

        public Builder() {
            this.mSurfaces = new HashSet();
            this.mImplementationOptions = MutableOptionsBundle.create();
            this.mTemplateType = -1;
            this.mCameraCaptureCallbacks = new ArrayList();
            this.mUseRepeatingSurface = false;
            this.mTag = null;
        }

        private Builder(CaptureConfig base) {
            this.mSurfaces = new HashSet();
            this.mImplementationOptions = MutableOptionsBundle.create();
            this.mTemplateType = -1;
            this.mCameraCaptureCallbacks = new ArrayList();
            this.mUseRepeatingSurface = false;
            this.mTag = null;
            this.mSurfaces.addAll(base.mSurfaces);
            this.mImplementationOptions = MutableOptionsBundle.from(base.mImplementationOptions);
            this.mTemplateType = base.mTemplateType;
            this.mCameraCaptureCallbacks.addAll(base.getCameraCaptureCallbacks());
            this.mUseRepeatingSurface = base.isUseRepeatingSurface();
            this.mTag = base.getTag();
        }

        public static Builder createFrom(UseCaseConfig<?> config) {
            OptionUnpacker unpacker = config.getCaptureOptionUnpacker(null);
            if (unpacker != null) {
                Builder builder = new Builder();
                unpacker.unpack(config, builder);
                return builder;
            }
            throw new IllegalStateException("Implementation is missing option unpacker for " + config.getTargetName(config.toString()));
        }

        public static Builder from(CaptureConfig base) {
            return new Builder(base);
        }

        public int getTemplateType() {
            return this.mTemplateType;
        }

        public void setTemplateType(int templateType) {
            this.mTemplateType = templateType;
        }

        public void addCameraCaptureCallback(CameraCaptureCallback cameraCaptureCallback) {
            if (!this.mCameraCaptureCallbacks.contains(cameraCaptureCallback)) {
                this.mCameraCaptureCallbacks.add(cameraCaptureCallback);
                return;
            }
            throw new IllegalArgumentException("duplicate camera capture callback");
        }

        public void addAllCameraCaptureCallbacks(Collection<CameraCaptureCallback> cameraCaptureCallbacks) {
            for (CameraCaptureCallback c : cameraCaptureCallbacks) {
                addCameraCaptureCallback(c);
            }
        }

        public void addSurface(DeferrableSurface surface) {
            this.mSurfaces.add(surface);
        }

        public void removeSurface(DeferrableSurface surface) {
            this.mSurfaces.remove(surface);
        }

        public void clearSurfaces() {
            this.mSurfaces.clear();
        }

        public Set<DeferrableSurface> getSurfaces() {
            return this.mSurfaces;
        }

        public void setImplementationOptions(Config config) {
            this.mImplementationOptions = MutableOptionsBundle.from(config);
        }

        public void addImplementationOptions(Config config) {
            for (Config.Option<?> option : config.listOptions()) {
                Object existValue = this.mImplementationOptions.retrieveOption(option, null);
                Object newValue = config.retrieveOption(option);
                if (existValue instanceof MultiValueSet) {
                    ((MultiValueSet) existValue).addAll(((MultiValueSet) newValue).getAllItems());
                } else {
                    if (newValue instanceof MultiValueSet) {
                        newValue = ((MultiValueSet) newValue).clone();
                    }
                    this.mImplementationOptions.insertOption(option, config.getOptionPriority(option), newValue);
                }
            }
        }

        public <T> void addImplementationOption(Config.Option<T> option, T value) {
            this.mImplementationOptions.insertOption(option, value);
        }

        public Config getImplementationOptions() {
            return this.mImplementationOptions;
        }

        boolean isUseRepeatingSurface() {
            return this.mUseRepeatingSurface;
        }

        public void setUseRepeatingSurface(boolean useRepeatingSurface) {
            this.mUseRepeatingSurface = useRepeatingSurface;
        }

        public void setTag(Object tag) {
            this.mTag = tag;
        }

        public CaptureConfig build() {
            return new CaptureConfig(new ArrayList(this.mSurfaces), OptionsBundle.from(this.mImplementationOptions), this.mTemplateType, this.mCameraCaptureCallbacks, this.mUseRepeatingSurface, this.mTag);
        }
    }
}
