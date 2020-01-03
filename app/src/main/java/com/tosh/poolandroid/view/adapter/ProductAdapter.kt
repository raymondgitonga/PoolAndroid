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
import com.tosh.poolandroid.model.Product


class ProductAdapter(private val context: Context, private val productModel: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductView(view)
    }

    override fun getItemCount(): Int {
        return productModel.size
    }

    override fun onBindViewHolder(holder: ProductView, position: Int) {
        var product = productModel[position]
        var price = product.price.toString() + " KSH"

        holder.foodName.text = product.productName
        holder.foodDesc.text = product.productDetails
        holder.foodPrice.text = price

        holder.product = listOf(product)

        Picasso.get()
            .load(productModel[position].imgUrl)
            .fit()
            .centerCrop()
            .into(holder.foodImg)

        holder.setClick(object : ItemClickListener {
            override fun onItemClickListener(view: View, position: Int) {
                Toast.makeText(context, "" + productModel[position].productName, Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    class ProductView(itemView: View, var product: List<Product>? = null) :
        RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val foodImg: ImageView = itemView.findViewById(R.id.foodImg)
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodDesc: TextView = itemView.findViewById(R.id.foodDesc)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        private val addToCart: ImageView = itemView.findViewById(R.id.addToCart)

        lateinit var listener: ItemClickListener

        fun setClick(listener: ItemClickListener) {
            this.listener = listener
        }

        init {
            addToCart.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickListener(v!!, adapterPosition)
        }
    }

    interface ItemClickListener {

        fun onItemClickListener(view: View, position: Int)
    }

}
