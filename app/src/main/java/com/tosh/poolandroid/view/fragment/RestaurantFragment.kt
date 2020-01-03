package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Product
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CategoryAdapter
import com.tosh.poolandroid.view.adapter.ProductAdapter
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import java.util.*

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
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        productRecyclerView()
        setupToolBar()
        productClick()

    }

    private fun productClick(){
        categoryAdapter?.setOnProductItemClickListener(object : CategoryAdapter.CategoryProductListener{
            override fun onProductItemClick(headerPosition: Int, childPosition: Int, headerView: View, childView: View) {
                val productDetails = categoryAdapter!!.category[headerPosition].products[childPosition]

                mainViewModel!!.loadProductExtras(productDetails.id)
                    ?.observe(viewLifecycleOwner, Observer { extras ->
                        if (extras.isEmpty()){
                            Toast.makeText(view!!.context, "No extras",
                                Toast.LENGTH_SHORT).show()
                        }else {
                            // prints out list of extras
                            extras.forEach(System.out::print)
                        }
                    })
            }

        })
    }

    private fun productRecyclerView(){
        vendorID = arguments?.getInt("VENDOR_ID")

        recyclerView = view!!.findViewById(R.id.foodRv)
        categoryAdapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = categoryAdapter


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

        (activity as MainActivity).setupToolbar(vendorName.toString())
    }

}

