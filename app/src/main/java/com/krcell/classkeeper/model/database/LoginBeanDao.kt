package com.krcell.classkeeper.model.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.krcell.classkeeper.model.LoginBean

@Dao
interface LoginBeanDao {
    @Query("SELECT * FROM loginData WHERE userID = :userId")
    fun queryUserById(userId: Int = 0): LoginBean?

    @Query("DELETE FROM loginData WHERE userID = :userId")
    fun deleteLogin(userId: Int = 0): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loginBean: LoginBean)
}
