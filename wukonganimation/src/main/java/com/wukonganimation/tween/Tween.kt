package com.wukonganimation.tween

import com.wukonganimation.event.EventEmitter
import com.wukonganimation.tween.targetvalue.TargetValueAbstract
import com.wukonganimation.tween.targetvalue.TargetValueManager

class Tween {
    //要修改的对象
    var target: Any
    var mTargetValueInterface: TargetValueAbstract

    @JvmField
    var manager: TweenManager? = null

    private var eventEmitter: EventEmitter? = null

    private fun getOrCreateEventEmitter(): EventEmitter {
        val existing = eventEmitter
        if (existing != null) {
            return existing
        }
        val created = EventEmitter()
        eventEmitter = created
        return created
    }

    private fun emitEvent(eventName: String) {
        eventEmitter?.emit(eventName)
    }

    private fun emitEvent(eventName: String, param: Any) {
        eventEmitter?.emit(eventName, param)
    }

    //执行的总时间
    private var countTime: Long = 0

    //是否已激活状态
    @JvmField
    var active: Boolean = false

    //使用的算法
    private var easingProperty: (t: Double) -> Double = Easing.linear()
    private var easingDoubleProperty: Easing.DoubleEasing? = Easing.linear() as? Easing.DoubleEasing
    private var easingFloatProperty: Easing.FloatEasing? = Easing.linear() as? Easing.FloatEasing

    //执行完一次后  是否从内存内清理出来
    @JvmField
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
    private var isStarted: Boolean = false

    /**
     * 是否正在执行
     */
    val isRunning:Boolean get() {
        if(TweenManager.isPause){
            return false
        }
        return !isEnded
    }

    //是否已结束
    @JvmField
    var isEnded: Boolean = false

    //是否调用了 start方法，用来判断restart回调
    private var isUseStartFun: Boolean = false

    //要执行到某个值的参数容器
    private var toData: MutableMap<String, Number> = mutableMapOf()

    //从哪里开始的值，不写从target的原值开始
    private var fromData: MutableMap<String, Number> = mutableMapOf()

    //当前等待时间
    private var currDelayTime: Double = 0.0

    //当前运行的时间
    private var currElapsedTime: Double = 0.0

    //当前重复的次数
    private var currRepeat: Int = 0

    //当前是不是PingPong
    private var currPingPong: Boolean = false

    //扩展的Tween，当前的Tween执行完成后会立刻执行此扩展的Tween
    private var chainTween: Tween? = null

    private var _updateDeltaOffsetFun: ((t: Double) -> Double)? = null

    private var propertyStateCount = 0
    private var propertyAccessors = arrayOfNulls<TargetValueAbstract.BoundPropertyAccessor>(0)
    private var propertyFloatAccessors = arrayOfNulls<TargetValueAbstract.FloatBoundPropertyAccessor>(0)
    private var propertyBeginValues = DoubleArray(0)
    private var propertyChangeValues = DoubleArray(0)
    private var propertyBeginFloatValues = FloatArray(0)
    private var propertyChangeFloatValues = FloatArray(0)
    private var propertyUseFloatPath = BooleanArray(0)

    private fun ensurePropertyStateCapacity(requiredSize: Int) {
        if (propertyAccessors.size >= requiredSize) {
            return
        }
        val newSize = maxOf(requiredSize, propertyAccessors.size.coerceAtLeast(4) * 2)
        propertyAccessors = propertyAccessors.copyOf(newSize)
        propertyFloatAccessors = propertyFloatAccessors.copyOf(newSize)
        propertyBeginValues = propertyBeginValues.copyOf(newSize)
        propertyChangeValues = propertyChangeValues.copyOf(newSize)
        propertyBeginFloatValues = propertyBeginFloatValues.copyOf(newSize)
        propertyChangeFloatValues = propertyChangeFloatValues.copyOf(newSize)
        propertyUseFloatPath = propertyUseFloatPath.copyOf(newSize)
    }

    private fun clearPropertyTweenStates() {
        var index = 0
        while (index < propertyStateCount) {
            propertyAccessors[index] = null
            propertyFloatAccessors[index] = null
            propertyUseFloatPath[index] = false
            index++
        }
        propertyStateCount = 0
    }

    // ...existing code...
    constructor(target: Any, manager: TweenManager? = null) {
        this.target = target
        mTargetValueInterface = TargetValueManager.createTargetValue(target)
        if (manager != null) this.addTo(manager)
        this.clear()
    }


    //把当前Tween对象添加到  TweenManager 队列里去
    private fun addTo(manager: TweenManager): Tween {
        this.manager = manager
        this.manager?.addTween(this)
        return this
    }

    private fun startChainedTween() {
        val nextTween = chainTween ?: return
        val currentManager = manager
        if (currentManager != null && nextTween.manager !== currentManager) {
            nextTween.addTo(currentManager)
        }
        nextTween.start()
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
        this.easingDoubleProperty = easingProperty as? Easing.DoubleEasing
        this.easingFloatProperty = easingProperty as? Easing.FloatEasing
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
        if (enqueueOnManagerThread { start() }) {
            return this
        }
        if (this.active) {
            return this
        }
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
        if (enqueueOnManagerThread { stop() }) {
            return this
        }
        if (!this.active) {
            return this
        }
        this.active = false
        emitEvent(TweenManager.EVENT_STOP)
        return this
    }

    /**
     * 设置最终的值
     * @param data
     */
    fun to(data: MutableMap<String, Number>): Tween {
        this.toData = data
        clearPropertyTweenStates()
        return this
    }

    /**
     * 设置最开始的值
     * @param data
     */
    fun from(data: MutableMap<String, Number>): Tween {
        this.fromData = data
        clearPropertyTweenStates()
        return this
    }

    /**
     * 从任务队列里删除
     */
    fun remove(): Tween {
        if (enqueueOnManagerThread { remove() }) {
            return this
        }
        if (this.manager == null) return this
        stop()
        this.manager?.removeTween(this)
        mTargetValueInterface.clear()
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
        if (enqueueOnManagerThread { on(eventName, listener) }) {
            return this
        }
        getOrCreateEventEmitter().on(eventName, listener)
        return this
    }

    /**
     * 为指定事件注册一个单次监听器，即 监听器最多只会触发一次，触发后立刻解除该监听器。
     * 请注意  调用2次一样的listener  会触发2次
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun once(eventName: String, listener: (param: MutableList<Any>) -> Unit): Tween {
        if (enqueueOnManagerThread { once(eventName, listener) }) {
            return this
        }
        getOrCreateEventEmitter().once(eventName, listener)
        return this
    }

    /**
     * 取消一个事件,当前事件下的一个对应事件
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun off(eventName: String, listener: (param: MutableList<Any>) -> Unit): Tween {
        if (enqueueOnManagerThread { off(eventName, listener) }) {
            return this
        }
        eventEmitter?.off(eventName, listener)
        return this
    }

    /**
     * 取消一个事件,当前事件下的全部事件
     * @param eventName 事件名
     */
    fun off(eventName: String): Tween {
        if (enqueueOnManagerThread { off(eventName) }) {
            return this
        }
        eventEmitter?.off(eventName)
        return this
    }

    /**
     * 取消全部事件
     */
    fun offAll(): Tween {
        if (enqueueOnManagerThread { offAll() }) {
            return this
        }
        eventEmitter?.offAll()
        eventEmitter = null
        return this
    }


    /**
     * 把Tween清空到初始化的地步，如果没有重新设置参数，start无法开启
     */
    fun clear(): Tween {
        countTime = 0
        active = false
        easing(Easing.linear())
        expire = false
        countRepeat = 0
        loop = false
        delayProperty = 0
        pingPongProperty = false
        isStarted = false
        isEnded = false
        toData.clear()
        fromData.clear()
        clearPropertyTweenStates()
        currDelayTime = 0.0
        currElapsedTime = 0.0
        currRepeat = 0
        currPingPong = false
        chainTween = null
        return this
    }

    /**
     * 重置Tween，保留参数可以重新执行动画
     */
    fun reset(): Tween {
        currElapsedTime = 0.0
        currRepeat = 0
        currDelayTime = 0.0
        isStarted = false
        isEnded = false
        if (pingPongProperty && currPingPong) {
            val toData = toData
            val fromData = fromData
            this.toData = fromData
            this.fromData = toData
            currPingPong = false
            rebuildPropertyTweenStates()
        }
        return this
    }

    // 解析数据
    private fun parseData() {
        if (isStarted) return
        _parseRecursiveData(toData, fromData)
        rebuildPropertyTweenStates()
    }

    //设置参数
    private fun apply(time: Long) {
        val progress = this.currElapsedTime / time.toDouble()
        val easedProgress = easingDoubleProperty?.apply(progress) ?: this.easingProperty(progress)
        val progressFloat = progress.toFloat()
        val easedProgressFloat = easingFloatProperty?.applyFloat(progressFloat) ?: easedProgress.toFloat()

        var index = 0
        while (index < propertyStateCount) {
            if (propertyUseFloatPath[index]) {
                propertyFloatAccessors[index]!!.setFloat(
                    propertyBeginFloatValues[index] + propertyChangeFloatValues[index] * easedProgressFloat
                )
            } else {
                propertyAccessors[index]!!.set(
                    propertyBeginValues[index] + propertyChangeValues[index] * easedProgress
                )
            }
            index++
        }
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
            emitEvent(TweenManager.EVENT_START)
        } else {
            if (isUseStartFun) {
                emitEvent(TweenManager.EVENT_RESTART)
            }
        }
        isUseStartFun = false
        emitEvent(TweenManager.EVENT_UPDATE, 0)
        if (pingPongProperty) {
            emitEvent(TweenManager.EVENT_PINGPONG)
            emitEvent(TweenManager.EVENT_UPDATE, 0)
        }
        isEnded = true
        active = false
        if (!pingPongProperty) {
            currElapsedTime = 1.0
            this.apply(1)
            startChainedTween()
        }
        emitEvent(TweenManager.EVENT_END)
    }


    fun setUpdateDeltaOffsetFun(updateDeltaOffsetFun: (t: Double) -> Double): Tween {
        this._updateDeltaOffsetFun = updateDeltaOffsetFun
        return this
    }


    fun update(_deltaMS: Double) {
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
            deltaMS += (this._updateDeltaOffsetFun?.let { it(deltaMS) } ?: 0.0)
        }
        if (this.delayProperty > this.currDelayTime) {
            this.currDelayTime += deltaMS
            return
        }

        if (!this.isStarted) {
            this.parseData()
            this.isStarted = true
            emitEvent(TweenManager.EVENT_START)
        } else {
            if (this.isUseStartFun) {
                emitEvent(TweenManager.EVENT_RESTART)
            }
        }
        this.isUseStartFun = false
        val time = if (this.pingPongProperty) this.countTime / 2 else this.countTime
        if (time > this.currElapsedTime) {
            val t = this.currElapsedTime + deltaMS
            val ended = t >= time

            this.currElapsedTime = if (ended) time.toDouble() else t
            this.apply(time)

            val realElapsed =
                if (this.currPingPong) time + this.currElapsedTime else this.currElapsedTime
            emitEvent(TweenManager.EVENT_UPDATE, realElapsed)

            if (ended) {
                if (this.pingPongProperty && !this.currPingPong) {
                    toData = this.toData
                    fromData = this.fromData
                    this.fromData = toData
                    this.toData = fromData
                    rebuildPropertyTweenStates()

                    emitEvent(TweenManager.EVENT_PINGPONG)
                    this.currElapsedTime = 0.0
                    return
                }

                if (this.loop || this.countRepeat > this.currRepeat) {
                    this.currRepeat++
                    emitEvent(TweenManager.EVENT_ERPEAT, this.currRepeat)
                    this.currElapsedTime = 0.0

                    if (this.pingPongProperty && this.currPingPong) {
                        toData = this.toData
                        fromData = this.fromData
                        this.toData = fromData
                        this.fromData = toData
                        rebuildPropertyTweenStates()

                        this.currPingPong = false
                    }
                    return
                }

                this.isEnded = true
                this.active = false
                this.currElapsedTime = 0.0
                this.currPingPong = false
                if (this.pingPongProperty) {
                    toData = this.toData
                    fromData = this.fromData
                    this.fromData = toData
                    this.toData = fromData
                    rebuildPropertyTweenStates()
                }
                emitEvent(TweenManager.EVENT_END)
                startChainedTween()
            }
            return
        }
    }

    private fun rebuildPropertyTweenStates() {
        clearPropertyTweenStates()
        if (toData.isEmpty()) {
            return
        }
        ensurePropertyStateCapacity(toData.size)
        for ((fieldName, toValue) in toData) {
            val accessor = mTargetValueInterface.bindProperty(fieldName)
            val beginNumber = fromData[fieldName] ?: accessor.get(toValue.toDouble())
            if (fromData[fieldName] == null) {
                fromData[fieldName] = beginNumber
            }
            val index = propertyStateCount
            propertyAccessors[index] = accessor
            val floatAccessor = accessor as? TargetValueAbstract.FloatBoundPropertyAccessor
            propertyFloatAccessors[index] = floatAccessor
            propertyUseFloatPath[index] = floatAccessor != null

            val beginDouble = beginNumber.toDouble()
            propertyBeginValues[index] = beginDouble
            propertyChangeValues[index] = toValue.toDouble() - beginDouble

            val beginFloat = beginNumber.toFloat()
            propertyBeginFloatValues[index] = beginFloat
            propertyChangeFloatValues[index] = toValue.toFloat() - beginFloat
            propertyStateCount++
        }
    }

    private fun _recursiveApplyTween(
        to: MutableMap<String, Number>,
        from: MutableMap<String, Number>,
        easedProgress: Double
    ) {

        to.forEach {
            val b = from[it.key]!!.toDouble()
            val c = to[it.key]!!.toDouble() - from[it.key]!!.toDouble()
            val value = b + c * easedProgress
            setTargetValue(it.key, value)
        }
    }


    private fun setTargetValue(fieldName: String, value: Double) {
        mTargetValueInterface.setTargetValue(fieldName, value)
    }

    private fun getTargetValue(fieldName: String, toValue: Double): Number {
        return mTargetValueInterface.getTargetValue(fieldName, toValue)
    }


    private fun _parseRecursiveData(
        to: MutableMap<String, Number>,
        from: MutableMap<String, Number>
    ) {

        to.forEach {
            if (from[it.key] == null) {
                from[it.key] = getTargetValue(it.key, it.value.toDouble())
            }
        }
    }

    private fun enqueueOnManagerThread(action: () -> Unit): Boolean {
        val currentManager = this.manager ?: return false
        if (currentManager.isMainThread()) {
            return false
        }
        currentManager.enqueueOperation(action)
        return true
    }
}
