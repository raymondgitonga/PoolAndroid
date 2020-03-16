package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.adapter.OrderDetailsAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_order_details.*

class OrderDetailsFragment : Fragment() {


    private var mainViewModel: MainViewModel? = null
    private var orderDetailsRv: RecyclerView? = null
    private var orderAdapter: OrderDetailsAdapter? = null
    private var productId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        productId = arguments!!.getInt("ORDER_ID")

        getUserId(productId!!)


    }

    private fun getUserId(productId: Int) {
        var id: Int? = null
        mainViewModel!!.getUserDetails().observe(viewLifecycleOwner, Observer { userEntities ->
            for (i in userEntities.indices) {
                id = userEntities[i].id
            }
            initRecyclerView(id!!, productId)
        })
    }

    private fun initRecyclerView(userId: Int, productId: Int) {
        orderDetailsRv = view?.findViewById(R.id.orderDetailsRv)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        orderDetailsRv!!.layoutManager = linearLayoutManager

        mainViewModel!!.getOrders(userId, productId).observe(viewLifecycleOwner, Observer {
            orderDetailsProgress.visibility = GONE
            it.forEach { order ->
                totalDelivery.text = order.cost.toString()
                deliveryCost.text = order.deliveryCost.toString()
                deliveryGrandTotal.text = order.total.toString()
            }

            orderReceipts.visibility = VISIBLE

            orderAdapter = OrderDetailsAdapter(it, context!!)
            orderDetailsRv!!.adapter = orderAdapter
        })
    }
}
