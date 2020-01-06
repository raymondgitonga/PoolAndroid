package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.adapter.ExtraAdapter
import com.tosh.poolandroid.viewmodel.MainViewModel



class ExtrasFragment: DialogFragment() {

    internal lateinit var rootView: View
    var productDetailsId: Int? = null


    private var mainViewModel: MainViewModel? = null
    private var extraAdapter: ExtraAdapter? = null
    lateinit var extraRv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_extra, container, false)
        dialog!!.window.requestFeature(Window.FEATURE_NO_TITLE);
        dialog!!.setCanceledOnTouchOutside(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        productClick()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window
        window.setLayout(1000, 600)
        window.setGravity(Gravity.CENTER)
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