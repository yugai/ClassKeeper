package com.krcell.classkeeper.model

import android.arch.persistence.room.Entity
import java.io.Serializable

@Entity(tableName = "loginData")
data class LoginBean(
    var result: Boolean,
    var isNew: Boolean,
    var userID: Int,
    var userKey: String,
    var nickname: String,
    var phone: String,
    var regTime: Long,
    var gender: String,
    var avatar: String,
    var identity: String,
    var introduction: String,
    var walletAmount: Int
) : Serializable

