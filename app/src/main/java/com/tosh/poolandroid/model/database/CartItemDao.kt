package com.tosh.poolandroid.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetails(cartItemEntity: CartItemEntity)

    @Query("SELECT * FROM cart_item ORDER BY vendorId")
    suspend fun getCartItems(): List<CartItemEntity>

    @Delete
    fun deleteCart(cartItemEntity: CartItemEntity)
}