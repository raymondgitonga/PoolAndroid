package com.tosh.poolandroid.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.MpesaRequest
import com.tosh.poolandroid.model.database.CartItemEntity
import com.tosh.poolandroid.model.database.MainDatabase
import com.tosh.poolandroid.util.getSharedPreferencesValue
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CartAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    var adapter: CartAdapter? = null
    lateinit var placeholderFragment: CartFragment
    lateinit var grandTotal: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        placeholderFragment = CartFragment()

        fetchDataFromCart()
        deleteAllCartItems()
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
        val phone = getSharedPreferencesValue(context!!, "phone")

        (activity as MainActivity).setupToolbar(getString(R.string.shopping_cart))

        cartRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        mainViewModel!!.getCartTotal().observe(viewLifecycleOwner, Observer { total ->
            grandTotal = total.toInt().toString()
            totalCart.text = "Total $grandTotal KES"
            val request = MpesaRequest(
                amount = grandTotal,
                phone = phone
            )
            MakeMpesaRequest(request)
        })

        launch {
            context.let {
                val cartItems = MainDatabase.getInstance(it!!)!!.cartItemDao().getCartItems()
                if (cartItems.isNotEmpty()) {
                    cartRv.adapter =
                        CartAdapter(cartItems as ArrayList<CartItemEntity>) { cartItems ->
                            deleteSingleCartItem(cartItems)
                        }

                    btnBuy.visibility = VISIBLE
                    emptyCart.visibility = VISIBLE
                    cartLl.visibility = VISIBLE
                } else {
                    cartEmpty.visibility = VISIBLE
                }
            }
        }
    }

    fun MakeMpesaRequest(request: MpesaRequest){
        btnBuy.setOnClickListener{
            mainViewModel!!.makeMpesaRequest(request).observe(viewLifecycleOwner, Observer {
                if (it.status == "Success"){

                }else{
                    Toasty.success(context!!, "Processing payment failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun deleteSingleCartItem(cartItem: CartItemEntity) {
        mainViewModel!!.deleteCartItem(cartItem.id)

        Handler().postDelayed({
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_fragment, placeholderFragment)
            transaction.commit()
        }, 400)

    }

    private fun deleteAllCartItems() {
        val emptyDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Clear Cart")
            .setMessage("Are you sure you want to clear your cart ?")
        emptyCart.setOnClickListener {
            emptyDialog.setPositiveButton("Yes") { _, _ ->
                launch {
                    context.let {
                        MainDatabase.getInstance(it!!)!!.cartItemDao().deleteCart()
                        val transaction = activity!!.supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.details_fragment, placeholderFragment)
                        transaction.commit()
                    }
                }
            }
                .setNegativeButton("No") { _, _ ->
                }

                .show()
        }
    }
}

