package com.wukonganimation.tween.targetvalue

abstract class TargetValueAbstract {
    private val changeFieldNameSet = mutableSetOf<String>()

    /**
     * 添加更改的属性名
     */
    fun addChangeFieldName(name: String) {
        changeFieldNameSet.add(name)
    }

    /**
     * 是否包含更改的属性名
     */
    fun containsChangeFieldName(name: String) {
        changeFieldNameSet.contains(name)
    }


    /**
     * 设置值
     */
    abstract fun setTargetValue(fieldName: String, value: Double)

    /**
     * 获取值
     */
    abstract fun getTargetValue(fieldName: String, toValue: Double): Number

    /**
     * 清理
     */
    open fun clear() {
        changeFieldNameSet.clear()
    }
}