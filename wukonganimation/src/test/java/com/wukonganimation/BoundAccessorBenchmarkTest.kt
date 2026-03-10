package com.wukonganimation

import com.wukonganimation.tween.targetvalue.TargetValueReflect
import org.junit.Test
import java.io.File
import kotlin.math.roundToLong

class BoundAccessorBenchmarkTest {

    @Test
    fun benchmarkBoundAccessorVsDispatch() {
        val iterations = 1_000_000
        val rounds = 7

        val boundFieldHolder = FieldHolder()
        val dispatchFieldHolder = FieldHolder()
        val boundMethodHolder = MethodHolder()
        val dispatchMethodHolder = MethodHolder()
        val boundMultiHolder = MultiFieldHolder()
        val dispatchMultiHolder = MultiFieldHolder()

        val boundFieldReflect = TargetValueReflect(boundFieldHolder)
        val dispatchFieldReflect = TargetValueReflect(dispatchFieldHolder)
        val boundMethodReflect = TargetValueReflect(boundMethodHolder)
        val dispatchMethodReflect = TargetValueReflect(dispatchMethodHolder)
        val boundMultiReflect = TargetValueReflect(boundMultiHolder)
        val dispatchMultiReflect = TargetValueReflect(dispatchMultiHolder)

        val boundFieldAccessor = boundFieldReflect.bindProperty("x")
        val boundMethodAccessor = boundMethodReflect.bindProperty("score")
        val boundXAccessor = boundMultiReflect.bindProperty("x")
        val boundYAccessor = boundMultiReflect.bindProperty("y")

        repeat(50_000) { index ->
            val value = index.toDouble()
            boundFieldAccessor.set(value)
            dispatchFieldReflect.setTargetValue("x", value)
            boundMethodAccessor.set(value)
            dispatchMethodReflect.setTargetValue("score", value)
            boundXAccessor.set(value)
            boundYAccessor.set(value + 1)
            dispatchMultiReflect.setTargetValue("x", value)
            dispatchMultiReflect.setTargetValue("y", value + 1)
            blackhole += boundFieldAccessor.get(0.0).toDouble()
            blackhole += dispatchFieldReflect.getTargetValue("x", 0.0).toDouble()
            blackhole += boundMethodAccessor.get(0.0).toDouble()
            blackhole += dispatchMethodReflect.getTargetValue("score", 0.0).toDouble()
            blackhole += boundMultiHolder.x + boundMultiHolder.y
            blackhole += dispatchMultiHolder.x + dispatchMultiHolder.y
        }

        val lines = listOf(
            formatResult(
                "bound field set",
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        boundFieldAccessor.set(index.toDouble())
                        index++
                    }
                    boundFieldHolder.x
                },
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        dispatchFieldReflect.setTargetValue("x", index.toDouble())
                        index++
                    }
                    dispatchFieldHolder.x
                }
            ),
            formatResult(
                "bound field get",
                measure(iterations, rounds) {
                    var sum = 0.0
                    var index = 0
                    while (index < iterations) {
                        sum += boundFieldAccessor.get(0.0).toDouble()
                        index++
                    }
                    sum
                },
                measure(iterations, rounds) {
                    var sum = 0.0
                    var index = 0
                    while (index < iterations) {
                        sum += dispatchFieldReflect.getTargetValue("x", 0.0).toDouble()
                        index++
                    }
                    sum
                }
            ),
            formatResult(
                "bound method set",
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        boundMethodAccessor.set(index.toDouble())
                        index++
                    }
                    boundMethodHolder.getScore().toDouble()
                },
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        dispatchMethodReflect.setTargetValue("score", index.toDouble())
                        index++
                    }
                    dispatchMethodHolder.getScore().toDouble()
                }
            ),
            formatResult(
                "bound method get",
                measure(iterations, rounds) {
                    var sum = 0.0
                    var index = 0
                    while (index < iterations) {
                        sum += boundMethodAccessor.get(0.0).toDouble()
                        index++
                    }
                    sum
                },
                measure(iterations, rounds) {
                    var sum = 0.0
                    var index = 0
                    while (index < iterations) {
                        sum += dispatchMethodReflect.getTargetValue("score", 0.0).toDouble()
                        index++
                    }
                    sum
                }
            ),
            formatResult(
                "bound multi-field set",
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        val value = index.toDouble()
                        boundXAccessor.set(value)
                        boundYAccessor.set(value + 1)
                        index++
                    }
                    boundMultiHolder.x + boundMultiHolder.y
                },
                measure(iterations, rounds) {
                    var index = 0
                    while (index < iterations) {
                        val value = index.toDouble()
                        dispatchMultiReflect.setTargetValue("x", value)
                        dispatchMultiReflect.setTargetValue("y", value + 1)
                        index++
                    }
                    dispatchMultiHolder.x + dispatchMultiHolder.y
                }
            )
        )

        val reportFile = File(System.getProperty("user.dir"), "build/reports/bound-accessor-benchmark.txt")
        reportFile.parentFile?.mkdirs()
        reportFile.writeText(lines.joinToString(separator = System.lineSeparator(), postfix = System.lineSeparator()))
    }

    private fun measure(iterations: Int, rounds: Int, block: () -> Double): Double {
        val samples = DoubleArray(rounds)
        repeat(rounds) { round ->
            val start = System.nanoTime()
            val checksum = block()
            blackhole += checksum
            samples[round] = (System.nanoTime() - start).toDouble() / iterations
        }
        samples.sort()
        return samples[samples.size / 2]
    }

    private fun formatResult(name: String, currentNsPerOp: Double, dispatchNsPerOp: Double): String {
        val speedup = dispatchNsPerOp / currentNsPerOp
        val improvement = ((dispatchNsPerOp - currentNsPerOp) / dispatchNsPerOp) * 100
        return buildString {
            append("[BoundAccessor benchmark] ")
            append(name)
            append(": bound=")
            append(currentNsPerOp.roundToLong())
            append(" ns/op, dispatch=")
            append(dispatchNsPerOp.roundToLong())
            append(" ns/op, speedup=")
            append(String.format("%.2fx", speedup))
            append(", improvement=")
            append(String.format("%.1f%%", improvement))
        }
    }

    class FieldHolder {
        @JvmField
        var x: Double = 0.0
    }

    class MethodHolder {
        private var backingScore: Int = 0

        fun setScore(value: Int) {
            backingScore = value
        }

        fun getScore(): Int = backingScore
    }

    class MultiFieldHolder {
        @JvmField
        var x: Double = 0.0

        @JvmField
        var y: Double = 0.0
    }

    companion object {
        @Volatile
        private var blackhole = 0.0
    }
}

