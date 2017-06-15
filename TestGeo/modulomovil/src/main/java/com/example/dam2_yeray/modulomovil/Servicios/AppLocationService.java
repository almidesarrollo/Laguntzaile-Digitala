package com.example.dam2_yeray.modulomovil.Servicios;

/**
 * Created by family on 19/12/2015.
 */


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class


        AppLocationService extends Service implements LocationListener {

    protected LocationManager locationManager;
    Location[] location;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 5000 ;
    private static final int TIEMPO = 1000 * 60*2 ;
    private static final int criterio = Criteria.ACCURACY_HIGH ;
    private Context mcontext;
    private Location l;
    public static String proveedores;

    private List<Location> savedLocations;


    public AppLocationService() {

    }

    public AppLocationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
        locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER,
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);

    }

    public void Unregister()
    {
        locationManager.removeUpdates(this);
    }

   // public Location[] getLocation(String provider) {
        public List<Location> getLocation(String provider) {

        Log.d("ARREGLOS", provider);
        if (locationManager.isProviderEnabled(provider)) {

            Log.d("ARREGLOS", "BBBBBBBBBBBBBBBBBBBBBBBB");
           // if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.


               locationManager.requestLocationUpdates(provider,
                        MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);


                if (locationManager != null) {

                    return getUltimateLocation();

                    }
                    return null;
                    // Log.d("ARREGLOS", String.valueOf(location.getLatitude()));

                }
            }



        //SISTEMA OPCIONAl


        Log.d("Errores", "AAAAAAAAAAAAAAAAAAAAAAA");
        return null;
    }


    public List<Location> getUltimateLocation() {


        List<String> providers = locationManager.getAllProviders();

        List<Location> bestLocation=new ArrayList<Location>();
        //Location[] bestLocation = new Location[providers.size()];
        Location l;
        int i=0;
         proveedores="";
        if (providers.size() > 0) {


            for (String provider : providers) {
                proveedores+="\n"+provider;
                if (locationManager.isProviderEnabled(provider)) {


                   /* locationManager.requestLocationUpdates(provider,
                            MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);*/
                    // if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    //  {

                    l = locationManager.getLastKnownLocation(provider);
                    // }else

                    //{
                    //  l=null;
                    // }


                    if (l == null) {
                        continue;
                    }
               /* if(bestLocation == null)
                {
                    bestLocation=l;
                }*/
                    //if(isBetterLocation(l,bestLocation)) {
                    //   if ( l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    //    bestLocation = l;
                    bestLocation.add(l);

                    //  }
                    // }

                }

                return bestLocation;
            }
        }
        return null;
    }





    public List<Location> getSavedLocations()
    {
      List<Location> drop=new ArrayList<Location>();
        drop.addAll(savedLocations);
        savedLocations.clear();
        return drop;
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
      /*  if (currentBestLocation == null) {
            // A new location is always better than no location
            Log.d("LOLAILO","CURRENT ES NULL");
            return true;
        }*/

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIEMPO;
        boolean isSignificantlyOlder = timeDelta < -TIEMPO;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            Log.d("LOLAILO", "ES SIFNIFICATIVAMENTE NUEVO ");
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            Log.d("LOLAILO", "ES SIFNIFICATIVAMENTE VIEJO ");
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;

        boolean isSignificantlyLessAccurate = accuracyDelta > 200;//Si dos minutos eran doscientos

        Log.i("WARIOWINS","Bool1:"+isLessAccurate+" Bool2:"+isMoreAccurate+ " Bool3"+isSignificantlyLessAccurate);

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            Log.d("LOLAILO", "ES MAS PRECISO");
            return true;
        } else if (isNewer && !isLessAccurate) {
            Log.d("LOLAILO", "ES MAS NUEVO Y MAS PRECISO ");
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            Log.d("LOLAILO","ES MAS PRECISO Y SI ES DEL MISMO PROVEEDOR");
            return true;
        }
        Log.d("LOLAILO","PASA DE TODO");
        return false;
    }

      /** Checks whether two providers are the same */
        private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("LOCOMOTIONES", location.getLatitude() + "+" + location.getLongitude());
//        savedLocations.addAll(getUltimateLocation());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
