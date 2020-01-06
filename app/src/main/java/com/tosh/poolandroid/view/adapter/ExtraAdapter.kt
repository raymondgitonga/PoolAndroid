package com.tosh.poolandroid.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Extra


class ExtraAdapter (private val extraModel: List<Extra>) : RecyclerView.Adapter<ExtraAdapter.ExtraView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraAdapter.ExtraView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.extra_item, parent, false)
        return ExtraAdapter.ExtraView(view, extraModel)
    }

    override fun onBindViewHolder(holder: ExtraView, position: Int) {

        val extra = extraModel[position]

        holder.extraName.text = extra.name
        holder.extraPrice.text = extra.price.toString()+ " KES"


        holder.extra = listOf(extra)
    }

    override fun getItemCount(): Int {
        return extraModel.size
    }

    class ExtraView(itemView: View, var extra: List<Extra>) : RecyclerView.ViewHolder(itemView) {
        val extraName: TextView = itemView.findViewById<View>(R.id.extraName) as TextView
        val extraPrice: TextView = itemView.findViewById<View>(R.id.extraPrice) as TextView
    }
}