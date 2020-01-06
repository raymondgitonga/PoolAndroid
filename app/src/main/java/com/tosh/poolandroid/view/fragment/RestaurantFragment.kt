package com.tosh.poolandroid.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.tosh.poolandroid.view.adapter.ExtraAdapter
import com.tosh.poolandroid.view.adapter.ProductAdapter
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_vendor.*
import java.util.*

class RestaurantFragment: Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var categoryAdapter: CategoryAdapter? = null
    lateinit var recyclerView: RecyclerView
    private var vendorName: String? = null
    private var vendorID: Int? = null
    lateinit var extraDialog: Dialog
    var  productDetails: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        extraDialog = Dialog(requireActivity())
        productRecyclerView()
        setupToolBar()
        productClick()

    }

    private fun productClick(){
        categoryAdapter?.setOnProductItemClickListener(object : CategoryAdapter.CategoryProductListener{
            override fun onProductItemClick(headerPosition: Int, childPosition: Int, headerView: View, childView: View) {
                productDetails = categoryAdapter!!.category[headerPosition].products[childPosition].id

                mainViewModel!!.loadProductExtras(productDetails!!)
                    ?.observe(viewLifecycleOwner, Observer { extras ->
                        if (extras.isEmpty()){
                            Toasty.success(view!!.context, "Added to cart", Toast.LENGTH_SHORT, true).show()
                        }else {
                            showExtraDialog()
                        }
                    })
            }

        })
    }

    fun showExtraDialog(){
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("dialog")

        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        val PRODUCT_ID = "PRODUCT_ID"
        val bundle = Bundle()
        bundle.putInt( PRODUCT_ID,productDetails!!)


        fragmentTransaction.addToBackStack(null)
        val fragmentExtras = ExtrasFragment()
        fragmentExtras.arguments = bundle
        fragmentExtras.show(fragmentTransaction, "extras")
    }

    private fun productRecyclerView(){
        restaurantPlaceholder.startShimmerAnimation()

        vendorID = arguments?.getInt("VENDOR_ID")

        recyclerView = view!!.findViewById(R.id.foodRv)
        categoryAdapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = categoryAdapter


        mainViewModel!!.loadCategories(vendorID!!)!!.observe(viewLifecycleOwner, Observer { categories ->
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                categoryAdapter!!.setCategories(categories)
                restaurantPlaceholder.stopShimmerAnimation()
                restaurantPlaceholder.visibility = View.GONE
                categoryAdapter!!.notifyDataSetChanged()
            }
        })
    }

    fun setupToolBar(){
        vendorName = arguments?.getString("VENDOR_NAME")

        (activity as MainActivity).setupToolbar(vendorName.toString())
    }

}

