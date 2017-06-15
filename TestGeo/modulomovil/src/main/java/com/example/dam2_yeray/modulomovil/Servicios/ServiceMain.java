package com.example.dam2_yeray.modulomovil.Servicios;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.example.dam2_yeray.modulomovil.BaseDatos.GPOenGelper;
import com.example.dam2_yeray.modulomovil.ControlSensores;


/**
 * Created by dam2-yeray on 17/12/2015.
 */
public class ServiceMain extends Service {



    public static final int MAX_RECORDS=30;

    //Base de datos en SQlite
    //----------------------------------------------------------------------------------------------
    private SQLiteDatabase personalDB;
    private GPOenGelper openhelper;
    //----------------------------------------------------------------------------------------------

    //Asyntask que gestiona la localizacion
    //----------------------------------------------------------------------------------------------
  //  private AsinLoc localizador;
    //----------------------------------------------------------------------------------------------

    //Servicio que permite sejar activo el cpu mientras el movil o el wear esta en modo reposo
    //----------------------------------------------------------------------------------------------
    private  PowerManager.WakeLock wakeLock;
    private  PowerManager mgr;
    //----------------------------------------------------------------------------------------------

    //SENSORES
    //----------------------------------------------------------------------------------------------
    private ControlSensores superSensorManager;
    //----------------------------------------------------------------------------------------------

    //Evento que comprueba sensores
    //----------------------------------------------------------------------------------------------


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */

    //----------------------------------------------------------------------------------------------



   /* public  ServiceMain(Context c)
    {
        super();

    }*/
   @Override
    public void onCreate() {

        super.onCreate();
      //  WeakReference<Handleator> whatdafuc=new WeakReference<Handleator>(new Handleator(Looper.getMainLooper()));

        //Crear el gestor de sensores y el gestor de eventos
        /**
         * ¿Porque crearlo aqui y no en la clase que lo gestiona?
         * Basicamente porque permite desactivar todos los sensores al apagar el servicio
         * */




        //Todo Instanciar esto cuando se acabe de comprobar qe el geo funciona

        superSensorManager=new ControlSensores(this);

        //Generar la base dedatos o acceder a ella si ya esta generada
        openhelper=new GPOenGelper(this,"Android",null,1);
        personalDB =openhelper.getWritableDatabase();

        //Borra todos los puntos de la base de datos
        openhelper.reiniciarPuntos(personalDB);

        //creas el hilo de gestion e puntos
       // localizador=new AsinLoc(this,personalDB);

        //Instanciar el sercivio del control de cpu para que funcione aunque la pantalla este apagada
        mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MIBLOQUEO");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //AQui vetamos el WAKELOCK
        wakeLock.acquire();
        Log.d("exception", "ESTO SE EJECUTA?");
        //SE LANZA EL ORGANIZADOR
        startService(new Intent(this,ServiLoc.class));
        superSensorManager.activatSensores();
       // startService(new Intent(this,AsynCad.class));
        // localizador.execute();
        //Con esta constantes especificas si tratara de coger recursos a lo bestia o si de intentara comer el minimo de recursos posible
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
        //Todo Instanciar esto cuando se acabe de comprobar qe el geo funciona

        superSensorManager.desactivarSensores();
        stopService(new Intent(this, ServiLoc.class));
        //estoPuedeSerUnAsyntask=null;
 //       localizador.cancel(true);
       // stopService(new Intent(this,AsynCad.class));

    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




   /* public void actualizarLoc()
    {
        localizador.update();
    }*/

}
