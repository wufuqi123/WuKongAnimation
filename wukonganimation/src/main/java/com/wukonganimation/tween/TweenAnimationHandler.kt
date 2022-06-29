package com.wukonganimation.tween

import android.animation.ValueAnimator
import androidx.core.animation.addListener

class TweenAnimationHandler {



    @Volatile
    private var startTime: Long = 0

    private var mTweenManager: TweenManager

    private var mValueAnimator: ValueAnimator? = null

    constructor(tweenManager: TweenManager) {
        mTweenManager = tweenManager
    }

    private fun update(dt: Long) {
        mTweenManager.upDate(dt)
    }

    fun start() {

        if (mValueAnimator != null) {
            return
        }
        mValueAnimator = ValueAnimator.ofInt(0)
        mValueAnimator?.addListener {
            mValueAnimator = null
            start()
        }
        mValueAnimator?.duration = Long.MAX_VALUE
        mValueAnimator?.addUpdateListener {
            if (mTweenManager.isEmpty()) {
                mValueAnimator?.cancel()
                mValueAnimator = null
                startTime = 0
                return@addUpdateListener
            }
            update(it.currentPlayTime - startTime)
            startTime = it.currentPlayTime
        }
        mValueAnimator?.start()
    }

    fun destroy() {
        mValueAnimator?.cancel()
        mValueAnimator = null
    }
}