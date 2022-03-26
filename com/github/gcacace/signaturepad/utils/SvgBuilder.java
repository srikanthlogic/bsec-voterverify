package com.github.gcacace.signaturepad.utils;
/* loaded from: classes.dex */
public class SvgBuilder {
    private final StringBuilder mSvgPathsBuilder = new StringBuilder();
    private SvgPathBuilder mCurrentPathBuilder = null;

    public void clear() {
        this.mSvgPathsBuilder.setLength(0);
        this.mCurrentPathBuilder = null;
    }

    public String build(int width, int height) {
        if (isPathStarted()) {
            appendCurrentPath();
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.2\" baseProfile=\"tiny\" height=\"" + height + "\" width=\"" + width + "\" viewBox=\"0 0 " + width + " " + height + "\"><g stroke-linejoin=\"round\" stroke-linecap=\"round\" fill=\"none\" stroke=\"black\">" + ((CharSequence) this.mSvgPathsBuilder) + "</g></svg>";
    }

    public SvgBuilder append(Bezier curve, float strokeWidth) {
        Integer roundedStrokeWidth = Integer.valueOf(Math.round(strokeWidth));
        SvgPoint curveStartSvgPoint = new SvgPoint(curve.startPoint);
        SvgPoint curveControlSvgPoint1 = new SvgPoint(curve.control1);
        SvgPoint curveControlSvgPoint2 = new SvgPoint(curve.control2);
        SvgPoint curveEndSvgPoint = new SvgPoint(curve.endPoint);
        if (!isPathStarted()) {
            startNewPath(roundedStrokeWidth, curveStartSvgPoint);
        }
        if (!curveStartSvgPoint.equals(this.mCurrentPathBuilder.getLastPoint()) || !roundedStrokeWidth.equals(this.mCurrentPathBuilder.getStrokeWidth())) {
            appendCurrentPath();
            startNewPath(roundedStrokeWidth, curveStartSvgPoint);
        }
        this.mCurrentPathBuilder.append(curveControlSvgPoint1, curveControlSvgPoint2, curveEndSvgPoint);
        return this;
    }

    private void startNewPath(Integer roundedStrokeWidth, SvgPoint curveStartSvgPoint) {
        this.mCurrentPathBuilder = new SvgPathBuilder(curveStartSvgPoint, roundedStrokeWidth);
    }

    private void appendCurrentPath() {
        this.mSvgPathsBuilder.append(this.mCurrentPathBuilder);
    }

    private boolean isPathStarted() {
        return this.mCurrentPathBuilder != null;
    }
}
