package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class MoveToActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val x = 100f
    private val y = 100f

    override fun getDescribe() = "view 移动到左上角100像素"

    override fun getResetBtnText() = "位置"

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("x" to x, "y" to y))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.moveTo(time, x, y))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .moveTo(time, x, y)
            .start()
    }


}