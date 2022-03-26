package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.LinearLayoutCompat;
/* loaded from: classes.dex */
public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING;
    static final int MIN_CELL_SIZE;
    private static final String TAG;
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    /* loaded from: classes.dex */
    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    /* loaded from: classes.dex */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBaselineAligned(false);
        float density = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * density);
        this.mGeneratedItemPadding = (int) (4.0f * density);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    public void setPopupTheme(int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public void setPresenter(ActionMenuPresenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.setMenuView(this);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MenuBuilder menuBuilder;
        boolean wasFormatted = this.mFormatItems;
        this.mFormatItems = View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824;
        if (wasFormatted != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (!(!this.mFormatItems || (menuBuilder = this.mMenu) == null || widthSize == this.mFormatItemsWidth)) {
            this.mFormatItemsWidth = widthSize;
            menuBuilder.onItemsChanged(true);
        }
        int childCount = getChildCount();
        if (!this.mFormatItems || childCount <= 0) {
            for (int i = 0; i < childCount; i++) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                lp.rightMargin = 0;
                lp.leftMargin = 0;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        onMeasureExactFormat(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX WARN: Type inference failed for: r10v30, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r10v41 */
    /* JADX WARN: Type inference failed for: r10v42 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private void onMeasureExactFormat(int widthMeasureSpec, int heightMeasureSpec) {
        int maxChildHeight;
        int widthSize;
        boolean needsExpansion;
        int cellSize;
        int heightSize;
        int i;
        int heightPadding;
        int cellSizeRemaining;
        ?? r10;
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize2 = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int heightPadding2 = getPaddingTop() + getPaddingBottom();
        int itemHeightSpec = getChildMeasureSpec(heightMeasureSpec, heightPadding2, -2);
        int widthSize3 = widthSize2 - widthPadding;
        int i2 = this.mMinCellSize;
        int cellCount = widthSize3 / i2;
        int cellSizeRemaining2 = widthSize3 % i2;
        if (cellCount == 0) {
            setMeasuredDimension(widthSize3, 0);
            return;
        }
        int cellSize2 = i2 + (cellSizeRemaining2 / cellCount);
        boolean hasOverflow = false;
        int childCount = getChildCount();
        int i3 = 0;
        int maxChildHeight2 = 0;
        int visibleItemCount = 0;
        int expandableItemCount = 0;
        int maxCellsUsed = 0;
        int cellsRemaining = cellCount;
        long smallestItemsAt = 0;
        while (i3 < childCount) {
            View child = getChildAt(i3);
            if (child.getVisibility() == 8) {
                heightPadding = heightPadding2;
                cellSizeRemaining = cellSizeRemaining2;
            } else {
                boolean isGeneratedItem = child instanceof ActionMenuItemView;
                visibleItemCount++;
                if (isGeneratedItem) {
                    int i4 = this.mGeneratedItemPadding;
                    cellSizeRemaining = cellSizeRemaining2;
                    r10 = 0;
                    child.setPadding(i4, 0, i4, 0);
                } else {
                    cellSizeRemaining = cellSizeRemaining2;
                    r10 = 0;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.expanded = r10;
                int i5 = r10 == true ? 1 : 0;
                int i6 = r10 == true ? 1 : 0;
                int i7 = r10 == true ? 1 : 0;
                lp.extraPixels = i5;
                lp.cellsUsed = r10;
                lp.expandable = r10;
                lp.leftMargin = r10;
                lp.rightMargin = r10;
                lp.preventEdgeOffset = isGeneratedItem && ((ActionMenuItemView) child).hasText();
                int cellsUsed = measureChildForCells(child, cellSize2, lp.isOverflowButton ? 1 : cellsRemaining, itemHeightSpec, heightPadding2);
                maxCellsUsed = Math.max(maxCellsUsed, cellsUsed);
                heightPadding = heightPadding2;
                if (lp.expandable) {
                    expandableItemCount++;
                }
                if (lp.isOverflowButton) {
                    hasOverflow = true;
                }
                cellsRemaining -= cellsUsed;
                maxChildHeight2 = Math.max(maxChildHeight2, child.getMeasuredHeight());
                if (cellsUsed == 1) {
                    smallestItemsAt |= (long) (1 << i3);
                    maxChildHeight2 = maxChildHeight2;
                }
            }
            i3++;
            widthPadding = widthPadding;
            cellCount = cellCount;
            cellSizeRemaining2 = cellSizeRemaining;
            heightPadding2 = heightPadding;
        }
        boolean centerSingleExpandedItem = hasOverflow && visibleItemCount == 2;
        boolean needsExpansion2 = false;
        while (expandableItemCount > 0 && cellsRemaining > 0) {
            long minCellsAt = 0;
            int i8 = 0;
            int minCells = Integer.MAX_VALUE;
            int minCellsItemCount = 0;
            while (i8 < childCount) {
                LayoutParams lp2 = (LayoutParams) getChildAt(i8).getLayoutParams();
                if (lp2.expandable) {
                    if (lp2.cellsUsed < minCells) {
                        minCells = lp2.cellsUsed;
                        minCellsAt = 1 << i8;
                        minCellsItemCount = 1;
                    } else if (lp2.cellsUsed == minCells) {
                        minCellsAt |= 1 << i8;
                        minCellsItemCount++;
                    }
                }
                i8++;
                needsExpansion2 = needsExpansion2;
            }
            needsExpansion = needsExpansion2;
            smallestItemsAt |= minCellsAt;
            if (minCellsItemCount > cellsRemaining) {
                widthSize = widthSize3;
                maxChildHeight = maxChildHeight2;
                break;
            }
            int minCells2 = minCells + 1;
            int i9 = 0;
            while (i9 < childCount) {
                View child2 = getChildAt(i9);
                LayoutParams lp3 = (LayoutParams) child2.getLayoutParams();
                if ((minCellsAt & ((long) (1 << i9))) != 0) {
                    if (centerSingleExpandedItem && lp3.preventEdgeOffset && cellsRemaining == 1) {
                        int i10 = this.mGeneratedItemPadding;
                        child2.setPadding(i10 + cellSize2, 0, i10, 0);
                    }
                    lp3.cellsUsed++;
                    lp3.expanded = true;
                    cellsRemaining--;
                } else if (lp3.cellsUsed == minCells2) {
                    smallestItemsAt |= (long) (1 << i9);
                }
                i9++;
                minCellsItemCount = minCellsItemCount;
                widthSize3 = widthSize3;
                maxChildHeight2 = maxChildHeight2;
            }
            needsExpansion2 = true;
        }
        widthSize = widthSize3;
        maxChildHeight = maxChildHeight2;
        needsExpansion = needsExpansion2;
        boolean singleItem = !hasOverflow && visibleItemCount == 1;
        if (cellsRemaining <= 0 || smallestItemsAt == 0) {
            cellSize = cellSize2;
        } else if (cellsRemaining < visibleItemCount - 1 || singleItem || maxCellsUsed > 1) {
            float expandCount = (float) Long.bitCount(smallestItemsAt);
            if (!singleItem) {
                if ((smallestItemsAt & 1) != 0) {
                    i = 0;
                    if (!((LayoutParams) getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        expandCount -= 0.5f;
                    }
                } else {
                    i = 0;
                }
                cellSize = cellSize2;
                if ((smallestItemsAt & ((long) (1 << (childCount - 1)))) != 0 && !((LayoutParams) getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset) {
                    expandCount -= 0.5f;
                }
            } else {
                cellSize = cellSize2;
                i = 0;
            }
            int extraPixels = expandCount > 0.0f ? (int) (((float) (cellsRemaining * cellSize)) / expandCount) : i;
            int i11 = 0;
            while (i11 < childCount) {
                if ((smallestItemsAt & ((long) (1 << i11))) != 0) {
                    View child3 = getChildAt(i11);
                    LayoutParams lp4 = (LayoutParams) child3.getLayoutParams();
                    if (child3 instanceof ActionMenuItemView) {
                        lp4.extraPixels = extraPixels;
                        lp4.expanded = true;
                        if (i11 == 0 && !lp4.preventEdgeOffset) {
                            lp4.leftMargin = (-extraPixels) / 2;
                        }
                        needsExpansion = true;
                    } else if (lp4.isOverflowButton) {
                        lp4.extraPixels = extraPixels;
                        lp4.expanded = true;
                        lp4.rightMargin = (-extraPixels) / 2;
                        needsExpansion = true;
                    } else {
                        if (i11 != 0) {
                            lp4.leftMargin = extraPixels / 2;
                        }
                        if (i11 != childCount - 1) {
                            lp4.rightMargin = extraPixels / 2;
                        }
                    }
                }
                i11++;
                singleItem = singleItem;
                expandCount = expandCount;
            }
        } else {
            cellSize = cellSize2;
        }
        if (needsExpansion) {
            for (int i12 = 0; i12 < childCount; i12++) {
                View child4 = getChildAt(i12);
                LayoutParams lp5 = (LayoutParams) child4.getLayoutParams();
                if (lp5.expanded) {
                    child4.measure(View.MeasureSpec.makeMeasureSpec((lp5.cellsUsed * cellSize) + lp5.extraPixels, 1073741824), itemHeightSpec);
                }
            }
        }
        if (heightMode != 1073741824) {
            heightSize = maxChildHeight;
        } else {
            heightSize = heightSize2;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    public static int measureChildForCells(View child, int cellSize, int cellsRemaining, int parentHeightMeasureSpec, int parentHeightPadding) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeightSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(parentHeightMeasureSpec) - parentHeightPadding, View.MeasureSpec.getMode(parentHeightMeasureSpec));
        ActionMenuItemView itemView = child instanceof ActionMenuItemView ? (ActionMenuItemView) child : null;
        boolean expandable = false;
        boolean hasText = itemView != null && itemView.hasText();
        int cellsUsed = 0;
        if (cellsRemaining > 0 && (!hasText || cellsRemaining >= 2)) {
            child.measure(View.MeasureSpec.makeMeasureSpec(cellSize * cellsRemaining, Integer.MIN_VALUE), childHeightSpec);
            int measuredWidth = child.getMeasuredWidth();
            cellsUsed = measuredWidth / cellSize;
            if (measuredWidth % cellSize != 0) {
                cellsUsed++;
            }
            if (hasText && cellsUsed < 2) {
                cellsUsed = 2;
            }
        }
        if (!lp.isOverflowButton && hasText) {
            expandable = true;
        }
        lp.expandable = expandable;
        lp.cellsUsed = cellsUsed;
        child.measure(View.MeasureSpec.makeMeasureSpec(cellsUsed * cellSize, 1073741824), childHeightSpec);
        return cellsUsed;
    }

    /* JADX INFO: Multiple debug info for r3v9 int: [D('b' int), D('dividerWidth' int)] */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int i;
        int nonOverflowCount;
        int overflowWidth;
        int dividerWidth;
        int r;
        int l;
        ActionMenuView actionMenuView = this;
        if (!actionMenuView.mFormatItems) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        int childCount = getChildCount();
        int midVertical = (bottom - top) / 2;
        int dividerWidth2 = getDividerWidth();
        int overflowWidth2 = 0;
        int nonOverflowCount2 = 0;
        int widthRemaining = ((right - left) - getPaddingRight()) - getPaddingLeft();
        int i2 = 0;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i3 = 0;
        while (true) {
            i = 8;
            if (i3 >= childCount) {
                break;
            }
            View v = actionMenuView.getChildAt(i3);
            if (v.getVisibility() == 8) {
                dividerWidth = dividerWidth2;
            } else {
                LayoutParams p = (LayoutParams) v.getLayoutParams();
                if (p.isOverflowButton) {
                    overflowWidth2 = v.getMeasuredWidth();
                    if (actionMenuView.hasSupportDividerBeforeChildAt(i3)) {
                        overflowWidth2 += dividerWidth2;
                    }
                    int height = v.getMeasuredHeight();
                    if (isLayoutRtl) {
                        l = getPaddingLeft() + p.leftMargin;
                        r = l + overflowWidth2;
                    } else {
                        r = (getWidth() - getPaddingRight()) - p.rightMargin;
                        l = r - overflowWidth2;
                    }
                    int t = midVertical - (height / 2);
                    dividerWidth = dividerWidth2;
                    v.layout(l, t, r, t + height);
                    widthRemaining -= overflowWidth2;
                    i2 = 1;
                } else {
                    dividerWidth = dividerWidth2;
                    widthRemaining -= (v.getMeasuredWidth() + p.leftMargin) + p.rightMargin;
                    actionMenuView.hasSupportDividerBeforeChildAt(i3);
                    nonOverflowCount2++;
                }
            }
            i3++;
            dividerWidth2 = dividerWidth;
        }
        if (childCount == 1 && i2 == 0) {
            View v2 = actionMenuView.getChildAt(0);
            int width = v2.getMeasuredWidth();
            int height2 = v2.getMeasuredHeight();
            int l2 = ((right - left) / 2) - (width / 2);
            int t2 = midVertical - (height2 / 2);
            v2.layout(l2, t2, l2 + width, t2 + height2);
            return;
        }
        int spacerCount = nonOverflowCount2 - (i2 ^ 1);
        int spacerSize = Math.max(0, spacerCount > 0 ? widthRemaining / spacerCount : 0);
        if (isLayoutRtl) {
            int startRight = getWidth() - getPaddingRight();
            int i4 = 0;
            while (i4 < childCount) {
                View v3 = actionMenuView.getChildAt(i4);
                LayoutParams lp = (LayoutParams) v3.getLayoutParams();
                if (v3.getVisibility() == i) {
                    overflowWidth = overflowWidth2;
                    nonOverflowCount = nonOverflowCount2;
                } else if (lp.isOverflowButton) {
                    overflowWidth = overflowWidth2;
                    nonOverflowCount = nonOverflowCount2;
                } else {
                    int startRight2 = startRight - lp.rightMargin;
                    int width2 = v3.getMeasuredWidth();
                    int height3 = v3.getMeasuredHeight();
                    int t3 = midVertical - (height3 / 2);
                    overflowWidth = overflowWidth2;
                    nonOverflowCount = nonOverflowCount2;
                    v3.layout(startRight2 - width2, t3, startRight2, t3 + height3);
                    startRight = startRight2 - ((lp.leftMargin + width2) + spacerSize);
                }
                i4++;
                overflowWidth2 = overflowWidth;
                nonOverflowCount2 = nonOverflowCount;
                i = 8;
            }
            return;
        }
        int startLeft = getPaddingLeft();
        int i5 = 0;
        while (i5 < childCount) {
            View v4 = actionMenuView.getChildAt(i5);
            LayoutParams lp2 = (LayoutParams) v4.getLayoutParams();
            if (v4.getVisibility() != 8 && !lp2.isOverflowButton) {
                int startLeft2 = startLeft + lp2.leftMargin;
                int width3 = v4.getMeasuredWidth();
                int height4 = v4.getMeasuredHeight();
                int t4 = midVertical - (height4 / 2);
                v4.layout(startLeft2, t4, startLeft2 + width3, t4 + height4);
                startLeft = startLeft2 + lp2.rightMargin + width3 + spacerSize;
            }
            i5++;
            actionMenuView = this;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    public void setOverflowIcon(Drawable icon) {
        getMenu();
        this.mPresenter.setOverflowIcon(icon);
    }

    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void setOverflowReserved(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 16;
        return params;
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams result;
        if (p == null) {
            return generateDefaultLayoutParams();
        }
        if (p instanceof LayoutParams) {
            result = new LayoutParams((LayoutParams) p);
        } else {
            result = new LayoutParams(p);
        }
        if (result.gravity <= 0) {
            result.gravity = 16;
        }
        return result;
    }

    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams result = generateDefaultLayoutParams();
        result.isOverflowButton = true;
        return result;
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.ItemInvoker
    public boolean invokeItem(MenuItemImpl item) {
        return this.mMenu.performItemAction(item, 0);
    }

    @Override // androidx.appcompat.view.menu.MenuView
    public int getWindowAnimations() {
        return 0;
    }

    @Override // androidx.appcompat.view.menu.MenuView
    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            this.mMenu = new MenuBuilder(context);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter(context);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
            if (callback == null) {
                callback = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter.setCallback(callback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    public void setMenuCallbacks(MenuPresenter.Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
    }

    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending();
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    protected boolean hasSupportDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return false;
        }
        View childBefore = getChildAt(childIndex - 1);
        View child = getChildAt(childIndex);
        boolean result = false;
        if (childIndex < getChildCount() && (childBefore instanceof ActionMenuChildView)) {
            result = false | ((ActionMenuChildView) childBefore).needsDividerAfter();
        }
        if (childIndex <= 0 || !(child instanceof ActionMenuChildView)) {
            return result;
        }
        return result | ((ActionMenuChildView) child).needsDividerBefore();
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    public void setExpandedActionViewsExclusive(boolean exclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(exclusive);
    }

    /* loaded from: classes.dex */
    public class MenuBuilderCallback implements MenuBuilder.Callback {
        MenuBuilderCallback() {
            ActionMenuView.this = r1;
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(item);
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menu) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menu);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
        @ViewDebug.ExportedProperty
        public int cellsUsed;
        @ViewDebug.ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug.ExportedProperty
        public int extraPixels;
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams other) {
            super(other);
        }

        public LayoutParams(LayoutParams other) {
            super((ViewGroup.LayoutParams) other);
            this.isOverflowButton = other.isOverflowButton;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.isOverflowButton = false;
        }

        LayoutParams(int width, int height, boolean isOverflowButton) {
            super(width, height);
            this.isOverflowButton = isOverflowButton;
        }
    }
}
