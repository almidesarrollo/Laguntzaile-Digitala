package com.example.dam2_yeray.modulomovil.Servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;


import com.example.dam2_yeray.modulomovil.Asyntask.AsynInsert;
import com.example.dam2_yeray.modulomovil.BaseDatos.GPOenGelper;
import com.example.dam2_yeray.modulomovil.Geolocator;
import com.example.dam2_yeray.modulomovil.MySqlNiko.MyFunctions;
import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private  List<Double> datos;

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



/*
    public ServiLoc(Context c, SQLiteDatabase db, Handler mHandler)
    {
        this.mHandler=mHandler;
        insertCounter = 0;

        latch=new CountDownLatch(1);
        personalDB = db;
        //geo = new Geolocator(c,this);
        datos = new ArrayList<Double>();
        onof = true;
        cContext = c;


    }
    public ServiLoc(Context c, SQLiteDatabase db)
    {

        insertCounter = 0;

        personalDB = db;
        geo = new Geolocator(c);
        datos = new ArrayList<Double>();
        onof = true;
        cContext = c;
        calendario=Calendar.getInstance();


    }*/







    /**
     *
     * Esta funcion crea una tarea asincrona
     * para insertar las ultimas geolocalizaciones
     * en la base de datos mysql
     *
     * */
    private void insertarLocalizacion() {

        AsynInsert asyn = new AsynInsert(obtenerParametrosWebConBd(), MyFunctions.URL_INSERT_LOCATION);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
            asyn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            asyn.execute();
        //Todo cambiar Este execSQL
        //personalDB.execSQL("DELETE FROM `Localizacion`");
    }


    /**
     *
     * aqui obtenemos
     * los parametros que vamos a enviar
     * por la red a nuestro web service
     *
     * */

    public PostArray obtenerParametrosWebConBd() {
        //ensenarcacho();


       // cursor=(SQLiteCursor)personalDB.rawQuery("SELECT DISTINCT latitud,longitud FROM `Localizacion` WHERE hora > "+ultimaHora+" and minuto >"+ultimoMin+" and segundo >"+ultimoSec,null);

        cursor=(SQLiteCursor)personalDB.rawQuery("SELECT latitud,longitud,milis FROM `Localizacion` GROUP BY latitud,longitud ORDER BY milis DESC LIMIT 5 ",null);

        if(cursor.moveToFirst())
        {

            PostArray array = MyFunctions.crearPost("localizador");
            array.setValue("max_inserts", (cursor.getCount()  + ""));
            do{
                if(cursor.getLong(cursor.getColumnIndex("milis"))>currentMilis) {


                    array.setValue("latitud_" + cursor.getPosition(), "" + cursor.getDouble(cursor.getColumnIndex("latitud")));
                    array.setValue("longitud_" + (cursor.getPosition()), "" + cursor.getDouble(cursor.getColumnIndex("longitud")));
                    Log.d("LOQUEMANDA", "latitud_" + cursor.getPosition() + "++++" + cursor.getDouble(cursor.getColumnIndex("latitud")));
                    Log.d("LOQUEMANDA", "longitud_" + (cursor.getPosition()) + "++++" + cursor.getDouble(cursor.getColumnIndex("longitud")));
                }


            }while(cursor.moveToNext());

            return array;
        }else
        return null;
    }
    public PostArray obtenerParametrosWeb() {

        /**instanciamos el array*/



        PostArray array = MyFunctions.crearPost("localizador");
        String sentence="SELECT DISTINCT latitud,longitud" +
                " FROM `Localizacion`";

      //  array.setValue("max_inserts",(datos.size()/2+""));


      /*  array.setValue("modo", "localizador");

        //todo AQUI DEBEREMOS METER LA VARIABLE ID DEL PACIENTE USUARIO
        array.setValue("USER_ID_", "171");

        /**esto sirve para no permitir las peticiones desde otro lado que no sea la aplicacion*/
        /*array.setValue(MyFunctions.KEY_, MyFunctions.PASSW_);*/

        /**para rellenar los valores pares primero (latitud)*/
        int count = 0;

        for (int i=0;i<datos.size();i++) {

            if (i % 2 == 0) {
                array.setValue("latitud_" + count, "" + datos.get(i));
                count++;

            }
        }

        count = 0;
        /**para rellenar los valores impares despues (longitud)*/
        for (int i=0;i<datos.size();i++)
        {

            if (i % 2 != 0) {
                array.setValue("longitud_" + count, "" + datos.get(i));
                count++;
            }
        }

        return array;
    }

    /**
     *
     * Este codigo se ejecuta cada 5 segundos para obtener
     * la localizacion y enviarla al servidor
     * en el caso dado
     *
     * */

    public void update()
    {

        //se obtiene la ultima lacalizacion captada por gps u otros
       if( obtenerLocalizacion()) {
          //  Toast.makeText(cContext, ""+AppLocationService.proveedores,Toast.LENGTH_SHORT).show();

           //Log.e("Counter", "Inserted: " + insertCounter);

           //Comprueba el nuermo de inserciones apra meter en la base de datos del server
           if (insertCounter >= MAX_INSERTS - 1) {
               //notifiva desde un mensaje emergente arriba
               MyFunctions.notificar(cContext,"Coordenadas","ENVIANDO A BASE DE DATOS");
               //inserta la localizacion en la BBDD del servidor enviando datos a traves de POST
               insertarLocalizacion();
               insertCounter = 0;
               //datos.clear();
               //Registra el moment de la ultima insercion para insertar datos de ahi para alante
            currentMilis=System.currentTimeMillis();

           } else {
               MyFunctions.notificar(cContext,"Coordenadas","LOCALIZACION PILLADA CORRECTAMENTE");
               insertCounter++;
           }
       }else
       {
           MyFunctions.notificar(cContext,"Coordenadas","ERROR AL PILLAR LOCALIZACION");
       }

    }

    /**
     *
     * Obtiene la localizacion actual del movil
     * en dos valores, latitud y longitud
     *
     * */
    private boolean obtenerLocalizacion()
    {
        return geo.pillarAltitudLongitudVersionGoogle(personalDB);
        //Todo cambiar si falla
        //return geo.pillarAltitudLongitud(personalDB);
    }
    /*
    public boolean obtenerLocalizacion() {

        List<Double> test;
        if( geo.pillarAltitudLongitud(personalDB))
        {

            if(datos.size()>0)
            {
                Log.e("BORRAR","borro");
                if(estaCerca(test)) {
                   return false;
                }
                MyFunctions.notificar(cContext,"Coordenadas","ENVIO CORRECTO");


            }
           // MyFunctions.notificar(cContext,"Coordenadas","Latitud:"+test.get(0)+"Longitud:"+test.get(1));
           double newLatitud, newLongitud;
           /* newLatitud = test.get(0);
            newLongitud = test.get(1);

            datos.add(newLatitud);
            datos.add(newLongitud);

            Log.d("LOCALIZAPUTO", "Latitud" + newLatitud + " Longitud:" + newLongitud);

            //insertar sqlite
            insertSQLite(newLatitud,newLongitud);*/

          /*  for (int i=0;i<test.size();i+=2)
            {
                newLatitud = test.get(i);
                newLongitud = test.get(i+1);

                datos.add(newLatitud);
                datos.add(newLongitud);
            }
            return true;*/
/*
        }
        return false;
    }*/


   /* private boolean estaCerca(List<Double> test)
    {

        double lastLatitud = datos.get(datos.size()-2);
        double lastLongitud = datos.get(datos.size()-1);

        if (lastLatitud == test.get(0) && lastLongitud == test.get(1))
            return true;

        /**TE DEJO AQUI ESTOS IFS POR PENA XD*/
        /*if(test.get(1)<=longitud)
            if(longitud-test.get(0)<DIST_MIN)
                return true;

        if(test.get(0)<=latitud)
            if(latitud-test.get(1)<DIST_MIN)
                return true;

        if(test.get(1)>latitud)
            if(test.get(0)-longitud<DIST_MIN)
                return true;

        if(test.get(0)>latitud)
            if(test.get(1)-latitud<DIST_MIN)
                return true;*/
/*
        return false;
    }*/


    public ServiLoc() {
        super("SERVILOOOC");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("INICIO", "lfdpoisuhyfgiopdgsyhulsuyhgioshgujidsfgdsfgdfg");
        insertCounter = 0;
        GPOenGelper openhelper=new GPOenGelper(this,"Android",null,1);
        personalDB =openhelper.getWritableDatabase();
       // personalDB = db;
        geo = new Geolocator(getApplicationContext());
        datos = new ArrayList<Double>();
        onof = true;
        cContext=getApplicationContext();

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
        geo.Apagar();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("INICIO", "lfdpoisuhyfgiopdgsyhulsuyhgioshgujidsfgdsfgdfg");
        while (onof) {


            /**
             * El hilo necesita esperar ya que sin el sleep iria tan rpido que no funcionaria del todo
             * */


            try {
                Thread.sleep(5000);

                if (geo.hayCoordenada())
                    update();


            } catch (InterruptedException e) {
                Log.e("ERROR", "INTERRUMPIDO ");
                onof = false;

            }
        }

    }
}
