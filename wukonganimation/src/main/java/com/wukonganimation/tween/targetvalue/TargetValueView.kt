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
}