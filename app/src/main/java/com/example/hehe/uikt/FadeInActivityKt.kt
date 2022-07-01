package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class FadeInActivityKt: BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L



    override fun getDescribe() = "view 透明度从0执行动画到 1"

    override fun getResetBtnText() = "alpha"


    override fun initRunView(runView: View) {
        super.initRunView(runView)
        runView.alpha = 0f
    }


    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("alpha" to 1))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.fadeIn(time))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .fadeIn(time)
            .start()
    }
}