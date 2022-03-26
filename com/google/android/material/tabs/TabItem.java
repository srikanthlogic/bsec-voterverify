package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;
/* loaded from: classes.dex */
public class TabItem extends View {
    public final int customLayout;
    public final Drawable icon;
    public final CharSequence text;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TintTypedArray a2 = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TabItem);
        this.text = a2.getText(R.styleable.TabItem_android_text);
        this.icon = a2.getDrawable(R.styleable.TabItem_android_icon);
        this.customLayout = a2.getResourceId(R.styleable.TabItem_android_layout, 0);
        a2.recycle();
    }
}
