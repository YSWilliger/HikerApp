package com.example.yswilliger.hikerapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HikerApp extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    TextView latTextView, addTextView, lngTextView, altTextView, bearTextView, accTextView,
            speedTextView;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiker_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lngTextView = (TextView) findViewById(R.id.lngTextView);
        latTextView = (TextView) findViewById(R.id.latTextView);
        altTextView = (TextView) findViewById(R.id.altTextView);
        bearTextView = (TextView) findViewById(R.id.bearTextView);
        accTextView = (TextView) findViewById(R.id.accTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        addTextView = (TextView) findViewById(R.id.addTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hiker_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        Float bearing = location.getBearing();
        Float speed = location.getSpeed();
        Float acc = location.getAccuracy();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> ListAddresses = geocoder.getFromLocation(lat, lng, 1);

            if (ListAddresses != null && ListAddresses.size() > 0) {

                String addressHolder = "";
                for (i = 0; i <= ListAddresses.get(0).getMaxAddressLineIndex(); i++) {
                    addressHolder += ListAddresses.get(0).getAddressLine(i) +  "\n";

                }
                addTextView.setText("Address:\n" + addressHolder);
                Log.i("Place Info", ListAddresses.get(0).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(lat.toString(), lng.toString());
        latTextView.setText("Latitude: " + lat.toString());
        lngTextView.setText("Longitude: " + lng.toString());
        altTextView.setText("Alitude: " + alt.toString()+ " m");
        bearTextView.setText("Bearing: " + bearing.toString());
        speedTextView.setText("Speed: " + speed.toString()+ " m/s");
        accTextView.setText("Accuracy: " + acc.toString() + " m");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
