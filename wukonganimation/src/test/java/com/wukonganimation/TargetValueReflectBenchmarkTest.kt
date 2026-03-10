package com.wukonganimation

import com.wukonganimation.tween.targetvalue.TargetValueReflect
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.math.roundToLong

class TargetValueReflectBenchmarkTest {

    @Test
    fun benchmarkCurrentVsLegacy() {
        val iterations = 1_000_000
        val rounds = 7

        val currentFieldHolder = FieldHolder()
        val legacyFieldHolder = FieldHolder()
        val currentMethodHolder = MethodHolder()
        val legacyMethodHolder = MethodHolder()

        val currentField = TargetValueReflect(currentFieldHolder)
        val legacyField = LegacyTargetValueReflect(legacyFieldHolder)
        val currentMethod = TargetValueReflect(currentMethodHolder)
        val legacyMethod = LegacyTargetValueReflect(legacyMethodHolder)

        repeat(50_000) { index ->
            val value = index.toDouble()
            currentField.setTargetValue("x", value)
            legacyField.setTargetValue("x", value)
            currentMethod.setTargetValue("score", value)
            legacyMethod.setTargetValue("score", value)
            blackhole += currentFieldHolder.x
            blackhole += legacyFieldHolder.x
            blackhole += currentMethodHolder.getScore().toDouble()
            blackhole += legacyMethodHolder.getScore().toDouble()
            blackhole += currentField.getTargetValue("x", 0.0).toDouble()
            blackhole += legacyField.getTargetValue("x", 0.0).toDouble()
            blackhole += currentMethod.getTargetValue("score", 0.0).toDouble()
            blackhole += legacyMethod.getTargetValue("score", 0.0).toDouble()
        }

        val fieldSetCurrent = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                currentField.setTargetValue("x", index.toDouble())
                index++
            }
            currentFieldHolder.x
        }
        val fieldSetLegacy = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                legacyField.setTargetValue("x", index.toDouble())
                index++
            }
            legacyFieldHolder.x
        }

        val fieldGetCurrent = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += currentField.getTargetValue("x", 0.0).toDouble()
                index++
            }
            sum
        }
        val fieldGetLegacy = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += legacyField.getTargetValue("x", 0.0).toDouble()
                index++
            }
            sum
        }

        val methodSetCurrent = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                currentMethod.setTargetValue("score", index.toDouble())
                index++
            }
            currentMethodHolder.getScore().toDouble()
        }
        val methodSetLegacy = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                legacyMethod.setTargetValue("score", index.toDouble())
                index++
            }
            legacyMethodHolder.getScore().toDouble()
        }

        val methodGetCurrent = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += currentMethod.getTargetValue("score", 0.0).toDouble()
                index++
            }
            sum
        }
        val methodGetLegacy = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += legacyMethod.getTargetValue("score", 0.0).toDouble()
                index++
            }
            sum
        }

        val lines = mutableListOf(
            formatResult("reflect field set", fieldSetCurrent, fieldSetLegacy),
            formatResult("reflect field get", fieldGetCurrent, fieldGetLegacy),
            formatResult("reflect method set", methodSetCurrent, methodSetLegacy),
            formatResult("reflect method get", methodGetCurrent, methodGetLegacy)
        )
        lines += benchmarkBoundAccessorVsDispatch()
        lines.forEach(::println)
        writeReport(lines)

        val fieldHolder = FieldHolder()
        val currentCheck = TargetValueReflect(fieldHolder)
        val legacyCheck = LegacyTargetValueReflect(fieldHolder)
        currentCheck.setTargetValue("x", 123.0)
        assertEquals(123.0, currentCheck.getTargetValue("x", 0.0).toDouble(), 0.0)
        legacyCheck.setTargetValue("x", 456.0)
        assertEquals(456.0, legacyCheck.getTargetValue("x", 0.0).toDouble(), 0.0)
    }

    private fun measure(iterations: Int, rounds: Int, block: () -> Double): Double {
        val samples = DoubleArray(rounds)
        repeat(rounds) { round ->
            val start = System.nanoTime()
            val checksum = block()
            blackhole += checksum
            val elapsed = System.nanoTime() - start
            samples[round] = elapsed.toDouble() / iterations
        }
        samples.sort()
        return samples[samples.size / 2]
    }

    private fun formatResult(name: String, currentNsPerOp: Double, legacyNsPerOp: Double): String {
        val speedup = legacyNsPerOp / currentNsPerOp
        val improvement = ((legacyNsPerOp - currentNsPerOp) / legacyNsPerOp) * 100
        return buildString {
            append("[TargetValueReflect benchmark] ")
            append(name)
            append(": current=")
            append(currentNsPerOp.roundToLong())
            append(" ns/op, legacy=")
            append(legacyNsPerOp.roundToLong())
            append(" ns/op, speedup=")
            append(String.format("%.2fx", speedup))
            append(", improvement=")
            append(String.format("%.1f%%", improvement))
        }
    }

    private fun writeReport(lines: List<String>) {
        val reportFile = File(System.getProperty("user.dir"), "build/reports/target-value-reflect-benchmark.txt")
        reportFile.parentFile?.mkdirs()
        reportFile.writeText(lines.joinToString(separator = System.lineSeparator(), postfix = System.lineSeparator()))
    }

    private fun benchmarkBoundAccessorVsDispatch(): List<String> {
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

        val boundFieldSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                boundFieldAccessor.set(index.toDouble())
                index++
            }
            boundFieldHolder.x
        }
        val dispatchFieldSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                dispatchFieldReflect.setTargetValue("x", index.toDouble())
                index++
            }
            dispatchFieldHolder.x
        }

        val boundFieldGet = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += boundFieldAccessor.get(0.0).toDouble()
                index++
            }
            sum
        }
        val dispatchFieldGet = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += dispatchFieldReflect.getTargetValue("x", 0.0).toDouble()
                index++
            }
            sum
        }

        val boundMethodSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                boundMethodAccessor.set(index.toDouble())
                index++
            }
            boundMethodHolder.getScore().toDouble()
        }
        val dispatchMethodSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                dispatchMethodReflect.setTargetValue("score", index.toDouble())
                index++
            }
            dispatchMethodHolder.getScore().toDouble()
        }

        val boundMethodGet = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += boundMethodAccessor.get(0.0).toDouble()
                index++
            }
            sum
        }
        val dispatchMethodGet = measure(iterations, rounds) {
            var sum = 0.0
            var index = 0
            while (index < iterations) {
                sum += dispatchMethodReflect.getTargetValue("score", 0.0).toDouble()
                index++
            }
            sum
        }

        val boundMultiSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                val value = index.toDouble()
                boundXAccessor.set(value)
                boundYAccessor.set(value + 1)
                index++
            }
            boundMultiHolder.x + boundMultiHolder.y
        }
        val dispatchMultiSet = measure(iterations, rounds) {
            var index = 0
            while (index < iterations) {
                val value = index.toDouble()
                dispatchMultiReflect.setTargetValue("x", value)
                dispatchMultiReflect.setTargetValue("y", value + 1)
                index++
            }
            dispatchMultiHolder.x + dispatchMultiHolder.y
        }

        return listOf(
            formatResult("bound field set", boundFieldSet, dispatchFieldSet),
            formatResult("bound field get", boundFieldGet, dispatchFieldGet),
            formatResult("bound method set", boundMethodSet, dispatchMethodSet),
            formatResult("bound method get", boundMethodGet, dispatchMethodGet),
            formatResult("bound multi-field set", boundMultiSet, dispatchMultiSet)
        )
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

        fun getScore(): Int {
            return backingScore
        }
    }

    class MultiFieldHolder {
        @JvmField
        var x: Double = 0.0

        @JvmField
        var y: Double = 0.0
    }

    private class LegacyTargetValueReflect(private val target: Any) {
        private var targetClass: Class<*> = target::class.java
        private val targetFieldMap = mutableMapOf<String, Field>()
        private val targetMethodMap = mutableMapOf<String, Method>()

        fun setTargetValue(fieldName: String, value: Double) {
            if (fieldName.isEmpty()) {
                throw Error("${target::class.java.name} 要设置空属性的字段。值为: $value")
            }
            val field = getTargetField(targetClass, fieldName)
            if (field != null) {
                when (field.type) {
                    java.lang.Double::class.java, java.lang.Double.TYPE -> field.set(target, value)
                    java.lang.Float::class.java, java.lang.Float.TYPE -> field.set(target, value.toFloat())
                    java.lang.Long::class.java, java.lang.Long.TYPE -> field.set(target, value.toLong())
                    java.lang.Integer::class.java, java.lang.Integer.TYPE -> field.set(target, value.toInt())
                    java.lang.Character::class.java, java.lang.Character.TYPE -> field.set(target, value.toInt().toChar())
                    java.lang.Short::class.java, java.lang.Short.TYPE -> field.set(target, value.toInt().toShort())
                    java.lang.Byte::class.java, java.lang.Byte.TYPE -> field.set(target, value.toInt().toByte())
                }
            } else {
                val methodName = "set${fieldName.replaceFirstChar { it.uppercaseChar() }}"
                val method = getTargetMethod(targetClass, methodName)
                if (method != null) {
                    if (method.parameterTypes.isNotEmpty()) {
                        when (method.parameterTypes[0]) {
                            java.lang.Double::class.java, java.lang.Double.TYPE -> method.invoke(target, value)
                            java.lang.Float::class.java, java.lang.Float.TYPE -> method.invoke(target, value.toFloat())
                            java.lang.Long::class.java, java.lang.Long.TYPE -> method.invoke(target, value.toLong())
                            java.lang.Integer::class.java, java.lang.Integer.TYPE -> method.invoke(target, value.toInt())
                            java.lang.Character::class.java, java.lang.Character.TYPE -> method.invoke(target, value.toInt().toChar())
                            java.lang.Short::class.java, java.lang.Short.TYPE -> method.invoke(target, value.toInt().toShort())
                            java.lang.Byte::class.java, java.lang.Byte.TYPE -> method.invoke(target, value.toInt().toByte())
                        }
                    }
                } else {
                    throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
                }
            }
        }

        fun getTargetValue(fieldName: String, toValue: Double): Number {
            if (fieldName.isEmpty()) {
                throw Error("${target::class.java.name} 要设置空属性的字段。值为: $toValue")
            }

            val field = getTargetField(targetClass, fieldName)
            if (field != null) {
                val value = field.get(target)
                if (value is Number) {
                    return value
                } else {
                    throw Error("${target::class.java.name} $fieldName 属性 类型不是number")
                }
            } else {
                val methodName = "get${fieldName.replaceFirstChar { it.uppercaseChar() }}"
                val method = getTargetMethod(targetClass, methodName)
                if (method != null) {
                    val value = method.invoke(target)
                    if (value is Number) {
                        return value
                    } else {
                        throw Error("${target::class.java.name} $methodName 方法 获取的类型不是number")
                    }
                } else {
                    throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
                }
            }
        }

        private fun getTargetField(targetClass: Class<*>, name: String): Field? {
            if (targetFieldMap[name] != null) {
                return targetFieldMap[name]
            }
            try {
                val field = targetClass.getField(name)
                targetFieldMap[name] = field
                field.isAccessible = true
                return field
            } catch (_: Exception) {
                if (targetClass.superclass != null) {
                    return getTargetField(targetClass.superclass!!, name)
                }
            }
            return null
        }

        private fun getTargetMethod(targetClass: Class<*>, name: String): Method? {
            if (targetMethodMap[name] != null) {
                return targetMethodMap[name]
            }
            targetClass.methods.forEach {
                if (it.name == name) {
                    targetMethodMap[name] = it
                    it.isAccessible = true
                    return it
                }
            }
            if (targetClass.superclass != null) {
                return getTargetMethod(targetClass.superclass!!, name)
            }
            return null
        }
    }

    companion object {
        @Volatile
        private var blackhole = 0.0
    }
}
