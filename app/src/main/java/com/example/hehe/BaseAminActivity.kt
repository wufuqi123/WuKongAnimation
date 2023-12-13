package com.example.hehe

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.wukonganimation.action.extend.isRunningAction
import com.wukonganimation.action.extend.stopAction
import com.wukonganimation.tween.Tween

open abstract class BaseAminActivity : Activity() {

    /**
     * 准备运行动画的view
     */
    private lateinit var mRunView: View

    private var mTween: Tween? = null

    private var mViewAminProperty: ViewAminProperty? = null

    private var defaultDescribe = ""



    init {
        defaultDescribe += "tween停止动画:"
        defaultDescribe += "使用tween.stop(),销毁使用tween.remove()。"
        defaultDescribe += "在执行动画时，界面销毁会造成执行动画的对象得不到释放，会造成内存泄露，请在界面销毁时使用tween.remove()来释放动画。"
        defaultDescribe += "如果动画为一次性，调用tween.setExpire(true)则动画停止后自动释放。"
        defaultDescribe += "\naction动画和链式动画停止："
        defaultDescribe += "kotlin调用view.stopAction()。"
        defaultDescribe += "java调用 RunAction.stopAction(view)。"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amin_layout)

        val className = this::class.java.name

        val name = className.substring(className.lastIndexOf(".") + 1, className.length)

        val ktStrIndex = name.lastIndexOf("Kt")

        val languageName =
            if (ktStrIndex != -1 && ktStrIndex == name.length - 2) "kotlin" else "java"

        val aminName = if (languageName == "kotlin") name.substring(0, ktStrIndex) else name

        findViewById<TextView>(R.id.tv_title).text = "$languageName $aminName"

        val tvIsRun = findViewById<TextView>(R.id.tv_is_run)
        mRunView = findViewById(R.id.tv_run)

        initRunView(mRunView)

        mRunView.post {
            mViewAminProperty = ViewAminProperty(
                mRunView.x,
                mRunView.y,
                mRunView.alpha,
                mRunView.rotation,
                mRunView.rotationX,
                mRunView.rotationY,
                mRunView.scaleX,
                mRunView.scaleY
            )
        }


        findViewById<TextView>(R.id.tv_describe).text = defaultDescribe + "\n" + getDescribe()
        findViewById<AppCompatButton>(R.id.btn_reset).text = "停止动画并且重置" + getResetBtnText()

        findViewById<View>(R.id.btn_tween).setOnClickListener {
            removeTween()
            mTween = clickTweenBtn(mRunView)
        }

        findViewById<View>(R.id.btn_action).setOnClickListener {
            removeTween()
            clickActionBtn(mRunView)
        }

        findViewById<View>(R.id.btn_chained).setOnClickListener {
            removeTween()
            clickChainedBtn(mRunView)
        }

        findViewById<View>(R.id.btn_is_run).setOnClickListener {
            var isRun = false
            if(mTween!=null){
                //说明正在执行  tween动画
                isRun = mTween!!.isRunning
            }
            if(!isRun){
                //不在执行tween动画检测是否在执行   Action 动画
                isRun = mRunView.isRunningAction()
            }

            tvIsRun.text = "状态：${if (isRun)"运行" else "停止"}"
        }


        findViewById<View>(R.id.btn_reset).setOnClickListener {
            removeTween()
            mRunView.stopAction()
            mViewAminProperty?.let {
                mRunView.x = it.x
                mRunView.y = it.y
                mRunView.alpha = it.alpha
                mRunView.rotation = it.rotation
                mRunView.rotationX = it.rotationX
                mRunView.rotationY = it.rotationY
                mRunView.scaleX = it.scaleX
                mRunView.scaleY = it.scaleY
            }

        }


    }

    open fun initRunView(runView: View){

    }


    /**
     * 设置描述文字
     */
    abstract fun getDescribe(): String

    /**
     * 设置重置按钮文字
     */
    abstract fun getResetBtnText(): String


    /**
     * 点击 tween动画 按钮
     */
    abstract fun clickTweenBtn(runView: View): Tween


    /**
     * 点击 runAction动画 按钮
     */
    abstract fun clickActionBtn(runView: View)


    /**
     * 点击 链式动画 按钮
     */
    abstract fun clickChainedBtn(runView: View)


    private fun removeTween() {
        mTween?.stop()?.remove()
        mTween = null
    }


    override fun onDestroy() {
        super.onDestroy()
        removeTween()
    }


    /**
     * view action 动画能改变的属性
     */
    data class ViewAminProperty(
        val x: Float,
        val y: Float,
        val alpha: Float,
        val rotation: Float,
        val rotationX: Float,
        val rotationY: Float,
        val scaleX: Float,
        val scaleY: Float
    )
}