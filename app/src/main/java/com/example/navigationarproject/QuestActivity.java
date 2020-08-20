package com.example.navigationarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QuestActivity extends AppCompatActivity{

    private static final long LOCATION_RATE_GPS_MS = TimeUnit.MILLISECONDS.toMillis(1000L);
    //private static final long LOCATION_RATE_GPS_MS = TimeUnit.SECONDS.toMillis(1L);
    TextView textViewCoordinates;
    TextView textViewDistance;
    LocationManager mLocationManager;
   // private Context context;
    private LocationListener mLocationListener =
            new LocationListener() {

                @Override
                public void onProviderEnabled(String provider) {
                    try {
                        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
                    } catch (SecurityException e) {

                    }
                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onLocationChanged(Location location) {
                if (location.getProvider().equals(LocationManager.GPS_PROVIDER))
                    {
                        Log.d("ArTack", "onLocationChanged");
                        textViewCoordinates.setText(formatLocation(location));
                        StartActivity._ws.send("Location:" + location.getLatitude() + " " + location.getLongitude());
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.14f,\n lon = %2$.14f,\n time = %3$tF %3$tT",
                location.getLatitude() , location.getLongitude() , new Date(
                        location.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Bundle arguments = getIntent().getExtras();
//        context = context.getApplicationContext();
        if (arguments != null) {

        }
        textViewCoordinates = findViewById(R.id.textViewCoordinate);
        textViewDistance = findViewById(R.id.textViewDistance);
    }

    public void registerLocation() {
        boolean isGpsProviderEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGpsProviderEnabled) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_RATE_GPS_MS,
                    0.0f /* minDistance */,
                    mLocationListener);
            Log.d("ArTack","requestLocationUpdates");
            textViewDistance.setText("XD");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //StartActivity._ws.send("Close:");
        StartActivity._ws.close(1000,"Close:");
    }
}