package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class RotateYToActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val rotationY = 180f

    override fun getDescribe() = "view Y角度  旋转180度"

    override fun getResetBtnText() = "旋转"

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("rotationY" to rotationY))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.rotateYTo(time, rotationY))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .rotateYTo(time, rotationY)
            .start()
    }


}