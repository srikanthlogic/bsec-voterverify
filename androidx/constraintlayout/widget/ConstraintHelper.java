package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: classes.dex */
public abstract class ConstraintHelper extends View {
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds;
    private HashMap<Integer, String> mMap;
    protected String mReferenceIds;
    protected String mReferenceTags;
    protected boolean mUseViewMeasure;
    private View[] mViews;
    protected Context myContext;

    public ConstraintHelper(Context context) {
        super(context);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<>();
        this.myContext = context;
        init(null);
    }

    public ConstraintHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<>();
        this.myContext = context;
        init(attrs);
    }

    public ConstraintHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.mViews = null;
        this.mMap = new HashMap<>();
        this.myContext = context;
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a2 = getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    this.mReferenceIds = a2.getString(attr);
                    setIds(this.mReferenceIds);
                } else if (attr == R.styleable.ConstraintLayout_Layout_constraint_referenced_tags) {
                    this.mReferenceTags = a2.getString(attr);
                    setReferenceTags(this.mReferenceTags);
                }
            }
            a2.recycle();
        }
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String str = this.mReferenceIds;
        if (str != null) {
            setIds(str);
        }
        String str2 = this.mReferenceTags;
        if (str2 != null) {
            setReferenceTags(str2);
        }
    }

    public void addView(View view) {
        if (view != this) {
            if (view.getId() == -1) {
                Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have an id");
            } else if (view.getParent() == null) {
                Log.e("ConstraintHelper", "Views added to a ConstraintHelper need to have a parent");
            } else {
                this.mReferenceIds = null;
                addRscID(view.getId());
                requestLayout();
            }
        }
    }

    public void removeView(View view) {
        int i;
        int id = view.getId();
        if (id != -1) {
            this.mReferenceIds = null;
            int i2 = 0;
            while (true) {
                if (i2 >= this.mCount) {
                    break;
                } else if (this.mIds[i2] == id) {
                    int j = i2;
                    while (true) {
                        i = this.mCount;
                        if (j >= i - 1) {
                            break;
                        }
                        int[] iArr = this.mIds;
                        iArr[j] = iArr[j + 1];
                        j++;
                    }
                    this.mIds[i - 1] = 0;
                    this.mCount = i - 1;
                } else {
                    i2++;
                }
            }
            requestLayout();
        }
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }

    public void setReferencedIds(int[] ids) {
        this.mReferenceIds = null;
        this.mCount = 0;
        for (int i : ids) {
            addRscID(i);
        }
    }

    private void addRscID(int id) {
        if (id != getId()) {
            int i = this.mCount + 1;
            int[] iArr = this.mIds;
            if (i > iArr.length) {
                this.mIds = Arrays.copyOf(iArr, iArr.length * 2);
            }
            int[] iArr2 = this.mIds;
            int i2 = this.mCount;
            iArr2[i2] = id;
            this.mCount = i2 + 1;
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mUseViewMeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public void validateParams() {
        if (this.mHelperWidget != null) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params instanceof ConstraintLayout.LayoutParams) {
                ((ConstraintLayout.LayoutParams) params).widget = (ConstraintWidget) this.mHelperWidget;
            }
        }
    }

    private void addID(String idString) {
        if (idString != null && idString.length() != 0 && this.myContext != null) {
            String idString2 = idString.trim();
            if (getParent() instanceof ConstraintLayout) {
                ConstraintLayout parent = (ConstraintLayout) getParent();
            }
            int rscId = findId(idString2);
            if (rscId != 0) {
                this.mMap.put(Integer.valueOf(rscId), idString2);
                addRscID(rscId);
                return;
            }
            Log.w("ConstraintHelper", "Could not find id of \"" + idString2 + "\"");
        }
    }

    private void addTag(String tagString) {
        if (!(tagString == null || tagString.length() == 0 || this.myContext == null)) {
            String tagString2 = tagString.trim();
            ConstraintLayout parent = null;
            if (getParent() instanceof ConstraintLayout) {
                parent = (ConstraintLayout) getParent();
            }
            if (parent == null) {
                Log.w("ConstraintHelper", "Parent not a ConstraintLayout");
                return;
            }
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = parent.getChildAt(i);
                ViewGroup.LayoutParams params = v.getLayoutParams();
                if ((params instanceof ConstraintLayout.LayoutParams) && tagString2.equals(((ConstraintLayout.LayoutParams) params).constraintTag)) {
                    if (v.getId() == -1) {
                        Log.w("ConstraintHelper", "to use ConstraintTag view " + v.getClass().getSimpleName() + " must have an ID");
                    } else {
                        addRscID(v.getId());
                    }
                }
            }
        }
    }

    private int findId(String referenceId) {
        ConstraintLayout parent = null;
        if (getParent() instanceof ConstraintLayout) {
            parent = (ConstraintLayout) getParent();
        }
        int rscId = 0;
        if (isInEditMode() && parent != null) {
            Object value = parent.getDesignInformation(0, referenceId);
            if (value instanceof Integer) {
                rscId = ((Integer) value).intValue();
            }
        }
        if (rscId == 0 && parent != null) {
            rscId = findId(parent, referenceId);
        }
        if (rscId == 0) {
            try {
                rscId = R.id.class.getField(referenceId).getInt(null);
            } catch (Exception e) {
            }
        }
        if (rscId == 0) {
            return this.myContext.getResources().getIdentifier(referenceId, "id", this.myContext.getPackageName());
        }
        return rscId;
    }

    private int findId(ConstraintLayout container, String idString) {
        Resources resources;
        if (idString == null || container == null || (resources = this.myContext.getResources()) == null) {
            return 0;
        }
        int count = container.getChildCount();
        for (int j = 0; j < count; j++) {
            View child = container.getChildAt(j);
            if (child.getId() != -1) {
                String res = null;
                try {
                    res = resources.getResourceEntryName(child.getId());
                } catch (Resources.NotFoundException e) {
                }
                if (idString.equals(res)) {
                    return child.getId();
                }
            }
        }
        return 0;
    }

    protected void setIds(String idList) {
        this.mReferenceIds = idList;
        if (idList != null) {
            int begin = 0;
            this.mCount = 0;
            while (true) {
                int end = idList.indexOf(44, begin);
                if (end == -1) {
                    addID(idList.substring(begin));
                    return;
                } else {
                    addID(idList.substring(begin, end));
                    begin = end + 1;
                }
            }
        }
    }

    protected void setReferenceTags(String tagList) {
        this.mReferenceTags = tagList;
        if (tagList != null) {
            int begin = 0;
            this.mCount = 0;
            while (true) {
                int end = tagList.indexOf(44, begin);
                if (end == -1) {
                    addTag(tagList.substring(begin));
                    return;
                } else {
                    addTag(tagList.substring(begin, end));
                    begin = end + 1;
                }
            }
        }
    }

    protected void applyLayoutFeatures(ConstraintLayout container) {
        int visibility = getVisibility();
        float elevation = 0.0f;
        if (Build.VERSION.SDK_INT >= 21) {
            elevation = getElevation();
        }
        for (int i = 0; i < this.mCount; i++) {
            View view = container.getViewById(this.mIds[i]);
            if (view != null) {
                view.setVisibility(visibility);
                if (elevation > 0.0f && Build.VERSION.SDK_INT >= 21) {
                    view.setTranslationZ(view.getTranslationZ() + elevation);
                }
            }
        }
    }

    protected void applyLayoutFeatures() {
        ViewParent parent = getParent();
        if (parent != null && (parent instanceof ConstraintLayout)) {
            applyLayoutFeatures((ConstraintLayout) parent);
        }
    }

    public void updatePreLayout(ConstraintLayout container) {
        String candidate;
        int foundId;
        if (isInEditMode()) {
            setIds(this.mReferenceIds);
        }
        Helper helper = this.mHelperWidget;
        if (helper != null) {
            helper.removeAllIds();
            for (int i = 0; i < this.mCount; i++) {
                int id = this.mIds[i];
                View view = container.getViewById(id);
                if (view == null && (foundId = findId(container, (candidate = this.mMap.get(Integer.valueOf(id))))) != 0) {
                    this.mIds[i] = foundId;
                    this.mMap.put(Integer.valueOf(foundId), candidate);
                    view = container.getViewById(foundId);
                }
                if (view != null) {
                    this.mHelperWidget.add(container.getViewWidget(view));
                }
            }
            this.mHelperWidget.updateConstraints(container.mLayoutWidget);
        }
    }

    public void updatePreLayout(ConstraintWidgetContainer container, Helper helper, SparseArray<ConstraintWidget> map) {
        helper.removeAllIds();
        for (int i = 0; i < this.mCount; i++) {
            helper.add(map.get(this.mIds[i]));
        }
    }

    protected View[] getViews(ConstraintLayout layout) {
        View[] viewArr = this.mViews;
        if (viewArr == null || viewArr.length != this.mCount) {
            this.mViews = new View[this.mCount];
        }
        for (int i = 0; i < this.mCount; i++) {
            this.mViews[i] = layout.getViewById(this.mIds[i]);
        }
        return this.mViews;
    }

    public void updatePostLayout(ConstraintLayout container) {
    }

    public void updatePostMeasure(ConstraintLayout container) {
    }

    public void updatePostConstraints(ConstraintLayout constainer) {
    }

    public void updatePreDraw(ConstraintLayout container) {
    }

    public void loadParameters(ConstraintSet.Constraint constraint, HelperWidget child, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> mapIdToWidget) {
        if (constraint.layout.mReferenceIds != null) {
            setReferencedIds(constraint.layout.mReferenceIds);
        } else if (constraint.layout.mReferenceIdString != null && constraint.layout.mReferenceIdString.length() > 0) {
            constraint.layout.mReferenceIds = convertReferenceString(this, constraint.layout.mReferenceIdString);
        }
        child.removeAllIds();
        if (constraint.layout.mReferenceIds != null) {
            for (int i = 0; i < constraint.layout.mReferenceIds.length; i++) {
                ConstraintWidget widget = mapIdToWidget.get(constraint.layout.mReferenceIds[i]);
                if (widget != null) {
                    child.add(widget);
                }
            }
        }
    }

    private int[] convertReferenceString(View view, String referenceIdString) {
        String[] split = referenceIdString.split(",");
        view.getContext();
        int[] rscIds = new int[split.length];
        int count = 0;
        for (String idString : split) {
            int id = findId(idString.trim());
            if (id != 0) {
                rscIds[count] = id;
                count++;
            }
        }
        if (count != split.length) {
            return Arrays.copyOf(rscIds, count);
        }
        return rscIds;
    }

    public void resolveRtl(ConstraintWidget widget, boolean isRtl) {
    }

    @Override // android.view.View
    public void setTag(int key, Object tag) {
        super.setTag(key, tag);
        if (tag == null && this.mReferenceIds == null) {
            addRscID(key);
        }
    }
}
