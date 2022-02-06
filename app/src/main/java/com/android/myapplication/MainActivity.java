package com.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button getLocationButton;
    Button openMapsButton;
    EditText addressText;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationButton = findViewById(R.id.get_current_location_button);
        openMapsButton = findViewById(R.id.open_maps_button);
        addressText = findViewById(R.id.address_input);

        getLocationButton.setOnClickListener( v-> {
            initCurrentAddress();
        });

        openMapsButton.setOnClickListener( v-> {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
    }

    private void initCurrentAddress() {

        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            initCurrentAddress();
        }
        else {
            FusedLocationProviderClient fusedLocationProvider = LocationServices.getFusedLocationProviderClient(getBaseContext());
            fusedLocationProvider.getLastLocation().addOnSuccessListener(location ->{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                List<Address> addresses = null;
                try {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String locality = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();

                String address = locality + ", " + city;
                addressText.setText(address);
            });
        }

    }

}