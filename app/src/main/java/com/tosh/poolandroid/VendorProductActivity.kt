package com.tosh.poolandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.tosh.poolandroid.view.LoginActivity
import com.tosh.poolandroid.view.adapter.VendorAdapter
import com.tosh.poolandroid.viewmodel.UserViewModel
import com.tosh.poolandroid.viewmodel.VendorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_vendor_product.*
import kotlinx.android.synthetic.main.fab_layout.*
import kotlinx.android.synthetic.main.navigation_drawer.*

class VendorProductActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)

        cartFab()
        initialize()
        loadUserDetails()
    }
    private fun cartFab() {
        cart_fab.setOnClickListener { Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show() }
    }

    private fun initialize() {

        toolbar_title.text = "KFC"

        //navigation drawer
        navigation_view!!.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolBar,
                R.string.open_drawer, R.string.close_drawer)
        drawer_layout!!.addDrawerListener(toggle)

        toggle.syncState()
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

}

