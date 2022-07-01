package com.wukonganimation.action


/**
 * action 动画集合
 */
object Action {

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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.fadeTo
            animationData.time = time
            animationData.alpha = alpha
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     * 透明度渐变到指定值
     * @param time 时间毫秒数
     * @param alpha 透明度 0 - 1
     */
    fun fadeTo(
        time: Long,
        alpha: Float
    ): () -> ActionData {
        return fadeTo(time, alpha, null)
    }

    /**
     *  透明度渐入
     * @param time 时间毫秒数
     * @param easing 缓动函数  Easing.linear()
     */
    fun fadeIn(time: Long, easing: ((t: Double) -> Double)? = null): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.fadeIn
            animationData.time = time
            animationData.alpha = 1f
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  透明度渐入
     * @param time 时间毫秒数
     */
    fun fadeIn(time: Long): () -> ActionData {
        return fadeIn(time, null)
    }

    /**
     *  透明度渐入
     * @param time 时间毫秒数
     * @param easing 缓动函数  Easing.linear()
     */
    fun fadeOut(time: Long, easing: ((t: Double) -> Double)? = null): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.fadeOut
            animationData.time = time
            animationData.alpha = 0f
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  透明度渐入
     * @param time 时间毫秒数
     */
    fun fadeOut(time: Long): () -> ActionData {
        return fadeOut(time, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.moveTo
            animationData.time = time
            animationData.x = x
            animationData.y = y
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  移动到目标位置
     * @param time 时间毫秒数
     * @param x x位置
     * @param y y位置
     */
    fun moveTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN
    ): () -> ActionData {
        return moveTo(time, x, y, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.moveBy
            animationData.time = time
            animationData.x = x
            animationData.y = y
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  继续移动多少位置
     * @param time 时间毫秒数
     * @param x x位置
     * @param y y位置
     */
    fun moveBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN
    ): () -> ActionData {
        return moveBy(time, x, y, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.scaleTo
            animationData.time = time
            animationData.scaleX = x
            animationData.scaleY = y
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  缩放到指定大小
     * @param time 时间毫秒数
     * @param x x缩放
     * @param y y缩放
     */
    fun scaleTo(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN
    ): () -> ActionData {
        return scaleTo(time, x, y, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.scaleBy
            animationData.time = time
            animationData.scaleX = x
            animationData.scaleY = y
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  继续缩放多少大小
     * @param time 时间毫秒数
     * @param x x缩放
     * @param y y缩放
     */
    fun scaleBy(
        time: Long,
        x: Float = Float.NaN,
        y: Float = Float.NaN
    ): () -> ActionData {
        return scaleBy(time, x, y, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateTo
            animationData.time = time
            animationData.rotation = rotation
            animationData.easingProperty = easing
            animationData
        }
    }


    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateTo(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateTo(time, rotation, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateBy
            animationData.time = time
            animationData.rotation = rotation
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateBy(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateBy(time, rotation, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateXTo
            animationData.time = time
            animationData.rotationX = rotation
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateXTo(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateXTo(time, rotation, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateXBy
            animationData.time = time
            animationData.rotationX = rotation
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateXBy(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateXBy(time, rotation, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateYTo
            animationData.time = time
            animationData.rotationY = rotation
            animationData.easingProperty = easing
            animationData
        }
    }

    /**
     *  旋转到目标角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateYTo(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateYTo(time, rotation, null)
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
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.rotateYBy
            animationData.time = time
            animationData.rotationY = rotation
            animationData.easingProperty = easing
            animationData
        }
    }


    /**
     *  继续旋转多少角度
     * @param time 时间毫秒数
     * @param rotation 角度 0 - 360
     */
    fun rotateYBy(
        time: Long,
        rotation: Float
    ): () -> ActionData {
        return rotateYBy(time, rotation, null)
    }


    /**
     *  回调
     * @param callback 回调函数
     */
    fun callFunc(
        callback: () -> Unit
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.callFunc
            animationData.callback = callback
            animationData
        }
    }

    /**
     *  同步动画
     * @param adArr Action.moveTo()  ...  等等方法
     */
    fun spawn(
        vararg adArr: () -> ActionData
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.spawn
            animationData.animationDataArray = adArr.toList()
            animationData
        }
    }

    /**
     *  顺序动画
     * @param adArr Action.moveTo()  ...  等等方法
     */
    fun sequence(
        vararg adArr: () -> ActionData
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.type = ActionData.sequence
            animationData.animationDataArray = adArr.toList()
            animationData
        }
    }

    /**
     *  等待
     * @param time 时间毫秒
     */
    fun wait(
        time: Long
    ): () -> ActionData {
        return {
            val animationData = ActionData()
            animationData.time = time
            animationData
        }
    }
}