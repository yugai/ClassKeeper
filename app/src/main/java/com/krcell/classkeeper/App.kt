package com.krcell.classkeeper

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.support.multidex.MultiDex
import com.krcell.classkeeper.utils.AppHelp


class App : Application() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        AppHelp.init(this)
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}

object AppContext : ContextWrapper(App.INSTANCE)