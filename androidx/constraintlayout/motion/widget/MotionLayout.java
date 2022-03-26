package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.constraintlayout.motion.utils.StopLogic;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Flow;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.R;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/* loaded from: classes.dex */
public class MotionLayout extends ConstraintLayout implements NestedScrollingParent3 {
    private static final boolean DEBUG;
    public static final int DEBUG_SHOW_NONE;
    public static final int DEBUG_SHOW_PATH;
    public static final int DEBUG_SHOW_PROGRESS;
    private static final float EPSILON;
    public static boolean IS_IN_EDIT_MODE;
    static final int MAX_KEY_FRAMES;
    static final String TAG;
    public static final int TOUCH_UP_COMPLETE;
    public static final int TOUCH_UP_COMPLETE_TO_END;
    public static final int TOUCH_UP_COMPLETE_TO_START;
    public static final int TOUCH_UP_DECELERATE;
    public static final int TOUCH_UP_DECELERATE_AND_COMPLETE;
    public static final int TOUCH_UP_STOP;
    public static final int VELOCITY_LAYOUT;
    public static final int VELOCITY_POST_LAYOUT;
    public static final int VELOCITY_STATIC_LAYOUT;
    public static final int VELOCITY_STATIC_POST_LAYOUT;
    private float lastPos;
    private float lastY;
    private DesignTool mDesignTool;
    DevModeDraw mDevModeDraw;
    int mEndWrapHeight;
    int mEndWrapWidth;
    int mHeightMeasureMode;
    Interpolator mInterpolator;
    int mLastLayoutHeight;
    int mLastLayoutWidth;
    int mOldHeight;
    int mOldWidth;
    float mPostInterpolationPosition;
    MotionScene mScene;
    float mScrollTargetDT;
    float mScrollTargetDX;
    float mScrollTargetDY;
    long mScrollTargetTime;
    int mStartWrapHeight;
    int mStartWrapWidth;
    private StateCache mStateCache;
    private boolean mTransitionInstantly;
    private long mTransitionLastTime;
    private TransitionListener mTransitionListener;
    int mWidthMeasureMode;
    float mLastVelocity = 0.0f;
    private int mBeginState = -1;
    int mCurrentState = -1;
    private int mEndState = -1;
    private int mLastWidthMeasureSpec = 0;
    private int mLastHeightMeasureSpec = 0;
    private boolean mInteractionEnabled = true;
    HashMap<View, MotionController> mFrameArrayList = new HashMap<>();
    private long mAnimationStartTime = 0;
    private float mTransitionDuration = 1.0f;
    float mTransitionPosition = 0.0f;
    float mTransitionLastPosition = 0.0f;
    float mTransitionGoalPosition = 0.0f;
    boolean mInTransition = false;
    boolean mIndirectTransition = false;
    int mDebugPath = 0;
    private boolean mTemporalInterpolator = false;
    private StopLogic mStopLogic = new StopLogic();
    private DecelerateInterpolator mDecelerateLogic = new DecelerateInterpolator();
    boolean firstDown = true;
    boolean mUndergoingMotion = false;
    private boolean mKeepAnimating = false;
    private ArrayList<MotionHelper> mOnShowHelpers = null;
    private ArrayList<MotionHelper> mOnHideHelpers = null;
    private ArrayList<TransitionListener> mTransitionListeners = null;
    private int mFrames = 0;
    private long mLastDrawTime = -1;
    private float mLastFps = 0.0f;
    private int mListenerState = 0;
    private float mListenerPosition = 0.0f;
    boolean mIsAnimating = false;
    protected boolean mMeasureDuringTransition = false;
    private KeyCache mKeyCache = new KeyCache();
    private boolean mInLayout = false;
    TransitionState mTransitionState = TransitionState.UNDEFINED;
    Model mModel = new Model();
    private boolean mNeedsFireTransitionCompleted = false;
    private RectF mBoundsCheck = new RectF();
    private View mRegionView = null;
    ArrayList<Integer> mTransitionCompleted = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface MotionTracker {
        void addMovement(MotionEvent motionEvent);

        void clear();

        void computeCurrentVelocity(int i);

        void computeCurrentVelocity(int i, float f);

        float getXVelocity();

        float getXVelocity(int i);

        float getYVelocity();

        float getYVelocity(int i);

        void recycle();
    }

    /* loaded from: classes.dex */
    public interface TransitionListener {
        void onTransitionChange(MotionLayout motionLayout, int i, int i2, float f);

        void onTransitionCompleted(MotionLayout motionLayout, int i);

        void onTransitionStarted(MotionLayout motionLayout, int i, int i2);

        void onTransitionTrigger(MotionLayout motionLayout, int i, boolean z, float f);
    }

    /* loaded from: classes.dex */
    public enum TransitionState {
        UNDEFINED,
        SETUP,
        MOVING,
        FINISHED
    }

    public MotionLayout(Context context) {
        super(context);
        init(null);
    }

    public MotionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MotionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected long getNanoTime() {
        return System.nanoTime();
    }

    public MotionTracker obtainVelocityTracker() {
        return MyTracker.obtain();
    }

    public void enableTransition(int transitionID, boolean enable) {
        MotionScene.Transition t = getTransition(transitionID);
        if (enable) {
            t.setEnable(true);
            return;
        }
        if (t == this.mScene.mCurrentTransition) {
            Iterator<MotionScene.Transition> it = this.mScene.getTransitionsWithState(this.mCurrentState).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MotionScene.Transition transition = it.next();
                if (transition.isEnabled()) {
                    this.mScene.mCurrentTransition = transition;
                    break;
                }
            }
        }
        t.setEnable(false);
    }

    public void setState(TransitionState newState) {
        if (newState != TransitionState.FINISHED || this.mCurrentState != -1) {
            TransitionState oldState = this.mTransitionState;
            this.mTransitionState = newState;
            if (oldState == TransitionState.MOVING && newState == TransitionState.MOVING) {
                fireTransitionChange();
            }
            int i = AnonymousClass2.$SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[oldState.ordinal()];
            if (i == 1 || i == 2) {
                if (newState == TransitionState.MOVING) {
                    fireTransitionChange();
                }
                if (newState == TransitionState.FINISHED) {
                    fireTransitionCompleted();
                }
            } else if (i == 3 && newState == TransitionState.FINISHED) {
                fireTransitionCompleted();
            }
        }
    }

    /* renamed from: androidx.constraintlayout.motion.widget.MotionLayout$2 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState = new int[TransitionState.values().length];

        static {
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.UNDEFINED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.MOVING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$motion$widget$MotionLayout$TransitionState[TransitionState.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MyTracker implements MotionTracker {
        private static MyTracker me = new MyTracker();
        VelocityTracker tracker;

        private MyTracker() {
        }

        public static MyTracker obtain() {
            me.tracker = VelocityTracker.obtain();
            return me;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public void recycle() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.tracker = null;
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public void clear() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public void addMovement(MotionEvent event) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.addMovement(event);
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public void computeCurrentVelocity(int units) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(units);
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public void computeCurrentVelocity(int units, float maxVelocity) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(units, maxVelocity);
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public float getXVelocity() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getXVelocity();
            }
            return 0.0f;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public float getYVelocity() {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getYVelocity();
            }
            return 0.0f;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public float getXVelocity(int id) {
            VelocityTracker velocityTracker = this.tracker;
            if (velocityTracker != null) {
                return velocityTracker.getXVelocity(id);
            }
            return 0.0f;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.MotionTracker
        public float getYVelocity(int id) {
            if (this.tracker != null) {
                return getYVelocity(id);
            }
            return 0.0f;
        }
    }

    public void setTransition(int beginId, int endId) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setStartState(beginId);
            this.mStateCache.setEndState(endId);
            return;
        }
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            this.mBeginState = beginId;
            this.mEndState = endId;
            motionScene.setTransition(beginId, endId);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(beginId), this.mScene.getConstraintSet(endId));
            rebuildScene();
            this.mTransitionLastPosition = 0.0f;
            transitionToStart();
        }
    }

    public void setTransition(int transitionId) {
        if (this.mScene != null) {
            MotionScene.Transition transition = getTransition(transitionId);
            int i = this.mCurrentState;
            this.mBeginState = transition.getStartConstraintSetId();
            this.mEndState = transition.getEndConstraintSetId();
            if (!isAttachedToWindow()) {
                if (this.mStateCache == null) {
                    this.mStateCache = new StateCache();
                }
                this.mStateCache.setStartState(this.mBeginState);
                this.mStateCache.setEndState(this.mEndState);
                return;
            }
            float pos = Float.NaN;
            int i2 = this.mCurrentState;
            if (i2 == this.mBeginState) {
                pos = 0.0f;
            } else if (i2 == this.mEndState) {
                pos = 1.0f;
            }
            this.mScene.setTransition(transition);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            rebuildScene();
            this.mTransitionLastPosition = Float.isNaN(pos) ? 0.0f : pos;
            if (Float.isNaN(pos)) {
                Log.v(TAG, Debug.getLocation() + " transitionToStart ");
                transitionToStart();
                return;
            }
            setProgress(pos);
        }
    }

    public void setTransition(MotionScene.Transition transition) {
        this.mScene.setTransition(transition);
        setState(TransitionState.SETUP);
        if (this.mCurrentState == this.mScene.getEndId()) {
            this.mTransitionLastPosition = 1.0f;
            this.mTransitionPosition = 1.0f;
            this.mTransitionGoalPosition = 1.0f;
        } else {
            this.mTransitionLastPosition = 0.0f;
            this.mTransitionPosition = 0.0f;
            this.mTransitionGoalPosition = 0.0f;
        }
        this.mTransitionLastTime = transition.isTransitionFlag(1) ? -1 : getNanoTime();
        int newBeginState = this.mScene.getStartId();
        int newEndState = this.mScene.getEndId();
        if (newBeginState != this.mBeginState || newEndState != this.mEndState) {
            this.mBeginState = newBeginState;
            this.mEndState = newEndState;
            this.mScene.setTransition(this.mBeginState, this.mEndState);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
            this.mModel.setMeasuredId(this.mBeginState, this.mEndState);
            this.mModel.reEvaluateState();
            rebuildScene();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    public void loadLayoutDescription(int motionScene) {
        if (motionScene != 0) {
            try {
                this.mScene = new MotionScene(getContext(), this, motionScene);
                if (Build.VERSION.SDK_INT < 19 || isAttachedToWindow()) {
                    this.mScene.readFallback(this);
                    this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
                    rebuildScene();
                    this.mScene.setRtl(isRtl());
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("unable to parse MotionScene file", ex);
            }
        } else {
            this.mScene = null;
        }
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        if (Build.VERSION.SDK_INT >= 19) {
            return super.isAttachedToWindow();
        }
        return getWindowToken() != null;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    public void setState(int id, int screenWidth, int screenHeight) {
        setState(TransitionState.SETUP);
        this.mCurrentState = id;
        this.mBeginState = -1;
        this.mEndState = -1;
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.updateConstraints(id, (float) screenWidth, (float) screenHeight);
            return;
        }
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.getConstraintSet(id).applyTo(this);
        }
    }

    public void setInterpolatedProgress(float pos) {
        if (this.mScene != null) {
            setState(TransitionState.MOVING);
            Interpolator interpolator = this.mScene.getInterpolator();
            if (interpolator != null) {
                setProgress(interpolator.getInterpolation(pos));
                return;
            }
        }
        setProgress(pos);
    }

    public void setProgress(float pos, float velocity) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            this.mStateCache.setVelocity(velocity);
            return;
        }
        setProgress(pos);
        setState(TransitionState.MOVING);
        this.mLastVelocity = velocity;
        animateTo(1.0f);
    }

    /* loaded from: classes.dex */
    public class StateCache {
        float mProgress = Float.NaN;
        float mVelocity = Float.NaN;
        int startState = -1;
        int endState = -1;
        final String KeyProgress = "motion.progress";
        final String KeyVelocity = "motion.velocity";
        final String KeyStartState = "motion.StartState";
        final String KeyEndState = "motion.EndState";

        StateCache() {
            MotionLayout.this = this$0;
        }

        void apply() {
            if (!(this.startState == -1 && this.endState == -1)) {
                int i = this.startState;
                if (i == -1) {
                    MotionLayout.this.transitionToState(this.endState);
                } else {
                    int i2 = this.endState;
                    if (i2 == -1) {
                        MotionLayout.this.setState(i, -1, -1);
                    } else {
                        MotionLayout.this.setTransition(i, i2);
                    }
                }
                MotionLayout.this.setState(TransitionState.SETUP);
            }
            if (!Float.isNaN(this.mVelocity)) {
                MotionLayout.this.setProgress(this.mProgress, this.mVelocity);
                this.mProgress = Float.NaN;
                this.mVelocity = Float.NaN;
                this.startState = -1;
                this.endState = -1;
            } else if (!Float.isNaN(this.mProgress)) {
                MotionLayout.this.setProgress(this.mProgress);
            }
        }

        public Bundle getTransitionState() {
            Bundle bundle = new Bundle();
            bundle.putFloat("motion.progress", this.mProgress);
            bundle.putFloat("motion.velocity", this.mVelocity);
            bundle.putInt("motion.StartState", this.startState);
            bundle.putInt("motion.EndState", this.endState);
            return bundle;
        }

        public void setTransitionState(Bundle bundle) {
            this.mProgress = bundle.getFloat("motion.progress");
            this.mVelocity = bundle.getFloat("motion.velocity");
            this.startState = bundle.getInt("motion.StartState");
            this.endState = bundle.getInt("motion.EndState");
        }

        public void setProgress(float progress) {
            this.mProgress = progress;
        }

        public void setEndState(int endState) {
            this.endState = endState;
        }

        public void setVelocity(float mVelocity) {
            this.mVelocity = mVelocity;
        }

        public void setStartState(int startState) {
            this.startState = startState;
        }

        public void recordState() {
            this.endState = MotionLayout.this.mEndState;
            this.startState = MotionLayout.this.mBeginState;
            this.mVelocity = MotionLayout.this.getVelocity();
            this.mProgress = MotionLayout.this.getProgress();
        }
    }

    public void setTransitionState(Bundle bundle) {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.setTransitionState(bundle);
        if (isAttachedToWindow()) {
            this.mStateCache.apply();
        }
    }

    public Bundle getTransitionState() {
        if (this.mStateCache == null) {
            this.mStateCache = new StateCache();
        }
        this.mStateCache.recordState();
        return this.mStateCache.getTransitionState();
    }

    public void setProgress(float pos) {
        if (pos < 0.0f || pos > 1.0f) {
            Log.w(TAG, "Warning! Progress is defined for values between 0.0 and 1.0 inclusive");
        }
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setProgress(pos);
            return;
        }
        if (pos <= 0.0f) {
            this.mCurrentState = this.mBeginState;
            if (this.mTransitionLastPosition == 0.0f) {
                setState(TransitionState.FINISHED);
            }
        } else if (pos >= 1.0f) {
            this.mCurrentState = this.mEndState;
            if (this.mTransitionLastPosition == 1.0f) {
                setState(TransitionState.FINISHED);
            }
        } else {
            this.mCurrentState = -1;
            setState(TransitionState.MOVING);
        }
        if (this.mScene != null) {
            this.mTransitionInstantly = true;
            this.mTransitionGoalPosition = pos;
            this.mTransitionPosition = pos;
            this.mTransitionLastTime = -1;
            this.mAnimationStartTime = -1;
            this.mInterpolator = null;
            this.mInTransition = true;
            invalidate();
        }
    }

    public void setupMotionViews() {
        MotionLayout motionLayout = this;
        int n = getChildCount();
        motionLayout.mModel.build();
        boolean flip = true;
        motionLayout.mInTransition = true;
        int layoutWidth = getWidth();
        int layoutHeight = getHeight();
        int arc = motionLayout.mScene.gatPathMotionArc();
        if (arc != -1) {
            for (int i = 0; i < n; i++) {
                MotionController motionController = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i));
                if (motionController != null) {
                    motionController.setPathMotionArc(arc);
                }
            }
        }
        for (int i2 = 0; i2 < n; i2++) {
            MotionController motionController2 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i2));
            if (motionController2 != null) {
                motionLayout.mScene.getKeyFrames(motionController2);
                motionController2.setup(layoutWidth, layoutHeight, motionLayout.mTransitionDuration, getNanoTime());
            }
        }
        float stagger = motionLayout.mScene.getStaggered();
        if (stagger != 0.0f) {
            if (((double) stagger) >= 0.0d) {
                flip = false;
            }
            boolean useMotionStagger = false;
            float stagger2 = Math.abs(stagger);
            float min = Float.MAX_VALUE;
            float max = -3.4028235E38f;
            int i3 = 0;
            while (true) {
                if (i3 >= n) {
                    break;
                }
                MotionController f = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i3));
                if (!Float.isNaN(f.mMotionStagger)) {
                    useMotionStagger = true;
                    break;
                }
                float x = f.getFinalX();
                float y = f.getFinalY();
                float mdist = flip ? y - x : y + x;
                min = Math.min(min, mdist);
                max = Math.max(max, mdist);
                i3++;
            }
            if (useMotionStagger) {
                float min2 = Float.MAX_VALUE;
                float max2 = -3.4028235E38f;
                for (int i4 = 0; i4 < n; i4++) {
                    MotionController f2 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i4));
                    if (!Float.isNaN(f2.mMotionStagger)) {
                        min2 = Math.min(min2, f2.mMotionStagger);
                        max2 = Math.max(max2, f2.mMotionStagger);
                    }
                }
                for (int i5 = 0; i5 < n; i5++) {
                    MotionController f3 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i5));
                    if (!Float.isNaN(f3.mMotionStagger)) {
                        f3.mStaggerScale = 1.0f / (1.0f - stagger2);
                        if (flip) {
                            f3.mStaggerOffset = stagger2 - (((max2 - f3.mMotionStagger) / (max2 - min2)) * stagger2);
                        } else {
                            f3.mStaggerOffset = stagger2 - (((f3.mMotionStagger - min2) * stagger2) / (max2 - min2));
                        }
                    }
                }
                return;
            }
            int i6 = 0;
            while (i6 < n) {
                MotionController f4 = motionLayout.mFrameArrayList.get(motionLayout.getChildAt(i6));
                float x2 = f4.getFinalX();
                float y2 = f4.getFinalY();
                float mdist2 = flip ? y2 - x2 : y2 + x2;
                f4.mStaggerScale = 1.0f / (1.0f - stagger2);
                f4.mStaggerOffset = stagger2 - (((mdist2 - min) * stagger2) / (max - min));
                i6++;
                motionLayout = this;
            }
        }
    }

    public void touchAnimateTo(int touchUpMode, float position, float currentVelocity) {
        if (this.mScene != null && this.mTransitionLastPosition != position) {
            this.mTemporalInterpolator = true;
            this.mAnimationStartTime = getNanoTime();
            this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
            this.mTransitionGoalPosition = position;
            this.mInTransition = true;
            if (touchUpMode == 0 || touchUpMode == 1 || touchUpMode == 2) {
                if (touchUpMode == 1) {
                    position = 0.0f;
                } else if (touchUpMode == 2) {
                    position = 1.0f;
                }
                this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                int currentState = this.mCurrentState;
                this.mTransitionGoalPosition = position;
                this.mCurrentState = currentState;
                this.mInterpolator = this.mStopLogic;
            } else if (touchUpMode != 3) {
                if (touchUpMode == 4) {
                    this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                    this.mInterpolator = this.mDecelerateLogic;
                } else if (touchUpMode == 5) {
                    if (willJump(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration())) {
                        this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                        this.mInterpolator = this.mDecelerateLogic;
                    } else {
                        this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                        this.mLastVelocity = 0.0f;
                        int currentState2 = this.mCurrentState;
                        this.mTransitionGoalPosition = position;
                        this.mCurrentState = currentState2;
                        this.mInterpolator = this.mStopLogic;
                    }
                }
            }
            this.mTransitionInstantly = false;
            this.mAnimationStartTime = getNanoTime();
            invalidate();
        }
    }

    private static boolean willJump(float velocity, float position, float maxAcceleration) {
        if (velocity > 0.0f) {
            float time = velocity / maxAcceleration;
            return position + ((velocity * time) - (((maxAcceleration * time) * time) / 2.0f)) > 1.0f;
        }
        float time2 = (-velocity) / maxAcceleration;
        return position + ((velocity * time2) + (((maxAcceleration * time2) * time2) / 2.0f)) < 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class DecelerateInterpolator extends MotionInterpolator {
        float maxA;
        float initalV = 0.0f;
        float currentP = 0.0f;

        DecelerateInterpolator() {
            MotionLayout.this = this$0;
        }

        public void config(float velocity, float position, float maxAcceleration) {
            this.initalV = velocity;
            this.currentP = position;
            this.maxA = maxAcceleration;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionInterpolator, android.animation.TimeInterpolator
        public float getInterpolation(float time) {
            float f = this.initalV;
            if (f > 0.0f) {
                float f2 = this.maxA;
                if (f / f2 < time) {
                    time = f / f2;
                }
                MotionLayout motionLayout = MotionLayout.this;
                float f3 = this.initalV;
                float f4 = this.maxA;
                motionLayout.mLastVelocity = f3 - (f4 * time);
                return this.currentP + ((f3 * time) - (((f4 * time) * time) / 2.0f));
            }
            float f5 = this.maxA;
            if ((-f) / f5 < time) {
                time = (-f) / f5;
            }
            MotionLayout motionLayout2 = MotionLayout.this;
            float f6 = this.initalV;
            float f7 = this.maxA;
            motionLayout2.mLastVelocity = (f7 * time) + f6;
            return this.currentP + (f6 * time) + (((f7 * time) * time) / 2.0f);
        }

        @Override // androidx.constraintlayout.motion.widget.MotionInterpolator
        public float getVelocity() {
            return MotionLayout.this.mLastVelocity;
        }
    }

    void animateTo(float position) {
        if (this.mScene != null) {
            float f = this.mTransitionLastPosition;
            float f2 = this.mTransitionPosition;
            if (f != f2 && this.mTransitionInstantly) {
                this.mTransitionLastPosition = f2;
            }
            if (this.mTransitionLastPosition != position) {
                this.mTemporalInterpolator = false;
                float currentPosition = this.mTransitionLastPosition;
                this.mTransitionGoalPosition = position;
                this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
                setProgress(this.mTransitionGoalPosition);
                this.mInterpolator = this.mScene.getInterpolator();
                this.mTransitionInstantly = false;
                this.mAnimationStartTime = getNanoTime();
                this.mInTransition = true;
                this.mTransitionPosition = currentPosition;
                this.mTransitionLastPosition = currentPosition;
                invalidate();
            }
        }
    }

    private void computeCurrentPositions() {
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            View v = getChildAt(i);
            MotionController frame = this.mFrameArrayList.get(v);
            if (frame != null) {
                frame.setStartCurrentState(v);
            }
        }
    }

    public void transitionToStart() {
        animateTo(0.0f);
    }

    public void transitionToEnd() {
        animateTo(1.0f);
    }

    public void transitionToState(int id) {
        if (!isAttachedToWindow()) {
            if (this.mStateCache == null) {
                this.mStateCache = new StateCache();
            }
            this.mStateCache.setEndState(id);
            return;
        }
        transitionToState(id, -1, -1);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002d  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void transitionToState(int id, int screenWidth, int screenHeight) {
        int id2;
        int i;
        int i2;
        MotionScene motionScene = this.mScene;
        if (motionScene == null || motionScene.mStateSet == null) {
            i2 = id;
        } else {
            i2 = id;
            int tmp_id = this.mScene.mStateSet.convertToConstraintSet(this.mCurrentState, i2, (float) screenWidth, (float) screenHeight);
            if (tmp_id != -1) {
                id2 = tmp_id;
                i = this.mCurrentState;
                if (i != id2) {
                    return;
                }
                if (this.mBeginState == id2) {
                    animateTo(0.0f);
                    return;
                } else if (this.mEndState == id2) {
                    animateTo(1.0f);
                    return;
                } else {
                    this.mEndState = id2;
                    if (i != -1) {
                        setTransition(i, id2);
                        animateTo(1.0f);
                        this.mTransitionLastPosition = 0.0f;
                        transitionToEnd();
                        return;
                    }
                    this.mTemporalInterpolator = false;
                    this.mTransitionGoalPosition = 1.0f;
                    this.mTransitionPosition = 0.0f;
                    this.mTransitionLastPosition = 0.0f;
                    this.mTransitionLastTime = getNanoTime();
                    this.mAnimationStartTime = getNanoTime();
                    this.mTransitionInstantly = false;
                    this.mInterpolator = null;
                    this.mTransitionDuration = ((float) this.mScene.getDuration()) / 1000.0f;
                    this.mBeginState = -1;
                    this.mScene.setTransition(this.mBeginState, this.mEndState);
                    this.mScene.getStartId();
                    int n = getChildCount();
                    this.mFrameArrayList.clear();
                    for (int i3 = 0; i3 < n; i3++) {
                        View v = getChildAt(i3);
                        this.mFrameArrayList.put(v, new MotionController(v));
                    }
                    this.mInTransition = true;
                    this.mModel.initFrom(this.mLayoutWidget, null, this.mScene.getConstraintSet(id2));
                    rebuildScene();
                    this.mModel.build();
                    computeCurrentPositions();
                    int layoutWidth = getWidth();
                    int layoutHeight = getHeight();
                    for (int i4 = 0; i4 < n; i4++) {
                        MotionController motionController = this.mFrameArrayList.get(getChildAt(i4));
                        this.mScene.getKeyFrames(motionController);
                        motionController.setup(layoutWidth, layoutHeight, this.mTransitionDuration, getNanoTime());
                    }
                    float stagger = this.mScene.getStaggered();
                    if (stagger != 0.0f) {
                        float min = Float.MAX_VALUE;
                        float max = -3.4028235E38f;
                        for (int i5 = 0; i5 < n; i5++) {
                            MotionController f = this.mFrameArrayList.get(getChildAt(i5));
                            float x = f.getFinalX();
                            float y = f.getFinalY();
                            min = Math.min(min, y + x);
                            max = Math.max(max, y + x);
                        }
                        int i6 = 0;
                        while (i6 < n) {
                            MotionController f2 = this.mFrameArrayList.get(getChildAt(i6));
                            float x2 = f2.getFinalX();
                            float y2 = f2.getFinalY();
                            f2.mStaggerScale = 1.0f / (1.0f - stagger);
                            f2.mStaggerOffset = stagger - ((((x2 + y2) - min) * stagger) / (max - min));
                            i6++;
                            layoutWidth = layoutWidth;
                        }
                    }
                    this.mTransitionPosition = 0.0f;
                    this.mTransitionLastPosition = 0.0f;
                    this.mInTransition = true;
                    invalidate();
                    return;
                }
            }
        }
        id2 = i2;
        i = this.mCurrentState;
        if (i != id2) {
        }
    }

    public float getVelocity() {
        return this.mLastVelocity;
    }

    public void getViewVelocity(View view, float posOnViewX, float posOnViewY, float[] returnVelocity, int type) {
        float position;
        float v = this.mLastVelocity;
        float position2 = this.mTransitionLastPosition;
        if (this.mInterpolator != null) {
            float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            float interpos = this.mInterpolator.getInterpolation(this.mTransitionLastPosition + EPSILON);
            float position3 = this.mInterpolator.getInterpolation(this.mTransitionLastPosition);
            v = (dir * ((interpos - position3) / EPSILON)) / this.mTransitionDuration;
            position = position3;
        } else {
            position = position2;
        }
        Interpolator interpolator = this.mInterpolator;
        if (interpolator instanceof MotionInterpolator) {
            v = ((MotionInterpolator) interpolator).getVelocity();
        }
        MotionController f = this.mFrameArrayList.get(view);
        if ((type & 1) == 0) {
            f.getPostLayoutDvDp(position, view.getWidth(), view.getHeight(), posOnViewX, posOnViewY, returnVelocity);
        } else {
            f.getDpDt(position, posOnViewX, posOnViewY, returnVelocity);
        }
        if (type < 2) {
            returnVelocity[0] = returnVelocity[0] * v;
            returnVelocity[1] = returnVelocity[1] * v;
        }
    }

    /* loaded from: classes.dex */
    public class Model {
        int mEndId;
        int mStartId;
        ConstraintWidgetContainer mLayoutStart = new ConstraintWidgetContainer();
        ConstraintWidgetContainer mLayoutEnd = new ConstraintWidgetContainer();
        ConstraintSet mStart = null;
        ConstraintSet mEnd = null;

        Model() {
            MotionLayout.this = this$0;
        }

        void copy(ConstraintWidgetContainer src, ConstraintWidgetContainer dest) {
            ConstraintWidget child_d;
            ArrayList<ConstraintWidget> children = src.getChildren();
            HashMap<ConstraintWidget, ConstraintWidget> map = new HashMap<>();
            map.put(src, dest);
            dest.getChildren().clear();
            dest.copy(src, map);
            Iterator<ConstraintWidget> it = children.iterator();
            while (it.hasNext()) {
                ConstraintWidget child_s = it.next();
                if (child_s instanceof Barrier) {
                    child_d = new Barrier();
                } else if (child_s instanceof Guideline) {
                    child_d = new Guideline();
                } else if (child_s instanceof Flow) {
                    child_d = new Flow();
                } else if (child_s instanceof Helper) {
                    child_d = new HelperWidget();
                } else {
                    child_d = new ConstraintWidget();
                }
                dest.add(child_d);
                map.put(child_s, child_d);
            }
            Iterator<ConstraintWidget> it2 = children.iterator();
            while (it2.hasNext()) {
                ConstraintWidget child_s2 = it2.next();
                map.get(child_s2).copy(child_s2, map);
            }
        }

        void initFrom(ConstraintWidgetContainer baseLayout, ConstraintSet start, ConstraintSet end) {
            this.mStart = start;
            this.mEnd = end;
            this.mLayoutStart = new ConstraintWidgetContainer();
            this.mLayoutEnd = new ConstraintWidgetContainer();
            this.mLayoutStart.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutEnd.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutStart.removeAllChildren();
            this.mLayoutEnd.removeAllChildren();
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
            copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
            if (((double) MotionLayout.this.mTransitionLastPosition) > 0.5d) {
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
                setupConstraintWidget(this.mLayoutEnd, end);
            } else {
                setupConstraintWidget(this.mLayoutEnd, end);
                if (start != null) {
                    setupConstraintWidget(this.mLayoutStart, start);
                }
            }
            this.mLayoutStart.setRtl(MotionLayout.this.isRtl());
            this.mLayoutStart.updateHierarchy();
            this.mLayoutEnd.setRtl(MotionLayout.this.isRtl());
            this.mLayoutEnd.updateHierarchy();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    this.mLayoutStart.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
                if (layoutParams.height == -2) {
                    this.mLayoutStart.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }

        private void setupConstraintWidget(ConstraintWidgetContainer base, ConstraintSet cset) {
            SparseArray<ConstraintWidget> mapIdToWidget = new SparseArray<>();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            mapIdToWidget.clear();
            mapIdToWidget.put(0, base);
            mapIdToWidget.put(MotionLayout.this.getId(), base);
            Iterator<ConstraintWidget> it = base.getChildren().iterator();
            while (it.hasNext()) {
                ConstraintWidget child = it.next();
                mapIdToWidget.put(((View) child.getCompanionWidget()).getId(), child);
            }
            Iterator<ConstraintWidget> it2 = base.getChildren().iterator();
            while (it2.hasNext()) {
                ConstraintWidget child2 = it2.next();
                View view = (View) child2.getCompanionWidget();
                cset.applyToLayoutParams(view.getId(), layoutParams);
                child2.setWidth(cset.getWidth(view.getId()));
                child2.setHeight(cset.getHeight(view.getId()));
                if (view instanceof ConstraintHelper) {
                    cset.applyToHelper((ConstraintHelper) view, child2, layoutParams, mapIdToWidget);
                    if (view instanceof androidx.constraintlayout.widget.Barrier) {
                        ((androidx.constraintlayout.widget.Barrier) view).validateParams();
                    }
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                } else {
                    layoutParams.resolveLayoutDirection(0);
                }
                MotionLayout.this.applyConstraintsFromLayoutParams(false, view, child2, layoutParams, mapIdToWidget);
                if (cset.getVisibilityMode(view.getId()) == 1) {
                    child2.setVisibility(view.getVisibility());
                } else {
                    child2.setVisibility(cset.getVisibility(view.getId()));
                }
            }
            Iterator<ConstraintWidget> it3 = base.getChildren().iterator();
            while (it3.hasNext()) {
                ConstraintWidget child3 = it3.next();
                if (child3 instanceof VirtualLayout) {
                    Helper helper = (Helper) child3;
                    ((ConstraintHelper) child3.getCompanionWidget()).updatePreLayout(base, helper, mapIdToWidget);
                    ((VirtualLayout) helper).captureWidgets();
                }
            }
        }

        ConstraintWidget getWidget(ConstraintWidgetContainer container, View view) {
            if (container.getCompanionWidget() == view) {
                return container;
            }
            ArrayList<ConstraintWidget> children = container.getChildren();
            int count = children.size();
            for (int i = 0; i < count; i++) {
                ConstraintWidget widget = children.get(i);
                if (widget.getCompanionWidget() == view) {
                    return widget;
                }
            }
            return null;
        }

        private void debugLayoutParam(String str, ConstraintLayout.LayoutParams params) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(params.startToStart != -1 ? "SS" : "__");
            String a2 = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(a2);
            String str2 = "|__";
            sb2.append(params.startToEnd != -1 ? "|SE" : str2);
            String a3 = sb2.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(a3);
            sb3.append(params.endToStart != -1 ? "|ES" : str2);
            String a4 = sb3.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(a4);
            sb4.append(params.endToEnd != -1 ? "|EE" : str2);
            String a5 = sb4.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(a5);
            sb5.append(params.leftToLeft != -1 ? "|LL" : str2);
            String a6 = sb5.toString();
            StringBuilder sb6 = new StringBuilder();
            sb6.append(a6);
            sb6.append(params.leftToRight != -1 ? "|LR" : str2);
            String a7 = sb6.toString();
            StringBuilder sb7 = new StringBuilder();
            sb7.append(a7);
            sb7.append(params.rightToLeft != -1 ? "|RL" : str2);
            String a8 = sb7.toString();
            StringBuilder sb8 = new StringBuilder();
            sb8.append(a8);
            sb8.append(params.rightToRight != -1 ? "|RR" : str2);
            String a9 = sb8.toString();
            StringBuilder sb9 = new StringBuilder();
            sb9.append(a9);
            sb9.append(params.topToTop != -1 ? "|TT" : str2);
            String a10 = sb9.toString();
            StringBuilder sb10 = new StringBuilder();
            sb10.append(a10);
            sb10.append(params.topToBottom != -1 ? "|TB" : str2);
            String a11 = sb10.toString();
            StringBuilder sb11 = new StringBuilder();
            sb11.append(a11);
            sb11.append(params.bottomToTop != -1 ? "|BT" : str2);
            String a12 = sb11.toString();
            StringBuilder sb12 = new StringBuilder();
            sb12.append(a12);
            if (params.bottomToBottom != -1) {
                str2 = "|BB";
            }
            sb12.append(str2);
            String a13 = sb12.toString();
            Log.v(MotionLayout.TAG, str + a13);
        }

        private void debugWidget(String str, ConstraintWidget child) {
            String str2;
            String str3;
            String str4;
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            String str5 = "B";
            String str6 = "__";
            if (child.mTop.mTarget != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(ExifInterface.GPS_DIRECTION_TRUE);
                sb2.append(child.mTop.mTarget.mType == ConstraintAnchor.Type.TOP ? ExifInterface.GPS_DIRECTION_TRUE : str5);
                str2 = sb2.toString();
            } else {
                str2 = str6;
            }
            sb.append(str2);
            String a2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(a2);
            if (child.mBottom.mTarget != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str5);
                if (child.mBottom.mTarget.mType == ConstraintAnchor.Type.TOP) {
                    str5 = ExifInterface.GPS_DIRECTION_TRUE;
                }
                sb4.append(str5);
                str3 = sb4.toString();
            } else {
                str3 = str6;
            }
            sb3.append(str3);
            String a3 = sb3.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(a3);
            String str7 = "R";
            if (child.mLeft.mTarget != null) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("L");
                sb6.append(child.mLeft.mTarget.mType == ConstraintAnchor.Type.LEFT ? "L" : str7);
                str4 = sb6.toString();
            } else {
                str4 = str6;
            }
            sb5.append(str4);
            String a4 = sb5.toString();
            StringBuilder sb7 = new StringBuilder();
            sb7.append(a4);
            if (child.mRight.mTarget != null) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str7);
                if (child.mRight.mTarget.mType == ConstraintAnchor.Type.LEFT) {
                    str7 = "L";
                }
                sb8.append(str7);
                str6 = sb8.toString();
            }
            sb7.append(str6);
            String a5 = sb7.toString();
            Log.v(MotionLayout.TAG, str + a5 + " ---  " + child);
        }

        private void debugLayout(String title, ConstraintWidgetContainer c) {
            String cName = title + " " + Debug.getName((View) c.getCompanionWidget());
            Log.v(MotionLayout.TAG, cName + "  ========= " + c);
            int count = c.getChildren().size();
            for (int i = 0; i < count; i++) {
                String str = cName + "[" + i + "] ";
                ConstraintWidget child = c.getChildren().get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("");
                String str2 = "_";
                sb.append(child.mTop.mTarget != null ? ExifInterface.GPS_DIRECTION_TRUE : str2);
                String a2 = sb.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(a2);
                sb2.append(child.mBottom.mTarget != null ? "B" : str2);
                String a3 = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(a3);
                sb3.append(child.mLeft.mTarget != null ? "L" : str2);
                String a4 = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(a4);
                if (child.mRight.mTarget != null) {
                    str2 = "R";
                }
                sb4.append(str2);
                String a5 = sb4.toString();
                View v = (View) child.getCompanionWidget();
                String name = Debug.getName(v);
                if (v instanceof TextView) {
                    name = name + "(" + ((Object) ((TextView) v).getText()) + ")";
                }
                Log.v(MotionLayout.TAG, str + "  " + name + " " + child + " " + a5);
            }
            Log.v(MotionLayout.TAG, cName + " done. ");
        }

        public void reEvaluateState() {
            measure(MotionLayout.this.mLastWidthMeasureSpec, MotionLayout.this.mLastHeightMeasureSpec);
            MotionLayout.this.setupMotionViews();
        }

        public void measure(int widthMeasureSpec, int heightMeasureSpec) {
            boolean recompute_start_end_size;
            int width;
            int height;
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            MotionLayout motionLayout = MotionLayout.this;
            motionLayout.mWidthMeasureMode = widthMode;
            motionLayout.mHeightMeasureMode = heightMode;
            int optimisationLevel = motionLayout.getOptimizationLevel();
            if (MotionLayout.this.mCurrentState == MotionLayout.this.getStartState()) {
                MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                if (this.mStart != null) {
                    MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                }
            } else {
                if (this.mStart != null) {
                    MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                }
                MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
            }
            if ((MotionLayout.this.getParent() instanceof MotionLayout) && widthMode == 1073741824 && heightMode == 1073741824) {
                recompute_start_end_size = false;
            } else {
                recompute_start_end_size = true;
            }
            if (recompute_start_end_size) {
                MotionLayout motionLayout2 = MotionLayout.this;
                motionLayout2.mWidthMeasureMode = widthMode;
                motionLayout2.mHeightMeasureMode = heightMode;
                if (motionLayout2.mCurrentState == MotionLayout.this.getStartState()) {
                    MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                    if (this.mStart != null) {
                        MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                    }
                } else {
                    if (this.mStart != null) {
                        MotionLayout.this.resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                    }
                    MotionLayout.this.resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                }
                MotionLayout.this.mStartWrapWidth = this.mLayoutStart.getWidth();
                MotionLayout.this.mStartWrapHeight = this.mLayoutStart.getHeight();
                MotionLayout.this.mEndWrapWidth = this.mLayoutEnd.getWidth();
                MotionLayout.this.mEndWrapHeight = this.mLayoutEnd.getHeight();
                MotionLayout motionLayout3 = MotionLayout.this;
                motionLayout3.mMeasureDuringTransition = (motionLayout3.mStartWrapWidth == MotionLayout.this.mEndWrapWidth && MotionLayout.this.mStartWrapHeight == MotionLayout.this.mEndWrapHeight) ? false : true;
            }
            int width2 = MotionLayout.this.mStartWrapWidth;
            int height2 = MotionLayout.this.mStartWrapHeight;
            if (MotionLayout.this.mWidthMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mWidthMeasureMode == 0) {
                width = (int) (((float) MotionLayout.this.mStartWrapWidth) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapWidth - MotionLayout.this.mStartWrapWidth))));
            } else {
                width = width2;
            }
            if (MotionLayout.this.mHeightMeasureMode == Integer.MIN_VALUE || MotionLayout.this.mHeightMeasureMode == 0) {
                height = (int) (((float) MotionLayout.this.mStartWrapHeight) + (MotionLayout.this.mPostInterpolationPosition * ((float) (MotionLayout.this.mEndWrapHeight - MotionLayout.this.mStartWrapHeight))));
            } else {
                height = height2;
            }
            MotionLayout.this.resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, width, height, this.mLayoutStart.isWidthMeasuredTooSmall() || this.mLayoutEnd.isWidthMeasuredTooSmall(), this.mLayoutStart.isHeightMeasuredTooSmall() || this.mLayoutEnd.isHeightMeasuredTooSmall());
        }

        public void build() {
            int n = MotionLayout.this.getChildCount();
            MotionLayout.this.mFrameArrayList.clear();
            for (int i = 0; i < n; i++) {
                View v = MotionLayout.this.getChildAt(i);
                MotionLayout.this.mFrameArrayList.put(v, new MotionController(v));
            }
            for (int i2 = 0; i2 < n; i2++) {
                View v2 = MotionLayout.this.getChildAt(i2);
                MotionController motionController = MotionLayout.this.mFrameArrayList.get(v2);
                if (motionController != null) {
                    if (this.mStart != null) {
                        ConstraintWidget startWidget = getWidget(this.mLayoutStart, v2);
                        if (startWidget != null) {
                            motionController.setStartState(startWidget, this.mStart);
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            Log.e(MotionLayout.TAG, Debug.getLocation() + "no widget for  " + Debug.getName(v2) + " (" + v2.getClass().getName() + ")");
                        }
                    }
                    if (this.mEnd != null) {
                        ConstraintWidget endWidget = getWidget(this.mLayoutEnd, v2);
                        if (endWidget != null) {
                            motionController.setEndState(endWidget, this.mEnd);
                        } else if (MotionLayout.this.mDebugPath != 0) {
                            Log.e(MotionLayout.TAG, Debug.getLocation() + "no widget for  " + Debug.getName(v2) + " (" + v2.getClass().getName() + ")");
                        }
                    }
                }
            }
        }

        public void setMeasuredId(int startId, int endId) {
            this.mStartId = startId;
            this.mEndId = endId;
        }

        public boolean isNotConfiguredWith(int startId, int endId) {
            return (startId == this.mStartId && endId == this.mEndId) ? false : true;
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewParent, android.view.View
    public void requestLayout() {
        MotionScene motionScene;
        if (this.mMeasureDuringTransition || this.mCurrentState != -1 || (motionScene = this.mScene) == null || motionScene.mCurrentTransition == null || this.mScene.mCurrentTransition.getLayoutDuringTransition() != 0) {
            super.requestLayout();
        }
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        Context context = getContext();
        return Debug.getName(context, this.mBeginState) + "->" + Debug.getName(context, this.mEndState) + " (pos:" + this.mTransitionLastPosition + " Dpos/Dt:" + this.mLastVelocity;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mScene == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        boolean recalc = (this.mLastWidthMeasureSpec == widthMeasureSpec && this.mLastHeightMeasureSpec == heightMeasureSpec) ? false : true;
        if (this.mNeedsFireTransitionCompleted) {
            this.mNeedsFireTransitionCompleted = false;
            onNewStateAttachHandlers();
            processTransitionCompleted();
            recalc = true;
        }
        if (this.mDirtyHierarchy) {
            recalc = true;
        }
        this.mLastWidthMeasureSpec = widthMeasureSpec;
        this.mLastHeightMeasureSpec = heightMeasureSpec;
        int startId = this.mScene.getStartId();
        int endId = this.mScene.getEndId();
        boolean setMeasure = true;
        if ((recalc || this.mModel.isNotConfiguredWith(startId, endId)) && this.mBeginState != -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(startId), this.mScene.getConstraintSet(endId));
            this.mModel.reEvaluateState();
            this.mModel.setMeasuredId(startId, endId);
            setMeasure = false;
        }
        if (this.mMeasureDuringTransition || setMeasure) {
            int heightPadding = getPaddingTop() + getPaddingBottom();
            int androidLayoutWidth = this.mLayoutWidget.getWidth() + getPaddingLeft() + getPaddingRight();
            int androidLayoutHeight = this.mLayoutWidget.getHeight() + heightPadding;
            int i = this.mWidthMeasureMode;
            if (i == Integer.MIN_VALUE || i == 0) {
                int i2 = this.mStartWrapWidth;
                androidLayoutWidth = (int) (((float) i2) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapWidth - i2))));
                requestLayout();
            }
            int i3 = this.mHeightMeasureMode;
            if (i3 == Integer.MIN_VALUE || i3 == 0) {
                int i4 = this.mStartWrapHeight;
                androidLayoutHeight = (int) (((float) i4) + (this.mPostInterpolationPosition * ((float) (this.mEndWrapHeight - i4))));
                requestLayout();
            }
            setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
        }
        evaluateLayout();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null || motionScene.mCurrentTransition == null || this.mScene.mCurrentTransition.getTouchResponse() == null || (this.mScene.mCurrentTransition.getTouchResponse().getFlags() & 2) != 0) {
            return false;
        }
        return true;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View target, int type) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            float f = this.mScrollTargetDX;
            float f2 = this.mScrollTargetDT;
            motionScene.processScrollUp(f / f2, this.mScrollTargetDY / f2);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        if (!(!this.mUndergoingMotion && dxConsumed == 0 && dyConsumed == 0)) {
            consumed[0] = consumed[0] + dxUnconsumed;
            consumed[1] = consumed[1] + dyUnconsumed;
        }
        this.mUndergoingMotion = false;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    /* JADX INFO: Multiple debug info for r1v2 float: [D('dir' float), D('progress' float)] */
    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(final View target, int dx, int dy, int[] consumed, int type) {
        TouchResponse touchResponse;
        int regionId;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && motionScene.mCurrentTransition != null && this.mScene.mCurrentTransition.isEnabled()) {
            MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
            if (currentTransition == null || !currentTransition.isEnabled() || (touchResponse = currentTransition.getTouchResponse()) == null || (regionId = touchResponse.getTouchRegionId()) == -1 || target.getId() == regionId) {
                MotionScene motionScene2 = this.mScene;
                if (motionScene2 != null && motionScene2.getMoveWhenScrollAtTop()) {
                    float f = this.mTransitionPosition;
                    if ((f == 1.0f || f == 0.0f) && target.canScrollVertically(-1)) {
                        return;
                    }
                }
                if (!(currentTransition.getTouchResponse() == null || (this.mScene.mCurrentTransition.getTouchResponse().getFlags() & 1) == 0)) {
                    float dir = this.mScene.getProgressDirection((float) dx, (float) dy);
                    if ((this.mTransitionLastPosition <= 0.0f && dir < 0.0f) || (this.mTransitionLastPosition >= 1.0f && dir > 0.0f)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            target.setNestedScrollingEnabled(false);
                            target.post(new Runnable() { // from class: androidx.constraintlayout.motion.widget.MotionLayout.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    target.setNestedScrollingEnabled(true);
                                }
                            });
                            return;
                        }
                        return;
                    }
                }
                float progress = this.mTransitionPosition;
                long time = getNanoTime();
                this.mScrollTargetDX = (float) dx;
                this.mScrollTargetDY = (float) dy;
                this.mScrollTargetDT = (float) (((double) (time - this.mScrollTargetTime)) * 1.0E-9d);
                this.mScrollTargetTime = time;
                this.mScene.processScrollMove((float) dx, (float) dy);
                if (progress != this.mTransitionPosition) {
                    consumed[0] = dx;
                    consumed[1] = dy;
                }
                evaluate(false);
                if (consumed[0] != 0 || consumed[1] != 0) {
                    this.mUndergoingMotion = true;
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    /* loaded from: classes.dex */
    private class DevModeDraw {
        private static final int DEBUG_PATH_TICKS_PER_MS;
        int mKeyFrameCount;
        Path mPath;
        float[] mPoints;
        int mShadowTranslate;
        final int RED_COLOR = -21965;
        final int KEYFRAME_COLOR = -2067046;
        final int GRAPH_COLOR = -13391360;
        final int SHADOW_COLOR = 1996488704;
        final int DIAMOND_SIZE = 10;
        Rect mBounds = new Rect();
        boolean mPresentationMode = false;
        Paint mPaint = new Paint();
        Paint mPaintKeyframes = new Paint();
        Paint mPaintGraph = new Paint();
        Paint mTextPaint = new Paint();
        private float[] mRectangle = new float[8];
        Paint mFillPaint = new Paint();
        DashPathEffect mDashPathEffect = new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f);
        float[] mKeyFramePoints = new float[100];
        int[] mPathMode = new int[50];

        public DevModeDraw() {
            MotionLayout.this = r7;
            this.mShadowTranslate = 1;
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-21965);
            this.mPaint.setStrokeWidth(2.0f);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaintKeyframes.setAntiAlias(true);
            this.mPaintKeyframes.setColor(-2067046);
            this.mPaintKeyframes.setStrokeWidth(2.0f);
            this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
            this.mPaintGraph.setAntiAlias(true);
            this.mPaintGraph.setColor(-13391360);
            this.mPaintGraph.setStrokeWidth(2.0f);
            this.mPaintGraph.setStyle(Paint.Style.STROKE);
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setColor(-13391360);
            this.mTextPaint.setTextSize(r7.getContext().getResources().getDisplayMetrics().density * 12.0f);
            this.mFillPaint.setAntiAlias(true);
            this.mPaintGraph.setPathEffect(this.mDashPathEffect);
            if (this.mPresentationMode) {
                this.mPaint.setStrokeWidth(8.0f);
                this.mFillPaint.setStrokeWidth(8.0f);
                this.mPaintKeyframes.setStrokeWidth(8.0f);
                this.mShadowTranslate = 4;
            }
        }

        public void draw(Canvas canvas, HashMap<View, MotionController> frameArrayList, int duration, int debugPath) {
            if (!(frameArrayList == null || frameArrayList.size() == 0)) {
                canvas.save();
                if (!MotionLayout.this.isInEditMode() && (debugPath & 1) == 2) {
                    String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.mEndState) + ":" + MotionLayout.this.getProgress();
                    canvas.drawText(str, 10.0f, (float) (MotionLayout.this.getHeight() - 30), this.mTextPaint);
                    canvas.drawText(str, 11.0f, (float) (MotionLayout.this.getHeight() - 29), this.mPaint);
                }
                for (MotionController motionController : frameArrayList.values()) {
                    int mode = motionController.getDrawPath();
                    if (debugPath > 0 && mode == 0) {
                        mode = 1;
                    }
                    if (mode != 0) {
                        this.mKeyFrameCount = motionController.buildKeyFrames(this.mKeyFramePoints, this.mPathMode);
                        if (mode >= 1) {
                            int frames = duration / 16;
                            float[] fArr = this.mPoints;
                            if (fArr == null || fArr.length != frames * 2) {
                                this.mPoints = new float[frames * 2];
                                this.mPath = new Path();
                            }
                            int i = this.mShadowTranslate;
                            canvas.translate((float) i, (float) i);
                            this.mPaint.setColor(1996488704);
                            this.mFillPaint.setColor(1996488704);
                            this.mPaintKeyframes.setColor(1996488704);
                            this.mPaintGraph.setColor(1996488704);
                            motionController.buildPath(this.mPoints, frames);
                            drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                            this.mPaint.setColor(-21965);
                            this.mPaintKeyframes.setColor(-2067046);
                            this.mFillPaint.setColor(-2067046);
                            this.mPaintGraph.setColor(-13391360);
                            int i2 = this.mShadowTranslate;
                            canvas.translate((float) (-i2), (float) (-i2));
                            drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                            if (mode == 5) {
                                drawRectangle(canvas, motionController);
                            }
                        }
                    }
                }
                canvas.restore();
            }
        }

        public void drawAll(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            if (mode == 4) {
                drawPathAsConfigured(canvas);
            }
            if (mode == 2) {
                drawPathRelative(canvas);
            }
            if (mode == 3) {
                drawPathCartesian(canvas);
            }
            drawBasicPath(canvas);
            drawTicks(canvas, mode, keyFrames, motionController);
        }

        private void drawBasicPath(Canvas canvas) {
            canvas.drawLines(this.mPoints, this.mPaint);
        }

        private void drawTicks(Canvas canvas, int mode, int keyFrames, MotionController motionController) {
            int viewHeight;
            int viewWidth;
            if (motionController.mView != null) {
                viewWidth = motionController.mView.getWidth();
                viewHeight = motionController.mView.getHeight();
            } else {
                viewWidth = 0;
                viewHeight = 0;
            }
            for (int i = 1; i < keyFrames - 1; i++) {
                if (mode != 4 || this.mPathMode[i - 1] != 0) {
                    float[] fArr = this.mKeyFramePoints;
                    float x = fArr[i * 2];
                    float y = fArr[(i * 2) + 1];
                    this.mPath.reset();
                    this.mPath.moveTo(x, y + 10.0f);
                    this.mPath.lineTo(x + 10.0f, y);
                    this.mPath.lineTo(x, y - 10.0f);
                    this.mPath.lineTo(x - 10.0f, y);
                    this.mPath.close();
                    motionController.getKeyFrame(i - 1);
                    if (mode == 4) {
                        int[] iArr = this.mPathMode;
                        if (iArr[i - 1] == 1) {
                            drawPathRelativeTicks(canvas, x - 0.0f, y - 0.0f);
                        } else if (iArr[i - 1] == 2) {
                            drawPathCartesianTicks(canvas, x - 0.0f, y - 0.0f);
                        } else if (iArr[i - 1] == 3) {
                            drawPathScreenTicks(canvas, x - 0.0f, y - 0.0f, viewWidth, viewHeight);
                        }
                        canvas.drawPath(this.mPath, this.mFillPaint);
                    }
                    if (mode == 2) {
                        drawPathRelativeTicks(canvas, x - 0.0f, y - 0.0f);
                    }
                    if (mode == 3) {
                        drawPathCartesianTicks(canvas, x - 0.0f, y - 0.0f);
                    }
                    if (mode == 6) {
                        drawPathScreenTicks(canvas, x - 0.0f, y - 0.0f, viewWidth, viewHeight);
                    }
                    if (0.0f == 0.0f && 0.0f == 0.0f) {
                        canvas.drawPath(this.mPath, this.mFillPaint);
                    } else {
                        drawTranslation(canvas, x - 0.0f, y - 0.0f, x, y);
                    }
                }
            }
            float[] fArr2 = this.mPoints;
            if (fArr2.length > 1) {
                canvas.drawCircle(fArr2[0], fArr2[1], 8.0f, this.mPaintKeyframes);
                float[] fArr3 = this.mPoints;
                canvas.drawCircle(fArr3[fArr3.length - 2], fArr3[fArr3.length - 1], 8.0f, this.mPaintKeyframes);
            }
        }

        private void drawTranslation(Canvas canvas, float x1, float y1, float x2, float y2) {
            canvas.drawRect(x1, y1, x2, y2, this.mPaintGraph);
            canvas.drawLine(x1, y1, x2, y2, this.mPaintGraph);
        }

        private void drawPathRelative(Canvas canvas) {
            float[] fArr = this.mPoints;
            canvas.drawLine(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1], this.mPaintGraph);
        }

        private void drawPathAsConfigured(Canvas canvas) {
            boolean path = false;
            boolean cart = false;
            for (int i = 0; i < this.mKeyFrameCount; i++) {
                if (this.mPathMode[i] == 1) {
                    path = true;
                }
                if (this.mPathMode[i] == 2) {
                    cart = true;
                }
            }
            if (path) {
                drawPathRelative(canvas);
            }
            if (cart) {
                drawPathCartesian(canvas);
            }
        }

        private void drawPathRelativeTicks(Canvas canvas, float x, float y) {
            float[] fArr = this.mPoints;
            float x1 = fArr[0];
            float y1 = fArr[1];
            float x2 = fArr[fArr.length - 2];
            float y2 = fArr[fArr.length - 1];
            float dist = (float) Math.hypot((double) (x1 - x2), (double) (y1 - y2));
            float t = (((x - x1) * (x2 - x1)) + ((y - y1) * (y2 - y1))) / (dist * dist);
            float xp = x1 + ((x2 - x1) * t);
            float yp = y1 + ((y2 - y1) * t);
            Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(xp, yp);
            float len = (float) Math.hypot((double) (xp - x), (double) (yp - y));
            String text = "" + (((float) ((int) ((len * 100.0f) / dist))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas.drawTextOnPath(text, path, (len / 2.0f) - ((float) (this.mBounds.width() / 2)), -20.0f, this.mTextPaint);
            canvas.drawLine(x, y, xp, yp, this.mPaintGraph);
        }

        void getTextBounds(String text, Paint paint) {
            paint.getTextBounds(text, 0, text.length(), this.mBounds);
        }

        private void drawPathCartesian(Canvas canvas) {
            float[] fArr = this.mPoints;
            float x1 = fArr[0];
            float y1 = fArr[1];
            float x2 = fArr[fArr.length - 2];
            float y2 = fArr[fArr.length - 1];
            canvas.drawLine(Math.min(x1, x2), Math.max(y1, y2), Math.max(x1, x2), Math.max(y1, y2), this.mPaintGraph);
            canvas.drawLine(Math.min(x1, x2), Math.min(y1, y2), Math.min(x1, x2), Math.max(y1, y2), this.mPaintGraph);
        }

        private void drawPathCartesianTicks(Canvas canvas, float x, float y) {
            float[] fArr = this.mPoints;
            float x1 = fArr[0];
            float y1 = fArr[1];
            float x2 = fArr[fArr.length - 2];
            float y2 = fArr[fArr.length - 1];
            float minx = Math.min(x1, x2);
            float maxy = Math.max(y1, y2);
            float xgap = x - Math.min(x1, x2);
            float ygap = Math.max(y1, y2) - y;
            String text = "" + (((float) ((int) (((double) ((xgap * 100.0f) / Math.abs(x2 - x1))) + 0.5d))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas.drawText(text, ((xgap / 2.0f) - ((float) (this.mBounds.width() / 2))) + minx, y - 20.0f, this.mTextPaint);
            canvas.drawLine(x, y, Math.min(x1, x2), y, this.mPaintGraph);
            String text2 = "" + (((float) ((int) (((double) ((ygap * 100.0f) / Math.abs(y2 - y1))) + 0.5d))) / 100.0f);
            getTextBounds(text2, this.mTextPaint);
            canvas.drawText(text2, x + 5.0f, maxy - ((ygap / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(y1, y2), this.mPaintGraph);
        }

        private void drawPathScreenTicks(Canvas canvas, float x, float y, int viewWidth, int viewHeight) {
            String text = "" + (((float) ((int) (((double) (((x - ((float) (viewWidth / 2))) * 100.0f) / ((float) (MotionLayout.this.getWidth() - viewWidth)))) + 0.5d))) / 100.0f);
            getTextBounds(text, this.mTextPaint);
            canvas.drawText(text, ((x / 2.0f) - ((float) (this.mBounds.width() / 2))) + 0.0f, y - 20.0f, this.mTextPaint);
            canvas.drawLine(x, y, Math.min(0.0f, 1.0f), y, this.mPaintGraph);
            String text2 = "" + (((float) ((int) (((double) (((y - ((float) (viewHeight / 2))) * 100.0f) / ((float) (MotionLayout.this.getHeight() - viewHeight)))) + 0.5d))) / 100.0f);
            getTextBounds(text2, this.mTextPaint);
            canvas.drawText(text2, x + 5.0f, 0.0f - ((y / 2.0f) - ((float) (this.mBounds.height() / 2))), this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(0.0f, 1.0f), this.mPaintGraph);
        }

        private void drawRectangle(Canvas canvas, MotionController motionController) {
            this.mPath.reset();
            for (int i = 0; i <= 50; i++) {
                motionController.buildRect(((float) i) / ((float) 50), this.mRectangle, 0);
                Path path = this.mPath;
                float[] fArr = this.mRectangle;
                path.moveTo(fArr[0], fArr[1]);
                Path path2 = this.mPath;
                float[] fArr2 = this.mRectangle;
                path2.lineTo(fArr2[2], fArr2[3]);
                Path path3 = this.mPath;
                float[] fArr3 = this.mRectangle;
                path3.lineTo(fArr3[4], fArr3[5]);
                Path path4 = this.mPath;
                float[] fArr4 = this.mRectangle;
                path4.lineTo(fArr4[6], fArr4[7]);
                this.mPath.close();
            }
            this.mPaint.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.mPath, this.mPaint);
            canvas.translate(-2.0f, -2.0f);
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    private void debugPos() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Log.v(TAG, " " + Debug.getLocation() + " " + Debug.getName(this) + " " + Debug.getName(getContext(), this.mCurrentState) + " " + Debug.getName(child) + child.getLeft() + " " + child.getTop());
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        String str;
        evaluate(false);
        super.dispatchDraw(canvas);
        if (this.mScene != null) {
            if ((this.mDebugPath & 1) == 1 && !isInEditMode()) {
                this.mFrames++;
                long currentDrawTime = getNanoTime();
                long j = this.mLastDrawTime;
                if (j != -1) {
                    long delay = currentDrawTime - j;
                    if (delay > 200000000) {
                        this.mLastFps = ((float) ((int) ((((float) this.mFrames) / (((float) delay) * 1.0E-9f)) * 100.0f))) / 100.0f;
                        this.mFrames = 0;
                        this.mLastDrawTime = currentDrawTime;
                    }
                } else {
                    this.mLastDrawTime = currentDrawTime;
                }
                Paint paint = new Paint();
                paint.setTextSize(42.0f);
                StringBuilder sb = new StringBuilder();
                sb.append(this.mLastFps + " fps " + Debug.getState(this, this.mBeginState) + " -> ");
                sb.append(Debug.getState(this, this.mEndState));
                sb.append(" (progress: ");
                sb.append(((float) ((int) (getProgress() * 1000.0f))) / 10.0f);
                sb.append(" ) state=");
                int i = this.mCurrentState;
                if (i == -1) {
                    str = "undefined";
                } else {
                    str = Debug.getState(this, i);
                }
                sb.append(str);
                String str2 = sb.toString();
                paint.setColor(ViewCompat.MEASURED_STATE_MASK);
                canvas.drawText(str2, 11.0f, (float) (getHeight() - 29), paint);
                paint.setColor(-7864184);
                canvas.drawText(str2, 10.0f, (float) (getHeight() - 30), paint);
            }
            if (this.mDebugPath > 1) {
                if (this.mDevModeDraw == null) {
                    this.mDevModeDraw = new DevModeDraw();
                }
                this.mDevModeDraw.draw(canvas, this.mFrameArrayList, this.mScene.getDuration(), this.mDebugPath);
            }
        }
    }

    private void evaluateLayout() {
        int i;
        float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
        long currentTime = getNanoTime();
        float deltaPos = 0.0f;
        if (!(this.mInterpolator instanceof StopLogic)) {
            deltaPos = ((((float) (currentTime - this.mTransitionLastTime)) * dir) * 1.0E-9f) / this.mTransitionDuration;
        }
        float position = this.mTransitionLastPosition + deltaPos;
        boolean done = false;
        if (this.mTransitionInstantly) {
            position = this.mTransitionGoalPosition;
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
            done = true;
        }
        Interpolator interpolator = this.mInterpolator;
        if (interpolator != null && !done) {
            if (this.mTemporalInterpolator) {
                position = interpolator.getInterpolation(((float) (currentTime - this.mAnimationStartTime)) * 1.0E-9f);
            } else {
                position = interpolator.getInterpolation(position);
            }
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
        }
        this.mPostInterpolationPosition = position;
        int n = getChildCount();
        long time = getNanoTime();
        int i2 = 0;
        while (i2 < n) {
            View child = getChildAt(i2);
            MotionController frame = this.mFrameArrayList.get(child);
            if (frame != null) {
                i = i2;
                frame.interpolate(child, position, time, this.mKeyCache);
            } else {
                i = i2;
            }
            i2 = i + 1;
        }
        if (this.mMeasureDuringTransition) {
            requestLayout();
        }
    }

    public void evaluate(boolean force) {
        int i;
        int i2;
        if (this.mTransitionLastTime == -1) {
            this.mTransitionLastTime = getNanoTime();
        }
        float f = this.mTransitionLastPosition;
        if (f > 0.0f && f < 1.0f) {
            this.mCurrentState = -1;
        }
        boolean newState = false;
        if (this.mKeepAnimating || (this.mInTransition && (force || this.mTransitionGoalPosition != this.mTransitionLastPosition))) {
            float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            long currentTime = getNanoTime();
            float deltaPos = 0.0f;
            if (!(this.mInterpolator instanceof MotionInterpolator)) {
                deltaPos = ((((float) (currentTime - this.mTransitionLastTime)) * dir) * 1.0E-9f) / this.mTransitionDuration;
                this.mLastVelocity = deltaPos;
            }
            float position = this.mTransitionLastPosition + deltaPos;
            boolean done = false;
            if (this.mTransitionInstantly) {
                position = this.mTransitionGoalPosition;
            }
            if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
                position = this.mTransitionGoalPosition;
                this.mInTransition = false;
                done = true;
            }
            this.mTransitionLastPosition = position;
            this.mTransitionPosition = position;
            this.mTransitionLastTime = currentTime;
            Interpolator interpolator = this.mInterpolator;
            if (interpolator != null && !done) {
                if (this.mTemporalInterpolator) {
                    float position2 = interpolator.getInterpolation(((float) (currentTime - this.mAnimationStartTime)) * 1.0E-9f);
                    this.mTransitionLastPosition = position2;
                    this.mTransitionLastTime = currentTime;
                    Interpolator interpolator2 = this.mInterpolator;
                    if (interpolator2 instanceof MotionInterpolator) {
                        float lastVelocity = ((MotionInterpolator) interpolator2).getVelocity();
                        this.mLastVelocity = lastVelocity;
                        if (Math.abs(lastVelocity) * this.mTransitionDuration <= EPSILON) {
                            this.mInTransition = false;
                        }
                        if (lastVelocity > 0.0f && position2 >= 1.0f) {
                            position2 = 1.0f;
                            this.mTransitionLastPosition = 1.0f;
                            this.mInTransition = false;
                        }
                        if (lastVelocity >= 0.0f || position2 > 0.0f) {
                            position = position2;
                        } else {
                            this.mTransitionLastPosition = 0.0f;
                            this.mInTransition = false;
                            position = 0.0f;
                        }
                    } else {
                        position = position2;
                    }
                } else {
                    position = interpolator.getInterpolation(position);
                    Interpolator interpolator3 = this.mInterpolator;
                    if (interpolator3 instanceof MotionInterpolator) {
                        this.mLastVelocity = ((MotionInterpolator) interpolator3).getVelocity();
                    } else {
                        this.mLastVelocity = ((interpolator3.getInterpolation(position + deltaPos) - position) * dir) / deltaPos;
                    }
                }
            }
            if (Math.abs(this.mLastVelocity) > EPSILON) {
                setState(TransitionState.MOVING);
            }
            if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
                position = this.mTransitionGoalPosition;
                this.mInTransition = false;
            }
            if (position >= 1.0f || position <= 0.0f) {
                this.mInTransition = false;
                setState(TransitionState.FINISHED);
            }
            int n = getChildCount();
            this.mKeepAnimating = false;
            long time = getNanoTime();
            this.mPostInterpolationPosition = position;
            for (int i3 = 0; i3 < n; i3++) {
                View child = getChildAt(i3);
                MotionController frame = this.mFrameArrayList.get(child);
                if (frame != null) {
                    this.mKeepAnimating = frame.interpolate(child, position, time, this.mKeyCache) | this.mKeepAnimating;
                }
            }
            boolean end = (dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition);
            if (!this.mKeepAnimating && !this.mInTransition && end) {
                setState(TransitionState.FINISHED);
            }
            if (this.mMeasureDuringTransition) {
                requestLayout();
            }
            this.mKeepAnimating |= !end;
            if (!(position > 0.0f || (i2 = this.mBeginState) == -1 || this.mCurrentState == i2)) {
                newState = true;
                this.mCurrentState = i2;
                this.mScene.getConstraintSet(i2).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
            }
            if (((double) position) >= 1.0d && this.mCurrentState != (i = this.mEndState)) {
                newState = true;
                this.mCurrentState = i;
                this.mScene.getConstraintSet(i).applyCustomAttributes(this);
                setState(TransitionState.FINISHED);
            }
            if (this.mKeepAnimating || this.mInTransition) {
                invalidate();
            } else if ((dir > 0.0f && position == 1.0f) || (dir < 0.0f && position == 0.0f)) {
                setState(TransitionState.FINISHED);
            }
            if ((!this.mKeepAnimating && this.mInTransition && dir > 0.0f && position == 1.0f) || (dir < 0.0f && position == 0.0f)) {
                onNewStateAttachHandlers();
            }
        }
        float dir2 = this.mTransitionLastPosition;
        if (dir2 >= 1.0f) {
            if (this.mCurrentState != this.mEndState) {
                newState = true;
            }
            this.mCurrentState = this.mEndState;
        } else if (dir2 <= 0.0f) {
            if (this.mCurrentState != this.mBeginState) {
                newState = true;
            }
            this.mCurrentState = this.mBeginState;
        }
        this.mNeedsFireTransitionCompleted |= newState;
        if (newState && !this.mInLayout) {
            requestLayout();
        }
        this.mTransitionPosition = this.mTransitionLastPosition;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mInLayout = true;
        try {
            if (this.mScene == null) {
                super.onLayout(changed, left, top, right, bottom);
                return;
            }
            int w = right - left;
            int h = bottom - top;
            if (!(this.mLastLayoutWidth == w && this.mLastLayoutHeight == h)) {
                rebuildScene();
                evaluate(true);
            }
            this.mLastLayoutWidth = w;
            this.mLastLayoutHeight = h;
            this.mOldWidth = w;
            this.mOldHeight = h;
        } finally {
            this.mInLayout = false;
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    protected void parseLayoutDescription(int id) {
        this.mConstraintLayoutSpec = null;
    }

    private void init(AttributeSet attrs) {
        MotionScene motionScene;
        IS_IN_EDIT_MODE = isInEditMode();
        if (attrs != null) {
            TypedArray a2 = getContext().obtainStyledAttributes(attrs, R.styleable.MotionLayout);
            int N = a2.getIndexCount();
            boolean apply = true;
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.MotionLayout_layoutDescription) {
                    this.mScene = new MotionScene(getContext(), this, a2.getResourceId(attr, -1));
                } else if (attr == R.styleable.MotionLayout_currentState) {
                    this.mCurrentState = a2.getResourceId(attr, -1);
                } else if (attr == R.styleable.MotionLayout_motionProgress) {
                    this.mTransitionGoalPosition = a2.getFloat(attr, 0.0f);
                    this.mInTransition = true;
                } else if (attr == R.styleable.MotionLayout_applyMotionScene) {
                    apply = a2.getBoolean(attr, apply);
                } else {
                    int i2 = 0;
                    if (attr == R.styleable.MotionLayout_showPaths) {
                        if (this.mDebugPath == 0) {
                            if (a2.getBoolean(attr, false)) {
                                i2 = 2;
                            }
                            this.mDebugPath = i2;
                        }
                    } else if (attr == R.styleable.MotionLayout_motionDebug) {
                        this.mDebugPath = a2.getInt(attr, 0);
                    }
                }
            }
            a2.recycle();
            if (this.mScene == null) {
                Log.e(TAG, "WARNING NO app:layoutDescription tag");
            }
            if (!apply) {
                this.mScene = null;
            }
        }
        if (this.mDebugPath != 0) {
            checkStructure();
        }
        if (this.mCurrentState == -1 && (motionScene = this.mScene) != null) {
            this.mCurrentState = motionScene.getStartId();
            this.mBeginState = this.mScene.getStartId();
            this.mEndState = this.mScene.getEndId();
        }
    }

    public void setScene(MotionScene scene) {
        this.mScene = scene;
        this.mScene.setRtl(isRtl());
        rebuildScene();
    }

    private void checkStructure() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            Log.e(TAG, "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        int startId = motionScene.getStartId();
        MotionScene motionScene2 = this.mScene;
        checkStructure(startId, motionScene2.getConstraintSet(motionScene2.getStartId()));
        SparseIntArray startToEnd = new SparseIntArray();
        SparseIntArray endToStart = new SparseIntArray();
        Iterator<MotionScene.Transition> it = this.mScene.getDefinedTransitions().iterator();
        while (it.hasNext()) {
            MotionScene.Transition definedTransition = it.next();
            if (definedTransition == this.mScene.mCurrentTransition) {
                Log.v(TAG, "CHECK: CURRENT");
            }
            checkStructure(definedTransition);
            int startId2 = definedTransition.getStartConstraintSetId();
            int endId = definedTransition.getEndConstraintSetId();
            String startString = Debug.getName(getContext(), startId2);
            String endString = Debug.getName(getContext(), endId);
            if (startToEnd.get(startId2) == endId) {
                Log.e(TAG, "CHECK: two transitions with the same start and end " + startString + "->" + endString);
            }
            if (endToStart.get(endId) == startId2) {
                Log.e(TAG, "CHECK: you can't have reverse transitions" + startString + "->" + endString);
            }
            startToEnd.put(startId2, endId);
            endToStart.put(endId, startId2);
            if (this.mScene.getConstraintSet(startId2) == null) {
                Log.e(TAG, " no such constraintSetStart " + startString);
            }
            if (this.mScene.getConstraintSet(endId) == null) {
                Log.e(TAG, " no such constraintSetEnd " + startString);
            }
        }
    }

    private void checkStructure(int csetId, ConstraintSet set) {
        String setName = Debug.getName(getContext(), csetId);
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View v = getChildAt(i);
            int id = v.getId();
            if (id == -1) {
                Log.w(TAG, "CHECK: " + setName + " ALL VIEWS SHOULD HAVE ID's " + v.getClass().getName() + " does not!");
            }
            if (set.getConstraint(id) == null) {
                Log.w(TAG, "CHECK: " + setName + " NO CONSTRAINTS for " + Debug.getName(v));
            }
        }
        int[] ids = set.getKnownIds();
        for (int i2 = 0; i2 < ids.length; i2++) {
            int id2 = ids[i2];
            String idString = Debug.getName(getContext(), id2);
            if (findViewById(ids[i2]) == null) {
                Log.w(TAG, "CHECK: " + setName + " NO View matches id " + idString);
            }
            if (set.getHeight(id2) == -1) {
                Log.w(TAG, "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
            if (set.getWidth(id2) == -1) {
                Log.w(TAG, "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
        }
    }

    private void checkStructure(MotionScene.Transition transition) {
        Log.v(TAG, "CHECK: transition = " + transition.debugString(getContext()));
        Log.v(TAG, "CHECK: transition.setDuration = " + transition.getDuration());
        if (transition.getStartConstraintSetId() == transition.getEndConstraintSetId()) {
            Log.e(TAG, "CHECK: start and end constraint set should not be the same!");
        }
    }

    public void setDebugMode(int debugMode) {
        this.mDebugPath = debugMode;
        invalidate();
    }

    public void getDebugMode(boolean showPaths) {
        this.mDebugPath = showPaths ? 2 : 1;
        invalidate();
    }

    private boolean handlesTouchEvent(float x, float y, View view, MotionEvent event) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                if (handlesTouchEvent(((float) view.getLeft()) + x, ((float) view.getTop()) + y, group.getChildAt(i), event)) {
                    return true;
                }
            }
        }
        this.mBoundsCheck.set(((float) view.getLeft()) + x, ((float) view.getTop()) + y, ((float) view.getRight()) + x, ((float) view.getBottom()) + y);
        if (event.getAction() == 0) {
            if (!this.mBoundsCheck.contains(event.getX(), event.getY()) || !view.onTouchEvent(event)) {
                return false;
            }
            return true;
        } else if (view.onTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        MotionScene.Transition currentTransition;
        TouchResponse touchResponse;
        int regionId;
        RectF region;
        MotionScene motionScene = this.mScene;
        if (motionScene != null && this.mInteractionEnabled && (currentTransition = motionScene.mCurrentTransition) != null && currentTransition.isEnabled() && (touchResponse = currentTransition.getTouchResponse()) != null && ((event.getAction() != 0 || (region = touchResponse.getTouchRegion(this, new RectF())) == null || region.contains(event.getX(), event.getY())) && (regionId = touchResponse.getTouchRegionId()) != -1)) {
            View view = this.mRegionView;
            if (view == null || view.getId() != regionId) {
                this.mRegionView = findViewById(regionId);
            }
            View view2 = this.mRegionView;
            if (view2 != null) {
                this.mBoundsCheck.set((float) view2.getLeft(), (float) this.mRegionView.getTop(), (float) this.mRegionView.getRight(), (float) this.mRegionView.getBottom());
                if (this.mBoundsCheck.contains(event.getX(), event.getY()) && !handlesTouchEvent(0.0f, 0.0f, this.mRegionView, event)) {
                    return onTouchEvent(event);
                }
            }
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null || !this.mInteractionEnabled || !motionScene.supportTouch()) {
            return super.onTouchEvent(event);
        }
        MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
        if (currentTransition != null && !currentTransition.isEnabled()) {
            return super.onTouchEvent(event);
        }
        this.mScene.processTouchEvent(event, getCurrentState(), this);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        int i;
        super.onAttachedToWindow();
        MotionScene motionScene = this.mScene;
        if (!(motionScene == null || (i = this.mCurrentState) == -1)) {
            ConstraintSet cSet = motionScene.getConstraintSet(i);
            this.mScene.readFallback(this);
            if (cSet != null) {
                cSet.applyTo(this);
            }
            this.mBeginState = this.mCurrentState;
        }
        onNewStateAttachHandlers();
        StateCache stateCache = this.mStateCache;
        if (stateCache != null) {
            stateCache.apply();
            return;
        }
        MotionScene motionScene2 = this.mScene;
        if (motionScene2 != null && motionScene2.mCurrentTransition != null && this.mScene.mCurrentTransition.getAutoTransition() == 4) {
            transitionToEnd();
            setState(TransitionState.SETUP);
            setState(TransitionState.MOVING);
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.setRtl(isRtl());
        }
    }

    public void onNewStateAttachHandlers() {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            if (motionScene.autoTransition(this, this.mCurrentState)) {
                requestLayout();
                return;
            }
            int i = this.mCurrentState;
            if (i != -1) {
                this.mScene.addOnClickListeners(this, i);
            }
            if (this.mScene.supportTouch()) {
                this.mScene.setupTouch();
            }
        }
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public float getProgress() {
        return this.mTransitionLastPosition;
    }

    public void getAnchorDpDt(int mTouchAnchorId, float pos, float locationX, float locationY, float[] mAnchorDpDt) {
        String idName;
        HashMap<View, MotionController> hashMap = this.mFrameArrayList;
        View v = getViewById(mTouchAnchorId);
        MotionController f = hashMap.get(v);
        if (f != null) {
            f.getDpDt(pos, locationX, locationY, mAnchorDpDt);
            float y = v.getY();
            float deltaPos = pos - this.lastPos;
            float deltaY = y - this.lastY;
            if (deltaPos != 0.0f) {
                float f2 = deltaY / deltaPos;
            }
            this.lastPos = pos;
            this.lastY = y;
            return;
        }
        if (v == null) {
            idName = "" + mTouchAnchorId;
        } else {
            idName = v.getContext().getResources().getResourceName(mTouchAnchorId);
        }
        Log.w(TAG, "WARNING could not find view id " + idName);
    }

    public long getTransitionTimeMs() {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            this.mTransitionDuration = ((float) motionScene.getDuration()) / 1000.0f;
        }
        return (long) (this.mTransitionDuration * 1000.0f);
    }

    public void setTransitionListener(TransitionListener listener) {
        this.mTransitionListener = listener;
    }

    public void addTransitionListener(TransitionListener listener) {
        if (this.mTransitionListeners == null) {
            this.mTransitionListeners = new ArrayList<>();
        }
        this.mTransitionListeners.add(listener);
    }

    public boolean removeTransitionListener(TransitionListener listener) {
        ArrayList<TransitionListener> arrayList = this.mTransitionListeners;
        if (arrayList == null) {
            return false;
        }
        return arrayList.remove(listener);
    }

    public void fireTrigger(int triggerId, boolean positive, float progress) {
        TransitionListener transitionListener = this.mTransitionListener;
        if (transitionListener != null) {
            transitionListener.onTransitionTrigger(this, triggerId, positive, progress);
        }
        ArrayList<TransitionListener> arrayList = this.mTransitionListeners;
        if (arrayList != null) {
            Iterator<TransitionListener> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onTransitionTrigger(this, triggerId, positive, progress);
            }
        }
    }

    private void fireTransitionChange() {
        ArrayList<TransitionListener> arrayList;
        if ((this.mTransitionListener != null || ((arrayList = this.mTransitionListeners) != null && !arrayList.isEmpty())) && this.mListenerPosition != this.mTransitionPosition) {
            if (this.mListenerState != -1) {
                TransitionListener transitionListener = this.mTransitionListener;
                if (transitionListener != null) {
                    transitionListener.onTransitionStarted(this, this.mBeginState, this.mEndState);
                }
                ArrayList<TransitionListener> arrayList2 = this.mTransitionListeners;
                if (arrayList2 != null) {
                    Iterator<TransitionListener> it = arrayList2.iterator();
                    while (it.hasNext()) {
                        it.next().onTransitionStarted(this, this.mBeginState, this.mEndState);
                    }
                }
                this.mIsAnimating = true;
            }
            this.mListenerState = -1;
            float f = this.mTransitionPosition;
            this.mListenerPosition = f;
            TransitionListener transitionListener2 = this.mTransitionListener;
            if (transitionListener2 != null) {
                transitionListener2.onTransitionChange(this, this.mBeginState, this.mEndState, f);
            }
            ArrayList<TransitionListener> arrayList3 = this.mTransitionListeners;
            if (arrayList3 != null) {
                Iterator<TransitionListener> it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    it2.next().onTransitionChange(this, this.mBeginState, this.mEndState, this.mTransitionPosition);
                }
            }
            this.mIsAnimating = true;
        }
    }

    protected void fireTransitionCompleted() {
        ArrayList<TransitionListener> arrayList;
        if ((this.mTransitionListener != null || ((arrayList = this.mTransitionListeners) != null && !arrayList.isEmpty())) && this.mListenerState == -1) {
            this.mListenerState = this.mCurrentState;
            int lastState = -1;
            if (!this.mTransitionCompleted.isEmpty()) {
                ArrayList<Integer> arrayList2 = this.mTransitionCompleted;
                lastState = arrayList2.get(arrayList2.size() - 1).intValue();
            }
            int i = this.mCurrentState;
            if (!(lastState == i || i == -1)) {
                this.mTransitionCompleted.add(Integer.valueOf(i));
            }
        }
        processTransitionCompleted();
    }

    private void processTransitionCompleted() {
        ArrayList<TransitionListener> arrayList;
        if (this.mTransitionListener != null || ((arrayList = this.mTransitionListeners) != null && !arrayList.isEmpty())) {
            this.mIsAnimating = false;
            Iterator<Integer> it = this.mTransitionCompleted.iterator();
            while (it.hasNext()) {
                Integer state = it.next();
                TransitionListener transitionListener = this.mTransitionListener;
                if (transitionListener != null) {
                    transitionListener.onTransitionCompleted(this, state.intValue());
                }
                ArrayList<TransitionListener> arrayList2 = this.mTransitionListeners;
                if (arrayList2 != null) {
                    Iterator<TransitionListener> it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        it2.next().onTransitionCompleted(this, state.intValue());
                    }
                }
            }
            this.mTransitionCompleted.clear();
        }
    }

    public DesignTool getDesignTool() {
        if (this.mDesignTool == null) {
            this.mDesignTool = new DesignTool(this);
        }
        return this.mDesignTool;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper helper = (MotionHelper) view;
            if (this.mTransitionListeners == null) {
                this.mTransitionListeners = new ArrayList<>();
            }
            this.mTransitionListeners.add(helper);
            if (helper.isUsedOnShow()) {
                if (this.mOnShowHelpers == null) {
                    this.mOnShowHelpers = new ArrayList<>();
                }
                this.mOnShowHelpers.add(helper);
            }
            if (helper.isUseOnHide()) {
                if (this.mOnHideHelpers == null) {
                    this.mOnHideHelpers = new ArrayList<>();
                }
                this.mOnHideHelpers.add(helper);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        ArrayList<MotionHelper> arrayList2 = this.mOnHideHelpers;
        if (arrayList2 != null) {
            arrayList2.remove(view);
        }
    }

    public void setOnShow(float progress) {
        ArrayList<MotionHelper> arrayList = this.mOnShowHelpers;
        if (arrayList != null) {
            int count = arrayList.size();
            for (int i = 0; i < count; i++) {
                this.mOnShowHelpers.get(i).setProgress(progress);
            }
        }
    }

    public void setOnHide(float progress) {
        ArrayList<MotionHelper> arrayList = this.mOnHideHelpers;
        if (arrayList != null) {
            int count = arrayList.size();
            for (int i = 0; i < count; i++) {
                this.mOnHideHelpers.get(i).setProgress(progress);
            }
        }
    }

    public int[] getConstraintSetIds() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getConstraintSetIds();
    }

    public ConstraintSet getConstraintSet(int id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getConstraintSet(id);
    }

    @Deprecated
    public void rebuildMotion() {
        Log.e(TAG, "This method is deprecated. Please call rebuildScene() instead.");
        rebuildScene();
    }

    public void rebuildScene() {
        this.mModel.reEvaluateState();
        invalidate();
    }

    public void updateState(int stateId, ConstraintSet set) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.setConstraintSet(stateId, set);
        }
        updateState();
        if (this.mCurrentState == stateId) {
            set.applyTo(this);
        }
    }

    public void updateState() {
        this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
        rebuildScene();
    }

    public ArrayList<MotionScene.Transition> getDefinedTransitions() {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.getDefinedTransitions();
    }

    public int getStartState() {
        return this.mBeginState;
    }

    public int getEndState() {
        return this.mEndState;
    }

    public float getTargetPosition() {
        return this.mTransitionGoalPosition;
    }

    public void setTransitionDuration(int milliseconds) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            Log.e(TAG, "MotionScene not defined");
        } else {
            motionScene.setDuration(milliseconds);
        }
    }

    public MotionScene.Transition getTransition(int id) {
        return this.mScene.getTransitionById(id);
    }

    public int lookUpConstraintId(String id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return 0;
        }
        return motionScene.lookUpConstraintId(id);
    }

    public String getConstraintSetNames(int id) {
        MotionScene motionScene = this.mScene;
        if (motionScene == null) {
            return null;
        }
        return motionScene.lookUpConstraintName(id);
    }

    public void disableAutoTransition(boolean disable) {
        MotionScene motionScene = this.mScene;
        if (motionScene != null) {
            motionScene.disableAutoTransition(disable);
        }
    }

    public void setInteractionEnabled(boolean enabled) {
        this.mInteractionEnabled = enabled;
    }

    public boolean isInteractionEnabled() {
        return this.mInteractionEnabled;
    }

    private void fireTransitionStarted(MotionLayout motionLayout, int mBeginState, int mEndState) {
        TransitionListener transitionListener = this.mTransitionListener;
        if (transitionListener != null) {
            transitionListener.onTransitionStarted(this, mBeginState, mEndState);
        }
        ArrayList<TransitionListener> arrayList = this.mTransitionListeners;
        if (arrayList != null) {
            Iterator<TransitionListener> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onTransitionStarted(motionLayout, mBeginState, mEndState);
            }
        }
    }
}
