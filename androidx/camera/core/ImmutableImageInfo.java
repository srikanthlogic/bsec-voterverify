package androidx.camera.core;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ImmutableImageInfo implements ImageInfo {
    @Override // androidx.camera.core.ImageInfo
    public abstract int getRotationDegrees();

    @Override // androidx.camera.core.ImageInfo
    public abstract Object getTag();

    @Override // androidx.camera.core.ImageInfo
    public abstract long getTimestamp();

    public static ImageInfo create(Object tag, long timestamp, int rotationDegrees) {
        return new AutoValue_ImmutableImageInfo(tag, timestamp, rotationDegrees);
    }
}
