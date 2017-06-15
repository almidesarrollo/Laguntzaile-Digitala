package com.asistencia.digital.monitor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asistencia.digital.monitor.R;
import com.asistencia.digital.monitor.geoutils.GeoContext;
import com.asistencia.digital.monitor.localdb.LocalDbManager;
import com.asistencia.digital.monitor.webservice.AsistRestApiClient;
import com.asistencia.digital.monitor.services.ServiceMain;


//public class MainGeo extends Activity implements SensorEventListener {
public class MainGeo extends Activity {

    public static final String TAG = "MANZANA";
    // public static TextView txt1,txt2,txt3,txt4;
    public static TextView txt5;
    private Button btnGeo, btnStop;
    private Button btnAyuda;
    public static TextView txtLocation;

    private AsistRestApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_geo);
        GeoContext.appContext = this;

        /* create the api rest client */
        apiClient = new AsistRestApiClient(this);

        /* create login on start */
        apiClient.doLogin("familiatest", "Almi123");

        btnAyuda = (Button)findViewById(R.id.btnAyuda);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        btnGeo = (Button) findViewById(R.id.btnSer);
        btnStop = (Button)findViewById(R.id.btnPararServicio);
        txt5 = (TextView) findViewById(R.id.txt5);

        btnGeo.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            stopService(new Intent(MainGeo.this, ServiceMain.class));
            startService(new Intent(MainGeo.this, ServiceMain.class));

        }
        });

        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiClient.createIssue(R.integer.const_issue_help);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!stopService(new Intent(MainGeo.this, ServiceMain.class)))
                {

                    Toast.makeText(getApplicationContext(), "EL SERVICIO ESTA APAGADO", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Apagando Servicio...", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_geo, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishActivityFromChild(Activity child, int requestCode) {
        super.finishActivityFromChild(child, requestCode);
        finish();
    }

   /* @Override
    public void onSensorChanged(SensorEvent event) {
        float[] data;
        switch( event.sensor.getType() ) {
            case Sensor.TYPE_GRAVITY:
                gData[0] = event.values[0];
                gData[1] = event.values[1];
                gData[2] = event.values[2];
                haveGrav = true;
                break;
            case Sensor.TYPE_ACCELEROMETER:
                if (haveGrav) break;    // don't need it, we have better
                gData[0] = event.values[0];
                gData[1] = event.values[1];
                gData[2] = event.values[2];
                haveAccel = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mData[0] = event.values[0];
                mData[1] = event.values[1];
                mData[2] = event.values[2];
                haveMag = true;
                break;
            default:
                return;
        }

        if ((haveGrav || haveAccel) && haveMag) {
            //LLENA AMBOS FLOAT Rmat e Imat con la rotacion

            //Rmat MATRIZ DE ROTACION
            //Imat MATRIZ DE INCLINACION
            SensorManager.getRotationMatrix(Rmat, Imat, gData, mData);
            SensorManager.remapCoordinateSystem(Imat,
                    SensorManager.AXIS_X, SensorManager.AXIS_MINUS_X, R2);
            // Orientation isn't as useful as a rotation matrix, but
            // we'll show it here anyway.
            SensorManager.getOrientation(R2, orientation);

            float incl = SensorManager.getInclination(Rmat);
            txt1.setText("mh: " +  (orientation[0]));
            txt2.setText("pitch: " + (orientation[1]));
            txt3.setText("roll: " + (orientation[2]));

           // txt5.setText("inclination: " + (incl));


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

}
