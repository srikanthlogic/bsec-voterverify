package com.scwang.wave;

import android.graphics.Path;
/* loaded from: classes3.dex */
class Wave {
    float offsetX;
    float offsetY;
    Path path;
    private float scaleX;
    private float scaleY;
    float velocity;
    int wave;
    int width;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Wave(int offsetX, int offsetY, int velocity, float scaleX, float scaleY, int w, int h, int wave) {
        this.width = (int) (2.0f * scaleX * ((float) w));
        this.wave = wave;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = (float) offsetX;
        this.offsetY = (float) offsetY;
        this.velocity = (float) velocity;
        this.path = buildWavePath(this.width, h);
    }

    public void updateWavePath(int w, int h, int waveHeight) {
        int i = this.wave;
        if (i <= 0) {
            i = waveHeight / 2;
        }
        this.wave = i;
        this.width = (int) (this.scaleX * 2.0f * ((float) w));
        this.path = buildWavePath(this.width, h);
    }

    private Path buildWavePath(int width, int height) {
        int DP = Util.dp2px(1.0f);
        if (DP < 1) {
            DP = 1;
        }
        int wave = (int) (this.scaleY * ((float) this.wave));
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.0f, (float) (height - wave));
        for (int x = DP; x < width; x += DP) {
            path.lineTo((float) x, ((float) (height - wave)) - (((float) wave) * ((float) Math.sin((((double) x) * 12.566370614359172d) / ((double) width)))));
        }
        path.lineTo((float) width, (float) (height - wave));
        path.lineTo((float) width, 0.0f);
        path.close();
        return path;
    }
}
