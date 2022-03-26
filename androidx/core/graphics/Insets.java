package androidx.core.graphics;

import android.graphics.Rect;
/* loaded from: classes.dex */
public final class Insets {
    public static final Insets NONE = new Insets(0, 0, 0, 0);
    public final int bottom;
    public final int left;
    public final int right;
    public final int top;

    private Insets(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static Insets of(int left, int top, int right, int bottom) {
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return NONE;
        }
        return new Insets(left, top, right, bottom);
    }

    public static Insets of(Rect r) {
        return of(r.left, r.top, r.right, r.bottom);
    }

    public static Insets add(Insets a2, Insets b) {
        return of(a2.left + b.left, a2.top + b.top, a2.right + b.right, a2.bottom + b.bottom);
    }

    public static Insets subtract(Insets a2, Insets b) {
        return of(a2.left - b.left, a2.top - b.top, a2.right - b.right, a2.bottom - b.bottom);
    }

    public static Insets max(Insets a2, Insets b) {
        return of(Math.max(a2.left, b.left), Math.max(a2.top, b.top), Math.max(a2.right, b.right), Math.max(a2.bottom, b.bottom));
    }

    public static Insets min(Insets a2, Insets b) {
        return of(Math.min(a2.left, b.left), Math.min(a2.top, b.top), Math.min(a2.right, b.right), Math.min(a2.bottom, b.bottom));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Insets insets = (Insets) o;
        if (this.bottom == insets.bottom && this.left == insets.left && this.right == insets.right && this.top == insets.top) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public String toString() {
        return "Insets{left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + '}';
    }

    @Deprecated
    public static Insets wrap(android.graphics.Insets insets) {
        return toCompatInsets(insets);
    }

    public static Insets toCompatInsets(android.graphics.Insets insets) {
        return of(insets.left, insets.top, insets.right, insets.bottom);
    }

    public android.graphics.Insets toPlatformInsets() {
        return android.graphics.Insets.of(this.left, this.top, this.right, this.bottom);
    }
}
