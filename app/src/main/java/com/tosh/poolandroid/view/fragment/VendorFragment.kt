package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.net.PlacesClient
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Vendor
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.view.adapter.VendorAdapter.OnItemClickListener
import com.tosh.poolandroid.viewmodel.MainViewModel

class VendorFragment: Fragment() {

    private var vendorAdapter: VendorAdapter? = null
    private var mainViewModel: MainViewModel? = null
    private var vendorsRv: RecyclerView? = null
    lateinit var placesClient:PlacesClient


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vendor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVendors()

    }

    private fun setupVendors(){

        //toolbar
        (activity as MainActivity).setToolBar("Choose Vendor")


        //recyclerView
        vendorsRv = view?.findViewById(R.id.vendorsRv)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        vendorsRv!!.layoutManager = gridLayoutManager

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel!!.loadVendors()?.observe(viewLifecycleOwner, Observer { vendors ->
            vendorAdapter = VendorAdapter(vendors)
            vendorsRv!!.adapter = vendorAdapter
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
                    Log.e("Clicked","AIII")
                }


            }

        })
    }

//    private fun setupPlacesAutoComplete(){
//
//        var placeFields = mutableListOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
//        Places.initialize(view!!.context, getString(R.string.google_api_key))
//        placesClient = Places.createClient(view!!.context)
//
//        val autoCompleteFragment =
//            activity?.supportFragmentManager?.findFragmentById(R.id.autoCompleteFragment) as? AutocompleteSupportFragment
//
//        autoCompleteFragment?.setPlaceFields(placeFields)
//
//        autoCompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener{
//            override fun onPlaceSelected(place: Place) {
//                Toast.makeText(view!!.context, ""+place.address, Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun onError(status: Status) {
//                Toast.makeText(view!!.context, ""+status.statusMessage, Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
}