package com.krcell.classkeeper.http

import com.krcell.classkeeper.extentions.ensureDir
import com.krcell.classkeeper.AppContext
import com.krcell.classkeeper.http.api.LoginService
import com.krcell.classkeeper.utils.URLManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

object NetRetrofitClient : BaseRetrofitClient() {
    private val cacheFile by lazy {
        File(AppContext.cacheDir, "WebServiceCache").apply {
            ensureDir()
        }
    }

    val loginService by lazy { NetRetrofitClient.getService(LoginService::class.java, URLManager.BASE_TIMING_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.cache(Cache(cacheFile, 1024 * 1024 * 1024))
            .sslSocketFactory(SSLSocketClient.sslSocketFactory)
            .hostnameVerifier(SSLSocketClient.hostnameVerifier)
    }
}