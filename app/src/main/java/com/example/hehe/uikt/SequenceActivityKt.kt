package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createSequenceAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class SequenceActivityKt : BaseAminActivity() {
    private val time = 1000L

    override fun getDescribe() = "view 顺序动画，fadeIn + fadeOut"

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
            .on(TweenManager.EVENT_END) {
                //使用TweenManager.builderOne  默认调用了setExpire(true)
                TweenManager.builderOne(runView)
                    .to(mutableMapOf("alpha" to 0f))
                    .time(time)
                    .start()
            }
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.sequence(Action.fadeIn(time), Action.fadeOut(time)))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createSequenceAction()
            .fadeIn(time)
            .fadeOut(time)
            .start()
    }
}