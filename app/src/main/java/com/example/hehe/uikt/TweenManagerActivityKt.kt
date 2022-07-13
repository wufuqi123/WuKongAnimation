package com.example.hehe.uikt

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.hehe.R
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class TweenManagerActivityKt : Activity() {

    private lateinit var mTween: Tween

    private lateinit var mTvDescribe: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween_manager)
        findViewById<AppCompatTextView>(R.id.tv_title).text = "tween manager"
        mTvDescribe = findViewById(R.id.tv_describe)


        findViewById<View>(R.id.tv_run).post {
            mTween = TweenManager.builder(findViewById(R.id.tv_run))
                .to(mutableMapOf("x" to 100, "y" to 100))
                .time(2000)
                .repeat(-1)
                .pingPong(true)
                .start()
            setDescribeText()
        }
    }

    fun speed01(v: View) {
        TweenManager.speed = 0.1
        setDescribeText()
    }


    fun speed05(v: View) {
        TweenManager.speed = 0.5
        setDescribeText()
    }

    fun speed10(v: View) {
        TweenManager.speed = 1.0
        setDescribeText()
    }

    fun speed50(v: View) {
        TweenManager.speed = 5.0
        setDescribeText()
    }

    fun pause(v: View) {
        TweenManager.pause()
        setDescribeText()
    }

    fun resume(v: View) {
        TweenManager.resume()
        setDescribeText()
    }


    fun setDescribeText() {
        mTvDescribe.text = "当前速度：${TweenManager.speed}   是否暂停：${TweenManager.isPause}"
    }

    override fun onDestroy() {
        super.onDestroy()
        mTween.remove()
    }
}