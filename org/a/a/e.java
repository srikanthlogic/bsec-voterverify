package org.a.a;
/* loaded from: classes3.dex */
public class e extends b {

    /* renamed from: a */
    String f120a;

    public e(String str) {
        if (a(str)) {
            this.f120a = str;
            return;
        }
        throw new IllegalArgumentException("string " + str + " not an OID");
    }

    private static boolean a(String str) {
        char charAt;
        if (str.length() < 3 || str.charAt(1) != '.' || (charAt = str.charAt(0)) < '0' || charAt > '2') {
            return false;
        }
        boolean z = false;
        for (int length = str.length() - 1; length >= 2; length--) {
            char charAt2 = str.charAt(length);
            if ('0' <= charAt2 && charAt2 <= '9') {
                z = true;
            } else if (charAt2 != '.' || !z) {
                return false;
            } else {
                z = false;
            }
        }
        return z;
    }

    @Override // org.a.a.b
    boolean a(d dVar) {
        if (!(dVar instanceof e)) {
            return false;
        }
        return this.f120a.equals(((e) dVar).f120a);
    }

    public String c() {
        return this.f120a;
    }

    @Override // org.a.a.d, org.a.a.a
    public int hashCode() {
        return this.f120a.hashCode();
    }

    public String toString() {
        return c();
    }
}
