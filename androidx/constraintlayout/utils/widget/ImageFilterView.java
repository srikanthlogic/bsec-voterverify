package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.R;
/* loaded from: classes.dex */
public class ImageFilterView extends AppCompatImageView {
    LayerDrawable mLayer;
    Drawable[] mLayers;
    private Path mPath;
    RectF mRect;
    ViewOutlineProvider mViewOutlineProvider;
    private ImageMatrix mImageMatrix = new ImageMatrix();
    private boolean mOverlay = true;
    private float mCrossfade = 0.0f;
    private float mRoundPercent = 0.0f;
    private float mRound = Float.NaN;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ImageMatrix {
        float[] m = new float[20];
        ColorMatrix mColorMatrix = new ColorMatrix();
        ColorMatrix mTmpColorMatrix = new ColorMatrix();
        float mBrightness = 1.0f;
        float mSaturation = 1.0f;
        float mContrast = 1.0f;
        float mWarmth = 1.0f;

        private void saturation(float saturationStrength) {
            float MS = 1.0f - saturationStrength;
            float Rt = 0.2999f * MS;
            float Gt = 0.587f * MS;
            float Bt = 0.114f * MS;
            float[] fArr = this.m;
            fArr[0] = Rt + saturationStrength;
            fArr[1] = Gt;
            fArr[2] = Bt;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = Rt;
            fArr[6] = Gt + saturationStrength;
            fArr[7] = Bt;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = Rt;
            fArr[11] = Gt;
            fArr[12] = Bt + saturationStrength;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        /* JADX INFO: Multiple debug info for r1v2 float: [D('tmpColor_b' float), D('colorR' float)] */
        private void warmth(float warmth) {
            float colorG;
            float colorR;
            float colorB;
            float colorG2;
            float colorR2;
            float colorB2;
            float centiKelvin = (5000.0f / (warmth <= 0.0f ? 0.01f : warmth)) / 100.0f;
            if (centiKelvin > 66.0f) {
                float tmp = centiKelvin - 60.0f;
                colorR = ((float) Math.pow((double) tmp, -0.13320475816726685d)) * 329.69873f;
                colorG = ((float) Math.pow((double) tmp, 0.07551484555006027d)) * 288.12216f;
            } else {
                colorG = (((float) Math.log((double) centiKelvin)) * 99.4708f) - 161.11957f;
                colorR = 255.0f;
            }
            if (centiKelvin >= 66.0f) {
                colorB = 255.0f;
            } else if (centiKelvin > 19.0f) {
                colorB = (((float) Math.log((double) (centiKelvin - 10.0f))) * 138.51773f) - 305.0448f;
            } else {
                colorB = 0.0f;
            }
            float tmpColor_r = Math.min(255.0f, Math.max(colorR, 0.0f));
            float tmpColor_g = Math.min(255.0f, Math.max(colorG, 0.0f));
            float colorR3 = Math.min(255.0f, Math.max(colorB, 0.0f));
            float centiKelvin2 = 5000.0f / 100.0f;
            if (centiKelvin2 > 66.0f) {
                float tmp2 = centiKelvin2 - 60.0f;
                colorR2 = ((float) Math.pow((double) tmp2, -0.13320475816726685d)) * 329.69873f;
                colorG2 = ((float) Math.pow((double) tmp2, 0.07551484555006027d)) * 288.12216f;
            } else {
                colorG2 = (((float) Math.log((double) centiKelvin2)) * 99.4708f) - 161.11957f;
                colorR2 = 255.0f;
            }
            if (centiKelvin2 >= 66.0f) {
                colorB2 = 255.0f;
            } else if (centiKelvin2 > 19.0f) {
                colorB2 = (((float) Math.log((double) (centiKelvin2 - 10.0f))) * 138.51773f) - 305.0448f;
            } else {
                colorB2 = 0.0f;
            }
            float tmpColor_r2 = Math.min(255.0f, Math.max(colorR2, 0.0f));
            float tmpColor_g2 = Math.min(255.0f, Math.max(colorG2, 0.0f));
            float[] fArr = this.m;
            fArr[0] = tmpColor_r / tmpColor_r2;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = tmpColor_g / tmpColor_g2;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = colorR3 / Math.min(255.0f, Math.max(colorB2, 0.0f));
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        private void brightness(float brightness) {
            float[] fArr = this.m;
            fArr[0] = brightness;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = brightness;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = brightness;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void updateMatrix(ImageView view) {
            this.mColorMatrix.reset();
            boolean filter = false;
            float f = this.mSaturation;
            if (f != 1.0f) {
                saturation(f);
                this.mColorMatrix.set(this.m);
                filter = true;
            }
            float f2 = this.mContrast;
            if (f2 != 1.0f) {
                this.mTmpColorMatrix.setScale(f2, f2, f2, 1.0f);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            float f3 = this.mWarmth;
            if (f3 != 1.0f) {
                warmth(f3);
                this.mTmpColorMatrix.set(this.m);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            float f4 = this.mBrightness;
            if (f4 != 1.0f) {
                brightness(f4);
                this.mTmpColorMatrix.set(this.m);
                this.mColorMatrix.postConcat(this.mTmpColorMatrix);
                filter = true;
            }
            if (filter) {
                view.setColorFilter(new ColorMatrixColorFilter(this.mColorMatrix));
            } else {
                view.clearColorFilter();
            }
        }
    }

    public ImageFilterView(Context context) {
        super(context);
        init(context, null);
    }

    public ImageFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a2 = getContext().obtainStyledAttributes(attrs, R.styleable.ImageFilterView);
            int N = a2.getIndexCount();
            Drawable drawable = a2.getDrawable(R.styleable.ImageFilterView_altSrc);
            for (int i = 0; i < N; i++) {
                int attr = a2.getIndex(i);
                if (attr == R.styleable.ImageFilterView_crossfade) {
                    this.mCrossfade = a2.getFloat(attr, 0.0f);
                } else if (attr == R.styleable.ImageFilterView_warmth) {
                    setWarmth(a2.getFloat(attr, 0.0f));
                } else if (attr == R.styleable.ImageFilterView_saturation) {
                    setSaturation(a2.getFloat(attr, 0.0f));
                } else if (attr == R.styleable.ImageFilterView_contrast) {
                    setContrast(a2.getFloat(attr, 0.0f));
                } else if (attr == R.styleable.ImageFilterView_round) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRound(a2.getDimension(attr, 0.0f));
                    }
                } else if (attr == R.styleable.ImageFilterView_roundPercent) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        setRoundPercent(a2.getFloat(attr, 0.0f));
                    }
                } else if (attr == R.styleable.ImageFilterView_overlay) {
                    setOverlay(a2.getBoolean(attr, this.mOverlay));
                }
            }
            a2.recycle();
            if (drawable != null) {
                this.mLayers = new Drawable[2];
                this.mLayers[0] = getDrawable();
                Drawable[] drawableArr = this.mLayers;
                drawableArr[1] = drawable;
                this.mLayer = new LayerDrawable(drawableArr);
                this.mLayer.getDrawable(1).setAlpha((int) (this.mCrossfade * 255.0f));
                super.setImageDrawable(this.mLayer);
            }
        }
    }

    private void setOverlay(boolean overlay) {
        this.mOverlay = overlay;
    }

    public void setSaturation(float saturation) {
        ImageMatrix imageMatrix = this.mImageMatrix;
        imageMatrix.mSaturation = saturation;
        imageMatrix.updateMatrix(this);
    }

    public float getSaturation() {
        return this.mImageMatrix.mSaturation;
    }

    public void setContrast(float contrast) {
        ImageMatrix imageMatrix = this.mImageMatrix;
        imageMatrix.mContrast = contrast;
        imageMatrix.updateMatrix(this);
    }

    public float getContrast() {
        return this.mImageMatrix.mContrast;
    }

    public void setWarmth(float warmth) {
        ImageMatrix imageMatrix = this.mImageMatrix;
        imageMatrix.mWarmth = warmth;
        imageMatrix.updateMatrix(this);
    }

    public float getWarmth() {
        return this.mImageMatrix.mWarmth;
    }

    public void setCrossfade(float crossfade) {
        this.mCrossfade = crossfade;
        if (this.mLayers != null) {
            if (!this.mOverlay) {
                this.mLayer.getDrawable(0).setAlpha((int) ((1.0f - this.mCrossfade) * 255.0f));
            }
            this.mLayer.getDrawable(1).setAlpha((int) (this.mCrossfade * 255.0f));
            super.setImageDrawable(this.mLayer);
        }
    }

    public float getCrossfade() {
        return this.mCrossfade;
    }

    public void setBrightness(float brightness) {
        ImageMatrix imageMatrix = this.mImageMatrix;
        imageMatrix.mBrightness = brightness;
        imageMatrix.updateMatrix(this);
    }

    public float getBrightness() {
        return this.mImageMatrix.mBrightness;
    }

    public void setRoundPercent(float round) {
        boolean change = this.mRoundPercent != round;
        this.mRoundPercent = round;
        if (this.mRoundPercent != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    this.mViewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterView.1
                        @Override // android.view.ViewOutlineProvider
                        public void getOutline(View view, Outline outline) {
                            int w = ImageFilterView.this.getWidth();
                            int h = ImageFilterView.this.getHeight();
                            outline.setRoundRect(0, 0, w, h, (((float) Math.min(w, h)) * ImageFilterView.this.mRoundPercent) / 2.0f);
                        }
                    };
                    setOutlineProvider(this.mViewOutlineProvider);
                }
                setClipToOutline(true);
            }
            int w = getWidth();
            int h = getHeight();
            float r = (((float) Math.min(w, h)) * this.mRoundPercent) / 2.0f;
            this.mRect.set(0.0f, 0.0f, (float) w, (float) h);
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, r, r, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (change && Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    public void setRound(float round) {
        if (Float.isNaN(round)) {
            this.mRound = round;
            float tmp = this.mRoundPercent;
            this.mRoundPercent = -1.0f;
            setRoundPercent(tmp);
            return;
        }
        boolean change = this.mRound != round;
        this.mRound = round;
        if (this.mRound != 0.0f) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            if (this.mRect == null) {
                this.mRect = new RectF();
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mViewOutlineProvider == null) {
                    this.mViewOutlineProvider = new ViewOutlineProvider() { // from class: androidx.constraintlayout.utils.widget.ImageFilterView.2
                        @Override // android.view.ViewOutlineProvider
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0, 0, ImageFilterView.this.getWidth(), ImageFilterView.this.getHeight(), ImageFilterView.this.mRound);
                        }
                    };
                    setOutlineProvider(this.mViewOutlineProvider);
                }
                setClipToOutline(true);
            }
            this.mRect.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            this.mPath.reset();
            Path path = this.mPath;
            RectF rectF = this.mRect;
            float f = this.mRound;
            path.addRoundRect(rectF, f, f, Path.Direction.CW);
        } else if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(false);
        }
        if (change && Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    public float getRoundPercent() {
        return this.mRoundPercent;
    }

    public float getRound() {
        return this.mRound;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        boolean clip = false;
        if (!(Build.VERSION.SDK_INT >= 21 || this.mRoundPercent == 0.0f || this.mPath == null)) {
            clip = true;
            canvas.save();
            canvas.clipPath(this.mPath);
        }
        super.draw(canvas);
        if (clip) {
            canvas.restore();
        }
    }
}
