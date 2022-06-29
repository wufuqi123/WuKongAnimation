package com.wukonganimation.action.chained

class SpawnActionBuild : AbstractActionBuild<SpawnActionBuild>() {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SPAWN
}