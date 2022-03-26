package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.R;
import androidx.recyclerview.widget.ItemTouchHelper;
/* loaded from: classes.dex */
public class MockView extends View {
    private Paint mPaintDiagonals = new Paint();
    private Paint mPaintText = new Paint();
    private Paint mPaintTextBackground = new Paint();
    private boolean mDrawDiagonals = true;
    private boolean mDrawLabel = true;
    protected String mText = null;
    private Rect mTextBounds = new Rect();
    private int mDiagonalsColor = Color.argb(255, 0, 0, 0);
    private int mTextColor = Color.argb(255, (int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, (int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, (int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    private int mTextBackgroundColor = Color.argb(255, 50, 50, 50);
    private int mMargin = 4;

    public MockView(Context context) {
        super(context);
        init(context, null);
    }

    public MockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.MockView);
            int N = a2.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.MockView_mock_label) {
                    this.mText = a2.getString(attr);
                } else if (attr == R.styleable.MockView_mock_showDiagonals) {
                    this.mDrawDiagonals = a2.getBoolean(attr, this.mDrawDiagonals);
                } else if (attr == R.styleable.MockView_mock_diagonalsColor) {
                    this.mDiagonalsColor = a2.getColor(attr, this.mDiagonalsColor);
                } else if (attr == R.styleable.MockView_mock_labelBackgroundColor) {
                    this.mTextBackgroundColor = a2.getColor(attr, this.mTextBackgroundColor);
                } else if (attr == R.styleable.MockView_mock_labelColor) {
                    this.mTextColor = a2.getColor(attr, this.mTextColor);
                } else if (attr == R.styleable.MockView_mock_showLabel) {
                    this.mDrawLabel = a2.getBoolean(attr, this.mDrawLabel);
                }
            }
            a2.recycle();
        }
        if (this.mText == null) {
            try {
                this.mText = context.getResources().getResourceEntryName(getId());
            } catch (Exception e) {
            }
        }
        this.mPaintDiagonals.setColor(this.mDiagonalsColor);
        this.mPaintDiagonals.setAntiAlias(true);
        this.mPaintText.setColor(this.mTextColor);
        this.mPaintText.setAntiAlias(true);
        this.mPaintTextBackground.setColor(this.mTextBackgroundColor);
        this.mMargin = Math.round(((float) this.mMargin) * (getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (this.mDrawDiagonals) {
            w--;
            h--;
            canvas.drawLine(0.0f, 0.0f, (float) w, (float) h, this.mPaintDiagonals);
            canvas.drawLine(0.0f, (float) h, (float) w, 0.0f, this.mPaintDiagonals);
            canvas.drawLine(0.0f, 0.0f, (float) w, 0.0f, this.mPaintDiagonals);
            canvas.drawLine((float) w, 0.0f, (float) w, (float) h, this.mPaintDiagonals);
            canvas.drawLine((float) w, (float) h, 0.0f, (float) h, this.mPaintDiagonals);
            canvas.drawLine(0.0f, (float) h, 0.0f, 0.0f, this.mPaintDiagonals);
        }
        String str = this.mText;
        if (str != null && this.mDrawLabel) {
            this.mPaintText.getTextBounds(str, 0, str.length(), this.mTextBounds);
            float tx = ((float) (w - this.mTextBounds.width())) / 2.0f;
            float ty = (((float) (h - this.mTextBounds.height())) / 2.0f) + ((float) this.mTextBounds.height());
            this.mTextBounds.offset((int) tx, (int) ty);
            Rect rect = this.mTextBounds;
            rect.set(rect.left - this.mMargin, this.mTextBounds.top - this.mMargin, this.mTextBounds.right + this.mMargin, this.mTextBounds.bottom + this.mMargin);
            canvas.drawRect(this.mTextBounds, this.mPaintTextBackground);
            canvas.drawText(this.mText, tx, ty, this.mPaintText);
        }
    }
}
