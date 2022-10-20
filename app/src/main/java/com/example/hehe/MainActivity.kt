package com.example.hehe

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hehe.adapter.MainRecyclerAdapter
import com.example.hehe.ui.*
import com.example.hehe.uikt.*


class MainActivity : AppCompatActivity() {

    private val mButtonTexts = mutableListOf<String>().apply {
        add("fadeTo")
        add("fadeIn")
        add("fadeOut")
        add("moveTo")
        add("moveBy")
        add("scaleTo")
        add("scaleBy")
        add("rotateTo")
        add("rotateBy")
        add("rotateXTo")
        add("rotateXBy")
        add("rotateYTo")
        add("rotateYBy")
        add("sequence")
        add("callback")
        add("wait")
        add("spawn")
        add("tween-loop")
        add("tween-PingPong")
        add("tween-event")
        add("tween-Slow function")
        add("TweenManager")
    }

    private val mActivityKTClassMap = mutableMapOf<String, Class<*>>().apply {
//        set(mButtonTexts[0], TestErrorActivity::class.java)
        set(mButtonTexts[0], FadeToActivityKt::class.java)
        set(mButtonTexts[1], FadeInActivityKt::class.java)
        set(mButtonTexts[2], FadeOutActivityKt::class.java)
        set(mButtonTexts[3], MoveToActivityKt::class.java)
        set(mButtonTexts[4], MoveByActivityKt::class.java)
        set(mButtonTexts[5], ScaleToActivityKt::class.java)
        set(mButtonTexts[6], ScaleByActivityKt::class.java)
        set(mButtonTexts[7], RotateToActivityKt::class.java)
        set(mButtonTexts[8], RotateByActivityKt::class.java)
        set(mButtonTexts[9], RotateXToActivityKt::class.java)
        set(mButtonTexts[10], RotateXByActivityKt::class.java)
        set(mButtonTexts[11], RotateYToActivityKt::class.java)
        set(mButtonTexts[12], RotateYByActivityKt::class.java)
        set(mButtonTexts[13], SequenceActivityKt::class.java)
        set(mButtonTexts[14], CallbackActivityKt::class.java)
        set(mButtonTexts[15], WaitActivityKt::class.java)
        set(mButtonTexts[16], SpawnActivityKt::class.java)
        set(mButtonTexts[17], TweenLoopActivityKt::class.java)
        set(mButtonTexts[18], TweenPingPongActivityKt::class.java)
        set(mButtonTexts[19], TweenEventActivityKt::class.java)
        set(mButtonTexts[20], TweenSlowFunctionActivityKt::class.java)
        set(mButtonTexts[21], TweenManagerActivityKt::class.java)
    }

    private val mActivityClassMap = mutableMapOf<String, Class<*>>().apply {
        set(mButtonTexts[0], FadeToActivity::class.java)
        set(mButtonTexts[1], FadeInActivity::class.java)
        set(mButtonTexts[2], FadeOutActivity::class.java)
        set(mButtonTexts[3], MoveToActivity::class.java)
        set(mButtonTexts[4], MoveByActivity::class.java)
        set(mButtonTexts[5], ScaleToActivity::class.java)
        set(mButtonTexts[6], ScaleByActivity::class.java)
        set(mButtonTexts[7], RotateToActivity::class.java)
        set(mButtonTexts[8], RotateByActivity::class.java)
        set(mButtonTexts[9], RotateXToActivity::class.java)
        set(mButtonTexts[10], RotateXByActivity::class.java)
        set(mButtonTexts[11], RotateYToActivity::class.java)
        set(mButtonTexts[12], RotateYByActivity::class.java)
        set(mButtonTexts[13], SequenceActivity::class.java)
        set(mButtonTexts[14], CallbackActivity::class.java)
        set(mButtonTexts[15], WaitActivity::class.java)
        set(mButtonTexts[16], SpawnActivity::class.java)
        set(mButtonTexts[17], TweenLoopActivityKt::class.java)
        set(mButtonTexts[18], TweenPingPongActivityKt::class.java)
        set(mButtonTexts[19], TweenEventActivityKt::class.java)
        set(mButtonTexts[20], TweenSlowFunctionActivityKt::class.java)
        set(mButtonTexts[21], TweenManagerActivityKt::class.java)
    }

    lateinit var mAdapter: MainRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rg = findViewById<RadioGroup>(R.id.rg)
        rg.setOnCheckedChangeListener { group, checkedId ->
            val radioButtonChecked = group.findViewById<RadioButton>(checkedId)
            mAdapter.setIsClickKtActivity(radioButtonChecked.text == "kotlin")
        }


        mAdapter =
            MainRecyclerAdapter(this, mButtonTexts, mActivityClassMap, mActivityKTClassMap, true)


        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = mAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager


    }


}