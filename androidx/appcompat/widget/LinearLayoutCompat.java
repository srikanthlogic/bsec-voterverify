package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class LinearLayoutCompat extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME;
    public static final int HORIZONTAL;
    private static final int INDEX_BOTTOM;
    private static final int INDEX_CENTER_VERTICAL;
    private static final int INDEX_FILL;
    private static final int INDEX_TOP;
    public static final int SHOW_DIVIDER_BEGINNING;
    public static final int SHOW_DIVIDER_END;
    public static final int SHOW_DIVIDER_MIDDLE;
    public static final int SHOW_DIVIDER_NONE;
    public static final int VERTICAL;
    private static final int VERTICAL_GRAVITY_COUNT;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface DividerMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface OrientationMode {
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray a2 = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.LinearLayoutCompat, defStyleAttr, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.LinearLayoutCompat, attrs, a2.getWrappedTypeArray(), defStyleAttr, 0);
        int index = a2.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }
        int index2 = a2.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (index2 >= 0) {
            setGravity(index2);
        }
        boolean baselineAligned = a2.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!baselineAligned) {
            setBaselineAligned(baselineAligned);
        }
        this.mWeightSum = a2.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = a2.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = a2.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(a2.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = a2.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = a2.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        a2.recycle();
    }

    public void setShowDividers(int showDividers) {
        if (showDividers != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = showDividers;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable divider) {
        if (divider != this.mDivider) {
            this.mDivider = divider;
            boolean z = false;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (divider == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int padding) {
        this.mDividerPadding = padding;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int bottom;
        int count = getVirtualChildCount();
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, (child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            View child2 = getVirtualChildAt(count - 1);
            if (child2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child2.getBottom() + ((LayoutParams) child2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawDividersHorizontal(Canvas canvas) {
        int position;
        int position2;
        int count = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isLayoutRtl) {
                    position2 = child.getRight() + lp.rightMargin;
                } else {
                    position2 = (child.getLeft() - lp.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, position2);
            }
        }
        if (hasDividerBeforeChildAt(count)) {
            View child2 = getVirtualChildAt(count - 1);
            if (child2 != null) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (isLayoutRtl) {
                    position = (child2.getLeft() - lp2.leftMargin) - this.mDividerWidth;
                } else {
                    position = child2.getRight() + lp2.rightMargin;
                }
            } else if (isLayoutRtl) {
                position = getPaddingLeft();
            } else {
                position = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, position);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, top, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + top);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean baselineAligned) {
        this.mBaselineAligned = baselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        this.mUseLargestChild = enabled;
    }

    @Override // android.view.View
    public int getBaseline() {
        int majorGravity;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View child = getChildAt(i);
            int childBaseline = child.getBaseline();
            if (childBaseline != -1) {
                int childTop = this.mBaselineChildTop;
                if (this.mOrientation == 1 && (majorGravity = this.mGravity & 112) != 48) {
                    if (majorGravity == 16) {
                        childTop += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                    } else if (majorGravity == 80) {
                        childTop = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                    }
                }
                return ((LayoutParams) child.getLayoutParams()).topMargin + childTop + childBaseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float weightSum) {
        this.mWeightSum = Math.max(0.0f, weightSum);
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 1) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    protected boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (childIndex == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        }
        if ((this.mShowDividers & 2) == 0) {
            return false;
        }
        for (int i = childIndex - 1; i >= 0; i--) {
            if (getChildAt(i).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Multiple debug info for r11v2 int: [D('count' int), D('weightedMaxWidth' int)] */
    /* JADX INFO: Multiple debug info for r13v15 'largestChildHeight'  int: [D('largestChildHeight' int), D('heightMode' int)] */
    /* JADX INFO: Multiple debug info for r13v2 int: [D('largestChildHeight' int), D('heightMode' int)] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x045d  */
    /* JADX WARN: Removed duplicated region for block: B:191:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int heightMode;
        int maxWidth;
        int alternativeMaxWidth;
        int largestChildHeight;
        int maxWidth2;
        int delta;
        float totalWeight;
        int heightMode2;
        int baselineChildIndex;
        float weightSum;
        int delta2;
        int maxWidth3;
        boolean allFillParent;
        int delta3;
        int maxWidth4;
        int delta4;
        float totalWeight2;
        int i;
        int i2;
        int heightMode3;
        int weightedMaxWidth;
        int alternativeMaxWidth2;
        int maxWidth5;
        int weightedMaxWidth2;
        int heightMode4;
        int childState;
        LayoutParams lp;
        View child;
        int weightedMaxWidth3;
        int alternativeMaxWidth3;
        int oldHeight;
        this.mTotalLength = 0;
        int margin = 0;
        float totalWeight3 = 0.0f;
        int count2 = getVirtualChildCount();
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode5 = View.MeasureSpec.getMode(heightMeasureSpec);
        int baselineChildIndex2 = this.mBaselineAlignedChildIndex;
        boolean useLargestChild = this.mUseLargestChild;
        int i3 = 0;
        boolean matchWidth = false;
        int alternativeMaxWidth4 = 0;
        int measuredWidth = 0;
        int maxWidth6 = 0;
        int weightedMaxWidth4 = 0;
        boolean skippedMeasure = false;
        boolean allFillParent2 = true;
        while (i3 < count2) {
            View child2 = getVirtualChildAt(i3);
            if (child2 == null) {
                this.mTotalLength += measureNullChild(i3);
                heightMode3 = heightMode5;
                maxWidth6 = maxWidth6;
                weightedMaxWidth = count2;
            } else if (child2.getVisibility() == 8) {
                i3 += getChildrenSkipCount(child2, i3);
                heightMode3 = heightMode5;
                maxWidth6 = maxWidth6;
                margin = margin;
                weightedMaxWidth = count2;
            } else {
                if (hasDividerBeforeChildAt(i3)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                float totalWeight4 = totalWeight3 + lp2.weight;
                if (heightMode5 == 1073741824 && lp2.height == 0 && lp2.weight > 0.0f) {
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, lp2.topMargin + totalLength + lp2.bottomMargin);
                    skippedMeasure = true;
                    lp = lp2;
                    alternativeMaxWidth2 = alternativeMaxWidth4;
                    heightMode3 = heightMode5;
                    weightedMaxWidth2 = maxWidth6;
                    childState = margin;
                    maxWidth5 = measuredWidth;
                    heightMode4 = weightedMaxWidth4;
                    child = child2;
                    weightedMaxWidth = count2;
                } else {
                    if (lp2.height != 0 || lp2.weight <= 0.0f) {
                        oldHeight = Integer.MIN_VALUE;
                    } else {
                        lp2.height = -2;
                        oldHeight = 0;
                    }
                    lp = lp2;
                    childState = margin;
                    maxWidth5 = measuredWidth;
                    heightMode3 = heightMode5;
                    heightMode4 = weightedMaxWidth4;
                    weightedMaxWidth2 = maxWidth6;
                    weightedMaxWidth = count2;
                    alternativeMaxWidth2 = alternativeMaxWidth4;
                    measureChildBeforeLayout(child2, i3, widthMeasureSpec, 0, heightMeasureSpec, totalWeight4 == 0.0f ? this.mTotalLength : 0);
                    if (oldHeight != Integer.MIN_VALUE) {
                        lp.height = oldHeight;
                    }
                    int childHeight = child2.getMeasuredHeight();
                    int totalLength2 = this.mTotalLength;
                    child = child2;
                    this.mTotalLength = Math.max(totalLength2, totalLength2 + childHeight + lp.topMargin + lp.bottomMargin + getNextLocationOffset(child));
                    if (useLargestChild) {
                        heightMode4 = Math.max(childHeight, heightMode4);
                    }
                }
                if (baselineChildIndex2 >= 0 && baselineChildIndex2 == i3 + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i3 >= baselineChildIndex2 || lp.weight <= 0.0f) {
                    boolean matchWidthLocally = false;
                    if (widthMode != 1073741824 && lp.width == -1) {
                        matchWidth = true;
                        matchWidthLocally = true;
                    }
                    int margin2 = lp.leftMargin + lp.rightMargin;
                    int measuredWidth2 = child.getMeasuredWidth() + margin2;
                    int maxWidth7 = Math.max(maxWidth5, measuredWidth2);
                    int childState2 = View.combineMeasuredStates(childState, child.getMeasuredState());
                    boolean allFillParent3 = allFillParent2 && lp.width == -1;
                    if (lp.weight > 0.0f) {
                        weightedMaxWidth3 = Math.max(weightedMaxWidth2, matchWidthLocally ? margin2 : measuredWidth2);
                        alternativeMaxWidth3 = alternativeMaxWidth2;
                    } else {
                        alternativeMaxWidth3 = Math.max(alternativeMaxWidth2, matchWidthLocally ? margin2 : measuredWidth2);
                        weightedMaxWidth3 = weightedMaxWidth2;
                    }
                    i3 += getChildrenSkipCount(child, i3);
                    measuredWidth = maxWidth7;
                    margin = childState2;
                    allFillParent2 = allFillParent3;
                    maxWidth6 = weightedMaxWidth3;
                    weightedMaxWidth4 = heightMode4;
                    totalWeight3 = totalWeight4;
                    alternativeMaxWidth4 = alternativeMaxWidth3;
                } else {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
            }
            i3++;
            count2 = weightedMaxWidth;
            heightMode5 = heightMode3;
        }
        int childState3 = margin;
        int maxWidth8 = measuredWidth;
        int maxWidth9 = 8;
        int largestChildHeight2 = weightedMaxWidth4;
        int weightedMaxWidth5 = maxWidth6;
        if (this.mTotalLength > 0) {
            count = count2;
            if (hasDividerBeforeChildAt(count)) {
                this.mTotalLength += this.mDividerHeight;
            }
        } else {
            count = count2;
        }
        if (useLargestChild) {
            heightMode = heightMode5;
            if (heightMode == Integer.MIN_VALUE || heightMode == 0) {
                this.mTotalLength = 0;
                int i4 = 0;
                while (i4 < count) {
                    View child3 = getVirtualChildAt(i4);
                    if (child3 == null) {
                        this.mTotalLength += measureNullChild(i4);
                        i2 = i4;
                    } else if (child3.getVisibility() == maxWidth9) {
                        i = i4 + getChildrenSkipCount(child3, i4);
                        i4 = i + 1;
                        maxWidth9 = 8;
                    } else {
                        LayoutParams lp3 = (LayoutParams) child3.getLayoutParams();
                        int totalLength3 = this.mTotalLength;
                        i2 = i4;
                        this.mTotalLength = Math.max(totalLength3, totalLength3 + largestChildHeight2 + lp3.topMargin + lp3.bottomMargin + getNextLocationOffset(child3));
                    }
                    i = i2;
                    i4 = i + 1;
                    maxWidth9 = 8;
                }
            }
        } else {
            heightMode = heightMode5;
        }
        this.mTotalLength += getPaddingTop() + getPaddingBottom();
        int heightSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), heightMeasureSpec, 0);
        int heightSize = heightSizeAndState & ViewCompat.MEASURED_SIZE_MASK;
        int delta5 = heightSize - this.mTotalLength;
        if (skippedMeasure) {
            maxWidth2 = maxWidth8;
            totalWeight = totalWeight3;
            delta = delta5;
        } else if (delta5 == 0 || totalWeight3 <= 0.0f) {
            int alternativeMaxWidth5 = Math.max(alternativeMaxWidth4, weightedMaxWidth5);
            if (useLargestChild) {
                alternativeMaxWidth = alternativeMaxWidth5;
                if (heightMode != 1073741824) {
                    int i5 = 0;
                    while (i5 < count) {
                        View child4 = getVirtualChildAt(i5);
                        if (child4 != null) {
                            maxWidth4 = maxWidth8;
                            totalWeight2 = totalWeight3;
                            if (child4.getVisibility() == 8) {
                                delta4 = delta5;
                            } else if (((LayoutParams) child4.getLayoutParams()).weight > 0.0f) {
                                delta4 = delta5;
                                child4.measure(View.MeasureSpec.makeMeasureSpec(child4.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(largestChildHeight2, 1073741824));
                            } else {
                                delta4 = delta5;
                            }
                        } else {
                            maxWidth4 = maxWidth8;
                            totalWeight2 = totalWeight3;
                            delta4 = delta5;
                        }
                        i5++;
                        heightSize = heightSize;
                        totalWeight3 = totalWeight2;
                        delta5 = delta4;
                        maxWidth8 = maxWidth4;
                    }
                    maxWidth = maxWidth8;
                    delta3 = delta5;
                } else {
                    maxWidth = maxWidth8;
                    delta3 = delta5;
                }
            } else {
                alternativeMaxWidth = alternativeMaxWidth5;
                maxWidth = maxWidth8;
                delta3 = delta5;
            }
            largestChildHeight = widthMeasureSpec;
            if (!allFillParent2 && widthMode != 1073741824) {
                maxWidth = alternativeMaxWidth;
            }
            setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), largestChildHeight, childState3), heightSizeAndState);
            if (!matchWidth) {
                forceUniformWidth(count, heightMeasureSpec);
                return;
            }
            return;
        } else {
            maxWidth2 = maxWidth8;
            totalWeight = totalWeight3;
            delta = delta5;
        }
        float totalWeight5 = this.mWeightSum;
        if (totalWeight5 <= 0.0f) {
            totalWeight5 = totalWeight;
        }
        float weightSum2 = totalWeight5;
        this.mTotalLength = 0;
        int i6 = 0;
        int alternativeMaxWidth6 = alternativeMaxWidth4;
        int measuredWidth3 = delta;
        int totalLength4 = maxWidth2;
        while (i6 < count) {
            View child5 = getVirtualChildAt(i6);
            if (child5.getVisibility() == 8) {
                heightMode2 = heightMode;
                baselineChildIndex = baselineChildIndex2;
            } else {
                LayoutParams lp4 = (LayoutParams) child5.getLayoutParams();
                float childExtra = lp4.weight;
                if (childExtra > 0.0f) {
                    baselineChildIndex = baselineChildIndex2;
                    int share = (int) ((((float) measuredWidth3) * childExtra) / weightSum2);
                    weightSum = weightSum2 - childExtra;
                    delta2 = measuredWidth3 - share;
                    int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp4.leftMargin + lp4.rightMargin, lp4.width);
                    if (lp4.height != 0) {
                        heightMode2 = heightMode;
                    } else if (heightMode != 1073741824) {
                        heightMode2 = heightMode;
                    } else {
                        heightMode2 = heightMode;
                        child5.measure(childWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(share > 0 ? share : 0, 1073741824));
                        childState3 = View.combineMeasuredStates(childState3, child5.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                    }
                    int childHeight2 = child5.getMeasuredHeight() + share;
                    if (childHeight2 < 0) {
                        childHeight2 = 0;
                    }
                    child5.measure(childWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(childHeight2, 1073741824));
                    childState3 = View.combineMeasuredStates(childState3, child5.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                } else {
                    heightMode2 = heightMode;
                    baselineChildIndex = baselineChildIndex2;
                    weightSum = weightSum2;
                    delta2 = measuredWidth3;
                }
                int margin3 = lp4.leftMargin + lp4.rightMargin;
                int measuredWidth4 = child5.getMeasuredWidth() + margin3;
                int maxWidth10 = Math.max(totalLength4, measuredWidth4);
                alternativeMaxWidth6 = Math.max(alternativeMaxWidth6, widthMode != 1073741824 && lp4.width == -1 ? margin3 : measuredWidth4);
                if (allFillParent2) {
                    maxWidth3 = maxWidth10;
                    if (lp4.width == -1) {
                        allFillParent = true;
                        int totalLength5 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength5, totalLength5 + child5.getMeasuredHeight() + lp4.topMargin + lp4.bottomMargin + getNextLocationOffset(child5));
                        allFillParent2 = allFillParent;
                        measuredWidth3 = delta2;
                        weightSum2 = weightSum;
                        totalLength4 = maxWidth3;
                    }
                } else {
                    maxWidth3 = maxWidth10;
                }
                allFillParent = false;
                int totalLength52 = this.mTotalLength;
                this.mTotalLength = Math.max(totalLength52, totalLength52 + child5.getMeasuredHeight() + lp4.topMargin + lp4.bottomMargin + getNextLocationOffset(child5));
                allFillParent2 = allFillParent;
                measuredWidth3 = delta2;
                weightSum2 = weightSum;
                totalLength4 = maxWidth3;
            }
            i6++;
            useLargestChild = useLargestChild;
            weightedMaxWidth5 = weightedMaxWidth5;
            largestChildHeight2 = largestChildHeight2;
            baselineChildIndex2 = baselineChildIndex;
            heightMode = heightMode2;
        }
        maxWidth = totalLength4;
        largestChildHeight = widthMeasureSpec;
        this.mTotalLength += getPaddingTop() + getPaddingBottom();
        alternativeMaxWidth = alternativeMaxWidth6;
        if (!allFillParent2) {
            maxWidth = alternativeMaxWidth;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), largestChildHeight, childState3), heightSizeAndState);
        if (!matchWidth) {
        }
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    /* JADX INFO: Multiple debug info for r12v2 int: [D('widthMode' int), D('weightedMaxHeight' int)] */
    /* JADX INFO: Multiple debug info for r14v1 boolean: [D('skippedMeasure' boolean), D('useLargestChild' boolean)] */
    /* JADX INFO: Multiple debug info for r2v29 int: [D('maxHeight' int), D('totalLength' int)] */
    /* JADX INFO: Multiple debug info for r2v3 int: [D('weightedMaxHeight' int), D('maxHeight' int)] */
    /* JADX INFO: Multiple debug info for r3v3 int: [D('alternativeMaxHeight' int), D('largestChildWidth' int)] */
    /* JADX WARN: Removed duplicated region for block: B:200:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0586  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0641  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int childState;
        int maxHeight;
        int widthMode;
        int childState2;
        int widthSizeAndState;
        int alternativeMaxHeight;
        int delta;
        int maxHeight2;
        int childState3;
        float totalWeight;
        int widthSize;
        int widthMode2;
        int count;
        boolean useLargestChild;
        int widthSizeAndState2;
        int widthMode3;
        int alternativeMaxHeight2;
        boolean allFillParent;
        int childState4;
        int largestChildWidth;
        int widthSize2;
        int alternativeMaxHeight3;
        int i;
        int maxHeight3;
        int largestChildWidth2;
        boolean baselineAligned;
        int weightedMaxHeight;
        int maxHeight4;
        int alternativeMaxHeight4;
        int weightedMaxHeight2;
        int widthMode4;
        int i2;
        int largestChildWidth3;
        LayoutParams lp;
        int largestChildWidth4;
        int margin;
        int weightedMaxHeight3;
        int alternativeMaxHeight5;
        int oldWidth;
        int alternativeMaxHeight6;
        this.mTotalLength = 0;
        int count2 = getVirtualChildCount();
        int widthMode5 = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] maxAscent = this.mMaxAscent;
        int[] maxDescent = this.mMaxDescent;
        maxAscent[3] = -1;
        maxAscent[2] = -1;
        maxAscent[1] = -1;
        maxAscent[0] = -1;
        maxDescent[3] = -1;
        maxDescent[2] = -1;
        maxDescent[1] = -1;
        maxDescent[0] = -1;
        boolean baselineAligned2 = this.mBaselineAligned;
        boolean skippedMeasure = false;
        boolean useLargestChild2 = this.mUseLargestChild;
        boolean isExactly = widthMode5 == 1073741824;
        int i3 = 0;
        int childState5 = 0;
        int alternativeMaxHeight7 = 0;
        boolean matchHeight = false;
        boolean allFillParent2 = true;
        int childHeight = 0;
        float totalWeight2 = 0.0f;
        int weightedMaxHeight4 = 0;
        int maxHeight5 = 0;
        while (i3 < count2) {
            View child = getVirtualChildAt(i3);
            if (child == null) {
                this.mTotalLength += measureNullChild(i3);
                baselineAligned = baselineAligned2;
                weightedMaxHeight = childState5;
                alternativeMaxHeight7 = alternativeMaxHeight7;
                largestChildWidth2 = widthMode5;
            } else if (child.getVisibility() == 8) {
                i3 += getChildrenSkipCount(child, i3);
                baselineAligned = baselineAligned2;
                weightedMaxHeight = childState5;
                alternativeMaxHeight7 = alternativeMaxHeight7;
                maxHeight5 = maxHeight5;
                largestChildWidth2 = widthMode5;
            } else {
                if (hasDividerBeforeChildAt(i3)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams lp2 = (LayoutParams) child.getLayoutParams();
                float totalWeight3 = totalWeight2 + lp2.weight;
                if (widthMode5 == 1073741824 && lp2.width == 0 && lp2.weight > 0.0f) {
                    if (isExactly) {
                        alternativeMaxHeight6 = weightedMaxHeight4;
                        this.mTotalLength += lp2.leftMargin + lp2.rightMargin;
                    } else {
                        alternativeMaxHeight6 = weightedMaxHeight4;
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, lp2.leftMargin + totalLength + lp2.rightMargin);
                    }
                    if (baselineAligned2) {
                        int freeSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        child.measure(freeSpec, freeSpec);
                        lp = lp2;
                        maxHeight4 = childHeight;
                        i2 = i3;
                        baselineAligned = baselineAligned2;
                        largestChildWidth3 = alternativeMaxHeight7;
                        weightedMaxHeight2 = maxHeight5;
                        alternativeMaxHeight4 = alternativeMaxHeight6;
                        largestChildWidth2 = widthMode5;
                        widthMode4 = -1;
                    } else {
                        skippedMeasure = true;
                        lp = lp2;
                        maxHeight4 = childHeight;
                        i2 = i3;
                        baselineAligned = baselineAligned2;
                        largestChildWidth3 = alternativeMaxHeight7;
                        weightedMaxHeight2 = maxHeight5;
                        alternativeMaxHeight4 = alternativeMaxHeight6;
                        largestChildWidth2 = widthMode5;
                        widthMode4 = -1;
                    }
                } else {
                    if (lp2.width != 0 || lp2.weight <= 0.0f) {
                        oldWidth = Integer.MIN_VALUE;
                    } else {
                        lp2.width = -2;
                        oldWidth = 0;
                    }
                    weightedMaxHeight2 = maxHeight5;
                    alternativeMaxHeight4 = weightedMaxHeight4;
                    maxHeight4 = childHeight;
                    i2 = i3;
                    baselineAligned = baselineAligned2;
                    largestChildWidth2 = widthMode5;
                    widthMode4 = -1;
                    measureChildBeforeLayout(child, i3, widthMeasureSpec, totalWeight3 == 0.0f ? this.mTotalLength : 0, heightMeasureSpec, 0);
                    if (oldWidth != Integer.MIN_VALUE) {
                        lp = lp2;
                        lp.width = oldWidth;
                    } else {
                        lp = lp2;
                    }
                    int childWidth = child.getMeasuredWidth();
                    if (isExactly) {
                        this.mTotalLength += lp.leftMargin + childWidth + lp.rightMargin + getNextLocationOffset(child);
                    } else {
                        int totalLength2 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength2, totalLength2 + childWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                    }
                    if (useLargestChild2) {
                        largestChildWidth3 = Math.max(childWidth, alternativeMaxHeight7);
                    } else {
                        largestChildWidth3 = alternativeMaxHeight7;
                    }
                }
                boolean matchHeightLocally = false;
                if (heightMode != 1073741824 && lp.height == widthMode4) {
                    matchHeight = true;
                    matchHeightLocally = true;
                }
                int margin2 = lp.topMargin + lp.bottomMargin;
                int childHeight2 = child.getMeasuredHeight() + margin2;
                int childState6 = View.combineMeasuredStates(childState5, child.getMeasuredState());
                if (baselineAligned) {
                    int childBaseline = child.getBaseline();
                    if (childBaseline != widthMode4) {
                        int index = ((((lp.gravity < 0 ? this.mGravity : lp.gravity) & 112) >> 4) & -2) >> 1;
                        margin = margin2;
                        maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                        largestChildWidth4 = largestChildWidth3;
                        maxDescent[index] = Math.max(maxDescent[index], childHeight2 - childBaseline);
                    } else {
                        margin = margin2;
                        largestChildWidth4 = largestChildWidth3;
                    }
                } else {
                    margin = margin2;
                    largestChildWidth4 = largestChildWidth3;
                }
                int maxHeight6 = Math.max(maxHeight4, childHeight2);
                boolean allFillParent3 = allFillParent2 && lp.height == -1;
                if (lp.weight > 0.0f) {
                    weightedMaxHeight3 = Math.max(weightedMaxHeight2, matchHeightLocally ? margin : childHeight2);
                    alternativeMaxHeight5 = alternativeMaxHeight4;
                } else {
                    alternativeMaxHeight5 = Math.max(alternativeMaxHeight4, matchHeightLocally ? margin : childHeight2);
                    weightedMaxHeight3 = weightedMaxHeight2;
                }
                childHeight = maxHeight6;
                allFillParent2 = allFillParent3;
                maxHeight5 = weightedMaxHeight3;
                totalWeight2 = totalWeight3;
                weightedMaxHeight4 = alternativeMaxHeight5;
                weightedMaxHeight = childState6;
                i3 = i2 + getChildrenSkipCount(child, i2);
                alternativeMaxHeight7 = largestChildWidth4;
            }
            i3++;
            childState5 = weightedMaxHeight;
            baselineAligned2 = baselineAligned;
            widthMode5 = largestChildWidth2;
        }
        int weightedMaxHeight5 = maxHeight5;
        int maxHeight7 = childHeight;
        int largestChildWidth5 = alternativeMaxHeight7;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(count2)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1 && maxAscent[3] == -1) {
            childState = childState5;
        } else {
            childState = childState5;
            maxHeight7 = Math.max(maxHeight7, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
        }
        if (useLargestChild2) {
            widthMode = widthMode5;
            if (widthMode == Integer.MIN_VALUE || widthMode == 0) {
                this.mTotalLength = 0;
                int i4 = 0;
                while (i4 < count2) {
                    View child2 = getVirtualChildAt(i4);
                    if (child2 == null) {
                        this.mTotalLength += measureNullChild(i4);
                        maxHeight3 = maxHeight7;
                        i = i4;
                    } else if (child2.getVisibility() == 8) {
                        maxHeight3 = maxHeight7;
                        i = i4 + getChildrenSkipCount(child2, i4);
                    } else {
                        LayoutParams lp3 = (LayoutParams) child2.getLayoutParams();
                        if (isExactly) {
                            maxHeight3 = maxHeight7;
                            i = i4;
                            this.mTotalLength += lp3.leftMargin + largestChildWidth5 + lp3.rightMargin + getNextLocationOffset(child2);
                        } else {
                            maxHeight3 = maxHeight7;
                            i = i4;
                            int totalLength3 = this.mTotalLength;
                            this.mTotalLength = Math.max(totalLength3, totalLength3 + largestChildWidth5 + lp3.leftMargin + lp3.rightMargin + getNextLocationOffset(child2));
                        }
                    }
                    i4 = i + 1;
                    maxHeight7 = maxHeight3;
                }
                maxHeight = maxHeight7;
            } else {
                maxHeight = maxHeight7;
            }
        } else {
            maxHeight = maxHeight7;
            widthMode = widthMode5;
        }
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int widthSizeAndState3 = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), widthMeasureSpec, 0);
        int widthSize3 = widthSizeAndState3 & ViewCompat.MEASURED_SIZE_MASK;
        int delta2 = widthSize3 - this.mTotalLength;
        if (skippedMeasure) {
            totalWeight = totalWeight2;
            widthSize = weightedMaxHeight4;
        } else if (delta2 == 0 || totalWeight2 <= 0.0f) {
            int alternativeMaxHeight8 = Math.max(weightedMaxHeight4, weightedMaxHeight5);
            if (!useLargestChild2) {
                alternativeMaxHeight = alternativeMaxHeight8;
            } else if (widthMode != 1073741824) {
                int i5 = 0;
                while (i5 < count2) {
                    View child3 = getVirtualChildAt(i5);
                    if (child3 != null) {
                        alternativeMaxHeight3 = alternativeMaxHeight8;
                        widthSize2 = widthSize3;
                        if (child3.getVisibility() == 8) {
                            largestChildWidth = largestChildWidth5;
                        } else if (((LayoutParams) child3.getLayoutParams()).weight > 0.0f) {
                            largestChildWidth = largestChildWidth5;
                            child3.measure(View.MeasureSpec.makeMeasureSpec(largestChildWidth5, 1073741824), View.MeasureSpec.makeMeasureSpec(child3.getMeasuredHeight(), 1073741824));
                        } else {
                            largestChildWidth = largestChildWidth5;
                        }
                    } else {
                        alternativeMaxHeight3 = alternativeMaxHeight8;
                        widthSize2 = widthSize3;
                        largestChildWidth = largestChildWidth5;
                    }
                    i5++;
                    alternativeMaxHeight8 = alternativeMaxHeight3;
                    totalWeight2 = totalWeight2;
                    widthSize3 = widthSize2;
                    largestChildWidth5 = largestChildWidth;
                }
                alternativeMaxHeight = alternativeMaxHeight8;
            } else {
                alternativeMaxHeight = alternativeMaxHeight8;
            }
            widthSizeAndState = widthSizeAndState3;
            maxHeight2 = maxHeight;
            childState3 = childState;
            delta = heightMeasureSpec;
            childState2 = count2;
            if (!allFillParent2 && heightMode != 1073741824) {
                maxHeight2 = alternativeMaxHeight;
            }
            setMeasuredDimension(widthSizeAndState | (-16777216 & childState3), View.resolveSizeAndState(Math.max(maxHeight2 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), delta, childState3 << 16));
            if (!matchHeight) {
                forceUniformHeight(childState2, widthMeasureSpec);
                return;
            }
            return;
        } else {
            totalWeight = totalWeight2;
            widthSize = weightedMaxHeight4;
        }
        float weightSum = this.mWeightSum;
        if (weightSum <= 0.0f) {
            weightSum = totalWeight;
        }
        maxAscent[3] = -1;
        maxAscent[2] = -1;
        maxAscent[1] = -1;
        maxAscent[0] = -1;
        maxDescent[3] = -1;
        maxDescent[2] = -1;
        maxDescent[1] = -1;
        maxDescent[0] = -1;
        maxHeight2 = -1;
        this.mTotalLength = 0;
        int i6 = 0;
        int alternativeMaxHeight9 = widthSize;
        childState3 = childState;
        while (i6 < count2) {
            View child4 = getVirtualChildAt(i6);
            if (child4 != null) {
                useLargestChild = useLargestChild2;
                if (child4.getVisibility() == 8) {
                    widthMode2 = widthMode;
                    widthMode3 = delta2;
                    widthSizeAndState2 = widthSizeAndState3;
                    count = count2;
                } else {
                    LayoutParams lp4 = (LayoutParams) child4.getLayoutParams();
                    float childExtra = lp4.weight;
                    if (childExtra > 0.0f) {
                        count = count2;
                        int share = (int) ((((float) delta2) * childExtra) / weightSum);
                        float weightSum2 = weightSum - childExtra;
                        int delta3 = delta2 - share;
                        widthSizeAndState2 = widthSizeAndState3;
                        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp4.topMargin + lp4.bottomMargin, lp4.height);
                        if (lp4.width == 0 && widthMode == 1073741824) {
                            child4.measure(View.MeasureSpec.makeMeasureSpec(share > 0 ? share : 0, 1073741824), childHeightMeasureSpec);
                            widthMode2 = widthMode;
                        } else {
                            int childWidth2 = child4.getMeasuredWidth() + share;
                            if (childWidth2 < 0) {
                                childWidth2 = 0;
                            }
                            widthMode2 = widthMode;
                            child4.measure(View.MeasureSpec.makeMeasureSpec(childWidth2, 1073741824), childHeightMeasureSpec);
                        }
                        childState3 = View.combineMeasuredStates(childState3, child4.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        weightSum = weightSum2;
                        widthMode3 = delta3;
                    } else {
                        widthMode2 = widthMode;
                        widthMode3 = delta2;
                        widthSizeAndState2 = widthSizeAndState3;
                        count = count2;
                    }
                    if (isExactly) {
                        this.mTotalLength += child4.getMeasuredWidth() + lp4.leftMargin + lp4.rightMargin + getNextLocationOffset(child4);
                    } else {
                        int totalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength4, child4.getMeasuredWidth() + totalLength4 + lp4.leftMargin + lp4.rightMargin + getNextLocationOffset(child4));
                    }
                    boolean matchHeightLocally2 = heightMode != 1073741824 && lp4.height == -1;
                    int margin3 = lp4.topMargin + lp4.bottomMargin;
                    int childHeight3 = child4.getMeasuredHeight() + margin3;
                    maxHeight2 = Math.max(maxHeight2, childHeight3);
                    int alternativeMaxHeight10 = Math.max(alternativeMaxHeight9, matchHeightLocally2 ? margin3 : childHeight3);
                    if (allFillParent2) {
                        alternativeMaxHeight2 = alternativeMaxHeight10;
                        if (lp4.height == -1) {
                            allFillParent = true;
                            if (!baselineAligned2) {
                                int childBaseline2 = child4.getBaseline();
                                allFillParent2 = allFillParent;
                                if (childBaseline2 != -1) {
                                    int index2 = ((((lp4.gravity < 0 ? this.mGravity : lp4.gravity) & 112) >> 4) & -2) >> 1;
                                    maxAscent[index2] = Math.max(maxAscent[index2], childBaseline2);
                                    childState4 = childState3;
                                    maxDescent[index2] = Math.max(maxDescent[index2], childHeight3 - childBaseline2);
                                } else {
                                    childState4 = childState3;
                                }
                            } else {
                                allFillParent2 = allFillParent;
                                childState4 = childState3;
                            }
                            weightSum = weightSum;
                            alternativeMaxHeight9 = alternativeMaxHeight2;
                            childState3 = childState4;
                        }
                    } else {
                        alternativeMaxHeight2 = alternativeMaxHeight10;
                    }
                    allFillParent = false;
                    if (!baselineAligned2) {
                    }
                    weightSum = weightSum;
                    alternativeMaxHeight9 = alternativeMaxHeight2;
                    childState3 = childState4;
                }
            } else {
                widthMode2 = widthMode;
                widthMode3 = delta2;
                widthSizeAndState2 = widthSizeAndState3;
                count = count2;
                useLargestChild = useLargestChild2;
            }
            i6++;
            delta2 = widthMode3;
            widthSizeAndState3 = widthSizeAndState2;
            useLargestChild2 = useLargestChild;
            count2 = count;
            weightedMaxHeight5 = weightedMaxHeight5;
            widthMode = widthMode2;
        }
        widthSizeAndState = widthSizeAndState3;
        childState2 = count2;
        delta = heightMeasureSpec;
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        if (!(maxAscent[1] == -1 && maxAscent[0] == -1 && maxAscent[2] == -1 && maxAscent[3] == -1)) {
            maxHeight2 = Math.max(maxHeight2, Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2]))) + Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2]))));
        }
        alternativeMaxHeight = alternativeMaxHeight9;
        if (!allFillParent2) {
            maxHeight2 = alternativeMaxHeight;
        }
        setMeasuredDimension(widthSizeAndState | (-16777216 & childState3), View.resolveSizeAndState(Math.max(maxHeight2 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), delta, childState3 << 16));
        if (!matchHeight) {
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    int getChildrenSkipCount(View child, int index) {
        return 0;
    }

    int measureNullChild(int childIndex) {
        return 0;
    }

    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    int getLocationOffset(View child) {
        return 0;
    }

    int getNextLocationOffset(View child) {
        return 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mOrientation == 1) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }

    void layoutVertical(int left, int top, int right, int bottom) {
        int childTop;
        int paddingLeft;
        int gravity;
        int childLeft;
        int paddingLeft2 = getPaddingLeft();
        int width = right - left;
        int childRight = width - getPaddingRight();
        int childSpace = (width - paddingLeft2) - getPaddingRight();
        int count = getVirtualChildCount();
        int i = this.mGravity;
        int majorGravity = i & 112;
        int minorGravity = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity == 16) {
            childTop = getPaddingTop() + (((bottom - top) - this.mTotalLength) / 2);
        } else if (majorGravity != 80) {
            childTop = getPaddingTop();
        } else {
            childTop = ((getPaddingTop() + bottom) - top) - this.mTotalLength;
        }
        int i2 = 0;
        while (i2 < count) {
            View child = getVirtualChildAt(i2);
            if (child == null) {
                childTop += measureNullChild(i2);
                paddingLeft = paddingLeft2;
            } else if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity2 = lp.gravity;
                if (gravity2 < 0) {
                    gravity = minorGravity;
                } else {
                    gravity = gravity2;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    childLeft = ((((childSpace - childWidth) / 2) + paddingLeft2) + lp.leftMargin) - lp.rightMargin;
                } else if (absoluteGravity != 5) {
                    childLeft = lp.leftMargin + paddingLeft2;
                } else {
                    childLeft = (childRight - childWidth) - lp.rightMargin;
                }
                if (hasDividerBeforeChildAt(i2)) {
                    childTop += this.mDividerHeight;
                }
                int childTop2 = childTop + lp.topMargin;
                paddingLeft = paddingLeft2;
                setChildFrame(child, childLeft, childTop2 + getLocationOffset(child), childWidth, childHeight);
                int childTop3 = childTop2 + childHeight + lp.bottomMargin + getNextLocationOffset(child);
                i2 += getChildrenSkipCount(child, i2);
                childTop = childTop3;
            } else {
                paddingLeft = paddingLeft2;
            }
            i2++;
            paddingLeft2 = paddingLeft;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x011a  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    void layoutHorizontal(int left, int top, int right, int bottom) {
        int childLeft;
        int dir;
        int start;
        int[] maxAscent;
        int[] maxDescent;
        int paddingTop;
        int count;
        int height;
        int layoutDirection;
        int childBaseline;
        int gravity;
        int gravity2;
        int gravity3;
        int childTop;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop2 = getPaddingTop();
        int height2 = bottom - top;
        int childBottom = height2 - getPaddingBottom();
        int childSpace = (height2 - paddingTop2) - getPaddingBottom();
        int count2 = getVirtualChildCount();
        int i = this.mGravity;
        int majorGravity = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int minorGravity = i & 112;
        boolean baselineAligned = this.mBaselineAligned;
        int[] maxAscent2 = this.mMaxAscent;
        int[] maxDescent2 = this.mMaxDescent;
        int layoutDirection2 = ViewCompat.getLayoutDirection(this);
        int absoluteGravity = GravityCompat.getAbsoluteGravity(majorGravity, layoutDirection2);
        if (absoluteGravity == 1) {
            childLeft = getPaddingLeft() + (((right - left) - this.mTotalLength) / 2);
        } else if (absoluteGravity != 5) {
            childLeft = getPaddingLeft();
        } else {
            childLeft = ((getPaddingLeft() + right) - left) - this.mTotalLength;
        }
        if (isLayoutRtl) {
            start = count2 - 1;
            dir = -1;
        } else {
            start = 0;
            dir = 1;
        }
        int i2 = 0;
        while (i2 < count2) {
            int childIndex = start + (dir * i2);
            View child = getVirtualChildAt(childIndex);
            if (child == null) {
                childLeft += measureNullChild(childIndex);
                layoutDirection = layoutDirection2;
                maxDescent = maxDescent2;
                maxAscent = maxAscent2;
                paddingTop = paddingTop2;
                height = height2;
                count = count2;
            } else {
                layoutDirection = layoutDirection2;
                if (child.getVisibility() != 8) {
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    if (baselineAligned) {
                        height = height2;
                        if (lp.height != -1) {
                            childBaseline = child.getBaseline();
                            gravity = lp.gravity;
                            if (gravity >= 0) {
                                gravity2 = minorGravity;
                            } else {
                                gravity2 = gravity;
                            }
                            gravity3 = gravity2 & 112;
                            count = count2;
                            if (gravity3 != 16) {
                                childTop = ((((childSpace - childHeight) / 2) + paddingTop2) + lp.topMargin) - lp.bottomMargin;
                            } else if (gravity3 == 48) {
                                int childTop2 = lp.topMargin + paddingTop2;
                                childTop = childBaseline != -1 ? childTop2 + (maxAscent2[1] - childBaseline) : childTop2;
                            } else if (gravity3 != 80) {
                                childTop = paddingTop2;
                            } else {
                                int childTop3 = (childBottom - childHeight) - lp.bottomMargin;
                                if (childBaseline != -1) {
                                    childTop = childTop3 - (maxDescent2[2] - (child.getMeasuredHeight() - childBaseline));
                                } else {
                                    childTop = childTop3;
                                }
                            }
                            if (hasDividerBeforeChildAt(childIndex)) {
                                childLeft += this.mDividerWidth;
                            }
                            int childLeft2 = childLeft + lp.leftMargin;
                            paddingTop = paddingTop2;
                            maxDescent = maxDescent2;
                            maxAscent = maxAscent2;
                            setChildFrame(child, childLeft2 + getLocationOffset(child), childTop, childWidth, childHeight);
                            int childLeft3 = childLeft2 + childWidth + lp.rightMargin + getNextLocationOffset(child);
                            i2 += getChildrenSkipCount(child, childIndex);
                            childLeft = childLeft3;
                        }
                    } else {
                        height = height2;
                    }
                    childBaseline = -1;
                    gravity = lp.gravity;
                    if (gravity >= 0) {
                    }
                    gravity3 = gravity2 & 112;
                    count = count2;
                    if (gravity3 != 16) {
                    }
                    if (hasDividerBeforeChildAt(childIndex)) {
                    }
                    int childLeft22 = childLeft + lp.leftMargin;
                    paddingTop = paddingTop2;
                    maxDescent = maxDescent2;
                    maxAscent = maxAscent2;
                    setChildFrame(child, childLeft22 + getLocationOffset(child), childTop, childWidth, childHeight);
                    int childLeft32 = childLeft22 + childWidth + lp.rightMargin + getNextLocationOffset(child);
                    i2 += getChildrenSkipCount(child, childIndex);
                    childLeft = childLeft32;
                } else {
                    maxDescent = maxDescent2;
                    maxAscent = maxAscent2;
                    paddingTop = paddingTop2;
                    height = height2;
                    count = count2;
                    i2 = i2;
                }
            }
            i2++;
            isLayoutRtl = isLayoutRtl;
            layoutDirection2 = layoutDirection;
            height2 = height;
            count2 = count;
            paddingTop2 = paddingTop;
            maxDescent2 = maxDescent;
            maxAscent2 = maxAscent;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((8388615 & gravity) == 0) {
                gravity |= GravityCompat.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i = this.mGravity;
        if ((8388615 & i) != gravity) {
            this.mGravity = (-8388616 & i) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        int i = this.mGravity;
        if ((i & 112) != gravity) {
            this.mGravity = (i & -113) | gravity;
            requestLayout();
        }
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}
