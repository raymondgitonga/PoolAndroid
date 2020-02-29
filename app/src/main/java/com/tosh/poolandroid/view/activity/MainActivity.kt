package com.tosh.poolandroid.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.tosh.poolandroid.R
import com.tosh.poolandroid.util.Constants.SHARED_EMAIL
import com.tosh.poolandroid.util.Constants.SHARED_LATITUDE
import com.tosh.poolandroid.util.Constants.SHARED_LONGITUDE
import com.tosh.poolandroid.util.Constants.SHARED_PHONE
import com.tosh.poolandroid.util.addLocationPreferences
import com.tosh.poolandroid.view.fragment.*
import com.tosh.poolandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_layout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    
    private var latitude: String? = null
    private var longitude: String? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mainViewModel: MainViewModel? = null
    private val vendorFragment = VendorFragment()
    private val profileFragment = ProfileFragment()
    private val cartFragment = CartFragment()
    private val helpFragment = HelpFragment()
    private val ordersFragment = OrdersFragment()
    lateinit var placesClient: PlacesClient

    private var placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        fetchLastLocation()
        createFormFragment()
        setupDrawer()
        loadUserDetails()
        bottomNavigation()
        cartFragment()
        initItemCounter()
    }

    override fun onResume() {
        super.onResume()
        initItemCounter()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        initItemCounter()
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
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.details_fragment, ordersFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.profile_navigation -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.details_fragment, profileFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.share_navigation -> {
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.help_navigation -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.details_fragment, helpFragment)
                transaction.addToBackStack(null)
                transaction.commit()
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
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.details_fragment, profileFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                R.id.help_bottom ->{
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.details_fragment, helpFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
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
                addLocationPreferences(applicationContext, latitude, longitude)

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    private fun loadUserDetails() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<View>(R.id.navigation_name) as TextView
        val navEmail = headerView.findViewById<View>(R.id.navigation_email) as TextView

        mainViewModel!!.getUserDetails().observe(this, Observer { userEntities ->
            for (i in userEntities.indices) {
                navName.text = userEntities[i].name
                navEmail.text = userEntities[i].email
            }
        })

    }

    private fun initItemCounter(){
        mainViewModel!!.getCartItemSize().observe(this, Observer {
            if(it >= 0){
                itemCount.text = it.toString()
            }else{
                itemCount.visibility = GONE
            }
        })
    }

    private fun logout() {
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        settings.edit().remove(SHARED_EMAIL).apply()
        settings.edit().remove(SHARED_PHONE).apply()
        settings.edit().remove(SHARED_LATITUDE).apply()
        settings.edit().remove(SHARED_LONGITUDE).apply()
        mainViewModel!!.deleteUser()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_CODE = 101
    }
}