package com.tosh.poolandroid.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.FadingCircle
import com.tosh.poolandroid.R
import com.tosh.poolandroid.model.Extra
import com.tosh.poolandroid.view.adapter.ExtraAdapter

import com.tosh.poolandroid.viewmodel.MainViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_extra.*
import kotlinx.android.synthetic.main.dialog_extra.spin_kit
import kotlinx.android.synthetic.main.fragment_restaurant.*


class ExtrasFragment: DialogFragment() {

    private lateinit var rootView: View
    var productDetailsId: Int? = null


    private var mainViewModel: MainViewModel? = null
    private var extraAdapter: ExtraAdapter? = null
    lateinit var extraRv: RecyclerView
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_extra, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog!!.window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        productClick()
        extraOnClick()
        setupProgressBar()

        btnExtra.setOnClickListener {
            Toasty.error(view!!.context, "Nothing selected", Toast.LENGTH_LONG, true).show()
            dismiss()
        }

    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window
        window.setLayout(900, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        progressBar.visibility = VISIBLE
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
                            progressBar.visibility = GONE
                            extraRv.apply {
                                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                                extraAdapter!!.setExtras(extras)
                                extraAdapter!!.notifyDataSetChanged()
                            }
                        }
                    })
        }

    private fun extraOnClick(){
        extraAdapter?.setOnItemClickListener(object : ExtraAdapter.OnItemClickListener{
            override fun onItemClick(extraModel: Extra) {
                btnExtra.setOnClickListener {
                    dismiss()
                    Toasty.success(view!!.context, "Added to cart", Toast.LENGTH_LONG, true).show()
                }
            }

        })
    }

    fun setupProgressBar(){
        progressBar = spin_kit as ProgressBar
        val wave = FadingCircle()
        progressBar.indeterminateDrawable = wave
    }
}