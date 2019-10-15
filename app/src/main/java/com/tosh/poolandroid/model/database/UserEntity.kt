package com.tosh.poolloginrebuild.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    val name: String,
    val email: String,
    @PrimaryKey
    val phone: String
)