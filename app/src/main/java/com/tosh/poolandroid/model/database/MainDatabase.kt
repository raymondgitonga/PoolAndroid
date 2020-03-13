package com.tosh.poolandroid.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, CartItemEntity::class], version = 6, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cartItemDao(): CartItemDao

    companion object {
        @Volatile private var db: MainDatabase? = null
        fun getInstance( context: Context): MainDatabase? {
            if (db == null) {
                synchronized(MainDatabase::class.java) {
                    db = Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "users_database")
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