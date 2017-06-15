package com.example.dam2_yeray.modulomovil;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.example.dam2_yeray.modulomovil.Asyntask.AsynInsert;
import com.example.dam2_yeray.modulomovil.MySqlNiko.MyFunctions;
import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;

/**
 * Created by me Mario
 */
//public class AsynCad extends AsyncTask<Void, String, String> implements SensorEventListener {
//public class AsynCad extends IntentService implements SensorEventListener {
public class ControlSensores implements SensorEventListener {
        //DATOS SUPER IMPORTANTES DEL SENSOR DE CAIDAS
    public static final int MAX_ACEL=30,MIN_ACEL=20,MAX_CAD=40;

    private Context cMain;
    private Sensor gsensor,asensor,msensor;
    private final float g = 9.81f;
    private final float freeFallGravitySquared = 0.01f * g * g;//CON ESTO SE PODRIA CALCULAR LA CAIDA
    private float[] datosGravitatorios = new float[3];           // Gravity or accelerometer
    private float[] datosMagneticos = new float[3];           // Magnetometer
    private boolean haveGrav = false;
    private float[] datosAccec = new float[3];
    private float opcional;

    private static float maxMov;
    private static float maxCad;
    private float alertCad,alertMod;

    private  float normsAcec,normH;
        private Sensor hSensor;

    private SensorManager mngrSensores;
    /*
    @Override
    protected String doInBackground(Void... params) {
        return null;
    }
    public AsynCad(Context c)
    {
        cMain=c;
    }
*/
    public ControlSensores(Context c)
    {
        maxCad=1000;
        maxMov=0;
        alertCad=0;
        alertMod=0;
        cMain=c;
        mngrSensores = (SensorManager)c.getSystemService(c.SENSOR_SERVICE);

        hSensor=mngrSensores.getDefaultSensor(65562);
        // gsensor =  mnrgSensores.getDefaultSensor(Sensor.TYPE_GRAVITY);
        asensor =  mngrSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor =  mngrSensores.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }
    public void activatSensores()
    {
        mngrSensores.registerListener(this,hSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, asensor, SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void desactivarSensores()
    {
        mngrSensores.unregisterListener(this);
    }

   /* public AsynCad ()
    {
        super("PENE");
    }*/
   /* @Override
    public void onCreate() {
        super.onCreate();
       // MainGeo.txt5.setText("PREPARANDO PIO PIO");
        maxCad=1000;
        maxMov=0;
        alertCad=0;
        alertMod=0;
        // cMain=c;
        mngrSensores = (SensorManager)getSystemService(this.SENSOR_SERVICE);

        hSensor=mngrSensores.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        // gsensor =  mnrgSensores.getDefaultSensor(Sensor.TYPE_GRAVITY);
        asensor =  mngrSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor =  mngrSensores.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }*/


   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mngrSensores.registerListener(this,hSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, asensor, SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_NOT_STICKY;
    }*/
    public static void resetarvalores()
    {
        maxCad=1000;
        maxMov=0;
    }


   /* @Override
    protected void onHandleIntent(Intent intent) {
        mngrSensores.registerListener(this,hSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, asensor, SensorManager.SENSOR_DELAY_NORMAL);
        mngrSensores.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(getApplicationContext(),"FIN DEL CODIGOO",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mngrSensores.unregisterListener(this);
    }*/

   /* public AsynCad ( SensorManager mnrgSensor)
    {
        super("PENE");
        maxCad=1000;
        maxMov=0;
        alertCad=0;
        alertMod=0;
       // cMain=c;
        mnrgSensores=mnrgSensor;
         hSensor=mnrgSensores.getDefaultSensor(Sensor.TYPE_HEART_RATE);
       // gsensor =  mnrgSensores.getDefaultSensor(Sensor.TYPE_GRAVITY);
        asensor =  mnrgSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor =  mnrgSensores.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //mnrgSensores.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL,mHandler);


    }*/





    /*
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    public void onSensorChanged(SensorEvent event) {
        //float[] data;
        switch (event.sensor.getType()) {
           /* case Sensor.TYPE_GRAVITY:
                datosGravitatorios[0] = event.values[0];
                datosGravitatorios[1] = event.values[1];
                datosGravitatorios[2] = event.values[2];
                haveGrav = true;
                break;
*/
            case Sensor.TYPE_ACCELEROMETER:
             //   if (!haveGrav) {// Si tiene el primero de este pasa

                    datosGravitatorios[0] = event.values[0];
                    datosGravitatorios[1] = event.values[1];
                    datosGravitatorios[2] = event.values[2];
           //     }
                datosAccec[0] = event.values[0];
                datosAccec[1] = event.values[1];
                datosAccec[2] = event.values[2];

                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                datosMagneticos[0] = event.values[0];
                datosMagneticos[1] = event.values[1];
                datosMagneticos[2] = event.values[2];

                break;
            case 65562:
                opcional=event.values[0];;
                Toast.makeText(cMain,"El  valor del corazon es:" + opcional, Toast.LENGTH_SHORT);
                break;
            default:

                return;
        }


        // SensorManager.getRotationMatrix(Rmat, Imat, gData, mData);
        //  SACA EL NORMAL DEL VEXTOR DE GRAVEDAD |G|

       // final float normsqA = (datosMagneticos[0] * datosMagneticos[0] + datosMagneticos[1] * datosMagneticos[1] + datosMagneticos[2] * datosMagneticos[2]);

        normsAcec = (float) Math.sqrt( (datosAccec[0] * datosAccec[0] + datosAccec[1] * datosAccec[1] + datosAccec[2] * datosAccec[2]));



        // G BER BI NORMALIZADO
        //   CON ESTE CALCULO SE TINE EN CUENTA SI LA GRAVEDAD DEL MOVIL ES UN DIED PORCIENTO MENOR AL VALOR
        //  POR ESO LA MULTIP`LICACION DEL 0.01

      /*  if (normsqA < freeFallGravitySquared) {
            // gravity less than 10% of normal value
            Message message = mHandler.obtainMessage(2);
            message.sendToTarget();



        }*/

        float Hx = datosMagneticos [1]*datosGravitatorios[2] -  datosMagneticos [2]*datosGravitatorios[1];
        float Hy = datosMagneticos [0]*datosGravitatorios[0] -  datosMagneticos [0]*datosGravitatorios[2];
        float Hz = datosMagneticos [1]*datosGravitatorios[1] -  datosMagneticos [1]*datosGravitatorios[0];

        normH = (float)Math.sqrt(Hx*Hx + Hy*Hy + Hz*Hz);
        /** COMPROBACIONES */

        if(normsAcec>maxMov)
        {
            maxMov=normsAcec;
        }
        if(normH<maxCad)
        {
            maxCad=normH;
        }





     //   if (normH < 0.1f) {

        //if(datosAccec[0]>MAX_ACEL || datosAccec[0]<-MAX_ACEL || datosAccec[1]>MAX_ACEL || datosAccec[1]<-MAX_ACEL || datosAccec[2]>MAX_ACEL || datosAccec[2]<-MAX_ACEL)

        /**
         *
         * --- COMPROBACIONES DEL ACELEROMETRO ---
         *
         * Notas: En mi humilde opinion, estas comprovaciones deverian ser ajenas a esta parte del cosigo pues son muchos sensores y las comprobaciones de unos pueden realentizar a otros
         * podria sacarse del hio principar y gestionarse en segundo plano
         *
         *
         * Hilo principal:
         *
         *
         * ___|____________________
         *
         * */

        if( normsAcec>MIN_ACEL && normsAcec<MAX_ACEL )
        {
            ahorrarCodigo();
        }
        if( normsAcec>-MAX_ACEL &&  normsAcec<-MIN_ACEL  ){
            ahorrarCodigo();
        }


       /* if(datosAccec[0]>MAX_ACEL || datosAccec[0]<-MAX_ACEL || datosAccec[1]>MAX_ACEL || datosAccec[1]<-MAX_ACEL || datosAccec[2]>MAX_ACEL || datosAccec[2]<-MAX_ACEL)
        {
            Message message3 = mHandler.obtainMessage(4);
            message3.sendToTarget();
        }*/
       /* Message message3 = mHandler.obtainMessage(4,"Normal de datos magneticos "+normH+"\n,Normal de aceleracion"+normsAcec+"\n Acel:x"+datosAccec[0] +"\n Acel:y"+datosAccec[1] +"\n Acel:z"+datosAccec[2]
                +"\n MAXACELELRACION="+maxMov+"\n MAXCAIDA="+maxCad+"\n AceleracionQuePilla="+alertMod+"\n CaidaQuepilla="+alertCad);
    //    message3.sendToTarget();
    */
        //TODO Cambiar esto

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
        private void ahorrarCodigo()
        {
            if (normH < MAX_CAD) {

                alertMod=normsAcec;
                alertCad=normH;
              /*  maxMov=normsAcec;
                maxCad=normH;*/
                // device is close to free fall (or in space?), or close to
                // magnetic north pole. Typical values are  > 100.
                // Toast.makeText(cMain,"ME CAIGO",Toast.LENGTH_SHORT).show();
              /*  Message message2 = mHandler.obtainMessage(3);
                message2.sendToTarget();*/

                //Toast.makeText(cMain, "TE CAISTE", Toast.LENGTH_LONG).show();
                PostArray array = MyFunctions.crearPost("incidencia");
                array.setValue("cod_incidencia", MyFunctions.INC_CAIDA);
                AsynInsert asyn = new AsynInsert(array, MyFunctions.URL_INSERT_LOCATION);
                asyn.execute();
            }
        }

}
/**
 *
 *
 *
 *  public static boolean getRotationMatrix(float[] R, float[] I,
 float[] gravity, float[] geomagnetic) {
 // TODO: move this to native code for efficiency

 PRIMERA PARTE DEL CALCULO


 //Pilla los datos de la gravedad pasados
 float Ax = gravity[0];
 float Ay = gravity[1];
 float Az = gravity[2];

 SACA EL NORMAL DEL VEXTOR DE GRAVEDAD |G|
 final float normsqA = (Ax*Ax + Ay*Ay + Az*Az);

 GRAVEDAD DE LA TIERRA MAS O MENOS
 final float g = 9.81f;

 G BER BI NORMALIZADO
 CON ESTE CALCULO SE TINE EN CUENTA SI LA GRAVEDAD DEL MOVIL ES UN DIED PORCIENTO MENOR AL VALOR
 POR ESO LA MULTIP`LICACION DEL 0.01
 final float freeFallGravitySquared = 0.01f * g * g;
 if (normsqA < freeFallGravitySquared) {
 // gravity less than 10% of normal value
 return false;
 }



 SEGUNDA PARTE DEL CALCULO




 final float Ex = geomagnetic[0];
 final float Ey = geomagnetic[1];
 final float Ez = geomagnetic[2];

 AQUI SE ESTA HACIENDO GRAVEDAD - GEOMAGNETISMO PARA SACAR EL PUNTO MEDIO

 AQUI EL EJEMPLO CLARO

 Para el ejemplo tengamos claro que x y z representan i j k

 Recordar que para esto hay que aplicar la regla magica de las matrices de 3x3


 (  i  j  k )
 ( Ex Ey Ez )  = i=> (Ey*Ax) - (Ay*Ez) j=> (Ez * Ax)- (Ex*Az) k=> (Ex*Ay) - (Ey*Ax)
 ( Ax Ay Az )

 float Hx = Ey*Az - Ez*Ay;
 float Hy = Ez*Ax - Ex*Az;
 float Hz = Ex*Ay - Ey*Ax;

 ESTO ES LA NORMAL DE UNA MATRIZ

 final float normH = (float)Math.sqrt(Hx*Hx + Hy*Hy + Hz*Hz);
 /**

 if (normH < 0.1f) {
 // device is close to free fall (or in space?), or close to
 // magnetic north pole. Typical values are  > 100.
 return false;
 }


 TERCERA PARTE
 (ESTA PARTE SE ME ESCAPA)

 SACAR LA PROYECCION DE AMBOS ANGULOS


 final float invH = 1.0f / normH;

 Hx *= invH;
 Hy *= invH;
 Hz *= invH;
 final float invA = 1.0f / (float)Math.sqrt(Ax*Ax + Ay*Ay + Az*Az);
 Ax *= invA;
 Ay *= invA;
 Az *= invA;



 final float Mx = Ay*Hz - Az*Hy;
 final float My = Az*Hx - Ax*Hz;
 final float Mz = Ax*Hy - Ay*Hx;
 if (R != null) {
 if (R.length == 9) {
 R[0] = Hx;     R[1] = Hy;     R[2] = Hz;
 R[3] = Mx;     R[4] = My;     R[5] = Mz;
 R[6] = Ax;     R[7] = Ay;     R[8] = Az;
 } else if (R.length == 16) {
 R[0]  = Hx;    R[1]  = Hy;    R[2]  = Hz;   R[3]  = 0;
 R[4]  = Mx;    R[5]  = My;    R[6]  = Mz;   R[7]  = 0;
 R[8]  = Ax;    R[9]  = Ay;    R[10] = Az;   R[11] = 0;
 R[12] = 0;     R[13] = 0;     R[14] = 0;    R[15] = 1;
 }
 }
 if (I != null) {
 // compute the inclination matrix by projecting the geomagnetic
 // vector onto the Z (gravity) and X (horizontal component
 // of geomagnetic vector) axes.
 final float invE = 1.0f / (float)Math.sqrt(Ex*Ex + Ey*Ey + Ez*Ez);
 final float c = (Ex*Mx + Ey*My + Ez*Mz) * invE;
 final float s = (Ex*Ax + Ey*Ay + Ez*Az) * invE;
 if (I.length == 9) {
 I[0] = 1;     I[1] = 0;     I[2] = 0;
 I[3] = 0;     I[4] = c;     I[5] = s;
 I[6] = 0;     I[7] =-s;     I[8] = c;
 } else if (I.length == 16) {
 I[0] = 1;     I[1] = 0;     I[2] = 0;
 I[4] = 0;     I[5] = c;     I[6] = s;
 I[8] = 0;     I[9] =-s;     I[10]= c;
 I[3] = I[7] = I[11] = I[12] = I[13] = I[14] = 0;
 I[15] = 1;
 }
 }
 return true;
 }

 *
 *
 *
 *
 *
 *
 * */
