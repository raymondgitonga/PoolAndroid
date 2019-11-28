package com.tosh.poolandroid.view.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.fragment.VendorFragment
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.appbar_layout.*

class MainActivity : AppCompatActivity(){

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mainViewModel: MainViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLastLocation()
        createFormFragment()


    }

    private fun createFormFragment() {
        val freightFragment = VendorFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragment, freightFragment)
        transaction.commit()
    }

    fun setToolBar(title: String) {
        toolbar_title.text = title
    }

    private fun fetchLastLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)

            return
        }
        val task = fusedLocationProviderClient!!.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                latitude = currentLocation!!.latitude.toString()
                longitude = currentLocation!!.longitude.toString()
                addToSharedPreferences(latitude, longitude)
            }
        }

    }

    private fun addToSharedPreferences(latitude: String?, longitude: String?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        editor = pref!!.edit()
        editor!!.putString("latitude", latitude)
        editor!!.putString("longitude", longitude)
        editor!!.apply()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    private fun logout() {
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        settings.edit().remove("email").apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val REQUEST_CODE = 101
    }

    private fun loadUserDetails() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<View>(R.id.navigation_name) as TextView
        val navEmail = headerView.findViewById<View>(R.id.navigation_email) as TextView

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel!!.getUserDetails().observe(this, Observer { userEntities ->
            for (i in userEntities.indices) {
                navName.text = userEntities[i].name
                navEmail.text = userEntities[i].email
            }
        })

    }

}