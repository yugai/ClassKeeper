package com.krcell.classkeeper.utils

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.didichuxing.doraemonkit.DoraemonKit
import com.jeremyliao.liveeventbus.LiveEventBus
import com.meituan.robust.Patch
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.meituan.robust.PatchExecutor
import com.meituan.robust.RobustCallBack
import com.meituan.android.walle.WalleChannelReader
import com.squareup.leakcanary.LeakCanary


class AppHelp private constructor() {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            AppHelp()
        }

        fun init(application: Application) {
            //工具类
            Utils.init(application)

            val channel = WalleChannelReader.getChannel(application)
            //bugly
            val packageName = application.packageName
            val processName = ProcessUtils.getCurrentProcessName()
            val strategy = UserStrategy(application)
            strategy.appChannel = channel
            strategy.isUploadProcess = processName == null || processName == packageName
            CrashReport.initCrashReport(application, BUGLY_ID, false, strategy)
            //热更新
            PatchExecutor(application, PatchManipulateImp(), object : RobustCallBack {
                override fun onPatchListFetched(result: Boolean, isNet: Boolean, patches: List<Patch>) {
                    Log.d("RobustCallBack", "onPatchListFetched result: $result")
                    Log.d("RobustCallBack", "onPatchListFetched isNet: $isNet")
                    for (patch in patches) {
                        Log.d("RobustCallBack", "onPatchListFetched patch: " + patch.name)
                    }
                }

                override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: Patch) {
                    Log.d("RobustCallBack", "onPatchFetched result: $result")
                    Log.d("RobustCallBack", "onPatchFetched isNet: $isNet")
                    Log.d("RobustCallBack", "onPatchFetched patch: " + patch.name)
                }

                override fun onPatchApplied(result: Boolean, patch: Patch) {
                    Log.d("RobustCallBack", "onPatchApplied result: $result")
                    Log.d("RobustCallBack", "onPatchApplied patch: " + patch.name)
                }

                override fun logNotify(log: String, where: String) {
                    Log.d("RobustCallBack", "logNotify log: $log")
                    Log.d("RobustCallBack", "logNotify where: $where")
                }

                override fun exceptionNotify(throwable: Throwable, where: String) {
                    Log.e("RobustCallBack", "exceptionNotify where: $where", throwable)
                }
            }).start()

            //内存检测工具
            LeakCanary.install(application)

            //UI查看工具
            DoraemonKit.install(application)

            //BUS注册
            LiveEventBus.get()
                .config()
                .supportBroadcast(application)
                .lifecycleObserverAlwaysActive(true)
        }

    }
}