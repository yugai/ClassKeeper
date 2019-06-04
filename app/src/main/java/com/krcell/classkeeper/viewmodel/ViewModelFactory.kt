package com.krcell.classkeeper.viewmodel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.krcell.classkeeper.repository.LoginRepository
import com.krcell.classkeeper.ui.activity.BaseActivity

class ViewModelFactory private constructor(
    private val loginRepository: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(loginRepository)
                }
                else ->
                    throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
            }

        } as T


    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(activity: BaseActivity) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideLoginRepository(activity)
                )
            }
    }
}