package com.krcell.classkeeper.wxapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.krcell.classkeeper.R
import com.krcell.classkeeper.extentions.dispatchDefault
import com.krcell.classkeeper.http.URLRetrofitClient
import com.krcell.classkeeper.utils.WEIXIN_LOGIN
import com.krcell.classkeeper.utils.WeixinAuthUtils.APP_ID
import com.krcell.classkeeper.utils.WeixinAuthUtils.APP_SECRET
import com.krcell.classkeeper.utils.toast
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.rxkotlin.subscribeBy
import org.json.JSONObject
import java.net.URLEncoder


private const val TAG = "WXEntryActivity"

class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    private var resp: BaseResp? = null
    // 获取第一步的code后，请求以下链接获取access_token
    private var getCodeRequest =
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
    // 获取用户个人信息
    private var getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, APP_ID, false)
        api!!.handleIntent(intent, this)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {
        finish()
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(resp: BaseResp?) {
        if (resp == null) {
            return
        }
        this@WXEntryActivity.resp = resp
        Log.d(TAG, "code:${resp.errCode}")
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (resp as SendAuth.Resp).code
                //将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
                val url = getCodeRequest(code)

                URLRetrofitClient.urlService.postApi(url)
                    .dispatchDefault()
                    .subscribeBy(
                        onNext = {
                            if (it != "") {
                                val json = JSONObject(it)
                                val accessToken = json
                                    .getString("access_token")
                                val openid = json.getString("openid")
                                val userInfoUrl = getUserInfo(accessToken, openid)
                                getUserInfo(userInfoUrl)
                            }
                        },
                        onError = {
                            toast(it.message!!)
                            it.printStackTrace()
                        }
                    )
                finish()
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                toast(getString(R.string.cancel_login))
                finish()
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                toast(getString(R.string.logon_denied))
                finish()
            }
            else -> {
                toast(getString(R.string.send_return))
                finish()
            }
        }
    }


    /**
     * 通过拼接的用户信息url获取用户信息
     *
     * @param userInfoUrl
     */
    private fun getUserInfo(userInfoUrl: String) {
        Log.i(TAG, ": $userInfoUrl")
        URLRetrofitClient.urlService.getApi(userInfoUrl)
            .dispatchDefault()
            .subscribeBy(
                onNext = {
                    if (it != "") {
                        toast(getString(R.string.send_success))
                        LiveEventBus.get()
                            .with(WEIXIN_LOGIN)
                            .post(it)
                    }
                },
                onError = {
                    toast(it.message!!)
                    it.printStackTrace()
                }
            )
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
        finish()
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code
     * 授权时，微信回调给的
     * @return URL
     */
    private fun getCodeRequest(code: String): String {
        var result: String?
        getCodeRequest = getCodeRequest.replace(
            "APPID",
            urlEnodeUTF8(APP_ID)
        )
        getCodeRequest = getCodeRequest.replace(
            "SECRET",
            urlEnodeUTF8(APP_SECRET)
        )
        getCodeRequest = getCodeRequest.replace("CODE", urlEnodeUTF8(code))
        result = getCodeRequest
        return result
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token
     * 获取access_token时给的
     * @param openid
     * 获取access_token时给的
     * @return URL
     */
    private fun getUserInfo(access_token: String, openid: String): String {
        var result: String?
        getUserInfo = getUserInfo.replace(
            "ACCESS_TOKEN",
            urlEnodeUTF8(access_token)
        )
        getUserInfo = getUserInfo.replace("OPENID", urlEnodeUTF8(openid))
        result = getUserInfo
        return result
    }

    private fun urlEnodeUTF8(str: String): String {
        var result = str
        try {
            result = URLEncoder.encode(str, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}