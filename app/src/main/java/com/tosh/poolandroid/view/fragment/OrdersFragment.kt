package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.OrderAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : Fragment() {

    private var mainViewModel: MainViewModel? = null
    private var ordersRv: RecyclerView? = null
    private var orderAdapter: OrderAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setupToolbar(getString(R.string.orders))

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        getUserId()
    }

    private fun getUserId(){
        var id: Int? = null
        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                id = userEntities[i].id
            }
            initRecyclerView(id!!)
        })
    }
    private fun initRecyclerView(userId:Int){
        ordersRv = view?.findViewById(R.id.ordersRv)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        ordersRv!!.layoutManager = linearLayoutManager

        mainViewModel!!.getOrders(userId, null).observe(viewLifecycleOwner, Observer {
            ordersProgress.visibility = GONE
            orderAdapter = OrderAdapter(it, context!!)
            ordersRv!!.adapter = orderAdapter
        })

    }
}
