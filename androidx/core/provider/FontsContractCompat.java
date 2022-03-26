package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Map;
/* loaded from: classes.dex */
public class FontsContractCompat {
    @Deprecated
    public static final String PARCEL_FONT_RESULTS;
    @Deprecated
    static final int RESULT_CODE_PROVIDER_NOT_FOUND;
    @Deprecated
    static final int RESULT_CODE_WRONG_CERTIFICATES;

    /* loaded from: classes.dex */
    public static final class Columns implements BaseColumns {
        public static final String FILE_ID;
        public static final String ITALIC;
        public static final String RESULT_CODE;
        public static final int RESULT_CODE_FONT_NOT_FOUND;
        public static final int RESULT_CODE_FONT_UNAVAILABLE;
        public static final int RESULT_CODE_MALFORMED_QUERY;
        public static final int RESULT_CODE_OK;
        public static final String TTC_INDEX;
        public static final String VARIATION_SETTINGS;
        public static final String WEIGHT;
    }

    private FontsContractCompat() {
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fonts) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fonts, 0);
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest request) throws PackageManager.NameNotFoundException {
        return FontProvider.getFontFamilyResult(context, request, cancellationSignal);
    }

    public static void requestFont(Context context, FontRequest request, FontRequestCallback callback, Handler handler) {
        CallbackWithHandler callbackWrapper = new CallbackWithHandler(callback);
        FontRequestWorker.requestFontAsync(context.getApplicationContext(), request, 0, RequestExecutor.createHandlerExecutor(handler), callbackWrapper);
    }

    public static Typeface requestFont(Context context, FontRequest request, int style, boolean isBlockingFetch, int timeout, Handler handler, FontRequestCallback callback) {
        CallbackWithHandler callbackWrapper = new CallbackWithHandler(callback, handler);
        if (isBlockingFetch) {
            return FontRequestWorker.requestFontSync(context, request, callbackWrapper, style, timeout);
        }
        return FontRequestWorker.requestFontAsync(context, request, style, null, callbackWrapper);
    }

    public static void resetTypefaceCache() {
        FontRequestWorker.resetTypefaceCache();
    }

    /* loaded from: classes.dex */
    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        @Deprecated
        public FontInfo(Uri uri, int ttcIndex, int weight, boolean italic, int resultCode) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = ttcIndex;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mResultCode = resultCode;
        }

        public static FontInfo create(Uri uri, int ttcIndex, int weight, boolean italic, int resultCode) {
            return new FontInfo(uri, ttcIndex, weight, italic, resultCode);
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    /* loaded from: classes.dex */
    public static class FontFamilyResult {
        public static final int STATUS_OK;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED;
        public static final int STATUS_WRONG_CERTIFICATES;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @Deprecated
        public FontFamilyResult(int statusCode, FontInfo[] fonts) {
            this.mStatusCode = statusCode;
            this.mFonts = fonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public static FontFamilyResult create(int statusCode, FontInfo[] fonts) {
            return new FontFamilyResult(statusCode, fonts);
        }
    }

    /* loaded from: classes.dex */
    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR;
        public static final int FAIL_REASON_FONT_NOT_FOUND;
        public static final int FAIL_REASON_FONT_UNAVAILABLE;
        public static final int FAIL_REASON_MALFORMED_QUERY;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND;
        public static final int FAIL_REASON_SECURITY_VIOLATION;
        public static final int FAIL_REASON_WRONG_CERTIFICATES;
        @Deprecated
        public static final int RESULT_OK;
        static final int RESULT_SUCCESS;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface FontRequestFailReason {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        public void onTypefaceRequestFailed(int reason) {
        }
    }

    @Deprecated
    public static Typeface getFontSync(Context context, FontRequest request, ResourcesCompat.FontCallback fontCallback, Handler handler, boolean isBlockingFetch, int timeout, int style) {
        return requestFont(context, request, style, isBlockingFetch, timeout, ResourcesCompat.FontCallback.getHandler(handler), new TypefaceCompat.ResourcesCallbackAdapter(fontCallback));
    }

    @Deprecated
    public static void resetCache() {
        FontRequestWorker.resetTypefaceCache();
    }

    @Deprecated
    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] fonts, CancellationSignal cancellationSignal) {
        return TypefaceCompatUtil.readFontInfoIntoByteBuffer(context, fonts, cancellationSignal);
    }

    @Deprecated
    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest request, Resources resources) throws PackageManager.NameNotFoundException {
        return FontProvider.getProvider(packageManager, request, resources);
    }
}
