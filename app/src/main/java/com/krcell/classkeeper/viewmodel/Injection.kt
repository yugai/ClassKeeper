package com.krcell.classkeeper.viewmodel

import com.krcell.classkeeper.model.database.AppDatabase
import com.krcell.classkeeper.repository.LoginRepository
import com.krcell.classkeeper.ui.activity.BaseActivity

object Injection {
    fun provideLoginRepository(activity: BaseActivity): LoginRepository {
        return LoginRepository.getInstance(AppDatabase.getInstance(activity).loginBeanDao(), activity)
    }
}