package androidx.constraintlayout.motion.utils;
/* loaded from: classes.dex */
public abstract class CurveFit {
    public static final int CONSTANT;
    public static final int LINEAR;
    public static final int SPLINE;

    public abstract double getPos(double d, int i);

    public abstract void getPos(double d, double[] dArr);

    public abstract void getPos(double d, float[] fArr);

    public abstract double getSlope(double d, int i);

    public abstract void getSlope(double d, double[] dArr);

    public abstract double[] getTimePoints();

    public static CurveFit get(int type, double[] time, double[][] y) {
        if (time.length == 1) {
            type = 2;
        }
        if (type == 0) {
            return new MonotonicCurveFit(time, y);
        }
        if (type != 2) {
            return new LinearCurveFit(time, y);
        }
        return new Constant(time[0], y[0]);
    }

    public static CurveFit getArc(int[] arcModes, double[] time, double[][] y) {
        return new ArcCurveFit(arcModes, time, y);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Constant extends CurveFit {
        double mTime;
        double[] mValue;

        Constant(double time, double[] value) {
            this.mTime = time;
            this.mValue = value;
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public void getPos(double t, double[] v) {
            double[] dArr = this.mValue;
            System.arraycopy(dArr, 0, v, 0, dArr.length);
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public void getPos(double t, float[] v) {
            int i = 0;
            while (true) {
                double[] dArr = this.mValue;
                if (i < dArr.length) {
                    v[i] = (float) dArr[i];
                    i++;
                } else {
                    return;
                }
            }
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public double getPos(double t, int j) {
            return this.mValue[j];
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public void getSlope(double t, double[] v) {
            for (int i = 0; i < this.mValue.length; i++) {
                v[i] = 0.0d;
            }
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public double getSlope(double t, int j) {
            return 0.0d;
        }

        @Override // androidx.constraintlayout.motion.utils.CurveFit
        public double[] getTimePoints() {
            return new double[]{this.mTime};
        }
    }
}
