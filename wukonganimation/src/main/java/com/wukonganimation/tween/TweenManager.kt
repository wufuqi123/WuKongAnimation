package com.wukonganimation.tween

import java.util.*

class TweenManager {

    //Tween任务池
    private val tweens = Vector<Tween>()

    //删除池
    private val tweensToDelete = Vector<Tween>()


    //    var mAnimationThread: AnimationThread? = null
    var mTweenAnimationHandler: TweenAnimationHandler? = null


    constructor() {
//        this.mAnimationThread = AnimationThread(this)
        this.mTweenAnimationHandler = TweenAnimationHandler(this)
    }


    companion object {
        const val EVENT_START = "start"
        const val EVENT_RESTART = "restart"
        const val EVENT_UPDATE = "update"
        const val EVENT_PINGPONG = "pingpong"
        const val EVENT_ERPEAT = "repeat"
        const val EVENT_END = "end"
        const val EVENT_STOP = "stop"


        private val instance by lazy(LazyThreadSafetyMode.NONE) {
            TweenManager()
        }

        fun builder(target: Any): Tween {
            return instance.createTween(target)
        }

    }


    /**
     * 当前是否还有任务
     */
    fun isEmpty(): Boolean {
        return tweens.isEmpty() && tweensToDelete.isEmpty()
    }

    fun upDate(dt: Long) {
        tweens.forEach {
            if (it.active) {
                it.update(dt)
            }
            if (it.isEnded && it.expire) {
                it.remove()
            }
        }
        if (tweensToDelete.isNotEmpty()) {
            tweensToDelete.forEach {
                tweens.remove(it)
            }
            tweensToDelete.clear()
        }
    }

    /**
     * 开始运行动画，如果有动画正在执行，则不再执行
     */
    fun runAnimation() {
        mTweenAnimationHandler?.start()
    }

    /**
     * 根据target找任务池里的tween
     */
    fun getTweensForTarget(target: Any): MutableList<Tween> {
        val tweens = mutableListOf<Tween>()
        this.tweens.forEach {
            if (it.target == target) {
                tweens.add(it)
            }
        }
        return tweens;
    }

    /**
     * 创建一个跟TweenManager 绑定的Tween
     */
    fun createTween(target: Any): Tween {
        return Tween(target, this)
    }

    //把Tween 添加到 任务池里
    fun addTween(tween: Tween) {
        tween.manager = this
        tweens.add(tween);
    }

    //把Tween添加到 删除池里，下一帧删除
    fun removeTween(tween: Tween) {
        tweensToDelete.add(tween)
    }


    fun destroy() {
        tweens.forEach {
            it.remove()
        }
        tweensToDelete.clear()
        tweens.clear()
//        mAnimationThread = null
        mTweenAnimationHandler?.destroy()
        mTweenAnimationHandler = null
    }

}