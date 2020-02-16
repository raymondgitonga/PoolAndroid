package com.tosh.poolandroid.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.CartItemEntity
import kotlinx.android.synthetic.main.cart_item.view.*


class CartAdapter(private val cartItems: ArrayList<CartItemEntity>? = null, private val clickListener: (CartItemEntity) -> Unit) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount() = cartItems!!.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems!![position], clickListener)
    }

    class CartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind( cart: CartItemEntity,clickListener: (CartItemEntity) -> Unit ){
            view.tvProduct.text = cart.productName
            view.tvExtra.text = cart.extraName
            view.tvPrice.text = cart.productPrice.toInt().toString()

            view.cartRemove.setOnClickListener {
                clickListener(cart)
            }
        }

    }


}