package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRecyclerView()
    }

    private fun productRecyclerView(){

        val vendorName = arguments?.getString("VENDOR_NAME")
        val vendorID = arguments?.getInt("VENDOR_ID")

        //toolbar
        (activity as MainActivity).setToolBar(vendorName.toString())

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
}