package androidx.core.content.pm;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class PackageInfoCompat {
    public static long getLongVersionCode(PackageInfo info) {
        if (Build.VERSION.SDK_INT >= 28) {
            return info.getLongVersionCode();
        }
        return (long) info.versionCode;
    }

    public static List<Signature> getSignatures(PackageManager packageManager, String packageName) throws PackageManager.NameNotFoundException {
        Signature[] array;
        if (Build.VERSION.SDK_INT >= 28) {
            SigningInfo signingInfo = packageManager.getPackageInfo(packageName, 134217728).signingInfo;
            if (Api28Impl.hasMultipleSigners(signingInfo)) {
                array = Api28Impl.getApkContentsSigners(signingInfo);
            } else {
                array = Api28Impl.getSigningCertificateHistory(signingInfo);
            }
        } else {
            array = packageManager.getPackageInfo(packageName, 64).signatures;
        }
        if (array == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(array);
    }

    public static boolean hasSignatures(PackageManager packageManager, String packageName, Map<byte[], Integer> certificatesAndType, boolean matchExact) throws PackageManager.NameNotFoundException {
        if (certificatesAndType.isEmpty()) {
            return false;
        }
        Set<byte[]> expectedCertBytes = certificatesAndType.keySet();
        for (byte[] bytes : expectedCertBytes) {
            if (bytes != null) {
                Integer type = certificatesAndType.get(bytes);
                if (type != null) {
                    int intValue = type.intValue();
                    if (!(intValue == 0 || intValue == 1)) {
                        throw new IllegalArgumentException("Unsupported certificate type " + type + " when verifying " + packageName);
                    }
                } else {
                    throw new IllegalArgumentException("Type must be specified for cert when verifying " + packageName);
                }
            } else {
                throw new IllegalArgumentException("Cert byte array cannot be null when verifying " + packageName);
            }
        }
        List<Signature> signers = getSignatures(packageManager, packageName);
        if (!matchExact && Build.VERSION.SDK_INT >= 28) {
            for (byte[] bytes2 : expectedCertBytes) {
                if (!Api28Impl.hasSigningCertificate(packageManager, packageName, bytes2, certificatesAndType.get(bytes2).intValue())) {
                    return false;
                }
            }
            return true;
        } else if (signers.size() == 0 || certificatesAndType.size() > signers.size() || (matchExact && certificatesAndType.size() != signers.size())) {
            return false;
        } else {
            byte[][] sha256Digests = null;
            if (certificatesAndType.containsValue(1)) {
                sha256Digests = new byte[signers.size()];
                for (int index = 0; index < signers.size(); index++) {
                    sha256Digests[index] = computeSHA256Digest(signers.get(index).toByteArray());
                }
            }
            Iterator<byte[]> it = expectedCertBytes.iterator();
            if (!it.hasNext()) {
                return false;
            }
            byte[] bytes3 = it.next();
            Integer type2 = certificatesAndType.get(bytes3);
            int intValue2 = type2.intValue();
            if (intValue2 != 0) {
                if (intValue2 != 1) {
                    throw new IllegalArgumentException("Unsupported certificate type " + type2);
                } else if (!byteArrayContains(sha256Digests, bytes3)) {
                    return false;
                }
            } else if (!signers.contains(new Signature(bytes3))) {
                return false;
            }
            return true;
        }
    }

    private static boolean byteArrayContains(byte[][] array, byte[] expected) {
        for (byte[] item : array) {
            if (Arrays.equals(expected, item)) {
                return true;
            }
        }
        return false;
    }

    private static byte[] computeSHA256Digest(byte[] bytes) {
        try {
            return MessageDigest.getInstance("SHA256").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Device doesn't support SHA256 cert checking", e);
        }
    }

    private PackageInfoCompat() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Api28Impl {
        private Api28Impl() {
        }

        static boolean hasSigningCertificate(PackageManager packageManager, String packageName, byte[] bytes, int type) {
            return packageManager.hasSigningCertificate(packageName, bytes, type);
        }

        static boolean hasMultipleSigners(SigningInfo signingInfo) {
            return signingInfo.hasMultipleSigners();
        }

        static Signature[] getApkContentsSigners(SigningInfo signingInfo) {
            return signingInfo.getApkContentsSigners();
        }

        static Signature[] getSigningCertificateHistory(SigningInfo signingInfo) {
            return signingInfo.getSigningCertificateHistory();
        }
    }
}
