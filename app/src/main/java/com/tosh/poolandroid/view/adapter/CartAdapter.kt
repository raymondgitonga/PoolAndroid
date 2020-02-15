package com.tosh.poolandroid.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.CartItemEntity

class CartAdapter(val cartItems: List<CartItemEntity>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

//    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view, cartItems)
    }

    override fun getItemCount() = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       val cartItem = cartItems[position]

        holder.productName.text = cartItem.productName
        holder.extraName.text = cartItem.extraName
        holder.productPrice.text = cartItem.productPrice.toString()

    }

    class CartViewHolder(val view: View, var cart: List<CartItemEntity>): RecyclerView.ViewHolder(view){
        val productName: TextView = view.findViewById(R.id.tvProduct)
        val extraName: TextView = view.findViewById(R.id.tvExtra)
        val productPrice: TextView = view.findViewById(R.id.tvPrice)
        private val cartRemove: TextView = view.findViewById(R.id.cartRemove)

//        init {
//            cartRemove.setOnClickListener {
//                listener.onItemClick(cart[0])
//            }
//        }

    }

//    interface OnItemClickListener {
//        fun onItemClick(cartItemEntity: CartItemEntity)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
}