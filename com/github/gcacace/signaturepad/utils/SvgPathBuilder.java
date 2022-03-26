package com.github.gcacace.signaturepad.utils;
/* loaded from: classes.dex */
public class SvgPathBuilder {
    private SvgPoint mLastPoint;
    private final SvgPoint mStartPoint;
    private final StringBuilder mStringBuilder = new StringBuilder();
    private final Integer mStrokeWidth;
    public static final Character SVG_RELATIVE_CUBIC_BEZIER_CURVE = 'c';
    public static final Character SVG_MOVE = 'M';

    public SvgPathBuilder(SvgPoint startPoint, Integer strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        this.mStartPoint = startPoint;
        this.mLastPoint = startPoint;
        this.mStringBuilder.append(SVG_RELATIVE_CUBIC_BEZIER_CURVE);
    }

    public final Integer getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public final SvgPoint getLastPoint() {
        return this.mLastPoint;
    }

    public SvgPathBuilder append(SvgPoint controlPoint1, SvgPoint controlPoint2, SvgPoint endPoint) {
        this.mStringBuilder.append(makeRelativeCubicBezierCurve(controlPoint1, controlPoint2, endPoint));
        this.mLastPoint = endPoint;
        return this;
    }

    public String toString() {
        return "<path stroke-width=\"" + this.mStrokeWidth + "\" d=\"" + SVG_MOVE + this.mStartPoint + ((CharSequence) this.mStringBuilder) + "\"/>";
    }

    private String makeRelativeCubicBezierCurve(SvgPoint controlPoint1, SvgPoint controlPoint2, SvgPoint endPoint) {
        String svg = controlPoint1.toRelativeCoordinates(this.mLastPoint) + " " + controlPoint2.toRelativeCoordinates(this.mLastPoint) + " " + endPoint.toRelativeCoordinates(this.mLastPoint) + " ";
        if ("c0 0 0 0 0 0".equals(svg)) {
            return "";
        }
        return svg;
    }
}
