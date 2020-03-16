package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class OrderDetailsView(itemView: View, var order: List<Order>) : RecyclerView.ViewHolder(itemView) {

    }
}