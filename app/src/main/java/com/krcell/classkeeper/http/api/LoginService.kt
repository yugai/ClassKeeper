package com.krcell.classkeeper.http.api

import com.krcell.classkeeper.model.LoginBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("user/common-account-login")
    fun loginByCommonAccount(
        @Field("openID")
        openID: String,
        @Field("unionID")
        unionID: String,
        @Field("nickname")
        nickname: String,
        @Field("gender")
        gender: Int,
        @Field("brand")
        brand: String,
        @Field("phoneModel")
        phoneModel: String,
        @Field("os")
        os: String,
        @Field("versioncode")
        versioncode: Int,
        @Field("imei")
        imei: String,
        @Field("market")
        market: String
    ): Observable<LoginBean>

    @FormUrlEncoded
    @POST("user/set-avatar")
    fun setUserAvatar(
        @Field("userID") userID: Int,
        @Field("userKey") userKey: String,
        @Field("fileID") fileID: Int
    ): Observable<String>
}