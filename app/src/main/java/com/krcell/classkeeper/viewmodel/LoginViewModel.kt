package com.krcell.classkeeper.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.krcell.classkeeper.extentions.dispatchDefault
import com.krcell.classkeeper.http.NetRetrofitClient
import com.krcell.classkeeper.model.LoginBean
import com.krcell.classkeeper.repository.LoginRepository
import io.reactivex.rxkotlin.subscribeBy

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {
    val mLoginUser: MutableLiveData<LoginBean> = MutableLiveData()
    val errMsg: MutableLiveData<String> = MutableLiveData()

    fun login(
        openID: String,
        unionID: String,
        nickname: String,
        gender: Int,
        brand: String,
        phoneModel: String,
        os: String,
        versioncode: Int,
        imei: String,
        market: String
    ): LiveData<LoginBean> {
        NetRetrofitClient.loginService
            .loginByCommonAccount(openID, unionID, nickname, gender, brand, phoneModel, os, versioncode, imei, market)
            .dispatchDefault()
            .subscribeBy(
                onNext = {
                    mLoginUser.value = it
                },
                onError = {
                    errMsg.value = it.message
                }
            ).addDispose()

        return mLoginUser
    }
}