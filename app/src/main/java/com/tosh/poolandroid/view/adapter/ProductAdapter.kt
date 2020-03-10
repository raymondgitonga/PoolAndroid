package com.tosh.poolandroid.view.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Product

class ProductAdapter(private val context: Context, private val productModel: List<Product>, val rootPosition:Int, val headerView:View, productClickListener: ProductClickListener) :
    RecyclerView.Adapter<ProductAdapter.ProductView>() {

    var productClickListener: ProductClickListener = productClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row, parent, false)
        return ProductView(view)
    }

    override fun getItemCount(): Int {
        return productModel.size
    }

    override fun onBindViewHolder(holder: ProductView, position: Int) {
        var product = productModel[position]
        var price = product.price.toInt().toString() + " KSH"

        holder.foodName.text = product.productName
        holder.foodDesc.text = product.productDetails
        holder.foodPrice.text = price
        holder.product = listOf(product)

    }

    inner class ProductView(itemView: View, var product: List<Product>? = null) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodDesc: TextView = itemView.findViewById(R.id.foodDesc)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        private val addToCart: LinearLayout = itemView.findViewById(R.id.addToCart)



        init {
            addToCart.setOnClickListener(this)
        }

        override fun onClick(childView: View) {
            productClickListener.onItemClick(rootPosition,adapterPosition, headerView,childView)
        }
    }

    interface ProductClickListener {
        fun onItemClick(rootPoition:Int,childPosition:Int,headerView: View ,childView: View)
    }



}
