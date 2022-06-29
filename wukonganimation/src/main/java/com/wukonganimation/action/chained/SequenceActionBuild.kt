package com.wukonganimation.action.chained

class SequenceActionBuild : AbstractActionBuild<SequenceActionBuild>() {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SEQUENCE
}