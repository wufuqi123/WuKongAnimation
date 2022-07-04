package com.wukonganimation.tween;

import kotlin.jvm.functions.Function1;

public class Easing {

    public static Function1<Double, Double> linear() {
        return t -> t;
    }

    public static Function1<Double, Double> inQuad() {
        return t -> t * t;
    }

    public static Function1<Double, Double> outQuad() {
        return t -> t * (2 - t);
    }

    public static Function1<Double, Double> inOutQuad() {
        return t -> {
            t *= 2;
            if (t < 1) return 0.5 * t * t;
            return -0.5 * (--t * (t - 2) - 1);
        };
    }


    public static Function1<Double, Double> inCubic() {
        return t -> t * t * t;
    }


    public static Function1<Double, Double> outCubic() {
        return t -> --t * t * t + 1;
    }


    public static Function1<Double, Double> inOutCubic() {
        return t -> {
            t *= 2;
            if (t < 1) return 0.5 * t * t * t;
            t -= 2;
            return 0.5 * (t * t * t + 2);
        };
    }

    public static Function1<Double, Double> inQuart() {
        return t -> t * t * t * t;
    }

    public static Function1<Double, Double> outQuart() {
        return t -> 1 - --t * t * t * t;
    }

    public static Function1<Double, Double> inOutQuart() {
        return t -> {
            t *= 2;
            if (t < 1) return 0.5 * t * t * t * t;
            t -= 2;
            return -0.5 * (t * t * t * t - 2);
        };
    }

    public static Function1<Double, Double> inQuint() {
        return t -> t * t * t * t * t;
    }

    public static Function1<Double, Double> outQuint() {
        return t -> --t * t * t * t * t + 1;
    }


    public static Function1<Double, Double> inOutQuint() {
        return t -> {
            t *= 2;
            if (t < 1) return 0.5 * t * t * t * t * t;
            t -= 2;
            return 0.5 * (t * t * t * t * t + 2);
        };
    }


    public static Function1<Double, Double> inSine() {
        return t -> 1 - Math.cos((t * Math.PI) / 2);
    }


    public static Function1<Double, Double> outSine() {
        return t -> Math.sin((t * Math.PI) / 2);
    }


    public static Function1<Double, Double> inOutSine() {
        return t -> 0.5 * (1 - Math.cos(Math.PI * t));
    }

    public static Function1<Double, Double> inExpo() {
        return t -> t == 0 ? 0 : Math.pow(1024, t - 1);
    }

    public static Function1<Double, Double> outExpo() {
        return t -> t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
    }

    public static Function1<Double, Double> inOutExpo() {
        return t -> {
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            t *= 2;
            if (t < 1) return 0.5 * Math.pow(1024, t - 1);
            return 0.5 * (-Math.pow(2, -10 * (t - 1)) + 2);
        };
    }

    public static Function1<Double, Double> inCirc() {
        return t -> 1 - Math.sqrt(1 - t * t);
    }

    public static Function1<Double, Double> outCirc() {
        return t -> Math.sqrt(1 - --t * t);
    }

    public static Function1<Double, Double> inOutCirc() {
        return t -> {
            t *= 2;
            if (t < 1) return -0.5 * (Math.sqrt(1 - t * t) - 1);
            return 0.5 * (Math.sqrt(1 - (t - 2) * (t - 2)) + 1);
        };
    }

    public static Function1<Double, Double> inElastic(Double al, Double pl) {
        return t -> {
            Double s;
            Double a = al;
            Double p = pl;
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            if (a != null || a < 1) {
                a = 1.0;
                s = p / 4;
            } else s = (p * Math.asin(1 / a)) / (2 * Math.PI);
            return -(
                    a *
                            Math.pow(2, 10 * (t - 1)) *
                            Math.sin(((t - 1 - s) * (2 * Math.PI)) / p)
            );
        };
    }

    public static Function1<Double, Double> inElastic() {
        return inElastic(0.1, 0.4);
    }


    public static Function1<Double, Double> outElastic(Double al, Double pl) {
        return t -> {
            Double s;
            Double a = al;
            Double p = pl;
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            if (a != null || a < 1) {
                a = 1.0;
                s = p / 4;
            } else s = (p * Math.asin(1 / a)) / (2 * Math.PI);
            return (
                    a * Math.pow(2, -10 * t) * Math.sin(((t - s) * (2 * Math.PI)) / p) + 1
            );
        };
    }

    public static Function1<Double, Double> outElastic() {
        return outElastic(0.1, 0.4);
    }


    public static Function1<Double, Double> inOutElastic(Double al, Double pl) {
        return t -> {
            Double s;
            Double a = al;
            Double p = pl;
            if (t == 0) return 0.0;
            if (t == 1) return 1.0;
            if (a != null || a < 1) {
                a = 1.0;
                s = p / 4;
            } else s = (p * Math.asin(1 / a)) / (2 * Math.PI);
            t *= 2;
            if (t < 1)
                return (
                        -0.5 *
                                (a *
                                        Math.pow(2, 10 * (t - 1)) *
                                        Math.sin(((t - 1 - s) * (2 * Math.PI)) / p))
                );
            return (
                    a *
                            Math.pow(2, -10 * (t - 1)) *
                            Math.sin(((t - 1 - s) * (2 * Math.PI)) / p) *
                            0.5 +
                            1
            );
        };
    }

    public static Function1<Double, Double> inOutElastic() {
        return inOutElastic(0.1, 0.4);
    }


    public static Function1<Double, Double> inBack(Double v) {
        return t -> {
            Double s = v == null ? 1.70158 : v;
            return t * t * ((s + 1) * t - s);
        };
    }

    public static Function1<Double, Double> inBack() {
        return inBack(null);
    }

    public static Function1<Double, Double> outBack(Double v) {
        return t -> {
            Double s = v == null ? 1.70158 : v;
            return --t * t * ((s + 1) * t + s) + 1;
        };
    }

    public static Function1<Double, Double> outBack() {
        return outBack(null);
    }


    public static Function1<Double, Double> inOutBack(Double v) {
        return t -> {
            Double s = (v == null ? 1.70158 : v) * 1.525;
            t *= 2;
            if (t < 1) return 0.5 * (t * t * ((s + 1) * t - s));
            return 0.5 * ((t - 2) * (t - 2) * ((s + 1) * (t - 2) + s) + 2);
        };
    }

    public static Function1<Double, Double> inOutBack() {
        return inOutBack(null);
    }


    public static Function1<Double, Double> inBounce() {
        return t -> 1 - outBounce().invoke(1 - t);
    }

    public static Function1<Double, Double> outBounce() {
        return t -> {
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
    }

    public static Function1<Double, Double> inOutBounce() {
        return t -> {
            if (t < 0.5) return inBounce().invoke(t * 2) * 0.5;
            return outBounce().invoke(t * 2 - 1) * 0.5 + 0.5;
        };
    }

}
