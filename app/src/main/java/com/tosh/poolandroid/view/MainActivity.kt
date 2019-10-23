package com.tosh.poolandroid.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.viewmodel.UserViewModel
import com.tosh.poolandroid.viewmodel.VendorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fab_layout.*
import kotlinx.android.synthetic.main.navigation_drawer.*


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var vendorsRv: RecyclerView? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var vendorAdapter: VendorAdapter? = null
    private var userViewModel: UserViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        visibilityActivity()

        initialize()

        cartFab()

        fetchLastLocation()

        loadUserDetails()

    }

    private fun cartFab() {
        cart_fab.setOnClickListener { Toast.makeText(this@MainActivity, "Cart clicked", Toast.LENGTH_SHORT).show() }
    }

    private fun initialize() {

//        //tool bar
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        //navigation drawer
        navigation_view!!.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolBar,
                R.string.open_drawer, R.string.close_drawer)
        drawer_layout!!.addDrawerListener(toggle)

        toggle.syncState()


        //recyclerView
        vendorsRv = findViewById(R.id.vendorsRv)

        val gridLayoutManager = GridLayoutManager(this, 2)
        vendorsRv!!.layoutManager = gridLayoutManager

        val vendorViewModel = ViewModelProviders.of(this).get(VendorViewModel::class.java)

        vendorViewModel.vendor.observe(this, Observer { vendors ->
            vendorAdapter = VendorAdapter(this, vendors)
            vendorsRv!!.adapter = vendorAdapter
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.orders_navigation -> Toast.makeText(this, "Orders clicked", Toast.LENGTH_SHORT).show()
            R.id.profile_navigation -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            R.id.settings_navigation -> Toast.makeText(this, "Address clicked", Toast.LENGTH_SHORT).show()
            R.id.info_navigation -> Toast.makeText(this, "Info clicked", Toast.LENGTH_SHORT).show()
            R.id.share_navigation -> Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()
            R.id.help_navigation -> Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show()
            R.id.logout_navigation -> logout()
        }
        drawer_layout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

    private fun loadUserDetails() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<View>(R.id.navigation_name) as TextView
        val navEmail = headerView.findViewById<View>(R.id.navigation_email) as TextView

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel!!.getUserDetails().observe(this, Observer { userEntities ->
            for (i in userEntities.indices) {
                navName.text = userEntities[i].name
                navEmail.text = userEntities[i].email
            }
        })

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

    private fun visibilityActivity() {
        activityMain.visibility = View.VISIBLE
    }

}
