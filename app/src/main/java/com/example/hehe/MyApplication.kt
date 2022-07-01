package com.example.hehe

import android.app.Application
import com.wukonganimation.action.ActionManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ActionManager.init(this)
    }

}