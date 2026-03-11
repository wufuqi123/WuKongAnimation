@file:Suppress("unused")

package com.wukonganimation.action.extend

import android.view.View
import com.wukonganimation.action.chained.AbstractActionBuild
import com.wukonganimation.action.chained.SequenceActionBuild
import com.wukonganimation.action.chained.SequenceActionRunBuild
import com.wukonganimation.action.chained.SpawnActionBuild
import com.wukonganimation.action.chained.SpawnActionRunBuild

@DslMarker
annotation class WukongDslMarker

/**
 * 纯构建顺序 action，不会自动 start，方便复用或测试。
 */
fun sequenceAction(block: WukongSequenceScope.() -> Unit): SequenceActionBuild {
    val builder = SequenceActionBuild()
    WukongSequenceScope(builder).block()
    return builder
}

/**
 * 纯构建并行动作，不会自动 start，方便复用或测试。
 */
fun spawnAction(block: WukongSpawnScope.() -> Unit): SpawnActionBuild {
    val builder = SpawnActionBuild()
    WukongSpawnScope(builder).block()
    return builder
}

/**
 * 悟空 DSL，默认顺序执行，并自动 start。
 */
fun View.wukong(block: WukongSequenceScope.() -> Unit): SequenceActionRunBuild {
    val builder = createAction()
    WukongSequenceScope(builder).block()
    return builder.start()
}

/**
 * 显式顺序 DSL，和 [wukong] 一样，默认自动 start。
 */
fun View.wukongSequence(block: WukongSequenceScope.() -> Unit): SequenceActionRunBuild {
    return wukong(block)
}

/**
 * 顶层并行 DSL，自动 start。
 */
fun View.wukongSpawn(block: WukongSpawnScope.() -> Unit): SpawnActionRunBuild {
    val builder = createSpawnAction()
    WukongSpawnScope(builder).block()
    return builder.start()
}

@WukongDslMarker
open class WukongActionScope internal constructor(
    private val builder: AbstractActionBuild<*>
) {
    fun fadeTo(time: Long, alpha: Float, easing: ((t: Double) -> Double)? = null) {
        builder.fadeTo(time, alpha, easing)
    }

    fun fadeIn(time: Long, easing: ((t: Double) -> Double)? = null) {
        builder.fadeIn(time, easing)
    }

    fun fadeOut(time: Long, easing: ((t: Double) -> Double)? = null) {
        builder.fadeOut(time, easing)
    }

    fun moveTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ) {
        builder.moveTo(time, x, y, easing)
    }

    fun moveBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ) {
        builder.moveBy(time, x, y, easing)
    }

    fun scaleTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ) {
        builder.scaleTo(time, x, y, easing)
    }

    fun scaleBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ) {
        builder.scaleBy(time, x, y, easing)
    }

    fun rotateTo(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateTo(time, rotation, easing)
    }

    fun rotateBy(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateBy(time, rotation, easing)
    }

    fun rotateXTo(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateXTo(time, rotation, easing)
    }

    fun rotateXBy(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateXBy(time, rotation, easing)
    }

    fun rotateYTo(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateYTo(time, rotation, easing)
    }

    fun rotateYBy(time: Long, rotation: Float, easing: ((t: Double) -> Double)? = null) {
        builder.rotateYBy(time, rotation, easing)
    }

    fun callFunc(callback: () -> Unit) {
        builder.callFunc(callback)
    }

    @JvmName("waitAction")
    fun wait(time: Long) {
        builder.wait(time)
    }

    fun spawn(block: WukongSpawnScope.() -> Unit) {
        builder.spawn(spawnAction(block))
    }

    fun sequence(block: WukongSequenceScope.() -> Unit) {
        builder.sequence(sequenceAction(block))
    }
}

@WukongDslMarker
class WukongSequenceScope internal constructor(
    builder: AbstractActionBuild<*>
) : WukongActionScope(builder)

@WukongDslMarker
class WukongSpawnScope internal constructor(
    builder: AbstractActionBuild<*>
) : WukongActionScope(builder)

