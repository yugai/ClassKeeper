package com.krcell.classkeeper.repository

import com.krcell.classkeeper.model.database.LoginBeanDao
import com.krcell.classkeeper.ui.activity.BaseActivity

class LoginRepository private constructor(loginBeanDao: LoginBeanDao, activity: BaseActivity) {

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(plantDao: LoginBeanDao, activity: BaseActivity) =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(plantDao, activity).also { instance = it }
            }
    }
}