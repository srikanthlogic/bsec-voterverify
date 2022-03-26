package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TintableImageSourceView;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.expandable.ExpandableTransformationWidget;
import com.google.android.material.expandable.ExpandableWidgetHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.stateful.ExtendableSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
@CoordinatorLayout.DefaultBehavior(Behavior.class)
/* loaded from: classes.dex */
public class FloatingActionButton extends VisibilityAwareImageButton implements TintableBackgroundView, TintableImageSourceView, ExpandableTransformationWidget {
    private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
    private static final String EXPANDABLE_WIDGET_HELPER_KEY = "expandableWidgetHelper";
    private static final String LOG_TAG = "FloatingActionButton";
    public static final int NO_CUSTOM_SIZE = 0;
    public static final int SIZE_AUTO = -1;
    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 0;
    private ColorStateList backgroundTint;
    private PorterDuff.Mode backgroundTintMode;
    private int borderWidth;
    boolean compatPadding;
    private int customSize;
    private final ExpandableWidgetHelper expandableWidgetHelper;
    private final AppCompatImageHelper imageHelper;
    private PorterDuff.Mode imageMode;
    private int imagePadding;
    private ColorStateList imageTint;
    private FloatingActionButtonImpl impl;
    private int maxImageSize;
    private ColorStateList rippleColor;
    final Rect shadowPadding;
    private int size;
    private final Rect touchArea;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Size {
    }

    /* loaded from: classes.dex */
    public static abstract class OnVisibilityChangedListener {
        public void onShown(FloatingActionButton fab) {
        }

        public void onHidden(FloatingActionButton fab) {
        }
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.floatingActionButtonStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.shadowPadding = new Rect();
        this.touchArea = new Rect();
        TypedArray a2 = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.FloatingActionButton, defStyleAttr, R.style.Widget_Design_FloatingActionButton, new int[0]);
        this.backgroundTint = MaterialResources.getColorStateList(context, a2, R.styleable.FloatingActionButton_backgroundTint);
        this.backgroundTintMode = ViewUtils.parseTintMode(a2.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.rippleColor = MaterialResources.getColorStateList(context, a2, R.styleable.FloatingActionButton_rippleColor);
        this.size = a2.getInt(R.styleable.FloatingActionButton_fabSize, -1);
        this.customSize = a2.getDimensionPixelSize(R.styleable.FloatingActionButton_fabCustomSize, 0);
        this.borderWidth = a2.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
        float elevation = a2.getDimension(R.styleable.FloatingActionButton_elevation, 0.0f);
        float hoveredFocusedTranslationZ = a2.getDimension(R.styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0f);
        float pressedTranslationZ = a2.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        this.compatPadding = a2.getBoolean(R.styleable.FloatingActionButton_useCompatPadding, false);
        this.maxImageSize = a2.getDimensionPixelSize(R.styleable.FloatingActionButton_maxImageSize, 0);
        MotionSpec showMotionSpec = MotionSpec.createFromAttribute(context, a2, R.styleable.FloatingActionButton_showMotionSpec);
        MotionSpec hideMotionSpec = MotionSpec.createFromAttribute(context, a2, R.styleable.FloatingActionButton_hideMotionSpec);
        a2.recycle();
        this.imageHelper = new AppCompatImageHelper(this);
        this.imageHelper.loadFromAttributes(attrs, defStyleAttr);
        this.expandableWidgetHelper = new ExpandableWidgetHelper(this);
        getImpl().setBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
        getImpl().setElevation(elevation);
        getImpl().setHoveredFocusedTranslationZ(hoveredFocusedTranslationZ);
        getImpl().setPressedTranslationZ(pressedTranslationZ);
        getImpl().setMaxImageSize(this.maxImageSize);
        getImpl().setShowMotionSpec(showMotionSpec);
        getImpl().setHideMotionSpec(hideMotionSpec);
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int preferredSize = getSizeDimension();
        this.imagePadding = (preferredSize - this.maxImageSize) / 2;
        getImpl().updatePadding();
        int d = Math.min(resolveAdjustedSize(preferredSize, widthMeasureSpec), resolveAdjustedSize(preferredSize, heightMeasureSpec));
        setMeasuredDimension(this.shadowPadding.left + d + this.shadowPadding.right, this.shadowPadding.top + d + this.shadowPadding.bottom);
    }

    @Deprecated
    public int getRippleColor() {
        ColorStateList colorStateList = this.rippleColor;
        if (colorStateList != null) {
            return colorStateList.getDefaultColor();
        }
        return 0;
    }

    public ColorStateList getRippleColorStateList() {
        return this.rippleColor;
    }

    public void setRippleColor(int color) {
        setRippleColor(ColorStateList.valueOf(color));
    }

    public void setRippleColor(ColorStateList color) {
        if (this.rippleColor != color) {
            this.rippleColor = color;
            getImpl().setRippleColor(this.rippleColor);
        }
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return this.backgroundTint;
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList tint) {
        if (this.backgroundTint != tint) {
            this.backgroundTint = tint;
            getImpl().setBackgroundTintList(tint);
        }
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (this.backgroundTintMode != tintMode) {
            this.backgroundTintMode = tintMode;
            getImpl().setBackgroundTintMode(tintMode);
        }
    }

    @Override // androidx.core.view.TintableBackgroundView
    public void setSupportBackgroundTintList(ColorStateList tint) {
        setBackgroundTintList(tint);
    }

    @Override // androidx.core.view.TintableBackgroundView
    public ColorStateList getSupportBackgroundTintList() {
        return getBackgroundTintList();
    }

    @Override // androidx.core.view.TintableBackgroundView
    public void setSupportBackgroundTintMode(PorterDuff.Mode tintMode) {
        setBackgroundTintMode(tintMode);
    }

    @Override // androidx.core.view.TintableBackgroundView
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return getBackgroundTintMode();
    }

    @Override // androidx.core.widget.TintableImageSourceView
    public void setSupportImageTintList(ColorStateList tint) {
        if (this.imageTint != tint) {
            this.imageTint = tint;
            onApplySupportImageTint();
        }
    }

    @Override // androidx.core.widget.TintableImageSourceView
    public ColorStateList getSupportImageTintList() {
        return this.imageTint;
    }

    @Override // androidx.core.widget.TintableImageSourceView
    public void setSupportImageTintMode(PorterDuff.Mode tintMode) {
        if (this.imageMode != tintMode) {
            this.imageMode = tintMode;
            onApplySupportImageTint();
        }
    }

    @Override // androidx.core.widget.TintableImageSourceView
    public PorterDuff.Mode getSupportImageTintMode() {
        return this.imageMode;
    }

    private void onApplySupportImageTint() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            ColorStateList colorStateList = this.imageTint;
            if (colorStateList == null) {
                DrawableCompat.clearColorFilter(drawable);
                return;
            }
            int color = colorStateList.getColorForState(getDrawableState(), 0);
            PorterDuff.Mode mode = this.imageMode;
            if (mode == null) {
                mode = PorterDuff.Mode.SRC_IN;
            }
            drawable.mutate().setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(color, mode));
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable background) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundResource(int resid) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    @Override // android.widget.ImageView
    public void setImageResource(int resId) {
        this.imageHelper.setImageResource(resId);
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getImpl().updateImageMatrixScale();
    }

    public void show() {
        show(null);
    }

    public void show(OnVisibilityChangedListener listener) {
        show(listener, true);
    }

    void show(OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().show(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    public void addOnShowAnimationListener(Animator.AnimatorListener listener) {
        getImpl().addOnShowAnimationListener(listener);
    }

    public void removeOnShowAnimationListener(Animator.AnimatorListener listener) {
        getImpl().removeOnShowAnimationListener(listener);
    }

    public void hide() {
        hide(null);
    }

    public void hide(OnVisibilityChangedListener listener) {
        hide(listener, true);
    }

    void hide(OnVisibilityChangedListener listener, boolean fromUser) {
        getImpl().hide(wrapOnVisibilityChangedListener(listener), fromUser);
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener listener) {
        getImpl().addOnHideAnimationListener(listener);
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener listener) {
        getImpl().removeOnHideAnimationListener(listener);
    }

    @Override // com.google.android.material.expandable.ExpandableWidget
    public boolean setExpanded(boolean expanded) {
        return this.expandableWidgetHelper.setExpanded(expanded);
    }

    @Override // com.google.android.material.expandable.ExpandableWidget
    public boolean isExpanded() {
        return this.expandableWidgetHelper.isExpanded();
    }

    @Override // com.google.android.material.expandable.ExpandableTransformationWidget
    public void setExpandedComponentIdHint(int expandedComponentIdHint) {
        this.expandableWidgetHelper.setExpandedComponentIdHint(expandedComponentIdHint);
    }

    @Override // com.google.android.material.expandable.ExpandableTransformationWidget
    public int getExpandedComponentIdHint() {
        return this.expandableWidgetHelper.getExpandedComponentIdHint();
    }

    public void setUseCompatPadding(boolean useCompatPadding) {
        if (this.compatPadding != useCompatPadding) {
            this.compatPadding = useCompatPadding;
            getImpl().onCompatShadowChanged();
        }
    }

    public boolean getUseCompatPadding() {
        return this.compatPadding;
    }

    public void setSize(int size) {
        this.customSize = 0;
        if (size != this.size) {
            this.size = size;
            requestLayout();
        }
    }

    public int getSize() {
        return this.size;
    }

    private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final OnVisibilityChangedListener listener) {
        if (listener == null) {
            return null;
        }
        return new FloatingActionButtonImpl.InternalVisibilityChangedListener() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButton.1
            @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.InternalVisibilityChangedListener
            public void onShown() {
                listener.onShown(FloatingActionButton.this);
            }

            @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.InternalVisibilityChangedListener
            public void onHidden() {
                listener.onHidden(FloatingActionButton.this);
            }
        };
    }

    public boolean isOrWillBeHidden() {
        return getImpl().isOrWillBeHidden();
    }

    public boolean isOrWillBeShown() {
        return getImpl().isOrWillBeShown();
    }

    public void setCustomSize(int size) {
        if (size >= 0) {
            this.customSize = size;
            return;
        }
        throw new IllegalArgumentException("Custom size must be non-negative");
    }

    public int getCustomSize() {
        return this.customSize;
    }

    public void clearCustomSize() {
        setCustomSize(0);
    }

    int getSizeDimension() {
        return getSizeDimension(this.size);
    }

    private int getSizeDimension(int size) {
        int i = this.customSize;
        if (i != 0) {
            return i;
        }
        Resources res = getResources();
        if (size != -1) {
            if (size != 1) {
                return res.getDimensionPixelSize(R.dimen.design_fab_size_normal);
            }
            return res.getDimensionPixelSize(R.dimen.design_fab_size_mini);
        } else if (Math.max(res.getConfiguration().screenWidthDp, res.getConfiguration().screenHeightDp) < AUTO_MINI_LARGEST_SCREEN_WIDTH) {
            return getSizeDimension(1);
        } else {
            return getSizeDimension(0);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().onAttachedToWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().onDetachedFromWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().onDrawableStateChanged(getDrawableState());
    }

    @Override // android.widget.ImageView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().jumpDrawableToCurrentState();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        ExtendableSavedState state = new ExtendableSavedState(super.onSaveInstanceState());
        state.extendableStates.put(EXPANDABLE_WIDGET_HELPER_KEY, this.expandableWidgetHelper.onSaveInstanceState());
        return state;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        ExtendableSavedState ess = (ExtendableSavedState) state;
        super.onRestoreInstanceState(ess.getSuperState());
        this.expandableWidgetHelper.onRestoreInstanceState(ess.extendableStates.get(EXPANDABLE_WIDGET_HELPER_KEY));
    }

    @Deprecated
    public boolean getContentRect(Rect rect) {
        if (!ViewCompat.isLaidOut(this)) {
            return false;
        }
        rect.set(0, 0, getWidth(), getHeight());
        offsetRectWithShadow(rect);
        return true;
    }

    public void getMeasuredContentRect(Rect rect) {
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        offsetRectWithShadow(rect);
    }

    private void offsetRectWithShadow(Rect rect) {
        rect.left += this.shadowPadding.left;
        rect.top += this.shadowPadding.top;
        rect.right -= this.shadowPadding.right;
        rect.bottom -= this.shadowPadding.bottom;
    }

    public Drawable getContentBackground() {
        return getImpl().getContentBackground();
    }

    private static int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(desiredSize, specSize);
        }
        if (specMode == 0) {
            return desiredSize;
        }
        if (specMode == 1073741824) {
            return specSize;
        }
        throw new IllegalArgumentException();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != 0 || !getContentRect(this.touchArea) || this.touchArea.contains((int) ev.getX(), (int) ev.getY())) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    /* loaded from: classes.dex */
    public static class Behavior extends BaseBehavior<FloatingActionButton> {
        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            return super.getInsetDodgeRect(coordinatorLayout, floatingActionButton, rect);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean isAutoHideEnabled() {
            return super.isAutoHideEnabled();
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
            super.onAttachedToLayoutParams(layoutParams);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            return super.onDependentViewChanged(coordinatorLayout, floatingActionButton, view);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i) {
            return super.onLayoutChild(coordinatorLayout, floatingActionButton, i);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ void setAutoHideEnabled(boolean z) {
            super.setAutoHideEnabled(z);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
            super.setInternalAutoHideListener(onVisibilityChangedListener);
        }

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    /* loaded from: classes.dex */
    protected static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T> {
        private static final boolean AUTO_HIDE_DEFAULT = true;
        private boolean autoHideEnabled;
        private OnVisibilityChangedListener internalAutoHideListener;
        private Rect tmpRect;

        public BaseBehavior() {
            this.autoHideEnabled = true;
        }

        public BaseBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = a2.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
            a2.recycle();
        }

        public void setAutoHideEnabled(boolean autoHide) {
            this.autoHideEnabled = autoHide;
        }

        public boolean isAutoHideEnabled() {
            return this.autoHideEnabled;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams lp) {
            if (lp.dodgeInsetEdges == 0) {
                lp.dodgeInsetEdges = 80;
            }
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child);
                return false;
            } else if (!isBottomSheet(dependency)) {
                return false;
            } else {
                updateFabVisibilityForBottomSheet(dependency, child);
                return false;
            }
        }

        private static boolean isBottomSheet(View view) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                return ((CoordinatorLayout.LayoutParams) lp).getBehavior() instanceof BottomSheetBehavior;
            }
            return false;
        }

        public void setInternalAutoHideListener(OnVisibilityChangedListener listener) {
            this.internalAutoHideListener = listener;
        }

        private boolean shouldUpdateVisibility(View dependency, FloatingActionButton child) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (this.autoHideEnabled && lp.getAnchorId() == dependency.getId() && child.getUserSetVisibility() == 0) {
                return true;
            }
            return false;
        }

        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent, AppBarLayout appBarLayout, FloatingActionButton child) {
            if (!shouldUpdateVisibility(appBarLayout, child)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                child.hide(this.internalAutoHideListener, false);
                return true;
            }
            child.show(this.internalAutoHideListener, false);
            return true;
        }

        private boolean updateFabVisibilityForBottomSheet(View bottomSheet, FloatingActionButton child) {
            if (!shouldUpdateVisibility(bottomSheet, child)) {
                return false;
            }
            if (bottomSheet.getTop() < (child.getHeight() / 2) + ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin) {
                child.hide(this.internalAutoHideListener, false);
                return true;
            }
            child.show(this.internalAutoHideListener, false);
            return true;
        }

        public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
            List<View> dependencies = parent.getDependencies(child);
            int count = dependencies.size();
            for (int i = 0; i < count; i++) {
                View dependency = dependencies.get(i);
                if (!(dependency instanceof AppBarLayout)) {
                    if (isBottomSheet(dependency) && updateFabVisibilityForBottomSheet(dependency, child)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child)) {
                    break;
                }
            }
            parent.onLayoutChild(child, layoutDirection);
            offsetIfNeeded(parent, child);
            return true;
        }

        public boolean getInsetDodgeRect(CoordinatorLayout parent, FloatingActionButton child, Rect rect) {
            Rect shadowPadding = child.shadowPadding;
            rect.set(child.getLeft() + shadowPadding.left, child.getTop() + shadowPadding.top, child.getRight() - shadowPadding.right, child.getBottom() - shadowPadding.bottom);
            return true;
        }

        private void offsetIfNeeded(CoordinatorLayout parent, FloatingActionButton fab) {
            Rect padding = fab.shadowPadding;
            if (padding != null && padding.centerX() > 0 && padding.centerY() > 0) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                int offsetTB = 0;
                int offsetLR = 0;
                if (fab.getRight() >= parent.getWidth() - lp.rightMargin) {
                    offsetLR = padding.right;
                } else if (fab.getLeft() <= lp.leftMargin) {
                    offsetLR = -padding.left;
                }
                if (fab.getBottom() >= parent.getHeight() - lp.bottomMargin) {
                    offsetTB = padding.bottom;
                } else if (fab.getTop() <= lp.topMargin) {
                    offsetTB = -padding.top;
                }
                if (offsetTB != 0) {
                    ViewCompat.offsetTopAndBottom(fab, offsetTB);
                }
                if (offsetLR != 0) {
                    ViewCompat.offsetLeftAndRight(fab, offsetLR);
                }
            }
        }
    }

    public float getCompatElevation() {
        return getImpl().getElevation();
    }

    public void setCompatElevation(float elevation) {
        getImpl().setElevation(elevation);
    }

    public void setCompatElevationResource(int id) {
        setCompatElevation(getResources().getDimension(id));
    }

    public float getCompatHoveredFocusedTranslationZ() {
        return getImpl().getHoveredFocusedTranslationZ();
    }

    public void setCompatHoveredFocusedTranslationZ(float translationZ) {
        getImpl().setHoveredFocusedTranslationZ(translationZ);
    }

    public void setCompatHoveredFocusedTranslationZResource(int id) {
        setCompatHoveredFocusedTranslationZ(getResources().getDimension(id));
    }

    public float getCompatPressedTranslationZ() {
        return getImpl().getPressedTranslationZ();
    }

    public void setCompatPressedTranslationZ(float translationZ) {
        getImpl().setPressedTranslationZ(translationZ);
    }

    public void setCompatPressedTranslationZResource(int id) {
        setCompatPressedTranslationZ(getResources().getDimension(id));
    }

    public MotionSpec getShowMotionSpec() {
        return getImpl().getShowMotionSpec();
    }

    public void setShowMotionSpec(MotionSpec spec) {
        getImpl().setShowMotionSpec(spec);
    }

    public void setShowMotionSpecResource(int id) {
        setShowMotionSpec(MotionSpec.createFromResource(getContext(), id));
    }

    public MotionSpec getHideMotionSpec() {
        return getImpl().getHideMotionSpec();
    }

    public void setHideMotionSpec(MotionSpec spec) {
        getImpl().setHideMotionSpec(spec);
    }

    public void setHideMotionSpecResource(int id) {
        setHideMotionSpec(MotionSpec.createFromResource(getContext(), id));
    }

    private FloatingActionButtonImpl getImpl() {
        if (this.impl == null) {
            this.impl = createImpl();
        }
        return this.impl;
    }

    private FloatingActionButtonImpl createImpl() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl());
        }
        return new FloatingActionButtonImpl(this, new ShadowDelegateImpl());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ShadowDelegateImpl implements ShadowViewDelegate {
        ShadowDelegateImpl() {
        }

        @Override // com.google.android.material.shadow.ShadowViewDelegate
        public float getRadius() {
            return ((float) FloatingActionButton.this.getSizeDimension()) / 2.0f;
        }

        @Override // com.google.android.material.shadow.ShadowViewDelegate
        public void setShadowPadding(int left, int top, int right, int bottom) {
            FloatingActionButton.this.shadowPadding.set(left, top, right, bottom);
            FloatingActionButton floatingActionButton = FloatingActionButton.this;
            floatingActionButton.setPadding(floatingActionButton.imagePadding + left, FloatingActionButton.this.imagePadding + top, FloatingActionButton.this.imagePadding + right, FloatingActionButton.this.imagePadding + bottom);
        }

        @Override // com.google.android.material.shadow.ShadowViewDelegate
        public void setBackgroundDrawable(Drawable background) {
            FloatingActionButton.super.setBackgroundDrawable(background);
        }

        @Override // com.google.android.material.shadow.ShadowViewDelegate
        public boolean isCompatPaddingEnabled() {
            return FloatingActionButton.this.compatPadding;
        }
    }
}
