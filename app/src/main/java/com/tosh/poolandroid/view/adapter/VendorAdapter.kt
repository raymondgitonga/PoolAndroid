package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Vendor

class VendorAdapter(private val context: Context, private val vendorModel: List<Vendor>) : RecyclerView.Adapter<VendorAdapter.VendorView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorView {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_vendor, parent, false)
        return VendorView(view)
    }

    override fun onBindViewHolder(holder: VendorView, position: Int) {
       holder.vendorName.text = vendorModel[position].name

        Picasso.get()
                .load(vendorModel[position].img_url)
                .fit()
                .centerCrop()
                .into(holder.vendorImage)

        holder.vendorImage.setOnClickListener {
            Toast.makeText(context, ""+vendorModel[position].name, Toast.LENGTH_SHORT).show()
        }




    }

    override fun getItemCount(): Int {
        return vendorModel.size
    }

    inner class VendorView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val vendorImage: ImageView = itemView.findViewById<View>(R.id.vendor_image) as ImageView
        val vendorName: TextView = itemView.findViewById(R.id.vendor_name)

    }
}

