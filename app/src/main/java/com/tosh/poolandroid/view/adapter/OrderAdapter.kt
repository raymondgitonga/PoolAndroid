package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Order
import com.tosh.poolandroid.util.getAddress
import java.text.SimpleDateFormat
import java.util.*


class OrderAdapter(private val orderModel: List<Order>, private val context: Context):  RecyclerView.Adapter<OrderAdapter.OrderView>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderView {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.row_order, parent, false)

        return OrderView(view, orderModel)
    }

    override fun getItemCount(): Int = orderModel.size

    override fun onBindViewHolder(holder: OrderView, position: Int) {
        val order = orderModel[position]
        val latitude = order.latitude.toDouble()
        val longitude = order.longitude.toDouble()
        val date = order.createdDate

        val formatDate: Date = SimpleDateFormat("yyyyMMddHHmmss").parse(date)
        val targetFormat = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = targetFormat.format(formatDate)

        holder.deliveryLocation.text = getAddress(context, latitude, longitude)
        holder.deliveryPrice.text = "${order.total} KES"
        holder.deliverDate.text = formattedDate

        if (order.state == "pending"){
            holder.inProgressLl.visibility = VISIBLE
        }else{
            holder.completedLl.visibility = VISIBLE
        }

    }

    class OrderView(itemView: View, var order: List<Order>) : RecyclerView.ViewHolder(itemView) {
        val driverImg = itemView.findViewById<ImageView>(R.id.driverImg)
        val driverName = itemView.findViewById<TextView>(R.id.driverName)
        val driverPlate = itemView.findViewById<TextView>(R.id.driverPlate)
        val deliveryLocation = itemView.findViewById<TextView>(R.id.deliveryLocation)
        val deliveryPrice = itemView.findViewById<TextView>(R.id.deliveryPrice)
        val deliverDate = itemView.findViewById<TextView>(R.id.deliverDate)
        val completedLl = itemView.findViewById<LinearLayout>(R.id.completed)
        val inProgressLl = itemView.findViewById<LinearLayout>(R.id.inProgress)
    }
}