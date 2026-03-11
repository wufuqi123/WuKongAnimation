package com.wukonganimation.tween

import android.os.Looper
import java.util.concurrent.ConcurrentLinkedQueue

class TweenManager {

    //Tween任务池
    private val tweens = ArrayList<Tween>()

    //删除池
    private val tweensToDelete = ArrayList<Tween>()

    //添加池，意味着下一帧添加，避免java.util.ConcurrentModificationException异常
    private val tweensToAdd = ArrayList<Tween>()

    private val pendingOperations = ConcurrentLinkedQueue<() -> Unit>()

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


        private val instance by lazy {
            TweenManager()
        }

        /**
         * 创建一个可以重复使用的tween
         * 最后调用  .remove() 销毁tween
         */
        fun builder(target: Any): Tween {
            return instance.createTween(target)
        }

        /**
         * 创建使用一次的tween
         */
        fun builderOne(target: Any): Tween {
            return instance.createTween(target).setExpire(true)
        }

        /**
         * 是否暂停
         */
        @Volatile
        var isPause = false
            private set


        /**
         * 动画速度
         */
        @Volatile
        var speed = 1.0

        /**
         * 暂停动画
         */
        fun pause() {
            isPause = true
        }

        /**
         * 恢复动画
         */
        fun resume() {
            isPause = false
        }

    }


    internal fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    internal fun enqueueOperation(operation: () -> Unit) {
        if (isMainThread()) {
            operation.invoke()
            return
        }
        pendingOperations.add(operation)
        runAnimation()
    }

    private fun drainPendingOperations() {
        while (true) {
            val operation = pendingOperations.poll() ?: break
            operation.invoke()
        }
    }

    /**
     * 当前是否还有任务
     */
    fun isEmpty(): Boolean {
        return tweens.isEmpty() && tweensToDelete.isEmpty() && tweensToAdd.isEmpty() && pendingOperations.isEmpty()
    }

    fun upDate(dtl: Long) {
        drainPendingOperations()
        if (isPause) {
            //如果暂停不执行动画
            return
        }
        val dt = dtl * speed
        if (tweensToAdd.isNotEmpty()) {
            tweens.addAll(tweensToAdd)
            tweensToAdd.clear()
        }

        var index = 0
        while (index < tweens.size) {
            val tween = tweens[index]
            if (tween.active) {
                tween.update(dt)
            }
            if (tween.isEnded && tween.expire) {
                tween.remove()
            }
            index++
        }

        if (tweensToDelete.isNotEmpty()) {
            compactDeletedTweens()
        }
    }

    private fun compactDeletedTweens() {
        var writeIndex = 0
        var readIndex = 0
        while (readIndex < tweens.size) {
            val tween = tweens[readIndex]
            if (!tween.pendingDeletion) {
                if (writeIndex != readIndex) {
                    tweens[writeIndex] = tween
                }
                writeIndex++
            } else {
                tween.pendingDeletion = false
                if (tween.manager === this) {
                    tween.manager = null
                }
            }
            readIndex++
        }
        if (writeIndex < tweens.size) {
            tweens.subList(writeIndex, tweens.size).clear()
        }
        tweensToDelete.clear()
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
        var index = 0
        while (index < this.tweens.size) {
            val tween = this.tweens[index]
            if (tween.target == target) {
                tweens.add(tween)
            }
            index++
        }
        return tweens
    }

    /**
     * 创建一个跟TweenManager 绑定的Tween
     */
    fun createTween(target: Any): Tween {
        return Tween(target, this)
    }

    //把Tween 添加到 任务池里
    fun addTween(tween: Tween) {
        enqueueOperation {
            if (tweens.contains(tween) || tweensToAdd.contains(tween)) {
                tween.pendingDeletion = false
                tween.manager = this
                return@enqueueOperation
            }
            tween.pendingDeletion = false
            tweensToDelete.remove(tween)
            tween.manager = this
            tweensToAdd.add(tween)
        }
    }

    //把Tween添加到 删除池里，下一帧删除
    fun removeTween(tween: Tween) {
        enqueueOperation {
            val removedFromAdd = tweensToAdd.remove(tween)
            if (removedFromAdd) {
                tween.pendingDeletion = false
                if (tween.manager === this) {
                    tween.manager = null
                }
                return@enqueueOperation
            }
            if (!tween.pendingDeletion) {
                tween.pendingDeletion = true
                tweensToDelete.add(tween)
            }
        }
    }


    private fun destroyInternal() {
        tweensToAdd.clear()
        pendingOperations.clear()
        var index = 0
        while (index < tweens.size) {
            tweens[index].remove()
            index++
        }
        tweensToDelete.clear()
        tweens.clear()
//        mAnimationThread = null
        mTweenAnimationHandler?.destroy()
        mTweenAnimationHandler = null
    }

    fun destroy() {
        if (isMainThread()) {
            destroyInternal()
        } else {
            mTweenAnimationHandler?.postToMain {
                destroyInternal()
            }
        }
    }

}