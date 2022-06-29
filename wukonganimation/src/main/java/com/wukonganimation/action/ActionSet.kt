package com.wukonganimation.action

import android.util.Log
import android.view.View
import com.wukonganimation.event.EventEmitter
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

/**
 * 动画集合
 */
class ActionSet : EventEmitter {


    companion object{
        const val EVENT_START = "start"
        const val EVENT_END = "end"
    }

    val TAG = this::class.java.name
    val component: View
    var mStartActionData: ActionData
    var mAnimationParse: ActionParse
    var mIsStart = false
    val mRunTweenList = mutableListOf<Tween>()

    constructor(view: View, func: () -> ActionData) {
        component = view
        mAnimationParse = ActionParse(component)
        mStartActionData = mAnimationParse.paresFunction(func)
    }


    /**
     * 执行动画
     */
    fun start() {
        if (mIsStart) {
            Log.w(TAG, "当前动画集合正在运行中")
            return
        }
        mIsStart = true
        emit(EVENT_START)
        this.runTween(mStartActionData)
    }


    /**
     * 停止动画
     */
    fun stop() {
        this.mRunTweenList.forEach {
            it.stop().remove()
        }
        mIsStart = false
        this.mRunTweenList.clear()
    }

    private fun runTween(animationData: ActionData, callback: (() -> Unit)? = null) {
        when (animationData.type) {
            ActionData.spawn -> {
                this.runSpawn(animationData, callback)
            }
            ActionData.sequence -> {
                val animationDataArray = animationData.animationDataArray
                if (animationDataArray.isNullOrEmpty()) {
                    callback?.invoke()
                    return
                }
                this.runSequence(animationDataArray, 0, callback);
            }
            ActionData.callFunc -> {
                animationData.callback?.invoke()
                callback?.invoke()
                this.tweenEnd()
            }
            else -> {
                val tween = this.mAnimationParse.paresTween(animationData)
                tween.on(TweenManager.EVENT_END) {
                    callback?.invoke()
                    this.tweenEnd(tween, animationData)
                }
                this.mRunTweenList.add(tween)
                tween.start()
            }
        }
    }

    private fun runSpawn(animationData: ActionData, callback: (() -> Unit)? = null) {
        val animationDataArray = animationData.animationDataArray
        if (animationDataArray == null) {
            callback?.invoke()
            return;
        }
        var index = 0
        animationDataArray.forEach {
            this.runTween(this.mAnimationParse.paresFunction(it)) {
                index++
                if (index >= animationDataArray.size) {
                    callback?.invoke()
                }
            }
        }
    }


    private fun runSequence(
        animationDataArray: List<() -> ActionData>,
        index: Int,
        callback: (() -> Unit)? = null
    ) {
        val animationData = this.mAnimationParse.paresFunction(animationDataArray[index])
        this.runTween(animationData) {
            val indexAddOne = index + 1
            if (indexAddOne >= animationDataArray.size) {
                callback?.invoke()
                return@runTween
            }
            this.runSequence(animationDataArray, indexAddOne, callback)
        }
    }

    private fun tweenEnd(tween: Tween? = null, animationData: ActionData? = null) {
        if (animationData != null) {
            val animationDataArray = animationData.animationDataArray
            if (!animationDataArray.isNullOrEmpty()) {
                this.runTween(this.mAnimationParse.paresFunction(animationDataArray[0]))
            }
        }
        if (tween != null) {
            this.mRunTweenList.remove(tween)
            tween.off(TweenManager.EVENT_END)
        }
        if (this.mRunTweenList.size == 0) {
            this.mIsStart = false
            this.emit(EVENT_END)
        }
    }

}