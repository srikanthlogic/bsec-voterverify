package okhttp3.internal.tls;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BasicTrustRootIndex.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0003\"\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0096\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000f\u001a\u00020\u0004H\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lokhttp3/internal/tls/BasicTrustRootIndex;", "Lokhttp3/internal/tls/TrustRootIndex;", "caCerts", "", "Ljava/security/cert/X509Certificate;", "([Ljava/security/cert/X509Certificate;)V", "subjectToCaCerts", "", "Ljavax/security/auth/x500/X500Principal;", "", "equals", "", "other", "", "findByIssuerAndSignature", "cert", "hashCode", "", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class BasicTrustRootIndex implements TrustRootIndex {
    private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;

    /* JADX INFO: Multiple debug info for r8v2 java.util.LinkedHashSet: [D('$i$a$-getOrPut-BasicTrustRootIndex$1' int), D('answer$iv' java.lang.Object)] */
    public BasicTrustRootIndex(X509Certificate... caCerts) {
        Intrinsics.checkParameterIsNotNull(caCerts, "caCerts");
        Map map = new LinkedHashMap();
        for (X509Certificate caCert : caCerts) {
            X500Principal subjectX500Principal = caCert.getSubjectX500Principal();
            Intrinsics.checkExpressionValueIsNotNull(subjectX500Principal, "caCert.subjectX500Principal");
            Object value$iv = map.get(subjectX500Principal);
            if (value$iv == null) {
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                map.put(subjectX500Principal, linkedHashSet);
                value$iv = linkedHashSet;
            }
            ((Set) value$iv).add(caCert);
        }
        this.subjectToCaCerts = map;
    }

    @Override // okhttp3.internal.tls.TrustRootIndex
    public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(cert, "cert");
        Iterable subjectCaCerts = (Set) this.subjectToCaCerts.get(cert.getIssuerX500Principal());
        Object obj = null;
        if (subjectCaCerts == null) {
            return null;
        }
        Iterator<T> it = subjectCaCerts.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object element$iv = it.next();
            try {
                cert.verify(((X509Certificate) element$iv).getPublicKey());
                z = true;
                continue;
            } catch (Exception e) {
                z = false;
                continue;
            }
            if (z) {
                obj = element$iv;
                break;
            }
        }
        return (X509Certificate) obj;
    }

    public boolean equals(Object other) {
        return other == this || ((other instanceof BasicTrustRootIndex) && Intrinsics.areEqual(((BasicTrustRootIndex) other).subjectToCaCerts, this.subjectToCaCerts));
    }

    public int hashCode() {
        return this.subjectToCaCerts.hashCode();
    }
}
