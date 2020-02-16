package com.tosh.poolandroid.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.CartItemEntity

class CartAdapter(
    val cartItems: List<CartItemEntity>? = null) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

//    private lateinit var listener: OnItemClickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view, cartItems!!)
    }

    override fun getItemCount() = cartItems!!.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems?.get(position)

        holder.productName.text = cartItem!!.productName
        holder.extraName.text = cartItem.extraName
        holder.productPrice.text = cartItem.productPrice.toInt().toString()


        holder.cart = listOf(cartItem)
    }

    class CartViewHolder(val view: View, var cart: List<CartItemEntity>) : RecyclerView.ViewHolder(view){
        val productName: TextView = view.findViewById(R.id.tvProduct)
        val extraName: TextView = view.findViewById(R.id.tvExtra)
        val productPrice: TextView = view.findViewById(R.id.tvPrice)
        val cartRemove: TextView = view.findViewById(R.id.cartRemove)
//        var onItemClickListener: OnItemClickListener


//        init {
//            this.onItemClickListener = onItemClickListener!!
//            cartRemove.setOnClickListener(this)
//        }

//        override fun onClick(v: View?) {
//            onItemClickListener.onItemClick(adapterPosition)
//        }


    }

//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }




}