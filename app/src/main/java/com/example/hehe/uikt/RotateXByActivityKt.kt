package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class RotateXByActivityKt : BaseAminActivity() {

    /**
     * 执行动画时间
     */
    private val time = 1000L

    private val rotationX = 180f

    override fun getDescribe() = "view  x角度  从180度 旋转偏移180度"

    override fun getResetBtnText() = "旋转"


    override fun initRunView(runView: View) {
        super.initRunView(runView)
        runView.rotationX = 180f
    }

    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(mutableMapOf("rotationX" to runView.rotationX + rotationX))
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(Action.rotateXBy(time, rotationX))
    }

    override fun clickChainedBtn(runView: View) {
        runView.createAction()
            .rotateXBy(time, rotationX)
            .start()
    }


}