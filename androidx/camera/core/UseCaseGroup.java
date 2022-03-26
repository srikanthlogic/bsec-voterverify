package androidx.camera.core;

import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class UseCaseGroup {
    private List<UseCase> mUseCases;
    private ViewPort mViewPort;

    UseCaseGroup(ViewPort viewPort, List<UseCase> useCases) {
        this.mViewPort = viewPort;
        this.mUseCases = useCases;
    }

    public ViewPort getViewPort() {
        return this.mViewPort;
    }

    public List<UseCase> getUseCases() {
        return this.mUseCases;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private List<UseCase> mUseCases = new ArrayList();
        private ViewPort mViewPort;

        public Builder setViewPort(ViewPort viewPort) {
            this.mViewPort = viewPort;
            return this;
        }

        public Builder addUseCase(UseCase useCase) {
            this.mUseCases.add(useCase);
            return this;
        }

        public UseCaseGroup build() {
            Preconditions.checkArgument(!this.mUseCases.isEmpty(), "UseCase must not be empty.");
            return new UseCaseGroup(this.mViewPort, this.mUseCases);
        }
    }
}
