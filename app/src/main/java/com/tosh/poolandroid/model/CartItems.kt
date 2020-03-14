package com.tosh.poolandroid.model

import java.util.*

data class CartItems(
    val cartOrderNumber: Long,
    val productId: Int,
    val productName: String,
    val extraId: Int,
    val extraName: String,
    val extraPrice: Double,
    val productQuantity: Int,
    val productPrice: Double,
    val totalPrice: Double,
    val vendorId: Int,
    val vendorName: String
)
