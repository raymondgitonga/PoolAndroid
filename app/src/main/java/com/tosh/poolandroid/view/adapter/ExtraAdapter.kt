package com.tosh.poolandroid.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Category
import com.tosh.poolandroid.model.Extra


class ExtraAdapter  : RecyclerView.Adapter<ExtraAdapter.ExtraView>() {

    var extra: List<Extra> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraAdapter.ExtraView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.extra_item, parent, false)
        return ExtraAdapter.ExtraView(view)
    }

    override fun onBindViewHolder(holder: ExtraView, position: Int) {

        val extra = extra[position]

        holder.extraName.text = extra.name
        holder.extraPrice.text = extra.price.toString()+ " KES"

    }

    override fun getItemCount(): Int {
        return extra.size
    }

    fun setExtras(extra: List<Extra>) {
        this.extra = extra
    }

    class ExtraView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val extraName: TextView = itemView.findViewById<View>(R.id.extraName) as TextView
        val extraPrice: TextView = itemView.findViewById<View>(R.id.extraPrice) as TextView
    }
}