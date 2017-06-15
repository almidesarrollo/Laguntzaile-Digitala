package com.asistencia.digital.monitor.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.asistencia.digital.monitor.localdb.GPOenGelper;
import com.asistencia.digital.monitor.Geolocator;
import com.asistencia.digital.monitor.PostPhp.PostArray;
import com.asistencia.digital.monitor.geoutils.GeoItem;
import com.asistencia.digital.monitor.geoutils.GeoUtils;
import com.asistencia.digital.monitor.webservice.AsistRestApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServiLoc extends IntentService {

    public static final int MAX_INSERTS = 1, WAIT_TIME = 5000;
   /**
    *
    * MAX_INSERTS CONTROLA CUANTAS VECES TIENE QUE HACER EL BUCLE DEL DO IN BACKGROUND PARA QUE REALICE UNA INSERCION
    *
    * WAIT TIME ESPECIFICA EL TIEMPO DE ESPERA DEL HILO PARA QUE SE EJECUTRE CADA VEZ
    *
    * */

   //  @Geolocator es la clase que devuelve los puntos obtenidos por el servicio de localizacion
    private Geolocator geo;

    //Esta variable esta obsoleta
    private List<Double> datos;

    //booleana para controlar el bucle principal
    private boolean onof;

    //No necesita presentaciones
    private Context cContext;

    //base de datos principal
    private SQLiteDatabase personalDB;

    //variable para controlar el numero de inserciones
    private int insertCounter;

    //Esto sobra
    private Handler mHandler;

    //El cursor con el que se accederan a los datos anteriormente insertados
    private SQLiteCursor cursor;

    //el calendario para las mediciones de hora
    private Calendar calendario;
    private long currentMilis;
    private GeoItem currentGeoLocation = null;

    private AsistRestApiClient mApiRest;

    /**
     *
     * Esta funcion crea una tarea asincrona
     * para insertar las ultimas geolocalizaciones
     * en la base de datos mysql
     *
     * */
    private void insertarLocalizacion() {
        GeoItem location = GeoUtils.getCurrentLocation();

        /* si ha cambiado la ubicacion con respeto a la anterior */
        if (location != null && !location.equals(currentGeoLocation)) {

            Log.e("mytag", location.toString());

            /* insert the location using ws */
            mApiRest.insertLocation(location);
            currentGeoLocation = location;
        }
    }

    /**
     *
     * Este codigo se ejecuta cada 5 segundos para obtener
     * la localizacion y enviarla al servidor
     * en el caso dado
     *
     * */
    public void update() {
        insertarLocalizacion();
    }

    public ServiLoc() {
        super("SERVILOOOC");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        insertCounter = 0;
        personalDB = new GPOenGelper(this,"Android",null,1).getWritableDatabase();
        geo = new Geolocator(getApplicationContext());
        datos = new ArrayList<>();
        onof = true;
        cContext = getApplicationContext();
        mApiRest = new AsistRestApiClient(cContext);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onof = false;
        geo.disconnectApi();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (onof) {
            /**
             * El hilo necesita esperar ya que sin el sleep iria tan rpido que no funcionaria del todo
             * */
            try {
                Thread.sleep(WAIT_TIME);
                update();
            } catch (InterruptedException e) {
                Log.e(e.getClass().toString(), "ERROR: "+e.getMessage());
                onof = false;
            }
        }

    }
}
