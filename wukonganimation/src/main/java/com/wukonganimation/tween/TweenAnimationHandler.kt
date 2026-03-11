package com.wukonganimation.tween


import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Choreographer

class TweenAnimationHandler(var mTweenManager: TweenManager) {

    private var lastFrameTimeNanos: Long = 0L

    private var isDestroy = false

    @Volatile
    private var isStart = false

    @Volatile
    private var isStartPosted = false

    private val mDestroyMessage = Object()

    private val mDestroyWhat = 9999

    private val mHandler = Handler(Looper.getMainLooper()) {
        if (it.what == mDestroyWhat && it.obj == mDestroyMessage) {
            //为了让isDestroy  在主线程执行，避免2个线程争抢资源
            isDestroy = true
        }
        return@Handler false
    }

    private val doFrame = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (isDestroy) {
                isStart = false
                isStartPosted = false
                return
            }
            val dt = if (lastFrameTimeNanos == 0L) 0L else (frameTimeNanos - lastFrameTimeNanos) / 1_000_000L
            lastFrameTimeNanos = frameTimeNanos
            update(dt)
            if (this@TweenAnimationHandler.mTweenManager.isEmpty() || isDestroy) {
                isStart = false
                isStartPosted = false
                lastFrameTimeNanos = 0L
                return
            }
            Choreographer.getInstance().postFrameCallback(this)
        }

    }

    private fun update(dt: Long) {
        mTweenManager.upDate(dt)
    }

    fun postToMain(action: () -> Unit) {
        mHandler.post(action)
    }

    fun start() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (isDestroy || isStart) {
                return
            }
            isStart = true
            isStartPosted = false
            lastFrameTimeNanos = 0L
            Choreographer.getInstance().postFrameCallback(doFrame)
            return
        }
        if (isDestroy || isStart || isStartPosted) {
            return
        }
        isStartPosted = true
        postToMain {
            isStartPosted = false
            if (isDestroy || isStart) {
                return@postToMain
            }
            isStart = true
            lastFrameTimeNanos = 0L
            Choreographer.getInstance().postFrameCallback(doFrame)
        }
    }

    fun destroy() {
        isStart = false
        isStartPosted = false
        lastFrameTimeNanos = 0L
        val message = Message()
        message.what = mDestroyWhat
        message.obj = mDestroyMessage
        //把销毁消息放到链表最前面，保证能第一个执行销毁消息。
        mHandler.sendMessageAtTime(message, 0)
    }
}