package com.wukonganimation.action

import android.app.Application

object ActionManager {

    private var isInit = false

    fun init(application: Application){
        if(isInit){
            return
        }
        isInit = true
        application.registerActivityLifecycleCallbacks(ActionActivityCallbacks())
    }


    fun isInit():Boolean{
        return isInit
    }
}