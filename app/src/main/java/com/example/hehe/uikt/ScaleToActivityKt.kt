package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class ScaleToActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val scaleX = 2f
    private val scaleY = 2f

    override fun getDescribe() = "view 缩放到2倍"

    override fun getResetBtnText() = "缩放"

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("scaleX" to scaleX, "scaleY" to scaleY))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.scaleTo(time, scaleX, scaleY))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .scaleTo(time, scaleX, scaleY)
            .start()
    }


}