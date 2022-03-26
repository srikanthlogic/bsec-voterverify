package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: classes.dex */
public class TypedArrayUtils {
    private static final String NAMESPACE;

    public static boolean hasAttribute(XmlPullParser parser, String attrName) {
        return parser.getAttributeValue(NAMESPACE, attrName) != null;
    }

    public static float getNamedFloat(TypedArray a2, XmlPullParser parser, String attrName, int resId, float defaultValue) {
        if (!hasAttribute(parser, attrName)) {
            return defaultValue;
        }
        return a2.getFloat(resId, defaultValue);
    }

    public static boolean getNamedBoolean(TypedArray a2, XmlPullParser parser, String attrName, int resId, boolean defaultValue) {
        if (!hasAttribute(parser, attrName)) {
            return defaultValue;
        }
        return a2.getBoolean(resId, defaultValue);
    }

    public static int getNamedInt(TypedArray a2, XmlPullParser parser, String attrName, int resId, int defaultValue) {
        if (!hasAttribute(parser, attrName)) {
            return defaultValue;
        }
        return a2.getInt(resId, defaultValue);
    }

    public static int getNamedColor(TypedArray a2, XmlPullParser parser, String attrName, int resId, int defaultValue) {
        if (!hasAttribute(parser, attrName)) {
            return defaultValue;
        }
        return a2.getColor(resId, defaultValue);
    }

    public static ComplexColorCompat getNamedComplexColor(TypedArray a2, XmlPullParser parser, Resources.Theme theme, String attrName, int resId, int defaultValue) {
        if (hasAttribute(parser, attrName)) {
            TypedValue value = new TypedValue();
            a2.getValue(resId, value);
            if (value.type >= 28 && value.type <= 31) {
                return ComplexColorCompat.from(value.data);
            }
            ComplexColorCompat complexColor = ComplexColorCompat.inflate(a2.getResources(), a2.getResourceId(resId, 0), theme);
            if (complexColor != null) {
                return complexColor;
            }
        }
        return ComplexColorCompat.from(defaultValue);
    }

    public static ColorStateList getNamedColorStateList(TypedArray a2, XmlPullParser parser, Resources.Theme theme, String attrName, int resId) {
        if (!hasAttribute(parser, attrName)) {
            return null;
        }
        TypedValue value = new TypedValue();
        a2.getValue(resId, value);
        if (value.type == 2) {
            throw new UnsupportedOperationException("Failed to resolve attribute at index " + resId + ": " + value);
        } else if (value.type < 28 || value.type > 31) {
            return ColorStateListInflaterCompat.inflate(a2.getResources(), a2.getResourceId(resId, 0), theme);
        } else {
            return getNamedColorStateListFromInt(value);
        }
    }

    private static ColorStateList getNamedColorStateListFromInt(TypedValue value) {
        return ColorStateList.valueOf(value.data);
    }

    public static int getNamedResourceId(TypedArray a2, XmlPullParser parser, String attrName, int resId, int defaultValue) {
        if (!hasAttribute(parser, attrName)) {
            return defaultValue;
        }
        return a2.getResourceId(resId, defaultValue);
    }

    public static String getNamedString(TypedArray a2, XmlPullParser parser, String attrName, int resId) {
        if (!hasAttribute(parser, attrName)) {
            return null;
        }
        return a2.getString(resId);
    }

    public static TypedValue peekNamedValue(TypedArray a2, XmlPullParser parser, String attrName, int resId) {
        if (!hasAttribute(parser, attrName)) {
            return null;
        }
        return a2.peekValue(resId);
    }

    public static TypedArray obtainAttributes(Resources res, Resources.Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    public static boolean getBoolean(TypedArray a2, int index, int fallbackIndex, boolean defaultValue) {
        return a2.getBoolean(index, a2.getBoolean(fallbackIndex, defaultValue));
    }

    public static Drawable getDrawable(TypedArray a2, int index, int fallbackIndex) {
        Drawable val = a2.getDrawable(index);
        if (val == null) {
            return a2.getDrawable(fallbackIndex);
        }
        return val;
    }

    public static int getInt(TypedArray a2, int index, int fallbackIndex, int defaultValue) {
        return a2.getInt(index, a2.getInt(fallbackIndex, defaultValue));
    }

    public static int getResourceId(TypedArray a2, int index, int fallbackIndex, int defaultValue) {
        return a2.getResourceId(index, a2.getResourceId(fallbackIndex, defaultValue));
    }

    public static String getString(TypedArray a2, int index, int fallbackIndex) {
        String val = a2.getString(index);
        if (val == null) {
            return a2.getString(fallbackIndex);
        }
        return val;
    }

    public static CharSequence getText(TypedArray a2, int index, int fallbackIndex) {
        CharSequence val = a2.getText(index);
        if (val == null) {
            return a2.getText(fallbackIndex);
        }
        return val;
    }

    public static CharSequence[] getTextArray(TypedArray a2, int index, int fallbackIndex) {
        CharSequence[] val = a2.getTextArray(index);
        if (val == null) {
            return a2.getTextArray(fallbackIndex);
        }
        return val;
    }

    public static int getAttr(Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        if (value.resourceId != 0) {
            return attr;
        }
        return fallbackAttr;
    }

    private TypedArrayUtils() {
    }
}
