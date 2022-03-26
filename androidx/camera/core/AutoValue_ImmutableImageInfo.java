package androidx.camera.core;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_ImmutableImageInfo extends ImmutableImageInfo {
    private final int rotationDegrees;
    private final Object tag;
    private final long timestamp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_ImmutableImageInfo(Object tag, long timestamp, int rotationDegrees) {
        this.tag = tag;
        this.timestamp = timestamp;
        this.rotationDegrees = rotationDegrees;
    }

    @Override // androidx.camera.core.ImmutableImageInfo, androidx.camera.core.ImageInfo
    public Object getTag() {
        return this.tag;
    }

    @Override // androidx.camera.core.ImmutableImageInfo, androidx.camera.core.ImageInfo
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override // androidx.camera.core.ImmutableImageInfo, androidx.camera.core.ImageInfo
    public int getRotationDegrees() {
        return this.rotationDegrees;
    }

    public String toString() {
        return "ImmutableImageInfo{tag=" + this.tag + ", timestamp=" + this.timestamp + ", rotationDegrees=" + this.rotationDegrees + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImmutableImageInfo)) {
            return false;
        }
        ImmutableImageInfo that = (ImmutableImageInfo) o;
        Object obj = this.tag;
        if (obj != null ? obj.equals(that.getTag()) : that.getTag() == null) {
            if (this.timestamp == that.getTimestamp() && this.rotationDegrees == that.getRotationDegrees()) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int h$ = 1 * 1000003;
        Object obj = this.tag;
        int hashCode = obj == null ? 0 : obj.hashCode();
        long j = this.timestamp;
        return ((((h$ ^ hashCode) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003) ^ this.rotationDegrees;
    }
}
