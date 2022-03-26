package org.a.a;
/* loaded from: classes3.dex */
public abstract class b extends d {
    abstract boolean a(d dVar);

    @Override // org.a.a.d, org.a.a.a
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof c) && a(((c) obj).a());
    }
}
