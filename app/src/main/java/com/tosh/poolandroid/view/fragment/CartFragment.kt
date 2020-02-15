package com.tosh.poolandroid.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.MainDatabase
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CartAdapter
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CartAdapter()
        adapter.notifyDataSetChanged()

        fetchDataFromCart()
        deleteCartItems()
    }


    private fun fetchDataFromCart() {

        (activity as MainActivity).setupToolbar(getString(R.string.shopping_cart))
        //recyclerView
        cartRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        launch {
            context.let {
                val cartItems = MainDatabase.getInstance(it!!)!!.cartItemDao().getCartItems()
                if (cartItems.isNotEmpty()) {
                    cartRv.adapter = CartAdapter(cartItems)
                    btnBuy.visibility = VISIBLE
                    emptyCart.visibility = VISIBLE
                    cartLl.visibility = VISIBLE
                } else {
                    cartEmpty.visibility = VISIBLE
                }
            }
        }
    }

    private fun deleteCartItems() {
        val placeholderFragment = PlaceholderFragment()
        emptyCart.setOnClickListener {
            launch {
                context.let {
                    MainDatabase.getInstance(it!!)!!.cartItemDao().deleteCart()
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.details_fragment, placeholderFragment)
                    transaction.commit()
                }
            }
        }
    }
}

