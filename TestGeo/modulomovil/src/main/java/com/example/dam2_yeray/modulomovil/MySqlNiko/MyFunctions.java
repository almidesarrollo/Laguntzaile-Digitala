package com.example.dam2_yeray.modulomovil.MySqlNiko;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;


import com.example.dam2_yeray.modulomovil.MainWearActivity;
import com.example.dam2_yeray.modulomovil.PostPhp.PostArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class MyFunctions {

    public static final String URL_INSERT_LOCATION = "http://188.84.117.60:8080/asistencia/web-service/insert.php", KEY_ = "clave", PASSW_ = "Almi123";
    public static final String INC_CAIDA="1",INC_PULSO="2",INC_LOST="3",INC_AYUD="4",INC_PULSO_BAJO="5";
    public static JSONObject[] webServiceConnect(PostArray sendParams, String url) {

        try {

            WebServiceHelper aux = new WebServiceHelper();
            JSONArray json = aux.httpUrlConnect(sendParams, url);
            JSONObject[] array = new JSONObject[json.length()];
            for (int i=0;i<array.length;i++) {
                array[i] = json.getJSONObject(i);
            }

            return array;

        } catch (Exception e) {
            return null;
        }

    }

    /**gets numbers formatted*/

    public static String getNumberWithPoints(double num) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(num);

    }

    public static PostArray crearPost(String modo)
    {
        /**instanciamos el array*/
        PostArray psArray = new PostArray();

        psArray.setValue("modo", modo);


        //todo AQUI DEBEREMOS METER LA VARIABLE ID DEL PACIENTE USUARIO
        ///String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        psArray.setValue("USER_ID_", "171");

        /**esto sirve para no permitir las peticiones desde otro lado que no sea la aplicacion*/
        psArray.setValue(MyFunctions.KEY_, MyFunctions.PASSW_);
        return psArray;
    }


    public static void notificar(Context c,String titulo,String... arraystring)
    {
        int icon= android.R.mipmap.sym_def_app_icon;
        NotificationManager nManager= (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        String texto="";
        for (String a:arraystring
             ) {
            texto=texto+a;
        }
        long cuando=System.currentTimeMillis();
        Notification.Builder notification=new Notification.Builder(c).setSmallIcon(icon).setContentTitle(titulo).setContentText(texto);
        Intent intent=new Intent(c,MainWearActivity.class);

        TaskStackBuilder task=TaskStackBuilder.create(c);
        task.addParentStack(MainWearActivity.class);
        task.addNextIntent(intent);
        PendingIntent penIntent=task.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(penIntent);


        nManager.notify(0, notification.build());

    }

}
