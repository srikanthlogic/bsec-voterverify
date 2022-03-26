package okhttp3;

import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;
import org.apache.commons.io.FilenameUtils;
/* compiled from: CertificatePinner.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000 !2\u00020\u0001:\u0003 !\"B\u001f\b\u0000\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J)\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0012\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u000fH\u0000¢\u0006\u0002\b\u0012J)\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0014\"\u00020\u0015H\u0007¢\u0006\u0002\u0010\u0016J\u001c\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0010J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\u001b\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u00102\u0006\u0010\f\u001a\u00020\rH\u0000¢\u0006\u0002\b\u001bJ\b\u0010\u001c\u001a\u00020\u001dH\u0016J\u0017\u0010\u001e\u001a\u00020\u00002\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0000¢\u0006\u0002\b\u001fR\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lokhttp3/CertificatePinner;", "", "pins", "", "Lokhttp3/CertificatePinner$Pin;", "certificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "(Ljava/util/Set;Lokhttp3/internal/tls/CertificateChainCleaner;)V", "getCertificateChainCleaner$okhttp", "()Lokhttp3/internal/tls/CertificateChainCleaner;", "check", "", "hostname", "", "cleanedPeerCertificatesFn", "Lkotlin/Function0;", "", "Ljava/security/cert/X509Certificate;", "check$okhttp", "peerCertificates", "", "Ljava/security/cert/Certificate;", "(Ljava/lang/String;[Ljava/security/cert/Certificate;)V", "equals", "", "other", "findMatchingPins", "findMatchingPins$okhttp", "hashCode", "", "withCertificateChainCleaner", "withCertificateChainCleaner$okhttp", "Builder", "Companion", "Pin", "okhttp"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class CertificatePinner {
    public static final Companion Companion = new Companion(null);
    public static final CertificatePinner DEFAULT = new Builder().build();
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set<Pin> pins;

    @JvmStatic
    public static final String pin(Certificate certificate) {
        return Companion.pin(certificate);
    }

    public CertificatePinner(Set<Pin> set, CertificateChainCleaner certificateChainCleaner) {
        Intrinsics.checkParameterIsNotNull(set, "pins");
        this.pins = set;
        this.certificateChainCleaner = certificateChainCleaner;
    }

    public final CertificateChainCleaner getCertificateChainCleaner$okhttp() {
        return this.certificateChainCleaner;
    }

    public final void check(String hostname, List<? extends Certificate> list) throws SSLPeerUnverifiedException {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        Intrinsics.checkParameterIsNotNull(list, "peerCertificates");
        check$okhttp(hostname, new Function0<List<? extends X509Certificate>>(list, hostname) { // from class: okhttp3.CertificatePinner$check$1
            final /* synthetic */ String $hostname;
            final /* synthetic */ List $peerCertificates;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$peerCertificates = r2;
                this.$hostname = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final List<? extends X509Certificate> invoke() {
                Iterable iterable;
                CertificateChainCleaner certificateChainCleaner$okhttp = CertificatePinner.this.getCertificateChainCleaner$okhttp();
                if (certificateChainCleaner$okhttp == null || (iterable = certificateChainCleaner$okhttp.clean(this.$peerCertificates, this.$hostname)) == null) {
                    iterable = this.$peerCertificates;
                }
                Iterable<Certificate> $this$map$iv = iterable;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Certificate it : $this$map$iv) {
                    if (it != null) {
                        destination$iv$iv.add((X509Certificate) it);
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                    }
                }
                return (List) destination$iv$iv;
            }
        });
    }

    public final void check$okhttp(String hostname, Function0<? extends List<? extends X509Certificate>> function0) {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        Intrinsics.checkParameterIsNotNull(function0, "cleanedPeerCertificatesFn");
        List<Pin> pins = findMatchingPins$okhttp(hostname);
        if (!pins.isEmpty()) {
            List<X509Certificate> peerCertificates = (List) function0.invoke();
            for (X509Certificate peerCertificate : peerCertificates) {
                ByteString sha1 = null;
                ByteString sha256 = null;
                for (Pin pin : pins) {
                    String hashAlgorithm = pin.getHashAlgorithm();
                    int hashCode = hashAlgorithm.hashCode();
                    if (hashCode != 109397962) {
                        if (hashCode == 2052263656 && hashAlgorithm.equals("sha256/")) {
                            if (sha256 == null) {
                                sha256 = Companion.toSha256ByteString$okhttp(peerCertificate);
                            }
                            if (Intrinsics.areEqual(pin.getHash(), sha256)) {
                                return;
                            }
                        }
                        throw new AssertionError("unsupported hashAlgorithm: " + pin.getHashAlgorithm());
                    } else if (hashAlgorithm.equals("sha1/")) {
                        if (sha1 == null) {
                            sha1 = Companion.toSha1ByteString$okhttp(peerCertificate);
                        }
                        if (Intrinsics.areEqual(pin.getHash(), sha1)) {
                            return;
                        }
                    } else {
                        throw new AssertionError("unsupported hashAlgorithm: " + pin.getHashAlgorithm());
                    }
                }
            }
            StringBuilder $this$buildString = new StringBuilder();
            $this$buildString.append("Certificate pinning failure!");
            $this$buildString.append("\n  Peer certificate chain:");
            for (X509Certificate element : peerCertificates) {
                $this$buildString.append("\n    ");
                $this$buildString.append(Companion.pin(element));
                $this$buildString.append(": ");
                Principal subjectDN = element.getSubjectDN();
                Intrinsics.checkExpressionValueIsNotNull(subjectDN, "element.subjectDN");
                $this$buildString.append(subjectDN.getName());
            }
            $this$buildString.append("\n  Pinned certificates for ");
            $this$buildString.append(hostname);
            $this$buildString.append(":");
            for (Pin pin2 : pins) {
                $this$buildString.append("\n    ");
                $this$buildString.append(pin2);
            }
            String message = $this$buildString.toString();
            Intrinsics.checkExpressionValueIsNotNull(message, "StringBuilder().apply(builderAction).toString()");
            throw new SSLPeerUnverifiedException(message);
        }
    }

    @Deprecated(message = "replaced with {@link #check(String, List)}.", replaceWith = @ReplaceWith(expression = "check(hostname, peerCertificates.toList())", imports = {}))
    public final void check(String hostname, Certificate... peerCertificates) throws SSLPeerUnverifiedException {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        Intrinsics.checkParameterIsNotNull(peerCertificates, "peerCertificates");
        check(hostname, ArraysKt.toList(peerCertificates));
    }

    public final List<Pin> findMatchingPins$okhttp(String hostname) {
        Intrinsics.checkParameterIsNotNull(hostname, "hostname");
        List result = CollectionsKt.emptyList();
        for (Pin pin : this.pins) {
            if (pin.matches(hostname)) {
                if (result.isEmpty()) {
                    result = new ArrayList();
                }
                if (result != null) {
                    TypeIntrinsics.asMutableList(result).add(pin);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableList<okhttp3.CertificatePinner.Pin>");
                }
            }
        }
        return result;
    }

    public final CertificatePinner withCertificateChainCleaner$okhttp(CertificateChainCleaner certificateChainCleaner) {
        if (Intrinsics.areEqual(this.certificateChainCleaner, certificateChainCleaner)) {
            return this;
        }
        return new CertificatePinner(this.pins, certificateChainCleaner);
    }

    public boolean equals(Object other) {
        return (other instanceof CertificatePinner) && Intrinsics.areEqual(((CertificatePinner) other).pins, this.pins) && Intrinsics.areEqual(((CertificatePinner) other).certificateChainCleaner, this.certificateChainCleaner);
    }

    public int hashCode() {
        int result = ((37 * 41) + this.pins.hashCode()) * 41;
        CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
        return result + (certificateChainCleaner != null ? certificateChainCleaner.hashCode() : 0);
    }

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0080\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÂ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0006HÆ\u0003J'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0003J\b\u0010\u0017\u001a\u00020\u0003H\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokhttp3/CertificatePinner$Pin;", "", "pattern", "", "hashAlgorithm", "hash", "Lokio/ByteString;", "(Ljava/lang/String;Ljava/lang/String;Lokio/ByteString;)V", "getHash", "()Lokio/ByteString;", "getHashAlgorithm", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "matches", "hostname", "toString", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Pin {
        private final ByteString hash;
        private final String hashAlgorithm;
        private final String pattern;

        private final String component1() {
            return this.pattern;
        }

        public static /* synthetic */ Pin copy$default(Pin pin, String str, String str2, ByteString byteString, int i, Object obj) {
            if ((i & 1) != 0) {
                str = pin.pattern;
            }
            if ((i & 2) != 0) {
                str2 = pin.hashAlgorithm;
            }
            if ((i & 4) != 0) {
                byteString = pin.hash;
            }
            return pin.copy(str, str2, byteString);
        }

        public final String component2() {
            return this.hashAlgorithm;
        }

        public final ByteString component3() {
            return this.hash;
        }

        public final Pin copy(String str, String str2, ByteString byteString) {
            Intrinsics.checkParameterIsNotNull(str, "pattern");
            Intrinsics.checkParameterIsNotNull(str2, "hashAlgorithm");
            Intrinsics.checkParameterIsNotNull(byteString, "hash");
            return new Pin(str, str2, byteString);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Pin)) {
                return false;
            }
            Pin pin = (Pin) obj;
            return Intrinsics.areEqual(this.pattern, pin.pattern) && Intrinsics.areEqual(this.hashAlgorithm, pin.hashAlgorithm) && Intrinsics.areEqual(this.hash, pin.hash);
        }

        public int hashCode() {
            String str = this.pattern;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.hashAlgorithm;
            int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            ByteString byteString = this.hash;
            if (byteString != null) {
                i = byteString.hashCode();
            }
            return hashCode2 + i;
        }

        public Pin(String pattern, String hashAlgorithm, ByteString hash) {
            Intrinsics.checkParameterIsNotNull(pattern, "pattern");
            Intrinsics.checkParameterIsNotNull(hashAlgorithm, "hashAlgorithm");
            Intrinsics.checkParameterIsNotNull(hash, "hash");
            this.pattern = pattern;
            this.hashAlgorithm = hashAlgorithm;
            this.hash = hash;
        }

        public final String getHashAlgorithm() {
            return this.hashAlgorithm;
        }

        public final ByteString getHash() {
            return this.hash;
        }

        public final boolean matches(String hostname) {
            Intrinsics.checkParameterIsNotNull(hostname, "hostname");
            if (StringsKt.startsWith$default(this.pattern, "**.", false, 2, (Object) null)) {
                int suffixLength = this.pattern.length() - 3;
                int prefixLength = hostname.length() - suffixLength;
                if (StringsKt.regionMatches(hostname, hostname.length() - suffixLength, this.pattern, 3, suffixLength, (r12 & 16) != 0 ? false : false)) {
                    return prefixLength == 0 || hostname.charAt(prefixLength + -1) == '.';
                }
                return false;
            } else if (!StringsKt.startsWith$default(this.pattern, "*.", false, 2, (Object) null)) {
                return Intrinsics.areEqual(hostname, this.pattern);
            } else {
                int suffixLength2 = this.pattern.length() - 1;
                return (StringsKt.regionMatches(hostname, hostname.length() - suffixLength2, this.pattern, 1, suffixLength2, (r12 & 16) != 0 ? false : false)) && StringsKt.lastIndexOf$default((CharSequence) hostname, (char) FilenameUtils.EXTENSION_SEPARATOR, (hostname.length() - suffixLength2) + -1, false, 4, (Object) null) == -1;
            }
        }

        public String toString() {
            return this.hashAlgorithm + this.hash.base64();
        }
    }

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J'\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\t\"\u00020\b¢\u0006\u0002\u0010\nJ\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lokhttp3/CertificatePinner$Builder;", "", "()V", "pins", "", "Lokhttp3/CertificatePinner$Pin;", "add", "pattern", "", "", "(Ljava/lang/String;[Ljava/lang/String;)Lokhttp3/CertificatePinner$Builder;", "build", "Lokhttp3/CertificatePinner;", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Builder {
        private final List<Pin> pins = new ArrayList();

        public final Builder add(String pattern, String... pins) {
            Intrinsics.checkParameterIsNotNull(pattern, "pattern");
            Intrinsics.checkParameterIsNotNull(pins, "pins");
            Builder $this$apply = this;
            for (String pin : pins) {
                $this$apply.pins.add(CertificatePinner.Companion.newPin$okhttp(pattern, pin));
            }
            return this;
        }

        public final CertificatePinner build() {
            return new CertificatePinner(CollectionsKt.toSet(this.pins), null);
        }
    }

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000¢\u0006\u0002\b\nJ\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0011\u0010\r\u001a\u00020\u000e*\u00020\u000fH\u0000¢\u0006\u0002\b\u0010J\u0011\u0010\u0011\u001a\u00020\u000e*\u00020\u000fH\u0000¢\u0006\u0002\b\u0012R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lokhttp3/CertificatePinner$Companion;", "", "()V", "DEFAULT", "Lokhttp3/CertificatePinner;", "newPin", "Lokhttp3/CertificatePinner$Pin;", "pattern", "", "pin", "newPin$okhttp", "certificate", "Ljava/security/cert/Certificate;", "toSha1ByteString", "Lokio/ByteString;", "Ljava/security/cert/X509Certificate;", "toSha1ByteString$okhttp", "toSha256ByteString", "toSha256ByteString$okhttp", "okhttp"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        @JvmStatic
        public final String pin(Certificate certificate) {
            Intrinsics.checkParameterIsNotNull(certificate, "certificate");
            if (certificate instanceof X509Certificate) {
                return "sha256/" + toSha256ByteString$okhttp((X509Certificate) certificate).base64();
            }
            throw new IllegalArgumentException("Certificate pinning requires X509 certificates".toString());
        }

        public final ByteString toSha1ByteString$okhttp(X509Certificate $this$toSha1ByteString) {
            Intrinsics.checkParameterIsNotNull($this$toSha1ByteString, "$this$toSha1ByteString");
            ByteString.Companion companion = ByteString.Companion;
            PublicKey publicKey = $this$toSha1ByteString.getPublicKey();
            Intrinsics.checkExpressionValueIsNotNull(publicKey, "publicKey");
            byte[] encoded = publicKey.getEncoded();
            Intrinsics.checkExpressionValueIsNotNull(encoded, "publicKey.encoded");
            return ByteString.Companion.of$default(companion, encoded, 0, 0, 3, null).sha1();
        }

        public final ByteString toSha256ByteString$okhttp(X509Certificate $this$toSha256ByteString) {
            Intrinsics.checkParameterIsNotNull($this$toSha256ByteString, "$this$toSha256ByteString");
            ByteString.Companion companion = ByteString.Companion;
            PublicKey publicKey = $this$toSha256ByteString.getPublicKey();
            Intrinsics.checkExpressionValueIsNotNull(publicKey, "publicKey");
            byte[] encoded = publicKey.getEncoded();
            Intrinsics.checkExpressionValueIsNotNull(encoded, "publicKey.encoded");
            return ByteString.Companion.of$default(companion, encoded, 0, 0, 3, null).sha256();
        }

        public final Pin newPin$okhttp(String pattern, String pin) {
            Intrinsics.checkParameterIsNotNull(pattern, "pattern");
            Intrinsics.checkParameterIsNotNull(pin, "pin");
            if ((StringsKt.startsWith$default(pattern, "*.", false, 2, (Object) null) && StringsKt.indexOf$default((CharSequence) pattern, "*", 1, false, 4, (Object) null) == -1) || (StringsKt.startsWith$default(pattern, "**.", false, 2, (Object) null) && StringsKt.indexOf$default((CharSequence) pattern, "*", 2, false, 4, (Object) null) == -1) || StringsKt.indexOf$default((CharSequence) pattern, "*", 0, false, 6, (Object) null) == -1) {
                String canonicalPattern = HostnamesKt.toCanonicalHost(pattern);
                if (canonicalPattern == null) {
                    throw new IllegalArgumentException("Invalid pattern: " + pattern);
                } else if (StringsKt.startsWith$default(pin, "sha1/", false, 2, (Object) null)) {
                    ByteString.Companion companion = ByteString.Companion;
                    String substring = pin.substring("sha1/".length());
                    Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                    ByteString hash = companion.decodeBase64(substring);
                    if (hash == null) {
                        Intrinsics.throwNpe();
                    }
                    return new Pin(canonicalPattern, "sha1/", hash);
                } else if (StringsKt.startsWith$default(pin, "sha256/", false, 2, (Object) null)) {
                    ByteString.Companion companion2 = ByteString.Companion;
                    String substring2 = pin.substring("sha256/".length());
                    Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
                    ByteString hash2 = companion2.decodeBase64(substring2);
                    if (hash2 == null) {
                        Intrinsics.throwNpe();
                    }
                    return new Pin(canonicalPattern, "sha256/", hash2);
                } else {
                    throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + pin);
                }
            } else {
                throw new IllegalArgumentException(("Unexpected pattern: " + pattern).toString());
            }
        }
    }
}
