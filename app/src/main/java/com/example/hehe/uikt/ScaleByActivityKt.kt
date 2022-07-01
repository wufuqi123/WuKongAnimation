package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class ScaleByActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val scaleX = 2f
    private val scaleY = 2f

    override fun getDescribe() = "view 从2倍 缩放偏移2倍"

    override fun getResetBtnText() = "缩放"


    override fun initRunView(runView: View) {
        super.initRunView(runView)
        runView.scaleX = 2f
        runView.scaleY = 2f
    }

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(
                mutableMapOf(
                    "scaleX" to runView.scaleX + scaleX,
                    "scaleY" to runView.scaleY + scaleY
                )
            )
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.scaleBy(time, scaleX, scaleY))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .scaleBy(time, scaleX, scaleY)
            .start()
    }


}