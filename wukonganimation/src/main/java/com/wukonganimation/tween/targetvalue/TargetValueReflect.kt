package com.wukonganimation.tween.targetvalue

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 反射属性设置器
 */
class TargetValueReflect(private val target: Any) : TargetValueAbstract() {
    private var targetClass: Class<*> = target::class.java
    private val mTargetFieldMap = mutableMapOf<String, Field>()
    private val mTargetMethodMap = mutableMapOf<String, Method>()


    override fun setTargetValue(fieldName: String, value: Double) {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $value")
        }
        val field = getTargetField(targetClass, fieldName)
        if (field != null) {
            when (field.type) {
                Double::class.java -> field.set(target, value)
                Float::class.java -> field.set(target, value.toFloat())
                Long::class.java -> field.set(target, value.toLong())
                Int::class.java -> field.set(target, value.toInt())
                Char::class.java -> field.set(target, value.toChar())
                Short::class.java -> field.set(target, value.toInt().toShort())
                Byte::class.java -> field.set(target, value.toInt().toByte())
            }

        } else {
            val methodName =
                "set${fieldName[0].toUpperCase() + fieldName.substring(1, fieldName.length)}"
            val method = getTargetMethod(targetClass, methodName)
            if (method != null) {
                if (method.genericParameterTypes.isNotEmpty()) {
                    when (method.genericParameterTypes[0]) {
                        Double::class.java -> method.invoke(target, value)
                        Float::class.java -> method.invoke(target, value.toFloat())
                        Long::class.java -> method.invoke(target, value.toLong())
                        Int::class.java -> method.invoke(target, value.toInt())
                        Char::class.java -> method.invoke(target, value.toChar())
                        Short::class.java -> method.invoke(target, value.toInt().toShort())
                        Byte::class.java -> method.invoke(target, value.toInt().toByte())
                    }

                }
            } else {
                throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
            }
        }


    }

    override fun getTargetValue(fieldName: String, toValue: Double): Number {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $toValue")
        }

        val field = getTargetField(targetClass, fieldName)
        if (field != null) {
            val value = field.get(target)
            if (value is Number) {
                return value
            } else {
                throw Error("${target::class.java.name} $fieldName 属性 类型不是number")
            }
        } else {
            val methodName =
                "get${fieldName[0].toUpperCase() + fieldName.substring(1, fieldName.length)}"
            val method = getTargetMethod(targetClass, methodName)
            if (method != null) {
                val value = method.invoke(target)
                if (value is Number) {
                    return value
                } else {
                    throw Error("${target::class.java.name} $methodName 方法 获取的类型不是number")
                }
            } else {
                throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
            }

        }
    }



    private fun getTargetField(targetClass: Class<*>, name: String): Field? {
        if (mTargetFieldMap[name] != null) {
            return mTargetFieldMap[name]
        }
        try {
            val field = targetClass.getField(name)
            mTargetFieldMap[name] = field
            field.isAccessible = true
            return field
        } catch (e: Exception) {
            if (targetClass.superclass != null) {
                return getTargetField(targetClass.superclass!!, name)
            }
        }
        return null
    }

    private fun getTargetMethod(targetClass: Class<*>, name: String): Method? {
        if (mTargetMethodMap[name] != null) {
            return mTargetMethodMap[name]
        }
        targetClass.methods.forEach {
            if (it.name == name) {
                mTargetMethodMap[name] = it
                it.isAccessible = true
                return it
            }
        }
        if (targetClass.superclass != null) {
            return getTargetMethod(targetClass.superclass!!, name)
        }
        return null
    }

    override fun clear() {
        super.clear()
        mTargetMethodMap.clear()
        mTargetFieldMap.clear()
    }
}