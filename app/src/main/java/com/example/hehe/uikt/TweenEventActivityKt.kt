package com.example.hehe.uikt

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.hehe.R
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class TweenEventActivityKt : Activity() {

    private lateinit var mTween: Tween


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween)
        findViewById<AppCompatTextView>(R.id.tv_title).text = "tween event"

        mTween = TweenManager.builder(findViewById(R.id.tv_run))
            .to(mutableMapOf("x" to 100, "y" to 100))
            .time(1000)
            .pingPong(true)
            .repeat(-1)
            .on(TweenManager.EVENT_START) {
                Log.i(this::class.java.name, "Tween start")
            }
            .on(TweenManager.EVENT_RESTART) {
                Log.i(this::class.java.name, "Tween restart")
            }
            .on(TweenManager.EVENT_UPDATE) {
                Log.i(this::class.java.name, "Tween update")
            }
            .on(TweenManager.EVENT_UPDATE) {
                Log.i(this::class.java.name, "Tween update")
            }
            .on(TweenManager.EVENT_ERPEAT) {
                Log.i(this::class.java.name, "Tween repeat")
            }
            .on(TweenManager.EVENT_END) {
                Log.i(this::class.java.name, "Tween end")
            }
            .on(TweenManager.EVENT_STOP) {
                Log.i(this::class.java.name, "Tween stop")
            }
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