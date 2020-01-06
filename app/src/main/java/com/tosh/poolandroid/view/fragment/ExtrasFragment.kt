package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.adapter.CategoryAdapter
import com.tosh.poolandroid.view.adapter.ExtraAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_restaurant.*

class ExtrasFragment: DialogFragment() {

    internal lateinit var rootView: View
    var productDetailsId: Int? = null


    private var mainViewModel: MainViewModel? = null
    private var extraAdapter: ExtraAdapter? = null
    lateinit var extraRv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_extra, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        productClick()
    }

    private fun productClick(){

        extraRv = view!!.findViewById(R.id.extraRv)
        extraAdapter = ExtraAdapter()
        extraRv.layoutManager = LinearLayoutManager(activity)
        extraRv.adapter = extraAdapter

        productDetailsId = arguments?.getInt("PRODUCT_ID")
        mainViewModel!!.loadProductExtras(productDetailsId!!)
                    ?.observe(viewLifecycleOwner, Observer { extras ->
                        if (extras.isEmpty()){

                        }else {
                            extraRv.apply {
                                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                                extraAdapter!!.setExtras(extras)
                                extraAdapter!!.notifyDataSetChanged()
                            }
                        }
                    })
        }
}