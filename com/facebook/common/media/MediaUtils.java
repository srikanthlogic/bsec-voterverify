package com.facebook.common.media;

import com.facebook.common.internal.ImmutableMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class MediaUtils {
    public static final Map<String, String> ADDITIONAL_ALLOWED_MIME_TYPES = ImmutableMap.of("mkv", "video/x-matroska", "glb", "model/gltf-binary");

    public static boolean isPhoto(@Nullable String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }

    public static boolean isVideo(@Nullable String mimeType) {
        return mimeType != null && mimeType.startsWith("video/");
    }

    public static boolean isThreeD(@Nullable String mimeType) {
        return mimeType != null && mimeType.equals("model/gltf-binary");
    }

    @Nullable
    public static String extractMime(String path) {
        String extension = extractExtension(path);
        if (extension == null) {
            return null;
        }
        String extension2 = extension.toLowerCase(Locale.US);
        String mimeType = MimeTypeMapWrapper.getMimeTypeFromExtension(extension2);
        if (mimeType == null) {
            return ADDITIONAL_ALLOWED_MIME_TYPES.get(extension2);
        }
        return mimeType;
    }

    @Nullable
    private static String extractExtension(String path) {
        int pos = path.lastIndexOf(46);
        if (pos < 0 || pos == path.length() - 1) {
            return null;
        }
        return path.substring(pos + 1);
    }

    public static boolean isNonNativeSupportedMimeType(String mimeType) {
        return ADDITIONAL_ALLOWED_MIME_TYPES.containsValue(mimeType);
    }
}