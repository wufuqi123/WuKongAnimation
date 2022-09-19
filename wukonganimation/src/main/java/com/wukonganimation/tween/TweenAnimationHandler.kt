package com.wukonganimation.tween


import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.view.Choreographer
import java.util.*

class TweenAnimationHandler(var mTweenManager: TweenManager) {

    private var lastTime: Long = 0

    private var isDestroy = false

    @Volatile
    private var isStart = false

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
            val frameTime = SystemClock.uptimeMillis()
            val dt = frameTime - lastTime
            lastTime = frameTime
            if (this@TweenAnimationHandler.mTweenManager.isEmpty() || isDestroy) {
                isStart = false
                return
            }
            Choreographer.getInstance().postFrameCallback(this)
            update(dt)
        }

    }

    private fun update(dt: Long) {
        mTweenManager.upDate(dt)
    }

    fun start() {
        if (isStart) {
            return
        }
        isStart = true
        mHandler.post {
            //保证在主线程执行
            if (isDestroy) {
                return@post
            }
            lastTime = SystemClock.uptimeMillis()
            Choreographer.getInstance().postFrameCallback(doFrame)
        }
    }

    fun destroy() {
        isStart = false
        val message = Message()
        message.what = mDestroyWhat
        message.obj = mDestroyMessage
        //把销毁消息放到链表最前面，保证能第一个执行销毁消息。
        mHandler.sendMessageAtTime(message, 0)
    }
}