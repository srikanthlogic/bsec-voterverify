package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import com.facebook.common.statfs.StatFsHelper;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fonts, int style) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromInputStream(Context context, InputStream is) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x006a, code lost:
        if (r5 != null) goto L_0x006d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006c, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0071, code lost:
        if ((r15 & 1) == 0) goto L_0x0076;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0073, code lost:
        r3 = 700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0076, code lost:
        r3 = com.facebook.common.statfs.StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x007a, code lost:
        if ((r15 & 2) == 0) goto L_0x007f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007c, code lost:
        r4 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0093, code lost:
        return new android.graphics.Typeface.CustomFallbackBuilder(r5.build()).setStyle(new android.graphics.fonts.FontStyle(r3, r4)).build();
     */
    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    /* Code decompiled incorrectly, please refer to instructions dump */
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fonts, int style) {
        int length;
        FontFamily.Builder familyBuilder;
        int i;
        ContentResolver resolver = context.getContentResolver();
        try {
            length = fonts.length;
            int i2 = 0;
            familyBuilder = null;
            i = 0;
        } catch (Exception e) {
        }
        while (true) {
            int i3 = 1;
            if (i >= length) {
                break;
            }
            try {
                FontsContractCompat.FontInfo font = fonts[i];
                try {
                    ParcelFileDescriptor pfd = resolver.openFileDescriptor(font.getUri(), "r", cancellationSignal);
                    if (pfd != null) {
                        try {
                            Font.Builder weight = new Font.Builder(pfd).setWeight(font.getWeight());
                            if (!font.isItalic()) {
                                i3 = 0;
                            }
                            Font platformFont = weight.setSlant(i3).setTtcIndex(font.getTtcIndex()).build();
                            if (familyBuilder == null) {
                                familyBuilder = new FontFamily.Builder(platformFont);
                            } else {
                                familyBuilder.addFont(platformFont);
                            }
                            if (pfd != null) {
                                pfd.close();
                            }
                        } catch (Throwable th) {
                            if (pfd != null) {
                                try {
                                    pfd.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                            break;
                        }
                    } else if (pfd != null) {
                        pfd.close();
                    }
                } catch (IOException e2) {
                }
                i++;
            } catch (Exception e3) {
            }
            return null;
        }
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry familyEntry, Resources resources, int style) {
        int i;
        try {
            FontResourcesParserCompat.FontFileResourceEntry[] entries = familyEntry.getEntries();
            int length = entries.length;
            int i2 = 0;
            FontFamily.Builder familyBuilder = null;
            int i3 = 0;
            while (true) {
                int i4 = 1;
                if (i3 >= length) {
                    break;
                }
                FontResourcesParserCompat.FontFileResourceEntry entry = entries[i3];
                try {
                    Font.Builder weight = new Font.Builder(resources, entry.getResourceId()).setWeight(entry.getWeight());
                    if (!entry.isItalic()) {
                        i4 = 0;
                    }
                    Font platformFont = weight.setSlant(i4).setTtcIndex(entry.getTtcIndex()).setFontVariationSettings(entry.getVariationSettings()).build();
                    if (familyBuilder == null) {
                        familyBuilder = new FontFamily.Builder(platformFont);
                    } else {
                        familyBuilder.addFont(platformFont);
                    }
                } catch (IOException e) {
                }
                i3++;
            }
            if (familyBuilder == null) {
                return null;
            }
            if ((style & 1) != 0) {
                i = 700;
            } else {
                i = StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
            }
            if ((style & 2) != 0) {
                i2 = 1;
            }
            return new Typeface.CustomFallbackBuilder(familyBuilder.build()).setStyle(new FontStyle(i, i2)).build();
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // androidx.core.graphics.TypefaceCompatBaseImpl
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int id, String path, int style) {
        try {
            Font font = new Font.Builder(resources, id).build();
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(font).build()).setStyle(font.getStyle()).build();
        } catch (Exception e) {
            return null;
        }
    }
}
