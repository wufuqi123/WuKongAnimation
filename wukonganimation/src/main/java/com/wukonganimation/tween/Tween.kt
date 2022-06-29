package com.wukonganimation.tween

import com.wukonganimation.event.EventEmitter
import java.lang.reflect.Field
import java.lang.reflect.Method

class Tween {
    //要修改的对象
    lateinit var target: Any
    private lateinit var targetClass: Class<*>

    private val eventEmitter by lazy {
        EventEmitter()
    }

    private val mTargetFieldMap = mutableMapOf<String, Field>()
    private val mTargetMethodMap = mutableMapOf<String, Method>()

    var manager: TweenManager? = null

    //执行的总时间
    private var countTime: Long = 0

    //是否已激活状态
    var active: Boolean = false

    //使用的算法
    private var easingProperty: (t: Double) -> Double = Easing.linear()

    //执行完一次后  是否从内存内清理出来
    var expire: Boolean = false

    //重复执行的次数，负数为无线循环
    private var countRepeat: Int = 0

    //是否为循环
    private var loop: Boolean = false

    //等待时间
    private var delayProperty: Long = 0

    //是否为pingPong 效果
    private var pingPongProperty: Boolean = false

    //是否已开启
    private var isStarted: Boolean = false;

    //是否已结束
    var isEnded: Boolean = false;

    //是否调用了 start方法，用来判断restart回调
    private var isUseStartFun: Boolean = false;

    //要执行到某个值的参数容器
    private var toData: MutableMap<String, Number> = mutableMapOf()

    //从哪里开始的值，不写从target的原值开始
    private var fromData: MutableMap<String, Number> = mutableMapOf()

    //当前等待时间
    private var currDelayTime: Long = 0;

    //当前运行的时间
    private var currElapsedTime: Long = 0;

    //当前重复的次数
    private var currRepeat: Int = 0;

    //当前是不是PingPong
    private var currPingPong: Boolean = false;

    //扩展的Tween，当前的Tween执行完成后会立刻执行此扩展的Tween
    private var chainTween: Tween? = null

    private var _updateDeltaOffsetFun: ((t: Long) -> Long)? = null

    constructor(target: Any, manager: TweenManager? = null) {
        this.target = target
        targetClass = target::class.java
        if (manager != null) this.addTo(manager)
        this.clear()
    }


    //把当前Tween对象添加到  TweenManager 队列里去
    private fun addTo(manager: TweenManager): Tween {
        this.manager = manager
        this.manager?.addTween(this)
        return this
    }


    /**
     * 设置执行的总时间
     * @param time
     */
    fun time(time: Long): Tween {
        this.countTime = time
        return this
    }

    /**
     * 设置使用的算法
     * @param easingProperty
     */
    fun easing(easingProperty: (t: Double) -> Double): Tween {
        this.easingProperty = easingProperty
        return this
    }

    /**
     * 设置pingPong
     * @param pingPong
     */
    fun pingPong(pingPong: Boolean): Tween {
        this.pingPongProperty = pingPong
        return this
    }

    /**
     * 设置等待时间
     * @param delay
     */
    fun delay(delay: Long): Tween {
        this.delayProperty = delay
        return this
    }

    /**
     * 负数 无线循环
     * @param repeat
     */
    fun repeat(repeat: Int): Tween {
        this.loop = repeat < 0
        this.countRepeat = repeat
        return this
    }

    /**
     * 扩展Tween，当前Tween执行完成之后会执行此扩展的Tween
     * @param tween
     */
    fun chain(tween: Tween): Tween {
        this.chainTween = tween
        return this
    }

    /**
     * 开始动画
     */
    fun start(): Tween {
        this.active = true
        this.isUseStartFun = true
        if (this.delayProperty <= 0 && this.countTime <= 0) {
            this.setAllVal()
        }
        this.manager?.runAnimation()
        return this
    }

    /**
     * 结束动画
     */
    fun stop(): Tween {
        this.active = false
        this.eventEmitter.emit(TweenManager.EVENT_STOP)
        return this
    }

    /**
     * 设置最终的值
     * @param data
     */
    fun to(data: MutableMap<String, Number>): Tween {
        this.toData = data
        return this
    }

    /**
     * 设置最开始的值
     * @param data
     */
    fun from(data: MutableMap<String, Number>): Tween {
        this.fromData = data
        return this
    }

    /**
     * 从任务队列里删除
     */
    fun remove(): Tween {
        if (this.manager == null) return this
        this.manager?.removeTween(this)
        clearReflect()
        offAll()
        return this
    }

    /**
     * 设置为true则会从内存中清理出来，默认为false
     * Tween会只执行一次
     * @param expire
     */
    fun setExpire(expire: Boolean): Tween {
        this.expire = expire
        return this
    }

    /**
     * 为指定事件注册一个监听器，接受一个字符串 event 和一个回调函数。
     * 请注意  调用2次一样的listener  会触发2次
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun on(eventName: String, listener: (param: MutableList<Any>) -> Unit): Tween {
        this.eventEmitter.on(eventName,listener)
        return this
    }

    /**
     * 为指定事件注册一个单次监听器，即 监听器最多只会触发一次，触发后立刻解除该监听器。
     * 请注意  调用2次一样的listener  会触发2次
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun once(eventName: String, listener: (param: MutableList<Any>) -> Unit): Tween {
        this.eventEmitter.once(eventName,listener)
        return this
    }

    /**
     * 取消一个事件,当前事件下的一个对应事件
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun off(eventName: String, listener: (param: MutableList<Any>) -> Unit): Tween {
        this.eventEmitter.off(eventName,listener)
        return this
    }

    /**
     * 取消一个事件,当前事件下的全部事件
     * @param eventName 事件名
     */
    fun off(eventName: String): Tween {
        this.eventEmitter.off(eventName)
        return this
    }



    /**
     * 取消全部事件
     */
    fun offAll(): Tween {
        this.eventEmitter.offAll()
        return this
    }

    /**
     * 清除反射数据
     */
    fun clearReflect() {
        mTargetMethodMap.clear()
        mTargetFieldMap.clear()
    }


    /**
     * 把Tween清空到初始化的地步，如果没有重新设置参数，start无法开启
     */
    fun clear(): Tween {
        countTime = 0
        active = false
        easingProperty = Easing.linear()
        expire = false
        countRepeat = 0
        loop = false
        delayProperty = 0
        pingPongProperty = false
        isStarted = false
        isEnded = false
        toData.clear()
        fromData.clear()
        currDelayTime = 0
        currElapsedTime = 0
        currRepeat = 0
        currPingPong = false
        chainTween = null
        return this
    }

    /**
     * 重置Tween，保留参数可以重新执行动画
     */
    fun reset(): Tween {
        currElapsedTime = 0
        currRepeat = 0
        currDelayTime = 0
        isStarted = false
        isEnded = false
        if (pingPongProperty && currPingPong) {
            val toData = toData
            val fromData = fromData
            this.toData = fromData
            this.fromData = toData
            currPingPong = false
        }
        return this
    }

    // 解析数据
    private fun parseData() {
        if (isStarted) return
        _parseRecursiveData(toData, fromData, target)
    }

    //设置参数
    private fun apply(time: Long) {
        _recursiveApplyTween(
            this.toData,
            this.fromData,
            this.target,
            time,
            this.currElapsedTime,
            this.easingProperty
        )
    }

    //当前是否能执行  update
    private fun canUpdate(): Boolean {
        return countTime != 0L && active
    }


    //是否执行  定时器模块，不执行  后面的
    private fun canDeltaUpdate(): Boolean {
        return countTime <= 0 && delayProperty > 0 && active
    }


    //直接设置全部值
    fun setAllVal() {
        if (!isStarted) {
            parseData()
            isStarted = true
            this.eventEmitter.emit(TweenManager.EVENT_START)
        } else {
            if (isUseStartFun) {
                this.eventEmitter.emit(TweenManager.EVENT_RESTART)
            }
        }
        isUseStartFun = false
        this.eventEmitter.emit(TweenManager.EVENT_UPDATE, 0)
        if (pingPongProperty) {
            this.eventEmitter.emit(TweenManager.EVENT_PINGPONG)
            this.eventEmitter.emit(TweenManager.EVENT_UPDATE, 0)
        }
        isEnded = true
        active = false
        //如果是  pingPong  则  不设置
        if (!pingPongProperty) {
            currElapsedTime = 1
            this.apply(1)
            if (chainTween != null && manager != null) {
                chainTween!!.addTo(manager!!)
                chainTween!!.start()
            }
        }
        this.eventEmitter.emit(TweenManager.EVENT_END)
    }


    fun setUpdateDeltaOffsetFun(updateDeltaOffsetFun: (t: Long) -> Long): Tween {
        this._updateDeltaOffsetFun = updateDeltaOffsetFun
        return this
    }


    fun update(_deltaMS: Long) {
        var deltaMS = _deltaMS
        //如果设置了定时器
        if (this.canDeltaUpdate()) {
            if (this.delayProperty > this.currDelayTime) {
                this.currDelayTime += deltaMS
                return
            }
            this.setAllVal()
            return
        }
        if (!this.canUpdate() && this.toData.isNotEmpty()) return
        val toData: MutableMap<String, Number>
        val fromData: MutableMap<String, Number>
        if (this._updateDeltaOffsetFun != null) {
            deltaMS += (this._updateDeltaOffsetFun?.let { it(deltaMS) } ?: 0L)
        }
        if (this.delayProperty > this.currDelayTime) {
            this.currDelayTime += deltaMS
            return
        }

        if (!this.isStarted) {
            this.parseData()
            this.isStarted = true
            this.eventEmitter.emit(TweenManager.EVENT_START)
        } else {
            if (this.isUseStartFun) {
                this.eventEmitter.emit(TweenManager.EVENT_RESTART)
            }
        }
        this.isUseStartFun = false;
        val time = if (this.pingPongProperty) this.countTime / 2 else this.countTime
        if (time > this.currElapsedTime) {
            val t = this.currElapsedTime + deltaMS
            val ended = t >= time

            this.currElapsedTime = if (ended) time else t
            this.apply(time)

            val realElapsed =
                if (this.currPingPong) time + this.currElapsedTime else this.currElapsedTime
            this.eventEmitter.emit(TweenManager.EVENT_UPDATE, realElapsed)

            if (ended) {
                if (this.pingPongProperty && !this.currPingPong) {
                    this.currPingPong = true
                    toData = this.toData
                    fromData = this.fromData
                    this.fromData = toData
                    this.toData = fromData

                    this.eventEmitter.emit(TweenManager.EVENT_PINGPONG)
                    this.currElapsedTime = 0
                    return
                }

                if (this.loop || this.countRepeat > this.currRepeat) {
                    this.currRepeat++
                    this.eventEmitter.emit(TweenManager.EVENT_ERPEAT, this.currRepeat)
                    this.currElapsedTime = 0

                    if (this.pingPongProperty && this.currPingPong) {
                        toData = this.toData
                        fromData = this.fromData
                        this.toData = fromData
                        this.fromData = toData

                        this.currPingPong = false
                    }
                    return
                }

                this.isEnded = true
                this.active = false
                this.eventEmitter.emit(TweenManager.EVENT_END)

                if (this.chainTween != null && this.manager != null) {
                    this.chainTween!!.addTo(this.manager!!)
                    this.chainTween!!.start()
                }
            }
            return
        }
    }


    private fun _recursiveApplyTween(
        to: MutableMap<String, Number>,
        from: MutableMap<String, Number>,
        target: Any,
        time: Long,
        elapsed: Long,
        easing: (t: Double) -> Double
    ) {

        to.forEach {
            val b = from[it.key]!!.toDouble()
            val c = to[it.key]!!.toDouble() - from[it.key]!!.toDouble()
            val t = elapsed / time.toDouble()
            val value = b + c * easing(t)
            setTargetValue(it.key, value)
        }
    }


    private fun setTargetValue(fieldName: String, value: Double) {
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

    private fun getTargetValue(fieldName: String, toValue: Double): Number {
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

    fun getTargetField(targetClass: Class<*>, name: String): Field? {
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
                return getTargetField(targetClass.superclass, name)
            }
        }
        return null
    }

    fun getTargetMethod(targetClass: Class<*>, name: String): Method? {
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
            return getTargetMethod(targetClass.superclass, name)
        }
        return null
    }


    private fun _parseRecursiveData(
        to: MutableMap<String, Number>,
        from: MutableMap<String, Number>,
        target: Any
    ) {

        to.forEach {
            if (from[it.key] == null) {
                from[it.key] = getTargetValue(it.key, it.value.toDouble())
            }
        }
    }
}

operator fun Number.minus(number: Number): Number {
    return this.toDouble() - number.toDouble()
}

operator fun Number.plus(number: Number): Number {
    return this.toDouble() + number.toDouble()
}

operator fun Number.times(number: Number): Number {
    return this.toDouble() * number.toDouble()
}

operator fun Number.div(number: Number): Number {
    return this.toDouble() / number.toDouble()
}
