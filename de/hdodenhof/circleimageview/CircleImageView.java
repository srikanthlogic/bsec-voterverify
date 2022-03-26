package de.hdodenhof.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
/* loaded from: classes3.dex */
public class CircleImageView extends ImageView {
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final int DEFAULT_BORDER_COLOR = -16777216;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_FILL_COLOR = 0;
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private final Paint mBitmapPaint;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBorderColor;
    private boolean mBorderOverlay;
    private final Paint mBorderPaint;
    private float mBorderRadius;
    private final RectF mBorderRect;
    private int mBorderWidth;
    private ColorFilter mColorFilter;
    private boolean mDisableCircularTransformation;
    private float mDrawableRadius;
    private final RectF mDrawableRect;
    private int mFillColor;
    private final Paint mFillPaint;
    private boolean mReady;
    private boolean mSetupPending;
    private final Matrix mShaderMatrix;
    private static final ImageView.ScaleType SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public CircleImageView(Context context) {
        super(context);
        this.mDrawableRect = new RectF();
        this.mBorderRect = new RectF();
        this.mShaderMatrix = new Matrix();
        this.mBitmapPaint = new Paint();
        this.mBorderPaint = new Paint();
        this.mFillPaint = new Paint();
        this.mBorderColor = -16777216;
        this.mBorderWidth = 0;
        this.mFillColor = 0;
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDrawableRect = new RectF();
        this.mBorderRect = new RectF();
        this.mShaderMatrix = new Matrix();
        this.mBitmapPaint = new Paint();
        this.mBorderPaint = new Paint();
        this.mFillPaint = new Paint();
        this.mBorderColor = -16777216;
        this.mBorderWidth = 0;
        this.mFillColor = 0;
        TypedArray a2 = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        this.mBorderWidth = a2.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, 0);
        this.mBorderColor = a2.getColor(R.styleable.CircleImageView_civ_border_color, -16777216);
        this.mBorderOverlay = a2.getBoolean(R.styleable.CircleImageView_civ_border_overlay, false);
        this.mFillColor = a2.getColor(R.styleable.CircleImageView_civ_fill_color, 0);
        a2.recycle();
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        this.mReady = true;
        if (this.mSetupPending) {
            setup();
            this.mSetupPending = false;
        }
    }

    @Override // android.widget.ImageView
    public ImageView.ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override // android.widget.ImageView
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDisableCircularTransformation) {
            super.onDraw(canvas);
        } else if (this.mBitmap != null) {
            if (this.mFillColor != 0) {
                canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mFillPaint);
            }
            canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mBitmapPaint);
            if (this.mBorderWidth > 0) {
                canvas.drawCircle(this.mBorderRect.centerX(), this.mBorderRect.centerY(), this.mBorderRadius, this.mBorderPaint);
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override // android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override // android.view.View
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor != this.mBorderColor) {
            this.mBorderColor = borderColor;
            this.mBorderPaint.setColor(this.mBorderColor);
            invalidate();
        }
    }

    @Deprecated
    public void setBorderColorResource(int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    @Deprecated
    public int getFillColor() {
        return this.mFillColor;
    }

    @Deprecated
    public void setFillColor(int fillColor) {
        if (fillColor != this.mFillColor) {
            this.mFillColor = fillColor;
            this.mFillPaint.setColor(fillColor);
            invalidate();
        }
    }

    @Deprecated
    public void setFillColorResource(int fillColorRes) {
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }

    public int getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth != this.mBorderWidth) {
            this.mBorderWidth = borderWidth;
            setup();
        }
    }

    public boolean isBorderOverlay() {
        return this.mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay != this.mBorderOverlay) {
            this.mBorderOverlay = borderOverlay;
            setup();
        }
    }

    public boolean isDisableCircularTransformation() {
        return this.mDisableCircularTransformation;
    }

    public void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (this.mDisableCircularTransformation != disableCircularTransformation) {
            this.mDisableCircularTransformation = disableCircularTransformation;
            initializeBitmap();
        }
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override // android.widget.ImageView
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override // android.widget.ImageView
    public void setColorFilter(ColorFilter cf) {
        if (cf != this.mColorFilter) {
            this.mColorFilter = cf;
            applyColorFilter();
            invalidate();
        }
    }

    @Override // android.widget.ImageView
    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    private void applyColorFilter() {
        Paint paint = this.mBitmapPaint;
        if (paint != null) {
            paint.setColorFilter(this.mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {
        if (this.mDisableCircularTransformation) {
            this.mBitmap = null;
        } else {
            this.mBitmap = getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    private void setup() {
        int i;
        if (!this.mReady) {
            this.mSetupPending = true;
        } else if (getWidth() != 0 || getHeight() != 0) {
            Bitmap bitmap = this.mBitmap;
            if (bitmap == null) {
                invalidate();
                return;
            }
            this.mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mBitmapPaint.setAntiAlias(true);
            this.mBitmapPaint.setShader(this.mBitmapShader);
            this.mBorderPaint.setStyle(Paint.Style.STROKE);
            this.mBorderPaint.setAntiAlias(true);
            this.mBorderPaint.setColor(this.mBorderColor);
            this.mBorderPaint.setStrokeWidth((float) this.mBorderWidth);
            this.mFillPaint.setStyle(Paint.Style.FILL);
            this.mFillPaint.setAntiAlias(true);
            this.mFillPaint.setColor(this.mFillColor);
            this.mBitmapHeight = this.mBitmap.getHeight();
            this.mBitmapWidth = this.mBitmap.getWidth();
            this.mBorderRect.set(calculateBounds());
            this.mBorderRadius = Math.min((this.mBorderRect.height() - ((float) this.mBorderWidth)) / 2.0f, (this.mBorderRect.width() - ((float) this.mBorderWidth)) / 2.0f);
            this.mDrawableRect.set(this.mBorderRect);
            if (!this.mBorderOverlay && (i = this.mBorderWidth) > 0) {
                this.mDrawableRect.inset(((float) i) - 1.0f, ((float) i) - 1.0f);
            }
            this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0f, this.mDrawableRect.width() / 2.0f);
            applyColorFilter();
            updateShaderMatrix();
            invalidate();
        }
    }

    private RectF calculateBounds() {
        int availableWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int availableHeight = (getHeight() - getPaddingTop()) - getPaddingBottom();
        int sideLength = Math.min(availableWidth, availableHeight);
        float left = ((float) getPaddingLeft()) + (((float) (availableWidth - sideLength)) / 2.0f);
        float top = ((float) getPaddingTop()) + (((float) (availableHeight - sideLength)) / 2.0f);
        return new RectF(left, top, ((float) sideLength) + left, ((float) sideLength) + top);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0.0f;
        float dy = 0.0f;
        this.mShaderMatrix.set(null);
        if (((float) this.mBitmapWidth) * this.mDrawableRect.height() > this.mDrawableRect.width() * ((float) this.mBitmapHeight)) {
            scale = this.mDrawableRect.height() / ((float) this.mBitmapHeight);
            dx = (this.mDrawableRect.width() - (((float) this.mBitmapWidth) * scale)) * 0.5f;
        } else {
            scale = this.mDrawableRect.width() / ((float) this.mBitmapWidth);
            dy = (this.mDrawableRect.height() - (((float) this.mBitmapHeight) * scale)) * 0.5f;
        }
        this.mShaderMatrix.setScale(scale, scale);
        this.mShaderMatrix.postTranslate(((float) ((int) (dx + 0.5f))) + this.mDrawableRect.left, ((float) ((int) (0.5f + dy))) + this.mDrawableRect.top);
        this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
    }
}
