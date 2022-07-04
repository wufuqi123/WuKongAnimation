package com.example.hehe.uikt

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hehe.R
import com.example.hehe.adapter.TweenSlowFunctionAdapter
import com.wukonganimation.tween.Easing
import com.wukonganimation.tween.Tween

class TweenSlowFunctionActivityKt : Activity() {

    var mTween: Tween? = null

    lateinit var mRunView: View
    lateinit var mIvImg: AppCompatImageView
    var x = 0f
    var y = 0f


    lateinit var mAdapter: TweenSlowFunctionAdapter

    private val mDatas: MutableList<TweenSlowFunctionAdapter.TweenSlowFunctionData> = mutableListOf(
        TweenSlowFunctionAdapter.TweenSlowFunctionData("linear", Easing.linear(), R.mipmap.linear),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inQuad", Easing.inQuad(), R.mipmap.in_quad),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outQuad",
            Easing.outQuad(),
            R.mipmap.out_quad
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutQuad",
            Easing.inOutQuad(),
            R.mipmap.in_out_quad
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inCubic",
            Easing.inCubic(),
            R.mipmap.in_cubic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outCubic",
            Easing.outCubic(),
            R.mipmap.out_cubic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutCubic",
            Easing.inOutCubic(),
            R.mipmap.in_out_cubic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inQuart",
            Easing.inQuart(),
            R.mipmap.in_quart
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outQuart",
            Easing.outQuart(),
            R.mipmap.out_quart
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutQuart",
            Easing.inOutQuart(),
            R.mipmap.in_out_quart
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inQuint",
            Easing.inQuint(),
            R.mipmap.in_quint
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outQuint",
            Easing.outQuint(),
            R.mipmap.out_quint
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutQuint",
            Easing.inOutQuint(),
            R.mipmap.in_out_quint
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inSine", Easing.inSine(), R.mipmap.in_sine),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outSine",
            Easing.outSine(),
            R.mipmap.out_sine
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutSine",
            Easing.inOutSine(),
            R.mipmap.in_out_sine
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inExpo", Easing.inExpo(), R.mipmap.in_expo),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outExpo",
            Easing.outExpo(),
            R.mipmap.out_expo
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutExpo",
            Easing.inOutExpo(),
            R.mipmap.in_out_expo
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inCirc", Easing.inCirc(), R.mipmap.in_circ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outCirc",
            Easing.outCirc(),
            R.mipmap.out_circ
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutCirc",
            Easing.inOutCirc(),
            R.mipmap.in_out_circ
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inElastic",
            Easing.inElastic(),
            R.mipmap.in_elastic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outElastic",
            Easing.outElastic(),
            R.mipmap.out_elastic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutElastic",
            Easing.inOutElastic(),
            R.mipmap.in_out_elastic
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData("inBack", Easing.inBack(), R.mipmap.in_back),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outBack",
            Easing.outBack(),
            R.mipmap.out_back
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutBack",
            Easing.inOutBack(),
            R.mipmap.in_out_back
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inBounce",
            Easing.inBounce(),
            R.mipmap.in_bounce
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "outBounce",
            Easing.outBounce(),
            R.mipmap.out_bounce
        ),
        TweenSlowFunctionAdapter.TweenSlowFunctionData(
            "inOutBounce",
            Easing.inOutBounce(),
            R.mipmap.in_out_bounce
        ),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween_slow_function)
        mRunView = findViewById(R.id.tv_run)
        mIvImg = findViewById(R.id.iv_img)
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


        mAdapter.clickCallback = {
            mIvImg.setImageResource(it.easingImg)
        }

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = mAdapter

        rv.layoutManager = GridLayoutManager(applicationContext, 4)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTween?.remove()
    }
}