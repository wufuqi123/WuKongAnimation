package com.wukonganimation

import com.wukonganimation.tween.Easing
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.targetvalue.TargetValueAbstract
import com.wukonganimation.tween.targetvalue.TargetValueReflect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun defaultFactories_returnCachedInstances() {
        assertSame(Easing.linear(), Easing.linear())
        assertSame(Easing.inOutSine(), Easing.inOutSine())
        assertSame(Easing.inElastic(), Easing.inElastic())
        assertSame(Easing.outElastic(), Easing.outElastic())
        assertSame(Easing.inOutElastic(), Easing.inOutElastic())
        assertSame(Easing.inBack(), Easing.inBack())
        assertSame(Easing.outBack(), Easing.outBack())
        assertSame(Easing.inOutBack(), Easing.inOutBack())
        assertSame(Easing.inBounce(), Easing.inBounce())
        assertSame(Easing.outBounce(), Easing.outBounce())
        assertSame(Easing.inOutBounce(), Easing.inOutBounce())
    }

    @Test
    fun easingEndpoints_stayStable() {
        val easings = listOf(
            Easing.linear(),
            Easing.inQuad(),
            Easing.outQuad(),
            Easing.inOutQuad(),
            Easing.inCubic(),
            Easing.outCubic(),
            Easing.inOutCubic(),
            Easing.inQuart(),
            Easing.outQuart(),
            Easing.inOutQuart(),
            Easing.inQuint(),
            Easing.outQuint(),
            Easing.inOutQuint(),
            Easing.inSine(),
            Easing.outSine(),
            Easing.inOutSine(),
            Easing.inExpo(),
            Easing.outExpo(),
            Easing.inOutExpo(),
            Easing.inCirc(),
            Easing.outCirc(),
            Easing.inOutCirc(),
            Easing.inElastic(),
            Easing.outElastic(),
            Easing.inOutElastic(),
            Easing.inBack(),
            Easing.outBack(),
            Easing.inOutBack(),
            Easing.inBounce(),
            Easing.outBounce(),
            Easing.inOutBounce()
        )

        easings.forEach {
            assertEquals(0.0, it.invoke(0.0), EPSILON)
            assertEquals(1.0, it.invoke(1.0), EPSILON)
        }
    }

    @Test
    fun representativeSamples_matchExpectedValues() {
        assertEquals(0.5, Easing.linear().invoke(0.5), EPSILON)
        assertEquals(0.25, Easing.inQuad().invoke(0.5), EPSILON)
        assertEquals(0.75, Easing.outQuad().invoke(0.5), EPSILON)
        assertEquals(-0.0876975, Easing.inBack().invoke(0.5), EPSILON)
        assertEquals(1.0876975, Easing.outBack().invoke(0.5), EPSILON)
        assertEquals(0.765625, Easing.outBounce().invoke(0.5), EPSILON)
        assertEquals(0.234375, Easing.inBounce().invoke(0.5), EPSILON)
        assertEquals(0.1171875, Easing.inOutBounce().invoke(0.25), EPSILON)
        assertEquals(Easing.inBack().invoke(0.5), Easing.inBack(null).invoke(0.5), EPSILON)
        assertEquals(Easing.outBack().invoke(0.5), Easing.outBack(null).invoke(0.5), EPSILON)
        assertEquals(Easing.inOutBack().invoke(0.5), Easing.inOutBack(null).invoke(0.5), EPSILON)
    }

    @Test
    fun tweenCachesBuiltInPrimitiveEasing() {
        val tween = Tween(Holder()).easing(Easing.inQuad())
        val primitiveField = Tween::class.java.getDeclaredField("easingDoubleProperty")
        primitiveField.isAccessible = true

        assertSame(Easing.inQuad(), primitiveField.get(tween))
    }

    @Test
    fun tweenCachesBuiltInFloatEasingForLinear() {
        val tween = Tween(Holder()).easing(Easing.linear())
        val primitiveField = Tween::class.java.getDeclaredField("easingFloatProperty")
        primitiveField.isAccessible = true

        assertSame(Easing.linear(), primitiveField.get(tween))
    }

    @Test
    fun tweenCachesBuiltInFloatEasingForInQuad() {
        val tween = Tween(Holder()).easing(Easing.inQuad())
        val primitiveField = Tween::class.java.getDeclaredField("easingFloatProperty")
        primitiveField.isAccessible = true

        assertSame(Easing.inQuad(), primitiveField.get(tween))
    }

    @Test
    fun tweenFallsBackForCustomLambdaAndStillInterpolates() {
        val holder = Holder()
        val tween = Tween(holder)
            .time(1000)
            .to(mutableMapOf("x" to 10.0))
            .easing { t -> t * t }
            .start()

        val primitiveField = Tween::class.java.getDeclaredField("easingDoubleProperty")
        primitiveField.isAccessible = true

        assertNull(primitiveField.get(tween))

        tween.update(500.0)

        assertEquals(2.5, holder.x, EPSILON)
    }

    @Test
    fun tweenBuiltInPrimitiveEasingInterpolatesCorrectly() {
        val holder = Holder()
        val tween = Tween(holder)
            .time(1000)
            .to(mutableMapOf("x" to 10.0))
            .easing(Easing.inQuad())
            .start()

        tween.update(500.0)

        assertEquals(2.5, holder.x, EPSILON)
    }

    @Test
    fun tweenCachesPropertyStatesForMultiFieldUpdates() {
        val holder = MultiHolder()
        val tween = Tween(holder)
            .time(1000)
            .to(mutableMapOf("x" to 10.0, "y" to 20.0))
            .start()

        val countField = Tween::class.java.getDeclaredField("propertyStateCount")
        countField.isAccessible = true

        tween.update(500.0)

        assertEquals(2, countField.getInt(tween))
        assertEquals(5.0, holder.x, EPSILON)
        assertEquals(10.0, holder.y, EPSILON)
    }

    @Test
    fun tweenPingPongRebuildsCachedPropertyStates() {
        val holder = Holder()
        val tween = Tween(holder)
            .time(1000)
            .to(mutableMapOf("x" to 10.0))
            .pingPong(true)
            .start()

        tween.update(500.0)
        assertEquals(10.0, holder.x, EPSILON)

        tween.update(250.0)
        assertEquals(5.0, holder.x, EPSILON)
    }

    @Test
    fun targetValueReflect_readsAndWritesPublicPrimitiveField() {
        val holder = ReflectFieldHolder()
        val reflect = TargetValueReflect(holder)

        reflect.setTargetValue("x", 12.5)

        assertEquals(12.5, holder.x, EPSILON)
        assertEquals(12.5, reflect.getTargetValue("x", 0.0).toDouble(), EPSILON)
    }

    @Test
    fun targetValueReflect_usesGetterSetterMethods() {
        val holder = ReflectMethodHolder()
        val reflect = TargetValueReflect(holder)

        reflect.setTargetValue("score", 8.9)

        assertEquals(8, holder.getScore())
        assertEquals(8.0, reflect.getTargetValue("score", 0.0).toDouble(), EPSILON)
    }

    @Test
    fun targetValueReflect_missingPropertyStillThrows() {
        val reflect = TargetValueReflect(ReflectFieldHolder())

        assertThrows(Error::class.java) {
            reflect.setTargetValue("missing", 1.0)
        }
    }

    @Test
    fun targetValueReflect_bindPropertyReadsAndWrites() {
        val holder = ReflectFieldHolder()
        val accessor = TargetValueReflect(holder).bindProperty("x")

        accessor.set(33.5)

        assertEquals(33.5, holder.x, EPSILON)
        assertEquals(33.5, accessor.get(0.0).toDouble(), EPSILON)
    }

    @Test
    fun tweenCachesBoundPropertyAccessorInState() {
        val holder = Holder()
        val tween = Tween(holder)
            .time(1000)
            .to(mutableMapOf("x" to 10.0))
            .start()

        tween.update(100.0)

        val countField = Tween::class.java.getDeclaredField("propertyStateCount")
        countField.isAccessible = true
        val accessorsField = Tween::class.java.getDeclaredField("propertyAccessors")
        accessorsField.isAccessible = true
        val useFloatField = Tween::class.java.getDeclaredField("propertyUseFloatPath")
        useFloatField.isAccessible = true

        val accessors = accessorsField.get(tween) as Array<*>
        val useFloat = useFloatField.get(tween) as BooleanArray

        assertEquals(1, countField.getInt(tween))
        assertEquals(1.0, holder.x, EPSILON)
        assertEquals("BoundPropertyAccessor", TargetValueAbstract.BoundPropertyAccessor::class.java.simpleName)
        assertEquals(false, useFloat[0])
        assertEquals(true, accessors[0] is TargetValueAbstract.BoundPropertyAccessor)
    }

    @Test
    fun tweenEnablesLinearFastPathForDefaultEasing() {
        val tween = Tween(Holder()).easing(Easing.linear())
        val fastPathField = Tween::class.java.getDeclaredField("useLinearEasingFastPath")
        fastPathField.isAccessible = true

        assertEquals(true, fastPathField.getBoolean(tween))
    }

    @Test
    fun tweenDisablesLinearFastPathForCustomLambda() {
        val tween = Tween(Holder()).easing { t -> t }
        val fastPathField = Tween::class.java.getDeclaredField("useLinearEasingFastPath")
        fastPathField.isAccessible = true

        assertEquals(false, fastPathField.getBoolean(tween))
    }

    class Holder {
        @JvmField
        var x: Double = 0.0
    }

    class MultiHolder {
        @JvmField
        var x: Double = 0.0

        @JvmField
        var y: Double = 0.0
    }

    class ReflectFieldHolder {
        @JvmField
        var x: Double = 0.0
    }

    class ReflectMethodHolder {
        private var backingScore: Int = 0

        fun setScore(value: Int) {
            backingScore = value
        }

        fun getScore(): Int {
            return backingScore
        }
    }

    companion object {
        private const val EPSILON = 1e-9
    }
}