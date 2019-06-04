package com.krcell.classkeeper.utils

object URLManager {

    private val SERVER_PROTOCOL = "http://"

    private val SERVER_IP = "120.26.168.156"

    private val TIMING_SERVER_IP = "112.124.5.142"

    val ossPrefix = "test"

    val endpoint = "http://oss-cn-hangzhou.aliyuncs.com"

    val bucket = "classkeeper"

    val ossImageFolder = "$ossPrefix/image/"

    val ossVoiceFolder = "$ossPrefix/voice/"

    val ossVideoFolder = "$ossPrefix/video/"

    val callbackAddress = "http://$SERVER_IP/oss/callback"

    val stsServer = "http://$SERVER_IP/oss/sts"

    val BASE_URL = "http://$SERVER_IP/"

    val BASE_TIMING_URL = "http://$TIMING_SERVER_IP/"

    val uploadFileUrl = "http://$SERVER_IP/pan/login"

    val codeUseUrl = "$SERVER_PROTOCOL$SERVER_IP/class/code-use"

    val cashTipUrl = "$SERVER_PROTOCOL$SERVER_IP/web/cash"

    val classManager = "$SERVER_PROTOCOL$SERVER_IP/class/create-success"

    var shareCircleUrl = "$SERVER_PROTOCOL$SERVER_IP/class/share-circle"

    var classPreviewUrl = "$SERVER_PROTOCOL$SERVER_IP/class/class-review"

    var inviteAssistantUrl = "$SERVER_PROTOCOL$SERVER_IP/web/assistant-invite"

    var playAudioUrl = "$SERVER_PROTOCOL$SERVER_IP/web/play-audio"

    var playVideoUrl = "$SERVER_PROTOCOL$SERVER_IP/web/play-video"

    var startTimingAuthenticationUrl = "$SERVER_PROTOCOL$SERVER_IP/web/authentication"

    var timingAuthenticationStatusUrl = "$SERVER_PROTOCOL$SERVER_IP/web/in-authentication"

    fun getCommonUrl(url: String, uid: Int, userKey: String): String {
        return "$url?userID=$uid&userKey=$userKey"
    }

    fun getClassPreviewUrl(uid: Int, userKey: String, classID: Int): String {
        return "$classPreviewUrl?userID=$uid&userKey=$userKey&classID=$classID"
    }

    fun getShareCircleUrl(uid: Int, userKey: String, classID: Int): String {
        return "$shareCircleUrl?userID=$uid&userKey=$userKey&classID=$classID&isHide=1"
    }

    fun getInviteAssistantUrl(uid: Int, inviteID: Int): String {
        return "$inviteAssistantUrl?u=$uid&i=$inviteID"
    }

    fun getInviteAssistantFaceUrl(uid: Int, inviteID: Int): String {
        return "$inviteAssistantUrl?u=$uid&i=$inviteID&t=faceInvite"
    }

    fun getAudioPlayUrl(fileUrl: String): String {
        return URLManager.playAudioUrl + "?fileUrl=" + fileUrl
    }

    fun getVideoPlayUrl(fileUrl: String): String {
        return URLManager.playVideoUrl + "?fileUrl=" + fileUrl
    }
}
