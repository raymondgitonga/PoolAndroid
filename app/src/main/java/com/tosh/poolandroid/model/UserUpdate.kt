package com.tosh.poolandroid.model

data class UserUpdate(val name: String, val phone: String, val oldEmail: String, val newEmail: String
)

data class UpdatePassword(val email: String, val password: String, val newPassword: String)