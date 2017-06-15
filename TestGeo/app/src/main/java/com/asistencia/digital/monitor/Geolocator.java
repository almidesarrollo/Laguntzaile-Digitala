package com.asistencia.digital.monitor;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Geolocator implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener {

    private static final int INTERVALO_DE_ACTUALIZACION = 6000;
    private static final int INTERVALO_MAS_TOCHO = 3000 ;
    private static final int DISPLACEMENT = 5; // 10 meters
    private GoogleApiClient mGoogleApiClient;
    private Location currentLocation = null;

    public Geolocator(Context c) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        if (mGoogleApiClient.isConnected()) {
            Toast.makeText(c,"mGoogleApiClient ha iniciado OK", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(c,"mGoogleApiClient ha iniciado con ERROR", Toast.LENGTH_LONG).show();
        }
    }

    public void disconnectApi() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setInterval(INTERVALO_DE_ACTUALIZACION);
        request.setFastestInterval(INTERVALO_MAS_TOCHO);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(DISPLACEMENT);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.reconnect();
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }
}
