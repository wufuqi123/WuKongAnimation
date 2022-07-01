package com.example.hehe.uikt

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hehe.R
import com.example.hehe.adapter.TweenSlowFunctionAdapter
import com.wukonganimation.tween.Easing
import com.wukonganimation.tween.Tween

class TweenSlowFunctionActivityKt : Activity() {

    var mTween: Tween? = null

    lateinit var mRunView: View
    var x = 0f
    var y = 0f


    lateinit var mAdapter: TweenSlowFunctionAdapter

    private val mDatas: MutableList<TweenSlowFunctionAdapter.TweenSlowFunctionData> = mutableListOf(
        TweenSlowFunctionAdapter.TweenSlowFunctionData("linear", Easing.linear()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inQuad", Easing.inQuad()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outQuad", Easing.outQuad()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutQuad", Easing.inOutQuad()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inCubic", Easing.inCubic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outCubic", Easing.outCubic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutCubic", Easing.inOutCubic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inQuart", Easing.inQuart()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outQuart", Easing.outQuart()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutQuart", Easing.inOutQuart()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inQuint", Easing.inQuint()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outQuint", Easing.outQuint()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutQuint", Easing.inOutQuint()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inSine", Easing.inSine()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outSine", Easing.outSine()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutSine", Easing.inOutSine()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inExpo", Easing.inExpo()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outExpo", Easing.outExpo()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutExpo", Easing.inOutExpo()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inCirc", Easing.inCirc()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outCirc", Easing.outCirc()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutCirc", Easing.inOutCirc()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inElastic", Easing.inElastic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outElastic", Easing.outElastic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutElastic", Easing.inOutElastic()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inBack", Easing.inBack()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outBack", Easing.outBack()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutBack", Easing.inOutBack()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inBounce", Easing.inBounce()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("outBounce", Easing.outBounce()),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inOutBounce", Easing.inOutBounce()),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween_slow_function)
        mRunView = findViewById(R.id.tv_run)
        mRunView.post {
            x = mRunView.x
            y = mRunView.y
        }

        findViewById<View>(R.id.btn_reset).setOnClickListener {
            mTween?.remove()
            mRunView.x = x
            mRunView.y = y
        }

        mAdapter = TweenSlowFunctionAdapter(this, mDatas)


        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = mAdapter

        rv.layoutManager = GridLayoutManager(applicationContext, 4)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTween?.remove()
    }
}