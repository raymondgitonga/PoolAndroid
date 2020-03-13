package com.tosh.poolandroid.model

data class CartItem(
    val cartPrimaryId: String,
    val productId: Int,
    val productName: String,
    val extraId: Int,
    val extraName: String,
    val extraPrice: Double,
    val productQuantity: Int,
    val productPrice: Double,
    val totalPrice: Double,
    val vendorId: Int
)

data class CartItemResult(val response: String)