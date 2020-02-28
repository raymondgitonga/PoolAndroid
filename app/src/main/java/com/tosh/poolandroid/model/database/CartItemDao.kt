package com.tosh.poolandroid.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetails(cartItemEntity: CartItemEntity)

    @Query("SELECT * FROM cart_item ORDER BY vendorId")
    suspend fun getCartItems(): List<CartItemEntity>

    @Query("DELETE FROM cart_item")
    suspend fun deleteCart()

    @Query("SELECT COALESCE(sum(COALESCE(productPrice,0)), 0) From cart_item")
    suspend fun cartTotal(): Double

    @Query("DELETE FROM cart_item WHERE id = :id")
    suspend fun deleteCartItem(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM cart_item WHERE productId = :productId)")
    suspend fun getItemCount(productId: Int): Int?

    @Query("SELECT COUNT(*) FROM cart_item")
    fun getCartItemSize(): LiveData<Int>?
}