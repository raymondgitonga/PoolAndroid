package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Product
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.view.adapter.VendorAdapter.OnItemClickListener
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_vendor.*

class VendorFragment(): Fragment() {

    private var vendorAdapter: VendorAdapter? = null
    private var mainViewModel: MainViewModel? = null
    private var vendorsRv: RecyclerView? = null
    private var textLocation: TextView? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vendor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup(){
        setupVendors()
    }

    private fun setupVendors(){

        vendorPlaceholder.startShimmerAnimation()

        //Toolbar
        (activity as MainActivity).setupToolbar(getString(R.string.choose_vendor))

        //recyclerView
        vendorsRv = view?.findViewById(R.id.vendorsRv)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        vendorsRv!!.layoutManager = gridLayoutManager

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel!!.loadVendors()?.observe(viewLifecycleOwner, Observer { vendors ->
            vendorAdapter = VendorAdapter(vendors)
            vendorsRv!!.adapter = vendorAdapter
            vendorPlaceholder.stopShimmerAnimation()
            vendorPlaceholder.visibility = GONE
            loadFragment()
        })
    }

    private fun loadFragment(){

        val VENDOR_NAME = "VENDOR_NAME"
        val VENDOR_ID = "VENDOR_ID"

        vendorAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(vendor: Vendor) {
                val fragmentRestaurant = RestaurantFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                if (vendor.category == "food"){
                    val bundle = Bundle()
                    bundle.putString(VENDOR_NAME, vendor.name)
                    bundle.putInt(VENDOR_ID, vendor.id)
                    fragmentRestaurant.arguments = bundle
                    fragmentTransaction.replace(R.id.details_fragment, fragmentRestaurant)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }else{
                    Log.e("Clicked","Shopping")
                }


            }

        })
    }


}

