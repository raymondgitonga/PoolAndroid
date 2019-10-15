package com.tosh.poolloginrebuild.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var db: UserDatabase? = null
        fun getInstance( context: Context): UserDatabase? {
            if (db == null) {
                synchronized(UserDatabase::class.java) {
                    db = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "users_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return db
        }

        fun destroyInstance() {
            db = null
        }
    }
}