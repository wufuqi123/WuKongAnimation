package com.wukonganimation.action.chained

import android.view.View
import com.wukonganimation.action.ActionData
import com.wukonganimation.action.RunAction

/**
 * 同步动画  可以执行动画的class
 */
class SpawnActionRunBuild : AbstractActionBuild<SpawnActionRunBuild> {

    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SPAWN

    private val component: View

    constructor(view: View) {
        component = view
    }

    fun start(): SpawnActionRunBuild {
        RunAction.runAction(component) {
            val animationData = ActionData()
            animationData.type = ActionData.spawn
            animationData.animationDataArray = animationDataArray
            animationData
        }
        return this
    }

    fun stop(): SpawnActionRunBuild {
        RunAction.stopAction(component)
        return this
    }
}