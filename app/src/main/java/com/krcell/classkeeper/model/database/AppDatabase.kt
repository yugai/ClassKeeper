package com.krcell.classkeeper.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.krcell.classkeeper.model.LoginBean

@Database(entities = [LoginBean::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginBeanDao(): LoginBeanDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "ClassKeeper.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }

}