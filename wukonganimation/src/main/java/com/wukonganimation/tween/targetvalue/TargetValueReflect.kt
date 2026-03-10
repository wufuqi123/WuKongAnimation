package com.wukonganimation.tween.targetvalue

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * 反射属性设置器
 */
class TargetValueReflect(private val target: Any) : TargetValueAbstract() {
    private val targetClass: Class<*> = target::class.java

    override fun bindProperty(fieldName: String): BoundPropertyAccessor {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: 0.0")
        }
        val setAccessor = resolveSetAccessor(targetClass, fieldName)
        if (setAccessor === MissingSetAccessor) {
            val methodName = setterName(fieldName)
            throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
        }
        val getAccessor = resolveGetAccessor(targetClass, fieldName)
        return BoundReflectPropertyAccessor(target, fieldName, setAccessor, getAccessor)
    }

    override fun setTargetValue(fieldName: String, value: Double) {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $value")
        }
        val accessor = resolveSetAccessor(targetClass, fieldName)
        if (accessor === MissingSetAccessor) {
            val methodName = setterName(fieldName)
            throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
        }
        accessor.set(target, value)
    }

    override fun getTargetValue(fieldName: String, toValue: Double): Number {
        if (fieldName.isEmpty()) {
            throw Error("${target::class.java.name} 要设置空属性的字段。值为: $toValue")
        }
        val accessor = resolveGetAccessor(targetClass, fieldName)
        if (accessor === MissingGetAccessor) {
            val methodName = getterName(fieldName)
            throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
        }
        return accessor.get(target, fieldName)
    }

    override fun clear() {
        super.clear()
    }

    companion object {
        private val setAccessorCache = ConcurrentHashMap<Class<*>, ConcurrentHashMap<String, SetAccessor>>()
        private val getAccessorCache = ConcurrentHashMap<Class<*>, ConcurrentHashMap<String, GetAccessor>>()

        private fun resolveSetAccessor(targetClass: Class<*>, fieldName: String): SetAccessor {
            val cache = getOrCreateSetCache(targetClass)
            val cached = cache[fieldName]
            if (cached != null) {
                return cached
            }
            val resolved = buildSetAccessor(targetClass, fieldName)
            val previous = cache.putIfAbsent(fieldName, resolved)
            return previous ?: resolved
        }

        private fun resolveGetAccessor(targetClass: Class<*>, fieldName: String): GetAccessor {
            val cache = getOrCreateGetCache(targetClass)
            val cached = cache[fieldName]
            if (cached != null) {
                return cached
            }
            val resolved = buildGetAccessor(targetClass, fieldName)
            val previous = cache.putIfAbsent(fieldName, resolved)
            return previous ?: resolved
        }

        private fun getOrCreateSetCache(targetClass: Class<*>): ConcurrentHashMap<String, SetAccessor> {
            val existing = setAccessorCache[targetClass]
            if (existing != null) {
                return existing
            }
            val created = ConcurrentHashMap<String, SetAccessor>()
            val previous = setAccessorCache.putIfAbsent(targetClass, created)
            return previous ?: created
        }

        private fun getOrCreateGetCache(targetClass: Class<*>): ConcurrentHashMap<String, GetAccessor> {
            val existing = getAccessorCache[targetClass]
            if (existing != null) {
                return existing
            }
            val created = ConcurrentHashMap<String, GetAccessor>()
            val previous = getAccessorCache.putIfAbsent(targetClass, created)
            return previous ?: created
        }

        private fun buildSetAccessor(targetClass: Class<*>, fieldName: String): SetAccessor {
            findField(targetClass, fieldName)?.let { field ->
                return buildFieldSetAccessor(field)
            }

            val methodName = setterName(fieldName)
            findSetter(targetClass, methodName)?.let { method ->
                val parameterType = method.parameterTypes.firstOrNull()
                val valueType = parameterType?.let { resolveValueType(it) }
                return if (valueType == null) NoOpSetAccessor else MethodSetAccessor(method, valueType)
            }

            return MissingSetAccessor
        }

        private fun buildGetAccessor(targetClass: Class<*>, fieldName: String): GetAccessor {
            findField(targetClass, fieldName)?.let { field ->
                return buildFieldGetAccessor(field)
            }

            val methodName = getterName(fieldName)
            findGetter(targetClass, methodName)?.let { method ->
                return MethodGetAccessor(method, methodName)
            }

            return MissingGetAccessor
        }

        private fun buildFieldSetAccessor(field: Field): SetAccessor {
            return when (field.type) {
                java.lang.Double.TYPE -> PrimitiveDoubleFieldSetAccessor(field)
                java.lang.Float.TYPE -> PrimitiveFloatFieldSetAccessor(field)
                java.lang.Long.TYPE -> PrimitiveLongFieldSetAccessor(field)
                java.lang.Integer.TYPE -> PrimitiveIntFieldSetAccessor(field)
                java.lang.Character.TYPE -> PrimitiveCharFieldSetAccessor(field)
                java.lang.Short.TYPE -> PrimitiveShortFieldSetAccessor(field)
                java.lang.Byte.TYPE -> PrimitiveByteFieldSetAccessor(field)
                java.lang.Double::class.java -> ObjectFieldSetAccessor(field, ValueType.DOUBLE)
                java.lang.Float::class.java -> ObjectFieldSetAccessor(field, ValueType.FLOAT)
                java.lang.Long::class.java -> ObjectFieldSetAccessor(field, ValueType.LONG)
                java.lang.Integer::class.java -> ObjectFieldSetAccessor(field, ValueType.INT)
                java.lang.Character::class.java -> ObjectFieldSetAccessor(field, ValueType.CHAR)
                java.lang.Short::class.java -> ObjectFieldSetAccessor(field, ValueType.SHORT)
                java.lang.Byte::class.java -> ObjectFieldSetAccessor(field, ValueType.BYTE)
                else -> NoOpSetAccessor
            }
        }

        private fun buildFieldGetAccessor(field: Field): GetAccessor {
            return when (field.type) {
                java.lang.Double.TYPE -> PrimitiveDoubleFieldGetAccessor(field)
                java.lang.Float.TYPE -> PrimitiveFloatFieldGetAccessor(field)
                java.lang.Long.TYPE -> PrimitiveLongFieldGetAccessor(field)
                java.lang.Integer.TYPE -> PrimitiveIntFieldGetAccessor(field)
                java.lang.Short.TYPE -> PrimitiveShortFieldGetAccessor(field)
                java.lang.Byte.TYPE -> PrimitiveByteFieldGetAccessor(field)
                else -> FieldGetAccessor(field)
            }
        }

        private fun findField(targetClass: Class<*>, name: String): Field? {
            return try {
                targetClass.getField(name).apply { isAccessible = true }
            } catch (_: Exception) {
                null
            }
        }

        private fun findSetter(targetClass: Class<*>, name: String): Method? {
            return targetClass.methods.firstOrNull { it.name == name && it.parameterTypes.size == 1 }?.apply {
                isAccessible = true
            }
        }

        private fun findGetter(targetClass: Class<*>, name: String): Method? {
            return targetClass.methods.firstOrNull { it.name == name && it.parameterTypes.isEmpty() }?.apply {
                isAccessible = true
            }
        }

        private fun setterName(fieldName: String): String =
            "set${fieldName.replaceFirstChar { it.uppercaseChar() }}"

        private fun getterName(fieldName: String): String =
            "get${fieldName.replaceFirstChar { it.uppercaseChar() }}"

        private fun resolveValueType(type: Class<*>): ValueType? {
            return when (type) {
                java.lang.Double.TYPE, java.lang.Double::class.java -> ValueType.DOUBLE
                java.lang.Float.TYPE, java.lang.Float::class.java -> ValueType.FLOAT
                java.lang.Long.TYPE, java.lang.Long::class.java -> ValueType.LONG
                java.lang.Integer.TYPE, java.lang.Integer::class.java -> ValueType.INT
                java.lang.Character.TYPE, java.lang.Character::class.java -> ValueType.CHAR
                java.lang.Short.TYPE, java.lang.Short::class.java -> ValueType.SHORT
                java.lang.Byte.TYPE, java.lang.Byte::class.java -> ValueType.BYTE
                else -> null
            }
        }

        private fun convertValue(value: Double, valueType: ValueType): Any {
            return when (valueType) {
                ValueType.DOUBLE -> value
                ValueType.FLOAT -> value.toFloat()
                ValueType.LONG -> value.toLong()
                ValueType.INT -> value.toInt()
                ValueType.CHAR -> value.toInt().toChar()
                ValueType.SHORT -> value.toInt().toShort()
                ValueType.BYTE -> value.toInt().toByte()
            }
        }
    }

    private enum class ValueType {
        DOUBLE,
        FLOAT,
        LONG,
        INT,
        CHAR,
        SHORT,
        BYTE
    }

    private sealed interface SetAccessor {
        fun set(target: Any, value: Double)
    }

    private object MissingSetAccessor : SetAccessor {
        override fun set(target: Any, value: Double) = Unit
    }

    private object NoOpSetAccessor : SetAccessor {
        override fun set(target: Any, value: Double) = Unit
    }

    private class PrimitiveDoubleFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setDouble(target, value)
        }
    }

    private class PrimitiveFloatFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setFloat(target, value.toFloat())
        }
    }

    private class PrimitiveLongFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setLong(target, value.toLong())
        }
    }

    private class PrimitiveIntFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setInt(target, value.toInt())
        }
    }

    private class PrimitiveCharFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setChar(target, value.toInt().toChar())
        }
    }

    private class PrimitiveShortFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setShort(target, value.toInt().toShort())
        }
    }

    private class PrimitiveByteFieldSetAccessor(private val field: Field) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.setByte(target, value.toInt().toByte())
        }
    }

    private class ObjectFieldSetAccessor(
        private val field: Field,
        private val valueType: ValueType
    ) : SetAccessor {
        override fun set(target: Any, value: Double) {
            field.set(target, convertValue(value, valueType))
        }
    }

    private class MethodSetAccessor(
        private val method: Method,
        private val valueType: ValueType
    ) : SetAccessor {
        override fun set(target: Any, value: Double) {
            method.invoke(target, convertValue(value, valueType))
        }
    }

    private sealed interface GetAccessor {
        fun get(target: Any, fieldName: String): Number
    }

    private object MissingGetAccessor : GetAccessor {
        override fun get(target: Any, fieldName: String): Number {
            throw Error("${target::class.java.name} 没有 $fieldName 属性")
        }
    }

    private class FieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number {
            val value = field.get(target)
            if (value is Number) {
                return value
            }
            throw Error("${target::class.java.name} $fieldName 属性 类型不是number")
        }
    }

    private class MethodGetAccessor(
        private val method: Method,
        private val methodName: String
    ) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number {
            val value = method.invoke(target)
            if (value is Number) {
                return value
            }
            throw Error("${target::class.java.name} $methodName 方法 获取的类型不是number")
        }
    }

    private class PrimitiveDoubleFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getDouble(target)
    }

    private class PrimitiveFloatFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getFloat(target)
    }

    private class PrimitiveLongFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getLong(target)
    }

    private class PrimitiveIntFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getInt(target)
    }

    private class PrimitiveShortFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getShort(target)
    }

    private class PrimitiveByteFieldGetAccessor(private val field: Field) : GetAccessor {
        override fun get(target: Any, fieldName: String): Number = field.getByte(target)
    }

    private class BoundReflectPropertyAccessor(
        private val target: Any,
        private val fieldName: String,
        private val setAccessor: SetAccessor,
        private val getAccessor: GetAccessor
    ) : BoundPropertyAccessor {
        override fun set(value: Double) {
            setAccessor.set(target, value)
        }

        override fun get(toValue: Double): Number {
            if (getAccessor === MissingGetAccessor) {
                val methodName = getterName(fieldName)
                throw Error("${target::class.java.name} 没有 $fieldName 属性 或者 $methodName 方法")
            }
            return getAccessor.get(target, fieldName)
        }
    }
}