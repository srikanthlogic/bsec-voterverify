package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.core.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class ColorStateListInflaterCompat {
    private ColorStateListInflaterCompat() {
    }

    public static ColorStateList inflate(Resources resources, int resId, Resources.Theme theme) {
        try {
            return createFromXml(resources, resources.getXml(resId), theme);
        } catch (Exception e) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", e);
            return null;
        }
    }

    public static ColorStateList createFromXml(Resources r, XmlPullParser parser, Resources.Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type == 2) {
            return createFromXmlInner(r, parser, attrs, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            return inflate(r, parser, attrs, theme);
        }
        throw new XmlPullParserException(parser.getPositionDescription() + ": invalid color state list tag " + name);
    }

    /* JADX INFO: Multiple debug info for r1v3 int[]: [D('colors' int[]), D('innerDepth' int)] */
    private static ColorStateList inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int depth;
        int i;
        int i2 = 1;
        int innerDepth = parser.getDepth() + 1;
        int[][] stateSpecList = new int[20];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;
        while (true) {
            int type = parser.next();
            if (type != i2 && ((depth = parser.getDepth()) >= innerDepth || type != 3)) {
                if (type != 2 || depth > innerDepth || !parser.getName().equals("item")) {
                    innerDepth = innerDepth;
                    i2 = 1;
                } else {
                    TypedArray a2 = obtainAttributes(r, theme, attrs, R.styleable.ColorStateListItem);
                    int baseColor = a2.getColor(R.styleable.ColorStateListItem_android_color, -65281);
                    float alphaMod = 1.0f;
                    if (a2.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                        alphaMod = a2.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0f);
                    } else if (a2.hasValue(R.styleable.ColorStateListItem_alpha)) {
                        alphaMod = a2.getFloat(R.styleable.ColorStateListItem_alpha, 1.0f);
                    }
                    a2.recycle();
                    int numAttrs = attrs.getAttributeCount();
                    int[] stateSpec = new int[numAttrs];
                    int i3 = 0;
                    int j = 0;
                    while (i3 < numAttrs) {
                        int stateResId = attrs.getAttributeNameResource(i3);
                        if (!(stateResId == 16843173 || stateResId == 16843551 || stateResId == R.attr.alpha)) {
                            int j2 = j + 1;
                            if (attrs.getAttributeBooleanValue(i3, false)) {
                                i = stateResId;
                            } else {
                                i = -stateResId;
                            }
                            stateSpec[j] = i;
                            j = j2;
                        }
                        i3++;
                        innerDepth = innerDepth;
                        a2 = a2;
                    }
                    int[] stateSpec2 = StateSet.trimStateSet(stateSpec, j);
                    colorList = GrowingArrayUtils.append(colorList, listSize, modulateColorAlpha(baseColor, alphaMod));
                    stateSpecList = (int[][]) GrowingArrayUtils.append(stateSpecList, listSize, stateSpec2);
                    listSize++;
                    innerDepth = innerDepth;
                    i2 = 1;
                }
            }
        }
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        return new ColorStateList(stateSpecs, colors);
    }

    private static TypedArray obtainAttributes(Resources res, Resources.Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    private static int modulateColorAlpha(int color, float alphaMod) {
        return (16777215 & color) | (Math.round(((float) Color.alpha(color)) * alphaMod) << 24);
    }
}
