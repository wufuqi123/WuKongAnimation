package com.wukonganimation.action

import com.wukonganimation.tween.Tween

class ActionData {
    companion object {
        const val fadeTo = "fadeTo"
        const val fadeIn = "fadeIn"
        const val fadeOut = "fadeOut"
        const val moveTo = "moveTo"
        const val moveBy = "moveBy"
        const val scaleTo = "scaleTo"
        const val scaleBy = "scaleBy"
        const val rotateTo = "rotateTo"
        const val rotateXTo = "rotateXTo"
        const val rotateYTo = "rotateYTo"
        const val rotateBy = "rotateBy"
        const val rotateXBy = "rotateXBy"
        const val rotateYBy = "rotateYBy"
        const val callFunc = "callFunc"
        const val spawn = "spawn"
        const val sequence = "sequence"
    }

    var type: String? = null
    var alpha: Float = Float.NaN
    var time: Long = 0
    var y: Float = Float.NaN
    var x: Float = Float.NaN
    var rotation: Float = Float.NaN
    var rotationX: Float = Float.NaN
    var rotationY: Float = Float.NaN
    var scaleX: Float = Float.NaN
    var scaleY: Float = Float.NaN
    var easingProperty: ((t: Double) -> Double)? = null
    var tween: Tween? = null

    /**
     * 准备自定义时，额外挂载的参数
     */
    var extra:Any? = null
    var callback: (() -> Unit)? = null
    var animationDataArray: (List<() -> ActionData>)? = null
}