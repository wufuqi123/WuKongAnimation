package com.wukonganimation.tween;

import kotlin.jvm.functions.Function1;

public class Easing {

    @FunctionalInterface
    public interface DoubleEasing extends Function1<Double, Double> {
        double apply(double t);

        @Override
        default Double invoke(Double t) {
            return apply(t);
        }
    }

    @FunctionalInterface
    public interface FloatEasing {
        float applyFloat(float t);
    }

    private abstract static class DualEasing implements DoubleEasing, FloatEasing {
    }

    private static final double HALF_PI = Math.PI / 2;
    private static final double TWO_PI = Math.PI * 2;
    private static final double DEFAULT_BACK = 1.70158;
    private static final double DEFAULT_BACK_IN_OUT = DEFAULT_BACK * 1.525;
    private static final double DEFAULT_ELASTIC_AMPLITUDE = 0.1;
    private static final double DEFAULT_ELASTIC_PERIOD = 0.4;

    private static final DualEasing LINEAR = new DualEasing() {
        @Override
        public double apply(double t) {
            return t;
        }

        @Override
        public float applyFloat(float t) {
            return t;
        }
    };
    private static final DualEasing IN_QUAD = new DualEasing() {
        @Override
        public double apply(double t) {
            return t * t;
        }

        @Override
        public float applyFloat(float t) {
            return t * t;
        }
    };
    private static final DualEasing OUT_QUAD = new DualEasing() {
        @Override
        public double apply(double t) {
            return t * (2 - t);
        }

        @Override
        public float applyFloat(float t) {
            return t * (2.0f - t);
        }
    };
    private static final DualEasing IN_OUT_QUAD = new DualEasing() {
        @Override
        public double apply(double t) {
            double scaled = t * 2.0;
            if (scaled < 1.0) return 0.5 * scaled * scaled;
            scaled -= 1.0;
            return -0.5 * (scaled * (scaled - 2.0) - 1.0);
        }

        @Override
        public float applyFloat(float t) {
            float scaled = t * 2.0f;
            if (scaled < 1.0f) return 0.5f * scaled * scaled;
            scaled -= 1.0f;
            return -0.5f * (scaled * (scaled - 2.0f) - 1.0f);
        }
    };
    private static final DualEasing IN_CUBIC = new DualEasing() {
        @Override
        public double apply(double t) {
            return t * t * t;
        }

        @Override
        public float applyFloat(float t) {
            return t * t * t;
        }
    };
    private static final DualEasing OUT_CUBIC = new DualEasing() {
        @Override
        public double apply(double t) {
            t -= 1.0;
            return t * t * t + 1.0;
        }

        @Override
        public float applyFloat(float t) {
            t -= 1.0f;
            return t * t * t + 1.0f;
        }
    };
    private static final DualEasing IN_OUT_CUBIC = new DualEasing() {
        @Override
        public double apply(double t) {
            double scaled = t * 2.0;
            if (scaled < 1.0) return 0.5 * scaled * scaled * scaled;
            scaled -= 2.0;
            return 0.5 * (scaled * scaled * scaled + 2.0);
        }

        @Override
        public float applyFloat(float t) {
            float scaled = t * 2.0f;
            if (scaled < 1.0f) return 0.5f * scaled * scaled * scaled;
            scaled -= 2.0f;
            return 0.5f * (scaled * scaled * scaled + 2.0f);
        }
    };
    private static final DualEasing IN_QUART = new DualEasing() {
        @Override
        public double apply(double t) {
            return t * t * t * t;
        }

        @Override
        public float applyFloat(float t) {
            return t * t * t * t;
        }
    };
    private static final DualEasing OUT_QUART = new DualEasing() {
        @Override
        public double apply(double t) {
            t -= 1.0;
            return 1.0 - t * t * t * t;
        }

        @Override
        public float applyFloat(float t) {
            t -= 1.0f;
            return 1.0f - t * t * t * t;
        }
    };
    private static final DualEasing IN_OUT_QUART = new DualEasing() {
        @Override
        public double apply(double t) {
            double scaled = t * 2.0;
            if (scaled < 1.0) return 0.5 * scaled * scaled * scaled * scaled;
            scaled -= 2.0;
            return -0.5 * (scaled * scaled * scaled * scaled - 2.0);
        }

        @Override
        public float applyFloat(float t) {
            float scaled = t * 2.0f;
            if (scaled < 1.0f) return 0.5f * scaled * scaled * scaled * scaled;
            scaled -= 2.0f;
            return -0.5f * (scaled * scaled * scaled * scaled - 2.0f);
        }
    };
    private static final DualEasing IN_QUINT = new DualEasing() {
        @Override
        public double apply(double t) {
            return t * t * t * t * t;
        }

        @Override
        public float applyFloat(float t) {
            return t * t * t * t * t;
        }
    };
    private static final DualEasing OUT_QUINT = new DualEasing() {
        @Override
        public double apply(double t) {
            t -= 1.0;
            return t * t * t * t * t + 1.0;
        }

        @Override
        public float applyFloat(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    private static final DualEasing IN_OUT_QUINT = new DualEasing() {
        @Override
        public double apply(double t) {
            double scaled = t * 2.0;
            if (scaled < 1.0) return 0.5 * scaled * scaled * scaled * scaled * scaled;
            scaled -= 2.0;
            return 0.5 * (scaled * scaled * scaled * scaled * scaled + 2.0);
        }

        @Override
        public float applyFloat(float t) {
            float scaled = t * 2.0f;
            if (scaled < 1.0f) return 0.5f * scaled * scaled * scaled * scaled * scaled;
            scaled -= 2.0f;
            return 0.5f * (scaled * scaled * scaled * scaled * scaled + 2.0f);
        }
    };
    private static final DoubleEasing IN_SINE = t -> 1 - Math.cos(t * HALF_PI);
    private static final DoubleEasing OUT_SINE = t -> Math.sin(t * HALF_PI);
    private static final DoubleEasing IN_OUT_SINE = t -> 0.5 * (1 - Math.cos(Math.PI * t));
    private static final DoubleEasing IN_EXPO = t -> t == 0 ? 0 : Math.pow(1024, t - 1);
    private static final DoubleEasing OUT_EXPO = t -> t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
    private static final DoubleEasing IN_OUT_EXPO = t -> {
        if (t == 0) return 0.0;
        if (t == 1) return 1.0;
        t *= 2;
        if (t < 1) return 0.5 * Math.pow(1024, t - 1);
        return 0.5 * (-Math.pow(2, -10 * (t - 1)) + 2);
    };
    private static final DoubleEasing IN_CIRC = t -> 1 - Math.sqrt(1 - t * t);
    private static final DoubleEasing OUT_CIRC = t -> Math.sqrt(1 - --t * t);
    private static final DoubleEasing IN_OUT_CIRC = t -> {
        t *= 2;
        if (t < 1) return -0.5 * (Math.sqrt(1 - t * t) - 1);
        return 0.5 * (Math.sqrt(1 - (t - 2) * (t - 2)) + 1);
    };
    private static final DoubleEasing DEFAULT_IN_ELASTIC = createInElastic(DEFAULT_ELASTIC_AMPLITUDE, DEFAULT_ELASTIC_PERIOD);
    private static final DoubleEasing DEFAULT_OUT_ELASTIC = createOutElastic(DEFAULT_ELASTIC_AMPLITUDE, DEFAULT_ELASTIC_PERIOD);
    private static final DoubleEasing DEFAULT_IN_OUT_ELASTIC = createInOutElastic(DEFAULT_ELASTIC_AMPLITUDE, DEFAULT_ELASTIC_PERIOD);
    private static final DoubleEasing DEFAULT_IN_BACK = createInBack(DEFAULT_BACK);
    private static final DoubleEasing DEFAULT_OUT_BACK = createOutBack(DEFAULT_BACK);
    private static final DoubleEasing DEFAULT_IN_OUT_BACK = createInOutBack(DEFAULT_BACK_IN_OUT);
    private static final DoubleEasing OUT_BOUNCE = t -> {
        if (t < 1 / 2.75) {
            return 7.5625 * t * t;
        } else if (t < 2 / 2.75) {
            t = t - 1.5 / 2.75;
            return 7.5625 * t * t + 0.75;
        } else if (t < 2.5 / 2.75) {
            t = t - 2.25 / 2.75;
            return 7.5625 * t * t + 0.9375;
        } else {
            t -= 2.625 / 2.75;
            return 7.5625 * t * t + 0.984375;
        }
    };
    private static final DoubleEasing IN_BOUNCE = t -> 1 - OUT_BOUNCE.apply(1 - t);
    private static final DoubleEasing IN_OUT_BOUNCE = t -> {
        if (t < 0.5) return IN_BOUNCE.apply(t * 2) * 0.5;
        return OUT_BOUNCE.apply(t * 2 - 1) * 0.5 + 0.5;
    };

    public static Function1<Double, Double> linear() {
        return LINEAR;
    }

    public static Function1<Double, Double> inQuad() {
        return IN_QUAD;
    }

    public static Function1<Double, Double> outQuad() {
        return OUT_QUAD;
    }

    public static Function1<Double, Double> inOutQuad() {
        return IN_OUT_QUAD;
    }

    public static Function1<Double, Double> inCubic() {
        return IN_CUBIC;
    }

    public static Function1<Double, Double> outCubic() {
        return OUT_CUBIC;
    }

    public static Function1<Double, Double> inOutCubic() {
        return IN_OUT_CUBIC;
    }

    public static Function1<Double, Double> inQuart() {
        return IN_QUART;
    }

    public static Function1<Double, Double> outQuart() {
        return OUT_QUART;
    }

    public static Function1<Double, Double> inOutQuart() {
        return IN_OUT_QUART;
    }

    public static Function1<Double, Double> inQuint() {
        return IN_QUINT;
    }

    public static Function1<Double, Double> outQuint() {
        return OUT_QUINT;
    }

    public static Function1<Double, Double> inOutQuint() {
        return IN_OUT_QUINT;
    }

    public static Function1<Double, Double> inSine() {
        return IN_SINE;
    }

    public static Function1<Double, Double> outSine() {
        return OUT_SINE;
    }

    public static Function1<Double, Double> inOutSine() {
        return IN_OUT_SINE;
    }

    public static Function1<Double, Double> inExpo() {
        return IN_EXPO;
    }

    public static Function1<Double, Double> outExpo() {
        return OUT_EXPO;
    }

    public static Function1<Double, Double> inOutExpo() {
        return IN_OUT_EXPO;
    }

    public static Function1<Double, Double> inCirc() {
        return IN_CIRC;
    }

    public static Function1<Double, Double> outCirc() {
        return OUT_CIRC;
    }

    public static Function1<Double, Double> inOutCirc() {
        return IN_OUT_CIRC;
    }

    public static Function1<Double, Double> inElastic(Double al, Double pl) {
        return createInElastic(al, pl);
    }

    public static Function1<Double, Double> inElastic() {
        return DEFAULT_IN_ELASTIC;
    }

    public static Function1<Double, Double> outElastic(Double al, Double pl) {
        return createOutElastic(al, pl);
    }

    public static Function1<Double, Double> outElastic() {
        return DEFAULT_OUT_ELASTIC;
    }

    public static Function1<Double, Double> inOutElastic(Double al, Double pl) {
        return createInOutElastic(al, pl);
    }

    public static Function1<Double, Double> inOutElastic() {
        return DEFAULT_IN_OUT_ELASTIC;
    }

    public static Function1<Double, Double> inBack(Double v) {
        return createInBack(v == null ? DEFAULT_BACK : v);
    }

    public static Function1<Double, Double> inBack() {
        return DEFAULT_IN_BACK;
    }

    public static Function1<Double, Double> outBack(Double v) {
        return createOutBack(v == null ? DEFAULT_BACK : v);
    }

    public static Function1<Double, Double> outBack() {
        return DEFAULT_OUT_BACK;
    }

    public static Function1<Double, Double> inOutBack(Double v) {
        double s = (v == null ? DEFAULT_BACK : v) * 1.525;
        return createInOutBack(s);
    }

    public static Function1<Double, Double> inOutBack() {
        return DEFAULT_IN_OUT_BACK;
    }

    public static Function1<Double, Double> inBounce() {
        return IN_BOUNCE;
    }

    public static Function1<Double, Double> outBounce() {
        return OUT_BOUNCE;
    }

    public static Function1<Double, Double> inOutBounce() {
        return IN_OUT_BOUNCE;
    }

    private static DoubleEasing createInElastic(Double al, Double pl) {
        final double p = pl == null ? DEFAULT_ELASTIC_PERIOD : pl;
        final double a;
        final double s;
        if (al == null || al < 1) {
            a = 1.0;
            s = p / 4;
        } else {
            a = al;
            s = (p * Math.asin(1 / a)) / TWO_PI;
        }
        return t -> {
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            return -(a * Math.pow(2, 10 * (t - 1)) * Math.sin(((t - 1 - s) * TWO_PI) / p));
        };
    }

    private static DoubleEasing createOutElastic(Double al, Double pl) {
        final double p = pl == null ? DEFAULT_ELASTIC_PERIOD : pl;
        final double a;
        final double s;
        if (al == null || al < 1) {
            a = 1.0;
            s = p / 4;
        } else {
            a = al;
            s = (p * Math.asin(1 / a)) / TWO_PI;
        }
        return t -> {
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            return a * Math.pow(2, -10 * t) * Math.sin(((t - s) * TWO_PI) / p) + 1;
        };
    }

    private static DoubleEasing createInOutElastic(Double al, Double pl) {
        final double p = pl == null ? DEFAULT_ELASTIC_PERIOD : pl;
        final double a;
        final double s;
        if (al == null || al < 1) {
            a = 1.0;
            s = p / 4;
        } else {
            a = al;
            s = (p * Math.asin(1 / a)) / TWO_PI;
        }
        return t -> {
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            t *= 2;
            if (t < 1) {
                return -0.5 * (a * Math.pow(2, 10 * (t - 1)) * Math.sin(((t - 1 - s) * TWO_PI) / p));
            }
            return a * Math.pow(2, -10 * (t - 1)) * Math.sin(((t - 1 - s) * TWO_PI) / p) * 0.5 + 1;
        };
    }

    private static DoubleEasing createInBack(double s) {
        return t -> t * t * ((s + 1) * t - s);
    }

    private static DoubleEasing createOutBack(double s) {
        return t -> --t * t * ((s + 1) * t + s) + 1;
    }

    private static DoubleEasing createInOutBack(double s) {
        return t -> {
            t *= 2;
            if (t < 1) return 0.5 * (t * t * ((s + 1) * t - s));
            return 0.5 * ((t - 2) * (t - 2) * ((s + 1) * (t - 2) + s) + 2);
        };
    }

}
