package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tosh.poolandroid.R
import com.tosh.poolandroid.VendorProductActivity
import com.tosh.poolandroid.model.Vendor



class VendorAdapter(private val context: Context, private val vendorModel: List<Vendor>) : RecyclerView.Adapter<VendorAdapter.VendorView>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorView {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_vendor, parent, false)
        return VendorView(view)
    }

    override fun onBindViewHolder(holder: VendorView, position: Int) {

        val vendor = vendorModel[position]

       holder.vendorName.text = vendor.name


        Picasso.get()
                .load(vendorModel[position].img_url)
                .fit()
                .centerCrop()
                .into(holder.vendorImage)

        holder.vendor = vendor
        
    }

    override fun getItemCount(): Int {
        return vendorModel.size
    }

    class VendorView(itemView: View, var vendor: Vendor? = null) : RecyclerView.ViewHolder(itemView) {

        companion object{
            val VENDOR_NAME = "VENDOR_NAME"
            val VENDOR_ID = "VENDOR_ID"
        }

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, VendorProductActivity::class.java)

                intent.putExtra(VENDOR_NAME, vendor?.name)
                intent.putExtra(VENDOR_ID, vendor?.id)

                itemView.context.startActivity(intent)
            }
        }

        val vendorImage: ImageView = itemView.findViewById<View>(R.id.vendor_image) as ImageView
        val vendorName: TextView = itemView.findViewById(R.id.vendor_name)

    }
}
