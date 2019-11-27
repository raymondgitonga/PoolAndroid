package com.tosh.poolandroid.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.view.adapter.CategoryAdapter
import com.tosh.poolandroid.R
import com.tosh.poolandroid.viewmodel.MainViewModel





class VendorFoodActivity : AppCompatActivity(){

    private var mainViewModel: MainViewModel? = null
    private var categoryAdapter: CategoryAdapter? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)
        productRecyclerView()
    }


    private fun productRecyclerView(){

        recyclerView = findViewById(R.id.foodRv)
        categoryAdapter = CategoryAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryAdapter

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel!!.loadCategories(intent.getIntExtra("VENDOR_ID", 0))?.observe(this, Observer { categories ->
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@VendorFoodActivity, RecyclerView.VERTICAL, false)
                categoryAdapter!!.setCategories(categories)
                categoryAdapter!!.notifyDataSetChanged()
            }
        })
    }
}

