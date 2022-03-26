package okhttp3.internal.tls;

import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import org.apache.commons.io.FilenameUtils;
/* compiled from: OkHostnameVerifier.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u0004H\u0002J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u001c\u0010\u0012\u001a\u00020\u000e2\b\u0010\u0013\u001a\u0004\u0018\u00010\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\bH\u0002J\u0018\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lokhttp3/internal/tls/OkHostnameVerifier;", "Ljavax/net/ssl/HostnameVerifier;", "()V", "ALT_DNS_NAME", "", "ALT_IPA_NAME", "allSubjectAltNames", "", "", "certificate", "Ljava/security/cert/X509Certificate;", "getSubjectAltNames", "type", "verify", "", "host", "session", "Ljavax/net/ssl/SSLSession;", "verifyHostname", "hostname", "pattern", "verifyIpAddress", "ipAddress", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class OkHostnameVerifier implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    @Override // javax.net.ssl.HostnameVerifier
    public boolean verify(String host, SSLSession session) {
        Intrinsics.checkParameterIsNotNull(host, "host");
        Intrinsics.checkParameterIsNotNull(session, "session");
        try {
            Certificate certificate = session.getPeerCertificates()[0];
            if (certificate != null) {
                return verify(host, (X509Certificate) certificate);
            }
            throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
        } catch (SSLException e) {
            return false;
        }
    }

    public final boolean verify(String host, X509Certificate certificate) {
        Intrinsics.checkParameterIsNotNull(host, "host");
        Intrinsics.checkParameterIsNotNull(certificate, "certificate");
        if (Util.canParseAsIpAddress(host)) {
            return verifyIpAddress(host, certificate);
        }
        return verifyHostname(host, certificate);
    }

    private final boolean verifyIpAddress(String ipAddress, X509Certificate certificate) {
        Iterable<String> $this$any$iv = getSubjectAltNames(certificate, 7);
        if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (String it : $this$any$iv) {
            if (StringsKt.equals(ipAddress, it, true)) {
                return true;
            }
        }
        return false;
    }

    private final boolean verifyHostname(String hostname, X509Certificate certificate) {
        Locale locale = Locale.US;
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
        if (hostname != null) {
            String hostname2 = hostname.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(hostname2, "(this as java.lang.String).toLowerCase(locale)");
            Iterable<String> $this$any$iv = getSubjectAltNames(certificate, 2);
            if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
                return false;
            }
            for (String it : $this$any$iv) {
                if (INSTANCE.verifyHostname(hostname2, it)) {
                    return true;
                }
            }
            return false;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    private final boolean verifyHostname(String hostname, String pattern) {
        String hostname2 = hostname;
        String pattern2 = pattern;
        String str = hostname2;
        if ((str == null || str.length() == 0) || StringsKt.startsWith$default(hostname2, ".", false, 2, (Object) null) || StringsKt.endsWith$default(hostname2, "..", false, 2, (Object) null)) {
            return false;
        }
        String str2 = pattern2;
        if ((str2 == null || str2.length() == 0) || StringsKt.startsWith$default(pattern2, ".", false, 2, (Object) null) || StringsKt.endsWith$default(pattern2, "..", false, 2, (Object) null)) {
            return false;
        }
        if (!StringsKt.endsWith$default(hostname2, ".", false, 2, (Object) null)) {
            hostname2 = hostname2 + ".";
        }
        if (!StringsKt.endsWith$default(pattern2, ".", false, 2, (Object) null)) {
            pattern2 = pattern2 + ".";
        }
        Locale locale = Locale.US;
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
        if (pattern2 != null) {
            String pattern3 = pattern2.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(pattern3, "(this as java.lang.String).toLowerCase(locale)");
            if (!StringsKt.contains$default((CharSequence) pattern3, (CharSequence) "*", false, 2, (Object) null)) {
                return Intrinsics.areEqual(hostname2, pattern3);
            }
            if (!StringsKt.startsWith$default(pattern3, "*.", false, 2, (Object) null) || StringsKt.indexOf$default((CharSequence) pattern3, '*', 1, false, 4, (Object) null) != -1 || hostname2.length() < pattern3.length() || Intrinsics.areEqual("*.", pattern3)) {
                return false;
            }
            if (pattern3 != null) {
                String suffix = pattern3.substring(1);
                Intrinsics.checkExpressionValueIsNotNull(suffix, "(this as java.lang.String).substring(startIndex)");
                if (!StringsKt.endsWith$default(hostname2, suffix, false, 2, (Object) null)) {
                    return false;
                }
                int suffixStartIndexInHostname = hostname2.length() - suffix.length();
                return suffixStartIndexInHostname <= 0 || StringsKt.lastIndexOf$default((CharSequence) hostname2, (char) FilenameUtils.EXTENSION_SEPARATOR, suffixStartIndexInHostname + -1, false, 4, (Object) null) == -1;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    public final List<String> allSubjectAltNames(X509Certificate certificate) {
        Intrinsics.checkParameterIsNotNull(certificate, "certificate");
        return CollectionsKt.plus((Collection) getSubjectAltNames(certificate, 7), (Iterable) getSubjectAltNames(certificate, 2));
    }

    private final List<String> getSubjectAltNames(X509Certificate certificate, int type) {
        Object altName;
        try {
            Collection subjectAltNames = certificate.getSubjectAlternativeNames();
            if (subjectAltNames == null) {
                return CollectionsKt.emptyList();
            }
            List result = new ArrayList();
            for (List subjectAltName : subjectAltNames) {
                if (subjectAltName != null && subjectAltName.size() >= 2 && !(!Intrinsics.areEqual(subjectAltName.get(0), Integer.valueOf(type))) && (altName = subjectAltName.get(1)) != null) {
                    if (altName != null) {
                        result.add((String) altName);
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                    }
                }
            }
            return result;
        } catch (CertificateParsingException e) {
            return CollectionsKt.emptyList();
        }
    }
}
