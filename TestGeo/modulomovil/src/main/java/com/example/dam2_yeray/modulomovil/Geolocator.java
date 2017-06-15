package com.example.dam2_yeray.modulomovil;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dam2_yeray.modulomovil.Servicios.AppLocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Geolocator implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener {
    //public class Geolocator {
    //private static final int TIEMPO = 1000 * 60*5 ;//Un minuto
    private Calendar calendario;


    private boolean primeraVez, recivioCoordenada, falla;


    /**
     * VALORES POR DEFECTO
     */
    /*
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    */

    //TODO: CAMBIAR AQUI a 10000, 5000, y 5 metros

    private static final int INTERVALO_DE_ACTUALIZACION = 6000;
    private static final int INTERVALO_MAS_TOCHO = 3000;
    private static final int DISPLACEMENT = 5; // 10 meters


    protected GoogleApiClient mGoogleApiClient;

    private Location theBetter;
    private Context mcontext;
    private List<Location> location;


    /**
     * old code
     */
    //  private AppLocationService appLocationService;
    public Geolocator(Context c) {

        primeraVez = true;
        recivioCoordenada = true;
        falla = false;
        mcontext = c;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            location = new ArrayList<Location>();

        }

       /* mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addApi(LocationServices.API)
                .build();
*/

        calendario = Calendar.getInstance();
        //  appLocationService = new AppLocationService (c);
        //
        //
        Toast.makeText(c, "INICIANDO GEOLOCALIZACION", Toast.LENGTH_LONG).show();

        mGoogleApiClient.connect();

    }


    /**
     * INSERTA EN LA BASE DE DATOS DE SQLLITE
     */
    private void insertSQLite(double latitud, double longitud, SQLiteDatabase db) {


        String sentence = "INSERT INTO localizacion (latitud,longitud,milis) values('" + latitud + "','" + longitud + "','" + System.currentTimeMillis() + "')";
        try {
            db.execSQL(sentence);
        } catch (SQLException e) {
            Log.e("DB_ERROR", "ERROR DE BASE DE DATOS \n" + e.toString());
        }

    }


    // public List<Double> pillarAltitudLongitud() {

    /**
     * OBTIENE TODAS LAS LOCALIZACIONES DE TODOS LOS PROVEEDORES POSIBLES
     */

    public boolean pillarAltitudLongitud(SQLiteDatabase bd) {
        /**OLD CODE*/
        // appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        //appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        List<Location> location = new ArrayList<Location>();
        // location.addAll( appLocationService.getLocation(LocationManager.GPS_PROVIDER));

            /*try {

                //MEtodo bueno;;
             //   location.addAll(appLocationService.getUltimateLocation());
                //Medodo mio
                //location.addAll(appLocationService.getSavedLocations());
            }catch (Exception e)
            {
                Log.e("NULL_ERROR", "NO PILLA NINGUN GPS ");
            }
*/
        if (location.size() > 0) {
            for (int i = 0; i < location.size(); i += 1) {
                //INSERTA EN LABASE DE DATOS E SQLLITE

                insertSQLite(location.get(i).getLatitude(), location.get(i).getLongitude(), bd);
            }
            return true;
        } else {
            //Null ppinter exception
            return false;
        }

        //Location location[] = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        // Location locationNetwork[] = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        //  Location location;
        // location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


/*
        List<Double> datos = new ArrayList<Double>();

       if (location != null) {
     /*       double latitude;
            double longitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            datos.add(0, latitude);
            datos.add(1, longitude);*/
        /*    int suma=location.length+locationNetwork.length;

           double[] latitude = new double[suma];
           double[] longitude = new double[suma];
           for (int i = 0; i < suma; i++) {
                if(i<location.length) {
                    latitude[i] = location[i].getLatitude();
                    longitude[i] = location[i].getLongitude();
                }else
                {
                    latitude[i] = locationNetwork[i-location.length].getLatitude();
                    longitude[i] = locationNetwork[i-location.length].getLongitude();

                }


               Log.d("LOCSTARIONS","LATITUD="+latitude[i]+"LONGITUD="+longitude[i]);
               datos.add(0, latitude[i]);
               datos.add(1, longitude[i]);


           }

       }*/


/*
           // if (isBetterLocation(location, theBetter)) {

               // if (isBetterLocation(locationViejo, theBetter)) {



                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double altitud= location.getAltitude();
                    Log.d("ALTITUD",altitud+"");
                    /** AQUI PILLO LA LATITUD Y LONGITUD */
        // LocationAddress locationAddress = new LocationAddress();/*
            /*
                    datos.add(0, latitude);
                    datos.add(1, longitude);
                    // MainGeo.txt5.setText("Altitud="+altitud);
                    //datos.add(2,altitud);
                    theBetter = location;
                    return datos;
              //  }

                    /*double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double altitud= location.getAltitude();
                    Log.d("ALTITUD",altitud+"");
                    /** AQUI PILLO LA LATITUD Y LONGITUD */
        // LocationAddress locationAddress = new LocationAddress();
                   /* datos.add(0, latitude);
                    datos.add(1, longitude);
                   // MainGeo.txt5.setText("Altitud="+altitud);
                    //datos.add(2,altitud);
                    theBetter = location;*/

        // } else return null;


      /*  } else {
            // showSettingsAlert();
            Log.e("Error de conexion", "Err");
            return null;
        }*/


        // return datos;

    }


    /*  protected boolean isBetterLocation(Location location, Location currentBestLocation) {

         if (currentBestLocation == null) {
              // A new location is always better than no location
              Log.d("LOLAILO","CURRENT ES NULL");
             return true;
          }

          // Check whether the new location fix is newer or older
          long timeDelta = location.getTime() - currentBestLocation.getTime();
        /*  boolean isSignificantlyNewer = timeDelta > TIEMPO;
          boolean isSignificantlyOlder = timeDelta < -TIEMPO;*/
      /*  boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            Log.d("LOLAILO", "ES SIFNIFICATIVAMENTE NUEVO ");
            return true;
            // If the new location is more than two minutes older, it must be worse
        }
        if (isSignificantlyOlder) {
            Log.d("LOLAILO", "ES SIFNIFICATIVAMENTE VIEJO ");
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;

        boolean isSignificantlyLessAccurate = accuracyDelta > 200;//Si dos minutos eran doscientos

        Log.i("WARIOWINS", "Bool1:" + isLessAccurate + " Bool2:" + isMoreAccurate + " Bool3" + isSignificantlyLessAccurate);

        // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {

            Log.d("LOLAILO", "ES MAS PRECISO");
            return true;

        }
        if (isNewer && !isLessAccurate) {

            Log.d("LOLAILO", "ES MAS NUEVO Y MAS PRECISO ");
            return true;

            //TODO: CAMBIAR AQUI SI NO FUNCIONA
        }
        if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
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

    public void Apagar() {
        ///Parar el listener
        //  appLocationService.Unregister();
        mGoogleApiClient.disconnect();

    }


    /**
     * UN BUEN GEOLOCALIZADOR DEVERIA PILLAR PORM LO MENOS EL PRIMER PUNTO PARA REALIZAR UNA APROXIMACION
     */
    public boolean pillarAltitudLongitudVersionGoogle(SQLiteDatabase bd) {






            /*if(primeraVez)
            {

                    Toast.makeText(mcontext, "ES MI PRIMERA VEEEZ", Toast.LENGTH_LONG).show();

                    location.add(LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient));
                    Log.d("INSPECCION_ANTI_NULL", "HA INSERTADO UN DATO ");
                    mGoogleApiClient.reconnect();
                    primeraVez = false;

            }*/


        if (location.size() > 0) {

            for (int i = 0; i < location.size(); i += 1) {

                //INSERTA EN LABASE DE DATOS E SQLLITE
                insertSQLite(location.get(i).getLatitude(), location.get(i).getLongitude(), bd);
            }
            location.clear();

            return true;
        } else {
            //Null ppinter exception
            return false;
        }


    }



    /*public String obtenerDireccion(double latitude, double longitude) {
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        } finally {
            //Message message = Message.obtain();
            //message.setTarget(handler);
            if (result != null) {
              // message.what = 1;
              //  Bundle bundle = new Bundle();

              //  bundle.putString("address", result);
              //  message.setData(bundle);
            } else {
                //message.what = 1;
               // Bundle bundle = new Bundle();
                result = "\n Unable to get address for this lat-long.";
                //bundle.putString("address", result);
               // message.setData(bundle);
            }
           //message.sendToTarget();

        }
        return result;
    }*/


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        falla = true;
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        // if(!primeraVez) {
        LocationRequest request = new LocationRequest();
        request.setInterval(INTERVALO_DE_ACTUALIZACION);
        request.setFastestInterval(INTERVALO_MAS_TOCHO);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(DISPLACEMENT);


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

        // }


    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.reconnect();

    }

    @Override
    public void onLocationChanged(Location location) {
        //   Toast.makeText(mcontext,"PENEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", Toast.LENGTH_SHORT).show();

        Log.d("LOCATION_ACC", location.getAccuracy() + "");
        if (location.getAccuracy() < 50) {
            this.location.add(location);
            recivioCoordenada = true;
        }

//        hilo.notify();
    }


    public void cancelar() {
        mGoogleApiClient.disconnect();


    }

    public boolean hayCoordenada() {
        return recivioCoordenada;
    }
}
