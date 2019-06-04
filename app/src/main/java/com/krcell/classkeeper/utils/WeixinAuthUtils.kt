package com.krcell.classkeeper.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object WeixinAuthUtils {
    val APP_ID = "wx4775b62a7d63b126"

    val APP_SECRET = "75c89b60d292338b1c6cda4ba95cb265"

    val WEIXIN_SCOPE = "snsapi_userinfo"// 用于请求用户信息的作用域

    val WEIXIN_STATE = "auth_state" // 自定义
}
