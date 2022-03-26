package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: classes.dex */
public abstract class Key {
    static final String ALPHA;
    static final String CUSTOM;
    static final String ELEVATION;
    static final String PIVOT_X;
    static final String PIVOT_Y;
    static final String PROGRESS;
    static final String ROTATION;
    static final String ROTATION_X;
    static final String ROTATION_Y;
    static final String SCALE_X;
    static final String SCALE_Y;
    static final String TRANSITION_PATH_ROTATE;
    static final String TRANSLATION_X;
    static final String TRANSLATION_Y;
    static final String TRANSLATION_Z;
    public static int UNSET = -1;
    static final String WAVE_OFFSET;
    static final String WAVE_PERIOD;
    static final String WAVE_VARIES_BY;
    HashMap<String, ConstraintAttribute> mCustomConstraints;
    int mFramePosition;
    int mTargetId;
    String mTargetString = null;
    protected int mType;

    public abstract void addValues(HashMap<String, SplineSet> hashMap);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void getAttributeNames(HashSet<String> hashSet);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void load(Context context, AttributeSet attributeSet);

    public abstract void setValue(String str, Object obj);

    public Key() {
        int i = UNSET;
        this.mFramePosition = i;
        this.mTargetId = i;
    }

    public boolean matches(String constraintTag) {
        String str = this.mTargetString;
        if (str == null || constraintTag == null) {
            return false;
        }
        return constraintTag.matches(str);
    }

    float toFloat(Object value) {
        return value instanceof Float ? ((Float) value).floatValue() : Float.parseFloat(value.toString());
    }

    int toInt(Object value) {
        return value instanceof Integer ? ((Integer) value).intValue() : Integer.parseInt(value.toString());
    }

    boolean toBoolean(Object value) {
        return value instanceof Boolean ? ((Boolean) value).booleanValue() : Boolean.parseBoolean(value.toString());
    }

    public void setInterpolation(HashMap<String, Integer> interpolation) {
    }
}
