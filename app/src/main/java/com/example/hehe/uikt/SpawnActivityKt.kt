package com.example.hehe.uikt

import android.view.View
import com.example.hehe.BaseAminActivity
import com.wukonganimation.action.Action
import com.wukonganimation.action.extend.createSpawnAction
import com.wukonganimation.action.extend.runAction
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager

class SpawnActivityKt : BaseAminActivity() {
    private val time = 1000L

    private val x = 100f
    private val y = 100f
    private val rotation = 360f

    override fun getDescribe() = "view 同步动画，moveBy + rotateBy"

    override fun getResetBtnText() = "同步动画"



    override fun clickTweenBtn(runView: View): Tween {
        //当前动画不会重复使用建议调用 setExpire(true)
        return TweenManager.builder(runView)
            .to(
                mutableMapOf(
                    "x" to runView.x + x,
                    "y" to runView.y + y,
                    "rotation" to runView.rotation + rotation
                )
            )
            .time(time)
            .setExpire(true)
            .start()
    }

    override fun clickActionBtn(runView: View) {
        runView.runAction(
            Action.spawn(
                Action.moveBy(time, x, y),
                Action.rotateBy(time, rotation)
            )
        )
    }

    override fun clickChainedBtn(runView: View) {
        runView.createSpawnAction()
            .moveBy(time, x, y)
            .rotateBy(time, rotation)
            .start()
    }
}