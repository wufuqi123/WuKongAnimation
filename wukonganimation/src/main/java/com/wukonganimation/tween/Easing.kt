package com.wukonganimation.tween

import kotlin.math.*

object Easing {
    fun linear(): (t: Double) -> Double {
        return {
            it
        }
    }

    fun inQuad(): (t: Double) -> Double {
        return {
            it * it
        }
    }

    fun outQuad(): (t: Double) -> Double {
        return {
            it * (2 - it)
        }
    }

    fun inOutQuad(): (t: Double) -> Double {
        return {
            var t = it
            t *= 2;
            if (t < 1) 0.5 * t * t else -0.5 * (--t * (t - 2) - 1)
        }
    }


    fun inCubic(): (t: Double) -> Double {
        return {
            it * it * it
        }
    }


    fun outCubic(): (t: Double) -> Double {
        return {
            (it - 1) * it * it + 1
        }
    }

    fun inOutCubic(): (t: Double) -> Double {
        return {
            var t = it
            t *= 2;
            if (t < 1) {
                0.5 * t * t * t
            } else {
                t -= 2
                0.5 * (t * t * t + 2)
            }
        }
    }

    fun inQuart(): (t: Double) -> Double {
        return {
            it * it * it * it
        }
    }

    fun outQuart(): (t: Double) -> Double {
        return {
            1 - (it - 1) * it * it * it
        }
    }

    fun inOutQuart(): (t: Double) -> Double {
        return {
            var t = it
            t *= 2
            if (t < 1) {
                0.5 * t * t * t * t
            } else {
                t -= 2;
                -0.5 * (t * t * t * t - 2)
            }

        }
    }

    fun inQuint(): (t: Double) -> Double {
        return {
            it * it * it * it * it
        }
    }

    fun outQuint(): (t: Double) -> Double {
        return {
            (it - 1) * it * it * it * it + 1
        }
    }

    fun inOutQuint(): (t: Double) -> Double {
        return {
            var t = it
            t *= 2
            if (t < 1) {
                0.5 * t * t * t * t * t
            } else {
                t -= 2
                0.5 * (t * t * t * t * t + 2)
            }
        }
    }

    fun inSine(): (t: Double) -> Double {
        return {
            1 - cos((it * Math.PI) / 2)
        }
    }

    fun outSine(): (t: Double) -> Double {
        return {
            sin((it * Math.PI) / 2)
        }
    }


    fun inOutSine(): (t: Double) -> Double {
        return {
            0.5 * (1 - cos(Math.PI * it))
        }
    }

    fun inExpo(): (t: Double) -> Double {
        return {
            if (it == 0.0) 0.0 else 1024.0.pow(it - 1)
        }
    }

    fun outExpo(): (t: Double) -> Double {
        return {
            if (it == 1.0) 1.0 else 1 - 2.0.pow(-10 * it)
        }
    }


    fun inOutExpo(): (t: Double) -> Double {
        return {
            if (it == 0.0) {
                0.0
            } else if (it == 1.0) {
                1.0
            } else {
                var t = it
                t *= 2
                if (t < 1) 0.5 * 1024.0.pow(t - 1);
                else 0.5 * ((-2.0).pow(-10 * (t - 1)) + 2)
            }
        }
    }


    fun inCirc(): (t: Double) -> Double {
        return {
            1 - sqrt(1 - it * it)
        }
    }

    fun outCirc(): (t: Double) -> Double {
        return {
            sqrt(1 - (it - 1) * it)
        }
    }


    fun inOutCirc(): (t: Double) -> Double {
        return {
            var t = it
            t *= 2
            if (t < 1) -0.5 * (sqrt(1 - t * t) - 1);
            else 0.5 * (sqrt(1 - (t - 2) * (t - 2)) + 1);
        }
    }


    fun inElastic(at: Double = 0.1, p: Double = 0.4): (t: Double) -> Double {
        return {
            var a = at
            val t = it
            val s: Double
            when (t) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> {
                    if (a < 1) {
                        a = 1.0
                        s = p / 4
                    } else s = (p * asin(1 / a)) / (2 * Math.PI)
                    -(a * 2.0.pow(10 * (t - 1)) * sin(((t - 1 - s) * (2 * Math.PI)) / p))
                }
            }
        }
    }


    fun outElastic(at: Double = 0.1, p: Double = 0.4): (t: Double) -> Double {
        return {
            var a = at
            val t = it
            val s: Double
            when (t) {
                0.0 -> 0.0
                1.0 -> 1.0
                else -> {
                    if (a < 1) {
                        a = 1.0
                        s = p / 4
                    } else s = (p * asin(1 / a)) / (2 * Math.PI)
                    (a * 2.0.pow(-10 * t) * sin(((t - s) * (2 * Math.PI)) / p) + 1)
                }
            }
        }
    }

    fun inOutElastic(at: Double = 0.1, p: Double = 0.4): (t: Double) -> Double {
        return {
            var a = at
            var t = it
            val s: Double
            if (t == 0.0)
                0.0
            else if (t == 1.0)
                1.0
            else {
                if (a < 1) {
                    a = 1.0
                    s = p / 4
                } else s = (p * asin(1 / a)) / (2 * Math.PI)
                t *= 2.0

                if (t < 1)
                    (-0.5 * (a * 2.0.pow(10 * (t - 1)) * sin(((t - 1 - s) * (2 * Math.PI)) / p)))
                else
                    (a * 2.0.pow(-10 * (t - 1)) * sin(((t - 1 - s) * (2 * Math.PI)) / p) * 0.5 + 1)
            }
        }
    }


    fun inBack(v: Double? = null): (t: Double) -> Double {
        return {
            val s = v ?: 1.70158
            it * it * ((s + 1) * it - s)
        }
    }

    fun outBack(v: Double? = null): (t: Double) -> Double {
        return {
            val s = v ?: 1.70158
            (it - 1) * it * ((s + 1) * it + s) + 1
        }
    }

    fun inOutBack(v: Double? = null): (t: Double) -> Double {
        return {
            var t = it
            val s = (v ?: 1.70158) * 1.525
            t *= 2
            if (t < 1) 0.5 * (t * t * ((s + 1) * t - s))
            else 0.5 * ((t - 2) * (t - 2) * ((s + 1) * (t - 2) + s) + 2)
        }
    }

    fun inBounce(): (t: Double) -> Double {
        return {
            1 - outBounce()(1 - it)
        }
    }

    fun outBounce(): (t: Double) -> Double {
        return {
            var t = it
            if (t < 1 / 2.75) {
                7.5625 * t * t;
            } else if (t < 2 / 2.75) {
                t -= 1.5 / 2.75;
                7.5625 * t * t + 0.75;
            } else if (t < 2.5 / 2.75) {
                t -= 2.25 / 2.75;
                7.5625 * t * t + 0.9375;
            } else {
                t -= 2.625 / 2.75;
                7.5625 * t * t + 0.984375;
            }
        }
    }

    fun inOutBounce(): (t: Double) -> Double {
        return {
            if (it < 0.5) inBounce()(it * 2) * 0.5
            else outBounce()(it * 2 - 1) * 0.5 + 0.5
        }
    }
}