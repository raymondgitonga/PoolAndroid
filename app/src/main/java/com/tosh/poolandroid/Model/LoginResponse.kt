package com.tosh.poolandroid.Model

data class LoginResponse(
    val isSuccessful:Boolean,
    val message: String,
    val user: List<User>
)