package com.krcell.classkeeper.http

import com.krcell.classkeeper.http.api.UrlService
import okhttp3.OkHttpClient

object URLRetrofitClient : BaseRetrofitClient() {

    val urlService by lazy { NetRetrofitClient.getService(UrlService::class.java, "https://127.0.0.1") }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
    }
}