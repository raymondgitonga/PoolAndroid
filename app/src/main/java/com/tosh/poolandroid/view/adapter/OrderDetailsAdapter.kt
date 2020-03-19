package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Order

class OrderDetailsAdapter(private val orderModel: List<Order>, private val context: Context) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsView>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_order_details, parent, false)

        return OrderDetailsView(view, orderModel)
    }

    override fun getItemCount(): Int = orderModel.size

    override fun onBindViewHolder(holder: OrderDetailsView, position: Int) {
        val cart = orderModel[position].cartItems

        cart.forEach {
            holder.productName.text = it.productName
            holder.productPrice.text = it.productPrice.toString()  +" KES"
            holder.productExtra.text = it.extraName
        }
    }

    class OrderDetailsView(itemView: View, var order: List<Order>) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById<TextView>(R.id.productName)
        val productPrice = itemView.findViewById<TextView>(R.id.productPrice)
        val productExtra = itemView.findViewById<TextView>(R.id.productExtra)

    }
}