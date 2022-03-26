package com.facebook.drawee.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.Rect;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import com.facebook.drawee.drawable.ScalingUtils;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DraweeTransition extends Transition {
    private static final String PROPNAME_BOUNDS = "draweeTransition:bounds";
    @Nullable
    private final PointF mFromFocusPoint;
    private final ScalingUtils.ScaleType mFromScale;
    @Nullable
    private final PointF mToFocusPoint;
    private final ScalingUtils.ScaleType mToScale;

    public static TransitionSet createTransitionSet(ScalingUtils.ScaleType fromScale, ScalingUtils.ScaleType toScale) {
        return createTransitionSet(fromScale, toScale, null, null);
    }

    public static TransitionSet createTransitionSet(ScalingUtils.ScaleType fromScale, ScalingUtils.ScaleType toScale, @Nullable PointF fromFocusPoint, @Nullable PointF toFocusPoint) {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new DraweeTransition(fromScale, toScale, fromFocusPoint, toFocusPoint));
        return transitionSet;
    }

    public DraweeTransition(ScalingUtils.ScaleType fromScale, ScalingUtils.ScaleType toScale, @Nullable PointF fromFocusPoint, @Nullable PointF toFocusPoint) {
        this.mFromScale = fromScale;
        this.mToScale = toScale;
        this.mFromFocusPoint = fromFocusPoint;
        this.mToFocusPoint = toFocusPoint;
    }

    public DraweeTransition(ScalingUtils.ScaleType fromScale, ScalingUtils.ScaleType toScale) {
        this(fromScale, toScale, null, null);
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.transition.Transition
    @Nullable
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);
        if (startBounds == null || endBounds == null) {
            return null;
        }
        if (this.mFromScale == this.mToScale && this.mFromFocusPoint == this.mToFocusPoint) {
            return null;
        }
        final GenericDraweeView draweeView = (GenericDraweeView) startValues.view;
        final ScalingUtils.InterpolatingScaleType scaleType = new ScalingUtils.InterpolatingScaleType(this.mFromScale, this.mToScale, startBounds, endBounds, this.mFromFocusPoint, this.mToFocusPoint);
        draweeView.getHierarchy().setActualImageScaleType(scaleType);
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.facebook.drawee.view.DraweeTransition.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleType.setValue(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.facebook.drawee.view.DraweeTransition.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                draweeView.getHierarchy().setActualImageScaleType(DraweeTransition.this.mToScale);
                if (DraweeTransition.this.mToFocusPoint != null) {
                    draweeView.getHierarchy().setActualImageFocusPoint(DraweeTransition.this.mToFocusPoint);
                }
            }
        });
        return animator;
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof GenericDraweeView) {
            transitionValues.values.put(PROPNAME_BOUNDS, new Rect(0, 0, transitionValues.view.getWidth(), transitionValues.view.getHeight()));
        }
    }
}
