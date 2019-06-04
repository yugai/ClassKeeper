package com.krcell.classkeeper.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.krcell.classkeeper.R
import com.krcell.classkeeper.extentions.toActivity
import com.krcell.classkeeper.utils.WEIXIN_LOGIN
import com.krcell.classkeeper.utils.WeixinAuthUtils
import com.krcell.classkeeper.utils.WeixinAuthUtils.APP_ID
import com.krcell.classkeeper.viewmodel.LoginViewModel
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : BaseActivity() {

    private var wxapi: IWXAPI? = null

    private lateinit var loginViewModel: LoginViewModel

    override fun getLayout() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login.setOnClickListener {
            wxapi = WXAPIFactory.createWXAPI(this, APP_ID, true)
            wxapi!!.registerApp(APP_ID)
            val req = SendAuth.Req()
            req.scope = WeixinAuthUtils.WEIXIN_SCOPE
            req.state = WeixinAuthUtils.WEIXIN_STATE
            wxapi!!.sendReq(req)
        }

        LiveEventBus.get()
            .with(WEIXIN_LOGIN, String::class.java)
            .observe(this, Observer<String> {
                val json = JSONObject(it)
                val openid = json.getString("openid")
                val nickname = json.getString("nickname")
                val headimgurl = json.getString("headimgurl")

                loginViewModel.login(openid, "11111", "test", 1, "111", "1312506087", "android", 1, "222", "333")
                    .observe(this, Observer {
                        toActivity(MainActivity::class.java)
                    })
            })
    }

}
