package androidx.core.provider;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import com.example.aadhaarfpoffline.tatvik.database.DBHelper;
import com.facebook.common.statfs.StatFsHelper;
import com.facebook.common.util.UriUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
class FontProvider {
    private static final Comparator<byte[]> sByteArrayComparator = new Comparator<byte[]>() { // from class: androidx.core.provider.FontProvider.1
        public int compare(byte[] l, byte[] r) {
            if (l.length != r.length) {
                return l.length - r.length;
            }
            for (int i = 0; i < l.length; i++) {
                if (l[i] != r[i]) {
                    return l[i] - r[i];
                }
            }
            return 0;
        }
    };

    private FontProvider() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FontsContractCompat.FontFamilyResult getFontFamilyResult(Context context, FontRequest request, CancellationSignal cancellationSignal) throws PackageManager.NameNotFoundException {
        ProviderInfo providerInfo = getProvider(context.getPackageManager(), request, context.getResources());
        if (providerInfo == null) {
            return FontsContractCompat.FontFamilyResult.create(1, null);
        }
        return FontsContractCompat.FontFamilyResult.create(0, query(context, request, providerInfo.authority, cancellationSignal));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest request, Resources resources) throws PackageManager.NameNotFoundException {
        String providerAuthority = request.getProviderAuthority();
        ProviderInfo info = packageManager.resolveContentProvider(providerAuthority, 0);
        if (info == null) {
            throw new PackageManager.NameNotFoundException("No package found for authority: " + providerAuthority);
        } else if (info.packageName.equals(request.getProviderPackage())) {
            List<byte[]> signatures = convertToByteArrayList(packageManager.getPackageInfo(info.packageName, 64).signatures);
            Collections.sort(signatures, sByteArrayComparator);
            List<List<byte[]>> requestCertificatesList = getCertificates(request, resources);
            for (int i = 0; i < requestCertificatesList.size(); i++) {
                List<byte[]> requestSignatures = new ArrayList<>(requestCertificatesList.get(i));
                Collections.sort(requestSignatures, sByteArrayComparator);
                if (equalsByteArrayList(signatures, requestSignatures)) {
                    return info;
                }
            }
            return null;
        } else {
            throw new PackageManager.NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + request.getProviderPackage());
        }
    }

    static FontsContractCompat.FontInfo[] query(Context context, FontRequest request, String authority, CancellationSignal cancellationSignal) {
        int i;
        int resultCode;
        int idColumnIndex;
        Uri fileUri;
        ArrayList<FontsContractCompat.FontInfo> result = new ArrayList<>();
        Uri uri = new Uri.Builder().scheme("content").authority(authority).build();
        Uri fileBaseUri = new Uri.Builder().scheme("content").authority(authority).appendPath(UriUtil.LOCAL_FILE_SCHEME).build();
        Cursor cursor = null;
        try {
            String[] projection = {DBHelper.Key_ID, FontsContractCompat.Columns.FILE_ID, FontsContractCompat.Columns.TTC_INDEX, FontsContractCompat.Columns.VARIATION_SETTINGS, FontsContractCompat.Columns.WEIGHT, FontsContractCompat.Columns.ITALIC, FontsContractCompat.Columns.RESULT_CODE};
            boolean z = true;
            if (Build.VERSION.SDK_INT > 16) {
                cursor = context.getContentResolver().query(uri, projection, "query = ?", new String[]{request.getQuery()}, null, cancellationSignal);
                i = 0;
            } else {
                i = 0;
                cursor = context.getContentResolver().query(uri, projection, "query = ?", new String[]{request.getQuery()}, null);
            }
            if (cursor != null && cursor.getCount() > 0) {
                int resultCodeColumnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.RESULT_CODE);
                result = new ArrayList<>();
                int weight = cursor.getColumnIndex(DBHelper.Key_ID);
                int fileIdColumnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.FILE_ID);
                int ttcIndexColumnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.TTC_INDEX);
                int weightColumnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.WEIGHT);
                int italicColumnIndex = cursor.getColumnIndex(FontsContractCompat.Columns.ITALIC);
                while (cursor.moveToNext()) {
                    if (resultCodeColumnIndex != -1) {
                        resultCode = cursor.getInt(resultCodeColumnIndex);
                    } else {
                        resultCode = i;
                    }
                    int ttcIndex = ttcIndexColumnIndex != -1 ? cursor.getInt(ttcIndexColumnIndex) : i;
                    if (fileIdColumnIndex == -1) {
                        idColumnIndex = weight;
                        fileUri = ContentUris.withAppendedId(uri, cursor.getLong(weight));
                    } else {
                        idColumnIndex = weight;
                        fileUri = ContentUris.withAppendedId(fileBaseUri, cursor.getLong(fileIdColumnIndex));
                    }
                    result.add(FontsContractCompat.FontInfo.create(fileUri, ttcIndex, weightColumnIndex != -1 ? cursor.getInt(weightColumnIndex) : StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, (italicColumnIndex == -1 || cursor.getInt(italicColumnIndex) != z) ? false : z, resultCode));
                    resultCodeColumnIndex = resultCodeColumnIndex;
                    weight = idColumnIndex;
                    i = 0;
                    z = true;
                }
            }
            return (FontsContractCompat.FontInfo[]) result.toArray(new FontsContractCompat.FontInfo[0]);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static List<List<byte[]>> getCertificates(FontRequest request, Resources resources) {
        if (request.getCertificates() != null) {
            return request.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, request.getCertificatesArrayResId());
    }

    private static boolean equalsByteArrayList(List<byte[]> signatures, List<byte[]> requestSignatures) {
        if (signatures.size() != requestSignatures.size()) {
            return false;
        }
        for (int i = 0; i < signatures.size(); i++) {
            if (!Arrays.equals(signatures.get(i), requestSignatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatures) {
        List<byte[]> shaList = new ArrayList<>();
        for (Signature signature : signatures) {
            shaList.add(signature.toByteArray());
        }
        return shaList;
    }
}
