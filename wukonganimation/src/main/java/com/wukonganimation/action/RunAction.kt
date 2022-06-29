package com.wukonganimation.action

import android.app.Activity
import android.content.Context
import android.view.View

/**
 * 执行action 动画
 */
object RunAction {

    /**
     * view 对应的Action集合
     */
    private val mViewActionMap = mutableMapOf<Context, MutableMap<View, ActionSet>>()

    /**
     * 运行动画
     * @param view 要执行的动画view
     * @param action Action.fadeIn(500) 等action 动画
     */
    fun runAction(view: View, action: () -> ActionData) {
        if (!ActionManager.isInit()) {
            throw Error("请先调用  ActionManager.init(application),否则无法开始执行动画！")
        }

        if (view.context == null) {
            throw Error("当前 ${view::class.java.name} 的 context 为空！无法执行动画！")
        }
        stopAction(view)
        val mActionSet = ActionSet(view, action)
        var map: MutableMap<View, ActionSet>? = mViewActionMap[view.context]
        if (map == null) {
            map = mutableMapOf()
            mViewActionMap[view.context] = map
        }
        map[view] = mActionSet
        mActionSet.on(ActionSet.EVENT_END) {
            map.remove(view)
            if (map.isEmpty()) {
                mViewActionMap.remove(view.context)
            }
        }
        mActionSet.start()
    }

    /**
     * 停止动画
     *
     * @param view 要停止的动画view
     */
    fun stopAction(view: View) {
        val map = mViewActionMap[view.context]
        map?.get(view)?.stop()
        map?.remove(view)
        if (map.isNullOrEmpty()) {
            mViewActionMap.remove(view.context)
        }
    }

    /**
     * 停止action下的所有动画
     */
    fun stopAction(activity: Activity) {
        val map = mViewActionMap[activity]
        map?.forEach {
            it.value.stop()
        }
        map?.clear()
        mViewActionMap.remove(activity)
    }


}

