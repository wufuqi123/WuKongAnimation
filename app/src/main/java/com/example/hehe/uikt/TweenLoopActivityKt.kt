package com.example.hehe.uikt

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.hehe.R
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class TweenLoopActivityKt : Activity() {

    private lateinit var mTween: Tween


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween)
        findViewById<AppCompatTextView>(R.id.tv_title).text = "tween loop"

        mTween = TweenManager.builder(findViewById(R.id.tv_run))
            .to(mutableMapOf("x" to 100, "y" to 100))
            .time(1000)
            .repeat(-1)
    }


    fun start(v: View) {
        mTween.start()
    }

    fun stop(v: View) {
        mTween.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mTween.remove()
    }
}