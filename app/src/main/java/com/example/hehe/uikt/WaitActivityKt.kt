package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createSequenceAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class WaitActivityKt : BaseAminActivity() {
    private val time = 1000L

    private val wait = 1000L

    override fun getDescribe() = "view 等待" + wait + "毫秒后执行动画  fadeIn"

    override fun getResetBtnText() = "顺序动画"


    override fun initRunView(runView: View) {
        super.initRunView(runView)
        runView.alpha = 0f
    }

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("alpha" to 1f))
            .time(time)
            .setExpire(true)
            .delay(wait)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.sequence(Action.wait(wait),Action.fadeIn(time)))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createSequenceAction()
            .wait(wait)
            .fadeIn(time)
            .start()
    }
}