package com.tosh.poolandroid.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart_item")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val extraId: Int?,
    val extraName: String?,
    val extraPrice: Double?,
    val productQuantity: Int?,
    val vendorId: Int,
    val total: Double,
    val vendorName:String
): Serializable