package com.tosh.poolandroid.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.FadingCircle
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.database.CartItemEntity
import com.tosh.poolandroid.util.vibrate
import com.tosh.poolandroid.view.activity.MainActivity
import com.tosh.poolandroid.view.adapter.CategoryAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.coroutines.launch

class RestaurantFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private var categoryAdapter: CategoryAdapter? = null
    lateinit var recyclerView: RecyclerView
    private var vendorName: String? = null
    private var vendorID: Int? = null
    private var vendorBanner: String? = null
    lateinit var extraDialog: Dialog
    var productId: Int? = null
    lateinit var progressBar: ProgressBar
    lateinit var extraFragment: ExtrasFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        vendorName = arguments?.getString("VENDOR_NAME")

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        extraDialog = Dialog(requireActivity())
        extraFragment = ExtrasFragment()



        productRecyclerView()
        setupToolBar()
        productClick()
        setupProgressBar()
    }

    private fun productClick() {
        categoryAdapter?.setOnProductItemClickListener(object :
            CategoryAdapter.CategoryProductListener {
            override fun onProductItemClick(
                headerPosition: Int,
                childPosition: Int,
                headerView: View,
                childView: View
            ) {
                progressBar.visibility = VISIBLE
                val product = categoryAdapter!!.category[headerPosition].products[childPosition]

                productId = product.id

                getCartItemCount(productId!!)

                mainViewModel!!.loadProductExtras(productId!!)
                    ?.observe(viewLifecycleOwner, Observer { extras ->
                        if (extras.isEmpty()) {
                            launch {
                                val cartItems = CartItemEntity(
                                    id = 0,
                                    productId = productId!!,
                                    productName = product.productName,
                                    productPrice = product.price,
                                    extraId = 3,
                                    extraName = "",
                                    extraPrice = 0.0,
                                    productQuantity = 0,
                                    vendorId = product.vendorId,
                                    total = product.price + 0.0,
                                    vendorName = vendorName!!

                                )
                                mainViewModel!!.insert(cartItems)
                            }
                            progressBar.visibility = GONE
                            vibrate(context!!, 80)
                            Toasty.success(
                                view!!.context,
                                "Added to cart",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        } else {
                            val cartItem = CartItemEntity(
                                id = 0,
                                productId = productId!!,
                                productName = product.productName,
                                productPrice = product.price,
                                extraId = 0,
                                extraName = null,
                                extraPrice = 0.0,
                                productQuantity = null,
                                vendorId = product.vendorId,
                                total = product.price + 0.0,
                                vendorName = vendorName!!
                            )
                            progressBar.visibility = GONE
                            showExtraDialog(
                                cartItem.productId, cartItem.productName,
                                cartItem.productPrice, cartItem.vendorId, cartItem.vendorName
                            )
                        }
                    })
            }

        })
    }

    fun getCartItemCount(productId: Int) {
        var itemCount: Int? = null

        mainViewModel!!.getCartItemCount(productId)!!.observe(viewLifecycleOwner, Observer {
            itemCount = it
        })
    }

    fun showExtraDialog(productId: Int, productName: String, productPrice: Double, vendorId: Int, vendorName:String) {
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("dialog")

        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        val bundle = Bundle()
        bundle.putInt("ID", productId)
        bundle.putString("NAME", productName)
        bundle.putDouble("PRICE", productPrice)
        bundle.putInt("VENDOR_ID", vendorId)
        bundle.putString("VENDOR_NAME", vendorName)
        fragmentTransaction.addToBackStack(null)
        val fragmentExtras = ExtrasFragment()
        fragmentExtras.arguments = bundle
        fragmentExtras.show(fragmentTransaction, "extras")
    }

    private fun productRecyclerView() {
        restaurantPlaceholder.startShimmerAnimation()

        vendorID = arguments?.getInt("VENDOR_ID")
        vendorBanner = arguments?.getString("VENDOR_BANNER")

//        vendorImage.loadImage(vendorBanner, getProgressDrawable(context!!))

        recyclerView = view!!.findViewById(R.id.foodRv)
        categoryAdapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = categoryAdapter


        mainViewModel!!.loadCategories(vendorID!!)!!.observe(
            viewLifecycleOwner,
            Observer { categories ->
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    categoryAdapter!!.setCategories(categories)
                    restaurantPlaceholder.stopShimmerAnimation()
                    restaurantPlaceholder.visibility = GONE
                    categoryAdapter!!.notifyDataSetChanged()
                }
            })
    }

    private fun setupToolBar() {
        (activity as MainActivity).setupToolbar(vendorName.toString())
    }

    private fun setupProgressBar() {
        progressBar = spin_kit as ProgressBar
        val wave = FadingCircle()
        progressBar.indeterminateDrawable = wave
    }

}

