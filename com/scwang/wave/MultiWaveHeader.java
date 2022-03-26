package com.scwang.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class MultiWaveHeader extends ViewGroup {
    private int mCloseColor;
    private float mColorAlpha;
    private int mGradientAngle;
    private boolean mIsRunning;
    private long mLastTime;
    private Matrix mMatrix;
    private Paint mPaint;
    private float mProgress;
    private int mStartColor;
    private float mVelocity;
    private int mWaveHeight;
    private List<Wave> mltWave;

    public MultiWaveHeader(Context context) {
        this(context, null, 0);
    }

    public MultiWaveHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiWaveHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint();
        this.mMatrix = new Matrix();
        this.mltWave = new ArrayList();
        this.mLastTime = 0;
        this.mPaint.setAntiAlias(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiWaveHeader);
        this.mWaveHeight = ta.getDimensionPixelOffset(R.styleable.MultiWaveHeader_mwhWaveHeight, Util.dp2px(50.0f));
        this.mStartColor = ta.getColor(R.styleable.MultiWaveHeader_mwhStartColor, -16421680);
        this.mCloseColor = ta.getColor(R.styleable.MultiWaveHeader_mwhCloseColor, -13520898);
        this.mColorAlpha = ta.getFloat(R.styleable.MultiWaveHeader_mwhColorAlpha, 0.45f);
        this.mProgress = ta.getFloat(R.styleable.MultiWaveHeader_mwhProgress, 1.0f);
        this.mVelocity = ta.getFloat(R.styleable.MultiWaveHeader_mwhVelocity, 1.0f);
        this.mGradientAngle = ta.getInt(R.styleable.MultiWaveHeader_mwhGradientAngle, 45);
        this.mIsRunning = ta.getBoolean(R.styleable.MultiWaveHeader_mwhIsRunning, true);
        if (ta.hasValue(R.styleable.MultiWaveHeader_mwhWaves)) {
            setTag(ta.getString(R.styleable.MultiWaveHeader_mwhWaves));
        } else if (getTag() == null) {
            setTag("70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15");
        }
        ta.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateWavePath(w, h);
        updateLinearGradient(w, h);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mltWave.size() > 0 && this.mPaint != null) {
            int height = getHeight();
            long thisTime = System.currentTimeMillis();
            for (Wave wave : this.mltWave) {
                this.mMatrix.reset();
                canvas.save();
                if (this.mLastTime <= 0 || wave.velocity == 0.0f) {
                    this.mMatrix.setTranslate(wave.offsetX, (1.0f - this.mProgress) * ((float) height));
                    canvas.translate(-wave.offsetX, (-wave.offsetY) - ((1.0f - this.mProgress) * ((float) height)));
                } else {
                    float offsetX = wave.offsetX - (((wave.velocity * this.mVelocity) * ((float) (thisTime - this.mLastTime))) / 1000.0f);
                    if ((-wave.velocity) > 0.0f) {
                        offsetX %= (float) (wave.width / 2);
                    } else {
                        while (offsetX < 0.0f) {
                            offsetX += (float) (wave.width / 2);
                        }
                    }
                    wave.offsetX = offsetX;
                    this.mMatrix.setTranslate(offsetX, (1.0f - this.mProgress) * ((float) height));
                    canvas.translate(-offsetX, (-wave.offsetY) - ((1.0f - this.mProgress) * ((float) height)));
                }
                this.mPaint.getShader().setLocalMatrix(this.mMatrix);
                canvas.drawPath(wave.path, this.mPaint);
                canvas.restore();
            }
            this.mLastTime = thisTime;
        }
        if (this.mIsRunning) {
            invalidate();
        }
    }

    private void updateLinearGradient(int width, int height) {
        int startColor = ColorUtils.setAlphaComponent(this.mStartColor, (int) (this.mColorAlpha * 255.0f));
        int closeColor = ColorUtils.setAlphaComponent(this.mCloseColor, (int) (this.mColorAlpha * 255.0f));
        double w = (double) width;
        double h = (double) (((float) height) * this.mProgress);
        double r = Math.sqrt((w * w) + (h * h)) / 2.0d;
        double y = r * Math.sin((((double) this.mGradientAngle) * 6.283185307179586d) / 360.0d);
        double x = r * Math.cos((((double) this.mGradientAngle) * 6.283185307179586d) / 360.0d);
        this.mPaint.setShader(new LinearGradient((float) ((int) ((w / 2.0d) - x)), (float) ((int) ((h / 2.0d) - y)), (float) ((int) ((w / 2.0d) + x)), (float) ((int) ((h / 2.0d) + y)), startColor, closeColor, Shader.TileMode.CLAMP));
    }

    private void updateWavePath(int w, int h) {
        this.mltWave.clear();
        if (getTag() instanceof String) {
            String[] waves = getTag().toString().split("\\s+");
            if ("-1".equals(getTag())) {
                waves = "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split("\\s+");
            } else if ("-2".equals(getTag())) {
                waves = "0,0,1,0.5,90\n90,0,1,0.5,90".split("\\s+");
            }
            int length = waves.length;
            char c = 0;
            int i = 0;
            while (i < length) {
                String[] args = waves[i].split("\\s*,\\s*");
                if (args.length == 5) {
                    this.mltWave.add(new Wave(Util.dp2px(Float.parseFloat(args[c])), Util.dp2px(Float.parseFloat(args[1])), Util.dp2px(Float.parseFloat(args[4])), Float.parseFloat(args[2]), Float.parseFloat(args[3]), w, h, this.mWaveHeight / 2));
                }
                i++;
                c = 0;
            }
            return;
        }
        this.mltWave.add(new Wave(Util.dp2px(50.0f), Util.dp2px(0.0f), Util.dp2px(5.0f), 1.7f, 2.0f, w, h, this.mWaveHeight / 2));
    }

    public void setWaves(String waves) {
        setTag(waves);
        if (this.mLastTime > 0) {
            updateWavePath(getWidth(), getHeight());
        }
    }

    public int getWaveHeight() {
        return this.mWaveHeight;
    }

    public void setWaveHeight(int waveHeight) {
        this.mWaveHeight = Util.dp2px((float) waveHeight);
        if (!this.mltWave.isEmpty()) {
            updateWavePath(getWidth(), getHeight());
        }
    }

    public float getVelocity() {
        return this.mVelocity;
    }

    public void setVelocity(float velocity) {
        this.mVelocity = velocity;
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        if (this.mPaint != null) {
            updateLinearGradient(getWidth(), getHeight());
        }
    }

    public int getGradientAngle() {
        return this.mGradientAngle;
    }

    public void setGradientAngle(int angle) {
        this.mGradientAngle = angle;
        if (!this.mltWave.isEmpty()) {
            updateLinearGradient(getWidth(), getHeight());
        }
    }

    public int getStartColor() {
        return this.mStartColor;
    }

    public void setStartColor(int color) {
        this.mStartColor = color;
        if (!this.mltWave.isEmpty()) {
            updateLinearGradient(getWidth(), getHeight());
        }
    }

    public void setStartColorId(int colorId) {
        setStartColor(Util.getColor(getContext(), colorId));
    }

    public int getCloseColor() {
        return this.mCloseColor;
    }

    public void setCloseColor(int color) {
        this.mCloseColor = color;
        if (!this.mltWave.isEmpty()) {
            updateLinearGradient(getWidth(), getHeight());
        }
    }

    public void setCloseColorId(int colorId) {
        setCloseColor(Util.getColor(getContext(), colorId));
    }

    public float getColorAlpha() {
        return this.mColorAlpha;
    }

    public void setColorAlpha(float alpha) {
        this.mColorAlpha = alpha;
        if (!this.mltWave.isEmpty()) {
            updateLinearGradient(getWidth(), getHeight());
        }
    }

    public void start() {
        if (!this.mIsRunning) {
            this.mIsRunning = true;
            this.mLastTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void stop() {
        this.mIsRunning = false;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }
}
