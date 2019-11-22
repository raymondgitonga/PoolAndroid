package com.tosh.poolandroid

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.model.Category



class CategoryAdapter (private val categoryModel: List<Category>):RecyclerView.Adapter<CategoryAdapter.CategoryView>(){

    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryView(view)
    }

    override fun getItemCount(): Int {
        return categoryModel.size
    }


    override fun onBindViewHolder(holder:CategoryView, position: Int) {
        val category = categoryModel[position]

        holder.categoryName.text = category.name

        var layoutManager = LinearLayoutManager(
                holder.productRv.context,
                RecyclerView.VERTICAL,
                false
        )

        layoutManager.initialPrefetchItemCount = 4

        var productAdapter = ProductAdapter(category.products)

        holder.productRv.layoutManager = layoutManager
        holder.productRv.adapter = productAdapter
        holder.productRv.setRecycledViewPool(viewPool)

    }

    class CategoryView(itemView: View, var category: Category? = null) : RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.category_title)

        val productRv : RecyclerView = itemView.findViewById(R.id.productRv)
    }

}