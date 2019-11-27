package com.tosh.poolandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel

class VendorFragment: Fragment() {

    private var vendorAdapter: VendorAdapter? = null
    private var mainViewModel: MainViewModel? = null
    private var vendorsRv: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vendor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVendors()

    }

    private fun setupVendors(){
        //recyclerView
        vendorsRv = view?.findViewById(R.id.vendorsRv)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        vendorsRv!!.layoutManager = gridLayoutManager

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        mainViewModel!!.loadVendors()?.observe(this, Observer { vendors ->
            vendorAdapter = VendorAdapter(vendors)
            vendorsRv!!.adapter = vendorAdapter
        })
    }
}