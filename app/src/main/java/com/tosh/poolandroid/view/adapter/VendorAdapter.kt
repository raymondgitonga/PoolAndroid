package com.tosh.poolandroid.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolandroid.util.getProgressDrawable
import com.tosh.poolandroid.util.loadImage

class VendorAdapter(private val vendorModel: List<Vendor>) : RecyclerView.Adapter<VendorAdapter.VendorView>() {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_vendor, parent, false)
        return VendorView(view, vendorModel, listener)
    }

    override fun onBindViewHolder(holder: VendorView, position: Int) {

        val vendor = vendorModel[position]

        holder.vendorName.text = vendor.name
        holder.vendorImage.loadImage(
            vendorModel[position].img_url,
            getProgressDrawable(holder.vendorImage.context)
        )
        holder.vendor = listOf(vendor)
    }

    override fun getItemCount(): Int {
        return vendorModel.size
    }

    class VendorView(itemView: View, var vendor: List<Vendor>, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {


        val vendorImage: ImageView = itemView.findViewById<View>(R.id.vendor_image) as ImageView
        val vendorName: TextView = itemView.findViewById(R.id.vendor_name)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(vendor[0])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(vendorModel: Vendor)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}
