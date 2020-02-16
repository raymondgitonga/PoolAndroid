package com.tosh.poolandroid.view.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.view.fragment.CartFragment
import com.tosh.poolandroid.view.fragment.VendorFragment
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_layout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mainViewModel: MainViewModel? = null
    private val vendorFragment = VendorFragment()
    private val cartFragment = CartFragment()
    lateinit var placesClient: PlacesClient

    private var placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setUpPlaces()
        fetchLastLocation()
        createFormFragment()
        setupDrawer()
        loadUserDetails()
        bottomNavigation()
        initialisePlaces()
        locationOnClick()
        cartFragment()
    }

    private fun createFormFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragment, vendorFragment)
        transaction.commit()
    }

    private fun setupDrawer(){
        setSupportActionBar(toolBar)
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
           this, drawerLayout, toolBar, R.string.open_drawer, R.string.close_drawer){}
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }

    fun setupToolbar(title: String){
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = title
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.orders_navigation -> {
                Toast.makeText(this, "Orders clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.profile_navigation -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.settings_navigation -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.info_navigation -> {
                Toast.makeText(this, "Info clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.share_navigation -> {
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.help_navigation -> {
                Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.logout_navigation -> {
                logout()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    private fun bottomNavigation(){
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNav)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_bottom -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.details_fragment, vendorFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                R.id.profile_bottom ->{
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                }

                R.id.help_bottom ->{
                    Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
    }

    private fun cartFragment(){
        topbarCart.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_fragment, cartFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    
    private fun fetchLastLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private fun initialisePlaces(){
        Places.initialize(applicationContext, getString(R.string.google_api_key))
        placesClient = Places.createClient(this)
    }

    private fun setUpPlaces(){
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                autoplacesFragment.visibility = GONE
                toolbar_title.visibility = VISIBLE
                textLocation.visibility = VISIBLE
                textLocation.text = place.name
            }

            override fun onError(status: Status) {
                Log.e("LOCATIONNN", ""+status.statusMessage)
            }

        })
    }

    private fun locationOnClick(){
        textLocation?.setOnClickListener {
            autoplacesFragment.visibility = VISIBLE
            toolbar_title.visibility = GONE
            textLocation.visibility = GONE
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
        private const val REQUEST_CODE = 101
    }

    private fun loadUserDetails() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
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