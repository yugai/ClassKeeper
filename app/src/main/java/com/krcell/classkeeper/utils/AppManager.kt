package com.krcell.classkeeper.utils

import android.app.Activity
import java.util.*

class AppManager private constructor() {
    companion object {
        var activityStack: Stack<Activity>? = null
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null && activityStack!!.contains(activity)) {
            activityStack!!.remove(activity)
        }
    }


    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }


    fun finishActivity(activity: Activity?) {
        if (activity != null && activityStack!!.contains(activity) && !activity.isFinishing) {
            activityStack!!.remove(activity)
            activity.finish()
        }
    }

    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }
}