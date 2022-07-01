package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class FadeToActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val alpha = 0.5f

    override fun getDescribe() = "view 透明度执行动画到 $alpha"

    override fun getResetBtnText() = "alpha"

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("alpha" to alpha))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.fadeTo(time, alpha))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .fadeTo(time, alpha)
            .start()
    }


}