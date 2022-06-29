package com.wukonganimation.action.chained

import com.wukonganimation.action.Action
import com.wukonganimation.action.ActionData

/**
 * 抽象的  action 链式build
 */
abstract class AbstractActionBuild<T> {

    val animationDataArray = mutableListOf<() -> ActionData>()

    /**
     * 类型
     */
    abstract val type: ActionBuildTypeEnum

    /**
     * 透明度渐变到指定值
     * @param time 时间毫秒数
     * @param alpha 透明度 0 - 1
     * @param easing 缓动函数  Easing.linear()
     */
    fun fadeTo(
        time: Long,
        alpha: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.fadeTo(time, alpha, easing))
        return this as T
    }


    /**
     *  透明度渐入
     * @param time 时间毫秒数
     * @param easing 缓动函数  Easing.linear()
     */
    fun fadeIn(time: Long, easing: ((t: Double) -> Double)? = null): T {
        animationDataArray.add(Action.fadeIn(time, easing))
        return this as T
    }


    /**
     *  透明度渐入
     * @param time 时间毫秒数
     * @param easing 缓动函数  Easing.linear()
     */
    fun fadeOut(time: Long, easing: ((t: Double) -> Double)? = null): T {
        animationDataArray.add(Action.fadeOut(time, easing))
        return this as T
    }

    /**
     *  移动到目标位置
     * @param time 时间毫秒数
     * @param x x位置
     * @param y y位置
     * @param easing 缓动函数  Easing.linear()
     */
    fun moveTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.moveTo(time, x, y, easing))
        return this as T
    }


    /**
     *  继续移动多少位置
     * @param time 时间毫秒数
     * @param x x位置
     * @param y y位置
     * @param easing 缓动函数  Easing.linear()
     */
    fun moveBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.moveBy(time, x, y, easing))
        return this as T
    }

    /**
     *  缩放到指定大小
     * @param time 时间毫秒数
     * @param x x缩放
     * @param y y缩放
     * @param easing 缓动函数  Easing.linear()
     */
    fun scaleTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.scaleTo(time, x, y, easing))
        return this as T
    }


    /**
     *  继续缩放多少大小
     * @param time 时间毫秒数
     * @param x x缩放
     * @param y y缩放
     * @param easing 缓动函数  Easing.linear()
     */
    fun scaleBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.scaleBy(time, x, y, easing))
        return this as T
    }


    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateTo(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateTo(time, rotation, easing))
        return this as T
    }

    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateBy(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateBy(time, rotation, easing))
        return this as T
    }

    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateXTo(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateXTo(time, rotation, easing))
        return this as T
    }

    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateXBy(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateXBy(time, rotation, easing))
        return this as T
    }

    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateYTo(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateYTo(time, rotation, easing))
        return this as T
    }


    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     * @param easing 缓动函数  Easing.linear()
     */
    fun rotateYBy(
        time: Long,
        rotation: Float,
        easing: ((t: Double) -> Double)? = null
    ): T {
        animationDataArray.add(Action.rotateYBy(time, rotation, easing))
        return this as T
    }


    /**
     *  同步动画
     * @param action SpawnActionBuild
     */
    fun spawn(
        action: SpawnActionBuild
    ): T {
        animationDataArray.add {
            val animationData = ActionData()
            animationData.type = ActionData.spawn
            animationData.animationDataArray = action.animationDataArray
            animationData
        }
        return this as T
    }

    /**
     *  同步动画
     * @param action SpawnActionBuild
     */
    fun spawn(
        actionFunc: () -> SpawnActionBuild
    ): T {
        return spawn(actionFunc.invoke())
    }


    /**
     *  顺序动画
     * @param action SequenceActionBuild
     */
    fun sequence(
        action: SequenceActionBuild
    ): T {
        animationDataArray.add {
            val animationData = ActionData()
            animationData.type = ActionData.sequence
            animationData.animationDataArray = action.animationDataArray
            animationData
        }
        return this as T
    }

    /**
     *  顺序动画
     * @param action SequenceActionBuild
     */
    fun sequence(
        actionFunc: () -> SequenceActionBuild
    ): T {
        return sequence(actionFunc.invoke())
    }

    /**
     *  回调
     * @param callback 回调函数
     */
    fun callFunc(
        callback: () -> Unit
    ): T {
        animationDataArray.add(Action.callFunc(callback))
        return this as T
    }

    /**
     *  等待
     * @param time 时间毫秒
     */
    fun wait(
        time: Long
    ): T {
        animationDataArray.add(Action.wait(time))
        return this as T
    }


    enum class ActionBuildTypeEnum {
        /**
         * 同步
         */
        SPAWN,

        /**
         * 顺序
         */
        SEQUENCE
    }
}