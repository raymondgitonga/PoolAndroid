package com.tosh.poolandroid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.addLocationPreferences
import kotlinx.android.synthetic.main.fragment_places.*

class PlacesFragment : BaseDialogFragment() {

    lateinit var placesClient: PlacesClient

    lateinit var newLocation: (data:String)-> Unit
    lateinit var newLatLon: (data:ArrayList<String>)-> Unit

    private var placeFields =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    lateinit var latitiude: String
    lateinit var longitude: String

    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autocompleteFragment = activity!!.supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        initialisePlaces()
        setUpPlaces()
    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.TOP)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val f: AutocompleteSupportFragment? =activity!!.supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        if (f != null) activity!!.supportFragmentManager.beginTransaction().remove(f).commit()
    }

    private fun initialisePlaces() {
        Places.initialize(context!!, getString(R.string.google_api_key))
        placesClient = Places.createClient(context!!)
    }

    private fun setUpPlaces() {

        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latlng = (place.latLng).toString().split(",")
                latitiude = latlng[0].split("(")[1]
                longitude = latlng[1].split(")")[0]
                var arrLatLon = ArrayList<String>()
                arrLatLon.add(latitiude)
                arrLatLon.add(longitude)

                btnPlaces.setOnClickListener {
                    addLocationPreferences(context!!, latitiude, longitude)
                    place.name?.let {
                            placeName -> newLocation.invoke(placeName)
                    }
                    arrLatLon?.let{ latlon -> newLatLon.invoke(latlon)}
                    dismiss()
                }
            }

            override fun onError(status: Status) {
                Log.e("LOCATIONNN", "" + status.statusMessage)
            }

        })

    }


}
