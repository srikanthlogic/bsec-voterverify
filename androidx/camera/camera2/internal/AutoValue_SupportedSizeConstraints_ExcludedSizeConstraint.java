package androidx.camera.camera2.internal;

import android.util.Range;
import android.util.Size;
import androidx.camera.camera2.internal.SupportedSizeConstraints;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
final class AutoValue_SupportedSizeConstraints_ExcludedSizeConstraint extends SupportedSizeConstraints.ExcludedSizeConstraint {
    private final Range<Integer> affectedApiLevels;
    private final Set<Integer> affectedFormats;
    private final List<Size> excludedSizes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_SupportedSizeConstraints_ExcludedSizeConstraint(Set<Integer> affectedFormats, Range<Integer> affectedApiLevels, List<Size> excludedSizes) {
        if (affectedFormats != null) {
            this.affectedFormats = affectedFormats;
            if (affectedApiLevels != null) {
                this.affectedApiLevels = affectedApiLevels;
                if (excludedSizes != null) {
                    this.excludedSizes = excludedSizes;
                    return;
                }
                throw new NullPointerException("Null excludedSizes");
            }
            throw new NullPointerException("Null affectedApiLevels");
        }
        throw new NullPointerException("Null affectedFormats");
    }

    @Override // androidx.camera.camera2.internal.SupportedSizeConstraints.ExcludedSizeConstraint, androidx.camera.camera2.internal.SupportedSizeConstraints.Constraint
    public Set<Integer> getAffectedFormats() {
        return this.affectedFormats;
    }

    @Override // androidx.camera.camera2.internal.SupportedSizeConstraints.ExcludedSizeConstraint, androidx.camera.camera2.internal.SupportedSizeConstraints.Constraint
    public Range<Integer> getAffectedApiLevels() {
        return this.affectedApiLevels;
    }

    @Override // androidx.camera.camera2.internal.SupportedSizeConstraints.ExcludedSizeConstraint
    public List<Size> getExcludedSizes() {
        return this.excludedSizes;
    }

    public String toString() {
        return "ExcludedSizeConstraint{affectedFormats=" + this.affectedFormats + ", affectedApiLevels=" + this.affectedApiLevels + ", excludedSizes=" + this.excludedSizes + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SupportedSizeConstraints.ExcludedSizeConstraint)) {
            return false;
        }
        SupportedSizeConstraints.ExcludedSizeConstraint that = (SupportedSizeConstraints.ExcludedSizeConstraint) o;
        if (!this.affectedFormats.equals(that.getAffectedFormats()) || !this.affectedApiLevels.equals(that.getAffectedApiLevels()) || !this.excludedSizes.equals(that.getExcludedSizes())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.affectedFormats.hashCode()) * 1000003) ^ this.affectedApiLevels.hashCode()) * 1000003) ^ this.excludedSizes.hashCode();
    }
}
