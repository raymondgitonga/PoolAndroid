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



class CategoryAdapter (val context: Context):RecyclerView.Adapter<CategoryAdapter.CategoryView>(){

    var category : List<Category> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryView(view)
    }

    override fun getItemCount(): Int {
        return category.size
    }


    override fun onBindViewHolder(holder:CategoryView, position: Int) {
        val category = category[position]

        holder.categoryName.text = category.name

    }

    fun setCategories(category: List<Category>){
        this.category = category
    }

    class CategoryView(itemView: View, var category: Category? = null) : RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.category_title)

    }

}