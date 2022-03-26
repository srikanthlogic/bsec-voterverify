package com.futuremind.recyclerviewfastscroll.viewprovider;

import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.futuremind.recyclerviewfastscroll.R;
import com.futuremind.recyclerviewfastscroll.Utils;
import com.futuremind.recyclerviewfastscroll.viewprovider.VisibilityAnimationManager;
/* loaded from: classes.dex */
public class DefaultScrollerViewProvider extends ScrollerViewProvider {
    private View bubble;
    private View handle;

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    public View provideHandleView(ViewGroup container) {
        this.handle = new View(getContext());
        int horizontalInset = 0;
        int verticalInset = getScroller().isVertical() ? 0 : getContext().getResources().getDimensionPixelSize(R.dimen.fastscroll__handle_inset);
        if (getScroller().isVertical()) {
            horizontalInset = getContext().getResources().getDimensionPixelSize(R.dimen.fastscroll__handle_inset);
        }
        Utils.setBackground(this.handle, new InsetDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fastscroll__default_handle), horizontalInset, verticalInset, horizontalInset, verticalInset));
        this.handle.setLayoutParams(new ViewGroup.LayoutParams(getContext().getResources().getDimensionPixelSize(getScroller().isVertical() ? R.dimen.fastscroll__handle_clickable_width : R.dimen.fastscroll__handle_height), getContext().getResources().getDimensionPixelSize(getScroller().isVertical() ? R.dimen.fastscroll__handle_height : R.dimen.fastscroll__handle_clickable_width)));
        return this.handle;
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    public View provideBubbleView(ViewGroup container) {
        this.bubble = LayoutInflater.from(getContext()).inflate(R.layout.fastscroll__default_bubble, container, false);
        return this.bubble;
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    public TextView provideBubbleTextView() {
        return (TextView) this.bubble;
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    public int getBubbleOffset() {
        int i;
        float f;
        if (getScroller().isVertical()) {
            f = ((float) this.handle.getHeight()) / 2.0f;
            i = this.bubble.getHeight();
        } else {
            f = ((float) this.handle.getWidth()) / 2.0f;
            i = this.bubble.getWidth();
        }
        return (int) (f - ((float) i));
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    protected ViewBehavior provideHandleBehavior() {
        return null;
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ScrollerViewProvider
    protected ViewBehavior provideBubbleBehavior() {
        return new DefaultBubbleBehavior(new VisibilityAnimationManager.Builder(this.bubble).withPivotX(1.0f).withPivotY(1.0f).build());
    }
}
