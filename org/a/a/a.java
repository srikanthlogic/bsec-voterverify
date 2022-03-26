package org.a.a;
/* loaded from: classes3.dex */
public abstract class a implements c {
    @Override // org.a.a.c
    public d a() {
        return b();
    }

    public abstract d b();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        return b().equals(((c) obj).a());
    }

    public int hashCode() {
        return b().hashCode();
    }
}
