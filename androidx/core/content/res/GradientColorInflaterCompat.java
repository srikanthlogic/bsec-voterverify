package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Xml;
import androidx.core.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    private GradientColorInflaterCompat() {
    }

    static Shader createFromXml(Resources resources, XmlPullParser parser, Resources.Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type == 2) {
            return createFromXmlInner(resources, parser, attrs, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Shader createFromXmlInner(Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
        String name = parser.getName();
        if (name.equals("gradient")) {
            TypedArray a2 = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.GradientColor);
            float startX = TypedArrayUtils.getNamedFloat(a2, parser, "startX", R.styleable.GradientColor_android_startX, 0.0f);
            float startY = TypedArrayUtils.getNamedFloat(a2, parser, "startY", R.styleable.GradientColor_android_startY, 0.0f);
            float endX = TypedArrayUtils.getNamedFloat(a2, parser, "endX", R.styleable.GradientColor_android_endX, 0.0f);
            float endY = TypedArrayUtils.getNamedFloat(a2, parser, "endY", R.styleable.GradientColor_android_endY, 0.0f);
            float centerX = TypedArrayUtils.getNamedFloat(a2, parser, "centerX", R.styleable.GradientColor_android_centerX, 0.0f);
            float centerY = TypedArrayUtils.getNamedFloat(a2, parser, "centerY", R.styleable.GradientColor_android_centerY, 0.0f);
            int type = TypedArrayUtils.getNamedInt(a2, parser, "type", R.styleable.GradientColor_android_type, 0);
            int startColor = TypedArrayUtils.getNamedColor(a2, parser, "startColor", R.styleable.GradientColor_android_startColor, 0);
            boolean hasCenterColor = TypedArrayUtils.hasAttribute(parser, "centerColor");
            int centerColor = TypedArrayUtils.getNamedColor(a2, parser, "centerColor", R.styleable.GradientColor_android_centerColor, 0);
            int endColor = TypedArrayUtils.getNamedColor(a2, parser, "endColor", R.styleable.GradientColor_android_endColor, 0);
            int tileMode = TypedArrayUtils.getNamedInt(a2, parser, "tileMode", R.styleable.GradientColor_android_tileMode, 0);
            float gradientRadius = TypedArrayUtils.getNamedFloat(a2, parser, "gradientRadius", R.styleable.GradientColor_android_gradientRadius, 0.0f);
            a2.recycle();
            ColorStops colorStops = checkColors(inflateChildElements(resources, parser, attrs, theme), startColor, endColor, hasCenterColor, centerColor);
            if (type != 1) {
                if (type != 2) {
                    return new LinearGradient(startX, startY, endX, endY, colorStops.mColors, colorStops.mOffsets, parseTileMode(tileMode));
                }
                return new SweepGradient(centerX, centerY, colorStops.mColors, colorStops.mOffsets);
            } else if (gradientRadius > 0.0f) {
                return new RadialGradient(centerX, centerY, gradientRadius, colorStops.mColors, colorStops.mOffsets, parseTileMode(tileMode));
            } else {
                throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
            }
        } else {
            throw new XmlPullParserException(parser.getPositionDescription() + ": invalid gradient color tag " + name);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0087, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r13.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' attribute!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static ColorStops inflateChildElements(Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int depth;
        int innerDepth = parser.getDepth() + 1;
        List<Float> offsets = new ArrayList<>(20);
        List<Integer> colors = new ArrayList<>(20);
        while (true) {
            int type = parser.next();
            if (type == 1 || ((depth = parser.getDepth()) < innerDepth && type == 3)) {
                break;
            } else if (type == 2 && depth <= innerDepth && parser.getName().equals("item")) {
                TypedArray a2 = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.GradientColorItem);
                boolean hasColor = a2.hasValue(R.styleable.GradientColorItem_android_color);
                boolean hasOffset = a2.hasValue(R.styleable.GradientColorItem_android_offset);
                if (!hasColor || !hasOffset) {
                    break;
                }
                int color = a2.getColor(R.styleable.GradientColorItem_android_color, 0);
                float offset = a2.getFloat(R.styleable.GradientColorItem_android_offset, 0.0f);
                a2.recycle();
                colors.add(Integer.valueOf(color));
                offsets.add(Float.valueOf(offset));
            }
        }
        if (colors.size() > 0) {
            return new ColorStops(colors, offsets);
        }
        return null;
    }

    private static ColorStops checkColors(ColorStops colorItems, int startColor, int endColor, boolean hasCenterColor, int centerColor) {
        if (colorItems != null) {
            return colorItems;
        }
        if (hasCenterColor) {
            return new ColorStops(startColor, centerColor, endColor);
        }
        return new ColorStops(startColor, endColor);
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        if (tileMode == 1) {
            return Shader.TileMode.REPEAT;
        }
        if (tileMode != 2) {
            return Shader.TileMode.CLAMP;
        }
        return Shader.TileMode.MIRROR;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(List<Integer> colorsList, List<Float> offsetsList) {
            int size = colorsList.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = colorsList.get(i).intValue();
                this.mOffsets[i] = offsetsList.get(i).floatValue();
            }
        }

        ColorStops(int startColor, int endColor) {
            this.mColors = new int[]{startColor, endColor};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(int startColor, int centerColor, int endColor) {
            this.mColors = new int[]{startColor, centerColor, endColor};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }
    }
}
