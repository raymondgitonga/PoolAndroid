package com.tosh.poolandroid.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Category


class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryView>(), ProductAdapter.ProductClickListener {

    var category: List<Category> = listOf()
    private val viewPool = RecyclerView.RecycledViewPool()
    val context: Context? = null
    lateinit var categoryProductListener: CategoryProductListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return CategoryView(view)
    }

    override fun getItemCount(): Int {
        return category.size
    }


    override fun onBindViewHolder(holder: CategoryView, position: Int) {
        val category = category[position]

        holder.categoryName.text = category.name

        val categoryLayoutManager =
            LinearLayoutManager(holder.productRv.context, LinearLayoutManager.VERTICAL, false)

        categoryLayoutManager.initialPrefetchItemCount = 5

        holder.productRv.apply {
            layoutManager = categoryLayoutManager
            adapter = ProductAdapter(context,category.products , position,holder.itemView , this@CategoryAdapter)
            setRecycledViewPool(viewPool)
        }

    }

    fun setOnProductItemClickListener(categoryProductListener: CategoryProductListener) {
        this.categoryProductListener = categoryProductListener
    }

    interface CategoryProductListener{
        fun onProductItemClick(headerPosition: Int,childPosition:Int, headerView: View,childView: View)
    }

    fun setCategories(category: List<Category>) {
        this.category = category
    }

    class CategoryView(itemView: View, var category: Category? = null) :
        ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_title)
        val productRv: RecyclerView = itemView.findViewById(R.id.productRv)

        }

    override fun onItemClick(rootPoition: Int, childPosition: Int, headerView: View, childView: View) {
        categoryProductListener.onProductItemClick(rootPoition,childPosition,headerView,childView)
    }

    }
