package com.wukonganimation.tween.targetvalue

abstract class TargetValueAbstract {
    private val changeFieldNameSet = mutableSetOf<String>()

    interface BoundPropertyAccessor {
        fun set(value: Double)
        fun get(toValue: Double): Number
    }

    interface FloatBoundPropertyAccessor : BoundPropertyAccessor {
        fun setFloat(value: Float)
        fun getFloat(toValue: Float): Float

        override fun set(value: Double) {
            setFloat(value.toFloat())
        }

        override fun get(toValue: Double): Number {
            return getFloat(toValue.toFloat())
        }
    }

    /**
     * 添加更改的属性名
     */
    fun addChangeFieldName(name: String) {
        changeFieldNameSet.add(name)
    }

    /**
     * 是否包含更改的属性名
     */
    fun containsChangeFieldName(name: String): Boolean {
        return changeFieldNameSet.contains(name)
    }

    open fun bindProperty(fieldName: String): BoundPropertyAccessor {
        return object : BoundPropertyAccessor {
            override fun set(value: Double) {
                setTargetValue(fieldName, value)
            }

            override fun get(toValue: Double): Number {
                return getTargetValue(fieldName, toValue)
            }
        }
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