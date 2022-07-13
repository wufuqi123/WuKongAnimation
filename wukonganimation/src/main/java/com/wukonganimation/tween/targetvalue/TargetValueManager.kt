package com.wukonganimation.tween.targetvalue

import android.view.View

/**
 * 管理设置属性和获取属性的类
 *
 * 属性设置器为了尽量优化代码，让设置属性和获取属性尽量不走反射，优化动画运行速度
 */
object TargetValueManager {


    private val mTargetValues = mutableListOf<(target: Any) -> TargetValueAbstract?>()

    /**
     * 根据target  创建一个属性设置器
     */
    fun createTargetValue(target: Any): TargetValueAbstract {
        mTargetValues.forEach {
            val tv = it.invoke(target)
            if (tv != null) {
                return tv
            }
        }
        if (target is View) {
            return TargetValueView(target)
        }
        return TargetValueReflect(target)
    }

    /**
     * 添加属性设置器
     */
    fun addTargetValue(tv: (target: Any) -> TargetValueAbstract?) {
        mTargetValues.add(tv)
    }


}