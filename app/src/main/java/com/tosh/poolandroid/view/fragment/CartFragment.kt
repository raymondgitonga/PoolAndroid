package com.tosh.poolandroid.view.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.MainDatabase
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CartAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_placeholder.*
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val adapter = CartAdapter()
        adapter.notifyDataSetChanged()

        fetchDataFromCart()
        deleteCartItems()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                cartEmpty.visibility = GONE
                val fm: FragmentManager? = fragmentManager
                fm!!.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun fetchDataFromCart() {

        (activity as MainActivity).setupToolbar(getString(R.string.shopping_cart))

        cartRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        mainViewModel!!.getCartTotal().observe(viewLifecycleOwner, Observer {total ->
           val grandTotal = total.toInt()
            totalCart.text = "Total $grandTotal KES"
        })

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
        val placeholderFragment = CartFragment()
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

