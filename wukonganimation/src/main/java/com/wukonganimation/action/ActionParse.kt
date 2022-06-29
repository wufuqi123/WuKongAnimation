package com.wukonganimation.action

import android.view.View
import com.wukonganimation.tween.Easing
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class ActionParse {
    val component: View

    constructor(view: View) {
        component = view
    }

    /**
     * 解析出  ActionData
     * @param fun
     */
    fun paresFunction(func: () -> ActionData): ActionData {
        return func.invoke()
    }

    /**
     * 解析 Tween
     * @param animationData
     */
    fun paresTween(animationData: ActionData): Tween {
        val tween = TweenManager.builder(this.component)
        tween.setExpire(true)
        tween.time(animationData.time)
        tween.easing(animationData.easingProperty ?: Easing.linear())
        this.paresAmin(animationData)
        this.bindAmin(animationData, tween)
        return tween;
    }

    /**
     * 解析额外的动画
     * 自定义动画时可重写他
     */
    fun paresExtraAmin(animationData: ActionData) {

    }

    /**
     * 绑定额外的动画
     * 自定义动画时可重写他
     */
    fun bindExtraAmin(animationData: ActionData, tween: Tween) {

    }


    /**
     * 初始化ActionData
     * @param animationData
     */
    private fun paresAmin(animationData: ActionData) {
        when (animationData.type) {
            ActionData.fadeTo ->
                this.paresFadeTo(animationData)
            ActionData.fadeIn ->
                this.paresFadeIn(animationData)

            ActionData.fadeOut ->
                this.paresFadeOut(animationData)
            ActionData.moveTo ->
                this.paresMoveTo(animationData)
            ActionData.moveBy ->
                this.paresMoveBy(animationData)
            ActionData.scaleTo ->
                this.paresScaleTo(animationData)
            ActionData.scaleBy ->
                this.paresScaleBy(animationData)
            ActionData.rotateTo ->
                this.paresRotateTo(animationData)
            ActionData.rotateBy ->
                this.paresRotateBy(animationData)
            ActionData.rotateXTo ->
                this.paresRotateXTo(animationData)
            ActionData.rotateXBy ->
                this.paresRotateXBy(animationData)
            ActionData.rotateYTo ->
                this.paresRotateYTo(animationData)
            ActionData.rotateYBy ->
                this.paresRotateYBy(animationData)
            else -> this.paresExtraAmin(animationData)
        }
    }


    /**
     * 绑定动画
     * @param animationData
     * @param tween
     */
    private fun bindAmin(animationData: ActionData, tween: Tween) {
        when (animationData.type) {
            ActionData.fadeTo,
            ActionData.fadeIn,
            ActionData.fadeOut ->

                tween.to(mutableMapOf("alpha" to animationData.alpha))

            ActionData.moveTo,
            ActionData.moveBy ->
                tween.to(mutableMapOf("x" to animationData.x, "y" to animationData.y))
            ActionData.scaleTo,
            ActionData.scaleBy ->
                tween.to(
                    mutableMapOf(
                        "scaleX" to animationData.scaleX,
                        "scaleY" to animationData.scaleY
                    )
                )
            ActionData.rotateTo,
            ActionData.rotateBy->
                tween.to(mutableMapOf("rotation" to animationData.rotation))
            ActionData.rotateXTo,
            ActionData.rotateXBy->
                tween.to(mutableMapOf("rotationX" to animationData.rotationX))
            ActionData.rotateYTo,
            ActionData.rotateYBy->
                tween.to(mutableMapOf("rotationY" to animationData.rotationY))
            ActionData.callFunc ->
                if (animationData.callback != null) {
                    tween.on(TweenManager.EVENT_END) {
                        animationData.callback?.invoke()
                    }
                }
            else -> this.bindExtraAmin(animationData, tween)
        }
    }


    /**
     * 解析FadeTo
     * @param animationData
     */
    private fun paresFadeTo(animationData: ActionData) {
        if (animationData.alpha.isNaN()) {
            animationData.alpha = this.component.alpha
        }
    }

    /**
     * 解析FadeIn
     * @param animationData
     */
    private fun paresFadeIn(animationData: ActionData) {
        this.paresFadeTo(animationData)
    }


    /**
     * 解析FadeOut
     * @param animationData
     */
    private fun paresFadeOut(animationData: ActionData) {
        this.paresFadeTo(animationData)
    }

    /**
     * 解析MoveTo
     * @param animationData
     */
    private fun paresMoveTo(animationData: ActionData) {
        if (animationData.x.isNaN()) {
            animationData.x = this.component.x
        }
        if (animationData.y.isNaN()) {
            animationData.y = this.component.y
        }
    }

    /**
     * 解析MoveBy
     * @param animationData
     */
    private fun paresMoveBy(animationData: ActionData) {
        if (animationData.x.isNaN()) {
            animationData.x = this.component.x
        } else {
            animationData.x += this.component.x
        }
        if (animationData.y.isNaN()) {
            animationData.y = this.component.y
        } else {
            animationData.y += this.component.y
        }
    }


    /**
     * 解析ScaleTo
     * @param animationData
     */
    private fun paresScaleTo(animationData: ActionData) {
        if (animationData.scaleX.isNaN()) {
            animationData.scaleX = this.component.scaleX
        }
        if (animationData.scaleY.isNaN()) {
            animationData.scaleY = this.component.scaleY
        }
    }


    /**
     * 解析ScaleBy
     * @param animationData
     */
    private fun paresScaleBy(animationData: ActionData) {
        if (animationData.scaleX.isNaN()) {
            animationData.scaleX = this.component.scaleX
        } else {
            animationData.scaleX += this.component.scaleX
        }
        if (animationData.scaleY.isNaN()) {
            animationData.scaleY = this.component.scaleY
        } else {
            animationData.scaleY += this.component.scaleY
        }
    }


    /**
     * 解析RotateTo
     * @param animationData
     */
    private fun paresRotateTo(animationData: ActionData) {
        if (animationData.rotation.isNaN()) {
            animationData.rotation = this.component.rotation
        }
    }

    /**
     * 解析RotateBy
     * @param animationData
     */
    private fun paresRotateBy(animationData: ActionData) {
        if (animationData.rotation.isNaN()) {
            animationData.rotation = this.component.rotation
        } else {
            animationData.rotation += this.component.rotation
        }
    }


    /**
     * 解析RotateXTo
     * @param animationData
     */
    private fun paresRotateXTo(animationData: ActionData) {
        if (animationData.rotationX.isNaN()) {
            animationData.rotationX = this.component.rotationX
        }
    }

    /**
     * 解析RotateXBy
     * @param animationData
     */
    private fun paresRotateXBy(animationData: ActionData) {
        if (animationData.rotationX.isNaN()) {
            animationData.rotationX = this.component.rotationX
        } else {
            animationData.rotationX += this.component.rotationX
        }
    }

    /**
     * 解析RotateYTo
     * @param animationData
     */
    private fun paresRotateYTo(animationData: ActionData) {
        if (animationData.rotationY.isNaN()) {
            animationData.rotationY = this.component.rotationY
        }
    }

    /**
     * 解析RotateYTo
     * @param animationData
     */
    private fun paresRotateYBy(animationData: ActionData) {
        if (animationData.rotationY.isNaN()) {
            animationData.rotationY = this.component.rotationY
        } else {
            animationData.rotationY += this.component.rotationY
        }
    }
}