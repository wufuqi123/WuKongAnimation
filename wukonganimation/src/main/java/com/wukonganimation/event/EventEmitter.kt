package com.wukonganimation.event


/**
 * 事件  发送订阅
 *
 * 注意线程不安全
 */
open class EventEmitter {

    companion object {

        /**
         * 实例化一个  event 方法，方便注册和取消
         */
        fun instanceEventFun(func: (param: MutableList<Any>) -> Unit): (param: MutableList<Any>) -> Unit {
            return func
        }

    }


    private val mEventMaps = mutableMapOf<String, MutableList<ListenerBean>>()


    /**
     * 获取不为null的 事件列表
     * @param eventName 事件名
     */
    private fun getFuncsNoNull(eventName: String): MutableList<ListenerBean> {
        var funcs = mEventMaps[eventName]
        if (funcs == null) {
            funcs = mutableListOf()
        }
        return funcs
    }


    /**
     * 为指定事件注册一个监听器，接受一个字符串 event 和一个回调函数。
     * 请注意  调用2次一样的listener  会触发2次
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun on(eventName: String, listener: (param: MutableList<Any>) -> Unit) {
        val funcs = getFuncsNoNull(eventName)
        mEventMaps[eventName] = funcs
        funcs.add(ListenerBean(EventType.REPEAT, listener))
    }

    /**
     * 为指定事件注册一个单次监听器，即 监听器最多只会触发一次，触发后立刻解除该监听器。
     * 请注意  调用2次一样的listener  会触发2次
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun once(eventName: String, listener: (param: MutableList<Any>) -> Unit) {
        val funcs = getFuncsNoNull(eventName)
        mEventMaps[eventName] = funcs
        funcs.add(ListenerBean(EventType.ONE, listener))
    }

    /**
     * 发出事件
     * @param eventName 事件名
     * @param params 携带的参数
     */
    fun emit(eventName: String, vararg params: Any) {
        val funcs = mEventMaps[eventName]
        val paramList = mutableListOf<Any>()
        for (i in params.indices) {
            paramList.add(params[i])
        }
        val removeFuns = mutableListOf<ListenerBean>()
        funcs?.forEach {
            if (it.type == EventType.ONE) {
                removeFuns.add(it)
            }
            it.func.invoke(paramList)
        }
        removeFuns.forEach {
            funcs?.remove(it)
        }

    }


    /**
     * 取消一个事件,当前事件下的一个对应事件
     * @param eventName 事件名
     * @param listener 事件触发回调
     */
    fun off(eventName: String, listener: (param: MutableList<Any>) -> Unit) {
        val funcs = mEventMaps[eventName]
        val removeFuns = mutableListOf<ListenerBean>()
        funcs?.forEach {
            if (it.func == listener) {
                removeFuns.add(it)
            }
        }
        removeFuns.forEach {
            funcs?.remove(it)
        }
    }

    /**
     * 取消一个事件,当前事件下的全部事件
     * @param eventName 事件名
     */
    fun off(eventName: String) {
        mEventMaps[eventName]?.clear()
    }

    /**
     * 取消全部事件
     */
    fun offAll() {
        mEventMaps.forEach {
            off(it.key)
        }
        mEventMaps.clear()
    }


    /**
     * 获取注册的事件名
     */
    fun eventNames(): MutableList<String> {
        val ens = mutableListOf<String>()
        mEventMaps.forEach {
            ens.add(it.key)
        }
        return ens
    }

    /**
     * 通过事件名，获取注册的事件数量
     * @param eventName 事件名
     */
    fun listenerCount(eventName: String): Int {
        return mEventMaps[eventName]?.size ?: 0
    }

    /**
     * 获取注册的全部事件数量
     */
    fun listenerCountAll(): Int {
        var count = 0
        mEventMaps.forEach {
            count += listenerCount(it.key)
        }
        return count
    }
    /**
     * 事件类型
     */
    private enum class EventType {
        /**
         * 重复触发
         */
        REPEAT,
        /**
         * 触发一次
         */
        ONE
    }
    /**
     * 事件对象
     */
    private data class ListenerBean(var type: EventType, var func: (param: MutableList<Any>) -> Unit)
}