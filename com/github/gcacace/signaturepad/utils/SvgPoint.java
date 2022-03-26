package com.github.gcacace.signaturepad.utils;
/* loaded from: classes.dex */
class SvgPoint {
    final Integer x;
    final Integer y;

    public SvgPoint(TimedPoint point) {
        this.x = Integer.valueOf(Math.round(point.x));
        this.y = Integer.valueOf(Math.round(point.y));
    }

    public SvgPoint(int x, int y) {
        this.x = Integer.valueOf(x);
        this.y = Integer.valueOf(y);
    }

    public String toAbsoluteCoordinates() {
        return this.x + "," + this.y;
    }

    public String toRelativeCoordinates(SvgPoint referencePoint) {
        return new SvgPoint(this.x.intValue() - referencePoint.x.intValue(), this.y.intValue() - referencePoint.y.intValue()).toString();
    }

    public String toString() {
        return toAbsoluteCoordinates();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SvgPoint svgPoint = (SvgPoint) o;
        if (!this.x.equals(svgPoint.x)) {
            return false;
        }
        return this.y.equals(svgPoint.y);
    }

    public int hashCode() {
        return (this.x.hashCode() * 31) + this.y.hashCode();
    }
}
