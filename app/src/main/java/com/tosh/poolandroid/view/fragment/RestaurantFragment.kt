package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CategoryAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel



class RestaurantFragment: Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var categoryAdapter: CategoryAdapter? = null
    lateinit var recyclerView: RecyclerView
    private var vendorName: String? = null
    private var vendorID: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRecyclerView()
        setupToolBar()
    }

    private fun productRecyclerView(){
        vendorID = arguments?.getInt("VENDOR_ID")

        recyclerView = view!!.findViewById(R.id.foodRv)
        categoryAdapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = categoryAdapter



        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel!!.loadCategories(vendorID!!)!!.observe(viewLifecycleOwner, Observer { categories ->
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                categoryAdapter!!.setCategories(categories)
                categoryAdapter!!.notifyDataSetChanged()
            }
        })
    }

    fun setupToolBar(){
        vendorName = arguments?.getString("VENDOR_NAME")

        (activity as MainActivity).setToolBar(vendorName.toString())
        (activity as MainActivity).setLocationVisibility(View.GONE)
    }
}