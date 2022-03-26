package com.github.gcacace.signaturepad.utils;
/* loaded from: classes.dex */
public class Bezier {
    public TimedPoint control1;
    public TimedPoint control2;
    public TimedPoint endPoint;
    public TimedPoint startPoint;

    public Bezier set(TimedPoint startPoint, TimedPoint control1, TimedPoint control2, TimedPoint endPoint) {
        this.startPoint = startPoint;
        this.control1 = control1;
        this.control2 = control2;
        this.endPoint = endPoint;
        return this;
    }

    public float length() {
        int steps;
        Bezier bezier = this;
        int steps2 = 10;
        int i = 0;
        float length = 0.0f;
        double px = 0.0d;
        double py = 0.0d;
        while (i <= steps2) {
            float t = ((float) i) / ((float) steps2);
            double cx = point(t, bezier.startPoint.x, bezier.control1.x, bezier.control2.x, bezier.endPoint.x);
            double cy = point(t, bezier.startPoint.y, bezier.control1.y, bezier.control2.y, bezier.endPoint.y);
            if (i > 0) {
                double xDiff = cx - px;
                double yDiff = cy - py;
                steps = steps2;
                length = (float) (((double) length) + Math.sqrt((xDiff * xDiff) + (yDiff * yDiff)));
            } else {
                steps = steps2;
            }
            px = cx;
            py = cy;
            i++;
            bezier = this;
            steps2 = steps;
        }
        return length;
    }

    public double point(float t, float start, float c1, float c2, float end) {
        return (((double) start) * (1.0d - ((double) t)) * (1.0d - ((double) t)) * (1.0d - ((double) t))) + (((double) c1) * 3.0d * (1.0d - ((double) t)) * (1.0d - ((double) t)) * ((double) t)) + (((double) c2) * 3.0d * (1.0d - ((double) t)) * ((double) t) * ((double) t)) + ((double) (end * t * t * t));
    }
}
