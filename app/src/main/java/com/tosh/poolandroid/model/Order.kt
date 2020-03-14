package com.tosh.poolandroid.model

data class Order(
    val cartItems: List<CartItem>,
    val cost: Double,
    val deliveryCost: Double,
    val latitude: String,
    val longitude: String,
    val orderNumber: Int,
    val state: String,
    val total: Double,
    val userId: Int
)

data class CartItem(
    val cartOrderNumber: Int,
    val extraId: Int,
    val extraName: String,
    val extraPrice: Double,
    val id: Int,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val productQuantity: Int,
    val totalPrice: Double,
    val vendorId: Int,
    val vendorName: String
)