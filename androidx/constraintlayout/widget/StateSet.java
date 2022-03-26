package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class StateSet {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    ConstraintSet mDefaultConstraintSet;
    int mDefaultState = -1;
    int mCurrentStateId = -1;
    int mCurrentConstraintNumber = -1;
    private SparseArray<State> mStateList = new SparseArray<>();
    private SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    private ConstraintsChangedListener mConstraintsChangedListener = null;

    public StateSet(Context context, XmlPullParser parser) {
        load(context, parser);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void load(Context context, XmlPullParser parser) {
        boolean z;
        TypedArray a2 = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.StateSet);
        int N = a2.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a2.getIndex(i);
            if (attr == R.styleable.StateSet_defaultState) {
                this.mDefaultState = a2.getResourceId(attr, this.mDefaultState);
            }
        }
        a2.recycle();
        State state = null;
        try {
            int eventType = parser.getEventType();
            while (eventType != 1) {
                if (eventType == 0) {
                    parser.getName();
                } else if (eventType == 2) {
                    String tagName = parser.getName();
                    switch (tagName.hashCode()) {
                        case 80204913:
                            if (tagName.equals("State")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        case 1301459538:
                            if (tagName.equals("LayoutDescription")) {
                                z = false;
                                break;
                            }
                            z = true;
                            break;
                        case 1382829617:
                            if (tagName.equals("StateSet")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        case 1901439077:
                            if (tagName.equals("Variant")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        default:
                            z = true;
                            break;
                    }
                    if (z && !z) {
                        if (z) {
                            state = new State(context, parser);
                            this.mStateList.put(state.mId, state);
                        } else if (!z) {
                            Log.v("ConstraintLayoutStates", "unknown tag " + tagName);
                        } else {
                            Variant match = new Variant(context, parser);
                            if (state != null) {
                                state.add(match);
                            }
                        }
                    }
                } else if (eventType == 3) {
                    if ("StateSet".equals(parser.getName())) {
                        return;
                    }
                }
                eventType = parser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public boolean needsToChange(int id, float width, float height) {
        int i = this.mCurrentStateId;
        if (i != id) {
            return true;
        }
        State state = id == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(i);
        if ((this.mCurrentConstraintNumber == -1 || !state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != state.findMatch(width, height)) {
            return true;
        }
        return false;
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }

    public int stateGetConstraintID(int id, int width, int height) {
        return updateConstraints(-1, id, (float) width, (float) height);
    }

    public int convertToConstraintSet(int currentConstrainSettId, int stateId, float width, float height) {
        State state = this.mStateList.get(stateId);
        if (state == null) {
            return stateId;
        }
        if (width != -1.0f && height != -1.0f) {
            Variant match = null;
            Iterator<Variant> it = state.mVariants.iterator();
            while (it.hasNext()) {
                Variant mVariant = it.next();
                if (mVariant.match(width, height)) {
                    if (currentConstrainSettId == mVariant.mConstraintID) {
                        return currentConstrainSettId;
                    }
                    match = mVariant;
                }
            }
            if (match != null) {
                return match.mConstraintID;
            }
            return state.mConstraintID;
        } else if (state.mConstraintID == currentConstrainSettId) {
            return currentConstrainSettId;
        } else {
            Iterator<Variant> it2 = state.mVariants.iterator();
            while (it2.hasNext()) {
                if (currentConstrainSettId == it2.next().mConstraintID) {
                    return currentConstrainSettId;
                }
            }
            return state.mConstraintID;
        }
    }

    public int updateConstraints(int currentid, int id, float width, float height) {
        State state;
        if (currentid == id) {
            if (id == -1) {
                state = this.mStateList.valueAt(0);
            } else {
                state = this.mStateList.get(this.mCurrentStateId);
            }
            if (state == null) {
                return -1;
            }
            if (this.mCurrentConstraintNumber != -1 && state.mVariants.get(currentid).match(width, height)) {
                return currentid;
            }
            int match = state.findMatch(width, height);
            if (currentid == match) {
                return currentid;
            }
            return match == -1 ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
        }
        State state2 = this.mStateList.get(id);
        if (state2 == null) {
            return -1;
        }
        int match2 = state2.findMatch(width, height);
        return match2 == -1 ? state2.mConstraintID : state2.mVariants.get(match2).mConstraintID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class State {
        int mConstraintID;
        int mId;
        boolean mIsLayout;
        ArrayList<Variant> mVariants = new ArrayList<>();

        public State(Context context, XmlPullParser parser) {
            this.mConstraintID = -1;
            this.mIsLayout = false;
            TypedArray a2 = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.State);
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.State_android_id) {
                    this.mId = a2.getResourceId(attr, this.mId);
                } else if (attr == R.styleable.State_constraints) {
                    this.mConstraintID = a2.getResourceId(attr, this.mConstraintID);
                    String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                }
            }
            a2.recycle();
        }

        void add(Variant size) {
            this.mVariants.add(size);
        }

        public int findMatch(float width, float height) {
            for (int i = 0; i < this.mVariants.size(); i++) {
                if (this.mVariants.get(i).match(width, height)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Variant {
        int mConstraintID;
        int mId;
        boolean mIsLayout;
        float mMaxHeight;
        float mMaxWidth;
        float mMinHeight;
        float mMinWidth;

        public Variant(Context context, XmlPullParser parser) {
            this.mMinWidth = Float.NaN;
            this.mMinHeight = Float.NaN;
            this.mMaxWidth = Float.NaN;
            this.mMaxHeight = Float.NaN;
            this.mConstraintID = -1;
            this.mIsLayout = false;
            TypedArray a2 = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.Variant);
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.Variant_constraints) {
                    this.mConstraintID = a2.getResourceId(attr, this.mConstraintID);
                    String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                } else if (attr == R.styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = a2.getDimension(attr, this.mMaxHeight);
                } else if (attr == R.styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = a2.getDimension(attr, this.mMinHeight);
                } else if (attr == R.styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = a2.getDimension(attr, this.mMaxWidth);
                } else if (attr == R.styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = a2.getDimension(attr, this.mMinWidth);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            a2.recycle();
        }

        boolean match(float widthDp, float heightDp) {
            if (!Float.isNaN(this.mMinWidth) && widthDp < this.mMinWidth) {
                return false;
            }
            if (!Float.isNaN(this.mMinHeight) && heightDp < this.mMinHeight) {
                return false;
            }
            if (!Float.isNaN(this.mMaxWidth) && widthDp > this.mMaxWidth) {
                return false;
            }
            if (Float.isNaN(this.mMaxHeight) || heightDp <= this.mMaxHeight) {
                return true;
            }
            return false;
        }
    }
}
