package com.tosh.poolandroid.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tosh.poolandroid.Adapters.VendorAdapter;
import com.tosh.poolandroid.Model.User;
import com.tosh.poolandroid.R;
import com.tosh.poolandroid.Retrofit.AuthRetrofitClient;
import com.tosh.poolandroid.Model.Vendor;
import com.tosh.poolandroid.Retrofit.NodeAuthService;
import com.tosh.poolandroid.ViewModel.VendorViewModel;


import java.util.ArrayList;
import java.util.List;


import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private NodeAuthService api;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String email;
    private String latitude;
    private String longitude;
    private String user_email;
    private MaterialToolbar toolbar;
    private RecyclerView vendorsRv;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    ArrayList<Vendor> vendorModel = new ArrayList<>();
    private VendorAdapter vendorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        email = pref.getString("email", "");


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        initialize();

        cartFab();

        fetchLastLocation();

        loadUserDetails();
    }
    private void cartFab() {

        FloatingActionButton cartFab = (FloatingActionButton) findViewById(R.id.cart_fab);
        cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cart clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialize() {

        //tool bar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        //navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        


        //recyclerView
        vendorsRv = findViewById(R.id.vendorsRv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        vendorsRv.setLayoutManager(gridLayoutManager);

        VendorViewModel vendorViewModel = ViewModelProviders.of(this).get(VendorViewModel.class);

        vendorViewModel.getVendor().observe(this, new Observer<List<Vendor>>() {
            @Override
            public void onChanged(List<Vendor> vendors) {
                vendorAdapter = new VendorAdapter(MainActivity.this, vendors);
                vendorsRv.setAdapter(vendorAdapter);

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.orders_navigation:
                Toast.makeText(this, "Orders clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_navigation:
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings_navigation:
                Toast.makeText(this, "Address clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_navigation:
                Toast.makeText(this, "Info clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_navigation:
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help_navigation:
                Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_navigation:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void fetchLastLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                    latitude = String.valueOf(currentLocation.getLatitude());
                    longitude = String.valueOf(currentLocation.getLongitude());
                    addToSharedPreferences(latitude, longitude);
                }
            }
        });

    }

    private void addToSharedPreferences(String latitude, String longitude){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.apply();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    private void loadUserDetails(){
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView navName = (TextView) headerView.findViewById(R.id.navigation_name);
        final TextView navEmail = (TextView) headerView.findViewById(R.id.navigation_email);

        Retrofit retrofit = AuthRetrofitClient.getUser();
        api = retrofit.create(NodeAuthService.class);

        Call<List<User>> users = api.getUser(email);

        users.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users =  response.body();
                navName.setText(users.get(0).getUserName());
                navEmail.setText(users.get(0).getUserEmail());

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("USER",t.getMessage());
            }
        });

    }



    private void logout(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().remove("email").apply();
        settings.edit().remove("name").apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
