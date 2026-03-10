package com.wukonganimation.tween.targetvalue

import android.view.View

/**
 * view 属性设置器
 */
class TargetValueView(private val target: View) : TargetValueAbstract() {

    companion object {
        /**
         * 可以更改的属性
         */
        private val VIEW_FIELD_NAME = mutableSetOf(
            "x",
            "y",
            "scaleX",
            "scaleY",
            "rotation",
            "rotationX",
            "rotationY",
            "alpha"
        )
    }

    /**
     * 可以更改的属性
     */
    private val mMayChangeFieldNames = mutableSetOf<String>()

    /**
     * 不能更改的属性
     */
    private val mNoChangeFieldNames = mutableSetOf<String>()

    private val mTargetValueReflect: TargetValueReflect = TargetValueReflect(target)

    private val xAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.x = value
        }

        override fun getFloat(toValue: Float): Float = target.x
    }

    private val yAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.y = value
        }

        override fun getFloat(toValue: Float): Float = target.y
    }

    private val scaleXAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.scaleX = value
        }

        override fun getFloat(toValue: Float): Float = target.scaleX
    }

    private val scaleYAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.scaleY = value
        }

        override fun getFloat(toValue: Float): Float = target.scaleY
    }

    private val rotationAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.rotation = value
        }

        override fun getFloat(toValue: Float): Float = target.rotation
    }

    private val rotationXAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.rotationX = value
        }

        override fun getFloat(toValue: Float): Float = target.rotationX
    }

    private val rotationYAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.rotationY = value
        }

        override fun getFloat(toValue: Float): Float = target.rotationY
    }

    private val alphaAccessor = object : FloatBoundPropertyAccessor {
        override fun setFloat(value: Float) {
            target.alpha = value
        }

        override fun getFloat(toValue: Float): Float = target.alpha
    }

    /**
     * 判断是否为可以更改的属性
     */
    private fun isChangeField(fieldName: String): Boolean {
        val isMayChange = mMayChangeFieldNames.contains(fieldName)
        if (isMayChange) {
            return true
        }
        val isNoChange = mNoChangeFieldNames.contains(fieldName)
        if (isNoChange) {
            return false
        }
        if (VIEW_FIELD_NAME.contains(fieldName)) {
            mMayChangeFieldNames.add(fieldName)
            return true
        }
        mNoChangeFieldNames.add(fieldName)
        return false
    }

    override fun setTargetValue(fieldName: String, value: Double) {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $value")
        }
        if (!isChangeField(fieldName)) {
            mTargetValueReflect.setTargetValue(fieldName, value)
            return
        }
        when (fieldName) {
            "x" -> target.x = value.toFloat()
            "y" -> target.y = value.toFloat()
            "scaleX" -> target.scaleX = value.toFloat()
            "scaleY" -> target.scaleY = value.toFloat()
            "rotation" -> target.rotation = value.toFloat()
            "rotationX" -> target.rotationX = value.toFloat()
            "rotationY" -> target.rotationY = value.toFloat()
            "alpha" -> target.alpha = value.toFloat()
        }
    }

    override fun getTargetValue(fieldName: String, toValue: Double): Number {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $toValue")
        }
        if (!isChangeField(fieldName)) {
            return mTargetValueReflect.getTargetValue(fieldName, toValue)
        }
        return when (fieldName) {
            "x" -> target.x
            "y" -> target.y
            "scaleX" -> target.scaleX
            "scaleY" -> target.scaleY
            "rotation" -> target.rotation
            "rotationX" -> target.rotationX
            "rotationY" -> target.rotationY
            "alpha" -> target.alpha
            else -> 0
        }
    }

    override fun clear() {
        super.clear()
        mMayChangeFieldNames.clear()
        mNoChangeFieldNames.clear()
    }

    override fun bindProperty(fieldName: String): BoundPropertyAccessor {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: 0.0")
        }
        if (!isChangeField(fieldName)) {
            return mTargetValueReflect.bindProperty(fieldName)
        }
        return when (fieldName) {
            "x" -> xAccessor
            "y" -> yAccessor
            "scaleX" -> scaleXAccessor
            "scaleY" -> scaleYAccessor
            "rotation" -> rotationAccessor
            "rotationX" -> rotationXAccessor
            "rotationY" -> rotationYAccessor
            "alpha" -> alphaAccessor
            else -> mTargetValueReflect.bindProperty(fieldName)
        }
    }
}